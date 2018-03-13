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
    private long id;
    private long statutAnnulation;
    private UserBean user;

    public CommandeBean() {
        compositionCommande = new ArrayList<>();
        user = new UserBean();
    }

    /**
     * MEthode pour mettre à jour une commande à partir de celle reçu par le serveur
     *
     * @param commandeBean
     */
    public void update(CommandeBean commandeBean) {
        this.compositionCommande = commandeBean.getCompositionCommande();
        this.dateCommande = commandeBean.getDateCommande();
        this.datePrevision = commandeBean.getDatePrevision();
        this.statut = commandeBean.getStatut();
        this.statutAnnulation = commandeBean.getStatutAnnulation();
        this.user = commandeBean.getUser();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CommandeBean that = (CommandeBean) o;

        return id == that.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
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

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public long getStatutAnnulation() {
        return statutAnnulation;
    }

    public void setStatutAnnulation(long statutAnnulation) {
        this.statutAnnulation = statutAnnulation;
    }
}
