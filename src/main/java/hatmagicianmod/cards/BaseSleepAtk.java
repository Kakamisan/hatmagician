package hatmagicianmod.cards;

import basemod.AutoAdd;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hatmagicianmod.powers.TempStrengthPower;

import java.util.ArrayList;

@AutoAdd.Ignore
public class BaseSleepAtk extends CustomCard {

    public BaseSleepAtk(String id, String name, String img, int cost, String rawDescription, CardType type, CardColor color, CardRarity rarity, CardTarget target) {
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
}
