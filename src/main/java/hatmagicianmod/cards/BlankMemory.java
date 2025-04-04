package hatmagicianmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hatmagicianmod.characters.MyCharacter;
import hatmagicianmod.helpers.ModHelper;
import hatmagicianmod.powers.OverloadFormPower;

import java.util.ArrayList;

public class BlankMemory extends BaseBrandAtk {

    public static final String ID;
    private static final CardStrings CARD_STRINGS;
    private static final String IMG_PATH;
    private static final int COST = 0;
    private static final CardType TYPE = CardType.POWER;

    private final ArrayList<CardTags> memory_brand_tags;

    static {
        String name = "BlankMemory";
        ID = ModHelper.makeID(name);
        CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
        IMG_PATH = ModHelper.makeCardImgPath(TYPE, name);
    }

    public BlankMemory() {
        super(ID, CARD_STRINGS.NAME, IMG_PATH, COST, CARD_STRINGS.DESCRIPTION, TYPE, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.SELF);
        this.memory_brand_tags = new ArrayList<>();
        this.magicNumber = this.baseMagicNumber = 1;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        super.use(p, m);
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        if (this.memory_brand_tags.isEmpty()) {
            this.cantUseMessage = CARD_STRINGS.EXTENDED_DESCRIPTION[0];
        } else {
            CardTags last_tag = this.memory_brand_tags.get(this.memory_brand_tags.size() - 1);
            if (last_tag == MyCharacter.PlayerCardTags.HAT_MAGICIAN_BRAND_LIGHTNING) {
                this.cantUseMessage = CARD_STRINGS.EXTENDED_DESCRIPTION[1];
            } else if (last_tag == MyCharacter.PlayerCardTags.HAT_MAGICIAN_BRAND_FIRE) {
                this.cantUseMessage = CARD_STRINGS.EXTENDED_DESCRIPTION[2];
            } else if (last_tag == MyCharacter.PlayerCardTags.HAT_MAGICIAN_BRAND_ICE) {
                this.cantUseMessage = CARD_STRINGS.EXTENDED_DESCRIPTION[3];
            }
        }
        return false;
    }

    @Override
    public void triggerOnOtherCardPlayed(AbstractCard c) {
        super.triggerOnOtherCardPlayed(c);
        CardTags tag = getCardBrandTag(c);
        if (tag != null && !this.memory_brand_tags.contains(tag)) {
            this.memory_brand_tags.add(tag);
        }
    }

    @Override
    public void triggerOnEndOfPlayerTurn() {
        super.triggerOnEndOfPlayerTurn();
        if (this.memory_brand_tags.size() >= 3) {
            // todo 先用这个能力代替一下
            this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new OverloadFormPower(AbstractDungeon.player, this.magicNumber)));
        }
    }

    @Override
    public void triggerOnExhaust() {
        super.triggerOnExhaust();
    }

    // 获取特定印记tag *不包含通用印记tag
    public static CardTags getCardBrandTag(AbstractCard c) {
        if (c.tags.contains(MyCharacter.PlayerCardTags.HAT_MAGICIAN_BRAND_LIGHTNING)) {
            return MyCharacter.PlayerCardTags.HAT_MAGICIAN_BRAND_LIGHTNING;
        } else if (c.tags.contains(MyCharacter.PlayerCardTags.HAT_MAGICIAN_BRAND_FIRE)) {
            return MyCharacter.PlayerCardTags.HAT_MAGICIAN_BRAND_FIRE;
        } else if (c.tags.contains(MyCharacter.PlayerCardTags.HAT_MAGICIAN_BRAND_ICE)) {
            return MyCharacter.PlayerCardTags.HAT_MAGICIAN_BRAND_ICE;
        } else {
            return null;
        }
    }
}
