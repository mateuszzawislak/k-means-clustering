package pl.edu.pw.elka.mzawisl2.ckmc.cluster;

import pl.edu.pw.elka.mzawisl2.ckmc.cluster.vector.Vector;

public interface Clusterable {

	public Long getClusterId();

	public Long getId();

	public Vector getLocation();

	public void setClusterId(Long id);
}
