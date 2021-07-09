package jmu.shijh.tinymall.controller;


import jmu.shijh.tinymall.common.annotation.MultiRequestBody;
import jmu.shijh.tinymall.common.annotation.ParamCheck;
import jmu.shijh.tinymall.common.annotation.ParamChecks;
import jmu.shijh.tinymall.common.annotation.UpdateField;
import jmu.shijh.tinymall.common.exception.CustomException;
import jmu.shijh.tinymall.common.util.*;
import jmu.shijh.tinymall.domain.dto.ProductDTO;
import jmu.shijh.tinymall.domain.dto.RedisCache;
import jmu.shijh.tinymall.domain.entity.Product;
import jmu.shijh.tinymall.domain.entity.Shop;
import jmu.shijh.tinymall.domain.enums.ProductStatus;
import jmu.shijh.tinymall.mq.sender.RedisCacheMsgSender;
import jmu.shijh.tinymall.service.ProductService;
import jmu.shijh.tinymall.service.ShopService;
import jmu.shijh.tinymall.shiro.UserIdentity;
import org.apache.commons.io.IOUtils;
import org.apache.ibatis.javassist.expr.Cast;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.*;
import java.time.Duration;
import java.util.Iterator;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author shijh
 * @since 2021-05-21
 */
@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private UserIdentity user;
    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;
    @Autowired
    private RedisCacheMsgSender redisCacheMsgSender;

    @PostMapping("/query/page")
    public JsonResp getProducts(@MultiRequestBody(required = false) ProductDTO dto,
                                @MultiRequestBody(required = false) PageDTO page) {
        if (!user.getInitSuccess() || "normal".equals(user.getRole().getRoleName())) {
            dto.setProductStatus(ProductStatus.NORMAL.code());
        }
        PageVO pageVO = productService.pagingQueryProductPre(dto, page);
        return R.ok().data(pageVO).build();
    }

    @PostMapping("/query/page/shop")
    @RequiresPermissions("shopinfo:select")
    public JsonResp getProductsShop(@MultiRequestBody(required = false) ProductDTO dto,
                                @MultiRequestBody(required = false) PageDTO page) {
        Shop userShop = shopService.getUserShop();
        dto.setShopId(userShop.getShopId());
        PageVO pageVO = productService.pagingQueryProductPre(dto, page);
        return R.ok().data(pageVO).build();
    }

    @RequestMapping("/query/{pid}")
    public  JsonResp getProductById(@PathVariable Long pid) {
        Product product = productService.getById(pid);
        if (product == null) {
            throw new CustomException("不存在该商品");
        }
        return R.ok().data(product).build();
    }


    @PostMapping("/update")
    @ParamChecks({
            @ParamCheck(include = {"productId"}),
            @ParamCheck(include = {"productTags"}, required = false, lengthLE = 5, split2Array = true),
            @ParamCheck(include = {"productTypes"}, required = false, lengthLE = 10, split2Array = true),
            @ParamCheck(include = {"productPic"}, required = false, lengthLE = 5, split2Array = true),
    })
    @RequiresPermissions("product:update")
    public JsonResp updateProduct(@RequestBody(required = false) Product product) {
        Product prod = productService.getById(product.getProductId());
        if (prod == null || !prod.getShopId().equals(shopService.getUserShop().getShopId())) {
            throw new CustomException("商品更新失败");
        }
        product.setProductStatus(null);
        productService.updateProduct(product);
        return R.ok().build();
    }

    @GetMapping("/status/{status}/{pid}")
    @RequiresPermissions("product:update")
    public JsonResp deployProduct(@PathVariable Long pid, @PathVariable String status) {
        Product product = productService.getById(pid);
        Shop shop = shopService.getUserShop();
        if (product == null || !product.getShopId().equals(shop.getShopId())) {
            throw new CustomException("不存在该商品");
        }
        if("deploy".equals(status) && !ProductStatus.NORMAL.equal(product.getProductStatus())) {
            if (product.getStock() <= 0) {
                throw new CustomException("当前商品库存为零");
            }
            product.setProductStatus(ProductStatus.NORMAL.code());
            product.setDeployTime(Times.localNow());
        } else if ("undeploy".equals(status) && ProductStatus.NORMAL.equal(product.getProductStatus())) {
            product.setProductStatus(ProductStatus.NO_DEPLOY.code());
            product.setDeployTime(null);
        } else {
            throw new CustomException("操作失败");
        }

        productService.updateProduct(product);
        return R.ok().msg("操作成功").build();
    }

    @GetMapping("/query/hot")
    public JsonResp getHot() {
        Object hotList = redisTemplate.opsForValue().get(RedisKeys.getProductHotKey());
        if (hotList == null) {
            hotList = productService.getHotProduct(20);
            redisCacheMsgSender.send(
                    new RedisCache().setKey(RedisKeys.getProductHotKey())
                            .setValue(hotList)
                            .setDuration(Duration.ofDays(1))
            );
        }
        return R.ok().data(hotList).build();
    }

    @GetMapping("/query/recommend")
    public JsonResp getRecommend() {
        return getHot();
    }

    @GetMapping("/delete/{pid}")
    @RequiresPermissions("product:delete")
    public JsonResp deleteProduct(@PathVariable Long pid) {
        productService.removeProducts(Cl.list(pid));
        return R.ok().msg("删除成功").build();
    }

    @PostMapping("/add")
    @RequiresPermissions("product:insert")
    @ParamChecks({
        @ParamCheck(include = {"productName","productDesc","stock","originalPrice","salePrice"}),
        @ParamCheck(include = {"productTags"}, required = false, lengthLE = 5, split2Array = true, value = "标签不可多于5个"),
        @ParamCheck(include = {"productTypes"}, lengthLE = 10, split2Array = true, value = "款式不可多于10个"),
        @ParamCheck(include = {"productPic"}, lengthLE = 5, split2Array = true, value = "图片不可多于5个"),
    })
    public JsonResp addProduct(@RequestBody(required = false) Product product) {
        if (product.getSalePrice().compareTo(product.getOriginalPrice()) > 0) {
            throw new CustomException("销售价不能大于原价");
        }
        productService.addProduct(product);
        return R.ok().build();
    }
}
