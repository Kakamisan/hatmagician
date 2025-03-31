package hatmagicianmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hatmagicianmod.powers.BrandPower;

public class BrandEvokeAction extends AbstractGameAction {
    private final AbstractMonster target;
    private final BrandPower.BRAND_TYPE source_type;
    private final int scar_turn;

    public BrandEvokeAction(AbstractMonster m, BrandPower.BRAND_TYPE source_type) {
        this.target = m;
        this.source_type = source_type;
        this.scar_turn = 0;
    }

    public BrandEvokeAction(AbstractMonster m, int scar_turn) {
        this.target = m;
        this.source_type = null;
        this.scar_turn = scar_turn;
    }

    public BrandEvokeAction(AbstractMonster m) {
        this.target = m;
        this.source_type = null;
        this.scar_turn = 0;
    }

    @Override
    public void update() {

        if (this.target != null && !this.target.isDeadOrEscaped()) {
            for(BrandPower p: BrandPower.getBrandPowers(this.target)) {
                p.evoke(this.source_type, this.scar_turn);
            }
        }

        this.isDone = true;
    }
}
