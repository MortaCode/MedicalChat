package org.myy.medicalchat.common.config;

import org.myy.medicalchat.chat.tools.ResourceDownLoadTool;
import org.myy.medicalchat.chat.tools.TerminateTool;
import org.myy.medicalchat.chat.tools.WebScrapingTool;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbacks;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ToolConfig {

    @Bean
    public ToolCallback[] allTools(){
//        ResourceDownLoadTool resourceDownLoadTool = new ResourceDownLoadTool();
//        WebScrapingTool webScrapingTool = new WebScrapingTool();
//
//        return ToolCallbacks.from(
//                resourceDownLoadTool,
//                webScrapingTool
//        );
//        WebScrapingTool webScrapingTool = new WebScrapingTool();
        TerminateTool terminateTool = new TerminateTool();

        return ToolCallbacks.from(
                terminateTool
        );
    }
}
