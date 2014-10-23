package pl.edu.pw.elka.mzawisl2.ckmc.cluster.converge;

import pl.edu.pw.elka.mzawisl2.ckmc.cluster.Cluster;

public class DriftConverger implements Converger {

	private double driftTolerance;

	public DriftConverger(double driftTolerance) {
		this.driftTolerance = driftTolerance;
	}

	@Override
	public boolean isAcceptable(Cluster[] before, Cluster[] after) {
		if (before.length != after.length)
			throw new IllegalStateException("Clasters amount has changed!");

		for (int i = 0; i < before.length; ++i) {
			if (before[i].getCentroidLocation().getDistance(after[i].getCentroidLocation()) > driftTolerance) {
				return false;
			}
		}

		return true;
	}
}
