package hatmagicianmod.helpers;

import com.megacrit.cardcrawl.cards.AbstractCard;

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
}
