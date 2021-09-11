package top.misec.task;

import lombok.extern.log4j.Log4j2;
import top.misec.config.ConfigLoader;
import top.misec.utils.VersionInfo;

/**
 * util test.
 *
 * @author Junzhou Liu
 * @create 2021/1/15 23:16
 */
@Log4j2
public class UnitTest {

    public static void main(String[] args) {

        log.info(" ");

        VersionInfo.printVersionInfo();

        //初始化配置
        if (args.length > 0) {
            log.info("使用自定义位置名称的配置文件当前加载的配置文件路径是:{}", args[0]);
            ConfigLoader.configInit(args[0]);
        } else {
            log.info("使用同目录下的config.json文件");
            ConfigLoader.configInit("config.json");
        }
        new UserCheck().run();
        new GiveGift().run();
        ServerPush.doServerPush();


    }
}
