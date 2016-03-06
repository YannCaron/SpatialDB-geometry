package ch.skyguide.geos.data; /**
 * Copyright (C) 18/12/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

import ch.skyguide.geos.data.coord.XY;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * The ch.skyguide.geos.loader.geom.CoordList definition.
 */
public class CoordList<C extends XY> implements Marshallable {

    // attribute
    private final List<C> coords;
    private final boolean isClosed;

    // constructor and factory
    public CoordList(boolean closed) {
        coords = new ArrayList<>();
        this.isClosed = closed;
    }

    public static <C extends XY> CoordList<C> unMarshall(Class<C> type, StringBuilder stringBuilder, boolean closed) {

        // '('
        Parse.removeBlanks(stringBuilder);
        if (!Parse.consumeSymbol(stringBuilder, "(")) return null;

        // (<xy> (',' <xy>)*)?
        CoordList<C> list = new CoordList(closed);
        while (stringBuilder.length() > 0 && !(Parse.nextSymbol(stringBuilder, ")") || (Parse.nextSymbol(stringBuilder, ",")))) {

            // <xy>
            Parse.removeBlanks(stringBuilder);
	        C coord = Geometry.unmarshallCoord(type, stringBuilder);
	        if (coord == null) return null;
            list.add(coord);

            // ','
            Parse.removeBlanks(stringBuilder);
            if (!Parse.consumeSymbol(stringBuilder, ",")) break;

        }

        // ')'
        Parse.removeBlanks(stringBuilder);
        if (!Parse.consumeSymbol(stringBuilder, ")")) return null;
        return list;
    }

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		CoordList<?> coordList = (CoordList<?>) o;

		if (isClosed != coordList.isClosed) return false;
		return !(coords != null ? !coords.equals(coordList.coords) : coordList.coords != null);

	}

	@Override
	public int hashCode() {
		int result = coords != null ? coords.hashCode() : 0;
		result = 31 * result + (isClosed ? 1 : 0);
		return result;
	}

	// accessor
    public List<C> getCoords() {
        return coords;
    }

    public int size() {
        return coords.size();
    }

    // method
    public boolean add(C xy) {
        return coords.add(xy);
    }

    public boolean removeAll(Collection<?> c) {
        return coords.removeAll(c);
    }

    // method implement
    @Override
    public void marshall(StringBuilder string) {
        string.append('(');
        boolean tail = false;
        for (C coord : coords) {
            if (tail) {
                string.append(", ");
            }
            tail = true;

            coord.marshall(string);
        }

        if (isClosed && coords.size() > 0 && !coords.get(0).equals(coords.get(coords.size() - 1))) {
            string.append(", ");
            coords.get(0).marshall(string);
        }
        string.append(')');
    }
}
