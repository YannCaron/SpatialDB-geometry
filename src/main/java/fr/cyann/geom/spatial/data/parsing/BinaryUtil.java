package fr.cyann.geom.spatial.data.parsing;/**
 * Copyright (C) 07/04/16 Yann Caron aka cyann
 * <p>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

import fr.cyann.geom.spatial.data.coord.XY;
import fr.cyann.geom.spatial.data.coord.XYM;
import fr.cyann.geom.spatial.data.coord.XYZ;
import fr.cyann.geom.spatial.data.coord.XYZM;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * The BinaryUtil definition.
 */
public class BinaryUtil {

	public static ByteBuffer toByteBufferEndianness(byte[] bytes) {
		ByteBuffer buffer = ByteBuffer.wrap(bytes);
		byte endianess = buffer.get();
		if (endianess == 0x0) {
			buffer = buffer.order(ByteOrder.BIG_ENDIAN);
		} else {
			buffer = buffer.order(ByteOrder.LITTLE_ENDIAN);
		}

		return buffer;
	}

	public static <C extends XY> C unMarshallCoord(Class<C> type, ByteBuffer buffer) {
		double x = buffer.getDouble();
		double y = buffer.getDouble();

		if (type.equals(XYZ.class)) {
			double z = buffer.getDouble();
			return (C) new XYZ(x, y, z);
		} else if (type.equals(XYM.class)) {
			double m = buffer.getDouble();
			return (C) new XYM(x, y, m);
		} else if (type.equals(XYZM.class)) {
			double z = buffer.getDouble();
			double m = buffer.getDouble();
			return (C) new XYZM(x, y, z, m);
		} else {
			return (C) new XY(x, y);
		}
	}

	public static String bytesToHex(byte[] bytes, int max) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < bytes.length && i < max; i++) {
			buffer.append(Integer.toHexString(bytes[i] & 0xFF));
		}
		return buffer.toString().toUpperCase();
	}
}
