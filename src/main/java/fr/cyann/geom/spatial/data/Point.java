package fr.cyann.geom.spatial.data;

/**
 * Copyright (C) 18/12/15 Yann Caron aka cyann
 * <p>
 * Cette œuvre est mise à disposition sous licence Attribution - Pas
 * d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez
 * http://creativecommons.org/licenses/by-nc-sa/3.0/fr/ ou écrivez à Creative
 * Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 *
 */

import fr.cyann.geom.spatial.data.coord.XY;
import fr.cyann.geom.spatial.data.coord.XYM;
import fr.cyann.geom.spatial.data.coord.XYZ;
import fr.cyann.geom.spatial.data.coord.XYZM;
import fr.cyann.geom.spatial.data.parsing.BinaryUtil;
import fr.cyann.geom.spatial.data.parsing.GeometryType;

import java.nio.ByteBuffer;

/**
 * The ch.skyguide.geos.loader.geom.Point definition.
 */
public class Point<C extends XY> extends Geometry {

    public static final String WKT_NAME = "POINT";

    private final C coordinate;

    public Point (C coordinate) {
        super((Class<C>) coordinate.getClass());
        this.coordinate = coordinate;
    }

    public static Point<XY> newPoint (double x, double y) {
        return new Point<>(new XY(x, y));
    }

    public static Point<XYZ> newPointZ (double x, double y, double z) {
        return new Point<>(new XYZ(x, y, z));
    }

    public static Point<XYM> newPointM (double x, double y, double m) {
        return new Point<>(new XYM(x, y, m));
    }

    public static Point<XYZM> newPointZM (double x, double y, double z, double m) {
        return new Point<>(new XYZM(x, y, z, m));
    }

    @Override
    public boolean equals (Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Point<?> point = (Point<?>) o;

        return !(coordinate != null ? !coordinate.equals(point.coordinate) : point.coordinate != null);

    }

    @Override
    public int hashCode () {
        return coordinate != null ? coordinate.hashCode() : 0;
    }

    @Override
    public void marshall (StringBuilder stringBuilder) {
        stringBuilder.append(WKT_NAME);
        appendType(stringBuilder);
        stringBuilder.append(' ');
        stringBuilder.append('(');
        coordinate.marshall(stringBuilder);
        stringBuilder.append(')');
    }

    public static <C extends XY> Point<C> unMarshall (Class<C> type, String string) {
        if (string == null) {
            return null;
        }
        return unMarshall(type, new StringBuilder(string));
    }

    public static <C extends XY> Point<C> unMarshall (Class<C> type, StringBuilder stringBuilder) {
        Parse.removeBlanks(stringBuilder);

        Class<? extends XY> parsedType = getCoordType(stringBuilder, WKT_NAME);
        if (type == null || !type.equals(parsedType)) {
            return null;
        }

        Parse.removeBlanks(stringBuilder);
        if (!Parse.consumeSymbol(stringBuilder, '(')) {
            return null;
        }

        XY coord = unmarshallCoord(type, stringBuilder);
        if (coord == null) {
            return null;
        }

        Parse.removeBlanks(stringBuilder);
        if (!Parse.consumeSymbol(stringBuilder, ')')) {
            return null;
        }
        return new Point(coord);
    }

    public static Point<? extends XY> unMarshall (byte[] bytes) {
        ByteBuffer buffer = BinaryUtil.toByteBufferEndianness(bytes);
        int geometryType = buffer.getInt();
        if (geometryType == GeometryType.POINT.getCode()) {
            return unMarshall(XY.class, buffer);
        }
        if (geometryType == GeometryType.POINTZ.getCode()) {
            return unMarshall(XYZ.class, buffer);
        }
        if (geometryType == GeometryType.POINTM.getCode()) {
            return unMarshall(XYM.class, buffer);
        }
        if (geometryType == GeometryType.POINTZM.getCode()) {
            return unMarshall(XYZM.class, buffer);
        }
        return null;
    }

    public static <C extends XY> Point<C> unMarshall (Class<C> type, ByteBuffer buffer) {
        return new Point(BinaryUtil.unMarshallCoord(type, buffer));
    }

    public C getCoordinate () {
        return coordinate;
    }

    @Override
    public void marshallToGeoJson (StringBuilder stringBuilder) {
        stringBuilder.append('{');
        stringBuilder.append("\"type\": \"Point\", \"coordinated\": ");
        coordinate.marshallToGeoJson(stringBuilder);
        stringBuilder.append('}');
    }

}
