package ergot.anthony.com.ergot.model.bean;

import java.io.Serializable;

/**
 * Created by Anthony on 23/10/2017.
 */

public class ProductBean implements Serializable, Cloneable {

    //Choix
    private String name;
    private String description;
    private long price;
    private long idSuppBean; //S'il y a un supplement à proposer
    private SuppBean suppBean;

    public ProductBean() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public long getIdSuppBean() {
        return idSuppBean;
    }

    public void setIdSuppBean(long idSuppBean) {
        this.idSuppBean = idSuppBean;
    }

    public SuppBean getSuppBean() {
        return suppBean;
    }

    public void setSuppBean(SuppBean suppBean) {
        this.suppBean = suppBean;
    }
}
