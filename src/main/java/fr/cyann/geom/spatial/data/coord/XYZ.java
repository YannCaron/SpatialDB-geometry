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
public class XYZ extends XY {

	private final double z;

	public XYZ(double x, double y, double z) {
		super(x, y);
		this.z = z;
	}

	public double getZ() {
		return z;
	}

	public static XYZ unMarshall(StringBuilder string) {
		XY xy = XY.unMarshall(string);
		Marshallable.Parse.removeBlanks(string);

		Double z = Marshallable.Parse.consumeDouble(string);
		if (z == null) return null;
		return new XYZ(xy.getX(), xy.getY(), z);
	}

	@Override
	public void marshall(StringBuilder stringBuilder) {
		super.marshall(stringBuilder);
		stringBuilder.append(' ');
		marshallNumber(stringBuilder, z);
	}

	@Override
	public String toString() {
		return "XYZM{" +
				"x=" + getX() +
				", y=" + getY() +
				", z=" + z +
				'}';
	}

}
