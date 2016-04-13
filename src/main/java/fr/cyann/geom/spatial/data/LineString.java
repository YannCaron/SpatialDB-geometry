package fr.cyann.geom.spatial.data; /**
 * Copyright (C) 18/12/15 Yann Caron aka cyann
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
import fr.cyann.geom.spatial.data.parsing.BinaryUtil;
import fr.cyann.geom.spatial.data.parsing.GeometryType;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Stack;

/**
 * The ch.skyguide.geos.loader.geom.LineString definition.
 */
public class LineString<C extends XY> extends Geometry {

	private final CoordList<C> coordinate;

	LineString(Class<C> type, CoordList<C> coordinate) {
		super(type);
		this.coordinate = coordinate;
	}

	protected LineString(Class<C> type, boolean closed) {
		this(type, new CoordList<C>(closed));
	}

	public LineString(Class<C> type) {
		this(type, new CoordList(false));
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		LineString<?> that = (LineString<?>) o;

		return !(coordinate != null ? !coordinate.equals(that.coordinate) : that.coordinate != null);

	}

	@Override
	public int hashCode() {
		return coordinate != null ? coordinate.hashCode() : 0;
	}

	public CoordList<C> getCoordinate() {
		return coordinate;
	}

	public LineString<C> addToCoordinate(C coordinate) {
		this.coordinate.add(coordinate);
		return this;
	}

	@Override
	public void marshall(StringBuilder stringBuilder) throws BadGeometryException {
		stringBuilder.append("LINESTRING");
		appendType(stringBuilder);
		stringBuilder.append(' ');
		coordinate.marshall(stringBuilder);
	}

	public static <C extends XY> LineString<C> unMarshall(Class<C> type, String string) {
		if (string == null) return null;
		return unMarshall(type, new StringBuilder(string));
	}

	public static <C extends XY> LineString<C> unMarshall(Class<C> type, StringBuilder stringBuilder) {

		// LINESTRING
		Parse.removeBlanks(stringBuilder);
		Class<? extends XY> parsedType = getCoordType(stringBuilder, "LINESTRING");
		if (type == null || !type.equals(parsedType)) return null;

		CoordList<C> list = CoordList.unMarshall(type, stringBuilder, false);
		if (list == null) return null;

		return new LineString<C>(type, list);
	}

	public static LineString<? extends XY> unMarshall(byte[] bytes) {
		ByteBuffer buffer = BinaryUtil.toByteBufferEndianness(bytes);
		int geometryType = buffer.getInt();
		if (geometryType == GeometryType.LINESTRING.getCode()) return unMarshall(XY.class, buffer);
		if (geometryType == GeometryType.LINESTRINGZ.getCode()) return unMarshall(XYZ.class, buffer);
		if (geometryType == GeometryType.LINESTRINGM.getCode()) return unMarshall(XYM.class, buffer);
		if (geometryType == GeometryType.LINESTRINGZM.getCode()) return unMarshall(XYZM.class, buffer);
		return null;
	}

	public static <C extends XY> LineString<C> unMarshall(Class<C> type, ByteBuffer buffer) {
		return new LineString<C>(type, CoordList.unMarshall(type, buffer, false));
	}

	public static LineString<XYZM> simplify(LineString<XYZM> lineString, double delta) {
		class Bounds {
			final int begin;
			final int end;
			final int endOfCreated;

			public Bounds(int begin, int end, int endOfCreated) {
				this.begin = begin;
				this.end = end;
				this.endOfCreated = endOfCreated;
			}
		}

		CoordList<XYZM> resCoord = new CoordList<>(lineString.getCoordinate().isClosed());
		List<XYZM> coords = lineString.getCoordinate().getCoords();

		Stack<Bounds> bounds = new Stack<Bounds>();
		bounds.add(new Bounds(0, coords.size(), 1));
		resCoord.add(coords.get(0));
		resCoord.add(coords.get(coords.size() - 1));

		while (!bounds.isEmpty()) {
			Bounds bound = bounds.pop();
			XYZM a = coords.get(bound.begin);
			XYZM b = coords.get(bound.end - 1);

			double maxDist = 0;
			int pivot = -1;
			for (int i = bound.begin; i < bound.end; i++) {
				double dist = coords.get(i).distanceToSegment(a, b);
				if (dist > delta && dist > maxDist) {
					maxDist = dist;
					pivot = i;
				}
			}

			if (pivot != -1) {
				XYZM p = coords.get(pivot);
				resCoord.add(bound.endOfCreated, p);
				bounds.add(new Bounds(bound.begin, pivot, bound.endOfCreated));
				bounds.add(new Bounds(pivot, bound.end, bound.endOfCreated + 1));
			}
		}


		return new LineString<XYZM>(lineString.getCoordinateType(), resCoord);
	}

}
