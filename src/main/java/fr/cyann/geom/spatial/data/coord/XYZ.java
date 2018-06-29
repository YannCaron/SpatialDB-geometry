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

import fr.cyann.geom.spatial.data.Marshallable;

/**
 * The XYZ definition.
 */
public class XYZ extends XY {

    protected double z;

    public XYZ (double x, double y, double z) {
        super(x, y);
        this.z = z;
    }

    public double getZ () {
        return z;
    }

    public void setZ (double z) {
        this.z = z;
    }

    public static XYZ unMarshall (StringBuilder string) {
        XY xy = XY.unMarshall(string);

        Marshallable.Parse.removeBlanks(string);
        Double z = Marshallable.Parse.consumeDouble(string);
        if (z == null) {
            return null;
        }
        return new XYZ(xy.getX(), xy.getY(), z);
    }

    @Override
    public void marshall (StringBuilder stringBuilder) {
        super.marshall(stringBuilder);
        stringBuilder.append(' ');
        marshallNumber(stringBuilder, z);
    }

    @Override
    public void marshallToGeoJson (StringBuilder stringBuilder) {
        stringBuilder.append('[');
        stringBuilder.append(x);
        stringBuilder.append(", ");
        stringBuilder.append(y);
        stringBuilder.append(", ");
        stringBuilder.append(z);
        stringBuilder.append(']');
    }

    @Override
    public String toString () {
        return "XYZ{"
                + "x=" + getX()
                + ", y=" + getY()
                + ", z=" + z
                + '}';
    }

    public XYZ add (XYZ p) {
        return new XYZ(
                x + p.x,
                y + p.y,
                z + p.z);
    }

    public XYZ sub (XYZ p) {
        return new XYZ(
                x - p.x,
                y - p.y,
                z - p.z);
    }

    public XYZ scalar (double k) {
        return new XYZ(
                x * k,
                y * k,
                z * k);
    }

    public double dot (XYZ p) {
        return x * p.x + y * p.y + z * p.z;
    }

    public XYZ cross (XYZ p) {
        return new XYZ(
                y * p.z - z * p.y, // x
                z * p.x - x * p.z, // y
                x * p.y - y * p.x // z
        );
    }

    public double modulus () {
        return Math.sqrt(dot(this));
    }

    public XYZ versor () {
        final double m = modulus();
        if (m == 0.0) {
            return new XYZ(0, 0, 0);
        }
        return scalar(1.0 / m);
    }

    public double distance (XYZ p) {
        return p.sub(this).modulus();
    }

    public double distanceToSegment (XYZ a, XYZ b) {
        XYZ ab = b.sub(a);
        XYZ av = sub(a);

        if (av.dot(ab) <= 0.0) // Point is lagging behind start of the segment, so perpendicular distance is not viable.
        {
            return av.modulus();                        // Use distance to start of segment instead.
        }
        final XYZ bv = sub(b);

        if (bv.dot(ab) >= 0.0) // Point is advanced past the end of the segment, so perpendicular distance is not viable.
        {
            return bv.modulus();                        // Use distance to end of the segment instead.
        }
        return (ab.cross(av)).modulus() / ab.modulus(); // Perpendicular distance of point to segment.
    }
}
