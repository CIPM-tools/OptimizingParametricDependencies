package tools.vitruv.applications.pcmjava.modelrefinement.parameters.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import weka.classifiers.functions.LinearRegression;
import weka.classifiers.trees.J48;
import weka.core.Instances;

public class Utils {
	private static final String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String CHAR_UPPER = CHAR_LOWER.toUpperCase();
    private static final String NUMBER = "0123456789";

    private static final String DATA_FOR_RANDOM_STRING = CHAR_LOWER + CHAR_UPPER + NUMBER;
    private static SecureRandom random = new SecureRandom();
	
	private static final String PLUS_PARENTHESIS = " + (";
	private static final String MULTIPLICATION = " * ";
	private static final String OPENING_PARENTHESIS = "(";
	private static final String CLOSING_PARENTHESIS = ")";
	private static final String NEW_LINE = "\n";
	private static final String SPACES = "\\s+";
	private static final char PIPE = '|';
	private static final String COLON = ":";
	private static final String IF = "IF";
	private static final String QUESTION = "?";

	public static double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		BigDecimal bd = BigDecimal.valueOf(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

	public static String replaceUnderscoreWithDot(String stoEx) {
		return stoEx.replaceAll("_VALUE", ".VALUE").replaceAll("_BYTESIZE", ".BYTESIZE");
	}

	public static String getStoExLinReg(LinearRegression classifier, Instances filteredData) {
		StringJoiner result = new StringJoiner(PLUS_PARENTHESIS);
		double[] coefficients = classifier.coefficients();
		int braces = 0;
		for (int i = 0; i < coefficients.length - 2; i++) {
			double coefficient = Utils.round(coefficients[i], 3);
			if (coefficient == 0) {
				continue;
			}
			StringBuilder coefficientPart = new StringBuilder();
			String paramStoEx = filteredData.attribute(i).name();
			coefficientPart.append(coefficient).append(MULTIPLICATION).append(paramStoEx);
			result.add(coefficientPart.toString());
			braces++;
		}
		result.add(String.valueOf(Utils.round(coefficients[coefficients.length - 1], 3)));
		StringBuilder strBuilder = new StringBuilder().append(result.toString());
		for (int i = 0; i < braces; i++) {
			strBuilder.append(CLOSING_PARENTHESIS);
		}
		return strBuilder.toString();
	}

	public static String getStoExTree(J48 j48Tree) {
		String[] lines = j48Tree.toString().split(NEW_LINE);
		if (lines.length == 6) { // constant value (6 is the min size ot the tree)
			return lines[3].replace(COLON, "").replaceAll(SPACES, "");
		}

		StringBuilder builder = new StringBuilder();
		int numPipes = 0;
		for (int currentLine = 3; !lines[currentLine].isEmpty(); currentLine++) {
			String line = PIPE + lines[currentLine];

			if (!line.contains(COLON)) { // line contains condition (no assignment)
				if (countPipes(line) == numPipes) { // closing
					builder.append(COLON);
				} else { // opening
					builder.append(OPENING_PARENTHESIS + OPENING_PARENTHESIS + line.replaceAll(SPACES, "")
							+ CLOSING_PARENTHESIS + QUESTION);
					numPipes++;
				}

			} else {
				if (countPipes(line) == numPipes) { // closing
					String[] lineParts = line.split(COLON);
					builder.append(COLON + lineParts[1].replaceAll(SPACES, "") + CLOSING_PARENTHESIS);
					numPipes--;
				} else { // opening
					String[] lineParts = lines[currentLine].split(COLON);
					builder.append(OPENING_PARENTHESIS + OPENING_PARENTHESIS + lineParts[0].replaceAll(SPACES, "")
							+ CLOSING_PARENTHESIS + QUESTION + lineParts[1].replaceAll(SPACES, ""));
					numPipes++;
				}
			}
		}
//		for (int i = 0; i < numPipes-1; i++) {
//		builder.append(")");
//	}
		return cleanStoEx(builder.toString().substring(1, builder.toString().length() - 1)); // remove the most outter
																								// parenthesis
	}

	private static String cleanStoEx(String stoEx) {
		// remove all pipe symbols that come from the decision tree
		String cleanStoEx = stoEx.replaceAll(Character.toString(PIPE), "");
		// replace all = with == (except the ones in !=)
		cleanStoEx = cleanStoEx.replaceAll("(?<!!)=", "==");
		// remove all strings e.g. (4.0/8.0) which come from the decision tree
		cleanStoEx = cleanStoEx.replaceAll("[(]\\d+\\.\\d+[)]", "");
		return cleanStoEx;
	}

	private static int countPipes(String str) {
		return (int) str.chars().filter(ch -> ch == PIPE).count();
	}

	public static String replaceDoubles(String stoEx) {
		Pattern p = Pattern.compile("(\\d+(?:\\.[1-9]\\d*))");
		Matcher m = p.matcher(stoEx);
		while (m.find()) {
			double d = Double.parseDouble(m.group(1));
			String dStr = String.valueOf(d);
			String[] parts = dStr.split("\\.");
			int lower = Integer.valueOf(parts[0]);
			int upper = lower+1;
			double decimalPart = d - lower;
			decimalPart = round(decimalPart, 2);
			double rest = round(1-decimalPart,2);
			String replacement = "DoublePMF[(" + lower + ";" + rest + ")(" + upper + ";" + decimalPart + ")]";
			stoEx = stoEx.replaceAll(m.group(1), replacement);			
		}
		return stoEx;
	}
	
	public static String generateRandomString(int length) {
        if (length < 1) throw new IllegalArgumentException();

        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {

			// 0-62 (exclusive), random returns 0-61
            int rndCharAt = random.nextInt(DATA_FOR_RANDOM_STRING.length());
            char rndChar = DATA_FOR_RANDOM_STRING.charAt(rndCharAt);

            sb.append(rndChar);

        }

        return sb.toString();

    }
    
}
