package pl.edu.pw.elka.mzawisl2.ckmc.cluster.converge;

import pl.edu.pw.elka.mzawisl2.ckmc.cluster.Cluster;

public interface Converger {

	public boolean isAcceptable(Cluster[] before, Cluster[] after);

}
