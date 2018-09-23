package free.review.allreview.service;

import free.review.allreview.entity.Goods;
import free.review.allreview.exceptions.DuplicatedException;
import free.review.allreview.exceptions.MissingInfoException;
import free.review.allreview.exceptions.NotFoundException;
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
public class GoodsServiceImpl implements GoodsService {

    private GoodsRepository goodsRepository;

    @Autowired
    public GoodsServiceImpl(GoodsRepository goodsRepository) {
        Assert.notNull(goodsRepository, "goodsRepository must not be null!");
        this.goodsRepository = goodsRepository;
    }

    @Override
    public ResponseEntity<Iterable<Goods>> getAllResponse() {
        Iterable<Goods> allContacts = goodsRepository.findAll();

        return new ResponseEntity<>(allContacts, HttpStatus.OK);

    }

    @Override
    public ResponseEntity<Goods> getOneResponse(Long id) {
        Goods getContact = findContactIfExists(id);
        return new ResponseEntity<>(getContact, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Goods> createNew(Goods goods, HttpServletRequest request) {
        if (isMissingInfo(goods)) {
            throw new MissingInfoException();
        }
        if (isExisted(goods)) {
            throw new DuplicatedException();
        }


        Goods newContact = goodsRepository.save(goods);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Location", newContact.getId().toString());

        return new ResponseEntity<>(newContact, responseHeaders, HttpStatus.CREATED);
    }


    @Override
    public ResponseEntity<Goods> putUpdate(Long id, Goods customer) {
        Goods existingContact = findContactIfExists(id);

        if (isMissingInfo(customer)) {
            throw new MissingInfoException();
        }

        BeanUtils.copyProperties(customer, existingContact);
        existingContact.setId(id);
        Goods updatedContact = goodsRepository.save(existingContact);

        return new ResponseEntity<>(updatedContact, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Goods> delete(Long id) {

        Goods existingContact = findContactIfExists(id);
        goodsRepository.delete(existingContact);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    private Goods findContactIfExists(Long id) {
        Optional<Goods> existingContact = goodsRepository.findById(id);

        if (existingContact.isPresent()) {
            return existingContact.get();
        } else {
            throw new NotFoundException();
        }
    }

    private boolean isMissingInfo(Goods goods) {
        return null == goods || null == goods.getName() || goods.getName().length() == 0;

    }

    private boolean isExisted(Goods goods) {
        Collection<Goods> goodsCollection = goodsRepository.findByName(goods.getName());
        return null != goodsCollection && !goodsCollection.isEmpty();
    }
}
