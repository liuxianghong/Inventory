package cn.liuxh.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by liuxianghong on 2016/12/9.
 */
public class PickSku {
    @JsonIgnore
    int id;
    @JsonIgnore
    String shortName;

    @JsonIgnore
    String location;
    String productName;
    String size;

    String locationNo;
    String seriesNo;
    int count;

    @JsonIgnore
    int pickOrderId;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getLockUserId() {
        return lockUserId;
    }

    public void setLockUserId(int lockUserId) {
        this.lockUserId = lockUserId;
    }

    @JsonIgnore
    int state;
    @JsonIgnore
    int lockUserId;

    public String getPo() {
        return po;
    }

    public void setPo(String po) {
        this.po = po;
    }

    @JsonIgnore
    String po;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String name;

    public String getGoodSize() {
        return goodSize;
    }

    public void setGoodSize(String goodSize) {
        this.goodSize = goodSize;
    }

    @JsonIgnore
    String goodSize;

    public String getRemarks1() {
        return remarks1;
    }

    public void setRemarks1(String remarks1) {
        this.remarks1 = remarks1;
    }

    public String getRemarks2() {
        return remarks2;
    }

    public void setRemarks2(String remarks2) {
        this.remarks2 = remarks2;
    }

    String remarks1;
    String remarks2;

    public PickSku()
    {

    }

    public  PickSku(SortSku sortSku){
        this.shortName = sortSku.orderName;
        this.size = sortSku.size;
        this.productName = sortSku.productName;
        this.location = sortSku.location;
        this.locationNo = sortSku.locationNo;
        this.seriesNo = sortSku.seriesNo;
        this.count = sortSku.count;
    }

    public static PickSku NameCopy(PickSku sortSku) {
        PickSku sku = new PickSku();
        sku.shortName = sortSku.shortName;
        sku.size = sortSku.size;
        sku.productName = sortSku.productName;
        sku.location = sortSku.location;
        sku.locationNo = sortSku.locationNo;
        sku.seriesNo = sortSku.seriesNo;
        sku.count = sortSku.count;
        return sku;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
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

    public String getLocationNo() {
        return locationNo;
    }

    public void setLocationNo(String locationNo) {
        this.locationNo = locationNo;
    }

    public String getSeriesNo() {
        return seriesNo;
    }

    public void setSeriesNo(String seriesNo) {
        this.seriesNo = seriesNo;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getPickOrderId() {
        return pickOrderId;
    }

    public void setPickOrderId(int pickOrderId) {
        this.pickOrderId = pickOrderId;
    }

}
