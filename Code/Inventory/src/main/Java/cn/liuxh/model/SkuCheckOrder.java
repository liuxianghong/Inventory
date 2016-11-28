package cn.liuxh.model;

import java.sql.Timestamp;

/**
 * Created by liuxianghong on 2016/11/28.
 */
public class SkuCheckOrder {

    int id;
    String seriesNo;
    String orderName;
    int calculate;
    private Timestamp time = new Timestamp(System.currentTimeMillis());

    int count;
    String locationNo;
    String name;
    String size;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSeriesNo() {
        return seriesNo;
    }

    public void setSeriesNo(String seriesNo) {
        this.seriesNo = seriesNo;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public int getCalculate() {
        return calculate;
    }

    public void setCalculate(int calculate) {
        this.calculate = calculate;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getLocationNo() {
        return locationNo;
    }

    public void setLocationNo(String locationNo) {
        this.locationNo = locationNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}

