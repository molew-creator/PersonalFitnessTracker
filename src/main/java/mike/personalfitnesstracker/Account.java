package mike.personalfitnesstracker;

public class Account {
    private String username;
    private String password;
    private String realname;
    private String email;
    private int age;
    private double weight;
    private double height;

    public Account(String username, String password, String realname, String email, int age, double weight, double height) {
        this.username = username;
        this.password = password;
        this.realname = realname;
        this.email = email;
        this.age = age;
        this.weight = weight;
        this.height = height;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double calculateBMI(double weight, double height) {
        return (weight / (height * height));
    }

}
