package thewarrior.powers;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import thewarrior.TheWarriorMod;
import thewarrior.actions.ComboAction;

public class DoubleComboPower extends AbstractWarriorPower {
	public static final String POWER_ID = "TheWarrior:DoubleCombo";
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

	public static List<AbstractGameAction> doubleComboAction = new ArrayList<>();

	public DoubleComboPower(int amount) {
		this.name = NAME;
		this.ID = POWER_ID;
		this.owner = AbstractDungeon.player;
		this.amount = amount;
		updateDescription();

		this.region128 = new AtlasRegion(ImageMaster.loadImage("images/powers/DoubleCombo.png"), 0, 0, 84, 84);
		this.region48 = new AtlasRegion(ImageMaster.loadImage("images/powers/DoubleComboSmall.png"), 0, 0, 32, 32);

		this.type = PowerType.BUFF;
	}

	@Override
	public void updateDescription() {
		if (amount == 1)
			description = DESCRIPTIONS[0];
		else
			this.description = String.format(DESCRIPTIONS[1], new Object[] { Integer.valueOf(this.amount) });
	}

	@Override
	public void onFinishCombo(AbstractMonster m) {
		TheWarriorMod.logger.info("double combo");
		this.flash();
		for (AbstractGameAction action : DoubleComboPower.doubleComboAction)
			AbstractDungeon.actionManager.addToBottom(action);
		int dazeAmount = (int) Math.sqrt(ComboAction.cardPlayed * 5000.0F / (ComboAction.speed / 2));
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, owner, new DazedPower(m, dazeAmount), dazeAmount));
	}

	@Override
	public void onComboEnd() {
		AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(owner, owner, this, 1));
	}
}
