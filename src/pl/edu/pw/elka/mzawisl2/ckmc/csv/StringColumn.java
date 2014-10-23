package pl.edu.pw.elka.mzawisl2.ckmc.csv;

import pl.edu.pw.elka.mzawisl2.ckmc.cluster.vector.Dimension;
import pl.edu.pw.elka.mzawisl2.ckmc.exception.IllegalDimensionException;
import pl.edu.pw.elka.mzawisl2.ckmc.util.TextUtils;

public class StringColumn implements Dimension {

	private String value;

	public StringColumn(String value) {
		super();
		this.value = value;
	}

	@Override
	public Double getDistance(Dimension dimension) throws IllegalDimensionException {
		if (!(dimension instanceof StringColumn))
			throw new IllegalDimensionException();

		return (double) TextUtils.getLevenshteinDistance(value.toCharArray(), ((StringColumn) dimension).getValue().toCharArray());
	}

	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return null == value ? "" : "string: " + value.toString();
	}

}
