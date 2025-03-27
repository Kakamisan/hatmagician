package hatmagicianmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hatmagicianmod.powers.BrandPower;

public class BrandEvokeAllAction extends AbstractGameAction {

    public BrandEvokeAllAction() {}

    @Override
    public void update() {
        for(AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            if (!m.isDeadOrEscaped()) {
                for(AbstractPower p: BrandPower.getBrandPowers(m)) {
                    BrandPower p2 = (BrandPower) p;
                    p2.evoke();
                }
            }
        }
        this.isDone = true;
    }
}
