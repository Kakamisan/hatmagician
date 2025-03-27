package hatmagicianmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hatmagicianmod.powers.BrandPower;

public class BrandEvokeAction extends AbstractGameAction {
    private final AbstractMonster target;

    public BrandEvokeAction(AbstractMonster m) {
        this.target = m;
    }

    @Override
    public void update() {

        if (this.target != null && !this.target.isDeadOrEscaped()) {
            for(AbstractPower p: BrandPower.getBrandPowers(this.target)) {
                BrandPower p2 = (BrandPower) p;
                p2.evoke();
            }
        }

        this.isDone = true;
    }
}
