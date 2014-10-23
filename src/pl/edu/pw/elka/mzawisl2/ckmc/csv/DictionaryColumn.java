package pl.edu.pw.elka.mzawisl2.ckmc.csv;

import pl.edu.pw.elka.mzawisl2.ckmc.cluster.vector.Dimension;
import pl.edu.pw.elka.mzawisl2.ckmc.config.ConfigService;
import pl.edu.pw.elka.mzawisl2.ckmc.config.ConfigService.Param;
import pl.edu.pw.elka.mzawisl2.ckmc.exception.IllegalDimensionException;

public class DictionaryColumn implements Dimension {

	private String value;

	public DictionaryColumn(String value) {
		super();
		this.value = value;
	}

	@Override
	public Double getDistance(Dimension dimension) throws IllegalDimensionException {
		if (!(dimension instanceof DictionaryColumn))
			throw new IllegalDimensionException();

		return this.value.equals(((DictionaryColumn) dimension).getValue()) ? 0. : ConfigService.getInstance().getParamFloat(
				Param.DICTIONARY_DISTANCE_WAGE);
	}

	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return null == value ? "" : "dict: " + value.toString();
	}

}
