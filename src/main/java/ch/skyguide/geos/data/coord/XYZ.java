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
public class XYZ extends XY {

	private final double z;

	public XYZ(double x, double y, double z) {
		super(x, y);
		this.z = z;
	}

	public static XYZ unMarshall(StringBuilder string) {
		Parse.removeBlanks(string);
		Double x = Parse.consumeDouble(string);
		if (x == null) return null;
		Parse.removeBlanks(string);
		Double y = Parse.consumeDouble(string);
		if (y == null) return null;
		Parse.removeBlanks(string);
		Double z = Parse.consumeDouble(string);
		if (z == null) return null;
		return new XYZ(x, y, z);
	}

	@Override
	public void marshall(StringBuilder string) {
		super.marshall(string);
		string.append(' ');
		marshallNumber(string, z);
	}
}
