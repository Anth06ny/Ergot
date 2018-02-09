package ergot.anthony.com.ergot.model.bean;

import android.util.Pair;

import java.util.ArrayList;

/**
 * Created by Anthony on 25/10/2017.
 */

public class CommandeBean {

    //Liste de produit ainsi que son ArrayList de supplement. En transient pour en pas l'envoyer
    private ArrayList<Pair<ProductBean, SuppBean>> productList;
    private long dateCommande;
    private long datePrevision;
    private int status;
    private String remarque;
    private String telephone;
    private String email;

    public CommandeBean() {
        productList = new ArrayList<>();
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDateCommande(long dateCommande) {
        this.dateCommande = dateCommande;
    }

    public long getDateCommande() {
        return dateCommande;
    }

    public String getRemarque() {
        return remarque;
    }

    public void setRemarque(String remarque) {
        this.remarque = remarque;
    }

    public ArrayList<Pair<ProductBean, SuppBean>> getProductList() {
        return productList;
    }

    public void setProductList(ArrayList<Pair<ProductBean, SuppBean>> productList) {
        this.productList = productList;
    }
}
