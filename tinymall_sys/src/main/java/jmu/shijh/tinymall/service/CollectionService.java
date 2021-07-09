package jmu.shijh.tinymall.service;

import jmu.shijh.tinymall.common.util.PageDTO;
import jmu.shijh.tinymall.common.util.PageVO;
import jmu.shijh.tinymall.domain.dto.CollectionDTO;
import jmu.shijh.tinymall.domain.entity.Collection;
import com.baomidou.mybatisplus.extension.service.IService;
import jmu.shijh.tinymall.domain.entity.Product;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *
 */
public interface CollectionService extends IService<Collection> {

    PageVO pagingQuery(CollectionDTO dto, PageDTO page);

    void collectProduct(Product product);

    @Transactional
    void removeBatch(List<Long> collIds);

    @Transactional
    void removeByDTO(CollectionDTO dto);
}
