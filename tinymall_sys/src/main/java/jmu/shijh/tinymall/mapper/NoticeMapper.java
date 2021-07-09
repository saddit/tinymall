package jmu.shijh.tinymall.mapper;

import com.github.pagehelper.Page;
import jmu.shijh.tinymall.common.sqlbuilder.QuerySQL;
import jmu.shijh.tinymall.domain.dto.NoticeDTO;
import jmu.shijh.tinymall.domain.entity.Notice;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import jmu.shijh.tinymall.domain.vo.NoticeVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.stereotype.Repository;

/**
 * @Entity jmu.shijh.tinymall.domain.entity.Notice
 */
@Repository
public interface NoticeMapper extends BaseMapper<Notice> {

    @SelectProvider(value = provider.class, method = "query")
    Page<NoticeVO> selectPageByDTO(NoticeDTO dto);

    class provider {
        public String query(NoticeDTO dto) {
            return new QuerySQL(dto,"notice")
                    .select("notice.*","u.username deploy_username")
                    .join("user_info u on u.user_id = notice.deploy_uid")
                    .eq("notice.user_id", "userId")
                    .eq("is_read", "isRead")
                    .eq("notice.deploy_uid", "deployUid")
                    .ge("notice.create_time", "createTimeStart")
                    .lt("notice.create_time", "createTimeEnd")
                    .groupBy(dto.getDeployUid()!=null, "notice.notice_title")
                    .toString();
        }
    }

}




