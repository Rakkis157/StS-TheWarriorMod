package thewarrior.cards;

import basemod.abstracts.CustomCard;
import thewarrior.TheWarriorMod;
import thewarrior.characters.TheWarriorEnum;

public abstract class AbstractWarriorCard extends CustomCard {
	public static final String tmpCardId = TheWarriorMod.MOD_ID + ':' + "tmp";

	public AbstractWarriorCard(String id, String name, int cost, String rawDescription, CardType type, CardRarity rarity,
			CardTarget target) {
		super(id, name, "images/cards/" + id.substring(new String(TheWarriorMod.MOD_ID + ':').length()) + ".png", cost, rawDescription,
				type, TheWarriorEnum.WARRIOR_GREY, rarity, target);
	}

	public AbstractWarriorCard(String id, String name, String img, int cost, String rawDescription, CardType type, CardRarity rarity,
			CardTarget target) {
		super(id, name, img, cost, rawDescription, type, TheWarriorEnum.WARRIOR_GREY, rarity, target);
	}

}
