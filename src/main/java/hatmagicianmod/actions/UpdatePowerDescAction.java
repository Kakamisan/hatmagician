package hatmagicianmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class UpdatePowerDescAction extends AbstractGameAction {
    AbstractPower p;

    public UpdatePowerDescAction(AbstractPower p) {
        this.p = p;
    }

    @Override
    public void update() {
        this.p.updateDescription();
        this.isDone = true;
    }
}
