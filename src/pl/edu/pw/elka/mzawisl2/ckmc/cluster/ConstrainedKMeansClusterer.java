package pl.edu.pw.elka.mzawisl2.ckmc.cluster;

import java.util.Collections;
import java.util.List;

import pl.edu.pw.elka.mzawisl2.ckmc.cluster.constraint.Constraint;
import pl.edu.pw.elka.mzawisl2.ckmc.cluster.util.ClusterUtils;
import pl.edu.pw.elka.mzawisl2.ckmc.cluster.vector.Vector;
import pl.edu.pw.elka.mzawisl2.ckmc.util.BaseUtils;

public class ConstrainedKMeansClusterer extends AbstractKClusterer {

	private List<Constraint> constraints;

	public ConstrainedKMeansClusterer(List<Constraint> constraints, int maxReclustering) {
		super(maxReclustering);
		this.constraints = constraints;
	}

	@Override
	protected boolean assignItemsToClusters(Cluster[] clusters, List<? extends Clusterable> items) {
		ClusterUtils.unassignClusterItems(clusters);
		Collections.shuffle(items);

		for (int j = 0; j < items.size(); j++) {
			Clusterable item = items.get(j);
			Cluster nearestCluster = null;
			double minDistance = Float.MAX_VALUE;
			for (int i = 0; i < clusters.length; i++) {
				Cluster cluster = clusters[i];
				double distance = ClusterUtils.getDistanceToCluster(cluster, item);

				if (distance < minDistance) {
					if (!violateConstraints(clusters, cluster, item)) {
						nearestCluster = cluster;
						minDistance = distance;
					}
				}
			}
			if (null == nearestCluster)
				return false;

			nearestCluster.addItem(item);
			item.setClusterId(nearestCluster.getId());
		}

		return true;
	}

	@Override
	protected Cluster[] getNewClustersCenters(Cluster[] clusters) {
		Cluster[] updatedClusters = new Cluster[clusters.length];
		for (int i = 0; i < clusters.length; i++) {
			Cluster oldCluster = clusters[i];
			Vector newCentroid = oldCluster.getCentroidLocation();
			if (!BaseUtils.isNullOrEmpty(oldCluster.getItems())) {
				newCentroid = ClusterUtils.getMeanValue(oldCluster.getItems());
			}

			updatedClusters[i] = new Cluster(newCentroid, (long) i);
		}
		return updatedClusters;
	}

	private boolean violateConstraints(Cluster[] clusters, Cluster cluster, Clusterable item) {
		if (!BaseUtils.isNullOrEmpty(constraints)) {
			for (Constraint constraint : constraints) {
				if (constraint.violate(clusters, cluster, item))
					return true;
			}
		}

		return false;
	}
}
