package ergot.anthony.com.ergot.model.bean;

import android.util.Pair;

import java.util.ArrayList;

/**
 * Created by Anthony on 25/10/2017.
 */

public class Commande {

    //Liste de produit ainsi que son ArrayList de supplement
    private ArrayList<Pair<ProductBean, ArrayList<ProductBean>>> productList;

    public Commande() {

        productList = new ArrayList<>();
    }

    public ArrayList<Pair<ProductBean, ArrayList<ProductBean>>> getProductList() {
        return productList;
    }
}
