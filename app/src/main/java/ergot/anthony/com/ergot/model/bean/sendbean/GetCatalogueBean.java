package ergot.anthony.com.ergot.model.bean.sendbean;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import ergot.anthony.com.ergot.model.bean.CategoryBean;
import ergot.anthony.com.ergot.model.bean.FormuleBean;

public class GetCatalogueBean {

    @SerializedName("Categories")
    private ArrayList<CategoryBean> categories;

    @SerializedName("Formules")
    private ArrayList<FormuleBean> formules;

    public ArrayList<CategoryBean> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<CategoryBean> categories) {
        this.categories = categories;
    }

    public ArrayList<FormuleBean> getFormules() {
        return formules;
    }

    public void setFormules(ArrayList<FormuleBean> formules) {
        this.formules = formules;
    }
}
