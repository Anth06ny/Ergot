package ergot.anthony.com.ergot.model.bean;

import java.io.Serializable;

public class ComplementchoixBean implements Serializable {

    private static final long serialVersionUID = -8287800215109727421L;
    private long id;
    private ProductBean produit;
    private boolean supprimer;
    private int suppPrix;

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

    public boolean isSupprimer() {
        return supprimer;
    }

    public void setSupprimer(boolean supprimer) {
        this.supprimer = supprimer;
    }

    public int getSuppPrix() {
        return suppPrix;
    }

    public void setSuppPrix(int suppPrix) {
        this.suppPrix = suppPrix;
    }
}
