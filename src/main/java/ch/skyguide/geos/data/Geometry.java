package ch.skyguide.geos.data;

import ch.skyguide.geos.data.coord.XY;
import ch.skyguide.geos.data.coord.XYM;
import ch.skyguide.geos.data.coord.XYZ;
import ch.skyguide.geos.data.coord.XYZM;

/**
 * Created by cyann on 20/12/15.
 */
public abstract class Geometry<C extends XY> implements Marshallable {

	public static final int GPS_SRID = 4326;

	protected final Class<C> type;

	public Geometry(Class<C> type) {
		this.type = type;
	}

	void appendType(StringBuilder stringBuilder) {
		if (XYZ.class == type) stringBuilder.append("Z");
		else if (XYM.class == type) stringBuilder.append("M");
		else if (XYZM.class == type) stringBuilder.append("ZM");
	}

	static Class<? extends XY> getCoordType(StringBuilder stringBuilder, String prefix) {
		if (Parse.consumeSymbol(stringBuilder, prefix + "ZM")) return XYZM.class;
		else if (Parse.consumeSymbol(stringBuilder, prefix + "M")) return XYM.class;
		else if (Parse.consumeSymbol(stringBuilder, prefix + "Z")) return XYZ.class;
		else if (Parse.consumeSymbol(stringBuilder, prefix)) return XY.class;
		else return null;
	}

	static <C extends XY> C unmarshallCoord(Class<C> type, StringBuilder stringBuilder) {
		XY coord = null;
		if (XY.class == type) coord = XY.unMarshall(stringBuilder);
		if (XYZ.class == type) coord = XYZ.unMarshall(stringBuilder);
		if (XYM.class == type) coord = XYM.unMarshall(stringBuilder);
		if (XYZM.class == type) coord = XYZM.unMarshall(stringBuilder);
		return  (C)coord;
	}

	@Override
	public String toString() {
		StringBuilder string = new StringBuilder();
		try {
			marshall(string);
		} catch (BadGeometryException e) {
			e.printStackTrace();
		}
		return string.toString();
	}

	public String toSpatialiteQuery(int srid) throws BadGeometryException {
		StringBuilder string = new StringBuilder();
		string.append("ST_GeomFromText('");
		marshall(string);
		string.append("', " + srid + ")");
		return string.toString();
	}

}
