package pl.edu.pw.elka.mzawisl2.ckmc.cluster.vector;

import org.apache.log4j.Logger;

import pl.edu.pw.elka.mzawisl2.ckmc.exception.IllegalDimensionException;
import pl.edu.pw.elka.mzawisl2.ckmc.util.LogUtils;

public class Vector implements Cloneable {

	private static Logger log = Logger.getLogger(Vector.class);

	private Dimension[] dimensions;

	public Vector(Dimension[] dimensions) {
		super();
		this.dimensions = dimensions;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public Dimension[] getDimensions() {
		return dimensions;
	}

	public Double getDistance(Vector other) {
		if (getLength() != other.getLength()) {
			log.error("Attempting to compare two clusterables of different vector lengths");
			throw new RuntimeException("Attempting to compare two clusterables of different vector lengths");
		}

		double sum = 0;
		Dimension[] otherDimensions = other.getDimensions();
		for (int i = 0; i < getLength(); ++i) {
			double diff = 0;
			try {
				diff = otherDimensions[i].getDistance(dimensions[i]);
			} catch (IllegalDimensionException e) {
				log.error("Attempting to compare two dimensions of different types" + LogUtils.getDescr(e));
				throw new RuntimeException("Attempting to compare two dimensions of different types");
			}

			// euclidian distance
			sum += diff * diff;
		}

		return Math.sqrt(sum);
	}

	public int getLength() {
		return null == dimensions ? 0 : dimensions.length;
	}

	public void setDimensions(Dimension[] dimensions) {
		this.dimensions = dimensions;
	}

	@Override
	public String toString() {
		StringBuffer str = new StringBuffer();
		str.append("[");

		if (null != dimensions) {
			for (int i = 0; i < dimensions.length; ++i) {
				Dimension dimension = dimensions[i];
				str.append(dimension.toString());
				if (i < dimensions.length - 1)
					str.append(", ");
			}
		}

		str.append("]");

		return str.toString();
	}

}
