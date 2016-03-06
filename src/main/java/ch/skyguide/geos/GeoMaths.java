package ch.skyguide.geos;

/**
 * Created by caronyn on 03.03.2016.
 */
public class GeoMaths {

	public static final double METER_TO_FEET_RATIO = 3.28084;

	private GeoMaths() {
		throw new RuntimeException("Cannot instantiate static class !");
	}

	public static double feetToMeter(double feet) {
		return feet / METER_TO_FEET_RATIO;
	}

	public static double meterToFeet(double meter) {
		return meter * METER_TO_FEET_RATIO;
	}

}
