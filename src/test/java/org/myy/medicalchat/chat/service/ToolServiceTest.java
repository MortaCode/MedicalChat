package org.myy.medicalchat.chat.service;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.myy.medicalchat.chat.vo.ChatMessageVo;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ToolServiceTest {

    @Resource
    ToolService toolService;

    @Test
    void doChatWithTools(){
        ChatMessageVo messageVo = new ChatMessageVo();
        messageVo.setUserInput("获取网址https://www.baidu.com/index.php?tn=68018901_58_oem_dg的内容并总结");
        String result = toolService.doChatWithTools(messageVo);
        System.out.println(result);
    }
}