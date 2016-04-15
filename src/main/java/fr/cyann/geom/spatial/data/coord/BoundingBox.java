package fr.cyann.geom.spatial.data.coord;/**
 * Copyright (C) 15/04/16 Yann Caron aka cyann
 * <p>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

/**
 * The BoundingBox definition.
 */
public class BoundingBox<T extends XY> {

	protected T min;
	protected T max;
	private final Class<T> type;

	public BoundingBox(Class<T> type) {
		this.type = type;
		try {
			min = type.newInstance();
			max = type.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	public void cover(T point) {
		if (point.getX() < min.getX()) min.setX(point.getX());
		if (point.getX() > max.getX()) max.setX(point.getX());

		if (point.getY() < min.getY()) min.setY(point.getY());
		if (point.getY() > max.getY()) max.setY(point.getY());

		if (XYZ.class.equals(type) || XYZM.class.equals(type)) {
			XYZ minZ = (XYZ) min;
			XYZ maxZ = (XYZ) max;
			XYZ pointZ = (XYZ) point;

			if (pointZ.getZ() < minZ.getZ()) minZ.setZ(pointZ.getZ());
			if (pointZ.getZ() > maxZ.getZ()) maxZ.setZ(pointZ.getZ());
		}

		if (XYM.class.equals(type) || XYZM.class.equals(type)) {
			XYM minM = (XYM) min;
			XYM maxM = (XYM) max;
			XYM pointM = (XYM) point;

			if (pointM.getM() < minM.getM()) minM.setM(pointM.getM());
			if (pointM.getM() > maxM.getM()) maxM.setM(pointM.getM());

		}
	}

}
