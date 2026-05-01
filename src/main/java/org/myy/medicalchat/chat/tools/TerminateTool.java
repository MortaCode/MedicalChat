package org.myy.medicalchat.chat.tools;

import org.springframework.ai.tool.annotation.Tool;

public class TerminateTool {

    @Tool(description = """  
            当请求得到满足时或当助手无法继续执行任务时，终止交互。
            当您完成所有任务后，请使用此工具来结束工作。
            """)
    public String doTerminate() {
        return "任务结束";
    }
}

