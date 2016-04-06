package fr.cyann.geom.spatial.data;

import fr.cyann.geom.spatial.data.coord.XY;
import fr.cyann.geom.spatial.data.coord.XYM;
import fr.cyann.geom.spatial.data.coord.XYZ;
import fr.cyann.geom.spatial.data.coord.XYZM;
import junit.framework.TestCase;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Copyright (C) 06/03/16 Yann Caron aka cyann
 * <p>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/
public class PointTest extends TestCase {

	public static final byte[] POINT = new byte[]{ // POINT (10.0, 10.0)
			0x01, // 01
			0x01, 0x00, 0x00, 0x00,  // POINT
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x24, 0x40, // 10.0
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x24, 0x40  // 10.0
	};

	public void testMarshall() throws Exception {

		Point<XY> pt1 = Point.newPoint(10, 20);
		Point<XYZ> pt2 = Point.newPointZ(10, 20, 30);
		Point<XYM> pt3 = Point.newPointM(10, 20, 3);
		Point<XYZM> pt4 = Point.newPointZM(10, 20, 30, 3);

		assertEquals("POINT (10 20)", pt1.toString());
		assertEquals("POINTZ (10 20 30)", pt2.toString());
		assertEquals("POINTM (10 20 3)", pt3.toString());
		assertEquals("POINTZM (10 20 30 3)", pt4.toString());

	}


	public void testUnMarshall() throws Exception {

		Point<XY> pt1 = Point.newPoint(10, 20);
		Point<XYZ> pt2 = Point.newPointZ(10, 20, 30);
		Point<XYM> pt3 = Point.newPointM(10, 20, 3);
		Point<XYZM> pt4 = Point.newPointZM(10, 20, 30, 3);

		assertEquals(pt1, Point.unMarshall(XY.class, "POINT (10 20)"));
		assertEquals(pt2, Point.unMarshall(XYZ.class, "POINTZ (10 20 30)"));
		assertEquals(pt3, Point.unMarshall(XYM.class, "POINTM (10 20 3)"));
		assertEquals(pt4, Point.unMarshall(XYZM.class, "POINTZM (10 20 30 3)"));
	}


	public static String bytesToHex(byte[] bytes) {
		StringBuffer buffer = new StringBuffer();
		for (int i=0; i<bytes.length; i++) {
			buffer.append(Integer.toHexString(bytes[i] & 0xF));
		}
		return buffer.toString().toUpperCase();
	}

	public void testBinaryUnMarshall() throws Exception {

		ByteBuffer byteBuffer = ByteBuffer.wrap(POINT);
		byte order = byteBuffer.get();

		if (order == 0x00) {
			byteBuffer.order(ByteOrder.BIG_ENDIAN);
		} else {
			byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
		}

		int geometryType = byteBuffer.getInt();
		System.out.println(geometryType);

		double x = byteBuffer.getDouble();
		double y = byteBuffer.getDouble();

		System.out.println(x);
		System.out.println(y);

	}


}