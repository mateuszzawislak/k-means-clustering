package pl.edu.pw.elka.mzawisl2.ckmc.cluster.vector;

import pl.edu.pw.elka.mzawisl2.ckmc.exception.IllegalDimensionException;

public interface Dimension {

	public Double getDistance(Dimension dimension) throws IllegalDimensionException;
}
