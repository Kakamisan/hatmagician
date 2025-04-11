package hatmagicianmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hatmagicianmod.helpers.ModHelper;

public class MyDamageAction extends AbstractGameAction {

    private final AbstractMonster m;
    private final AbstractPlayer p;
    private final int damage;
    private final DamageInfo.DamageType type;
    private final AttackEffect effect;

    public MyDamageAction(AbstractMonster m, AbstractPlayer p, int damage, DamageInfo.DamageType type, AttackEffect effect) {
        this.m = m;
        this.p = p;
        this.damage = damage;
        this.type = type;
        this.effect = effect;
    }

    @Override
    public void update() {
//        ModHelper.log("[My单体伤害]addToTop");
        this.addToTop(new DamageAction(this.m, new DamageInfo(this.p, this.damage, this.type), this.effect));
        this.isDone = true;
    }
}
