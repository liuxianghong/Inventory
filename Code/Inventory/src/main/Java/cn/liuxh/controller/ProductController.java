package cn.liuxh.controller;

import cn.liuxh.model.Product;
import cn.liuxh.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
}
