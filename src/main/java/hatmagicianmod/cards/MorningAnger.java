package hatmagicianmod.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import hatmagicianmod.helpers.ModHelper;
import hatmagicianmod.powers.TempStrengthPower;

import java.util.ArrayList;

public class MorningAnger extends CustomCard {

    public static final String ID;
    private static final CardStrings CARD_STRINGS;
    private static final String IMG_PATH;
    private static final int COST = 0;
    private static final CardType TYPE = CardType.ATTACK;

    static {
        String name = "MorningAnger";
        ID = ModHelper.makeID(name);
        CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
        IMG_PATH = ModHelper.makeCardImgPath(TYPE, name);
    }

    public MorningAnger() {
        super(ID, CARD_STRINGS.NAME, IMG_PATH, COST, CARD_STRINGS.DESCRIPTION, TYPE, ModHelper.color(), CardRarity.COMMON, CardTarget.ENEMY);
        this.baseDamage = 2;
        this.magicNumber = this.baseMagicNumber = 1;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(1);
            this.upgradeMagicNumber(1);
        }
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        AbstractPlayer player = AbstractDungeon.player;
        ArrayList<AbstractPower> tmp_list = new ArrayList<>();

        for (AbstractPower p : player.powers) {
            if (p.ID.equals(TempStrengthPower.POWER_ID)) {
                p.amount = -p.amount;
                tmp_list.add(p);
            }
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

        for (AbstractPower p : player.powers) {
            if (p.ID.equals(TempStrengthPower.POWER_ID)) {
                p.amount = -p.amount;
                tmp_list.add(p);
            }
        }

        super.applyPowers();

        for (AbstractPower p : tmp_list) {
            p.amount = -p.amount;
        }

    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        this.addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        this.addToBot(new ApplyPowerAction(m, p, new VulnerablePower(m, this.magicNumber, false)));
        this.addToBot(new RemoveSpecificPowerAction(p, p, ModHelper.makeID("TempStrengthPower")));
    }
}
