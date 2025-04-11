package hatmagicianmod.cards;

import basemod.AutoAdd;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hatmagicianmod.helpers.ModHelper;
import hatmagicianmod.powers.BrandPower;

import java.util.ArrayList;

// 带印记/激活的攻击中间类
@AutoAdd.Ignore
public class BaseBrandAtk extends CustomCard {

    public BaseBrandAtk(String id, String name, String img, int cost, String rawDescription, AbstractCard.CardType type, AbstractCard.CardColor color, AbstractCard.CardRarity rarity, AbstractCard.CardTarget target) {
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
//        ModHelper.log("[" + this.name + "]计算属性");

        // 临时把印记设置成激活状态
        ArrayList<BrandPower> tmp_pl = new ArrayList<>();
        if (!this.isMultiDamage && mo != null) {
            BrandPower p = BrandPower.getCanEvokeBrandPower(mo);
            if (p != null) {
//                ModHelper.log("[" + p.name + "]本次计算中激活");
                p.is_evoking = true;
                tmp_pl.add(p);
            }
        } else {
            ArrayList<AbstractMonster> ms = AbstractDungeon.getCurrRoom().monsters.monsters;
            for (AbstractMonster m : ms) {
                if (m.isDeadOrEscaped()) continue;
                BrandPower p = BrandPower.getCanEvokeBrandPower(m);
                if (p != null) {
//                    ModHelper.log("[" + p.name + "]本次计算中激活");
                    p.is_evoking = true;
                    tmp_pl.add(p);
                }
            }
        }

        super.calculateCardDamage(mo);

        // 把印记复原
        for (BrandPower p : tmp_pl) {
//            ModHelper.log("[" + p.name + "]本次计算中还原");
            p.is_evoking = false;
        }
    }

}
