package pl.edu.pw.elka.mzawisl2.ckmc.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class BaseUtils {

	public static <T extends Collection<C>, C> boolean isNullOrEmpty(T ref) {
		if (null == ref || ref.isEmpty()) {
			return true;
		}

		return false;
	}

	public static <T> T getMostCommonValue(List<T> list) {
		Map<T, Integer> map = new HashMap<>();

		for (T t : list) {
			Integer val = map.get(t);
			map.put(t, val == null ? 1 : val + 1);
		}

		Entry<T, Integer> max = null;

		for (Entry<T, Integer> e : map.entrySet()) {
			if (max == null || e.getValue() > max.getValue())
				max = e;
		}

		return max.getKey();
	}
}
