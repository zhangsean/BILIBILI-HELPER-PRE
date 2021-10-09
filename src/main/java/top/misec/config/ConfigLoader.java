package top.misec.config;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import top.misec.task.ServerPush;
import top.misec.utils.GsonUtils;
import top.misec.utils.HttpUtils;
import top.misec.utils.ReadFileUtils;

import java.io.File;

/**
 * Auto-generated: 2020-10-13 17:10:40.
 *
 * @author Junzhou Liu
 * @since 2020/10/13 17:11
 */
@Slf4j
@Data
public class ConfigLoader {
    public static HelperConfig helperConfig;
    private static String defaultHelperConfig;

    static {
        defaultHelperConfig = ReadFileUtils.loadJsonFromAsset("config.json");
        helperConfig = buildHelperConfig(defaultHelperConfig);
    }

    /**
     * 云函数初始化配置 .
     *
     * @param helperConfigJson config json
     */
    public static void serverlessConfigInit(String helperConfigJson) {
        helperConfig = buildHelperConfig(helperConfigJson);
        helperConfig.getBiliVerify().initCookiesMap();
        HttpUtils.setUserAgent(helperConfig.getTaskConfig().getUserAgent());
        log.info("云函数配置初始化成功\n");
    }

    /**
     * 优先从jar包同级目录读取.
     */
    public static void configInit(String filePath) {
        File Config = new File(filePath);
        if (Config.exists()) {
            String customConfig = ReadFileUtils.readFile(filePath);
            mergeConfig(GsonUtils.fromJson(customConfig, HelperConfig.class));
            log.info("读取自定义配置文件成功,若部分配置项不存在则会采用默认配置.");
            validationConfig();
            helperConfig.getBiliVerify().initCookiesMap();
            HttpUtils.setUserAgent(helperConfig.getTaskConfig().getUserAgent());
        } else {
            log.error("配置文件：{} 不存在", filePath);
            System.exit(0);
        }

    }

    /**
     * 使用自定义文件时校验相关值.
     */
    private static void validationConfig() {

        if (helperConfig.getBiliVerify().getBiliCookies().length() < 1) {
            log.error("未配置cookies,请先配置cookies再启动。");
            ServerPush.doServerPush();
            System.exit(0);
        }

        TaskConfig taskConfig = helperConfig.getTaskConfig();

        taskConfig.setChargeDay(taskConfig.getChargeDay() > 28 || taskConfig.getChargeDay() < 1 ? 28 : taskConfig.getChargeDay())
                .setTaskIntervalTime(taskConfig.getTaskIntervalTime() <= 0 ? 1 : taskConfig.getTaskIntervalTime())
                .setPredictNumberOfCoins(taskConfig.getPredictNumberOfCoins() > 10 ? 10 : taskConfig.getPredictNumberOfCoins() <= 0 ? 1 : taskConfig.getPredictNumberOfCoins());

        helperConfig.setTaskConfig(taskConfig);
    }

    /**
     * override config .
     *
     * @param sourceConfig sourceConfig
     */
    private static void mergeConfig(HelperConfig sourceConfig) {
        BeanUtil.copyProperties(sourceConfig, helperConfig, new CopyOptions().setIgnoreNullValue(true));
    }

    private static HelperConfig buildHelperConfig(String configJson) {
        return GsonUtils.fromJson(configJson, HelperConfig.class);
    }
}