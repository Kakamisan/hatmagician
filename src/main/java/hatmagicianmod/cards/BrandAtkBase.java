package hatmagicianmod.cards;

import basemod.AutoAdd;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hatmagicianmod.helpers.ModHelper;
import hatmagicianmod.powers.BrandPower;

import java.util.ArrayList;

// 带印记/激活的攻击中间类
@AutoAdd.Ignore
public class BrandAtkBase extends CustomCard {

    public BrandAtkBase(String id, String name, String img, int cost, String rawDescription, AbstractCard.CardType type, AbstractCard.CardColor color, AbstractCard.CardRarity rarity, AbstractCard.CardTarget target) {
        super(id, name, img, cost, rawDescription, type, color, rarity, target);
    }

    @Override
    public void upgrade() {

    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {

    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        ModHelper.log("[" + this.name + "]计算属性");

        // 临时把印记设置成激活状态
        ArrayList<BrandPower> tmp_pl = new ArrayList<>();
        if (!this.isMultiDamage && mo != null) {
            for (AbstractPower p : mo.powers) {
                if (BrandPower.isBrandPower(p, BrandPower.BRAND_TYPE.FIRE)) {
                    BrandPower p2 = ((BrandPower) p);
                    p2.is_evoking = true;
                    tmp_pl.add(p2);
                }
            }
        } else {
            ArrayList<AbstractMonster> ms = AbstractDungeon.getCurrRoom().monsters.monsters;
            for (AbstractMonster m : ms) {
                for (AbstractPower p : m.powers) {
                    if (BrandPower.isBrandPower(p, BrandPower.BRAND_TYPE.FIRE)) {
                        BrandPower p2 = ((BrandPower) p);
                        p2.is_evoking = true;
                        tmp_pl.add(p2);
                    }
                }
            }
        }

        super.calculateCardDamage(mo);

        // 把印记复原
        for (BrandPower p : tmp_pl) {
            p.is_evoking = false;
        }
    }

}
