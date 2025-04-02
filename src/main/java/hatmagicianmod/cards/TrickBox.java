package hatmagicianmod.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hatmagicianmod.actions.ApplyBrandPowerAction;
import hatmagicianmod.helpers.ModHelper;
import hatmagicianmod.powers.BrandPower;

import java.util.ArrayList;

public class TrickBox extends CustomCard {

    public static final String ID;
    private static final CardStrings CARD_STRINGS;
    private static final String IMG_PATH;
    private static final int COST = 2;
    private static final CardType TYPE = CardType.SKILL;

    private boolean reverse = false;

    private static final ArrayList<BrandPower.BRAND_TYPE> brand_list;
    private static final ArrayList<BrandPower.BRAND_TYPE> reverse_brand_list;

    static {
        String name = "TrickBox";
        ID = ModHelper.makeID(name);
        CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
        IMG_PATH = ModHelper.makeCardImgPath(TYPE, name);
        brand_list = new ArrayList<>();
        brand_list.add(BrandPower.BRAND_TYPE.LIGHTNING);
        brand_list.add(BrandPower.BRAND_TYPE.FIRE);
        brand_list.add(BrandPower.BRAND_TYPE.ICE);
        brand_list.add(BrandPower.BRAND_TYPE.LIGHTNING);
        brand_list.add(BrandPower.BRAND_TYPE.FIRE);
        brand_list.add(BrandPower.BRAND_TYPE.ICE);
        reverse_brand_list = new ArrayList<>();
        reverse_brand_list.add(BrandPower.BRAND_TYPE.ICE);
        reverse_brand_list.add(BrandPower.BRAND_TYPE.FIRE);
        reverse_brand_list.add(BrandPower.BRAND_TYPE.LIGHTNING);
        reverse_brand_list.add(BrandPower.BRAND_TYPE.ICE);
        reverse_brand_list.add(BrandPower.BRAND_TYPE.FIRE);
        reverse_brand_list.add(BrandPower.BRAND_TYPE.LIGHTNING);
    }

    public TrickBox() {
        super(ID, CARD_STRINGS.NAME, IMG_PATH, COST, CARD_STRINGS.DESCRIPTION, TYPE, ModHelper.color(), CardRarity.RARE, CardTarget.ENEMY);
        this.magicNumber = this.baseMagicNumber = 2;
        this.exhaust = true;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(1);
        }
    }

    public void triggerOnCardPlayed(AbstractCard c) {
        this.reverse = !this.reverse;
        this.updateDesc();
    }

    public void updateDesc() {
        if (this.reverse) {
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
        } else {
            this.rawDescription = CARD_STRINGS.DESCRIPTION;
        }
        this.initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        BrandPower power = BrandPower.getCanEvokeBrandPower(m);
        if (power == null) {
            return;
        }
        ArrayList<BrandPower.BRAND_TYPE> gen_list = this.genList(power.brand_type);
        for (BrandPower.BRAND_TYPE e : gen_list) {
            switch (e) {
                case LIGHTNING:
//                    this.addToBot(new VFXAction(new GenBrandLightningEffect(m)));
                    this.addToBot(new ApplyBrandPowerAction(m, BrandPower.BRAND_TYPE.LIGHTNING));
                    break;
                case FIRE:
//                    AbstractDungeon.effectList.add(new FlashAtkImgEffect(m.hb.cX, m.hb.cY, AbstractGameAction.AttackEffect.FIRE));
                    this.addToBot(new ApplyBrandPowerAction(m, BrandPower.BRAND_TYPE.FIRE));
                    break;
                case ICE:
                    this.addToBot(new ApplyBrandPowerAction(m, BrandPower.BRAND_TYPE.ICE));
                    break;
            }
        }
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

    public void calculateCardDamage(AbstractMonster mo) {
        super.calculateCardDamage(mo);
        BrandPower power = BrandPower.getCanEvokeBrandPower(mo);
        if (power == null) {
//            this.updateDesc();
            return;
        }
        ArrayList<BrandPower.BRAND_TYPE> gen_list = this.genList(power.brand_type);
        this.rawDescription = CARD_STRINGS.EXTENDED_DESCRIPTION[1];
        for (int i = 0; i < gen_list.size(); i++) {
            switch (gen_list.get(i)) {
                case LIGHTNING:
                    this.rawDescription = this.rawDescription + CARD_STRINGS.EXTENDED_DESCRIPTION[2];
                    break;
                case FIRE:
                    this.rawDescription = this.rawDescription + CARD_STRINGS.EXTENDED_DESCRIPTION[3];
                    break;
                case ICE:
                    this.rawDescription = this.rawDescription + CARD_STRINGS.EXTENDED_DESCRIPTION[4];
                    break;
            }
            if (i < gen_list.size() - 1) {
                this.rawDescription = this.rawDescription + CARD_STRINGS.EXTENDED_DESCRIPTION[6];
            }
        }
        this.rawDescription = this.rawDescription + CARD_STRINGS.EXTENDED_DESCRIPTION[5];
        this.initializeDescription();
    }

    @Override
    public void onMoveToDiscard() {
        this.updateDesc();
    }

    // 当前效果下会生成的印记类型列表
    public ArrayList<BrandPower.BRAND_TYPE> genList(BrandPower.BRAND_TYPE brand_type) {
        ArrayList<BrandPower.BRAND_TYPE> ret_list = new ArrayList<>();
        ArrayList<BrandPower.BRAND_TYPE> in_list = brand_list;
        if (this.reverse) {
            in_list = reverse_brand_list;
        }
        int start = 0;
        for (int i = 0; i < in_list.size(); i++) {
            if (in_list.get(i) == brand_type) {
                start = i;
                break;
            }
        }
        for (int i = 0; i < this.magicNumber; i++) {
            ret_list.add(in_list.get(i + start + 1));
        }
        return ret_list;
    }
}
