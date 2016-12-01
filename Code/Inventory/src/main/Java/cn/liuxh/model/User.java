package cn.liuxh.model;

/**
 * Created by liuxianghong on 16/11/23.
 */
public class User {
    private String name;
    private String pw;
    private int id;

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

    public String getPassword() {
        return pw;
    }

    public void setPassword(String password) {
        this.pw = password;
    }
}
