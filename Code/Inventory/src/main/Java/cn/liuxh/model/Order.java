package cn.liuxh.model;

import java.sql.Timestamp;
/**
 * Created by liuxianghong on 16/11/24.
 */
public class Order {
    private int id;
    private String name;

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    private String format;
    private String code;
    private int number;
    private String site;
    private Timestamp time;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    private int state = 0;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }


}


