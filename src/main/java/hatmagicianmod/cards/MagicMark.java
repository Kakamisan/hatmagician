package hatmagicianmod.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hatmagicianmod.actions.BrandEvokeAction;
import hatmagicianmod.helpers.ModHelper;
import hatmagicianmod.powers.BrandPower;

public class MagicMark extends BaseBrandAtk {

    public static final String ID;
    private static final CardStrings CARD_STRINGS;
    private static final String IMG_PATH;
    private static final int COST = 0;
    private static final CardType TYPE = CardType.SKILL;

    static {
        String name = "MagicMark";
        ID = ModHelper.makeID(name);
        CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
        IMG_PATH = ModHelper.makeCardImgPath(TYPE, name);
    }

    public MagicMark() {
        super(ID, CARD_STRINGS.NAME, IMG_PATH, COST, CARD_STRINGS.DESCRIPTION, TYPE, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.ENEMY);
        this.magicNumber = this.baseMagicNumber = 3;
        this.exhaust = true;
        this.isEthereal = true;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new BrandEvokeAction(m, this.magicNumber));
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        boolean canUse = super.canUse(p, m);
        if (canUse) {
            BrandPower power = BrandPower.getCanEvokeBrandPower(m);
            if (power != null) {
                return true;
            }
            this.cantUseMessage = CARD_STRINGS.EXTENDED_DESCRIPTION[0];
        }
        return false;
    }
}
