package ergot.anthony.com.ergot.model.bean;

import java.io.Serializable;
import java.util.List;

public class ComplementBean implements Serializable {

    private static final long serialVersionUID = 228705454883023022L;
    private Long id;
    private String nomComplement;
    private String description;
    private String question;
    private boolean supprimer;

    private List<ComplementchoixBean> complementchoixes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomComplement() {
        return nomComplement;
    }

    public void setNomComplement(String nomComplement) {
        this.nomComplement = nomComplement;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }


    public boolean isSupprimer() {
        return supprimer;
    }

    public void setSupprimer(boolean supprimer) {
        this.supprimer = supprimer;
    }

    public List<ComplementchoixBean> getComplementchoixes() {
        return complementchoixes;
    }

    public void setComplementchoixes(List<ComplementchoixBean> complementchoixes) {
        this.complementchoixes = complementchoixes;
    }
}
