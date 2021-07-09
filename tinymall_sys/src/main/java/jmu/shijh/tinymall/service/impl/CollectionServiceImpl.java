package jmu.shijh.tinymall.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import jmu.shijh.tinymall.common.exception.CustomException;
import jmu.shijh.tinymall.common.util.PageDTO;
import jmu.shijh.tinymall.common.util.PageVO;
import jmu.shijh.tinymall.common.util.Str;
import jmu.shijh.tinymall.domain.dto.CollectionDTO;
import jmu.shijh.tinymall.domain.entity.Collection;
import jmu.shijh.tinymall.domain.entity.Product;
import jmu.shijh.tinymall.domain.vo.CollectionVO;
import jmu.shijh.tinymall.shiro.UserIdentity;
import jmu.shijh.tinymall.service.CollectionService;
import jmu.shijh.tinymall.mapper.CollectionMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *
 */
@Service
public class CollectionServiceImpl extends ServiceImpl<CollectionMapper, Collection> implements CollectionService{

    @Autowired
    private CollectionMapper collectionMapper;

    @Autowired
    private UserIdentity user;

    @Override
    public PageVO pagingQuery(CollectionDTO dto, PageDTO page) {
        PageHelper.startPage(page);
        Page<CollectionVO> pages = collectionMapper.selectPageByDTO(dto);
        return new PageVO(pages);
    }

    @Override
    public void collectProduct(Product product) {
        Collection collection = new Collection();
        BeanUtils.copyProperties(product, collection, "userId", "deleted","createTime");
        collection.setUserId(user.getId());
        save(collection);
    }

    @Override
    @Transactional
    public void removeBatch(List<Long> collIds) {
        boolean b = removeByIds(collIds);
        if (!b) {
            throw new CustomException("删除失败");
        }
    }

    @Override
    @Transactional
    public void removeByDTO(CollectionDTO dto) {
        boolean remove = lambdaUpdate().eq(Str.notEmpty(dto.getCollStatus()), Collection::getCollStatus, dto.getCollStatus())
                .eq(Str.notEmpty(dto.getCollId()), Collection::getCollId, dto.getCollId()).remove();
        if(!remove) {
            throw new CustomException("删除失败");
        }
    }
}




