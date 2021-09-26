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
    public Boolean skipDailyTask;
    public Boolean monthEndAutoCharge;
    public Boolean giveGift;
    public Boolean matchGame;
    public Boolean showHandModel;
    public Boolean silver2Coin;
    public Boolean JURY;
    public Integer predictNumberOfCoins;
    public Integer minimumNumberOfCoins;
    public Integer numberOfCoins;
    public Integer selectLike;
    public Integer JURY_VOTE;
    public Integer JURY_ANONYMOUS;
    public String upLive;
    public String devicePlatform;
    public Integer coinAddPriority;
    public String userAgent;
    public String chargeForLove;
    public String JURY_CONTENT;
    public Integer chargeDay;
    public Integer reserveCoins;
    public Integer taskIntervalTime;
}