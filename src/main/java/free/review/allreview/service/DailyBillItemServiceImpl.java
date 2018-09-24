package free.review.allreview.service;

import free.review.allreview.entity.Customer;
import free.review.allreview.entity.DailyBillItem;
import free.review.allreview.entity.GoodsPrice;
import free.review.allreview.exceptions.DuplicatedException;
import free.review.allreview.exceptions.MissingInfoException;
import free.review.allreview.exceptions.NotFoundException;
import free.review.allreview.repository.CustomerRepository;
import free.review.allreview.repository.DailyBillItemRepository;
import free.review.allreview.repository.GoodsPriceRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
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
    public ResponseEntity<DailyBillItem> putUpdate(Long id, DailyBillItem dailyBillItem) {
        DailyBillItem existingContact = findIfExists(id);

        if (isMissingInfo(dailyBillItem)) {
            throw new MissingInfoException();
        }

        Optional<GoodsPrice> goods = goodsPriceRepository.findById(dailyBillItem.getGoodsPrice().getId());
        Optional<Customer> customer = customerRepository.findById(dailyBillItem.getCustomer().getId());
        if (goods.isPresent() && customer.isPresent()) {
            dailyBillItem.setGoodsPrice(goods.get());
            dailyBillItem.setCustomer(customer.get());
        } else {
            throw new MissingInfoException();
        }


        BeanUtils.copyProperties(dailyBillItem, existingContact);
        existingContact.setId(id);
        DailyBillItem updatedContact = dailyBillItemRepository.save(existingContact);

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
