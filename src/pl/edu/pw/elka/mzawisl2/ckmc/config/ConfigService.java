package pl.edu.pw.elka.mzawisl2.ckmc.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import pl.edu.pw.elka.mzawisl2.ckmc.exception.CKMCException;
import pl.edu.pw.elka.mzawisl2.ckmc.util.LogUtils;
import pl.edu.pw.elka.mzawisl2.ckmc.util.TextUtils;

public class ConfigService {

	public enum Param {
		K_NUMBER("cluster.k.number"),
		MAX_RECLUSTERING_NUMBER("cluster.max.reclustering.number"),
		DICTIONARY_DISTANCE_WAGE("dictionary.distance.wage"),
		CSV_FILE_NAME("csv.file.name"),
		CSV_SPLIT_BY("csv.split.by"),
		CSV_CLUSTERABLE_COLUMNS("csv.clusterable.columns"),
		CSV_DICTIONARY_COLUMNS("csv.dictionary.columns"),
		CSV_CONSTRAINTS_MUST_LINK_FILE_NAME("csv.constraints.must.link.file.name"),
		CSV_CONSTRAINTS_CAN_NOT_LINK_FILE_NAME("csv.constraints.can.not.link.file.name"),
		DRIFT_TELRANCE("cluster.drift.tolerance"),
		TEST_RETEST_NUMBER("test.retest.number"),
		TEST_CONSTRAINTS_MAX_NUMBER("test.constraints.max.number"),
		TEST_CLASS_COLUMN_INDEX("test.class.column.index"),
		TEST_CONSTRAINTS_STEP_SIZE("test.constraints.step.size");

		private String name;

		Param(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}

	private static Logger log = Logger.getLogger(ConfigService.class);

	public static ConfigService getInstance() {
		if (instance == null) {
			synchronized (ConfigService.class) {
				if (instance == null) {
					try {
						instance = new ConfigService();
					} catch (CKMCException e) {
						log.error("Failed to init ConfigService");
						return null;
					}
				}
			}
		}
		return instance;
	}

	private String configFile = System.getProperty("pl.edu.pw.elka.mzawisl2.ckmc.config", "ckmc.properties");

	private final static String LIST_SEPARATOR = ",";

	private Properties props;

	private static volatile ConfigService instance = null;

	private ConfigService() throws CKMCException {
		init();
	}

	public List<Integer> getIntegerList(Param param) throws CKMCException {
		String types = getParam(param);
		List<Integer> results = new ArrayList<Integer>();

		if (!TextUtils.isSet(types))
			return results;

		String[] typeList = types.split(LIST_SEPARATOR);

		for (int i = 0; i < typeList.length; i++) {
			String name = typeList[i].trim();
			try {
				Integer value = Integer.parseInt(name);
				results.add(value);
			} catch (NumberFormatException e) {
				log.error("Invalid field type. Expected Integer!" + LogUtils.getDescr(e));
				throw new CKMCException("Invalid field type. Expected Integer!");
			}
		}

		return results;
	}

	public String getParam(Param param) {
		return getParam(param, null);
	}

	public String getParam(Param param, String defaultValue) {
		if (null == param)
			return null;

		String _val = props.getProperty(param.getName());
		return _val != null ? _val.trim() : defaultValue;
	}

	public Boolean getParamBool(Param param) {
		return getParamBool(param, null);
	}

	public Boolean getParamBool(Param param, Boolean defaultValue) {
		String val = getParam(param);

		if (null == val)
			return defaultValue;

		return TextUtils.toBool(val);
	}

	public Float getParamFloat(Param param) {
		return getParamFloat(param, null);
	}

	public Float getParamFloat(Param param, Float defaultValue) {
		if (null == param)
			return null;

		Float _val = null;

		try {
			_val = Float.parseFloat(getParam(param));
		} catch (NumberFormatException e) {
			// ignore
		} catch (NullPointerException e) {
			// ignore
		}

		return _val != null ? _val : defaultValue;
	}

	public Integer getParamInt(Param param) {
		return getParamInt(param, null);
	}

	public Integer getParamInt(Param param, Integer defaultValue) {
		if (null == param)
			return null;

		Integer _val = null;

		try {
			_val = Integer.parseInt(getParam(param));
		} catch (NumberFormatException e) {
			// ignore
		} catch (NullPointerException e) {
			// ignore
		}

		return _val != null ? _val : defaultValue;
	}

	public Long getParamLong(Param param) {
		return getParamLong(param, null);
	}

	public Long getParamLong(Param param, Long defaultValue) {
		String val = getParam(param);

		if (null == val)
			return defaultValue;

		return TextUtils.toLong(val);
	}

	private void init() throws CKMCException {
		load();
	}

	private void load() throws CKMCException {
		Properties _props = new Properties();

		if (null == configFile)
			return;

		configFile = configFile.trim();
		if (!TextUtils.isSet(configFile))
			return;

		InputStream is = null;
		try {
			is = new FileInputStream(configFile);
			_props.load(is);
		} catch (IOException e) {
			log.error("Failed to load configuration from " + new File(configFile).getAbsolutePath() + "\n" + LogUtils.getDescr(e));
			throw new CKMCException("Failed to load configuration from " + new File(configFile).getAbsolutePath());
		} finally {
			if (null != is) {
				try {
					is.close();
				} catch (IOException e) {
					// ignore
				}
			}
		}

		// activate config
		props = _props;
	}

}
