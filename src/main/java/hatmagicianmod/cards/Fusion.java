package hatmagicianmod.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hatmagicianmod.helpers.ModHelper;
import hatmagicianmod.powers.BrandPower;

public class Fusion extends CustomCard {

    public static final String ID;
    private static final CardStrings CARD_STRINGS;
    private static final String IMG_PATH;
    private static final int COST = 1;
    private static final CardType TYPE = CardType.SKILL;

    static {
        String name = "Fusion";
        ID = ModHelper.makeID(name);
        CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
        IMG_PATH = ModHelper.makeCardImgPath(TYPE, name);
    }

    public Fusion() {
        super(ID, CARD_STRINGS.NAME, IMG_PATH, COST, CARD_STRINGS.DESCRIPTION, TYPE, ModHelper.color(), CardRarity.UNCOMMON, CardTarget.SELF);
        this.baseBlock = 7;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBlock(3);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int cnt = 0;
        if (BrandPower.hasAnyMonstersBrand(BrandPower.BRAND_TYPE.LIGHTNING))
            cnt++;
        if (BrandPower.hasAnyMonstersBrand(BrandPower.BRAND_TYPE.ICE))
            cnt++;
        if (BrandPower.hasAnyMonstersBrand(BrandPower.BRAND_TYPE.FIRE))
            cnt++;
//        for (AbstractMonster mo : AbstractDungeon.getMonsters().monsters) {
//            if (!mo.isDeadOrEscaped()) {
//                cnt = BrandPower.getBrandPowers(mo).size() + cnt;
//            }
//        }
        for (int i = 0; i < cnt; i++) {
            this.addToBot(new GainBlockAction(p, this.block));
        }
    }
}
