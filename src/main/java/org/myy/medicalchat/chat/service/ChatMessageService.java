package org.myy.medicalchat.chat.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.myy.medicalchat.chat.entity.ChatMessage;
import org.myy.medicalchat.chat.mapper.ChatMessageMapper;
import org.springframework.stereotype.Service;

/**
* @author ADMIN
* @description 针对表【message(对话消息表)】的数据库操作Service
* @createDate 2026-04-02 02:44:19
*/
@Service
public class ChatMessageService extends ServiceImpl<ChatMessageMapper, ChatMessage> {

}
