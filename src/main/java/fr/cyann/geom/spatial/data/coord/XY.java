package fr.cyann.geom.spatial.data.coord; /**
 * Copyright (C) 18/12/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

import fr.cyann.geom.spatial.data.Marshallable;

/**
 * The ch.skyguide.geos.loader.geom.XY definition.
 */
public class XY implements Marshallable {

    private final double x, y;

    public XY(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public static XY unMarshall(StringBuilder string) {
        Parse.removeBlanks(string);
	    Double x = Parse.consumeDouble(string);
	    if (x == null) return null;
        Parse.removeBlanks(string);
	    Double y = Parse.consumeDouble(string);
	    if (y == null) return null;
        return new XY(x, y);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        XY xy = (XY) o;

        if (Double.compare(xy.x, x) != 0) return false;
        return Double.compare(xy.y, y) == 0;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(x);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(y);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    protected void marshallNumber(StringBuilder string, double number) {
        if (number == Math.round(number))
            string.append((int) number);
        else
            string.append(number);
    }

    @Override
    public void marshall(StringBuilder stringBuilder) {
        marshallNumber(stringBuilder, x);
        stringBuilder.append(' ');
        marshallNumber(stringBuilder, y);
    }

	@Override
	public String toString() {
		return "XYZM{" +
				"x=" + x +
				", y=" + y +
				'}';
	}

}
