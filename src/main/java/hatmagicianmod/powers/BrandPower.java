package hatmagicianmod.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
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

    public static int postfix = 0;  // 实现唯一ID

    public boolean is_activated = false;    // 是否已激活
    public int scar_turn = 0;               // 创伤，激活后经过多少次自动触发被动后消失
    public boolean is_played_sfx = false;   // 只播放一次生成音效

    // 印记配置类
    public static class BrandBaseClass {
        public int passive_value;   // 被动数值
        public int evoke_value;     // 激活数值

        public BrandBaseClass(int passive_value, int evoke_value) {
            this.passive_value = passive_value;
            this.evoke_value = evoke_value;
        }
    }

    public static BrandBaseClass[] BASE;

    static {
        POWER_ID = ModHelper.makeID("BrandPower");
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;

        BASE = new BrandBaseClass[3];
        BASE[BRAND_TYPE.LIGHTNING.ordinal()] = new BrandBaseClass(3, 8);
        BASE[BRAND_TYPE.FIRE.ordinal()] = new BrandBaseClass(1, 1);
        BASE[BRAND_TYPE.ICE.ordinal()] = new BrandBaseClass(1, 3);
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

    // 每回合被动效果
    public void atStartOfTurn() {
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && !AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            // 触发被动
            this.usePassive();
            // 如果是已激活印记，使其创伤数-1，创伤小于等于0则移除此印记
            if (this.is_activated) {
                this.scar_turn--;
                this.tryRemove();
            }
        }
    }

//    // 推印记 *不生效，具体原因懒得看
//    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
//        ModHelper.log("添加了新的能力" + power.ID);
//        if (BrandPower.isBrandPower(power) && target == this.owner && !this.is_activated) {
//            this.evoke();
//        }
//    }

    // 使用被动（因为创伤数要自动使用被动时处理，不能一起放在被动里）
    public void usePassive() {
        switch (this.brand_type) {
            case LIGHTNING:
                this.addToTop(new DamageChainLightningEnemiesAction(this.brandPassiveValue()));
                this.addToTop(new VFXAction(new AtkChainLightningEffect()));
                break;
            case FIRE:
            case ICE:
            default:
        }
        this.flash();
    }

    // 使用激活
    public void useEvoke() {
        switch (this.brand_type) {
            case LIGHTNING:
                this.addToTop(new DamageChainLightningEnemiesAction(this.brandEvokeValue()));
                this.addToTop(new VFXAction(new AtkChainLightningEffect()));
                break;
            case FIRE:
            case ICE:
            default:
        }
        this.flash();
    }

    // 激活
    public void evoke() {
        if (this.is_activated)
            return;
        this.useEvoke();
        this.is_activated = true;
        this.tryRemove();
    }

    // 尝试移除印记 (若已激活且创伤数为0，则移除)
    public void tryRemove() {
        if (this.is_activated && this.scar_turn <= 0) {
            // 执行移除
            this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        }
    }

    // 生成时音效
    public void playApplyPowerSfx() {
        if (this.is_played_sfx)
            return;
        switch (this.brand_type) {
            case LIGHTNING:
                CardCrawlGame.sound.play("ORB_LIGHTNING_CHANNEL", 0.1F);
            case FIRE:
                CardCrawlGame.sound.play("ORB_DARK_CHANNEL", 0.1F);
            case ICE:
                CardCrawlGame.sound.play("ORB_FROST_CHANNEL", 0.1F);
            default:
        }
        this.is_played_sfx = true;
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
            if (BrandPower.isBrandPower(p)) {
                list.add(p);
            }
        }
        return list;
    }

    // 怪物是否存在任意印记
    public static boolean hasBrandPower(AbstractMonster m) {
        for (AbstractPower p : m.powers) {
            if (BrandPower.isBrandPower(p)) {
                return true;
            }
        }
        return false;
    }

    // 怪物是否存在特定类型的印记
    public static boolean hasBrandPower(AbstractMonster m, BRAND_TYPE type) {
        for (AbstractPower p : m.powers) {
            if (BrandPower.isBrandPower(p)) {
                BrandPower p2 = (BrandPower) p;
                if (p2.brand_type == type)
                    return true;
            }
        }
        return false;
    }

    // 是否印记能力
    public static boolean isBrandPower(AbstractPower p) {
        return p.ID.contains(ModHelper.makeID("BrandPower"));
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
                return String.format(DESCRIPTIONS[4], this.brandPassiveValue());
            case FIRE:
                return String.format(DESCRIPTIONS[5], this.brandPassiveValue());
            case ICE:
                return String.format(DESCRIPTIONS[6], this.brandPassiveValue());
            default:
                return "";
        }
    }

    // 印记的被动最终数值
    private int brandPassiveValue() {
        return BASE[this.brand_type.ordinal()].passive_value + this.playerCuriosity();
    }

    // 印记的激活最终数值
    private int brandEvokeValue() {
        return BASE[this.brand_type.ordinal()].evoke_value + this.playerCuriosity();
    }

    // 好奇心提供的数值
    private int playerCuriosity() {
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
