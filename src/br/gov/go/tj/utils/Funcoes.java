/*
 * Funcoes.java
 *
 * Created on 1 de Agosto de 2005, 17:07
 */

/**
 *
 * @author  jcorrea
 * @version 
 */
package br.gov.go.tj.utils;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import javax.activation.DataHandler;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;
import javax.mail.util.ByteArrayDataSource;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import net.sourceforge.barbecue.Barcode;
import net.sourceforge.barbecue.BarcodeException;
import net.sourceforge.barbecue.BarcodeFactory;
import net.sourceforge.barbecue.BarcodeImageHandler;
import net.sourceforge.barbecue.output.OutputException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import br.gov.go.tj.projudi.dt.ArquivoTipoDt;
import br.gov.go.tj.projudi.dt.Dados;
import br.gov.go.tj.projudi.dt.GuiaEmissaoDt;
import br.gov.go.tj.projudi.dt.GuiaItemDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.PendenciaTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.ne.GuiaEmissaoNe;
import br.gov.go.tj.projudi.ne.PrazoSuspensoNe;
import br.gov.go.tj.projudi.ne.ProcessoNe;
import br.gov.go.tj.projudi.ne.ServentiaNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;


public class Funcoes {
	
	private static String MIME_OCTET_STREAM = "application/octet-stream";
	
	private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("dd/MM/yyyy");
	
	private static final String CHAVE_AES = "pqlTiKBVBdB2zf0E";

	public Funcoes() {
	}

	/**
	 * Calcula a diferenca entre duas datas e formata o resultado
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 19/11/2008 15:27
	 * @param String
	 *            dataInicial, data inicial
	 * @param String
	 *            dataFinal, data final
	 * @return String
	 */
	public static String diferencaDatasFormatado(String dataFinal, String dataInicial) {
		long dif[] = diferencaDatas(dataFinal, dataInicial);
		if (dif == null) return "";
		String stRetorno = "";
		if (dif[0] > 0) stRetorno = dif[0] + " dias ";
		stRetorno += dif[1] + "h" + dif[2] + "m" + dif[3] + "s";
		return stRetorno;
	}

	public static String iniciaisNome(String nome) {
		if (nome == null) return "";
		String[] stNomes = nome.split(" ");
		StringBuffer stRetorno = new StringBuffer("");

		for (int i = 0; i < stNomes.length; i++) {
			//todas com duas posições
			if (stNomes[i].length() > 2) if (!stNomes[i].equalsIgnoreCase("dos") || !stNomes[i].equalsIgnoreCase("das")) {
				stRetorno.append(stNomes[i].substring(0, 1).toUpperCase());
			}
		}
		return stRetorno.toString();
	}

	public static String iniciaisNomeComSeparador(String nome, String separador){
		if (nome == null) return "";
		String[] stNomes = nome.split(" ");
		StringBuffer stRetorno = new StringBuffer("");
		for (int i = 0; i < stNomes.length; i++) {
			if (stNomes[i].length() > 2) if (!stNomes[i].equalsIgnoreCase("dos") || !stNomes[i].equalsIgnoreCase("das")) {
				stRetorno.append(stNomes[i].substring(0, 1).toUpperCase()).append(separador);
			}
		}
		return stRetorno.toString();
	}
	
	/**
	 * Calcula a diferenca de duas datas
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 19/11/2008 15:26
	 * @param String
	 *            dataInicial, data inicial
	 * @param String
	 *            dataFinal, data final
	 * @return int[]
	 */
	public static long[] diferencaDatas(String dataFinal, String dataInicial) {
		if (dataInicial == null || dataInicial.trim().equals("") || dataFinal == null || dataFinal.trim().equals("")) return null;

		return diferencaDatas(DataHora(dataFinal), DataHora(dataInicial));
	}

	/**
	 * Calcula a diferença de duas datas ATENCAO: Nesta funcao assume-se que um
	 * mes possui 30 dias
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 19/11/2008 15:25
	 * @param Date
	 *            dataInicial, data inicial
	 * @param Date
	 *            dataFinal, data final
	 * @return int[]
	 */
	public static long[] diferencaDatas(Date dataFinal, Date dataInicial) {
		long diferenca = dataFinal.getTime() - dataInicial.getTime();

		long dias = diferenca / 86400000;
		if (dias > 0) {
			diferenca -= dias * 86400000;
		}
		int horas = (int) diferenca / 3600000;
		if (horas > 0) {
			diferenca -= horas * 3600000;
		}
		int minutos = (int) diferenca / 60000;
		if (minutos > 0) {
			diferenca -= minutos * 60000;
		}
		int segundos = (int) diferenca / 1000;
		if (segundos > 0) {
			diferenca -= segundos * 1000;
		}
		return new long[] { dias, horas, minutos, segundos };
	}

	public static String formatarTempo(long tempo) {

		long dias = tempo / 86400000;

		if (dias > 0) {
			tempo -= dias * 86400000;
		}
		int horas = (int) tempo / 3600000;
		if (horas > 0) {
			tempo -= horas * 3600000;
		}
		int minutos = (int) tempo / 60000;
		if (minutos > 0) {
			tempo -= minutos * 60000;
		}
		int segundos = (int) tempo / 1000;
		if (segundos > 0) {
			tempo -= segundos * 1000;
		}
		return dias + " dia(s) " + horas + " hora(s) " + minutos + " minuto(s) " + segundos + " segundo(s)";
	}

	/**
	 * Calcula a diferença entre datas e retorna a quantidade de dias
	 * @param dataInicial, dataFinal
	 * @return quantidade de dias
	 * @throws Exception
	 */
	public static int calculaDiferencaEntreDatas(String dataInicial, String dataFinal) throws Exception {	
		try {
		    Date d1 = FORMATTER.parse(dataInicial);
		    Date d2 = FORMATTER.parse(dataFinal);
			return calculaDiferencaEntreDatas(d1, d2);
		} catch (Exception e) {
			throw new MensagemException("Erro ao Calcular a Diferença Entre Datas. Verificar as datas. Data Inicial: " + dataInicial + " - Data Final: " + dataFinal);
		}
	}
	
	private static final long DAY = 24L * 60L * 60L * 1000L;
	
	public static int calculaDiferencaEntreDatas(Date dataInicial, Date dataFinal) throws Exception {
		int dif = (int) ((dataFinal.getTime() - dataInicial.getTime()) / DAY);
		return dif;
	}

	public static String completarZeros(String valor, int qtd) {
		if (valor == null) valor = "0";
		StringBuffer stTemp = new StringBuffer(valor);

		for (int i = 0; i < (qtd - valor.length()); i++)
			stTemp.insert(0, "0");

		return stTemp.toString();
	}
	
	public static String completarZerosDireita(String valor, int qtd) {
		if (valor == null) valor = "0";
		StringBuffer stTemp = new StringBuffer(valor);

		for (int i = 0; i < (qtd - valor.length()); i++)
			stTemp.insert(stTemp.length(), "0");

		return stTemp.toString();
	}
	
	public static String completarEspacosDireita(String valor, int qtd) {
		if (valor == null) valor = "";
		StringBuffer stTemp = new StringBuffer(valor);
		for (int i = 0; i < (qtd - valor.length()); i++){
			stTemp.insert(stTemp.length(), " ");
		}
		return stTemp.toString();
	}
	

	/*
	 * ATENÇÂO -- Se o digito verificador de um processo não estiver correto.
	 *  Provavelmente o cadastro do forum estava incompleto (codigo=0 ou null), durante a distribuição do mesmo. A falta do código fez o digito verificador do processo
	 * ser gerado incorredamente.
	 */
	public static String calcula_mod97(long numeroProcesso, long ano, long JTR, long codigoForum) {

		String valor2 = "";
		String valor3 = "";

		valor2 = (numeroProcesso % 97) + preencheZeros(ano, 4) + preencheZeros(JTR, 3);

		valor3 = (Funcoes.StringToLong(valor2) % 97) + preencheZeros(codigoForum, 4) + "00";

		return preencheZeros(98 - (Funcoes.StringToLong(valor3) % 97), 2);
	}
	
	
	/**
	 * Metodo que recebe uma lista de String e retorna os elementos em sequencia separados por ',' para 
	 * ser utilizado nas consultas no banco de dados, em especial na função in( 1, 2, 3, 4)
	 * @param listaString
	 * @return elementos no formato: elemento1, elemento2, elemento3, elemento4
	 * @author jpcpresa
	 */
	public static String listaToString(List listaString) {
		if (listaString == null || listaString.size() < 1) {
			return "";
		}
		Iterator it = listaString.iterator();
		StringBuffer str = new StringBuffer();
		while (it.hasNext()) {
			String aux = (String) it.next();
			str.append(aux + ", ");
		}
		int tamanho = str.length();
		str.delete(tamanho - 2, tamanho - 1);
		return str.toString();
	}
	
	/**
	 * validação do mod 97
	 * melhoria 
	 * 
	 * @author jesus rodrigo
	 * @since 14/07/2015
	 * @return boolean
	 */
	public static boolean valida_mod97(long NNNNNNN, long DD, long AAAA, long JTR, long OOOO) {		
		long resto1 = 0;
		String valor2 = "";
		long resto2 = 0;
		String valor3 = "";		
		resto1 = NNNNNNN % 97;
		valor2 = preencheZeros(resto1, 2) + preencheZeros(AAAA, 4) + preencheZeros(JTR, 3);
		resto2 = Funcoes.StringToLong(valor2) % 97;
		valor3 = preencheZeros(resto2, 2) + preencheZeros(OOOO, 4) + preencheZeros(DD, 2);
		return ((Funcoes.StringToLong(valor3) % 97) == 1);
	}
	
//	public static boolean valida_mod97(long NNNNNNN, long DD, long AAAA, long JTR, long OOOO) {
//		String valor1 = "";
//		long resto1 = 0;
//		String valor2 = "";
//		long resto2 = 0;
//		String valor3 = "";
//		valor1 = preencheZeros(NNNNNNN, 7);
//		resto1 = Funcoes.StringToLong(valor1) % 97;
//		valor2 = preencheZeros(resto1, 2) + preencheZeros(AAAA, 4) + preencheZeros(JTR, 3);
//		resto2 = Funcoes.StringToLong(valor2) % 97;
//		valor3 = preencheZeros(resto2, 2) + preencheZeros(OOOO, 4) + preencheZeros(DD, 2);
//		return ((Funcoes.StringToLong(valor3) % 97) == 1);
//	}

	public static String preencheZeros(long numero, int quantidade) {
		String temp = String.valueOf(numero);
		String retorno = "";
		if (quantidade < temp.length()) return temp;
		else {
			for (int i = 0; i < (quantidade - temp.length()); i++)
				retorno = "0" + retorno;
			return retorno + temp;
		}
	}
	
	public static String preencheVazioEsquerda(String texto, int tamanho){
		String _texto = (texto != null) ? texto.trim() : "";
		return preencheVazio(tamanho) + _texto;
	}
	
	public static String preencheVazioDireita(String texto, int tamanho){
		String _texto = (texto != null) ? texto.trim() : "";
		return _texto + preencheVazio(tamanho);
	}
	
	public static String preencheVazio(int tamanho){
		return CharBuffer.allocate(tamanho).toString().replace('\0', ' ');
	}
	

	/**
	 * Este tratamento serve tanto para impedir ataques como tambem possibilitar
	 * os usuarios a armazenar alguns caracteres especiais em alguns dados
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 09/09/2008 09:26
	 * @return String
	 */
	public static String tratamento(String valor) {
		// Contra barras
		String contraBarra[] = { "\\", "\"", "'" };

		for (int i = 0; i < contraBarra.length; i++) {
			valor = valor.replace(contraBarra[i], "\\" + contraBarra[i]);
		}

		return valor;
	}

	public static String SenhaMd5(String senha) throws Exception {
		return GeraHashMd5(senha, 64);
	}
	
	public static String GeraHashMd5(String base) throws Exception {
		return GeraHashMd5(base, 0);
	}
	

/**
 * Método que converte uma Lista de Object para array de String
 * @param lista
 * @return arrayString
 */
public static String[] objectListToStringArray(List lista) {
	String[] arrayString = new String[lista.size()];
	if (lista.size() > 0) {
		for (int y = 0; y < lista.size(); y++) {
			arrayString[y] = (String) lista.get(y);
		}	
	}
	return arrayString;
	
}

	public static String GeraHashMd5_32(String base) throws Exception {
		
		byte[] output = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
	
			md.update(base.getBytes());
			output = md.digest();
						
		} catch (NoSuchAlgorithmException ns) {
			throw ns;
		}
		return bytesToHex(output);
	}
	public static String bytesToHex(byte[] b) {
	    char hexDigit[] = {'0', '1', '2', '3', '4', '5', '6', '7',
	                       '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
	    StringBuffer buf = new StringBuffer();
	    for (int j=0; j<b.length; j++) {
	       buf.append(hexDigit[(b[j] >> 4) & 0x0f]);
	       buf.append(hexDigit[b[j] & 0x0f]);
	    }
	    return buf.toString();
	 }
	
	private static String GeraHashMd5(String base, int radix) throws Exception {
		String tempHash;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");

			md.update(base.getBytes());
			BigInteger hash = new BigInteger(1, md.digest());
			if (radix > 0) tempHash = hash.toString(radix);
			else tempHash = hash.toString();
		} catch (NoSuchAlgorithmException ns) {
			throw ns;
		}
		return tempHash;
	}
	
	public static String GeraHashMd5Completa0(String base) throws Exception {
		MessageDigest m = MessageDigest.getInstance("MD5");
		m.update(base.getBytes(), 0, base.length());
		BigInteger i = new BigInteger(1, m.digest());

		return String.format("%1$032x", i);

	}
	
	public static Date StringToDate(String data) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		return sdf.parse(data);
	}

	public static Date StringDDMMAAAAToDate(String data) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
		return sdf.parse(data);
	}
	
	public static Date StringDDMMAAAAHHmmssToDate(String dataHora) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyHHmmss");
		return sdf.parse(dataHora);
	}
	
	public static Date StringAAAAMMDDToDate(String data) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		return sdf.parse(data);
	}
	
	public static Date StringAAAAMMDDHHmmssToDate(String data) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		return sdf.parse(data);
	}

	public static Date StringToDateTime(String data) throws MensagemException {
		SimpleDateFormat sdf = null;
		Date dtRetorno= null;
		try{
			if (data.length()==10)
				sdf = new SimpleDateFormat("dd/MM/yyyy");
			else if (data.length()==16)
				sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			else
				sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		
			dtRetorno=sdf.parse(data);
		}catch(Exception e){
			throw new MensagemException("Erro: Verifique a data: " + data + ",  e tente novamente");
		}		
		 
		return dtRetorno;
	}
	
	public static String convertStringDateUSToStringDatePTBR(String data) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdfDataRetorno = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date dateUs = sdf.parse(data);
		
		return sdfDataRetorno.format(dateUs);
	}
	
	
	public static Date Stringyyyy_MM_ddToDateTime(String data) throws MensagemException {
		SimpleDateFormat sdf = null;
		Date dtRetorno= null;
		try {
			if (data.length() == 8 )
				sdf = new SimpleDateFormat("yyyyMMdd");
			else if (data.length() < 16)
				sdf = new SimpleDateFormat("yyyy-MM-dd");
			else if (data.length()==16)
				sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			else
				sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
			dtRetorno=sdf.parse(data);
		}
		catch(Exception e) {
			throw new MensagemException("Erro: Verifique a data: " + data + ",  e tente novamente");
		}		
		 
		return dtRetorno;
	}

//	public static String BancoString(String valor) throws Exception {
//
//		String stRetorno = valor.toLowerCase();
//
//		String[] stPalavrasChaves = { "SELECT ", "delete ", "UPDATE ", "FROM ", "alter table", "alter column", "alter database", "alter view", "alter procedure", "drop ", "onblur(", "onclick(", "ondblclick(", "onfocus(", "onkeydown(", "onkeypress(", "onkeyup(", "onmousedown(", "onmousemove(", "onmouseout(", "onmouseover(", "onmouseup(", "onload(", "<script" };
//
//		for (int i = 0; i < stPalavrasChaves.length; i++)
//			if (stRetorno.indexOf(stPalavrasChaves[i]) != -1) throw new Exception("<{Tentativa de utilização de palavra não permitida: " + stPalavrasChaves[i] + "}> Local Exception: ): Tentativa de utilização de palavra não permitida: " + stPalavrasChaves[i]);
//
//		valor = tratamento(valor);
//
//		if (valor.length() >= 1) stRetorno = "'" + valor.trim() + "'";
//		else stRetorno = "Null";	
//
//		return stRetorno;
//	}

	public static String BancoStringSimples(String valor) throws Exception {
		String stRetorno = "Null";
		
		valor = tratamento(valor);
		
		if (valor.length() >= 1) stRetorno = "'" + valor.trim() + "'";

		return stRetorno;
	}

	public static String BancoInteiro(String valor) {
		String stRetorno = "Null";
		if (valor.length() >= 1) stRetorno = valor;
		return stRetorno;
	}

	public static String BancoDecimal(String valor) {
		String stRetorno = "Null";
		if (valor.length() >= 1) stRetorno = valor.replace(".", "").replace(",", ".").trim();
		return stRetorno;
	}

	/**
	 * Retorna valor no formato 9.999,99
	 */
	public static String FormatarDecimal(String valor) {
		String stRetorno = "";
		if (valor != null && valor.length() >= 1 && !valor.equals("0.00")) {
			stRetorno = valor.replace(".", ",");
			stRetorno = new DecimalFormat("###,##0.00").format(Funcoes.StringToDouble(valor));
		}
		else {
			if(valor != null && valor.equals("0.00"))
				stRetorno = "0,00";
		}
		return stRetorno;
	}
	
	/**
	 * Retorna valor no formato 9.999,99
	 */
	public static String FormatarDecimal(double valor) {
		String stRetorno = "0,00";
		if (valor > 0) stRetorno = new DecimalFormat("###,##0.00").format(valor);
		return stRetorno;
	}
	
	public static String FormatarBigDecimal(String valor) {
		String stRetorno = "";
		if (valor != null && valor.length() >= 1 && !valor.equals("0.00")) {			
			stRetorno = new DecimalFormat("###,##0.00" ).format(new BigDecimal(valor));			
		}
		else {
			if(valor != null && valor.equals("0.00"))
				stRetorno = "0,00";
		}
		return stRetorno;
	}

	public static String BancoLogico(String valor) {
		String stRetorno = "0";

		if (valor.equalsIgnoreCase("true") || valor.equalsIgnoreCase("t") || valor.equalsIgnoreCase("s")) stRetorno = "1";
		else if (valor.equalsIgnoreCase("false") || valor.equalsIgnoreCase("f")) stRetorno = "0";
		else if (valor.equalsIgnoreCase("1") || valor.equalsIgnoreCase("0")) stRetorno = valor;
		return stRetorno;
	}

	public static boolean BancoLogicoBoolean(String valor) {
		boolean boRetorno = false;

		if (valor.equalsIgnoreCase("true") || valor.equalsIgnoreCase("t") || valor.equalsIgnoreCase("1")) boRetorno = true;
		else if (valor.equalsIgnoreCase("false") || valor.equalsIgnoreCase("f") || valor.equalsIgnoreCase("0")) boRetorno = false;
		return boRetorno;
	}

	public static String FormatarLogico(String valor) {
		String stRetorno = "";
		if ((valor != null) && (valor.equalsIgnoreCase("1"))) stRetorno = "true";
		else stRetorno = "false";
		return stRetorno;
	}

	/**
	 * Método que formata uma data recebida no formato yyyy/MM/dd retornando no formato dd/MM/yyyy.
	 * @param valor - data sem formatação
	 * @return data formatada ou vazio (caso não consiga formatar)
	 * @author hmgodinho(inserindo o try)
	 */
	public static String FormatarData(String valor) {
		String retorno = "";
		// formatos possíveis
		DateTimeFormatter parser = DateTimeFormatter.ofPattern("[yyyy/MM/dd][dd/MM/yyyy][HH:mm:ss yyyy/MM/dd][dd/MM/yyyy HH:mm:ss]", new Locale("pt", "BR"));
		// formato de saída
		DateTimeFormatter formatoSaida = DateTimeFormatter.ofPattern("dd/MM/yyyy");
				
		try{			
			LocalDate data = LocalDate.parse(valor, parser); // obter a data
			retorno = formatoSaida.format(data);
		} catch (Exception e) {
			retorno = "";
		}
		
		return retorno;
	}
	
	/**
	 * Formata uma data para uma nova data no formato específico 
	 * @param data objeto date
	 * @param formato 
	 * @return
	 */
	public static String FormatarData(Date data, String formato) {
		SimpleDateFormat sdf = new SimpleDateFormat(formato);
		return sdf.format(data);		
	}
	
	/**
	 * Método para formatar a data no estilo: 20120131 para o código de barra.
	 * @param String valor
	 * @return String
	 */
	public static String FormatarDataCodigoBarraGuia(String valor) {
		String stRetorno = "";
		valor = Funcoes.TelaData(valor);
		if( valor != null ) {
			if( !valor.equals("") ) {
				stRetorno = valor.substring(6) + valor.substring(3, 5) + valor.substring(0, 2);
			}
		}
		
		return stRetorno;
	}
	
	/**
	 * Método para formatar o número da guia e a série para apresentação na guia.
	 * @param String numeroGuia
	 * @return String numeroGuiaFormatada
	 */
	public static String FormatarNumeroSerieGuia(String numeroGuia) {
		String stRetorno = "";
		if( numeroGuia != null && numeroGuia.length() > 2 ) {
			int digito_Serie = numeroGuia.length() - 3;
			
			String numero = numeroGuia.substring(0, digito_Serie);
			String digito = numeroGuia.substring(digito_Serie).substring(0, 1);
			String serie = numeroGuia.substring(numeroGuia.length() - 2);
			
			stRetorno = numero + "-" + digito + "/" + serie;
		} else {
			stRetorno = numeroGuia;
		}
		return stRetorno;
	}
	
	/**
	 * Retorna somente o numero da guia sem a série
	 * @param String numeroCompletoGuia
	 * @return String numeroGuia
	 */
	public static String ObtenhaSomenteNumeroGuiaSemSerie(String numeroCompletoGuia) {
		String stRetorno = "";
		if( numeroCompletoGuia != null && numeroCompletoGuia.length() > 2 ) {
			int posicaoSerie = numeroCompletoGuia.length() - 2;			
			stRetorno = numeroCompletoGuia.substring(0, posicaoSerie);			
		} else {
			stRetorno = numeroCompletoGuia;
		}
		return stRetorno;
	}
	
	/**
	 * Retorna somente a série da guia
	 * @param String numeroCompletoGuia
	 * @return String serieDaGuia
	 */
	public static String ObtenhaSomenteSerieDaGuia(String numeroCompletoGuia) {
		String stRetorno = "";
		if( numeroCompletoGuia != null && numeroCompletoGuia.length() > 2 ) {
			stRetorno = numeroCompletoGuia.substring(numeroCompletoGuia.length() - 2);			
		} else {
			stRetorno = numeroCompletoGuia;
		}
		return stRetorno;
	}
	
	/**
	 * Método para validar se o número da guia é válido.
	 * Método avalia somente guias emitidas pelo Projudi.
	 * Obs.: O algoritmo de geração do dígito foi copiado do SPG, porém não testei com os números do SPG, somente do Projudi.
	 * @param String numeroGuiaCompleto
	 * @return boolean true se for válido
	 * @throws Exception 
	 */
	public static boolean isNumeroGuiaProjudiValido(String numeroGuiaCompleto) throws Exception {
		boolean numeroValido = isNumeroGuiaSerie50Valido(numeroGuiaCompleto);
		
		if (!numeroValido && numeroGuiaCompleto != null && numeroGuiaCompleto.length() > 0 ) {
			GuiaEmissaoDt guiaEmissaoDt = new GuiaEmissaoNe().consultarNumeroCompleto(numeroGuiaCompleto, null, null);
			numeroValido = (guiaEmissaoDt != null); 
		}
		
		return numeroValido;
	}
	
	/**
	 * Método para validar se o número da guia é válido.
	 * Método avalia somente guias emitidas pelo Projudi.
	 * Obs.: O algoritmo de geração do dígito foi copiado do SPG, porém não testei com os números do SPG, somente do Projudi.
	 * @param String numeroGuiaCompleto
	 * @return boolean true se for válido
	 * @throws Exception 
	 */
	public static boolean isNumeroGuiaSerie50Valido(String numeroGuiaCompleto) throws Exception {
		boolean numeroValido = false;
		
		if( numeroGuiaCompleto != null && numeroGuiaCompleto.length() > 0 ) {
			int digito_Serie = numeroGuiaCompleto.length() - 3;
			
			String numero = numeroGuiaCompleto.substring(0, digito_Serie);
			String digito = numeroGuiaCompleto.substring(digito_Serie).substring(0, 1);
			String serie = numeroGuiaCompleto.substring(numeroGuiaCompleto.length() - 2);
			
			if( digito != null ) {
				GuiaEmissaoNe guiaEmissaoNe = new GuiaEmissaoNe();
				String numeroGuiaCompletoParaConferir = guiaEmissaoNe.getNumeroGuiaCompleto(numero);
				
				if( Funcoes.completarZeros(numeroGuiaCompleto, 11).equals(numeroGuiaCompletoParaConferir) ) {
					numeroValido = true;
				}
			}
		}
		
		return numeroValido;
	}
	
	/**
	 * Método para verificar se o número da guia é extra judicial 02.
	 * 
	 * @param String numeroGuiaCompleto
	 * @return boolean
	 * @throws Exception
	 */
	public static boolean isNumeroGuiaSerie02(String numeroGuiaCompleto) throws Exception {
		boolean isNumeroGuiaSerie02 = false;
		
		if( numeroGuiaCompleto != null && numeroGuiaCompleto.length() > 3 ) {
			
			String serie = numeroGuiaCompleto.substring(numeroGuiaCompleto.length() - 2);
			
			if( serie.equals("02") ) {
				isNumeroGuiaSerie02 = true;
			}
		}
		
		return isNumeroGuiaSerie02;
	}
	
	/**
	 * Método para verificar se o número da guia é final 06.
	 * 
	 * @param String numeroGuiaCompleto
	 * @return boolean
	 * @throws Exception
	 */
	public static boolean isNumeroGuiaSerie06(String numeroGuiaCompleto) throws Exception {
		boolean isNumeroGuiaSerie06 = false;
		
		if( numeroGuiaCompleto != null && numeroGuiaCompleto.length() > 3 ) {
			
			String serie = numeroGuiaCompleto.substring(numeroGuiaCompleto.length() - 2);
			
			if( serie.equals("06") ) {
				isNumeroGuiaSerie06 = true;
			}
		}
		
		return isNumeroGuiaSerie06;
	}
	
	/**
	 * Método para verificar se o número da guia é série 09.
	 * 
	 * @param String numeroGuiaCompleto
	 * @return boolean
	 * @throws Exception
	 */
	public static boolean isNumeroGuiaSerie09(String numeroGuiaCompleto) throws Exception {
		boolean isNumeroGuiaSerie09 = false;
		
		if( numeroGuiaCompleto != null && numeroGuiaCompleto.length() > 3 ) {
			
			String serie = numeroGuiaCompleto.substring(numeroGuiaCompleto.length() - 2);
			
			if( serie.equals("09") ) {
				isNumeroGuiaSerie09 = true;
			}
		}
		
		return isNumeroGuiaSerie09;
	}
	
	/**
	 * Método para formatar a sequência do código de barra para adicionar os dígitos verificadores a cada 11 caracteres.
	 * @param String codigoBarra
	 * @param String digito1
	 * @param String digito2
	 * @param String digito3
	 * @param String digito4
	 * @return String
	 */
	public static String FormatarCodigoBarraGuiaDigito(String codigoBarra, String digito1, String digito2, String digito3, String digito4) {
		String stRetorno = "";
		
		if( codigoBarra != null ) {
			if( !codigoBarra.equals("") ) {
				stRetorno = codigoBarra.substring(0,11) + "-" + digito1 + " " + codigoBarra.substring( 11 , 22) + "-" + digito2 + " " + codigoBarra.substring(22, 33) + "-" + digito3 + " " + codigoBarra.substring(33) + "-" + digito4;
			}
		}
		
		return stRetorno;
	}

	/**
	 * Retorna somente a hora para um Data passada (completa), no formato HH:mm:ss ou HH:mm
	 * @param valor
	 * @return
	 */
	public static String FormatarHora(String valor) {
		String stRetorno = "";
		if (valor != null && !valor.equalsIgnoreCase("")) {
			if (valor.length() == 16) { // Data passada sem os segundos (HH:mm)
				stRetorno = valor.substring(11, 13) + ":" + valor.substring(14, 16);
			} else {
				stRetorno = valor.substring(11, 13) + ":" + valor.substring(14, 16) + ":" + valor.substring(17, 19);
			}
		}
		return stRetorno;
	}

	public static String BancoData(String valor) {
		String stRetorno = "Null";

		String stAno;
		String stMes;
		String stDia;

		if (valor != null) {
			if (valor.length() >= 10) {

				if (valor.substring(2, 3).equalsIgnoreCase("/") && valor.substring(5, 6).equalsIgnoreCase("/")) {
					stAno = valor.substring(6, 10);
					stMes = valor.substring(3, 5);
					stDia = valor.substring(0, 2);
					stRetorno = "'" + stAno + "-" + stMes + "-" + stDia + "'";
				}

			}
		}
		return stRetorno;
	}
	
	//TODO Fred: Ver o nome deste método
	public static String BancoDataLiteral(String valor) {
		String stRetorno = "Null";

		String stAno;
		String stMes;
		String stDia;
		
		if( valor != null ) {
			if( valor.length() == 8 ) {
				stAno = valor.substring(4, 8);
				stMes = valor.substring(2, 4);
				stDia = valor.substring(0, 2);
				stRetorno = "'" + stAno + "-" + stMes + "-" + stDia + "'";
			}
		}
		
		return stRetorno;
	}

	/*
	 * 25/03/1978 dia (0, 1) 2 (1, 2) 5 (2, 3) / mes (3, 4) 0 (4, 5) 3 (5, 6) /
	 * ano (6, 7) 1 (7, 8) 9 (8, 9) 7 (9, 10) 8
	 * 
	 * 1978-03-25 ano (0, 1) 1 (1, 2) 9 (2, 3) 7 (3, 4) 8 (4, 5) - mes (5, 6) 0
	 * (6, 7) 3 (7, 8) - dia (8, 9) 2 (9, 10) 5
	 */

	public static String TelaData(String data) {

		String stRetorno = " ";
		String stAno;
		String stMes;
		String stDia;

		////System.out.println("......Funcoes.TelaData " + data);

		if (data != null) if (data.length() >= 10) {
			data = data.trim();
			////System.out.println(data.substring(4, 5));
			////System.out.println(data.substring(7, 8));
			////System.out.println(data.substring(2, 3));
			////System.out.println(data.substring(5, 6));
			if (data.substring(4, 5).equalsIgnoreCase("-") && data.substring(7, 8).equalsIgnoreCase("-")) {
				////System.out.println(" " + data);
				////System.out.println(" " + data.substring(4, 5));
				////System.out.println(" " + data.substring(7, 8));
				////System.out.println(" " + data.substring(0, 4));
				////System.out.println(" " + data.substring(5, 7));
				////System.out.println(" " + data.substring(8, 10));

				stAno = data.substring(0, 4);
				stMes = data.substring(5, 7);
				stDia = data.substring(8, 10);
				stRetorno = stDia + "/" + stMes + "/" + stAno;
			} else if (data.substring(2, 3).equalsIgnoreCase("/") && data.substring(5, 6).equalsIgnoreCase("/")) {
				////System.out.println(" " + data);
				////System.out.println(" " + data.substring(4, 5));
				////System.out.println(" " + data.substring(7, 8));
				////System.out.println(" " + data.substring(0, 4));
				////System.out.println(" " + data.substring(5, 7));
				////System.out.println(" " + data.substring(8, 10));

				stRetorno = data;
			}
		}
		////System.out.println("......." + stRetorno);
		return stRetorno;
	}

	/**
	 * Recebe string no formato dd/MM/yyyy HH:mm:ss e converte para um Date
	 */
	public static Date DataHora(String valor) {
		Date data = null;

		SimpleDateFormat FormatoData = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		try {
			data = FormatoData.parse(valor);
		} catch (ParseException e) {
			// sempre que houver erro mostre onde ele ocorreu
			////System.out.println("..Funcoes.DataHora() Erro na conversão da data " + valor);
		}

		return data;

	}
	
	/**
	 * Recebe string no formato dd/MM/yyyy HH:mm:ss e converte para um Date
	 */
	public static Date DataHoraMinuto(String valor) {
		Date data = null;

		SimpleDateFormat FormatoData = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		try {
			data = FormatoData.parse(valor);
		} catch (ParseException e) {
			// sempre que houver erro mostre onde ele ocorreu
			////System.out.println("..Funcoes.DataHora() Erro na conversão da data " + valor);
		}

		return data;

	}

	public static String DataHora(long valor) {
		String stRetorno = "";

		SimpleDateFormat FormatoData = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

		Date data = new Date(valor);
		stRetorno = FormatoData.format(data);

		return stRetorno;
	}

	/**
	 * Formata uma data para dia/mes/ano hora:minuto:segundo, todos com 2
	 * digitos exceto o ano com 4
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 24/06/2008 15:07
	 * @param Date data
	 * @return String
	 */
	public static String DataHora(Date data) {
		SimpleDateFormat FormatoData = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

		return FormatoData.format(data);
	}

	/**
	 * Formata uma data para mostrar apenas hora:minuto:segundo, todos com 2
	 * digitos 
	 * @author Alex Rocha
	 * @since 09/03/1010 10:00
	 * @param Date data
	 * @return String
	 */
	public static String Hora(Date data) {
		SimpleDateFormat FormatoData = new SimpleDateFormat("HH:mm:ss");

		return FormatoData.format(data);
	}

/*
 * Funções para converter data para data hora no formado que o bd reconhece.
 * Jesus Rodrigo
 * 30/10/2009
 */
	public static String BancoDataHora1(Date data) {
		String stRetorno = "Null";
		SimpleDateFormat FormatoData = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (data != null) {
			stRetorno = "'" + FormatoData.format(data) + "'";
		}
		return stRetorno;
	}

	/*
	 * Fuções para converter data para  data  no formado que o bd reconhece.
	 * Jesus Rodrigo
	 * 30/10/2009
	 */

	public static String BancoData(Date valor) {
		String stRetorno = "Null";

		if (valor != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			stRetorno = "'" + sdf.format(valor) + "'";
		}
		return stRetorno;
	}

	/*
	 * Fuções para converter data para hora no formado que o bd reconhece.
	 * Jesus Rodrigo
	 * 30/10/2009
	 */
	public static String BancoHora(Date valor) {
		String stRetorno = "Null";
		if (valor != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
			stRetorno = "'" + sdf.format(valor) + "'";
		}
		return stRetorno;
	}
	
	/**
	 * Método para gerar a Data de Vencimento da Guia.
	 * @return String
	 */
	public static String getDataVencimentoGuia() {
		return "31/01/" + (Calendar.getInstance().get(Calendar.YEAR) + 1);
	}
	
	public static String getDataVencimentoGuia15Dias() {
		int prazo = 15;
		
		Calendar hoje = Calendar.getInstance();
		
		hoje.add(Calendar.DATE, prazo);
		
		String dataVencimento = Funcoes.completarZeros(String.valueOf(hoje.get(Calendar.DATE)), 2) + "/";
		dataVencimento += Funcoes.completarZeros(String.valueOf(hoje.get(Calendar.MONTH)+1), 2) + "/";
		dataVencimento += hoje.get(Calendar.YEAR);
		
		return dataVencimento;
	}
	
	public static String getDataVencimentoGuia30Dias() {
		int prazo = 30;
		
		Calendar hoje = Calendar.getInstance();
		
		hoje.add(Calendar.DATE, prazo);
		
		String dataVencimento = Funcoes.completarZeros(String.valueOf(hoje.get(Calendar.DATE)), 2) + "/";
		dataVencimento += Funcoes.completarZeros(String.valueOf(hoje.get(Calendar.MONTH)+1), 2) + "/";
		dataVencimento += hoje.get(Calendar.YEAR);
		
		return dataVencimento;
	}
	
	public static String getDataVencimentoGuiaParcela(int numeroParcela) {
		int prazo = numeroParcela * 30;
		
		Calendar hoje = Calendar.getInstance();
		
		hoje.add(Calendar.DATE, prazo);
		
		String dataVencimento = Funcoes.completarZeros(String.valueOf(hoje.get(Calendar.DATE)), 2) + "/";
		dataVencimento += Funcoes.completarZeros(String.valueOf(hoje.get(Calendar.MONTH)+1), 2) + "/";
		dataVencimento += hoje.get(Calendar.YEAR);
		
		return dataVencimento;
	}
	
	/**
	 * Método para gerar a Data de Vencimento da Guia para o Banco do Brasil no formato ddMMyyyy.
	 * @return String
	 */
	public static String getDataVencimentoGuiaBB() {
		return "3101" + (Calendar.getInstance().get(Calendar.YEAR) + 1);
	}
	
	/**
	 * Recebe string no formato dd/MM/yyyy HH:mm:ss e converte para yyyy-MM-dd
	 * HH:mm:ss ou yyyy-MM-dd HH:mm
	 * 
	 * @author Keila Sousa Silva
	 * @return String
	 */
	public static String BancoDataHora(String valor) {
		String retorno = "Null";
		String ano;
		String mes;
		String dia;
		String hora;
		String minuto;
		String segundo;

		if (valor != null) {
			// Data: dd/mm/yyyy Hora: hh:mm:ss
			if (valor.length() == 19) {
				if (valor.substring(2, 3).equalsIgnoreCase("/") && valor.substring(5, 6).equalsIgnoreCase("/") && (valor.substring(13, 14).equalsIgnoreCase(":")) && (valor.substring(16, 17).equalsIgnoreCase(":"))) {
					ano = valor.substring(6, 10);
					mes = valor.substring(3, 5);
					dia = valor.substring(0, 2);
					hora = valor.substring(11, 13);
					minuto = valor.substring(14, 16);
					segundo = valor.substring(17, 19);

					retorno = "'" + ano + "-" + mes + "-" + dia + " " + hora + ":" + minuto + ":" + segundo + "'";
				}
			}
			// Data: dd/mm/yyyy Hora: hh:mm
			else if (valor.length() == 16) {

				if (valor.substring(2, 3).equalsIgnoreCase("/") && valor.substring(5, 6).equalsIgnoreCase("/") && (valor.substring(13, 14).equalsIgnoreCase(":"))) {
					ano = valor.substring(6, 10);
					mes = valor.substring(3, 5);
					dia = valor.substring(0, 2);
					hora = valor.substring(11, 13);
					minuto = valor.substring(14, 16);

					retorno = "'" + ano + "-" + mes + "-" + dia + " " + hora + ":" + minuto + "'";
				}
			} else if (valor.equals("")) {
				retorno = "Null";
			} else {
				retorno = valor;
			}
		}
		return retorno;
	}

	/**
	 * Recebe string no formato yyyy-MM-dd HH:mm:ss e converte para dd/MM/yyyy
	 * HH:mm:ss
	 * 
	 * @author Keila Sousa Silva, hmgodinho (adicionando o try)
	 * @return String
	 * 
	 */
	public static String FormatarDataHora(String valor) {
		String retorno = "";
		//Adicionando o try para evitar que o método retorne erro. Caso a data esteja num formato diferente, vai retornar a string vazia.
		try{
			if ((valor != null) && (!valor.equalsIgnoreCase(""))) {
				retorno = valor.substring(8, 10) + "/" + valor.substring(5, 7) + "/" + valor.substring(0, 4) + " " + valor.substring(11, 13) + ":" + valor.substring(14, 16) + ":" + valor.substring(17, 19);
			}
		} catch (Exception e) {}
		return retorno;
	}
	
	/**
	 * Recebe uma data do banco e retorna uma string no formato dd/MM/yyyy HH:mm:ss
	 * @param data data
	 * @return string
	 * @author hmgodinho
	 * 11/01/2016
	 */
	public static String FormatarDataHora(Date data) {
		String stRetorno = "";
		SimpleDateFormat FormatoData = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		if (data != null) {
			stRetorno = FormatoData.format(data);
		}
		return stRetorno;
	}

	/**
	 * Recebe uma data do banco e retorna uma string no formato dd/MM/yyyy HH:mm:ss
	 * @param data data
	 * @return string
	 * @author jrcorrea
	 * 11/11/2016
	 */
	public static String FormatarData(Date data) {
		String stRetorno = "";
		SimpleDateFormat FormatoData = new SimpleDateFormat("dd/MM/yyyy");
		if (data != null) {
			stRetorno = FormatoData.format(data);
		}
		return stRetorno;
	}
	
	/**
	 * Método recebe uma data e retorna a hora formatada no formato HH:mm:ss
	 * @param data data
	 * @return string
	 * @author hmgodinho
	 */
	public static String FormatarHora(Date data) {
		String stRetorno = "";
		SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm:ss");
		if (data != null) {
			stRetorno = formatoHora.format(data);
		}
		return stRetorno;
	}

	/**
	 * Recebe string no formato yyyy-MM-dd HH:mm:ss e converte para dd/MM/yyyy HH:mm, desconsiderando os segundos
	 * 
	 * @author msapaula
	 * @return String
	 */
	public static String FormatarDataHoraMinuto(String valor) {
		String retorno = "";
		if ((valor != null) && (!valor.equalsIgnoreCase(""))) {
			retorno = valor.substring(8, 10) + "/" + valor.substring(5, 7) + "/" + valor.substring(0, 4) + " " + valor.substring(11, 13) + ":" + valor.substring(14, 16);
		}
		return retorno;
	}

	/**
	 * Método que valida o valor do pis
	 * 
	 * @param pis
	 *            É o valor que se deseja checar
	 * @return true se o valor é válido, false caso contrário.
	 */
	public static boolean validaPIS(String pis) throws NumberFormatException {
		Funcoes.StringToLong(pis);
		if (pis.length() != 11) return false;
		int wdv = Funcoes.StringToInt(pis.substring(pis.length() - 1, pis.length()));
		int wsoma = 0;
		int wm11 = 2;
		for (int i = 0; i < 10; i++) {
			wsoma += wm11 * Funcoes.StringToInt(pis.substring(9 - i, 10 - i));
			if (wm11 < 9) wm11++;
			else wm11 = 2;
		}
		int wdigito = 11 - (wsoma % 11);
		if (wdigito > 9) wdigito = 0;
		if (wdv == wdigito) return true;
		return false;
	}

	/**
	 * Método que faz a validação de um título de eleitor
	 * 
	 * @param titulo
	 *            é o valor do título
	 * @return True se for uma tírulo de eleitor válido, false caso contrário
	 */
	public static boolean validaTituloEleitor(String titulo) throws NumberFormatException {
		Funcoes.StringToLong(titulo);
		if (titulo.length() < 12) return validaTituloEleitor("0" + titulo);
		if (titulo.length() > 12) return false;

		int estado = Funcoes.StringToInt(titulo.substring(8, 10));
		if (estado < 1 || estado > 28) return false;

		int soma = 0;
		for (int i = 0; i < 8; i++)
			soma += Funcoes.StringToInt(titulo.substring(i, i + 1)) * (9 - i);
		int resto = soma % 11;
		int digito1;
		if (resto == 0) if (estado == 1 || estado == 2) digito1 = 1;
		else digito1 = 0;
		else if (resto == 1) digito1 = 0;
		else digito1 = 11 - resto;

		if (digito1 != Funcoes.StringToInt(titulo.substring(10, 11))) return false;

		soma = 0;
		for (int i = 8; i < 10; i++)
			soma += Funcoes.StringToInt(titulo.substring(i, i + 1)) * (12 - i);
		soma += digito1 * 2;
		resto = soma % 11;
		int digito2;
		if (resto == 0) if (estado == 1 || estado == 2) digito2 = 1;
		else digito2 = 0;
		else if (resto == 1) digito2 = 0;
		else digito2 = 11 - resto;

		if (digito2 != Funcoes.StringToInt(titulo.substring(11, 12))) return false;

		return true;
	}

	
	
	/**
	 * Formata um CPF e retorna no formato XXX.XXX.XXX-XX
	 */
	public static String formataCPF(String cpf) {
		if (cpf != null && cpf.trim().length() > 0) {
			cpf = cpf.trim();
			if (cpf.length() > 11) {
				long cpfTemp = Funcoes.StringToLong(cpf);
				cpf = String.valueOf(cpfTemp);
			}			
			cpf = completarZeros(cpf, 11);
			if (cpf.length() == 11) { 
				return cpf.substring(0, 3) + "." + cpf.substring(3, 6) + "." + cpf.substring(6, 9) + "-" + cpf.substring(9, 11); 
			}
			//throw new RuntimeException("<{CPF inválido.}> Local Exception: Funcoes.formataCPF()");
		}
		return cpf;
	}

	/**
	 * Formata um CNPJ e retorna no formato XX.XXX.XXX/XXXX-XX
	 */
	public static String formataCNPJ(String cnpj) {
		if (cnpj != null && cnpj.length() > 0) {
			cnpj = completarZeros(cnpj,14);
			if (cnpj.length() == 14) { 
				return cnpj.substring(0, 2) + "." + cnpj.substring(2, 5) + "." + cnpj.substring(5, 8) + "/" + cnpj.substring(8, 12) + "-" + cnpj.substring(12, 14); 
			}			
		}
		return cnpj;
	}
	
	/**
	 * Formata um CPF acresencentado zeros à esquerda se necessário
	 */
	public static String completaCPFZeros(String cpf) {
		if (cpf != null && cpf.length() < 11) cpf = new DecimalFormat("00000000000").format(Double.parseDouble(cpf));
		return cpf;
	}

	/**
	 * Formata um CNPJ acresencentado zeros à esquerda se necessário
	 */
	public static String completaCNPJZeros(String cnpj) {
		if (cnpj != null && cnpj.length() < 14) cnpj = new DecimalFormat("00000000000000").format(Funcoes.StringToDouble(cnpj));
		return cnpj;
	}

	/**
	 * Método que testa a integridade de um cpf / cnpj <<<<<<< .mine
	 * 
	 * @param cpfCnpj:
	 *            É o cpf ou cnpj que se deseja testar, contendo apenas
	 *            algarismos =======
	 * @param cpf:
	 *            É o cpf que se deseja testar, contendo apenas algarismos
	 *            >>>>>>> .r179
	 * @return true se for um cpf válido, false caso contrário
	 */
	public static boolean testaCPFCNPJ(String cpfCnpj) {
		if (cpfCnpj != null && cpfCnpj.length() > 11) {
			
			cpfCnpj = completaCNPJZeros(cpfCnpj);
			
			if (cpfCnpj.equalsIgnoreCase("00000000000000")
					|| cpfCnpj.equalsIgnoreCase("11111111111111")
					|| cpfCnpj.equalsIgnoreCase("22222222222222")
					|| cpfCnpj.equalsIgnoreCase("33333333333333")
					|| cpfCnpj.equalsIgnoreCase("44444444444444")
					|| cpfCnpj.equalsIgnoreCase("55555555555555")
					|| cpfCnpj.equalsIgnoreCase("66666666666666")
					|| cpfCnpj.equalsIgnoreCase("77777777777777")
					|| cpfCnpj.equalsIgnoreCase("88888888888888")
					|| cpfCnpj.equalsIgnoreCase("99999999999999"))
				return false;
			
			char[] digitos = cpfCnpj.toCharArray();
			int pesos[] = { 6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2 };
			int soma = 0;
			for (int i = 0; i < 12; i++)
				soma += pesos[i + 1] * CharToInt( digitos[i],0);
			soma = 11 - (soma - ((soma / 11) * 11));
			if ((soma == 10) || (soma == 11)) soma = 0;
			if (soma != CharToInt( digitos[12],0)) return false;

			soma = 0;
			for (int i = 0; i < 13; i++)
				soma += pesos[i] * CharToInt( digitos[i],0);
			soma = 11 - (soma - ((soma / 11) * 11));
			if ((soma == 10) || (soma == 11)) soma = 0;
			if (soma != CharToInt( digitos[13],0)) return false;
		} else {
			
			cpfCnpj = completaCPFZeros(cpfCnpj);
			
			if (cpfCnpj.equalsIgnoreCase("00000000000")
					|| cpfCnpj.equalsIgnoreCase("11111111111")
					|| cpfCnpj.equalsIgnoreCase("22222222222")
					|| cpfCnpj.equalsIgnoreCase("33333333333")
					|| cpfCnpj.equalsIgnoreCase("44444444444")
					|| cpfCnpj.equalsIgnoreCase("55555555555")
					|| cpfCnpj.equalsIgnoreCase("66666666666")
					|| cpfCnpj.equalsIgnoreCase("77777777777")
					|| cpfCnpj.equalsIgnoreCase("88888888888")
					|| cpfCnpj.equalsIgnoreCase("99999999999"))
				return false;

			char[] digitos = cpfCnpj.toCharArray();

			int soma = 0;
			for (int i = 0; i < 9; i++)
				soma += (10 - i) * CharToInt(digitos[i],0);
			soma = 11 - (soma - ((soma / 11) * 11));
			if ((soma == 10) || (soma == 11)) soma = 0;
			if (soma != CharToInt(digitos[9],0)) return false;

			soma = 0;
			for (int i = 0; i < 10; i++)
				soma += (11 - i) * CharToInt( digitos[i],0);
			soma = 11 - (soma - ((soma / 11) * 11));
			if ((soma == 10) || (soma == 11)) soma = 0;
			if (soma != CharToInt( digitos[10],0)) return false;
		}		
		
		return true;
	}

	/**
	 * Valida se determinado valor é numérico
	 */
	public static boolean validaNumerico(String str) {
		try {
			Funcoes.StringToLong(str);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	/**
	 * Valida se determinado valor é double
	 */
	public static boolean validaDouble(String str) {
		try {
			Funcoes.StringToDouble(str);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	/**
	 * Valida se uma data está no formato correto dd/mm/yyyy
	 */
	public static boolean validaData(String data) {
		try {
			SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy");			
			formatador.parse(data);	
			
			return true;
		} catch (ParseException e) {
			return false;
		}
	}
	
	/**
	 * Valida se uma data está no formato correto yyyyMMdd
	 */
	public static boolean validaDataYYYYMMDD(String data) {
		try {
			SimpleDateFormat formatador = new SimpleDateFormat("yyyyMMdd");			
			formatador.parse(data);	
			
			return true;
		} catch (ParseException e) {
			return false;
		}
	}
	
	/**
	 * Valida se uma data está no formato correto yyyyMMddhhmmss
	 */
	public static boolean validaDataYYYYMMDDHHMMSS(String dataHora) {
		try {
			SimpleDateFormat formatador = new SimpleDateFormat("yyyyMMddhhmmss");			
			formatador.parse(dataHora);	
			
			return true;
		} catch (ParseException e) {
			return false;
		}
	}
	
	/**
	 * Valida se uma data está no formato correto yyyy-MM-dd HH:mm:ss
	 */
	public static boolean validaData_yyyy_MM_ddHHmmss(String dataHora) {
		try {
			SimpleDateFormat formatador = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");			
			formatador.parse(dataHora);	
			
			return true;
		} catch (ParseException e) {
			return false;
		}
	}
	
	/**
	 * Valida se a data informada está no formato dd/mm/yyyy hh:mm:ss
	 * @param data
	 * @return true se for data no formato correto
	 * @author hmgodinho
	 */
	public static boolean validaDataHora(String data) {
		try {
			SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");			
			formatador.parse(data);	
			
			return true;
		} catch (ParseException e) {
			return false;
		}
	}

	/**
	 * Método responsável em gravar arquivo no banco
	 */
	public static void gravaArquivo(byte[] conteudo, File diretorio) throws Exception {
		new Compactar().criarZip(conteudo, diretorio);
	}

	public static String FormatarMoeda(String valor) {
		String stRetorno = "";
		if (valor.length() >= 1) stRetorno = NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format(Funcoes.StringToDouble(valor));
		return stRetorno;
	}

	/**
	 * Retorna o número do processo formatado Exemplo: 001.2005.900.001-0
	 */
	public static String formataNumeroProcesso(String numeroProcesso) {
//        if (numeroProcesso != null && numeroProcesso.length() > 0) {
//            if (numeroProcesso.length() == 12)
//                numeroProcesso = "00" + numeroProcesso;
//            if (numeroProcesso.length() == 13)
//                numeroProcesso = "0" + numeroProcesso;
//            numeroProcesso = numeroProcesso.substring(0, 3) + "." + numeroProcesso.substring(3, 7) + "." + numeroProcesso.substring(7, 10) + "." + numeroProcesso.substring(10, 13) + "-" + numeroProcesso.substring(13, 14);
//        }
		return numeroProcesso;
	}
	
	public static String formataNumeroCompletoProcesso(String numeroCompletoProcesso) {
		if (numeroCompletoProcesso == null || numeroCompletoProcesso.trim().length() == 0) return "";
		
		numeroCompletoProcesso = completarZeros(obtenhaSomenteNumeros(numeroCompletoProcesso), 20).trim();
		
		long numero = Funcoes.StringToLong(numeroCompletoProcesso.substring(0, 7) ); 
		long digito  = Funcoes.StringToLong(numeroCompletoProcesso.substring(7, 9));
		long ano = Funcoes.StringToLong(numeroCompletoProcesso.substring(9, 13)); 
		long jtrParte1 = Funcoes.StringToLong((numeroCompletoProcesso.substring(13, 14))); 
		long jtrParte2 = Funcoes.StringToLong((numeroCompletoProcesso.substring(14, 16)));
		long forum = Funcoes.StringToLong(numeroCompletoProcesso.substring(16, 20));
		
		return String.format("%07d.%02d.%04d.%s.%02d.%04d", numero, digito, ano, jtrParte1, jtrParte2, forum); //"5000280.28.2010.8.09.0059";		
	}
	
	public static String formataNumeroProcessoDigitoAno(String numeroCompletoProcesso) {
		if (numeroCompletoProcesso == null || numeroCompletoProcesso.trim().length() == 0) return "";
		
		numeroCompletoProcesso = completarZeros(obtenhaSomenteNumeros(numeroCompletoProcesso), 20).trim();
		
		long numero = Funcoes.StringToLong(numeroCompletoProcesso.substring(0, 7) ); 
		long digito  = Funcoes.StringToLong(numeroCompletoProcesso.substring(7, 9));
		long ano = Funcoes.StringToLong(numeroCompletoProcesso.substring(9, 13));
		
		return String.format("%07d.%02d.%04d", numero, digito, ano); //"5000280.28.2010";		
	}
	
	public static String formataNumeroDigitoProcesso(String numeroCompletoProcesso) {
		if (numeroCompletoProcesso == null || numeroCompletoProcesso.trim().length() == 0) return "";
		
		numeroCompletoProcesso = completarZeros(obtenhaSomenteNumeros(numeroCompletoProcesso), 20).trim();
		
		long numero = Funcoes.StringToLong(numeroCompletoProcesso.substring(0, 7) ); 
		long digito  = Funcoes.StringToLong(numeroCompletoProcesso.substring(7, 9));		
		
		return String.format("%07d.%02d", numero, digito); //"5000280.28"		
	}
	
	public static String[] separaNumeroProcessoDigitoAno(String numeroProcesso) {
		String[] retorno = null;
		
		if( numeroProcesso != null && !numeroProcesso.isEmpty() ) {
			if( numeroProcesso.length() > 6 ) {
				
				retorno = new String[3];
				
				retorno[0] = numeroProcesso.substring(0, numeroProcesso.length() - 6);
				retorno[1] = numeroProcesso.substring(numeroProcesso.length() - 6, numeroProcesso.length() - 4);
				retorno[2] = numeroProcesso.substring(numeroProcesso.length() - 4, numeroProcesso.length());
				
			}
		}
		
		return retorno;
	}
	
	public static String obtenhaSomenteNumeros(String valor) {
		if (valor == null || valor.trim().length() == 0) return "";
		
		String retorno = "";
		
		final String caracteresValidos = "0123456789";
		
		for(int i = 0; i < valor.length(); i++) {
			if (caracteresValidos.contains(valor.substring(i, i + 1))) retorno += valor.substring(i, i + 1);
		}
		
		return retorno;
	}

	/**
	 * Desformata um numero de processo
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 08/05/2008 - 14:58
	 * @param String
	 *            numeroProcesso, numero do processo
	 * @return String
	 */
	public static String desformataNumeroProcesso(String numeroProcesso) {
		return numeroProcesso.replace(".", "").replace("-", "");
	}
	
	/**
	 * Método para retirar o ponto e a vírgula do valor. Método utilizado para gerar código de barra e passar 
	 * como parâmetro para bancos.
	 * @param valor
	 * @return String
	 */
	public static String retiraVirgulaPonto(String valor) {
		return valor.trim().replaceAll("[,.]", "");
	}
	
//	/**
//	 * @author jpcpresa
//	 * Gera uma expressão regular para pesquisa fonética no banco.
//	 * @param String nome
//	 * @return String expressão regular
//	 */
//	
//	public static String getNomePesquisa(String nome) {
//		//TODO colocar números 1 [uU][mM] trar PH com F
//		
//		String nomeRetorno = "";
//		nome = nome.trim();
//		Pattern p = Pattern.compile("( [dD][aAoOeEiIuU]s? )+| [jJ][Rr] ?| [Ss][/.][Aa] ?| [lL][tT][dD][aA] ?| [cC][iI][aA]");
//		Matcher m = p.matcher(nome);
//		while (m.find()) {
//			nome = m.replaceAll(" ");
//			m = p.matcher(nome);
//		}
//		String letraAux = " ";
//		char[] nomeArray = nome.toCharArray();
//		int i = 0;
//		
//		while (i < nome.length()) {
//			String aux = new String(String.valueOf(nomeArray[i]));
//			
//			
//			
//			//trata mesmo fonema com 2 letras diferentes
//			if(aux.equals(letraAux)) {
//				i = i + 1;
//				continue;
//			} else if (letraAux.matches("[sS]") && aux.matches("[cC]")) {
//				i = i + 1;
//				continue;
//			} else if (letraAux.matches("[cC]") && aux.matches("[kK]")) {
//				i = i + 1;
//				continue;
//			} else if(letraAux.matches("[qQ]") && aux.matches("[uUúÚùÙûÛüÜ]")) {
//				i = i + 1;
//				continue;
//			} 
//			
//			
//			// A
//			if (aux.matches("[aAáÁàÀãÃâÂäÄ]")) {
//				
//				nomeRetorno+="[aAáÁàÀãÃâÂäÄ][hH]?";
//				
//				// B, P
//			} else if (aux.matches("[bBpP]")) {
//				
//				if(i < nome.length() - 2) {
//					if (String.valueOf(nomeArray[i + 1]).matches("[hH]")&& aux.matches("[pP]")) {
//						
//							i = i + 2;
//							nomeRetorno += "[fFpP][hH]?";
//							continue;
//					}
//					
//				}
//				nomeRetorno +="[bBpP]{1,2}[hH]?";
//				
//				// C, K, Q
//			} else if (aux.matches("[cCkKqQ]")) {
//				
//				//trata CH
//				if(i < nome.length() - 2) {
//					if (String.valueOf(nomeArray[i + 1]).matches("[hH]") && aux.matches("[cC]")) {
//							
//							nomeRetorno += "[XxsScC][hH]?";
//							i = i + 2;
//							continue;					
//					}
//				}
//				
//				nomeRetorno +="([cC][kK]|[qQ][uU]|[cCkKqQ]{1,2}|[sS][cC]|[sS][Ss])[hH]?";
//					
//				// D, T
//			} else if (aux.matches("[tTdD]")) {
//				
//				nomeRetorno +="[tTdD]{1,2}[hH]?";
//				// E, I, Y
//			} else if (aux.matches("[iIíÍìÌîÎïÏeEéÉèÈêÊëËyY]")) {
//				
//				nomeRetorno +="[iIíÍìÌîÎïÏeEéÉèÈêÊëËyY]{1,2}[hH]?";
//				// F, V, W
//			} else if (aux.matches("[fFvVwW]")) {
//				
//				nomeRetorno +="[fFvVwWuU]{1,2}[hH]?";
//				// G, J
//			} else if (aux.matches("[gGjJ]")) {
//				
//				nomeRetorno +="([gGjJ]{1,2}|[Gg][iI])[hH]?";
//				// H
//			} else if (aux.matches("[Hh]")) {
//				//trata SH , CH e PH
//									
//					nomeRetorno +="([Hh]|[rR])?";
//				
//					
//				// L
//			} else if (aux.matches("[lL]")) {
//				
//				nomeRetorno +="[lL]{1,2}[hH]?";
//				// M, N
//			} else if (aux.matches("[mMnN]")) {
//				
//				nomeRetorno +="[mMnN]{1,2}[hH]?";
//				// O
//			} else if (aux.matches("[oOóÓòÒõÕôÔöÖ]")) {
//			
//				nomeRetorno +="[uUúÚùÙûÛüÜoOóÓòÒõÕôÔöÖ]{1,2}[hH]?";
//				// R
//			} else if (aux.matches("[Rr]")) {
//				if(letraAux.matches("[ ]") || i == 0) {
//					nomeRetorno +="[Rr]{0,2}[hH]?";
//				} else {
//					nomeRetorno +="[Rr]{1,2}[hH]?";
//				}
//				// S
//			} else if (aux.matches("[sS]")) {
//				
//				//trata SH
//				if(i < nome.length() - 2) {
//					if (String.valueOf(nomeArray[i + 1]).matches("[hH]") && aux.matches("[sS]")) {
//							nomeRetorno += "[XxsScC][hH]?";
//							i = i + 2;
//							continue;					
//					}
//				}
//				
//				nomeRetorno +="([sSzZcç]{1,2}|[sS][cC])[hH]?";
//				// U
//			} else if (aux.matches("[uUúÚùÙûÛüÜ]")) {
//				
//				if(i > 0 || !letraAux.matches("[ ]")) {
//					nomeRetorno +="[uUúÚùÙûÛüÜoOóÓòÒõÕôÔöÖlL]{1,2}";
//				} else {
//					nomeRetorno +="[wWuUúÚùÙûÛüÜ]{1,2}[hH]?";
//				}
//				// X
//			} else if (aux.matches("[Xx]")) {
//			
//				nomeRetorno +="[XxsScC][hH]?";
//				// Z
//			} else if (aux.matches("[Zz]{1,2}")) {
//				
//				nomeRetorno +="[sSzZ]{1,2}[hH]?";
//			} else {
//				
//				nomeRetorno+=aux+".*";
//			}
//			
//			letraAux = aux;
//			i = i + 1;
//		}
//		return nomeRetorno;
//	}
	
	
//	/**
//	 * @author jpcpresa
//	 * Compara duas Strings ignorando acentos e caixa.
//	 * @param String
//	 * 			 string1, string2
//	 * @return boolean 
//	 * 			true - se são iguais ignorando caixa e acentos. 
//	 *	 		false - se são diferentes ignorando caixa e acentos.
//	 */
//	
//	public static boolean equalsIgnoraAcentoCaixa(String st1, String st2) {
//		Collator collator = Collator.getInstance(new Locale("pt", "BR"));
//		collator.setStrength(Collator.PRIMARY); 
//		return collator.compare(st1, st2) == 0;
//	}
	
	 /** 
	 * @author jpcpresa
	 * Retorna  os nomes sem acento e ç
	 * @param String nome para ser limpo. 
	 * @return retorna a string alterada. 
	 * 22/09/2016 Jrcorrea
	 */  
	
	public static String retirarAcentos(String nome)     {  
//	     nome = Normalizer.normalize(nome, Normalizer.Form.NFD);  
//	     nome = nome.replaceAll("[^\\p{ASCII}]", "");  
	     nome = nome.replaceAll("[áàãâä]", "a");
	     nome = nome.replaceAll("[ÁÀÃÂÄ]", "A");
	     nome = nome.replaceAll("[éèêë]", "e");
	     nome = nome.replaceAll("[ÉÈÊË]", "E");
	     nome = nome.replaceAll("[íìîï]", "i");
	     nome = nome.replaceAll("[ÍÌÎÏ]", "I");
	     nome = nome.replaceAll("[óòõôö]", "o");
	     nome = nome.replaceAll("[ÓÒÕÔÖ]", "O");
	     nome = nome.replaceAll("[úùûü]", "u");
	     nome = nome.replaceAll("[ÚÙÛÜ]", "U");
	     nome = nome.replaceAll("ç", "c");
	     nome = nome.replaceAll("Ç", "C");
	     nome = nome.replaceAll("º", ".");
	     nome = nome.replaceAll("ª", ".");
	     return nome;  
	 }
	
	/**
	 * @author jpcpresa
	 * Remove o excesso de espaços em branco em um nome, 
	 * deixando apenas 1 espaço em branco entre cada nome 
	 * e nenhum antes ou depois do nome.
	 * @param String nome que terá os espaços em branco excessivos removidos. 
	 * @return String nome sem espaços em brancos em excesso.
	 * @author jrcorrea 28/05/2015
	 */
	
	public static String removeEspacosExcesso(String nome) {
		if(nome == null || nome.isEmpty()) {
			return nome;
		}
		String string = nome.trim();  
	    while (string.contains("  ")) {  
	        string = string.replaceAll("  ", " ");  
	    }  	    
	    return string;
	}
	
	public static String removeTodoEspacos(String nome) {
		if(nome == null || nome.isEmpty()) {
			return nome;
		}
		String string = nome.trim();  
	    while (string.contains(" ")) {  
	        string = string.replaceAll(" ", "");  
	    }  	    
	    return string;
	}
	
	/**
	 * Retira código html que pode aparecer nos textos do cnj e outros caracteres aspas simples e duplas
	 * @param texto
	 * @return
	 */
	public static String removerAspasSimplesDuplasETagsHtml(String texto){
		String s = StringEscapeUtils.unescapeHtml(texto);
		s = s.replaceAll("(?m)^\\s+$","");		
		s = s.replaceAll("\"|\'|", "");		
		return s.replaceAll("\\<[^>]*>","");
	}
	
	public static String getTesauro(String valor, String curinga) {
		String stRetorno = "";
		valor = limparNomeRetiarSufixosOutros(valor);
		String[] stDivisao = valor.split(" ");

		for (int i = 0; i < stDivisao.length; i++)
			stRetorno += LimparNome(stDivisao[i], curinga);

		return stRetorno.replace(curinga + curinga, curinga);
	}

	public static String LimparNome(String valor, String curinga) {
		String stRetorno = "";

		stRetorno = retirarVogais(valor, curinga);
		// stRetorno = RetirarLetras(stRetorno, "%");
		return stRetorno.replace(curinga + curinga, curinga);
	}
	
	public static String converteNomeSimplificado(String nome){
		if (nome == null) nome = "";
		
	    String stNome =  " " + nome.toLowerCase() + " ";
	    
	    stNome = retirarPreposicao(stNome);

	    stNome = limparNomeRetiarSufixosOutros(stNome);
	    
	    stNome = trocarLetras(stNome);
	    	    
	    //stNome = retirarVogais(stNome,"");
	    
	    stNome = stNome.replace(" ", "");
	    //pego somente os 30 primeiro caracteres
	    if (stNome.length()>30)  stNome= stNome.substring(0,30);
	    
	    return stNome;
	}
	
//	public static String[] getNomeSimplificadoPesquisa(String nome) {
//		nome = limparNome(nome);
//		nome = removeEspacosExcesso(nome);
//		nome = limparNomeRetiarSufixosOutros(nome);
//		String [] nomeArray = null;
//		String[] nomeArrayAux = nome.split(" ");
//		
//		if(nomeArrayAux.length < 2) {
//			nomeArray = new String[1];
//			nomeArray[0] = converteNomeSimplificado(nomeArrayAux[0]);
//			
//		} else if (nomeArrayAux.length <= 2) {
//			nomeArray = new String[2];
//			nomeArray[0] = converteNomeSimplificado(nomeArrayAux[0] + nomeArrayAux[1]);
//			nomeArray[1] = converteNomeSimplificado(nomeArrayAux[1] + nomeArrayAux[0]);
//			
//		} else if (nomeArrayAux.length <= 3) {
//			nomeArray = new String[3];
//			nomeArray[0] = converteNomeSimplificado(nomeArrayAux[0] + nomeArrayAux[1] + nomeArrayAux[2]);
//			nomeArray[1] = converteNomeSimplificado(nomeArrayAux[1] + nomeArrayAux[0] + nomeArrayAux[2]);
//			nomeArray[2] = converteNomeSimplificado(nomeArrayAux[0] + nomeArrayAux[1]);
//			
//		} else if (nomeArrayAux.length > 3) {
//			nomeArray = new String[5];
//			nomeArray[0] = converteNomeSimplificado(nomeArrayAux[0] + nomeArrayAux[1] + nomeArrayAux[2] + nomeArrayAux[3]);
//			if (nomeArrayAux.length > 4) {
//				for (int i = 4; i < nomeArrayAux.length; i++) {
//					nomeArray[0] = nomeArray[0] + nomeArrayAux[i];
//				}
//			}
//			
//			nomeArray[1] = converteNomeSimplificado(nomeArrayAux[1] + nomeArrayAux[0] + nomeArrayAux[2] + nomeArrayAux[3]);
//			nomeArray[2] = converteNomeSimplificado(nomeArrayAux[0] + nomeArrayAux[2]+ nomeArrayAux[3]);
//			nomeArray[3] = converteNomeSimplificado(nomeArrayAux[0] + nomeArrayAux[1]+ nomeArrayAux[2]);
//			nomeArray[4] = converteNomeSimplificado(nomeArrayAux[0] + nomeArrayAux[3] + nomeArrayAux[4]);
//			
//		}
//		
//		
//		return nomeArray;
//	}
	
	public static String limparNomeRetiarSufixosOutros(String nome){
	    String stNome = nome.toLowerCase();
	    
	    String[] stTrocaA = {",",")","&","ª","º","(","_","\\","/",".","-","ltda"," sc "," ss "," sa "," me ","'"};
        String[] stTrocaB = { "", "", "", "", "", "", "",  "", "", "", "",    "",   "",   "",   "",  "", ""};
        
	    //troco palavras
        for (int i=0; i<stTrocaA.length;i++)
            stNome = stNome.replace(stTrocaA[i], stTrocaB[i]);
        
        stNome = stNome.replaceAll("[1234567890]", "");
        
	    return stNome;
	}
	
    public static String retirarPreposicao(String nome){
        String stNome = null;
        stNome = nome.replace(" em ", " ").replace(" do ", " ").replace(" dos ", " ").replace(" da ", " ").replace(" das ", " ").replace(" de ", " ").replace(" e ", " ");
        return stNome;
    }

    public static String trocarLetras(String nome){
        String stNome =  nome ;               
        String[] stConsoantes = {"b","c","d","f","g","h","j","k","l","m","n","p","q","r","s","t","v","x","z","y","w"};
        					//retiro letras no final        letras no inicio	
        String[] stTrocaA = {"s ","z ","n ","m ","l ","r "  ," w"," u"             ,"sh","ch","ck","ngt","ph","ct","t","ç","z","c","k","q","b","v","g","h"};
        String[] stTrocaB = { " ", " ", " ", " ", " ", " "  ," f"," f"             , "x", "x", "q",  "d", "f", "d","d","s","s","s","s","s","p","f","j", ""};

        //retiro todas as consoantes dobradas
        for (int i=0; i<stConsoantes.length;i++)
            stNome = stNome.replace(stConsoantes[i] +stConsoantes[i], stConsoantes[i]);
        
        //troco letras
        for (int i=0; i<stTrocaA.length;i++)
            stNome = stNome.replace(stTrocaA[i], stTrocaB[i]);

        stNome = stNome.replaceAll("[eéèêëiíìîïyýÿ]","e");
        
        stNome = stNome.replaceAll("[aáàãâä]","a");
        
        stNome = stNome.replaceAll("[oóòõôöuúùûüw]","o");
        
        return stNome;
    }
    
	public static String retirarVogais(String valor, String curringa) {
		return valor.replaceAll("[aAáÁàÀãÃâÂäÄeEéÉèÈêÊëËiIíÍìÌîÎïÏoOóÓòÒõÕôÔöÖuUúÚùÙûÛüÜ]", curringa) + curringa;
	}
	
	public static String retirarVogalConsoante(String valor, String curringa) {
		return valor.replaceAll("[aAáÁàÀãÃâÂäÄeEéÉèÈêÊëËiIíÍìÌîÎïÏoOóÓòÒõÕôÔöÖuUúÚùÙûÛüÜbcdfghjklmnopqrstvwxyzBCDFGHJKLMNPQRSTVWXYZªº° ]", curringa) + curringa;
	}
	
	public static String RetirarAssinantes(String assinantes) {

		Pattern paTeste01 = Pattern.compile(",CN=(.*?),L=");
		Matcher maTeste01 = paTeste01.matcher(assinantes);
		StringBuffer assinador = new StringBuffer("");
		if (maTeste01.find()) assinador.append(maTeste01.group().replace(",CN=", "").replace(",L=", ""));
		while (maTeste01.find()) {
			assinador.append(" ; " + maTeste01.group().replace(",CN=", "").replace(",L=", ""));
		}

		return assinador.toString();
	}

	public static String nomeArquivoFormatado(String nomeArquivo, boolean assinado) throws Exception {
		// Verifica se e um arquivo fisico
		// if (this.isArquivoFisico())
		int pos = nomeArquivo.lastIndexOf(".");

		// Se nao encontrou o ultimo ponto ou se o arquivo nao e assinado
		if (pos == -1 || !assinado) return nomeArquivo;

		// Retira segunda extensao .p7s
		return URLEncoder.encode( nomeArquivo.substring(0, nomeArquivo.lastIndexOf(".")), "UTF-8");
	}

	public static String RetirarLetras(String valor, String caracter) {
		return valor.replaceAll("[sSzZtTdDvVfFbBpP]", caracter).replace(caracter + caracter, caracter);
	}

	/**
	 * Método que retorna o valor percentual, relaivo à parte em relação ao
	 * total. Internamente formata o valor para duas casas decimais
	 * 
	 * @param parte
	 *            Valor parte cujo percentual se deseja saber em relação ao
	 *            total
	 * @param total
	 *            Valor Total
	 * @return O percentual, num formato x.xx
	 */
	public static String getPercentual(long parte, long total) {

		if (total == 0) return "0.00";
		return formataCasasDecimais((parte * 100) / total,2);

	}

	/**
	 * Método que formata o valor para duas casas decimais
	 * 
	 * @param valor
	 *            Valor que se deseja formatar
	 * @return Valor com duas casas decimais
	 */
	private static String formataCasasDecimais(double valor, int numeroCasas) {
		NumberFormat formatadorDeNumeros = NumberFormat.getInstance();
		formatadorDeNumeros.setMaximumFractionDigits(numeroCasas);
		return formatadorDeNumeros.format(valor);

	}

	/**
	 * Funcão para permitir inserir uma String como BLOB - 16/09/2008 17:06
	 * Modificacoes para tratamento dos caracteres da string
	 * 
	 * @author mspaula
	 * @author Ronneesley Moura Teles
	 * @since 16/09/2008 17:06
	 */
	public static String BancoByte(String valor) throws Exception {
		String stRetorno = "null"; // Ate que se prove o contrario

		if (valor != null && valor.length() > 0) stRetorno = "_binary'" + tratamento(valor) + "'";

		// caracteres reservados
		stRetorno = stRetorno.replace("@", "¹");
		stRetorno = stRetorno.replace("#", "³");
		stRetorno = stRetorno.replace("$", "²");

		return stRetorno;
	}

	public static List converterSetParaList(Set set) {
		List lista = null;
		if (set != null) lista = new ArrayList(set);

		return lista;
	}

	public static Set converterListParaSet(List lista) {
		Set set = null;
		if (lista != null) {
			set = new HashSet();
			set.addAll(lista);
		}
		return set;
	}

	public static List converterMapParaList(Map map) {
		List lista = null;
		if (map != null) {
			lista = new ArrayList();
			lista.addAll(map.values());
		}
		return lista;
	}

	public static <T> List<T> converterMapParaListTipado(Map<String,T> map) {
		List<T> lista = null;
		if (map != null) {
			lista = new ArrayList<T>();
			lista.addAll(map.values());
		}
		return lista;
	}

	public static int horaToMinuto(String horaMinuto) {
		String[] horasMinutos = horaMinuto.split(":", -1);

		return (Funcoes.StringToInt(horasMinutos[0]) * 60) + Funcoes.StringToInt(horasMinutos[1]);
	}

	public static List GerarAgenda(String dataInicial, String dataFinal, String qtd, String[] configuracoes) throws Exception {

		List lisDatas = new ArrayList();
		Date dtFinal = StringToDate(dataFinal);
		int[] inPosicao = { 0, 3, 6, 9, 12, 15, 18 };

		Calendar calCalendarioGeral = Calendar.getInstance();

		Calendar calCalendarioInicial = Calendar.getInstance();
		Calendar calCalendarioFinal = Calendar.getInstance();

		calCalendarioGeral.setTime(StringToDate(dataInicial));
		do {
			// while (!calCalendarioGeral.after(dtFinal.getTime())){

			int inDia = inPosicao[(calCalendarioGeral.get(Calendar.DAY_OF_WEEK) - 1)];

			if (!configuracoes[inDia].equals("")) {
				calCalendarioInicial.setTime(calCalendarioGeral.getTime());
				calCalendarioFinal.setTime(calCalendarioGeral.getTime());

				String stHoraInicial = configuracoes[inDia];
				String stHoraFinal = configuracoes[inDia + 1];
				int inDuracao = Funcoes.StringToInt(configuracoes[inDia + 2]);

				calCalendarioInicial.add(Calendar.MINUTE, horaToMinuto(stHoraInicial));
				////System.out.println(DataHora(calCalendarioInicial.getTime()));

				calCalendarioFinal.add(Calendar.MINUTE, horaToMinuto(stHoraFinal));
				////System.out.println(DataHora(calCalendarioFinal.getTime()));

				while (calCalendarioInicial.getTime().getTime() < calCalendarioFinal.getTime().getTime()) {
					for (int j = 0; j < Funcoes.StringToInt(qtd); j++)
						lisDatas.add(new Date(calCalendarioInicial.getTime().getTime()));
					calCalendarioInicial.add(Calendar.MINUTE, inDuracao);
				}
			}

			calCalendarioGeral.add(Calendar.DAY_OF_WEEK, 1);
			////System.out.println(DataHora(calCalendarioGeral.getTime()));

		} while (calCalendarioGeral.getTimeInMillis() <= dtFinal.getTime());

		return lisDatas;
	}

	/**
	 * Método responsável em receber uma string e convertê-la em horário, ou
	 * seja, convertê-la em Date que corresponderá ao horário
	 * 
	 * @author Keila Sousa Silva
	 * @param horario
	 * @return Date = horário
	 */
	public static Date stringToTime(String horario) throws Exception {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
		return simpleDateFormat.parse(horario);
	}
	
	public static boolean validaHora(String horario) throws Exception {
		try {
			@SuppressWarnings("unused")
			Date horaValida = stringToTime(horario);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Método responsável por receber uma data completa e convertê-la para uma
	 * string, considerando apenas a data (dia + mês + ano) e desconsiderando o
	 * horário
	 * 
	 * @author Keila Sousa Silva
	 * @param data
	 * @return String = data
	 */
	public static String dateToStringSoData(Date data) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		return simpleDateFormat.format(data);
	}

	/**
	 * Método responsável por receber uma data completa e convertê-la para uma
	 * string, considerando apenas o horário e desconsiderando a data (dia + mês +
	 * ano)
	 * 
	 * @author Keila Sousa Silva
	 * @param data
	 * @return String = horário
	 */
	public static String dateToStringSoHorario(Date data) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
		return simpleDateFormat.format(data);
	}

	/**
	 * Saudacoes quanto ao dia para um visitante
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 04/09/2008 10:29
	 * @return String
	 */
	public static String saudacaoDia() {
		String s[] = { "Bom Dia, ", "Boa Tarde, ", "Boa Noite, " };

		int i = 0;

		Calendar c = Calendar.getInstance();

		if (c.get(Calendar.HOUR_OF_DAY) < 12) {
			i = 0;
		} else {
			if (c.get(Calendar.HOUR_OF_DAY) < 18) {
				i = 1;
			} else {
				i = 2;
			}
		}

		return s[i];
	}

	/**
	 * Converte quantidade de ano/mes/dia em dias
	 * 
	 * @author Wilana Carlos da Silva
	 * @since 03/09/2009 11:15
	 * @return String
	 */
	public static String converterParaDias(String qtdeAnos, String qtdeMeses, String qtdeDias) {
		
		if (qtdeAnos == null || qtdeAnos.length() == 0) qtdeAnos = "0";
		if (qtdeMeses == null || qtdeMeses.length() == 0) qtdeMeses = "0";
		if (qtdeDias == null || qtdeDias.length() == 0) qtdeDias = "0";

		//OPÇÃO 1
		//***************************************************************
		int ano = Funcoes.StringToInt(qtdeAnos);
		int mes = Funcoes.StringToInt(qtdeMeses);
		double dia = Funcoes.StringToDouble(qtdeDias);
		double totalDia = 0;
		
		//-------------------------------------------------------------
		//PROBLEMA DE ARREDONDAMENTO: 
		// subtrair um dia a cada 4 anos, à partir deste ano 
		// 0 - 8 - 0:  
		// 2 - 0 - 0:
		// 3 - 4 - 0:
		boolean subtrai1 = false;
		
		if (ano == 0 && mes == 8 && dia >= 0) subtrai1 = true;
		else if (ano >= 4 && mes == 8 && dia >= 0){
			double resto = (ano) % 4;
			if (resto == 0) 
				subtrai1 = true;
		}
		//--OPC1
		else if (ano == 2 && mes == 0 && dia >= 0) subtrai1 = true;
		else if (ano >= 6 && mes == 0 && dia >= 0){
			double resto = (ano-2) % 4;
			if (resto == 0) 
				subtrai1 = true; 
		}
		//--OPC2
		else if (ano == 3 && mes == 4 && dia >= 0) subtrai1 = true;
		else if (ano >= 7 && mes == 4 && dia >= 0){
			double resto = (ano-3) % 4;
			if (resto == 0) 
				subtrai1 = true;
		}
		
		if (subtrai1) totalDia--;
		//-------------------------------------------------------------
		
		while(1 <= ano) { //Ano Para Dia
			totalDia = totalDia + 365.25; 
			ano = ano - 1;
		}

		while(1 <= mes) { //Mes Para Dia
			totalDia = totalDia + 30.4375; 
			mes = mes - 1;
		}
		totalDia = totalDia + dia;
		

		
		return String.valueOf((int) Math.round(totalDia));
		//***************************************************************
	}


	/**
	 * Converte quantidade de dias em ano/mes/dia
	 * 
	 * @author Wilana Carlos da Silva
	 * @since 04/09/2009 09:17
	 * @return int
	 */
	public static String converterParaAnoMesDia(int qtdeDias) {
		boolean diasNegativo = false;
		if (qtdeDias < 0) diasNegativo = true;
		
		double tempo = Math.abs(qtdeDias); //coloca a qtde de dias como positivo

		//OPÇÃO 1
		//***************************************************************
		int ano = 0;
		while( 365.25 <= tempo) { //Dia Para Ano
			ano++; 
			tempo = tempo - 365.25;
		}
		int mes = 0;
		while( 30.4375 <= tempo) { //Dia Para Mes
			mes++;
			tempo = tempo - 30.4375;
		}
		
		double dia = tempo;
		qtdeDias = (int)Math.round(dia);
		
		if (qtdeDias >= 30) {
			mes += 1;
			qtdeDias = 0;
		}
		if (mes == 12) {
			ano += 1;
			mes -= 12;
		}
		
		StringBuffer retorno = new StringBuffer();
		if (String.valueOf(ano).length() <= 2)  retorno.append("00".substring(0, 2 - String.valueOf(ano).length()) + ano + " - ");
		else retorno.append(ano + " - ");
		retorno.append("00".substring(0, 2 - String.valueOf(mes).length()) + mes + " - ");
		retorno.append("00".substring(0, 2 - String.valueOf(qtdeDias).length()) + qtdeDias);

		String stRetorno = retorno.toString();
		if (diasNegativo) stRetorno = "(" + stRetorno + ")";
	
		return stRetorno;
	}

	/**
	 * Soma a qtde de dias à data informada.
	 * @param data, data no formato dd/mm/aaaa.
	 * @param qtdeDias, qtde de dias
	 * @return dataFim, data em String no formato dd/mm/aaa
	 * @author wcsilva
	 */
	public static String somaData(String data, int qtdeDias) throws Exception {
		Date dataFim = Funcoes.StringToDate(data);
		GregorianCalendar gr = new GregorianCalendar();
		gr.setTime(dataFim);
		gr.add(Calendar.DAY_OF_MONTH, qtdeDias);
		dataFim = gr.getTime();

		return Funcoes.dateToStringSoData(dataFim);
	}
	
	/**
	 * Soma a qtde de dias à data informada.
	 * @param data - Date
	 * @param qtdeDias - qtde de dias a ser adicionado/subtraido
	 * @return dataFim - data em String no formato dd/mm/aaaa
	 * @author hmgodinho
	 */
	public static String somarData(Date data, int qtdeDias) throws Exception {
		GregorianCalendar gr = new GregorianCalendar();
		gr.setTime(data);
		gr.add(Calendar.DAY_OF_MONTH, qtdeDias);
		data = gr.getTime();

		return Funcoes.dateToStringSoData(data);
	}

	/**
	 * Verifica o campo de entrada se está de acordo com o parâmetro, se estiver substitui pelo Retorno informado.
	 * No caso da lista de eventos quando o campo estiver nulo, será substituído por "-".
	 * @param campo, o campo a ser verificado.
	 * @param parametro, o parametro que o campo será comparado
	 * @return retorno, se o campo for igual ao parametro retorna o valor desejado
	 * @author kbsriccioppo
	 */
	public static String verificarCampo(String campo, String parametro, String retorno) {
		if (campo == parametro) {
			return retorno;
		} else return campo;
	}

	/**
	 * Método que recebe o número do Mês e retorna o nome.
	 * @param numeroMes - número do mês (de 01 a 12)
	 * @return Strin - nome do mês (de Janeiro a Dezembro)
	 * @author hmgodinho
	 */
	public static String identificarNomeMes(Integer numeroMes) {
		String nomeMes = null;
		switch (numeroMes) {
			case 1:
				nomeMes = "Janeiro";
				break;
			case 2:
				nomeMes = "Fevereiro";
				break;
			case 3:
				nomeMes = "Março";
				break;
			case 4:
				nomeMes = "Abril";
				break;
			case 5:
				nomeMes = "Maio";
				break;
			case 6:
				nomeMes = "Junho";
				break;
			case 7:
				nomeMes = "Julho";
				break;
			case 8:
				nomeMes = "Agosto";
				break;
			case 9:
				nomeMes = "Setembro";
				break;
			case 10:
				nomeMes = "Outubro";
				break;
			case 11:
				nomeMes = "Novembro";
				break;
			case 12:
				nomeMes = "Dezembro";
				break;
		}
		return nomeMes;
	}
	
	/**
	 * Valida a numeração de processo no formato: NNNNNNN.DD.AAAA.JTR.OOOO 
	 * Onde: NNNNNNN = Número do processo
	 *       DD = Dígito
	 *       AAAA = Ano
	 *       JTR = Constante 8.09
	 *       OOOO = Código do Forum
	 * Ex: 5000280.28.2010.8.09.0059
	 */
	public static boolean validaProcessoNumero(String numeroProcesso) {
		
		//Verifica os parâmetros informados...
		if (numeroProcesso == null) return false;
		numeroProcesso = numeroProcesso.replace("-", ".");
		String[] partesNumero = numeroProcesso.split("\\.");
		if (partesNumero.length != 6) return false;		
				
		//Extrai do número do processo as informações necessárias para validação...
		long NNNNNNN = Funcoes.StringToLong(partesNumero[0]); 
		long DD  = Funcoes.StringToLong(partesNumero[1]);
		long AAAA = Funcoes.StringToLong(partesNumero[2]); 
		long JTR = Funcoes.StringToLong(partesNumero[3] + partesNumero[4]); 
		long OOOO = Funcoes.StringToLong(partesNumero[5]);
		
		//Se o número não for válido é retornado...
		return Funcoes.valida_mod97(NNNNNNN, DD, AAAA, JTR, OOOO);	
		
	}
	
	public static boolean validarJTRProcesso(String numeroProcesso) {
		
		if (numeroProcesso == null) return false;
		String[] partesNumero = numeroProcesso.split("\\.");
				
		String JTRprocesso = partesNumero[3] + "." +partesNumero[4]; 
		if(!JTRprocesso.equalsIgnoreCase(Configuracao.JTR)){
			return false;
		}
		
		return true;	
		
	}

	/**
	 * @author jrcorrea
	 * @param char valor
	 * @param Long valor padrao
	 * @return Long
	 */
	public static int CharToInt(char valor, int valorPadrao ){
		int inRetorno = 0;
		try{
			inRetorno = Character.getNumericValue(valor);
		}catch(Exception e){		
			inRetorno = valorPadrao;
		}
		
		return inRetorno;
	}

	/**
	 * @author jrcorrea
	 * @param String valor
	 * @return int
	 */
	public static int StringToInt(String valor ){
		int inRetorno = 0;
		try{
			inRetorno = Integer.valueOf(valor.trim());
		}catch(Exception e){	
			inRetorno = 0;
		}
		
		return inRetorno;
	}
	/**
	 * @author jrcorrea
	 * @param String valor
	 * @param Long valor padrao
	 * @return int
	 */
	public static int StringToInt(String valor, int valorPadrao ){
		int inRetorno = 0;
		try{
			inRetorno = Integer.valueOf(valor.trim());
		}catch(Exception e){		
			inRetorno = valorPadrao;
		}
		
		return inRetorno;
	}

	/**
	 * @author jrcorrea
	 * @param String valor	
	 * @return boolean
	 */
	public static boolean StringToBoolean(String valor){
		if (valor!=null) {
			String stTemp = valor.trim().toLowerCase();
			if ("1".equals(stTemp)) {
				return true;
			}if ("sim".equals(stTemp)) {
				return true;
			}if ("true".equals(stTemp)) {
				return true;
			}if ("verdadeiro".equals(stTemp)) {
				return true;
			}
		}	
		return false;
	}
	
	/**
	 * @author jrcorrea
	 * @param String valor	
	 * @return double
	 */
	public static double StringToDouble(String valor ){
		double douRetorno = 0;
		try{			
			if (valor != null && valor.contains(",")) valor = BancoDecimal(valor);			
			douRetorno =  Double.valueOf(valor.trim());
		}catch(Exception e){
			douRetorno = 0;
		}
		
		return douRetorno;
	}
	
	/**
	 * @author jrcorrea
	 * @param String valor	
	 * @return Long
	 */
	public static long StringToLong(String valor ){
		long loRetorno = 0;
		try{
			loRetorno =  Long.valueOf(valor.trim());
		}catch(Exception e){
			loRetorno = 0;
		}
		
		return loRetorno;
	}
	/**
	 * @author jrcorrea
	 * @param String valor
	 * @param Long valor padrao
	 * @return Long
	 */
	public static long StringToLong(String valor, long valorPadrao ){
		long loRetorno = 0;
		try{
			loRetorno = Long.valueOf(valor.trim());
		}catch(Exception e){		
			loRetorno = valorPadrao;
		}
		
		return loRetorno;
	}
	
	
	/**
	 * Retorna o conteúdo da execeção incluindo stack, de toda a pilha de exceções
	 * 
	 * @param excecao
	 * @return
	 */
	public static String obtenhaConteudoExcecao(Throwable excecao){	
		return obtenhaConteudoExcecao(excecao, false, "");
	}
	
	/**
	 * Retorna o conteúdo da primeira execeção da pilha incluindo stack
	 * 
	 * @param excecao
	 * @return
	 */
	public static String obtenhaConteudoPrimeiraExcecao(Throwable excecao){	
		if (excecao == null) return "";
		return obtenhaConteudoExcecao(excecao, true, excecao.getMessage());
	}
	
	/**
	 * Obtem o conteúdo da exceção, incluindo stacktrace.
	 * 
	 * Caso seja enviado true para o parametro somentePrimeiraExcecao, é realizado um desempilhamento
	 * obtendo o conteúdo apenas da exceção originária.
	 * 
	 * Caso seja enviado false para o parametro somentePrimeiraExcecao, é realizado um desempilhamento
	 * obtendo o conteúdo de todas as exceções empilhadas.
	 * 
	 * @param excecao
	 * @param somentePrimeiraExcecao
	 * @param mensagemOriginal
	 * 
	 * @return
	 * 
	 * @author mmgomes
	 */
	private static String obtenhaConteudoExcecao(Throwable excecao, boolean somentePrimeiraExcecao, String mensagemOriginal){	
		if (excecao == null) return "";
		StringBuffer sb = new StringBuffer();
		
		try {
			
			if (somentePrimeiraExcecao && (excecao.getCause() !=null)){
				sb.append(obtenhaConteudoExcecao(excecao.getCause(), somentePrimeiraExcecao, mensagemOriginal));
				return sb.toString();
			}
		    
			if (excecao.getClass() != null) sb.append("\n CLASS: " + excecao.getClass().toString());
		    sb.append("\n LOCALIZE MESSAGE: " + excecao.getLocalizedMessage());
		    
		    sb.append("\n MESSAGE: ");
		    if(mensagemOriginal != null && mensagemOriginal.trim().length() > 0) sb.append(mensagemOriginal);
		    else sb.append(excecao.getMessage());
		    
		    sb.append("\n STACK TRACE: "); 
		    
		    Writer writer = new StringWriter();
		    PrintWriter printWriter = new PrintWriter(writer);
		    excecao.printStackTrace(printWriter);
		    sb.append("\n " + writer.toString());
		    
		   if (excecao.getCause() !=null){
		    	sb.append("\n InnerException: \n");
		    	sb.append(obtenhaConteudoExcecao(excecao.getCause(), somentePrimeiraExcecao, mensagemOriginal));
		   }	    
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return sb.toString();
	}

    public static String BancoAno(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
        return simpleDateFormat.format(date);
    }
    
//    /**
//     * Serializa a excecao em um arquivo txt
//     * 
//     * @param Exception excecao
//     *  
//     */
//	public static void serializeExcecao(Throwable excecao){	
//		try {		
//			ObjectOutputStream output = getNovoArquivo(ProjudiPropriedades.getInstance().getCaminhoAplicacao(),
//                                                       getGereNomeArquivoErro()); 
//
//            output.writeUTF(obtenhaConteudoPrimeiraExcecao(excecao));          
//            
//            output.flush();
//            
//            output.close();
//		}
//		catch(Exception e){
//			e.printStackTrace();
//		}		
//	}
	
	/**
     * Método para serializar objeto na memória.
     * Exemplo de utilização é para fazer cópias de objetos sem manter a referência do objeto.
     * @param Object obj
     * @return Object
     */
    public static Object serializeObjeto(Object obj) throws Exception {
    	Object retorno = null;
    	ByteArrayOutputStream bos = null;
    	ObjectOutputStream out = null;
    	ObjectInputStream in = null;
    	try {
	    	 bos = new ByteArrayOutputStream();
	         out = new ObjectOutputStream(bos);
	        out.writeObject(obj);
	        out.flush();	        
	        in = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));
	        retorno = in.readObject();
    	} finally {
    		try {if (out!=null) out.close(); } catch(Exception ex ) {};
    		try {if (bos!=null) bos.close(); } catch(Exception ex ) {};
    		try {if (in!=null) in.close(); } catch(Exception ex ) {};
    	}
    	
    	return retorno;
    }
	
	/**
     * Retorna o nome de um arquivo de erro utilizando a extensão txt
     *  
     */
	public static String getGereNomeArquivoErro(){
		return getGereNomeArquivo("txt"); 
	}
	
	/**
     * Retorna o nome gerado de um arquivo utilizando a data e hora do sistema
     * 
     * @param String extencaoSemPonto
     *  
     */
	public static String getGereNomeArquivo(String extencao){
		UUID idGerado = UUID.randomUUID();
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss");
		if (extencao == null) extencao = "";		
		return String.format("%s_%s.%s", 
				              df.format(new Date()),
				              idGerado.toString(),
				              extencao.replace(".", ""));		
	}
	
	/**
     * Obtem referência a um arquivo
     * 
     * @param String diretorio
     * @param String nomeDoArquivo
     *  
     */
	public static ObjectOutputStream getNovoArquivo(String diretorio, String nomeArquivo) throws FileNotFoundException, IOException{
		return new ObjectOutputStream(new FileOutputStream(diretorio + nomeArquivo));
	}
	
	/**
	 * Método para retornar o último dia do mês.
	 * O primeiro parâmetro é o mês e deve estar na seguinte sequencia: janeiro = 1, fevereiro = 2 e etc.
	 * O segundo parâmetro é o ano e deve estar no formato de 4 dígitos. Ex.: 2011 ou 2012.
	 * @param String mes
	 * @param String ano
	 * @return Date
	 */
	public static Date getUltimoDiaMes(String mes, String ano) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Funcoes.StringToInt(ano), Funcoes.StringToInt(mes), 1);
		calendar.setTimeInMillis( calendar.getTimeInMillis() - 86400000 );
		
		return calendar.getTime();
	}
	
	/**
	 * Método para retornar o último dia do mês do dia atual
	 * @return
	 */
	public static Date getUltimoDiaMes(){
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, 1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		return calendar.getTime();
	}
	
	
	/**
	 * Método para retornar o último dia, hora, minuto e segundo do mês.
	 * O primeiro parâmetro é o mês e deve estar na seguinte sequencia: janeiro = 1, fevereiro = 2 e etc.
	 * O segundo parâmetro é o ano e deve estar no formato de 4 dígitos. Ex.: 2011 ou 2012.
	 * @param String mes
	 * @param String ano
	 * @return Date
	 */
	public static Date getUltimoDiaHoraMinutoSegundoMes(String mes, String ano) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Funcoes.StringToInt(ano), Funcoes.StringToInt(mes), 1);
		calendar.setTimeInMillis(calendar.getTimeInMillis() - 86400000);
		
		calendar = calculeUltimaHora(calendar);
		
		return calendar.getTime();
	}
	
	/**
	 * Método para retornar o primeiro dia do mês no primeiro segundo dele.
	 * O primeiro parâmetro é o mês e deve ser enviado como: Janeiro = 1, Fevereiro = 2, etc.
	 * O segundo parâmetro é o ano e deve estar no formato de 4 dígitos. Ex.: 2011 ou 2012.
	 * @param String mes
	 * @param String ano
	 * @return Date
	 */
	public static Date getPrimeiroDiaMes(int mes, int ano) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.MONTH, mes - 1);
		calendar.set(Calendar.YEAR, ano);
		
		calendar = calculePrimeraHora(calendar);
		
		return calendar.getTime();
	}
	
	/**
	 * Método que retorna o dia anterior do atual.
	 * @return Date
	 * @author fasoares
	 */
	public static Date getDiaAnterior() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis( calendar.getTimeInMillis() - 86400000 );
		
		return calendar.getTime();
	}
	
	/**
	 * Método que retorna o dia anterior do atual.
	 * @return Date
	 * @author fasoares
	 */
	public static Date get2DiasAtras() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis( calendar.getTimeInMillis() - 172800000 );
		
		return calendar.getTime();
	}
	
	/**
	 * Método que 3 dias atrás.
	 * @return Date
	 * @author fasoares
	 */
	public static Date get3DiasAtras() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis( calendar.getTimeInMillis() - 259200000 );
		
		return calendar.getTime();
	}
	
	/**
	 * Método para retornar o primeiro dia do mês.
	 * @return Date
	 */
	public static Date getPrimeiroDiaMes() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		return calendar.getTime();
	}
	
	/** 
     * Calcula a diferença de duas datas em horas. Importante: Quando realiza a diferença em horas entre duas datas, 
     * este método considera os minutos restantes e os converte em fração de horas. 
     * @param dataInicial 
     * @param dataFinal 
     * @return quantidade de horas existentes entre a dataInicial e dataFinal. 
     */  
    public static double diferencaEmHoras(Date dataInicial, Date dataFinal){  
        double result = 0;  
        long diferenca = dataFinal.getTime() - dataInicial.getTime();  
        long diferencaEmHoras = (diferenca /1000) / 60 / 60;  
        long minutosRestantes = (diferenca / 1000)/60 %60;  
        double horasRestantes = minutosRestantes / 60d;  
        result = diferencaEmHoras + (horasRestantes);  
          
        return result;  
    }  
      
    /** 
     * Calcula a diferença de duas datas em minutos. Importante: Quando realiza a diferença em minutos entre duas datas, 
     * este método considera os segundos restantes e os converte em fração de minutos. 
     * @param dataInicial 
     * @param dataFinal 
     * @return quantidade de minutos existentes entre a dataInicial e dataFinal. 
     */  
    public static double diferencaEmMinutos(Date dataInicial, Date dataFinal){  
        double result = 0;  
        long diferenca = dataFinal.getTime() - dataInicial.getTime();  
        double diferencaEmMinutos = (diferenca /1000) / 60; //resultado é diferença entre as datas em minutos  
        long segundosRestantes = (diferenca / 1000)%60; //calcula os segundos restantes  
        result = diferencaEmMinutos + (segundosRestantes /60d); //transforma os segundos restantes em minutos  
      
        return result;  
    }
    
    /**
     * Retorna a data e hora no formato String utilizado pelo Projudi dd/MM/yyyy HH:mm:ss
     * 
     * @param GregorianCalendar calendar
     *  
     */
	public static String getDataDiaMesAnoHoraMinuntoSegundo(GregorianCalendar calendar){
		if (calendar == null) return "";
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		return df.format(calendar.getTime());				
	}
	
	/**
	 * Verifica se foi infomado algum texto no editor html
	 * 
	 * @param texto
	 * @return
	 */
	public static boolean possuiTextoInformadoEditorHTML(String texto){
		if (texto == null) return false;
		
		return (texto.replaceAll("<p>", "").replaceAll("</p>", "").replaceAll("&nbsp;", "").trim().length() > 0);
	}
	
	/**
	 * Retorna um objeto calendar de acordo com os parâmetros informados
	 *  
	 * @param int ano
	 * @param int mes
	 * @param int dia
	 *  
	 */
	public static Calendar obtenhaCalendar(int ano, int mes, int dia){
		return obtenhaCalendar(ano, mes, dia, 0, 0, 0);
	}
	
	/**
	 * Retorna um objeto calendar de acordo com os parâmetros informados
	 *  
	 * @param int ano
	 * @param int mes
	 * @param int dia
	 * @param int hora
	 * @param int minuto
	 * @param int segundo
	 *  
	 */
	public static Calendar obtenhaCalendar(int ano, int mes, int dia, 
			                               int hora, int minuto, int segundo){
		Calendar data = null;
		data = Calendar.getInstance();
		data.setTime(new Date());
		//o mês em Calendar inicia de 0
		data.set(ano, (mes - 1), dia, 
				hora, minuto, segundo);
		
		return data;
	}
	
	/**
	 * Retorna um objeto calendar com a última hora do dia
	 *  
	 * @param Calendar data
	 *  
	 */
	public static Calendar calculeUltimaHora(Calendar data){
		data.set(Calendar.HOUR_OF_DAY, 23);
		data.set(Calendar.MINUTE, 59);
		data.set(Calendar.SECOND, 59);
		data.set(Calendar.MILLISECOND, 999);
		
		return data;
	}
	
	/**
	 * Retorna um objeto calendar com a primeira hora do dia
	 *  
	 * @param Calendar data
	 *  
	 */
	public static Calendar calculePrimeraHora(Calendar data){
		data.set(Calendar.HOUR_OF_DAY, 0);
		data.set(Calendar.MINUTE, 0);
		data.set(Calendar.SECOND, 0);
		data.set(Calendar.MILLISECOND, 0);
		
		return data;
	}
	
	/**
	 * Retorna um objeto calendar com a última hora do dia
	 *  
	 * @param Calendar data
	 *  
	 */
	public static Date calculeUltimaHora(Date data){
		Calendar dataTemp = new GregorianCalendar();
		dataTemp.setTime(data);
		dataTemp.set(Calendar.HOUR_OF_DAY, 23);
		dataTemp.set(Calendar.MINUTE, 59);
		dataTemp.set(Calendar.SECOND, 59);
		dataTemp.set(Calendar.MILLISECOND, 999);
		
		return dataTemp.getTime();
	}
	
	/**
	 * Retorna um objeto calendar com a primeira hora do dia
	 *  
	 * @param Calendar data
	 *  
	 */
	public static Date calculePrimeraHora(Date data){
		Calendar dataTemp = new GregorianCalendar();
		dataTemp.setTime(data);
		dataTemp.set(Calendar.HOUR_OF_DAY, 0);
		dataTemp.set(Calendar.MINUTE, 0);
		dataTemp.set(Calendar.SECOND, 0);
		dataTemp.set(Calendar.MILLISECOND, 0);
		
		return dataTemp.getTime();
	}
	
	/**
	 * Método para retornar o primeiro dia do ano no primeiro segundo.
	 * @param String ano
	 * @return Date
	 * @author hmgodinho
	 */
	public static Date getPrimeiroDiaHoraMinutoSegundoAno(String ano) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.MONTH, Calendar.JANUARY);
		calendar.set(Calendar.YEAR, Funcoes.StringToInt(ano));
		
		calendar = calculePrimeraHora(calendar);
		
		return calendar.getTime();
	}
	
	/**
	 * Método para retornar a Imagem do código de barra.
	 * @param String Numero - A ser transformado em uma imagem do código de barra
	 * @param String Tipo - É o tipo de código de barra que será gerado, por padrão é o 3of9
	 * @author jlsilva - 03/08/2012
	 */
	public static Image CodigoBarraGeraImagem(String Numero, String Tipo) throws BarcodeException, OutputException{
		Barcode barcode = null;
		if(Tipo=="2of7"){
			barcode = BarcodeFactory.create2of7(Numero);	
		}else if(Tipo=="128"){
			barcode = BarcodeFactory.createCode128(Numero);
		}else{
			barcode = BarcodeFactory.create3of9(Numero, false); //Tipo padrão 3of9
		}
		Image CodigoBarra = null;
        BufferedImage image = new BufferedImage(220, 130, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D g = (Graphics2D) image.getGraphics();
        g.setBackground(Color.black);  
        barcode.draw(g, 10, 56);
		CodigoBarra =  (Image) BarcodeImageHandler.getImage(barcode);
        return CodigoBarra;		
	}
	
	
	/**
	 * Método para gravar a Imagem do código de barra em determinado local.
	 * @param String Numero - A ser transformado em uma imagem do código de barra
	 * @param String Tipo - É o tipo de código de barra que será gerado, por padrão é o 3of9
	 * @param String Caminho - Local que será gravado a imagem, exemplo: Funcoes.CodigoBarraSalvaImagem(Numero,"3of9","E:\\Projetos\\");
	 * @author jlsilva - 03/08/2012
	 */
	public static void CodigoBarraSalvaImagem(String Numero, String Tipo, String Caminho) throws BarcodeException, OutputException{
		Barcode barcode = null;
		if(Tipo=="2of7"){
			barcode = BarcodeFactory.create2of7(Numero);	
		}else if(Tipo=="128"){
			barcode = BarcodeFactory.createCode128(Numero);
		}else{
			barcode = BarcodeFactory.create3of9(Numero, false); //Tipo padrão 3of9
		}
		BufferedImage image = new BufferedImage(220, 130, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D g = (Graphics2D) image.getGraphics();
        g.setBackground(Color.black);  
        barcode.draw(g, 10, 56);
    	File f = new File(Caminho+Numero + ".jpg");  
    	BarcodeImageHandler.saveJPEG(barcode, f);  
	}
	

	
	/**
	 * Método para transformar uma Imagem em um byte[]
	 * @param Image image
	 * @author jlsilva - 23/07/2013
	 */
	public static byte[] ImageParaByte(Image image) throws BarcodeException, OutputException{
	    BufferedImage bi = new BufferedImage(image.getWidth(null),image.getHeight(null),BufferedImage.TYPE_INT_RGB);  
	    Graphics bg = bi.getGraphics();  
	    bg.drawImage(image, 0, 0, null);  
	    bg.dispose();  
	      
	    ByteArrayOutputStream buff = new ByteArrayOutputStream();         
	    try {    
	        ImageIO.write(bi, "JPG", buff);    
	    } catch (IOException e) {    
	        e.printStackTrace();    
	    }    
	    return buff.toByteArray();   
	}

	
	
	

	/**
	 * Método para formatar o cep.
	 * @param String cep
	 * @return String cepFormatado
	 */
	public static String formatarCep(String cep) {
		String cepFormatado = "";
		
		if( cep != null && cep.length() > 0) {
			cepFormatado = Funcoes.completarZeros( cep , 8);
			
			cepFormatado = cepFormatado.substring(0,5) + "-" + cepFormatado.substring(5);
		}
		
		return cepFormatado;
	}
	
	/**
	 * Método que consulta um xml através de requisição http método get
	 * @param HttpGetUrl
	 * @return
	 * @throws Exception
	 * @author jrcorrea 29/09/2015
	 */
	public static String lerURL(String HttpGetUrl) throws Exception {

		try(DefaultHttpClient httpclient = new DefaultHttpClient()){
			HttpGet httpget = new HttpGet(HttpGetUrl);
			httpget.getParams().setParameter("http.socket.timeout",new Integer(99920000));
		
			HttpResponse response;
			String stRetorno = null;
			response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				stRetorno = EntityUtils.toString(entity);
			}
	
			return stRetorno;
		}
	}
	
	/**
	 * Calculo de idade
	 * @param dataNascimentoDD_MM_AAAAA
	 * @return
	 * @throws MensagemException
	 */
	public static int calculeIdade(String dataNascimentoDD_MM_AAAAA) throws MensagemException
	{	
		if(dataNascimentoDD_MM_AAAAA == null || dataNascimentoDD_MM_AAAAA.trim().length() == 0 || !validaData(dataNascimentoDD_MM_AAAAA)) return 0;
		
		TJDataHora dataNascimento = new TJDataHora(dataNascimentoDD_MM_AAAAA);
				
		TJDataHora dataAtual = new TJDataHora();
				
		int quantidadeDeAnos = (dataAtual.getAno() - dataNascimento.getAno());
		
		if ((dataAtual.getMes() < dataNascimento.getMes()) || (dataAtual.getMes() == dataNascimento.getMes() && dataAtual.getDia() < dataNascimento.getDia()))
			quantidadeDeAnos -= 1;		
		
		return quantidadeDeAnos;
	}
	
//	/**
//	 * Remove caracteres especiaisde uma string.
//	 * @param valor
//	 * @return
//	 */
//	public static String remover CaracteresEspeciais(String valor) {
//		if (valor != null){
//			valor = valor.replaceAll("\t|\r|\\\\", "");
//		}
//		return valor;
//	} 
	
	/**
	 * Remove caracteres especiais e aspas duplas de uma string.
	 * @param valor
	 * @return
	 */
	public static String removerCaracteresEspeciaisEAspasDuplas(String valor) {
		if (valor != null){
			valor = valor.replaceAll("\"", "");
		}
		return removerCaracteresControleEspeciais(valor);
	} 
	
	public static String removerQuebrasDeLinha(String valor) {
		if (valor != null){
			valor = valor.replaceAll("\n", "").replaceAll("\r", "");
		}
		return valor;
	} 
	
	/**
	 * Método que faz a limpeza do nome dos arquivos a serem inseridos no sistema.
	 * Após a limpeza o nome do arquivo ficará com letras minúsculas, sem caracteres especiais
	 * e sem qualquer outro caracter de comando.
	 * @param nomeArquivo - nome do arquivo a ser limpo
	 * @return string com o nome do arquivo limpo
	 * @author hmgodinho
	 */
	public static String limparNomeArquivo(String nomeArquivo) {
		
		// sempre minusculas
		nomeArquivo = nomeArquivo.toLowerCase().trim();
	
		// sempre sem acentos ou cedilha
		nomeArquivo = nomeArquivo.replaceAll("[åáàãâä]", "a").replaceAll("[éèêë]", "e").replaceAll("[íìîï]", "i").replaceAll("[øóòõôö]", "o").replaceAll("[úùûü]", "u").replaceAll("[ç]", "c");
	
		// tira tudo que não for de 0 a 9, A a Z, ponto e espaço simples
		nomeArquivo = nomeArquivo.replaceAll("[^a-z0-9\\.\\s\\_]", "");
			
		// substitui os espaços duplos por espaços simples
		nomeArquivo = nomeArquivo.replaceAll("\\s\\s*", " ");
					
		// substitui os espaços por espaços simples por underline
		nomeArquivo = nomeArquivo.replaceAll("\\s\\s*", "_").trim();

		return nomeArquivo;
	}
	
	/**
	 * Substitui caracteres especiais XML de uma string.
	 * @param valor
	 * @return
	 */
	public static String substituirCaracteresEspeciaisXML(String valor, boolean substituiAberturaFechamentoTags) {
		if (valor != null)
		{
			// Volta todos os caracteres...
			valor = valor.replaceAll("&amp;", "&");
			if (substituiAberturaFechamentoTags)
			{
				valor = valor.replaceAll("&lt;", "<");
				valor = valor.replaceAll("&gt;", ">");
			}
			valor = valor.replaceAll("&quot;", "\"");
			valor = valor.replaceAll("&apos;", "'");			
			
			// Substitui para os caracteres válidos...
			valor = valor.replaceAll("&", "&amp;");
			if (substituiAberturaFechamentoTags)
			{
				valor = valor.replaceAll("<", "&lt;");			
				valor = valor.replaceAll(">", "&gt;");
			}
			valor = valor.replaceAll("\"", "&quot;");
			valor = valor.replaceAll("'", "&apos;");
		}
		return valor;
	} 
	
//    public static String removerAcentos(String acentuada) {  
//        CharSequence cs = new StringBuilder(acentuada);  
//        return Normalizer.normalize(cs, Normalizer.Form.NFKD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");  
//    }
	
	/**
     * Retorna o tempo formatado em minutos e segundos e entre parenteses: em dias, horas, minutos e segundos, omitindo os valores zerados
     * Exemplo: 1440 m 20 s (24 h 20 s)
     * @param long tempoEmSegundos 
     *  
     */
	public static String tempoFormatadoParaRelatorioEmMinutos(long tempoEmSegundos){
		
		String retorno = tempoFormatadoEmMinutos(tempoEmSegundos);
		
		if (tempoEhMaiorQueUmaHora(tempoEmSegundos * 1000)){
			retorno += " (" +
	                   tempoFormatado(tempoEmSegundos) + 
	                   ")";
		}       
			
		return  retorno;	           		
	}
	
	/**
     * Retorna o tempo formatado em dias, horas, minutos e segundos, omitindo os que tem valores zerados
     * 
     * @param long tempoEmSegundos 
     *  
     */
	public static String tempoFormatadoEmMinutos(long tempoEmSegundos){
		return intervaloFormatado(obtenhaTempo(tempoEmSegundos * 1000, true), true);
	}
	
	/**
     * Retorna o tempo formatado em dias, horas, minutos e segundos, omitindo os que tem valores zerados
     * 
     * @param long tempoEmSegundos 
     *  
     */
	public static String tempoFormatado(long tempoEmSegundos){
		return intervaloFormatado(obtenhaTempo(tempoEmSegundos * 1000, false), true);
	}
	
	/**
     * Retorna true se a diferença entre as datas forem superiores a uma hora.
     * 
     * @param long tempoEmMilisegundos
     *  
     */
	private static boolean tempoEhMaiorQueUmaHora(long tempoEmMilisegundos){
		return (((int) tempoEmMilisegundos / 3600000) > 0);
	}
	
	/**
     * Retorna true se a diferença entre as datas forem superiores a uma hora.     
     * 
     * @param Date dataFinal
     * @param Date dataInicial
     *  
     */
	private static boolean tempoEhMaiorQueUmaHora(Date dataFinal, Date dataInicial){
		return tempoEhMaiorQueUmaHora(dataFinal.getTime() - dataInicial.getTime());
	}
	
	/**
     * Retorna um array com o tempo em milisegundos, sendo:
     * [0] dias
     * [1] horas
     * [2] minutos
     * [3] segundos
     * 
     * @param long tempoEmMilisegundos 
     *  
     */
	private static long[] obtenhaTempo(long tempoEmMilisegundos, 
			                           boolean somenteEmMinutos){	
		
		long dias = 0;
		int horas = 0;
		
		if (!somenteEmMinutos){
			dias = tempoEmMilisegundos / 86400000;
			if (dias > 0) {
				tempoEmMilisegundos -= dias * 86400000;
			}
			horas = (int) tempoEmMilisegundos / 3600000;
			if (horas > 0) {
				tempoEmMilisegundos -= horas * 3600000;
			}
		}		
		int minutos = (int) tempoEmMilisegundos / 60000;
		if (minutos > 0) {
			tempoEmMilisegundos -= minutos * 60000;
		}
		int segundos = (int) tempoEmMilisegundos / 1000;
		if (segundos > 0) {
			tempoEmMilisegundos -= segundos * 1000;
		}
		return new long[] { dias, horas, minutos, segundos };
	}

	/**
     * Retorna o tempo formatado em dias, horas, minutos e segundos, sem omitir os valores zerados     * 
     * @param long tempo[]
     * [0] dias
     * [1] horas
     * [2] minutos
     * [3] segundos
     * @param boolean omiteSemValores
     *  Exemplo false: 0 dias 24 h 0 m e 20 s
     *  Exemplo true: 24 h 20 s
     */
	private static String intervaloFormatado(long tempo[], boolean omiteSemValores){
		if (tempo == null) return "";
		
		String stRetorno = "";	
		
		if (!omiteSemValores)
			stRetorno = tempo[0] + " dias ";
		else if (tempo[0] > 0) 
			stRetorno = tempo[0] + " dias ";
		
		if (!omiteSemValores)
			stRetorno += tempo[1] + "h ";
		else if (tempo[1] > 0)				
			stRetorno += tempo[1] + "h ";
		
		if (!omiteSemValores)
			stRetorno += tempo[2] + "m ";
		else if (tempo[2] > 0)				
			stRetorno += tempo[2] + "m ";
		
		if (!omiteSemValores)
			stRetorno += tempo[3] + "s";
		else if (tempo[3] > 0)				
			stRetorno += tempo[3] + "s";		
			
		return stRetorno;
	}
	
	/**
     * Retorna o tempo formatado em minutos e segundos e entre parenteses: em dias, horas, minutos e segundos, omitindo os valores zerados
     * Exemplo: 1440 m 20 s (24 h 20 s)
     * @param Date dataFinal 
     * @param Date dataInicial
     *  
     */
	public static String diferencaDatasFormatadaParaRelatorioEmMinutos(Date dataFinal, Date dataInicial){
		String retorno = diferencaDatasEmMinutosFormatadaOmitindoValoresZerados(dataFinal, dataInicial);		
		
		if (tempoEhMaiorQueUmaHora(dataFinal, dataInicial)){
			retorno += " (" + 
            Funcoes.diferencaDatasFormatadaOmitindoValoresZerados(dataFinal, dataInicial) + 
            ")";
		}              
		
		return retorno;
	}
	
	/**
     * Retorna o tempo formatado em minutos e segundos, omitindo os valores zerados
     * Exemplo: 24 h 20 s
     * @param Date dataFinal 
     * @param Date dataInicial
     *  
     */
	public static String diferencaDatasEmMinutosFormatadaOmitindoValoresZerados(Date dataFinal, Date dataInicial) {
		return diferencaDatasFormatada(dataFinal, dataInicial, true, true);		
	}
	
	/**
     * Retorna o tempo formatado em dias, horas, minutos e segundos, omitindo os valores zerados
     * Exemplo: 24 h 20 s
     * @param Date dataFinal 
     * @param Date dataInicial
     *  
     */
	public static String diferencaDatasFormatadaOmitindoValoresZerados(Date dataFinal, Date dataInicial) {
		return diferencaDatasFormatada(dataFinal, dataInicial, true,false);		
	}
	
	/**
     * Retorna o tempo formatado em dias, horas, minutos e segundos da diferença entre as datas informadas.
     * 
     * @param Date dataFinal 
     * @param Date dataInicial
     *  
     */
	private static String diferencaDatasFormatada(Date dataFinal, Date dataInicial, boolean omiteSemValores, boolean somenteEmMinutos) {

		if ((dataFinal == null) || (dataInicial == null)) return "";

		long dif[] = diferencaDatas(dataFinal, dataInicial, somenteEmMinutos);
		
		return intervaloFormatado(dif,omiteSemValores);
	}
	
	/**
     * Retorna um array com a diferença de tempo entre os parâmetros, sendo: 
     * [0] dias
     * [1] horas
     * [2] minutos
     * [3] segundos
     * 
     * @param Date dataFinal
     * @param Date dataInicial
     *  
     */
	public static long[] diferencaDatas(Date dataFinal, Date dataInicial, boolean somenteEmMinutos) {
		long diferenca = dataFinal.getTime() - dataInicial.getTime();
		
		return obtenhaTempo(diferenca, somenteEmMinutos);		
	}
	
	/**
     * Retorna a diferença de percentual dos valores informados
     * 
     * @param long parte
     * @param long total
     * Exemplo:
     * parte = 10
     * total = 100
     * retorno = 90% 
     */
	public static String getDiferencaDePercentual(long parte, long total) {

		if (total == 0) return "0.00";
		double cemPorCento = 100.0;
		return formataPara2CasasDecimais(cemPorCento - getCalculoPercentual(parte, total));

	}
	
	/**
     * Retorna o valor calculado para obter o percentual
     * 
     * @param long parte
     * @param long total
     *  
     */
	private static double getCalculoPercentual(long parte, long total) {
		double parteTodo = 100.0;
		return ((parte * parteTodo) / total);
	}
	
	/**
     * Retorna os valores formatados utilizando duas casas decimais
     * 
     * @param double valor 
     *  
     */
	private static String formataPara2CasasDecimais(double valor) {

		double valorParaConversao;

		if (valor <= Long.MAX_VALUE){
			// Evitando o arredondamento...
			double valorTemp = (long) (valor * 100);
			valorParaConversao = (valorTemp / 100);	
		}		
		else{
			valorParaConversao = valor;
		}

		NumberFormat formatadorDeNumeros = NumberFormat.getInstance();
		formatadorDeNumeros.setMaximumFractionDigits(2);
		formatadorDeNumeros.setMinimumFractionDigits(2);
		return formatadorDeNumeros.format(valorParaConversao);
	}
	
	/**
     * Retorna o tempo em segundos da diferença entre as datas informadas como parâmetro
     * 
     * @param Date dataFinal 
     * @param Date dataInicial
     *  
     */
	public static long diferencaDatasEmSegundos(Date dataFinal, Date dataInicial) {

		if ((dataFinal == null) || (dataInicial == null)) return 0;

		return ((dataFinal.getTime() - dataInicial.getTime()) / 1000);
	}
	
	/**
     * Realiza a configuração proxy para o contexto do processo atual...
     * 
     * @param String proxyHost 
     * @param String proxyPort
     * @param String proxyUser
     * @param String proxyPassword
     *  
     */
//	public static void configureProxy(String proxyHost, String proxyPort, String proxyUser, String proxyPassword) {
//		System.getProperties().put("http.proxyHost", proxyHost);
//		System.getProperties().put("http.proxyPort", proxyPort);
//		System.getProperties().put("http.proxyUser", proxyUser);
//		System.getProperties().put("http.proxyPassword", proxyPassword);		
//	}
	
	/**
	 * Recebe string no formato bd (yyyy-MM-dd HH:mm:ss) e converte para TJDataHora
	 * 
	 * @author Márcio Mendonça Gomes
	 * @return Calendar
	 * 
	 * @param String valor
	 *  
	 */
	public static TJDataHora BancoTJDataHora(String valor) {		
		int[] dataDesdobrada = desdobraData(valor);
		
		if (!(dataDesdobrada == null)){
			return new TJDataHora(obtenhaCalendar(dataDesdobrada[0], dataDesdobrada[1], dataDesdobrada[2], 
					dataDesdobrada[3], dataDesdobrada[4], dataDesdobrada[5]));
		}
		
		return null;
	}
	
	/**
	 * Recebe string no formato yyyy-MM-dd HH:mm:ss e converte para um array de long
	 * 
	 * @author Márcio Mendonça Gomes
	 * @return int[] = {ano, mes, dia, hora, minuto, segundo}
	 */
	public static int[] desdobraData(String valor) {
		int ano = 0;
		int mes = 0;
		int dia = 0;
		int hora = 0;
		int minuto = 0;
		int segundo = 0;
		
		if ((valor == null) || (valor.trim().equalsIgnoreCase("")) || (valor.length() < 10)) return null;
		
		ano = Integer.valueOf(valor.trim().substring(0,4));
		mes = Integer.valueOf(valor.trim().substring(5,7));
		dia = Integer.valueOf(valor.trim().substring(8,10));
		
		if (valor.trim().length() >= 19){
			hora = Integer.valueOf(valor.trim().substring(11,13));
			minuto = Integer.valueOf(valor.trim().substring(14,16));
			segundo = Integer.valueOf(valor.trim().substring(17,19));
		}	
		
		return new int[] { ano, mes, dia, hora, minuto, segundo };
	}
	
	/**
     * Retorna o caminho completo da imagem utilizada como logo em relatórios
     * 
     * @param String diretorioProjeto
     *  
     */
	public static String getCaminhoLogotipoRelatorio(String diretorioProjeto) {
		return diretorioProjeto + "imagens" + File.separator + "logoEstadoGoias02.jpg";		
	}
	
	/**
     * Retorna o caminho completo dos arquivos .jasper
     * 
     * @param String diretorioProjeto
     *  
     */
	public static String getPathRelatorio(String diretorioProjeto) {
		return diretorioProjeto + "WEB-INF" + File.separator + "relatorios" + File.separator;
	}
	
	/**
     * Retorna o caminho completo do arquivo .jasper passado como parâmetro
     * 
     * @param String diretorioProjeto
     * @param String nomeDoRelatorio
     *  
     */
	public static String getPathRelatorio(String diretorioProjeto,
			                              String nomeDoRelatorio) {
		return getPathRelatorio(diretorioProjeto) + nomeDoRelatorio;
	}
	
	/**
	 * Método para formatar a data no estilo: ddmmyyyy, retirando as barras.
	 * @param String valor - no formato dd/mm/yyyy
	 * @return String
	 * @author hmgodinho
	 */
	public static String FormatarDataSemBarra(String valor) {
		String stRetorno = "";
		valor = Funcoes.TelaData(valor);
		if( valor != null ) {
			if( !valor.equals("") ) {
				stRetorno = valor.substring(0, 2) + valor.substring(3, 5) + valor.substring(6);
			}
		}
		
		return stRetorno;
	}
	
	public static boolean Contendo(String a, String b)
    {
		if (a == null || b == null || a.trim().length() == 0 || b.trim().length() == 0) return false;
        return RemoveAcentos(a).contains(RemoveAcentos(b));
    }
	
	public static boolean Iniciando(String a, String b)
	{
		if (a == null || b == null || a.trim().length() == 0 || b.trim().length() == 0) return false;
		 return RemoveAcentos(a).startsWith(RemoveAcentos(b));
	}
	
	public static String RemoveAcentos(String text)
	{
	    if (text == null) return null;
	
	    String acentos = "çÇáéíóúýÁÉÍÓÚÝàèìòùÀÈÌÒÙãõñäëïöüÿÄËÏÖÜÃÕÑâêîôûÂÊÎÔÛºª°ø§";
	    String semAcento = "cCaeiouyAEIOUYaeiouAEIOUaonaeiouyAEIOUAONaeiouAEIOU     ";
	
	    for (int i = 0; i < acentos.length(); i++)
	    {
	    	text = text.replaceAll(String.valueOf(acentos.charAt(i)), String.valueOf(semAcento.charAt(i)));
	    }
	
	    return text;
	}
	
	/**
	 * Converte um DataHandler para array de bytes.
	 * @param dh
	 *            DataHandler
	 * @return array de bytes.
	 */
	public static byte[] converterParaBytes(DataHandler dh) {
		byte[] resultado = null;
		try {
			if (dh != null && dh.getInputStream() != null) {
				InputStream is = dh.getInputStream();
				resultado = converterParaBytes(is);
			}
		} catch (IOException e) {
		}

		return resultado;
	}
	
	/**
	 * Converte um InputStream para array de bytes.
	 * @param is
	 *            InputStream
	 * @return array de bytes.
	 */
	public static byte[] converterParaBytes(InputStream is) {
		byte[] resultado = null;
		try {
			resultado = IOUtils.toByteArray(is);
		} catch (IOException e) {
		}

		return resultado;
	}
	
	/**
	 * Converte um array de bytes em um DataHandler.
	 * @param bytes Array de bytes.
	 * @return DataHandler.
	 */
	public static DataHandler converterParaDataHandler(byte[] bytes) {
		return converterParaDataHandler(bytes, MIME_OCTET_STREAM);
	}
	
	/**
	 * Converte um array de bytes em um DataHandler.
	 * 
	 * @param bytes Array de bytes.
	 * @param mimeType Mime type do tipo do dado.
	 * @return DataHandler.
	 */
	public static DataHandler converterParaDataHandler(byte[] bytes, String mimeType) {
		DataHandler resultado = null;
		
		if (bytes != null && mimeType != null) {
			resultado = new DataHandler(new ByteArrayDataSource(bytes, mimeType));
		}
			
		return resultado;
	}
	
	/**
	 * Retorna a quantidade de bytes do DataHandler.
	 * 
	 * @param dh DataHandler
	 * @return quantidade de bytes.
	 */
	public static int getTamanho(DataHandler dh) {
		int resultado = 0;
		byte[] bytes = converterParaBytes(dh);
		if (bytes != null) {
			resultado = bytes.length;
		}
		return resultado;
	}
	
	/**
	 * Método para retirar casas decimais acima de 2 casas, utilizado para guias, onde se converte o valor de UFR para reais.
	 * Arredondando
	 * @throws Exception
	 */
	public static Double retirarCasasDecimais(Double valor) {
		if( valor != null && valor > 0.0D ) {
			BigDecimal bd = new BigDecimal(valor);
			bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
			//bd = bd.setScale(2);
			//Math.floor(totalGuia * 100) / 100
			
			valor = bd.doubleValue();
		}
		
		return valor;
	}
	
	/**
	 * Método para retirar casas decimais acima de 2 casas, utilizado para guias, onde se converte o valor de UFR para reais.
	 * Truncando
	 * @throws Exception
	 */
	public static Double retirarCasasDecimaisTruncando(Double valor) {
		if( valor != null && valor > 0.0D ) {
			BigDecimal bd = new BigDecimal(valor);
			bd = bd.setScale(2, BigDecimal.ROUND_DOWN);
			valor = bd.doubleValue();
		}
		
		return valor;
	}
	
	public static String primeiroNome(String nomeCompleto) {
		String[] stNomes = nomeCompleto.split(" ");
		
		if (stNomes.length > 0) return stNomes[0];
		
		return "";
	}
	
	/**
	 * Método para verificar se a data final é maior que a data inicial.
	 * @param String dataInicial
	 * @param String dataFinal
	 * @return boolean
	 * @throws Exception 
	 */
	public static boolean validaDataInicialMenorDataFinal(String dataInicial, String dataFinal) throws Exception {
		boolean retorno = false;
		
		if( dataInicial != null && dataFinal != null ) {
			Date dInicial = FORMATTER.parse(dataInicial);
			Date dFinal = FORMATTER.parse(dataFinal);
			
			if( dInicial.getTime() < dFinal.getTime() ) {
				retorno = true;
			}
		}
		
		return retorno;
	}
	
	/**
	 * Método para verificar se a data é menor que a data atual.
	 * @param String data (dd/mm/yyyy)
	 * @return boolean
	 * @throws Exception
	 */
	public static boolean isDataMenorDataAtual(String data) throws Exception {
		boolean retorno = false;
		
		if( data != null ) {
			Date dataParametro = FORMATTER.parse(data);
			
			Date hoje = Calendar.getInstance().getTime();
			
			if( dataParametro.getTime() < hoje.getTime() ) {
				retorno = true;
			}
		}
		
		return retorno;
	}
	
	/**
	 * Método para verificar se a data é menor que a data atual.
	 * @param String data (dd/mm/yyyy)
	 * @return boolean
	 * @throws Exception
	 */
	public static boolean isDataMaiorIgualDataAtual(String data) throws Exception {
		boolean retorno = false;
		
		if( data != null ) {
			Date dataParametro = FORMATTER.parse(data);
			
			Date hoje = Funcoes.calculePrimeraHora(Calendar.getInstance().getTime());
			
			if( dataParametro.getTime() >= hoje.getTime() ) {
				retorno = true;
			}
		}
		
		return retorno;
	}
	
	/**
	 * Método para calcular a diferença das datas em anos e meses.
	 * Por exemplo, pode retornar 1,7(1 ano e 7 meses) ou 2,11(2 anos e 11 meses)
	 * @param String dataInicial
	 * @param String dataFinal
	 * @return String 
	 */
	public static String getDiferencaDatasEmAnosMeses(String dataInicial, String dataFinal) throws Exception {
		String retorno = "0,0";
		
		if( dataInicial != null && dataFinal != null ) {
			
			Date dInicial = FORMATTER.parse(dataInicial);
			Date dFinal = FORMATTER.parse(dataFinal);
			
			Calendar calendarInicial = Calendar.getInstance();
			calendarInicial.setTime(dInicial);
			
			Calendar calendarFinal = Calendar.getInstance();
			calendarFinal.setTime(dFinal);
			
			int diferencaMesesDataInicial = (calendarInicial.get(Calendar.YEAR) * 12) + calendarInicial.get(Calendar.MONTH);
			int diferencaMesesDataFinal = (calendarFinal.get(Calendar.YEAR) * 12) + calendarFinal.get(Calendar.MONTH);
			
			int totalMeses = diferencaMesesDataFinal - diferencaMesesDataInicial;
			
			int quantidadeAnos = 0;
			int quantidadeMeses = 0;
			
			if( totalMeses < 12 ) {
				quantidadeAnos = 0;
				quantidadeMeses = totalMeses;
			}
			else {
				if( totalMeses == 12 ) {
					quantidadeAnos = 0;
					quantidadeMeses = 12;
				}
				else {
					if( totalMeses > 12 ) {
						quantidadeAnos = totalMeses / 12;
						quantidadeMeses = totalMeses - (quantidadeAnos*12);
					}
				}
			}
			
			retorno = String.valueOf(quantidadeAnos) + "," + String.valueOf(quantidadeMeses);
		}
		
		return retorno;
	}
	
	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN, Pattern.CASE_INSENSITIVE);
	
	public static boolean validarEmail(String email) {
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}
	
	/**
	 * Os caracteres de controles são os de codigo inferio a 32
	 * sendo assim nos campos to tipo string estes devem ser retirados,
	 * estes tambem são conhecidos como carateres não imprimiveis
	 * @param string
	 * @return string
	 * jesus rodrigo
	 * 21/09/2016
	 */
	public static String removerCaracteresControleEspeciais(String str) {
		StringBuffer buf = new StringBuffer();
		char[] c = str.toCharArray();		
		for (int i = 0; i < c.length; i++) {						
			if (c[i] >= 32 ) {
				buf.append(c[i]);
			}
		}
		return new String(buf);
	}

	
	public static String retorneVazio(String valor) {
		if(null == valor ||  valor.equalsIgnoreCase("null")){
			valor = "";
		}
		return valor;
	}
	
	/**
	 * Returna o último tipo da extenção do arquivo.
	 * @param nomeArquivo
	 * @return retorna o tipo do arquivo pdf, jpg ou html se existir. 
	 * Vazio se não for nenhum dos 3 tipos
	 */
	public static String retorneExtencaoDoArquivo(String nomeArquivo){
		String _nomeArquivo = nomeArquivo.toLowerCase().trim();
		int ihtml = _nomeArquivo.lastIndexOf("html");
		int ijpg = _nomeArquivo.lastIndexOf("jpg");
		int ipdf = _nomeArquivo.lastIndexOf("pdf");		
		if (ihtml > ipdf && ihtml > ijpg){
			return _nomeArquivo.substring(ihtml, ihtml + 4);
		} else if (ipdf > ijpg){
			return _nomeArquivo.substring(ipdf, ipdf + 3);
		} else if (ijpg > -1){
			return _nomeArquivo.substring(ijpg, ijpg + 3);
		} else {
			return "";
		}
	}
	
	public static boolean guiaEmissaoPossuiLocomocao(List listaGuiaEmissaoDt) {
		
		if (listaGuiaEmissaoDt == null) return false;
		
		for(Object guiaEmissaoObj : listaGuiaEmissaoDt) {
			GuiaEmissaoDt guiaEmissaoTemp = (GuiaEmissaoDt) guiaEmissaoObj;
			if (guiaEmissaoTemp.getListaGuiaItemDt() != null && Funcoes.guiaItemPossuiLocomocao(guiaEmissaoTemp.getListaGuiaItemDt())) return true;
		}
		
		return false;
	}
	
	public static boolean guiaItemPossuiLocomocao(List listaGuiaItemDt) {
		
		if (listaGuiaItemDt == null) return false;
		
		for(Object guiaItemObj : listaGuiaItemDt) {
			GuiaItemDt guiaItemTemp = (GuiaItemDt) guiaItemObj;
			if (guiaItemTemp.getLocomocaoDt() != null) return true;
		}
		
		return false;
	}
	
	// metodo que Converter ISO-8859-1 para UTF-8 - Devido a acentuação nas palavras, pois no banco postgres está ISO-8859-1, jlsilva 02/03/2016.
	public static String ISO8859_UTF8(String string) {
		try {
			byte array[] = string.getBytes("ISO-8859-1");
			string = new String(array, "UTF-8");
			
			return string;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	// metodo que Converter UTF-8 para ISO-8859-1 - Devido a acentuaçãoo nas palavras, pois no banco postgres está ISO-8859-1, jlsilva 02/03/2016.
	public static String UTF8_ISO8859(String string) {
		try {
			byte array[] = string.getBytes("UTF-8");
			string = new String(array, "ISO-8859-1");
			
			return string;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	public static String FormataNome(String valor) {
		if (valor == null) return "";		
		return WordUtils.capitalizeFully(valor);
	}

	public static String removeZerosEsquerda(String numero) {
		if(numero == null)
			return null;
		return Integer.toString(Integer.parseInt(numero));
	}
	
	/**
	 * Utiliza algoritmo AES (Advanced Encryption Standard) e uma chave
	 * fixa para embaralhar informações que serão incorporadas a links
	 * e enviadas para o e-mail de usuários, tornando-as assim ilegíveis.
	 * A string criptografada é "URL safe". Posteriormente, ao ser interpretada
	 * pelo sistema, a informação pode ser desembaralhada utilizando o método
	 * aesDecrypt(String texto).
	 * 
	 * @author hrrosa
	 * @since 20/02/2017
	 * 
	 * @param texto
	 * @return String
	 * @throws Exception
	 **/
	public static String aesEncrypt(String texto) throws Exception {
		byte[] encrypted = null;
			
		Key aesKey = new SecretKeySpec(CHAVE_AES.getBytes(), "AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, aesKey);
		encrypted = cipher.doFinal(texto.getBytes());
		encrypted = Base64.encodeBase64URLSafe(encrypted);
		
		return new  String(encrypted);
	}
	
	/**
	 * Desembaralha strings que foram embaralhadas utilizando o método aesEncrypt()
	 * deste mesmo arquivo. Se não for possível desembaralhar por algum erro de formato
	 * da String, retorna null.
	 * 
	 * @param texto
	 * @return String
	 * @throws Exception
	 */
	public static String aesDecrypt(String texto) throws Exception {
		String decrypted = null;
		byte b[];
		
		Key aesKey = new SecretKeySpec(CHAVE_AES.getBytes(), "AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, aesKey);
		b = Base64.decodeBase64(texto);
		//Para decifrar o tamanho da String precisa ser múltiplo de 16
		if(b.length%16 == 0) {
			decrypted = new String(cipher.doFinal(b));
		}
	        
		return decrypted;
	}
	
	public static String convertePalavraSimplificada(String nome){
	    String stNome =  " " + nome.toLowerCase() + " ";	    	    	    
	    
	    stNome = trocarLetras(stNome);	    	    
	    
	    stNome = stNome.replace(" ", "");
	    //pego somente os 30 primeiro caracteres
	    if (stNome.length()>60)  stNome= stNome.substring(0,60);
	    
	    return stNome;
	}
	
	
	/**
	 * Limita uma string para um tamanho específico 
	 * @param valor
	 * @param tamanho
	 * @return
	 */
	public static String limitarString(String valor, int tamanho){
		return (valor.length() > tamanho ? (valor.substring(0, tamanho).concat("...")) : valor);
	}
	
	
	/**
	 * Converte um array de bytes para o formato base64 e retorna como string 
	 * @param dados
	 * @return
	 */
	public static String byteArrayToBase64 (byte[] dados){
		return Base64.encodeBase64String(dados);		
	}
	
	
	/**
	 * Formata um nome atribuindo primeira letra maiúscula e demais minúsculas a todas as palavras.
	 * @param nome
	 * @return nomeCapitulado
	 * @author lsrodrigues
	 */
	public static String capitularNome(String nome){
		String nomeCapitulado = null;
		
		if(  nome != null && !(nome.equals(""))){
			
			nomeCapitulado = "";
			nome = nome.trim();
			
			nome = nome.toLowerCase();
			String array[] = nome.split("\\s+");
			
			int i = 0;
			
			while(i < array.length){					
				nomeCapitulado += array[i].substring(0,1).toUpperCase().concat(array[i].substring(1));
				i++;
				if(i != array.length) nomeCapitulado += " ";
			}
		}
		
		return nomeCapitulado;
	}
	
	public static String capitalizeNome(String nome){
		if (nome == null) return nome;		
		nome = WordUtils.capitalizeFully(nome);		
		nome = nome.replace(" Em ", " em ").replace(" Do ", " do ").replace(" Dos ", " dos ").replace(" Da ", " da ").replace(" Das ", " das ").replace(" De ", " de ").replace(" E ", " e ");
        return nome;
    }
	
	/**
	 * Converte um objeto do tipo Date para XMLGregorianCalendar
	 * @param date
	 * @return
	 */
	public static XMLGregorianCalendar asXMLGregorianCalendar(Date date) {	
		try {
			DatatypeFactory df = DatatypeFactory.newInstance();
			if (date == null) {
				return null;
			} else {
				GregorianCalendar gc = new GregorianCalendar();
				gc.setTimeInMillis(date.getTime());
				return df.newXMLGregorianCalendar(gc);
			}
		} catch (DatatypeConfigurationException e) {
			throw new IllegalStateException("Error while trying to obtain a new instance of DatatypeFactory", e);
		}
	}
	
	/**
	 * Converte um objeto do tipo String (dd/mm/aaaa) para XMLGregorianCalendar
	 * @param date
	 * @return
	 */
	public static XMLGregorianCalendar asXMLGregorianCalendar(String date) throws Exception {		
		return ValidacaoUtil.isNaoVazio(date) ? asXMLGregorianCalendar(StringToDate(date)) : null;
	}

	/**
	 * Converte um objeto do tipo XMLGregorianCalendar para Date
	 * @param xmlGC
	 * @return
	 */
	public static Date asDate(XMLGregorianCalendar xmlGC) {
		if (xmlGC == null) {
			return null;
		} else {
			return xmlGC.toGregorianCalendar().getTime();
		}
	}	
	
	public static void validarString(String valor) throws Exception {
		
		if(valor == null) {
			return;
		}
		
		String stRetorno = valor.toLowerCase();

		String[] stPalavrasChaves = { "onblur(", "onclick(", "ondblclick(", "onfocus(", "onkeydown(", "onkeypress(", "onkeyup(", "onmousedown(", "onmousemove(", "onmouseout(", "onmouseover(", "onmouseup(", "onload(", "<script" };

		for (int i = 0; i < stPalavrasChaves.length; i++){
			if (stRetorno.indexOf(stPalavrasChaves[i]) != -1) 
				throw new Exception("<{Tentativa de utilização de palavra não permitida.}> Local Exception: ): Tentativa de utilização de palavra não permitida: " + stPalavrasChaves[i].substring(0, 2) + "_" + stPalavrasChaves[i].substring(2, stPalavrasChaves[i].length()));
		}
		
	}
	
	public static String leiaArquivoCompleto(String path, Charset encoding) throws IOException 
	{
	    byte[] encoded = Files.readAllBytes(Paths.get(path));
	    return encoding.decode(ByteBuffer.wrap(encoded)).toString();
	}
	
	public static boolean isNotStringVazia(String valor) 
	{
		return !isStringVazia(valor);
	}
	
	public static boolean isStringVazia(String valor) 
	{
		return valor == null || valor.trim().isEmpty() || valor.trim().length() == 0;
	}
	
	public static boolean isStringDiferente(String valor1, String valor2) 
	{
		return !isStringIgual(valor1, valor2);
	}
	
	public static boolean isStringIgual(String valor1, String valor2) 
	{
		if (valor1 == null && valor2 == null) return true;
		
		if (valor1 == null || valor2 == null) return false;
		
		return valor1.trim().equalsIgnoreCase(valor2.trim());
	}
	
	/**
	 * 
	 * Método criado após contato com a Talita do suporte que relatou problemas em processos que estão sendo 
	 * recadastrados no PJD. Ao inves de digitalizar, estão recadastrando os processos e com isso as guias antigas 
	 * não são reconhecidas.
	 * O algoritmo foi criado com base nos dados do SPG passado pelo Júnior Feitosa.
	 * Segue o algoritmo recebido para documentar:
	 * 
	  	1440 DEFINE SUBROUTINE GERA-DIGITO
		1450 *****************************
		1460 *
		1470 #TOT  := 0
		1480 #MULT := 2
		1490 *
		1500 FOR #I 11 TO 1 STEP -1
		1510   #TOT := #TOT + (#MULT * #DIG(#I))
		1520   IF #MULT = 10
		1530     #MULT := 2
		1540   ELSE
		1550     ADD 1 TO #MULT
		1560   END-IF
		1570 END-FOR
		1580 *
		1590 #QUOC  := #TOT / 11
		1600 #RESTO := #TOT - (#QUOC * 11)
		1610 IF #RESTO = 0 OR = 1
		1620   #DV := 0
		1630 ELSE
		1640   #DV := 11 - #RESTO
		1650 END-IF
		1660 *
		1670 END-SUBROUTINE
	 * 
	 * @param String numeroBase
	 * @return String
	 */
	public static String gerarDigitoNumeroProcessoAntigoSPG(String numeroBase) {
		String retorno = null;
		
		if( numeroBase != null && numeroBase.length() == 11 ) {
			int total = 0;
			int multiplicacao = 2;
			
			for( int i = 11; i > 0; i-- ) {
				total = total + (multiplicacao * Funcoes.StringToInt(numeroBase.substring(i-1, i)));
				
				if( multiplicacao == 10 ) {
					multiplicacao = 2;
				}
				else {
					multiplicacao++;
				}
			}
			
			int quociente = total / 11;
			int resto = total % 11;
			
			if( resto == 0 || resto == 1 ) {
				retorno = "0";
			}
			else {
				retorno = String.valueOf(11 - resto);
			}
		}
		
		return retorno;
	}

	/**
	 * Recebe uma string e retorna o texto entre a primeira ocorrência das duas tags especificadas.
	 * Caso não encontre, retorna null; 
	 * @param entrada
	 * @param tagInicio
	 * @param tagFim
	 * @return
	 * @author hrrosa
	 */
	public static String retornarTextoEntreTags(String entrada, String tagInicio, String tagFim)
	{
		String textoRetorno = null;
		if(entrada != null && tagInicio != null && tagFim != null) {
			int indexTagInicio = entrada.indexOf(tagInicio) + tagInicio.length();
			int indexTagFim = entrada.indexOf(tagFim);
			if(indexTagInicio > 0 && indexTagFim > 0 && indexTagInicio < indexTagFim){
				textoRetorno = entrada.substring(indexTagInicio, indexTagFim);
			}
		}
		return textoRetorno;
	}
	public static String processoNovaNumeracao(String numeroProcesso) {
		String[] novaNumeracao = numeroProcesso.split("\\.");
		return novaNumeracao[0];
		
	}
	
	public static boolean objectToBoolean(Object o) {
		return o instanceof Boolean && ((boolean) o);
	}
	
	/**
	 * Retira o excesso de espaço em branco, tabulações e 
	 * outros caracteres de tabulação entre as palavras do texto.
	 * @param value - texto que será normalizado
	 * @return
	 */
	public static String normalizarEspacoEmBrancoEntreTexto (String value){
		StringBuilder buffer = new StringBuilder();
		String[] tokens = value.split("\\h");		
		for (String s : tokens){
			if (!s.trim().isEmpty()){
				buffer.append(s.trim() + " ");
			}
		}
		return buffer.toString().trim();
	}
	
	public static String normalizeText (String value){
		StringBuilder buffer = new StringBuilder();
		String[] tokens = value.split("\\h");		
		for (String s : tokens){
			if (!s.trim().isEmpty()){
				buffer.append(s.trim() + " ");
			}
		}
		return buffer.toString().trim();
	}
	
	public static String obtenhaPrimeirosCarctereString(String valor, int quantidadeCaracteres) {
		if (valor == null || valor.trim().length() <= quantidadeCaracteres) return valor;
		return valor.substring(0, quantidadeCaracteres);
	}
	
	/**
	 * Função que cria um link para a página na tabela de indices de um arquivo PDF
	 */
	public static HashMap<String, Object> addLink (String title, int page){
		HashMap<String, Object> link = new HashMap<String, Object>();
		link.put("Title", title);
		link.put("Action", "GoTo");
		link.put("Page", String.format("%d Fit", page));		
		return link;
	}
	
	// jvosantos - 26/08/2019 16:46 - Mover função do PJD para PROJUDI para usar
	public static String formatarProcessoDigito(String processo) throws Exception {
		if(processo.length() == 10 && processo.contains(".")) return processo;
		
		int indexOf = processo.indexOf('.');
		
		if(indexOf == -1) {
			//throw new Exception("Número de Processo sem '.', (N. Processo: \""+processo+"\", Tamanho: \""+processo.length()+"\")");
			return processo; // Temporário
		}
		
		String numero = processo.substring(0, indexOf);
		String digito = processo.substring(indexOf+1);

		digito = completarZeros(digito, 2);
		numero = completarZeros(numero, 7);
		
		return numero + '.' + digito;
		
	}

	public static float StringToFloat(String probabilidadea) {
		float valor=0;
		try{
			valor = Float.parseFloat(probabilidadea);
		}catch (Exception e) {
			valor=0;
		}
		return valor;
	}	

	//lrcampos - 10/12/2019 11:45 - parse de data para LocalDatetime
	public static LocalDateTime parseData(String data) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
		return LocalDateTime.parse(data.substring(0, 16), formatter);
	}
	
	//jsantonelli - 16/10/2019 13:36 - implantação funcionalidade classificadores
	public static String formatarCallbackURL(HttpServletRequest request) {
		String rawURL = (String) request.getAttribute("callbackURL");
		String urlEncode = java.util.Base64.getUrlEncoder().encodeToString(rawURL.getBytes());
		String hexCode = DatatypeConverter.printHexBinary(urlEncode.getBytes());
		return hexCode;
	}
	
	//jsantonelli - 17/10/2019 11:36 - implantação funcionalidade classificadores
	public static String filterCallbackURL(String queryString) {
		//jsantonelli - 05/11/2019 12:27 - correção de null pointer
		if(queryString == null) return StringUtils.EMPTY;
		
		String[] queryParameters = queryString.split("&");
		return Stream.of(queryParameters)
				.filter(a -> !a.contains("MensagemOk="))
				.filter(a -> StringUtils.isNotEmpty(a))
				.reduce("", (a,b) -> a+"&"+b);
	}

	// alsqueiroz - 01/10/2019 15:39 - Pegar data e hora atual
	public static String getDataHoraAtual() {
		LocalDateTime agora = LocalDateTime.now();

		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/uuuu HH:mm:ss");

		return dateTimeFormatter.format(agora);
	}
	
	/**
	 * Metodo para buscar o dia util anterior de uma data
	 * @since 10/12/2019 : 15:27
	 * @author lrcampos
	 * @param data, idProcesso, prazo
	 **/
	public static String removeDiasUteis(String data, String idProcesso, Integer prazo) throws Exception {
		FabricaConexao obFabricaConexao = null;
		try {
			ProcessoDt procDt = new ProcessoNe().consultarId(idProcesso);
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			PrazoSuspensoNe prazoSuspensoNe = new PrazoSuspensoNe();
			ServentiaDt serventiaDt = new ServentiaNe().consultarId(procDt.getId_Serventia());
			return prazoSuspensoNe.getAnteriorDataValidaPrazoCorridoPJD(data, prazo, serventiaDt,
					obFabricaConexao);
		} finally {
			obFabricaConexao.fecharConexao();
		}
	}
	
	/**
	 * Método que faz a conversão dos caracteres HTML em caracteres especiais do Português.
	 * @param htmlString - string html com as tags
	 * @return texto com as tags convertidas
	 * @author hmgodinho
	 */
	public static String converterCaracteresHTMLEmCaracteresEspeciais(String htmlString) {
		try {
			htmlString = htmlString.replace("&Agrave;", "À").replace("&Aacute;", "Á").replace("&Acirc;", "Â").replace("&Atilde;", "Ã").replace("&agrave;", "à")
					.replace("&aacute;", "á").replace("&acirc;", "â").replace("&atilde;", "ã").replace("&Ograve;", "Ò").replace("&Oacute;", "Ó").replace("&Ocirc;", "Ô")
					.replace("&Otilde;", "Õ").replace("&ograve;", "ò").replace("&oacute;", "ó").replace("&ocirc;", "ô").replace("&otilde;", "õ").replace("&Egrave;", "È")
					.replace("&Eacute;", "É").replace("&Ecirc;", "Ê").replace("&egrave;", "è").replace("&eacute;", "é").replace("&ecirc;", "ê").replace("&Igrave;", "Ì")
					.replace("&Iacute;", "Í").replace("&igrave;", "ì").replace("&iacute;", "í").replace("&Ugrave;", "Ù").replace("&Uacute;", "Ú").replace("&ugrave;", "ù")
					.replace("&uacute;", "ú").replace("&Ccedil;", "Ç").replace("&ccedil;", "ç").replace("&circ;", "^").replace("&tilde;", "~").replace("&167;", "º")
					.replace("&166;", "ª").replace("&ordm;", "º").replace("&ordf;", "ª").replace("&deg;", "°");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return htmlString;
	}

	public static String retirarTagsHTMLTexto(String textoCert) {
		
		textoCert = textoCert.replace("<p>", "");
		textoCert = textoCert.replace("</p>", "");			

		return textoCert;
	}
	
	public static boolean isPendenciaSessaoVirtualNaoDescartar(String pendenciaTipoCodigo) {
		int pendenciaTipoCodigoInt = Funcoes.StringToInt(pendenciaTipoCodigo, 0);
		
		if (pendenciaTipoCodigoInt == PendenciaTipoDt.VERIFICAR_IMPEDIMENTO) return true;
		
		if (pendenciaTipoCodigoInt == PendenciaTipoDt.VERIFICAR_IMPEDIMENTO_VOTANTES) return true;
		
		if (pendenciaTipoCodigoInt == PendenciaTipoDt.VERIFICAR_RESULTADO_VOTACAO) return true;
		
		if (pendenciaTipoCodigoInt == PendenciaTipoDt.VERIFICAR_IMPEDIMENTO_MP) return true;
		
		if (pendenciaTipoCodigoInt == PendenciaTipoDt.VOTO_SESSAO) return true;
		
		if (pendenciaTipoCodigoInt == PendenciaTipoDt.PROCLAMACAO_VOTO) return true;
		
		if (pendenciaTipoCodigoInt == PendenciaTipoDt.PEDIDO_SUSTENTACAO_ORAL_DEFERIDO) return true;
		
		if (pendenciaTipoCodigoInt == PendenciaTipoDt.PEDIDO_SUSTENTACAO_ORAL_INDEFERIDA) return true;
		
		if (pendenciaTipoCodigoInt == PendenciaTipoDt.APRECIADOS) return true;
		
		return false;
	}

	public static void preencheUsuarioLog(Dados dados, UsuarioDt usuarioDt) {
		dados.setId_UsuarioLog(usuarioDt.getId());
		dados.setIpComputadorLog(usuarioDt.getIpComputadorLog());
	}
	
	
	/**
	 * Verifica se o arquivo tem apenas uma pagina e com tamanho perto do máximo.
	 * Com isso, gera uma exceção de arquivo mal formado.
	 * @param conteudo
	 * @throws Exception
	 */
	public static void validarTamanhoPadraoArquivo (byte[] conteudo) throws Exception {
		
		//tamanho maximo de pdf 6 mb
		final int TAMANHO_MAXIMO= 6291456;
		final int DOIS_MEGAS= 2097152;
		final int DUZENTOS_KB= 204800;
		PdfReader reader = new PdfReader(conteudo);
		int nPages = reader.getNumberOfPages();			
		long tamanho = reader.getFileLength();		
		reader.close();		
		if ( tamanho > TAMANHO_MAXIMO) {
			throw new MensagemException("\n\n O tamanho máximo de arquivo permitido é 6 MB.");
		} else {
			// if (tamanho > 2 * HUM_MEGA && (nPages * (200 * HUM_KB) >= tamanho)){
			if (tamanho > DOIS_MEGAS && tamanho < TAMANHO_MAXIMO){
				// Apenas considerar o tamanho médio da página em arquivos maiores de 2 MB.
				long tamanhoPorPagina = tamanho / nPages;
				if (tamanhoPorPagina > DUZENTOS_KB){
					throw new MensagemException("\n\n Este arquivo possui páginas de tamanho médio de " + 
							Math.floor(tamanhoPorPagina / 1024) + " KB, acima do limite máximo (200 KB) permitido. " +  
							"Rever a qualidade do escaneamento ou edição do arquivo.");
				}			
			}			
		}
	}
	
	public static String getIdServentiaCargo(UsuarioDt usuario) {
		return StringUtils.defaultIfEmpty(usuario.getId_ServentiaCargoUsuarioChefe(), usuario.getId_ServentiaCargo());
	}			

	/**
	 * Valida se existe pelo menos uma página no arquivo que contém texto puro
	 * Importante porque o texto do arquivo é usado para pesquisas no elasticsearch e publicação
	 * @param conteudo
	 * @return
	 * @throws Exception
	 */
	public static void validarSeConteudoEhTexto (String IdArquivoTipo, byte[] conteudo) throws Exception {		
		switch (Integer.valueOf(IdArquivoTipo)){
			case ArquivoTipoDt.SENTENCA:
			case ArquivoTipoDt.DECISAO:
			case ArquivoTipoDt.DESPACHO:
			case ArquivoTipoDt.ACORDAO:
			case ArquivoTipoDt.EMENTA:
			case ArquivoTipoDt.RELATORIO_VOTO:
				PdfReader reader = new PdfReader(conteudo);
				PdfReaderContentParser parser = new PdfReaderContentParser(reader);
				SimpleTextExtractionStrategy strategy = null;
				for (int i = 1; i <= reader.getNumberOfPages(); i++){
					strategy = parser.processContent(i, new SimpleTextExtractionStrategy());
					if (ValidacaoUtil.isNaoVazio(strategy.getResultantText())){
						reader.close();
						return;
					}
				}
				reader.close();
				throw new MensagemException("\n\n Este arquivo não possui nenhum TEXTO que possa ser extraído.");				
		}
	}
	
	/**
	 * Valida se o arquivo PDF é do tipo PDF-A/1A ou PDF-A/1B 
	 * @param conteudo
	 * @throws Exception
	 */
	public static void validarSeArquivoEhTipoPDFA(byte[] conteudo)throws Exception {
		boolean isPdfA = false;
		try {
			PdfReader reader = new PdfReader(conteudo);			
	    	byte[] metadata = reader.getMetadata();
	    	if (metadata != null){	    		
	    		String value = new String(reader.getMetadata());
	    		isPdfA = isConformancePDFA1A(value) || isConformancePDFA1B(value);
	    	}
		} catch (Exception ex) {}		
    	if (!isPdfA) throw new MensagemException("\n\n Este arquivo não está em conformidade com o padrão Pdf-A.");
	}
	
	private static boolean isConformancePDFA1A (String value){
		return isConformancePDFA("A", value);
	}
	
	private static boolean isConformancePDFA1B (String value){
		return isConformancePDFA("B", value);
	}
		
	private static boolean isConformancePDFA(String type, String value) {
		return value.contains("pdfaid:conformance=\""+ type + "\"") || value.contains("<pdfaid:conformance>"+type+"</pdfaid:conformance>");
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T pegarNaSessaoOuCriar(HttpServletRequest request, String name, Class<T> class1) throws InstantiationException, IllegalAccessException {
		Object obj = request.getSession().getAttribute(name);
		
		if(obj == null || !obj.getClass().equals(class1))
			return class1.newInstance();
		
		return (T) obj;
	}

	public static void preencheLogDt(Dados dados, LogDt logDt) {
		dados.setId_UsuarioLog(logDt.getId_Usuario());
		dados.setIpComputadorLog(logDt.getIpComputador());
	}
	
	public static ProcessoDt setarNumeroProcesso(ResultSetTJGO rs, ProcessoDt dados) throws Exception {
		return setarNumeroProcesso(rs, dados, "PROC_NUMERO", "DIGITO_VERIFICADOR");
	}

	public static ProcessoDt setarNumeroProcesso(ResultSetTJGO rs, ProcessoDt dados, String colunaNumero, String colunaDV) throws Exception {
		return setarNumeroProcesso(dados, rs.getString(colunaNumero), rs.getString(colunaDV));
	}
	
	public static ProcessoDt setarNumeroProcesso(ProcessoDt dados, String numero, String dv) throws Exception {
		if(StringUtils.isBlank(numero) || StringUtils.isBlank(dv))
			throw new Exception("Processo: "+StringUtils.defaultString(dados.getId())+"; Numero = "+StringUtils.defaultString(numero, "null")+"; DV = "+StringUtils.defaultString(dv, "null"));
		
		dados.setProcessoNumero(formatarProcessoNumero(numero));
		dados.setDigitoVerificador(formatarProcessoDigitoVerificador(dv));
		
		return dados;
	}
	
	public static String formatarProcessoNumero(String numero) {
		return completarZeros(numero, 7);
	}

	public static String formatarProcessoDigitoVerificador(String dv) {
		return completarZeros(dv, 2);
	}
	
	public static long verificaDiferencaDatas(String dataInicial, String dataFinal) throws Exception {	
		try {
		    Date d1 = FORMATTER.parse(dataInicial);
		    Date d2 = FORMATTER.parse(dataFinal);
		    long diff = d2.getTime() - d1.getTime();
			return diff;
		} catch (Exception e) {
			throw new MensagemException("Erro ao Calcular a Diferença Entre Datas. Verificar as datas. Data Inicial: " + dataInicial + " - Data Final: " + dataFinal);
		}
	}
	
	public static boolean isZeroOuPositivo(long numero) throws Exception {	
		return numero >= 0L;
	}
	
	public static String formataNumeroCompletoProcessoNovo(String numeroCompletoProcesso) {
		if (numeroCompletoProcesso == null || numeroCompletoProcesso.trim().length() == 0) return "";
		
		numeroCompletoProcesso = completarZeros(obtenhaSomenteNumeros(numeroCompletoProcesso), 20).trim();
		
		long numero = Funcoes.StringToLong(numeroCompletoProcesso.substring(0, 7) ); 
		long digito  = Funcoes.StringToLong(numeroCompletoProcesso.substring(7, 9));
		long ano = Funcoes.StringToLong(numeroCompletoProcesso.substring(9, 13)); 
		long jtrParte1 = Funcoes.StringToLong((numeroCompletoProcesso.substring(13, 14))); 
		long jtrParte2 = Funcoes.StringToLong((numeroCompletoProcesso.substring(14, 16)));
		long forum = Funcoes.StringToLong(numeroCompletoProcesso.substring(16, 20));
		
		return String.format("%07d-%02d.%04d.%s.%02d.%04d", numero, digito, ano, jtrParte1, jtrParte2, forum); //"5000280-28.2010.8.09.0059";		
	}
	
	/**
	 * Método que retorna a data atual por extenso.
	 * @return data formatada
	 * @author hmgodinho
	 */
	public static String dataAtualPorExtenso() {
		Date data =  new Date();
		Locale local = new Locale("pt","BR");
		DateFormat formato = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy",local);
		return formato.format(data);
	}
	
	/**
	 * Método que recebe código de sexo e retorna a descrição.
	 * @param codigoSexo - M ou F
	 * @return descrição do sexo
	 * @author hmgodinho
	 */
	public static String getSexoDescricao(String codigoSexo) {
		String descricao = "";
		if(codigoSexo != null) {
			if(codigoSexo.equalsIgnoreCase("M")) {
				descricao = "Masculino";
			} else if(codigoSexo.equalsIgnoreCase("F") ) {
				descricao = "Feminino";
			}
		}
		return descricao;
	}

	public static String getDataArquivoCorreios(String nomeArquivo) {
		String[] aux = null;
		String dataArquivo = "";
		if(nomeArquivo.contains("ecibo") || nomeArquivo.contains("nconsistencia")) {
			aux = nomeArquivo.split("_"); // ecarta_17921_11_07082020124417_recibo.zip   ecarta_17921_262_18092020131245_inconsistencia.zip
			dataArquivo = aux[3].substring(0, 2) + "/" + aux[3].substring(2, 4) + "/" + aux[3].substring(4, 8);
		} else if(nomeArquivo.contains("evolucao")) {
			aux = nomeArquivo.split("_"); // ecarta_17921_1_devolucaoar_17082020042432.zip
			dataArquivo = aux[4].substring(0, 2) + "/" + aux[4].substring(2, 4) + "/" + aux[4].substring(4, 8);
		} else if(nomeArquivo.contains("inalizador")) {
			aux = nomeArquivo.split("_"); // ecarta_17921_1_rastreamento_antecipado_finalizador08082020225550.zip
			dataArquivo = aux[5].substring(11, 13) + "/" + aux[5].substring(13, 15) + "/" + aux[5].substring(15, 19);
		} else if(nomeArquivo.contains("ostado")) {
			aux = nomeArquivo.split("_"); // ecarta_17921_41_rastreamento_antecipado_postado07112020093327.zip
			dataArquivo = aux[5].substring(7, 9) + "/" + aux[5].substring(9, 11) + "/" + aux[5].substring(11, 15);
		}
		return dataArquivo;
	}

	public static String limparStringToJSON(String valor) {
		if(valor==null) {
			return "";
		}
		return valor.replaceAll("'","").replaceAll("\"","").replaceAll("\\\\","");		
	}
	
	public static String getIpCliente(HttpServletRequest request) {
        final String HEADER_X_FORWARDED_FOR = "X-FORWARDED-FOR";
        String stIps = "";
        String stTemp= "";        
        stIps =request.getHeader(HEADER_X_FORWARDED_FOR);
        if (stIps != null) {
            ////System.out.println("IPs...: " + stIps);            
            String[] vetIps = stIps.split(",");
            if (vetIps.length>0){           
                stTemp = vetIps[vetIps.length-1];
                ////System.out.println("IP Recuperado...: " + vetIps[vetIps.length-1]);  
            }
        }else{
            stTemp =request.getRemoteAddr();
        }
        return stTemp;
    }
	
	public static String obtenhaConteudoLog(Exception ex, UsuarioNe usuarioNe){
    	String dadosUsuarioLog = "Usuário: "+usuarioNe.getUsuarioDt().getUsuario() +" -- Perfil: "+ usuarioNe.getUsuarioDt().getGrupo();
    	try{
    		return dadosUsuarioLog+Funcoes.obtenhaConteudoPrimeiraExcecao(ex);
    	}catch(Exception e){
    		return ex.getMessage();
    	}    	
    }  
	
	public static String getMensagemExceptionUser(Exception ex) {
		return getMensagemExceptionUser(ex.getMessage());
	}
	
	public static String getMensagemExceptionUser(String mensagem) {
		String stMensagem = mensagem;
		if (stMensagem != null && stMensagem.length() > 0) {
		    Pattern paTeste01 = Pattern.compile("<\\{(.*?)\\}>");
	        Matcher maTeste01 = paTeste01.matcher(stMensagem);
	        stMensagem = "";
	        while (maTeste01.find()) {
	            stMensagem += maTeste01.group().replaceAll("(<\\{)|(\\}>)", "");
	        }
	    } else {
	    	stMensagem = "";
	    }
	    
	    if (stMensagem.trim().length() == 0) {
	    	stMensagem = mensagem;
	    }
	    return stMensagem;
	}
	
	/**
	 * Método que retornar o radical do CNPJ.
	 * @param cnpjCompleto - string com o CNPJ completo
	 * @return string com o radical
	 * @author hmgodinho	  
	 */
	public static String getRadicalCnpj(String cnpjCompleto) {
		String radical = "";
		cnpjCompleto = cnpjCompleto.replaceAll("\\.", "").replaceAll("/", "").replaceAll("-", "");
		if(!cnpjCompleto.isEmpty()){
			radical = cnpjCompleto.substring(0, cnpjCompleto.length() - 6);
		}
		radical = radical.trim();
		long cnpjRadical = Long.parseLong(radical);
		return String.valueOf(cnpjRadical);
	}

	public static boolean isSenhaFraca(String senha) {
		//remover quando for liberado a senha forte
//		return false;
		
		if(errosSenha(senha).equalsIgnoreCase("")) {
			return false;
		}
		return true;
	}
	
	public static String errosSenha(String senha) {
		String stErros = "";
		/*
		 * 	Mínimo 8 caracteres.
			Mínimo 1 maiúscula, 
			Mínimo 1 minúscula, 
			Mínimo 1 número e 1 caractere especial	
		 */
		
		if(senha==null || senha.isEmpty()) {
			stErros = "Senha não pode ser vazia";
			return stErros;	
		}
		
		if (senha.length()<8) {
			stErros +="A Senha deve ter no mínimo 8 caracteres\n";
		}
			    
	    boolean achouNumero = false;
	    boolean achouMaiuscula = false;
	    boolean achouMinuscula = false;
	    boolean achouSimbolo = false;
	    for (char c : senha.toCharArray()) {
	         if (c >= '0' && c <= '9') {
	             achouNumero = true;
	         } else if (c >= 'A' && c <= 'Z') {
	             achouMaiuscula = true;
	         } else if (c >= 'a' && c <= 'z') {
	             achouMinuscula = true;
	         } else {
	             achouSimbolo = true;
	         }
	    }
	    
	    if (!achouNumero) {
	    	stErros +="A Senha deve ter ao menos 1 Número.\n";
	    }	    
	    if(!achouMaiuscula) {
	    	stErros +="A Senha deve ter ao menos 1 Letra Maiúscula.\n";
	    }
	    if (!achouMinuscula) {
	    	stErros +="A Senha deve ter ao menos 1 Letra Minúscula.\n";
	    }
	    if(!achouSimbolo) {
	    	stErros +="A Senha deve ter ao menos 1 Símbolo.\n";
	    }
	    
		return stErros;				
	}

	public static String retiraEspacosInicioFim(String valor) {
		if (valor == null) return "";
		return valor.trim();
	}

	public static String LongToString(long valor) {
		String stTemp ="";
		try {
			stTemp = String.valueOf(valor);
		}catch (Exception e) {
			// TODO: handle exception
		}
		return stTemp;
	}
}
