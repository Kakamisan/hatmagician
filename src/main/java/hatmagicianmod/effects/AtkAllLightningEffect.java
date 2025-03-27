package hatmagicianmod.effects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;

// 所有敌人雷劈特效+音效
public class AtkAllLightningEffect extends AbstractGameEffect {
    public AtkAllLightningEffect() {}

    public void update() {

        AbstractDungeon.actionManager.addToTop(new SFXAction("ORB_LIGHTNING_EVOKE"));

        for(AbstractMonster m3 : AbstractDungeon.getMonsters().monsters) {
            if (!m3.isDeadOrEscaped()) {
                AbstractDungeon.actionManager.addToTop(new VFXAction(new LightningEffect(m3.drawX, m3.drawY), 0.0F));
            }
        }

        this.isDone = true;
    }

    public void render(SpriteBatch sb) {
    }

    public void dispose() {
    }
}
