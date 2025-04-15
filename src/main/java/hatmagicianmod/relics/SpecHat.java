package hatmagicianmod.relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import hatmagicianmod.cards.*;
import hatmagicianmod.helpers.ModHelper;

import java.util.ArrayList;

public class SpecHat extends CustomRelic {
    // 遗物ID（此处的ModHelper在“04 - 本地化”中提到）
    public static final String ID = ModHelper.makeID("SpecHat");
    // 图片路径（大小128x128，可参考同目录的图片）
    private static final String IMG_PATH = "HatMagicianModRes/img/relics/SpecHat.png";
    // 遗物未解锁时的轮廓。可以不使用。如果要使用，取消注释
    // private static final String OUTLINE_PATH = "HatMagicianModRes/img/relics/MyRelic_Outline.png";
    // 遗物类型
    private static final RelicTier RELIC_TIER = RelicTier.STARTER;
    // 点击音效
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT;

    private final ArrayList<AbstractCard.CardTags> exhaust_brand_tags = new ArrayList<>();

    public SpecHat() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER, LANDING_SOUND);
        // 如果你需要轮廓图，取消注释下面一行并注释上面一行，不需要就删除
        // super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(OUTLINE_PATH), RELIC_TIER, LANDING_SOUND);
    }

    // 获取遗物描述，但原版游戏只在初始化和获取遗物时调用，故该方法等于初始描述
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new SpecHat();
    }

    @Override
    public void atBattleStart() {
        super.atBattleStart();
        this.flash();
        this.addToBot(new MakeTempCardInHandAction(new Prank()));

        // test
//        this.addToBot(new MakeTempCardInHandAction(new BlankMemory()));
//        this.addToBot(new MakeTempCardInHandAction(new Discharge()));
//        this.addToBot(new MakeTempCardInHandAction(new Ice()));
//        this.addToBot(new MakeTempCardInHandAction(new BrandScar()));
//        this.addToBot(new MakeTempCardInHandAction(new BurningAbyss()));
//        this.addToBot(new MakeTempCardInHandAction(new ElectricExchange()));
//        this.addToBot(new MakeTempCardInHandAction(new Improvise()));
//        this.addToBot(new MakeTempCardInHandAction(new OverloadBeam()));
//        this.addToBot(new MakeTempCardInHandAction(new Kite()));
//        this.addToBot(new MakeTempCardInHandAction(new Yawn()));
//        this.addToBot(new MakeTempCardInHandAction(new Rest()));
//        this.addToBot(new MakeTempCardInHandAction(new SleepTalk()));
//        this.addToBot(new MakeTempCardInHandAction(new MorningAnger()));
//        this.addToBot(new MakeTempCardInHandAction(new ZzzWorld()));
//        this.addToBot(new MakeTempCardInHandAction(new RapidCold()));
//        AbstractCard c = new BlearyEyed();
//        c.upgrade();
//        this.addToBot(new MakeTempCardInHandAction(c));
//        this.addToBot(new MakeTempCardInHandAction(new PlayFire()));
//        this.addToBot(new MakeTempCardInHandAction(new Fire()));
//        this.addToBot(new MakeTempCardInHandAction(new Ice()));
//        this.addToBot(new MakeTempCardInHandAction(new Ice()));
//        this.addToBot(new MakeTempCardInHandAction(new Lightning()));
//        this.addToBot(new MakeTempCardInHandAction(new TrickBox()));
        // end of test

        this.exhaust_brand_tags.clear();
    }

    @Override
    public void onExhaust(AbstractCard card) {
        super.onExhaust(card);

        if (this.exhaust_brand_tags.size() >= 4) {
            return;
        }

        if (card == null) {
            return;
        }

        AbstractCard.CardTags tag = BlankMemory.getCardBrandTag(card);

        if (tag != null) {
            if (this.exhaust_brand_tags.size() < 3) {
                if (!this.exhaust_brand_tags.contains(tag)) {
                    this.exhaust_brand_tags.add(tag);
                } else {
                    this.exhaust_brand_tags.clear();
                    this.exhaust_brand_tags.add(tag);
                }
            } else {
                if (this.exhaust_brand_tags.get(0) == tag) {
                    this.exhaust_brand_tags.add(tag);
                    AbstractCard blank_memory = new BlankMemory();
                    this.addToTop(new MakeTempCardInHandAction(blank_memory));
                } else {
                    this.exhaust_brand_tags.clear();
                    this.exhaust_brand_tags.add(tag);
                }
            }
            ModHelper.log("[空白记忆]计数" + this.exhaust_brand_tags.size());
        }
    }
}
