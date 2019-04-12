package ergot.anthony.com.ergot.model.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Anthony on 23/10/2017.
 */

public class ProductBean implements Serializable, Cloneable {

    private static final long serialVersionUID = 8397614529181902717L;

    //Base
    private Long id;
    private String nom;
    private String description;
    private long prix;

    private boolean rupture;
    private ArrayList<ListcomplementBean> listcomplements;

    public ProductBean() {
    }

    public boolean isRupture() {
        return rupture;
    }

    public void setRupture(boolean rupture) {
        this.rupture = rupture;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getPrix() {
        return prix;
    }

    public void setPrix(long prix) {
        this.prix = prix;
    }


    public ArrayList<ListcomplementBean> getListcomplements() {
        return listcomplements;
    }

    public void setListcomplements(ArrayList<ListcomplementBean> listcomplements) {
        this.listcomplements = listcomplements;
    }
}
