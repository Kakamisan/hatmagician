package hatmagicianmod.powers;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hatmagicianmod.actions.UpdatePowerDescAction;
import hatmagicianmod.helpers.ModHelper;

public class ZzzWorldPower extends AbstractPower {
    public static final String POWER_ID;
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;

    static {
        POWER_ID = ModHelper.makeID("ZzzWorldPower");
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }

    public ZzzWorldPower(AbstractCreature owner, int Amount) {
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

    // 能力在更新时如何修改描述
    public void updateDescription() {
//        this.description = String.format(DESCRIPTIONS[0], this.amount + this.iceBrandAdd());
        this.description = String.format(DESCRIPTIONS[0], this.amount);
    }

    public void atEndOfTurnPreEndTurnCards(boolean isPlayer) {
        this.addToBot(new GainBlockAction(this.owner, this.amount + this.iceBrandAdd()));
    }

    public int iceBrandAdd() {
        if(BrandPower.hasAnyMonstersBrand(BrandPower.BRAND_TYPE.ICE)){
            return this.amount;
        }
        return 0;
    }

    public static void onBrandPowerApplied() {
//        ZzzWorldPower p = (ZzzWorldPower)AbstractDungeon.player.getPower(POWER_ID);
//        if (p != null) {
//            AbstractDungeon.actionManager.addToBottom(new UpdatePowerDescAction(p));
//        }
    }

//    @Override
//    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
//        super.onApplyPower(power, target, source);
//        this.updateDescription();
//    }
}
