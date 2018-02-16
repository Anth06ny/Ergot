package ergot.anthony.com.ergot.model.bean;

import java.io.Serializable;
import java.util.ArrayList;

import ergot.anthony.com.ergot.model.bean.sendbean.SelectProductBean;

/**
 * Created by Anthony on 25/10/2017.
 */

public class CommandeBean implements Serializable {

    //Liste de produit ainsi que son ArrayList de supplement. En transient pour en pas l'envoyer
    private ArrayList<SelectProductBean> compositionCommande;
    private long dateCommande;
    private long datePrevision;
    private int statut;
    private String remarque;
    private String telephone;
    private String email;
    private String deviceToken;
    private long id;

    public CommandeBean() {
        compositionCommande = new ArrayList<>();
    }



    /* ---------------------------------
    // GETTER /SETTER
    // -------------------------------- */

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getTotalPrice() {
        int totalPrice = 0;
        if (compositionCommande != null) {
            for (SelectProductBean selection : compositionCommande) {
                ProductBean productBean = selection.getProduct();
                totalPrice += productBean.getPrice();
                //on regarde s'il y a des suppléments dans les produits séléctionnés
                if (selection.getSupplement() != null) {
                    totalPrice += selection.getSupplement().getNewPrice();
                }
            }
        }
        return totalPrice;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public long getDatePrevision() {
        return datePrevision;
    }

    public void setDatePrevision(long datePrevision) {
        this.datePrevision = datePrevision;
    }

    public int getStatut() {
        return statut;
    }

    public void setStatut(int statut) {
        this.statut = statut;
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

    public ArrayList<SelectProductBean> getCompositionCommande() {
        return compositionCommande;
    }

    public void setCompositionCommande(ArrayList<SelectProductBean> compositionCommande) {
        this.compositionCommande = compositionCommande;
    }
}
