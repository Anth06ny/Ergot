package ergot.anthony.com.ergot.model.bean;

import java.util.ArrayList;

/**
 * Created by Anthony on 24/10/2017.
 */

public class ResultBean {

    private ArrayList<CategoryBean> categories;
    private ArrayList<SuppBean> supplements;

    public ArrayList<CategoryBean> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<CategoryBean> categories) {
        this.categories = categories;
    }

    public ArrayList<SuppBean> getSupplements() {
        return supplements;
    }

    public void setSupplements(ArrayList<SuppBean> supplements) {
        this.supplements = supplements;
    }
}
