package entity.db;

public class Product {
    private int id;
    private String name;
    private String description;
    private double price;
    private int amount;
    private int groupId;
    private String producer;


    public Product(String name, String description, String producer , double price, int amount, int groupId, int id)
    {
        this.amount = amount;
        this.name = name;
        this.description = description;
        this.groupId = groupId;
        if(price < 0 || amount < 0)
            throw new RuntimeException("incorrect value");
        this.price = price;
        this.producer = producer;
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
            return this.id == (((Product) obj).id);
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

    public double getPrice() {
        return price;
    }


    public void setPrice(double price) {
        this.price = price;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

}
