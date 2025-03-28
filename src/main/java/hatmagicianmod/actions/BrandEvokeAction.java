package hatmagicianmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hatmagicianmod.powers.BrandPower;

public class BrandEvokeAction extends AbstractGameAction {
    private final AbstractMonster target;
    private final BrandPower.BRAND_TYPE source_type;

    public BrandEvokeAction(AbstractMonster m, BrandPower.BRAND_TYPE source_type) {
        this.target = m;
        this.source_type = source_type;
    }

    @Override
    public void update() {

        if (this.target != null && !this.target.isDeadOrEscaped()) {
            for(BrandPower p: BrandPower.getBrandPowers(this.target)) {
                p.evoke(this.source_type);
            }
        }

        this.isDone = true;
    }
}
