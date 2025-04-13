package hatmagicianmod.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hatmagicianmod.actions.ApplyBrandPowerAction;
import hatmagicianmod.characters.MyCharacter;
import hatmagicianmod.helpers.ModHelper;
import hatmagicianmod.powers.BrandBurnPower;
import hatmagicianmod.powers.BrandPower;

public class PlayFire extends CustomCard {

    public static final String ID;
    private static final CardStrings CARD_STRINGS;
    private static final String IMG_PATH;
    private static final int COST = 0;
    private static final CardType TYPE = CardType.SKILL;

    static {
        String name = "PlayFire";
        ID = ModHelper.makeID(name);
        CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
        IMG_PATH = ModHelper.makeCardImgPath(TYPE, name);
    }

    public PlayFire() {
        super(ID, CARD_STRINGS.NAME, IMG_PATH, COST, CARD_STRINGS.DESCRIPTION, TYPE, ModHelper.color(), CardRarity.UNCOMMON, CardTarget.ENEMY);
        this.tags.add(MyCharacter.PlayerCardTags.HAT_MAGICIAN_BRAND);
        this.tags.add(MyCharacter.PlayerCardTags.HAT_MAGICIAN_BRAND_FIRE);
        this.magicNumber = this.baseMagicNumber = 4;
        this.cardsToPreview = new Burn();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(2);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ApplyBrandPowerAction(m, BrandPower.BRAND_TYPE.FIRE));
        this.addToBot(new ApplyPowerAction(m, p, new BrandBurnPower(m, this.magicNumber)));
        this.addToBot(new MakeTempCardInDiscardAction(new Burn(), 2));
    }
}
