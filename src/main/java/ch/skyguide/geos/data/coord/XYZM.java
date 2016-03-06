package ch.skyguide.geos.data.coord;/**
 * Copyright (C) 04/03/16 Yann Caron aka cyann
 * <p>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

/**
 * The XYZ definition.
 */
public class XYZM extends XYZ {

	private final double m;

	public XYZM(double x, double y, double z, double m) {
		super(x, y, z);
		this.m = m;
	}

	public static XYZM unMarshall(StringBuilder string) {
		Parse.removeBlanks(string);
		Double x = Parse.consumeDouble(string);
		if (x == null) return null;
		Parse.removeBlanks(string);
		Double y = Parse.consumeDouble(string);
		if (y == null) return null;
		Parse.removeBlanks(string);
		Double z = Parse.consumeDouble(string);
		if (z == null) return null;
		Parse.removeBlanks(string);
		Double m = Parse.consumeDouble(string);
		if (m == null) return null;
		return new XYZM(x, y, z, m);
	}

	@Override
	public void marshall(StringBuilder string) {
		super.marshall(string);
		string.append(' ');
		marshallNumber(string, m);
	}
}
