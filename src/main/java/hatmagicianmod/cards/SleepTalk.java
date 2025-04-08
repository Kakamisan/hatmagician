package hatmagicianmod.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.ShockWaveEffect;
import hatmagicianmod.characters.MyCharacter;
import hatmagicianmod.helpers.ModHelper;
import hatmagicianmod.powers.TempStrengthPower;

import java.util.ArrayList;

import static hatmagicianmod.modcore.HatMagicianMod.MY_COLOR;

public class SleepTalk extends CustomCard {

    public static final String ID;
    private static final CardStrings CARD_STRINGS;
    private static final String IMG_PATH;
    private static final int COST = 1;
    private static final CardType TYPE = CardType.ATTACK;

    static {
        String name = "SleepTalk";
        ID = ModHelper.makeID(name);
        CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
        IMG_PATH = ModHelper.makeCardImgPath(TYPE, name);
    }

    public SleepTalk() {
        super(ID, CARD_STRINGS.NAME, IMG_PATH, COST, CARD_STRINGS.DESCRIPTION, TYPE, ModHelper.color(), CardRarity.COMMON, CardTarget.ALL_ENEMY);
        this.baseDamage = 6;
        this.isMultiDamage = true;
        this.tags.add(MyCharacter.PlayerCardTags.HAT_MAGICIAN_SLEEP);
        this.cardsToPreview = new IsSleep();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(3);
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
        this.addToBot(new VFXAction(p, new ShockWaveEffect(p.hb.cX, p.hb.cY, MY_COLOR, ShockWaveEffect.ShockWaveType.NORMAL), 0.3F));
        this.addToBot(new DamageAllEnemiesAction(p, this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.BLUNT_LIGHT));
    }
}
