package hatmagicianmod.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hatmagicianmod.characters.MyCharacter;
import hatmagicianmod.helpers.ModHelper;
import hatmagicianmod.powers.TempStrengthPower;
import hatmagicianmod.powers.YawnPower;

public class Yawn extends CustomCard {

    public static final String ID;
    private static final CardStrings CARD_STRINGS;
    private static final String IMG_PATH;
    private static final int COST = 0;
    private static final CardType TYPE = CardType.SKILL;

    static {
        String name = "Yawn";
        ID = ModHelper.makeID(name);
        CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
        IMG_PATH = ModHelper.makeCardImgPath(TYPE, name);
    }

    public Yawn() {
        super(ID, CARD_STRINGS.NAME, IMG_PATH, COST, CARD_STRINGS.DESCRIPTION, TYPE, ModHelper.color(), CardRarity.UNCOMMON, CardTarget.SELF);
        this.baseBlock = 3;
        this.magicNumber = this.baseMagicNumber = 2;
        this.tags.add(MyCharacter.PlayerCardTags.HAT_MAGICIAN_SLEEP);
        this.exhaust = true;
        this.cardsToPreview = new IsSleep();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBlock(2);
            this.upgradeMagicNumber(1);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new GainBlockAction(p, this.block));
//        AbstractPower power = p.getPower(TempStrengthPower.POWER_ID);
//        if (power != null && power.amount < 0) {
//            this.addToBot(new HealAction(p, p, -power.amount));
//        }
        this.addToBot(new ApplyPowerAction(p, p, new YawnPower(p, this.magicNumber)));
    }
}
