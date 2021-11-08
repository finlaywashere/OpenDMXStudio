package xyz.finlaym.opendmx;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpdateChecker {
	public boolean checkUpdate() {
		try {
			URL url = new URL(Constants.UPDATE_ENDPOINT);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(1000);
			conn.setReadTimeout(1000);
			HttpURLConnection.setFollowRedirects(true);
			int status = conn.getResponseCode();
			if(status != 200) {
				System.err.println("Failed to retrieve update information!");
				System.err.println("Status code: "+status);
				return false;
			}
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String inputLine;
			StringBuffer content = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				content.append(inputLine);
			}
			in.close();
			conn.disconnect();
			String data = content.toString();
			String[] split = data.split(":");
			if(split.length != 2) {
				System.err.println("Received invalid data from update server");
				return false;
			}
			if(!Utils.isInt(split[0]) || !Utils.isInt(split[1])) {
				System.err.println("Received invalid data from update server");
				return false;
			}
			if(Integer.valueOf(split[0]) != Constants.VERSION_MAJOR || Integer.valueOf(split[1]) != Constants.VERSION_MINOR)
				return true;
			return false;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
