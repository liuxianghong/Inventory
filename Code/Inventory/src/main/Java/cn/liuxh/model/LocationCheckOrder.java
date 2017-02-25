package cn.liuxh.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuxianghong on 16/11/28.
 */

public class LocationCheckOrder {
    int id;
    String orderName;

    @JsonIgnore
    String locationNo;
    private Timestamp time = new Timestamp(System.currentTimeMillis());

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    @JsonIgnore
    int groupId;

    public List<LocationLocation> getLocations() {
        return locations;
    }

    public void setLocations(List<LocationLocation> locations) {
        this.locations = locations;
    }

    List<LocationLocation> locations = new ArrayList<>();

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
        if (locations != null) {
            for (int i = 0; i < locations.size(); i++) {
                str += " LocationNo: " + locations.get(i).getLocationNo();
            }
        }

        return str;
    }

    public void addSku(LocationSku sku) {
        if (locations == null) {
            locations = new ArrayList<LocationLocation>();
        }
        for (LocationLocation location:
        locations) {
            System.out.println("addSku: "+location.getLocationNo() + " " + sku.getLocationNo());
            if (location.getLocationNo().equalsIgnoreCase(sku.getLocationNo())){
                location.addSku(sku);
                System.out.println("addSku: " + "didaddSku");
                return;
            }
        }

        LocationLocation location = new LocationLocation();
        location.setLocationNo(sku.getLocationNo());
        location.addSku(sku);
        System.out.println("addSku: " + "newLocation: " + location.getLocationNo());
        locations.add(location);
    }
}
