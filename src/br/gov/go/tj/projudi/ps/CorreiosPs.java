package br.gov.go.tj.projudi.ps;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;

import br.gov.go.tj.projudi.dt.ArquivoDt;
import br.gov.go.tj.projudi.dt.ArquivoTipoDt;
import br.gov.go.tj.projudi.dt.CorreiosDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.ne.ArquivoNe;
import br.gov.go.tj.projudi.ne.LogNe;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class CorreiosPs extends Persistencia  {
	
	private static final long serialVersionUID = -8148915938303199982L;
	
	public CorreiosPs(Connection conexao){
		Conexao = conexao;
	}
	
	public CorreiosPs(){}
	
	/**
	 * Envia dois arquivos .zip, 1.Arquivo de Serviço(contendo as cartas) 2.Resposta à Notificação(aprovando a confecção)
	 * @param listaCartas cartas a serem enviadas
	 * @param LL número do lote
	 * @return true, caso tudo for enviado
	 * @throws Exception
	 */
	public boolean enviarArquivoServicoRespostaNotificacao(List<CorreiosDt> listaCartas, String LL)  throws Exception { 
		FTPClient ftp = new FTPClient();
		OutputStream outputStream;
		ZipEntry zipEntry;
		ZipOutputStream zipOutputStream;
		byte[] data;
		CorreiosDt correiosDt = null;
		String DDMMAAAAHHMMSS = "";
		String MM = CorreiosDt.MM_1_FOLHA;  	
		StringBuffer arquivoServico = new StringBuffer();
		LogNe logNe = new LogNe();
		LogDt logDt = null;
		StringWriter writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		boolean sucesso = false;

		try {
			ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(printWriter), true));
			ftp.setStrictReplyParsing(false);
			ftp.connect(CorreiosDt.URL_HOST, CorreiosDt.FTP_PORT);
			ftp.login(CorreiosDt.USERNAME, CorreiosDt.PASSWORD);
			ftp.enterLocalPassiveMode();
			ftp.setFileType(FTP.BINARY_FILE_TYPE);
			ftp.cwd("//e-carta/");
			
			//Arquivo de Serviço
			outputStream = ftp.storeFileStream("e-Carta_" + MM + "_" + LL + "_servico.zip");	//e-Carta_MM_LL_servico.zip
			zipOutputStream = new ZipOutputStream(outputStream, StandardCharsets.UTF_8);
			for (int i = 0; i < listaCartas.size(); i++) {
				try {
					correiosDt = listaCartas.get(i);
					correiosDt.setNumeroLote(LL);
					correiosDt.setMatrizRelacionamento(MM);
					correiosDt.setNomeArquivoComplementar("e-Carta_" + MM + "_" + LL + "_" + correiosDt.getIdPendencia() + "_1_complementar.pdf");
					arquivoServico.append(correiosDt.montarArquivoServico() + "\n");
					zipEntry = new ZipEntry(correiosDt.getNomeArquivoComplementar());
					zipOutputStream.putNextEntry(zipEntry);
					data = correiosDt.getArquivoComplementar();
					zipOutputStream.write(data, 0, data.length);
					zipOutputStream.closeEntry();
				} catch (Exception e) {
					logDt = new LogDt("JobEnviarCartasCorreios", "", UsuarioDt.SistemaProjudi, "Servidor",  String.valueOf(LogTipoDt.ExecucaoAutomatica), "\nID_PEND=" + correiosDt.getIdPendencia(), Funcoes.obtenhaConteudoPrimeiraExcecao(e));
					logNe.salvar(logDt);
					listaCartas.remove(i);
					i--;
				}
			}	
			
			zipEntry = new ZipEntry("e-Carta_" + MM + "_" + LL + "_servico.txt");				//e-Carta_MM_LL_servico.txt
			zipOutputStream.putNextEntry(zipEntry);
			data = arquivoServico.toString().getBytes("UTF-8");
			zipOutputStream.write(data, 0, data.length);
			zipOutputStream.closeEntry();
			
			zipOutputStream.flush();
			zipOutputStream.close();
			outputStream.flush();
			outputStream.close();
			ftp.completePendingCommand();
			
			//Resposta a Notificação 
			data = ("1|" + LL + "|A").getBytes("UTF-8");
			Date date = Calendar.getInstance().getTime();  
		    DateFormat dateFormat = new SimpleDateFormat("ddMMyyyyHHmmss");  
		    DDMMAAAAHHMMSS = dateFormat.format(date);
			outputStream = ftp.storeFileStream("e-Carta_" + MM + "_" + LL + "_Resposta" + DDMMAAAAHHMMSS + ".zip");		//e-Carta_MM_LL_RespostaDDMMAAAAHHMMSS.zip
			zipOutputStream = new ZipOutputStream(outputStream, StandardCharsets.UTF_8);
			zipEntry = new ZipEntry("e-Carta_" + MM + "_" + LL + "_Resposta" + DDMMAAAAHHMMSS + ".txt");				//e-Carta_MM_LL_RespostaDDMMAAAAHHMMSS.txt
			zipOutputStream.putNextEntry(zipEntry);
			zipOutputStream.write(data, 0, data.length);
			zipOutputStream.closeEntry();
			zipOutputStream.flush();
			zipOutputStream.close();
			outputStream.flush();
			outputStream.close();
			ftp.completePendingCommand();
			sucesso = true;
		} catch (Exception e) {
			logDt = new LogDt("JobEnviarCartasCorreios", "", UsuarioDt.SistemaProjudi, "Servidor",  String.valueOf(LogTipoDt.ExecucaoAutomatica), writer.toString(), Funcoes.obtenhaConteudoPrimeiraExcecao(e));
			logNe.salvar(logDt);
			return false;
		} finally {
			if(ftp.isConnected()) {
				ftp.logout();
				ftp.disconnect();
			}
			if(sucesso) {
				logDt = new LogDt("JobEnviarCartasCorreios", "", UsuarioDt.SistemaProjudi, "Servidor",  String.valueOf(LogTipoDt.ExecucaoAutomatica), writer.toString(), "");
				logNe.salvar(logDt);
			}
		}
		return true;
	}	
	
	/**
	 * Cancela todo o lote informado - se nenhuma carta tiver sido confeccionada
	 * @param MM Matriz de relacionamento
	 * @param LL Número do lote
	 * @return verdadeiro, caso o arquivo seja enviado
	 * @throws Exception
	 */
//	public boolean enviarArquivoCancelamento(String MM, String LL) throws Exception { 
//		FTPClient ftp = new FTPClient();
//		OutputStream outputStream;
//		ZipEntry zipEntry;
//		ZipOutputStream zipOutputStream;
//		byte[] data;
//
//		try {
//			ftp.setStrictReplyParsing(false);
//			ftp.enterLocalPassiveMode();
//			ftp.setFileType(FTP.BINARY_FILE_TYPE);
//			ftp.setActivePortRange(55000, 65535);
//			ftp.connect(CorreiosDt.URL_HOST, CorreiosDt.FTPS_PORT);
//			ftp.login(CorreiosDt.USERNAME, CorreiosDt.PASSWORD);
//			
//			data = (MM + ";" + LL).getBytes("UTF-8");	//[MM;LL]
//			outputStream = ftp.storeFileStream("e-Carta_" + MM + "_" + LL + "_Cancelamento.zip"); 	//e-Carta_MM_LL_Cancelamento.zip
//			zipOutputStream = new ZipOutputStream(outputStream, StandardCharsets.UTF_8);
//			zipEntry = new ZipEntry("e-Carta_" + MM + "_" + LL + "_Cancelamento.txt");				//e-Carta_MM_LL_Cancelamento.txt
//			zipOutputStream.putNextEntry(zipEntry);
//			zipOutputStream.write(data, 0, data.length);
//			zipOutputStream.closeEntry();
//			zipOutputStream.flush();
//			zipOutputStream.close();
//			outputStream.flush();
//			outputStream.close();
//			ftp.completePendingCommand();
//		} catch (Exception e) {
//			throw e;
//		} finally {
//			ftp.logout();
//			ftp.disconnect();
//		}
//		return true;
//	}
	
	/**
	 * Lê arquivo de resposta do cancelamento, que fala se o lote foi mesmo cancelado
	 * @return [MM;LL;Código Cancelamento;Mensagem Cancelamento]
	 * @throws Exception
	 */
//	public String[] lerRespostaCancelamento() throws Exception {
//		FTPClient ftp = new FTPClient();
//		StringBuilder recibo = new StringBuilder();
//		InputStream inputStream;
//		ByteArrayOutputStream byteArrayOutputStream;
//		ByteArrayInputStream byteArrayInputStream;
//		ZipInputStream zipInputStream;
//		ZipEntry zipEntry;
//		byte[] buffer;
//		byte[] arquivoCancelamento;
//		String[] linhaCancelamento = null;
//
//		try {
//			ftp.setStrictReplyParsing(false);
//			ftp.enterLocalPassiveMode();
//			ftp.setFileType(FTP.BINARY_FILE_TYPE);
//			ftp.setActivePortRange(55000, 65535);
//			ftp.connect(CorreiosDt.URL_HOST, CorreiosDt.FTPS_PORT);
//			ftp.login(CorreiosDt.USERNAME, CorreiosDt.PASSWORD);
//			ftp.cwd("//e-carta/");
//
//			String[] names = ftp.listNames();
//			for (String name : names) {
//				if (name.contains("Cancelamento_Resposta")) {
//					inputStream = ftp.retrieveFileStream(name);
//					byteArrayOutputStream = new ByteArrayOutputStream();
//					buffer = new byte[1024];
//					int length = 0;
//					while ((length = inputStream.read(buffer)) != -1) {
//						byteArrayOutputStream.write(buffer, 0, length);
//					}
//					arquivoCancelamento = byteArrayOutputStream.toByteArray();
//
//					ArquivoDt arquivo = new ArquivoDt();
//					arquivo.setNomeArquivo(name);
//					arquivo.setArquivo(arquivoCancelamento);
//					arquivo.setContentType("application/zip");
//					arquivo.setId_ArquivoTipo(String.valueOf(ArquivoTipoDt.OUTROS));
//					arquivo.setId_UsuarioLog(UsuarioDt.SistemaProjudi);
//					new ArquivoNe().salvar(arquivo);
//
//					byteArrayInputStream = new ByteArrayInputStream(arquivoCancelamento);
//					zipInputStream = new ZipInputStream(byteArrayInputStream, StandardCharsets.UTF_8);
//					buffer = new byte[1024];
//					while ((zipEntry = zipInputStream.getNextEntry()) != null) {
//						while ((length = zipInputStream.read(buffer, 0, 1024)) >= 0) {
//							recibo.append(new String(buffer, 0, length, StandardCharsets.UTF_8));
//						}
//					}
//					recibo.append("\n");
//					inputStream.close();
//					byteArrayOutputStream.close();
//					byteArrayInputStream.close();
//					zipInputStream.close();
////					ftp.deleteFile("//e-carta/" + name);
//					ftp.completePendingCommand();
//				}
//			}
//			linhaCancelamento = recibo.toString().split("\n");
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			ftp.logout();
//			ftp.disconnect();
//		}
//		return linhaCancelamento;
//	}
	
	/**
	 * Lê arquivo com os códigos de rastreamento das cartas enviadas
	 * @return [TipoRegistro|CódigoObjetoCliente|NúmeroLote|NúmeroEtiqueta|InformaçãoLimitePostagem]
	 * @throws Exception
	 */
	public String[] lerReciboServico() throws Exception {
		FTPClient ftp = new FTPClient();
		StringBuilder recibo = new StringBuilder();
		InputStream inputStream;
		ByteArrayOutputStream byteArrayOutputStream;
		ByteArrayInputStream byteArrayInputStream;
		ZipInputStream zipInputStream;
		ZipEntry zipEntry;
		byte[] buffer;
		byte[] arquivoRecibo;
		String[] linhasRecibo = null;
		LogNe logNe = new LogNe();
		StringWriter writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		ArquivoNe arquivoNe = new ArquivoNe();
		LogDt logDt = null;
		String nomeArquivo = "";
		String[] names = null;

		try {
			ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(printWriter), true));
			ftp.setStrictReplyParsing(false);
			ftp.connect(CorreiosDt.URL_HOST, CorreiosDt.FTP_PORT);
			int reply = ftp.getReplyCode();
	        if(!FTPReply.isPositiveCompletion(reply)) {
		        ftp.disconnect();
		        logDt = new LogDt("JobConfirmarRecebimentoCorreios", "", UsuarioDt.SistemaProjudi, "Servidor",  String.valueOf(LogTipoDt.ExecucaoAutomatica), writer.toString(), "Não foi possível conectar ao servidor. Resposta: " + String.valueOf(reply));
				logNe.salvar(logDt);
	        } else {
	        	ftp.login(CorreiosDt.USERNAME, CorreiosDt.PASSWORD);
	        	ftp.enterLocalPassiveMode();
	        	ftp.setFileType(FTP.BINARY_FILE_TYPE);
	        	ftp.cwd("//e-carta/");
	        	
	        	names = ftp.listNames();
	        	for (String name : names) {
	        		if (name.contains("_Recibo.zip")) {
	        			inputStream = ftp.retrieveFileStream(name);
	        			byteArrayOutputStream = new ByteArrayOutputStream();
	        			buffer = new byte[1024];
	        			int length = 0;
	        			while ((length = inputStream.read(buffer)) != -1) {
	        				byteArrayOutputStream.write(buffer, 0, length);
	        			}
	        			arquivoRecibo = byteArrayOutputStream.toByteArray();
	        			nomeArquivo = Funcoes.limparNomeArquivo(name);
	        			
	        			if(!arquivoNe.arquivoExiste(nomeArquivo, ArquivoTipoDt.ID_CORREIO_RECIBO)) {
	        				ArquivoDt arquivo = new ArquivoDt();
	        				arquivo.setNomeArquivo(name);
	        				arquivo.setDataInsercao(Funcoes.getDataArquivoCorreios(nomeArquivo));
	        				arquivo.setArquivo(arquivoRecibo);
	        				arquivo.setContentType("application/zip");
	        				arquivo.setId_ArquivoTipo(String.valueOf(ArquivoTipoDt.ID_CORREIO_RECIBO));
	        				arquivo.setId_UsuarioLog(UsuarioDt.SistemaProjudi);
	        				arquivoNe.salvarECarta(arquivo);
	        			}
	        			
	        			byteArrayInputStream = new ByteArrayInputStream(arquivoRecibo);
	        			zipInputStream = new ZipInputStream(byteArrayInputStream, StandardCharsets.UTF_8);
	        			buffer = new byte[1024];
	        			while ((zipEntry = zipInputStream.getNextEntry()) != null) {
	        				while ((length = zipInputStream.read(buffer, 0, 1024)) >= 0) {
	        					recibo.append(new String(buffer, 0, length, StandardCharsets.UTF_8));
	        				}
	        			}
	        			recibo.append("\n");
	        			inputStream.close();
	        			byteArrayOutputStream.close();
	        			byteArrayInputStream.close();
	        			zipInputStream.close();
	        			ftp.completePendingCommand();
	        		}
	        	}
	        	if(recibo != null) {
	        		linhasRecibo = recibo.toString().split("\n");
	        	}
	        }
		} catch (Exception e) {
			logDt = new LogDt("JobConfirmarRecebimentoCorreios", "", UsuarioDt.SistemaProjudi, "Servidor",  String.valueOf(LogTipoDt.ExecucaoAutomatica), writer.toString(), Funcoes.obtenhaConteudoPrimeiraExcecao(e));
			logNe.salvar(logDt);
		} finally {
			if(ftp.isConnected()) {
				ftp.logout();
				ftp.disconnect();
			}
		}
		return linhasRecibo; 
	}

	/**
	 * Lê arquivo com as cartas que tiveram algum problema na confecção
	 * @return [Tipo de Registro|Número do Lote|Identificador Tipo Iconsistência Arquivo|Mensagem Tipo Iconsistência Arquivo]
	 * 		   [Tipo de Registro|Código do Objeto do Cliente Inconsistente|Identificador Tipo Inconsistência Objeto|Mensagem Tipo Inconsistência Objeto]
	 * @throws Exception
	 */
	public String[] lerNotificacaoInconsistencia() throws Exception {
		FTPClient ftp = new FTPClient();
		StringBuilder inconsistencia = new StringBuilder();
		InputStream inputStream;
		ByteArrayOutputStream byteArrayOutputStream;
		ByteArrayInputStream byteArrayInputStream;
		ZipInputStream zipInputStream;
		ZipEntry zipEntry;
		byte[] buffer;
		byte[] arquivoInconsistencia;
		String[] linhasInconsistencia = null;
		LogNe logNe = new LogNe();
		StringWriter writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		StringBuffer log = new StringBuffer();
		ArquivoNe arquivoNe = new ArquivoNe();
		LogDt logDt = null;
		String nomeArquivo = "";

		try {
			ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(printWriter), true));
			ftp.setStrictReplyParsing(false);
			ftp.connect(CorreiosDt.URL_HOST, CorreiosDt.FTP_PORT);
			int reply = ftp.getReplyCode();
	        if(!FTPReply.isPositiveCompletion(reply)) {
		        ftp.disconnect();
		        logDt = new LogDt("JobConfirmarRecebimentoCorreios", "", UsuarioDt.SistemaProjudi, "Servidor",  String.valueOf(LogTipoDt.ExecucaoAutomatica), writer.toString(), "Não foi possível conectar ao servidor. Resposta: " + String.valueOf(reply));
				logNe.salvar(logDt);
	        } else {
	        	ftp.login(CorreiosDt.USERNAME, CorreiosDt.PASSWORD);
	        	ftp.enterLocalPassiveMode();
	        	ftp.setFileType(FTP.BINARY_FILE_TYPE);
	        	ftp.cwd("//e-carta/");
	        	
	        	String[] names = ftp.listNames();
	        	for (String name : names) {
	        		if (name.contains("_Inconsistencia.zip")) {
	        			inputStream = ftp.retrieveFileStream(name);
	        			byteArrayOutputStream = new ByteArrayOutputStream();
	        			buffer = new byte[1024];
	        			int length = 0;
	        			while ((length = inputStream.read(buffer)) != -1) {
	        				byteArrayOutputStream.write(buffer, 0, length);
	        			}
	        			arquivoInconsistencia = byteArrayOutputStream.toByteArray();
	        			nomeArquivo = Funcoes.limparNomeArquivo(name);
	        			
	        			if(!arquivoNe.arquivoExiste(nomeArquivo, ArquivoTipoDt.ID_CORREIO_INCONSISTENCIA)) {
	        				ArquivoDt arquivo = new ArquivoDt();
	        				arquivo.setNomeArquivo(name);
	        				arquivo.setDataInsercao(Funcoes.getDataArquivoCorreios(nomeArquivo));
	        				arquivo.setArquivo(arquivoInconsistencia);
	        				arquivo.setContentType("application/zip");
	        				arquivo.setId_ArquivoTipo(String.valueOf(ArquivoTipoDt.ID_CORREIO_INCONSISTENCIA));
	        				arquivo.setId_UsuarioLog(UsuarioDt.SistemaProjudi);
							arquivoNe.salvarECarta(arquivo);
	        			}
	        			
	        			byteArrayInputStream = new ByteArrayInputStream(arquivoInconsistencia);
	        			zipInputStream = new ZipInputStream(byteArrayInputStream, StandardCharsets.UTF_8);
	        			buffer = new byte[1024];
	        			while ((zipEntry = zipInputStream.getNextEntry()) != null) {
	        				while ((length = zipInputStream.read(buffer, 0, 1024)) >= 0) {
	        					inconsistencia.append(new String(buffer, 0, length, StandardCharsets.UTF_8));
	        				}
	        			}
	        			inputStream.close();
	        			byteArrayOutputStream.close();
	        			byteArrayInputStream.close();
	        			zipInputStream.close();
	        			ftp.completePendingCommand();
	        		}
	        	}
	        	if(inconsistencia != null) {
	        		linhasInconsistencia = inconsistencia.toString().split("\n");
	        	}
	        }
		} catch (Exception e) {
			logDt = new LogDt("JobConfirmarRecebimentoCorreios", "", UsuarioDt.SistemaProjudi, "Servidor",  String.valueOf(LogTipoDt.ExecucaoAutomatica), writer.toString(), Funcoes.obtenhaConteudoPrimeiraExcecao(e));
			logNe.salvar(logDt);
		} finally {
			if(ftp.isConnected()) {
				ftp.logout();
				ftp.disconnect();
			}
		}										
		return linhasInconsistencia; 	
	}
	
	/**
	 * Lê arquivo com a data de postagem e data estimada de entrega das cartas
	 * @return [NúmeroLote|EtiquetaRegistro|TipoPostal|ServiçoContratado|CategoriaServiço|SiglaEventoSRO|CódigoEvento|DataPostagem|HoraPostagem|EventoSRO|UnidadeCorreios|CEPCorreios|CidadeCorreios|UFCorreios|DataEstimadaPrimeiraTentativaEntrega]
	 * @throws Exception
	 */
	public String[] lerRastreamentoDataEstimadaEntrega() throws Exception {
		FTPClient ftp = new FTPClient();
		StringBuilder postagem = new StringBuilder();
		InputStream inputStream;
		ByteArrayOutputStream byteArrayOutputStream;
		ByteArrayInputStream byteArrayInputStream;
		ZipInputStream zipInputStream;
		ZipEntry zipEntry;
		byte[] buffer;
		byte[] arquivoPostagem;
		String[] linhasPostagem = null;
		LogNe logNe = new LogNe();
		StringWriter writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		ArquivoNe arquivoNe = new ArquivoNe();
		LogDt logDt = null;
		String nomeArquivo = "";

		try {
			ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(printWriter), true));
			ftp.setStrictReplyParsing(false);
			ftp.connect(CorreiosDt.URL_HOST, CorreiosDt.FTP_PORT);
			int reply = ftp.getReplyCode();
	        if(!FTPReply.isPositiveCompletion(reply)) {
		        ftp.disconnect();
		        logDt = new LogDt("JobConfirmarPostagemCorreios", "", UsuarioDt.SistemaProjudi, "Servidor",  String.valueOf(LogTipoDt.ExecucaoAutomatica), writer.toString(), "Não foi possível conectar ao servidor. Resposta: " + String.valueOf(reply));
				logNe.salvar(logDt);
	        } else {
	        	ftp.login(CorreiosDt.USERNAME, CorreiosDt.PASSWORD);
	        	ftp.enterLocalPassiveMode();
	        	ftp.setFileType(FTP.BINARY_FILE_TYPE);
	        	ftp.cwd("//e-carta/");
	        	
	        	String[] names = ftp.listNames();
	        	for (String name : names) {
	        		if (name.contains("_Postado")) {
	        			inputStream = ftp.retrieveFileStream(name);
	        			byteArrayOutputStream = new ByteArrayOutputStream();
	        			buffer = new byte[1024];
	        			int length = 0;
	        			while ((length = inputStream.read(buffer)) != -1) {
	        				byteArrayOutputStream.write(buffer, 0, length);
	        			}
	        			arquivoPostagem = byteArrayOutputStream.toByteArray();
	        			nomeArquivo = Funcoes.limparNomeArquivo(name);
	        			
	        			if(!arquivoNe.arquivoExiste(nomeArquivo, ArquivoTipoDt.ID_CORREIO_POSTAGEM)) {
	        				ArquivoDt arquivo = new ArquivoDt();
	        				arquivo.setNomeArquivo(name);
	        				arquivo.setDataInsercao(Funcoes.getDataArquivoCorreios(nomeArquivo));
	        				arquivo.setArquivo(arquivoPostagem);
	        				arquivo.setContentType("application/zip");
	        				arquivo.setId_ArquivoTipo(String.valueOf(ArquivoTipoDt.ID_CORREIO_POSTAGEM));
	        				arquivo.setId_UsuarioLog(UsuarioDt.SistemaProjudi);
	        				arquivoNe.salvarECarta(arquivo);
	        			}
	        			
	        			byteArrayInputStream = new ByteArrayInputStream(arquivoPostagem);
	        			zipInputStream = new ZipInputStream(byteArrayInputStream, StandardCharsets.UTF_8);
	        			buffer = new byte[1024];
	        			while ((zipEntry = zipInputStream.getNextEntry()) != null) {
	        				while ((length = zipInputStream.read(buffer, 0, 1024)) >= 0) {
	        					postagem.append(new String(buffer, 0, length, StandardCharsets.UTF_8));
	        				}
	        			}
	        			postagem.append("\n");
	        			inputStream.close();
	        			byteArrayOutputStream.close();
	        			byteArrayInputStream.close();
	        			zipInputStream.close();
	        			ftp.completePendingCommand();
	        		}
	        	}
	        	if(postagem != null) {
	        		linhasPostagem = postagem.toString().split("\n");
	        	}
	        }
		} catch (Exception e) {
			logDt = new LogDt("JobConfirmarPostagemCorreios", "", UsuarioDt.SistemaProjudi, "Servidor",  String.valueOf(LogTipoDt.ExecucaoAutomatica), writer.toString(), Funcoes.obtenhaConteudoPrimeiraExcecao(e));
			logNe.salvar(logDt);
		} finally {
			if(ftp.isConnected()) {
				ftp.logout();
				ftp.disconnect();
			}
		}
		return linhasPostagem; 
	}
	
	/**
	 * Lê arquivo com a data da entrega da carta
	 * @return [NúmeroLote|EtiquetaRegistro|TipoPostal|ServiçoContratado|CategoriaServiço|SiglaEventoSRO|CódigoEvento|DataEntrega|HoraEntrega|EventoSRO|UnidadeCorreios|CEPCorreios|CidadeCorreios|UFCorreios|DataEstimadaPostagem]
	 * @throws Exception
	 */
	public String[] lerRastreamentoDataFinalEntrega() throws Exception {
		FTPClient ftp = new FTPClient();
		StringBuilder rastreamento = new StringBuilder();
		InputStream inputStream;
		ByteArrayOutputStream byteArrayOutputStream;
		ByteArrayInputStream byteArrayInputStream;
		ZipInputStream zipInputStream;
		ZipEntry zipEntry;
		byte[] buffer;
		byte[] arquivoRastreamento;
		String[] linhasRastreamento = null;
		LogNe logNe = new LogNe();
		StringWriter writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		StringBuffer log = new StringBuffer();
		boolean sucesso = false;
		ArquivoNe arquivoNe = new ArquivoNe();
		LogDt logDt = null;
		String nomeArquivo = "";

		try {
			ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(printWriter), true));
			ftp.setStrictReplyParsing(false);
			ftp.connect(CorreiosDt.URL_HOST, CorreiosDt.FTP_PORT);
			int reply = ftp.getReplyCode();
	        if(!FTPReply.isPositiveCompletion(reply)) {
		        ftp.disconnect();
		        logDt = new LogDt("JobConfirmarEntregaCorreios", "", UsuarioDt.SistemaProjudi, "Servidor",  String.valueOf(LogTipoDt.ExecucaoAutomatica), writer.toString(), "Não foi possível conectar ao servidor. Resposta: " + String.valueOf(reply));
				logNe.salvar(logDt);
	        } else {
	        	ftp.login(CorreiosDt.USERNAME, CorreiosDt.PASSWORD);
	        	ftp.enterLocalPassiveMode();
	        	ftp.setFileType(FTP.BINARY_FILE_TYPE);
	        	ftp.cwd("//e-carta/");
	        	
	        	String[] names = ftp.listNames();
	        	for (String name : names) {
	        		if (name.contains("_Finalizador")) {
	        			inputStream = ftp.retrieveFileStream(name);
	        			byteArrayOutputStream = new ByteArrayOutputStream();
	        			buffer = new byte[1024];
	        			int length = 0;
	        			while ((length = inputStream.read(buffer)) != -1) {
	        				byteArrayOutputStream.write(buffer, 0, length);
	        			}
	        			arquivoRastreamento = byteArrayOutputStream.toByteArray();
	        			nomeArquivo = Funcoes.limparNomeArquivo(name);
	        			
	        			if(!arquivoNe.arquivoExiste(nomeArquivo, ArquivoTipoDt.ID_CORREIO_ENTREGA)) {
	        				ArquivoDt arquivo = new ArquivoDt();
	        				arquivo.setNomeArquivo(name);
	        				arquivo.setDataInsercao(Funcoes.getDataArquivoCorreios(nomeArquivo));
	        				arquivo.setArquivo(arquivoRastreamento);
	        				arquivo.setContentType("application/zip");
	        				arquivo.setId_ArquivoTipo(String.valueOf(ArquivoTipoDt.ID_CORREIO_ENTREGA));
	        				arquivo.setId_UsuarioLog(UsuarioDt.SistemaProjudi);
	        				arquivoNe.salvarECarta(arquivo);
	        			}
	        			
	        			byteArrayInputStream = new ByteArrayInputStream(arquivoRastreamento);
	        			zipInputStream = new ZipInputStream(byteArrayInputStream, StandardCharsets.UTF_8);
	        			buffer = new byte[1024];
	        			while ((zipEntry = zipInputStream.getNextEntry()) != null) {
	        				while ((length = zipInputStream.read(buffer, 0, 1024)) >= 0) {
	        					rastreamento.append(new String(buffer, 0, length, StandardCharsets.UTF_8));
	        				}
	        			}
	        			inputStream.close();
	        			byteArrayOutputStream.close();
	        			byteArrayInputStream.close();
	        			zipInputStream.close();
	        			ftp.completePendingCommand();
	        			sucesso = true;
	        		}
	        	}
	        	if(rastreamento != null) {
	        		linhasRastreamento = rastreamento.toString().split("\n");
	        	}
	        }
		} catch (Exception e) {
			logDt = new LogDt("JobConfirmarEntregaCorreios", "", UsuarioDt.SistemaProjudi, "Servidor",  String.valueOf(LogTipoDt.ExecucaoAutomatica), writer.toString(), Funcoes.obtenhaConteudoPrimeiraExcecao(e));
			logNe.salvar(logDt);
		} finally {
			if(ftp.isConnected()) {
				ftp.logout();
				ftp.disconnect();
			}
			if(sucesso) {
				logDt = new LogDt("JobConfirmarEntregaCorreios", "", UsuarioDt.SistemaProjudi, "Servidor",  String.valueOf(LogTipoDt.ExecucaoAutomatica), writer.toString(), log.toString());
				logNe.salvar(logDt);
			}
		}
		return linhasRastreamento;
	}
	
	/**
	 * Lê arquivo com as imagens dos ARs
	 * @return <IdPendencia, imagem>
	 * @throws Exception
	 */
	public Map<String, byte[]> lerDevolucaoARs() throws Exception {
		FTPClient ftp = new FTPClient();
		StringBuilder avisoRecebimento = new StringBuilder();
		InputStream inputStream;
		ByteArrayOutputStream byteArrayOutputStream;
		ByteArrayInputStream byteArrayInputStream;
		ZipInputStream zipInputStream;
		ZipEntry zipEntry;
		byte[] buffer;
		byte[] arquivoDevolucaoAR;
		String[] linhasAvisoRecebimento = null;
		String[] linhaAR = null;
		Map<String, String> mapNomeId = new HashMap<String, String>();
		Map<String, byte[]> mapIdConteudo = new HashMap<String, byte[]>();
		LogNe logNe = new LogNe();
		StringWriter writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		StringBuffer log = new StringBuffer();
		boolean sucesso = false;
		ArquivoNe arquivoNe = new ArquivoNe();
		LogDt logDt = null;
		String nomeArquivo = "";

		try {
			ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(printWriter), true));
			ftp.setStrictReplyParsing(false);
			ftp.connect(CorreiosDt.URL_HOST, CorreiosDt.FTP_PORT);
			int reply = ftp.getReplyCode();
	        if(!FTPReply.isPositiveCompletion(reply)) {
		        ftp.disconnect();
		        logDt = new LogDt("JobConfirmarRecebimentoARCorreios", "", UsuarioDt.SistemaProjudi, "Servidor",  String.valueOf(LogTipoDt.ExecucaoAutomatica), writer.toString(), "Não foi possível conectar ao servidor. Resposta: " + String.valueOf(reply));
				logNe.salvar(logDt);
	        } else {
	        	ftp.login(CorreiosDt.USERNAME, CorreiosDt.PASSWORD);
	        	ftp.enterLocalPassiveMode();
	        	ftp.setFileType(FTP.BINARY_FILE_TYPE);
	        	ftp.cwd("//e-carta/");
	        	
	        	String[] names = ftp.listNames();
	        	for (String name : names) {
	        		if (name.contains("DevolucaoAR")) {
	        			inputStream = ftp.retrieveFileStream(name);
	        			byteArrayOutputStream = new ByteArrayOutputStream();
	        			buffer = new byte[1024];
	        			int length = 0;
	        			while ((length = inputStream.read(buffer)) != -1) {
	        				byteArrayOutputStream.write(buffer, 0, length);
	        			}
	        			arquivoDevolucaoAR = byteArrayOutputStream.toByteArray();
	        			nomeArquivo = Funcoes.limparNomeArquivo(name);
	        			
	        			if(!arquivoNe.arquivoExiste(nomeArquivo, ArquivoTipoDt.ID_CORREIO_DEVOLUCAO_AR)) {
	        				ArquivoDt arquivo = new ArquivoDt();
	        				arquivo.setNomeArquivo(name);
	        				arquivo.setDataInsercao(Funcoes.getDataArquivoCorreios(nomeArquivo));
	        				arquivo.setArquivo(arquivoDevolucaoAR);
	        				arquivo.setContentType("application/zip");
	        				arquivo.setId_ArquivoTipo(String.valueOf(ArquivoTipoDt.ID_CORREIO_DEVOLUCAO_AR));
	        				arquivo.setId_UsuarioLog(UsuarioDt.SistemaProjudi);
	        				arquivoNe.salvarECarta(arquivo);
	        			}
	        			
	        			byteArrayInputStream = new ByteArrayInputStream(arquivoDevolucaoAR);
	        			zipInputStream = new ZipInputStream(byteArrayInputStream, StandardCharsets.UTF_8);
	        			buffer = new byte[1024];
	        			while ((zipEntry = zipInputStream.getNextEntry()) != null) {
	        				if(zipEntry.getName().contains("DevolucaoAR")) {
	        					while ((length = zipInputStream.read(buffer, 0, 1024)) >= 0) {
	        						avisoRecebimento.append(new String(buffer, 0, length, StandardCharsets.UTF_8));
	        					}
	        				}
	        			}
	        			linhasAvisoRecebimento = avisoRecebimento.toString().split("\n");
	        			for (int i = 0; i < linhasAvisoRecebimento.length; i++) {
	        				linhaAR = linhasAvisoRecebimento[i].split("\\|");   	//[Tipo de Registro|Código do Objeto do Cliente|Número do Lote|Número da Etiqueta|Número do AR|Código da Baixa|Nome da Imagem do AR]
	        				mapNomeId.put(linhaAR[6], linhaAR[1]+"-"+linhaAR[3]);
	        			}
	        			
	        			byteArrayInputStream = new ByteArrayInputStream(arquivoDevolucaoAR);
	        			zipInputStream = new ZipInputStream(byteArrayInputStream, StandardCharsets.UTF_8);
	        			while ((zipEntry = zipInputStream.getNextEntry()) != null) {
	        				if(zipEntry.getName().contains(".PDF")) {
	        					byteArrayOutputStream = new ByteArrayOutputStream();
	        					while ((length = zipInputStream.read(buffer, 0, 1024)) >= 0) {
	        						byteArrayOutputStream.write(buffer, 0, length);
	        					}
	        					mapIdConteudo.put(mapNomeId.get(zipEntry.getName()), byteArrayOutputStream.toByteArray());
	        				}
	        			}
	        			
	        			inputStream.close();
	        			byteArrayOutputStream.close();
	        			byteArrayInputStream.close();
	        			zipInputStream.close();
	        			ftp.completePendingCommand();
	        			sucesso = true;
	        		}
	        	}
	        }
		} catch (Exception e) {
			logDt = new LogDt("JobConfirmarRecebimentoARCorreios", "", UsuarioDt.SistemaProjudi, "Servidor",  String.valueOf(LogTipoDt.ExecucaoAutomatica), writer.toString(), Funcoes.obtenhaConteudoPrimeiraExcecao(e));
			logNe.salvar(logDt);
		} finally {
			if(ftp.isConnected()) {
				ftp.logout();
				ftp.disconnect();
			}
			if(sucesso) {
				logDt = new LogDt("JobConfirmarRecebimentoARCorreios", "", UsuarioDt.SistemaProjudi, "Servidor",  String.valueOf(LogTipoDt.ExecucaoAutomatica), writer.toString(), log.toString());
				logNe.salvar(logDt);
			}
		}
		return mapIdConteudo;		
	}
	
	/**
	 * Envia resposta positiva de recebimento dos ARs digitais
	 * @return verdadeiro, caso o arquivo seja enviado
	 * @throws Exception 
	 */
//	public boolean enviarArquivoRespostaDevolucaoARs(List<CorreiosDt> listaCartas, String MM, String LL, String DDMMAAAAHHMMSS) throws Exception {
//		FTPClient ftp = new FTPClient();
//		OutputStream outputStream;
//		ZipEntry zipEntry;
//		ZipOutputStream zipOutputStream;
//		byte[] data;
//		CorreiosDt correiosDt;
//		StringBuffer arquivoResposta = new StringBuffer();
//
//		try {
//			ftp.connect(CorreiosDt.URL_HOST, CorreiosDt.FTPS_PORT);
//			ftp.login(CorreiosDt.USERNAME, CorreiosDt.PASSWORD);
//			ftp.setStrictReplyParsing(false);
//			ftp.enterLocalPassiveMode();
//			ftp.setFileType(FTP.BINARY_FILE_TYPE);
//			
//			for (int i = 0; i < listaCartas.size(); i++) {
//				correiosDt = listaCartas.get(i);
//				correiosDt.setNumeroLote(LL);
//				arquivoResposta.append(correiosDt.montarRespostaDevolucaoARs() + "\n");
//			}
//			
//			data = arquivoResposta.toString().getBytes("UTF-8");
//			outputStream = ftp.storeFileStream("e-Carta_" + MM + "_DevolucaoAR_" + LL + "_" + DDMMAAAAHHMMSS + "_Resposta_" + DDMMAAAAHHMMSS + ".zip"); 	//e-Carta_MM_DevolucaoAR_LL_DDMMAAAAHHMMSS_Resposta_DDMMAAAAHHMMSS.zip
//			zipOutputStream = new ZipOutputStream(outputStream, StandardCharsets.UTF_8);
//			zipEntry = new ZipEntry("e-Carta_" + MM + "_DevolucaoAR_" + LL + "_" + DDMMAAAAHHMMSS + "_Resposta_" + DDMMAAAAHHMMSS + ".txt");				//e-Carta_MM_DevolucaoAR_LL_DDMMAAAAHHMMSS_Resposta_DDMMAAAAHHMMSS.txt
//			zipOutputStream.putNextEntry(zipEntry);
//			zipOutputStream.write(data, 0, data.length);
//			zipOutputStream.closeEntry();
//			zipOutputStream.flush();
//			zipOutputStream.close();
//			outputStream.flush();
//			outputStream.close();
//			ftp.completePendingCommand();
//		} catch (Exception e) {
//			e.printStackTrace();
//			return false;
//		} finally {
//			ftp.disconnect();
//		}
//		return true;
//	}
	
	/**
	 * 
	 * @return
	 */
//	public String[] lerArquivoRecusaRespostaDevolucaoARs() throws Exception {
//		FTPClient ftp = new FTPClient();
//		StringBuilder rastreamento = new StringBuilder();
//		InputStream inputStream;
//		ByteArrayOutputStream byteArrayOutputStream;
//		ByteArrayInputStream byteArrayInputStream;
//		ZipInputStream zipInputStream;
//		ZipEntry zipEntry;
//		byte[] buffer;
//		byte[] arquivoRastreamento;
//		String[] linhasRastreamento = null;
//
//		try {
//			ftp.connect(CorreiosDt.URL_HOST, CorreiosDt.FTPS_PORT);
//			ftp.login(CorreiosDt.USERNAME, CorreiosDt.PASSWORD);
//			ftp.setStrictReplyParsing(false);
//			ftp.enterLocalPassiveMode();
//			ftp.setFileType(FTP.BINARY_FILE_TYPE);
//			ftp.cwd("//e-carta/");
//
//			String[] names = ftp.listNames();
//			for (String name : names) {
//				if (name.contains("Rastreamento_Antecipado_Finalizador")) {
//					inputStream = ftp.retrieveFileStream(name);
//					byteArrayOutputStream = new ByteArrayOutputStream();
//					buffer = new byte[1024];
//					int length = 0;
//					while ((length = inputStream.read(buffer)) != -1) {
//						byteArrayOutputStream.write(buffer, 0, length);
//					}
//					arquivoRastreamento = byteArrayOutputStream.toByteArray();
//
//					ArquivoDt arquivo = new ArquivoDt();
//					arquivo.setNomeArquivo(name);
//					arquivo.setArquivo(arquivoRastreamento);
//					arquivo.setContentType("application/zip");
//					arquivo.setId_ArquivoTipo(String.valueOf(ArquivoTipoDt.ID_CORREIO_ENTREGA));
//					arquivo.setId_UsuarioLog(UsuarioDt.SistemaProjudi);
//					new ArquivoNe().salvar(arquivo);
//
//					byteArrayInputStream = new ByteArrayInputStream(arquivoRastreamento);
//					zipInputStream = new ZipInputStream(byteArrayInputStream);
//					buffer = new byte[1024];
//					while ((zipEntry = zipInputStream.getNextEntry()) != null) {
//						while ((length = zipInputStream.read(buffer, 0, 1024)) >= 0) {
//							rastreamento.append(new String(buffer, 0, length, StandardCharsets.UTF_8));
//						}
//					}
//					inputStream.close();
//					byteArrayOutputStream.close();
//					byteArrayInputStream.close();
//					zipInputStream.close();
////					ftp.deleteFile("//e-carta/" + name);
//					ftp.completePendingCommand();
//				}
//			}
//			linhasRastreamento = rastreamento.toString().split("\n");
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			ftp.disconnect();
//		}
//		return linhasRastreamento;
//	}
	
	public String getLote() throws Exception {
		String loTemp="";
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		ResultSetTJGO rs2 = null;
		try {	
			String sql =" SELECT SEQ_LOTES_CORREIOS.NEXTVAL lote FROM DUAL";
			ps =  new PreparedStatementTJGO();
			rs2 = this.consultar(sql, ps);
			rs2.next();
			loTemp =rs2.getString("lote");
		}finally{			
			if (rs2 != null) rs2.close();
		}
		return loTemp;
	}
	
	public static byte[] gerarPdfImagem(byte[] bytes)throws Exception {
        Document document = new Document();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		byte[] temp= null;
        try{
            PdfWriter.getInstance(document, outputStream);
            document.open();
            Image image = Image.getInstance(bytes);
            float scaler = ((document.getPageSize().getWidth() - document.leftMargin() - document.rightMargin()) / image.getWidth()) * 100;
            image.scalePercent(scaler);
            document.add(image);
            document.close();
            temp = outputStream.toByteArray();
        }
        catch(Exception e) {
        	try{if (document!=null) document.close(); } catch(Exception ex ) {};
        	try{if (outputStream!=null) outputStream.close(); } catch(Exception ex ) {};
        	throw e;
        }
        return temp;
	}
	
	/**
	 * Lê arquivo gravado no banco de dados com os códigos de rastreamento das cartas enviadas
	 * @return [TipoRegistro|CódigoObjetoCliente|NúmeroLote|NúmeroEtiqueta|InformaçãoLimitePostagem]
	 * @throws Exception
	 */
	public String[] lerReciboServicoBD(String nomeArquivo, String dataInicial, String dataFinal) throws Exception {
		StringBuilder recibo = new StringBuilder();
		ByteArrayInputStream byteArrayInputStream;
		ZipInputStream zipInputStream;
		ZipEntry zipEntry;
		byte[] buffer;
		String[] linhasRecibo = null;
		LogNe logNe = new LogNe();
		StringWriter writer = new StringWriter();
		ArquivoNe arquivoNe = new ArquivoNe();
		LogDt logDt = null;

		try {
			List<ArquivoDt> arquivosZip = arquivoNe.consultarArquivosECarta(nomeArquivo, ArquivoTipoDt.ID_CORREIO_RECIBO, dataInicial, dataFinal);
			for (ArquivoDt arquivo : arquivosZip) {
				recibo.append(arquivo.getNomeArquivo() + "\n");
    			byteArrayInputStream = new ByteArrayInputStream(arquivo.getConteudoSemAssinar());
    			zipInputStream = new ZipInputStream(byteArrayInputStream, StandardCharsets.UTF_8);
    			buffer = new byte[1024];
    			int length = 0;
    			while ((zipEntry = zipInputStream.getNextEntry()) != null) {
    				while ((length = zipInputStream.read(buffer, 0, 1024)) >= 0) {
    					recibo.append(new String(buffer, 0, length, StandardCharsets.UTF_8));
    				}
    			}
    			recibo.append("\n");
    			byteArrayInputStream.close();
    			zipInputStream.close();
        	}
        	if(recibo != null) {
        		linhasRecibo = recibo.toString().split("\n");
        	}
		} catch (Exception e) {
			logDt = new LogDt("JobConfirmarRecebimentoCorreios", "", UsuarioDt.SistemaProjudi, "Servidor",  String.valueOf(LogTipoDt.ExecucaoAutomatica), writer.toString(), Funcoes.obtenhaConteudoPrimeiraExcecao(e));
			logNe.salvar(logDt);
		}
		return linhasRecibo; 
	}
	
	/**
	 * Lê arquivo gravado no banco de dados com as cartas que tiveram algum problema na confecção
	 * @return [Tipo de Registro|Número do Lote|Identificador Tipo Iconsistência Arquivo|Mensagem Tipo Iconsistência Arquivo]
	 * 		   [Tipo de Registro|Código do Objeto do Cliente Inconsistente|Identificador Tipo Inconsistência Objeto|Mensagem Tipo Inconsistência Objeto]
	 * @throws Exception
	 */
	public String[] lerNotificacaoInconsistenciaBD(String nomeArquivo, String dataInicial, String dataFinal) throws Exception {
		StringBuilder inconsistencia = new StringBuilder();
		ByteArrayInputStream byteArrayInputStream;
		ZipInputStream zipInputStream;
		ZipEntry zipEntry;
		byte[] buffer;
		String[] linhasInconsistencia = null;
		StringWriter writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		ArquivoNe arquivoNe = new ArquivoNe();

		List<ArquivoDt> arquivosZip = arquivoNe.consultarArquivosECarta(nomeArquivo, ArquivoTipoDt.ID_CORREIO_INCONSISTENCIA, dataInicial, dataFinal);
		for (ArquivoDt arquivo : arquivosZip) {
			try {
				inconsistencia.append(arquivo.getNomeArquivo() + "\n");
				int length = 0;
				byteArrayInputStream = new ByteArrayInputStream(arquivo.getConteudoSemAssinar());
				zipInputStream = new ZipInputStream(byteArrayInputStream, StandardCharsets.UTF_8);
				buffer = new byte[1024];
				while ((zipEntry = zipInputStream.getNextEntry()) != null) {
					while ((length = zipInputStream.read(buffer, 0, 1024)) >= 0) {
						inconsistencia.append(new String(buffer, 0, length, StandardCharsets.UTF_8));
					}
				}
				inconsistencia.append("\n");
				byteArrayInputStream.close();
				zipInputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (inconsistencia != null) {
			linhasInconsistencia = inconsistencia.toString().split("\n");
		}
		return linhasInconsistencia;
	}
	
	public String[] lerRastreamentoDataEstimadaEntregaBD(String nomeArquivo, String dataInicial, String dataFinal) throws Exception {
		StringBuilder postagem = new StringBuilder();
		ByteArrayInputStream byteArrayInputStream;
		ZipInputStream zipInputStream;
		ZipEntry zipEntry;
		byte[] buffer;
		String[] linhasPostagem = null;
		StringWriter writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		ArquivoNe arquivoNe = new ArquivoNe();

		List<ArquivoDt> arquivosZip = arquivoNe.consultarArquivosECarta(nomeArquivo, ArquivoTipoDt.ID_CORREIO_POSTAGEM, dataInicial, dataFinal);
		for (ArquivoDt arquivo : arquivosZip) {
			try {
				postagem.append(arquivo.getNomeArquivo() + "\n");
				int length = 0;
				byteArrayInputStream = new ByteArrayInputStream(arquivo.getConteudoSemAssinar());
				zipInputStream = new ZipInputStream(byteArrayInputStream, StandardCharsets.UTF_8);
				buffer = new byte[1024];
				while ((zipEntry = zipInputStream.getNextEntry()) != null) {
					while ((length = zipInputStream.read(buffer, 0, 1024)) >= 0) {
						postagem.append(new String(buffer, 0, length, StandardCharsets.UTF_8));
					}
				}
				postagem.append("\n");
				byteArrayInputStream.close();
				zipInputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (postagem != null) {
			linhasPostagem = postagem.toString().split("\n");
		}
		return linhasPostagem;
	}
	
	/**
	 * Lê arquivo do banco de dados com a data da entrega da carta
	 * @return [NúmeroLote|EtiquetaRegistro|TipoPostal|ServiçoContratado|CategoriaServiço|SiglaEventoSRO|CódigoEvento|DataEntrega|HoraEntrega|EventoSRO|UnidadeCorreios|CEPCorreios|CidadeCorreios|UFCorreios|DataEstimadaPostagem]
	 * @throws Exception
	 */
	public String[] lerRastreamentoDataFinalEntregaBD(String nomeArquivo, String dataInicial, String dataFinal) throws Exception {
		StringBuilder entrega = new StringBuilder();
		ByteArrayInputStream byteArrayInputStream;
		ZipInputStream zipInputStream;
		ZipEntry zipEntry;
		byte[] buffer;
		String[] linhasEntrega = null;
		StringWriter writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		ArquivoNe arquivoNe = new ArquivoNe();

		List<ArquivoDt> arquivosZip = arquivoNe.consultarArquivosECarta(nomeArquivo, ArquivoTipoDt.ID_CORREIO_ENTREGA, dataInicial, dataFinal);
		for (ArquivoDt arquivo : arquivosZip) {
			try {
				entrega.append(arquivo.getNomeArquivo() + "\n");
				int length = 0;
				byteArrayInputStream = new ByteArrayInputStream(arquivo.getConteudoSemAssinar());
				zipInputStream = new ZipInputStream(byteArrayInputStream, StandardCharsets.UTF_8);
				buffer = new byte[1024];
				while ((zipEntry = zipInputStream.getNextEntry()) != null) {
					while ((length = zipInputStream.read(buffer, 0, 1024)) >= 0) {
						entrega.append(new String(buffer, 0, length, StandardCharsets.UTF_8));
					}
				}
				entrega.append("\n");
				byteArrayInputStream.close();
				zipInputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (entrega != null) {
			linhasEntrega = entrega.toString().split("\n");
		}
		return linhasEntrega;
	}
	
	public Map<String, byte[]> lerDevolucaoARsBD(String nomeArquivo, String dataInicial, String dataFinal) throws Exception {
		StringBuilder avisoRecebimento = new StringBuilder();
		ByteArrayOutputStream byteArrayOutputStream;
		ByteArrayInputStream byteArrayInputStream;
		ZipInputStream zipInputStream;
		ZipEntry zipEntry;
		byte[] buffer;
		String[] linhasAvisoRecebimento = null;
		String[] linhaAR = null;
		Map<String, String> mapNomeId = new HashMap<String, String>();
		Map<String, byte[]> mapIdConteudo = new HashMap<String, byte[]>();
		LogNe logNe = new LogNe();
		StringWriter writer = new StringWriter();
		StringBuffer log = new StringBuffer();
		boolean sucesso = false;
		ArquivoNe arquivoNe = new ArquivoNe();
		LogDt logDt = null;

		try {
			List<ArquivoDt> arquivosZip = arquivoNe.consultarArquivosECarta(nomeArquivo, ArquivoTipoDt.ID_CORREIO_DEVOLUCAO_AR, dataInicial, dataFinal);
			for (ArquivoDt arquivo : arquivosZip) {
    			byteArrayOutputStream = new ByteArrayOutputStream();
    			buffer = new byte[1024];
    			int length = 0;
    			byteArrayInputStream = new ByteArrayInputStream(arquivo.getConteudoSemAssinar());
    			zipInputStream = new ZipInputStream(byteArrayInputStream, StandardCharsets.UTF_8);
    			buffer = new byte[1024];
    			while ((zipEntry = zipInputStream.getNextEntry()) != null) {
    				if(zipEntry.getName().contains("DevolucaoAR")) {
    					while ((length = zipInputStream.read(buffer, 0, 1024)) >= 0) {
    						avisoRecebimento.append(new String(buffer, 0, length, StandardCharsets.UTF_8));
    					}
    				}
    			}
    			linhasAvisoRecebimento = avisoRecebimento.toString().split("\n");
    			for (int i = 0; i < linhasAvisoRecebimento.length; i++) {
    				linhaAR = linhasAvisoRecebimento[i].split("\\|");   	//[Tipo de Registro|Código do Objeto do Cliente|Número do Lote|Número da Etiqueta|Número do AR|Código da Baixa|Nome da Imagem do AR]
    				mapNomeId.put(linhaAR[6], linhaAR[1]+"-"+linhaAR[3]);
    			}
    			
    			byteArrayInputStream = new ByteArrayInputStream(arquivo.getConteudoSemAssinar());
    			zipInputStream = new ZipInputStream(byteArrayInputStream, StandardCharsets.UTF_8);
    			while ((zipEntry = zipInputStream.getNextEntry()) != null) {
    				if(zipEntry.getName().contains(".PDF")) {
    					byteArrayOutputStream = new ByteArrayOutputStream();
    					while ((length = zipInputStream.read(buffer, 0, 1024)) >= 0) {
    						byteArrayOutputStream.write(buffer, 0, length);
    					}
    					mapIdConteudo.put(mapNomeId.get(zipEntry.getName()), byteArrayOutputStream.toByteArray());
    				}
    			}
    			
    			byteArrayOutputStream.close();
    			byteArrayInputStream.close();
    			zipInputStream.close();
    			sucesso = true;
        	}
		} catch (Exception e) {
			logDt = new LogDt("JobConfirmarRecebimentoARCorreios", "", UsuarioDt.SistemaProjudi, "Servidor",  String.valueOf(LogTipoDt.ExecucaoAutomatica), writer.toString(), Funcoes.obtenhaConteudoPrimeiraExcecao(e));
			logNe.salvar(logDt);
		} finally {
			if(sucesso) {
				logDt = new LogDt("JobConfirmarRecebimentoARCorreios", "", UsuarioDt.SistemaProjudi, "Servidor",  String.valueOf(LogTipoDt.ExecucaoAutomatica), writer.toString(), log.toString());
				logNe.salvar(logDt);
			}
		}
		return mapIdConteudo;		
	}
	
	public String[] lerARsBD(String dataInicial, String dataFinal) throws Exception {
		StringBuilder avisoRecebimento = new StringBuilder();
		ByteArrayInputStream byteArrayInputStream;
		ZipInputStream zipInputStream;
		ZipEntry zipEntry;
		byte[] buffer;
		String[] linhasAvisoRecebimento = null;
		ArquivoNe arquivoNe = new ArquivoNe();

		List<ArquivoDt> arquivosZip = arquivoNe.consultarArquivosECarta("", ArquivoTipoDt.ID_CORREIO_DEVOLUCAO_AR, dataInicial, dataFinal);
		for (ArquivoDt arquivo : arquivosZip) {
			avisoRecebimento.append(arquivo.getNomeArquivo() + "\n");
			int length = 0;
			byteArrayInputStream = new ByteArrayInputStream(arquivo.getConteudoSemAssinar());
			zipInputStream = new ZipInputStream(byteArrayInputStream, StandardCharsets.UTF_8);
			buffer = new byte[1024];
			while ((zipEntry = zipInputStream.getNextEntry()) != null) {
				if (zipEntry.getName().contains("DevolucaoAR")) {
					while ((length = zipInputStream.read(buffer, 0, 1024)) >= 0) {
						avisoRecebimento.append(new String(buffer, 0, length, StandardCharsets.UTF_8));
					}
				}
			}
			byteArrayInputStream.close();
			zipInputStream.close();
		}
		linhasAvisoRecebimento = avisoRecebimento.toString().split("\n");
		return linhasAvisoRecebimento;
	}
	
	public byte[] getArBD(String idPendencia, String codRastreamento, String arquivoZip, String dataInicio, String dataFim) throws Exception {
		StringBuilder avisoRecebimento = new StringBuilder();
		ByteArrayOutputStream byteArrayOutputStream = null;
		ByteArrayInputStream byteArrayInputStream = null;
		ZipInputStream zipInputStream = null;
		ZipEntry zipEntry;
		byte[] buffer;
		byte[] arquivoDevolucaoAR = null;
		String[] linhasAvisoRecebimento = null;
		String[] linhaAR = null;
		String nomeArquivo = "";
		ArquivoNe arquivoNe = new ArquivoNe();

		List<ArquivoDt> arquivosZip = arquivoNe.consultarArquivosECarta(arquivoZip, ArquivoTipoDt.ID_CORREIO_DEVOLUCAO_AR, dataInicio, dataFim);
		for (ArquivoDt arquivo : arquivosZip) {
			int length = 0;
			byteArrayInputStream = new ByteArrayInputStream(arquivo.getConteudoSemAssinar());
			zipInputStream = new ZipInputStream(byteArrayInputStream, StandardCharsets.UTF_8);
			buffer = new byte[1024];
			while ((zipEntry = zipInputStream.getNextEntry()) != null) {
				if (zipEntry.getName().contains("DevolucaoAR")) {
					while ((length = zipInputStream.read(buffer, 0, 1024)) >= 0) {
						avisoRecebimento.append(new String(buffer, 0, length, StandardCharsets.UTF_8));
					}
				}
			}
			linhasAvisoRecebimento = avisoRecebimento.toString().split("\n");
			for (int i = 0; i < linhasAvisoRecebimento.length; i++) {
				linhaAR = linhasAvisoRecebimento[i].split("\\|"); // [Tipo de Registro|Código do Objeto do Cliente|Número do Lote|Número da Etiqueta|Número do AR|Código da Baixa|Nome da Imagem do AR]
				if(linhaAR.length > 6 && (linhaAR[3].equalsIgnoreCase(codRastreamento) || linhaAR[1].equalsIgnoreCase(idPendencia))) {
					nomeArquivo = linhaAR[6];
					break;
				}
			}
			if (nomeArquivo.length() > 0) {
				byteArrayInputStream = new ByteArrayInputStream(arquivo.getConteudoSemAssinar());
				zipInputStream = new ZipInputStream(byteArrayInputStream, StandardCharsets.UTF_8);
				while ((zipEntry = zipInputStream.getNextEntry()) != null) {
					if (zipEntry.getName().equalsIgnoreCase(nomeArquivo)) {
						byteArrayOutputStream = new ByteArrayOutputStream();
						while ((length = zipInputStream.read(buffer, 0, 1024)) >= 0) {
							byteArrayOutputStream.write(buffer, 0, length);
						}
						arquivoDevolucaoAR = byteArrayOutputStream.toByteArray();
						break;
					}
				}
			}
			if (byteArrayOutputStream != null) byteArrayOutputStream.close();
			if (byteArrayInputStream != null) byteArrayInputStream.close();
			if (zipInputStream != null) zipInputStream.close();
		}
		return arquivoDevolucaoAR;
	}
}
