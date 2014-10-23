package pl.edu.pw.elka.mzawisl2.ckmc.cluster;

import java.util.LinkedList;
import java.util.List;

import pl.edu.pw.elka.mzawisl2.ckmc.cluster.vector.Vector;
import pl.edu.pw.elka.mzawisl2.ckmc.util.BaseUtils;

public class Cluster {

	private Vector centroidLocation;

	private List<Clusterable> items;

	private Long id;

	public Cluster(Vector location, Long id) {
		centroidLocation = location;
		items = new LinkedList<Clusterable>();
		this.id = id;
	}

	public void addItem(Clusterable item) {
		items.add(item);
	}

	public void clearItems() {
		if (!BaseUtils.isNullOrEmpty(items)) {
			for (Clusterable item : items) {
				item.setClusterId(null);
			}
		}

		this.items.clear();
	}

	public Vector getCentroidLocation() {
		return centroidLocation;
	}

	public Long getId() {
		return id;
	}

	public List<Clusterable> getItems() {
		return items;
	}

	public void removeItem(Clusterable item) {
		items.remove(item);
	}

	public void setCentroidLocation(Vector location) {
		centroidLocation = location;
	}

	@Override
	public String toString() {
		StringBuffer str = new StringBuffer();
		str.append("Id: " + id + " centroid: ");
		str.append("Centroid: " + centroidLocation + "\n");

		if (!BaseUtils.isNullOrEmpty(items)) {
			for (Clusterable item : items) {
				str.append(item.getId() + ": " + item.toString() + "\n");
			}
		}

		return str.toString();
	}
}
