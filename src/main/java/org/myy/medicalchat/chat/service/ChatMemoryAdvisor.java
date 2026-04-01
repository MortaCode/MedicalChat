//package org.myy.medicalchat.chat.service;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.ai.chat.messages.Message;
//import org.springframework.ai.chat.messages.UserMessage;
//import org.springframework.ai.chat.model.ChatResponse;
//import org.springframework.ai.chat.prompt.Prompt;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Slf4j
//@Component
//public class ChatMemoryAdvisor implements CallAroundAdvisor, StreamAroundAdvisor {
//    private final ChatMemoryService chatMemoryService;
//
//    // 默认上下文窗口大小
//    private static final int DEFAULT_CONTEXT_WINDOW = 10;
//    private int contextWindowSize = DEFAULT_CONTEXT_WINDOW;
//
//    public ChatMemoryAdvisor(ChatMemoryService chatMemoryService) {
//        this.chatMemoryService = chatMemoryService;
//    }
//
//    @Override
//    public AdvisedResponse aroundCall(AdvisedRequest advisedRequest, CallAroundAdvisorChain chain) {
//        String sessionId = extractSessionId(advisedRequest);
//        log.info("处理对话请求, sessionId: {}", sessionId);
//
//        // 1. 获取历史记忆
//        List<Message> historyMessages = chatMemoryService.getMemory(sessionId);
//        log.info("获取到历史消息数: {}", historyMessages.size());
//
//        // 2. 获取当前用户消息
//        String currentUserInput = advisedRequest.userText();
//        UserMessage currentMessage = new UserMessage(currentUserInput);
//
//        // 3. 构建包含历史消息的Prompt
//        Prompt enhancedPrompt = buildEnhancedPrompt(advisedRequest, historyMessages, currentMessage);
//
//        // 4. 创建增强后的请求
//        AdvisedRequest enhancedRequest = AdvisedRequest.from(advisedRequest)
//                .prompt(enhancedPrompt)
//                .build();
//
//        // 5. 执行调用
//        AdvisedResponse response = chain.nextCall(enhancedRequest);
//
//        // 6. 保存对话记忆
//        saveConversationMemory(sessionId, historyMessages, currentMessage, response);
//
//        return response;
//    }
//
//    @Override
//    public Flux<AdvisedResponse> aroundStream(AdvisedRequest advisedRequest, StreamAroundAdvisorChain chain) {
//        String sessionId = extractSessionId(advisedRequest);
//        log.info("处理流式对话请求, sessionId: {}", sessionId);
//
//        // 获取历史记忆
//        List<Message> historyMessages = chatMemoryService.getMemory(sessionId);
//
//        // 构建增强后的Prompt
//        UserMessage currentMessage = new UserMessage(advisedRequest.userText());
//        Prompt enhancedPrompt = buildEnhancedPrompt(advisedRequest, historyMessages, currentMessage);
//
//        AdvisedRequest enhancedRequest = AdvisedRequest.from(advisedRequest)
//                .prompt(enhancedPrompt)
//                .build();
//
//        // 处理流式响应
//        return chain.nextStream(enhancedRequest)
//                .doOnComplete(() -> {
//                    // 流式响应完成后，需要获取完整响应来保存记忆
//                    // 实际实现中需要收集所有响应片段
//                    log.info("流式响应完成, sessionId: {}", sessionId);
//                });
//    }
//
//    /**
//     * 构建增强后的Prompt，包含历史消息
//     */
//    private Prompt buildEnhancedPrompt(AdvisedRequest request, List<Message> historyMessages, UserMessage currentMessage) {
//        // 限制历史消息数量，取最近的N条
//        List<Message> recentHistory = historyMessages.stream()
//                .skip(Math.max(0, historyMessages.size() - contextWindowSize))
//                .collect(Collectors.toList());
//
//        // 构建消息列表：历史消息 + 当前消息
//        List<Message> allMessages = new java.util.ArrayList<>(recentHistory);
//        allMessages.add(currentMessage);
//
//        // 创建新的Prompt
//        return new Prompt(allMessages);
//    }
//
//    /**
//     * 保存对话记忆
//     */
//    private void saveConversationMemory(String sessionId, List<Message> historyMessages,
//                                        UserMessage currentMessage, AdvisedResponse response) {
//        try {
//            // 获取AI响应
//            ChatResponse chatResponse = response.response();
//            if (chatResponse != null && chatResponse.getResult() != null) {
//                String assistantResponse = chatResponse.getResult().getOutput().getText();
//                org.springframework.ai.chat.messages.AssistantMessage assistantMessage =
//                        new org.springframework.ai.chat.messages.AssistantMessage(assistantResponse);
//
//                // 更新记忆列表
//                List<Message> updatedMessages = new java.util.ArrayList<>(historyMessages);
//                updatedMessages.add(currentMessage);
//                updatedMessages.add(assistantMessage);
//
//                // 保存记忆
//                chatMemoryService.saveMemory(sessionId, updatedMessages);
//                log.info("保存对话记忆成功, sessionId: {}, 消息总数: {}", sessionId, updatedMessages.size());
//            }
//        } catch (Exception e) {
//            log.error("保存对话记忆失败, sessionId: {}", sessionId, e);
//        }
//    }
//
//    /**
//     * 从请求中提取sessionId
//     */
//    private String extractSessionId(AdvisedRequest request) {
//        // 从请求参数中获取sessionId
//        Object sessionIdObj = request.getUserParams().get("sessionId");
//        if (sessionIdObj != null) {
//            return sessionIdObj.toString();
//        }
//
//        // 从请求头获取
//        sessionIdObj = request.getRequestParams().get("sessionId");
//        if (sessionIdObj != null) {
//            return sessionIdObj.toString();
//        }
//
//        // 如果都没有，生成新的sessionId
//        return java.util.UUID.randomUUID().toString();
//    }
//
//    @Override
//    public int getOrder() {
//        return 0;
//    }
//}
