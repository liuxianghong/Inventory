package cn.liuxh.mapper;

import cn.liuxh.model.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by liuxianghong on 16/11/24.
 */
public interface ProductMapper {
    Product getProduct();
    List getAllProducts(@Param(value="start")int start, @Param(value="rows")int rows);
    int selectCount();

    int updateProduct(Product product);
    int addProduct(Product product);
    int delProduct(@Param(value="id")int id);
}
