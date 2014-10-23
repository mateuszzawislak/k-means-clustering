package pl.edu.pw.elka.mzawisl2.ckmc;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;

import pl.edu.pw.elka.mzawisl2.ckmc.cluster.Cluster;
import pl.edu.pw.elka.mzawisl2.ckmc.cluster.Clusterable;
import pl.edu.pw.elka.mzawisl2.ckmc.cluster.ConstrainedKMeansClusterer;
import pl.edu.pw.elka.mzawisl2.ckmc.cluster.KClusterer;
import pl.edu.pw.elka.mzawisl2.ckmc.cluster.constraint.CannotLinkConstraint;
import pl.edu.pw.elka.mzawisl2.ckmc.cluster.constraint.Constraint;
import pl.edu.pw.elka.mzawisl2.ckmc.cluster.constraint.MustLinkConstraint;
import pl.edu.pw.elka.mzawisl2.ckmc.config.ConfigService;
import pl.edu.pw.elka.mzawisl2.ckmc.config.ConfigService.Param;
import pl.edu.pw.elka.mzawisl2.ckmc.csv.CSVRow;
import pl.edu.pw.elka.mzawisl2.ckmc.csv.util.CSVUtils;
import pl.edu.pw.elka.mzawisl2.ckmc.exception.CKMCException;
import pl.edu.pw.elka.mzawisl2.ckmc.exception.IllegalClustersException;
import pl.edu.pw.elka.mzawisl2.ckmc.exception.IllegalDimensionException;
import pl.edu.pw.elka.mzawisl2.ckmc.util.BaseUtils;
import pl.edu.pw.elka.mzawisl2.ckmc.util.LogUtils;

public class TestMain {

	private static Logger log = Logger.getLogger(TestMain.class);

	private final static ConfigService configService = ConfigService.getInstance();

	private static boolean constraintExists(Constraint constraint, List<Constraint> constraints) {
		if (!BaseUtils.isNullOrEmpty(constraints)) {
			for (Constraint constraint2 : constraints) {
				if (constraint2.equals(constraint))
					return true;
			}
		}

		return false;
	}

	private static List<Constraint> generateConstraints(List<CSVRow> rows, int number, int classIndex) throws IllegalDimensionException {
		Random random = new Random(System.currentTimeMillis());
		List<Constraint> constraints = new ArrayList<Constraint>();

		while (number > 0) {
			int randIndex = random.nextInt(rows.size());
			int otherIndex = 0;

			while (true) {
				otherIndex = random.nextInt(rows.size());

				if (otherIndex != randIndex)
					break;
			}

			boolean mustLink = 0 == rows.get(otherIndex).getAllColumns()[classIndex]
					.getDistance(rows.get(randIndex).getAllColumns()[classIndex]) ? true : false;

			Constraint constraint = mustLink ? new MustLinkConstraint((long) randIndex, (long) otherIndex) : new CannotLinkConstraint(
					(long) randIndex, (long) otherIndex);

			if (!constraintExists(constraint, constraints)) {
				number--;
				constraints.add(constraint);
			}
		}

		return constraints;
	}

	public static void main(String[] args) {
		List<String> messages = new ArrayList<String>();

		try {
			int maxConstraintsNumber = configService.getParamInt(Param.TEST_CONSTRAINTS_MAX_NUMBER);
			int retestsNumber = configService.getParamInt(Param.TEST_RETEST_NUMBER);
			int classColumn = configService.getParamInt(Param.TEST_CLASS_COLUMN_INDEX);
			int stepSize = configService.getParamInt(Param.TEST_CONSTRAINTS_STEP_SIZE);

			for (int i = 0; i <= maxConstraintsNumber; i += stepSize) {
				int sumTime = 0;
				double sumRand = 0.;

				for (int j = 0; j < retestsNumber; j++) {
					List<CSVRow> rows = CSVUtils.parseFile();
					List<Constraint> constraints = generateConstraints(rows, i, classColumn);
					KClusterer clusterer = new ConstrainedKMeansClusterer(constraints,
							configService.getParamInt(Param.MAX_RECLUSTERING_NUMBER));
					Cluster[] clusters;

					Long start = System.currentTimeMillis();
					clusters = clusterer.cluster(rows, configService.getParamInt(Param.K_NUMBER));
					Long end = System.currentTimeMillis();

					sumTime += end - start;
					sumRand += randIndex(clusters, classColumn);
				}
				double avgTime = (double) sumTime / (double) retestsNumber;
				double avgRand = sumRand / retestsNumber;
				log.info("time: " + avgTime + " constraints number: " + i + " randIndex: " + avgRand);
				messages.add(i + "," + avgTime + "," + avgRand);
			}
		} catch (IllegalClustersException e) {
			log.error("Illegal clusters were choosen: " + LogUtils.getDescr(e));
		} catch (CKMCException ee) {
			log.error("Something went wrong: " + LogUtils.getDescr(ee));
		} catch (Throwable t) {
			log.error("Internal error: " + LogUtils.getDescr(t));
		}

		for (String msg : messages) {
			log.info(msg);
		}
		System.exit(0);
	}

	private static double randIndex(Cluster[] clusters, int classColumn) throws IllegalDimensionException {
		List<Clusterable> allRows = new ArrayList<Clusterable>();
		for (Cluster cluster : clusters) {
			allRows.addAll(cluster.getItems());
		}

		Long a = 0L, b = 0L, c = 0L, d = 0L;

		for (int i = 0; i < allRows.size(); ++i) {
			for (int j = i + 1; j < allRows.size(); ++j) {
				CSVRow one = (CSVRow) allRows.get(i);
				CSVRow second = (CSVRow) allRows.get(j);

				boolean sameInGenerated = one.getClusterId().equals(second.getClusterId());
				boolean sameInIdeal = 0 == one.getAllColumns()[classColumn].getDistance(second.getAllColumns()[classColumn]) ? true : false;

				if (sameInGenerated && sameInIdeal)
					a++;
				if (!sameInGenerated && sameInIdeal)
					d++;
				if (sameInGenerated && !sameInIdeal)
					c++;
				if (!sameInGenerated && !sameInIdeal)
					b++;
			}
		}

		return ((double) (a + b)) / ((double) (a + b + c + d));
	}
}
