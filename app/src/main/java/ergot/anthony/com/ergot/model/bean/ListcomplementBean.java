package ergot.anthony.com.ergot.model.bean;

import java.io.Serializable;

public class ListcomplementBean implements Serializable {

    private static final long serialVersionUID = 6397651389009756303L;
    private long id;
    private ComplementBean complement;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ComplementBean getComplement() {
        return complement;
    }

    public void setComplement(ComplementBean complement) {
        this.complement = complement;
    }

}
