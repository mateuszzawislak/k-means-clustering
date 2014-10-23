package pl.edu.pw.elka.mzawisl2.ckmc.cluster;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import pl.edu.pw.elka.mzawisl2.ckmc.cluster.converge.Converger;
import pl.edu.pw.elka.mzawisl2.ckmc.cluster.converge.DriftConverger;
import pl.edu.pw.elka.mzawisl2.ckmc.config.ConfigService;
import pl.edu.pw.elka.mzawisl2.ckmc.config.ConfigService.Param;

public abstract class AbstractKClusterer implements KClusterer {

	private final static float DEFAULT_DRIFT_TELRANCE = 100.f;

	private Converger converger;

	int maxReclustering;

	protected final static ConfigService configService = ConfigService.getInstance();

	protected AbstractKClusterer(int maxReclustering) {
		this.maxReclustering = maxReclustering;
		converger = new DriftConverger(configService.getParamFloat(Param.DRIFT_TELRANCE, DEFAULT_DRIFT_TELRANCE));
	}

	protected abstract boolean assignItemsToClusters(Cluster[] clusters, final List<? extends Clusterable> items);

	protected Cluster[] calculateInitialClusters(List<? extends Clusterable> items, int numClusters) {
		Cluster[] clusters = new Cluster[numClusters];
		Random random = new Random(System.currentTimeMillis());
		Set<Integer> clusterCenters = new HashSet<Integer>();

		for (int i = 0; i < numClusters; i++) {
			int index = random.nextInt(items.size());

			while (clusterCenters.contains(index)) {
				index = random.nextInt(items.size());
			}

			clusterCenters.add(index);
			clusters[i] = new Cluster(items.get(index).getLocation(), (long) i);
		}
		return clusters;
	}

	public Cluster[] cluster(final List<? extends Clusterable> items, int numClusters) {
		Cluster[] clusters = calculateInitialClusters(items, numClusters);

		while (!assignItemsToClusters(clusters, items)) {
			clusters = calculateInitialClusters(items, numClusters);
		}

		int numIterations = 0;
		while (numIterations < maxReclustering) {
			Cluster[] updatedClusters = getNewClustersCenters(clusters);
			// add all items to nearest cluster
			while (!assignItemsToClusters(updatedClusters, items))
				;

			// see if the algorithm converges
			if (converger.isAcceptable(clusters, updatedClusters)) {
				return updatedClusters;
			}

			clusters = updatedClusters;
			numIterations++;
		}

		return clusters;
	}

	protected abstract Cluster[] getNewClustersCenters(Cluster[] clusters);
}
