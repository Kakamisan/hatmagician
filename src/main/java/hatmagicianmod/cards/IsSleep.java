package hatmagicianmod.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hatmagicianmod.characters.MyCharacter;
import hatmagicianmod.helpers.ModHelper;
import hatmagicianmod.powers.TempStrengthPower;

public class IsSleep extends CustomCard {

    public static final String ID;
    private static final CardStrings CARD_STRINGS;
    private static final String IMG_PATH;
    private static final int COST = 0;
    private static final CardType TYPE = CardType.SKILL;

    static {
        String name = "IsSleep";
        ID = ModHelper.makeID(name);
        CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
        IMG_PATH = ModHelper.makeCardImgPath(TYPE, name);
    }

    public IsSleep() {
        super(ID, CARD_STRINGS.NAME, IMG_PATH, COST, CARD_STRINGS.DESCRIPTION, TYPE, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.SELF);
        this.magicNumber = this.baseMagicNumber = 1;
        this.isEthereal = true;
        this.exhaust = true;
        this.tags.add(MyCharacter.PlayerCardTags.HAT_MAGICIAN_SLEEP);
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ApplyPowerAction(p, p, new TempStrengthPower(p, -this.magicNumber)));
        this.addToBot(new HealAction(p, p, this.magicNumber));
    }
}
