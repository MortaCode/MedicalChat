package org.myy.medicalchat.common.config;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;
@Slf4j
@SpringBootTest
class DataSourceConfigTest {
    @Resource
    VectorStore pgVectorVectorStore;

    @Test
    void test() {
        List<Document> medicalDocuments = List.of(
                // 感冒/上呼吸道感染
                new Document("感冒症状：发热、鼻塞、流涕、咽痛。" +
                        "饮食建议：多喝温水、蜂蜜柠檬水、鸡汤；避免辛辣、生冷、油腻食物。" +
                        "用药建议：解热镇痛药如对乙酰氨基酚或布洛芬（体温>38.5℃使用）；抗组胺药缓解鼻塞。" +
                        "注意事项：症状持续超过7天或出现呼吸困难需就医。",
                        Map.of("category", "cold", "severity", "mild")),

                // 急性胃肠炎
                new Document("急性胃肠炎：腹泻、呕吐、腹痛。" +
                        "饮食建议：BRAT饮食（香蕉、米饭、苹果泥、吐司）；少量多次补充口服补液盐。" +
                        "避免：乳制品、高纤维食物、咖啡因、酒精。" +
                        "用药建议：蒙脱石散（吸附毒素）；益生菌（间隔2小时）；慎用止泻药（感染性腹泻禁用）。" +
                        "警示：出现血便、高热>39℃、意识模糊需立即就医。",
                        Map.of("category", "gastroenteritis", "warning", "high_risk")),

                // 偏头痛
                new Document("偏头痛症状：单侧搏动性头痛、畏光、恶心。" +
                        "饮食建议：补充镁（深绿色蔬菜、坚果）；避免触发食物：奶酪、红酒、巧克力、加工肉制品。" +
                        "用药建议：轻中度用布洛芬/萘普生；中重度用曲普坦类药物（需处方）。" +
                        "特殊人群：孕妇禁用曲普坦，咨询医生。" +
                        "紧急情况：突发剧烈头痛伴颈部僵硬或意识改变→立即急诊。",
                        Map.of("category", "headache", "requires_prescription", "true")),

                // 儿童发热（特殊人群）
                new Document("儿童发热（3个月-5岁）：体温≥38℃。" +
                        "饮食建议：母乳/配方奶频繁喂养；大儿童给电解质水、稀粥。" +
                        "用药建议：对乙酰氨基酚（10-15mg/kg，每4-6小时）；布洛芬（6个月以上，5-10mg/kg）。" +
                        "⚠️绝对禁止：阿司匹林（瑞氏综合征风险）。" +
                        "立即就医指征：<3个月发热、>40℃、热性惊厥、呼吸急促、精神萎靡。",
                        Map.of("category", "pediatric", "special_population", "children")),

                // 胃食管反流
                new Document("胃食管反流病：烧心、反酸。" +
                        "饮食建议：低脂饮食、燕麦、芦荟汁；避免：咖啡、薄荷、巧克力、番茄、柑橘。" +
                        "用药建议：抗酸剂（铝碳酸镁，短期）；H2受体拮抗剂（法莫替丁）；PPI（奥美拉唑，最多14天）。" +
                        "生活方式：睡前3小时禁食，抬高床头15-20cm。" +
                        "警示：吞咽疼痛、黑便、体重下降→胃镜检查。",
                        Map.of("category", "gerd", "lifestyle", "important")),

                // 妊娠期恶心呕吐（孕妇）
                new Document("孕早期恶心呕吐：通常6-12周。" +
                        "饮食建议：干饼干、烤面包、姜茶、少量多餐；避免油腻、辛辣、强烈气味。" +
                        "用药建议：维生素B6（10-25mg，每日3次）+多西拉敏（需医生处方）；严重时昂丹司琼（医院评估）。" +
                        "❗严格避免：异丙嗪、甲氧氯普胺（除非医生指导）。" +
                        "住院指征：无法进食超过24小时、体重下降>5%、尿酮体阳性。",
                        Map.of("category", "pregnancy", "special_population", "pregnant"))
        );
        // 添加文档
        pgVectorVectorStore.add(medicalDocuments);
        // 相似度查询
        List<Document> results = pgVectorVectorStore.similaritySearch(SearchRequest.builder().query("感冒症状：发热、鼻塞、流涕、咽痛").topK(5).build());
        log.info(results.toString());
        Assertions.assertNotNull(results);
    }

}