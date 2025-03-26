package hatmagicianmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hatmagicianmod.powers.BrandPower;

// 对所有闪电印记敌人造成伤害
public class DamageChainLightningEnemiesAction extends AbstractGameAction {

    private final int damage;

    public DamageChainLightningEnemiesAction(int damage) {
        this.damage = damage;
    }

    @Override
    public void update() {
        for(AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            if (!m.isDeadOrEscaped() && BrandPower.hasBrandPower(m, BrandPower.BRAND_TYPE.LIGHTNING)) {
                this.addToTop(new DamageAction(m, new DamageInfo(AbstractDungeon.player, this.damage, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.NONE));
            }
        }
        this.isDone = true;
    }
}
