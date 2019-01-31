package ergot.anthony.com.ergot.model.bean;

import java.util.ArrayList;

public class SelectionBean {

    private ProductBean productBean;
    private ArrayList<ComplementchoixBean> complementchoixBeans;

    public SelectionBean() {
    }

    public SelectionBean(ProductBean productBean, ArrayList<ComplementchoixBean> complementchoixBeans) {
        this.productBean = productBean;
        this.complementchoixBeans = complementchoixBeans;
    }

    public ProductBean getProductBean() {
        return productBean;
    }

    public void setProductBean(ProductBean productBean) {
        this.productBean = productBean;
    }

    public ArrayList<ComplementchoixBean> getComplementchoixBeans() {
        return complementchoixBeans;
    }

    public void setComplementchoixBeans(ArrayList<ComplementchoixBean> complementchoixBeans) {
        this.complementchoixBeans = complementchoixBeans;
    }
}
