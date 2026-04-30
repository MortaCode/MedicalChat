package org.myy.medicalchat.chat.tools;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WebScrapingTool {

    @Tool(description = "抓取该网址的内容信息")
    public String webScraping(@ToolParam(description = "正在被抓取网页内容的网址URL") String url){
        try {
            Document dc = Jsoup.connect(url).get();
            return dc.html();
        } catch (Exception e){
            log.info("网页内容获取失败, {}", e.getMessage());
            return "网页内容获取失败";
        }
    }

    public static void main(String[] args) {
        WebScrapingTool tool = new WebScrapingTool();
        String url = "https://www.baidu.com/index.php?tn=68018901_58_oem_dg";
        System.out.println(tool.webScraping(url));
    }
}
