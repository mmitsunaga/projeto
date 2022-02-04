package br.gov.go.tj.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class Web {
			
	public static String sendPostUrl(String stUrl, String dados) {
		URL url;
		StringBuilder stTemp = new StringBuilder();

		try{
			url = new URL(stUrl);
			//System.setProperty("http.proxyHost", ProjudiPropriedades.getPropriedades(ProjudiPropriedades.enderecoProxy));
			//System.setProperty("http.proxyPort", ProjudiPropriedades.getPropriedades(ProjudiPropriedades.portaProxy));
			
			URLConnection conn = url.openConnection();			
			conn.setDoInput(true);

			conn.setDoOutput(true);

			DataOutputStream out = new DataOutputStream(conn.getOutputStream());

			out.writeBytes(dados);
			out.flush();
			out.close();

			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line = "";
			while ((line = in.readLine()) != null) {
				stTemp.append(line);
			}
			in.close();

		} catch(MalformedURLException ex) {
			ex.printStackTrace();
		} catch(IOException ex) {
			ex.printStackTrace();
		}

		return stTemp.toString();
	}
	
	public static String sendGetUrl(String stUrl) throws IOException {
		URL url;
		StringBuilder stTemp = new StringBuilder();

		url = new URL(stUrl);

		HttpURLConnection  conn = (HttpURLConnection) url.openConnection();			
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setRequestMethod("GET");

		BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String line = "";
		while ((line = in.readLine()) != null) {
			stTemp.append(line);
		}
		in.close();

		return stTemp.toString();
	}
}
