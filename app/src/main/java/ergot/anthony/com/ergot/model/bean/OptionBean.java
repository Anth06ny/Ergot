package ergot.anthony.com.ergot.model.bean;

public class OptionBean {

    private long id;
    private String name;
    private String description;
    private long price;
    private boolean rupture;

    private ProductBean product;



    public ProductBean getProduct() {
        return product;
    }

    public void setProduct(ProductBean product) {
        this.product = product;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public boolean isRupture() {
        return rupture;
    }

    public void setRupture(boolean rupture) {
        this.rupture = rupture;
    }
}
