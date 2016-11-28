package cn.liuxh.model;


import java.sql.Timestamp;
import java.util.List;

/**
 * Created by liuxianghong on 16/11/28.
 */

public class LocationCheckOrder {
    int id;
    String orderName;
    String locationNo;
    private Timestamp time = new Timestamp(System.currentTimeMillis());


    public List<LocationSku> getSku() {
        return sku;
    }

    public void setSku(List<LocationSku> sku) {
        this.sku = sku;
    }

    List<LocationSku> sku;


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

    public String getLocationNo() {
        return locationNo;
    }

    public void setLocationNo(String locationNo) {
        this.locationNo = locationNo;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }


    public  String toString() {
        String str =  " orderName: " + orderName
                +" locationNo: " + locationNo;
        if (sku != null) {
            for (int i = 0; i < sku.size(); i++) {
                str += " seriesNo: " + sku.get(i).getSeriesNo();
                str += " getCalculate: " + sku.get(i).getCalculate();
            }
        }

        return str;
    }
}
