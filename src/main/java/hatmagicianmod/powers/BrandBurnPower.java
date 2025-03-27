package hatmagicianmod.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hatmagicianmod.helpers.ModHelper;

public class BrandBurnPower extends AbstractPower {
    public static final String POWER_ID;
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;

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

        this.priority = 4;

        // 添加一大一小两张能力图
        String path128 = "HatMagicianModRes/img/powers/Example84.png";
        String path48 = "HatMagicianModRes/img/powers/Example32.png";
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 32, 32);

        // 首次添加能力更新描述
        this.updateDescription();
    }

    // 能力在更新时如何修改描述
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], this.amount);
    }

    // 火印记激活时，受到伤害增加对应数值
    public float atDamageReceive(float damage, DamageInfo.DamageType type) {
        ModHelper.log("[" + this.name + "]伤害变更计算");
        if (type == DamageInfo.DamageType.NORMAL && !this.owner.isPlayer) {
            AbstractMonster m = (AbstractMonster) this.owner;
            if (BrandPower.isBurning(m)) {
                ModHelper.log("[" + this.name + "]伤害变更计算 √");
                return damage + this.amount;
            }
        }
        ModHelper.log("[" + this.name + "]伤害变更计算 ×");
        return damage;
    }

}
