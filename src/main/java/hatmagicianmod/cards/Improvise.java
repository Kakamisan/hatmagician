package hatmagicianmod.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hatmagicianmod.characters.MyCharacter;
import hatmagicianmod.helpers.ModHelper;

import java.util.ArrayList;

public class Improvise extends CustomCard {

    public static final String ID;
    private static final CardStrings CARD_STRINGS;
    private static final String IMG_PATH;
    private static final int COST = 1;
    private static final CardType TYPE = CardType.SKILL;

    static {
        String name = "Improvise";
        ID = ModHelper.makeID(name);
        CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
        IMG_PATH = ModHelper.makeCardImgPath(TYPE, name);
    }

    public Improvise() {
        super(ID, CARD_STRINGS.NAME, IMG_PATH, COST, CARD_STRINGS.DESCRIPTION, TYPE, ModHelper.color(), CardRarity.UNCOMMON, CardTarget.SELF);
        this.magicNumber = this.baseMagicNumber = 1;
        this.exhaust = true;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBaseCost(0);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractCard c = returnTrulyRandomCardInCombat(MyCharacter.PlayerCardTags.HAT_MAGICIAN_BRAND).makeCopy();
        c.setCostForTurn(-99);
        this.addToBot(new MakeTempCardInHandAction(c, true));
    }

    public static AbstractCard returnTrulyRandomCardInCombat(CardTags tag) {
        ArrayList<AbstractCard> list = new ArrayList<>();

        for(AbstractCard c : AbstractDungeon.srcCommonCardPool.group) {
            if (c.tags.contains(tag) && !c.hasTag(CardTags.HEALING)) {
                list.add(c);
            }
        }

        for(AbstractCard c : AbstractDungeon.srcUncommonCardPool.group) {
            if (c.tags.contains(tag) && !c.hasTag(CardTags.HEALING)) {
                list.add(c);
            }
        }

        for(AbstractCard c : AbstractDungeon.srcRareCardPool.group) {
            if (c.tags.contains(tag) && !c.hasTag(CardTags.HEALING)) {
                list.add(c);
            }
        }

        return (AbstractCard)list.get(ModHelper.cardRand(list.size()));
    }
}
