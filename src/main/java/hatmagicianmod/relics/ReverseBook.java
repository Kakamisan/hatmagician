package hatmagicianmod.relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import hatmagicianmod.cards.IsSleep;
import hatmagicianmod.helpers.ModHelper;
import hatmagicianmod.powers.BrandPower;
import hatmagicianmod.powers.BrandPower.BRAND_TYPE;

import java.util.ArrayList;

import static hatmagicianmod.powers.BrandPower.BRAND_TYPE.LIGHTNING;
import static hatmagicianmod.powers.BrandPower.BRAND_TYPE.FIRE;
import static hatmagicianmod.powers.BrandPower.BRAND_TYPE.ICE;

public class ReverseBook extends CustomRelic {
    // 遗物ID（此处的ModHelper在“04 - 本地化”中提到）
    public static final String ID = ModHelper.makeID("ReverseBook");
    // 图片路径（大小128x128，可参考同目录的图片）
    private static final String IMG_PATH = "HatMagicianModRes/img/relics/ReverseBook.png";
    // 遗物未解锁时的轮廓。可以不使用。如果要使用，取消注释
    // private static final String OUTLINE_PATH = "HatMagicianModRes/img/relics/MyRelic_Outline.png";
    // 遗物类型
    private static final RelicTier RELIC_TIER = RelicTier.BOSS;
    // 点击音效
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT;

    // 超载类型记录
    private final ArrayList<Integer> overload_list = new ArrayList<>();

    public ReverseBook() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER, LANDING_SOUND);
        // 如果你需要轮廓图，取消注释下面一行并注释上面一行，不需要就删除
        // super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(OUTLINE_PATH), RELIC_TIER, LANDING_SOUND);
    }

    // 获取遗物描述，但原版游戏只在初始化和获取遗物时调用，故该方法等于初始描述
    public String getUpdatedDescription() {
//        return this.setDesc();
        return this.DESCRIPTIONS[0];
    }

    private int getOverloadType(BRAND_TYPE source, BRAND_TYPE target) {
        // 正序列
        if (source == LIGHTNING && target == ICE) {
            return 1;
        } else if (source == FIRE && target == LIGHTNING) {
            return 2;
        } else if (source == ICE && target == FIRE) {
            return 3;
        }
        // 逆序列
        else if (source == LIGHTNING && target == FIRE) {
            return 4;
        } else if (source == ICE && target == LIGHTNING) {
            return 5;
        } else if (source == FIRE && target == ICE) {
            return 6;
        }
        return 0;
    }

    private boolean isNext(int last, int next) {
        if (last < 3 || last > 3 && last < 6) {
            return next == last + 1;
        } else if (last == 3) {
            return next == 1;
        } else if (last == 6) {
            return next == 4;
        }
        return false;
    }

    public void onOverloadEvoke(BrandPower.BRAND_TYPE source, BrandPower.BRAND_TYPE target) {
        if (this.overload_list.size() >= 3) {
            return;
        }
        int next = this.getOverloadType(source, target);
        this.flash();
        // 空的，添加一个并返回
        if (this.overload_list.isEmpty()) {
            this.overload_list.add(next);
        } else {
            int last = this.overload_list.get(this.overload_list.size() - 1);
            // 如果是下一个，添加进去，不是的话，清除原来然后再添加进去
            if (this.isNext(last, next)) {
                this.overload_list.add(next);
            } else {
                this.overload_list.clear();
                this.overload_list.add(next);
            }
            // 添加了3个，触发效果
            if (this.overload_list.size() == 3) {
                if (this.overload_list.get(0) <= 3) {
                    this.addToTop(new MakeTempCardInHandAction(new IsSleep(), 2));
                    this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new IntangiblePlayerPower(AbstractDungeon.player, 2)));
                } else {
                    this.addToTop(new DrawCardAction(3));
                    this.addToTop(new GainEnergyAction(3));
                }
            }
        }
        this.updateDescription(null);
        this.counter = this.overload_list.size();
    }

    @Override
    public void updateDescription(AbstractPlayer.PlayerClass c) {
        this.description = this.setDesc();
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        this.initializeTips();
    }

    private String setDesc() {
        String desc;
        if (this.overload_list.isEmpty()) {
            desc = this.DESCRIPTIONS[0];
        } else {
            int first = this.overload_list.get(0);
            if (first <= 3) {
                desc = this.DESCRIPTIONS[0] + this.DESCRIPTIONS[1] + this.DESCRIPTIONS[2] + this.makeQueueDesc(first);
            } else {
                desc = this.DESCRIPTIONS[0] + this.DESCRIPTIONS[1] + this.DESCRIPTIONS[3] + this.makeQueueDesc(first);
            }
        }
        return desc;
    }

    private String makeQueueDesc(int first) {
        String desc = "";

        switch (first) {
            case 1:
            case 6:
                desc += this.DESCRIPTIONS[7];
                break;
            case 2:
            case 5:
                desc += this.DESCRIPTIONS[5];
                break;
            default:
                desc += this.DESCRIPTIONS[6];
                break;
        }

        for (Integer integer : this.overload_list) {
            switch (integer) {
                case 1:
                case 4:
                    desc += this.DESCRIPTIONS[4] + this.DESCRIPTIONS[5];
                    break;
                case 2:
                case 6:
                    desc += this.DESCRIPTIONS[4] + this.DESCRIPTIONS[6];
                    break;
                default:
                    desc += this.DESCRIPTIONS[4] + this.DESCRIPTIONS[7];
                    break;
            }
        }

        return desc;
    }

    @Override
    public void atBattleStart() {
        super.atBattleStart();
        this.overload_list.clear();
        this.updateDescription(null);
        this.counter = 0;
    }
}
