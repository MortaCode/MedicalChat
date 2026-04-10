package org.myy.medicalchat.chat.advisor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.*;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.myy.medicalchat.chat.service.KnowledgeBaseService;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class RAGAdvisor implements CallAdvisor, StreamAdvisor {

    private final KnowledgeBaseService knowledgeBaseService;

    // RAG 系统提示词模板
    private static final String RAG_SYSTEM_PROMPT = """
        你是一个专业的医疗咨询助手。请基于以下知识库内容回答用户问题。
        
        知识库内容：
        {context}
        
        注意事项：
        1. 如果知识库中没有相关信息，请明确告知用户并建议咨询专业医生
        2. 回答要准确、专业，避免编造信息
        3. 涉及诊断和治疗建议时，必须强调这仅供参考，建议线下就医
        4. 保持回答简洁清晰
        """;

    @Override
    public ChatClientResponse adviseCall(ChatClientRequest request, CallAdvisorChain chain) {
        log.debug("RAG顾问开始处理");

        // 1. 提取用户问题
        String userQuery = extractUserQuery(request);
        if (!StringUtils.hasText(userQuery)) {
            return chain.nextCall(request);
        }

        // 2. 从知识库检索相关内容
        String relevantContext = knowledgeBaseService.retrieveRelevantContext(userQuery, 5);

        // 3. 构建增强的Prompt
        ChatClientRequest enhancedRequest = enhanceRequestWithContext(request, relevantContext);

        // 4. 继续调用链
        return chain.nextCall(enhancedRequest);
    }

    @Override
    public Flux<ChatClientResponse> adviseStream(ChatClientRequest request, StreamAdvisorChain chain) {
        log.debug("RAG顾问流式处理开始");

        String userQuery = extractUserQuery(request);
        if (!StringUtils.hasText(userQuery)) {
            return chain.nextStream(request);
        }

        String relevantContext = knowledgeBaseService.retrieveRelevantContext(userQuery, 5);
        ChatClientRequest enhancedRequest = enhanceRequestWithContext(request, relevantContext);

        return chain.nextStream(enhancedRequest);
    }

    private ChatClientRequest enhanceRequestWithContext(ChatClientRequest request, String context) {
        // 如果没有检索到相关内容，直接返回原请求
        if (!StringUtils.hasText(context)) {
            log.debug("未检索到相关知识，使用原请求");
            return request;
        }

        // 构建包含知识库的SystemMessage
        String systemPrompt = RAG_SYSTEM_PROMPT.replace("{context}", context);
        SystemMessage ragSystemMessage = new SystemMessage(systemPrompt);

        // 获取原始消息
        Prompt originalPrompt = request.prompt();
        List<org.springframework.ai.chat.messages.Message> originalMessages = originalPrompt.getInstructions();

        // 构建新消息列表：RAG SystemMessage + 原始消息（移除原有的SystemMessage）
        List<org.springframework.ai.chat.messages.Message> newMessages = new ArrayList<>();
        newMessages.add(ragSystemMessage);

        for (org.springframework.ai.chat.messages.Message msg : originalMessages) {
            // 跳过原有的SystemMessage，避免冲突
            if (!(msg instanceof SystemMessage)) {
                newMessages.add(msg);
            }
        }

        // 创建新的Prompt
        Prompt enhancedPrompt = new Prompt(newMessages, originalPrompt.getOptions());

        return ChatClientRequest.builder()
                .prompt(enhancedPrompt)
                .context(request.context())
                .build();
    }

    private String extractUserQuery(ChatClientRequest request) {
        if (request.prompt() == null) {
            return null;
        }

        List<org.springframework.ai.chat.messages.Message> messages = request.prompt().getInstructions();
        if (messages == null || messages.isEmpty()) {
            return null;
        }

        // 获取最后一条用户消息
        for (int i = messages.size() - 1; i >= 0; i--) {
            org.springframework.ai.chat.messages.Message msg = messages.get(i);
            if (msg instanceof UserMessage) {
                return msg.getText();
            }
        }
        return null;
    }

    @Override
    public String getName() {
        return "ragAdvisor";
    }

    @Override
    public int getOrder() {
        return 50; // 在ChatMemoryAdvisor之前执行（order值越小优先级越高）
    }
}
