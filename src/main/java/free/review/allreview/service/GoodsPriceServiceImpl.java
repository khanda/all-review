package free.review.allreview.service;

import free.review.allreview.entity.Goods;
import free.review.allreview.entity.GoodsPrice;
import free.review.allreview.exceptions.DuplicatedException;
import free.review.allreview.exceptions.MissingInfoException;
import free.review.allreview.exceptions.NotFoundException;
import free.review.allreview.repository.GoodsPriceRepository;
import free.review.allreview.repository.GoodsRepository;
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
public class GoodsPriceServiceImpl implements GoodsPriceService {

    private GoodsPriceRepository goodsPriceRepository;
    private GoodsRepository goodsRepository;

    @Autowired
    public GoodsPriceServiceImpl(GoodsPriceRepository goodsPriceRepository, GoodsRepository goodsRepository) {
        Assert.notNull(goodsPriceRepository, "goodsPriceRepository must not be null!");
        Assert.notNull(goodsRepository, "goodsRepository must not be null!");
        this.goodsPriceRepository = goodsPriceRepository;
        this.goodsRepository = goodsRepository;
    }

    @Override
    public ResponseEntity<Iterable<GoodsPrice>> getAllResponse() {
        Iterable<GoodsPrice> modelList = goodsPriceRepository.findAll();

        return new ResponseEntity<>(modelList, HttpStatus.OK);

    }

    @Override
    public ResponseEntity<GoodsPrice> getOneResponse(Long id) {
        GoodsPrice getContact = findIfExists(id);
        return new ResponseEntity<>(getContact, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<GoodsPrice> createNew(GoodsPrice goodsPrice, HttpServletRequest request) {
        if (isMissingInfo(goodsPrice)) {
            throw new MissingInfoException();
        }
        if (isExisted(goodsPrice)) {
            throw new DuplicatedException();
        }
        Optional<Goods> goods = goodsRepository.findById(goodsPrice.getTransientGoodsId());
        goods.ifPresent(goodsPrice::setGoods);

        GoodsPrice newContact = goodsPriceRepository.save(goodsPrice);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Location", newContact.getId().toString());

        return new ResponseEntity<>(newContact, responseHeaders, HttpStatus.CREATED);
    }


    @Override
    public ResponseEntity<GoodsPrice> putUpdate(Long id, GoodsPrice goodsPrice) {
        GoodsPrice existingContact = findIfExists(id);

        if (isMissingInfo(goodsPrice)) {
            throw new MissingInfoException();
        }

        Optional<Goods> goods = goodsRepository.findById(goodsPrice.getTransientGoodsId());
        if (goods.isPresent()) {
            goodsPrice.setGoods(goods.get());
        } else {
            throw new MissingInfoException();
        }


        BeanUtils.copyProperties(goodsPrice, existingContact);
        existingContact.setId(id);
        GoodsPrice updatedContact = goodsPriceRepository.save(existingContact);

        return new ResponseEntity<>(updatedContact, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<GoodsPrice> delete(Long id) {

        GoodsPrice existingContact = findIfExists(id);
        goodsPriceRepository.delete(existingContact);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    private GoodsPrice findIfExists(Long id) {
        Optional<GoodsPrice> existingContact = goodsPriceRepository.findById(id);

        if (existingContact.isPresent()) {
            return existingContact.get();
        } else {
            throw new NotFoundException();
        }
    }

    private boolean isMissingInfo(GoodsPrice goodsPrice) {
        return null == goodsPrice ||
                null == goodsPrice.getPrice() ||
                null == goodsPrice.getDate() ||
                null == goodsPrice.getTransientGoodsId();
    }

    private boolean isExisted(GoodsPrice goodsPrice) {
        Collection<GoodsPrice> goodsCollection = goodsPriceRepository
                .findByGoodsIdAndDate(
                        goodsPrice.getTransientGoodsId(),
                        goodsPrice.getDate());

        return null != goodsCollection && !goodsCollection.isEmpty();
//
//        return false;
    }
}
