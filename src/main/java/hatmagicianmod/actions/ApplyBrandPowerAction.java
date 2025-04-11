package hatmagicianmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hatmagicianmod.helpers.ModHelper;
import hatmagicianmod.powers.BrandPower;

public class ApplyBrandPowerAction extends AbstractGameAction {

    private final AbstractMonster target;
    private final BrandPower.BRAND_TYPE type;
    private final int scar_turn;

    public ApplyBrandPowerAction(AbstractMonster m, BrandPower.BRAND_TYPE type) {
        this(m, type, 0);
    }

    public ApplyBrandPowerAction(AbstractMonster m, BrandPower.BRAND_TYPE type, int scar_turn) {
        this.target = m;
        this.type = type;
        this.scar_turn = scar_turn;
    }

    @Override
    public void update() {
//        ModHelper.log("[添加印记]addToTop");
        this.addToTop(new ApplyPowerAction(this.target, AbstractDungeon.player, new BrandPower(this.target, this.type)));
        this.addToTop(new BrandEvokeAction(this.target, this.type, this.scar_turn));
        this.isDone = true;
    }
}
