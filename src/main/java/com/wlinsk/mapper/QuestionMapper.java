package com.wlinsk.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wlinsk.model.entity.Question;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Entity com.wlinsk.model.entity.Question
 */
public interface QuestionMapper extends BaseMapper<Question> {

    IPage<Question> queryPage(Page<Question> page, @Param("appId") String appId);

    Question queryById( @Param("questionId") String questionId);

    int updateQuestion(@Param("question") Question question);

    Question queryByAppId(@Param("appId") String appId);

    Question queryLatestQuestionContentByAppId(@Param("appId") String appId);
}




