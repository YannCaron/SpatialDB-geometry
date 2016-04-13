package fr.cyann.geom.spatial.data.coord;/**
 * Copyright (C) 04/03/16 Yann Caron aka cyann
 * <p>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

import fr.cyann.geom.spatial.data.Marshallable;

/**
 * The XYZ definition.
 */
public class XYM extends XY {

	final double m;

	public XYM(double x, double y, double m) {
		super(x, y);
		this.m = m;
	}

	public static XYM unMarshall(StringBuilder string) {
		XY xy = XY.unMarshall(string);
		Marshallable.Parse.removeBlanks(string);
		Double m = Marshallable.Parse.consumeDouble(string);
		if (m == null) return null;
		return new XYM(xy.getX(), xy.getY(), m);
	}

	@Override
	public void marshall(StringBuilder stringBuilder) {
		super.marshall(stringBuilder);
		stringBuilder.append(' ');
		marshallNumber(stringBuilder, m);
	}

	@Override
	public String toString() {
		return "XYZM{" +
				"x=" + getX() +
				", y=" + getY() +
				", m=" + m +
				'}';
	}

}
