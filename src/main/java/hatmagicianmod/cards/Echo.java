package hatmagicianmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.ShockWaveEffect;
import hatmagicianmod.actions.BrandEvokeAction;
import hatmagicianmod.helpers.ModHelper;
import hatmagicianmod.powers.BrandPower;

import static hatmagicianmod.modcore.HatMagicianMod.MY_COLOR;

public class Echo extends BaseBrandAtk {

    public static final String ID;
    private static final CardStrings CARD_STRINGS;
    private static final String IMG_PATH;
    private static final int COST = 1;
    private static final CardType TYPE = CardType.ATTACK;

    static {
        String name = "Echo";
        ID = ModHelper.makeID(name);
        CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
        IMG_PATH = ModHelper.makeCardImgPath(TYPE, name);
    }

    public Echo() {
        super(ID, CARD_STRINGS.NAME, IMG_PATH, COST, CARD_STRINGS.DESCRIPTION, TYPE, ModHelper.color(), CardRarity.COMMON, CardTarget.ENEMY);
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
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new VFXAction(p, new ShockWaveEffect(p.hb.cX, p.hb.cY, MY_COLOR, ShockWaveEffect.ShockWaveType.CHAOTIC), 0.3F));
        this.addToBot(new BrandEvokeAction(m));
        int cnt = 0;
        if (BrandPower.hasAnyMonstersBrand(BrandPower.BRAND_TYPE.LIGHTNING))
            cnt++;
        if (BrandPower.hasAnyMonstersBrand(BrandPower.BRAND_TYPE.ICE))
            cnt++;
        if (BrandPower.hasAnyMonstersBrand(BrandPower.BRAND_TYPE.FIRE))
            cnt++;
        for (int i = 0; i < cnt; i++) {
            this.addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        }
    }
}
