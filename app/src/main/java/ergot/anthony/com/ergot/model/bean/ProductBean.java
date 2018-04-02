package ergot.anthony.com.ergot.model.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Anthony on 23/10/2017.
 */

public class ProductBean implements Serializable, Cloneable {

    //Base
    private long id;
    private String name;
    private String description;
    private long price;
    private boolean is_supp;
    private boolean rupture;
    private ArrayList<SuppBean> supplements;

    public ProductBean() {
    }

    public boolean isRupture() {
        return rupture;
    }

    public void setRupture(boolean rupture) {
        this.rupture = rupture;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public boolean isIs_supp() {
        return is_supp;
    }

    public void setIs_supp(boolean is_supp) {
        this.is_supp = is_supp;
    }

    public ArrayList<SuppBean> getSupplements() {
        return supplements;
    }

    public void setSupplements(ArrayList<SuppBean> supplements) {
        this.supplements = supplements;
    }
}
