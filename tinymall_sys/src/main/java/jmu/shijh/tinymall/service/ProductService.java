package jmu.shijh.tinymall.service;

import jmu.shijh.tinymall.common.util.PageDTO;
import jmu.shijh.tinymall.common.util.PageVO;
import jmu.shijh.tinymall.domain.dto.ProductDTO;
import jmu.shijh.tinymall.domain.entity.Product;
import com.baomidou.mybatisplus.extension.service.IService;
import jmu.shijh.tinymall.domain.vo.ProductPreVO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *
 */
public interface ProductService extends IService<Product> {

    @Transactional
    void updateProduct(Product product);

    boolean reduceStockBatch(List<Long> ids, int size);

    @Transactional
    void addProduct(Product product);

    PageVO pagingQueryProductPre(ProductDTO dto, PageDTO page);

    List<ProductPreVO> getHotProduct(int size);

    @Transactional
    void removeProducts(List<Long> productIds);
}
