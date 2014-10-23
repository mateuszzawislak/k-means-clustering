package pl.edu.pw.elka.mzawisl2.ckmc.exception;

public class CKMCException extends Exception {

	private static final long serialVersionUID = -4479323825871360572L;

	public CKMCException() {
		super();
	}

	public CKMCException(String message) {
		super(message);
	}

	public CKMCException(String message, Throwable cause) {
		super(message, cause);
	}

	public CKMCException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public CKMCException(Throwable cause) {
		super(cause);
	}

}
