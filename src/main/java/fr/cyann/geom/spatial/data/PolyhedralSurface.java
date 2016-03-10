package fr.cyann.geom.spatial.data;/**
 * Copyright (C) 10/03/16 Yann Caron aka cyann
 * <p>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

import fr.cyann.geom.spatial.data.coord.XY;

import java.util.ArrayList;
import java.util.List;

/**
 * The PolyhedralSurface definition.
 */
public class PolyhedralSurface<C extends XY> extends Geometry<XY> {

	private final List<CoordList<C>> faces;

	public PolyhedralSurface(Class<XY> type) {
		super(type);
		faces = new ArrayList<>();
	}

	public CoordList<C> addFace(CoordList<C> face) {
		faces.add(face);
		return face;
	}

	/**
	 * The abstract method that transform Geometry structure into string understandable by GeomFromText() spatialite function.
	 * see at http://www.gaia-gis.it/gaia-sins/spatialite-cookbook/html/wkt-wkb.html
	 *
	 * @param string
	 */
	@Override
	public void marshall(StringBuilder string) throws BadGeometryException {

	}
}
