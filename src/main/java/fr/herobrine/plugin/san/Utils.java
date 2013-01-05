package fr.herobrine.plugin.san;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Class provinding practical methods
 * @author Sébastien Mériot
 */
public class Utils {

	/**
	 * Build a parametrized URL
	 * @param baseUrl Base url to append params
	 * @param params Params to append
	 * @return Full url
	 */
	public static String BuildFullUrl(String baseUrl, Map<String, String> param) {

		// If no parameters, do not continue
		if (param.isEmpty()) {
			return baseUrl;
		}

		StringBuffer finalUrl = new StringBuffer(baseUrl);

		Set<String> keys = param.keySet();
		Iterator<String> it = keys.iterator();

		finalUrl.append('?');

		// For each parameter, append it to the final url
		while (it.hasNext()) {
			String currentKey = it.next();
			String currentVal = param.get(currentKey);

			finalUrl.append(currentKey);
			finalUrl.append("=");
			finalUrl.append(currentVal);

			if (it.hasNext()) {
				finalUrl.append('&');
			}
		}

		return finalUrl.toString();
	}

}
