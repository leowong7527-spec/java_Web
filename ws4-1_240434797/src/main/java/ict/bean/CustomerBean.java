package ict.bean;

public class CustomerBean {
  private String custid;
  private String name;
  private String tel;
  private int age;

  public String getCustid() {
    return custid;
  }

  public void setCustid(String custid) {
    this.custid = custid;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getTel() {
    return tel;
  }

  public void setTel(String tel) {
    this.tel = tel;
  }

  public int getAge() {
    return age;
  }

  public void setAge(int age) {
    this.age = age;
  }

  @Override
  public String toString() {
    return "CustomerBean{" +
            "custid=" + custid +
            ", name=" + name +
            ", tel=" + tel +
            ", age=" + age +
            '}';
  }

  public CustomerBean() {
  }

  public CustomerBean(String custid, String name, String tel, int age) {
    this.custid = custid;
    this.name = name;
    this.tel = tel;
    this.age = age;
  }
}
