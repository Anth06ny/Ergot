package ergot.anthony.com.ergot.model.bean;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class FormuleBean {

    private long id;
    private String name;
    private long price;

    @SerializedName("gr_option")
    private ArrayList<GroupeOptionBean> gr_option;
}
