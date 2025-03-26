package hatmagicianmod.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.unique.PoisonLoseHpAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import hatmagicianmod.actions.DamageChainLightningEnemiesAction;
import hatmagicianmod.effects.AtkChainLightningEffect;
import hatmagicianmod.helpers.ModHelper;

import java.util.ArrayList;

public class BrandPower extends AbstractPower {

    public static final String POWER_ID;
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;

    public final BRAND_TYPE brand_type;

    public static int postfix = 0;

    static {
        POWER_ID = ModHelper.makeID("BrandPower");
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }

    public BrandPower(AbstractCreature owner, BRAND_TYPE type) {
        this.brand_type = type;

        // 实际名字是不同的
        this.name = this.getBrandName();
        // ID是独立且唯一
        this.ID = POWER_ID + postfix++;
        this.owner = owner;
        this.type = PowerType.BUFF;

        // 如果需要不能叠加的能力，只需将上面的Amount参数删掉，并把下面的Amount改成-1就行
        this.amount = -1;

        // 添加一大一小两张能力图
        String path128 = "ExampleModResources/img/powers/Example84.png";
        String path48 = "ExampleModResources/img/powers/Example32.png";
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 32, 32);

        // 首次添加能力更新描述
        this.updateDescription();
    }

    // 能力在更新时如何修改描述
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], this.getBrandSubDesc());
    }

    // 被动效果
    public void atStartOfTurn() {
//        if (isPlayer)
//            return;
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && !AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            switch (this.brand_type) {
                case LIGHTNING:
                    this.addToBot(new VFXAction(new AtkChainLightningEffect()));
                    this.addToBot(new DamageChainLightningEnemiesAction(this.brandValue()));
                    break;
                case FIRE:
                case ICE:
                default:
            }
        }
    }

    // 生成时音效
    public void playApplyPowerSfx() {
        switch (this.brand_type) {
            case LIGHTNING:
                CardCrawlGame.sound.play("ORB_LIGHTNING_CHANNEL", 0.1F);
            case FIRE:
                CardCrawlGame.sound.play("ORB_DARK_CHANNEL", 0.1F);
            case ICE:
                CardCrawlGame.sound.play("ORB_FROST_CHANNEL", 0.1F);
            default:
        }
    }

    // ############## 工具 ##############

    // 场上是否存在任意印记
    public static boolean hasAnyMonstersBrand() {
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            if (!m.isDeadOrEscaped() && BrandPower.hasBrandPower(m)) {
                return true;
            }
        }
        return false;
    }

    // 获取该怪物的所有印记
    public static ArrayList<AbstractPower> getBrandPowers(AbstractMonster m) {
        ArrayList<AbstractPower> list = new ArrayList<>();
        for (AbstractPower p : m.powers) {
            if (p.ID.contains(ModHelper.makeID("BrandPower"))) {
                list.add(p);
            }
        }
        return list;
    }

    // 怪物是否存在任意印记
    public static boolean hasBrandPower(AbstractMonster m) {
        for (AbstractPower p : m.powers) {
            if (p.ID.contains(ModHelper.makeID("BrandPower"))) {
                return true;
            }
        }
        return false;
    }

    // 怪物是否存在特定类型的印记
    public static boolean hasBrandPower(AbstractMonster m, BRAND_TYPE type) {
        for (AbstractPower p : m.powers) {
            if (p.ID.contains(ModHelper.makeID("BrandPower"))) {
                BrandPower p2 = (BrandPower) p;
                if (p2.brand_type == type)
                    return true;
            }
        }
        return false;
    }

    // 获取印记实际名字 闪电/火焰/冰冻
    private String getBrandName() {
        switch (this.brand_type) {
            case LIGHTNING:
                return DESCRIPTIONS[1];
            case FIRE:
                return DESCRIPTIONS[2];
            case ICE:
                return DESCRIPTIONS[3];
            default:
                return "";
        }
    }

    // 获取印记实际描述 闪电/火焰/冰冻
    private String getBrandSubDesc() {
        switch (this.brand_type) {
            case LIGHTNING:
                return String.format(DESCRIPTIONS[4], this.brandValue());
            case FIRE:
                return String.format(DESCRIPTIONS[5], this.brandValue());
            case ICE:
                return String.format(DESCRIPTIONS[6], this.brandValue());
            default:
                return "";
        }
    }

    // 印记的最终数值
    private int brandValue() {
        return this.baseValue() + this.playerAddValue();
    }

    // 印记的基础数值
    private int baseValue() {
        switch (this.brand_type) {
            case LIGHTNING:
                return 3;
            case FIRE:
            case ICE:
            default:
                return 1;
        }
    }

    // 好奇心提供的数值
    private int playerAddValue() {
        AbstractPower p = AbstractDungeon.player.getPower(ModHelper.makeID("CuriosityPower"));
        if (p != null) {
            return p.amount;
        }
        return 0;
    }

    // 印记类型
    public enum BRAND_TYPE {
        LIGHTNING, FIRE, ICE
    }
}
