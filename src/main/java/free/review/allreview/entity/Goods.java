package free.review.allreview.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Goods implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "INT(11)")
    private Long id;
    @Column(unique = true)
    private String name;

    public Goods() {

    }

    public Goods(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
