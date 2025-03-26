package hatmagicianmod.effects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;

// 单个敌人雷劈特效+音效
public class AtkOneLightningEffect extends AbstractGameEffect {

    private final AbstractMonster target;

    public AtkOneLightningEffect(AbstractMonster m) {
        this.target = m;
    }

    public void update() {

        if (!this.target.isDeadOrEscaped()) {
            AbstractDungeon.actionManager.addToTop(new VFXAction(new LightningEffect(this.target.drawX, this.target.drawY), 0.0F));
            AbstractDungeon.actionManager.addToTop(new SFXAction("ORB_LIGHTNING_EVOKE"));
        }

        this.isDone = true;
    }

    public void render(SpriteBatch sb) {
    }

    public void dispose() {
    }
}
