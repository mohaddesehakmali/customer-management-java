package model;

public class Customer {
    private int id;
    private String name;
    private String nationalId;
    private String email;

    public Customer(int id, String name, String nationalId, String email) {
        this.id = id;
        this.name = name;
        this.nationalId = nationalId;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getNationalId() {
        return nationalId;
    }

    public String getEmail() {
        return email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", nationalId='" + nationalId + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}