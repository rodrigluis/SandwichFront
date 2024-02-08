package be.abis.sandwich.model;

public class Sandwich {
    private int id;
    private String name;
    private String description;
    private String category;
    private double basePrice;
    private boolean salad;
    private boolean breadType;

    public Sandwich(){}

    public Sandwich(int id, String name, String description, String category, boolean salad, boolean breadType) {
        this.id          = id;
        this.name        = name;
        this.description = description;
        this.category    = category;
        this.salad       = salad;
        this.breadType   = breadType;
    }

    public Sandwich(int id, String name, String description, String category, double basePrice, boolean salad, boolean breadType) {
        this(id,name, description, category, salad, breadType);
        this.basePrice = basePrice;
    }

    public String toString() {
        return this.name + " - " + this.category + " - " + this.description + " - " + this.basePrice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    public boolean isSaladAllowed() {
        return this.salad;
    }

    public boolean isBreadTypeAllowed() {
        return this.breadType;
    }
}
