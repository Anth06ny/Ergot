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

    public int getPrix() {
        int price = 0;
        if (productBean != null) {
            price += productBean.getPrix();
        }

        if (complementchoixBeans != null) {
            for (ComplementchoixBean c : complementchoixBeans) {
                price += c.getSuppPrix();
            }
        }

        return price;
    }

    /* -------------------
    // Getter /Setter
    --------------------- */

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
