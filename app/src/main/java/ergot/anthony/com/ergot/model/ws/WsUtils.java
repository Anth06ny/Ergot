package ergot.anthony.com.ergot.model.ws;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Arrays;

import ergot.anthony.com.ergot.model.bean.CategoryBean;
import ergot.anthony.com.ergot.model.bean.ProductBean;
import ergot.anthony.com.ergot.model.bean.ResultBean;
import ergot.anthony.com.ergot.model.bean.SuppBean;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Anthony on 04/05/2017.
 */

public class WsUtils {

    private static final String KEY = "2a1b07b2a523f81188fe34e348206a57ffa6f2a7";
    private static final String CONTRAT = "Toulouse";
    private static final String URL = "https://api.jcdecaux.com/vls/v1/stations?contract=" + CONTRAT + "&apiKey=" + KEY;

    private static final Gson gson = new Gson();

    public static ArrayList<CategoryBean> getStationsDuServeur() throws Exception {

        Log.w("TAG_URL", URL);
        OkHttpClient client = new OkHttpClient();

        //Création de la requete
        Request request = new Request.Builder().url(URL).build();

        //Execution de la requête
        Response response = client.newCall(request).execute();

        //Analyse du code retour
        if (response.code() != HttpURLConnection.HTTP_OK) {
            throw new Exception("Reponse du serveur incorrect : " + response.code());
        }
        else {
            //Résultat de la requete.
            InputStreamReader inputStreamReader = new InputStreamReader(response.body().byteStream());

            //JSON -> Java (Parser une ArrayList typée)
            ArrayList<CategoryBean> listStation = gson.fromJson(inputStreamReader,
                    new TypeToken<ArrayList<CategoryBean>>() {
                    }.getType());

            return listStation;
        }
    }

    public static ArrayList<CategoryBean> getCategories() {
        ResultBean resultBean = printJSon();

        //ON Parcourt tous les produits et on leur applique le supplement qu'il convient
        for (CategoryBean categoryBean : resultBean.getCategories()) {
            for (ProductBean productBean : categoryBean.getProductBeenList()) {
                productBean.setSuppBean(getSuppBean(productBean.getIdSuppBean(), resultBean.getSupplements()));
            }
        }

        return resultBean.getCategories();
    }

    private static SuppBean getSuppBean(long id, ArrayList<SuppBean> suppBeen) {
        for (SuppBean s : suppBeen) {
            if (s.getId() == id) {
                return s;
            }
        }
        return null;
    }

    public static ResultBean printJSon() {
        ResultBean resultBean = new ResultBean();

        //Complements
        ProductBean frites = new ProductBean();
        frites.setName("Frites");
        frites.setDescription("");
        frites.setPrice(395);

        final ProductBean aucun = new ProductBean();
        aucun.setName("Aucun");
        aucun.setDescription("");
        aucun.setPrice(0);

        ProductBean pdt = new ProductBean();
        pdt.setName("Pomme de terre maison");
        pdt.setDescription("");
        pdt.setPrice(395);

        //Supplement  1
        SuppBean suppBean1 = new SuppBean();
        suppBean1.setId(1);
        suppBean1.setSupplement(new ArrayList<>(Arrays.asList(new ProductBean[]{aucun, frites, pdt})));
        suppBean1.setMinOne(true);

        //Supplement  2
        frites = new ProductBean();
        frites.setName("Frites");
        frites.setDescription("");
        frites.setPrice(0);

        pdt = new ProductBean();
        pdt.setName("Pomme de terre maison");
        pdt.setDescription("");
        pdt.setPrice(0);

        SuppBean suppBean2 = new SuppBean();
        suppBean2.setId(2);
        suppBean2.setSupplement(new ArrayList<>(Arrays.asList(new ProductBean[]{frites, pdt})));
        suppBean2.setMinOne(true);

        resultBean.setSupplements(new ArrayList<>(Arrays.asList(new SuppBean[]{suppBean1, suppBean2})));

        //--------------
        //Produit  Burger
        ProductBean special = new ProductBean();
        special.setName("LE SPÉCIAL");
        special.setDescription("Pain burger, sauce, tartare de légumes, salade fraîche, poulet rôti du Sud-ouest");
        special.setPrice(495);
        special.setIdSuppBean(1);

        ProductBean chedar = new ProductBean();
        chedar.setName("LE CHEDDAR");
        chedar.setDescription("Pain burger, sauce, tartare de légumes, salade fraîche, poulet rôti du Sud ouest, cheddar");
        chedar.setPrice(550);
        special.setIdSuppBean(1);

        ProductBean chevre = new ProductBean();
        chevre.setName("LE CHÈVRE-MIEL");
        chevre.setDescription("Pain burger, sauce blanche, tartare de légumes, salade fraîche, poulet rôti du Sud ouest, chèvre, miel");
        chevre.setPrice(595);
        special.setIdSuppBean(1);

        CategoryBean burger = new CategoryBean();
        burger.setName("Burger");
        burger.setUrl("burger.jpg");
        burger.setProductBeenList(new ArrayList<>(Arrays.asList(new ProductBean[]{special, chedar, chevre})));

        //--------------
        //Produit  Poulet
        ProductBean quart = new ProductBean();
        quart.setName("1/4 de poulet Roti");
        quart.setDescription("+ garniture au choix");
        quart.setPrice(695);
        quart.setIdSuppBean(2);

        ProductBean quartFermier = new ProductBean();
        quartFermier.setName("1/4 de poulet Roti fermier");
        quartFermier.setDescription("+ garniture au choix");
        quartFermier.setPrice(895);
        quartFermier.setIdSuppBean(2);

        ProductBean quartFermierBio = new ProductBean();
        quartFermierBio.setName("1/4 de poulet Roti fermier bio");
        quartFermierBio.setDescription("+ garniture au choix");
        quartFermierBio.setPrice(1195);
        quartFermierBio.setIdSuppBean(2);

        ProductBean quartRaclette = new ProductBean();
        quartRaclette.setName("1/4 de poulet Roti standard façon raclette");
        quartRaclette.setDescription("(bacon ou jambon cru, fromage à raclette et pommes de terre maison)");
        quartRaclette.setPrice(1095);

        CategoryBean poulet = new CategoryBean();
        poulet.setName("Poulet Rôti");
        poulet.setUrl("poulet.jpg");
        poulet.setProductBeenList(new ArrayList<>(Arrays.asList(new ProductBean[]{quart, quartFermier, quartFermierBio, quartRaclette})));

        //--------------
        //Produit  Accompagnement
        CategoryBean accompagnement = new CategoryBean();

        frites = new ProductBean();
        frites.setName("Frites");
        frites.setDescription("");
        frites.setPrice(395);

        pdt = new ProductBean();
        pdt.setName("Pomme de terre maison");
        pdt.setDescription("");
        pdt.setPrice(395);

        accompagnement.setName("Accompagnements");
        accompagnement.setUrl("accompagnements.jpg");
        accompagnement.setProductBeenList(new ArrayList<>(Arrays.asList(new ProductBean[]{frites, pdt})));

        resultBean.setCategories(new ArrayList<>(Arrays.asList(new CategoryBean[]{burger, poulet, accompagnement})));

        Log.w("JSON", new Gson().toJson(resultBean));

        return resultBean;
    }
}
