package fr.cyann.geom.spatial.data;

import fr.cyann.geom.spatial.data.coord.XY;
import fr.cyann.geom.spatial.data.coord.XYM;
import fr.cyann.geom.spatial.data.coord.XYZ;
import fr.cyann.geom.spatial.data.coord.XYZM;
import junit.framework.TestCase;

/**
 * Copyright (C) 06/03/16 Yann Caron aka cyann
 * <p>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/
public class LineStringTest extends TestCase {

	public void testMarshall() throws Exception {

		LineString<XY> pt1 = new LineString<XY>(XY.class).addCoordinate(new XY(10, 20)).addCoordinate(new XY(20, 20));
		LineString<XYZ> pt2 = new LineString<XYZ>(XYZ.class).addCoordinate(new XYZ(10, 20, 30)).addCoordinate(new XYZ(20, 20, 30));
		LineString<XYM> pt3 = new LineString<XYM>(XYM.class).addCoordinate(new XYM(10, 20, 3)).addCoordinate(new XYM(20, 20, 3));
		LineString<XYZM> pt4 = new LineString<XYZM>(XYZM.class).addCoordinate(new XYZM(10, 20, 30, 3)).addCoordinate(new XYZM(20, 20, 30, 3));

		assertEquals("LINESTRING (10 20, 20 20)", pt1.toString());
		assertEquals("LINESTRINGZ (10 20 30, 20 20 30)", pt2.toString());
		assertEquals("LINESTRINGM (10 20 3, 20 20 3)", pt3.toString());
		assertEquals("LINESTRINGZM (10 20 30 3, 20 20 30 3)", pt4.toString());

	}

	public void testUnMarshall() throws Exception {

		LineString<XY> pt1 = new LineString<XY>(XY.class).addCoordinate(new XY(10, 20)).addCoordinate(new XY(20, 20));
		LineString<XYZ> pt2 = new LineString<XYZ>(XYZ.class).addCoordinate(new XYZ(10, 20, 30)).addCoordinate(new XYZ(20, 20, 30));
		LineString<XYM> pt3 = new LineString<XYM>(XYM.class).addCoordinate(new XYM(10, 20, 3)).addCoordinate(new XYM(20, 20, 3));
		LineString<XYZM> pt4 = new LineString<XYZM>(XYZM.class).addCoordinate(new XYZM(10, 20, 30, 3)).addCoordinate(new XYZM(20, 20, 30, 3));

		assertEquals(pt1, LineString.unMarshall("LINESTRING (10 20, 20 20)"));
		assertEquals(pt2, LineString.unMarshall("LINESTRINGZ (10 20 30, 20 20 30)"));
		assertEquals(pt3, LineString.unMarshall("LINESTRINGM (10 20 3, 20 20 3)"));
		assertEquals(pt4, LineString.unMarshall("LINESTRINGZM (10 20 30 3, 20 20 30 3)"));

	}
}