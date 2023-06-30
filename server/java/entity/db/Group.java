package entity.db;

public class Group {
    private int id;
    private String name;
    private String description;

    public Group(int id, String name, String description)
    {
        this.description = description;
        this.id = id;
        this.name = name;
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

    @Override
    public boolean equals(Object obj) {
        return this.id == (((Group) obj).id);
    }
}
