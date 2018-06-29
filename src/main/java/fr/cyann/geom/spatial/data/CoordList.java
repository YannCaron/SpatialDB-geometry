package fr.cyann.geom.spatial.data;

/**
 * Copyright (C) 18/12/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution - Pas
 * d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez
 * http://creativecommons.org/licenses/by-nc-sa/3.0/fr/ ou écrivez à Creative
 * Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 *
 */
import fr.cyann.geom.spatial.data.coord.XY;
import fr.cyann.geom.spatial.data.parsing.BinaryUtil;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * The ch.skyguide.geos.loader.geom.CoordList definition.
 */
public class CoordList<C extends XY> implements Marshallable, GeoJsonMarshallable, Iterable<C> {

    // attribute
    private final List<C> coords;
    private final boolean isClosed;

    // constructor and factory
    public CoordList (boolean closed) {
        coords = new ArrayList<>();
        this.isClosed = closed;
    }

    @Override
    public boolean equals (Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CoordList<?> that = (CoordList<?>) o;

        if (isClosed != that.isClosed) {
            return false;
        }

        if (coords == null && that.coords != null) {
            return false;
        }
        if (coords.size() != that.coords.size()) {
            return false;
        }

        for (int i = 0; i < coords.size(); i++) {
            if (!coords.get(i).equals(that.coords.get(i))) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int hashCode () {
        int result = coords.hashCode();
        result = 31 * result + (isClosed ? 1 : 0);
        return result;
    }

    public static <C extends XY> CoordList<C> unMarshall (Class<C> type, StringBuilder stringBuilder, boolean closed) {

        // '('
        Parse.removeBlanks(stringBuilder);
        if (!Parse.consumeSymbol(stringBuilder, '(')) {
            return null;
        }

        // (<xy> (',' <xy>)*)?
        CoordList<C> list = new CoordList(closed);
        while (stringBuilder.length() > 0 && !(Parse.nextSymbol(stringBuilder, ')') || (Parse.nextSymbol(stringBuilder, ',')))) {

            // <xy>
            Parse.removeBlanks(stringBuilder);
            C coord = Geometry.unmarshallCoord(type, stringBuilder);
            if (coord == null) {
                return null;
            }
            list.add(coord);

            // ','
            Parse.removeBlanks(stringBuilder);
            if (!Parse.consumeSymbol(stringBuilder, ',')) {
                break;
            }

        }

        // ')'
        Parse.removeBlanks(stringBuilder);
        if (!Parse.consumeSymbol(stringBuilder, ')')) {
            return null;
        }
        return list;
    }

    public static <C extends XY> CoordList<C> unMarshall (Class<C> type, ByteBuffer buffer, boolean closed) {
        CoordList<C> coordList = new CoordList<>(closed);
        int size = buffer.getInt();
        for (int i = 0; i < size; i++) {
            coordList.add(BinaryUtil.unMarshallCoord(type, buffer));
        }
        return coordList;
    }

    // accessor
    public List<C> getCoords () {
        return coords;
    }

    public int size () {
        return coords.size();
    }

    public boolean isClosed () {
        return isClosed;
    }

    // method
    public CoordList<C> add (C xy) {
        coords.add(xy);
        return this;
    }

    public CoordList<C> add (int index, C xy) {
        coords.add(index, xy);
        return this;
    }

    public boolean removeAll (Collection<?> c) {
        return coords.removeAll(c);
    }

    // method implement
    @Override
    public void marshall (StringBuilder stringBuilder) {
        stringBuilder.append('(');
        boolean tail = false;
        for (C coord : coords) {
            if (tail) {
                stringBuilder.append(", ");
            }
            tail = true;

            coord.marshall(stringBuilder);
        }

        if (isClosed && coords.size() > 0 && !coords.get(0).equals(coords.get(coords.size() - 1))) {
            stringBuilder.append(", ");
            coords.get(0).marshall(stringBuilder);
        }
        stringBuilder.append(')');
    }

    @Override
    public void marshallToGeoJson (StringBuilder stringBuilder) {
        boolean first = true;

        stringBuilder.append('[');
        for (C coord : coords) {
            if (!first) {
                stringBuilder.append(", ");
            }
            coord.marshallToGeoJson(stringBuilder);
            first = false;
        }
        stringBuilder.append(']');
    }

    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    @Override
    public Iterator<C> iterator () {
        return coords.iterator();
    }

}
