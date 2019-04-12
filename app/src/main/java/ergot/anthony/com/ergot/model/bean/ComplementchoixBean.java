package ergot.anthony.com.ergot.model.bean;

import java.io.Serializable;

public class ComplementchoixBean implements Serializable {

    private static final long serialVersionUID = -8287800215109727421L;
    private Long id;
    private ProductBean produit;
    private boolean supprimer;
    private int suppPrix;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
