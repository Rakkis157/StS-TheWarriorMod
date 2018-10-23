package thewarrior.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class DistractedPower extends AbstractPower {
	public static final String POWER_ID = "TheWarrior:Distracted";
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

	public DistractedPower(AbstractMonster owner, int amount) {
		this.name = NAME;
		this.ID = POWER_ID;
		this.owner = owner;
		this.amount = amount;
		updateDescription();

		this.region128 = new AtlasRegion(ImageMaster.loadImage("images/powers/Distracted.png"), 0, 0, 84, 84);
		this.region48 = new AtlasRegion(ImageMaster.loadImage("images/powers/DistractedSmall.png"), 0, 0, 32, 32);

		this.type = PowerType.DEBUFF;
	}

	@Override
	public float atDamageGive(float damage, DamageType type) {
		if (type == DamageType.NORMAL) {
			damage -= damage * amount / 10.0F;
		}
		return damage;
	}

	@Override
	public float atDamageReceive(float damage, DamageType damageType) {
		if (damageType == DamageType.NORMAL) {
			damage += damage * amount / 10.0F;
		}
		return damage;
	}

	@Override
	public void atEndOfRound() {
		AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(owner, owner, this));
	}

	@Override
	public void updateDescription() {
		this.description = DESCRIPTIONS[0] + amount * 10 + DESCRIPTIONS[1] + amount * 10 + DESCRIPTIONS[2];
	}
}
