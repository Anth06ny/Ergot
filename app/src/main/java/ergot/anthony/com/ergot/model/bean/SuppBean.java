package ergot.anthony.com.ergot.model.bean;

import java.io.Serializable;

/**
 * Created by Anthony on 23/10/2017.
 */

public class SuppBean implements Serializable {

    //TODO A supprimer

    //Choix
    private long id;
    private ProductBean produit;
    private long newPrice;



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ProductBean getProduit() {
        return produit;
    }

    public void setProduit(ProductBean produit) {
        this.produit = produit;
    }

    public long getNewPrice() {
        return newPrice;
    }

    public void setNewPrice(long newPrice) {
        this.newPrice = newPrice;
    }
}
