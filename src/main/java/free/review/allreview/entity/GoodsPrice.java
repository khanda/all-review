package free.review.allreview.entity;

import free.review.allreview.listener.GoodsPriceListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@EntityListeners(GoodsPriceListener.class)
public class GoodsPrice implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column
    private Float price;
    @Column
    @Temporal(TemporalType.DATE)
    private Date date;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "goods_id")
    private Goods goods;

    @Transient
    private Long transientGoodsId;

    public GoodsPrice() {
    }


    public GoodsPrice(Float price, Date date, Goods goods, Long transientGoodsId) {
        this.price = price;
        this.date = date;
        this.goods = goods;
        this.transientGoodsId = transientGoodsId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    public Long getTransientGoodsId() {
        return transientGoodsId;
    }

    public void setTransientGoodsId(Long transientGoodsId) {
        this.transientGoodsId = transientGoodsId;
    }
}
