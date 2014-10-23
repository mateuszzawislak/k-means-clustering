package pl.edu.pw.elka.mzawisl2.ckmc.util;

public class TextUtils {

	public static final Integer getLevenshteinDistance(char s[], char t[]) {
		return getLevenshteinDistance(s, s.length, t, t.length);
	}

	private static int getLevenshteinDistance(char s[], int len_s, char t[], int len_t) {
		if (len_s == 0)
			return len_t;
		if (len_t == 0)
			return len_s;
		int cost;

		if (s[len_s - 1] == t[len_t - 1])
			cost = 0;
		else
			cost = 1;

		return Math.min(getLevenshteinDistance(s, len_s - 1, t, len_t) + 1,
				Math.min(getLevenshteinDistance(s, len_s, t, len_t - 1) + 1, getLevenshteinDistance(s, len_s - 1, t, len_t - 1) + cost));
	}

	public static final boolean isSet(String s) {
		return null != s && s.trim().length() > 0;
	}

	public static final String removeQuotes(String str) {
		if (null != str && str.length() > 0 && str.charAt(0) == '"') {
			str = str.substring(1, str.length());
		}

		if (null != str && str.length() > 0 && str.charAt(str.length() - 1) == '"') {
			str = str.substring(0, str.length() - 1);
		}

		return str;
	}

	public static final Boolean toBool(String val) {
		if (null == val) {
			return false;
		}

		try {
			return Boolean.parseBoolean(val);
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public static final Long toLong(String val) {
		if (null == val) {
			return null;
		}

		try {
			return Long.parseLong(val);
		} catch (NumberFormatException e) {
			return null;
		}
	}

}
