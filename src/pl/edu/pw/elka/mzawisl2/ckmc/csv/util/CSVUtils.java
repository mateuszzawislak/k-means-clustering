package pl.edu.pw.elka.mzawisl2.ckmc.csv.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import pl.edu.pw.elka.mzawisl2.ckmc.cluster.constraint.CannotLinkConstraint;
import pl.edu.pw.elka.mzawisl2.ckmc.cluster.constraint.Constraint;
import pl.edu.pw.elka.mzawisl2.ckmc.cluster.constraint.MustLinkConstraint;
import pl.edu.pw.elka.mzawisl2.ckmc.cluster.vector.Dimension;
import pl.edu.pw.elka.mzawisl2.ckmc.config.ConfigService;
import pl.edu.pw.elka.mzawisl2.ckmc.config.ConfigService.Param;
import pl.edu.pw.elka.mzawisl2.ckmc.csv.CSVRow;
import pl.edu.pw.elka.mzawisl2.ckmc.csv.DictionaryColumn;
import pl.edu.pw.elka.mzawisl2.ckmc.csv.DoubleColumn;
import pl.edu.pw.elka.mzawisl2.ckmc.csv.IntegerColumn;
import pl.edu.pw.elka.mzawisl2.ckmc.csv.StringColumn;
import pl.edu.pw.elka.mzawisl2.ckmc.exception.CKMCException;
import pl.edu.pw.elka.mzawisl2.ckmc.util.BaseUtils;
import pl.edu.pw.elka.mzawisl2.ckmc.util.LogUtils;
import pl.edu.pw.elka.mzawisl2.ckmc.util.TextUtils;

public class CSVUtils {

	private static Logger log = Logger.getLogger(CSVUtils.class);

	private final static ConfigService configService = ConfigService.getInstance();

	private static Dimension getDimensionValue(String column, boolean dictionaryColumn) {
		if (dictionaryColumn) {
			return new DictionaryColumn(TextUtils.removeQuotes(column));
		}

		try {
			Integer value = Integer.parseInt(column);
			return new IntegerColumn(value);
		} catch (NumberFormatException e) {
			// ignore...
		}

		try {
			Double value = Double.parseDouble(column);
			return new DoubleColumn(value);
		} catch (NumberFormatException e) {
			// ignore...
		}

		return new StringColumn(TextUtils.removeQuotes(column));
	}

	private static List<Constraint> parseConstraintFile(String fileName, boolean mustLink) throws CKMCException {
		List<Constraint> constraints = new ArrayList<Constraint>();

		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = configService.getParam(Param.CSV_SPLIT_BY);

		try {
			br = new BufferedReader(new FileReader(fileName));
			while ((line = br.readLine()) != null) {
				String[] identifiers = line.split(cvsSplitBy);
				if (identifiers.length != 2)
					throw new CKMCException("Invalid constraints file " + fileName);

				if (mustLink)
					constraints.add(new MustLinkConstraint(Long.parseLong(identifiers[0]), Long.parseLong(identifiers[1])));
				else
					constraints.add(new CannotLinkConstraint(Long.parseLong(identifiers[0]), Long.parseLong(identifiers[1])));
			}

		} catch (FileNotFoundException e) {
			log.error("CSV file " + fileName + " not found!" + LogUtils.getDescr(e));
			throw new CKMCException("CSV file " + fileName + " not found!");
		} catch (IOException e) {
			log.error("Internal error: " + LogUtils.getDescr(e));
			throw new CKMCException("Internal error!");
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					log.error("Internal error: " + LogUtils.getDescr(e));
					throw new CKMCException("Internal error!");
				}
			}
		}

		return constraints;
	}

	public static List<Constraint> parseConstraintFiles() throws CKMCException {
		List<Constraint> constraints = new ArrayList<Constraint>();
		String mustLinkFileName = configService.getParam(Param.CSV_CONSTRAINTS_MUST_LINK_FILE_NAME);
		if (TextUtils.isSet(mustLinkFileName)) {
			constraints.addAll(parseConstraintFile(mustLinkFileName, true));
		}

		String cannotLinkFileName = configService.getParam(Param.CSV_CONSTRAINTS_CAN_NOT_LINK_FILE_NAME);
		if (TextUtils.isSet(cannotLinkFileName)) {
			constraints.addAll(parseConstraintFile(cannotLinkFileName, false));
		}

		return constraints;
	}

	public static List<CSVRow> parseFile() throws CKMCException {
		List<CSVRow> rows = new ArrayList<CSVRow>();

		BufferedReader br = null;
		Long currentId = 0L;
		String line = "";
		String cvsSplitBy = configService.getParam(Param.CSV_SPLIT_BY);
		String fileName = configService.getParam(Param.CSV_FILE_NAME);
		List<Integer> clusterableColumns = configService.getIntegerList(Param.CSV_CLUSTERABLE_COLUMNS);
		List<Integer> dictionaryColumns = configService.getIntegerList(Param.CSV_DICTIONARY_COLUMNS);

		try {
			br = new BufferedReader(new FileReader(fileName));
			while ((line = br.readLine()) != null) {
				String[] columns = line.split(cvsSplitBy);
				CSVRow csvRow = new CSVRow(currentId++, columns.length, clusterableColumns.isEmpty() ? columns.length
						: clusterableColumns.size());

				for (int i = 0; i < columns.length; ++i) {
					String column = columns[i];
					Dimension dimensionValue = getDimensionValue(column, dictionaryColumns.contains(i));
					if (BaseUtils.isNullOrEmpty(clusterableColumns) || clusterableColumns.contains(i)) {
						csvRow.addClusterableColumn(dimensionValue);
					} else {
						csvRow.addColumn(dimensionValue);
					}
				}

				rows.add(csvRow);
			}

		} catch (FileNotFoundException e) {
			log.error("CSV file " + fileName + " not found!" + LogUtils.getDescr(e));
			throw new CKMCException("CSV file " + fileName + " not found!");
		} catch (IOException e) {
			log.error("Internal error: " + LogUtils.getDescr(e));
			throw new CKMCException("Internal error!");
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					log.error("Internal error: " + LogUtils.getDescr(e));
					throw new CKMCException("Internal error!");
				}
			}
		}

		return rows;
	}
}
