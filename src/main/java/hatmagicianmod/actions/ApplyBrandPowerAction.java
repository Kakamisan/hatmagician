package hatmagicianmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hatmagicianmod.powers.BrandPower;

public class ApplyBrandPowerAction extends AbstractGameAction {

    private final AbstractMonster target;
    private final BrandPower.BRAND_TYPE type;

    public ApplyBrandPowerAction(AbstractMonster m, BrandPower.BRAND_TYPE type) {
        this.target = m;
        this.type = type;
    }

    @Override
    public void update() {
        this.addToTop(new ApplyPowerAction(this.target, AbstractDungeon.player, new BrandPower(this.target, this.type)));
        this.addToTop(new BrandEvokeAction(this.target));
        this.isDone = true;
    }
}
