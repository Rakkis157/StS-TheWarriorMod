package thewarrior.relics;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.RelicStrings;

import basemod.abstracts.CustomRelic;

public class Gladiatoria extends CustomRelic {
	public static final String ID = "TheWarrior:Gladiatoria";
	private static final String IMG = "images/relics/Gladiatoria.png";
	private static final RelicStrings relicStrings = CardCrawlGame.languagePack.getRelicStrings(ID);
	public static final String NAME = relicStrings.NAME;
	public static final String DESCRIPTION[] = relicStrings.DESCRIPTIONS;

	public Gladiatoria() {
		super(ID, new Texture(IMG), RelicTier.STARTER, LandingSound.SOLID);
	}

	@Override
	public String getUpdatedDescription() {
		return DESCRIPTIONS[0];
	}
}
