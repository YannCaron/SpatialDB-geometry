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
import java.util.List;

/**
 * The ch.skyguide.geos.loader.geom.LineString definition.
 */
public class Polygon<C extends XY> extends LineString {

    private final List<CoordList<C>> interiors;

    public Polygon(Class<C> type) {
        super(type, true);
        this.interiors = new ArrayList<>();
    }

    Polygon(Class<C> type, CoordList<C> exterior) {
        super(type, exterior);
        this.interiors = new ArrayList<>();
    }

    public static Polygon<? extends XY> unMarshall(String stringBuilder) {
        if (stringBuilder == null) return null;
        return unMarshall(new StringBuilder(stringBuilder));
    }

    public static Polygon<? extends XY> unMarshall(StringBuilder stringBuilder) {

        // 'POLYGON'
        Parse.removeBlanks(stringBuilder);
        if (!Parse.consumeSymbol(stringBuilder, "POLYGON")) return null;

        // '('
        Parse.removeBlanks(stringBuilder);
        if (!Parse.consumeSymbol(stringBuilder, "(")) return null;

        // <exterior>
        CoordList<XY> exterior = CoordList.unMarshall(stringBuilder, true);
        if (exterior == null) return null;
        Polygon polygon = new Polygon(XY.class, exterior);

        // <interior>*
        while (stringBuilder.length() > 0 && Parse.nextSymbol(stringBuilder, ",")) {

            // ','
            Parse.removeBlanks(stringBuilder);
            Parse.consumeSymbol(stringBuilder, ",");

            // <interior>
            Parse.removeBlanks(stringBuilder);
            CoordList interior = CoordList.unMarshall(stringBuilder, true);
            if (interior == null) return null;
            polygon.addInterior(interior);

        }

        // ')'
        Parse.removeBlanks(stringBuilder);
        if (!Parse.consumeSymbol(stringBuilder, ")")) return null;

        return polygon;
    }

    public boolean addInterior(CoordList<C> xyList) {
        return interiors.add(xyList);
    }

    @Override
    public void marshall(StringBuilder string) throws BadGeometryException {
        string.append("POLYGON");
        string.append(' ');
        string.append('(');

        //if (getCoordinate().size() < 4) throw new BadGeometryException("POLYGON geometry should hava at least 4 coordinates!");
        getCoordinate().marshall(string);

        for (CoordList interior : interiors) {
            string.append(", ");
            interior.marshall(string);
        }

        string.append(')');
    }
}
