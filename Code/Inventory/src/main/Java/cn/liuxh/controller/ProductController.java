package cn.liuxh.controller;

import cn.liuxh.model.Order;
import cn.liuxh.model.Product;
import cn.liuxh.service.ProductService;
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
 * Created by liuxianghong on 16/11/24.
 */
@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @RequestMapping("/getProduct")
    @ResponseBody
    public Product getProduct() {
        Product product = productService.getProduct();
        return product;
    }


    @RequestMapping("/getAllProducts")
    @ResponseBody
    public Map getAllOrders(int page, int rows) throws Exception{
        Map map = new HashMap();
        int start = (page-1)*rows;
        List students = productService.getAllProducts(start,rows);
        map.put("rows",students);
        map.put("total", productService.selectCount());
        System.out.println("getAllProducts:"+students.toString()+" page: "+page + " rows: "+ rows);
        return map;
    }

    @RequestMapping(value = "/saveProduct", method = RequestMethod.POST)
    @ResponseBody
    public int saveProduct(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {

            String userId = request.getParameter("id");
            String name =  request.getParameter("name");
            String format = request.getParameter("format");
            String code = request.getParameter("code");
            String number = request.getParameter("number");
            String site = request.getParameter("site");

            System.out.println("saveProduct:"+" userId: "+userId + " name: "+ name);

            Product user = new Product();
            user.setName(name);
            user.setFormat(format);
            user.setCode(code);
            user.setNumber(Integer.parseInt(number));
            user.setSite(site);
            int ret = 0;
            if (!userId.isEmpty()) {
                //修改用户信息
                user.setId(Integer.parseInt(userId));
                ret = productService.updateProduct(user);
                //ret = userService.updateUser(user);
            } else {
                //ret = userService.addUser(user);
                ret = productService.addProduct(user);
            }
            System.out.println("saveProduct:"+" ret: "+ret);
            if (ret != 0){
                return 1;
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return 0;
    }

    @RequestMapping(value = "/delProduct", method = RequestMethod.POST)
    @ResponseBody
    public int delProduct(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {

            String id = request.getParameter("id");



            System.out.println("delProduct:"+" ParameterMap: "+request.getParameterMap().toString());

            int ret = 0;
            if (!id.isEmpty()) {
                ret = productService.delProduct(Integer.parseInt(id));
            }
            System.out.println("delProduct:"+" ret: "+ret);
            if (ret != 0){
                return 1;
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return 0;
    }
}
