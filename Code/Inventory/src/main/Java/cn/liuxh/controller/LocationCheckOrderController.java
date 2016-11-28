package cn.liuxh.controller;

import cn.liuxh.model.LocationCheckOrder;
import cn.liuxh.model.LocationSku;
import cn.liuxh.model.SkuCheckOrder;
import cn.liuxh.service.LocationCheckOrderService;
import cn.liuxh.service.SkuCheckOrderService;
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
//            List<LocationSku> sku = locationCheckOrderService.getAllSku(order.getId());
//            order.setSku(sku);
        }
        map.put("orders",students);
        return map;
    }

    @RequestMapping("/createLocationCheckOrder")
    @ResponseBody
    public LocationCheckOrder addOrder(@RequestBody LocationCheckOrder order,HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {

            System.out.println("createLocationCheckOrder: "+request.getParameterValues("sku"));
            System.out.println("createLocationCheckOrder: "+order.toString());
            Timestamp time = new Timestamp(System.currentTimeMillis());
            order.setTime(time);
            int ret = 0;
            ret = locationCheckOrderService.add(order);
            if (ret != 0){
                return order;
                //return locationCheckOrderService.getDetailById(sku.getId());
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return new LocationCheckOrder();
    }

    @RequestMapping("/updateLocationCheckOrder")
    @ResponseBody
    public LocationCheckOrder updateOrder(@RequestBody LocationCheckOrder order) throws Exception {
        try {
            int ret = 0;
//            ret = locationCheckOrderService.update(order);
//            if (ret != 0){
//                return order;
//                //return locationCheckOrderService.getDetailById(sku.getId());
//            }

        } catch (Exception e) {
            // TODO: handle exception
        }
        return new LocationCheckOrder();
    }
}




