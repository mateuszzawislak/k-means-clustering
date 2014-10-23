package pl.edu.pw.elka.mzawisl2.ckmc;

import org.apache.log4j.Logger;

import pl.edu.pw.elka.mzawisl2.ckmc.cluster.Cluster;
import pl.edu.pw.elka.mzawisl2.ckmc.cluster.ConstrainedKMeansClusterer;
import pl.edu.pw.elka.mzawisl2.ckmc.cluster.KClusterer;
import pl.edu.pw.elka.mzawisl2.ckmc.config.ConfigService;
import pl.edu.pw.elka.mzawisl2.ckmc.config.ConfigService.Param;
import pl.edu.pw.elka.mzawisl2.ckmc.csv.util.CSVUtils;
import pl.edu.pw.elka.mzawisl2.ckmc.exception.CKMCException;
import pl.edu.pw.elka.mzawisl2.ckmc.exception.IllegalClustersException;
import pl.edu.pw.elka.mzawisl2.ckmc.util.LogUtils;

public class Main {

	private static Logger log = Logger.getLogger(Main.class);

	private final static ConfigService configService = ConfigService.getInstance();

	public static void main(String[] args) {
		try {
			KClusterer clusterer = new ConstrainedKMeansClusterer(CSVUtils.parseConstraintFiles(),
					configService.getParamInt(Param.MAX_RECLUSTERING_NUMBER));
			Cluster[] clusters;

			clusters = clusterer.cluster(CSVUtils.parseFile(), configService.getParamInt(Param.K_NUMBER));

			for (Cluster c : clusters) {
				System.out.println(c);
			}
		} catch (IllegalClustersException e) {
			log.error("Illegal clusters were choosen: " + LogUtils.getDescr(e));
		} catch (CKMCException ee) {
			log.error("Something went wrong: " + LogUtils.getDescr(ee));
		} catch (Throwable t) {
			log.error("Internal error: " + LogUtils.getDescr(t));
		}

		System.exit(0);
	}
}
