package br.gov.go.tj.projudi.ne;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import com.amazonaws.util.json.JSONArray;
import com.amazonaws.util.json.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import br.gov.go.tj.projudi.dt.ArquivoDt;
import br.gov.go.tj.projudi.dt.ArquivoFisicoMetadadosDt;
import br.gov.go.tj.projudi.dt.ArquivoTipoDt;
import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.GrupoTipoDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.MovimentacaoArquivoDt;
import br.gov.go.tj.projudi.dt.MovimentacaoDt;
import br.gov.go.tj.projudi.dt.MovimentacaoProcessoDt;
import br.gov.go.tj.projudi.dt.MovimentacaoTipoDt;
import br.gov.go.tj.projudi.dt.NumeroProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ServentiaCargoDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.ps.MovimentacaoArquivoPs;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;
import br.gov.go.tj.utils.ValidacaoUtil;
import br.gov.go.tj.utils.Certificado.Signer;

public class MovimentacaoArquivoNe extends MovimentacaoArquivoNeGen {
	/**
     * 
     */
    private static final long serialVersionUID = 4322091844287086651L;

	public String Verificar(MovimentacaoArquivoDt dados) {

		String stRetorno = "";
		return stRetorno;

	}

	public void salvar(MovimentacaoArquivoDt dados, FabricaConexao obFabricaConexao) throws Exception {
		LogDt obLogDt;
		try{
			MovimentacaoArquivoPs obPersistencia = new MovimentacaoArquivoPs(obFabricaConexao.getConexao());
			if (dados.getId().equalsIgnoreCase("")) {				
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("MovimentacaoArquivo", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir), "", dados.getPropriedades());
			} else {				
				obPersistencia.alterar(dados);
				obLogDt = new LogDt("MovimentacaoArquivo", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar), obDados.getPropriedades(), dados.getPropriedades());
			}
			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
		} catch(Exception e) {
			dados.setId("");
			throw e;
		}
	}

	/**
	 * Método que permite baixar um arquivo de movimentação, podendo ser o arquivo ou o recibo.
	 * Efetua a verificação se o usuário em questão pode realmente visualizar esse arquivo.
	 * 
	 * @param id_MovimentacaoArquivo, identificação do MovimentacaoArquivo
	 * @param processoDt, objeto com dados do processo em sessão
	 * @param usuarioDt, usuario que está tentando visualizar arquivo
	 * @param recibo, define se arquivo é recibo
	 * @param response
	 * @param logDt, objeto com dados do log
	 * @throws Exception
	 * retorna verdadeiro se o arquivo foi baixado
	 */
	public boolean baixarArquivoMovimentacao(String id_MovimentacaoArquivo, ProcessoDt processoDt, UsuarioDt usuarioDt, boolean recibo, HttpServletResponse response, LogDt logDt, boolean ehConsultaPublica) throws Exception{
		ArquivoNe arquivoNe = new ArquivoNe();
		MovimentacaoArquivoDt movimentacaoArquivoDt = this.consultarIdCompleto(id_MovimentacaoArquivo);
		
		if (!podeBaixarArquivo(usuarioDt, processoDt, movimentacaoArquivoDt)){
			return false;
		}
										
		if (recibo){
			MovimentacaoDt moviTemp = new MovimentacaoNe().consultarId(movimentacaoArquivoDt.getId_Movimentacao());
			arquivoNe.baixarRecibo(moviTemp, movimentacaoArquivoDt.getId_Arquivo() , response, logDt, ehConsultaPublica);
		}else{
			arquivoNe.baixarArquivo(movimentacaoArquivoDt.getId_Arquivo(), response, logDt, ehConsultaPublica);
		}
		
		return true;	
	}
	
	/**
	 * Método que permite baixar um arquivo de movimentação, podendo ser o arquivo ou o recibo.
	 * Efetua a verificação se o usuário em questão pode realmente visualizar esse arquivo.
	 * Esse método é utilizado pelo WebService Servico04 pois não tem o processo em memória, devendo consultar os dados
	 * 
	 * @param id_MovimentacaoArquivo, identificação do MovimentacaoArquivo
	 * @param usuarioDt, usuario que está tentando visualizar arquivo
	 * @param recibo, define se arquivo é recibo
	 * @param response
	 * @param logDt, objeto com dados do log
	 * @throws Exception
	 * retorna verdadeiro se o arquivo foi baixado
	 */
	public boolean baixarArquivoMovimentacaoWebService(String id_MovimentacaoArquivo, UsuarioDt usuarioDt, boolean recibo, HttpServletResponse response, LogDt logDt, ProcessoDt processoDt) throws Exception{
		ArquivoNe arquivoNe = new ArquivoNe();
		
		//Nesse caso como não foi passado o processo referente, deve consultar dados completos
		MovimentacaoArquivoDt movimentacaoArquivoDt = this.consultarDadosMovimentacaoArquivo(id_MovimentacaoArquivo);

		if (!podeBaixarArquivo(usuarioDt, processoDt, movimentacaoArquivoDt))
			return false;
			
		if (recibo){
			MovimentacaoDt moviTemp = new MovimentacaoNe().consultarId(movimentacaoArquivoDt.getId_Movimentacao());
			arquivoNe.baixarRecibo(moviTemp, movimentacaoArquivoDt.getId_Arquivo(), response, logDt, false);
		}else{
			if (Funcoes.StringToInt(movimentacaoArquivoDt.getCodigoTemp()) == MovimentacaoArquivoDt.FILE_SYSTEM) {
				arquivoNe.baixarArquivoFileSystem(movimentacaoArquivoDt.getId_Arquivo(), response, logDt, false);
			} else if (Funcoes.StringToInt(movimentacaoArquivoDt.getCodigoTemp()) == MovimentacaoArquivoDt.OBJECT_STORAGE) {
				arquivoNe.baixarArquivoObjectStorageWebServiceDigitalizacao(movimentacaoArquivoDt.getId_Arquivo(), response, logDt, false);				
			} else {
				arquivoNe.baixarArquivo(movimentacaoArquivoDt.getId_Arquivo(), response, logDt, false);	
			}			
		}
		
		return true;
	}

	/**
	 * Consulta de dados MovimentacaoArquivo completa
	 * 
	 * @param id_MovimentacaoArquivo identificação do MovimentacaoArquivo
	 */
	public MovimentacaoArquivoDt consultarIdCompleto(String id_MovimentacaoArquivo) throws Exception {
		MovimentacaoArquivoDt dtRetorno = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			MovimentacaoArquivoPs obPersistencia = new MovimentacaoArquivoPs(obFabricaConexao.getConexao());
			dtRetorno = obPersistencia.consultarIdCompleto(id_MovimentacaoArquivo);
			obDados.copiar(dtRetorno);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}
	
	public String consultarIdProcesso(String idMovimentacaoArquivo) throws Exception {
		String idProcesso = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			MovimentacaoArquivoPs obPersistencia = new MovimentacaoArquivoPs(obFabricaConexao.getConexao());
			idProcesso = obPersistencia.consultarIdProcesso(idMovimentacaoArquivo);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return idProcesso;
	}
	
	/**
	 * Consulta Id do aruqivo
	 * @param id_MovimentacaoArquivo identificação do MovimentacaoArquivo
	 */
	public String consultarIdArquivo(String id_MovimentacaoArquivo) throws Exception {
		String stRetorno = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			MovimentacaoArquivoPs obPersistencia = new MovimentacaoArquivoPs(obFabricaConexao.getConexao());
			stRetorno = obPersistencia.consultarIdArquivo(id_MovimentacaoArquivo);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stRetorno;
	}
	
	/**
	 * Consulta detalhada de dados de MovimentacaoArquivo, retornando dados do processo e movimentação
	 * 
	 * @param id_MovimentacaoArquivo, identificação de MovimentacaoArquivo
	 * 
	 * @author msapaula
	 * @throws Exception 
	 */
	private MovimentacaoArquivoDt consultarDadosMovimentacaoArquivo(String id_MovimentacaoArquivo) throws Exception{
		MovimentacaoArquivoDt dtRetorno = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			MovimentacaoArquivoPs obPersistencia = new MovimentacaoArquivoPs(obFabricaConexao.getConexao());
			dtRetorno = obPersistencia.consultarDadosMovimentacaoArquivo(id_MovimentacaoArquivo);
			obDados.copiar(dtRetorno);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}

	/**
	 * Método que verifica se determinado usuário pode fazer download de um arquivo. São feitas as
	 * seguintes verificações para permitir a visualização do arquivo: 
	 *  - Se arquivo é do tipo Decisão, Despacho ou Sentença
	 * 	- Se usuário é advogado no processo 
	 *  - Se usuário é assistente de um advogado no processo
	 * 	- Se usuário é uma parte ativa no processo 
	 * 	- Se usuário é responsável no processo 
	 * 	- Se usuário é da mesma serventia do processo
	 * 	- Se acesso é proveniente de outra serventia e deve liberar acesso aos arquivos
	 *  - Se usuário é público e processo é segredo de justiça, não libera acesso. Só libera acesso se o usuário fizer a consulta pelo código do processo
	 * 
	 * @param processoDt, objeto de processo
	 * @param usuarioDt, objeto de usuário
	 * @param acessoOutraServentia, define se acesso é proveniente de outra serventia com acesso liberado
	 * @author msapaula
	 */
	public boolean podeBaixarArquivo(UsuarioDt usuarioDt, ProcessoDt processoDt, MovimentacaoArquivoDt moviArquivo) throws Exception {
		boolean boRetorno = false;

		int grupoCodigo = Funcoes.StringToInt(usuarioDt.getGrupoCodigo());
		
		switch (moviArquivo.getId_MovimentacaoArquivoAcessoToInt()) {
			case  MovimentacaoArquivoDt.ACESSO_PUBLICO :
				boRetorno = true;
				break;
			case  MovimentacaoArquivoDt.ACESSO_NORMAL :
				boRetorno = new ProcessoNe().podeAcessarProcesso(usuarioDt, processoDt, null);
				break;
			case  MovimentacaoArquivoDt.ACESSO_SOMENTE_ADVS_DELEGACIA_MP_CARTORIO_MAGISTRADO :
				if (usuarioDt.isAdvogado()) {					
					boRetorno = new ProcessoNe().verificarAdvogadoParteProcesso(processoDt.getId(), usuarioDt.getId_UsuarioServentia(), null);
				}else {
					boRetorno = new ProcessoNe().podeAcessarProcesso(usuarioDt, processoDt, null);
				}
				break;
			case  MovimentacaoArquivoDt.ACESSO_SOMENTE_DELEGACIA_MP_CARTORIO_MAGISTRADO :
				switch (grupoCodigo) {
				case GrupoDt.ANALISTAS_JUDICIARIOS_TURMA_RECURSAL:
				case GrupoDt.ANALISTA_CALCULO_EXECUCAO_PENAL:
				case GrupoDt.ANALISTA_CALCULO_PROCESSO_FISICO:
				case GrupoDt.ANALISTA_JUDICIARIO_SEGUNDO_GRAU_CIVEL:
				case GrupoDt.ANALISTA_PRE_PROCESSUAL:
				case GrupoDt.ANALISTAS_EXECUCAO_PENAL:
				case GrupoDt.ANALISTA_JUDICIARIO_PRIMEIRO_GRAU_CIVEL:
				case GrupoDt.ANALISTA_JUDICIARIO_PRIMEIRO_GRAU_CRIMINAL:
				case GrupoDt.ANALISTA_JUDICIARIO_SEGUNDO_GRAU_CRIMINAL:
				case GrupoDt.ESTAGIARIO_PRIMEIRO_GRAU:
				case GrupoDt.ESTAGIARIO_SEGUNDO_GRAU:	
				case GrupoDt.TECNICO_EXECUCAO_PENAL:
				case GrupoDt.TECNICO_JUDICIARIO_SEGUNDO_GRAU_CIVEL:
				case GrupoDt.TECNICOS_JUDICIARIOS_TURMA_RECURSAL:
				case GrupoDt.TECNICO_JUDICIARIO_PRIMEIRO_GRAU_CIVEL:
				case GrupoDt.TECNICO_JUDICIARIO_PRIMEIRO_GRAU_CRIMINAL:
				case GrupoDt.TECNICO_JUDICIARIO_SEGUNDO_GRAU_CRIMINAL:
				case GrupoDt.CONCILIADORES_VARA:
				case GrupoDt.AUTORIDADES_POLICIAIS:
				case GrupoDt.MINISTERIO_PUBLICO :
				case GrupoDt.MP_TCE:
				case GrupoDt.DISTRIBUIDOR_CAMARA:
				case GrupoDt.DESEMBARGADOR:
				case GrupoDt.JUIZ_EXECUCAO_PENAL:
				case GrupoDt.JUIZES_TURMA_RECURSAL:
				case GrupoDt.JUIZES_VARA:
				case GrupoDt.MAGISTRADO_UPJ_PRIMEIRO_GRAU: 
				case GrupoDt.ASSESSOR:
				case GrupoDt.ASSESSOR_DESEMBARGADOR:
				case GrupoDt.ASSESSOR_JUIZES_SEGUNDO_GRAU:
				case GrupoDt.ASSESSOR_JUIZES_VARA:		
				case GrupoDt.JUIZ_LEIGO:
				case GrupoDt.ASSISTENTE_GABINETE:
				case GrupoDt.ASSISTENTE_GABINETE_FLUXO:
				case GrupoDt.ASSESSOR_PRESIDENTE_SEGUNDO_GRAU:
					boRetorno = new ProcessoNe().podeAcessarProcesso(usuarioDt, processoDt, null);
					break;
				default:
					 boRetorno = false;
					 break;
				}				
			break;
			case  MovimentacaoArquivoDt.ACESSO_SOMENTE_MP_CARTORIO_MAGISTRADO :
				switch (grupoCodigo) {
					case GrupoDt.ANALISTAS_JUDICIARIOS_TURMA_RECURSAL:
					case GrupoDt.ANALISTA_CALCULO_EXECUCAO_PENAL:
					case GrupoDt.ANALISTA_CALCULO_PROCESSO_FISICO:
					case GrupoDt.ANALISTA_JUDICIARIO_SEGUNDO_GRAU_CIVEL:
					case GrupoDt.ANALISTA_PRE_PROCESSUAL:
					case GrupoDt.ANALISTAS_EXECUCAO_PENAL:
					case GrupoDt.ANALISTA_JUDICIARIO_PRIMEIRO_GRAU_CIVEL:
					case GrupoDt.ANALISTA_JUDICIARIO_PRIMEIRO_GRAU_CRIMINAL:
					case GrupoDt.ANALISTA_JUDICIARIO_SEGUNDO_GRAU_CRIMINAL:
					case GrupoDt.TECNICO_EXECUCAO_PENAL:
					case GrupoDt.TECNICO_JUDICIARIO_SEGUNDO_GRAU_CIVEL:
					case GrupoDt.TECNICOS_JUDICIARIOS_TURMA_RECURSAL:
					case GrupoDt.TECNICO_JUDICIARIO_PRIMEIRO_GRAU_CIVEL:
					case GrupoDt.TECNICO_JUDICIARIO_PRIMEIRO_GRAU_CRIMINAL:
					case GrupoDt.TECNICO_JUDICIARIO_SEGUNDO_GRAU_CRIMINAL:
					case GrupoDt.CONCILIADORES_VARA:
					case GrupoDt.MINISTERIO_PUBLICO:
					case GrupoDt.MP_TCE:
					case GrupoDt.DISTRIBUIDOR_CAMARA:
					case GrupoDt.DESEMBARGADOR:
					case GrupoDt.JUIZ_EXECUCAO_PENAL:
					case GrupoDt.JUIZES_TURMA_RECURSAL:
					case GrupoDt.JUIZES_VARA:
					case GrupoDt.MAGISTRADO_UPJ_PRIMEIRO_GRAU: 
					case GrupoDt.ASSESSOR:
					case GrupoDt.ASSESSOR_DESEMBARGADOR:
					case GrupoDt.ASSESSOR_JUIZES_SEGUNDO_GRAU:
					case GrupoDt.ASSESSOR_JUIZES_VARA:		
					case GrupoDt.JUIZ_LEIGO:					
					case GrupoDt.ASSISTENTE_GABINETE:
					case GrupoDt.ASSISTENTE_GABINETE_FLUXO:
					case GrupoDt.ASSESSOR_PRESIDENTE_SEGUNDO_GRAU:
						boRetorno = new ProcessoNe().podeAcessarProcesso(usuarioDt, processoDt, null);
						break;
					default:
						 boRetorno = false;
						 break;
					}				
				break;
			case  MovimentacaoArquivoDt.ACESSO_SOMENTE_CARTORIO_MAGISTRADO :
				switch (grupoCodigo) {
					case GrupoDt.ANALISTAS_JUDICIARIOS_TURMA_RECURSAL:
					case GrupoDt.ANALISTA_CALCULO_EXECUCAO_PENAL:
					case GrupoDt.ANALISTA_CALCULO_PROCESSO_FISICO:
					case GrupoDt.ANALISTA_JUDICIARIO_SEGUNDO_GRAU_CIVEL:
					case GrupoDt.ANALISTA_PRE_PROCESSUAL:
					case GrupoDt.ANALISTAS_EXECUCAO_PENAL:
					case GrupoDt.ANALISTA_JUDICIARIO_PRIMEIRO_GRAU_CIVEL:
					case GrupoDt.ANALISTA_JUDICIARIO_PRIMEIRO_GRAU_CRIMINAL:
					case GrupoDt.ANALISTA_JUDICIARIO_SEGUNDO_GRAU_CRIMINAL:
					case GrupoDt.TECNICO_EXECUCAO_PENAL:
					case GrupoDt.TECNICO_JUDICIARIO_SEGUNDO_GRAU_CIVEL:
					case GrupoDt.TECNICOS_JUDICIARIOS_TURMA_RECURSAL:
					case GrupoDt.TECNICO_JUDICIARIO_PRIMEIRO_GRAU_CIVEL:
					case GrupoDt.TECNICO_JUDICIARIO_PRIMEIRO_GRAU_CRIMINAL:
					case GrupoDt.TECNICO_JUDICIARIO_SEGUNDO_GRAU_CRIMINAL:						
					case GrupoDt.CONCILIADORES_VARA:
					case GrupoDt.DISTRIBUIDOR_CAMARA:
					case GrupoDt.DESEMBARGADOR:
					case GrupoDt.JUIZ_EXECUCAO_PENAL:
					case GrupoDt.JUIZES_TURMA_RECURSAL:
					case GrupoDt.JUIZES_VARA:
					case GrupoDt.MAGISTRADO_UPJ_PRIMEIRO_GRAU: 
					case GrupoDt.ASSESSOR:
					case GrupoDt.ASSESSOR_DESEMBARGADOR:
					case GrupoDt.ASSESSOR_JUIZES_SEGUNDO_GRAU:
					case GrupoDt.ASSESSOR_JUIZES_VARA:
					case GrupoDt.JUIZ_LEIGO:
					case GrupoDt.ASSESSOR_PRESIDENTE_SEGUNDO_GRAU:		
					case GrupoDt.ASSISTENTE_GABINETE:
					case GrupoDt.ASSISTENTE_GABINETE_FLUXO:						
						boRetorno = new ProcessoNe().podeAcessarProcesso(usuarioDt, processoDt, null);
						break;
					default:
						 boRetorno = false;
						 break;
					}				
				break;
			case  MovimentacaoArquivoDt.ACESSO_SOMENTE_MAGISTRADO :
				switch (grupoCodigo) {
					case GrupoDt.DESEMBARGADOR:
					case GrupoDt.JUIZ_EXECUCAO_PENAL:
					case GrupoDt.JUIZES_TURMA_RECURSAL:
					case GrupoDt.JUIZES_VARA:
					case GrupoDt.MAGISTRADO_UPJ_PRIMEIRO_GRAU: 
					case GrupoDt.ASSESSOR:
					case GrupoDt.ASSESSOR_DESEMBARGADOR:
					case GrupoDt.ASSESSOR_JUIZES_SEGUNDO_GRAU:
					case GrupoDt.ASSESSOR_JUIZES_VARA:
					case GrupoDt.JUIZ_LEIGO:
					case GrupoDt.ASSESSOR_PRESIDENTE_SEGUNDO_GRAU:		
					case GrupoDt.ASSISTENTE_GABINETE:
					case GrupoDt.ASSISTENTE_GABINETE_FLUXO:						
						boRetorno = new ProcessoNe().podeAcessarProcesso(usuarioDt, processoDt, null);
						break;
					default:
						boRetorno = false;
						break;
				}
				break;						
			}

		return boRetorno;
	}
	
	/**
	 * Método que faz validação de acesso aos arquivos do processo para o usuário Distribuidor de Gabinete.
	 * @param usuarioDt - usuário logado (distribuidor de gabinete)
	 * @param processoDt - processo que o usuário está tentando acessar
	 * @return true ou false para indicar se o acesso será ou não permitido
	 * @throws Exception
	 */
	public boolean validarAcessoArquivoDistribuidorGabinete(UsuarioDt usuarioDt, ProcessoDt processoDt) throws Exception {
		UsuarioNe usuarioNe = new UsuarioNe();
		ProcessoResponsavelNe responsavelNe = new ProcessoResponsavelNe();
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		
		try{
			String idServentiaCargoDesembargador = usuarioNe.consultarIdServentiaCargoDesembargadorServentia(usuarioDt.getId_Serventia());
			if(!idServentiaCargoDesembargador.equalsIgnoreCase("") && responsavelNe.isResponsavelProcesso(idServentiaCargoDesembargador, processoDt.getId()))
				return true;			
			
			ServentiaCargoDt relator = responsavelNe.consultarRelator2Grau(processoDt.getId(), obFabricaConexao);			
			if (relator != null && relator.getId_Serventia().equalsIgnoreCase(usuarioDt.getId_Serventia()))
				return true;
			
			ServentiaCargoDt revisor = responsavelNe.consultarRevisor2Grau(processoDt.getId(), obFabricaConexao);			
			if (revisor != null && revisor.getId_Serventia().equalsIgnoreCase(usuarioDt.getId_Serventia()))
				return true;
			
			ServentiaCargoDt vogal = responsavelNe.consultarVogal2Grau(processoDt.getId(), obFabricaConexao);			
			if (vogal != null && vogal.getId_Serventia().equalsIgnoreCase(usuarioDt.getId_Serventia()))
				return true;
			
		} finally{
			obFabricaConexao.fecharConexao();
		}
		
		return false;
	}

	/**
	 * Consultar arquivos de uma determinada movimentação setando hash 
	 * 
	 * @param id_Movimentacao, identificação da movimentação
	 * @param usuarioNe, usuário logado para permitir geração de Hash
	 * 
	 * @author jrcorrea
	 * @since 28/11/2013
	 */
	public String consultarArquivosMovimentacaoHashJSON(String id_Movimentacao, UsuarioNe usuarioNe) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			MovimentacaoArquivoPs obPersistencia = new MovimentacaoArquivoPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarArquivosMovimentacaoJSON(id_Movimentacao, usuarioNe);						
			
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}
	
	/**
	 * Consultar arquivos de uma determinada movimentação
	 * 
	 * @param id_Movimentacao, identificação da movimentação
     * @param id_Processo, identificação do processo
	 * 
	 * @author msapaula
	 */
	public List consultarArquivosMovimentacao(String id_Movimentacao, String id_Processo) throws Exception {
		List tempList = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			MovimentacaoArquivoPs obPersistencia = new MovimentacaoArquivoPs(obFabricaConexao.getConexao());
			tempList = obPersistencia.consultarArquivosMovimentacao(id_Movimentacao, id_Processo);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;
	}
	
	public List consultarArquivosMovimentacao(String id_Movimentacao, String id_Processo, FabricaConexao obFabricaConexao) throws Exception {
		MovimentacaoArquivoPs obPersistencia = new MovimentacaoArquivoPs(obFabricaConexao.getConexao());
		return obPersistencia.consultarArquivosMovimentacao(id_Movimentacao, id_Processo);
	}
	
	/**
	 * Consultar arquivos de uma determinada movimentação
	 * 
	 * @param id_Movimentacao, identificação da movimentação
	 * 
	 * @author lsbernardes
	 */
	public List consultarArquivosMovimentacao(String id_Movimentacao) throws Exception {
		List tempList = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			MovimentacaoArquivoPs obPersistencia = new MovimentacaoArquivoPs(obFabricaConexao.getConexao());
			tempList = obPersistencia.consultarArquivosMovimentacao(id_Movimentacao);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;
	}

	/**
	 * Mapea todas as movimentaçoes e seus arquivos e conteudo
	 * 
	 * @author lsbernardes/ jrcorrea
	 * @param MovimentacaoDt movimentacaoDt, objeto da movimentação
	 */
	public List consultarConteudoArquivosMovimentacoes(String id_movimentacao, int inAcessoUsuario) throws Exception {
		List tempList = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			MovimentacaoArquivoPs obPersistencia = new MovimentacaoArquivoPs(obFabricaConexao.getConexao());
			tempList = obPersistencia.consultarConteudoArquivosMovimentacao(id_movimentacao,  inAcessoUsuario);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;
	}

	/**
	 * Mapea todas as movimentaçoes e seus arquivos sem o conteudo
	 * 
	 * @author jrcorrea 17/07/2017
	 * @param MovimentacaoDt movimentacaoDt, objeto da movimentação
	 */
	
	public Map consultarArquivosMovimentacoes(String id_processo, int inAcessoUsuario) throws Exception {
		Map tempMap = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			MovimentacaoArquivoPs obPersistencia = new MovimentacaoArquivoPs(obFabricaConexao.getConexao());
			tempMap = obPersistencia.consultarArquivosMovimentacoes(id_processo,  inAcessoUsuario);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return tempMap;
	}
	
	/**
	 * Consultar arquivos de uma determinada movimentação setando hash
	 * 
	 * @param id_Movimentacao, identificação da movimentação
	 * @param id_Processo, identificação do processo
	 * @param usuarioNe, usuário logado para permitir geração de Hash
	 * 
	 * @author msapaula
	 * @throws Exception 
	 */
	public List consultarArquivosMovimentacaoComHash(String id_Movimentacao, String id_Processo, UsuarioNe usuarioNe) throws Exception{
		List tempList = this.consultarArquivosMovimentacao(id_Movimentacao, id_Processo);

		if (tempList == null)
			throw new MensagemException("Erro ao tentar acessar o arquivo, consulte o Processo Novamente.");
		
		for (int i = 0; i < tempList.size(); i++) {
			MovimentacaoArquivoDt movimentacaoArquivoDt = (MovimentacaoArquivoDt) tempList.get(i);
			movimentacaoArquivoDt.setHash(usuarioNe.getCodigoHash(movimentacaoArquivoDt.getId() + id_Processo));
		}
			

		return tempList;
	}
	
	/**
	 * Consultar arquivos de uma determinada movimentação setando hash
	 * 
	 * @param id_Movimentacao, identificação da movimentação
	 * @param usuarioNe, usuário logado para permitir geração de Hash
	 * 
	 * @author lsbernardes
	 * @throws Exception 
	 */
	public List consultarArquivosMovimentacaoWebServiceComHash(String id_Movimentacao, UsuarioNe usuarioNe) throws Exception{
		List tempList = this.consultarArquivosMovimentacao(id_Movimentacao);

		if(tempList != null) {
			for (int i = 0; i < tempList.size(); i++) {
				MovimentacaoArquivoDt movimentacaoArquivoDt = (MovimentacaoArquivoDt) tempList.get(i);
				movimentacaoArquivoDt.setHash(usuarioNe.getCodigoHash(movimentacaoArquivoDt.getId() ));
			}
		}

		return tempList;
	}

	/**
	 * Método que salva vários arquivos vinculados a uma movimentação. Como a movimentação vinculada
	 * já é conhecida, já gera o recibo e grava o arquivo no banco.
	 * 
	 * @param movimentacaoDt: dt com os dados da movimentação
	 * @param arquivos: lista de arquivos a serem inseridos
	 * @param processoDt: dt do processo
	 * @param logDt: objeto log
	 * @param obFabricaConexao: conexão
	 * @author msapaula
	 * @throws Exception 
	 */
	public void inserirArquivosSemRecibo(String id_Movimentacao, String numeroProcesso, List arquivos, LogDt logDt, FabricaConexao obFabricaConexao) throws Exception{
		
		ArquivoNe arquivoNe = new ArquivoNe();
		// Salva arquivos no banco de dados

		for (int i = 0; i < arquivos.size(); i++) {
			ArquivoDt arquivoDt = (ArquivoDt) arquivos.get(i);
			arquivoDt.setId_UsuarioLog(logDt.getId_Usuario());
			arquivoDt.setIpComputadorLog(logDt.getIpComputador());
			arquivoDt.setRecibo("false");
			// Gera Recibo Assinado Projudi
			//Signer.gerarRecibo(id_Movimentacao, numeroProcesso, arquivoDt);
			// Salva arquivo
			arquivoNe.inserir(arquivoDt, logDt, obFabricaConexao);
		}
		MovimentacaoArquivoNe movimentacaoArquivoNe = new MovimentacaoArquivoNe();
		movimentacaoArquivoNe.vinculaMovimentacaoArquivoControleAcesso(arquivos, id_Movimentacao, null, logDt, obFabricaConexao);
			
	}

	/**
	 * Método responsável em salvar uma lista de arquivos de movimentação em um processo. a partir de 29/04/2021 não será mais salvo o recibo
	 * 
	 * @param arquivos: lista de arquivos a serem salvos
	 * @param logDt: objeto de log
	 * @param obFabricaConexao: conexão
	 * @author msapaula
	 * @since 27/01/2009 17:28
	 */
	public void inserirArquivos(List arquivos, LogDt logDt, FabricaConexao obFabricaConexao) throws Exception {
		ArquivoNe arquivoNe = new ArquivoNe();
		
		for (int i = 0; i < arquivos.size(); i++) {
			ArquivoDt arquivoDt = (ArquivoDt) arquivos.get(i);
			// Se o arquivo já tem id não é necessário salvar novamente
			if (!arquivoDt.isTemId()) {
				//arquivoDt.setNaoSalvarConteudo();
				//arquivoDt.setConteudoSemRecibo(arquivoDt.conteudoBytes());
				//byte[] teste = null;
				//arquivoDt.setArquivo(teste);
				arquivoDt.setId_UsuarioLog(logDt.getId_Usuario());
				arquivoDt.setIpComputadorLog(logDt.getIpComputador());
				// Salva arquivo sem o conteúdo so para pegar o id para regar o recibo
				arquivoNe.inserir(arquivoDt, logDt, obFabricaConexao);
			}
			//não faz mais sentido testar primeiro
//			else if (arquivoDt.conteudoBytes() == null) {
//				arquivoDt.setConteudoSemRecibo(arquivoNe.consultarConteudoArquivo(arquivoDt, obFabricaConexao));
//			}
		}
		
	}

//	/**
//	 * Gera recibo assinado projudi para cada um dos arquivos que são de movimentação, e ao final
//	 * atualiza o conteudo desses arquivos no banco.
//	 * 
//	 * @param arquivos, lista de arquivos
//	 * @param movimentacoes, lista de movimentações vinculadas ao processo
//	 * 
//	 * @author msapaula
//	 */
//	public void gerarReciboArquivoMovimentacao(List arquivos, List movimentacoes, FabricaConexao fabConexao) throws Exception {
//		ArquivoNe arquivoNe = new ArquivoNe();
//		
//		if (arquivos != null) {
//			for (int i = 0; i < arquivos.size(); i++) {
//				ArquivoDt arquivoDt = (ArquivoDt) arquivos.get(i);
//				//não estiver assinado não vai gerar recibo, situação de devolução de videos não assinados 
//				//da carta precatória
//				if (arquivoDt.isAssinado()) {
//					//quando o conteudo do arquivo estiver vazio, tem que carrega-lo
//					if (!arquivoDt.isConteudo() && arquivoDt.isTemId()){
//						arquivoDt.setConteudoSemRecibo(arquivoNe.consultarConteudoArquivo(arquivoDt, fabConexao));						
//					}
//	
//					if (arquivoDt.getRecibo().equalsIgnoreCase("true")) {
//						// ALTERA RECIBO EXISTENTE
//						Signer.alterarRecibo(movimentacoes, arquivoDt);
//					}else {
//						// Gera Recibo Assinado Projudi
//						Signer.gerarRecibo(movimentacoes, arquivoDt);
//					}
//					// Atualiza dados no banco de dados
//					arquivoNe.atualizaConteudoRecibo(arquivoDt, fabConexao);
//				}
//			}
//		}
//
//	}

//	/**
//	 * Método que recebe uma lista de arquivos e uma movimentação e salva o vínculo entre esses.
//	 * 
//	 * @param listaArquivos: lista de Arquivos a serem vinculados a uma movimentação
//	 * @param movimentacaoDt: objeto com dados da movimentação
//	 * @param logDt: objeto log
//	 * @param obFabricaConexao: objeto fábrica
//	 * 
//	 * @author msapaula
//	 */
//	public void vinculaMovimentacao Arquivo(List listaArquivos, String id_Movimentacao, LogDt logDt, FabricaConexao obFabricaConexao) throws Exception {
//		ArquivoTipoNe arquivoTipo = new ArquivoTipoNe();
//		for (int i = 0; i < listaArquivos.size(); i++) {
//			ArquivoDt arquivoDt = (ArquivoDt) listaArquivos.get(i);
//			MovimentacaoArquivoDt movimentacaoArquivoDt = new MovimentacaoArquivoDt();
//			movimentacaoArquivoDt.setId_Movimentacao(id_Movimentacao);			
//			movimentacaoArquivoDt.setId_Arquivo(arquivoDt.getId());
//			if (arquivoTipo.isPublico(arquivoDt.getId_ArquivoTipo())){
//				movimentacaoArquivoDt.setId_MovimentacaoArquivoAcesso(String.valueOf(MovimentacaoArquivoDt.ACESSO_PUBLICO));
//			}else{
//				movimentacaoArquivoDt.setId_MovimentacaoArquivoAcesso(String.valueOf(MovimentacaoArquivoDt.ACESSO_NORMAL));
//			}
//			movimentacaoArquivoDt.setId_UsuarioLog(logDt.getId_Usuario());
//			movimentacaoArquivoDt.setIpComputadorLog(logDt.getIpComputador());
//			this.salvar(movimentacaoArquivoDt, obFabricaConexao);
//		}
//	}
	
	/**
	 * Método que recebe uma lista de arquivos e uma movimentação e salva o vínculo entre esses.
	 * 
	 * @param listaArquivos: lista de Arquivos a serem vinculados a uma movimentação
	 * @param movimentacaoDt: objeto com dados da movimentação
	 * @param id_MovimentacaoArquivoAcesso: Nível de acesso do arquivo
	 * @param logDt: objeto log
	 * @param obFabricaConexao: objeto fábrica
	 * 
	 * @author msapaula
	 */
	public void vinculaMovimentacaoArquivoControleAcesso(List listaArquivos, String id_Movimentacao, String id_MovimentacaoArquivoAcesso, LogDt logDt, FabricaConexao obFabricaConexao) throws Exception {
		ArquivoTipoNe arquivoTipo = new ArquivoTipoNe();
		for (int i = 0;listaArquivos!=null && i < listaArquivos.size(); i++) {
			ArquivoDt arquivoDt = (ArquivoDt) listaArquivos.get(i);
			MovimentacaoArquivoDt movimentacaoArquivoDt = new MovimentacaoArquivoDt();
			//vejo se o arquivo é público o tipo do arquivo
			if (id_MovimentacaoArquivoAcesso==null){
				if (arquivoTipo.isPublico(arquivoDt.getId_ArquivoTipo())){
					movimentacaoArquivoDt.setId_MovimentacaoArquivoAcesso(String.valueOf(MovimentacaoArquivoDt.ACESSO_PUBLICO));
				}else{
					movimentacaoArquivoDt.setId_MovimentacaoArquivoAcesso(String.valueOf(MovimentacaoArquivoDt.ACESSO_NORMAL));
				}
			} else {
				movimentacaoArquivoDt.setId_MovimentacaoArquivoAcesso(id_MovimentacaoArquivoAcesso);
			}
			movimentacaoArquivoDt.setId_Movimentacao(id_Movimentacao);
			movimentacaoArquivoDt.setId_Arquivo(arquivoDt.getId());			
			movimentacaoArquivoDt.setId_UsuarioLog(logDt.getId_Usuario());
			movimentacaoArquivoDt.setIpComputadorLog(logDt.getIpComputador());
			this.salvar(movimentacaoArquivoDt, obFabricaConexao);
		}
	}

	/**
	 * Invalida um arquivo em uma movimentação. Muda CodigoTemp para 2 - Restrição download
	 * 
	 * @param movimentacaoArquivodt, dt com dados do arquivo na movimentação
	 * @param conexao, conexão ativa se houver
	 * @author msapaula
	 */
	public void invalidarArquivoMovimentacao(MovimentacaoArquivoDt movimentacaoArquivodt, int constanteAcesso, FabricaConexao conexao) throws Exception {
		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;
		try{
			if (conexao == null) {
				obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
				obFabricaConexao.iniciarTransacao();
			} else obFabricaConexao = conexao;
			MovimentacaoArquivoPs obPersistencia = new MovimentacaoArquivoPs(obFabricaConexao.getConexao());

			String valorAtual = "[Id_MovimentacaoArquivo:" + movimentacaoArquivodt.getId() + ";ID_MOVI_ARQ_ACESSO:" + movimentacaoArquivodt.getId_MovimentacaoArquivoAcessoToInt() + "]";
			String valorNovo = "[Id_MovimentacaoArquivo:" + movimentacaoArquivodt.getId() + ";ID_MOVI_ARQ_ACESSO:" + constanteAcesso + "]";

			obLogDt = new LogDt("MovimentacaoArquivo",movimentacaoArquivodt.getId(), movimentacaoArquivodt.getId_UsuarioLog(), movimentacaoArquivodt.getIpComputadorLog(), String.valueOf(LogTipoDt.AlteracaoVisibilidadeAquivo), valorAtual, valorNovo);
			obPersistencia.alterarStatusMovimentacaoArquivo(movimentacaoArquivodt.getId(), constanteAcesso);

			obLog.salvar(obLogDt, obFabricaConexao);
			if (conexao == null) obFabricaConexao.finalizarTransacao();
		} catch(Exception e) {
			if (conexao == null) obFabricaConexao.cancelarTransacao();
			throw e;
		} finally{
			if (conexao == null) obFabricaConexao.fecharConexao();
		}
	}

	/**
	 * Valida um arquivo em uma movimentação. Muda CodigoTemp para 0 - Normal
	 * 
	 * @param movimentacaoArquivodt, dt com dados do arquivo na movimentação
	 * @param conexao, conexão ativa se houver
	 * @author msapaula
	 */
	public void validarArquivoMovimentacao(MovimentacaoArquivoDt movimentacaoArquivodt, FabricaConexao obFabricaConexao) throws Exception {
		LogDt obLogDt;
			
		MovimentacaoArquivoPs obPersistencia = new MovimentacaoArquivoPs(obFabricaConexao.getConexao());

		String valorAtual = "[Id_MovimentacaoArquivo:" + movimentacaoArquivodt.getId() + ";ID_MOVI_ARQ_ACESSO:" + movimentacaoArquivodt.getId_MovimentacaoArquivoAcessoToInt() + "]";
		String valorNovo = "[Id_MovimentacaoArquivo:" + movimentacaoArquivodt.getId() + ";ID_MOVI_ARQ_ACESSO:" + MovimentacaoArquivoDt.ACESSO_NORMAL + "]";

		obLogDt = new LogDt("MovimentacaoArquivo", movimentacaoArquivodt.getId(), movimentacaoArquivodt.getId_UsuarioLog(), movimentacaoArquivodt.getIpComputadorLog(), String.valueOf(LogTipoDt.AlteracaoVisibilidadeAquivo), valorAtual, valorNovo);
		obPersistencia.alterarStatusMovimentacaoArquivo(movimentacaoArquivodt.getId(), MovimentacaoArquivoDt.ACESSO_NORMAL);

		obLog.salvar(obLogDt, obFabricaConexao);
			
	}

	/**
	 * Valida um arquivo em uma movimentação. Muda CodigoTemp para 0 - Normal
	 * 
	 * @param movimentacaoArquivodt, dt com dados do arquivo na movimentação
	 * @param conexao, conexão ativa se houver
	 * @author jrcorrea
	 */
	public void validarArquivoMovimentacao(MovimentacaoArquivoDt movimentacaoArquivodt) throws Exception {
		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;
		try{
			
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			obFabricaConexao.iniciarTransacao();
			
			MovimentacaoArquivoPs obPersistencia = new MovimentacaoArquivoPs(obFabricaConexao.getConexao());

			String valorAtual = "[Id_MovimentacaoArquivo:" + movimentacaoArquivodt.getId() + ";ID_MOVI_ARQ_ACESSO:" +  movimentacaoArquivodt.getId_MovimentacaoArquivoAcessoToInt() + "]";
			String valorNovo = "[Id_MovimentacaoArquivo:" + movimentacaoArquivodt.getId() + ";ID_MOVI_ARQ_ACESSO:" + MovimentacaoArquivoDt.ACESSO_NORMAL + "]";

			obLogDt = new LogDt("MovimentacaoArquivo", movimentacaoArquivodt.getId(), movimentacaoArquivodt.getId_UsuarioLog(), movimentacaoArquivodt.getIpComputadorLog(), String.valueOf(LogTipoDt.AlteracaoVisibilidadeAquivo), valorAtual, valorNovo);
			obPersistencia.alterarStatusMovimentacaoArquivo(movimentacaoArquivodt.getId(), MovimentacaoArquivoDt.ACESSO_NORMAL);

			obLog.salvar(obLogDt, obFabricaConexao);
			 obFabricaConexao.finalizarTransacao();
		
		} finally{
			 obFabricaConexao.fecharConexao();
		}
	}
	/**
	 * Invalida os vários arquivos de uma movimentação. Método será utilizado quando uma
	 * movimentação for invalidada, e todos seus arquivos também devem ser invalidados
	 * 
	 * @param movimentacaoDt, dt com dados da movimentação
	 * @param conexao, conexão ativa
	 * @author msapaula
	 */
	public void invalidarArquivosMovimentacao(MovimentacaoDt movimentacaoDt, int constanteAcesso, FabricaConexao conexao) throws Exception {
		// Consulta os arquivos de uma movimentação
		List tempList = this.consultarArquivosMovimentacao(movimentacaoDt.getId(), movimentacaoDt.getId_Processo());

		if (tempList != null) {
			// Invalida cada um
			for (int i = 0; i < tempList.size(); i++) {
				MovimentacaoArquivoDt movimentacaoArquivoDt = (MovimentacaoArquivoDt) tempList.get(i);
				movimentacaoArquivoDt.setId_UsuarioLog(movimentacaoDt.getId_UsuarioLog());
				movimentacaoArquivoDt.setIpComputadorLog(movimentacaoDt.getIpComputadorLog());
				this.invalidarArquivoMovimentacao(movimentacaoArquivoDt, constanteAcesso, conexao);
			}
		}
	}

	/**
	 * Valida os vários arquivos de uma movimentação. Método será utilizado quando uma movimentação
	 * for validada, e todos seus arquivos também devem ser validados
	 * 
	 * @param movimentacaoDt, dt com dados da movimentação
	 * @param conexao, conexão ativa
	 * @author msapaula
	 */
	public void validarArquivosMovimentacao(MovimentacaoDt movimentacaoDt, FabricaConexao conexao) throws Exception {
		// Consulta os arquivos de uma movimentação
		List tempList = this.consultarArquivosMovimentacao(movimentacaoDt.getId(), movimentacaoDt.getId_Processo());

		if (tempList != null) {
			// Vaida cada um
			for (int i = 0; i < tempList.size(); i++) {
				MovimentacaoArquivoDt movimentacaoArquivoDt = (MovimentacaoArquivoDt) tempList.get(i);
				movimentacaoArquivoDt.setId_UsuarioLog(movimentacaoDt.getId_UsuarioLog());
				movimentacaoArquivoDt.setIpComputadorLog(movimentacaoDt.getIpComputadorLog());
				this.validarArquivoMovimentacao(movimentacaoArquivoDt, conexao);
			}
		}
	}

	public String consultarDescricaoJSON(String descricao, String posicao) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			MovimentacaoArquivoPs obPersistencia = new MovimentacaoArquivoPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarDescricaoJSON(descricao, posicao);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}
	
	/**
	 * Verifia se um usuário pode alterar a visibilidade de um arquivo
	 * 
	 * @param MovimentacaoArquivoDt, dt com dados do aquivo e da movimentação
	 * @param UsuarioDt, dt com dados do usuário
	 * @author lsbernardes
	 */
	public boolean VerificarAlteracaoVisibilidadeAquivo(MovimentacaoArquivoDt dados, UsuarioDt usuarioDt) {

		if ( dados.getId_MovimentacaoArquivoAcessoToInt() == MovimentacaoArquivoDt.ACESSO_SOMENTE_MAGISTRADO &&
			 (usuarioDt.getGrupoTipoCodigoToInt() != GrupoTipoDt.JUIZ_AUXILIAR && usuarioDt.getGrupoTipoCodigoToInt() != GrupoTipoDt.JUIZ_TURMA 
				&& usuarioDt.getGrupoTipoCodigoToInt() != GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU && usuarioDt.getGrupoTipoCodigoToInt() != GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU)
			){
			return false;
		}
		
		return true;
	}
	
	/**
	 * Consulta os dados do arquivo, processo e movimentação para o id de arquivo específico
	 * @param id_MovimentacaoArquivo identificação do MovimentacaoArquivo
	 */
	public MovimentacaoArquivoDt consultarEntidadePorIdArquivo(String id_MovimentacaoArquivo, FabricaConexao conexao) throws Exception {
		MovimentacaoArquivoDt dtRetorno = null;
		FabricaConexao obFabricaConexao = null;
		try {
			obFabricaConexao = ValidacaoUtil.isNulo(conexao) ? new FabricaConexao(FabricaConexao.CONSULTA) : conexao;
			MovimentacaoArquivoPs obPersistencia = new MovimentacaoArquivoPs(obFabricaConexao.getConexao());
			dtRetorno = obPersistencia.consultarPorIdArquivo(id_MovimentacaoArquivo);		
		} finally{
			if (ValidacaoUtil.isNulo(conexao)) obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}
	
	/**
	 * Consulta os dados do arquivo para o id específico
	 * @param id_MovimentacaoArquivo identificação do MovimentacaoArquivo
	 */
	public MovimentacaoArquivoDt consultarArquivoPublicadoForaProcesso (String id_Arquivo, FabricaConexao conexao) throws Exception {
		MovimentacaoArquivoDt dtRetorno = null;
		FabricaConexao obFabricaConexao = null;
		try {
			obFabricaConexao = ValidacaoUtil.isNulo(conexao) ? new FabricaConexao(FabricaConexao.CONSULTA) : conexao;
			MovimentacaoArquivoPs obPersistencia = new MovimentacaoArquivoPs(obFabricaConexao.getConexao());
			dtRetorno = obPersistencia.consultarArquivoPublicadoForaProcesso (id_Arquivo);		
		} finally{
			if (ValidacaoUtil.isNulo(conexao)) obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}
	
	public void atualizeListaDeArquivoDoObjectStorageDigitalizacao(ProcessoDt processoDt, int ordemInicial, String id_movimentacao, UsuarioDt usuarioDt, MovimentacaoProcessoDt movimentacaoProcessoDt) throws Exception {
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try {		
			obFabricaConexao.iniciarTransacao();
			MovimentacaoNe movimentacaoNe = new MovimentacaoNe();
			MovimentacaoDt movimentacaoDt = null;

			if (id_movimentacao != null && id_movimentacao.trim().length() > 0) {
				movimentacaoDt = movimentacaoNe.consultarId(id_movimentacao);
			} else {
				movimentacaoNe.salvarMovimentacaoGenerica(movimentacaoProcessoDt, usuarioDt, obFabricaConexao);
				
				movimentacaoDt = movimentacaoNe.gerarMovimentacao(processoDt.getId(),
																  MovimentacaoTipoDt.JUNTADA_DE_DOCUMENTO, usuarioDt.getId_UsuarioServentia(),
																  MovimentacaoTipoDt.COMPLEMENTO_JUNTADA_DOCUMENTO_PROCESSO_FISICO, 
																  new LogDt(processoDt.getId_UsuarioLog(), processoDt.getIpComputadorLog()), 
																  obFabricaConexao);
			}
			
			movimentacaoDt.setId_UsuarioLog(processoDt.getId_UsuarioLog());
			movimentacaoDt.setIpComputadorLog(processoDt.getIpComputadorLog());

			List listaArquivosObjectStorage = obtenhaListaDeArquivoDoObjectStorageDigitalizacao(processoDt.getProcessoNumeroCompleto(), ordemInicial);
			incluaArquivosMovimentacaoStorage(movimentacaoDt, listaArquivosObjectStorage, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();			
		} catch (Exception ex) {
			obFabricaConexao.cancelarTransacao();
			obFabricaConexao.fecharConexao();
			throw ex;
		}
	}
	
	private void incluaArquivosMovimentacaoStorage(MovimentacaoDt movimentacaoDt, List listaArquivosObjectStorage, FabricaConexao obFabricaConexao) throws Exception {
		MovimentacaoArquivoNe MovimentacaoArquivone = new MovimentacaoArquivoNe();

		LogDt logDt = new LogDt(movimentacaoDt.getId_UsuarioLog(), movimentacaoDt.getIpComputadorLog());

		List<ArquivoDt> tempList = MovimentacaoArquivone.consultarArquivosMovimentacao(movimentacaoDt.getId(), movimentacaoDt.getId_Processo());

		if (!movimentacaoDt.temArquivos() && movimentacaoDt.isValida()) {
			if (tempList == null || tempList.size() == 0) {
				inserirArquivosObjectStorage(movimentacaoDt.getId(), listaArquivosObjectStorage, logDt, obFabricaConexao);
			}
		}
	}
	
	public void inserirArquivosObjectStorage(String id_Movimentacao, List<ArquivoDt> arquivos, LogDt logDt, FabricaConexao obFabricaConexao) throws Exception {
		if (arquivos == null)
			return;

		ArquivoNe arquivoNe = new ArquivoNe();
		// Salva arquivos no banco de dados
		for(ArquivoDt arquivoDt : arquivos) {
			arquivoDt.setId_UsuarioLog(logDt.getId_Usuario());
			arquivoDt.setIpComputadorLog(logDt.getIpComputador());
			// Salva arquivo
			arquivoNe.inserirArquivoStorage(arquivoDt, logDt, obFabricaConexao);
		}
		
		vinculaMovimentacaoArquivoObjectStorage(arquivos, id_Movimentacao, logDt, obFabricaConexao);
	}
	
	private void vinculaMovimentacaoArquivoObjectStorage(List listaArquivos, String id_Movimentacao, LogDt logDt, FabricaConexao obFabricaConexao) throws Exception {
		MovimentacaoArquivoNe movimentacaoArquivoNe = new MovimentacaoArquivoNe();
		for (int i = 0; i < listaArquivos.size(); i++) {
			ArquivoDt arquivoDt = (ArquivoDt) listaArquivos.get(i);
			MovimentacaoArquivoDt movimentacaoArquivoDt = new MovimentacaoArquivoDt();
			movimentacaoArquivoDt.setId_Movimentacao(id_Movimentacao);
			movimentacaoArquivoDt.setId_Arquivo(arquivoDt.getId());
			movimentacaoArquivoDt.setId_UsuarioLog(logDt.getId_Usuario());
			movimentacaoArquivoDt.setIpComputadorLog(logDt.getIpComputador());
			movimentacaoArquivoDt.setCodigoTemp(Integer.toString(MovimentacaoArquivoDt.OBJECT_STORAGE));
			movimentacaoArquivoDt.setId_MovimentacaoArquivoAcesso("2");
			movimentacaoArquivoNe.salvar(movimentacaoArquivoDt, obFabricaConexao);
		}
	}
	
	private List obtenhaListaDeArquivoDoObjectStorageDigitalizacao(String numeroCompletoDoProcesso, int ordemInicial) throws Exception {
		List arquivosObjectStorage = new ArrayList();

		numeroCompletoDoProcesso = Funcoes.obtenhaSomenteNumeros(numeroCompletoDoProcesso);

		String conteudoMetadataJson = obtenhaListaArquivoObjectStorageDigitalizacaoMetaDataJson(numeroCompletoDoProcesso);

		JSONObject obj = new JSONObject(conteudoMetadataJson);

		JSONArray arr = obj.getJSONArray("pecas");
		for (int i = 0; i < arr.length(); i++) {
			JSONObject peca = arr.getJSONObject(i);

			int ordem = peca.getInt("ordem");

			if (ordem >= ordemInicial) {
				ArquivoDt arquivoDt = new ArquivoDt();
				arquivoDt.setNomeArquivo(peca.getString("nome"));
				arquivoDt.setCaminho(numeroCompletoDoProcesso + "/" + arquivoDt.getNomeArquivo());

				ArquivoTipoDt arquivoTipo = obtenhaArquivoTipoDt(peca.getString("tipo_peca"));
				if (arquivoTipo != null)
					arquivoDt.setId_ArquivoTipo(arquivoTipo.getId());
				else
					arquivoDt.setId_ArquivoTipo("1"); // 1=Outros

				arquivoDt.setContentType("application/pdf");
				byte[] bytes = ByteBuffer.allocate(4).putInt(Funcoes.StringToInt(peca.getString("tamanho"))).array();
				arquivoDt.setArquivo(bytes);

				arquivosObjectStorage.add(arquivoDt);
			}
		}

		return arquivosObjectStorage;
	}
	
	private Map<String, ArquivoTipoDt> mapArquivoTipo = new HashMap<String, ArquivoTipoDt>();

	private ArquivoTipoDt obtenhaArquivoTipoDt(String tipo_peca) throws Exception {
		if (mapArquivoTipo.isEmpty()) {
			List<ArquivoTipoDt> tiposDeArquivo = new ArquivoTipoNe().consultarTodos();
			if (tiposDeArquivo != null) {
				for (ArquivoTipoDt arquivoTipoDt : tiposDeArquivo) {
					String chave = Funcoes.converteNomeSimplificado(arquivoTipoDt.getArquivoTipo());
					if (mapArquivoTipo.get(chave) == null)
						mapArquivoTipo.put(chave, arquivoTipoDt);
				}
			}
		}
		return mapArquivoTipo.get(Funcoes.converteNomeSimplificado(tipo_peca));
	}
	
	private String obtenhaListaArquivoObjectStorageDigitalizacaoMetaDataJson(String numeroCompletoDoProcesso) throws IOException, MensagemException {
		String path = Funcoes.obtenhaSomenteNumeros(numeroCompletoDoProcesso) + "/metadata.json";

		if (path != null && path.indexOf("/") != 3) {
			path = Funcoes.obtenhaSomenteNumeros(path).substring(9, 13) + "/"
					+ Funcoes.obtenhaSomenteNumeros(path).substring(0, 3) + "/" + path;
		}

		InputStream in = (new ArquivoNe()).obtenhaStreamObjectStorageDigitalizacao(path);
		StringWriter writer = new StringWriter();
		IOUtils.copy(in, writer, "UTF-8");
		return writer.toString();
	}
	
	/**
	 * Método responsável em corrigir um json com erro de vírgula antes
	 * de fechar um array ou elemento, dando erro de parser
	 * 
	 * @param json
	 * @return
	 */
	private String replaceTrailingCommasJsonParserError(String json) {
		json = json.replaceAll("(\\r|\\n)", "");
		json = json.replaceAll(",  }", "}");
		json = json.replaceAll(",  ]", "]");
		return json;
	}
	
	
	/**
	 * Obtém o arquivo metadata.json do processo
	 * @param numeroCompletoDoProcesso
	 * @return
	 * @throws IOException
	 * @throws MensagemException
	 */
	public ArquivoFisicoMetadadosDt obtenhaStorageMetaDataJson(String numeroCompletoDoProcesso) throws IOException, MensagemException {		
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        mapper.configure(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS, true);
        String json = obtenhaListaArquivoObjectStorageDigitalizacaoMetaDataJson(numeroCompletoDoProcesso);
        return mapper.readValue(replaceTrailingCommasJsonParserError(json), ArquivoFisicoMetadadosDt.class);
	}
	
	public void atualizeListaDeArquivoDoProcessoHibrido(ProcessoDt processoDt, UsuarioDt usuarioDt, List listaDeArquivos, boolean atualizaDataHibridoSPG) throws Exception {
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		ProcessoNe processoNe = new ProcessoNe();
		try {				
			obFabricaConexao.iniciarTransacao();
			
			MovimentacaoNe movimentacaoNe = new MovimentacaoNe();
			MovimentacaoDt movimentacaoDt = movimentacaoNe.consultarMovimentacaoJuntadaDeDocumentoHistoricoProcessoFisico(processoDt.getId(), obFabricaConexao);
			
			if (movimentacaoDt == null) throw new MensagemException("Não foi possível localizar a juntada de documento de histórico de processo físico!");
			
			movimentacaoDt.setId_UsuarioLog(processoDt.getId_UsuarioLog());
			movimentacaoDt.setIpComputadorLog(processoDt.getIpComputadorLog());
			
			incluaArquivosMovimentacaoProcessoHibrido(movimentacaoDt, listaDeArquivos, atualizaDataHibridoSPG, obFabricaConexao);
			
			if (atualizaDataHibridoSPG) {
				if (tentaLimparDataHibridoSPG(processoDt.getProcessoNumeroCompleto())) {
					processoNe.atualizarProcessoMistoParaDigitalConcluidaLimpezaDataSPG(movimentacaoDt.getId_Processo(), obFabricaConexao);
				}
			}
			
			obFabricaConexao.finalizarTransacao();			
		} catch (Exception ex) {
			obFabricaConexao.cancelarTransacao();
			obFabricaConexao.fecharConexao();
			throw ex;
		}
	}
	
	public void limpeDataHibridoSPGDeProcessosConvertidos() throws Exception {
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		ProcessoNe processoNe = new ProcessoNe();
		int quantidadeMaximaTransacaoSPG = 100;
		try {				
			List<ProcessoDt> processosPendentes = processoNe.consultarProcessosConvertidosMistoParaDigitalPentendeLimpezaDataSPG(obFabricaConexao, quantidadeMaximaTransacaoSPG);
			
			for(ProcessoDt processoDt : processosPendentes) {
				if (tentaLimparDataHibridoSPG(processoDt.getProcessoNumeroCompleto())) {
					processoNe.atualizarProcessoMistoParaDigitalConcluidaLimpezaDataSPG(processoDt.getId_Processo(), obFabricaConexao);
				}
			}		
						
		} catch (Exception ex) {
			obFabricaConexao.fecharConexao();
			throw ex;
		}
	}	
	
	private boolean tentaLimparDataHibridoSPG(String numeroCompletoDoProcesso) {
		try {
			ProcessoSPGNe processoSPGNe = new ProcessoSPGNe();
			NumeroProcessoDt numeroProcessoCompletoDt = new NumeroProcessoDt();
			numeroProcessoCompletoDt.setNumeroCompletoProcessoSemValidacao(Funcoes.formataNumeroCompletoProcesso(numeroCompletoDoProcesso));
			processoSPGNe.retireDataHibrido(numeroProcessoCompletoDt);
			return true;
		} catch (Exception e) {
			try {
				e.printStackTrace();
				Logger logger = Logger.getLogger(MovimentacaoArquivoNe.class);
				logger.error(e.getMessage() + "\n Erro ao limpar data do processo híbrido para o processo " + numeroCompletoDoProcesso + ".");
			} catch (Exception e2) {
				// TODO: handle exception
			}			
			return false;
		}
	}
	
	private void incluaArquivosMovimentacaoProcessoHibrido(MovimentacaoDt movimentacaoDt, List listaDeArquivos, boolean atualizaDataHibridoSPG, FabricaConexao obFabricaConexao) throws Exception {
		ProcessoNe processoNe = new ProcessoNe();

		LogDt logDt = new LogDt(movimentacaoDt.getId_UsuarioLog(), movimentacaoDt.getIpComputadorLog());

		List tempList = this.consultarArquivosMovimentacao(movimentacaoDt.getId(), movimentacaoDt.getId_Processo(), obFabricaConexao);
		
		MovimentacaoArquivoDt movimentacaoArquivoDtHistoricoSPG = null;
		if (tempList != null && tempList.size() == 1) {				
			movimentacaoArquivoDtHistoricoSPG = (MovimentacaoArquivoDt) tempList.get(0);
			movimentacaoArquivoDtHistoricoSPG.setId_UsuarioLog(movimentacaoDt.getId_UsuarioLog());
			movimentacaoArquivoDtHistoricoSPG.setIpComputadorLog(movimentacaoDt.getIpComputadorLog());
			this.invalidarArquivoMovimentacao(movimentacaoArquivoDtHistoricoSPG, MovimentacaoArquivoDt.ACESSO_BLOQUEADO, obFabricaConexao);
		}
		
		if (movimentacaoDt.isValida()) {
			inserirArquivosProcessoHibrido(movimentacaoDt.getId(), listaDeArquivos, movimentacaoArquivoDtHistoricoSPG, logDt, obFabricaConexao);
		}
		
		if (atualizaDataHibridoSPG) {
			processoNe.atualizarProcessoMistoParaDigitalPendenteLimpezaDataSPG(movimentacaoDt.getId_Processo(), obFabricaConexao);
		}
	}
	
	private void inserirArquivosProcessoHibrido(String id_Movimentacao, List arquivos, MovimentacaoArquivoDt movimentacaoArquivoDtHistoricoSPG, LogDt logDt, FabricaConexao obFabricaConexao) throws Exception {
		if (arquivos == null) return;

		ArquivoNe arquivoNe = new ArquivoNe();

		arquivoNe.inserirArquivos(arquivos, logDt, obFabricaConexao);
		
		vinculaMovimentacaoArquivoControleAcesso(arquivos, id_Movimentacao, null, logDt, obFabricaConexao);
	}
	
	public void retornarProcessoHibridoParaDigital(ProcessoDt processoDt, UsuarioDt usuarioDt, List listaDeArquivos, MovimentacaoProcessoDt movimentacaoProcessoDt) throws Exception {
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try {				
			obFabricaConexao.iniciarTransacao();
			
			MovimentacaoNe movimentacaoNe = new MovimentacaoNe();
			MovimentacaoDt movimentacaoDt = movimentacaoNe.consultarMovimentacaoJuntadaDeDocumentoHistoricoProcessoFisico(processoDt.getId(), obFabricaConexao);
			
			if (movimentacaoDt == null) throw new MensagemException("Não foi possível localizar a juntada de documento de histórico de processo físico!");
			
			movimentacaoDt.setId_UsuarioLog(processoDt.getId_UsuarioLog());
			movimentacaoDt.setIpComputadorLog(processoDt.getIpComputadorLog());
			
			excluaArquivosMovimentacaoProcessoHibrido(movimentacaoDt, obFabricaConexao);
			
			tentaInserirDataHibridoSPG(processoDt.getProcessoNumeroCompleto());
			
			if (movimentacaoProcessoDt != null) {
				LogDt logDt = new LogDt(processoDt.getId_UsuarioLog(), processoDt.getIpComputadorLog());
				
				MovimentacaoDt novaMovimentacaoDt = movimentacaoNe.gerarMovimentacao(processoDt.getId(),
						                                                             Funcoes.StringToInt(movimentacaoProcessoDt.getMovimentacaoTipoCodigo()),
																                     usuarioDt.getId_UsuarioServentia(),
																                     MovimentacaoTipoDt.COMPLEMENTO_RETORNAR_PARA_HIBRIDO, 
																                     logDt, 
																					 obFabricaConexao);
				
				ArquivoNe arquivoNe = new ArquivoNe();

				arquivoNe.inserirArquivos(listaDeArquivos, logDt, obFabricaConexao);
				
				vinculaMovimentacaoArquivoControleAcesso(listaDeArquivos, novaMovimentacaoDt.getId(), null, logDt, obFabricaConexao);
			}
			
			obFabricaConexao.finalizarTransacao();			
		} catch (Exception ex) {
			obFabricaConexao.cancelarTransacao();
			obFabricaConexao.fecharConexao();
			throw ex;
		}
	}
	
	private void excluaArquivosMovimentacaoProcessoHibrido(MovimentacaoDt movimentacaoDt, FabricaConexao obFabricaConexao) throws Exception {
		ProcessoNe processoNe = new ProcessoNe();

		LogDt logDt = new LogDt(movimentacaoDt.getId_UsuarioLog(), movimentacaoDt.getIpComputadorLog());

		List tempList = this.consultarArquivosMovimentacao(movimentacaoDt.getId(), movimentacaoDt.getId_Processo(), obFabricaConexao);
		
		MovimentacaoArquivoDt movimentacaoArquivoDtHistoricoSPG = null;
		if (tempList != null && tempList.size() > 0) {	
			for (int i = 0; i < tempList.size(); i++) {
				movimentacaoArquivoDtHistoricoSPG = (MovimentacaoArquivoDt) tempList.get(i);
				movimentacaoArquivoDtHistoricoSPG.setId_UsuarioLog(movimentacaoDt.getId_UsuarioLog());
				movimentacaoArquivoDtHistoricoSPG.setIpComputadorLog(movimentacaoDt.getIpComputadorLog());
				
				if (i == 0) {
					this.validarArquivoMovimentacao(movimentacaoArquivoDtHistoricoSPG, obFabricaConexao);
				} else {
					this.excluir(movimentacaoArquivoDtHistoricoSPG, obFabricaConexao);
				}
			}	
		}
		
		processoNe.atualizarProcessoDigitalParaMisto(movimentacaoDt.getId_Processo(), obFabricaConexao);
	}
	
	public void excluir(MovimentacaoArquivoDt dados, FabricaConexao obFabricaConexao) throws Exception {
		LogDt obLogDt;	
		MovimentacaoArquivoPs obPersistencia = new MovimentacaoArquivoPs(obFabricaConexao.getConexao());
		obLogDt = new LogDt("MovimentacaoArquivo", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir), dados.getPropriedades(), "");
		obPersistencia.excluir(dados.getId());		
		obLog.salvar(obLogDt, obFabricaConexao);
	}
	
	private boolean tentaInserirDataHibridoSPG(String numeroCompletoDoProcesso) {
		try {
			ProcessoSPGNe processoSPGNe = new ProcessoSPGNe();
			NumeroProcessoDt numeroProcessoCompletoDt = new NumeroProcessoDt();
			numeroProcessoCompletoDt.setNumeroCompletoProcessoSemValidacao(Funcoes.formataNumeroCompletoProcesso(numeroCompletoDoProcesso));
			processoSPGNe.insereDataHibrido(numeroProcessoCompletoDt);
			return true;
		} catch (Exception e) {
			try {
				e.printStackTrace();
				Logger logger = Logger.getLogger(MovimentacaoArquivoNe.class);
				logger.error(e.getMessage() + "\n Erro ao inserir data do processo híbrido para o processo " + numeroCompletoDoProcesso + ".");
			} catch (Exception e2) {
				// TODO: handle exception
			}			
			return false;
		}
	}
	
	public List consultarArquivoMovimentacao(String id_Movimentacao) throws Exception {
		List tempList = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			MovimentacaoArquivoPs obPersistencia = new MovimentacaoArquivoPs(obFabricaConexao.getConexao());
			tempList = obPersistencia.consultarArquivoMovimentacao(id_Movimentacao);
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;
	}
	
	public String consultarNumeroProcesso(String id_movimentacaoarquivo) throws Exception {
		String numeroProcesso = "";
		FabricaConexao obFabricaConexao = null;
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{
			MovimentacaoArquivoPs obPersistencia = new MovimentacaoArquivoPs(obFabricaConexao.getConexao());
			numeroProcesso = obPersistencia.consultarNumeroProcesso(id_movimentacaoarquivo);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return numeroProcesso;
	}
}
