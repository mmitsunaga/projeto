package br.gov.go.tj.projudi.ne;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import org.apache.axis.utils.StringUtils;

import br.gov.go.tj.projudi.dt.AnaliseConclusaoDt;
import br.gov.go.tj.projudi.dt.AnalisePendenciaDt;
import br.gov.go.tj.projudi.dt.ArquivoDt;
import br.gov.go.tj.projudi.dt.ArquivoTipoDt;
import br.gov.go.tj.projudi.dt.AudienciaMovimentacaoDt;
import br.gov.go.tj.projudi.dt.AudienciaProcessoDt;
import br.gov.go.tj.projudi.dt.ComarcaDt;
import br.gov.go.tj.projudi.dt.GrupoTipoDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.PendenciaArquivoDt;
import br.gov.go.tj.projudi.dt.PendenciaDt;
import br.gov.go.tj.projudi.dt.PendenciaResponsavelDt;
import br.gov.go.tj.projudi.dt.PendenciaStatusDt;
import br.gov.go.tj.projudi.dt.PendenciaTipoDt;
import br.gov.go.tj.projudi.dt.PreAnaliseConclusaoDt;
import br.gov.go.tj.projudi.dt.PreAnalisePendenciaDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ServentiaCargoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.ServentiaTipoDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.dt.dwrMovimentarProcesso;
import br.gov.go.tj.projudi.ps.PendenciaArquivoPs;
import br.gov.go.tj.projudi.ps.PendenciaPs;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.ConverterHTML;
import br.gov.go.tj.utils.ConverterPDF;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;

import com.softwareag.common.resourceutilities.message.MessageException;

public class PendenciaArquivoNe extends PendenciaArquivoNeGen {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7049294185213078733L;

	public String Verificar(PendenciaArquivoDt dados) {
		String stRetorno = "";

		return stRetorno;
	}

	/**
	 * Extrair arquivos
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 23/01/2009 15:27
	 * @param lista
	 *            lista de pendencia arquivo
	 * @return lista de arquivos contidos na lista de pendencias
	 * @throws Exception
	 */
	public static List extrairArquivos(List lista) throws Exception {
		List arquivos = new ArrayList();
		
		if (lista != null){
			Iterator it = lista.iterator();
			
	
			while (it.hasNext()) {
				PendenciaArquivoDt pendenciaArquivoDt = (PendenciaArquivoDt) it.next();
	
				arquivos.add(pendenciaArquivoDt.getArquivoDt());
			}
		}

		return arquivos;
	}

	/**
	 * Consultar somente arquivos assinados que sao problemas
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 23/01/2009 15:18
	 * @param pendenciaDt
	 *            vo da pendencia
	 * @param comArquivos
	 *            adicionar os arquivos
	 * @param conexao
	 *            fabrica para poder continuar a conexao
	 * @return lista de pendencias arquivos com arquivos
	 * @throws Exception
	 */
	public List consultarArquivosAssinadosProblema(PendenciaDt pendenciaDt, boolean comArquivos, FabricaConexao conexao) throws Exception {
		return this.consultarArquivosPendencia(pendenciaDt, null, comArquivos, true, true, conexao);
	}

	/**
	 * Consultar arquivos assinados de uma determinada pendencia com o hash do
	 * arquivo
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 23/01/2009 14:44
	 * @param pendenciaDt
	 *            pojo da pendencia
	 * @param comArquivos
	 *            adicionar na pendencia os arquivos
	 * @param usuarioSessao
	 *            usuario da sessao
	 * @throws Exception
	 */
	public List consultarArquivosAssinadosProblemaComHash(PendenciaDt pendenciaDt, boolean comArquivos, UsuarioNe usuarioSessao) throws Exception{
		List arquivos = this.consultarArquivosPendencia(pendenciaDt, null, comArquivos, true, true, null);

		if (arquivos != null) {
			Iterator iterator = arquivos.iterator();

			while (iterator.hasNext()) {
				PendenciaArquivoDt pendenciaArquivoDt = (PendenciaArquivoDt) iterator.next();

				pendenciaArquivoDt.setHash(usuarioSessao.getCodigoHash(pendenciaArquivoDt.getId()));
			}
		}

		return arquivos;
	}

	/**
	 * Consultar arquivos assinados de uma determinada pendencia com o hash do
	 * arquivo
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 21/01/2009 16:18
	 * @param pendenciaDt
	 *            pojo da pendencia
	 * @param comArquivos
	 *            adicionar na pendencia os arquivos
	 * @param usuarioSessao
	 *            usuario da sessao
	 * @throws Exception
	 */
	public List consultarArquivosProblemaComHash(PendenciaDt pendenciaDt, boolean comArquivos, UsuarioNe usuarioSessao) throws Exception{
		List arquivos = this.consultarArquivosPendencia(pendenciaDt, null, comArquivos, false, true, null);

		if (arquivos != null) {
			Iterator iterator = arquivos.iterator();

			while (iterator.hasNext()) {
				PendenciaArquivoDt pendenciaArquivoDt = (PendenciaArquivoDt) iterator.next();

				pendenciaArquivoDt.setHash(usuarioSessao.getCodigoHash(pendenciaArquivoDt.getId()));
			}
		}

		return arquivos;
	}

	/**
	 * Baixar publicacao publica
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 17/10/2008 10:56
	 * @param PendenciaArquivoDt
	 *            pendenciaArquivoDt, vo de pendencia arquivo
	 * @param HttpServletResponse
	 *            response, resposta
	 * @throws Exception
	 */
	public void baixarPublicacaoPublica(PendenciaArquivoDt pendenciaArquivoDt, HttpServletResponse response) throws Exception{
		// Pesquisa os dados da pendencia
		// Moditivo deste funcionamento sao os dados do log
		PendenciaArquivoDt pendenciaArquivoAuxDt = this.consultarId(pendenciaArquivoDt.getId());
		pendenciaArquivoDt.setId_Pendencia(pendenciaArquivoAuxDt.getId_Pendencia());
		pendenciaArquivoDt.setId_Arquivo(pendenciaArquivoAuxDt.getId_Arquivo());

		PendenciaNe pendenciaNe = new PendenciaNe();
		PendenciaDt pendenciaDt = new PendenciaDt();
		pendenciaDt.setId(pendenciaArquivoDt.getId_Pendencia());

		if (pendenciaNe.verificarPendenciaPublica(pendenciaDt)) {
			ArquivoNe arquivoNe = new ArquivoNe();
			arquivoNe.baixarArquivo(pendenciaArquivoDt.getId_Arquivo(), response, new LogDt(pendenciaArquivoDt.getId_UsuarioLog(), pendenciaArquivoDt.getIpComputadorLog()), false);
		} else {
			throw new MensagemException("Este arquivo não é uma publicação pública.");
		}
	}

	/**
	 * Baixar um arquivo de pendencia
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 11/09/2008 14:57
	 * @param PendenciaArquivoDt
	 *            pendenciaArquivoDt, pojo de pendencia
	 * @param HttpServletResponse
	 *            response, resposta
	 * @param recibo
	 *            se deseja baixar o recibo
	 * @throws Exception
	 */
	public void baixarArquivo(PendenciaArquivoDt pendenciaArquivoDt, HttpServletResponse response) throws Exception{
		// Pesquisa os dados da pendencia
		pendenciaArquivoDt.setId_Arquivo(this.consultarId(pendenciaArquivoDt.getId()).getId_Arquivo());

		ArquivoNe arquivoNe = new ArquivoNe();

		LogDt logDt = new LogDt(pendenciaArquivoDt.getId_UsuarioLog(), pendenciaArquivoDt.getIpComputadorLog());
		
		arquivoNe.baixarArquivo(pendenciaArquivoDt.getId_Arquivo(), response, logDt, false);
		
	}
	
	
	public PendenciaArquivoDt consultarFinalizadaId(String id_PendenciaArquivo ) throws Exception {

		PendenciaArquivoDt dtRetorno=null;
		FabricaConexao obFabricaConexao = null;
		//////System.out.println("..ne-ConsultaId_PendenciaArquivo" );

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			PendenciaArquivoPs obPersistencia = new PendenciaArquivoPs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarFinalizadaId(id_PendenciaArquivo ); 
			obDados.copiar(dtRetorno);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}
	
	/**
	 * Baixar um arquivo de pendencia
	 * 
	 * @param PendenciaArquivoDt
	 *            pendenciaArquivoDt, pojo de pendencia
	 * @param HttpServletResponse
	 *            response, resposta
	 * @param recibo
	 *            se deseja baixar o recibo
	 * @throws Exception
	 */
	public void baixarArquivo(PendenciaArquivoDt pendenciaArquivoDt, HttpServletResponse response,  boolean finalizado) throws Exception{
		// Pesquisa os dados da pendencia
		
		if (finalizado) {
			PendenciaArquivoDt temp = this.consultarFinalizadaId(pendenciaArquivoDt.getId());
			if (temp==null) {
				throw new MessageException("Não foi possível encontrar o arquivo.");
			}
			pendenciaArquivoDt.setId_Arquivo(temp.getId_Arquivo());
		}else {
			PendenciaArquivoDt temp = this.consultarId(pendenciaArquivoDt.getId());
			if (temp==null) {
				throw new MessageException("Não foi possível encontrar o arquivo.");
			}
			pendenciaArquivoDt.setId_Arquivo(temp.getId_Arquivo());
		}
		
		ArquivoNe arquivoNe = new ArquivoNe();

		LogDt logDt = new LogDt(pendenciaArquivoDt.getId_UsuarioLog(), pendenciaArquivoDt.getIpComputadorLog());
		
		arquivoNe.baixarArquivo(pendenciaArquivoDt.getId_Arquivo(), response, logDt, false);
		
	}

	/**
	 * Baixar um arquivo de pendencia
	 * 
	 * @since 26/11/2009
	 * @param PendenciaArquivoDt
	 *            pendenciaArquivoDt, pojo de pendencia
	 * @param HttpServletResponse
	 *            response, resposta
	 * @param recibo
	 *            se deseja baixar o recibo
	 * @throws Exception
	 */
	public void baixarArquivo(String idArquivo, HttpServletResponse response, LogDt logDt) throws Exception , MensagemException{

		ArquivoNe arquivoNe = new ArquivoNe();
		
		arquivoNe.baixarArquivo(idArquivo, response, logDt, false);
	
	}

	/**
	 * Inseri uma lista de arquivos e cria os relacionamentos de pendnecia
	 * arquivo
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 11/09/2008 09:57
	 * @param PendenciaDt
	 *            pendenciaDt, pojo de pendencia
	 * @param Collection
	 *            <ArquivoDt> arquivos, lista de arquivos, [Parametro modificado
	 *            em 16/09/2008 para melhorar compatibilidade]
	 * @param resposta
	 *            especifica o valor (resposta) para todos os vinculos
	 * @return boolean
	 * @throws Exception
	 */
	public boolean inserirArquivos(PendenciaDt pendenciaDt, List arquivos, boolean resposta, boolean aguardandoAssinatura, LogDt logDt) throws Exception{
		PendenciaArquivoDt pendenciaArquivoDt = new PendenciaArquivoDt();
		pendenciaArquivoDt.setId_Pendencia(pendenciaDt.getId());
		
		logDt.setId_Usuario(pendenciaDt.getId_UsuarioLog());
		logDt.setIpComputadorLog(pendenciaDt.getIpComputadorLog());
		return this.inserirArquivos(pendenciaArquivoDt, arquivos, resposta, aguardandoAssinatura, logDt, null);
	}

//	/**
//	 * Inseri uma lista de arquivos e cria os relacionamentos de pendnecia
//	 * arquivo criando ainda a indexação do conteúdo do pdf para cunsulta 
//	 * 
//	 * @author jesus rodrigo
//	 * @since 23/09/2009 13:27
//	 * @param PendenciaDt
//	 *            pendenciaDt, pojo de pendencia
//	 * @param Collection
//	 *            <ArquivoDt> arquivos, lista de arquivos, [Parametro modificado
//	 *            em 16/09/2008 para melhorar compatibilidade]
//	 * @param resposta
//	 *            especifica o valor (resposta) para todos os vinculos
//	 * @param FabricaConexao
//	 *            fabConexao, fabrica de conxao
//	 * @return boolean
//	 * @throws Exception
//	 */
//	public void inserirArquivos Publicacao(PendenciaDt pendenciaDt, List arquivos, boolean resposta, LogDt logDt, FabricaConexao obFabricaConexao) throws Exception {
//		PendenciaArquivoDt pendenciaArquivoDt = new PendenciaArquivoDt();
//		pendenciaArquivoDt.setId_Pendencia(pendenciaDt.getId());
//
//
//		// Modifica a conexao local
//		PendenciaArquivoPs obPersistencia = new PendenciaArquivoPs(obFabricaConexao.getConexao());
//
//		// Inseri todos arquivos em sequencia inserido as pendencias
//		// arquivos
//		Iterator itArquivos = arquivos.iterator();
//
//		// Objetos de negocio
//		ArquivoNe arquivoNe = new ArquivoNe();
//		// crio o obj Arquivo palavra para salvar a indexação
////		ArquivoPalavraNe objArquivoPalavraNe = new ArquivoPalavraNe();
//
//		// Enquanto possuir arquivos a serem inseridos e a resposta ainda
//		// continuar true
//		while (itArquivos.hasNext()) {
//			// Pega o arquivo da lista
//			ArquivoDt arquivo = (ArquivoDt) itArquivos.next();
//
//			// PROVISORIO SOMENTE PARA TESTES
//			// arquivo.setConteudo( arquivo.getArquivo().getBytes() );
//
//			// Inseri o novo arquivo
//			arquivoNe.inserir(arquivo, logDt, obFabricaConexao);
//			//******************************************************
//			//pego o conteudo do arquivo para fazer a indexação
//			byte[] byConteudoArquivo = arquivo.getConteudo();
//			String palavras = "";
//			//transformo o conteudo de byte para pdf
//			if (arquivo.getNomeArquivo().indexOf(".html") > 0) {
//				palavras = ConverterHTML.htmlToString(byConteudoArquivo);
//			}else if (arquivo.getNomeArquivo().indexOf(".pdf") > 0) {
//				palavras = ConverterPDF.pdfToString(byConteudoArquivo);
//			}
//
//			//depois de salvar o arquivo vou fazer a indexação
//			//objArquivoPalavraNe.salvar(arquivo.getId(), palavras, obFabricaConexao);
//			//******************************************************
//
//			// Prepara o Pendencia Arquivo
//			PendenciaArquivoDt pendenciaArquivo = new PendenciaArquivoDt();
//
//			pendenciaArquivo.setId_Pendencia(pendenciaArquivoDt.getId_Pendencia());
//			pendenciaArquivo.setId_Arquivo(arquivo.getId());
//			pendenciaArquivo.setResposta(resposta ? "true" : "false");
//
//			// Inserir a ligacao entre arquivo e pendencia arquivo
//			obPersistencia.inserir(pendenciaArquivo);
//		}
//
//
//	}

	/**
	 * Inseri uma lista de arquivos e cria os relacionamentos de pendnecia
	 * arquivo
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 11/09/2008 09:57
	 * @param PendenciaDt
	 *            pendenciaDt, pojo de pendencia
	 * @param Collection
	 *            <ArquivoDt> arquivos, lista de arquivos, [Parametro modificado
	 *            em 16/09/2008 para melhorar compatibilidade]
	 * @param resposta
	 *            especifica o valor (resposta) para todos os vinculos
	 * @param aguardandoAssinatura
	 * 			  especifica se é para salvar com o vinculo de aguardando assinatura.
	 * @param FabricaConexao
	 *            fabConexao, fabrica de conxao
	 * @return boolean
	 * @throws Exception
	 */
	public boolean inserirArquivos(PendenciaDt pendenciaDt, List arquivos, boolean resposta, boolean aguardandoAssinatura, LogDt logDt, FabricaConexao fabConexao) throws Exception {
		PendenciaArquivoDt pendenciaArquivoDt = new PendenciaArquivoDt();
		pendenciaArquivoDt.setId_Pendencia(pendenciaDt.getId());
		return this.inserirArquivos(pendenciaArquivoDt, arquivos, resposta, aguardandoAssinatura, logDt, fabConexao);
	}

	/**
	 * Vincular arquivos com uma pendencia
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 26/11/2008 09:46
	 * @param pendenciaDt
	 *            vo de pendencia
	 * @param arquivos
	 *            lista de arquivos
	 * @param resposta
	 *            especifica o valor (resposta) para todos os vinculos
	 * @param fabConexao
	 *            fabrica de conexao
	 * @return boolean
	 * @throws Exception
	 */
	public boolean vincularArquivos(PendenciaDt pendenciaDt, List arquivos, boolean resposta, FabricaConexao fabConexao) throws Exception {
	    FabricaConexao obFabricaConexao = null;
		try{
			// Se a fabrica nao foi passada
			if (fabConexao == null) {
				obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
				obFabricaConexao.iniciarTransacao();
			} else {
				obFabricaConexao = fabConexao;
			}

			// Modifica a conexao local
			PendenciaArquivoPs obPersistencia = new PendenciaArquivoPs(obFabricaConexao.getConexao());

			// Inseri todos arquivos em sequencia inserido as pendencias
			// arquivos
			Iterator itArquivos = arquivos.iterator();

			// Enquanto possuir arquivos a serem vinculados e a resposta ainda
			// continuar true
			while (itArquivos.hasNext()) {
				// Pega o arquivo da lista
				ArquivoDt arquivo = (ArquivoDt) itArquivos.next();

				// Verifica se o arquivo esta cadastrado
				if (arquivo.getId() == null || arquivo.getId().trim().equals("")) throw new MensagemException("Erro ao vincular o arquivo, ID não foi especificado");

				// Prepara o Pendencia Arquivo
				PendenciaArquivoDt pendenciaArquivo = new PendenciaArquivoDt();

				pendenciaArquivo.setId_Pendencia(pendenciaDt.getId());
				pendenciaArquivo.setId_Arquivo(arquivo.getId());
				pendenciaArquivo.setResposta(resposta ? "true" : "false");

				// Inserir a ligacao entre arquivo e pendencia arquivo
				obPersistencia.inserir(pendenciaArquivo);
			}

			// Se a fabrica foi criada no metodo
			if (fabConexao == null) {
				obFabricaConexao.finalizarTransacao();
			}

		} catch(Exception e) {
			// Caso ocorra algum erro a transacao e cancelada, se nao foi
			// passado a fabrica de conexao
			if (fabConexao == null) {
				obFabricaConexao.cancelarTransacao();
			}

			throw e;
		} finally{
			// Se a fabrica nao foi passada
			if (fabConexao == null) {
				obFabricaConexao.fecharConexao();
			}
		}

		return true;
	}
	
	// jvosantos - 03/02/2020 19:02 - Tipar Lista
	/**
	 * Retorna os arquivos de uma determinada pendencia
	 * 
	 * @author Leandro Bernardes
	 * @param String
	 *            id_Pendencia, id da pendencia
	 * @return List
	 * @throws Exception
	 */
	public List<ArquivoDt> consultarArquivosPendencia(String id_Pendencia) throws Exception {
		FabricaConexao obFabricaConexao = null;
		try{
			// essea
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			return consultarArquivosPendencia(id_Pendencia, obFabricaConexao);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	// jvosantos - 29/10/2019 13:22 - Tipar lista
	public List consultarArquivosPendencia(String id_Pendencia, FabricaConexao obFabricaConexao) throws Exception {
		List arquivos = null;
		PendenciaArquivoPs obPersistencia = new PendenciaArquivoPs(obFabricaConexao.getConexao());
		arquivos = obPersistencia.consultarArquivosPendencia(id_Pendencia);

		return arquivos;
	}
	
	/**
	 * Retorna os arquivos de uma determinada pendencia finalizada
	 * 
	 * @author hrrosa
	 * @param String
	 *            id_Pendencia, id da pendencia
	 * @return List
	 * @throws Exception
	 */
	public List consultarArquivosPendenciaFinalizada(String id_Pendencia) throws Exception {
		List arquivos = null;
		FabricaConexao obFabricaConexao = null;
		try{
			// essea
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			arquivos = consultarArquivosPendenciaFinalizada(id_Pendencia, obFabricaConexao);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}

		return arquivos;
	}

	public List consultarArquivosPendenciaFinalizada(String id_Pendencia, FabricaConexao obFabricaConexao)
			throws Exception {
		List arquivos;
		PendenciaArquivoPs obPersistencia = new PendenciaArquivoPs(obFabricaConexao.getConexao());
		arquivos = obPersistencia.consultarArquivosPendenciaFinalizada(id_Pendencia);
		return arquivos;
	}

	/**
	 * Este metodo tem como objetivo abrir uma transacao e inserir varios
	 * arquivos em um lote apenas Retorna true: se todos arquivos foram
	 * inseridos com sucesso e a transacao acabou Retorna false: se algum
	 * arquivo nao foi inserido, e cancela toda transacao
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 08/09/2008 14:26
	 * @param PendenciaArquivoDt
	 *            pendenciaArquivoDt, pojo da pendencia arquivo
	 * @param Collection
	 *            <ArquivoDt> arquivos, pojo de arquivo
	 * @param resposta
	 *            especifica o valor (resposta) para todos os vinculos
	 * @param aguardandoAssinatura
	 * @param FabricaConexao
	 *            fabConexao, fabrica de conxao
	 * @return boolean
	 * @throws Exception
	 */
	public boolean inserirArquivos(PendenciaArquivoDt pendenciaArquivoDt, List arquivos, boolean resposta, boolean aguardandoAssinatura, LogDt logDt, FabricaConexao fabConexao) throws Exception {
		// Tudo ocorreu com sucesso ate que se prove o contrario
		boolean retorno = true;
		FabricaConexao obFabricaConexao = null;

		try{
			// Se a fabrica nao foi passada
			if (fabConexao == null) {
				obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
				obFabricaConexao.iniciarTransacao();
			} else {
				obFabricaConexao = fabConexao;
			}

			// Modifica a conexao local
			PendenciaArquivoPs obPersistencia = new PendenciaArquivoPs(obFabricaConexao.getConexao());

			// Inseri todos arquivos em sequencia inserido as pendencias
			// arquivos
			Iterator itArquivos = arquivos.iterator();

			// Objetos de negocio
			ArquivoNe arquivoNe = new ArquivoNe();

			// Enquanto possuir arquivos a serem inseridos e a resposta ainda
			// continuar true
			while (itArquivos.hasNext()) {
				// Pega o arquivo da lista
				ArquivoDt arquivo = (ArquivoDt) itArquivos.next();
				
				if (aguardandoAssinatura && arquivo.getNomeArquivo().toLowerCase().contains(".html"))
				{	
					arquivo.setNomeArquivo(arquivo.getNomeArquivo().toLowerCase().replaceAll(".html", ""));
					arquivo.setNomeArquivo(arquivo.getNomeArquivo()+".html");
				}	
				
				// PROVISORIO SOMENTE PARA TESTES
				// arquivo.setConteudo( arquivo.getArquivo().getBytes() );

				// Inseri o novo arquivo
				arquivoNe.inserir(arquivo, logDt, obFabricaConexao);

				// Prepara o Pendencia Arquivo
				PendenciaArquivoDt pendenciaArquivo = new PendenciaArquivoDt();

				pendenciaArquivo.setId_Pendencia(pendenciaArquivoDt.getId_Pendencia());
				pendenciaArquivo.setId_Arquivo(arquivo.getId());
				pendenciaArquivo.setResposta(resposta ? "true" : "false");
				if (aguardandoAssinatura)
					pendenciaArquivo.setCodigoTemp(String.valueOf(PendenciaArquivoDt.AGUARDANDO_ASSINATURA));

				// Inserir a ligacao entre arquivo e pendencia arquivo
				obPersistencia.inserir(pendenciaArquivo);
			}

			// Se a fabrica foi criada no metodo
			if (fabConexao == null) {
				obFabricaConexao.finalizarTransacao();
			}

		} catch(Exception e) {
			// Caso ocorra algum erro a transacao e cancelada, se nao foi
			// passado a fabrica de conexao
			if (fabConexao == null) {
				obFabricaConexao.cancelarTransacao();
			}

			retorno = false;

			throw e;
		} finally{
			// Se a fabrica nao foi passada
			if (fabConexao == null) {
				obFabricaConexao.fecharConexao();
			}
		}

		return retorno;
	}

	/**
	 * Retorna os arquivos de resposta de uma determinada pendencia
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 15/01/2009 10:42
	 * @param pendenciaArquivoDt
	 *            pojo da pendencia
	 * @return lista de arquivos
	 * @throws Exception
	 */
	public List consultarArquivosResposta(String id_Pendencia) throws Exception {
		List arquivos = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			PendenciaArquivoPs obPersistencia = new PendenciaArquivoPs(obFabricaConexao.getConexao());
			arquivos = obPersistencia.consultarArquivosResposta(id_Pendencia);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}

		return arquivos;
	}

	/**
	 * Retorna os arquivos de uma determinada pendencia
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 08/09/2008 14:04
	 * @param PendenciaArquivoDt
	 *            pendenciaArquivoDt, pojo da pendencia
	 * @return List
	 * @throws Exception
	 */
	public List consultarArquivos(PendenciaArquivoDt pendenciaArquivoDt) throws Exception {
		List arquivos = null;
		FabricaConexao obFabricaConexao = null;
		try{
			// essea
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			PendenciaArquivoPs obPersistencia = new PendenciaArquivoPs(obFabricaConexao.getConexao());
			arquivos = obPersistencia.consultarArquivos(pendenciaArquivoDt);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}

		return arquivos;
	}

	/**
	 * Consulta os arquivos de uma publicacao publica
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 15/10/2008 16:32
	 * @param PendenciaDt
	 *            pendenciaDt
	 * @return List
	 * @throws Exception
	 */
	public List consultarPublicacaoPublica(PendenciaDt pendenciaDt) throws Exception {
		// Verifica se a pendencia e uma pendencia publica
		PendenciaNe pendenciaNe = new PendenciaNe();

		// Se nao for uma pendencia publica retorna uma excessao
		if (!pendenciaNe.verificarPendenciaPublica(pendenciaDt)) throw new MensagemException("Esta pendência não é pública.");

		// Se for retorna os arquivos da pendencia
		return this.consultarPendencia(pendenciaDt, true);
	}

	// jvosantos - 29/10/2019 13:22 - Tipar lista e realizar overload com fabrica
	public List consultarPendencia(PendenciaDt pendenciaDt, boolean comArquivos) throws Exception{
		return this.consultarPendencia(pendenciaDt, comArquivos, null);
	}

	// jvosantos - 29/10/2019 13:22 - Tipar lista e realizar overload com fabrica
	/**
	 * Consulta os arquivos de uma determinada pendencia
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 16/12/2008 09:50
	 * @param pendenciaDt
	 *            pojo da pendencia
	 * @param comArquivos
	 *            adicionar na pendencia os arquivos
	 * @throws Exception
	 */	
	public List consultarPendencia(PendenciaDt pendenciaDt, boolean comArquivos, FabricaConexao fabrica) throws Exception{
		return this.consultarArquivosPendencia(pendenciaDt, null, comArquivos, false, fabrica);
	}

	/**
	 * Consulta os arquivos de resposta de uma determinada pendencia
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 16/12/2008 09:50
	 * @param pendenciaDt
	 *            pojo da pendencia
	 * @param comArquivos
	 *            adicionar na pendencia os arquivos
	 * alteração jesus           
	 * @throws Exception
	 */
	public List consultarPendenciaResposta(PendenciaDt pendenciaDt, boolean comArquivos) throws Exception {
		List arquivos = null;
		FabricaConexao obFabricaConexao = null;
		try{
			
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);							

			PendenciaArquivoPs obPersistencia = new PendenciaArquivoPs(obFabricaConexao.getConexao());

			arquivos = obPersistencia.consultarPendenciaResposta(pendenciaDt, comArquivos);
		
		} finally{
			 obFabricaConexao.fecharConexao();
		}

		return arquivos;
	}
	
	/**
	 * Consulta os arquivos de resposta de uma determinada pendencia
	 * 
	 * @author Jesus Rodrigo
	 * @since 26/08/2014
	 * @param pendenciaDt      pojo da pendencia
	 * @param comArquivos    adicionar na pendencia os arquivos
	 * alteração jesus           
	 * @throws Exception
	 */
	public List consultarPendenciaFinalizadaResposta(PendenciaDt pendenciaDt, boolean comArquivos) throws Exception {
		List arquivos = null;
		FabricaConexao obFabricaConexao = null;
		try{
			
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);							

			PendenciaArquivoPs obPersistencia = new PendenciaArquivoPs(obFabricaConexao.getConexao());

			arquivos = obPersistencia.consultarPendenciaFinalizadaResposta(pendenciaDt, comArquivos);
		
		} finally{
			 obFabricaConexao.fecharConexao();
		}

		return arquivos;
	}

	// jvosantos - 29/10/2019 17:22 - Consultar PendenciaArquivoDt de pendencia finalizada, comArquivos = true não parece funcionar
	public List consultarPendenciaFinalizada(PendenciaDt pendenciaDt, boolean comArquivos, FabricaConexao fabrica) throws Exception {
		PendenciaArquivoPs obPersistencia = new PendenciaArquivoPs(fabrica.getConexao());

		return obPersistencia.consultarPendenciaFinalizada(pendenciaDt, comArquivos);
	}

//	/**
//	 * Consulta os arquivos de resposta de uma determinada pendencia
//	 * 
//	 * @author Ronneesley Moura Teles
//	 * @since 15/01/2009 10:55
//	 * @param pendenciaDt
//	 *            pojo da pendencia
//	 * @param comArquivos
//	 *            adicionar na pendencia os arquivos
//	 * @param conexao
//	 *            conexao para continuar uma transacao ou null para iniciar uma
//	 *            nova
//	 * @throws Exception
//	 */
//	public List consultar PendenciaResposta(PendenciaDt pendenciaDt, boolean comArquivos, FabricaConexao obFabricaConexao) throws Exception {
//		List arquivos = null;
//		
//	PendenciaArquivoPs obPersistencia = new PendenciaArquivoPs(obFabricaConexao.getConexao());
//
//	arquivos = obPersistencia.consultarPendenciaResposta(pendenciaDt, comArquivos);
//
//		return arquivos;
//	}
	// jvosantos - 29/10/2019 13:22 - Tipar lista
	/**
	 * Consulta os arquivos de uma determinada pendencia
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 23/01/2009 14:41
	 * @param pendenciaDt
	 *            pojo da pendencia
	 * @param comArquivos
	 *            adicionar na pendencia os arquivos
	 * @param somenteAssinados
	 *            somente arquivos assinados
	 * @param conexao
	 *            conexao para continuar uma transacao ou null para iniciar uma
	 *            nova
	 * @throws Exception
	 */
	public List consultarArquivosPendencia(PendenciaDt pendenciaDt, UsuarioNe usuarioNe, boolean comArquivos, boolean somenteAssinados, FabricaConexao conexao) throws Exception {
		return this.consultarArquivosPendencia(pendenciaDt, usuarioNe, comArquivos, somenteAssinados, false, conexao);
	}
	
	/**
	 * Consulta os arquivos de uma determinada pendencia
	 * 
	 * @param pendenciaDt
	 *            pojo da pendencia
	 * @param comArquivos
	 *            adicionar na pendencia os arquivos
	 * @param somenteAssinados
	 *            somente arquivos assinados
	 * @param conexao
	 *            conexao para continuar uma transacao ou null para iniciar uma
	 *            nova
	 * @throws Exception
	 */
	public List consultarArquivosPendenciaFinalizada(PendenciaDt pendenciaDt, UsuarioNe usuarioNe, boolean comArquivos, boolean somenteAssinados, FabricaConexao conexao) throws Exception {
		List arquivos = null;
		FabricaConexao obFabricaConexao = null;
		try{
			if (conexao == null) {
				obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			} else {
				obFabricaConexao = conexao;
			}

			PendenciaArquivoPs obPersistencia = new PendenciaArquivoPs(obFabricaConexao.getConexao());

			arquivos = obPersistencia.consultarArquivosPendenciaFinalizada(pendenciaDt, usuarioNe, comArquivos, somenteAssinados, false);
		
		} finally{
			if (conexao == null) obFabricaConexao.fecharConexao();
		}

		return arquivos;
	}
	
	// jvosantos - 29/10/2019 13:22 - Tipar lista
	/**
	 * Consulta os arquivos de uma determinada pendencia
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 02/08/2008 10:52
	 * @param pendenciaDt
	 *            pojo da pendencia
	 * @param comArquivos
	 *            adicionar na pendencia os arquivos
	 * @param somenteAssinados
	 *            somente arquivos assinados
	 * @param somenteProblema
	 *            somente
	 * @param conexao
	 *            conexao para continuar uma transacao ou null para iniciar uma
	 *            nova
	 * @throws Exception
	 */
	public List consultarArquivosPendencia(PendenciaDt pendenciaDt, UsuarioNe usuarioNe, boolean comArquivos, boolean somenteAssinados, boolean somenteProblema, FabricaConexao conexao) throws Exception {
		List arquivos = null;
		FabricaConexao obFabricaConexao = null;
		try{
			if (conexao == null) {
				obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			} else {
				obFabricaConexao = conexao;
			}

			PendenciaArquivoPs obPersistencia = new PendenciaArquivoPs(obFabricaConexao.getConexao());

			arquivos = obPersistencia.consultarPendencia(pendenciaDt, usuarioNe, comArquivos, somenteAssinados, somenteProblema);
		
		} finally{
			if (conexao == null) obFabricaConexao.fecharConexao();
		}

		return arquivos;
	}

	/**
	 * Consultar vinculos com arquivos que sao resposta e assinados
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 05/02/2009 10:52
	 * @param pendenciaDt
	 *            vo de pendencia
	 * @param comConteudo
	 *            com o conteudo do arquivo
	 * @param conexao
	 *            conexao
	 * @return lista de vinculos
	 * @throws Exception
	 */
	public List consultarRespostaAssinados(PendenciaDt pendenciaDt, FabricaConexao conexao) throws Exception {
		return this.consultarRespostaAssinados(pendenciaDt, false, conexao);
	}

	/**
	 * Consultar vinculos com arquivos que sao resposta e assinados
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 04/02/2009 09:23
	 * @param pendenciaDt
	 *            vo de pendencia
	 * @param comConteudo
	 *            com o conteudo do arquivo
	 * @param conexao
	 *            conexao
	 * @return lista de vinculos
	 * @throws Exception
	 */
	public List consultarRespostaAssinados(PendenciaDt pendenciaDt, boolean comConteudo, FabricaConexao conexao) throws Exception {
		List arquivos = null;
		FabricaConexao obFabricaConexao = null;

		try{
			if (conexao == null) {
				obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			} else {
				obFabricaConexao = conexao;
			}

			PendenciaArquivoPs obPersistencia = new PendenciaArquivoPs(obFabricaConexao.getConexao());

			arquivos = obPersistencia.consultarRespostaAssinados(pendenciaDt, comConteudo);
		
		} finally{
			if (conexao == null) obFabricaConexao.fecharConexao();
		}

		return arquivos;
	}
	
	/**
	 * Consultar arquivos anexados a uma publicação
	 * 
	 * @param pendenciaDt - dados da pendência
	 * @param comConteudo - com o conteudo do arquivo
	 * @param conexao - conexao
	 * @return lista de vinculos
	 * @throws Exception
	 * @author hmgodinho
	 */
	public List consultarArquivosAssinadosPublicacao(PendenciaDt pendenciaDt, boolean comConteudo, FabricaConexao conexao) throws Exception {
		List arquivos = null;
		FabricaConexao obFabricaConexao = null;

		try{
			if (conexao == null) {
				obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			} else {
				obFabricaConexao = conexao;
			}

			PendenciaArquivoPs obPersistencia = new PendenciaArquivoPs(obFabricaConexao.getConexao());

			arquivos = obPersistencia.consultarArquivosAssinadosPublicacao(pendenciaDt, comConteudo);
		
		} finally{
			if (conexao == null) obFabricaConexao.fecharConexao();
		}

		return arquivos;
	}

	/**
	 * Consultar vinculos com arquivos que sao resposta e assinados
	 * 
	 * @author Leandro Bernardes
	 * @since 03/09/2009
	 * @param pendenciaDt
	 *            vo de pendencia
	 * @param comConteudo
	 *            com o conteudo do arquivo
	 * @param conexao
	 *            conexao
	 * @return lista de vinculos
	 * @throws Exception
	 */
	public List consultarResposta(PendenciaDt pendenciaDt, boolean comConteudo, FabricaConexao conexao) throws Exception {
		List arquivos = null;
		FabricaConexao obFabricaConexao = null;

		try{
			if (conexao == null) {
				obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			} else {
				obFabricaConexao = conexao;
			}

			PendenciaArquivoPs obPersistencia = new PendenciaArquivoPs(obFabricaConexao.getConexao());

			arquivos = obPersistencia.consultarResposta(pendenciaDt, comConteudo);
		
		} finally{
			if (conexao == null) obFabricaConexao.fecharConexao();
		}

		return arquivos;
	}

//	/**
//	 * Consultar arquivos de resposta de uma determinada pendencia com o hash do
//	 * arquivo
//	 * 
//	 * @author Ronneesley Moura Teles
//	 * @since 15/01/2009 10:59
//	 * @param pendenciaDt
//	 *            pojo da pendencia
//	 * @param comArquivos
//	 *            adicionar na pendencia os arquivos
//	 * @param usuarioSessao
//	 *            usuario da sessao
//	 * @throws Exception
//	 */
//	public List consultar PendenciaRespostaComHash(PendenciaDt pendenciaDt, boolean comArquivos, UsuarioNe usuarioSessao){
//		return this.consultarPendenciaRespostaComHash(pendenciaDt, comArquivos, usuarioSessao, null);
//	}

	/**
	 * Consultar arquivos de resposta de uma determinada pendencia com o hash do
	 * arquivo
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 15/01/2009 10:59
	 * @param pendenciaDt
	 *            pojo da pendencia
	 * @param comArquivos
	 *            adicionar na pendencia os arquivos
	 * @param usuarioSessao
	 *            usuario da sessao
	 * @param conexao
	 *            conexao para poder continuar uma transacao
	 * @throws Exception
	 */
	public List consultarPendenciaRespostaComHash(PendenciaDt pendenciaDt, boolean comArquivos, UsuarioNe usuarioSessao, FabricaConexao conexao) throws Exception {
		
		List arquivos = this.consultarPendenciaResposta(pendenciaDt, comArquivos);

		if (arquivos != null) {
			Iterator iterator = arquivos.iterator();

			while (iterator.hasNext()) {
				PendenciaArquivoDt pendenciaArquivoDt = (PendenciaArquivoDt) iterator.next();

				pendenciaArquivoDt.setHash(usuarioSessao.getCodigoHash(pendenciaArquivoDt.getId()));
			}
		}

		return arquivos;
	}
	
	/**
	 * Consultar arquivos de resposta de uma determinada pendencia com o hash do
	 * arquivo
	 * 
	 * @author Jesus Rodrigo
	 * @since 26/08/2014
	 * @param pendenciaDt
	 *            pojo da pendencia
	 * @param comArquivos
	 *            adicionar na pendencia os arquivos
	 * @param usuarioSessao
	 *            usuario da sessao
	 * @param conexao
	 *            conexao para poder continuar uma transacao
	 * @throws Exception
	 */
	public List consultarPendenciaFinalizadaRespostaComHash(PendenciaDt pendenciaDt, boolean comArquivos, UsuarioNe usuarioSessao, FabricaConexao conexao) throws Exception {
		
		List arquivos = this.consultarPendenciaFinalizadaResposta(pendenciaDt, comArquivos);

		if (arquivos != null) {
			Iterator iterator = arquivos.iterator();

			while (iterator.hasNext()) {
				PendenciaArquivoDt pendenciaArquivoDt = (PendenciaArquivoDt) iterator.next();

				pendenciaArquivoDt.setHash(usuarioSessao.getCodigoHash(pendenciaArquivoDt.getId()));
			}
		}

		return arquivos;
	}

	/**
	 * Consultar arquivos de resposta de uma determinada conclusão com o hash do
	 * arquivo
	 * 
	 * @author msapaula
	 * @param id_Pendencia, id da pendencia
	 * @param usuarioSessao, usuario da sessao
	 * @param conexao, conexao para poder continuar uma transacao
	 * @throws Exception
	 */
	public List consultarArquivosRespostaConclusao(String id_Pendencia, UsuarioNe usuarioSessao) throws Exception {
		List arquivos = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			PendenciaArquivoPs obPersistencia = new PendenciaArquivoPs(obFabricaConexao.getConexao());

			arquivos = obPersistencia.consultarArquivosRespostaConclusao(id_Pendencia);

			if (arquivos != null) {
				Iterator iterator = arquivos.iterator();
				while (iterator.hasNext()) {
					PendenciaArquivoDt pendenciaArquivoDt = (PendenciaArquivoDt) iterator.next();
					pendenciaArquivoDt.setHash(usuarioSessao.getCodigoHash(pendenciaArquivoDt.getId()));
				}
			}
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return arquivos;
	}
	
	/**
	 * Consultar arquivos de resposta de uma determinada conclusão com o hash do
	 * arquivo
	 * 
	 * @author Jesus Rodrigo
	 * @param id_Pendencia, id da pendencia
	 * @param usuarioSessao, usuario da sessao
	 * @param conexao, conexao para poder continuar uma transacao
	 * @throws Exception
	 * 26/08/2014
	 */
	public List consultarArquivosRespostaConclusaoFinalizada(String id_Pendencia, UsuarioNe usuarioSessao) throws Exception {
		List arquivos = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			PendenciaArquivoPs obPersistencia = new PendenciaArquivoPs(obFabricaConexao.getConexao());

			arquivos = obPersistencia.consultarArquivosRespostaConclusaoFinalizada(id_Pendencia);

			if (arquivos != null) {
				Iterator iterator = arquivos.iterator();
				while (iterator.hasNext()) {
					PendenciaArquivoDt pendenciaArquivoDt = (PendenciaArquivoDt) iterator.next();
					pendenciaArquivoDt.setHash(usuarioSessao.getCodigoHash(pendenciaArquivoDt.getId()));
				}
			}
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return arquivos;
	}
	
	
	
	/**
	 * Consultar arquivos de uma determinada pendencia com o hash do arquivo
	 * 
	 * 
	 * mrbatista - 03/09/2019 11:00 - Adicionar fabrica na chamada, já que temos uma instanciada
	 */
	
	public List consultarArquivosPendenciaComHash(PendenciaDt pendenciaDt, boolean comArquivos, boolean somenteAssinados, UsuarioNe usuarioSessao) throws Exception{
		return consultarArquivosPendenciaComHash(pendenciaDt, comArquivos, somenteAssinados, usuarioSessao, null);
	}

	/**
	 * Consultar arquivos de uma determinada pendencia com o hash do arquivo
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 10/11/2008 10:06
	 * @param pendenciaDt
	 *            pojo da pendencia
	 * @param comArquivos
	 *            adicionar na pendencia os arquivos
	 * @param somenteAssinados
	 *            somente arquivos assinados
	 * @param UsuarioNe
	 *            usuarioSessao, usuario da sessao
	 * @throws Exception
	 */
	
	public List consultarArquivosPendenciaComHash(PendenciaDt pendenciaDt, boolean comArquivos, boolean somenteAssinados, UsuarioNe usuarioSessao, FabricaConexao conexao) throws Exception{
		List arquivos = this.consultarArquivosPendencia(pendenciaDt, usuarioSessao, comArquivos, somenteAssinados,conexao);

		if (arquivos != null) {
			Iterator iterator = arquivos.iterator();

			while (iterator.hasNext()) {
				PendenciaArquivoDt pendenciaArquivoDt = (PendenciaArquivoDt) iterator.next();

				pendenciaArquivoDt.setHash(usuarioSessao.getCodigoHash(pendenciaArquivoDt.getId()));
				// Seta se arquivo da pendência é válido ou não
				if (!pendenciaArquivoDt.getCodigoTemp().equals(String.valueOf(PendenciaArquivoDt.NORMAL))) pendenciaArquivoDt.setValido(false);
			}
		}
		
		if(pendenciaDt.isElaboracaoVoto() && usuarioSessao.getUsuarioDt().isTurmaJulgadora()){
			
			List arquivosTemp = new ArrayList();
			for(int i = (arquivos.size() - 1) ; i >= 0; i--){

				PendenciaArquivoDt arquivo = (PendenciaArquivoDt)arquivos.get(i);
				
				if(!arquivo.getArquivoDt().isArquivoConfiguracao()){
					arquivosTemp.add(arquivo);	
				}
				
			}
			
			arquivos = arquivosTemp;
			
		}

		return arquivos;
	}
	
	/**
	 * Consultar arquivos de uma determinada pendencia com o hash do arquivo
	 * 
	 * @param pendenciaDt
	 *            pojo da pendencia
	 * @param comArquivos
	 *            adicionar na pendencia os arquivos
	 * @param somenteAssinados
	 *            somente arquivos assinados
	 * @param UsuarioNe
	 *            usuarioSessao, usuario da sessao
	 * @throws Exception
	 */
	public List consultarArquivosPendenciaFinalizadaComHash(PendenciaDt pendenciaDt, boolean comArquivos, boolean somenteAssinados, UsuarioNe usuarioSessao) throws Exception{
		List arquivos = this.consultarArquivosPendenciaFinalizada(pendenciaDt, usuarioSessao, comArquivos, somenteAssinados, null);

		if (arquivos != null) {
			Iterator iterator = arquivos.iterator();

			while (iterator.hasNext()) {
				PendenciaArquivoDt pendenciaArquivoDt = (PendenciaArquivoDt) iterator.next();

				pendenciaArquivoDt.setHash(usuarioSessao.getCodigoHash(pendenciaArquivoDt.getId()));
				// Seta se arquivo da pendência é válido ou não
				if (!pendenciaArquivoDt.getCodigoTemp().equals(String.valueOf(PendenciaArquivoDt.NORMAL))) pendenciaArquivoDt.setValido(false);
			}
		}

		return arquivos;
	}

	/**
	 * Consultar arquivos assinados de uma determinada pendencia com o hash do
	 * arquivo
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 21/01/2009 16:18
	 * @param pendenciaDt
	 *            pojo da pendencia
	 * @param comArquivos
	 *            adicionar na pendencia os arquivos
	 * @param usuarioSessao
	 *            usuario da sessao
	 * @throws Exception
	 */
	public List consultarArquivosAssinadosComHash(PendenciaDt pendenciaDt, boolean comArquivos, UsuarioNe usuarioSessao) throws Exception{
		List arquivos = this.consultarArquivosPendencia(pendenciaDt, null, comArquivos, true, null);

		if (arquivos != null) {
			Iterator iterator = arquivos.iterator();

			while (iterator.hasNext()) {
				PendenciaArquivoDt pendenciaArquivoDt = (PendenciaArquivoDt) iterator.next();

				pendenciaArquivoDt.setHash(usuarioSessao.getCodigoHash(pendenciaArquivoDt.getId()));
				// Seta se arquivo da pendência é válido ou não
				if (!pendenciaArquivoDt.getCodigoTemp().equals(String.valueOf(PendenciaArquivoDt.NORMAL))) pendenciaArquivoDt.setValido(false);
			}
		}

		return arquivos;
	}
	
	// jvosantos - 29/10/2019 13:23 - Alterar private para public
	/**
	 * Método para salvar PendenciaArquivo, que recebe uma conexão como
	 * parâmetro
	 * 
	 * @throws Exception
	 */
	public void salvar(PendenciaArquivoDt dados, FabricaConexao obFabricaConexao) throws Exception {
		LogDt obLogDt;
		
		PendenciaArquivoPs obPersistencia = new PendenciaArquivoPs(obFabricaConexao.getConexao());
		if (dados.getId().equalsIgnoreCase("")) {
			obPersistencia.inserir(dados);
			obLogDt = new LogDt("PendenciaArquivo", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir), "", dados.getPropriedades());
		} else {
			obPersistencia.alterar(dados);
			obLogDt = new LogDt("PendenciaArquivo", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar), obDados.getPropriedades(), dados.getPropriedades());
		}
		obDados.copiar(dados);
		obLog.salvar(obLogDt, obFabricaConexao);
		
	}

	/**
	 * Adiciona arquivo de uma pendencia
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 24/06/2008 14:43
	 * @param PendenciaDt
	 *            pendenciaDt, pojo da pendencia
	 * @param String
	 *            conteudo, dados do arquivo
	 * @param UsuarioDt
	 *            usuarioDt, pojo do usuario
	 * @deprecated
	 * @throws Exception
	 */
	public void adicionarOnline(PendenciaDt pendenciaDt, String conteudo, UsuarioDt usuarioDt) throws Exception {
	    FabricaConexao obFabricaConexao = null;
		try{
			PendenciaArquivoDt pendenciaArquivoDt = new PendenciaArquivoDt();

			pendenciaArquivoDt.setId_Pendencia(pendenciaDt.getId());
			// DEVE SER CORRIGIDO
			/*
			 * pendenciaArquivoDt.setContentType("text/html");
			 * pendenciaArquivoDt.setArquivo(conteudo);
			 * pendenciaArquivoDt.setUsuarioAssinador(usuarioDt.getId_UsuarioServentia());
			 * pendenciaArquivoDt.setDataInsercao(Funcoes.DataHora(new Date()));
			 * pendenciaArquivoDt.setArquivoTipoCodigo(String.valueOf(ArquivoTipoDt.ARQUIVO_ONLINE));
			 */

			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);

			PendenciaArquivoPs obPersistencia = new PendenciaArquivoPs(obFabricaConexao.getConexao());
			obPersistencia.inserir(pendenciaArquivoDt);

		
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}

	/**
	 * Adiciona um arquivo em um pendencia
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 04/07/2008 16:33
	 * @param PendenciaDt
	 *            pendenciaDt, pojo de pendencia
	 * @param String
	 *            arquivoTipo, tipo do arquivo
	 * @param String
	 *            conteudo, conteudo
	 * @param String
	 *            contentType, tipo de conteudo
	 * @param UsuarioDt
	 *            usuarioDt, pojo de usuario
	 * @deprecated
	 */
	public void adicionarArquivo(PendenciaDt pendenciaDt, String arquivoTipo, String conteudo, String tipoConteudo, UsuarioDt usuarioDt) throws Exception {
	    FabricaConexao obFabricaConexao = null;
		try{
			PendenciaArquivoDt pendenciaArquivoDt = new PendenciaArquivoDt();

			pendenciaArquivoDt.setId_Pendencia(pendenciaDt.getId());
			// DEVE SER CORRIGIDO
			/*
			 * pendenciaArquivoDt.setContentType(tipoConteudo);
			 * pendenciaArquivoDt.setArquivo(conteudo);
			 * pendenciaArquivoDt.setUsuarioAssinador(usuarioDt.getId_UsuarioServentia());
			 * pendenciaArquivoDt.setDataInsercao(Funcoes.DataHora(new Date()));
			 * pendenciaArquivoDt.setArquivoTipoCodigo(arquivoTipo);
			 */

			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);

			PendenciaArquivoPs obPersistencia = new PendenciaArquivoPs(obFabricaConexao.getConexao());
			obPersistencia.inserir(pendenciaArquivoDt);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}

	/**
	 * Salva a pré-análise de uma pendência, gerando o arquivo de configuração e
	 * salvando o arquivo com o texto da pré-análise. Método estruturado para
	 * tratar múltiplas pré-analises
	 * 
	 * @param preAnaliseConclusaoDt, dt com os dados da pré-análise
	 * @param usuarioDt, usuário que está realizando a pré-análise
	 * 
	 * @return String, retorna o id_Aquivo da pré-analise, informação importante para distribuir uma pre-analise multipla 
	 * 
	 * @author msapaula
	 */
	public String salvarPreAnaliseConclusao(AnaliseConclusaoDt preAnaliseConclusaoDt, UsuarioDt usuarioDt) throws Exception {
	    FabricaConexao obFabricaConexao = null;
	    String retorno = "";
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			
			retorno = salvarPreAnaliseConclusao(preAnaliseConclusaoDt, usuarioDt, obFabricaConexao);

			obFabricaConexao.finalizarTransacao();
			
			return retorno;
			
		
		} finally{
			obFabricaConexao.fecharConexao();
			
		}
	}
	
	/**
	 * Salva a pré-análise de uma pendência, gerando o arquivo de configuração e
	 * salvando o arquivo com o texto da pré-análise. Método estruturado para
	 * tratar múltiplas pré-analises
	 * 
	 * @param preAnaliseConclusaoDt, dt com os dados da pré-análise	  
	 * @param usuarioDt, usuário que está realizando a pré-análise
	 * @param obFabricaConexao, conexão com o banco de dados
	 * 
	 * @return String, retorna o id_Aquivo da pré-analise, informação importante para distribuir uma pre-analise multipla 
	 * 
	 * @author mmgomes (Refatoração)
	 */
	public String salvarPreAnaliseConclusao(AnaliseConclusaoDt preAnaliseConclusaoDt, UsuarioDt usuarioDt, FabricaConexao obFabricaConexao) throws Exception {	    

		// Pega o arquivo da pré-análise simples ou múltipla
		PendenciaArquivoDt preAnalise = preAnaliseConclusaoDt.getArquivoPreAnalise();

		if (preAnalise == null) {
			return inserirPreAnaliseConclusao(preAnaliseConclusaoDt, usuarioDt, obFabricaConexao);
		} else {
			return alterarPreAnaliseConclusao(preAnaliseConclusaoDt, preAnalise, usuarioDt, obFabricaConexao);
		}
		
	}

	/**
	 * Método responsavel em inserir pré-analise simples ou múltipla para pendências do tipo Conclusão.
	 * 
	 * @param preAnaliseConclusaoDt, objeto com dados da pré-analise
	 * @param usuarioDt, usuario pré-analisador
	 * @param conexao, conexão com o banco de dados
	 * 
	 * @return String, retorna o id_Aquivo da pré-analise, informação importante para distribuir uma pre-analise multipla 
	 * 
	 * @author msapaula
	 * @throws Exception 
	 */
	private String inserirPreAnaliseConclusao(AnaliseConclusaoDt preAnaliseConclusaoDt, UsuarioDt usuarioDt, FabricaConexao conexao) throws Exception{

		// Salva arquivo da pré-analise (configuração e texto redigido)
		ArquivoDt arquivoConfiguracao = salvaArquivoConfiguracaoPreAnalise(preAnaliseConclusaoDt, true, conexao);
		ArquivoDt arquivoPreAnalise = salvaArquivoPreAnaliseConclusao(preAnaliseConclusaoDt, true, conexao);
		PendenciaNe pendenciaNe = new PendenciaNe();

		List pendenciasFechar = preAnaliseConclusaoDt.getListaPendenciasFechar();
		if (pendenciasFechar!=null){
			// Para cada pendência a ser fechada
			for (int i = 0; i < pendenciasFechar.size(); i++) {
				PendenciaDt pendenciaDt = (PendenciaDt) pendenciasFechar.get(i);
				
				//Verifica se já não foi inserida uma pré-análise para a conclusão. Foi necessário incluir essa verificação pois acontecia de assistentes
				//pré-analisarem o mesmo processo ao mesmo tempo, duplicando assim os arquivos de pré-análise.
				if (this.verificaPreAnaliseConclusao(pendenciaDt.getId(), conexao)){
					throw new MensagemException("Já foi inserida uma pré-análise para essa pendência. Efetue a consulta de Não analisadas novamente.");
				}
	
				// Vincula arquivos a pendência. Somente arquivo da pré-analise é do tipo resposta
				vincularPendenciaArquivo(pendenciaDt, arquivoConfiguracao, false, false, conexao);
				vincularPendenciaArquivo(pendenciaDt, arquivoPreAnalise, true, preAnaliseConclusaoDt.isPendenteAssinatura(), conexao);
	
				// Se usuário é assistente, adiciona ele como responsável pela  pendência
				//if (Funcoes.StringToInt(usuarioDt.getGrupoCodigo()) == GrupoDt.ASSISTENTES_JUIZES_VARA || Funcoes.StringToInt(usuarioDt.getGrupoCodigo()) == GrupoDt.ASSISTENTES_JUIZES_SEGUNDO_GRAU) {
				if (Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo()) == GrupoTipoDt.ASSESSOR_JUIZ_VARA_TURMA || 
					Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo()) == GrupoTipoDt.ASSESSOR_PRESIDENTE_SEGUNDO_GRAU ||
					Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo()) == GrupoTipoDt.ASSESSOR_DESEMBARGADOR ||
					Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo()) == GrupoTipoDt.JUIZ_LEIGO ) {
					
					if (pendenciaDt.getResponsaveis() == null || pendenciaDt.getResponsaveis().size() == 0) 
						pendenciaDt.setResponsaveis(pendenciaNe.consultarResponsaveis(pendenciaDt));
					
					if (!pendenciaDt.isResponsavelUsuarioServentia(usuarioDt.getId_UsuarioServentia())) {
						
						PendenciaResponsavelNe responsavelNe = new PendenciaResponsavelNe();
						LogDt logDt = new LogDt(preAnaliseConclusaoDt.getId_UsuarioLog(), preAnaliseConclusaoDt.getIpComputadorLog());
						PendenciaResponsavelDt responsavel = getResponsavelPreAnalise(pendenciaDt, usuarioDt.getId_UsuarioServentia(), "", logDt); 
						responsavelNe.salvar(responsavel, conexao);
						pendenciaDt.addResponsavel(responsavel);
					}
				}
				// Atualiza Classificador processo
				//if (preAnaliseConclusaoDt.getId_Classificador().length() > 0) {
				//ProcessoNe processoNe = new ProcessoNe();
				//processoNe.alterarClassificadorProcesso(pendenciaDt.getId_Processo(), "", preAnaliseConclusaoDt.getId_Classificador(), new LogDt(usuarioDt.getId(), preAnaliseConclusaoDt.getIpComputadorLog()), conexao);
				//}
				
				if (preAnaliseConclusaoDt.getListaPendenciasFechar() != null &&  preAnaliseConclusaoDt.getListaPendenciasFechar().size() > 0) {
					//ALTERAR CLASSIFICADOR DA CONCLUSÃO***********************************************************************
					pendenciaNe.alterarClassificadorPendencia(((PendenciaDt) preAnaliseConclusaoDt.getListaPendenciasFechar().get(0)).getId(), "", preAnaliseConclusaoDt.getId_Classificador(), new LogDt(usuarioDt.getId(), preAnaliseConclusaoDt.getIpComputadorLog()), conexao);
			    	//*********************************************************************************************************	
				}			
			}
		}
		return arquivoPreAnalise.getId();
	}

	/**
	 * Método responsável em alterar uma pré-analise registrada para Conclusões, seja essa simples ou múltipla.
	 * 
	 * @param preAnaliseConclusaoDt, objeto com dados da pré-análise
	 * @param preAnalise, PendenciaArquivoDt com os dados da pré-analise anterior
	 * @param usuarioDt, usuário que está alterando
	 * @param obFabricaConexao, conexão com o banco de dados
	 * 
	 * @return String, retorna o id_Aquivo da pré-analise, informação importante para distribuir uma pre-analise multipla 
	 * 
	 * @author msapaula
	 * @throws Exception 
	 */
	private String alterarPreAnaliseConclusao(AnaliseConclusaoDt preAnaliseConclusaoDt, PendenciaArquivoDt preAnalise, UsuarioDt usuarioDt, FabricaConexao conexao) throws Exception{
		String retorno = "";
		if (geraHistoricoSimplesConclusao(preAnaliseConclusaoDt, usuarioDt)){	
			PendenciaNe pendenciaNe = new PendenciaNe();
			PendenciaResponsavelNe responsavelNe = new PendenciaResponsavelNe();
			
			// Cria novos arquivos
			ArquivoDt arquivoConfiguracao = salvaArquivoConfiguracaoPreAnalise(preAnaliseConclusaoDt, true, conexao);
			ArquivoDt arquivoPreAnalise = salvaArquivoPreAnaliseConclusao(preAnaliseConclusaoDt, true, conexao);

			List pendenciasFechar = preAnaliseConclusaoDt.getListaPendenciasFechar();
			if (pendenciasFechar != null){
				AudienciaProcessoPendenciaNe audienciaProcessoPendenciaNe = new AudienciaProcessoPendenciaNe();
				String idAudiProc = null;
				// Para cada pendência a ser fechada
				for (int i = 0; i < pendenciasFechar.size(); i++) {
					PendenciaDt pendenciaDt = (PendenciaDt) pendenciasFechar.get(i);

					PendenciaDt pendenciaFilha = criarPendenciaFilha(pendenciaDt, usuarioDt, new LogDt(preAnaliseConclusaoDt.getId_UsuarioLog(), preAnaliseConclusaoDt.getIpComputadorLog()), conexao);
					
					if(StringUtils.isEmpty(idAudiProc)) {
						idAudiProc = audienciaProcessoPendenciaNe.consultarPorIdPend(pendenciaDt.getId());
					}
					
					if(idAudiProc != null)
						audienciaProcessoPendenciaNe.salvar(pendenciaFilha.getId(), idAudiProc, conexao);
					
					// Vincula arquivos de pré-analise a pendência filha
					vincularPendenciaArquivo(pendenciaFilha, arquivoConfiguracao, false, false, conexao);
					vincularPendenciaArquivo(pendenciaFilha, arquivoPreAnalise, true, preAnaliseConclusaoDt.isPendenteAssinatura(), conexao);

					// Consulta os arquivos problema da pendência pai que devem
					// ser vinculados também a pendencia filha
					List arquivos = PendenciaArquivoNe.extrairArquivos(this.consultarArquivosAssinadosProblema(pendenciaDt, true, conexao));
					this.vincularArquivos(pendenciaFilha, arquivos, false, conexao);

					//Atualiza os responsáveis
					List responsaveisAtuais = pendenciaNe.consultarResponsaveis(pendenciaDt);
					if (responsaveisAtuais != null) {
						for(int j = 0; j < responsaveisAtuais.size() ; j++) {
							PendenciaResponsavelDt responsavelAtual = (PendenciaResponsavelDt)responsaveisAtuais.get(j);
							
							if (responsavelAtual != null && 
								responsavelAtual.getId_ServentiaCargo() != null && 
								responsavelAtual.getId_ServentiaCargo().trim().length() > 0 &&
								!pendenciaFilha.isResponsavelServentiaCargo(responsavelAtual.getId_ServentiaCargo())) {
								
								LogDt logDt = new LogDt(preAnaliseConclusaoDt.getId_UsuarioLog(), preAnaliseConclusaoDt.getIpComputadorLog());
								PendenciaResponsavelDt responsavel = getResponsavelPreAnalise(pendenciaFilha, "", responsavelAtual.getId_ServentiaCargo(), logDt); 
								responsavelNe.salvar(responsavel, conexao);
								pendenciaFilha.addResponsavel(responsavel);
							}
						}	
					}
					
					if (Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo()) == GrupoTipoDt.ASSESSOR_JUIZ_VARA_TURMA || 
						Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo()) == GrupoTipoDt.ASSESSOR_PRESIDENTE_SEGUNDO_GRAU ||
						Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo()) == GrupoTipoDt.ASSESSOR_DESEMBARGADOR) {
						
						if (!pendenciaFilha.isResponsavelUsuarioServentia(usuarioDt.getId_UsuarioServentia())) {								
							LogDt logDt = new LogDt(preAnaliseConclusaoDt.getId_UsuarioLog(), preAnaliseConclusaoDt.getIpComputadorLog());
							PendenciaResponsavelDt responsavel = getResponsavelPreAnalise(pendenciaFilha, usuarioDt.getId_UsuarioServentia(), "", logDt); 
							responsavelNe.salvar(responsavel, conexao);
							pendenciaFilha.addResponsavel(responsavel);
						}
					}
					
					if (pendenciaFilha != null)
					{
						//ALTERAR CLASSIFICADOR DA CONCLUSÃO***********************************************************************
				    	new PendenciaNe().alterarClassificadorPendencia(pendenciaFilha.getId(), "", preAnaliseConclusaoDt.getId_Classificador(), new LogDt(usuarioDt.getId(), preAnaliseConclusaoDt.getIpComputadorLog()), conexao);
				    	//*********************************************************************************************************
				    	preAnaliseConclusaoDt.addPendenciasGeradas(pendenciaFilha);
					}
				}
			}
			
		} else {
			boolean alterarArquivos = false;
			
			// Se juiz está alterando a pré-análise
			//if (Funcoes.StringToInt(usuarioDt.getGrupoCodigo()) == GrupoDt.JUIZES_VARA || Funcoes.StringToInt(usuarioDt.getGrupoCodigo()) == GrupoDt.JUIZES_TURMA_RECURSAL) {
			if (Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo()) == GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU 
					|| Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo()) == GrupoTipoDt.JUIZ_TURMA
					|| Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo()) == GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU
					|| Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo()) == GrupoTipoDt.PRESIDENTE_SEGUNDO_GRAU) {
				// Se a pré analise tinha sido feita por assistente, será criada uma
				// pendência filha, fechando a atual.
				if (!preAnalise.getAssistenteResponsavel().equals("")) {

					// Cria novos arquivos
					ArquivoDt arquivoConfiguracao = salvaArquivoConfiguracaoPreAnalise(preAnaliseConclusaoDt, true, conexao);
					ArquivoDt arquivoPreAnalise = salvaArquivoPreAnaliseConclusao(preAnaliseConclusaoDt, true, conexao);

					List pendenciasFechar = preAnaliseConclusaoDt.getListaPendenciasFechar();
					if (pendenciasFechar != null && pendenciasFechar.size() > 0){
						// Para cada pendência a ser fechada
						for (int i = 0; i < pendenciasFechar.size(); i++) {
							PendenciaDt pendenciaDt = (PendenciaDt) pendenciasFechar.get(i);
		
							PendenciaDt pendenciaFilha = criarPendenciaFilha(pendenciaDt, usuarioDt, new LogDt(preAnaliseConclusaoDt.getId_UsuarioLog(), preAnaliseConclusaoDt.getIpComputadorLog()), conexao);
		
							// Vincula arquivos de pré-analise a pendência filha
							vincularPendenciaArquivo(pendenciaFilha, arquivoConfiguracao, false, false, conexao);
							vincularPendenciaArquivo(pendenciaFilha, arquivoPreAnalise, true, preAnaliseConclusaoDt.isPendenteAssinatura(), conexao);
		
							// Consulta os arquivos problema da pendência pai que devem
							// ser vinculados também a pendencia filha
							List arquivos = PendenciaArquivoNe.extrairArquivos(this.consultarArquivosAssinadosProblema(pendenciaDt, true, conexao));
							this.vincularArquivos(pendenciaFilha, arquivos, false, conexao);
							
							preAnaliseConclusaoDt.addPendenciasGeradas(pendenciaFilha);
						}
					} else alterarArquivos = true;

				} else alterarArquivos = true;

			//} else if (Funcoes.StringToInt(usuarioDt.getGrupoCodigo()) == GrupoDt.ASSISTENTES_JUIZES_VARA || Funcoes.StringToInt(usuarioDt.getGrupoCodigo()) == GrupoDt.ASSISTENTES_JUIZES_SEGUNDO_GRAU) alterarArquivos = true;
			} else if (Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo()) == GrupoTipoDt.ASSESSOR_JUIZ_VARA_TURMA 
					|| Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo()) == GrupoTipoDt.ASSISTENTE_GABINETE 
					|| Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo()) == GrupoTipoDt.ASSISTENTE_GABINETE_FLUXO 
					|| Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo()) == GrupoTipoDt.ASSESSOR_PRESIDENTE_SEGUNDO_GRAU
					|| Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo()) == GrupoTipoDt.ASSESSOR_DESEMBARGADOR) 
				alterarArquivos = true;
				
			if (alterarArquivos) {
				// Significa que juiz ou assistente está alterando uma pré-analise simples feita por ele mesmo
				if (preAnaliseConclusaoDt.getArquivoConfiguracaoPreAnalise() != null) {
					salvaArquivoConfiguracaoPreAnalise(preAnaliseConclusaoDt, false, conexao);
				} else {
					//Alteração para compatibilidade com pré-analises antigas onde não havia o arquivo de configuração e podia refazer
					//Salva o arquivo de configuração
					ArquivoDt arquivoConfiguracao = salvaArquivoConfiguracaoPreAnalise(preAnaliseConclusaoDt, true, conexao);
					if (preAnaliseConclusaoDt.getListaPendenciasFechar() != null && preAnaliseConclusaoDt.getListaPendenciasFechar().size() > 0)
					{
						// Vincula arquivo à pendência
						vincularPendenciaArquivo((PendenciaDt) preAnaliseConclusaoDt.getListaPendenciasFechar().get(0), arquivoConfiguracao, false, false, conexao);	
					}				
				}
				salvaArquivoPreAnaliseConclusao(preAnaliseConclusaoDt, false, conexao);
				
				// Atualiza status da pré-analise
				if (preAnaliseConclusaoDt.isPendenteAssinatura()) this.atualizeStatusPreAnalisesConclusaoSimplesParaPendentesAssinatura(preAnaliseConclusaoDt.getArquivoPreAnalise().getId_Pendencia(), conexao);
				else this.atualizeStatusPreAnalisesConclusaoSimplesParaNaoPendentesAssinatura(preAnaliseConclusaoDt.getArquivoPreAnalise().getId_Pendencia(), conexao);
				
				retorno = preAnaliseConclusaoDt.getArquivoPreAnalise().getId_Arquivo();
			}
			
			// Atualiza Classificador processo
			//if (preAnaliseConclusaoDt.getId_Classificador().length() > 0 && preAnaliseConclusaoDt.getListaPendenciasFechar() != null && preAnaliseConclusaoDt.getListaPendenciasFechar().size() > 0) {
			//ProcessoNe processoNe = new ProcessoNe();
			//processoNe.alterarClassificadorProcesso(((PendenciaDt) preAnaliseConclusaoDt.getListaPendenciasFechar().get(0)).getId_Processo(), "", preAnaliseConclusaoDt.getId_Classificador(), new LogDt(usuarioDt.getId(), preAnaliseConclusaoDt.getIpComputadorLog()), conexao);
			//}
			
			if (preAnaliseConclusaoDt.getListaPendenciasFechar() != null && preAnaliseConclusaoDt.getListaPendenciasFechar().size() > 0)
			{
				//ALTERAR CLASSIFICADOR DA CONCLUSÃO***********************************************************************
		    	new PendenciaNe().alterarClassificadorPendencia(((PendenciaDt) preAnaliseConclusaoDt.getListaPendenciasFechar().get(0)).getId(), "", preAnaliseConclusaoDt.getId_Classificador(), new LogDt(usuarioDt.getId(), preAnaliseConclusaoDt.getIpComputadorLog()), conexao);
		    	//*********************************************************************************************************	
			}
		}
		return retorno;
	}
	
	private boolean geraHistoricoSimplesConclusao(AnaliseConclusaoDt preAnaliseConclusaoDt, UsuarioDt usuarioDt) {
		
		if (preAnaliseConclusaoDt != null &&
			preAnaliseConclusaoDt.getListaPendenciasFechar() != null && 
			preAnaliseConclusaoDt.getListaPendenciasFechar().size() > 0 && 
			preAnaliseConclusaoDt.getListaPendenciasFechar().get(0) != null && 
			((PendenciaDt) preAnaliseConclusaoDt.getListaPendenciasFechar().get(0)).isConclusao() &&			
			(!usuarioDt.isGabinetePresidenciaTjgo() &&
			!usuarioDt.isGabineteVicePresidenciaTjgo() && 
			!usuarioDt.isGabineteUPJ() )
			) return true;
		
		return false;
	}

	public void descartarPreAnalise(List pendenciasDescartar, UsuarioDt usuarioDt, LogDt logDt) throws Exception {
	    FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
	   
		try{
			obFabricaConexao.iniciarTransacao();			

			descartarPreAnalise(pendenciasDescartar, usuarioDt, logDt, obFabricaConexao);
			
			obFabricaConexao.finalizarTransacao();
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	/**
	 * Método que descarta uma pré-análise registrada, seja essa múltipla ou simples.
	 * 
	 * @param pendenciasDescartar, lista de pendências a serem descartadas
	 * @param usuarioDt, usuário que está descartando
	 * @param logDt, objeto com dados do log
	 * @author msapaula
	 * @throws Exception 
	 */
	public void descartarPreAnalise(List pendenciasDescartar, UsuarioDt usuarioDt, LogDt logDt, FabricaConexao obFabricaConexao) throws Exception {
		AudienciaProcessoNe audienciaProcessoNe = new AudienciaProcessoNe();
	    AudienciaProcessoPendenciaNe audienciaProcessoPendenciaNe = new AudienciaProcessoPendenciaNe();
	    PendenciaNe pendenciaNe = new PendenciaNe();
	    
		if (pendenciasDescartar != null) {
			for (int i = 0; i < pendenciasDescartar.size(); i++) {
				PendenciaDt pendenciaDt = (PendenciaDt) pendenciasDescartar.get(i);
				
				AudienciaProcessoDt audienciaProcessoDt = null;
				
				String idTmp = audienciaProcessoPendenciaNe.consultarPorIdPend(pendenciaDt.getId());
				
				if(idTmp != null)
					audienciaProcessoDt = audienciaProcessoNe.consultarIdCompleto(idTmp);
				
				if (pendenciaDt.getPendenciaTipoCodigoToInt() == PendenciaTipoDt.CONCLUSO_VOTO) {
					if (audienciaProcessoDt == null) audienciaProcessoDt = audienciaProcessoNe.consultarCompletoPelaPendenciaDeVoto(pendenciaDt.getId());
					if (audienciaProcessoDt != null) audienciaProcessoNe.limpaVinculoPendenciaVoto(audienciaProcessoDt, obFabricaConexao);
				} else if (pendenciaDt.getPendenciaTipoCodigoToInt() == PendenciaTipoDt.CONCLUSO_EMENTA) {
					if (audienciaProcessoDt == null) audienciaProcessoDt = audienciaProcessoNe.consultarCompletoPelaPendenciaDeEmenta(pendenciaDt.getId());
					if (audienciaProcessoDt != null) audienciaProcessoNe.limpaVinculoPendenciaEmenta(audienciaProcessoDt, obFabricaConexao);
				}
				
				// Cria uma nova pendência filha, fechando a atual
				PendenciaDt pendenciaFilha = criarPendenciaFilha(pendenciaDt, usuarioDt, logDt, obFabricaConexao);
								
				if (audienciaProcessoDt != null) {
					audienciaProcessoPendenciaNe.salvar(pendenciaFilha.getId(), audienciaProcessoDt.getId(), obFabricaConexao);
					if (pendenciaDt.getPendenciaTipoCodigoToInt() == PendenciaTipoDt.CONCLUSO_VOTO) {
						
						if (pendenciaDt.getResponsaveis() == null || pendenciaDt.getResponsaveis().size() == 0) 
							pendenciaDt.setResponsaveis(pendenciaNe.consultarResponsaveis(pendenciaDt));
						
						if (pendenciaDt.isResponsavelServentiaCargo(audienciaProcessoDt.getId_ServentiaCargo())) {
							audienciaProcessoDt.setId_PendenciaVotoRelator(pendenciaFilha.getId());
							audienciaProcessoDt.setId_PendenciaEmentaRelator("null"); //Atualizar só a de voto
							audienciaProcessoNe.vincularPendenciaVotoEmenta(audienciaProcessoDt, obFabricaConexao);
						} else if (pendenciaDt.isResponsavelServentiaCargo(audienciaProcessoDt.getId_ServentiaCargoRedator())) {
							audienciaProcessoDt.setId_PendenciaVotoRedator(pendenciaFilha.getId());
							audienciaProcessoDt.setId_PendenciaEmentaRedator("null"); //Atualizar só a de voto
							audienciaProcessoNe.vincularPendenciaVotoEmenta(audienciaProcessoDt, obFabricaConexao);
						}
					} else if (pendenciaDt.getPendenciaTipoCodigoToInt() == PendenciaTipoDt.CONCLUSO_EMENTA) {
						
						if (pendenciaDt.getResponsaveis() == null || pendenciaDt.getResponsaveis().size() == 0) 
							pendenciaDt.setResponsaveis(pendenciaNe.consultarResponsaveis(pendenciaDt));
						
						if (pendenciaDt.isResponsavelServentiaCargo(audienciaProcessoDt.getId_ServentiaCargo())) {
							audienciaProcessoDt.setId_PendenciaEmentaRelator(pendenciaFilha.getId());
							audienciaProcessoDt.setId_PendenciaVotoRelator("null"); //Atualizar só a de ementa
							audienciaProcessoNe.vincularPendenciaVotoEmenta(audienciaProcessoDt, obFabricaConexao);
						} else if (pendenciaDt.isResponsavelServentiaCargo(audienciaProcessoDt.getId_ServentiaCargoRedator())) {
							audienciaProcessoDt.setId_PendenciaEmentaRedator(pendenciaFilha.getId());
							audienciaProcessoDt.setId_PendenciaVotoRedator("null"); //Atualizar só a de ementa
							audienciaProcessoNe.vincularPendenciaVotoEmenta(audienciaProcessoDt, obFabricaConexao);
						}						
					}	
				}					

				// Consulta os arquivos problema da pendência pai que devem ser vinculados também a pendencia filha
				List arquivosProblema = this.consultarArquivosAssinadosProblema(pendenciaDt, true, obFabricaConexao);
				if (arquivosProblema != null && arquivosProblema.size() > 0) {
					this.vincularArquivos(pendenciaFilha, PendenciaArquivoNe.extrairArquivos(arquivosProblema), false, obFabricaConexao);
				}
				
				if (usuarioDt.isGabinetePresidenciaTjgo() || usuarioDt.isGabineteVicePresidenciaTjgo() || usuarioDt.isGabineteUPJ()){
					// Atualiza o Historico para pendência Fillha
					PendenciaResponsavelHistoricoNe penHistoricoNe = new PendenciaResponsavelHistoricoNe();
					penHistoricoNe.atualizaHistoricoPendenciaFilha(pendenciaFilha.getId_PendenciaPai(), pendenciaFilha.getId(), logDt.getId_Usuario(), logDt.getIpComputador(), obFabricaConexao);
				}
				
			}
		}
	}

	/**
	 * Modificacao para adaptar com o novo fechar pendencia
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 26/11/2008 09:51 Método auxiliar que recebe uma pendência, fecha
	 *        essa e cria uma pendência filha, já adicionando ServentiaCargo
	 *        como responsável
	 * @param pendenciaDt:
	 *            pendência a ser fechada (pai)
	 * @param usuarioDt:
	 *            usuário responsável
	 * @param logDt:
	 *            objeto log
	 * @return objeto pendência filha criada
	 * @author msapaula
	 * @throws Exception 
	 */
	public PendenciaDt criarPendenciaFilha(PendenciaDt pendenciaDt, UsuarioDt usuarioDt, LogDt logDt, FabricaConexao conexao) throws Exception{
		PendenciaNe pendenciaNe = new PendenciaNe();
		pendenciaDt.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_CORRECAO));

		// Cria nova pendência
		PendenciaDt novaPendencia = pendenciaDt.criarFilhaPreAnalise(pendenciaDt);
		novaPendencia.setId_UsuarioLog(logDt.getId_Usuario());
		novaPendencia.setIpComputadorLog(logDt.getIpComputador());
		
		// Adiciona Responsável
		//switch (Funcoes.StringToInt(usuarioDt.getGrupoCodigo())) {
		switch (Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo())) {
			//case GrupoDt.ASSISTENTES_JUIZES_VARA:
			//case GrupoDt.ASSISTENTES_JUIZES_SEGUNDO_GRAU:
			case GrupoTipoDt.JUIZ_LEIGO:			
 				novaPendencia.addResponsavel(getResponsavelPreAnalise(novaPendencia, "", pendenciaNe.consultarServentiaCargoResponsavelPendencia(pendenciaDt.getId(), conexao), logDt));
			break;
			case GrupoTipoDt.ASSESSOR_JUIZ_VARA_TURMA:
			case GrupoTipoDt.ASSESSOR_PRESIDENTE_SEGUNDO_GRAU:
			case GrupoTipoDt.ASSESSOR_DESEMBARGADOR:	
 				novaPendencia.addResponsavel(getResponsavelPreAnalise(novaPendencia, "", usuarioDt.getId_ServentiaCargoUsuarioChefe(), logDt));
				break;

//			case GrupoDt.DESEMBARGADOR:
//			case GrupoDt.ASSISTENTES_GABINETE:
			case GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU:
			case GrupoTipoDt.ASSISTENTE_GABINETE:
			case GrupoTipoDt.ASSISTENTE_GABINETE_FLUXO:
				//Adiciona à pendência filha os mesmos responsáveis da pai
				novaPendencia.setResponsaveis(pendenciaNe.consultarResponsaveis(pendenciaDt));
				break;

			//case GrupoDt.MINISTERIO_PUBLICO:
			case GrupoTipoDt.MP:
				novaPendencia.addResponsavel(getResponsavelPreAnalise(novaPendencia, "", usuarioDt.getId_ServentiaCargo(), logDt));
				break;
				
			//case GrupoDt.ADVOGADOS:
			case GrupoTipoDt.ADVOGADO:
				novaPendencia.addResponsavel(getResponsavelPreAnalise(novaPendencia, usuarioDt.getId_UsuarioServentia(),"", logDt));
				break;
				
			//case GrupoDt.ASSISTENTES_ADVOGADOS_PROMOTORES:
			case GrupoTipoDt.ASSESSOR_MP:
				if (usuarioDt.getGrupoUsuarioChefe() != null && usuarioDt.getGrupoUsuarioChefe().length() > 0) { 					
 							novaPendencia.addResponsavel(getResponsavelPreAnalise(novaPendencia, "", usuarioDt.getId_ServentiaCargoUsuarioChefe(), logDt));
				}
 				break;
			case GrupoTipoDt.ASSESSOR_ADVOGADO:
				if (usuarioDt.getGrupoUsuarioChefe() != null && usuarioDt.getGrupoUsuarioChefe().length() > 0) {
 					novaPendencia.addResponsavel(getResponsavelPreAnalise(novaPendencia, usuarioDt.getId_UsuarioServentiaChefe(), "", logDt));
 				}
				break;												
			default:
				novaPendencia.addResponsavel(getResponsavelPreAnalise(novaPendencia, "", usuarioDt.getId_ServentiaCargo(), logDt));
				break;
		}

		pendenciaNe.gerarPendencia(novaPendencia, conexao);
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
        pendenciaDt.setDataFim(df.format(new Date()));
		pendenciaNe.fecharPendencia(pendenciaDt, usuarioDt, conexao);
		return novaPendencia;
	}

	/**
	 * Método auxiliar para vincular uma pendência ao arquivo passado
	 * 
	 * @param pendencia:
	 *            obj Pendencia
	 * @param arquivo:
	 *            obj Arquivo
	 * @param resposta:
	 *            define se o pendenciaArquivo será do tipo resposta ou não
	 * @author msapaula
	 * @throws Exception 
	 */
	private void vincularPendenciaArquivo(PendenciaDt pendencia, ArquivoDt arquivo, boolean resposta, boolean pendenteAssinatura, FabricaConexao conexao) throws Exception{

		PendenciaArquivoDt pendenciaArquivoDt = new PendenciaArquivoDt();
		pendenciaArquivoDt.setId_Arquivo(arquivo.getId());
		pendenciaArquivoDt.setId_Pendencia(pendencia.getId());
		pendenciaArquivoDt.setResposta(resposta ? "true" : "false");
		if (pendenteAssinatura) pendenciaArquivoDt.setCodigoTemp(String.valueOf(PendenciaArquivoDt.AGUARDANDO_ASSINATURA));
		pendenciaArquivoDt.setId_UsuarioLog(arquivo.getId_UsuarioLog());
		pendenciaArquivoDt.setIpComputadorLog(arquivo.getIpComputadorLog());

		this.salvar(pendenciaArquivoDt, conexao);
	}

	/**
	 * Método que monta o arquivo de configuração da pré-analise e salva esse.
	 * 
	 * @param analisePendenciaDt:
	 *            objeto com dados da pré-analise
	 * @param novo:
	 *            define se será gerado um novo arquivo, ou caso false, arquivo
	 *            será alterado.
	 * @author msapaula
	 * @throws Exception 
	 */
	private ArquivoDt salvaArquivoConfiguracaoPreAnalise(AnaliseConclusaoDt preAnaliseConclusaoDt, boolean novo, FabricaConexao conexao) throws Exception{
		ArquivoNe arquivoNe = new ArquivoNe();
		ArquivoDt arquivoConfiguracao = null;

		if (novo) {
			arquivoConfiguracao = new ArquivoDt();
		} else {
			arquivoConfiguracao = preAnaliseConclusaoDt.getArquivoConfiguracaoPreAnalise().getArquivoDt();
		}
		arquivoConfiguracao = montaArquivoConfiguracaoPreAnalise(preAnaliseConclusaoDt, arquivoConfiguracao);
		arquivoNe.salvar(arquivoConfiguracao, conexao);

		return arquivoConfiguracao;
	}

	/**
	 * Método que monta o arquivo da pré-analise (texto redigido) de uma Conclusão e salva esse.
	 * 
	 * @param analisePendenciaDt, objeto com dados da pré-analise
	 * @param novo, define se será gerado um novo arquivo, ou caso false, arquivo será alterado.
	 * @author msapaula
	 * @throws Exception 
	 */
	private ArquivoDt salvaArquivoPreAnaliseConclusao(AnaliseConclusaoDt preAnaliseConclusaoDt, boolean novo, FabricaConexao conexao) throws Exception{
		ArquivoNe arquivoNe = new ArquivoNe();
		ArquivoDt arquivoPreAnalise = null;
		
		if (novo) {
			arquivoPreAnalise = new ArquivoDt();
		} else {
			arquivoPreAnalise = preAnaliseConclusaoDt.getArquivoPreAnalise().getArquivoDt();
		}
		arquivoPreAnalise = montaArquivoPreAnaliseConclusao(preAnaliseConclusaoDt, arquivoPreAnalise);
		
		//Validação para saber se o arquivo tem acesso liberado, está formatado corretamente e se pode ser convertido em PDF
		try {
			arquivoNe.validarArquivoInseridoProcesso(arquivoPreAnalise);
		} catch (Exception e) {
			throw new MensagemException("Arquivo formatado incorretamente. "); 
		}
		
		arquivoNe.salvar(arquivoPreAnalise, conexao);

		return arquivoPreAnalise;
	}

	/**
	 * Método auxiliar que retorna responsável pela pré-análise
	 * 
	 * @param pendenciaDt:
	 *            pendência em questão
	 * @param id_UsuarioServentia:
	 *            usuário responsável pela pré-analise
	 * @param id_ServentiaCargo:
	 *            serventia cargo responsável pela pré-análise
	 * @author msapaula
	 */
	private PendenciaResponsavelDt getResponsavelPreAnalise(PendenciaDt pendenciaDt, String id_UsuarioServentia, String id_ServentiaCargo, LogDt logDt){

		// Se usuário é assistente, adiciona ele como responsável pela pendência
		PendenciaResponsavelDt responsavelDt = new PendenciaResponsavelDt();
		responsavelDt.setId_Pendencia(pendenciaDt.getId());
		responsavelDt.setId_UsuarioResponsavel(id_UsuarioServentia);
		responsavelDt.setId_ServentiaCargo(id_ServentiaCargo);
		responsavelDt.setId_UsuarioLog(logDt.getId_Usuario());
		responsavelDt.setIpComputadorLog(logDt.getIpComputador());

		return responsavelDt;
	}

	/**
	 * Verifica a existência de uma pré-análise para a pendência passada. 
	 * Internamente procura pelo arquivo da pré-análise (texto redigido pelo assistente) e caso encontre chama o método para interpretar a
	 * pré-analise.
	 * 
	 * @param id_Pendencia, pendência para a qual será verificada se existe pré-analise
	 * 
	 * @return Objeto AnaliseConclusaoDt com os dados da pré-analise setados e com os arquivos da pré-analise 
	 * (arquivo do texto da pré-analise e arquivo da configuração)
	 * 
	 * @author msapaula
	 */
	public AnaliseConclusaoDt getPreAnaliseConclusao(String id_Pendencia) throws Exception {
		AnaliseConclusaoDt preAnaliseConclusaoDt = null;		
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{
			
			
			preAnaliseConclusaoDt = getPreAnaliseConclusao(id_Pendencia, obFabricaConexao);

		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return preAnaliseConclusaoDt;
	}
	
	/**
	 * Verifica a existência de uma pré-análise para a pendência passada. 
	 * Internamente procura pelo arquivo da pré-análise (texto redigido pelo assistente) e caso encontre chama o método para interpretar a
	 * pré-analise.
	 * 
	 * @param id_Pendencia, pendência para a qual será verificada se existe pré-analise
	 * @param obFabricaConexao, conexão com o banco de dados.
	 * 
	 * @return Objeto AnaliseConclusaoDt com os dados da pré-analise setados e com os arquivos da pré-analise 
	 * (arquivo do texto da pré-analise e arquivo da configuração)
	 * 
	 * @author mmgomes
	 */
	public AnaliseConclusaoDt getPreAnaliseConclusao(String id_Pendencia, FabricaConexao obFabricaConexao) throws Exception {
		AnaliseConclusaoDt preAnaliseConclusaoDt = null;
		PendenciaArquivoDt arquivoPreAnalise = null;		
		
		// Procura por PendenciaArquivo da pré-analise
		arquivoPreAnalise = this.getArquivoPreAnaliseConclusao(id_Pendencia, obFabricaConexao);
		preAnaliseConclusaoDt = getPreAnaliseConclusao(arquivoPreAnalise, obFabricaConexao);

		return preAnaliseConclusaoDt;
	}


	/**
	 * Obtém os dados de uma pré-analise de conclusão baseado no arquivo passado. 
	 * A partir desse arquivo será obtida a configuração da pré-análise, que será interpretada e os dados 
	 * setados no objeto AnaliseConclusaoDtDt
	 * 
	 * @param arquivoPreAnaliseDt, arquivo da pré-análise (texto)
	 * @return Objeto AnaliseConclusaoDt com os dados da pré-analise setados
	 * @author msapaula
	 */
	public PreAnaliseConclusaoDt getPreAnaliseConclusao(PendenciaArquivoDt arquivoPreAnaliseDt, FabricaConexao fabConexao) throws Exception {
		PreAnaliseConclusaoDt preAnaliseConclusaoDt = null;
		PendenciaArquivoDt configuracaoPreAnalise = null;

		if (arquivoPreAnaliseDt != null) {
			//Busca arquivo de configuração
			configuracaoPreAnalise = this.getArquivoConfiguracaoPreAnalise(arquivoPreAnaliseDt.getId_Pendencia(), fabConexao);

			//Se encontrou interpreta os dados
			if (configuracaoPreAnalise != null) preAnaliseConclusaoDt = lerConfiguracaoPreAnaliseConclusao(configuracaoPreAnalise.getArquivoDt().getArquivo());
			
			if (preAnaliseConclusaoDt == null) preAnaliseConclusaoDt = new PreAnaliseConclusaoDt();

			// Seta arquivos da pré-análise no objeto AnaliseConclusaoDt
			preAnaliseConclusaoDt.setArquivoConfiguracaoPreAnalise(configuracaoPreAnalise);
			preAnaliseConclusaoDt.setArquivoPreAnalise(arquivoPreAnaliseDt);

		}
		return preAnaliseConclusaoDt;
	}
	
	
	/**
	 * Obtém os dados de uma pré-analise de conclusão finalizada baseado no arquivo passado. 
	 * A partir desse arquivo será obtida a configuração da pré-análise, que será interpretada e os dados 
	 * setados no objeto AnaliseConclusaoDtDt
	 * 
	 * @param arquivoPreAnaliseDt, arquivo da pré-análise (texto)
	 * @return Objeto AnaliseConclusaoDt com os dados da pré-analise setados
	 * @author jrcorrea
	 * 26/08/2014
	 */
	public PreAnaliseConclusaoDt getPreAnaliseConclusaoFinalizada(PendenciaArquivoDt arquivoPreAnaliseDt, FabricaConexao fabConexao) throws Exception {
		PreAnaliseConclusaoDt preAnaliseConclusaoDt = null;
		PendenciaArquivoDt configuracaoPreAnalise = null;

		if (arquivoPreAnaliseDt != null) {
			//Busca arquivo de configuração
			configuracaoPreAnalise = this.getArquivoConfiguracaoPreAnaliseFinalizada(arquivoPreAnaliseDt.getId_Pendencia(), fabConexao);

			//Se encontrou interpreta os dados
			if (configuracaoPreAnalise != null) preAnaliseConclusaoDt = lerConfiguracaoPreAnaliseConclusao(configuracaoPreAnalise.getArquivoDt().getArquivo());
			
			if (preAnaliseConclusaoDt == null) preAnaliseConclusaoDt = new PreAnaliseConclusaoDt();

			// Seta arquivos da pré-análise no objeto AnaliseConclusaoDt
			preAnaliseConclusaoDt.setArquivoConfiguracaoPreAnalise(configuracaoPreAnalise);
			preAnaliseConclusaoDt.setArquivoPreAnalise(arquivoPreAnaliseDt);

		}
		return preAnaliseConclusaoDt;
	}
	
	/**
	 * Método responsável por consultar as pendências a gerar para uma conclusção do tipo voto
	 * @param PendenciaArquivoDt pendenciaArquivoDt
	 * @param FabricaConexao fabConexao
	 * @return List
	 * @throws Exception
	 */
	public List consultarPendenciasVotoEmenta(PendenciaArquivoDt arquivoPreAnaliseDt, FabricaConexao fabConexao) throws Exception {
		List pendenciasAGerar = null;
		PendenciaArquivoDt configuracaoPreAnalise = null;

		if (arquivoPreAnaliseDt != null) {
			//Busca arquivo de configuração
			configuracaoPreAnalise = this.getArquivoConfiguracaoPreAnalise(arquivoPreAnaliseDt.getId_Pendencia(), fabConexao);

			//Se encontrou interpreta os dados
			if (configuracaoPreAnalise != null) 
				pendenciasAGerar = lerConfiguracaoVotoEmenta(configuracaoPreAnalise.getArquivoDt().getArquivo());

		}
		return pendenciasAGerar;
	}

	/**
	 * Retorna o arquivo da pré-análise feita para a pendência passada.
	 * Conexão será iniciada nesse momento
	 * @param id_Pendencia
	 * @return
	 * @throws Exception
	 */
	public PendenciaArquivoDt getArquivoPreAnaliseConclusao(String id_Pendencia) throws Exception{
		return this.getArquivoPreAnaliseConclusao(id_Pendencia, null);

	}

	/**
	 * Retorna o Arquivo da pré-analise de uma Conclusão feita para a pendência passada (texto da pré-análise), retornando também o usuário responsável
	 * 
	 * @param id_Pendencia, pendência para a qual será buscada a pré-análise
	 * @return PendenciaArquivoDt referente ao arquivo da pré-análise
	 * @author msapaula
	 */
	private PendenciaArquivoDt getArquivoPreAnaliseConclusao(String id_Pendencia, FabricaConexao fabConexao) throws Exception {
		PendenciaArquivoDt pendenciaArquivoDt = null;
		FabricaConexao obFabricaConexao = null;
		try{
			if (fabConexao == null) obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			else obFabricaConexao = fabConexao;
			PendenciaArquivoPs obPersistencia = new PendenciaArquivoPs(obFabricaConexao.getConexao());

			pendenciaArquivoDt = obPersistencia.getArquivoPreAnaliseConclusao(id_Pendencia);
		
		} finally{
			if (fabConexao == null) obFabricaConexao.fecharConexao();
		}
		return pendenciaArquivoDt;
	}
	
	/**
	 * Retorna o Arquivo da pré-analise de uma Conclusão feita para a pendência passada (texto da pré-análise), retornando também o usuário responsável para as conclusões finalizadas
	 * 
	 * @param id_Pendencia, pendência para a qual será buscada a pré-análise
	 * @return PendenciaArquivoDt referente ao arquivo da pré-análise
	 * @author jesus
	 * 21/08/2014
	 */
	private PendenciaArquivoDt getArquivoPreAnaliseConclusaoFinalizada(String id_Pendencia, FabricaConexao fabConexao) throws Exception {
		PendenciaArquivoDt pendenciaArquivoDt = null;
		FabricaConexao obFabricaConexao = null;
		try{
			if (fabConexao == null) obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			else obFabricaConexao = fabConexao;
			PendenciaArquivoPs obPersistencia = new PendenciaArquivoPs(obFabricaConexao.getConexao());

			pendenciaArquivoDt = obPersistencia.getArquivoPreAnaliseConclusaoFinalizada(id_Pendencia);
		
		} finally{
			if (fabConexao == null) obFabricaConexao.fecharConexao();
		}
		return pendenciaArquivoDt;
	}
	
	/**
	 * Método responsável por consultar a pendência do tipo voto em um processo para um serventia cargo
	 * @param String id_ServentiaCargo
	 * @param String id_Processo
	 * @param FabricaConexao fabConexao
	 * @return PendenciaArquivoDt
	 * @throws Exception
	 */
	public PendenciaArquivoDt consultarVotoDesembargador(String id_ServentiaCargo, String id_Processo, String id_ProcessoTipoSessao, FabricaConexao fabConexao) throws Exception {
		PendenciaArquivoDt pendenciaArquivoDt = null;
		FabricaConexao obFabricaConexao = null;
		try{
			if (fabConexao == null) obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			else obFabricaConexao = fabConexao;
			PendenciaArquivoPs obPersistencia = new PendenciaArquivoPs(obFabricaConexao.getConexao());

			pendenciaArquivoDt = obPersistencia.consultaVotoDesembargador(id_ServentiaCargo, id_Processo, id_ProcessoTipoSessao, null);
		
		} finally{
			if (fabConexao == null) obFabricaConexao.fecharConexao();
		}
		return pendenciaArquivoDt;
	}

	// jvosantos - 28/11/2019 16:32 - Criar método para consultar o Voto, filtrando pela pendencia
	public PendenciaArquivoDt consultarVotoDesembargador(String id_ServentiaCargo, String idAudiProc, String id_ProcessoTipoSessao, String id_Pendencia, FabricaConexao fabConexao) throws Exception {
		PendenciaArquivoDt pendenciaArquivoDt = null;
		FabricaConexao obFabricaConexao = null;
		try{
			if (fabConexao == null) obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			else obFabricaConexao = fabConexao;
			PendenciaArquivoPs obPersistencia = new PendenciaArquivoPs(obFabricaConexao.getConexao());

			pendenciaArquivoDt = obPersistencia.consultaVotoDesembargador(id_ServentiaCargo, idAudiProc, id_ProcessoTipoSessao, id_Pendencia);
		
		} finally{
			if (fabConexao == null) obFabricaConexao.fecharConexao();
		}
		return pendenciaArquivoDt;
	}
	
	/**
	 * Método responsável por consultar a pendência do tipo voto em um processo para um serventia cargo
	 * @param String id_ServentiaCargo
	 * @param String id_Processo
	 * @param String id_PendenciaVoto
	 * @param FabricaConexao fabConexao
	 * @return PendenciaArquivoDt
	 * @throws Exception
	 */
	public PendenciaArquivoDt consultarVotoDesembargadorPendencia(String id_ServentiaCargo, String idAudiProc, String id_PendenciaVoto) throws Exception {
		PendenciaArquivoDt pendenciaArquivoDt = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{
			PendenciaArquivoPs obPersistencia = new PendenciaArquivoPs(obFabricaConexao.getConexao());
			pendenciaArquivoDt = obPersistencia.consultaVotoDesembargador(id_ServentiaCargo, idAudiProc, null, id_PendenciaVoto);
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return pendenciaArquivoDt;
	}
	
	public PendenciaArquivoDt consultarVotoDesembargadorPendencia(String id_ServentiaCargo, String idAudiProc, String id_PendenciaVoto, FabricaConexao obFabricaConexao) throws Exception {
		PendenciaArquivoPs obPersistencia = new PendenciaArquivoPs(obFabricaConexao.getConexao());
		return obPersistencia.consultaVotoDesembargador(id_ServentiaCargo, idAudiProc, null, id_PendenciaVoto);
	}

	/**
	 * Retorna o Arquivo de Configuração de uma pré-analise feita para a
	 * pendência passada (texto da pré-análise)
	 * 
	 * @param id_Pendencia:
	 *            pendência para a qual será buscado o arquivo de configuração
	 *            da pré-análise
	 * 
	 * @return PendenciaArquivoDt referente ao arquivo de configuração
	 * @author msapaula
	 * @throws Exception 
	 */
	public PendenciaArquivoDt getArquivoConfiguracaoPreAnalise(String id_Pendencia, FabricaConexao conexao) throws Exception{
		PendenciaArquivoDt pendenciaArquivoDt = null;
		FabricaConexao obFabricaConexao = null;
		if (conexao == null) obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			else obFabricaConexao = conexao;
		try{
			PendenciaArquivoPs obPersistencia = new PendenciaArquivoPs(obFabricaConexao.getConexao());
			pendenciaArquivoDt = obPersistencia.getArquivoConfiguracaoPreAnalise(id_Pendencia);
		
		} finally{
			if (conexao == null) obFabricaConexao.fecharConexao();
		}
		return pendenciaArquivoDt;
	}
	
	
	/**
	 * Retorna o Arquivo de Configuração de uma pré-analise finalizada feita para a
	 * pendência passada (texto da pré-análise)
	 * 
	 * @param id_Pendencia:
	 *            pendência para a qual será buscado o arquivo de configuração
	 *            da pré-análise
	 * 
	 * @return PendenciaArquivoDt referente ao arquivo de configuração
	 * @author jrcorrea
	 * 26/08/2014
	 * @throws Exception 
	 */
	private PendenciaArquivoDt getArquivoConfiguracaoPreAnaliseFinalizada(String id_Pendencia, FabricaConexao conexao) throws Exception{
		PendenciaArquivoDt pendenciaArquivoDt = null;
		FabricaConexao obFabricaConexao = null;
		if (conexao == null) obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			else obFabricaConexao = conexao;
		try{
			PendenciaArquivoPs obPersistencia = new PendenciaArquivoPs(obFabricaConexao.getConexao());
			pendenciaArquivoDt = obPersistencia.getArquivoConfiguracaoPreAnaliseFinalizada(id_Pendencia);
		
		} finally{
			if (conexao == null) obFabricaConexao.fecharConexao();
		}
		return pendenciaArquivoDt;
	}

	/**
	 * Interpreta o texto de configuração de uma pré-analise
	 * 
	 * @param textoConfiguracao, String com as configurações da pré-análise
	 * @return AnaliseConclusaoDt com dados necessários para que juiz finalize a conclusão
	 * @author msapaula
	 */
	private PreAnaliseConclusaoDt lerConfiguracaoPreAnaliseConclusao(String textoConfiguracao){
		PreAnaliseConclusaoDt preAnaliseConclusaoDt = null;
		String[] vetor = null;
		String[] dadosMovimentacao = null;
		String[] pendenciasGerar = null;
		
		String stSeparador1 = Configuracao.SEPARDOR_ANTIGO01;
		String stSeparador2 = Configuracao.SEPARDOR_ANTIGO02;
		String stSeparador3 = Configuracao.SEPARDOR_ANTIGO03;
		
		if (textoConfiguracao != null && textoConfiguracao.length() > 0) {
			
			if((textoConfiguracao.indexOf(Configuracao.SEPARDOR01)!=-1) ||
				(textoConfiguracao.indexOf(Configuracao.SEPARDOR02)!=-1) ||
				(textoConfiguracao.indexOf(Configuracao.SEPARDOR03)!=-1)){
				stSeparador1 = Configuracao.SEPARDOR01;
				stSeparador2 = Configuracao.SEPARDOR02;
				stSeparador3 = Configuracao.SEPARDOR03;
			}

			vetor = textoConfiguracao.split(stSeparador1, -1);
			if (vetor.length > 0) {
				dadosMovimentacao = vetor[0].split(stSeparador3, -1);
			}
			if (vetor.length > 1) {
				pendenciasGerar = vetor[1].split(stSeparador2, -1);
			}			

			preAnaliseConclusaoDt = new PreAnaliseConclusaoDt();
			/**
			 * vetor dadosMovimentacao[]: [0] - Id_MovimentacaoTipo [1] -
			 * MovimentacaoTipo [2] - Id_Classificador [3] - Classificador - [4] ComplementoMovimentacao -
			 * JulgadoMeritoProcessoPrincipal [5] - PedidoAssistencia [6]
			 * 
			 */
			if (dadosMovimentacao != null && dadosMovimentacao.length > 0) {
				preAnaliseConclusaoDt.setId_MovimentacaoTipo(dadosMovimentacao[0]);
				
				if (dadosMovimentacao.length > 2)
					preAnaliseConclusaoDt.setMovimentacaoTipo(dadosMovimentacao[1]);
				
				if (dadosMovimentacao.length > 3)
					preAnaliseConclusaoDt.setId_Classificador(dadosMovimentacao[2]);
				
				if (dadosMovimentacao.length > 4)
					preAnaliseConclusaoDt.setClassificador(dadosMovimentacao[3]);
				
				if (dadosMovimentacao.length > 5)
					preAnaliseConclusaoDt.setComplementoMovimentacao(dadosMovimentacao[4]);
				
				if (dadosMovimentacao.length > 6)
					preAnaliseConclusaoDt.setJulgadoMeritoProcessoPrincipal(dadosMovimentacao[5]);
				
				if (dadosMovimentacao.length == 7)
					preAnaliseConclusaoDt.setPedidoAssistencia(dadosMovimentacao[6]);
			}

			/**
			 * Vetor pendenciasGerar[]: [0] - Id [1] - CodPendenciaTipo [2] -
			 * PendenciaTipo [3] - CodDestinatario [4] - Destinatario [5] -
			 * Prazo [6] - Urgencia [7] - PessoalAdvogado  [8] - Pessoal  [9] - IntimadoCartorio
			 */
			List listaPendencias = new ArrayList();
			if (pendenciasGerar != null && pendenciasGerar.length > 0) {
				for (int i = 0; i < pendenciasGerar.length; i++) {
					String[] pendencia = pendenciasGerar[i].split(stSeparador3, -1);
					if (pendencia != null && pendencia.length > 1) {
						dwrMovimentarProcesso dt = new dwrMovimentarProcesso();
						dt.setId(i + 1);
						dt.setCodPendenciaTipo(pendencia[1]);
						dt.setPendenciaTipo(pendencia[2]);
						dt.setCodDestinatario(pendencia[3]);
						dt.setDestinatario(pendencia[4]);
						dt.setPrazo(pendencia[5]);
						dt.setUrgencia(pendencia[6]);
						dt.setPessoalAdvogado(pendencia[7]);
						
						if (pendencia.length > 8){
							dt.setPessoal(pendencia[8]);
							dt.setIntimacaoAudiencia(pendencia[9]);
						}						
						if (pendencia.length > 10){
							dt.setId_Sessao(pendencia[10]);
						}
						
						if(pendencia.length > 11){
							dt.setIdAreaDistribuicaoDestino(pendencia[11]);
						}
						
						if(pendencia.length > 12){
							dt.setIdServentiaDestino(pendencia[12]);
						}
						if(pendencia.length > 13){
							dt.setCodTipoAudiencia(pendencia[13]);
						}
						if(pendencia.length > 14){
							dt.setCodExpedicaoAutomatica(pendencia[14]);
						}
						if(pendencia.length > 15){
							dt.setId_ProcArquivamentoTipo(pendencia[15]);
						}
						if(pendencia.length > 16){
							dt.setProcArquivamentoTipo(pendencia[16]);
						}
						if(pendencia.length > 17){
							dt.setCodTipoProcessoFase(pendencia[17]);
						}
						
						listaPendencias.add(dt);
					}
				}
			}
			preAnaliseConclusaoDt.setListaPendenciasGerar(listaPendencias);
		}

		return preAnaliseConclusaoDt;
	}
	
	/**
	 * Interpreta o texto de configuração de uma pré-analise de uma pendência do tipo Voto e Ementa
	 * 
	 * @param textoConfiguracao, String com as configurações da pré-análise
	 * @return List listaPendencias com pendências a gerar de uma conclusão do tipo voto
	 * @author lsBernardes
	 */
	private List lerConfiguracaoVotoEmenta(String textoConfiguracao){
		String[] vetor = null;
		String[] pendenciasGerar = null;
		List listaPendencias = new ArrayList();
		String stSeparador1 = Configuracao.SEPARDOR_ANTIGO01;
		String stSeparador2 = Configuracao.SEPARDOR_ANTIGO02;
		String stSeparador3 = Configuracao.SEPARDOR_ANTIGO03;
		
		if (textoConfiguracao != null && textoConfiguracao.length() > 0) {
			
			if(textoConfiguracao.indexOf(Configuracao.SEPARDOR01)!=-1 ||
			   textoConfiguracao.indexOf(Configuracao.SEPARDOR02)!=-1 ||
			   textoConfiguracao.indexOf(Configuracao.SEPARDOR03)!=-1){
				stSeparador1 = Configuracao.SEPARDOR01;
				stSeparador2 = Configuracao.SEPARDOR02;
				stSeparador3 = Configuracao.SEPARDOR03;
			}
			
			vetor = textoConfiguracao.split(stSeparador1, -1);
			if (vetor != null && vetor.length > 1) {
				pendenciasGerar = vetor[1].split(stSeparador2, -1);
			}			

			/**
			 * Vetor pendenciasGerar[]: [0] - Id [1] - CodPendenciaTipo [2] -
			 * PendenciaTipo [3] - CodDestinatario [4] - Destinatario [5] -
			 * Prazo [6] - Urgencia [7] - PessoalAdvogado  [8] - Pessoal  [9] - IntimadoCartorio
			 */
			if (pendenciasGerar != null && pendenciasGerar.length > 0) {
				for (int i = 0; i < pendenciasGerar.length; i++) {
					String[] pendencia = pendenciasGerar[i].split(stSeparador3, -1);
					if (pendencia != null && pendencia.length > 1) {
						dwrMovimentarProcesso dt = new dwrMovimentarProcesso();
						dt.setId(i + 1);
						dt.setCodPendenciaTipo(pendencia[1]);
						dt.setPendenciaTipo(pendencia[2]);
						dt.setCodDestinatario(pendencia[3]);
						dt.setDestinatario(pendencia[4]);
						dt.setPrazo(pendencia[5]);
						dt.setUrgencia(pendencia[6]);
						dt.setPessoalAdvogado(pendencia[7]);
						
						if (pendencia.length > 8){
							dt.setPessoal(pendencia[8]);
							dt.setIntimacaoAudiencia(pendencia[9]);
						}
						
						listaPendencias.add(dt);
					}
				}
			}
		}

		return listaPendencias;
	}

	/**
	 * Monta o arquivo de configuração da pré-análise, registrando todas as
	 * ações definidas pelo assistente: Tipo Movimentação, Classificador e
	 * pendências a serem geradas. Essas ações serão efetivadas somente mediante
	 * assinatura do juiz
	 * 
	 * @param analisePendenciaDt:
	 *            dt com os dados da pré-analise
	 * @param id_Pendencia:
	 *            pendência a qual estará vinculado o PendenciaArquivo
	 * @author msapaula
	 */
	public ArquivoDt montaArquivoConfiguracaoPreAnalise(AnaliseConclusaoDt preAnaliseConclusaoDt, ArquivoDt arquivoDt){
		StringBuilder configuracao = new StringBuilder();

		if (arquivoDt == null) arquivoDt = new ArquivoDt();

		// Gerando texto de configuração da pre-análise
		configuracao.append(preAnaliseConclusaoDt.getId_MovimentacaoTipo()).append(Configuracao.SEPARDOR03).append( preAnaliseConclusaoDt.getMovimentacaoTipo()).append(Configuracao.SEPARDOR03);
		configuracao.append(preAnaliseConclusaoDt.getId_Classificador()).append( Configuracao.SEPARDOR03).append( preAnaliseConclusaoDt.getClassificador()).append( Configuracao.SEPARDOR03);
		configuracao.append(preAnaliseConclusaoDt.getComplementoMovimentacao()).append( Configuracao.SEPARDOR03); // @
		configuracao.append(preAnaliseConclusaoDt.getJulgadoMeritoProcessoPrincipal()).append(Configuracao.SEPARDOR03);
		configuracao.append(preAnaliseConclusaoDt.getPedidoAssistencia()).append(Configuracao.SEPARDOR01);
		// marca fim das informações da movimentação

		// Registrando pendências a serem geradas
		List listaPendenciasGerar = preAnaliseConclusaoDt.getListaPendenciasGerar();
		if (listaPendenciasGerar != null) {
			for (int i = 0; i < listaPendenciasGerar.size(); i++) {
				dwrMovimentarProcesso dt = (dwrMovimentarProcesso) listaPendenciasGerar.get(i);
				configuracao.append( dt.getId()).append(Configuracao.SEPARDOR03);
				configuracao.append( dt.getCodPendenciaTipo()).append( Configuracao.SEPARDOR03);
				configuracao.append( dt.getPendenciaTipo() ).append( Configuracao.SEPARDOR03);
				configuracao.append( dt.getCodDestinatario() ).append(Configuracao.SEPARDOR03);
				configuracao.append( dt.getDestinatario() ).append(Configuracao.SEPARDOR03);
				configuracao.append( dt.getPrazo()).append( Configuracao.SEPARDOR03);
				configuracao.append( dt.getUrgencia() ).append( Configuracao.SEPARDOR03);
				configuracao.append( dt.getPessoalAdvogado()).append(Configuracao.SEPARDOR03);
				configuracao.append( dt.getPessoal()).append( Configuracao.SEPARDOR03);
				configuracao.append( dt.getIntimacaoAudiencia() ).append( Configuracao.SEPARDOR03);
				configuracao.append( dt.getId_Sessao()).append( Configuracao.SEPARDOR03);
				configuracao.append( dt.getIdAreaDistribuicaoDestino() ).append( Configuracao.SEPARDOR03);
				configuracao.append( dt.getIdServentiaDestino()).append( Configuracao.SEPARDOR03);
				configuracao.append( dt.getCodTipoAudiencia()).append( Configuracao.SEPARDOR03);
				configuracao.append( dt.getCodExpedicaoAutomatica()).append( Configuracao.SEPARDOR03);
				configuracao.append( dt.getId_ProcArquivamentoTipo()).append( Configuracao.SEPARDOR03);
				configuracao.append( dt.getProcArquivamentoTipo()).append( Configuracao.SEPARDOR03);
				configuracao.append( dt.getCodTipoProcessoFase());
				// Adiciona delimitador das pendências que serão geradas '#'
				if (i + 1 < listaPendenciasGerar.size()) configuracao.append(Configuracao.SEPARDOR02);
			}
		}

		// Monta arquivo
		arquivoDt.setArquivo(configuracao.toString());
		arquivoDt.setArquivoTipoCodigo(String.valueOf(ArquivoTipoDt.CONFIGURACAO_PRE_ANALISE));
		arquivoDt.setNomeArquivo("configuracao");
		arquivoDt.setContentType("text/plain");
		arquivoDt.setId_UsuarioLog(preAnaliseConclusaoDt.getId_UsuarioLog());
		arquivoDt.setIpComputadorLog(preAnaliseConclusaoDt.getIpComputadorLog());

		return arquivoDt;
	}

	/**
	 * Monta o arquivo com o texto da pré-analise redigida pelo assistente
	 * 
	 * @param analisePendenciaDt, dt com os dados da pré-analise
	 * @param id_Pendencia, pendência a qual será vinculado o PendenciaArquivo
	 * @author msapaula
	 */
	public ArquivoDt montaArquivoPreAnaliseConclusao(AnaliseConclusaoDt preAnaliseConclusaoDt, ArquivoDt arquivoDt){

		if (arquivoDt == null) arquivoDt = new ArquivoDt();

		// Monta arquivo
		arquivoDt.setArquivo(preAnaliseConclusaoDt.getTextoEditor());
		arquivoDt.setId_ArquivoTipo(preAnaliseConclusaoDt.getId_ArquivoTipo());
		
		if (preAnaliseConclusaoDt.getNomeArquivo() != null && !preAnaliseConclusaoDt.getNomeArquivo().equals(""))
			arquivoDt.setNomeArquivo(preAnaliseConclusaoDt.getNomeArquivo().replace(".html","")+".html");
		else
			arquivoDt.setNomeArquivo("online.html");
		
		arquivoDt.setContentType("text/html");
		arquivoDt.setId_UsuarioLog(preAnaliseConclusaoDt.getId_UsuarioLog());
		arquivoDt.setIpComputadorLog(preAnaliseConclusaoDt.getIpComputadorLog());

		return arquivoDt;
	}
	
	/**
	 * Método que verifica se já existe uma pré-análise registrada para uma Conclusão.
	 * Esse método será utilizado para impossbilitar que dois assistentes registrem pré-análise ao mesmo tempo. 
	 * 
	 * @param id_Pendencia, pendência para a qual será verificada a pré-analise
	 * @author msapaula
	 */
	private synchronized boolean verificaPreAnaliseConclusao(String id_Pendencia, FabricaConexao obFabricaConexao) throws Exception {
		boolean boTeste = false;
		
		PendenciaArquivoPs obPersistencia = new PendenciaArquivoPs(obFabricaConexao.getConexao());
		boTeste = obPersistencia.verificaPreAnaliseConclusao(id_Pendencia);
		
		return boTeste;
	}

	/**
	 * Retorna o conteudo de arquivos nao assinados
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 30/09/2008 10:01
	 * @param String
	 *            id_pendenciaArquivo, id do pendencia arquivo
	 * @return String (Conteudo do arquivo)
	 * @throws Exception
	 */
	public ArquivoDt conteudoArquivoNaoAssinado(String id_pendenciaArquivo) throws Exception {
		ArquivoDt arquivoDt = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			PendenciaArquivoPs obPersistencia = new PendenciaArquivoPs(obFabricaConexao.getConexao());

			PendenciaArquivoDt pendenciaArquivoDt = obPersistencia.consultarId(id_pendenciaArquivo);

			ArquivoNe arquivoNe = new ArquivoNe();
			arquivoDt = arquivoNe.consultarArquivoNaoAssinado(pendenciaArquivoDt.getId_Arquivo());

			// Se o arquivo for assinado
			if (arquivoDt == null) {throw new MensagemException("Não é permitido mostrar o conteudo deste arquivo"); }
		
		} finally{
			obFabricaConexao.fecharConexao();
		}

		return arquivoDt;
	}

	/**
	 * Consulta as pré-análises simples de Conclusões realizadas por um usuário ou ServentiaCargo. 
	 * Retorna as que ainda não foram assinadas
	 * 
	 * @param usuarioDt, usuário responsável pelas pré-análises
	 * @param numeroProcesso, filtro para número de processo
	 * @param id_Classificador, filtro para classificador
	 * @param id_PendenciaTipo, filtro para tipo de pendência
	 * @author msapaula
	 */
	public List consultarPreAnalisesConclusaoSimples(UsuarioNe usuarioSessao, String numeroProcesso, String id_Classificador, String id_PendenciaTipo, boolean ehVoto, boolean ehVotoVencido) throws Exception {
		List preAnalises = null;
		String digitoVerificador = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			PendenciaArquivoPs obPersistencia = new PendenciaArquivoPs(obFabricaConexao.getConexao());
			//int grupo = Funcoes.StringToInt(usuarioDt.getGrupoCodigo());
			int grupoTipo = usuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt();

			//Divide número de processo do dígito verificador
			String[] stTemp = numeroProcesso.split("\\.");
			if (stTemp.length >= 1) numeroProcesso = stTemp[0];
			else numeroProcesso = "";
			if (stTemp.length >= 2) digitoVerificador = stTemp[1];

			switch (grupoTipo) {
				case GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU:
				case GrupoTipoDt.JUIZ_TURMA:
				case GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU:
				case GrupoTipoDt.PRESIDENTE_SEGUNDO_GRAU:
					preAnalises = obPersistencia.consultarPreAnalisesConclusaoSimples(usuarioSessao, usuarioSessao.getUsuarioDt().getId_ServentiaCargo(), numeroProcesso, digitoVerificador, id_Classificador, id_PendenciaTipo, ehVoto, ehVotoVencido, false);
					break;
				case GrupoTipoDt.ASSISTENTE_GABINETE:
				case GrupoTipoDt.ASSISTENTE_GABINETE_FLUXO:
					preAnalises = obPersistencia.consultarPreAnalisesConclusaoSimples(usuarioSessao, usuarioSessao.getUsuarioDt().getId_ServentiaCargo(), numeroProcesso, digitoVerificador, id_Classificador, id_PendenciaTipo, ehVoto, ehVotoVencido, true);
					break;

				case GrupoTipoDt.JUIZ_LEIGO:			
					
		            PendenciaPs pendPs = new  PendenciaPs(obFabricaConexao.getConexao());				
					String id_Serventia = usuarioSessao.getUsuarioDt().getId_Serventia();
			        List listaServentiaCargo = new ArrayList();

			        listaServentiaCargo = pendPs.consultarJuizesServentia(id_Serventia);
					
			        preAnalises = new ArrayList();
			        
			        if (listaServentiaCargo != null){
			        	for(int i = 0; i < listaServentiaCargo.size(); i++) {
			        		preAnalises.addAll(obPersistencia.consultarPreAnalisesConclusaoSimples(usuarioSessao, (String)listaServentiaCargo.get(i), numeroProcesso, digitoVerificador, id_Classificador, id_PendenciaTipo, ehVoto, ehVotoVencido, false) );

			        	}
			        }
					
				break;
				case GrupoTipoDt.ASSESSOR_JUIZ_VARA_TURMA:
				case GrupoTipoDt.ASSESSOR_PRESIDENTE_SEGUNDO_GRAU:
				case GrupoTipoDt.ASSESSOR_DESEMBARGADOR:
					preAnalises = obPersistencia.consultarPreAnalisesConclusaoSimples(usuarioSessao, usuarioSessao.getUsuarioDt().getId_ServentiaCargoUsuarioChefe(), numeroProcesso, digitoVerificador, id_Classificador, id_PendenciaTipo, ehVoto, ehVotoVencido, false);
					break;
			}

		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return preAnalises;
	}
	
	/**
	 * Consulta as pré-análises simples de Conclusões realizadas por um usuário ou ServentiaCargo. 
	 * Retorna as que ainda não foram assinadas
	 * 
	 * @param usuarioDt, usuário responsável pelas pré-análises
	 * @param numeroProcesso, filtro para número de processo
	 * @param id_Classificador, filtro para classificador
	 * @param id_PendenciaTipo, filtro para tipo de pendência
	 * @author lsbernardes
	 */
	
	/**
	 * Consulta as pré-análises simples de Conclusões realizadas por um usuário ou ServentiaCargo. 
	 * Retorna as que ainda não foram assinadas
	 * 
	 * @param usuarioDt, usuário responsável pelas pré-análises
	 * @param numeroProcesso, filtro para número de processo
	 * @param id_Classificador, filtro para classificador
	 * @param id_PendenciaTipo, filtro para tipo de pendência
	 * @author msapaula
	 */
	public List consultarPreAnalisesConclusaoSimplesPJD(UsuarioNe usuarioSessao, String idServentiaFiltro, String numeroProcesso, String id_Classificador, String id_PendenciaTipo, boolean ehVoto, boolean ehVotoVencido) throws Exception {
		List preAnalises = null;
		String digitoVerificador = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			PendenciaArquivoPs obPersistencia = new PendenciaArquivoPs(obFabricaConexao.getConexao());
			//int grupo = Funcoes.StringToInt(usuarioDt.getGrupoCodigo());
			int grupoTipo = usuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt();

			//Divide número de processo do dígito verificador
			String[] stTemp = numeroProcesso.split("[-\\.]");
			if (stTemp.length >= 1) numeroProcesso = stTemp[0];
			else numeroProcesso = "";
			if (stTemp.length >= 2) digitoVerificador = stTemp[1];

			switch (grupoTipo) {
				case GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU:
				case GrupoTipoDt.JUIZ_TURMA:
				case GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU:
				case GrupoTipoDt.PRESIDENTE_SEGUNDO_GRAU:
					//lrcampos 28/10/2019 11:51 - Condição para caso for virtual chamar método específico	
					preAnalises = obPersistencia.consultarPreAnalisesConclusaoSimplesPJD(usuarioSessao, idServentiaFiltro, usuarioSessao.getUsuarioDt().getId_ServentiaCargo(), numeroProcesso, digitoVerificador, id_Classificador, id_PendenciaTipo, ehVoto, false, ehVotoVencido, false);
					break;
				case GrupoTipoDt.ASSISTENTE_GABINETE:
				case GrupoTipoDt.ASSISTENTE_GABINETE_FLUXO:
					preAnalises = obPersistencia.consultarPreAnalisesConclusaoSimplesPJD(usuarioSessao, idServentiaFiltro, usuarioSessao.getUsuarioDt().getId_ServentiaCargo(), numeroProcesso, digitoVerificador, id_Classificador, id_PendenciaTipo, ehVoto, false, ehVotoVencido, true);
					break;

				case GrupoTipoDt.JUIZ_LEIGO:			
					
		            PendenciaPs pendPs = new  PendenciaPs(obFabricaConexao.getConexao());				
					String id_Serventia = usuarioSessao.getUsuarioDt().getId_Serventia();
			        List listaServentiaCargo = new ArrayList();

			        listaServentiaCargo = pendPs.consultarJuizesServentia(id_Serventia);
					
			        preAnalises = new ArrayList<PendenciaArquivoDt>();
			        
			        if (listaServentiaCargo != null){
			        	for(int i = 0; i < listaServentiaCargo.size(); i++) {
			        		preAnalises.addAll(obPersistencia.consultarPreAnalisesConclusaoSimplesPJD(usuarioSessao, idServentiaFiltro, (String)listaServentiaCargo.get(i), numeroProcesso, digitoVerificador, id_Classificador, id_PendenciaTipo, ehVoto, false, ehVotoVencido, false) );

			        	}
			        }
					
				break;
				case GrupoTipoDt.ASSESSOR_JUIZ_VARA_TURMA:
				case GrupoTipoDt.ASSESSOR_PRESIDENTE_SEGUNDO_GRAU:
				case GrupoTipoDt.ASSESSOR_DESEMBARGADOR:
					preAnalises = obPersistencia.consultarPreAnalisesConclusaoSimplesPJD(usuarioSessao, idServentiaFiltro, usuarioSessao.getUsuarioDt().getId_ServentiaCargoUsuarioChefe(), numeroProcesso, digitoVerificador, id_Classificador, id_PendenciaTipo, ehVoto, false, ehVotoVencido, false);
					break;
			}

		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return preAnalises;
	}
	
		
	public List consultarPreAnalisesConclusaoSimplesVirtual(UsuarioNe usuarioSessao, String idServentia, String numeroProcesso, String id_Classificador, String id_PendenciaTipo, boolean ehVoto, boolean ehVotoVencido, boolean isIniciada) throws Exception {
		List preAnalises = null;
		String digitoVerificador = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			PendenciaArquivoPs obPersistencia = new PendenciaArquivoPs(obFabricaConexao.getConexao());
			//int grupo = Funcoes.StringToInt(usuarioDt.getGrupoCodigo());
			int grupoTipo = usuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt();

			//Divide número de processo do dígito verificador
			String[] stTemp = numeroProcesso.split("[-\\.]");
			if (stTemp.length >= 1) numeroProcesso = stTemp[0];
			else numeroProcesso = "";
			if (stTemp.length >= 2) digitoVerificador = stTemp[1];

			switch (grupoTipo) {
//				case GrupoDt.JUIZES_VARA:
//				case GrupoDt.JUIZES_TURMA_RECURSAL:
//				case GrupoDt.DESEMBARGADOR:
//				case GrupoDt.ASSISTENTES_GABINETE:
				case GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU:
				case GrupoTipoDt.JUIZ_TURMA:
				case GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU:
				case GrupoTipoDt.PRESIDENTE_SEGUNDO_GRAU:
				case GrupoTipoDt.ASSISTENTE_GABINETE:
				case GrupoTipoDt.ASSISTENTE_GABINETE_FLUXO:
				//lrcampos 28/10/2019 11:51 - Condição para caso for virtual chamar método específico
					preAnalises = obPersistencia.consultarPreAnalisesConclusaoSimplesVirtual(usuarioSessao, idServentia, usuarioSessao.getUsuarioDt().getId_ServentiaCargo(), numeroProcesso, digitoVerificador, id_Classificador, id_PendenciaTipo, ehVoto, ehVotoVencido, isIniciada);
					break;

//				case GrupoDt.ASSISTENTES_JUIZES_VARA:
//				case GrupoDt.ASSISTENTES_JUIZES_SEGUNDO_GRAU:
				case GrupoTipoDt.ASSESSOR_JUIZ_VARA_TURMA:
				case GrupoTipoDt.ASSESSOR_PRESIDENTE_SEGUNDO_GRAU:
				case GrupoTipoDt.ASSESSOR_DESEMBARGADOR:
					preAnalises = obPersistencia.consultarPreAnalisesConclusaoSimplesVirtual(usuarioSessao, idServentia, usuarioSessao.getUsuarioDt().getId_ServentiaCargoUsuarioChefe(), numeroProcesso, digitoVerificador, id_Classificador, id_PendenciaTipo, ehVoto, ehVotoVencido, isIniciada);
					break;
			}

		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return preAnalises;
	}
	public List consultarPreAnalisesAguardandoInicioVirtual(UsuarioNe usuarioSessao, String numeroProcesso, String id_Classificador, String id_PendenciaTipo, boolean ehVoto, boolean ehVotoVencido) throws Exception {
		List preAnalises = null;
		String digitoVerificador = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			PendenciaArquivoPs obPersistencia = new PendenciaArquivoPs(obFabricaConexao.getConexao());
			//int grupo = Funcoes.StringToInt(usuarioDt.getGrupoCodigo());
			int grupoTipo = usuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt();

			//Divide número de processo do dígito verificador
			String[] stTemp = numeroProcesso.split("\\.");
			if (stTemp.length >= 1) numeroProcesso = stTemp[0];
			else numeroProcesso = "";
			if (stTemp.length >= 2) digitoVerificador = stTemp[1];

			switch (grupoTipo) {
//				case GrupoDt.JUIZES_VARA:
//				case GrupoDt.JUIZES_TURMA_RECURSAL:
//				case GrupoDt.DESEMBARGADOR:
//				case GrupoDt.ASSISTENTES_GABINETE:
				case GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU:
				case GrupoTipoDt.JUIZ_TURMA:
				case GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU:
				case GrupoTipoDt.PRESIDENTE_SEGUNDO_GRAU:
				case GrupoTipoDt.ASSISTENTE_GABINETE:
				case GrupoTipoDt.ASSISTENTE_GABINETE_FLUXO:
					preAnalises = obPersistencia.consultarPreAnalisesAguardandoInicioVirtual(usuarioSessao, usuarioSessao.getUsuarioDt().getId_ServentiaCargo(), numeroProcesso, digitoVerificador, id_Classificador, id_PendenciaTipo, ehVoto, ehVotoVencido);
					break;

//				case GrupoDt.ASSISTENTES_JUIZES_VARA:
//				case GrupoDt.ASSISTENTES_JUIZES_SEGUNDO_GRAU:
				case GrupoTipoDt.ASSESSOR_JUIZ_VARA_TURMA:
				case GrupoTipoDt.ASSESSOR_PRESIDENTE_SEGUNDO_GRAU:
				case GrupoTipoDt.ASSESSOR_DESEMBARGADOR:
					preAnalises = obPersistencia.consultarPreAnalisesAguardandoInicioVirtual(usuarioSessao, usuarioSessao.getUsuarioDt().getId_ServentiaCargoUsuarioChefe(), numeroProcesso, digitoVerificador, id_Classificador, id_PendenciaTipo, ehVoto, ehVotoVencido);
					break;
			}

		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return preAnalises;
	}
	
	public List consultarPreAnalisesConclusaoSimplesAssistenteGabinete(UsuarioNe usuarioSessao, String numeroProcesso, String id_Classificador, String id_PendenciaTipo, String id_ServentiaGrupo, boolean ehVoto, boolean ehVotoVencido) throws Exception {
		List preAnalises = null;
		String digitoVerificador = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			PendenciaArquivoPs obPersistencia = new PendenciaArquivoPs(obFabricaConexao.getConexao());
			//int grupo = Funcoes.StringToInt(usuarioDt.getGrupoCodigo());
			int grupoTipo = usuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt();

			//Divide número de processo do dígito verificador
			String[] stTemp = numeroProcesso.split("\\.");
			if (stTemp.length >= 1) numeroProcesso = stTemp[0];
			else numeroProcesso = "";
			if (stTemp.length >= 2) digitoVerificador = stTemp[1];

			switch (grupoTipo) {
				case GrupoTipoDt.ASSISTENTE_GABINETE:
				case GrupoTipoDt.ASSISTENTE_GABINETE_FLUXO:
				case GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU:
					preAnalises = obPersistencia.consultarPreAnalisesConclusaoSimplesAssistenteGabinete(usuarioSessao, usuarioSessao.getUsuarioDt().getId_ServentiaCargo(), numeroProcesso, digitoVerificador, id_Classificador, id_PendenciaTipo, id_ServentiaGrupo, ehVoto, ehVotoVencido);
					break;
			}

		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return preAnalises;
	}

	/**
	 * Consulta as pré-análises múltiplas realizadas por um usuário ou Serventia Cargo. Retorna as que ainda não foram assinadas
	 * 
	 * @param usuarioDt, usuário responsável pelas pré-análises
	 * @param numeroProcesso, filtro para número de processo
	 * @author msapaula
	 */
	public List consultarPreAnalisesConclusoesMultiplas(UsuarioDt usuarioDt, String numeroProcesso) throws Exception {
		List preAnalises = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			PendenciaArquivoPs obPersistencia = new PendenciaArquivoPs(obFabricaConexao.getConexao());
			//int grupo = Funcoes.StringToInt(usuarioDt.getGrupoCodigo());
			int grupoTipo = Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo());

			switch (grupoTipo) {
//				case GrupoDt.JUIZES_VARA:
//				case GrupoDt.JUIZES_TURMA_RECURSAL:
//				case GrupoDt.DESEMBARGADOR:
//				case GrupoDt.ASSISTENTES_GABINETE:
				case GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU:
				case GrupoTipoDt.JUIZ_TURMA:
				case GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU:
				case GrupoTipoDt.ASSISTENTE_GABINETE:
				case GrupoTipoDt.ASSISTENTE_GABINETE_FLUXO:
					ServentiaCargoDt serventiaCargoDt = new ServentiaCargoDt();
					serventiaCargoDt.setId(usuarioDt.getId_ServentiaCargo());
					preAnalises = obPersistencia.consultarPreAnalisesConclusoesMultiplas(usuarioDt.getId_ServentiaCargo(), numeroProcesso);
					break;

//					case GrupoDt.ASSISTENTES_JUIZES_VARA:
//					case GrupoDt.ASSISTENTES_JUIZES_SEGUNDO_GRAU:
				case GrupoTipoDt.ASSESSOR_JUIZ_VARA_TURMA:
				case GrupoTipoDt.ASSESSOR_PRESIDENTE_SEGUNDO_GRAU:
				case GrupoTipoDt.ASSESSOR_DESEMBARGADOR:
					preAnalises = obPersistencia.consultarPreAnalisesConclusoesMultiplas(usuarioDt.getId_ServentiaCargoUsuarioChefe(), numeroProcesso);
					break;
			}

		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return preAnalises;
	}

	/**
	 * Consulta as pré-análises de um usuário ou ServentiaCargo que já foram finalizadas, assinadas pelo juiz.
	 * 
	 * @param usuarioDt, usuário que realizou a pré-analise
	 * @author msapaula
	 */
	public List consultarPreAnalisesConclusaoFinalizadas(String numeroProcesso, String dataInicial, String dataFinal, UsuarioDt usuarioDt) throws Exception {
		List preAnalises = null;
		String digitoVerificador = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			PendenciaArquivoPs obPersistencia = new PendenciaArquivoPs(obFabricaConexao.getConexao());
			//int grupo = Funcoes.StringToInt(usuarioDt.getGrupoCodigo());
			int grupoTipo = Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo());

			//Divide número de processo do dígito verificador
			String[] stTemp = numeroProcesso.split("\\.");
			if (stTemp.length >= 1) numeroProcesso = stTemp[0];
			else numeroProcesso = "";
			if (stTemp.length >= 2) digitoVerificador = stTemp[1];

			switch (grupoTipo) {
				case GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU:
				case GrupoTipoDt.JUIZ_TURMA:
				case GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU:
				case GrupoTipoDt.ASSISTENTE_GABINETE:
				case GrupoTipoDt.ASSISTENTE_GABINETE_FLUXO:
					preAnalises = obPersistencia.consultarPreAnalisesConclusaoFinalizadas(numeroProcesso, digitoVerificador, dataInicial, dataFinal, usuarioDt.getId_ServentiaCargo(), null);
					break;

				case GrupoTipoDt.ASSESSOR_JUIZ_VARA_TURMA:
				case GrupoTipoDt.ASSESSOR_PRESIDENTE_SEGUNDO_GRAU:
				case GrupoTipoDt.ASSESSOR_DESEMBARGADOR:
					preAnalises = obPersistencia.consultarPreAnalisesConclusaoFinalizadas(numeroProcesso, digitoVerificador, dataInicial, dataFinal, usuarioDt.getId_ServentiaCargoUsuarioChefe(), null);
					break;
			}
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return preAnalises;
	}

	/**
	 * Consulta as pendências vinculadas a um arquivo. Geralmente esse arquivo
	 * refere-se a uma pré-analise multipla realizada
	 * 
	 * @param id_Arquivo:
	 *            identificação do arquivo de pré-analise
	 * @return lista de pendências vinculadas ao arquivo
	 * @author msapaula
	 */
	public List consultarPendencias(String id_Arquivo) throws Exception {
		List pendencias = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			PendenciaPs obPersistencia = new PendenciaPs(obFabricaConexao.getConexao());
			pendencias = obPersistencia.consultarPendencias(id_Arquivo);

		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return pendencias;
	}

	/**
	 * Consulta todas as pré-analises registradas para a pendência passada,
	 * montando o histórico de todas correções existentes. Já retorna o
	 * responsável pela pre-análise setado na pendência.
	 * 
	 * @param pendenciaDt:
	 *            pendencia para a qual serão procuradas as pré-análises
	 * @author msapaula
	 * @throws Exception 
	 */
	public List consultarPreAnalisesConclusaoAnteriores(PendenciaDt pendenciaDt, UsuarioNe usuarioSessao) throws Exception{
		List preAnalises = new ArrayList();
		PendenciaNe neObjeto = new PendenciaNe();
		PendenciaArquivoDt pendenciaArquivoDt = null;

		// Retorna pendências anteriores (pais)
		List pendencias = neObjeto.consultarPaiResponsavelRecursiva(pendenciaDt);

		// Para cada pendência encontrada, seta o responsável e monta um
		// PendenciaArquivo com as informações
		for (int i = 0; i < pendencias.size(); i++) {
			PendenciaDt obj = (PendenciaDt) pendencias.get(i);
			pendenciaArquivoDt = this.getArquivoPreAnaliseConclusao(obj.getId(), null);
			pendenciaArquivoDt.setHash(usuarioSessao.getCodigoHash(pendenciaArquivoDt.getId()));
			pendenciaArquivoDt.setPendenciaDt(obj);

			preAnalises.add(pendenciaArquivoDt);
		}
	
		return preAnalises;
	}

	/**
	 * Consulta todas as pré-analises finalizadas registradas para a pendência passada,
	 * montando o histórico de todas correções existentes. Já retorna o
	 * responsável pela pre-análise setado na pendência.
	 * @author jrcorrea
	 * 
	 * @param pendenciaDt:	 *            pendencia para a qual serão procuradas as pré-análises
	 * @throws Exception 
	 
	 */
	public List consultarPreAnalisesConclusaoFinalizadaAnteriores(PendenciaDt pendenciaDt, UsuarioNe usuarioSessao) throws Exception{
		List preAnalises = new ArrayList();
		PendenciaNe neObjeto = new PendenciaNe();
		PendenciaArquivoDt pendenciaArquivoDt = null;
		
		// Retorna pendências anteriores (pais)
		List pendencias = neObjeto.consultarPaiResponsavelRecursivaFinalizado(pendenciaDt);

		// Para cada pendência encontrada, seta o responsável e monta um
		// PendenciaArquivo com as informações
		for (int i = 0; i < pendencias.size(); i++) {
			PendenciaDt obj = (PendenciaDt) pendencias.get(i);
			pendenciaArquivoDt = this.getArquivoPreAnaliseConclusaoFinalizada(obj.getId(), null);
			pendenciaArquivoDt.setHash(usuarioSessao.getCodigoHash(pendenciaArquivoDt.getId()));
			pendenciaArquivoDt.setPendenciaDt(obj);

			preAnalises.add(pendenciaArquivoDt);
		}
		
		return preAnalises;
	}
	
	/**
	 * Captura o responsável por uma pré-analise passada.
	 * 
	 * @param pendenciaArquivoDt:
	 *            arquivo de pré-analise no qual será setado o responsável pela
	 *            pré-análise
	 * @author msapaula
	 */
	public void consultaResponsavelPreAnalise(PendenciaArquivoDt pendenciaArquivoDt) throws Exception {
		
		// Consulta todos os responsáveis da pendencia
		PendenciaResponsavelNe responsavelNe = new PendenciaResponsavelNe();
		List responsaveis = responsavelNe.consultarResponsaveisDetalhado(pendenciaArquivoDt.getPendenciaDt().getId());
		// Seta o assistente ou juiz como responsável da pré-analise
		pendenciaArquivoDt.setResponsavelPreAnalise(responsaveis);

	}
	
	/**
	 * Captura o responsável por uma pré-analise passada.
	 * 
	 * @param id_pendecia:
	 *            arquivo de pré-analise no qual será setado o responsável pela
	 *            pré-análise
	 * @author jesus rodrigo
	 * 26/082014
	 * @throws Exception 
	 */
	public List consultaResponsavelPreAnaliseFinalizada(String id_pendencia) throws Exception{
		List tempList = null;
		
		// Consulta todos os responsáveis da pendencia
		PendenciaResponsavelNe responsavelNe = new PendenciaResponsavelNe();
		tempList = responsavelNe.consultarResponsaveisDetalhadoPendenciaFinalizada(id_pendencia,null,null);			

		return tempList;
	}

	/**
	 * Altera o status de um arquivo em uma pendência
	 * 
	 * @param pendenciaArquivodt,
	 *            dt com dados do arquivo na pendência
	 * @param novoStatus,
	 *            novo status do arquivo 0 - normal, 1 - vírus, 2 - restrição
	 *            download
	 * 
	 * @author lsbernardes
	 */
	public void alterarStatusPendenciaArquivo(PendenciaArquivoDt pendenciaoArquivodt, String novoStatus) throws Exception {
		LogDt obLogDt;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			PendenciaArquivoPs obPersistencia = new PendenciaArquivoPs(obFabricaConexao.getConexao());

			String valorAtual = "[Id_PendenciaArquivo:" + pendenciaoArquivodt.getId() + ";CodigoTemp:" + pendenciaoArquivodt.getCodigoTemp() + "]";
			String valorNovo = "[Id_PendenciaArquivo:" + pendenciaoArquivodt.getId() + ";CodigoTemp:" + novoStatus + "]";

			obLogDt = new LogDt("PendenciaArquivo", pendenciaoArquivodt.getId(), pendenciaoArquivodt.getId_UsuarioLog(), pendenciaoArquivodt.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar), valorAtual, valorNovo);
			obPersistencia.alterarStatusPendenciaArquivo(pendenciaoArquivodt.getId(), novoStatus);

			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}

	/**
	 * Publica os arquivos já salvos que foram passados, cria os relacionamentos de pendencia arquivo e cria a indexação do conteúdo do pdf para consulta 
	 * 
	 * @author msapaula
	 * @param pendenciaDt, pojo de pendencia
	 * @param arquivos, lista de arquivos
	 * @param resposta, especifica o valor (resposta) para todos os vinculos
	 * @param obFabricaConexao, fabrica de conxao
	 */
	public void publicarArquivos(PendenciaDt pendenciaDt, List arquivos, boolean resposta, FabricaConexao obFabricaConexao) throws Exception {
		
		// Modifica a conexao local
		PendenciaArquivoPs obPersistencia = new PendenciaArquivoPs(obFabricaConexao.getConexao());

		// Salva a indexação do conteúdo para cada arquivo e insere os vínculos PendenciaArquivo
		Iterator itArquivos = arquivos.iterator();

		IndexadorNe indexadorArquivo = new IndexadorNe();
		
		while (itArquivos.hasNext()) {
			// Pega o arquivo da lista
			ArquivoDt arquivo = (ArquivoDt) itArquivos.next();

			//pego o conteudo do arquivo para fazer a indexação
			byte[] byConteudoArquivo = arquivo.getConteudo();
			String palavras = "";
			//transformo o conteudo de byte para pdf
			if (arquivo.getNomeArquivo().indexOf(".html") > 0) {
				palavras = ConverterHTML.htmlToString(byConteudoArquivo);
			}else if (arquivo.getNomeArquivo().indexOf(".pdf") > 0) {
				palavras = ConverterPDF.pdfToString(byConteudoArquivo);
			}

			// Prepara o Pendencia Arquivo
			PendenciaArquivoDt pendenciaArquivo = new PendenciaArquivoDt();
			pendenciaArquivo.setId_Pendencia(pendenciaDt.getId());
			pendenciaArquivo.setId_Arquivo(arquivo.getId());
			pendenciaArquivo.setResposta(resposta ? "true" : "false");

			// Inserir a ligacao entre arquivo e pendencia arquivo
			obPersistencia.inserir(pendenciaArquivo);
			
			// Salvar a indexação...alteração novo
			indexadorArquivo.indexar(arquivo.getId(), arquivos, obFabricaConexao);
		}
		
	}

	/**
	 * Salva a pré-análise de uma pendência, salvando o arquivo com o texto da pré-análise. Método estruturado para
	 * tratar múltiplas pré-analises
	 * 
	 * @param preAnaliseConclusaoDt, dt com os dados da pré-análise
	 * @param usuarioDt, usuário que está realizando a pré-análise
	 * @author msapaula
	 */
	public void salvarPreAnalisePendencia(AnalisePendenciaDt preAnalisePendenciaDt, UsuarioDt usuarioDt) throws Exception {
	    FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();			

			// Pega o arquivo da pré-análise simples ou múltipla
			PendenciaArquivoDt preAnalise = preAnalisePendenciaDt.getArquivoPreAnalise();

			if (preAnalise == null) inserirPreAnalisePendencia(preAnalisePendenciaDt, usuarioDt, obFabricaConexao);
			else {
				//Nesse caso irá somente alterar o conteúdo do arquivo da pré-análise
				salvaArquivoPreAnalise(preAnalisePendenciaDt, false, obFabricaConexao);
			}

			obFabricaConexao.finalizarTransacao();
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}

	/**
	 * Método responsavel em inserir pré-analise simples ou múltipla para outras pendências.
	 * 
	 * @param preAnalisePendenciaDt, objeto com dados da pré-analise
	 * @param usuarioDt, usuario pré-analisador
	 * @author msapaula
	 * @throws Exception 
	 */
	private void inserirPreAnalisePendencia(AnalisePendenciaDt preAnalisePendenciaDt, UsuarioDt usuarioDt, FabricaConexao conexao) throws Exception{

		// Salva arquivo da pré-analise (texto redigido)
		ArquivoDt arquivoPreAnalise = salvaArquivoPreAnalise(preAnalisePendenciaDt, true, conexao);

		List pendenciasFechar = preAnalisePendenciaDt.getListaPendenciasFechar();
		// Para cada pendência a ser fechada
		for (int i = 0; i < pendenciasFechar.size(); i++) {
			PendenciaDt pendenciaDt = (PendenciaDt) pendenciasFechar.get(i);

			// Vincula arquivo a pendência
			vincularPendenciaArquivo(pendenciaDt, arquivoPreAnalise, true, preAnalisePendenciaDt.isPendenteAssinatura(), conexao);

			// Se usuário é assistente, adiciona ele como responsável pela  pendência
			//if (Funcoes.StringToInt(usuarioDt.getGrupoCodigo()) == GrupoDt.ASSISTENTES_ADVOGADOS_PROMOTORES) {
			if (usuarioDt.isAssessorAdvogado() || usuarioDt.isAssessorMP() ) {
				PendenciaResponsavelNe responsavelNe = new PendenciaResponsavelNe();
				LogDt logDt = new LogDt(preAnalisePendenciaDt.getId_UsuarioLog(), preAnalisePendenciaDt.getIpComputadorLog());
				responsavelNe.salvar(getResponsavelPreAnalise(pendenciaDt, usuarioDt.getId_UsuarioServentia(), "", logDt), conexao);
			}
		}
	}

	/**
	 * Método que monta o arquivo da pré-analise (texto redigido) e salva esse.
	 * 
	 * @param analisePendenciaDt, objeto com dados da pré-analise
	 * @param novo, define se será gerado um novo arquivo, ou caso false, arquivo será alterado.
	 * @author msapaula
	 * @throws Exception 
	 */
	private ArquivoDt salvaArquivoPreAnalise(AnalisePendenciaDt preAnalisePendenciaDt, boolean novo, FabricaConexao conexao) throws Exception{
		ArquivoNe arquivoNe = new ArquivoNe();
		ArquivoDt arquivoPreAnalise = null;

		if (novo) {
			arquivoPreAnalise = new ArquivoDt();
		} else {
			PendenciaArquivoDt pendenciaArquivo = preAnalisePendenciaDt.getArquivoPreAnalise();
			arquivoPreAnalise = pendenciaArquivo.getArquivoDt();
			if (pendenciaArquivo.getResposta().equalsIgnoreCase("True")){
				if (preAnalisePendenciaDt.isPendenteAssinatura()) atualizeStatusPreAnalisesSimplesParaPendentesAssinatura(pendenciaArquivo.getId(), conexao);
				else atualizeStatusPreAnalisesSimplesParaNaoPendentesAssinatura(pendenciaArquivo.getId(), conexao);
			}
		}
		arquivoPreAnalise = montaArquivoPreAnalise(preAnalisePendenciaDt, arquivoPreAnalise);
		arquivoNe.salvar(arquivoPreAnalise, conexao);

		return arquivoPreAnalise;
	}

	/**
	 * Monta o arquivo com o texto da pré-analise redigida pelo assistente.
	 * Nesse arquivo será concatenado o texto redigido pelo usuário com outras informações importantes que não podem ser persistidas no momento por
	 * se tratar de uma pré-análise. Essas informações serão separadas por <!--Projudi Projudi-->.
	 * 
	 * @param preAnalisePendenciaDt, dt com os dados da pré-analise
	 * @param id_Pendencia, pendência a qual será vinculado o PendenciaArquivo
	 * @author msapaula
	 */
	public ArquivoDt montaArquivoPreAnalise(AnalisePendenciaDt preAnalisePendenciaDt, ArquivoDt arquivoDt){

		if (arquivoDt == null) arquivoDt = new ArquivoDt();

		// Monta conteúdo do arquivo da pré-análise, concatenando o texto digitado com os dados importantes da pré-analise que não serão persistidos
		String conteudoArquivo = "<!--Projudi Id_MovimentacaoTipo:" + preAnalisePendenciaDt.getId_MovimentacaoTipo();
		conteudoArquivo += ";MovimentacaoTipo:" + preAnalisePendenciaDt.getMovimentacaoTipo();
		conteudoArquivo += ";MovimentacaoComplemento:" + preAnalisePendenciaDt.getComplementoMovimentacao();
		conteudoArquivo += " Projudi-->";
		String conteudoEditor = preAnalisePendenciaDt.getTextoEditor();

		if (conteudoEditor != null && conteudoEditor.length() > 0) {
			conteudoEditor = conteudoEditor.replaceAll("(<!--Projudi(.*?)Projudi-->)", "");
		}
		conteudoArquivo += conteudoEditor;

		arquivoDt.setArquivo(conteudoArquivo);
		arquivoDt.setId_ArquivoTipo(preAnalisePendenciaDt.getId_ArquivoTipo());
		
		if (preAnalisePendenciaDt.getNomeArquivo() != null && !preAnalisePendenciaDt.getNomeArquivo().equals(""))
			arquivoDt.setNomeArquivo(preAnalisePendenciaDt.getNomeArquivo().replace(".html","")+".html");
		else
			arquivoDt.setNomeArquivo("online.html");
		
		arquivoDt.setContentType("text/html");
		arquivoDt.setId_UsuarioLog(preAnalisePendenciaDt.getId_UsuarioLog());
		arquivoDt.setIpComputadorLog(preAnalisePendenciaDt.getIpComputadorLog());

		return arquivoDt;
	}
	
	/**
	 * Monta o arquivo com o texto da pré-analise redigida pelo assistente.
	 * Nesse arquivo será concatenado o texto redigido pelo usuário com outras informações importantes que não podem ser persistidas no momento por
	 * se tratar de uma pré-análise. Essas informações serão separadas por <!--Projudi Projudi-->.
	 * 
	 * @param preAnalisePendenciaDt, dt com os dados da pré-analise
	 * @param id_Pendencia, pendência a qual será vinculado o PendenciaArquivo
	 * @author lsbernardes
	 */
	public void montaArquivoPreAnalisePrecatoria(ComarcaDt comarcaDt, ArquivoDt arquivoDt){

		// Monta conteúdo do arquivo da pré-análise, concatenando o texto digitado com os dados importantes da pré-analise que não serão persistidos
		String conteudoArquivo = "<!--Projudi Id_Comarca:" + comarcaDt.getId();
		conteudoArquivo += ";Comarca:" + comarcaDt.getComarca();
		conteudoArquivo += " Projudi-->";
		String conteudoEditor = arquivoDt.getArquivo();

		if (conteudoEditor != null && conteudoEditor.length() > 0) {
			conteudoEditor = conteudoEditor.replaceAll("(<!--Projudi(.*?)Projudi-->)", "");
		}
		conteudoArquivo += conteudoEditor;
		arquivoDt.setArquivo(conteudoArquivo);

	}
	
	/**
	 * Monta o texto de pré-análise concatenando no arquivo os campos específicos do mandado judicial
	 * 
	 * @param pendenciaDt
	 * @param arquivoDt
	 * @author hrrosa
	 */
	public void montaArquivoPreAnaliseMandado(PendenciaDt pendenciaDt, ServentiaDt serventiaExpedirDt, ArquivoDt arquivoDt){

		// Monta conteúdo do arquivo da pré-análise, concatenando o texto digitado com os dados importantes da pré-analise que não serão persistidos
		String conteudoArquivo = "<!--Projudi qtdLocomocoesMandado:" + pendenciaDt.getQtdLocomocoesMandado();
		conteudoArquivo += ";idMandadoTipo:" + pendenciaDt.getId_MandadoTipo();
		conteudoArquivo += ";mandadoTipo:" + pendenciaDt.getMandadoTipo();
		conteudoArquivo += ";numeroMandadoReservadoPreanalise:" + pendenciaDt.getNumeroReservadoMandadoExpedir();
		conteudoArquivo += ";prazoMandado:" + pendenciaDt.getPrazoMandado();
		conteudoArquivo += ";codigoPrazoMandado:" + pendenciaDt.getCodigoPrazoMandado();
		conteudoArquivo += ";Id_ServentiaExpedir:" + serventiaExpedirDt.getId();
		conteudoArquivo += ";ServentiaExpedir:" + serventiaExpedirDt.getServentia();
		conteudoArquivo += " Projudi-->";
		String conteudoEditor = arquivoDt.getArquivo();

		if (conteudoEditor != null && conteudoEditor.length() > 0) {
			conteudoEditor = conteudoEditor.replaceAll("(<!--Projudi(.*?)Projudi-->)", "");
		}
		conteudoArquivo += conteudoEditor;
		arquivoDt.setArquivo(conteudoArquivo);

	}

	/**
	 * Consulta as pré-análises simples de pendências realizadas por um usuário ou ServentiaCargo. 
	 * Retorna as que ainda não foram assinadas
	 * 
	 * @param usuarioDt, usuário responsável pelas pré-análises
	 * @param numeroProcesso, filtro para número de processo
	 * @param id_PendenciaTipo, filtro para tipo de pendência
	 * @author msapaula
	 */
	public List consultarPreAnalisesSimples(UsuarioDt usuarioDt, String numeroProcesso, String id_PendenciaTipo) throws Exception {
		List preAnalises = null;
		String digitoVerificador = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			PendenciaArquivoPs obPersistencia = new PendenciaArquivoPs(obFabricaConexao.getConexao());
			//int grupo = Funcoes.StringToInt(usuarioDt.getGrupoCodigo());
			int grupoTipo = Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo());

			//Divide número de processo do dígito verificador
			String[] stTemp = numeroProcesso.split("\\.");
			if (stTemp.length >= 1) numeroProcesso = stTemp[0];
			else numeroProcesso = "";
			if (stTemp.length >= 2) digitoVerificador = stTemp[1];

			switch (grupoTipo) {
//				case GrupoDt.JUIZES_VARA:
//				case GrupoDt.DESEMBARGADOR:
//				case GrupoDt.ASSISTENTES_GABINETE:
//				case GrupoDt.MINISTERIO_PUBLICO:
				case GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU:
				case GrupoTipoDt.JUIZ_TURMA:
				case GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU:
				case GrupoTipoDt.ASSISTENTE_GABINETE:
				case GrupoTipoDt.ASSISTENTE_GABINETE_FLUXO:
					preAnalises = obPersistencia.consultarPreAnalisesSimples(usuarioDt.getId_ServentiaCargo(), null, numeroProcesso, digitoVerificador, id_PendenciaTipo);
					break;
					
				case GrupoTipoDt.MP:
					preAnalises = obPersistencia.consultarPreAnalisesSimplesCargo(usuarioDt.getId_ServentiaCargo(), numeroProcesso, digitoVerificador, id_PendenciaTipo);
					break;

//				case GrupoDt.ADVOGADOS:
				case GrupoTipoDt.ADVOGADO:
					preAnalises = obPersistencia.consultarPreAnalisesSimples(usuarioDt.getId_UsuarioServentia(), numeroProcesso, digitoVerificador, id_PendenciaTipo);
					break;
					
//				case GrupoDt.ASSISTENTES_JUIZES_VARA:
				case GrupoTipoDt.ASSESSOR_JUIZ_VARA_TURMA:
				case GrupoTipoDt.ASSESSOR_PRESIDENTE_SEGUNDO_GRAU:
				case GrupoTipoDt.ASSESSOR_DESEMBARGADOR:
					if (usuarioDt.getGrupoUsuarioChefe() != null && usuarioDt.getGrupoUsuarioChefe().length() > 0) {
						if (usuarioDt.getId_ServentiaCargoUsuarioChefe() != null && !usuarioDt.getId_ServentiaCargoUsuarioChefe().equals("")) {
							preAnalises = obPersistencia.consultarPreAnalisesSimples(usuarioDt.getId_ServentiaCargoUsuarioChefe(), null, numeroProcesso, digitoVerificador, id_PendenciaTipo);
						}
					}
					break;

//				case GrupoDt.ASSISTENTES_ADVOGADOS_PROMOTORES:
				case GrupoTipoDt.ASSESSOR_MP:
					if (usuarioDt.getId_ServentiaCargoUsuarioChefe() != null && !usuarioDt.getId_ServentiaCargoUsuarioChefe().equals("")) {
						preAnalises = obPersistencia.consultarPreAnalisesSimplesCargo(usuarioDt.getId_ServentiaCargoUsuarioChefe(), numeroProcesso, digitoVerificador, id_PendenciaTipo);
					}
					break;
				case GrupoTipoDt.ASSESSOR_ADVOGADO:
					if (usuarioDt.getGrupoUsuarioChefe() != null && usuarioDt.getGrupoUsuarioChefe().length() > 0) {
						preAnalises = obPersistencia.consultarPreAnalisesSimples(usuarioDt.getId_UsuarioServentiaChefe(), numeroProcesso, digitoVerificador, id_PendenciaTipo);
						break;
					}					
					break;
			}
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return preAnalises;
	}

	/**
	 * Consulta as pré-análises múltiplas de pendências realizadas por um usuário ou Serventia Cargo
	 * 
	 * @param usuarioDt, usuário responsável pelas pré-análises
	 * @param numeroProcesso, filtro para número de processo
	 * @author msapaula
	 */
	public List consultarPreAnalisesMultiplas(UsuarioDt usuarioDt, String numeroProcesso) throws Exception {
		List preAnalises = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			PendenciaArquivoPs obPersistencia = new PendenciaArquivoPs(obFabricaConexao.getConexao());
			//int grupo = Funcoes.StringToInt(usuarioDt.getGrupoCodigo());
			int grupoTipo = Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo());

			switch (grupoTipo) {
//				case GrupoDt.JUIZES_VARA:
//				case GrupoDt.DESEMBARGADOR:
//				case GrupoDt.ASSISTENTES_GABINETE:
//				case GrupoDt.MINISTERIO_PUBLICO:
				case GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU:
				case GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU:
				case GrupoTipoDt.ASSISTENTE_GABINETE:
				case GrupoTipoDt.ASSISTENTE_GABINETE_FLUXO:
				case GrupoTipoDt.MP:
					preAnalises = obPersistencia.consultarPreAnalisesMultiplas(usuarioDt.getId_ServentiaCargo(), null, numeroProcesso);
					break;

//			    case GrupoDt.ADVOGADOS:
				case GrupoTipoDt.ADVOGADO:
					preAnalises = obPersistencia.consultarPreAnalisesMultiplas(null, usuarioDt.getId_UsuarioServentia(), numeroProcesso);
					break;
					
//				case GrupoDt.ASSISTENTES_JUIZES_VARA:
				case GrupoTipoDt.ASSESSOR_JUIZ_VARA_TURMA:
				case GrupoTipoDt.ASSESSOR_PRESIDENTE_SEGUNDO_GRAU:
				case GrupoTipoDt.ASSESSOR_DESEMBARGADOR:
					if (usuarioDt.getGrupoUsuarioChefe() != null && usuarioDt.getGrupoUsuarioChefe().length() > 0) {
						if (usuarioDt.getId_ServentiaCargoUsuarioChefe() != null && !usuarioDt.getId_ServentiaCargoUsuarioChefe().equals("")) {
							preAnalises = obPersistencia.consultarPreAnalisesMultiplas(usuarioDt.getId_ServentiaCargoUsuarioChefe(), null, numeroProcesso);
						}
					}
					break;

//				case GrupoDt.ASSISTENTES_ADVOGADOS_PROMOTORES:
				case GrupoTipoDt.ASSESSOR_MP:					
					if (usuarioDt.getId_ServentiaCargoUsuarioChefe() != null && !usuarioDt.getId_ServentiaCargoUsuarioChefe().equals("")) {
						preAnalises = obPersistencia.consultarPreAnalisesMultiplas(usuarioDt.getId_ServentiaCargoUsuarioChefe(), null, numeroProcesso);
					}
					break;
				case GrupoTipoDt.ASSESSOR_ADVOGADO:
					if (usuarioDt.getGrupoUsuarioChefe() != null && usuarioDt.getGrupoUsuarioChefe().length() > 0) {					
						preAnalises = obPersistencia.consultarPreAnalisesMultiplas(null, usuarioDt.getId_UsuarioServentiaChefe(), numeroProcesso);					
					}
					break;
			}

		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return preAnalises;
	}

	/**
	 * Verifica a existência de uma pré-análise para a pendência passada. 
	 * Internamente procura pelo arquivo da pré-análise (texto redigido pelo assistente ou chefe).
	 * 
	 * @param id_Pendencia, pendência para a qual será verificada se existe pré-analise
	 * 
	 * @return Objeto AnaliseConclusaoDt com os dados da pré-analise setados e com os arquivos da pré-analise 
	 * (arquivo do texto da pré-analise)
	 * 
	 * @author msapaula
	 */
	public AnalisePendenciaDt getPreAnalisePendencia(String id_Pendencia) throws Exception {
		AnalisePendenciaDt preAnalisePendenciaDt = null;
		PendenciaArquivoDt arquivoPreAnalise = null;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			PendenciaArquivoPs obPersistencia = new PendenciaArquivoPs(obFabricaConexao.getConexao());

			// Procura por PendenciaArquivo da pré-analise
			arquivoPreAnalise = obPersistencia.getArquivoPreAnalise(id_Pendencia);

			if (arquivoPreAnalise != null) {
				String conteudoArquivo = arquivoPreAnalise.getArquivoDt().getArquivo();

				//Chama método para ler pré-analise de pendência
				preAnalisePendenciaDt = lerDadosPreAnalisePendencia(conteudoArquivo);
				preAnalisePendenciaDt.setArquivoPreAnalise(arquivoPreAnalise);
			}

		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return preAnalisePendenciaDt;
	}

	/**
	 * Realiza chamada ao método que obtém os dados da pré-análise de uma pendência sem uma conexão já iniciada.
	 * @param arquivoPreAnaliseDt
	 * @param fabConexao
	 * @throws Exception 
	 */
	public PreAnalisePendenciaDt getPreAnalisePendencia(PendenciaArquivoDt arquivoPreAnaliseDt) throws Exception{
		return this.getPreAnalisePendencia(arquivoPreAnaliseDt, null);
	}

	/**
	 * Obtém os dados de uma pré-analise de pendência baseado no arquivo passado. 
	 * A partir desse arquivo será obtida o conteúdo da pré-análise, que será interpretada e os dados 
	 * setados no objeto PreAnalisePendenciaDt
	 * 
	 * @param arquivoPreAnaliseDt, arquivo da pré-análise (texto)
	 * @return Objeto PreAnalisePendenciaDt com os dados da pré-analise setados
	 * @author msapaula
	 */
	private PreAnalisePendenciaDt getPreAnalisePendencia(PendenciaArquivoDt arquivoPreAnaliseDt, FabricaConexao fabConexao) throws Exception {
		PreAnalisePendenciaDt preAnalisePendenciaDt = null;

		if (arquivoPreAnaliseDt != null) {

			preAnalisePendenciaDt = lerDadosPreAnalisePendencia(arquivoPreAnaliseDt.getArquivoDt().getArquivo());

			// Seta arquivo da pré-análise no objeto PreAnalisePendenciaDt
			preAnalisePendenciaDt.setArquivoPreAnalise(arquivoPreAnaliseDt);

		}
		return preAnalisePendenciaDt;
	}

	/**
	 * Retorna o arquivo da pré-análise feita para a pendência passada.
	 * Conexão será iniciada nesse momento
	 * @param id_Pendencia
	 * @return
	 * @throws Exception
	 */
	public PendenciaArquivoDt getArquivoPreAnalise(String id_Pendencia) throws Exception{
		return this.getArquivoPreAnalise(id_Pendencia, null);

	}
	
	/**
	 * Retorna o arquivo da pré-análise feita para a pendência passada.
	 * Conexão será iniciada nesse momento
	 * @param id_Pendencia
	 * @return
	 * @throws Exception
	 */
	public PendenciaArquivoDt getArquivoPreAnaliseFinalizada(String id_Pendencia) throws Exception{
		return this.getArquivoPreAnaliseFinalizada(id_Pendencia, null);

	}

	/**
	 * Retorna o Arquivo da pré-analise feita para a pendência passada (texto da pré-análise), retornando também o usuário responsável
	 * 
	 * @param id_Pendencia, pendência para a qual será buscada a pré-análise
	 * @return PendenciaArquivoDt referente ao arquivo da pré-análise
	 * @author msapaula
	 */
	private PendenciaArquivoDt getArquivoPreAnalise(String id_Pendencia, FabricaConexao fabConexao) throws Exception {
		PendenciaArquivoDt pendenciaArquivoDt = null;
		FabricaConexao obFabricaConexao = null;
		try{
			if (fabConexao == null) obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			else obFabricaConexao = fabConexao;
			PendenciaArquivoPs obPersistencia = new PendenciaArquivoPs(obFabricaConexao.getConexao());

			pendenciaArquivoDt = obPersistencia.getArquivoPreAnalise(id_Pendencia);
		
		} finally{
			if (fabConexao == null) obFabricaConexao.fecharConexao();
		}
		return pendenciaArquivoDt;
	}
	
	/**
	 * Retorna o Arquivo da pré-analise feita para a pendência passada (texto da pré-análise), retornando também o usuário responsável
	 * 
	 * @param id_Pendencia, pendência para a qual será buscada a pré-análise
	 * @return PendenciaArquivoDt referente ao arquivo da pré-análise
	 * @author lsbernardes
	 */
	private PendenciaArquivoDt getArquivoPreAnaliseFinalizada(String id_Pendencia, FabricaConexao fabConexao) throws Exception {
		PendenciaArquivoDt pendenciaArquivoDt = null;
		FabricaConexao obFabricaConexao = null;
		try{
			if (fabConexao == null) obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			else obFabricaConexao = fabConexao;
			PendenciaArquivoPs obPersistencia = new PendenciaArquivoPs(obFabricaConexao.getConexao());

			pendenciaArquivoDt = obPersistencia.getArquivoPreAnaliseFinalizada(id_Pendencia);
		
		} finally{
			if (fabConexao == null) obFabricaConexao.fecharConexao();
		}
		return pendenciaArquivoDt;
	}

	/**
	 * Interpreta os dados do arquivo da pré-análise, pois juntamente com o texto digitado pelo usuário, terá outras informações
	 * separadas pela tag <!--Projudi Projudi-->
	 * 
	 * @param conteudoArquivo, String com o conteúdo do arquivo
	 * @return PreAnalisePendenciaDt com dados necessários para finalizar a análise
	 * @author msapaula
	 */
	private PreAnalisePendenciaDt lerDadosPreAnalisePendencia(String conteudoArquivo){
		PreAnalisePendenciaDt preAnalisePendenciaDt = new PreAnalisePendenciaDt();
		String[] dadosMovimentacao = null;
		String dados = null;

		if (conteudoArquivo != null && conteudoArquivo.length() > 0) {
			Pattern paTeste = Pattern.compile("<!--Projudi(.*?)Projudi-->");
			Matcher maTeste = paTeste.matcher(conteudoArquivo);
			dados = "";
			while (maTeste.find()) {
				dados += maTeste.group().replaceAll("(<!--Projudi)|(Projudi-->)", "");
			}
		}

		if (dados != null && dados.length() > 0) {
			dadosMovimentacao = dados.split(";");
			String[] id_MovimentacaoTipo = dadosMovimentacao[0].split(":");
			String[] movimentacaoTipo = dadosMovimentacao[1].split(":");
			String[] movimentacaoComplemento = null;
			if (dadosMovimentacao.length > 2)
				movimentacaoComplemento = dadosMovimentacao[2].split(":");
				
			if (dadosMovimentacao.length > 0) {
				if(id_MovimentacaoTipo!= null && id_MovimentacaoTipo.length > 1 
						&& id_MovimentacaoTipo[1].length()>1){
					preAnalisePendenciaDt.setId_MovimentacaoTipo(id_MovimentacaoTipo[1]);
				}
				if(movimentacaoTipo!= null && movimentacaoTipo.length > 1 
						&& movimentacaoTipo[1].length()>1){
					preAnalisePendenciaDt.setMovimentacaoTipo(movimentacaoTipo[1]);
				}
				if (movimentacaoComplemento != null && movimentacaoComplemento.length > 1 
						&& movimentacaoComplemento[1].length()>1){
					preAnalisePendenciaDt.setComplementoMovimentacao(movimentacaoComplemento[1]);
				}
			}
			
		}
		return preAnalisePendenciaDt;
	}
	
	/**
	 * Interpreta os dados do arquivo da pré-análise, pois juntamente com o texto digitado pelo usuário, terá outras informações
	 * separadas pela tag <!--Projudi Projudi-->
	 * 
	 * @param conteudoArquivo, String com o conteúdo do arquivo
	 * @return PreAnalisePendenciaDt com dados necessários para finalizar a análise
	 * @author lsbernardes
	 */
	public String[] lerDadosPreAnalisePendenciaPrecatoria(ArquivoDt arquivoDt){
		String[] dadosComarca = null;
		String[] retorno = new String[2];
		String dados = null;

		if (arquivoDt.getArquivo() != null && arquivoDt.getArquivo().length() > 0) {
			Pattern paTeste = Pattern.compile("<!--Projudi(.*?)Projudi-->");
			Matcher maTeste = paTeste.matcher(arquivoDt.getArquivo());
			dados = "";
			while (maTeste.find()) {
				dados += maTeste.group().replaceAll("(<!--Projudi)|(Projudi-->)", "");
			}
		}

		if (dados != null && dados.length() > 0) {
			dadosComarca = dados.split(";");
			String[] id_Comarca = dadosComarca[0].split(":");
			String[] comarca = dadosComarca[1].split(":");
			retorno[0] = id_Comarca[1];
			retorno[1] = comarca[1];
		}
		return retorno;
	}

	/**
	 * Consulta as pré-análises de pendências que já foram finalizadas, assinadas pelo juiz.
	 * 
	 * @param usuarioDt, usuário que realizou a pré-analise
	 * @author msapaula
	 */
	public List consultarPreAnalisesFinalizadas(String numeroProcesso, String dataInicial, String dataFinal, UsuarioDt usuarioDt) throws Exception {
		List preAnalises = null;
		String digitoVerificador = null;
		FabricaConexao obFabricaConexao = null;
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			PendenciaArquivoPs obPersistencia = new PendenciaArquivoPs(obFabricaConexao.getConexao());
			//int grupo = Funcoes.StringToInt(usuarioDt.getGrupoCodigo());
			int grupoTipo = Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo());

			//Divide número de processo do dígito verificador
			String[] stTemp = numeroProcesso.split("\\.");
			if (stTemp.length >= 1) numeroProcesso = stTemp[0];
			else numeroProcesso = "";
			if (stTemp.length >= 2) digitoVerificador = stTemp[1];

			switch (grupoTipo) {
//			    case GrupoDt.JUIZES_VARA:
//				case GrupoDt.DESEMBARGADOR:
//				case GrupoDt.ASSISTENTES_GABINETE:
//				case GrupoDt.PROMOTORES:
				case GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU:
				case GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU:
				case GrupoTipoDt.ASSISTENTE_GABINETE:
				case GrupoTipoDt.ASSISTENTE_GABINETE_FLUXO:
				case GrupoTipoDt.MP:
					preAnalises = obPersistencia.consultarPreAnalisesFinalizadas(usuarioDt.getId_ServentiaCargo(), null, numeroProcesso, digitoVerificador, dataInicial, dataFinal);
					break;
					
//				case GrupoDt.ADVOGADOS:
				case GrupoTipoDt.ADVOGADO:
					preAnalises = obPersistencia.consultarPreAnalisesFinalizadas(null, usuarioDt.getId_UsuarioServentia(), numeroProcesso, digitoVerificador, dataInicial, dataFinal);
					break;

//				case GrupoDt.ASSISTENTES_JUIZES_VARA:
				case GrupoTipoDt.ASSESSOR_JUIZ_VARA_TURMA:
				case GrupoTipoDt.ASSESSOR_PRESIDENTE_SEGUNDO_GRAU:
				case GrupoTipoDt.ASSESSOR_DESEMBARGADOR:
					if (usuarioDt.getGrupoUsuarioChefe() != null && usuarioDt.getGrupoUsuarioChefe().length() > 0) {
						if (usuarioDt.getId_ServentiaCargoUsuarioChefe() != null && !usuarioDt.getId_ServentiaCargoUsuarioChefe().equals("")) {
							preAnalises = obPersistencia.consultarPreAnalisesFinalizadas(usuarioDt.getId_ServentiaCargoUsuarioChefe(), null, numeroProcesso, digitoVerificador, dataInicial, dataFinal);
						}
					}
					break;
					
//				case GrupoDt.ASSISTENTES_ADVOGADOS_PROMOTORES:
				case GrupoTipoDt.ASSESSOR_MP:																
					if (usuarioDt.getId_ServentiaCargoUsuarioChefe() != null && !usuarioDt.getId_ServentiaCargoUsuarioChefe().equals("")) {
						preAnalises = obPersistencia.consultarPreAnalisesFinalizadas(usuarioDt.getId_ServentiaCargoUsuarioChefe(), null, numeroProcesso, digitoVerificador, dataInicial, dataFinal);
					}
					break;
				case GrupoTipoDt.ASSESSOR_ADVOGADO:
					if (usuarioDt.getGrupoUsuarioChefe() != null && usuarioDt.getGrupoUsuarioChefe().length() > 0) {
						preAnalises = obPersistencia.consultarPreAnalisesFinalizadas(null, usuarioDt.getId_UsuarioServentiaChefe(), numeroProcesso, digitoVerificador, dataInicial, dataFinal);						
					}
					break;
			}
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return preAnalises;
	}

	/**
	 * Consultar arquivos de resposta de uma determinada pendência com o hash do arquivo
	 * 
	 * @author msapaula
	 * @param id_Pendencia, id da pendencia
	 * @param usuarioSessao, usuario da sessao
	 * @param conexao, conexao para poder continuar uma transacao
	 * @throws Exception
	 */
	public List consultarArquivosRespostaPendencia(String id_Pendencia, UsuarioNe usuarioSessao) throws Exception {
		List arquivos = null;
		FabricaConexao obFabricaConexao = null;
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			PendenciaArquivoPs obPersistencia = new PendenciaArquivoPs(obFabricaConexao.getConexao());

			arquivos = obPersistencia.consultarArquivosRespostaPendencia(id_Pendencia);

			if (arquivos != null) {
				Iterator iterator = arquivos.iterator();
				while (iterator.hasNext()) {
					PendenciaArquivoDt pendenciaArquivoDt = (PendenciaArquivoDt) iterator.next();
					pendenciaArquivoDt.setHash(usuarioSessao.getCodigoHash(pendenciaArquivoDt.getId()));
				}
			}
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return arquivos;
	}
	
	/**
	 * Consultar arquivos de resposta de uma determinada pendência com o hash do arquivo
	 * 
	 * @author lsbernardes
	 * @param id_Pendencia, id da pendencia
	 * @param usuarioSessao, usuario da sessao
	 * @param conexao, conexao para poder continuar uma transacao
	 * @throws Exception
	 */
	public List consultarArquivosRespostaPendenciaFinalizada(String id_Pendencia, UsuarioNe usuarioSessao) throws Exception {
		List arquivos = null;
		FabricaConexao obFabricaConexao = null;
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			PendenciaArquivoPs obPersistencia = new PendenciaArquivoPs(obFabricaConexao.getConexao());

			arquivos = obPersistencia.consultarArquivosRespostaPendenciaFinalizada(id_Pendencia);

			if (arquivos != null) {
				Iterator iterator = arquivos.iterator();
				while (iterator.hasNext()) {
					PendenciaArquivoDt pendenciaArquivoDt = (PendenciaArquivoDt) iterator.next();
					pendenciaArquivoDt.setHash(usuarioSessao.getCodigoHash(pendenciaArquivoDt.getId()));
				}
			}
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return arquivos;
	}

	/**
	 * Consulta todas as pré-analises registradas para a pendência passada, montando o histórico de todas correções existentes. 
	 * Já retorna o responsável pela pre-análise setado na pendência.
	 * 
	 * @param pendenciaDt, pendencia para a qual serão procuradas as pré-análises
	 * @author msapaula
	 * @throws Exception 
	 */
	public List consultarPreAnalisesAnteriores(PendenciaDt pendenciaDt, UsuarioNe usuarioSessao) throws Exception{
		List preAnalises = new ArrayList();
		PendenciaNe neObjeto = new PendenciaNe();
		PendenciaArquivoDt pendenciaArquivoDt = null;
		
		// Retorna pendências anteriores (pais)
		List pendencias = neObjeto.consultarPaiResponsavelRecursiva(pendenciaDt);

		// Para cada pendência encontrada, seta o responsável e monta um PendenciaArquivo com as informações
		for (int i = 0; i < pendencias.size(); i++) {
			PendenciaDt obj = (PendenciaDt) pendencias.get(i);
			pendenciaArquivoDt = this.getArquivoPreAnaliseFinalizada(obj.getId());
			if (pendenciaArquivoDt != null) {
				pendenciaArquivoDt.setHash(usuarioSessao.getCodigoHash(pendenciaArquivoDt.getId()));
				pendenciaArquivoDt.setPendenciaDt(obj);
				preAnalises.add(pendenciaArquivoDt);
			}
		}

		return preAnalises;
	}
	
	/**
	 * Consulta as pré analises pendentes de assinatura 
	 * @param usuarioDt
	 * @param numeroProcesso
	 * @param id_Classificador
	 * @param id_PendenciaTipo
	 * @return
	 * @author mmgomes
	 * @throws Exception
	 */
	public List consultarPreAnalisesConclusaoSimplesPendentesAssinatura(UsuarioDt usuarioDt, String numeroProcesso, String id_Classificador, String id_PendenciaTipo) throws Exception {
		List preAnalises = null;
		String digitoVerificador = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			PendenciaArquivoPs obPersistencia = new PendenciaArquivoPs(obFabricaConexao.getConexao());			
			int grupoTipo = Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo());

			//Divide número de processo do dígito verificador
			String[] stTemp = numeroProcesso.split("\\.");
			if (stTemp.length >= 1) numeroProcesso = stTemp[0];
			else numeroProcesso = "";
			if (stTemp.length >= 2) digitoVerificador = stTemp[1];

			switch (grupoTipo) {
				case GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU:
				case GrupoTipoDt.JUIZ_TURMA:
				case GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU:
				case GrupoTipoDt.PRESIDENTE_SEGUNDO_GRAU:
				case GrupoTipoDt.ASSISTENTE_GABINETE:
				case GrupoTipoDt.ASSISTENTE_GABINETE_FLUXO:
					preAnalises = obPersistencia.consultarPreAnalisesConclusaoSimplesPendentesAssinatura(usuarioDt.getId_ServentiaCargo(), numeroProcesso, digitoVerificador, id_Classificador, id_PendenciaTipo);
					break;

				case GrupoTipoDt.ASSESSOR_JUIZ_VARA_TURMA:
				case GrupoTipoDt.ASSESSOR_PRESIDENTE_SEGUNDO_GRAU:
				case GrupoTipoDt.ASSESSOR_DESEMBARGADOR:
					preAnalises = obPersistencia.consultarPreAnalisesConclusaoSimplesPendentesAssinatura(usuarioDt.getId_ServentiaCargoUsuarioChefe(), numeroProcesso, digitoVerificador, id_Classificador, id_PendenciaTipo);
					break;
			}

		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return preAnalises;
	}
	
	/**
	 * Altera o status de um arquivo em uma lista de pendências
	 *  
	 * @param listaPendenciaArquivodt
	 * @param novoStatus
	 * @throws Exception
	 * 
	 * @author mmgomes
	 */
	public void alterarStatusPendenciaArquivo(List listaPendenciaArquivodt, String novoStatus) throws Exception {		
		FabricaConexao obFabricaConexao = null;
		PendenciaArquivoDt pendenciaArquivodt = null;
		LogDt obLogDt = null;
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			PendenciaArquivoPs obPersistencia = new PendenciaArquivoPs(obFabricaConexao.getConexao());
			
			for(int i = 0 ; i< listaPendenciaArquivodt.size();i++) {
				pendenciaArquivodt = (PendenciaArquivoDt)listaPendenciaArquivodt.get(i);
				
				String valorAtual = "[Id_PendenciaArquivo:" + pendenciaArquivodt.getId() + ";CodigoTemp:" + pendenciaArquivodt.getCodigoTemp() + "]";
				String valorNovo = "[Id_PendenciaArquivo:" + pendenciaArquivodt.getId() + ";CodigoTemp:" + novoStatus + "]";

				obLogDt = new LogDt("PendenciaArquivo", pendenciaArquivodt.getId(), pendenciaArquivodt.getId_UsuarioLog(), pendenciaArquivodt.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar), valorAtual, valorNovo);
				obPersistencia.alterarStatusPendenciaArquivo(pendenciaArquivodt.getId(), novoStatus);

				obLog.salvar(obLogDt, obFabricaConexao);
				
			}			
			
			obFabricaConexao.finalizarTransacao();
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	/**
	 *Atualize o status da pré-análise de conclusão de Normal para Aguardando Assinatura
	 * 
	 * @param id_ServentiaCargo
	 * @param Id_Pendencia
	 * @throws Exception 
	 */
	public void atualizeStatusPreAnalisesConclusaoSimplesParaPendentesAssinatura(String Id_Pendencia, FabricaConexao obFabricaConexao) throws Exception{
		
		PendenciaArquivoPs obPersistencia = new PendenciaArquivoPs(obFabricaConexao.getConexao());		
		obPersistencia.atualizeStatusPreAnalisesConclusaoSimplesParaPendentesAssinatura(Id_Pendencia);					
				
	}
	
	/**
	 * Atualize o status da pré-análise de conclusão Aguardando Assinatura para Normal
	 * 
	 * @param id_ServentiaCargo
	 * @param Id_Pendencia
	 * @throws Exception 
	 */
	public void atualizeStatusPreAnalisesConclusaoSimplesParaNaoPendentesAssinatura(String Id_Pendencia, FabricaConexao obFabricaConexao) throws Exception{
								
		PendenciaArquivoPs obPersistencia = new PendenciaArquivoPs(obFabricaConexao.getConexao());
		obPersistencia.atualizeStatusPreAnalisesConclusaoSimplesParaNaoPendentesAssinatura(Id_Pendencia);
		
	}
	
	/**
	 *Atualize o status da pré-análise de Normal para Aguardando Assinatura
	 * 
	 * @param id_ServentiaCargo
	 * @param Id_Pendencia
	 * @throws Exception 
	 */
	public void atualizeStatusPreAnalisesSimplesParaPendentesAssinatura(String Id_PendenciaArquivo, FabricaConexao obFabricaConexao) throws Exception{
		
		PendenciaArquivoPs obPersistencia = new PendenciaArquivoPs(obFabricaConexao.getConexao());			
		obPersistencia.atualizeStatusPreAnalisesSimplesParaPendentesAssinatura(Id_PendenciaArquivo);					
			
	}
	
	/**
	 * Atualize o status da pré-análise de Aguardando Assinatura para Normal
	 * 
	 * @param id_ServentiaCargo
	 * @param Id_Pendencia
	 * @throws Exception 
	 */
	public void atualizeStatusPreAnalisesSimplesParaNaoPendentesAssinatura(String Id_PendenciaArquivo, FabricaConexao obFabricaConexao) throws Exception{

		PendenciaArquivoPs obPersistencia = new PendenciaArquivoPs(obFabricaConexao.getConexao());
		obPersistencia.atualizeStatusPreAnalisesSimplesParaNaoPendentesAssinatura(Id_PendenciaArquivo);
		
	}
	
	/**
	 * Consulta as pré-análises simples de pendências realizadas por um usuário ou ServentiaCargo. 
	 * Retorna as que ainda não foram assinadas
	 * 
	 * @param usuarioDt, usuário responsável pelas pré-análises
	 * @param numeroProcesso, filtro para número de processo
	 * @param id_PendenciaTipo, filtro para tipo de pendência
	 * @author mmgomes
	 */
	public List consultarPreAnalisesSimplesPendentesAssinatura(UsuarioDt usuarioDt, String numeroProcesso, String id_PendenciaTipo) throws Exception {
		List preAnalises = null;
		String digitoVerificador = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			PendenciaArquivoPs obPersistencia = new PendenciaArquivoPs(obFabricaConexao.getConexao());
			int grupoTipo = Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo());

			//Divide número de processo do dígito verificador
			String[] stTemp = numeroProcesso.split("\\.");
			if (stTemp.length >= 1) numeroProcesso = stTemp[0];
			else numeroProcesso = "";
			if (stTemp.length >= 2) digitoVerificador = stTemp[1];

			switch (grupoTipo) {
				case GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU:
				case GrupoTipoDt.JUIZ_TURMA:
				case GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU:
				case GrupoTipoDt.ASSISTENTE_GABINETE:
				case GrupoTipoDt.ASSISTENTE_GABINETE_FLUXO:
					preAnalises = obPersistencia.consultarPreAnalisesSimplesPendentesAssinatura(usuarioDt.getId_ServentiaCargo(), null, numeroProcesso, digitoVerificador, id_PendenciaTipo);
					break;
					
				case GrupoTipoDt.MP:
					preAnalises = obPersistencia.consultarPreAnalisesSimplesCargoPendentesAssinatura(usuarioDt.getId_ServentiaCargo(), numeroProcesso, digitoVerificador, id_PendenciaTipo);
					break;

				case GrupoTipoDt.ADVOGADO:
					preAnalises = obPersistencia.consultarPreAnalisesSimplesPendentesAssinatura(usuarioDt.getId_UsuarioServentia(), numeroProcesso, digitoVerificador, id_PendenciaTipo);
					break;
					
				case GrupoTipoDt.ASSESSOR_JUIZ_VARA_TURMA:
				case GrupoTipoDt.ASSESSOR_PRESIDENTE_SEGUNDO_GRAU:
				case GrupoTipoDt.ASSESSOR_DESEMBARGADOR:
					if (usuarioDt.getGrupoUsuarioChefe() != null && usuarioDt.getGrupoUsuarioChefe().length() > 0) {
						if (usuarioDt.getId_ServentiaCargoUsuarioChefe() != null && !usuarioDt.getId_ServentiaCargoUsuarioChefe().equals("")) {
							preAnalises = obPersistencia.consultarPreAnalisesSimplesPendentesAssinatura(usuarioDt.getId_ServentiaCargoUsuarioChefe(), null, numeroProcesso, digitoVerificador, id_PendenciaTipo);
						}
					}
					break;
				case GrupoTipoDt.ASSESSOR_MP:
					if (usuarioDt.getId_ServentiaCargoUsuarioChefe() != null && !usuarioDt.getId_ServentiaCargoUsuarioChefe().equals("")) {
						preAnalises = obPersistencia.consultarPreAnalisesSimplesPendentesAssinatura(usuarioDt.getId_ServentiaCargoUsuarioChefe(), null, numeroProcesso, digitoVerificador, id_PendenciaTipo);
					}
					break;					
				case GrupoTipoDt.ASSESSOR_ADVOGADO:
					if (usuarioDt.getGrupoUsuarioChefe() != null && usuarioDt.getGrupoUsuarioChefe().length() > 0) {						
						preAnalises = obPersistencia.consultarPreAnalisesSimplesPendentesAssinatura(null, usuarioDt.getId_UsuarioServentiaChefe(), numeroProcesso, digitoVerificador, id_PendenciaTipo);						
					}
					break;
			}
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return preAnalises;
	}
	
	/**
	 * Método responsável por consultar a pendência do tipo ementa em um processo para um serventia cargo
	 * @param String id_ServentiaCargo
	 * @param String id_Processo
	 * @param FabricaConexao fabConexao
	 * @return PendenciaArquivoDt
	 * @throws Exception
	 * @author mmgomes
	 */
	public PendenciaArquivoDt consultarEmentaDesembargador(String id_ServentiaCargo, AudienciaProcessoDt AudienciaProcessoDt, String id_ProcessoTipoSessao) throws Exception {
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			
			return consultarEmentaDesembargador(id_ServentiaCargo, AudienciaProcessoDt, id_ProcessoTipoSessao, obFabricaConexao);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	// lrcampos - 04/02/2020 11:19 - Busca a ementa pelo idAudiProc em vez do idProc
	// jvosantos - 28/11/2019 16:32 - Criar método para consultar a Ementa, filtrando pela pendencia
	public PendenciaArquivoDt consultarEmentaDesembargador(String id_ServentiaCargo, String idAudiProc, String id_ProcessoTipoSessao, String idPendencia) throws Exception {
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			
			return consultarEmentaDesembargador(id_ServentiaCargo, idAudiProc, id_ProcessoTipoSessao, idPendencia, obFabricaConexao);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	public PendenciaArquivoDt consultarEmentaDesembargador(String id_ServentiaCargo, AudienciaProcessoDt audienciaProcessoDt, String id_ProcessoTipoSessao, FabricaConexao obFabricaConexao) throws Exception {
		PendenciaArquivoPs obPersistencia = new PendenciaArquivoPs(obFabricaConexao.getConexao());
		return obPersistencia.consultaEmentaDesembargador(id_ServentiaCargo, audienciaProcessoDt.getId(), id_ProcessoTipoSessao, null);
	}

	// lrcampos - 04/02/2020 11:19 - Busca a ementa pelo idAudiProc em vez do idProc
	// jvosantos - 28/11/2019 16:32 - Criar método para consultar a Ementa, filtrando pela pendencia
	public PendenciaArquivoDt consultarEmentaDesembargador(String id_ServentiaCargo, String idAudiProc, String id_ProcessoTipoSessao, String idPendencia, FabricaConexao obFabricaConexao) throws Exception {
		PendenciaArquivoPs obPersistencia = new PendenciaArquivoPs(obFabricaConexao.getConexao());
		return obPersistencia.consultaEmentaDesembargador(id_ServentiaCargo, idAudiProc, id_ProcessoTipoSessao, idPendencia);
	}
	
	public PendenciaArquivoDt consultarEmentaDesembargadorPendencia(String id_ServentiaCargo, String idAudiProc, String id_Pendencia) throws Exception {
		PendenciaArquivoDt pendenciaArquivoDt = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			
			PendenciaArquivoPs obPersistencia = new PendenciaArquivoPs(obFabricaConexao.getConexao());

			pendenciaArquivoDt = obPersistencia.consultaEmentaDesembargador(id_ServentiaCargo, idAudiProc, null, id_Pendencia);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return pendenciaArquivoDt;
	}
	
	public PendenciaArquivoDt consultarEmentaDesembargadorPendencia(String id_ServentiaCargo, String idAudiProc, String id_Pendencia, FabricaConexao obFabricaConexao) throws Exception {
		PendenciaArquivoPs obPersistencia = new PendenciaArquivoPs(obFabricaConexao.getConexao());
		return obPersistencia.consultaEmentaDesembargador(id_ServentiaCargo, idAudiProc, null, id_Pendencia);
	}
	
	/**
	 * Salva a pré-análise de uma pendência, gerando o arquivo de configuração e
	 * salvando o arquivo com o texto da pré-análise. Método estruturado para
	 * tratar múltiplas pré-analises
	 * 
	 * @param preAnaliseConclusaoDt, dt com os dados da pré-análise
	 * @param usuarioDt, usuário que está realizando a pré-análise
	 * @author mmgomes
	 * @throws Exception 
	 */	
	public void salvarPreAnaliseVotoEmenta(AnaliseConclusaoDt preAnaliseConclusaoDt, UsuarioDt usuarioDt, String idAudienciaProcessoSessaoSegundoGrau, boolean virtual, FabricaConexao obFabricaConexao) throws Exception {
		PendenciaNe pendenciaNe = new PendenciaNe();	
		AudienciaProcessoNe audienciaProcessoNe = new AudienciaProcessoNe();
		AudienciaProcessoDt audienciaProcessoDt = null;
		
		if (idAudienciaProcessoSessaoSegundoGrau != null && idAudienciaProcessoSessaoSegundoGrau.trim().length() > 0) {
			audienciaProcessoDt = audienciaProcessoNe.consultarIdCompleto(idAudienciaProcessoSessaoSegundoGrau);				
		}
		
		String Id_ServentiaCargo = usuarioDt.getId_ServentiaCargo();
		if (Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo()) == GrupoTipoDt.ASSESSOR_DESEMBARGADOR) Id_ServentiaCargo = usuarioDt.getId_ServentiaCargoUsuarioChefe();		

		// Pega o arquivo da pré-análise simples do tipo Voto
		PendenciaArquivoDt preAnalise = preAnaliseConclusaoDt.getArquivoPreAnalise();

		// Insere ou altera os dados da conclusão do tipo Voto
		if (preAnalise == null) {
			inserirPreAnaliseConclusao(preAnaliseConclusaoDt, usuarioDt, obFabricaConexao);
		}
		else 
		{
			alterarPreAnaliseConclusao(preAnaliseConclusaoDt, preAnalise, usuarioDt, obFabricaConexao);				
			if (preAnaliseConclusaoDt.getListaPendenciasGeradas() != null && preAnaliseConclusaoDt.getListaPendenciasGeradas().size() > 0) {
				PendenciaDt pendenciaGerada = (PendenciaDt)preAnaliseConclusaoDt.getListaPendenciasGeradas().get(0);
				if (audienciaProcessoDt != null) {
					audienciaProcessoDt.setId_PendenciaVotoRelator(pendenciaGerada.getId());
				}
			}
		}
		
		// Pega o arquivo da pré-análise simples do tipo Ementa
		PendenciaArquivoDt preAnaliseEmenta = preAnaliseConclusaoDt.getArquivoPreAnaliseEmenta();
		
		// Consulta dados completos do processo, pois será necessário ao salvar responsáveis pela audiência e ao gerar pendências
		ProcessoDt processoDt = new ProcessoNe().consultarIdCompleto(preAnaliseConclusaoDt.getProcessoDt().getId(), obFabricaConexao);						
		PendenciaDt pendenciaProcessoDt = new PendenciaDt();
		pendenciaProcessoDt.setProcessoDt(processoDt);
		pendenciaProcessoDt.setId_Processo(processoDt.getId());
		pendenciaProcessoDt.setId_ProcessoPrioridade(processoDt.getId_ProcessoPrioridade());
		
		//Consulta Ementa 2º Grau
		PendenciaDt pendenciaDtEmenta = null;			
		if (preAnaliseEmenta != null){
			pendenciaDtEmenta = pendenciaNe.consultarId(preAnaliseEmenta.getId_Pendencia(), obFabricaConexao);
		} else {
			List listaDePendenciasEmenta = virtual ? pendenciaNe.consultarPendenciasVotoEmentaProcessoPeloIdAudiProc(idAudienciaProcessoSessaoSegundoGrau, Id_ServentiaCargo, String.valueOf(PendenciaTipoDt.CONCLUSO_EMENTA), obFabricaConexao) : pendenciaNe.consultarPendenciasVotoEmentaProcesso(processoDt.getId(), Id_ServentiaCargo, String.valueOf(PendenciaTipoDt.CONCLUSO_EMENTA), idAudienciaProcessoSessaoSegundoGrau, obFabricaConexao);
			if (listaDePendenciasEmenta != null && listaDePendenciasEmenta.size() > 0) pendenciaDtEmenta = (PendenciaDt) listaDePendenciasEmenta.get(0);
		}			
				
		//Se não existir uma pendência do tipo Ementa 2º Grau, iremos criá-la
		boolean fecharPendenciaEmenta = true;
		if (pendenciaDtEmenta == null) {
			fecharPendenciaEmenta = false;
			
			String idResponsavel = "";
			if (audienciaProcessoDt != null) {
				idResponsavel = audienciaProcessoDt.getId_ServentiaCargo();
			} else if (preAnaliseConclusaoDt.getPendenciaDt() != null && 
					   preAnaliseConclusaoDt.getPendenciaDt().getId_ServentiaCargo() != null && 
					   preAnaliseConclusaoDt.getPendenciaDt().getId_ServentiaCargo().trim().length() > 0) {
				idResponsavel = preAnaliseConclusaoDt.getPendenciaDt().getId_ServentiaCargo();
			} else {
				idResponsavel = Id_ServentiaCargo;
			}
			
			pendenciaDtEmenta = pendenciaNe.criarPendenciaDesembargador(pendenciaProcessoDt, String.valueOf(PendenciaTipoDt.CONCLUSO_EMENTA), usuarioDt, idAudienciaProcessoSessaoSegundoGrau, idResponsavel, obFabricaConexao);
			
			AudienciaProcessoPendenciaNe audienciaProcessoPendenciaNe = new AudienciaProcessoPendenciaNe();
			
			
			if (audienciaProcessoDt != null) {
				
				audienciaProcessoPendenciaNe.salvar(pendenciaDtEmenta.getId(), audienciaProcessoDt.getId(), obFabricaConexao);
				
				audienciaProcessoDt.setIpComputadorLog(preAnaliseConclusaoDt.getIpComputadorLog());
				audienciaProcessoDt.setId_UsuarioLog(preAnaliseConclusaoDt.getId_UsuarioLog());
				audienciaProcessoDt.setId_PendenciaEmentaRelator(pendenciaDtEmenta.getId());
				audienciaProcessoNe.vincularPendenciaVotoEmenta(audienciaProcessoDt, obFabricaConexao);	
			}				
		}	
		
		//atualiza o id_ementa no objeto principal
		preAnaliseConclusaoDt.setId_PendenciaEmentaGerada(pendenciaDtEmenta.getId());
		
		//Obtem a conclusão do tipo Ementa
		AnaliseConclusaoDt analiseConclusaoDtEmenta = this.getPreAnaliseConclusao(pendenciaDtEmenta.getId(), obFabricaConexao);
		if (analiseConclusaoDtEmenta == null){
			analiseConclusaoDtEmenta = new AnaliseConclusaoDt();
			analiseConclusaoDtEmenta.addPendenciasFechar(pendenciaDtEmenta);
			fecharPendenciaEmenta = false;
			analiseConclusaoDtEmenta.setId_TipoPendencia(String.valueOf(PendenciaTipoDt.CONCLUSO_EMENTA));
		}
		
		if (fecharPendenciaEmenta) {
			analiseConclusaoDtEmenta.addPendenciasFechar(pendenciaDtEmenta);
		}
		
		analiseConclusaoDtEmenta.setTextoEditor(preAnaliseConclusaoDt.getTextoEditorEmenta());
		analiseConclusaoDtEmenta.setNomeArquivo(preAnaliseConclusaoDt.getNomeArquivoEmenta());
		analiseConclusaoDtEmenta.setId_ArquivoTipo(preAnaliseConclusaoDt.getId_ArquivoTipoEmenta());
		analiseConclusaoDtEmenta.setArquivoTipo(preAnaliseConclusaoDt.getArquivoTipoEmenta());
		analiseConclusaoDtEmenta.setId_UsuarioLog(usuarioDt.getId());
		analiseConclusaoDtEmenta.setIpComputadorLog(usuarioDt.getIpComputadorLog());
		
		//Salva a conclusão do tipo Ementa
		this.salvarPreAnaliseConclusao(analiseConclusaoDtEmenta, usuarioDt, obFabricaConexao);
	
		if (analiseConclusaoDtEmenta.getListaPendenciasGeradas() != null && analiseConclusaoDtEmenta.getListaPendenciasGeradas().size() > 0) {
			PendenciaDt pendenciaGerada = (PendenciaDt)analiseConclusaoDtEmenta.getListaPendenciasGeradas().get(0);
			if (audienciaProcessoDt != null) {
				audienciaProcessoDt.setId_PendenciaEmentaRelator(pendenciaGerada.getId());
			}
		}
		
		if (audienciaProcessoDt != null) {
			audienciaProcessoNe.vincularPendenciaVotoEmenta(audienciaProcessoDt, obFabricaConexao);	
		}
	}
	
	public void salvarPreAnaliseVotoEmenta(AnaliseConclusaoDt preAnaliseConclusaoDt, UsuarioDt usuarioDt, String idAudienciaProcessoSessaoSegundoGrau) throws Exception {
		salvarPreAnaliseVotoEmenta(preAnaliseConclusaoDt, usuarioDt, idAudienciaProcessoSessaoSegundoGrau, false);
	}
	
	public void salvarPreAnaliseVotoEmenta(AnaliseConclusaoDt preAnaliseConclusaoDt, UsuarioDt usuarioDt, String idAudienciaProcessoSessaoSegundoGrau, boolean virtual) throws Exception {
	    FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();	
		
			salvarPreAnaliseVotoEmenta(preAnaliseConclusaoDt, usuarioDt, idAudienciaProcessoSessaoSegundoGrau, virtual, obFabricaConexao);

			obFabricaConexao.finalizarTransacao();
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	/**
	 * Monta o arquivo com o texto da pré-analise redigida pelo assistente.
	 * Nesse arquivo será concatenado o texto redigido pelo usuário com outras informações importantes que não podem ser persistidas no momento por
	 * se tratar de uma pré-análise. Essas informações serão separadas por <!--Projudi Projudi-->.
	 * 
	 * @param serventiaExpedirDt
	 * @param arquivoDt
	 * 
	 * @author mmgomes
	 */
	public void montaArquivoPreAnalisePendencias(ServentiaDt serventiaExpedirDt, ArquivoDt arquivoDt, ServentiaTipoDt serventiaTipoExpedirDt) throws Exception {

		//Impede a elaboração de pré análise com upload de arquivo pdf. O sistema não está preparado para este uso e corrompe o pdf
		//ao inserir nele as metainformações da tela. 
		if( arquivoDt != null && arquivoDt.isArquivoPDF()){
			throw new MensagemException("Não é possível utilizar o upload de arquivos ao montar uma pré-análise.");
		}
		
		// Monta conteúdo do arquivo da pré-análise, concatenando o texto digitado com os dados importantes da pré-analise que não serão persistidos
		if (serventiaExpedirDt == null || arquivoDt == null) return;
		
		String conteudoArquivo = "<!--Projudi Id_ServentiaExpedir:" + serventiaExpedirDt.getId();
		conteudoArquivo += ";ServentiaExpedir:" + serventiaExpedirDt.getServentia();		
		if (serventiaTipoExpedirDt != null)
		{
			conteudoArquivo += ";Id_ServentiaTipo:" + serventiaTipoExpedirDt.getId();
			conteudoArquivo += ";ServentiaTipo:" + serventiaTipoExpedirDt.getServentiaTipo();
		}		
		conteudoArquivo += " Projudi-->";
		String conteudoEditor = arquivoDt.getArquivo();

		if (conteudoEditor != null && conteudoEditor.length() > 0) {
			conteudoEditor = conteudoEditor.replaceAll("(<!--Projudi(.*?)Projudi-->)", "");
		}
		conteudoArquivo += conteudoEditor;
		arquivoDt.setArquivo(conteudoArquivo);
	}
	
	
	/**
	 * Salva a pré-análise da pendência elaboração de voto, salvando o arquivo com o texto da pré-análise. 
	 * 
	 * @param pendenciaDt, dt com os dados das pendências a gerar
	 * @param usuarioDt, usuário que está realizando a pré-análise
	 * @param audienciaMovimentacaoDt, dados da movimentação
	 * @author lsrodrigues
	 */
	public void salvarPreAnaliseElaboracaoVoto(PendenciaDt pendenciaDt, AudienciaMovimentacaoDt audienciaMovimentacaoDt, UsuarioDt usuarioDt) throws Exception {
	    FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();			
			PreAnaliseConclusaoDt preAnaliseConclusaoDt = new PreAnaliseConclusaoDt();
			
			List arquivos = this.consultarArquivosPendencia(pendenciaDt.getId());
			
			ArquivoDt arquivoDt = new ArquivoDt();
			ArquivoDt tempArq = new ArquivoDt();
			PendenciaArquivoDt preAnalise = null;
			
			int tempId = 0;
			
			if(arquivos != null) {
				
				for(int i = 0; i < arquivos.size(); i++){				
					tempArq = (ArquivoDt)arquivos.get(i);
					Integer id = Integer.valueOf(tempArq.getId());
					if(id > tempId && !tempArq.isArquivoConfiguracao()){
						arquivoDt = tempArq;
						tempId = id;
					}
				}
				
				preAnalise = this.consultarPendenciaArquivo(String.valueOf(tempId));
				preAnalise.setArquivoDt(arquivoDt);
				preAnaliseConclusaoDt.setArquivoPreAnalise(preAnalise);				

	
			}
			
			List listaPendenciasFechar = new ArrayList();
			listaPendenciasFechar.add(pendenciaDt);
			
			preAnaliseConclusaoDt.setArquivoTipo(audienciaMovimentacaoDt.getArquivoTipo());
			preAnaliseConclusaoDt.setId_ArquivoTipo(audienciaMovimentacaoDt.getId_ArquivoTipo());
			preAnaliseConclusaoDt.setId_Modelo(audienciaMovimentacaoDt.getId_Modelo());
			preAnaliseConclusaoDt.setModelo(audienciaMovimentacaoDt.getModelo());
			preAnaliseConclusaoDt.setTextoEditor(audienciaMovimentacaoDt.getTextoEditor());
			preAnaliseConclusaoDt.setNomeArquivo(audienciaMovimentacaoDt.getNomeArquivo());
			preAnaliseConclusaoDt.setListaPendenciasFechar(listaPendenciasFechar);
			preAnaliseConclusaoDt.setId_UsuarioLog(usuarioDt.getId());
			preAnaliseConclusaoDt.setIpComputadorLog(usuarioDt.getIpComputadorLog());
			preAnaliseConclusaoDt.setListaPendenciasGerar(audienciaMovimentacaoDt.getListaPendenciasGerar());
			

			if (preAnalise == null){ 
				inserirPreAnaliseConclusao(preAnaliseConclusaoDt, usuarioDt, obFabricaConexao);
			}
			else{				
				alterarPreAnaliseConclusao(preAnaliseConclusaoDt, preAnalise, usuarioDt, obFabricaConexao);
			}

			obFabricaConexao.finalizarTransacao();
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}

	
	
	
	/**
	 * Salva a pré-análise da pendência elaboração de voto, salvando o arquivo com o texto da pré-análise. 
	 * 
	 * @param id_pendencia id da pendência que se quer obter as configurações
	 * @author lsrodrigues
	 */
	
	
	public List getArquivoConfiguracaoElaboracaoVoto(String id_pendencia) throws Exception{
		
	    FabricaConexao obFabricaConexao = null;
	    PreAnaliseConclusaoDt preAnaliseConclusaoDt = new PreAnaliseConclusaoDt();
		try{
			
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			obFabricaConexao.iniciarTransacao();			
			
			PendenciaArquivoDt configuracaoPreAnalise = null;
			
			configuracaoPreAnalise = this.getArquivoConfiguracaoPreAnalise(id_pendencia, obFabricaConexao);

			//Se encontrou interpreta os dados
			if (configuracaoPreAnalise != null){

				preAnaliseConclusaoDt = lerConfiguracaoPreAnaliseConclusao(configuracaoPreAnalise.getArquivoDt().getArquivo());
				// Seta arquivos da pré-análise no objeto AnaliseConclusaoDt
				preAnaliseConclusaoDt.setArquivoConfiguracaoPreAnalise(configuracaoPreAnalise);
				
			} 
			
			if (preAnaliseConclusaoDt == null) preAnaliseConclusaoDt = new PreAnaliseConclusaoDt();


			obFabricaConexao.finalizarTransacao();
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		
		List pendenciasGerar = preAnaliseConclusaoDt.getListaPendenciasGerar();
		
		return pendenciasGerar;
		
	}
	
	
	
	
	
	
	
	/**
	 * Salva a pré-análise da pendência elaboração de voto, salvando o arquivo com o texto da pré-análise. 
	 * 
	 * @param id_pendencia id da pendência que se quer obter as configurações
	 * @author lsrodrigues
	 */
	
	
	public PendenciaArquivoDt consultarPendenciaArquivo(String id_arquivo) throws Exception{
		
		
	    FabricaConexao obFabricaConexao = null;

	    PendenciaArquivoDt pendenciaArquivoDt = new PendenciaArquivoDt();
		try{
			
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			obFabricaConexao.iniciarTransacao();			

			PendenciaArquivoPs obPersistencia = new PendenciaArquivoPs(obFabricaConexao.getConexao());
			pendenciaArquivoDt = obPersistencia.consultarPendenciaArquivo(id_arquivo);


			obFabricaConexao.finalizarTransacao();
		
		} finally{
			obFabricaConexao.fecharConexao();
		}


		return pendenciaArquivoDt;
		
	}
	
	
	
	/**
     * Gerar pdf de uma publicação
     * @param String
     *            stIdArquivo, id de um arquivo de uma publicação (pendencia do
     *            tipo publicação)
     * @return byte[] , retorna bytes contendo a publicação em pdf
     * @throws Exception
     */
    public byte[] gerarPdfPublicacao(String diretorioProjeto, String id_PendenciaArquivo, UsuarioDt usuarioDt, LogDt logDt) throws Exception {
    	byte[] byTemp = null;
        
        PendenciaArquivoDt pendenciaArquivoDt = this.consultarFinalizadaId(id_PendenciaArquivo);
        
        if(pendenciaArquivoDt == null) {
        	pendenciaArquivoDt = this.consultarId(id_PendenciaArquivo);
        }
        
        this.consultarFinalizadaId(id_PendenciaArquivo);
        PendenciaNe pendenciaNe = new PendenciaNe();
        ProcessoNe processone = new ProcessoNe();
        
        PendenciaDt pendenciaDt = pendenciaNe.consultarFinalizadaId(pendenciaArquivoDt.getId_Pendencia());
        
        if(pendenciaDt == null){
        	pendenciaDt = pendenciaNe.consultarId(pendenciaArquivoDt.getId_Pendencia());
        }
        
        pendenciaDt.setProcessoDt(processone.consultarId(pendenciaDt.getId_Processo()));		
		
        String stDados = "id_pend=" + pendenciaDt.getId() + "; id_proc=" + pendenciaDt.getId_Processo();
        
		//Grava o log da requisicao
		LogNe logNe = new LogNe();
		logNe.salvar(new LogDt("Arquivo", pendenciaArquivoDt.getId_Arquivo(), logDt.getId_Usuario(), logDt.getIpComputador(), String.valueOf(LogTipoDt.Download), "", stDados));        
		
        byTemp = new PendenciaNe().gerarPdfPublicacao(diretorioProjeto, pendenciaArquivoDt.getId_Arquivo(), usuarioDt, pendenciaDt.getProcessoDt());
        return byTemp;
    }
	
	public void excluir(PendenciaArquivoDt dados, FabricaConexao conexao) throws Exception {
		LogDt obLogDt;
		FabricaConexao obFabricaConexao = (conexao == null ? new FabricaConexao(FabricaConexao.PERSISTENCIA) : conexao);
		try {
			PendenciaArquivoPs obPersistencia = new PendenciaArquivoPs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("PendenciaArquivo", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId()); dados.limpar();

			obLog.salvar(obLogDt, obFabricaConexao);
		} finally {
			if (conexao == null) obFabricaConexao.fecharConexao();
		}		
	}

	public PendenciaArquivoDt consultarVotoDesembargadorPorIdAudienciaProcesso(String id_ServentiaCargo,
			String idAudienciaProcesso, FabricaConexao obFabricaConexao) throws Exception {

		PendenciaArquivoPs obPersistencia = new PendenciaArquivoPs(obFabricaConexao.getConexao());
		return obPersistencia.consultarVotoDesembargadorPorIdAudienciaProcesso(id_ServentiaCargo, idAudienciaProcesso);
	}

	public PendenciaArquivoDt consultarEmentaDesembargadorPorIdAudienciaProcesso(String id_ServentiaCargo,
			String idAudienciaProcesso, FabricaConexao obFabricaConexao) throws Exception {

		PendenciaArquivoPs obPersistencia = new PendenciaArquivoPs(obFabricaConexao.getConexao());
		return obPersistencia.consultarEmentaDesembargadorPorIdAudienciaProcesso(id_ServentiaCargo,
				idAudienciaProcesso);
	}

	public PendenciaArquivoDt consultarEmentaDesembargadorPorId(String id_ServentiaCargo, String idAudiProc)
			throws Exception {
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			return consultarEmentaDesembargadorPorId(id_ServentiaCargo, idAudiProc, obFabricaConexao);

		} finally {
			obFabricaConexao.fecharConexao();
		}
	}

	private PendenciaArquivoDt consultarEmentaDesembargadorPorId(String id_ServentiaCargo, String idAudiProc,
			FabricaConexao obFabricaConexao) throws Exception {

		PendenciaArquivoPs obPersistencia = new PendenciaArquivoPs(obFabricaConexao.getConexao());
		return obPersistencia.consultarEmentaDesembargadorPorIdAudienciaProcesso(id_ServentiaCargo, idAudiProc);
	}
	
	/**
	 * Salva o texto parcial da pré-análise de uma pendência, gerando o arquivo de configuração e
	 * salvando o arquivo com o texto da pré-análise. Método estruturado para
	 * tratar múltiplas pré-analises
	 * 
	 * @param preAnaliseConclusaoDt, dt com os dados da pré-análise
	 * @param usuarioDt, usuário que está realizando a pré-análise
	 * @author msapaula
	 */
	public PendenciaDt salvarTextoParcialPreAnaliseConclusao(AnaliseConclusaoDt preAnaliseConclusaoDt, UsuarioDt usuarioDt) throws Exception {
	    FabricaConexao obFabricaConexao = null;
	    PendenciaDt pendenciaDt = null;
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			
			pendenciaDt = salvarTextoParcialPreAnaliseConclusao(preAnaliseConclusaoDt, usuarioDt, obFabricaConexao);

			obFabricaConexao.finalizarTransacao();
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		
		return pendenciaDt;
	}
	
	public PendenciaDt salvarTextoParcialPreAnaliseConclusao(AnaliseConclusaoDt preAnaliseConclusaoDt, UsuarioDt usuarioDt, FabricaConexao obFabricaConexao) throws Exception {	    

		// Pega o arquivo da pré-análise simples ou múltipla
		PendenciaArquivoDt preAnalise = preAnaliseConclusaoDt.getArquivoPreAnalise();
		
		PendenciaDt pendenciaDt = null;

		if (preAnalise == null) pendenciaDt = inserirTextoParcialPreAnaliseConclusao(preAnaliseConclusaoDt, usuarioDt, obFabricaConexao);
		else pendenciaDt = alterarTextoParcialPreAnaliseConclusao(preAnaliseConclusaoDt, preAnalise, usuarioDt, obFabricaConexao);
		
		return pendenciaDt;
	}
	
	/**
	 * Método responsavel em inserir pré-analise simples ou múltipla para pendências do tipo Conclusão.
	 * 
	 * @param preAnaliseConclusaoDt, objeto com dados da pré-analise
	 * @param usuarioDt, usuario pré-analisador
	 * @author msapaula
	 * @throws Exception 
	 */
	private PendenciaDt inserirTextoParcialPreAnaliseConclusao(AnaliseConclusaoDt preAnaliseConclusaoDt, UsuarioDt usuarioDt, FabricaConexao conexao) throws Exception{

		PendenciaDt pendenciaDt = null;
		// Salva arquivo da pré-analise (configuração e texto redigido)
		ArquivoDt arquivoConfiguracao = salvaArquivoConfiguracaoPreAnalise(preAnaliseConclusaoDt, true, conexao);
		ArquivoDt arquivoPreAnalise = salvaArquivoPreAnaliseConclusao(preAnaliseConclusaoDt, true, conexao);
		PendenciaNe pendenciaNe = new PendenciaNe();

		List pendenciasFechar = preAnaliseConclusaoDt.getListaPendenciasFechar();
		if (pendenciasFechar!=null){
			// Para cada pendência a ser fechada
			for (int i = 0; i < pendenciasFechar.size(); i++) {
				pendenciaDt = (PendenciaDt) pendenciasFechar.get(i);
				
				//Verifica se já não foi inserida uma pré-análise para a conclusão. Foi necessário incluir essa verificação pois acontecia de assistentes
				//pré-analisarem o mesmo processo ao mesmo tempo, duplicando assim os arquivos de pré-análise.
				if (this.verificaPreAnaliseConclusao(pendenciaDt.getId(), conexao)){
					throw new MensagemException("Já foi inserida uma pré-análise para essa pendência. Efetue a consulta de Não analisadas novamente.");
				}
	
				// Vincula arquivos a pendência. Somente arquivo da pré-analise é do tipo resposta
				vincularPendenciaArquivo(pendenciaDt, arquivoConfiguracao, false, false, conexao);
				vincularPendenciaArquivo(pendenciaDt, arquivoPreAnalise, true, preAnaliseConclusaoDt.isPendenteAssinatura(), conexao);
	
				// Se usuário é assistente, adiciona ele como responsável pela  pendência
				//if (Funcoes.StringToInt(usuarioDt.getGrupoCodigo()) == GrupoDt.ASSISTENTES_JUIZES_VARA || Funcoes.StringToInt(usuarioDt.getGrupoCodigo()) == GrupoDt.ASSISTENTES_JUIZES_SEGUNDO_GRAU) {
				if (Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo()) == GrupoTipoDt.ASSESSOR_JUIZ_VARA_TURMA || 
					Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo()) == GrupoTipoDt.ASSESSOR_PRESIDENTE_SEGUNDO_GRAU ||
					Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo()) == GrupoTipoDt.ASSESSOR_DESEMBARGADOR ||
					Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo()) == GrupoTipoDt.JUIZ_LEIGO ) {
					
					if (pendenciaDt.getResponsaveis() == null || pendenciaDt.getResponsaveis().size() == 0) 
						pendenciaDt.setResponsaveis(pendenciaNe.consultarResponsaveis(pendenciaDt));
					
					if (!pendenciaDt.isResponsavelUsuarioServentia(usuarioDt.getId_UsuarioServentia())) {
						
						PendenciaResponsavelNe responsavelNe = new PendenciaResponsavelNe();
						LogDt logDt = new LogDt(preAnaliseConclusaoDt.getId_UsuarioLog(), preAnaliseConclusaoDt.getIpComputadorLog());
						PendenciaResponsavelDt responsavel = getResponsavelPreAnalise(pendenciaDt, usuarioDt.getId_UsuarioServentia(), "", logDt); 
						responsavelNe.salvar(responsavel, conexao);
						pendenciaDt.addResponsavel(responsavel);
					}
				}
				
				if (preAnaliseConclusaoDt.getListaPendenciasFechar() != null &&  preAnaliseConclusaoDt.getListaPendenciasFechar().size() > 0) {
					//ALTERAR CLASSIFICADOR DA CONCLUSÃO***********************************************************************
					pendenciaNe.alterarClassificadorPendencia(((PendenciaDt) preAnaliseConclusaoDt.getListaPendenciasFechar().get(0)).getId(), "", preAnaliseConclusaoDt.getId_Classificador(), new LogDt(usuarioDt.getId(), preAnaliseConclusaoDt.getIpComputadorLog()), conexao);
			    	//*********************************************************************************************************	
				}			
			}
		}
		
		return pendenciaDt;
	}
	
	/**
	 * Método responsável em alterar o texto parcial de uma pré-analise registrada para Conclusões, seja essa simples ou múltipla.
	 * 
	 * @param preAnaliseConclusaoDt, objeto com dados da pré-análise
	 * @param preAnalise, PendenciaArquivoDt com os dados da pré-analise anterior
	 * @param usuarioDt, usuário que está alterando
	 * @author msapaula
	 * @throws Exception 
	 */
	private PendenciaDt alterarTextoParcialPreAnaliseConclusao(AnaliseConclusaoDt preAnaliseConclusaoDt, PendenciaArquivoDt preAnalise, UsuarioDt usuarioDt, FabricaConexao conexao) throws Exception{
		PendenciaDt pendenciaFilha = null;
		
		if (geraHistoricoSimplesConclusao(preAnaliseConclusaoDt, usuarioDt)){	
			PendenciaNe pendenciaNe = new PendenciaNe();
			PendenciaResponsavelNe responsavelNe = new PendenciaResponsavelNe();
			
			// Cria novos arquivos
			ArquivoDt arquivoConfiguracao = salvaArquivoConfiguracaoPreAnalise(preAnaliseConclusaoDt, true, conexao);
			ArquivoDt arquivoPreAnalise = salvaArquivoPreAnaliseConclusao(preAnaliseConclusaoDt, true, conexao);

			List pendenciasFechar = preAnaliseConclusaoDt.getListaPendenciasFechar();
			if (pendenciasFechar != null){
				AudienciaProcessoPendenciaNe audienciaProcessoPendenciaNe = new AudienciaProcessoPendenciaNe();
				String idAudiProc = null;
				// Para cada pendência a ser fechada
				for (int i = 0; i < pendenciasFechar.size(); i++) {
					PendenciaDt pendenciaDt = (PendenciaDt) pendenciasFechar.get(i);

					pendenciaFilha = criarPendenciaFilha(pendenciaDt, usuarioDt, new LogDt(preAnaliseConclusaoDt.getId_UsuarioLog(), preAnaliseConclusaoDt.getIpComputadorLog()), conexao);
					
					if(StringUtils.isEmpty(idAudiProc)) {
						idAudiProc = audienciaProcessoPendenciaNe.consultarPorIdPend(pendenciaDt.getId());
					}
					
					if(idAudiProc != null)
						audienciaProcessoPendenciaNe.salvar(pendenciaFilha.getId(), idAudiProc, conexao);
					
					// Vincula arquivos de pré-analise a pendência filha
					vincularPendenciaArquivo(pendenciaFilha, arquivoConfiguracao, false, false, conexao);
					vincularPendenciaArquivo(pendenciaFilha, arquivoPreAnalise, true, preAnaliseConclusaoDt.isPendenteAssinatura(), conexao);

					// Consulta os arquivos problema da pendência pai que devem
					// ser vinculados também a pendencia filha
					List arquivos = PendenciaArquivoNe.extrairArquivos(this.consultarArquivosAssinadosProblema(pendenciaDt, true, conexao));
					this.vincularArquivos(pendenciaFilha, arquivos, false, conexao);

					//Atualiza os responsáveis
					List responsaveisAtuais = pendenciaNe.consultarResponsaveis(pendenciaDt);
					if (responsaveisAtuais != null) {
						for(int j = 0; j < responsaveisAtuais.size() ; j++) {
							PendenciaResponsavelDt responsavelAtual = (PendenciaResponsavelDt)responsaveisAtuais.get(j);
							
							if (responsavelAtual != null && 
								responsavelAtual.getId_ServentiaCargo() != null && 
								responsavelAtual.getId_ServentiaCargo().trim().length() > 0 &&
								!pendenciaFilha.isResponsavelServentiaCargo(responsavelAtual.getId_ServentiaCargo())) {
								
								LogDt logDt = new LogDt(preAnaliseConclusaoDt.getId_UsuarioLog(), preAnaliseConclusaoDt.getIpComputadorLog());
								PendenciaResponsavelDt responsavel = getResponsavelPreAnalise(pendenciaFilha, "", responsavelAtual.getId_ServentiaCargo(), logDt); 
								responsavelNe.salvar(responsavel, conexao);
								pendenciaFilha.addResponsavel(responsavel);
							}
						}	
					}
					
					if (Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo()) == GrupoTipoDt.ASSESSOR_JUIZ_VARA_TURMA || 
						Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo()) == GrupoTipoDt.ASSESSOR_PRESIDENTE_SEGUNDO_GRAU ||
						Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo()) == GrupoTipoDt.ASSESSOR_DESEMBARGADOR) {
						
						if (!pendenciaFilha.isResponsavelUsuarioServentia(usuarioDt.getId_UsuarioServentia())) {								
							LogDt logDt = new LogDt(preAnaliseConclusaoDt.getId_UsuarioLog(), preAnaliseConclusaoDt.getIpComputadorLog());
							PendenciaResponsavelDt responsavel = getResponsavelPreAnalise(pendenciaFilha, usuarioDt.getId_UsuarioServentia(), "", logDt); 
							responsavelNe.salvar(responsavel, conexao);
							pendenciaFilha.addResponsavel(responsavel);
						}
					}
					
					if (pendenciaFilha != null)
					{
						//ALTERAR CLASSIFICADOR DA CONCLUSÃO***********************************************************************
				    	new PendenciaNe().alterarClassificadorPendencia(pendenciaFilha.getId(), "", preAnaliseConclusaoDt.getId_Classificador(), new LogDt(usuarioDt.getId(), preAnaliseConclusaoDt.getIpComputadorLog()), conexao);
				    	//*********************************************************************************************************
				    	preAnaliseConclusaoDt.addPendenciasGeradas(pendenciaFilha);
					}
				}
			}
			
		} else {
			boolean alterarArquivos = false;
			
			// Se juiz está alterando a pré-análise
			//if (Funcoes.StringToInt(usuarioDt.getGrupoCodigo()) == GrupoDt.JUIZES_VARA || Funcoes.StringToInt(usuarioDt.getGrupoCodigo()) == GrupoDt.JUIZES_TURMA_RECURSAL) {
			if (Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo()) == GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU 
					|| Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo()) == GrupoTipoDt.JUIZ_TURMA
					|| Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo()) == GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU
					|| Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo()) == GrupoTipoDt.PRESIDENTE_SEGUNDO_GRAU) {
				// Se a pré analise tinha sido feita por assistente, será criada uma
				// pendência filha, fechando a atual.
				if (!preAnalise.getAssistenteResponsavel().equals("")) {

					// Cria novos arquivos
					ArquivoDt arquivoConfiguracao = salvaArquivoConfiguracaoPreAnalise(preAnaliseConclusaoDt, true, conexao);
					ArquivoDt arquivoPreAnalise = salvaArquivoPreAnaliseConclusao(preAnaliseConclusaoDt, true, conexao);

					List pendenciasFechar = preAnaliseConclusaoDt.getListaPendenciasFechar();
					if (pendenciasFechar != null && pendenciasFechar.size() > 0){
						// Para cada pendência a ser fechada
						for (int i = 0; i < pendenciasFechar.size(); i++) {
							PendenciaDt pendenciaDt = (PendenciaDt) pendenciasFechar.get(i);
		
							pendenciaFilha = criarPendenciaFilha(pendenciaDt, usuarioDt, new LogDt(preAnaliseConclusaoDt.getId_UsuarioLog(), preAnaliseConclusaoDt.getIpComputadorLog()), conexao);
		
							// Vincula arquivos de pré-analise a pendência filha
							vincularPendenciaArquivo(pendenciaFilha, arquivoConfiguracao, false, false, conexao);
							vincularPendenciaArquivo(pendenciaFilha, arquivoPreAnalise, true, preAnaliseConclusaoDt.isPendenteAssinatura(), conexao);
		
							// Consulta os arquivos problema da pendência pai que devem
							// ser vinculados também a pendencia filha
							List arquivos = PendenciaArquivoNe.extrairArquivos(this.consultarArquivosAssinadosProblema(pendenciaDt, true, conexao));
							this.vincularArquivos(pendenciaFilha, arquivos, false, conexao);
							
							preAnaliseConclusaoDt.addPendenciasGeradas(pendenciaFilha);
						}
					} else alterarArquivos = true;

				} else alterarArquivos = true;

			//} else if (Funcoes.StringToInt(usuarioDt.getGrupoCodigo()) == GrupoDt.ASSISTENTES_JUIZES_VARA || Funcoes.StringToInt(usuarioDt.getGrupoCodigo()) == GrupoDt.ASSISTENTES_JUIZES_SEGUNDO_GRAU) alterarArquivos = true;
			} else if (Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo()) == GrupoTipoDt.ASSESSOR_JUIZ_VARA_TURMA 
					|| Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo()) == GrupoTipoDt.ASSISTENTE_GABINETE 
					|| Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo()) == GrupoTipoDt.ASSISTENTE_GABINETE_FLUXO 
					|| Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo()) == GrupoTipoDt.ASSESSOR_PRESIDENTE_SEGUNDO_GRAU
					|| Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo()) == GrupoTipoDt.ASSESSOR_DESEMBARGADOR) 
				alterarArquivos = true;
				
			if (alterarArquivos) {
				// Significa que juiz ou assistente está alterando uma pré-analise simples feita por ele mesmo
				if (preAnaliseConclusaoDt.getArquivoConfiguracaoPreAnalise() != null) {
					salvaArquivoConfiguracaoPreAnalise(preAnaliseConclusaoDt, false, conexao);
				} else {
					//Alteração para compatibilidade com pré-analises antigas onde não havia o arquivo de configuração e podia refazer
					//Salva o arquivo de configuração
					ArquivoDt arquivoConfiguracao = salvaArquivoConfiguracaoPreAnalise(preAnaliseConclusaoDt, true, conexao);
					if (preAnaliseConclusaoDt.getListaPendenciasFechar() != null && preAnaliseConclusaoDt.getListaPendenciasFechar().size() > 0)
					{
						// Vincula arquivo à pendência
						vincularPendenciaArquivo((PendenciaDt) preAnaliseConclusaoDt.getListaPendenciasFechar().get(0), arquivoConfiguracao, false, false, conexao);	
					}				
				}
				salvaArquivoPreAnaliseConclusao(preAnaliseConclusaoDt, false, conexao);
				
				// Atualiza status da pré-analise
				if (preAnaliseConclusaoDt.isPendenteAssinatura()) this.atualizeStatusPreAnalisesConclusaoSimplesParaPendentesAssinatura(preAnaliseConclusaoDt.getArquivoPreAnalise().getId_Pendencia(), conexao);
				else this.atualizeStatusPreAnalisesConclusaoSimplesParaNaoPendentesAssinatura(preAnaliseConclusaoDt.getArquivoPreAnalise().getId_Pendencia(), conexao);
			}
			
			if (preAnaliseConclusaoDt.getListaPendenciasFechar() != null && preAnaliseConclusaoDt.getListaPendenciasFechar().size() > 0)
			{
				//ALTERAR CLASSIFICADOR DA CONCLUSÃO***********************************************************************
		    	new PendenciaNe().alterarClassificadorPendencia(((PendenciaDt) preAnaliseConclusaoDt.getListaPendenciasFechar().get(0)).getId(), "", preAnaliseConclusaoDt.getId_Classificador(), new LogDt(usuarioDt.getId(), preAnaliseConclusaoDt.getIpComputadorLog()), conexao);
		    	//*********************************************************************************************************	
			}
		}
		
		return pendenciaFilha;
	}
	
	public List consultaIdPendenciaPorIdArquivo(String idArquivo) throws Exception {
		FabricaConexao fabricaConexao = null;
		List listaIdPendencia = new ArrayList();
		try {
			fabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			fabricaConexao.iniciarTransacao();
			PendenciaArquivoPs pendArqPs = new PendenciaArquivoPs(fabricaConexao.getConexao());
			listaIdPendencia = pendArqPs.consultarIdPendenciaPorIdArquivo(idArquivo);
			fabricaConexao.finalizarTransacao();
		} catch (Exception e) {
			fabricaConexao.cancelarTransacao();
			throw e;
		} finally {
			fabricaConexao.fecharConexao();
		}
		return listaIdPendencia;
	}
	
	/**
	 * Consultar vinculos com arquivos que sao resposta e não assinados
	 * 
	 * @author asrocha
	 * @param pendenciaDt vo de pendencia
	 * @param comConteudo  com o conteudo do arquivo
	 * @param conexao conexao
	 * @return lista de vinculos
	 * @throws Exception
	 */
	public List consultarRespostaNaoAssinados(PendenciaDt pendenciaDt, boolean comConteudo, FabricaConexao conexao) throws Exception {
		List arquivos = null;
		FabricaConexao obFabricaConexao = null;
		try{
			if (conexao == null) {
				obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			} else {
				obFabricaConexao = conexao;
			}
			PendenciaArquivoPs obPersistencia = new PendenciaArquivoPs(obFabricaConexao.getConexao());
			arquivos = obPersistencia.consultarRespostaNaoAssinados(pendenciaDt, comConteudo);
		} finally{
			if (conexao == null) obFabricaConexao.fecharConexao();
		}
		return arquivos;
	}
	
	/**
	 * Desvincular arquivo pré-analise que sao resposta e não assinados
	 * 
	 * @author lsbernardes
	 * @param lista com objetos pendenciaDt vo de pendencia
	 * @param LogDt vo de logDt
	 * @param fabConexao conexao
	 * @throws Exception
	 */
	public void desvincularArquivosPreAnalise(List pendencias, LogDt logDt, FabricaConexao fabConexao) throws Exception {
	    FabricaConexao obFabricaConexao = null;
		try{
			// Se a fabrica nao foi passada
			if (fabConexao == null) {
				obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
				obFabricaConexao.iniciarTransacao();
			} else {
				obFabricaConexao = fabConexao;
			}
			
			for (Iterator iterator = pendencias.iterator(); iterator.hasNext();) {
				PendenciaDt pendenciaDt = (PendenciaDt) iterator.next();
				// Modifica a conexao local
				PendenciaArquivoPs obPersistencia = new PendenciaArquivoPs(obFabricaConexao.getConexao());
				// exclui a ligacao entre arquivo e pendencia arquivo
				obPersistencia.desvincularArquivosPreAnalise(pendenciaDt.getId());
				
				LogNe logNe = new LogNe();
				logNe.salvar(new LogDt("Pendencia", pendenciaDt.getId(), logDt.getId_UsuarioLog(), logDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir), "", "Desvincular arquivos de pré-anlise da pendência"),obFabricaConexao);
			}

			// Se a fabrica foi criada no metodo
			if (fabConexao == null) {
				obFabricaConexao.finalizarTransacao();
			}

		} catch(Exception e) {
			// Caso ocorra algum erro a transacao e cancelada, se nao foi
			// passado a fabrica de conexao
			if (fabConexao == null) {
				obFabricaConexao.cancelarTransacao();
			}

			throw e;
		} finally{
			// Se a fabrica nao foi passada
			if (fabConexao == null) {
				obFabricaConexao.fecharConexao();
			}
		}
	}
	
	/**
	 * Consulta os arquivos de uma determinada pendencia
	 * 
	 * @param pendenciaDt
	 *            pojo da pendencia
	 * @param comArquivos
	 *            adicionar na pendencia os arquivos
	 * @param somenteAssinados
	 *            somente arquivos assinados
	 * @param conexao
	 *            conexao para continuar uma transacao ou null para iniciar uma
	 *            nova
	 * @throws Exception
	 */
	public String consultarArquivosPendenciaFinalizadaJSON(PendenciaDt pendenciaDt, UsuarioNe usuarioNe, boolean comArquivos, boolean somenteAssinados, boolean hash, String posicao) throws Exception {
    	String stTemp = "";
        FabricaConexao obFabricaConexao = null;
     
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            PendenciaArquivoPs obPersistencia = new PendenciaArquivoPs(obFabricaConexao.getConexao());
            
            stTemp = obPersistencia.consultarArquivosPendenciaFinalizadaJSON(pendenciaDt, usuarioNe, comArquivos, somenteAssinados, false, hash, posicao);

        } finally {
            obFabricaConexao.fecharConexao();
        }

        return stTemp;
    }
	
	/**
	 * Consulta os arquivos de uma determinada pendencia
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 02/08/2008 10:52
	 * @param pendenciaDt
	 *            pojo da pendencia
	 * @param comArquivos
	 *            adicionar na pendencia os arquivos
	 * @param somenteAssinados
	 *            somente arquivos assinados
	 * @param somenteProblema
	 *            somente
	 * @param conexao
	 *            conexao para continuar uma transacao ou null para iniciar uma
	 *            nova
	 * @throws Exception
	 */
	public String consultarArquivosPendenciaJSON(PendenciaDt pendenciaDt, UsuarioNe usuarioNe, boolean comArquivos, boolean somenteAssinados, boolean somenteProblema, boolean hash, String posicao) throws Exception {
    	String stTemp = "";
        FabricaConexao obFabricaConexao = null;
     
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            PendenciaArquivoPs obPersistencia = new PendenciaArquivoPs(obFabricaConexao.getConexao());
            
            stTemp = obPersistencia.consultarArquivosPendenciaJSON(pendenciaDt, usuarioNe, comArquivos, somenteAssinados, somenteProblema, hash, posicao);

        } finally {
            obFabricaConexao.fecharConexao();
        }

        return stTemp;
    }
	
	/**
	 *  Verifica se o registro na tabela PEND_ARQ já foi movido para a a tabela PEND_FINAL_ARQ
	 * 
	 * @param id_PendenciaArquivo
	 *            identificador do registro
	 *            
	 * @return boolean
	 *  
	 * @throws Exception
	 */
	public boolean isPendenciaArquivoMovido(String id_PendenciaArquivo ) throws Exception {
		PendenciaArquivoDt dtRetorno=null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			PendenciaArquivoPs obPersistencia = new PendenciaArquivoPs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarId(id_PendenciaArquivo); 		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno == null || dtRetorno.getId() == null || dtRetorno.getId().length()==0;
	}
	
	
	public void excluirPJD(PendenciaArquivoDt dados, FabricaConexao conexao) throws Exception {
		PendenciaArquivoPs obPersistencia = new PendenciaArquivoPs(conexao.getConexao());
		LogDt obLogDt = new LogDt("PendenciaArquivo", dados.getId(), dados.getId_UsuarioLog(),
				dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir), dados.getPropriedades(), "");
		obPersistencia.excluir(dados.getId());
		dados.limpar();
		obLog.salvar(obLogDt, conexao);
	}
}
