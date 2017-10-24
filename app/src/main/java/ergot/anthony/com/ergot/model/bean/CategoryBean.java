package ergot.anthony.com.ergot.model.bean;

import java.util.ArrayList;

/**
 * Created by Anthony on 23/10/2017.
 */

public class CategoryBean {


    private String name;
    private String url;
    private ArrayList<ProductBean> productBeenList;

    public CategoryBean() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ArrayList<ProductBean> getProductBeenList() {
        return productBeenList;
    }

    public void setProductBeenList(ArrayList<ProductBean> productBeenList) {
        this.productBeenList = productBeenList;
    }
}
