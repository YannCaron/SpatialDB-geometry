package fr.cyann.geom.spatial.data;

/**
 * Copyright (C) 10/03/16 Yann Caron aka cyann
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
 * The PolyhedralSurface definition.
 */
public class PolyhedralSurface<C extends XY> extends Geometry {

    private final List<Polygon<C>> polygons;

    public PolyhedralSurface (Class<XY> type) {
        super(type);
        polygons = new ArrayList<>();
    }

    @Override
    public boolean equals (Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PolyhedralSurface<?> that = (PolyhedralSurface<?>) o;

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

    void addPolygon (Polygon polygon) {
        polygons.add(polygon);
    }

    public Iterable<Polygon<C>> getPolygons () {
        return polygons;
    }

    public static <C extends XY> PolyhedralSurface<C> unMarshall (Class<C> type, String stringBuilder) {
        if (stringBuilder == null) {
            return null;
        }
        return unMarshall(type, new StringBuilder(stringBuilder));
    }

    public static <C extends XY> PolyhedralSurface<C> unMarshall (Class<C> type, StringBuilder stringBuilder) {

        // 'POLYHEDRALSURFACE'
        Parse.removeBlanks(stringBuilder);
        Class<? extends XY> parsedType = getCoordType(stringBuilder, "POLYHEDRALSURFACE");
        if (type == null || !type.equals(parsedType)) {
            return null;
        }

        // '('
        Parse.removeBlanks(stringBuilder);
        if (!Parse.consumeSymbol(stringBuilder, '(')) {
            return null;
        }

        PolyhedralSurface<C> polyhedralSurface = new PolyhedralSurface(type);

        // <polygon>*
        boolean first = true;
        Parse.removeBlanks(stringBuilder);
        while (stringBuilder.length() > 0 && (first || Parse.nextSymbol(stringBuilder, ','))) {

            if (!first) {
                // ','
                Parse.consumeSymbol(stringBuilder, ',');
            }
            first = false;
            polyhedralSurface.addPolygon(Polygon.unMarshallData(type, stringBuilder));
        }

        // ')'
        Parse.removeBlanks(stringBuilder);
        if (!Parse.consumeSymbol(stringBuilder, ')')) {
            return null;
        }

        return polyhedralSurface;
    }

    public static PolyhedralSurface<? extends XY> unMarshall (byte[] bytes) {
        ByteBuffer buffer = BinaryUtil.toByteBufferEndianness(bytes);
        int geometryType = buffer.getInt();
        if (geometryType == GeometryType.POLYHEDRALSURFACE.getCode()) {
            return unMarshall(XY.class, buffer);
        }
        if (geometryType == GeometryType.POLYHEDRALSURFACEZ.getCode()) {
            return unMarshall(XYZ.class, buffer);
        }
        if (geometryType == GeometryType.POLYHEDRALSURFACEM.getCode()) {
            return unMarshall(XYM.class, buffer);
        }
        if (geometryType == GeometryType.POLYHEDRALSURFACEM.getCode()) {
            return unMarshall(XYZM.class, buffer);
        }
        return null;
    }

    public static <C extends XY> PolyhedralSurface<C> unMarshall (Class<C> type, ByteBuffer buffer) {
        PolyhedralSurface<C> polyhedralSurface = new PolyhedralSurface(type);
        int polygonSize = buffer.getInt();

        for (int ng = 0; ng < polygonSize; ng++) {
            buffer.get(); // endianess
            buffer.getInt(); // geometry type

            Polygon<C> polygon = Polygon.unMarshall(type, buffer);
            polyhedralSurface.addPolygon(polygon);
        }

        return polyhedralSurface;
    }

    /**
     * The abstract method that transform Geometry structure into string
     * understandable by GeomFromText() spatialite function. see at
     * http://www.gaia-gis.it/gaia-sins/spatialite-cookbook/html/wkt-wkb.html
     *
     * @param stringBuilder
     */
    @Override
    public void marshall (StringBuilder stringBuilder) throws BadGeometryException {
        stringBuilder.append("POLYHEDRALSURFACE");
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
        stringBuilder.append("\"type\": \"Polyhedralsurface\", \"coordinates\": ");
        stringBuilder.append('[');
        for (Polygon<?> polygon : polygons) {
            if (!first) {
                stringBuilder.append(", ");
            }
            polygon.marshallDataToGeoJson(stringBuilder);
            first = false;
        }
        stringBuilder.append(']');
        stringBuilder.append('}');
    }
}
