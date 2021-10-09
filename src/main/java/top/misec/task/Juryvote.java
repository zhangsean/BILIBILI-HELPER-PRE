package top.misec.task;

import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import top.misec.api.ApiList;
import top.misec.config.ConfigLoader;
import top.misec.utils.HttpUtils;

import java.util.Random;

/**
 * 风纪委投票.
 *
 * @author junzhou
 */
@Slf4j
public class Juryvote implements Task {
    @Override
    public void run() {
        if (null == ConfigLoader.helperConfig.getTaskConfig().JURY || !ConfigLoader.helperConfig.getTaskConfig().JURY) {
            log.error("未开启风纪委投票功能");
            return;
        }
        int code;
        int i = 1;
        do {
            JsonObject jsonObject = HttpUtils.doGet(ApiList.GET_JURY_CASE_NEXT);
            code = jsonObject.get("code").getAsInt();
            if (code == 0) {
                JsonObject data = jsonObject.getAsJsonObject("data");
                String caseid = data.get("case_id").getAsString();
                log.info("处理案件编号: {}", caseid);
                Randompause(16);
                int vote = ConfigLoader.helperConfig.getTaskConfig().getJURY_VOTE();
                if (vote == 0) {
                    Random random = new Random();
                    vote = random.nextInt(3) + 1;
                }
                String content = ConfigLoader.helperConfig.getTaskConfig().getJURY_CONTENT();
                if (null == content) {
                    content = "";
                }
                String requestBody = "case_id=" + caseid
                        + "&vote=" + vote // 投票观点，1：合适 2：一般 3：不合适 4：无法判断
                        + "&content=" + content //投票理由
                        + "&anonymous=" + ConfigLoader.helperConfig.getTaskConfig().getJURY_ANONYMOUS()  //0：匿名 1：不匿名
                        + "&csrf=" + ConfigLoader.helperConfig.getBiliVerify().getBiliJct();

                JsonObject votejsonObject = HttpUtils.doPost(ApiList.JURY_VOTE, requestBody);
                if (votejsonObject.get("code").getAsInt() == 0) {
                    log.info("案件编号:{}处理成功,本次运行已处理{}件案件.", caseid, i);
                    i++;
                    Randompause(0); //随机暂停
                } else {
                    log.error("案件编号:{}处理失败。错误报告：{}", caseid, votejsonObject.get("message").getAsString());
                }

                code = votejsonObject.get("code").getAsInt();
            } else {
                log.info(jsonObject.get("message").getAsString());
            }
        } while (code == 0);
    }

    public void Randompause(int min) {
        Random random = new Random();
        int sleepTime = (int) ((random.nextDouble() * 10 + min) * 1000);
        log.info("-----随机暂停{}ms-----", sleepTime);
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            log.warn("延时异常", e);
        }
    }


    @Override
    public String getName() {
        return "风纪委投票";
    }
}
