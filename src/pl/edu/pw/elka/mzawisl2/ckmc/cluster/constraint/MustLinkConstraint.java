package pl.edu.pw.elka.mzawisl2.ckmc.cluster.constraint;

import pl.edu.pw.elka.mzawisl2.ckmc.cluster.Cluster;
import pl.edu.pw.elka.mzawisl2.ckmc.cluster.Clusterable;
import pl.edu.pw.elka.mzawisl2.ckmc.util.BaseUtils;

public class MustLinkConstraint implements Constraint {

	private Long leftItemId;

	private Long rightItemId;

	public MustLinkConstraint(Long leftItemId, Long rightItemId) {
		this.leftItemId = leftItemId;
		this.rightItemId = rightItemId;
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof MustLinkConstraint))
			return false;

		return (((MustLinkConstraint) other).leftItemId.equals(this.leftItemId) && ((MustLinkConstraint) other).rightItemId
				.equals(this.rightItemId))
				|| (((MustLinkConstraint) other).leftItemId.equals(this.rightItemId) && ((MustLinkConstraint) other).rightItemId
						.equals(this.leftItemId));
	}

	@Override
	public boolean violate(Cluster[] clusters, Cluster choosenCluster, Clusterable cj) {
		Long otherItemId = null;

		if (cj.getId().equals(leftItemId))
			otherItemId = rightItemId;
		else if (cj.getId().equals(rightItemId))
			otherItemId = leftItemId;

		if (null != otherItemId) {
			if (!BaseUtils.isNullOrEmpty(choosenCluster.getItems())) {
				for (Clusterable item : choosenCluster.getItems()) {
					if (item.getId().equals(otherItemId))
						return false;
				}
			}

			for (Cluster cluster : clusters) {
				if (cluster.getId().equals(choosenCluster.getId()))
					continue;

				if (!BaseUtils.isNullOrEmpty(cluster.getItems())) {
					for (Clusterable item : cluster.getItems()) {
						if (item.getId().equals(otherItemId))
							return true;
					}
				}
			}
		}

		return false;
	}

}
