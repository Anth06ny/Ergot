package ergot.anthony.com.ergot.model.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Anthony on 23/10/2017.
 */

public class CategoryBean implements Serializable {

    private long id;
    private String name;
    private String url_image;
    private ArrayList<ProductBean> products;

    public CategoryBean() {
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

    public String getUrl_image() {
        return url_image;
    }

    public void setUrl_image(String url_image) {
        this.url_image = url_image;
    }

    public ArrayList<ProductBean> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<ProductBean> products) {
        this.products = products;
    }
}
