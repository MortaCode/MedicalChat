package org.myy.medicalchat.chat.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myy.medicalchat.chat.vo.ChatMessageVo;
import org.myy.medicalchat.chat.vo.ChatModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class ToolService {

    private final ToolCallback[] allTools;
    private final ModelSelectService modelSelectService;

    public String doChatWithTools(ChatMessageVo messageVo){
        ChatClient chatClient = modelSelectService.selectModel(ChatModel.fromString(messageVo.getModelName()));
        log.info("开始执行，大语言模型：{}", messageVo.getModelName());
        String result = chatClient.prompt()
                .user(messageVo.getUserInput())
                .toolCallbacks(allTools)  //tools()
                .call()
                .content();
        log.info("执行结束，大语言模型：{}，输出内容：{}", messageVo.getModelName(), result);
        return result;
    }

}
