package com.track.util;

import java.util.HashMap;

/**
 * 
 * @author Shayanthan K
 * @version 1.0.0
 * @created Aug 24, 2010 - 2:38:32 PM
 */
public interface IMLogger {

	public HashMap<String, String> populateMapData(String companyCode,
			String userCode);

	public void setMapDataToLogger(HashMap<String, String> dataForLogging);
}
