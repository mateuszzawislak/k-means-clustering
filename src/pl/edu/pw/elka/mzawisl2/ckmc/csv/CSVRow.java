package pl.edu.pw.elka.mzawisl2.ckmc.csv;

import pl.edu.pw.elka.mzawisl2.ckmc.cluster.Clusterable;
import pl.edu.pw.elka.mzawisl2.ckmc.cluster.vector.Dimension;
import pl.edu.pw.elka.mzawisl2.ckmc.cluster.vector.Vector;

public class CSVRow implements Clusterable {

	private Dimension[] allColumns;

	private Dimension[] clusterableColumns;

	private int currColumnIndex = 0;

	private int currClusterableColumnIndex = 0;

	private Long clusterId;

	private Long id;

	public CSVRow(Long id, int allColumnsNumber, int clusterableColumnsNumber) {
		this.id = id;
		allColumns = new Dimension[allColumnsNumber];
		clusterableColumns = new Dimension[clusterableColumnsNumber];
	}

	public void addClusterableColumn(Dimension column) {
		addColumn(column);
		clusterableColumns[currClusterableColumnIndex++] = column;
	}

	public void addColumn(Dimension column) {
		allColumns[currColumnIndex++] = column;
	}

	public Dimension[] getAllColumns() {
		return allColumns;
	}

	@Override
	public Long getClusterId() {
		return clusterId;
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public Vector getLocation() {
		return new Vector(clusterableColumns);
	}

	@Override
	public void setClusterId(Long id) {
		this.clusterId = id;
	}

	@Override
	public String toString() {
		StringBuffer str = new StringBuffer();
		str.append("[");

		if (null != allColumns) {
			for (int i = 0; i < allColumns.length; ++i) {
				Dimension dimension = allColumns[i];
				str.append(dimension.toString());
				if (i < allColumns.length - 1)
					str.append(", ");
			}
		}

		str.append("]");

		return str.toString();
	}

}
