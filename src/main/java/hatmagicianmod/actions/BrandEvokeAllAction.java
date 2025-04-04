package hatmagicianmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hatmagicianmod.powers.BrandPower;

public class BrandEvokeAllAction extends AbstractGameAction {

    private final int scar_turn;

    public BrandEvokeAllAction() {
        this.scar_turn = 0;
    }
    public BrandEvokeAllAction(int scar_turn) {
        this.scar_turn = scar_turn;
    }

    @Override
    public void update() {
        for(AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            if (!m.isDeadOrEscaped()) {
                for(BrandPower p: BrandPower.getBrandPowers(m)) {
                    p.evoke(null, this.scar_turn);
                }
            }
        }
        this.isDone = true;
    }
}
