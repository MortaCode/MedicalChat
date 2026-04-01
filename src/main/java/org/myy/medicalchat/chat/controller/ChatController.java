package org.myy.medicalchat.chat.controller;

import org.myy.medicalchat.chat.service.ModelSelectService;
import org.myy.medicalchat.chat.vo.ChatModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("chat")
public class ChatController {

    private static final Logger log = LoggerFactory.getLogger(ChatController.class);

    private final ModelSelectService modelSelectService;

    public ChatController(ModelSelectService modelSelectService) {
        this.modelSelectService = modelSelectService;
    }

    @GetMapping("/ai")
    public ResponseEntity<String> generation(@RequestParam String userInput,
                                             @RequestParam(defaultValue = "deepseek") String modelName) {
        ChatClient chatClient = modelSelectService.selectModel(ChatModel.valueOf(modelName));
        log.info("开始执行，大语言模型：{}", chatClient.getClass().getSimpleName());
        String result = chatClient.prompt()
                .user(userInput)
                .call()
                .content();
        log.info("执行结束，输出内容：{}", result);
        return ResponseEntity.ok(result);
    }
}
