package ergot.anthony.com.ergot.model.bean;

import java.io.Serializable;
import java.util.ArrayList;

import ergot.anthony.com.ergot.model.bean.sendbean.SelectProductBean;

/**
 * Created by Anthony on 25/10/2017.
 */

public class CommandeBean implements Serializable {

    private static final long serialVersionUID = 5046775210799311032L;

    private ArrayList<SelectionBean> selectionList;


    //OLD
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
        selectionList = new ArrayList<>();
        user = new UserBean();
    }

    /**
     * MEthode pour mettre à jour une commande à partir de celle reçu par le serveur
     *
     * @param commandeBean
     */
    public void update(CommandeBean commandeBean) {
        compositionCommande = commandeBean.getCompositionCommande();
        dateCommande = commandeBean.getDateCommande();
        datePrevision = commandeBean.getDatePrevision();
        statut = commandeBean.getStatut();
        statutAnnulation = commandeBean.getStatutAnnulation();
        user = commandeBean.getUser();
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

    public int getTotalPrice() {
        int totalPrice = 0;
        if (selectionList != null) {

            for (SelectionBean selectionBean : selectionList) {
                ProductBean productBean = selectionBean.getProductBean();
                totalPrice += productBean.getPrix();
                if (selectionBean.getComplementchoixBeans() != null) {
                    for (ComplementchoixBean c : selectionBean.getComplementchoixBeans()) {
                        totalPrice += c.getSuppPrix();
                    }
                }
            }
        }
        return totalPrice;
    }


/* ---------------------------------
    // GETTER /SETTER
    // -------------------------------- */

    public ArrayList<SelectionBean> getSelectionList() {
        return selectionList;
    }

    public void setSelectionList(ArrayList<SelectionBean> selectionList) {
        this.selectionList = selectionList;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public long getDateCommande() {
        return dateCommande;
    }

    public void setDateCommande(long dateCommande) {
        this.dateCommande = dateCommande;
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
