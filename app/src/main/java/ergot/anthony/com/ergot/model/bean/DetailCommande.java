package ergot.anthony.com.ergot.model.bean;
// Generated 10 oct. 2018 13:33:12 by Hibernate Tools 5.1.0.Alpha1

public class DetailCommande implements java.io.Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Long id;
    private String nomProduit, description, listComplement;
    private int prix;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomProduit() {
        return nomProduit;
    }

    public void setNomProduit(String nomProduit) {
        this.nomProduit = nomProduit;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getListComplement() {
        return listComplement;
    }

    public void setListComplement(String listComplement) {
        this.listComplement = listComplement;
    }

    public int getPrix() {
        return prix;
    }

    public void setPrix(int prix) {
        this.prix = prix;
    }

}
