package top.misec.config;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * task config.
 *
 * @author JunzhouLiu
 */
@Data
@Accessors(chain = true)
public class TaskConfig {
    public Integer numberOfCoins;
    public Integer selectLike;
    public Boolean monthEndAutoCharge;
    public Boolean giveGift;
    public String upLive;
    public String devicePlatform;
    public Integer coinAddPriority;
    public String userAgent;
    public Boolean skipDailyTask;
    public String chargeForLove;
    public Integer reserveCoins;
    public Integer taskIntervalTime;
    public Integer chargeDay;
    public Integer predictNumberOfCoins;
    public Integer minimumNumberOfCoins;
    public Boolean matchGame;
    public Boolean showHandModel;
}