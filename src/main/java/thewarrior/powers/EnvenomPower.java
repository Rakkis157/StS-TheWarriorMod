package thewarrior.powers;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PoisonPower;

public class EnvenomPower extends AbstractPower {
	public static final String POWER_ID = "TheWarrior:Envenom";
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

	public EnvenomPower(int amount) {
		this.name = EnvenomPower.NAME;
		this.ID = POWER_ID;
		this.owner = AbstractDungeon.player;
		this.amount = amount;
		this.updateDescription();

		this.region128 = new AtlasRegion(ImageMaster.loadImage("images/powers/Envenom.png"), 0, 0, 84, 84);
		this.region48 = new AtlasRegion(ImageMaster.loadImage("images/powers/EnvenomSmall.png"), 0, 0, 32, 32);

		this.type = PowerType.BUFF;
	}

	@Override
	public void updateDescription() {
		this.description = String.format(DESCRIPTIONS[0], new Object[] { Integer.valueOf(this.amount) });
	}

	@Override
	public void onAttack(final DamageInfo info, final int damageAmount, final AbstractCreature target) {
		if (damageAmount > 0 && target != owner && info.type == DamageInfo.DamageType.NORMAL) {
			this.flash();
			AbstractDungeon.actionManager
					.addToBottom(new ApplyPowerAction(target, owner, new PoisonPower(target, owner, amount), amount, true));
		}
	}

	@Override
	public void atEndOfRound() {
		AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(owner, owner, this));
	}
}
