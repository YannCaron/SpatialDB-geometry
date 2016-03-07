package fr.cyann.geom.spatial.data; /**
 * Copyright (C) 18/12/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

import fr.cyann.geom.spatial.data.coord.XY;

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
	    Class<? extends XY> type = getCoordType(stringBuilder, "LINESTRING");
	    if (type == null) return null;

        // '('
        Parse.removeBlanks(stringBuilder);
        if (!Parse.consumeSymbol(stringBuilder, "(")) return null;

        // <exterior>
	    CoordList<? extends XY> exterior = CoordList.unMarshall(type, stringBuilder, true);
	    if (exterior == null) return null;

        Polygon<? extends XY> polygon = new Polygon(type, exterior);

        // <interior>*
        while (stringBuilder.length() > 0 && Parse.nextSymbol(stringBuilder, ",")) {

            // ','
            Parse.removeBlanks(stringBuilder);
            Parse.consumeSymbol(stringBuilder, ",");

            // <interior>
            Parse.removeBlanks(stringBuilder);
	        CoordList interior = CoordList.unMarshall(type, stringBuilder, true);
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
