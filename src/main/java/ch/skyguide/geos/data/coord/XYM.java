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
public class XYM extends XY {

	private final double m;

	public XYM(double x, double y, double m) {
		super(x, y);
		this.m = m;
	}

	public static XYM unMarshall(StringBuilder string) {
		Parse.removeBlanks(string);
		Double x = Parse.consumeDouble(string);
		if (x == null) return null;
		Parse.removeBlanks(string);
		Double y = Parse.consumeDouble(string);
		if (y == null) return null;
		Parse.removeBlanks(string);
		Double m = Parse.consumeDouble(string);
		if (m == null) return null;
		return new XYM(x, y, m);
	}

	@Override
	public void marshall(StringBuilder string) {
		super.marshall(string);
		string.append(' ');
		marshallNumber(string, m);
	}
}
