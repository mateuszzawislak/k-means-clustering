package pl.edu.pw.elka.mzawisl2.ckmc.csv;

import pl.edu.pw.elka.mzawisl2.ckmc.cluster.vector.Dimension;
import pl.edu.pw.elka.mzawisl2.ckmc.exception.IllegalDimensionException;

public class IntegerColumn implements Dimension {

	private Integer value;

	public IntegerColumn(Integer value) {
		super();
		this.value = value;
	}

	@Override
	public Double getDistance(Dimension dimension) throws IllegalDimensionException {
		if (!(dimension instanceof IntegerColumn))
			throw new IllegalDimensionException();

		return (double) Math.abs(this.value - ((IntegerColumn) dimension).getValue());
	}

	public Integer getValue() {
		return value;
	}

	@Override
	public String toString() {
		return null == value ? "" : "int: " + value.toString();
	}

}
