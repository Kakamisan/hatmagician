package hatmagicianmod.modcore;

import basemod.AutoAdd;
import basemod.BaseMod;
import basemod.abstracts.CustomRelic;
import basemod.helpers.RelicType;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import hatmagicianmod.cards.Strike;
import hatmagicianmod.characters.MyCharacter;
import hatmagicianmod.relics.SpecHat;

import java.nio.charset.StandardCharsets;

import static hatmagicianmod.characters.MyCharacter.PlayerColorEnum.HAT_MAGICIAN_YELLOW;
import static hatmagicianmod.characters.MyCharacter.PlayerColorEnum.CHARACTER_HAT_MAGICIAN;

@SpireInitializer // 加载mod的注解
public class HatMagicianMod implements EditCardsSubscriber, EditStringsSubscriber, EditCharactersSubscriber, EditRelicsSubscriber, EditKeywordsSubscriber {
    // 人物选择界面按钮的图片
    private static final String MY_CHARACTER_BUTTON = "HatMagicianModRes/img/char/Character_Button.png";
    // 人物选择界面的立绘
    private static final String MY_CHARACTER_PORTRAIT = "HatMagicianModRes/img/char/Character_Portrait.png";
    // 攻击牌的背景（小尺寸）
    private static final String BG_ATTACK_512 = "HatMagicianModRes/img/512/bg_attack_512.png";
    // 能力牌的背景（小尺寸）
    private static final String BG_POWER_512 = "HatMagicianModRes/img/512/bg_power_512.png";
    // 技能牌的背景（小尺寸）
    private static final String BG_SKILL_512 = "HatMagicianModRes/img/512/bg_skill_512.png";
    // 在卡牌和遗物描述中的能量图标
    private static final String SMALL_ORB = "HatMagicianModRes/img/char/small_orb.png";
    // 攻击牌的背景（大尺寸）
    private static final String BG_ATTACK_1024 = "HatMagicianModRes/img/1024/bg_attack.png";
    // 能力牌的背景（大尺寸）
    private static final String BG_POWER_1024 = "HatMagicianModRes/img/1024/bg_power.png";
    // 技能牌的背景（大尺寸）
    private static final String BG_SKILL_1024 = "HatMagicianModRes/img/1024/bg_skill.png";
    // 在卡牌预览界面的能量图标
    private static final String BIG_ORB = "HatMagicianModRes/img/char/card_orb.png";
    // 小尺寸的能量图标（战斗中，牌堆预览）
    private static final String ENERGY_ORB = "HatMagicianModRes/img/char/cost_orb.png";

    public static final Color MY_COLOR = new Color(249.0F / 255.0F, 210.0F / 255.0F, 43.0F / 255.0F, 1.0F);

    // 构造方法
    public HatMagicianMod() {
        BaseMod.subscribe(this); // 告诉basemod你要订阅事件
        BaseMod.addColor(HAT_MAGICIAN_YELLOW, MY_COLOR, MY_COLOR, MY_COLOR, MY_COLOR, MY_COLOR, MY_COLOR, MY_COLOR, BG_ATTACK_512, BG_SKILL_512, BG_POWER_512, ENERGY_ORB, BG_ATTACK_1024, BG_SKILL_1024, BG_POWER_1024, BIG_ORB, SMALL_ORB);
    }

    // 注解需要调用的方法，必须写
    public static void initialize() {
        new HatMagicianMod();
    }

    @Override
    public void receiveEditCards() {
        String lang;
        if (Settings.language == Settings.GameLanguage.ZHS) {
            lang = "ZHS";
        } else {
            lang = "ZHS";
        }
        BaseMod.loadCustomStringsFile(CardStrings.class, "HatMagicianModRes/localization/" + lang + "/cards.json");
//        BaseMod.addCard(new Strike());
//        BaseMod.addCard(new Block());
//        BaseMod.addCard(new Activate());
        new AutoAdd("HatMagicianMod").packageFilter(Strike.class).setDefaultSeen(true).cards();
    }

    // 当开始添加人物时，调用这个方法
    @Override
    public void receiveEditCharacters() {
        // 向basemod注册人物
        BaseMod.addCharacter(new MyCharacter(CardCrawlGame.playerName), MY_CHARACTER_BUTTON, MY_CHARACTER_PORTRAIT, CHARACTER_HAT_MAGICIAN);
    }

    public void receiveEditStrings() {
        String lang;
        if (Settings.language == Settings.GameLanguage.ZHS) {
            lang = "ZHS";
        } else {
            lang = "ZHS";
        }
        BaseMod.loadCustomStringsFile(CardStrings.class, "HatMagicianModRes/localization/" + lang + "/cards.json");
        BaseMod.loadCustomStringsFile(CharacterStrings.class, "HatMagicianModRes/localization/" + lang + "/characters.json");
        BaseMod.loadCustomStringsFile(RelicStrings.class, "HatMagicianModRes/localization/" + lang + "/relics.json");
        BaseMod.loadCustomStringsFile(PowerStrings.class, "HatMagicianModRes/localization/" + lang + "/powers.json");
    }

    @Override
    public void receiveEditRelics() {
//        BaseMod.addRelic(new SpecHat(), RelicType.SHARED); // RelicType表示是所有角色都能拿到的遗物，还是一个角色的独有遗物
        new AutoAdd("HatMagicianMod").packageFilter(SpecHat.class)
                .any(CustomRelic.class, (info, relic) -> {
                    BaseMod.addRelic(relic, RelicType.SHARED);
                    if (info.seen) {
                        UnlockTracker.markRelicAsSeen(relic.relicId);
                    }
                });
    }

    @Override
    public void receiveEditKeywords() {
        Gson gson = new Gson();
        String lang = "ZHS";
        if (Settings.language == Settings.GameLanguage.ZHS) {
            lang = "ZHS";
        }

        String json = Gdx.files.internal("HatMagicianModRes/localization/" + lang + "/keywords.json")
                .readString(String.valueOf(StandardCharsets.UTF_8));
        Keyword[] keywords = gson.fromJson(json, Keyword[].class);
        if (keywords != null) {
            for (Keyword keyword : keywords) {
                // 这个id要全小写
                BaseMod.addKeyword("hatmagician", keyword.NAMES[0], keyword.NAMES, keyword.DESCRIPTION);
            }
        }
    }
}
