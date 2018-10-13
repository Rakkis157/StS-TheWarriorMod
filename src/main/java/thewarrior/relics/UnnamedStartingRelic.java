package thewarrior.relics;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.RelicStrings;

import basemod.abstracts.CustomRelic;

public class UnnamedStartingRelic extends CustomRelic {
	public static final String ID = "TheWarrior:UnnamedStartingRelic";
    private static final String IMG = "images/relics/UnnamedStartingRelic.png";
    private static final RelicStrings relicStrings = CardCrawlGame.languagePack.getRelicStrings(ID);
    public static final String NAME = relicStrings.NAME;
	public static final String DESCRIPTION[] = relicStrings.DESCRIPTIONS;

	public UnnamedStartingRelic() {
		super(ID, new Texture(IMG), RelicTier.STARTER, LandingSound.SOLID);
	}

	@Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
