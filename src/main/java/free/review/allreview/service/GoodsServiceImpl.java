package free.review.allreview.service;

import free.review.allreview.BusinessConfig.GoodsConfig;
import free.review.allreview.entity.Goods;
import free.review.allreview.exceptions.ConstraintException;
import free.review.allreview.exceptions.DuplicatedException;
import free.review.allreview.exceptions.MissingInfoException;
import free.review.allreview.exceptions.NotAllowChangeException;
import free.review.allreview.repository.GoodsRepository;
import free.review.allreview.utils.MyBeanUtil;
import free.review.allreview.utils.MyRepositoryUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;

@Service
public class GoodsServiceImpl implements GoodsService {

    private GoodsRepository goodsRepository;

    @Autowired
    public GoodsServiceImpl(GoodsRepository goodsRepository) {
        Assert.notNull(goodsRepository, "goodsRepository must not be null!");
        this.goodsRepository = goodsRepository;
    }

    @Override
    public ResponseEntity<Page<Goods>> getAllResponse(Pageable pageable) {
        Page<Goods> goods = goodsRepository.findAll(pageable);

        return new ResponseEntity<>(goods, HttpStatus.OK);

    }

    @Override
    public ResponseEntity<Goods> getOneResponse(Long id) {
        Goods getContact = findIfExists(id);
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
    public ResponseEntity<Goods> patchUpdate(Long id, Goods goods) {
        List<String> notAllowChange = MyBeanUtil.filterNotAllowChange(goods, GoodsConfig.NOT_ALLOW_CHANGE);
        if (null != notAllowChange && !notAllowChange.isEmpty()) {
            throw new NotAllowChangeException();
        }
        Goods existingGoods = findIfExists(id);

        MyBeanUtil.copyNonNullProperties(goods, existingGoods);
        Goods updatedContact = goodsRepository.save(existingGoods);

        return new ResponseEntity<>(updatedContact, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Goods> delete(Long id) {

        Goods existingContact = findIfExists(id);
        try {
            goodsRepository.delete(existingContact);
        } catch (Exception e) {
            throw new ConstraintException();
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    private Goods findIfExists(Long id) {
        return MyRepositoryUtil.findIfExists(id, goodsRepository);
    }

    private boolean isMissingInfo(Goods goods) {
        return null == goods || null == goods.getName() || goods.getName().length() == 0;

    }

    private boolean isExisted(Goods goods) {
        Collection<Goods> goodsCollection = goodsRepository.findByName(goods.getName());
        return null != goodsCollection && !goodsCollection.isEmpty();
    }
}
