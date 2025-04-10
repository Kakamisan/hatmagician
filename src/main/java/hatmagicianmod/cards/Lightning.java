package hatmagicianmod.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hatmagicianmod.actions.ApplyBrandPowerAction;
import hatmagicianmod.characters.MyCharacter;
import hatmagicianmod.effects.GenBrandLightningEffect;
import hatmagicianmod.helpers.ModHelper;
import hatmagicianmod.powers.BrandPower;

public class Lightning extends CustomCard {

    public static final String ID;
    private static final CardStrings CARD_STRINGS;
    private static final String IMG_PATH;
    private static final int COST = 1;
    private static final CardType TYPE = CardType.SKILL;

    private final boolean sleep;

    static {
        String name = "Lightning";
        ID = ModHelper.makeID(name);
        CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
        IMG_PATH = ModHelper.makeCardImgPath(TYPE, name);
    }

    public Lightning() {
        super(ID, CARD_STRINGS.NAME, IMG_PATH, COST, CARD_STRINGS.DESCRIPTION, TYPE, ModHelper.color(), CardRarity.BASIC, CardTarget.ENEMY);
        this.tags.add(MyCharacter.PlayerCardTags.HAT_MAGICIAN_BRAND);
        this.tags.add(MyCharacter.PlayerCardTags.HAT_MAGICIAN_BRAND_LIGHTNING);
        this.sleep = false;
    }

    public Lightning(boolean upgraded) {
        super(ID, CARD_STRINGS.NAME, IMG_PATH, COST, CARD_STRINGS.UPGRADE_DESCRIPTION, TYPE, ModHelper.color(), CardRarity.BASIC, CardTarget.ENEMY);
        this.tags.add(MyCharacter.PlayerCardTags.HAT_MAGICIAN_BRAND);
        this.tags.add(MyCharacter.PlayerCardTags.HAT_MAGICIAN_BRAND_LIGHTNING);
        this.tags.add(MyCharacter.PlayerCardTags.HAT_MAGICIAN_SLEEP);
        this.cardsToPreview = new IsSleep();
        this.sleep = true;
        this.exhaust = true;
        this.isEthereal = true;
        if (upgraded) {
            this.upgrade();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        if (this.sleep) {
            return new Lightning(this.upgraded);
        } else {
            return new Lightning();
        }
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
        this.addToBot(new VFXAction(new GenBrandLightningEffect(m)));
        this.addToBot(new ApplyBrandPowerAction(m, BrandPower.BRAND_TYPE.LIGHTNING));
    }
}
