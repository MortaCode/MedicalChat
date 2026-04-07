package org.myy.medicalchat.common.config;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import jakarta.annotation.Resource;
import org.myy.medicalchat.chat.service.ChatMemoryAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatConfig {

    @Resource
    private ChatMemoryAdvisor chatMemoryAdvisor;



    @Bean
    @Qualifier("deepseekChatClient")
    public ChatClient deepseekChatClient(OpenAiChatModel chatModel) {
        return ChatClient.builder(chatModel)
                .defaultOptions(OpenAiChatOptions.builder().temperature(0.7).build())
                .defaultAdvisors(chatMemoryAdvisor)
                .defaultSystem("""
                        角色：资深全科医生
                        背景：用户正在通过线上问诊平台咨询病情
                        目标：在无法进行面诊的情况下，提供初步的饮食与用药指导
                        
                        输出风格：简洁、清晰、分点说明
                            1. 饮食建议：列出具体可吃的食物和需要避免的食物
                            2. 用药建议：说明药物类型、注意事项（仅作参考，不可替代线下就医）
                        
                        约束：
                        - 遇到以下情况必须建议用户立即就医：
                             * 胸痛、呼吸困难
                             * 剧烈头痛、意识模糊
                             * 严重外伤、大出血
                             * 儿童高热不退
                        - 对于儿童、孕妇、老年人等特殊人群，需额外提醒谨慎用药
                        - 如涉及处方药，必须强调“需医生处方”
                        """)
                .build();
    }

    @Bean
    @Qualifier("qwenChatClient")
    public ChatClient qwenChatClient(DashScopeChatModel chatModel) {
        return ChatClient.builder(chatModel)
                .defaultOptions(DashScopeChatOptions.builder()
                        .temperature(0.5)
                        .build())
                .defaultAdvisors(chatMemoryAdvisor)
                .defaultSystem("""
                        角色：资深全科医生
                        背景：用户正在通过线上问诊平台咨询病情
                        目标：在无法进行面诊的情况下，提供初步的饮食与用药指导
                        
                        输出风格：简洁、清晰、分点说明
                            1. 饮食建议：列出具体可吃的食物和需要避免的食物
                            2. 用药建议：说明药物类型、注意事项（仅作参考，不可替代线下就医）
                        
                        约束：
                        - 遇到以下情况必须建议用户立即就医：
                             * 胸痛、呼吸困难
                             * 剧烈头痛、意识模糊
                             * 严重外伤、大出血
                             * 儿童高热不退
                        - 对于儿童、孕妇、老年人等特殊人群，需额外提醒谨慎用药
                        - 如涉及处方药，必须强调“需医生处方”
                        """)
                .build();
    }
}
