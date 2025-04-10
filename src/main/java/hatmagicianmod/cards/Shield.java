package hatmagicianmod.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hatmagicianmod.actions.ApplyBrandPowerAction;
import hatmagicianmod.characters.MyCharacter;
import hatmagicianmod.helpers.ModHelper;
import hatmagicianmod.powers.BrandPower;

public class Shield extends CustomCard {

    public static final String ID;
    private static final CardStrings CARD_STRINGS;
    private static final String IMG_PATH;
    private static final int COST = 1;
    private static final CardType TYPE = CardType.SKILL;

    static {
        String name = "Shield";
        ID = ModHelper.makeID(name);
        CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
        IMG_PATH = ModHelper.makeCardImgPath(TYPE, name);
    }

    public Shield() {
        super(ID, CARD_STRINGS.NAME, IMG_PATH, COST, CARD_STRINGS.DESCRIPTION, TYPE, ModHelper.color(), CardRarity.COMMON, CardTarget.SELF);
        this.baseBlock = 7;
        this.tags.add(MyCharacter.PlayerCardTags.HAT_MAGICIAN_SLEEP);
        this.tags.add(MyCharacter.PlayerCardTags.HAT_MAGICIAN_BRAND);
        this.tags.add(MyCharacter.PlayerCardTags.HAT_MAGICIAN_BRAND_ICE);
        this.magicNumber = this.baseMagicNumber = 1;
        this.cardsToPreview = new IsSleep();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBlock(3);
        }
    }

//    @Override
//    protected void applyPowersToBlock() {
//        this.isBlockModified = false;
//        float tmp = (float) this.baseBlock;
//
//        for (AbstractPower p : AbstractDungeon.player.powers) {
//            tmp = p.modifyBlock(tmp, this);
//        }
//
//        for (AbstractPower p : AbstractDungeon.player.powers) {
//            tmp = p.modifyBlockLast(tmp);
//        }
//
//        tmp += this.getVariantBlock();
//
//        if (this.baseBlock != MathUtils.floor(tmp)) {
//            this.isBlockModified = true;
//        }
//
//        if (tmp < 0.0F) {
//            tmp = 0.0F;
//        }
//
//        this.block = MathUtils.floor(tmp);
//        this.initializeDescription();
//    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new GainBlockAction(p, this.block));
        for (int i = 0; i < this.magicNumber; i++) {
            AbstractMonster mo = AbstractDungeon.getRandomMonster();
            if (mo != null) {
                this.addToBot(new ApplyBrandPowerAction(mo, BrandPower.BRAND_TYPE.ICE));
            }
        }
    }

//    private int getVariantBlock() {
//        int tmp = 0;
//        for (AbstractMonster m : ModHelper.getAliveMonsters()) {
//            if (BrandPower.hasBrandPower(m, BrandPower.BRAND_TYPE.ICE)) {
//                tmp += this.magicNumber;
//            }
//        }
//        return tmp;
//    }

}
