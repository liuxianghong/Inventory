package cn.liuxh.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by liuxianghong on 2016/11/30.
 */
public class SortSku {

    int id;
    String seriesNo;
    int calculate;

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    @JsonIgnore
    String orderName;

    int count;
    String productName;
    String size;
    String location;

    @JsonIgnore
    int shipped;
    @JsonIgnore
    int unShipped;
    @JsonIgnore
    String goodNo;

    public int getShipped() {
        return shipped;
    }

    public void setShipped(int shipped) {
        this.shipped = shipped;
    }

    public int getUnShipped() {
        return unShipped;
    }

    public void setUnShipped(int unShipped) {
        this.unShipped = unShipped;
    }

    public String getGoodNo() {
        return goodNo;
    }

    public void setGoodNo(String goodNo) {
        this.goodNo = goodNo;
    }



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

    public int getCalculate() {
        return calculate;
    }

    public void setCalculate(int calculate) {
        this.calculate = calculate;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}
