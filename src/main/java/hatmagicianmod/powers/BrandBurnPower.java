package hatmagicianmod.powers;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hatmagicianmod.helpers.ModHelper;

public class BrandBurnPower extends AbstractPower {
    public static final String POWER_ID;
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;

    private boolean is_damaging = false;
    private boolean go_for_reduce = false;

    static {
        POWER_ID = ModHelper.makeID("BrandBurnPower");
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }

    public BrandBurnPower(AbstractCreature owner, int Amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = PowerType.DEBUFF;

        // 如果需要不能叠加的能力，只需将上面的Amount参数删掉，并把下面的Amount改成-1就行
        this.amount = Amount;

        this.priority = 6;

//        // 添加一大一小两张能力图
//        String path128 = "HatMagicianModRes/img/powers/Example84.png";
//        String path48 = "HatMagicianModRes/img/powers/Example32.png";
//        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 84, 84);
//        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 32, 32);

        this.loadRegion("flameBarrier");

        // 首次添加能力更新描述
        this.updateDescription();
    }

    // 能力在更新时如何修改描述
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], this.amount);
    }

    // 火印记激活时，受到伤害增加对应数值
    public float atDamageReceive(float damage, DamageInfo.DamageType type) {
//        ModHelper.log("[" + this.name + "]伤害变更计算");
        this.is_damaging = false;
        if (type == DamageInfo.DamageType.NORMAL && !this.owner.isPlayer) {
            AbstractMonster m = (AbstractMonster) this.owner;
            if (BrandPower.isBurning(m)) {
//                ModHelper.log("[" + this.name + "]伤害变更计算 √");
                this.is_damaging = true;
                return damage + this.amount;
            }
        }
//        ModHelper.log("[" + this.name + "]伤害变更计算 ×");
        return damage;
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {

        if (!this.go_for_reduce && this.is_damaging && info.output > 0) {
//            ModHelper.log("灼烧准备减半层数");
            this.go_for_reduce = true;
            this.is_damaging = false;
            ModHelper.addToBotAbstract(() -> {
//                ModHelper.log("灼烧减半层数了");
                this.go_for_reduce = false;
                this.addToTop(new ReducePowerAction(this.owner, this.owner, this, Math.round(this.amount / 2.0F)));
            });
        }

        return super.onAttacked(info, damageAmount);
    }
}
