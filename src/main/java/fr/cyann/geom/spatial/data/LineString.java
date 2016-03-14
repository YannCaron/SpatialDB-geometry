package fr.cyann.geom.spatial.data; /**
 * Copyright (C) 18/12/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

import fr.cyann.geom.spatial.data.coord.XY;

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

    public static <C extends XY>LineString<C> unMarshall(Class<C> type, String string) {
        if (string == null) return null;
        return unMarshall(type, new StringBuilder(string));
    }

    public static <C extends XY>LineString<C> unMarshall(Class<C> type, StringBuilder stringBuilder) {

	    // LINESTRING
	    Parse.removeBlanks(stringBuilder);
	    Class<? extends XY> parsedType = getCoordType(stringBuilder, "LINESTRING");
	    if (type == null || !type.equals(parsedType)) return null;

        CoordList<C> list = CoordList.unMarshall(type, stringBuilder, false);
	    if (list == null) return null;

        return new LineString<C>(type, list);
    }


}
