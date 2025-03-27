package hatmagicianmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import hatmagicianmod.helpers.ModHelper;
import hatmagicianmod.powers.BrandPower;

public class BrandEvokeEndAction extends AbstractGameAction {

    private final BrandPower target;

    public BrandEvokeEndAction(BrandPower p) {
        this.target = p;
    }

    @Override
    public void update() {
        ModHelper.log("[" + this.target.name + "]激活结束");
        this.target.is_evoking = false;
        this.isDone = true;
    }
}
