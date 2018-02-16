package ergot.anthony.com.ergot.model.bean.sendbean;

/**
 * Created by Utilisateur on 16/02/2018.
 */

public class CancelCommandeBean {

    private long idCommande;
    private String userEmail;
    private String deviceToken;



    public CancelCommandeBean(long idCommande, String userEmail, String deviceToken) {
        this.idCommande = idCommande;
        this.userEmail = userEmail;
        this.deviceToken = deviceToken;
    }

    public long getIdCommande() {
        return idCommande;
    }

    public void setIdCommande(long idCommande) {
        this.idCommande = idCommande;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }
}
