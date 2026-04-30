package org.myy.medicalchat.chat.tools;

import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.myy.medicalchat.common.constants.FileConstants;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ResourceDownLoadTool {

    @Tool(description = "根据用户给的网址URL信息,网址资源下载到指定文件中")
    public String resourceDownLoad(@ToolParam(description = "用户给的网址URL") String url,
                                   @ToolParam(description = "保存的目标文件名称") String fileName){
        String fileDir = FileConstants.FILE_SAVE_DIR + "/download";
        String filePath = fileDir + "/" + fileName;
        try{
            FileUtil.mkdir(fileDir);
            HttpUtil.downloadFile(url, filePath);
            return "已成功保存到文件中" + filePath;
        } catch (Exception e){
            log.info( "资源下载失败", e.getMessage());
            return "资源下载失败";
        }
    }

    public static void main(String[] args) {
        ResourceDownLoadTool tool = new ResourceDownLoadTool();
        String url = "https://www.baidu.com/index.php?tn=68018901_58_oem_dg";
        System.out.println(tool.resourceDownLoad(url, "百度"));
    }
}
