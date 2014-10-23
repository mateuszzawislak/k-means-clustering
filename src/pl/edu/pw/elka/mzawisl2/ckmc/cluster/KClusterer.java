package pl.edu.pw.elka.mzawisl2.ckmc.cluster;

import java.util.List;

import pl.edu.pw.elka.mzawisl2.ckmc.exception.IllegalClustersException;

public interface KClusterer {

	public Cluster[] cluster(final List<? extends Clusterable> values, int numClusters) throws IllegalClustersException;

}
