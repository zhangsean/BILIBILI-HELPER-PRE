package top.misec.task;

import lombok.extern.log4j.Log4j2;
import top.misec.config.ConfigLoader;
import top.misec.login.ServerVerify;
import top.misec.login.Verify;
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
        if (args.length < 3) {
            log.info("正在使用新版配置文件置启动");
        }
        if (args.length >= 3) {
            log.info("正在使用执行参数启动，此方式即将弃用，请在config.json中配置ck和推送渠道");
            Verify.verifyInit(args[0], args[1], args[2]);
        }

        if (args.length == 4) {

            ServerVerify.verifyInit(args[3]);
        }

        if (args.length == 5) {
            ServerVerify.verifyInit(args[3], args[4]);
        }
        VersionInfo.printVersionInfo();
        //每日任务65经验
        //初始化配置
        ConfigLoader.configInit();

        new UserCheck().run();
        new GiveGift().run();
        ServerPush.doServerPush();


    }
}
