package thewarrior.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.unique.PoisonLoseHpAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom.RoomPhase;

public class BleedingPower extends AbstractPower {
	public static final String POWER_ID = "TheWarrior:Bleeding";
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
	private AbstractCreature source;

	public BleedingPower(AbstractCreature owner, AbstractCreature source, int amount) {
		this.name = NAME;
		this.ID = POWER_ID;
		this.owner = owner;
		this.source = source;
		this.amount = amount;
		updateDescription();

		this.region128 = new AtlasRegion(ImageMaster.loadImage("images/powers/Bleeding.png"), 0, 0, 84, 84);
		this.region48 = new AtlasRegion(ImageMaster.loadImage("images/powers/BleedingSmall.png"), 0, 0, 32, 32);

		this.type = PowerType.DEBUFF;
	}

	@Override
	public void playApplyPowerSfx() {
		CardCrawlGame.sound.play("POWER_POISON", 0.05F);
	}

	@Override
	public void updateDescription() {
		this.description = String.format(DESCRIPTIONS[0], new Object[] { Integer.valueOf(this.amount / 2), Integer.valueOf(this.amount) });
	}

	@Override
	public float atDamageReceive(float damage, DamageType damageType) {
		if (damageType == DamageType.NORMAL) {
			damage += damage * amount / 100;
		}
		return damage;
	}

	@Override
	public void atStartOfTurn() {
		if (AbstractDungeon.getCurrRoom().phase == RoomPhase.COMBAT) {
			if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
				flashWithoutSound();
				AbstractDungeon.actionManager // bleed
						.addToBottom(new DamageAction(owner, new DamageInfo(owner, amount / 2, DamageType.HP_LOSS), AttackEffect.FIRE));
				if (owner.hasPower("Poison")) {
					owner.getPower("Poison").amount++; // poison lose hp action will reduce poison amount by 1, so I give 1 extra here
					AbstractDungeon.actionManager.addToBottom( // let poison deal extra damage
							new PoisonLoseHpAction(owner, source, owner.getPower("Poison").amount * amount / 100, AttackEffect.POISON));
				}
			}
		}
	}
}
