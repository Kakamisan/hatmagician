package hatmagicianmod.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hatmagicianmod.characters.MyCharacter;
import hatmagicianmod.helpers.ModHelper;
import hatmagicianmod.powers.BrandPower;

public class TrickBox extends BaseBrandQueue {

    public static final String ID;
    private static final CardStrings CARD_STRINGS;
    private static final String IMG_PATH;
    private static final int COST = 2;
    private static final CardType TYPE = CardType.SKILL;

    static {
        String name = "TrickBox";
        ID = ModHelper.makeID(name);
        CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
        IMG_PATH = ModHelper.makeCardImgPath(TYPE, name);
    }

    public TrickBox() {
        super(ID, CARD_STRINGS.NAME, IMG_PATH, COST, CARD_STRINGS.DESCRIPTION, TYPE, ModHelper.color(), CardRarity.RARE, CardTarget.ENEMY);
        this.tags.add(MyCharacter.PlayerCardTags.HAT_MAGICIAN_BRAND);
        this.magicNumber = this.baseMagicNumber = 3;
        this.exhaust = true;
        this.scar_turn = 3;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
//            this.upgradeMagicNumber(1);
            this.exhaust = false;
            this.updateBrandQueueBaseDesc();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        super.use(p, m);
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

    @Override
    public String cardNormalDesc() {
        if (this.upgraded) {
            return CARD_STRINGS.UPGRADE_DESCRIPTION;
        }
        return CARD_STRINGS.DESCRIPTION;
    }

    @Override
    protected String cardCalcDesc() {
        if (this.upgraded) {
            return CARD_STRINGS.EXTENDED_DESCRIPTION[2];
        }
        return CARD_STRINGS.EXTENDED_DESCRIPTION[1];
    }
}
