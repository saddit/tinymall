package jmu.shijh.tinymall.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import jmu.shijh.tinymall.common.exception.CustomException;
import jmu.shijh.tinymall.common.util.PageDTO;
import jmu.shijh.tinymall.common.util.PageVO;
import jmu.shijh.tinymall.common.util.Str;
import jmu.shijh.tinymall.domain.dto.ProductDTO;
import jmu.shijh.tinymall.domain.entity.Product;
import jmu.shijh.tinymall.domain.entity.Shop;
import jmu.shijh.tinymall.domain.enums.ProductStatus;
import jmu.shijh.tinymall.domain.vo.ProductPreVO;
import jmu.shijh.tinymall.shiro.UserIdentity;
import jmu.shijh.tinymall.service.ProductService;
import jmu.shijh.tinymall.mapper.ProductMapper;
import jmu.shijh.tinymall.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *  1. 添加商品
 *  2. 删除商品
 *  3. 更改商品
 *  4. 查询商品（模糊查询、条件查询）
 *  5. 发布商品
 *  6. 下架商品
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    @Autowired
    private UserIdentity user;
    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ShopService shopService;

    @Override
    @Transactional
    public void updateProduct(Product product) {
        if (product.getStock() == 0) {
            product.setProductStatus(ProductStatus.INVALID.code());
        } else if (product.getStock() < 0){
            throw new CustomException("库存不能为负数");
        }
        boolean s = updateById(product);
        if (!s) {
            throw new CustomException("商品更新失败");
        }
    }

    @Override
    public boolean reduceStockBatch(List<Long> ids, int size) {
        return productMapper.reduceStockBatch(ids, size) == ids.size();
    }

    @Override
    @Transactional
    public void addProduct(Product product) {
        Shop shop = shopService.getUserShop();
        if (shop == null) {
            throw new CustomException("你还没有店铺");
        }
        product.setShopId(shop.getShopId());
        boolean s = save(product);
        if (!s) {
            throw new CustomException("添加失败");
        }
    }

    @Override
    public PageVO pagingQueryProductPre(ProductDTO dto, PageDTO page) {
        PageHelper.startPage(page);
        Page<ProductPreVO> product = productMapper.selectPageByDTO(dto);
        return new PageVO(product);
    }

    @Override
    public List<ProductPreVO> getHotProduct(int size) {
        PageHelper.startPage(1,size, "pv");
        Page<ProductPreVO> page = productMapper.selectPageByDTO(new ProductDTO().setProductStatus(ProductStatus.NORMAL.code()));
        return page.getResult();
    }

    @Override
    @Transactional
    public void removeProducts(List<Long> productIds) {
        Shop shop = shopService.getUserShop();
        List<Product> products = productMapper.selectBatchIds(productIds);
        for (Product product : products) {
            if (!product.getShopId().equals(shop.getShopId())) {
                throw new  CustomException("非法访问");
            }
            else if (ProductStatus.NORMAL.equal(product.getProductStatus())) {
                throw new CustomException("已发布产品不能删除");
            }
        }
        removeByIds(productIds);
    }
}




