package ch.skyguide.geos.data; /**
 * Copyright (C) 18/12/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

import ch.skyguide.geos.data.coord.XY;

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

	public CoordList getCoordinate() {
		return coordinate;
	}

	public LineString<C> addCoordinate(C coordinate) {
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

    public static LineString<? extends XY> unMarshall(String string) {
        if (string == null) return null;
        return unMarshall(new StringBuilder(string));
    }

    public static LineString<? extends XY> unMarshall(StringBuilder stringBuilder) {

	    Parse.removeBlanks(stringBuilder);

	    Class<? extends XY> type = getCoordType(stringBuilder, "LINESTRING");
	    if (type == null) return null;

        CoordList<? extends XY> list = CoordList.unMarshall(type, stringBuilder, false);

        return new LineString(type, list);
    }


}
