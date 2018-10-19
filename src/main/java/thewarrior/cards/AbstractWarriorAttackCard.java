package thewarrior.cards;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thewarrior.TheWarriorMod;
import thewarrior.actions.ChooseAction;
import thewarrior.actions.ComboAction;
import thewarrior.actions.UseCardAction;

public abstract class AbstractWarriorAttackCard extends AbstractWarriorCard {

	public static enum WeaponType {
		CLAW, DAGGER, SWORD, AXE, HAMMER;

		public static final int WEAPON_NUM = 5;

		/**
		 * Return the name of the weapon type with only the first letter capitalized.
		 */
		@Override
		public String toString() {
			return this.name().charAt(0) + this.name().toLowerCase().substring(1);
		}

		public int toId() {
			return getId(this);
		}

		public static int getId(WeaponType t) {
			switch (t) {
			case AXE:
				return 3;
			case CLAW:
				return 0;
			case DAGGER:
				return 1;
			case HAMMER:
				return 4;
			case SWORD:
				return 2;
			default:
				break;
			}
			return -1;
		}

		public static WeaponType getWeapontypeById(int id) {
			if (id == 0)
				return CLAW;
			if (id == 1)
				return DAGGER;
			if (id == 2)
				return SWORD;
			if (id == 3)
				return AXE;
			if (id == 4)
				return HAMMER;
			return null;
		}

		public static List<ArrayList<AttackType>> getAttacktypeByWeapontype = new ArrayList<ArrayList<AttackType>>(
				Arrays.asList(/* claw */new ArrayList<AttackType>(Arrays.asList(AttackType.GRASP, AttackType.SCRATCH, AttackType.FEINT)),
						/* dagger */new ArrayList<AttackType>(Arrays.asList(AttackType.FEINT, AttackType.CUT, AttackType.THRUST)),
						/* sword */new ArrayList<AttackType>(Arrays.asList(AttackType.THRUST, AttackType.SLASH, AttackType.STRIKE)),
						/* axe */new ArrayList<AttackType>(Arrays.asList(AttackType.STRIKE, AttackType.CHOP, AttackType.DISARM)),
						/* hammer */new ArrayList<AttackType>(Arrays.asList(AttackType.DISARM, AttackType.BLOW, AttackType.HAMMER))));
	}

	public static enum AttackType {
		GRASP, SCRATCH, FEINT, CUT, THRUST, SLASH, STRIKE, CHOP, DISARM, BLOW, HAMMER;

		public static final int ATTACK_NUM = 11;

		/**
		 * Return the name of the attack type with only the first letter capitalized.
		 */
		@Override
		public String toString() {
			return this.name().charAt(0) + this.name().toLowerCase().substring(1);
		}

		public int toId() {
			return getId(this);
		}

		public static int getId(AttackType t) {
			switch (t) {
			case BLOW:
				return 9;
			case CHOP:
				return 7;
			case CUT:
				return 3;
			case DISARM:
				return 8;
			case FEINT:
				return 2;
			case GRASP:
				return 0;
			case HAMMER:
				return 10;
			case SCRATCH:
				return 1;
			case SLASH:
				return 5;
			case STRIKE:
				return 6;
			case THRUST:
				return 4;
			default:
				break;
			}
			return -1;
		}

		public static AttackType getAttacktypeById(int id) {
			if (id == 0)
				return GRASP;
			if (id == 1)
				return SCRATCH;
			if (id == 2)
				return FEINT;
			if (id == 3)
				return CUT;
			if (id == 4)
				return THRUST;
			if (id == 5)
				return SLASH;
			if (id == 6)
				return STRIKE;
			if (id == 7)
				return CHOP;
			if (id == 8)
				return DISARM;
			if (id == 9)
				return BLOW;
			if (id == 10)
				return HAMMER;
			return null;
		}

		public static List<ArrayList<WeaponType>> getWeapontypeByAttacktype =
				new ArrayList<ArrayList<WeaponType>>(Arrays.asList(/* grasp */new ArrayList<WeaponType>(Arrays.asList(WeaponType.CLAW)),
						/* scratch */new ArrayList<WeaponType>(Arrays.asList(WeaponType.CLAW)),
						/* feint */new ArrayList<WeaponType>(Arrays.asList(WeaponType.CLAW, WeaponType.DAGGER)),
						/* cut */new ArrayList<WeaponType>(Arrays.asList(WeaponType.DAGGER)),
						/* thrust */new ArrayList<WeaponType>(Arrays.asList(WeaponType.DAGGER, WeaponType.SWORD)),
						/* slash */new ArrayList<WeaponType>(Arrays.asList(WeaponType.SWORD)),
						/* strike */new ArrayList<WeaponType>(Arrays.asList(WeaponType.SWORD, WeaponType.AXE)),
						/* chop */new ArrayList<WeaponType>(Arrays.asList(WeaponType.AXE)),
						/* disarm */new ArrayList<WeaponType>(Arrays.asList(WeaponType.AXE, WeaponType.HAMMER)),
						/* blow */new ArrayList<WeaponType>(Arrays.asList(WeaponType.HAMMER)),
						/* hammer */new ArrayList<WeaponType>(Arrays.asList(WeaponType.HAMMER))));
		public static List<ArrayList<AttackType>> getCombotype =
				new ArrayList<ArrayList<AttackType>>(Arrays.asList(/* grasp */new ArrayList<AttackType>(Arrays.asList()),
						/* scratch */new ArrayList<AttackType>(
								Arrays.asList(AttackType.GRASP, AttackType.SCRATCH, AttackType.FEINT, AttackType.CUT)),
						/* feint */new ArrayList<AttackType>(Arrays.asList(AttackType.GRASP, AttackType.SCRATCH, AttackType.FEINT,
								AttackType.CUT, AttackType.THRUST, AttackType.SLASH)),
						/* cut */new ArrayList<AttackType>(
								Arrays.asList(AttackType.GRASP, AttackType.SCRATCH, AttackType.FEINT, AttackType.CUT, AttackType.THRUST)),
						/* thrust */new ArrayList<AttackType>(Arrays.asList(AttackType.CUT, AttackType.THRUST)),
						/* slash */new ArrayList<AttackType>(Arrays.asList(AttackType.SLASH)),
						/* strike */new ArrayList<AttackType>(
								Arrays.asList(AttackType.STRIKE, AttackType.CHOP, AttackType.DISARM, AttackType.BLOW, AttackType.HAMMER)),
						/* chop */new ArrayList<AttackType>(
								Arrays.asList(AttackType.STRIKE, AttackType.CHOP, AttackType.DISARM, AttackType.BLOW, AttackType.HAMMER)),
						/* disarm */new ArrayList<AttackType>(Arrays.asList(AttackType.CHOP, AttackType.BLOW, AttackType.HAMMER)),
						/* blow */new ArrayList<AttackType>(
								Arrays.asList(AttackType.STRIKE, AttackType.CHOP, AttackType.DISARM, AttackType.BLOW, AttackType.HAMMER)),
						/* hammer */new ArrayList<AttackType>(Arrays.asList())));
		public static List<String> getCombotypeTip =
				new ArrayList<String>(Arrays.asList(/* grasp */"Doesn't combo.", /* scratch */"Combo with grasp, scratch, feint and cut.",
						/* feint */"Combo with grasp, scratch, feint, cut, thrust and slash.",
						/* cut */"Combo with grasp, scratch, feint, cut and thrust.", /* thrust */"Combo with cut and thrust.",
						/* slash */"Combo with slash.", /* strike */"Combo with strike, chop, disarm, blow and hammer.",
						/* chop */"Combo with strike, chop, disarm, blow and hammer.", /* disarm */"Combo with chop, blow and hammer.",
						/* blow */"Combo with strike, chop, disarm, blow and hammer.", /* hammer */"Doesn't combo."));
	}

	public static List<ArrayList<String>> listWeaponType = new ArrayList<>();
	public static List<ArrayList<String>> listAttackType = new ArrayList<>();
	public static ArrayList<AbstractCard> cardToPreview = new ArrayList<>();
	private static AbstractCard cardPreviewing = null;

	private WeaponType weaponType = null;
	private AbstractCard[] previewCards = new AbstractCard[3];
	private boolean isRenderingTip = false;

	public AbstractWarriorAttackCard(String id, String name, int cost, String rawDescription, CardRarity rarity, CardTarget target,
			WeaponType t) {
		super(id, name, cost, rawDescription, CardType.ATTACK, rarity, target);

		this.weaponType = t;
		if (!listWeaponType.get(WeaponType.getId(t)).contains(id)) {
			listWeaponType.get(WeaponType.getId(t)).add(id);
			for (AttackType a : WeaponType.getAttacktypeByWeapontype.get(WeaponType.getId(t))) {
				listAttackType.get(AttackType.getId(a)).add(id);
				if (a == AttackType.STRIKE)
					this.tags.add(CardTags.STRIKE);
			}
		}
	}

	public void changePreviewCards(AbstractCard c1, AbstractCard c2, AbstractCard c3) {
		previewCards[0] = c1;
		previewCards[1] = c2;
		previewCards[2] = c3;

		for (AbstractCard card : previewCards) {
			if (card != null)
				if (upgraded)
					card.upgrade();
		}
	}

	public static AbstractCard getSubcard(String cardId, int attackTypeNum) {
		try {
			Object mainCard =
					Class.forName("thewarrior.cards." + cardId.substring(new String(TheWarriorMod.MOD_ID + ':').length())).newInstance();
			for (Class<?> c : mainCard.getClass().getDeclaredClasses()) {
				if (c.getName().endsWith(Integer.toString(attackTypeNum)))
					return (AbstractCard) c.getDeclaredConstructor(new Class[] { mainCard.getClass() })
							.newInstance(new Object[] { mainCard });
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		if (ComboAction.attackType != null) {
			int index = 0;
			for (AttackType t : WeaponType.getAttacktypeByWeapontype.get(weaponType.toId())) {
				if (t == ComboAction.attackType) {
					AbstractDungeon.actionManager.addToBottom(new UseCardAction(previewCards[index], m));
					return;
				}
				index++;
			}
			TheWarriorMod.logger.info(name + " have no attack type " + ComboAction.attackType.toString());
			return;
		}

		AbstractDungeon.actionManager.addToBottom(new ChooseAction(new ArrayList<AbstractCard>(Arrays.asList(previewCards)), m));
	}

	@Override
	public void calculateCardDamage(AbstractMonster mo) {
		super.calculateCardDamage(mo);
		try {
			for (AbstractCard card : previewCards) {
				if (card != null)
					card.calculateCardDamage(mo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void resetAttributes() {
		super.resetAttributes();
		try {
			for (AbstractCard card : previewCards) {
				if (card != null)
					card.resetAttributes();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void applyPowers() {
		super.applyPowers();
		try {
			for (AbstractCard card : previewCards) {
				if (card != null)
					card.applyPowers();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void upgradeName() {
		super.upgradeName();
		try {
			for (AbstractCard c : previewCards) {
				if (c != null)
					c.upgrade();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void hover() {
		super.hover();
		if (!isRenderingTip) {
			isRenderingTip = true;
			cardPreviewing = this;
			cardToPreview.clear();
			for (AbstractCard card : previewCards)
				cardToPreview.add(card);
		}
	}

	@Override
	public void unhover() {
		super.unhover();
		if (isRenderingTip) {
			isRenderingTip = false;
			if (cardPreviewing == this) {
				cardToPreview.clear();
				cardPreviewing = null;
			}
		}
	}

	@Override
	public void untip() {
		super.untip();
		if (isRenderingTip) {
			isRenderingTip = false;
			if (cardPreviewing == this) {
				cardToPreview.clear();
				cardPreviewing = null;
			}
		}
	}
}
