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
import java.util.ArrayList;
import java.util.List;

/**
 * The ch.skyguide.geos.loader.geom.LineString definition.
 */
public class MultiLineString<C extends XY> extends Geometry {

    public static final String WKT_NAME = "MULTILINESTRING";

    private final List<LineString<C>> lineStrings;

    public MultiLineString (Class<C> type, List<LineString<C>> lineStrings) {
        super(type);
        this.lineStrings = lineStrings;
    }

    protected MultiLineString (Class<C> type) {
        this(type, new ArrayList<>());
    }

    @Override
    public boolean equals (Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MultiLineString<?> that = (MultiLineString<?>) o;

        if (lineStrings == null && that.lineStrings != null) {
            return false;
        }
        if (lineStrings.size() != that.lineStrings.size()) {
            return false;
        }

        for (int i = 0; i < lineStrings.size(); i++) {
            if (!lineStrings.get(i).equals(that.lineStrings.get(i))) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int hashCode () {
        return lineStrings != null ? lineStrings.hashCode() : 0;
    }

    public void addLineString (LineString<C> lineString) {
        lineStrings.add(lineString);
    }

    public Iterable<LineString<C>> getLineStrings () {
        return lineStrings;
    }

    public static <C extends XY> MultiLineString<C> unMarshall (Class<C> type, String string) {
        if (string == null) {
            return null;
        }
        return unMarshall(type, new StringBuilder(string));
    }

    public static <C extends XY> MultiLineString<C> unMarshall (Class<C> type, StringBuilder stringBuilder) {

        // MULTILINESTRING
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

        MultiLineString<C> multiLineString = new MultiLineString(type);

        // <linestring>*
        boolean first = true;
        Parse.removeBlanks(stringBuilder);
        while (stringBuilder.length() > 0 && (first || Parse.nextSymbol(stringBuilder, ','))) {

            if (!first) {
                // ','
                Parse.consumeSymbol(stringBuilder, ',');
            }
            first = false;
            multiLineString.addLineString(LineString.unMarshallData(type, stringBuilder));
        }

        // ')'
        Parse.removeBlanks(stringBuilder);
        if (!Parse.consumeSymbol(stringBuilder, ')')) {
            return null;
        }

        return multiLineString;
    }

    public static MultiLineString<? extends XY> unMarshall (byte[] bytes) {
        ByteBuffer buffer = BinaryUtil.toByteBufferEndianness(bytes);
        int geometryType = buffer.getInt();
        if (geometryType == GeometryType.MULTILINESTRING.getCode()) {
            return unMarshall(XY.class, buffer);
        }
        if (geometryType == GeometryType.MULTILINESTRINGZ.getCode()) {
            return unMarshall(XYZ.class, buffer);
        }
        if (geometryType == GeometryType.MULTILINESTRINGM.getCode()) {
            return unMarshall(XYM.class, buffer);
        }
        if (geometryType == GeometryType.MULTILINESTRINGZM.getCode()) {
            return unMarshall(XYZM.class, buffer);
        }
        return null;
    }

    public static <C extends XY> MultiLineString<C> unMarshall (Class<C> type, ByteBuffer buffer) {
        MultiLineString<C> multiLineString = new MultiLineString<>(type);

        int size = buffer.getInt();

        for (int i = 1; i < size; i++) {
            buffer.get(); // endianess
            buffer.getInt(); // geometry type

            LineString<C> lineString = LineString.unMarshall(type, buffer);
            multiLineString.addLineString(lineString);
        }
        return multiLineString;
    }

    @Override
    public void marshall (StringBuilder stringBuilder) throws BadGeometryException {
        stringBuilder.append(WKT_NAME);
        appendType(stringBuilder);
        stringBuilder.append(' ');
        stringBuilder.append('(');

        boolean first = true;
        for (LineString<C> lineString : lineStrings) {
            if (!first) {
                stringBuilder.append(", ");
            }
            first = false;

            lineString.getCoordinate().marshall(stringBuilder);
        }

        stringBuilder.append(')');
    }

    @Override
    public void marshallToGeoJson (StringBuilder stringBuilder) {
        boolean first = true;

        stringBuilder.append('{');
        stringBuilder.append("\"type\": \"MultiLineString\", \"coordinates\": ");
        for (LineString<?> lineString : lineStrings) {
            if (!first) {
                stringBuilder.append(", ");
            }
            stringBuilder.append('[');
            lineString.getCoordinate().marshallToGeoJson(stringBuilder);
            stringBuilder.append(']');
            first = false;
        }
        stringBuilder.append('}');
    }

}
