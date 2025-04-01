package hatmagicianmod.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hatmagicianmod.helpers.ModHelper;
import hatmagicianmod.powers.BrandPower;

public class Absorb extends CustomCard {

    public static final String ID;
    private static final CardStrings CARD_STRINGS;
    private static final String IMG_PATH;
    private static final int COST = 1;
    private static final CardType TYPE = CardType.ATTACK;

    static {
        String name = "Absorb";
        ID = ModHelper.makeID(name);
        CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
        IMG_PATH = ModHelper.makeCardImgPath(TYPE, name);
    }

    public Absorb() {
        super(ID, CARD_STRINGS.NAME, IMG_PATH, COST, CARD_STRINGS.DESCRIPTION, TYPE, ModHelper.color(), CardRarity.COMMON, CardTarget.ENEMY);
        this.baseDamage = 9;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(3);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int cnt = 1;
        if (BrandPower.hasBrandPower(m, BrandPower.BRAND_TYPE.LIGHTNING))
            cnt++;
        if (BrandPower.hasBrandPower(m, BrandPower.BRAND_TYPE.ICE))
            cnt++;
        if (BrandPower.hasBrandPower(m, BrandPower.BRAND_TYPE.FIRE))
            cnt++;
        this.addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        this.addToBot(new DrawCardAction(p, cnt));
    }
}
