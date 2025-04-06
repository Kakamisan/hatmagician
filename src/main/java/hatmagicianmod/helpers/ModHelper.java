package hatmagicianmod.helpers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.sun.jna.Function;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.util.ArrayList;

import static hatmagicianmod.characters.MyCharacter.PlayerColorEnum.HAT_MAGICIAN_YELLOW;

public class ModHelper {
    public static String makeID(String id) {
        return "HatMagicianMod:" + id;
    }

    public static String makeCardImgPath(AbstractCard.CardType type, String name) {
        String type_name;
        switch (type) {
            case ATTACK:
                type_name = "attack";
                break;
            case POWER:
                type_name = "power";
                break;
            case STATUS:
                type_name = "status";
                break;
            case CURSE:
                type_name = "curse";
                break;
            case SKILL:
                type_name = "skill";
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }
        return "HatMagicianModRes/img/cards/" + type_name + "/" + name + ".png";
    }

    public static AbstractCard.CardColor color() {
        return HAT_MAGICIAN_YELLOW;
    }

    public static final Logger logger = LogManager.getLogger(ModHelper.class);

    public static void log(String str) {
        logger.info(str);
    }

    public static int cardRand(int size) {
        return AbstractDungeon.cardRng.random(size - 1);
    }

    public static ArrayList<AbstractMonster> getAliveMonsters() {
        ArrayList<AbstractMonster> ret_list = new ArrayList<>();
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            if (!m.isDeadOrEscaped()) {
                ret_list.add(m);
            }
        }
        return ret_list;
    }

    public interface Lambda extends Runnable {
    }

    public static void addToBotAbstract(Lambda func) {
        AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
            @Override
            public void update() {
                func.run();
                isDone = true;
            }
        });
    }

    public static void addToTopAbstract(Lambda func) {
        AbstractDungeon.actionManager.addToTop(new AbstractGameAction() {
            @Override
            public void update() {
                func.run();
                isDone = true;
            }
        });
    }
}
