package zombiefu.items;

import jade.util.datatype.ColoredChar;

/**
 * 
 * @author tomas
 */
public class Waffe extends Item {

	private int damage;
	private boolean ranged;
	private boolean directed;
	private Waffentyp wtyp;
	private int blastRadius = 2; // TODO: Individuell
	private int range = 6; // TODO: Individuell

	public Waffe(ColoredChar c, String n, int d, Waffentyp w) {
		super(c, n);
		damage = d;
		wtyp = w;
	}

	public Waffe(ColoredChar c, String n, int damage, int range,
			int blastRadius, Waffentyp w) {
		this(c, n, damage, w);
		this.blastRadius = blastRadius;
		this.range = range;
	}

	public Waffe(ColoredChar c, String n, int damage, int anderes, Waffentyp w) {
		this(c, n, damage, w);
		if (w.equals(Waffentyp.FERNKAMPF)) {
			this.range = anderes;
		} else {
			this.blastRadius = anderes;
		}
	}

	public Waffe(ColoredChar c, String n, int d) {
		this(c, n, d, Waffentyp.NAHKAMPF);
	}

	public int getDamage() {
		return damage;
	}

	public int getRange() {
		return range;
	}

	public int getBlastRadius() {
		return blastRadius;
	}

	public Waffentyp getTyp() {
		return wtyp;
	}

}