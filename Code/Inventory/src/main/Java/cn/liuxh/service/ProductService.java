package cn.liuxh.service;

import cn.liuxh.mapper.ProductMapper;
import cn.liuxh.model.Product;
import cn.liuxh.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List getAllProducts(int start, int rows){
        return productMapper.getAllProducts(start, rows);
    }

    public int selectCount(){
        return productMapper.selectCount();
    }

    public int updateProduct(Product product) {
        return  productMapper.updateProduct(product);
    }

    public int addProduct(Product product){
        return  productMapper.addProduct(product);
    }

    public int delProduct(int id){
        return  productMapper.delProduct(id);
    }
}
