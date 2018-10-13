package thewarrior.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

// draw reduction power in the game is turn based, so it reduces card draw for 2 turns instead of draw 2 less cards
public class DrawReductionPower extends AbstractPower {
	public static final String POWER_ID = "TheWarrior:DrawReduction";
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

	public DrawReductionPower(int amount) {
		name = NAME;
		ID = POWER_ID;
		owner = AbstractDungeon.player;
		this.amount = amount;
		updateDescription();

		this.region128 = new AtlasRegion(ImageMaster.loadImage("images/powers/DrawReduction.png"), 0, 0, 84, 84);
		this.region48 = new AtlasRegion(ImageMaster.loadImage("images/powers/DrawReductionSmall.png"), 0, 0, 32, 32);

		this.type = PowerType.DEBUFF;
	}

	@Override
	public void updateDescription() {
		if (amount == 1)
			this.description = String.format(DESCRIPTIONS[0], new Object[] { Integer.valueOf(this.amount) });
		else
			this.description = String.format(DESCRIPTIONS[1], new Object[] { Integer.valueOf(this.amount) });
	}

	@Override
	public void onInitialApplication() {
		AbstractDungeon.player.gameHandSize -= amount;
	}

	@Override
	public void onRemove() {
		AbstractDungeon.player.gameHandSize += amount;
	}

	@Override
	public void stackPower(int stackAmount) {
		AbstractDungeon.player.gameHandSize -= stackAmount;
		super.stackPower(stackAmount);
	}

	@Override
	public void atStartOfTurnPostDraw() {
		AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(owner, owner, this));
	}
}
