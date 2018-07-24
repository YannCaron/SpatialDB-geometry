/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.cyann.geom.spatial.data;

import static fr.cyann.geom.spatial.data.Geometry.getCoordType;
import static fr.cyann.geom.spatial.data.MultiLineString.WKT_NAME;
import fr.cyann.geom.spatial.data.coord.XY;
import fr.cyann.geom.spatial.data.coord.XYM;
import fr.cyann.geom.spatial.data.coord.XYZ;
import fr.cyann.geom.spatial.data.coord.XYZM;
import fr.cyann.geom.spatial.data.parsing.BinaryUtil;
import fr.cyann.geom.spatial.data.parsing.GeometryType;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author cyann
 */
public class MultiPolygon<C extends XY> extends Geometry {

    public static final String WKT_NAME = "MULTIPOLYGON";

    private final List<Polygon<C>> polygons;

    public MultiPolygon (Class<C> type, List<Polygon<C>> polygons) {
        super(type);
        this.polygons = polygons;
    }

    protected MultiPolygon (Class<C> type) {
        this(type, new ArrayList<>());
    }

    public boolean addPolygon (Polygon<C> e) {
        return polygons.add(e);
    }

    public List<Polygon<C>> getPolygons () {
        return polygons;
    }

    public static <C extends XY> MultiPolygon<C> unMarshall (Class<C> type, String string) {
        if (string == null) {
            return null;
        }
        return unMarshall(type, new StringBuilder(string));
    }

    public static <C extends XY> MultiPolygon<C> unMarshall (Class<C> type, StringBuilder stringBuilder) {

        // MULTIPOLYGON
        Parse.removeBlanks(stringBuilder);
        Class<? extends XY> parsedType = getCoordType(stringBuilder, WKT_NAME);
        if (type == null || !type.equals(parsedType)) {
            return null;
        }

        // '('
        Parse.removeBlanks(stringBuilder);
        if (!Parse.consumeSymbol(stringBuilder, '(')) {
            return null;
        }

        MultiPolygon<C> multiPolygon = new MultiPolygon(type);

        // <linestring>*
        boolean first = true;
        Parse.removeBlanks(stringBuilder);
        while (stringBuilder.length() > 0 && (first || Parse.nextSymbol(stringBuilder, ','))) {

            if (!first) {
                // ','
                Parse.consumeSymbol(stringBuilder, ',');
            }
            first = false;
            multiPolygon.addPolygon(Polygon.unMarshallData(type, stringBuilder));
        }

        // ')'
        Parse.removeBlanks(stringBuilder);
        if (!Parse.consumeSymbol(stringBuilder, ')')) {
            return null;
        }

        return multiPolygon;
    }

    public static MultiPolygon<? extends XY> unMarshall (byte[] bytes) {
        ByteBuffer buffer = BinaryUtil.toByteBufferEndianness(bytes);
        int geometryType = buffer.getInt();
        if (geometryType == GeometryType.MULTIPOLYGON.getCode()) {
            return unMarshall(XY.class, buffer);
        }
        if (geometryType == GeometryType.MULTIPOLYGONZ.getCode()) {
            return unMarshall(XYZ.class, buffer);
        }
        if (geometryType == GeometryType.MULTIPOLYGONM.getCode()) {
            return unMarshall(XYM.class, buffer);
        }
        if (geometryType == GeometryType.MULTIPOLYGONZM.getCode()) {
            return unMarshall(XYZM.class, buffer);
        }
        return null;
    }

    public static <C extends XY> MultiPolygon<C> unMarshall (Class<C> type, ByteBuffer buffer) {
        MultiPolygon<C> multiPolygon = new MultiPolygon<>(type);

        int size = buffer.getInt();

        for (int i = 1; i < size; i++) {
            buffer.get(); // endianess
            buffer.getInt(); // geometry type

            Polygon<C> polygon = Polygon.unMarshall(type, buffer);
            multiPolygon.addPolygon(polygon);
        }
        return multiPolygon;
    }

    @Override
    public void marshall (StringBuilder stringBuilder) throws BadGeometryException {
        stringBuilder.append(WKT_NAME);
        appendType(stringBuilder);
        stringBuilder.append(' ');
        stringBuilder.append('(');

        boolean first = true;
        for (Polygon<C> polygon : polygons) {
            if (!first) {
                stringBuilder.append(", ");
            }
            first = false;

            polygon.marshallData(stringBuilder);
        }

        stringBuilder.append(')');
    }

    @Override
    public void marshallToGeoJson (StringBuilder stringBuilder) {
        boolean first = true;

        stringBuilder.append('{');
        stringBuilder.append("\"type\": \"MultiPolygon\", \"coordinates\": ");
        stringBuilder.append('[');
        for (Polygon<?> polygon : polygons) {
            if (!first) {
                stringBuilder.append(", ");
            }
            polygon.marshallToGeoJson(stringBuilder);
            first = false;
        }
        stringBuilder.append(']');
        stringBuilder.append('}');
    }

    @Override
    public boolean equals (Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MultiPolygon<?> that = (MultiPolygon<?>) o;

        if (polygons == null && that.polygons != null) {
            return false;
        }
        if (polygons.size() != that.polygons.size()) {
            return false;
        }

        for (int i = 0; i < polygons.size(); i++) {
            if (!polygons.get(i).equals(that.polygons.get(i))) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int hashCode () {
        return polygons != null ? polygons.hashCode() : 0;
    }

    
    
}
