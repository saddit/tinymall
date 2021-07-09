package jmu.shijh.tinymall.controller;

import jmu.shijh.tinymall.common.util.JsonResp;
import jmu.shijh.tinymall.common.util.R;
import jmu.shijh.tinymall.common.util.RedisKeys;
import jmu.shijh.tinymall.domain.dto.RedisCache;
import jmu.shijh.tinymall.domain.entity.Category;
import jmu.shijh.tinymall.domain.entity.Shop;
import jmu.shijh.tinymall.domain.vo.CategoryProduct;
import jmu.shijh.tinymall.domain.vo.CategoryPv;
import jmu.shijh.tinymall.mq.sender.RedisCacheMsgSender;
import jmu.shijh.tinymall.service.CategoryService;
import jmu.shijh.tinymall.service.ShopService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;
    @Autowired
    private RedisCacheMsgSender redisCacheMsgSender;

    @GetMapping("/all")
    @SuppressWarnings("unchecked")
    public JsonResp getAll() {
        List<Category> categories = (List<Category>  ) redisTemplate.opsForValue().get(RedisKeys.getCategoryAllKey());
        if (categories == null) {
            categories = categoryService.getAll();
            redisCacheMsgSender.send(
                    new RedisCache().setKey(RedisKeys.getCategoryAllKey())
                            .setValue(categories)
            );
        }
        return R.ok().data(categories).build();
    }

    @GetMapping("/shop/pv")
    @RequiresPermissions("shopinfo:select")
    public JsonResp getShopPv() {
        Shop userShop = shopService.getUserShop();
        List<CategoryPv> pvs = (List<CategoryPv>) redisTemplate.opsForValue().get(RedisKeys.getCategoryPvKey(userShop.getShopId()));
        if (pvs == null) {
            pvs = categoryService.getShopCategoryPv(userShop.getShopId());
            redisCacheMsgSender.send(
                    new RedisCache().setKey(RedisKeys.getCategoryPvKey(userShop.getShopId()))
                            .setValue(pvs)
                            .setDuration(Duration.ofDays(1))
            );
        }
        return R.ok().data(pvs).build();
    }

    @PostMapping("/add")
    @RequiresPermissions("category:insert")
    public JsonResp addCategory(@RequestBody Category category) {
        categoryService.addCategory(category);
        return R.ok().build();
    }

    @GetMapping("/product")
    public JsonResp getCateProduct() {
        List<CategoryProduct> cateProduct = categoryService.getCateProduct();
        return R.ok().data(cateProduct).build();
    }
}
