package cn.liuxh.model;

import java.sql.Timestamp;

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

    int id;
    String seriesNo;
    int calculate;
    int orderId;

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

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
}

