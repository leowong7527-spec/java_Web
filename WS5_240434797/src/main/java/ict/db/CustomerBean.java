package ict.db;

public class CustomerBean {
    private String custId;
    private String name;
    private String tel;
    private int age;

    public CustomerBean() {}

    public String getCustId() { return custId; }
    public void setCustId(String custId) { this.custId = custId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getTel() { return tel; }
    public void setTel(String tel) { this.tel = tel; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
}
