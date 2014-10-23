package pl.edu.pw.elka.mzawisl2.ckmc.cluster.constraint;

import pl.edu.pw.elka.mzawisl2.ckmc.cluster.Cluster;
import pl.edu.pw.elka.mzawisl2.ckmc.cluster.Clusterable;
import pl.edu.pw.elka.mzawisl2.ckmc.util.BaseUtils;

public class CannotLinkConstraint implements Constraint {

	private Long leftItemId;

	private Long rightItemId;

	public CannotLinkConstraint(Long leftItemId, Long rightItemId) {
		this.leftItemId = leftItemId;
		this.rightItemId = rightItemId;
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof CannotLinkConstraint))
			return false;

		return (((CannotLinkConstraint) other).leftItemId.equals(this.leftItemId) && ((CannotLinkConstraint) other).rightItemId
				.equals(this.rightItemId))
				|| (((CannotLinkConstraint) other).leftItemId.equals(this.rightItemId) && ((CannotLinkConstraint) other).rightItemId
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
						return true;
				}
			}
		}

		return false;
	}
}
