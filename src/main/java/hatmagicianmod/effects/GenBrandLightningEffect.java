package hatmagicianmod.effects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;

// 生成闪电印记，雷劈特效
public class GenBrandLightningEffect extends AbstractGameEffect {

    private final AbstractMonster target;

    public GenBrandLightningEffect(AbstractMonster m) {
        this.target = m;
    }

    public void update() {

        if (!this.target.isDeadOrEscaped()) {
            AbstractDungeon.actionManager.addToTop(new VFXAction(new LightningEffect(this.target.drawX, this.target.drawY), 0.0F));
        }

        this.isDone = true;
    }

    public void render(SpriteBatch sb) {
    }

    public void dispose() {
    }
}
