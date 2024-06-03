package com.wlinsk.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wlinsk.model.entity.UserAnswerRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Entity com.wlinsk.model.entity.UserAnswerRecord
 */
public interface UserAnswerRecordMapper extends BaseMapper<UserAnswerRecord> {

    UserAnswerRecord queryById(@Param("recordId") String recordId);

    IPage<UserAnswerRecord> queryPage(Page<UserAnswerRecord> page, @Param("userId") String userId,
                                      @Param("appId") String appId, @Param("recordId") String recordId,
                                      @Param("resultName") String resultName);

    int deleteRecord(@Param("userAnswerRecord") UserAnswerRecord userAnswerRecord);
}




