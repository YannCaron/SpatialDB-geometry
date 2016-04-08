package fr.cyann.geom.spatial.data;

import fr.cyann.geom.spatial.data.coord.XY;
import fr.cyann.geom.spatial.data.coord.XYZ;
import junit.framework.TestCase;

/**
 * Copyright (C) 10/03/16 Yann Caron aka cyann
 * <p>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/
public class PolyhedralSurfaceTest extends TestCase {

	public void testUnMarshall() throws Exception {
		PolyhedralSurface<XY> pt1 = new PolyhedralSurface(XY.class);
		pt1.addPolygon(new Polygon(XY.class, new CoordList<XY>(true).add(new XY(10, 20)).add(new XY(20, 20)).add(new XY(20, 10))));

		PolyhedralSurface<XYZ> pt2 = new PolyhedralSurface(XYZ.class);
		pt2.addPolygon(new Polygon(XYZ.class, new CoordList<XYZ>(true).add(new XYZ(10, 20, 30))));
		pt2.addPolygon(new Polygon(XYZ.class, new CoordList<XYZ>(true).add(new XYZ(20, 20, 30))));
		pt2.addPolygon(new Polygon(XYZ.class, new CoordList<XYZ>(true).add(new XYZ(20, 10, 30))));
		pt2.addPolygon(new Polygon(XYZ.class, new CoordList<XYZ>(true).add(new XYZ(10, 20, 30))));

		assertEquals("POLYHEDRALSURFACE (((10 20, 20 20, 20 10, 10 20)))", pt1.toString());
		assertEquals("POLYHEDRALSURFACEZ (((10 20 30)), ((20 20 30)), ((20 10 30)), ((10 20 30)))", pt2.toString());

	}

	public void testMarshall() throws Exception {
		PolyhedralSurface<XY> pt1 = new PolyhedralSurface(XY.class);
		pt1.addPolygon(new Polygon(XY.class, new CoordList<XY>(true).add(new XY(10, 20)).add(new XY(20, 20)).add(new XY(20, 10))));

		PolyhedralSurface<XYZ> pt2 = new PolyhedralSurface(XYZ.class);
		pt2.addPolygon(new Polygon(XYZ.class, new CoordList<XYZ>(true).add(new XYZ(10, 20, 30))));
		pt2.addPolygon(new Polygon(XYZ.class, new CoordList<XYZ>(true).add(new XYZ(20, 20, 30))));
		pt2.addPolygon(new Polygon(XYZ.class, new CoordList<XYZ>(true).add(new XYZ(20, 10, 30))));
		pt2.addPolygon(new Polygon(XYZ.class, new CoordList<XYZ>(true).add(new XYZ(10, 20, 30))));

		assertEquals(pt1.toString(), PolyhedralSurface.unMarshall(XY.class, "POLYHEDRALSURFACE (((10 20, 20 20, 20 10, 10 20)))").toString());
		assertEquals(pt2.toString(), PolyhedralSurface.unMarshall(XYZ.class, "POLYHEDRALSURFACEZ (((10 20 30)), ((20 20 30)), ((20 10 30)), ((10 20 30)))").toString());
	}
}