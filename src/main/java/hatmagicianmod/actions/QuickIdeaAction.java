package hatmagicianmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import hatmagicianmod.cards.MagicMark;
import hatmagicianmod.characters.MyCharacter;

public class QuickIdeaAction extends AbstractGameAction {
    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    private final AbstractPlayer p;

    private final int draw_num;

    public QuickIdeaAction(int draw_num) {
        this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
        this.p = AbstractDungeon.player;
        this.duration = Settings.ACTION_DUR_FAST;
        this.draw_num = draw_num;
    }

    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            if (this.p.hand.isEmpty()) {

                this.doDiscard(null);

                this.tickDuration();
            } else if (this.p.hand.size() == 1) {
                AbstractCard c = this.p.hand.getBottomCard();

                this.doDiscard(c);

                this.tickDuration();
            } else {
                AbstractDungeon.handCardSelectScreen.open(TEXT[0], 1, false);
                this.tickDuration();
            }
        } else {
            if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
                for (AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                    this.doDiscard(c);
                }

                AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
                AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
            }

            this.tickDuration();
        }
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("DiscardAction");
        TEXT = uiStrings.TEXT;
    }

    private void doDiscard(AbstractCard c) {

        if (c != null) {
            this.p.hand.moveToDiscardPile(c);
            c.triggerOnManualDiscard();
        }

        this.addToBot(new DrawCardAction(this.draw_num));

        if (c != null) {
            if (!c.tags.contains(MyCharacter.PlayerCardTags.HAT_MAGICIAN_BRAND)) {
                AbstractCard card = new MagicMark();
                this.addToTop(new MakeTempCardInHandAction(card));
            }
        }

    }
}
