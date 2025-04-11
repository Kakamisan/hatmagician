package hatmagicianmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import hatmagicianmod.cards.Charge;
import hatmagicianmod.cards.Melt;

import java.util.ArrayList;
import java.util.Iterator;

public class RapidColdAction extends AbstractGameAction {
    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    private final AbstractPlayer p;
    private final ArrayList<AbstractCard> cache_cards = new ArrayList<>();
    private final boolean upgrade;

    public RapidColdAction(boolean upgrade) {
        this.p = AbstractDungeon.player;
        this.setValues(this.p, AbstractDungeon.player, this.amount);
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
        this.upgrade = upgrade;
    }


    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            if (AbstractDungeon.player.hand.size() == 10) {
                AbstractDungeon.player.createHandIsFullDialog();
                this.isDone = true;
            } else if (this.p.exhaustPile.isEmpty()) {
                this.isDone = true;
            } else if (this.p.exhaustPile.size() == 1) {
                if (!Charge.checkCardTag((AbstractCard)this.p.exhaustPile.group.get(0))) {
                    this.isDone = true;
                } else {
                    AbstractCard c = this.p.exhaustPile.getTopCard();
                    c.unfadeOut();

                    this.doToHand(c);

                    c.fadingOut = false;
                    this.isDone = true;
                }
            } else {
                for(AbstractCard c : this.p.exhaustPile.group) {
                    c.stopGlowing();
                    c.unhover();
                    c.unfadeOut();
                }

                Iterator<AbstractCard> c = this.p.exhaustPile.group.iterator();

                while(c.hasNext()) {
                    AbstractCard cache_c = (AbstractCard)c.next();
                    if (!Charge.checkCardTag(cache_c)) {
                        c.remove();
                        this.cache_cards.add(cache_c);
                    }
                }

                if (this.p.exhaustPile.isEmpty()) {
                    this.p.exhaustPile.group.addAll(this.cache_cards);
                    this.cache_cards.clear();
                    this.isDone = true;
                } else if (this.p.exhaustPile.size() == 1) {

                    AbstractCard c2 = this.p.exhaustPile.getTopCard();
                    this.doToHand(c2);
                    c2.fadingOut = false;

                    this.p.exhaustPile.group.addAll(this.cache_cards);
                    this.cache_cards.clear();
                    this.isDone = true;
                }
                else {
                    AbstractDungeon.gridSelectScreen.open(this.p.exhaustPile, 1, TEXT[0], false);
                    this.tickDuration();
                }
            }
        } else {
            if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
                for(AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {
                    this.doToHand(c);
                }

                AbstractDungeon.gridSelectScreen.selectedCards.clear();
                this.p.hand.refreshHandLayout();
                this.p.exhaustPile.group.addAll(this.cache_cards);
                this.cache_cards.clear();

                for(AbstractCard c : this.p.exhaustPile.group) {
                    c.unhover();
                    c.target_x = (float) CardGroup.DISCARD_PILE_X;
                    c.target_y = 0.0F;
                }
            }

            this.tickDuration();
        }
    }
    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("ExhumeAction");
        TEXT = uiStrings.TEXT;
    }

    private void doToHand(AbstractCard c) {
        this.p.hand.addToHand(c);
        if (AbstractDungeon.player.hasPower("Corruption") && c.type == AbstractCard.CardType.SKILL) {
            c.setCostForTurn(-9);
        }

        this.p.exhaustPile.removeCard(c);
//        if (this.upgrade && c.canUpgrade()) {
//            c.upgrade();
//        }
        if (this.upgrade) {
            c.setCostForTurn(-9);
        }

        c.unhover();
    }
}
