package entity.db;

public class User {
    private String password;
    private String login;
    private String role;
    private int id;
    private String name;
    public User(String password, String login, String role , int id, String name)
    {
        this.login = login;
        this.role = role;
        this.password = password;
        this.id = id;
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
