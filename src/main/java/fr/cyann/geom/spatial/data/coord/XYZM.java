package fr.cyann.geom.spatial.data.coord;

/**
 * Copyright (C) 04/03/16 Yann Caron aka cyann
 * <p>
 * Cette œuvre est mise à disposition sous licence Attribution - Pas
 * d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez
 * http://creativecommons.org/licenses/by-nc-sa/3.0/fr/ ou écrivez à Creative
 * Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 *
 */

/**
 * The XYZ definition.
 */
public class XYZM extends XYZ {

    protected double m;

    public XYZM (double x, double y, double z, double m) {
        super(x, y, z);
        this.m = m;
    }

    public static XYZM unMarshall (StringBuilder string) {
        XYZ xyz = XYZ.unMarshall(string);
        Parse.removeBlanks(string);
        Double m = Parse.consumeDouble(string);
        if (m == null) {
            return null;
        }
        return new XYZM(xyz.getX(), xyz.getY(), xyz.getZ(), m);
    }

    @Override
    public void marshall (StringBuilder stringBuilder) {
        super.marshall(stringBuilder);
        stringBuilder.append(' ');
        marshallNumber(stringBuilder, m);
    }

    @Override
    public void marshallToGeoJson (StringBuilder stringBuilder) {
        stringBuilder.append('[');
        stringBuilder.append(x);
        stringBuilder.append(", ");
        stringBuilder.append(y);
        stringBuilder.append(", ");
        stringBuilder.append(z);
        stringBuilder.append(", ");
        stringBuilder.append(m);
        stringBuilder.append(']');
    }

    public double getM () {
        return m;
    }

    public void setM (double m) {
        this.m = m;
    }

    @Override
    public String toString () {
        return "XYZM{"
                + "x=" + getX()
                + ", y=" + getY()
                + ", z=" + getZ()
                + ", m=" + m
                + '}';
    }
}
