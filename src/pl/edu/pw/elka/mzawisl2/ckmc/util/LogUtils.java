package pl.edu.pw.elka.mzawisl2.ckmc.util;

public class LogUtils {

	public static String getDescr(Throwable t) {
		if (null == t)
			return null;

		boolean causePresent = false;

		StringBuilder sb = new StringBuilder();
		if (null != t.getCause()) {
			causePresent = true;
			sb.append(t.getClass().getName() + (null != t.getLocalizedMessage() ? ": " + t.getLocalizedMessage() : "") + "\n");
		}

		while (null != t.getCause())
			t = t.getCause();

		if (causePresent)
			sb.append("Root cause: ");
		sb.append(t.getClass().getName() + (null != t.getLocalizedMessage() ? ": " + t.getLocalizedMessage() : ""));

		for (StackTraceElement ste : t.getStackTrace()) {
			sb.append("\n\t at " + ste.getClassName() + "." + ste.getMethodName());

			if (null != ste.getFileName())
				sb.append("(" + ste.getFileName() + ":" + ste.getLineNumber() + ")");
		}

		return sb.toString();
	}

}
