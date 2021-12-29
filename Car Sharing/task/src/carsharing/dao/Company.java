package carsharing.dao;

public class Company {
    private int id;
    private String name;

    public Company() {}

    public Company(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return id + ". " + name;
    }

}
