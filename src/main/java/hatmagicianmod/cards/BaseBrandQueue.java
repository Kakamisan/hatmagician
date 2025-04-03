package hatmagicianmod.cards;

import basemod.AutoAdd;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hatmagicianmod.actions.ApplyBrandPowerAction;
import hatmagicianmod.characters.MyCharacter;
import hatmagicianmod.helpers.ModHelper;
import hatmagicianmod.powers.BrandPower;

import java.util.ArrayList;

// 序列牌基础类
@AutoAdd.Ignore
public class BaseBrandQueue extends BaseBrandAtk {

    protected static final CardStrings BASE_CARD_STRINGS;

    // 是否逆转
    protected boolean reverse = false;

    // 两个固定序列
    protected static final ArrayList<BrandPower.BRAND_TYPE> BRAND_LIST;
    protected static final ArrayList<BrandPower.BRAND_TYPE> REVERSE_BRAND_LIST;

    static {
        BASE_CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ModHelper.makeID("BaseBrandQueue"));
        BRAND_LIST = new ArrayList<>();
        BRAND_LIST.add(BrandPower.BRAND_TYPE.LIGHTNING);
        BRAND_LIST.add(BrandPower.BRAND_TYPE.FIRE);
        BRAND_LIST.add(BrandPower.BRAND_TYPE.ICE);
        BRAND_LIST.add(BrandPower.BRAND_TYPE.LIGHTNING);
        BRAND_LIST.add(BrandPower.BRAND_TYPE.FIRE);
        BRAND_LIST.add(BrandPower.BRAND_TYPE.ICE);
        REVERSE_BRAND_LIST = new ArrayList<>();
        REVERSE_BRAND_LIST.add(BrandPower.BRAND_TYPE.ICE);
        REVERSE_BRAND_LIST.add(BrandPower.BRAND_TYPE.FIRE);
        REVERSE_BRAND_LIST.add(BrandPower.BRAND_TYPE.LIGHTNING);
        REVERSE_BRAND_LIST.add(BrandPower.BRAND_TYPE.ICE);
        REVERSE_BRAND_LIST.add(BrandPower.BRAND_TYPE.FIRE);
        REVERSE_BRAND_LIST.add(BrandPower.BRAND_TYPE.LIGHTNING);
    }

    public BaseBrandQueue(String id, String name, String img, int cost, String rawDescription, AbstractCard.CardType type, AbstractCard.CardColor color, AbstractCard.CardRarity rarity, AbstractCard.CardTarget target) {
        super(id, name, img, cost, rawDescription + BASE_CARD_STRINGS.DESCRIPTION, type, color, rarity, target);
        this.tags.add(MyCharacter.PlayerCardTags.HAT_MAGICIAN_BRAND);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 打出时按序列给予印记
        BrandPower power = BrandPower.getCanEvokeBrandPower(m);
        if (power == null) {
            return;
        }
        ArrayList<BrandPower.BRAND_TYPE> gen_list = this.genBrandQueueList(power.brand_type);
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
    public void triggerOnCardPlayed(AbstractCard c) {
        // 打出非印记牌，逆转序列
        if (c.tags.contains(MyCharacter.PlayerCardTags.HAT_MAGICIAN_BRAND)) {
            return;
        }
        this.reverse = !this.reverse;
        this.updateBrandQueueBaseDesc();
    }

    // 更新描述，并给描述加上 “以序列/逆序列给予M个印记。”
    protected void updateBrandQueueBaseDesc() {
        if (this.reverse) {
            this.rawDescription = this.cardNormalDesc() + BASE_CARD_STRINGS.UPGRADE_DESCRIPTION;
        } else {
            this.rawDescription = this.cardNormalDesc() + BASE_CARD_STRINGS.DESCRIPTION;
        }
        this.initializeDescription();
    }

    // 更新描述，并给描述加上 “给予A、B、C印记。”
    protected void updateBrandQueueCalcDesc(BrandPower power) {
        ArrayList<BrandPower.BRAND_TYPE> gen_list = this.genBrandQueueList(power.brand_type);
        this.rawDescription = this.cardCalcDesc();
        this.rawDescription = this.rawDescription + BASE_CARD_STRINGS.EXTENDED_DESCRIPTION[0];
        for (int i = 0; i < gen_list.size(); i++) {
            switch (gen_list.get(i)) {
                case LIGHTNING:
                    this.rawDescription = this.rawDescription + BASE_CARD_STRINGS.EXTENDED_DESCRIPTION[1];
                    break;
                case FIRE:
                    this.rawDescription = this.rawDescription + BASE_CARD_STRINGS.EXTENDED_DESCRIPTION[2];
                    break;
                case ICE:
                    this.rawDescription = this.rawDescription + BASE_CARD_STRINGS.EXTENDED_DESCRIPTION[3];
                    break;
            }
            if (i < gen_list.size() - 1) {
                this.rawDescription = this.rawDescription + BASE_CARD_STRINGS.EXTENDED_DESCRIPTION[4];
            }
        }
        this.rawDescription = this.rawDescription + BASE_CARD_STRINGS.EXTENDED_DESCRIPTION[5];
        this.initializeDescription();
    }

    // 由子类实现
    // 卡牌本身的描述
    protected String cardNormalDesc() {
        return "";
    }

    // 由子类实现
    // 卡牌指向时的描述
    protected String cardCalcDesc() {
        return "";
    }

    @Override
    public void onMoveToDiscard() {
        // 到弃牌堆之后恢复常规描述
        this.updateBrandQueueBaseDesc();
    }

    // 生成一个印记序列列表
    protected ArrayList<BrandPower.BRAND_TYPE> genBrandQueueList(BrandPower.BRAND_TYPE cur_brand_type) {
        return genBrandQueueList(cur_brand_type, this.magicNumber, this.reverse);
    }

    public static ArrayList<BrandPower.BRAND_TYPE> genBrandQueueList(BrandPower.BRAND_TYPE cur_brand_type, int number, boolean reverse) {
        ArrayList<BrandPower.BRAND_TYPE> ret_list = new ArrayList<>();
        ArrayList<BrandPower.BRAND_TYPE> in_list = BRAND_LIST;
        if (reverse) {
            in_list = REVERSE_BRAND_LIST;
        }
        int start = 0;
        for (int i = 0; i < in_list.size(); i++) {
            if (in_list.get(i) == cur_brand_type) {
                start = i;
                break;
            }
        }
        for (int i = 0; i < number; i++) {
            ret_list.add(in_list.get(i + start + 1));
        }
        return ret_list;
    }

    // 指向时
    public void calculateCardDamage(AbstractMonster mo) {
        super.calculateCardDamage(mo);
        BrandPower power = BrandPower.getCanEvokeBrandPower(mo);
        if (power == null) {
            // 恢复常规文本
            this.updateBrandQueueBaseDesc();
            return;
        }
        // 变为指向后的文本
        this.updateBrandQueueCalcDesc(power);
    }
}
