package cn.liuxh.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuxianghong on 2016/12/9.
 */
public class PickOrder {

    int id;
    int state;
    String shortName;
    String location;

    @JsonIgnore
    int lockUserId;
    @JsonIgnore
    int pickState;

    List<PickSku> skus;

    public int getLockUserId() {
        return lockUserId;
    }

    public void setLockUserId(int lockUserId) {
        this.lockUserId = lockUserId;
    }

    public int getPickState() {
        return pickState;
    }

    public void setPickState(int pickState) {
        this.pickState = pickState;
    }



    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public List<PickSku> getSkus() {
        return skus;
    }

    public void setSkus(List<PickSku> skus) {
        this.skus = skus;
    }

    public int getCount(){
        if (skus == null) return 0;
        int count = 0;
        for (PickSku sku:
        skus) {
            count += sku.count;
        }
        return  count;
    }

    public void addSku(PickSku sku){
        if (skus == null) skus = new ArrayList<>();
        sku.setPickOrderId(id);
        skus.add(sku);
    }
}
