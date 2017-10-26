package ergot.anthony.com.ergot.model.bean;

import android.util.Pair;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Anthony on 25/10/2017.
 */

public class CommandeBean {

    //Liste de produit ainsi que son ArrayList de supplement
    private ArrayList<Pair<ProductBean, ArrayList<ProductBean>>> productList;
    private Date dateCommande;
    private String remarque;

    public CommandeBean() {

        productList = new ArrayList<>();
    }

    public void setProductList(ArrayList<Pair<ProductBean, ArrayList<ProductBean>>> productList) {
        this.productList = productList;
    }

    public Date getDateCommande() {
        return dateCommande;
    }

    public void setDateCommande(Date dateCommande) {
        this.dateCommande = dateCommande;
    }

    public ArrayList<Pair<ProductBean, ArrayList<ProductBean>>> getProductList() {
        return productList;
    }

    public String getRemarque() {
        return remarque;
    }

    public void setRemarque(String remarque) {
        this.remarque = remarque;
    }
}
