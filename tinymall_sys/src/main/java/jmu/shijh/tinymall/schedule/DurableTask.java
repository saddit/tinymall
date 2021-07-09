package jmu.shijh.tinymall.schedule;

import jmu.shijh.tinymall.common.util.Times;
import jmu.shijh.tinymall.domain.dto.ActivityDTO;
import jmu.shijh.tinymall.domain.entity.Activity;
import jmu.shijh.tinymall.domain.entity.Collection;
import jmu.shijh.tinymall.domain.entity.Product;
import jmu.shijh.tinymall.domain.entity.Shop;
import jmu.shijh.tinymall.domain.enums.ActivityStatus;
import jmu.shijh.tinymall.domain.enums.ProductStatus;
import jmu.shijh.tinymall.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Component
public class DurableTask {

    @Autowired
    private ActivityService activityService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private CollectionService collectionService;

    @Scheduled(cron = "59 59 23 * * ?")
    @Async
    public void activityExpire() {
        activityService.updateByDTO(
                new ActivityDTO().setExpiredTimeEnd(Times.localNow())
                        .setActivityStatus(ActivityStatus.DEPLOYED.code()),
                new Activity().setActivityStatus(ActivityStatus.INVALID.code())
        );
    }

    @Scheduled(cron = "59 59 23 * * ?")
    @Async
    @Transactional
    public void productInvalid() {
        productService.lambdaUpdate()
                .set(Product::getProductStatus, ProductStatus.INVALID.code())
                .eq(Product::getStock, 0)
                .update();
    }

}
