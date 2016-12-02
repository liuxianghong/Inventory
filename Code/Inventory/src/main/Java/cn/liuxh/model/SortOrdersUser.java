package cn.liuxh.model;

/**
 * Created by liuxianghong on 2016/12/2.
 */
public class SortOrdersUser {
    int uid;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public SortOrders getOrder() {
        return order;
    }

    public void setOrder(SortOrders order) {
        this.order = order;
    }

    SortOrders order;
}
