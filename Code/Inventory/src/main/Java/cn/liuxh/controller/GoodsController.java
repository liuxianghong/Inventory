package cn.liuxh.controller;

import cn.liuxh.model.Goods;
import cn.liuxh.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liuxianghong on 2016/11/28.
 */

@Controller
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @RequestMapping("/getAllGoodsE")
    @ResponseBody
    public Map getAllOrdersE(int page, int rows) throws Exception{
        Map map = new HashMap();
        int start = (page-1)*rows;
        List students = goodsService.getAllE(start,rows);
        map.put("rows",students);
        map.put("total", goodsService.count());
        return map;
    }

    @RequestMapping("/getAllGoods")
    @ResponseBody
    public Map getAllOrders() throws Exception{
        Map map = new HashMap();
        List students = goodsService.getAll();
        map.put("rows",students);
        return map;
    }

    @RequestMapping(value = "/saveGoods", method = RequestMethod.POST)
    @ResponseBody
    public int saveProduct(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {

            String userId = request.getParameter("id");
            String name =  request.getParameter("name");
            String Size = request.getParameter("size");
            String SeriesNo = request.getParameter("seriesNo");
            String Count = request.getParameter("count");
            String LocationNo = request.getParameter("locationNo");


            Goods user = new Goods();
            user.setName(name);
            user.setSeriesNo(SeriesNo);
            user.setSize(Size);
            user.setCount(Integer.parseInt(Count));
            user.setLocationNo(LocationNo);
            int ret = 0;
            if (!userId.isEmpty()) {
                //修改用户信息
                user.setId(Integer.parseInt(userId));
                ret = goodsService.update(user);
                //ret = userService.updateUser(user);
            } else {
                //ret = userService.addUser(user);
                ret = goodsService.add(user);
            }
            System.out.println("saveProduct:"+" ret: "+ret + " id: "+user.getId());
            if (ret != 0){
                return 1;
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return 0;
    }

    @RequestMapping(value = "/delGoods", method = RequestMethod.POST)
    @ResponseBody
    public int delProduct(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            String id = request.getParameter("id");


            int ret = 0;
            if (!id.isEmpty()) {
                ret = goodsService.delete(Integer.parseInt(id));
            }
            if (ret != 0){
                return 1;
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return 0;
    }


    @RequestMapping("/getSkuDetails")
    @ResponseBody
    public Goods getSkuDetails(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String seriesNo = request.getParameter("seriesNo");

        return goodsService.getGoodsBySeriesNo(seriesNo);
    }

    @RequestMapping("/updateSkuLocation")
    @ResponseBody
    public Goods updateSkuLocation(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String seriesNo = request.getParameter("seriesNo");

        String locationNo = request.getParameter("locationNo");

        Goods user = new Goods();
        user.setSeriesNo(seriesNo);
        user.setLocationNo(locationNo);

        goodsService.updateSkuLocation(user);


        return goodsService.getGoodsBySeriesNo(seriesNo);
    }
}
