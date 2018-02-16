package ergot.anthony.com.ergot.model.bean.sendbean;

import java.io.Serializable;

import ergot.anthony.com.ergot.model.bean.ProductBean;
import ergot.anthony.com.ergot.model.bean.SuppBean;

/**
 * Created by Utilisateur on 14/02/2018.
 */

public class SelectProductBean implements Serializable {

    private ProductBean product;
    private SuppBean supplement;

    public SelectProductBean() {
    }

    public SelectProductBean(ProductBean product, SuppBean supplement) {
        this.product = product;
        this.supplement = supplement;
    }

    public ProductBean getProduct() {
        return product;
    }

    public void setProduct(ProductBean product) {
        this.product = product;
    }

    public SuppBean getSupplement() {
        return supplement;
    }

    public void setSupplement(SuppBean supplement) {
        this.supplement = supplement;
    }
}
