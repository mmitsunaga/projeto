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
 * Classe utilitária com os métodos padrões utilizados na geração dos relatórios
 * do sistema.
 * @author hmgodinho
 *
 */
public class Relatorios {

	public Relatorios() {
	}

	/**
	 * Método que retorna o local exato onde estão armazenados os relatórios do
	 * projeto.
	 * 
	 * @param diretorioProjeto
	 *            - diretório raiz do projeto
	 * @return caminho do local dos relatórios
	 * @author hmgodinho
	 */
	public static String getPathRelatorio(String diretorioProjeto) {
		return diretorioProjeto + "WEB-INF" + File.separator + "relatorios" + File.separator;
	}

	/**
	 * Método que retorna o caminho exato para o arquivo
	 * 
	 * @param diretorioProjeto
	 *            - diretório raiz do projeto
	 * @return caminho do logotipo dos relatórios
	 * @author hmgodinho
	 */
	public static String getCaminhoLogotipoRelatorio(String diretorioProjeto) {
		return diretorioProjeto + "imagens" + File.separator + "brasaoGoias.png";
	}

	/**
	 * Método que gera o arquivo de relatório do tipo PDF a partir dos parâmetros passados.
	 * 
	 * @param pathRelatorio - diretório dos relatórios
	 * @param nomeRelatorio - nome do arquivo do relatório jasper armazenado
	 * @param parametros - parâmetros que devem ser enviados para gerar o relatório
	 * @param listaFields - lista de fields que devem ser inseridos nos detail do relatório
	 * @return byte[] com o relatório montado
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
	 * Método que gera o arquivo de relatório do tipo ODT a partir dos parâmetros passados.
	 * 
	 * @param pathRelatorio - diretório dos relatórios
	 * @param nomeRelatorio - nome do arquivo do relatório jasper armazenado
	 * @param parametros - parâmetros que devem ser enviados para gerar o relatório
	 * @param listaFields - lista de fields que devem ser inseridos nos detail do relatório
	 * @return byte[] com o relatório montado
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
	 * Método que gera o arquivo de relatório do tipo PDF a partir dos
	 * parâmetros passados, para mais de um subrelatorio
	 * 
	 * @param pathRelatorio
	 *            - diretório dos relatórios
	 * @param nomeRelatorio
	 *            - nome do arquivo do relatório jasper armazenado
	 * @param parametros
	 *            - parâmetros que devem ser enviados para gerar o relatório
	 * @return byte[] com o relatório montado
	 * @author kbsriccioppo
	 */
	public static byte[] gerarSubRelatoriosPdf(String pathRelatorio, String nomeRelatorio, Map parametro, Map subRelatorio) throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		//interface utilizada para montar mais de um subrelatório
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
	 * Método que gera o arquivo de relatório do tipo TXT a partir dos parâmetros passados. Este método faz gravação do arquivo 
	 * em disco e pode trazer problemas por conta das permissões de leitura/escrita na pasta do pathRelatorio.
	 * 
	 * @param pathRelatorio - diretório dos relatórios
	 * @param nomeRelatorio - nome do arquivo do relatório txt que será gerado
	 * @param conteudoArquivo - uma string que deve conter todo o conteúdo do arquivo do relatório a ser gerado
	 * @return byte[] com o relatório montado
	 * @author hmgodinho
	 */
	public static byte[] gerarRelatorioTxt(String pathRelatorio, String nomeRelatorio, String conteudoArquivo) throws Exception {
		byte[] temp = null;

		// o false significa que o arquivo não será constante
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
	 * Método que retorna o separador de colunas utilizados nos arquivos TXT de
	 * relatórios.
	 * 
	 * @return separador de texto
	 * @author hmgodinho
	 */
	public static String getSeparadorRelatorioTxt() {
		return "#";
	}

}