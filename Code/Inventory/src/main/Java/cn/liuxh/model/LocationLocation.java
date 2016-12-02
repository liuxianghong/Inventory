package cn.liuxh.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuxianghong on 16/12/1.
 */


public class LocationLocation {
    public List<LocationSku> getSku() {
        return sku;
    }

    public void setSku(List<LocationSku> sku) {
        this.sku = sku;
    }

    public String getLocationNo() {
        return locationNo;
    }

    public void setLocationNo(String locationNo) {
        this.locationNo = locationNo;
    }

    List<LocationSku> sku = new ArrayList<>();;
    String locationNo;

    public void addSku(LocationSku sk) {
        if (sku == null) {
            sku = new ArrayList<LocationSku>();
        }
        sku.add(sk);
    }
}
