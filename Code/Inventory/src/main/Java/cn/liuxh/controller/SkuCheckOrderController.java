package cn.liuxh.controller;

import cn.liuxh.model.SkuCheckOrder;
import cn.liuxh.service.SkuCheckOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liuxianghong on 2016/11/28.
 */

@Controller
public class SkuCheckOrderController {

    @Autowired
    private SkuCheckOrderService skuCheckOrderService;

    @RequestMapping("/getSkuCheckOrders")
    @ResponseBody
    public Map getAllOrders(@RequestParam(value="page", defaultValue="1") int page
            , @RequestParam(value="rows", defaultValue="100") int rows,@RequestParam(value="uid") int uid) throws Exception{
        Map map = new HashMap();
        int start = (page-1)*rows;
        if (!UserController.userController.haveUser(uid)) return null;
        List students = skuCheckOrderService.getAll(start,rows);
        map.put("total",skuCheckOrderService.count());
        map.put("orders",students);
        return map;
    }

    @RequestMapping(value = "/getSkuCheckOrderById")
    @ResponseBody
    public SkuCheckOrder getSkuCheckOrder(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            String id =  request.getParameter("id");
            String uid = request.getParameter("uid");
            if (!UserController.userController.haveUser(Integer.parseInt(uid))) return null;
            return skuCheckOrderService.getDetailById(Integer.parseInt(id));
        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }

    @RequestMapping(value = "/createSkuCheckOrder")
    @ResponseBody
    public SkuCheckOrder addProduct(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            String uid = request.getParameter("uid");
            if (!UserController.userController.haveUser(Integer.parseInt(uid))) return null;
            Timestamp time = new Timestamp(System.currentTimeMillis());
            String seriesNo =  request.getParameter("seriesNo");
            String orderName = request.getParameter("orderName");
            String calculate = request.getParameter("calculate");
            SkuCheckOrder sku = new SkuCheckOrder();
            sku.setOrderName(orderName);
            sku.setSeriesNo(seriesNo);
            sku.setCalculate(Integer.parseInt(calculate));
            sku.setTime(time);
            if (skuCheckOrderService.haveGoods(seriesNo)) {
                int ret = 0;
                ret = skuCheckOrderService.add(sku);
                if (ret != 0){
                    return skuCheckOrderService.getDetailById(sku.getId());
                }
            }

        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }

    @RequestMapping(value = "/updateSkuCheckOrder")
    @ResponseBody
    public SkuCheckOrder updateProduct(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            String uid = request.getParameter("uid");
            if (!UserController.userController.haveUser(Integer.parseInt(uid))) return null;
            Timestamp time = new Timestamp(System.currentTimeMillis());
            String orderName = request.getParameter("orderName");
            String calculate = request.getParameter("calculate");
            String id = request.getParameter("id");
            SkuCheckOrder sku = new SkuCheckOrder();
            sku.setOrderName(orderName);
            sku.setId(Integer.parseInt(id));
            sku.setCalculate(Integer.parseInt(calculate));
            sku.setTime(time);
            int ret = 0;
            ret = skuCheckOrderService.update(sku);
            if (ret != 0){
                return skuCheckOrderService.getDetailById(sku.getId());
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }

    @RequestMapping(value = "/deleteSkuCheckOrder")
    @ResponseBody
    public Map delProduct(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            String uid = request.getParameter("uid");
            if (!UserController.userController.haveUser(Integer.parseInt(uid))) return null;
            String id = request.getParameter("id");


            int ret = 0;
            if (!id.isEmpty()) {
                ret = skuCheckOrderService.delete(Integer.parseInt(id));
            }
            if (ret != 0){
                return getAllOrders(1,100,Integer.parseInt(uid));
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }

    @RequestMapping(value = "/getAllSkuOrdersE")
    @ResponseBody
    public Map getAllSkuOrdersE(@RequestParam(value="page", defaultValue="1") int page
            , @RequestParam(value="rows", defaultValue="100") int rows) throws Exception{
        Map map = new HashMap();
        int start = (page-1)*rows;
        List students = skuCheckOrderService.getAll(start,rows);
        map.put("rows",students);
        map.put("total", skuCheckOrderService.count());
        System.out.println("getAllSkuOrdersE: " + students.size());
        return map;
    }
}
