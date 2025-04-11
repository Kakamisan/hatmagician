package hatmagicianmod.powers;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import hatmagicianmod.actions.BrandEvokeEndAction;
import hatmagicianmod.actions.DamageChainLightningEnemiesAction;
import hatmagicianmod.cards.BaseOnOverloadEvoke;
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
    public int scar_turn = 0;               // 烙印数，激活后经过多少次自动触发被动后消失
    public boolean go_for_stack = false;    // 是否即将去执行重叠烙印能力，是的话，即使有烙印数也要移除 *对应的堆叠数加到第一个相同的烙印上面
    public boolean stacked = false;         // 是否已重叠完成
    public boolean is_played_sfx = false;   // 只播放一次生成音效
    public boolean is_evoking = false;      // 印记是否激活中

    private int curiosity = 0;      // 临时存储好奇心数值
//    private boolean charge = false; // 临时存储充电能力是否开启 *原因：onRemove时player身上还有这个power

    // 印记配置类
    public static class BrandBaseClass {
        public int passive_value;   // 被动数值
        public int evoke_value;     // 激活数值
        public int overload_value1; // 超载1
        public int overload_value2; // 超载2
        public int curiosity_rate;  // 好奇心提供加成的倍率 *闪电为2倍，其他为1倍
        public float charge_rate;   // 充电提供加成的倍率 *闪电为1.5倍，其他为1倍
        public int base_scar;       // 基础烙印回合数

        public BrandBaseClass(int passive_value, int evoke_value, int overload_value1, int overload_value2, int curiosity_rate, float charge_rate, int base_scar) {
            this.passive_value = passive_value;
            this.evoke_value = evoke_value;
            this.overload_value1 = overload_value1;
            this.overload_value2 = overload_value2;
            this.curiosity_rate = curiosity_rate;
            this.charge_rate = charge_rate;
            this.base_scar = base_scar;
        }
    }

    public static BrandBaseClass[] BASE;

    static {
        POWER_ID = ModHelper.makeID("BrandPower");
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;

        BASE = new BrandBaseClass[3];
        BASE[BRAND_TYPE.LIGHTNING.ordinal()] = new BrandBaseClass(3, 8, 8, 2, 2, 1.5F, 0);
        BASE[BRAND_TYPE.FIRE.ordinal()] = new BrandBaseClass(1, 1, 1, 1, 1, 1.0F, 2);
        BASE[BRAND_TYPE.ICE.ordinal()] = new BrandBaseClass(2, 5, 8, 3, 1, 1.0F, 0);
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

//        // 添加一大一小两张能力图
//        String path128 = "HatMagicianModRes/img/powers/Example84.png";
//        String path48 = "HatMagicianModRes/img/powers/Example32.png";
//        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 84, 84);
//        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 32, 32);

        switch (type) {
            case LIGHTNING:
                this.loadRegion("mastery");
                break;
            case FIRE:
                this.loadRegion("attackBurn");
                break;
            case ICE:
                this.loadRegion("int");
                break;
        }

        // 首次添加能力更新描述
        this.updateDescription();
    }

    // 能力在更新时如何修改描述
    public void updateDescription() {
        if (this.go_for_stack) {
            return;
        }
//        ModHelper.log("[" + this.name + "]更新了描述");
        if (this.is_activated && this.scar_turn > 0) {
//            this.description = DESCRIPTIONS[0] + this.getScarDesc();
            this.description = this.getScarDesc();
            this.name = this.getScarName();
        } else {
            this.description = DESCRIPTIONS[0] + this.getBrandSubDesc();
        }
        this.curiosity = 0; // 更新完之后把这个数值设成0，下次若直接调用更新描述则是获取player上的好奇心数值
    }

    public void updateDescription(int curiosity) {
//        ModHelper.log("[" + this.name + "]更新了描述");
        this.curiosity = curiosity;
        this.updateDescription();
    }

    // 每回合被动效果 *实际是怪回合开始时调用，参考毒
    public void atStartOfTurn() {
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && !AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            // 触发被动
            this.usePassive();
            // 如果是已激活印记，使其创伤数-1，创伤小于等于0则移除此印记
            if (this.is_activated) {
                this.scar_turn--;
                this.updateDescription();
                this.tryRemove();
            }
        }
    }

    // 这个只会在怪的回合结束时调用，所以实现不了
//    @Override
//    public void atEndOfTurnPreEndTurnCards(boolean isPlayer) {
//        ModHelper.log("[" + this.name + "]回合结束 isPlayer=" + isPlayer);
//        super.atEndOfTurn(isPlayer);
//        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && !AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
//            // 触发被动
//            this.usePassive();
//            // 如果是已激活印记，使其创伤数-1，创伤小于等于0则移除此印记
//            if (this.is_activated) {
//                this.scar_turn--;
//                this.updateDescription();
//                this.tryRemove();
//            }
//        }
//    }

    // 火印记激活时，受到伤害变为2倍
    public float atDamageReceive(float damage, DamageInfo.DamageType type) {
//        ModHelper.log("[" + this.name + "]伤害变更计算");
        if (type == DamageInfo.DamageType.NORMAL && !this.owner.isPlayer && this.brand_type == BRAND_TYPE.FIRE) {
            if (BrandPower.isBurning(this)) {
//                ModHelper.log("[" + this.name + "]伤害变更计算 √");
                return damage * 2.0F;
            }
        }
//        ModHelper.log("[" + this.name + "]伤害变更计算 ×");
        return damage;
    }

    // 使用被动（因为创伤数要自动使用被动时处理，不能一起放在被动里）
    public void usePassive() {
        // 这个印记已经合并完成了，不能触发被动
        if (this.go_for_stack && this.stacked) {
            return;
        }
        // 烙印可以重叠成同一个能力，并且触发X次
        int use_cnt = 1;
        if (this.amount > 0) {
            use_cnt = this.amount;
        }
        for (int i = 0; i < use_cnt; i++) {
            switch (this.brand_type) {
                case LIGHTNING:
                    // 造成连锁闪电伤害
                    this.addToTop(new DamageChainLightningEnemiesAction(this.brandPassiveValue()));
                    this.addToTop(new VFXAction(new AtkChainLightningEffect()));
                    break;
                case FIRE:
                    // 增加X层灼烧
                    this.addToTop(new ApplyPowerAction(this.owner, AbstractDungeon.player, new BrandBurnPower(this.owner, this.brandPassiveValue())));
                    break;
                case ICE:
                    // 失去X点临时力量
                    this.addToTop(new ApplyPowerAction(this.owner, AbstractDungeon.player, new TempStrengthPower(this.owner, -this.brandPassiveValue()), -this.brandPassiveValue(), true, AbstractGameAction.AttackEffect.NONE));
                    break;
                default:
            }
            this.flash();
        }
    }

    // 使用激活
    public void useEvoke(BRAND_TYPE source_type) {

        // 常规激活效果
        switch (this.brand_type) {
            case LIGHTNING:
                // 造成更高的连锁闪电伤害
                this.addToTop(new DamageChainLightningEnemiesAction(this.brandEvokeValue()));
                this.addToTop(new VFXAction(new AtkChainLightningEffect()));
                break;
            case FIRE:  // 火印记激活本身不存在效果
                break;
            case ICE:
                // 失去X点临时力量
//                if (!this.owner.hasPower("Artifact")) {
//                    this.addToTop(new ApplyPowerAction(this.owner, AbstractDungeon.player, new GainStrengthPower(this.owner, this.brandEvokeValue()), this.brandEvokeValue(), true, AbstractGameAction.AttackEffect.NONE));
//                }
//                this.addToTop(new ApplyPowerAction(this.owner, AbstractDungeon.player, new StrengthPower(this.owner, -this.brandEvokeValue()), -this.brandEvokeValue(), true, AbstractGameAction.AttackEffect.NONE));
                this.addToTop(new ApplyPowerAction(this.owner, AbstractDungeon.player, new TempStrengthPower(this.owner, -this.brandEvokeValue()), -this.brandEvokeValue(), true, AbstractGameAction.AttackEffect.NONE));
                break;
            default:
                break;
        }

        this.flash();

        // 超载效果
        if (source_type != null && source_type != this.brand_type) {

            this.onPlayerOverloadEvoke();

            switch (this.brand_type) {
                case LIGHTNING:
                    if (source_type == BRAND_TYPE.FIRE) {
                        // 额外1次激活
                        // 造成更高的连锁闪电伤害
                        this.addToTop(new DamageChainLightningEnemiesAction(this.brandOverloadValue1()));
                        this.addToTop(new VFXAction(new AtkChainLightningEffect()));
                    } else {
                        // 抽2
                        this.addToTop(new DrawCardAction(this.brandOverloadValue2()));
                    }
                    break;
                case FIRE:
                    if (source_type == BRAND_TYPE.ICE) {
                        // X层虚弱
                        this.addToTop(new ApplyPowerAction(this.owner, AbstractDungeon.player, new WeakPower(this.owner, this.brandOverloadValue1(), false)));
                    } else {
                        // X层易伤
                        this.addToBot(new ApplyPowerAction(this.owner, AbstractDungeon.player, new VulnerablePower(this.owner, this.brandOverloadValue2(), false)));
                    }
                    break;
                case ICE:
                    if (source_type == BRAND_TYPE.LIGHTNING) {
                        // 获得X点格挡
                        this.addToTop(new GainBlockAction(AbstractDungeon.player, this.brandOverloadValue1()));
                        break;
                    } else {
                        // 添加X层灼烧
                        this.addToTop(new ApplyPowerAction(this.owner, AbstractDungeon.player, new BrandBurnPower(this.owner, this.brandOverloadValue2())));
                    }
                    break;
                default:
                    break;
            }

            this.flash();
        }
    }

    // 印记激活
    // source_type=造成本次激活的印记 *null=普通激活
    // scar_turn=附带烙印回合数
    public void evoke(BRAND_TYPE source_type) {
        this.evoke(source_type, 0);
    }

    public void evoke(BRAND_TYPE source_type, int scar_turn) {
//        ModHelper.log("[" + this.name + "]激活");
        if (this.is_activated) {
//            ModHelper.log("[" + this.name + "]激活 ×");
            return;
        }
        this.is_evoking = true;
//        ModHelper.log("[" + this.name + "]激活 √");
        this.is_activated = true;
        this.useEvoke(source_type);
        this.evokeEnd();
        // 烙印能力 + 附加烙印
        this.scar_turn = this.playerBrandScar() + scar_turn + this.baseBrandScar();
        // 相同烙印数的同属性印记叠加在一起
        if (this.scar_turn > 0) {
            this.amount = 1;
            this.go_for_stack = this.checkStack();
        }
        this.updateDescription();
        this.tryRemove();
    }

    // 激活结束 *添加到底部
    public void evokeEnd() {
        this.addToBot(new BrandEvokeEndAction(this));
    }

    // 检查是否有旧的烙印可以重叠
    public boolean checkStack() {
        ArrayList<BrandPower> powers = getBrandPowers((AbstractMonster) this.owner);
        for (BrandPower p : powers) {
            // 查找另一个相同的烙印
            if (p.amount > 0 && !p.ID.equals(this.ID) && p.is_activated && p.scar_turn == this.scar_turn && p.brand_type == this.brand_type && !p.go_for_stack) {
                ModHelper.addToBotAbstract(() -> {
                    p.stackPower(this.amount);
                    this.stacked = true;
                    p.updateDescription();
                });
                return true;
            }
        }
        return false;
    }

    // 尝试移除印记 *若已激活且创伤数为0，则移除 *添加到底部
    public void tryRemove() {
        if (this.is_activated && this.scar_turn <= 0 || this.go_for_stack) {
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

        // 因为每个印记只会执行一次，所以这里也可以去做生成印记时的额外处理
        ZzzWorldPower.onBrandPowerApplied();
    }

    @Override
    public void onRemove() {
        super.onRemove();
        ZzzWorldPower.onBrandPowerApplied();
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

    // 场上是否存在任意印记
    public static boolean hasAnyMonstersBrand(BRAND_TYPE type) {
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            if (!m.isDeadOrEscaped() && BrandPower.hasBrandPower(m, type)) {
                return true;
            }
        }
        return false;
    }

    // 获取该怪物的所有印记
    public static ArrayList<BrandPower> getBrandPowers(AbstractMonster m) {
        ArrayList<BrandPower> list = new ArrayList<>();
        for (AbstractPower p : m.powers) {
            if (BrandPower.isBrandPower(p)) {
                BrandPower p2 = (BrandPower) p;
                list.add(p2);
            }
        }
        return list;
    }

    // 获取该怪物的所有特定类型印记
    public static ArrayList<BrandPower> getBrandPowers(AbstractMonster m, BRAND_TYPE type) {
        ArrayList<BrandPower> list = new ArrayList<>();
        for (AbstractPower p : m.powers) {
            if (BrandPower.isBrandPower(p)) {
                BrandPower p2 = (BrandPower) p;
                if (p2.brand_type == type) {
                    list.add(p2);
                }
            }
        }
        return list;
    }

    // 获取该怪物的未激活的印记 *只会存在1个
    public static BrandPower getCanEvokeBrandPower(AbstractMonster m) {
        if (m == null) return null;
        for (AbstractPower p : m.powers) {
            if (BrandPower.isBrandPower(p)) {
                BrandPower p2 = (BrandPower) p;
                if (!p2.is_activated) return p2;
            }
        }
        return null;
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
        return p.ID.contains(POWER_ID);
    }

    // 是否印记能力
    public static boolean isBrandPower(AbstractPower p, BRAND_TYPE type) {
        if (isBrandPower(p)) {
            return ((BrandPower) p).brand_type == type;
        }
        return false;
    }

    // 是否处于激活火印记
    public static boolean isBurning(AbstractMonster m) {
        ArrayList<BrandPower> list = getBrandPowers(m, BRAND_TYPE.FIRE);
        for (BrandPower p : list) {
            if (p.is_evoking) {
                return true;
            }
        }
        return false;
    }

    public static boolean isBurning(BrandPower p) {
        return p.brand_type == BRAND_TYPE.FIRE && p.is_evoking;
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
                return String.format(DESCRIPTIONS[4], this.brandPassiveValue())
                        + String.format(DESCRIPTIONS[7], this.brandEvokeValue())
                        + String.format(DESCRIPTIONS[10], this.brandOverloadValue1())
                        + String.format(DESCRIPTIONS[11], this.brandOverloadValue2())
                        ;
            case FIRE:
                return String.format(DESCRIPTIONS[5], this.brandPassiveValue())
                        + DESCRIPTIONS[8]
                        + String.format(DESCRIPTIONS[12], this.brandOverloadValue1())
                        + String.format(DESCRIPTIONS[13], this.brandOverloadValue2())
                        ;
            case ICE:
                return String.format(DESCRIPTIONS[6], this.brandPassiveValue())
                        + String.format(DESCRIPTIONS[9], this.brandEvokeValue())
                        + String.format(DESCRIPTIONS[14], this.brandOverloadValue1())
                        + String.format(DESCRIPTIONS[15], this.brandOverloadValue2())
                        ;
            default:
                return "";
        }
    }

    // 获取烙印实际名字 闪电/火焰/冰冻
    private String getScarName() {
        switch (this.brand_type) {
            case LIGHTNING:
                return DESCRIPTIONS[17];
            case FIRE:
                return DESCRIPTIONS[18];
            case ICE:
                return DESCRIPTIONS[19];
            default:
                return "";
        }
    }

    // 获取变为烙印后的描述
    private String getScarDesc() {
        return String.format(DESCRIPTIONS[16], this.amount, this.scar_turn);
//        switch (this.brand_type) {
//            case LIGHTNING:
//                return String.format(DESCRIPTIONS[4], this.brandPassiveValue())
//                        + String.format(DESCRIPTIONS[16], this.scar_turn)
//                        ;
//            case FIRE:
//                return String.format(DESCRIPTIONS[5], this.brandPassiveValue())
//                        + String.format(DESCRIPTIONS[16], this.scar_turn)
//                        ;
//            case ICE:
//                return String.format(DESCRIPTIONS[6], this.brandPassiveValue())
//                        + String.format(DESCRIPTIONS[16], this.scar_turn)
//                        ;
//            default:
//                return "";
//        }
    }

    // 印记的被动最终数值
    private int brandPassiveValue() {
        return MathUtils.floor((
                BASE[this.brand_type.ordinal()].passive_value + this.playerCuriosity() * BASE[this.brand_type.ordinal()].curiosity_rate
        ) * this.playerCharge());
    }

    // 印记的激活最终数值
    private int brandEvokeValue() {
        return MathUtils.floor((
                BASE[this.brand_type.ordinal()].evoke_value + this.playerCuriosity() * BASE[this.brand_type.ordinal()].curiosity_rate
        ) * this.playerCharge());
    }

    // 超载1数值
    private int brandOverloadValue1() {
        return MathUtils.floor((
                (BASE[this.brand_type.ordinal()].overload_value1 + this.playerCuriosity() * BASE[this.brand_type.ordinal()].curiosity_rate) * this.playerOverWorld()
        ) * this.playerCharge());
    }

    // 超载2数值
    private int brandOverloadValue2() {
        if (this.brand_type == BRAND_TYPE.LIGHTNING) {
            return BASE[this.brand_type.ordinal()].overload_value2 * this.playerOverWorld();
        }
        return (BASE[this.brand_type.ordinal()].overload_value2 + this.playerCuriosity() * BASE[this.brand_type.ordinal()].curiosity_rate) * this.playerOverWorld();
    }

    // 好奇心的数值
    private int playerCuriosity() {
        if (this.curiosity != 0) {
            return this.curiosity;
        }
        AbstractPower p = AbstractDungeon.player.getPower(CuriosityPower.POWER_ID);
        if (p != null) {
            return p.amount;
        }
        return 0;
    }

    // 充电的数值
    private float playerCharge() {
        AbstractPower p = AbstractDungeon.player.getPower(ChargePower.POWER_ID);
        if (p != null) {
            return BASE[this.brand_type.ordinal()].charge_rate;
        }
        return 1;
    }

    // 超世形态 *超载2倍效果
    private int playerOverWorld() {
        AbstractPower p = AbstractDungeon.player.getPower(OverWorldPower.POWER_ID);
        if (p != null) {
            return 2;
        }
        return 1;
    }

    // 基础烙印层数
    private int baseBrandScar() {
        return BASE[this.brand_type.ordinal()].base_scar;
    }

    // 烙印的层数
    private int playerBrandScar() {
        AbstractPower p = AbstractDungeon.player.getPower(BrandScarPower.POWER_ID);
        if (p != null) {
            return p.amount;
        }
        return 0;
    }

    // 超载时触发一些能力的效果
    private void onPlayerOverloadEvoke() {
        OverloadFormPower p1 = (OverloadFormPower) AbstractDungeon.player.getPower(OverloadFormPower.POWER_ID);
        if (p1 != null) {
            p1.onOverloadEvoke();
        }

        for (AbstractCard card : AbstractDungeon.player.hand.group) {
            if (this.isInstanceOfOnOverloadEvoke(card)) {
                ((BaseOnOverloadEvoke) card).onOverloadEvoke();
            }
        }

        for (AbstractCard card : AbstractDungeon.player.discardPile.group) {
            if (this.isInstanceOfOnOverloadEvoke(card)) {
                ((BaseOnOverloadEvoke) card).onOverloadEvoke();
            }
        }

        for (AbstractCard card : AbstractDungeon.player.drawPile.group) {
            if (this.isInstanceOfOnOverloadEvoke(card)) {
                ((BaseOnOverloadEvoke) card).onOverloadEvoke();
            }
        }
    }

    private boolean isInstanceOfOnOverloadEvoke(AbstractCard card) {
        return card instanceof BaseOnOverloadEvoke;
    }

    // 更新所有印记的描述
    public static void updateAllBrandDesc() {
        updateAllBrandDesc(0);
    }

    public static void updateAllBrandDesc(int amount) {
        // 所有印记也要更新说明
        ArrayList<AbstractMonster> ms = AbstractDungeon.getMonsters().monsters;
        for (AbstractMonster m : ms) {
            if (m.isDeadOrEscaped()) continue;
            ArrayList<BrandPower> ps = BrandPower.getBrandPowers(m);
            for (BrandPower p : ps) {
                p.updateDescription(amount);
            }
        }
    }

    // 印记类型
    public enum BRAND_TYPE {
        LIGHTNING, FIRE, ICE
    }
}
