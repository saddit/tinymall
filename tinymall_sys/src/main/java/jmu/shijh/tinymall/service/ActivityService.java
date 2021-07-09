package jmu.shijh.tinymall.service;

import jmu.shijh.tinymall.common.util.PageDTO;
import jmu.shijh.tinymall.common.util.PageVO;
import jmu.shijh.tinymall.domain.dto.ActivityDTO;
import jmu.shijh.tinymall.domain.dto.ActivityUpdate;
import jmu.shijh.tinymall.domain.entity.Activity;
import com.baomidou.mybatisplus.extension.service.IService;
import jmu.shijh.tinymall.domain.vo.ActivityVO;

import java.util.List;

/**
 *
 */
public interface ActivityService extends IService<Activity> {

    void removeActivity(Long activityId);

    List<Activity> getByIds(Long... ids);

    Long addActivity(Activity activity);

    PageVO pagingQueryPage(ActivityDTO activity, PageDTO page);

    void updateByDTO(ActivityDTO dto, Activity activity);

    void updateActivity(ActivityUpdate activity);

    ActivityVO getActivityDetail(Long id);
}
