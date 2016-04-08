package fr.cyann.geom.spatial.data; /**
 * Copyright (C) 18/12/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

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
public class Polygon<C extends XY> extends Geometry {

	private final CoordList<C> exterior;
    private final List<CoordList<C>> interiors;

    public Polygon(Class<C> type) {
        this(type, new CoordList<C>(true));
    }

    Polygon(Class<C> type, CoordList<C> exterior) {
        super(type);
	    this.exterior = exterior;
        this.interiors = new ArrayList<>();
    }

	public static <C extends XY>Polygon<C> unMarshall(Class<C> type, String string) {
		if (string == null) return null;
		return unMarshall(type, new StringBuilder(string));
	}

	public static <C extends XY>Polygon<C> unMarshall(Class<C> type, StringBuilder stringBuilder) {

        // 'POLYGON'
        Parse.removeBlanks(stringBuilder);
	    Class<? extends XY> parsedType = getCoordType(stringBuilder, "POLYGON");
		if (type == null || !type.equals(parsedType)) return null;

        return unMarshall(type, stringBuilder);
    }

	static <C extends XY>Polygon<C> unMarshallData(Class<C> type, StringBuilder stringBuilder) {
		// '('
		Parse.removeBlanks(stringBuilder);
		if (!Parse.consumeSymbol(stringBuilder, '(')) return null;

		// <exterior>
		CoordList<C> exterior = CoordList.unMarshall(type, stringBuilder, true);
		if (exterior == null) return null;

		Polygon<C> polygon = new Polygon(type, exterior);

		// <interior>*
		while (stringBuilder.length() > 0 && Parse.nextSymbol(stringBuilder, ',')) {

			// ','
			Parse.consumeSymbol(stringBuilder, ',');
			Parse.removeBlanks(stringBuilder);

			// <interior>
			CoordList interior = CoordList.unMarshall(type, stringBuilder, true);
			if (interior == null) return null;
			polygon.addInterior(interior);

		}

		// ')'
		Parse.removeBlanks(stringBuilder);
		if (!Parse.consumeSymbol(stringBuilder, ')')) return null;

		return polygon;
	}
	public static Polygon<? extends XY> unMarshall(byte[] bytes) {
		ByteBuffer buffer = BinaryUtil.toByteBufferEndianness(bytes);
		int geometryType = buffer.getInt();
		if (geometryType == GeometryType.POLYGON.getCode()) return unMarshall(XY.class, buffer);
		if (geometryType == GeometryType.POLYGONZ.getCode()) return unMarshall(XYZ.class, buffer);
		if (geometryType == GeometryType.POLYGONM.getCode()) return unMarshall(XYM.class, buffer);
		if (geometryType == GeometryType.POLYGONZM.getCode()) return unMarshall(XYZM.class, buffer);
		return null;
	}

	public static <C extends XY> Polygon<C> unMarshall(Class<C> type, ByteBuffer buffer) {
		int size = buffer.getInt();
		CoordList<C> exterior = CoordList.unMarshall(type, buffer, true);

		Polygon<C> polygon = new Polygon<C>(type, exterior);

		for (int i = 1; i < size; i++) {
			polygon.addInterior(CoordList.unMarshall(type, buffer, true));
		}

		return polygon;
	}

	public Iterable<C> getExterior() {
		return exterior;
	}

	public void addExteriorCoordinate(C coord) {
		exterior.add(coord);
	}

    void addInterior(CoordList<C> xyList) {
        interiors.add(xyList);
    }

	public CoordList<C> createAndAddInterion() {
		CoordList<C> exterior = new CoordList<>(true);
		interiors.add(exterior);
		return exterior;
	}

    @Override
    public void marshall(StringBuilder stringBuilder) throws BadGeometryException {
        stringBuilder.append("POLYGON");
	    appendType(stringBuilder);
        stringBuilder.append(' ');
        marshallData(stringBuilder);
    }

	void marshallData(StringBuilder stringBuilder) throws BadGeometryException {
		stringBuilder.append('(');

		//if (getCoordinate().size() < 4) throw new BadGeometryException("POLYGON geometry should hava at least 4 coordinates!");
		exterior.marshall(stringBuilder);

		for (CoordList interior : interiors) {
			stringBuilder.append(", ");
			interior.marshall(stringBuilder);
		}

		stringBuilder.append(')');
	}
}
