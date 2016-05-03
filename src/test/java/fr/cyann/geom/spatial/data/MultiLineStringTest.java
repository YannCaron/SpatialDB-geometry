package fr.cyann.geom.spatial.data;

import fr.cyann.geom.spatial.data.coord.XYZM;
import junit.framework.TestCase;

/**
 * Copyright (C) 03/05/16 Yann Caron aka cyann
 * <p>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/
public class MultiLineStringTest extends TestCase {

	public void testMarshallUnMarshall() throws Exception {

		String wktInput = "MULTILINESTRINGZM ((0 0 0 0, 0 1 0 0, 0 2 0 0, 0 2 1 0), (0 4 0 0, 0 4 1 0, 0 4 2 0))";
		MultiLineString<XYZM> multiLineString1 = MultiLineString.unMarshall(XYZM.class, wktInput);
		String wktOuput = multiLineString1.toString();
		MultiLineString<XYZM> multiLineString2 = MultiLineString.unMarshall(XYZM.class, wktOuput);

		assertEquals(multiLineString1, multiLineString2);

	}
}