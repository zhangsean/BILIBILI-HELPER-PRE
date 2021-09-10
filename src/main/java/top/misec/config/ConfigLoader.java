package top.misec.config;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import lombok.Getter;
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
public class ConfigLoader {

    @Getter
    private static TaskConfig taskConfig;

    @Getter
    private static String defaultConfig;

    static {
        defaultConfig = LoadFileResource.loadJsonFromAsset("config.json");
        taskConfig = build(defaultConfig);
    }

    /**
     * 云函数初始化配置 .
     *
     * @param json config json
     */
    public static void configInit(String json) {
        taskConfig = build(json);
        HttpUtil.setUserAgent(taskConfig.getUserAgent());
        log.info(taskConfig.toString());
    }

    /**
     * 优先从jar包同级目录读取.
     */
    public static void configInit() {
        String customConfig = LoadFileResource.loadConfigJsonFromFile();
        if (customConfig != null) {
            mergeConfig(GsonUtils.fromJson(customConfig, TaskConfig.class));
            log.info("读取外部自定义配置文件成功,若部分配置项不存在则会采用默认配置,合并后的配置为\n{}", taskConfig.toString());
        } else {
            log.info("未扫描到外部配置文件,使用默认配置文件\n{}", defaultConfig);
        }
        validationConfig();
        HttpUtil.setUserAgent(taskConfig.getUserAgent());
    }

    /**
     * 使用自定义文件时校验相关值.
     */
    private static void validationConfig() {
        taskConfig.setChargeDay(taskConfig.getChargeDay() > 28 || taskConfig.getChargeDay() < 1 ? 28 : taskConfig.getChargeDay())
                .setTaskIntervalTime(taskConfig.getTaskIntervalTime() <= 0 ? 1 : taskConfig.getTaskIntervalTime())
                .setPredictNumberOfCoins(taskConfig.getPredictNumberOfCoins() > 10 ? 10 : taskConfig.getPredictNumberOfCoins() <= 0 ? 1 : taskConfig.getPredictNumberOfCoins());
    }

    /**
     * override config .
     *
     * @param sourceConfig sourceConfig
     */
    private static void mergeConfig(TaskConfig sourceConfig) {
        BeanUtil.copyProperties(sourceConfig, taskConfig, new CopyOptions().setIgnoreNullValue(true));
    }

    private static TaskConfig build(String configJson) {
        return GsonUtils.fromJson(configJson, TaskConfig.class);
    }
}