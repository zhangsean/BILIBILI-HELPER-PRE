package top.misec.config;

import cn.hutool.core.bean.BeanUtil;
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
        chargeDay = 28;
        taskIntervalTime = 10;
        predictNumberOfCoins = 1;
        showHandModel = false;
        matchGame = false;
        monthEndAutoCharge = false;
        minimumNumberOfCoins = 1000;
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
     * 读取配置文件 src/main/resources/config.json
     */
    public void configInit() {
        String configJson = LoadFileResource.loadJsonFromAsset("config.json");
        configJson = resolveOriginConfig(configJson);
        if (configJson != null) {
            log.debug("读取初始化配置文件成功");
            mergeConfig(GsonUtils.fromJson(configJson, TaskConfig.class));
        }

        configJson = LoadFileResource.loadConfigJsonFromFile();
        configJson = resolveOriginConfig(configJson);
        if (configJson != null) {
            log.debug("读取外部配置文件成功");
            mergeConfig(GsonUtils.fromJson(configJson, TaskConfig.class));
        }
        validationConfig();
        HttpUtil.setUserAgent(userAgent);
        log.info(toString());
    }

    private String resolveOriginConfig(String originConfig) {
        if (originConfig == null) {
            return null;
        }
        /*
         *兼容旧配置文件.
         * "skipDailyTask": 0 -> "skipDailyTask": false.
         * "skipDailyTask": 1 -> "skipDailyTask": true.
         */
        String target0 = "\"skipDailyTask\": 0";
        String target1 = "\"skipDailyTask\": 1";
        if (originConfig.contains(target0)) {
            log.debug("兼容旧配置文件，skipDailyTask的值由0变更为false");
            return originConfig.replaceAll(target0, "\"skipDailyTask\": false");
        } else if (originConfig.contains(target1)) {
            log.debug("兼容旧配置文件，skipDailyTask的值由1变更为true");
            return originConfig.replaceAll(target1, "\"skipDailyTask\": true");
        } else {
            log.debug("使用的是最新格式的配置文件，无需执行兼容性转换");
            return originConfig;
        }
    }

    private void validationConfig() {
        chargeDay = chargeDay > 28 || chargeDay < 1 ? 28 : chargeDay;
        taskIntervalTime = taskIntervalTime <= 0 ? 1 : taskIntervalTime;
        predictNumberOfCoins = predictNumberOfCoins > 10 ? 10 : predictNumberOfCoins;
        predictNumberOfCoins = predictNumberOfCoins <= 0 ? 1 : predictNumberOfCoins;
    }

    private void mergeConfig(TaskConfig config) {
        BeanUtil.copyProperties(config, taskConfig);
    }
}
