package fr.cyann.geom.spatial;/**
 * Copyright (C) 06/03/16 Yann Caron aka cyann
 * <p>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The Sexadecimal definition.
 */
public class Sexadecimal {
	public static final String FORMAT_REGEX = "(\\d{2}):(\\d{2}):(\\d{2}\\.\\d{3})";
	private final int degree, minute;
	private final double second;

	public Sexadecimal(int degree, int minute, double second) {
		this.degree = degree;
		this.minute = minute;
		this.second = second;
	}

	public int getDegree() {
		return degree;
	}

	public int getMinute() {
		return minute;
	}

	public double getSecond() {
		return second;
	}

	public double toDouble() {
		return degree + minute / 60d + second / 3600;
	}

	public static Sexadecimal parseSexadecimal(String coord, String regex) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(coord);
		if (matcher.find()) {
			int d = Integer.parseInt(matcher.group(1));
			int m = Integer.parseInt(matcher.group(2));
			double s = Double.parseDouble(matcher.group(3));

			return new Sexadecimal(d, m, s);
		}

		return null;
	}

	public static Sexadecimal parseSexadecimal(String coord) {
		return parseSexadecimal(coord, FORMAT_REGEX);
	}
}
