package cn.liuxh.controller;

import cn.liuxh.model.*;
import cn.liuxh.service.LocationCheckOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.ArrayList;
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
    public Map getAllOrders(@RequestParam(value="page", defaultValue="1") int page
            , @RequestParam(value="rows", defaultValue="100") int rows,@RequestParam(value="uid") int uid)
            throws Exception{
        int start = (page-1)*rows;
        if (!UserController.userController.haveUser(uid)) return null;
        Map map = new HashMap();
        List<LocationCheckOrder> students = locationCheckOrderService.getAll(start, rows);
        for (int i = 0; i < students.size(); i++) {
            LocationCheckOrder order = students.get(i);
            List<LocationSku> skus = locationCheckOrderService.getAllSku(order.getId());
            for (LocationSku sku:
                 skus) {
                order.addSku(sku);
            }
            System.out.println("getLocationCheckOrders: "+order.toString());
        }
        map.put("total",locationCheckOrderService.count());
        map.put("orders",students);
        return map;
    }

    @RequestMapping("/createLocationCheckOrder")
    @ResponseBody
    public LocationCheckOrder addOrder(@RequestBody LocationCheckOrderUser orderUser,HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {

            if (!UserController.userController.haveUser(orderUser.getUid())) return null;
            LocationCheckOrder order = orderUser.getOrder();
            Timestamp time = new Timestamp(System.currentTimeMillis());
            order.setTime(time);
            int ret = 0;
            ret = locationCheckOrderService.add(order);

            List<LocationSku> skus = new ArrayList<LocationSku>();
            List<LocationLocation> locations = order.getLocations();
            for (LocationLocation location:locations) {
                for (LocationSku sku:
                location.getSku()) {
                    skus.add(sku);
                    sku.setOrderId(order.getId());
                }
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
        List<LocationSku> skus = locationCheckOrderService.getAllSku(id);
        for (LocationSku sku:
                skus) {
            order.addSku(sku);
        }

        System.out.println("getOrderDetail: "+order.toString());

        return order;
    }


    @RequestMapping("/getLocationDetails")
    @ResponseBody
    public Map getSkuDetails(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String LocationNo = request.getParameter("locationNo");
        String uid = request.getParameter("uid");
        if (!UserController.userController.haveUser(Integer.parseInt(uid))) return null;
        List<Goods> goodses = locationCheckOrderService.getDetailByLocationNo(LocationNo);

        Map map = new HashMap();
        map.put("locationNo",LocationNo);
        map.put("sku",goodses);
        return map;
    }

    @RequestMapping("/updateLocationCheckOrder")
    @ResponseBody
    public LocationCheckOrder updateOrder(@RequestBody LocationCheckOrderUser orderUser) throws Exception {
        try {
            int ret = 0;
            if (!UserController.userController.haveUser(orderUser.getUid())) return null;
            LocationCheckOrder order = orderUser.getOrder();
            locationCheckOrderService.deleteSku(order.getId());
            ret = locationCheckOrderService.update(order);
            List<LocationSku> skus = new ArrayList<>();
            for (LocationLocation locations:
                    order.getLocations()) {
                skus.addAll(locations.getSku());
            }

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

            String uid = request.getParameter("uid");
            if (!UserController.userController.haveUser(Integer.parseInt(uid))) return null;
            int ret = 0;
            int ret2 = 0;
            if (!id.isEmpty()) {
                ret = locationCheckOrderService.delete(Integer.parseInt(id));
                ret2 = locationCheckOrderService.deleteSku(Integer.parseInt(id));
            }
            return getAllOrders(1,100,Integer.parseInt(uid));
        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }
}




