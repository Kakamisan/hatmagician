package hatmagicianmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hatmagicianmod.powers.BrandPower;

// 使用所有印记的被动
public class BrandUsePassiveAllAction extends AbstractGameAction {

    public BrandUsePassiveAllAction() {}

    @Override
    public void update() {
        for(AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            if (!m.isDeadOrEscaped()) {
                for(BrandPower p: BrandPower.getBrandPowers(m)) {
                    p.usePassive();
                }
            }
        }
        this.isDone = true;
    }
}
