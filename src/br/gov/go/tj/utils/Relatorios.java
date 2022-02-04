package br.gov.go.tj.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;
import net.sf.jasperreports.engine.export.oasis.JROdtExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOdtReportConfiguration;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfReportConfiguration;
import br.gov.go.tj.utils.pdf.InterfaceBeanJasper;
import br.gov.go.tj.utils.pdf.InterfaceJasper;
import br.gov.go.tj.utils.pdf.InterfaceMultiplaSubReportJasper;

/**
 * Classe utilit�ria com os m�todos padr�es utilizados na gera��o dos relat�rios
 * do sistema.
 * @author hmgodinho
 *
 */
public class Relatorios {

	public Relatorios() {
	}

	/**
	 * M�todo que retorna o local exato onde est�o armazenados os relat�rios do
	 * projeto.
	 * 
	 * @param diretorioProjeto
	 *            - diret�rio raiz do projeto
	 * @return caminho do local dos relat�rios
	 * @author hmgodinho
	 */
	public static String getPathRelatorio(String diretorioProjeto) {
		return diretorioProjeto + "WEB-INF" + File.separator + "relatorios" + File.separator;
	}

	/**
	 * M�todo que retorna o caminho exato para o arquivo
	 * 
	 * @param diretorioProjeto
	 *            - diret�rio raiz do projeto
	 * @return caminho do logotipo dos relat�rios
	 * @author hmgodinho
	 */
	public static String getCaminhoLogotipoRelatorio(String diretorioProjeto) {
		return diretorioProjeto + "imagens" + File.separator + "brasaoGoias.png";
	}

	/**
	 * M�todo que gera o arquivo de relat�rio do tipo PDF a partir dos par�metros passados.
	 * 
	 * @param pathRelatorio - diret�rio dos relat�rios
	 * @param nomeRelatorio - nome do arquivo do relat�rio jasper armazenado
	 * @param parametros - par�metros que devem ser enviados para gerar o relat�rio
	 * @param listaFields - lista de fields que devem ser inseridos nos detail do relat�rio
	 * @return byte[] com o relat�rio montado
	 * @author hmgodinho
	 */
	public static byte[] gerarRelatorioPdf(String pathRelatorio, String nomeRelatorio, Map parametros, List listaFields) throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		InterfaceJasper ei = new InterfaceJasper(listaFields);

		JasperPrint jp;
		try{
			jp = JasperFillManager.fillReport(pathRelatorio + nomeRelatorio + ".jasper", parametros, ei);
			JRPdfExporter jr = new JRPdfExporter();
			jr.setExporterInput(new SimpleExporterInput(jp));
			jr.setExporterOutput(new SimpleOutputStreamExporterOutput(baos));
			SimplePdfReportConfiguration conf = new SimplePdfReportConfiguration();
			jr.setConfiguration(conf);
			jr.exportReport();
			
			byte[] temp = baos.toByteArray();
			baos.close();
			return  temp;

		} catch(JRException e) {
			try{if (baos!=null) baos.close(); } catch(Exception ex ) {};		
			throw e;
		}
		
	}
	
	public static byte[] gerarRelatorioPdfSemDetail(String pathRelatorio, String nomeRelatorio, Map parametros) throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		InterfaceBeanJasper ei = new InterfaceBeanJasper(parametros);

		JasperPrint jp;
		try{
			jp = JasperFillManager.fillReport(pathRelatorio + nomeRelatorio + ".jasper", parametros, ei);
			JRPdfExporter jr = new JRPdfExporter();
			jr.setExporterInput(new SimpleExporterInput(jp));
			jr.setExporterOutput(new SimpleOutputStreamExporterOutput(baos));
			SimplePdfReportConfiguration conf = new SimplePdfReportConfiguration();
			jr.setConfiguration(conf);
			jr.exportReport();
			
			byte[] temp = baos.toByteArray();
			baos.close();
			return  temp;

		} catch(JRException e) {
			try{if (baos!=null) baos.close(); } catch(Exception ex ) {};		
			throw e;
		}
		
	}
	
	/**
	 * M�todo que gera o arquivo de relat�rio do tipo ODT a partir dos par�metros passados.
	 * 
	 * @param pathRelatorio - diret�rio dos relat�rios
	 * @param nomeRelatorio - nome do arquivo do relat�rio jasper armazenado
	 * @param parametros - par�metros que devem ser enviados para gerar o relat�rio
	 * @param listaFields - lista de fields que devem ser inseridos nos detail do relat�rio
	 * @return byte[] com o relat�rio montado
	 * @author hmgodinho
	 */
	public static byte[] gerarRelatorioOdt(String pathRelatorio, String nomeRelatorio, Map parametros, List listaFields) throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		InterfaceJasper ei = new InterfaceJasper(listaFields);

		JasperPrint jp;
		try{
	        jp = JasperFillManager.fillReport(pathRelatorio + nomeRelatorio + ".jasper", parametros, ei);
	        JROdtExporter jr = new JROdtExporter();
	        jr.setExporterInput(new SimpleExporterInput(jp));
	        jr.setExporterOutput(new SimpleOutputStreamExporterOutput(baos));
	        SimpleOdtReportConfiguration config = new SimpleOdtReportConfiguration();
	        jr.setConfiguration(config);
	        jr.exportReport();	
			
	        byte[] temp = baos.toByteArray();
			baos.close();
			return  temp;

		} catch(JRException e) {
			try{if (baos!=null) baos.close(); } catch(Exception ex ) {};		
			throw e;
		}
		
	}
	
	/**
	 * M�todo que gera o arquivo de relat�rio do tipo PDF a partir dos
	 * par�metros passados, para mais de um subrelatorio
	 * 
	 * @param pathRelatorio
	 *            - diret�rio dos relat�rios
	 * @param nomeRelatorio
	 *            - nome do arquivo do relat�rio jasper armazenado
	 * @param parametros
	 *            - par�metros que devem ser enviados para gerar o relat�rio
	 * @return byte[] com o relat�rio montado
	 * @author kbsriccioppo
	 */
	public static byte[] gerarSubRelatoriosPdf(String pathRelatorio, String nomeRelatorio, Map parametro, Map subRelatorio) throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		//interface utilizada para montar mais de um subrelat�rio
		InterfaceMultiplaSubReportJasper ei = new InterfaceMultiplaSubReportJasper(subRelatorio);

		JasperPrint jp;
		try{
			jp = JasperFillManager.fillReport(pathRelatorio + nomeRelatorio + ".jasper", parametro, ei);
			JRPdfExporter jr = new JRPdfExporter();
			jr.setParameter(JRExporterParameter.JASPER_PRINT, jp);
			jr.setParameter(JRPdfExporterParameter.OUTPUT_STREAM, baos);
			jr.exportReport();
			byte[] temp = baos.toByteArray();
			
			baos.close();
			return  temp;

		} catch(JRException e) {
			try{if (baos!=null) baos.close(); } catch(Exception ex ) {};
			throw e;
		}
		
	}

	/**
	 * M�todo que gera o arquivo de relat�rio do tipo TXT a partir dos par�metros passados. Este m�todo faz grava��o do arquivo 
	 * em disco e pode trazer problemas por conta das permiss�es de leitura/escrita na pasta do pathRelatorio.
	 * 
	 * @param pathRelatorio - diret�rio dos relat�rios
	 * @param nomeRelatorio - nome do arquivo do relat�rio txt que ser� gerado
	 * @param conteudoArquivo - uma string que deve conter todo o conte�do do arquivo do relat�rio a ser gerado
	 * @return byte[] com o relat�rio montado
	 * @author hmgodinho
	 */
	public static byte[] gerarRelatorioTxt(String pathRelatorio, String nomeRelatorio, String conteudoArquivo) throws Exception {
		byte[] temp = null;

		// o false significa que o arquivo n�o ser� constante
		FileWriter arquivo = new FileWriter(pathRelatorio + nomeRelatorio + ".txt", false);
		arquivo.write(conteudoArquivo); // armazena o texto no
		// objeto x, que aponta para
		// o arquivo
		arquivo.close(); // cria o arquivo

		BufferedReader reader = new BufferedReader(new FileReader(pathRelatorio + nomeRelatorio + ".txt"));
		try{
			String linha = reader.readLine();

			StringBuffer arquivoLido = new StringBuffer();
			while (linha != null) {
				arquivoLido.append(linha + "\n");
				linha = reader.readLine();
			}

			temp = arquivoLido.toString().getBytes();
		} catch(Exception e) {
			e.printStackTrace();
		} finally{
			reader.close();
		}
		return temp;
	}

	/**
	 * M�todo que retorna o separador de colunas utilizados nos arquivos TXT de
	 * relat�rios.
	 * 
	 * @return separador de texto
	 * @author hmgodinho
	 */
	public static String getSeparadorRelatorioTxt() {
		return "#";
	}

}