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
/**
 * The ch.skyguide.geos.loader.geom.Marshallable definition.
 */
public interface Marshallable {

    /**
     * Abstract class Parse that contains necessarily method for geometry
     * parsing (unMarshalling)
     */
    class Parse {

        /**
         * Recognize symbol and consume it.
         *
         * @param string the input string (string builder) to parseSexadecimal.
         * @param symbol the symbol to recognize
         * @return true if parsing is ok.
         */
        public static boolean consumeSymbol(StringBuilder string, char symbol) {
            if (string.charAt(0) != symbol) {
                return false;
            }
            string.deleteCharAt(0);
            return true;
        }

        /**
         * Recognize symbol and consume it.
         *
         * @param string the input string (string builder) to parseSexadecimal.
         * @param symbol the symbol to recognize
         * @return true if parsing is ok.
         */
        public static boolean consumeSymbol(StringBuilder string, String symbol) {
            if (!nextSymbol(string, symbol)) return false;
            string.delete(0, symbol.length());
            return true;
        }

        /**
         * Recognize double and consume it.
         *
         * @param string the input string (string builder) to parseSexadecimal.
         * @return true if parsing is ok.
         */
        public static Double consumeDouble(StringBuilder string) {
            int len = string.length();
            int to = 0;
            for (int i = 0; i < len; i++) {
                char chr = string.charAt(i);
                if (chr != '-' && chr != '.' && chr != '0' && chr != '1'
                        && chr != '2' && chr != '3' && chr != '4' && chr != '5'
                        && chr != '6' && chr != '7' && chr != '8' && chr != '9') {
                    break;
                }
                to++;
            }

            Double dbl = Double.parseDouble(string.subSequence(0, to).toString());
            string.delete(0, to);
            return dbl;
        }

        /**
         * Remove unnecessary blanks in the input string.
         *
         * @param string the input string (string builder) to parseSexadecimal.
         */
        public static void removeBlanks(StringBuilder string) {
            while (string.charAt(0) == ' ' /*string.indexOf(" ") == 0*/) {
                string.deleteCharAt(0);
            }
        }

        /**
         * Look at the next symbol and recognize it without consuming it. LL(1)
         * parser, first symbol is deterministic for the choice.
         *
         * @param string the input string (string builder) to parseSexadecimal.
         * @param symbol the symbol to recognize
         * @return true if next symbol corresponding.
         */
        public static boolean nextSymbol(StringBuilder string, char symbol) {
            return string.charAt(0) == symbol;
        }

        /**
         * Look at the next symbol and recognize it without consuming it. LL(1)
         * parser, first symbol is deterministic for the choice.
         *
         * @param string the input string (string builder) to parseSexadecimal.
         * @param symbol the symbol to recognize
         * @return true if next symbol corresponding.
         */
        public static boolean nextSymbol(StringBuilder string, String symbol) {
            int len = symbol.length();
            for (int i = 0; i < len; i++) {
                if (string.charAt(i) != symbol.charAt(i)) {
                    return false;
                }
            }
            return true;
        }

    }

    /**
     * The abstract method that transform Geometry structure into stringBuilder
     * understandable by GeomFromText() spatialite function. see at
     * http://www.gaia-gis.it/gaia-sins/spatialite-cookbook/html/wkt-wkb.html
     *
     * @param stringBuilder
     */
    void marshall(StringBuilder stringBuilder) throws BadGeometryException;

}
