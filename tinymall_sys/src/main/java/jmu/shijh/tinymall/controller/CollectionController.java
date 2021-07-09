package jmu.shijh.tinymall.controller;


import jmu.shijh.tinymall.common.annotation.MultiRequestBody;
import jmu.shijh.tinymall.common.util.JsonResp;
import jmu.shijh.tinymall.common.util.PageDTO;
import jmu.shijh.tinymall.common.util.PageVO;
import jmu.shijh.tinymall.common.util.R;
import jmu.shijh.tinymall.domain.dto.CollectionDTO;
import jmu.shijh.tinymall.domain.entity.Product;
import jmu.shijh.tinymall.domain.enums.ProductStatus;
import jmu.shijh.tinymall.service.CollectionService;
import jmu.shijh.tinymall.service.ProductService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author shijh
 * @since 2021-06-08
 */
@RestController
@RequestMapping("/collection")
public class CollectionController {

    @Autowired
    private CollectionService collectionService;
    @Autowired
    private ProductService productService;

    @PostMapping("/query/page")
    @RequiresPermissions("collection:select")
    public JsonResp getCollection(@MultiRequestBody(value = "dto", required = false) CollectionDTO dto,
                                   @MultiRequestBody(value = "page",required = false) PageDTO page) {
        PageVO pageVO = collectionService.pagingQuery(dto, page);
        return R.ok().data(pageVO).build();
    }

    @GetMapping("/delete/{collId}")
    @RequiresPermissions("collection:delete")
    public JsonResp deleteCollection(@PathVariable Long collId) {
        collectionService.removeByDTO(new CollectionDTO().setCollId(collId));
        return R.ok().msg("删除成功").build();
    }

    @GetMapping("/add/{productId}")
    @RequiresPermissions("collection:insert")
    public JsonResp addCollection(@PathVariable Long productId) {
        Product p = productService.getById(productId);
        collectionService.collectProduct(p);
        return R.ok().msg("收藏成功").build();
    }
}
