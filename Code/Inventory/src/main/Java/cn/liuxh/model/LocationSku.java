package cn.liuxh.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by liuxianghong on 16/11/28.
 */

//
//CREATE TABLE `Inventory`.`LocationSKU` (
//        `id` INT NOT NULL,
//        `SeriesNo` VARCHAR(45) NOT NULL,
//        `Calculate` INT NOT NULL DEFAULT 1,
//        `OrderId` INT NOT NULL DEFAULT 0,
//        PRIMARY KEY (`id`));

public class LocationSku {

    String seriesNo;
    int calculate;
    int orderId;

    int count;
    String name;
    String size;

    public String getLocationNo() {
        return locationNo;
    }

    public void setLocationNo(String locationNo) {
        this.locationNo = locationNo;
    }

    @JsonIgnore
    String locationNo;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
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

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
}

