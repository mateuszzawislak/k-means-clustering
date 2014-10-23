package pl.edu.pw.elka.mzawisl2.ckmc.cluster.constraint;

import pl.edu.pw.elka.mzawisl2.ckmc.cluster.Cluster;
import pl.edu.pw.elka.mzawisl2.ckmc.cluster.Clusterable;

public interface Constraint {

	public boolean violate(Cluster[] clusters, Cluster choosenCluster, Clusterable cj);

}
