package pl.edu.pw.elka.mzawisl2.ckmc.cluster.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pl.edu.pw.elka.mzawisl2.ckmc.cluster.Cluster;
import pl.edu.pw.elka.mzawisl2.ckmc.cluster.Clusterable;
import pl.edu.pw.elka.mzawisl2.ckmc.cluster.vector.Dimension;
import pl.edu.pw.elka.mzawisl2.ckmc.cluster.vector.Vector;
import pl.edu.pw.elka.mzawisl2.ckmc.csv.DictionaryColumn;
import pl.edu.pw.elka.mzawisl2.ckmc.csv.DoubleColumn;
import pl.edu.pw.elka.mzawisl2.ckmc.csv.IntegerColumn;
import pl.edu.pw.elka.mzawisl2.ckmc.csv.StringColumn;
import pl.edu.pw.elka.mzawisl2.ckmc.util.BaseUtils;

public class ClusterUtils {

	public static Double getDistanceToCluster(Cluster cluster, Clusterable item) {
		return cluster.getCentroidLocation().getDistance(item.getLocation());
	}

	public static Vector getMeanValue(List<Clusterable> items) {
		assert (items != null);
		assert (items.size() > 0);

		int vectorLength = items.get(0).getLocation().getLength();

		Dimension[] newLocation = new Dimension[vectorLength];
		for (int i = 0; i < vectorLength; ++i) {
			Dimension dimension = items.get(0).getLocation().getDimensions()[i];

			if (dimension instanceof DoubleColumn) {
				double sum = 0.;
				for (Clusterable item : items) {
					sum += ((DoubleColumn) item.getLocation().getDimensions()[i]).getValue();
				}
				newLocation[i] = new DoubleColumn(sum / items.size());
			} else if (dimension instanceof IntegerColumn) {
				Integer sum = 0;
				for (Clusterable item : items) {
					sum += ((IntegerColumn) item.getLocation().getDimensions()[i]).getValue();
				}
				newLocation[i] = new IntegerColumn(sum / items.size());
			} else if (dimension instanceof DictionaryColumn) {
				List<String> clazzes = new ArrayList<String>();
				for (Clusterable item : items) {
					clazzes.add(((DictionaryColumn) item.getLocation().getDimensions()[i]).getValue());
				}
				newLocation[i] = new DictionaryColumn(BaseUtils.getMostCommonValue(clazzes));
			} else if (dimension instanceof StringColumn) {
				List<String> clazzes = new ArrayList<String>();
				for (Clusterable item : items) {
					clazzes.add(((StringColumn) item.getLocation().getDimensions()[i]).getValue());
				}
				Collections.sort(clazzes);
				newLocation[i] = new StringColumn(clazzes.get(clazzes.size() / 2));
			} else {
				throw new RuntimeException("Unknown dimension type!");
			}

		}

		return new Vector(newLocation);
	}

	public static void unassignClusterItems(Cluster[] clusters) {
		for (Cluster cluster : clusters) {
			cluster.clearItems();
		}
	}

}
