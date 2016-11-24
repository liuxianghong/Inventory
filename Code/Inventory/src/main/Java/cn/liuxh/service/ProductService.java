package cn.liuxh.service;

import cn.liuxh.mapper.ProductMapper;
import cn.liuxh.model.Product;
import cn.liuxh.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by liuxianghong on 16/11/24.
 */
@Service
public class ProductService {
    @Autowired
    private ProductMapper productMapper;

    public Product getProduct(){
        Product product = productMapper.getProduct();
        return product;
    }
}
