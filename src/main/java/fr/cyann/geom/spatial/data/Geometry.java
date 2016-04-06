package fr.cyann.geom.spatial.data;

import fr.cyann.geom.spatial.data.coord.XY;
import fr.cyann.geom.spatial.data.coord.XYM;
import fr.cyann.geom.spatial.data.coord.XYZ;
import fr.cyann.geom.spatial.data.coord.XYZM;

/**
 * Created by cyann on 20/12/15.
 */
public abstract class Geometry<C extends XY> implements Marshallable {

    public static final int GPS_SRID = 4326;

    protected final Class<C> type;

    public Geometry(Class<C> type) {
        this.type = type;
    }

    public Class<C> getCoordinateType() {
        return type;
    }

    void appendType(StringBuilder stringBuilder) {
        if (XYZ.class == type) {
            stringBuilder.append("Z");
        } else if (XYM.class == type) {
            stringBuilder.append("M");
        } else if (XYZM.class == type) {
            stringBuilder.append("ZM");
        }
    }

    public static <C extends XY> Geometry<C> unMarshall(String string) {
        return unMarshall(new StringBuilder(string));
    }

    public static <C extends XY> Geometry<C> unMarshall(StringBuilder stringBuilder) {

        if (Parse.nextSymbol(stringBuilder, "POINTZM") || Parse.nextSymbol(stringBuilder, "POINT ZM")) {
            return Point.unMarshall(XYZM.class, stringBuilder);
        } else if (Parse.nextSymbol(stringBuilder, "POINTZ") || Parse.nextSymbol(stringBuilder, "POINT Z")) {
            return Point.unMarshall(XYZ.class, stringBuilder);
        } else if (Parse.nextSymbol(stringBuilder, "POINTM") || Parse.nextSymbol(stringBuilder, "POINT M")) {
            return Point.unMarshall(XYM.class, stringBuilder);
        } else if (Parse.nextSymbol(stringBuilder, "POINT")) {
            return Point.unMarshall(XY.class, stringBuilder);

        } else if (Parse.nextSymbol(stringBuilder, "LINESTRINGZM") || Parse.nextSymbol(stringBuilder, "LINESTRING ZM")) {
            return LineString.unMarshall(XYZM.class, stringBuilder);
        } else if (Parse.nextSymbol(stringBuilder, "LINESTRINGZ") || Parse.nextSymbol(stringBuilder, "LINESTRING Z")) {
            return LineString.unMarshall(XYZ.class, stringBuilder);
        } else if (Parse.nextSymbol(stringBuilder, "LINESTRINGM") || Parse.nextSymbol(stringBuilder, "LINESTRING M")) {
            return LineString.unMarshall(XYM.class, stringBuilder);
        } else if (Parse.nextSymbol(stringBuilder, "LINESTRING")) {
            return LineString.unMarshall(XY.class, stringBuilder);

        } else if (Parse.nextSymbol(stringBuilder, "POLYGONZM") || Parse.nextSymbol(stringBuilder, "POLYGON ZM")) {
            return Polygon.unMarshall(XYZM.class, stringBuilder);
        } else if (Parse.nextSymbol(stringBuilder, "POLYGONZ") || Parse.nextSymbol(stringBuilder, "POLYGON Z")) {
            return Polygon.unMarshall(XYZ.class, stringBuilder);
        } else if (Parse.nextSymbol(stringBuilder, "POLYGONM") || Parse.nextSymbol(stringBuilder, "POLYGON M")) {
            return Polygon.unMarshall(XYM.class, stringBuilder);
        } else if (Parse.nextSymbol(stringBuilder, "POLYGON")) {
            return Polygon.unMarshall(XY.class, stringBuilder);

        } else if (Parse.nextSymbol(stringBuilder, "POLYHEDRALSURFACEZM") || Parse.nextSymbol(stringBuilder, "POLYHEDRALSURFACE ZM")) {
            return PolyhedralSurface.unMarshall(XYZM.class, stringBuilder);
        } else if (Parse.nextSymbol(stringBuilder, "POLYHEDRALSURFACEZ") || Parse.nextSymbol(stringBuilder, "POLYHEDRALSURFACE Z")) {
            return PolyhedralSurface.unMarshall(XYZ.class, stringBuilder);
        } else if (Parse.nextSymbol(stringBuilder, "POLYHEDRALSURFACEM") || Parse.nextSymbol(stringBuilder, "POLYHEDRALSURFACE M")) {
            return PolyhedralSurface.unMarshall(XYM.class, stringBuilder);
        } else if (Parse.nextSymbol(stringBuilder, "POLYHEDRALSURFACE")) {
            return PolyhedralSurface.unMarshall(XY.class, stringBuilder);
        } else {
            return null;
        }
    }

	//public static Class<? extends XY>
    static Class<? extends XY> getCoordType(StringBuilder stringBuilder, String prefix) {
        if (Parse.consumeSymbol(stringBuilder, prefix + "ZM") || Parse.consumeSymbol(stringBuilder, prefix + " ZM")) {
            return XYZM.class;
        } else if (Parse.consumeSymbol(stringBuilder, prefix + "M") || Parse.consumeSymbol(stringBuilder, prefix + " M")) {
            return XYM.class;
        } else if (Parse.consumeSymbol(stringBuilder, prefix + "Z") || Parse.consumeSymbol(stringBuilder, prefix + " Z")) {
            return XYZ.class;
        } else if (Parse.consumeSymbol(stringBuilder, prefix)) {
            return XY.class;
        } else {
            return null;
        }
    }

    static <C extends XY> C unmarshallCoord(Class<C> type, StringBuilder stringBuilder) {
        XY coord = null;
        if (XY.class == type) {
            coord = XY.unMarshall(stringBuilder);
        }
        if (XYZ.class == type) {
            coord = XYZ.unMarshall(stringBuilder);
        }
        if (XYM.class == type) {
            coord = XYM.unMarshall(stringBuilder);
        }
        if (XYZM.class == type) {
            coord = XYZM.unMarshall(stringBuilder);
        }
        return (C) coord;
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
