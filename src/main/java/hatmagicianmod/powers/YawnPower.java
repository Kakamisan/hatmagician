package hatmagicianmod.powers;

import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hatmagicianmod.helpers.ModHelper;

public class YawnPower extends AbstractPower {
    public static final String POWER_ID;
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;

    static {
        POWER_ID = ModHelper.makeID("YawnPower");
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }

    public YawnPower(AbstractCreature owner, int Amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = PowerType.BUFF;

        // 如果需要不能叠加的能力，只需将上面的Amount参数删掉，并把下面的Amount改成-1就行
        this.amount = Amount;

//        // 添加一大一小两张能力图
//        String path128 = "HatMagicianModRes/img/powers/Example84.png";
//        String path48 = "HatMagicianModRes/img/powers/Example32.png";
//        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 84, 84);
//        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 32, 32);

        this.loadRegion("blur");

        // 首次添加能力更新描述
        this.updateDescription();
    }

    public void updateDescription() {
//        this.description = String.format(DESCRIPTIONS[0], this.amount);
        this.description = DESCRIPTIONS[0];
    }

    @Override
    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
        super.onApplyPower(power, target, source);
        if (power.amount < 0 && power.owner.isPlayer) {
//            if (power.ID.equals("Strength") || power.ID.equals(TempStrengthPower.POWER_ID)) {
            if (power.ID.equals(TempStrengthPower.POWER_ID)) {
                this.flash();
                this.addToTop(new HealAction(this.owner, this.owner, -power.amount));
            }
        }
    }

    @Override
    public void atEndOfRound() {
        super.atEndOfRound();
        if (this.amount == 0) {
            this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        } else {
            this.addToBot(new ReducePowerAction(this.owner, this.owner, this, 1));
        }
    }
}
