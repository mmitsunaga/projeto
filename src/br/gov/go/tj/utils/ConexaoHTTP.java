package br.gov.go.tj.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class ConexaoHTTP {

	/**
	 * Realiza uma chamada HTTP conforme URL passada
	 * 
	 * @param URL
	 * @return
	 * @throws Exception
	 */
	public static String chamadaHTTP(String URL) throws Exception {
		String httpResponse = "";
		URLConnection conn;
		URL url = new URL(URL);
		conn = url.openConnection();
		//conn.setReadTimeout(timeout);
		conn.setDoOutput(true);
		//OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
		//wr.write(data);
		//wr.flush();
		BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String line;
		while ((line = rd.readLine()) != null) {
			httpResponse = httpResponse + "\n" + line;
		}
		//wr.close();
		rd.close();
		
		httpResponse.replace("\n", "");
		httpResponse = httpResponse.trim();
		
		return httpResponse;
	}

}
