package pl.edu.pw.elka.mzawisl2.ckmc.csv;

import pl.edu.pw.elka.mzawisl2.ckmc.cluster.vector.Dimension;
import pl.edu.pw.elka.mzawisl2.ckmc.exception.IllegalDimensionException;

public class DoubleColumn implements Dimension {

	private Double value;

	public DoubleColumn(Double value) {
		super();
		this.value = value;
	}

	@Override
	public Double getDistance(Dimension dimension) throws IllegalDimensionException {
		if (!(dimension instanceof DoubleColumn))
			throw new IllegalDimensionException();

		return Math.abs(this.value - ((DoubleColumn) dimension).getValue());
	}

	public Double getValue() {
		return value;
	}

	@Override
	public String toString() {
		return null == value ? "" : "double: " + value.toString();
	}

}
