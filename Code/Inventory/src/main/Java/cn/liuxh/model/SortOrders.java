package cn.liuxh.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuxianghong on 2016/11/30.
 */
public class SortOrders {

    int id;
    String orderName;
    int state;
    String address;
    private Timestamp time = new Timestamp(System.currentTimeMillis());
    List<SortSku> sku;

    @JsonIgnore
    String po;
    @JsonIgnore
    String type;
    @JsonIgnore
    String inTime;

    public String getPo() {
        return po;
    }

    public void setPo(String po) {
        this.po = po;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getInTime() {
        return inTime;
    }

    public void setInTime(String inTime) {
        this.inTime = inTime;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public List<SortSku> getSku() {
        return sku;
    }

    public void setSku(List<SortSku> sku) {
        this.sku = sku;
    }

    public  String toString() {
        String str =  " orderName: " + orderName +
                " id: " + id;
        if (sku != null) {
            for (int i = 0; i < sku.size(); i++) {
                str += " id: " + sku.get(i).getId();
                str += " seriesNo: " + sku.get(i).getSeriesNo();
                str += " getCalculate: " + sku.get(i).getCalculate();
            }
        }

        return str;
    }

    public void addSku(SortSku s) {
        if (this.sku == null)
        {
            this.sku = new ArrayList<SortSku>();
        }
        this.sku.add(s);
    }
}
