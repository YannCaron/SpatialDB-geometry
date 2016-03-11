package fr.cyann.geom.spatial.data;/**
 * Copyright (C) 10/03/16 Yann Caron aka cyann
 * <p>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

import fr.cyann.geom.spatial.data.coord.XY;

import java.util.ArrayList;
import java.util.List;

/**
 * The PolyhedralSurface definition.
 */
public class PolyhedralSurface<C extends XY> extends Geometry<XY> {

	private final List<NormalGroup<C>> groups;

	public PolyhedralSurface(Class<XY> type) {
		super(type);
		groups = new ArrayList<>();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		PolyhedralSurface<?> that = (PolyhedralSurface<?>) o;

		if (groups == null && that.groups != null) return false;
		if (groups.size() != that.groups.size()) return false;

		for (int i = 0; i < groups.size(); i++) {
			if (!groups.get(i).equals(that.groups.get(i))) return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		return groups != null ? groups.hashCode() : 0;
	}

	public NormalGroup<C> createGroup() {
		NormalGroup<C> normalGroup = new NormalGroup<>();
		groups.add(normalGroup);
		return normalGroup;
	}

	void addFaceNormalGroup(NormalGroup group) {
		groups.add(group);
	}

	public List<NormalGroup<C>> getNormalGroups() {
		return groups;
	}

	public static <C extends XY>PolyhedralSurface<C> unMarshall(Class<C> type, String stringBuilder) {
		if (stringBuilder == null) return null;
		return unMarshall(type, new StringBuilder(stringBuilder));
	}

	public static <C extends XY>PolyhedralSurface<C> unMarshall(Class<C> type, StringBuilder stringBuilder) {

		// 'POLYGON'
		Parse.removeBlanks(stringBuilder);
		Class<? extends XY> parsedType = getCoordType(stringBuilder, "POLYHEDRALSURFACE");
		if (type == null || !type.equals(parsedType)) return null;

		// '('
		Parse.removeBlanks(stringBuilder);
		if (!Parse.consumeSymbol(stringBuilder, "(")) return null;

		PolyhedralSurface<C> polyhedralSurface = new PolyhedralSurface(type);

		// <interior>*
		boolean first = true;
		Parse.removeBlanks(stringBuilder);
		while (stringBuilder.length() > 0 && (first || Parse.nextSymbol(stringBuilder, ","))) {

			if (!first) {
				// ','
				Parse.consumeSymbol(stringBuilder, ",");
			}
			first = false;

			NormalGroup<C> normalGroup = NormalGroup.unMarshall(type, stringBuilder);
			polyhedralSurface.addFaceNormalGroup(normalGroup);

			Parse.removeBlanks(stringBuilder);
		}

		// ')'
		Parse.removeBlanks(stringBuilder);
		if (!Parse.consumeSymbol(stringBuilder, ")")) return null;

		return polyhedralSurface;
	}

	/**
	 * The abstract method that transform Geometry structure into string understandable by GeomFromText() spatialite function.
	 * see at http://www.gaia-gis.it/gaia-sins/spatialite-cookbook/html/wkt-wkb.html
	 *
	 * @param stringBuilder
	 */
	@Override
	public void marshall(StringBuilder stringBuilder) throws BadGeometryException {
		stringBuilder.append("POLYHEDRALSURFACE");
		appendType(stringBuilder);
		stringBuilder.append(' ');
		stringBuilder.append('(');

		boolean first = true;
		for (NormalGroup<C> normalGroup : groups) {
			if (!first) stringBuilder.append(", ");
			first = false;

			normalGroup.marshall(stringBuilder);

		}

		stringBuilder.append(')');
	}
}
