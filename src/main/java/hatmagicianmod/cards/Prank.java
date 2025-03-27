package hatmagicianmod.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.ShockWaveEffect;
import hatmagicianmod.actions.BrandEvokeAllAction;
import hatmagicianmod.effects.AtkAllLightningEffect;
import hatmagicianmod.helpers.ModHelper;

import static hatmagicianmod.modcore.HatMagicianMod.MY_COLOR;

public class Prank extends CustomCard {

    public static final String ID;
    private static final CardStrings CARD_STRINGS;
    private static final String IMG_PATH;
    private static final int COST = 0;
    private static final CardType TYPE = CardType.ATTACK;

    static {
        String name = "Prank";
        ID = ModHelper.makeID(name);
        CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
        IMG_PATH = ModHelper.makeCardImgPath(TYPE, name);
    }

    public Prank() {
        super(ID, CARD_STRINGS.NAME, IMG_PATH, COST, CARD_STRINGS.DESCRIPTION, TYPE, ModHelper.color(), CardRarity.BASIC, CardTarget.ALL_ENEMY);
        this.baseDamage = 2;
        this.magicNumber = this.baseMagicNumber = 2;
        this.selfRetain = true;
        this.exhaust = true;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(1);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new VFXAction(p, new ShockWaveEffect(p.hb.cX, p.hb.cY, MY_COLOR, ShockWaveEffect.ShockWaveType.CHAOTIC), 0.3F));
        this.addToBot(new BrandEvokeAllAction());
        for (int i = 0; i < this.magicNumber; i++) {
//            this.addToBot(new VFXAction(new AtkAllLightningEffect()));
            this.addToBot(new DamageAllEnemiesAction(p, this.damage, this.damageTypeForTurn, AttackEffect.SLASH_HORIZONTAL));
        }
    }
}
