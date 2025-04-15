package hatmagicianmod.relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import hatmagicianmod.helpers.ModHelper;

public class DayDream extends CustomRelic {
    // 遗物ID（此处的ModHelper在“04 - 本地化”中提到）
    public static final String ID = ModHelper.makeID("DayDream");
    // 图片路径（大小128x128，可参考同目录的图片）
    private static final String IMG_PATH = "HatMagicianModRes/img/relics/DayDream.png";
    // 遗物未解锁时的轮廓。可以不使用。如果要使用，取消注释
    // private static final String OUTLINE_PATH = "HatMagicianModRes/img/relics/MyRelic_Outline.png";
    // 遗物类型
    private static final RelicTier RELIC_TIER = RelicTier.COMMON;
    // 点击音效
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT;

    public DayDream() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER, LANDING_SOUND);
        // 如果你需要轮廓图，取消注释下面一行并注释上面一行，不需要就删除
        // super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(OUTLINE_PATH), RELIC_TIER, LANDING_SOUND);
    }

    // 获取遗物描述，但原版游戏只在初始化和获取遗物时调用，故该方法等于初始描述
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public void onGainTempPower(int amount) {
        if (amount > 0) {
            AbstractDungeon.actionManager.addToTop(new GainBlockAction(AbstractDungeon.player, amount));
        }
    }
}
