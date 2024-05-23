package com.wlinsk.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wlinsk.model.entity.Question;
import com.wlinsk.service.QuestionService;
import com.wlinsk.mapper.QuestionMapper;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question>
    implements QuestionService{

}




