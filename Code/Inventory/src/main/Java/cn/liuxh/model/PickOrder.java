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

    public String getPo() {
        return po;
    }

    public void setPo(String po) {
        this.po = po;
    }

    @JsonIgnore
    String po;



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

    @JsonIgnore
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
        for (PickSku sku2:
        skus) {
            if (sku2.seriesNo.equals(sku.seriesNo)){
                sku2.setCount(sku.getCount() + sku2.getCount());
                return;
            }
        }
        skus.add(sku);
    }
}
