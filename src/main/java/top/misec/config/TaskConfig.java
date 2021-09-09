package top.misec.config;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import top.misec.utils.GsonUtils;
import top.misec.utils.HttpUtil;
import top.misec.utils.LoadFileResource;

/**
 * Auto-generated: 2020-10-13 17:10:40.
 *
 * @author Junzhou Liu
 * @since 2020/10/13 17:11
 */
@Log4j2
@Data
public class TaskConfig {

    private static TaskConfig taskConfig;
    private static String defaultConfig;

    static {
        defaultConfig = LoadFileResource.loadJsonFromAsset("config.json");
        taskConfig = GsonUtils.fromJson(defaultConfig, TaskConfig.class);
    }

    private Integer numberOfCoins;
    private Integer selectLike;
    private Boolean monthEndAutoCharge;
    private Boolean giveGift;
    private String upLive;
    private String devicePlatform;
    private Integer coinAddPriority;
    private String userAgent;
    private Boolean skipDailyTask;
    private String chargeForLove;
    private Integer reserveCoins;
    private Integer taskIntervalTime;
    private Integer chargeDay;
    private Integer predictNumberOfCoins;
    private Integer minimumNumberOfCoins;
    private Boolean matchGame;
    private Boolean showHandModel;

    private TaskConfig() {
    }

    public static TaskConfig getInstance() {
        if (taskConfig == null) {
            taskConfig = new TaskConfig();
        }
        return taskConfig;
    }

    public void configInit(String json) {
        taskConfig = GsonUtils.fromJson(json, TaskConfig.class);
        HttpUtil.setUserAgent(userAgent);
        log.info(toString());
    }

    /**
     * 优先从jar包同级目录读取.
     */
    public void configInit() {
        String customConfig = LoadFileResource.loadConfigJsonFromFile();
        if (customConfig != null) {
            log.info("读取外部自定义配置文件成功,若部分配置项不存在则会采用默认配置\n{}", customConfig);
            mergeConfig(GsonUtils.fromJson(customConfig, TaskConfig.class));
        } else {
            log.info("未扫描到外部配置文件,使用默认配置文件\n{}", defaultConfig);
        }
        validationConfig();
        HttpUtil.setUserAgent(userAgent);
    }

    /**
     * 使用自定义文件时校验相关值.
     */
    private void validationConfig() {
        chargeDay = chargeDay > 28 || chargeDay < 1 ? 28 : chargeDay;
        taskIntervalTime = taskIntervalTime <= 0 ? 1 : taskIntervalTime;
        predictNumberOfCoins = predictNumberOfCoins > 10 ? 10 : predictNumberOfCoins;
        predictNumberOfCoins = predictNumberOfCoins <= 0 ? 1 : predictNumberOfCoins;
    }

    private void mergeConfig(TaskConfig config) {
        BeanUtil.copyProperties(config, taskConfig, new CopyOptions().setIgnoreNullValue(true));
    }
}