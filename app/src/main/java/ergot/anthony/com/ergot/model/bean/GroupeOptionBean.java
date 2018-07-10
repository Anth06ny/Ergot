package ergot.anthony.com.ergot.model.bean;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GroupeOptionBean {

    private long id;
    private String ref;
    private String name;

    @SerializedName("option")
    private ArrayList<OptionBean> options ;



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<OptionBean> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<OptionBean> options) {
        this.options = options;
    }
}
