package hatmagicianmod.effects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;
import hatmagicianmod.powers.BrandPower;

// 所有闪电印记的敌人出现雷劈特效+音效
public class AtkChainLightningEffect extends AbstractGameEffect {
    public AtkChainLightningEffect() {}

    public void update() {

        for(AbstractMonster m3 : AbstractDungeon.getMonsters().monsters) {
            if (!m3.isDeadOrEscaped() && BrandPower.hasBrandPower(m3, BrandPower.BRAND_TYPE.LIGHTNING)) {
                AbstractDungeon.actionManager.addToTop(new VFXAction(new LightningEffect(m3.drawX, m3.drawY), 0.0F));
            }
        }

        AbstractDungeon.actionManager.addToTop(new SFXAction("ORB_LIGHTNING_EVOKE"));

        this.isDone = true;
    }

    public void render(SpriteBatch sb) {
    }

    public void dispose() {
    }
}
