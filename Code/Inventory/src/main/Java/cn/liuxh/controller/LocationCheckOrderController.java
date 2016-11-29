package cn.liuxh.controller;

import cn.liuxh.model.LocationCheckOrder;
import cn.liuxh.model.LocationSku;
import cn.liuxh.service.LocationCheckOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liuxianghong on 16/11/28.
 */

@Controller
public class LocationCheckOrderController {

    @Autowired
    private LocationCheckOrderService locationCheckOrderService;

    @RequestMapping("/getLocationCheckOrders")
    @ResponseBody
    public Map getAllOrders() throws Exception{
        Map map = new HashMap();
        List<LocationCheckOrder> students = locationCheckOrderService.getAll();
        for (int i = 0; i < students.size(); i++) {
            LocationCheckOrder order = students.get(i);
            List<LocationSku> sku = locationCheckOrderService.getAllSku(order.getId());
            order.setSku(sku);
            System.out.println("getLocationCheckOrders: "+order.toString());
        }
        map.put("orders",students);
        return map;
    }

    @RequestMapping("/createLocationCheckOrder")
    @ResponseBody
    public LocationCheckOrder addOrder(@RequestBody LocationCheckOrder order,HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {

            Timestamp time = new Timestamp(System.currentTimeMillis());
            order.setTime(time);
            int ret = 0;
            ret = locationCheckOrderService.add(order);

            List<LocationSku> skus = order.getSku();
            for (LocationSku sku:skus) {
                sku.setOrderId(order.getId());
            }
            locationCheckOrderService.addSku(skus);

            if (ret != 0){
                return getOrderDetail(order.getId());
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }

    public LocationCheckOrder getOrderDetail(int id) {
        LocationCheckOrder order = locationCheckOrderService.getDetailById(id);
        List skus = locationCheckOrderService.getAllSku(id);
        order.setSku(skus);

        System.out.println("getOrderDetail: "+order.toString());

        return order;
    }


    @RequestMapping("/getLocationDetails")
    @ResponseBody
    public LocationCheckOrder getSkuDetails(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String LocationNo = request.getParameter("locationNo");
        LocationCheckOrder order = locationCheckOrderService.getDetailByLocationNo(LocationNo);
        List skus = locationCheckOrderService.getAllSku(order.getId());
        order.setSku(skus);
        return order;
    }

    @RequestMapping("/updateLocationCheckOrder")
    @ResponseBody
    public LocationCheckOrder updateOrder(@RequestBody LocationCheckOrder order) throws Exception {
        try {
            int ret = 0;
            locationCheckOrderService.deleteSku(order.getId());
            ret = locationCheckOrderService.update(order);
            List<LocationSku> skus = order.getSku();
            for (LocationSku sku:skus) {
                sku.setOrderId(order.getId());
            }
            locationCheckOrderService.addSku(skus);
            if (ret != 0){
                return getOrderDetail(order.getId());
            }

        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }

    @RequestMapping(value = "/deleteLocationCheckOrder")
    @ResponseBody
    public Map delProduct(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            String id = request.getParameter("id");

            int ret = 0;
            int ret2 = 0;
            if (!id.isEmpty()) {
                ret = locationCheckOrderService.delete(Integer.parseInt(id));
                ret2 = locationCheckOrderService.deleteSku(Integer.parseInt(id));
            }
            return getAllOrders();
        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }
}




