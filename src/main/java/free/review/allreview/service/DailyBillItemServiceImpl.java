package free.review.allreview.service;

import free.review.allreview.BusinessConfig.DailyBillItemConfig;
import free.review.allreview.entity.Customer;
import free.review.allreview.entity.DailyBillItem;
import free.review.allreview.entity.GoodsPrice;
import free.review.allreview.exceptions.DuplicatedException;
import free.review.allreview.exceptions.MissingInfoException;
import free.review.allreview.exceptions.NotAllowChangeException;
import free.review.allreview.exceptions.NotFoundException;
import free.review.allreview.repository.CustomerRepository;
import free.review.allreview.repository.DailyBillItemRepository;
import free.review.allreview.repository.GoodsPriceRepository;
import free.review.allreview.utils.MyBeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class DailyBillItemServiceImpl implements DailyBillItemService {

    private DailyBillItemRepository dailyBillItemRepository;
    private GoodsPriceRepository goodsPriceRepository;
    private CustomerRepository customerRepository;

    @Autowired
    public DailyBillItemServiceImpl(DailyBillItemRepository dailyBillItemRepository,
                                    GoodsPriceRepository goodsPriceRepository,
                                    CustomerRepository customerRepository) {

        Assert.notNull(dailyBillItemRepository, "dailyBillItemRepository must not be null!");
        Assert.notNull(goodsPriceRepository, "goodsPriceRepository must not be null!");
        Assert.notNull(goodsPriceRepository, "customerRepository must not be null!");

        this.dailyBillItemRepository = dailyBillItemRepository;
        this.goodsPriceRepository = goodsPriceRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public ResponseEntity<Iterable<DailyBillItem>> getAllResponse() {
        Iterable<DailyBillItem> modelList = dailyBillItemRepository.findAll();

        return new ResponseEntity<>(modelList, HttpStatus.OK);

    }

    @Override
    public ResponseEntity<DailyBillItem> getOneResponse(Long id) {
        DailyBillItem getContact = findIfExists(id);
        return new ResponseEntity<>(getContact, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<DailyBillItem> createNew(DailyBillItem dailyBillItem, HttpServletRequest request) {
        if (isMissingInfo(dailyBillItem)) {
            throw new MissingInfoException();
        }
        if (isExisted(dailyBillItem)) {
            throw new DuplicatedException();
        }

        Optional<GoodsPrice> goods = goodsPriceRepository.findById(dailyBillItem.getGoodsPriceId());
        goods.ifPresent(dailyBillItem::setGoodsPrice);
        Optional<Customer> customer = customerRepository.findById(dailyBillItem.getCustomerId());
        customer.ifPresent(dailyBillItem::setCustomer);

        try {
            DailyBillItem newContact = dailyBillItemRepository.save(dailyBillItem);
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("Location", newContact.getId().toString());

            return new ResponseEntity<>(newContact, responseHeaders, HttpStatus.CREATED);

        } catch (Exception e) {
            throw new MissingInfoException();
        }
    }


    @Override
    public ResponseEntity<DailyBillItem> patchUpdate(Long id, DailyBillItem dailyBillItem) {
        List<String> notAllowChange = MyBeanUtil.filterNotAllowChange(dailyBillItem, DailyBillItemConfig.NOT_ALLOW_CHANGE);
        if (null != notAllowChange && !notAllowChange.isEmpty()) {
            throw new NotAllowChangeException();
        }
        DailyBillItem existingGoods = findIfExists(id);

        MyBeanUtil.copyNonNullProperties(dailyBillItem, existingGoods);
        DailyBillItem updatedContact = dailyBillItemRepository.save(existingGoods);

        return new ResponseEntity<>(updatedContact, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<DailyBillItem> delete(Long id) {

        DailyBillItem existingContact = findIfExists(id);
        dailyBillItemRepository.delete(existingContact);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    private DailyBillItem findIfExists(Long id) {
        Optional<DailyBillItem> existingContact = dailyBillItemRepository.findById(id);

        if (existingContact.isPresent()) {
            return existingContact.get();
        } else {
            throw new NotFoundException();
        }
    }

    private boolean isMissingInfo(DailyBillItem dailyBillItem) {
        return null == dailyBillItem ||
                null == dailyBillItem.getCustomerId() ||
                null == dailyBillItem.getDate() ||
                null == dailyBillItem.getQuantity() ||
                null == dailyBillItem.getGoodsPriceId();
    }

    private boolean isExisted(DailyBillItem dailyBillItem) {
        Collection<DailyBillItem> goodsCollection = dailyBillItemRepository
                .findByGoodsAndCustomer(
                        dailyBillItem.getCustomerId(),
                        dailyBillItem.getGoodsPriceId());

        return null != goodsCollection && !goodsCollection.isEmpty();
    }
}
