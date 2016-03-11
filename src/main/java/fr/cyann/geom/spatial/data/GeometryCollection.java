package fr.cyann.geom.spatial.data;

import fr.cyann.geom.spatial.data.coord.XY;
import fr.cyann.geom.spatial.data.coord.XYM;
import fr.cyann.geom.spatial.data.coord.XYZ;
import fr.cyann.geom.spatial.data.coord.XYZM;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by cyann on 29/01/16.
 */
public class GeometryCollection extends Geometry {

    private final List<Geometry> geometries;

    public GeometryCollection() {
	    super(XY.class);
        this.geometries = new LinkedList<>();
    }

    public void addGeometry(Geometry geometry) {
        this.geometries.add(geometry);
    }

    @Override
    public void marshall(StringBuilder stringBuilder) throws BadGeometryException {
        stringBuilder.append("GEOMETRYCOLLECTION ");
        stringBuilder.append("(");

        boolean tail = false;
        for (Geometry geometry : geometries) {
            if (tail) stringBuilder.append(", ");
            geometry.marshall(stringBuilder);
            tail = true;
        }

        stringBuilder.append(")");
    }

    public static GeometryCollection unMarshall(String string) {
        if (string == null) return null;
        return unMarshall(new StringBuilder(string));
    }

    public static GeometryCollection unMarshall(StringBuilder string) {

        // 'GEOMETRYCOLLECTION'
        Parse.removeBlanks(string);
        if (!Parse.consumeSymbol(string, "GEOMETRYCOLLECTION")) return null;

        GeometryCollection geometryCollection = new GeometryCollection();

        // '('
        Parse.removeBlanks(string);
        if (!Parse.consumeSymbol(string, "(")) return null;

        // <geometry>*
        while (string.length() > 0) {

            // <geometry>
            Parse.removeBlanks(string);
            Geometry geometry;
            geometry = GeometryCollection.unMarshall(string);
            if (geometry == null)
                geometry = Point.unMarshall(XY.class, string);
	        if (geometry == null)
		        geometry = Point.unMarshall(XYZ.class, string);
	        if (geometry == null)
		        geometry = Point.unMarshall(XYM.class, string);
	        if (geometry == null)
		        geometry = Point.unMarshall(XYZM.class, string);
            if (geometry == null)
                geometry = LineString.unMarshall(XY.class, string);
	        if (geometry == null)
		        geometry = LineString.unMarshall(XYZ.class, string);
	        if (geometry == null)
		        geometry = LineString.unMarshall(XYM.class, string);
	        if (geometry == null)
		        geometry = LineString.unMarshall(XYZM.class, string);
            if (geometry == null)
                geometry = Polygon.unMarshall(XY.class, string);
	        if (geometry == null)
		        geometry = Polygon.unMarshall(XYZ.class, string);
	        if (geometry == null)
		        geometry = Polygon.unMarshall(XYM.class, string);
	        if (geometry == null)
		        geometry = Polygon.unMarshall(XYZM.class, string);
	        if (geometry == null)
		        geometry = PolyhedralSurface.unMarshall(XY.class, string);
	        if (geometry == null)
		        geometry = PolyhedralSurface.unMarshall(XYZ.class, string);
	        if (geometry == null)
		        geometry = PolyhedralSurface.unMarshall(XYM.class, string);
	        if (geometry == null)
		        geometry = PolyhedralSurface.unMarshall(XYZM.class, string);
            if (geometry == null) return null;
            geometryCollection.addGeometry(geometry);

            if (!Parse.nextSymbol(string, ",")) break;

            // ','
            Parse.removeBlanks(string);
            Parse.consumeSymbol(string, ",");

        }

        // ')'
        Parse.removeBlanks(string);
        if (!Parse.consumeSymbol(string, ")")) return null;
        return geometryCollection;
    }
}
