
package com.hasim.genesis.api.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Class for utility methods
 * 
 * @author Hasim Mollah
 *
 */
public class CommonUtil {
	private static final Logger logger = LogManager.getLogger(CommonUtil.class);
	private static final String VALIDATOR_PATTERN = "[<>]" + "|" + "&lt;" + "|" + "&gt;";

	private CommonUtil() {
		// making sure private constructor for util
	}

	/**
	 * @param requestParam
	 * @return boolean value.
	 */
	public static boolean validateRequestParameter(String requestParam) {

		Pattern pattern = Pattern.compile(VALIDATOR_PATTERN, Pattern.CASE_INSENSITIVE);

		Matcher headerMatcher = pattern.matcher(requestParam);
		return headerMatcher.find();
	}

	/**
	 * @param object
	 *            anyObject.
	 * @return PrintWriter writer.
	 * @throws JsonProcessingException
	 */
	public static String convertObjectToJson(Object object) throws JsonProcessingException {
		if (object == null) {
			return null;
		}
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(object);
	}

	public static <T> T convertStringToObject(String inputJson, Class<T> t) {
		ObjectMapper mapper = new ObjectMapper();
		T response = null;
		try {
			response = mapper.readValue(inputJson, t);
		} catch (Exception e) {
			logger.error(e);
		}
		return response;
	}

	

}
