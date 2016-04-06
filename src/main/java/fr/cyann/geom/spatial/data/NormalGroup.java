package fr.cyann.geom.spatial.data;/**
 * Copyright (C) 11/03/16 Yann Caron aka cyann
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
 * The NormalGroup definition.
 */
public class NormalGroup<C extends XY> implements Marshallable  {

	private final List<CoordList<C>> faces;

	public NormalGroup() {
		faces = new ArrayList<>();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		NormalGroup<?> that = (NormalGroup<?>) o;

		if (faces == null && that.faces != null) return false;
		if (faces.size() != that.faces.size()) return false;

		for (int i = 0; i < faces.size(); i++) {
			if (!faces.get(i).equals(that.faces.get(i))) return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		return faces != null ? faces.hashCode() : 0;
	}

	public NormalGroup<C> addFace(CoordList<C> face) {
		faces.add(face);
		return this;
	}

	public List<CoordList<C>> getFaces() {
		return faces;
	}

	public static <C extends XY> NormalGroup<C> unMarshall(Class<C> type, StringBuilder stringBuilder) {

		Parse.removeBlanks(stringBuilder);
		if (!Parse.consumeSymbol(stringBuilder, '(')) return null;

		NormalGroup<C> normalGroup = new NormalGroup<>();

		// <groups>
		boolean first = true;
		Parse.removeBlanks(stringBuilder);
		while (stringBuilder.length() > 0 && (first || Parse.nextSymbol(stringBuilder, ','))) {

			if (!first) {
				// ','
				Parse.consumeSymbol(stringBuilder, ',');
			}
			first = false;

			CoordList<C> face = CoordList.unMarshall(type, stringBuilder, true);
			if (face == null) return null;

			normalGroup.addFace(face);

			Parse.removeBlanks(stringBuilder);
		}

		Parse.removeBlanks(stringBuilder);
		if (!Parse.consumeSymbol(stringBuilder, ')')) return null;

		return normalGroup;
	}

	@Override
	public void marshall(StringBuilder stringBuilder) throws BadGeometryException {

		stringBuilder.append('(');

		boolean first = true;
		for (CoordList face : faces) {
			if (!first) stringBuilder.append(", ");
			first = false;

			face.marshall(stringBuilder);
		}

		stringBuilder.append(')');

	}
}
