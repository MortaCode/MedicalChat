package org.myy.medicalchat.chat.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.myy.medicalchat.chat.entity.ChatSession;
import org.myy.medicalchat.chat.mapper.ChatSessionMapper;
import org.springframework.stereotype.Service;

/**
* @author ADMIN
* @description 针对表【session(对话会话表)】的数据库操作Service
* @createDate 2026-04-02 02:43:53
*/
@Service
public class ChatSessionService extends ServiceImpl<ChatSessionMapper, ChatSession> {

}
