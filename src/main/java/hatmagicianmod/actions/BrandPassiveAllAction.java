package hatmagicianmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hatmagicianmod.powers.BrandPower;

// 使用所有印记的被动
public class BrandPassiveAllAction extends AbstractGameAction {
    public BrandPassiveAllAction() {

    }

    @Override
    public void update() {
        for(AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            if (!m.isDeadOrEscaped()) {
                for(AbstractPower p: BrandPower.getBrandPowers(m)) {
                    p.atStartOfTurn();
                }
            }
        }
        this.isDone = true;
    }
}
