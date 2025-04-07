package hatmagicianmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hatmagicianmod.characters.MyCharacter;
import hatmagicianmod.helpers.ModHelper;
import hatmagicianmod.powers.TempStrengthPower;

public class BlearyEyedAction extends AbstractGameAction {
    private final int magicNumber;

    public BlearyEyedAction(int magicNumber) {
        this.duration = 0.0F;
        this.actionType = ActionType.WAIT;
        this.magicNumber = magicNumber;
    }

    public void update() {
        for (AbstractCard c : DrawCardAction.drawnCards) {
            if (c.hasTag(MyCharacter.PlayerCardTags.HAT_MAGICIAN_SLEEP)) {
                for (AbstractMonster m : ModHelper.getAliveMonsters()) {
                    this.addToBot(new ApplyPowerAction(m, AbstractDungeon.player, new TempStrengthPower(m, -this.magicNumber)));
                }
                this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new TempStrengthPower(AbstractDungeon.player, -this.magicNumber)));
                break;
            }
        }

        this.isDone = true;
    }
}
