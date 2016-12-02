package cn.liuxh.model;

/**
 * Created by liuxianghong on 2016/12/2.
 */
public class LocationCheckOrderUser {
    int uid;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public LocationCheckOrder getOrder() {
        return order;
    }

    public void setOrder(LocationCheckOrder order) {
        this.order = order;
    }

    LocationCheckOrder order;
}
