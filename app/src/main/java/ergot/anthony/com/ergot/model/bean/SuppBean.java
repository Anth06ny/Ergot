package ergot.anthony.com.ergot.model.bean;

import java.util.ArrayList;

/**
 * Created by Anthony on 23/10/2017.
 */

public class SuppBean {

    //Choix
    private long id;
    private ArrayList<ProductBean> supplement;
    private boolean multi; //
    private boolean minOne; //Au moins 1

    //Selection
    private ArrayList<ProductBean> selected; //Supplement choisi


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ArrayList<ProductBean> getSupplement() {
        return supplement;
    }

    public void setSupplement(ArrayList<ProductBean> supplement) {
        this.supplement = supplement;
    }

    public boolean isMulti() {
        return multi;
    }

    public void setMulti(boolean multi) {
        this.multi = multi;
    }

    public boolean isMinOne() {
        return minOne;
    }

    public void setMinOne(boolean minOne) {
        this.minOne = minOne;
    }

    public ArrayList<ProductBean> getSelected() {
        return selected;
    }

    public void setSelected(ArrayList<ProductBean> selected) {
        this.selected = selected;
    }
}
