package ergot.anthony.com.ergot.model.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Anthony on 23/10/2017.
 */

public class CategoryBean implements Serializable {

    private static final long serialVersionUID = -683916466855464433L;

    private Long id;
    private String nom;
    private ArrayList<ProductBean> produitsVisible;
    private String url_image;

    public CategoryBean() {
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


    public ArrayList<ProductBean> getProduitsVisible() {
        return produitsVisible;
    }

    public void setProduitsVisible(ArrayList<ProductBean> produitsVisible) {
        this.produitsVisible = produitsVisible;
    }

    public String getUrl_image() {
        return url_image;
    }

    public void setUrl_image(String url_image) {
        this.url_image = url_image;
    }
}
