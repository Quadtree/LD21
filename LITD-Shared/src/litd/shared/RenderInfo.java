package litd.shared;

import java.io.Serializable;

public class RenderInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -525094318453169127L;

	public static final byte PC_STRENGTH = 24;
	public static final byte PC_AGILITY = 25;
	public static final byte PC_ENDURANCE = 26;
	public static final byte PC_MAGIC = 27;
	public static final byte PC_AURA = 28;
	public static final byte DEST = 10;
	public static final byte TRANS_0 = 30;
	public static final byte TRANS_1 = 31;
	public static final byte TRANS_2 = 32;
	public static final byte TRANS_3 = 33;
	public static final byte TRANS_4 = 34;
	public static final byte TRANS_5 = 35;
	public static final byte TRANS_6 = 36;
	public static final byte TRANS_7 = 37;
	public static final byte TRANS_8 = 38;
	public static final byte TRANS_9 = 39;
	
	public static final byte SPEAR = 11;
	public static final byte SWORD = 12;
	public static final byte STAFF = 13;
	public static final byte WARHAMMER = 14;
	public static final byte LEATHER_ARMOR = 15;
	public static final byte PLATE_ARMOR = 16;
	public static final byte ROBE = 17;
	
	public static final byte CARVING = 40;
	
	public static final byte GOBLIN = 41;
	public static final byte GOBLIN_SORC = 42;
	public static final byte TENTACLE = 43;
	public static final byte CAVE_SLUG = 44;
	public static final byte DEMON = 45;
	public static final byte LICH = 46;
	public static final byte FROST_ELEMENTAL = 47;
	public static final byte FIRE_ELEMENTAL = 48;
	public static final byte DEATH_SPIRIT = 49;
	
	public static final byte EXPLOSION = 51;
	public static final byte CLEAVE = 52;
	public static final byte EFF_SLEEP = 53;
	public static final byte EFF_BLESS = 54;
	public static final byte EFF_CURSE = 55;
	public static final byte EFF_SHIELD = 56;
	public static final byte EFF_STUN = 57;
	public static final byte EFF_INJURED = 58;
	public static final byte EFF_DYING = 59;
	
	public byte x,y;
	public byte sprite;
	
	public RenderInfo(byte x, byte y, byte sprite) {
		super();
		this.x = x;
		this.y = y;
		this.sprite = sprite;
	}

	@Override
	public String toString() {
		return sprite + " at " + x + ", " + y;
	}
}
