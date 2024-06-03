package com.wlinsk.service.user;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wlinsk.model.dto.answer.req.AddUserAnswerReqDTO;
import com.wlinsk.model.dto.answer.req.QueryUserAnswerPageReqDTO;
import com.wlinsk.model.dto.answer.resp.QueryUserAnswerRecordDetailsRespDTO;
import com.wlinsk.model.entity.UserAnswerRecord;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 *
 */
public interface UserAnswerRecordService extends IService<UserAnswerRecord> {

    String addUserAnswer(AddUserAnswerReqDTO reqDTO);

    QueryUserAnswerRecordDetailsRespDTO queryById(String recordId);

    IPage<QueryUserAnswerRecordDetailsRespDTO> queryPage(QueryUserAnswerPageReqDTO reqDTO);

    void deleteById(String recordId);
}
