package hatmagicianmod.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hatmagicianmod.characters.MyCharacter;
import hatmagicianmod.helpers.ModHelper;
import hatmagicianmod.powers.SweetDreamPower;

public class SweetDream extends CustomCard {

    public static final String ID;
    private static final CardStrings CARD_STRINGS;
    private static final String IMG_PATH;
    private static final int COST = 1;
    private static final CardType TYPE = CardType.POWER;

    static {
        String name = "SweetDream";
        ID = ModHelper.makeID(name);
        CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
        IMG_PATH = ModHelper.makeCardImgPath(TYPE, name);
    }

    public SweetDream() {
        super(ID, CARD_STRINGS.NAME, IMG_PATH, COST, CARD_STRINGS.DESCRIPTION, TYPE, ModHelper.color(), CardRarity.RARE, CardTarget.SELF);
        this.magicNumber = this.baseMagicNumber = 1;
        this.tags.add(MyCharacter.PlayerCardTags.HAT_MAGICIAN_SLEEP);
        this.cardsToPreview = new IsSleep();
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
        this.addToBot(new ApplyPowerAction(p, p, new SweetDreamPower(p, this.magicNumber)));
    }
}
