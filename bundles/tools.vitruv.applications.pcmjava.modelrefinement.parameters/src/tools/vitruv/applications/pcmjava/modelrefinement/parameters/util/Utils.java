package tools.vitruv.applications.pcmjava.modelrefinement.parameters.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Utils {
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = BigDecimal.valueOf(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
	
	public static String replaceUnderscoreWithDot(String stoEx) {
		return stoEx.replaceAll("_VALUE", ".VALUE").replaceAll("_BYTESIZE", ".BYTESIZE");
	}
}
