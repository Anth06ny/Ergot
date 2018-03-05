package ergot.anthony.com.ergot.model.bean;

import java.io.Serializable;

/**
 * Created by Anthony on 05/03/2018.
 */

public class UserBean implements Serializable {

    private String telephone;
    private String token;
    private String email;

    private int nbCmdOk; //Nombre de commande en succes       (Statut non cancel)
    private int nbCmdCancel; //Nombre de commande en cancel Client
    private int nbCmdReadyCancel; //Nombre de commande en cancel Client commande prete
    private int nbCmdNverCome;   //Nombre de commande en client jamais venu

    public int getNbCmdOk() {
        return nbCmdOk;
    }

    public void setNbCmdOk(int nbCmdOk) {
        this.nbCmdOk = nbCmdOk;
    }

    public int getNbCmdCancel() {
        return nbCmdCancel;
    }

    public void setNbCmdCancel(int nbCmdCancel) {
        this.nbCmdCancel = nbCmdCancel;
    }

    public int getNbCmdReadyCancel() {
        return nbCmdReadyCancel;
    }

    public void setNbCmdReadyCancel(int nbCmdReadyCancel) {
        this.nbCmdReadyCancel = nbCmdReadyCancel;
    }

    public int getNbCmdNverCome() {
        return nbCmdNverCome;
    }

    public void setNbCmdNverCome(int nbCmdNverCome) {
        this.nbCmdNverCome = nbCmdNverCome;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
