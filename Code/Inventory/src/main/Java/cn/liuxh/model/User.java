package cn.liuxh.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by liuxianghong on 16/11/23.
 */
public class User {

    @JsonProperty("userName")
    private String name;

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    @JsonProperty("password")
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
}
