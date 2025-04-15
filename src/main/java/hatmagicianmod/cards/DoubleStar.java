package hatmagicianmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hatmagicianmod.actions.BrandEvokeAction;
import hatmagicianmod.characters.MyCharacter;
import hatmagicianmod.helpers.ModHelper;
import hatmagicianmod.powers.TempStrengthPower;

import java.util.ArrayList;

public class DoubleStar extends BaseBrandAtk {

    public static final String ID;
    private static final CardStrings CARD_STRINGS;
    private static final String IMG_PATH;
    private static final int COST = 1;
    private static final CardType TYPE = CardType.ATTACK;

    static {
        String name = "DoubleStar";
        ID = ModHelper.makeID(name);
        CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
        IMG_PATH = ModHelper.makeCardImgPath(TYPE, name);
    }

    public DoubleStar() {
        super(ID, CARD_STRINGS.NAME, IMG_PATH, COST, CARD_STRINGS.DESCRIPTION, TYPE, ModHelper.color(), CardRarity.COMMON, CardTarget.ENEMY);
        this.tags.add(MyCharacter.PlayerCardTags.HAT_MAGICIAN_SLEEP);
        this.cardsToPreview = new IsSleep();
        this.baseDamage = 5;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(2);
        }
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        AbstractPlayer player = AbstractDungeon.player;
        ArrayList<AbstractPower> tmp_list = new ArrayList<>();

        AbstractPower po = player.getPower(TempStrengthPower.POWER_ID);
        if (po != null) {
            po.amount = -po.amount;
            tmp_list.add(po);
        }

        super.calculateCardDamage(mo);

        for (AbstractPower p : tmp_list) {
            p.amount = -p.amount;
        }

    }

    @Override
    public void applyPowers() {
        AbstractPlayer player = AbstractDungeon.player;
        ArrayList<AbstractPower> tmp_list = new ArrayList<>();

        AbstractPower po = player.getPower(TempStrengthPower.POWER_ID);
        if (po != null) {
            po.amount = -po.amount;
            tmp_list.add(po);
        }

        super.applyPowers();

        for (AbstractPower p : tmp_list) {
            p.amount = -p.amount;
        }

    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new BrandEvokeAction(m));
        this.addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
        this.addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
    }
}
