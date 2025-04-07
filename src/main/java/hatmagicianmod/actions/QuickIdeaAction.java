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
import hatmagicianmod.cards.Melt;

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

                this.doExhaust(null);

                this.tickDuration();
            } else if (this.p.hand.size() == 1) {
                AbstractCard c = this.p.hand.getBottomCard();

                this.doExhaust(c);

                this.tickDuration();
            } else {
                AbstractDungeon.handCardSelectScreen.open(TEXT[0], 1, false);
                this.tickDuration();
            }
        } else {
            if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
                for (AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                    this.doExhaust(c);
                }

                AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
                AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
            }

            this.tickDuration();
        }
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("RecycleAction");
        TEXT = uiStrings.TEXT;
    }

    private void doExhaust(AbstractCard c) {

        if (c != null) {
//            this.p.hand.moveToDiscardPile(c);
//            c.triggerOnManualDiscard();
            this.p.hand.moveToExhaustPile(c);
        }

        this.addToBot(new DrawCardAction(this.draw_num));

        if (c != null) {
            if (Melt.checkCardTag(c)) {
                AbstractCard card = new MagicMark();
                this.addToTop(new MakeTempCardInHandAction(card));
            }
        }

    }
}
