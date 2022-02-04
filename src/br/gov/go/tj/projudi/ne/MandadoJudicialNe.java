package br.gov.go.tj.projudi.ne;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ArquivoDt;
import br.gov.go.tj.projudi.dt.EscalaDt;
import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.HistoricoRedistribuicaoMandadosDt; 
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.MandadoJudicialDt;
import br.gov.go.tj.projudi.dt.MandadoJudicialStatusDt;
import br.gov.go.tj.projudi.dt.MandadoTipoDt;
import br.gov.go.tj.projudi.dt.PendenciaDt;
import br.gov.go.tj.projudi.dt.PendenciaResponsavelDt;
import br.gov.go.tj.projudi.dt.PendenciaStatusDt;
import br.gov.go.tj.projudi.dt.PendenciaTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ServentiaCargoEscalaDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.ps.CidadePs;
import br.gov.go.tj.projudi.ps.MandadoJudicialPs;
import br.gov.go.tj.projudi.ps.PrazoSuspensoPs;
import br.gov.go.tj.projudi.ps.ServentiaCargoEscalaPs;
import br.gov.go.tj.projudi.ps.ServentiaPs;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;

public class MandadoJudicialNe extends MandadoJudicialNeGen {

	private static final long serialVersionUID = 112561029577543419L;

	private PendenciaResponsavelNe pendenciaResponsavelNe;

	private ServentiaCargoEscalaNe serventiaCargoEscalaNe;

	public MandadoJudicialNe() {
		this.pendenciaResponsavelNe = new PendenciaResponsavelNe();
		this.serventiaCargoEscalaNe = new ServentiaCargoEscalaNe();
	}

	/**
	 * Método implementado da interface abstract
	 */
	public String Verificar(MandadoJudicialDt dados) {

		String stRetorno = "";

		return stRetorno;
	}

	/**
	 * Método de Consulta do Mandado Judicial qua retorna o objeto Mandado Judicial
	 * com os objetos encapsulado.
	 * 
	 * @param MandadoJudicialDt
	 * @return MandadoJudicialDt
	 * @throws Exception
	 */
	// public MandadoJudicialDt consularId(MandadoJudicialDt mandadoJudicialDt)
	// throws Exception {
	//
	// MandadoJudicialDt dtRetorno = null;
	// FabricaConexao obFabricaConexao = null;
	//
	// try {
	// obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
	// MandadoJudicialPs obPersistencia = new
	// MandadoJudicialPs(obFabricaConexao.getConexao());
	// dtRetorno = obPersistencia.consultarId(mandadoJudicialDt.getId());;
	// if (dtRetorno != null)
	// obDados.copiar(dtRetorno);
	//
	// } finally {
	// obFabricaConexao.fecharConexao();
	// }
	// return dtRetorno;
	// }

	public List consultarMandadosAbertos(String numero, String idUsuarioServentia, String idServentia) throws Exception {

		List tempList = null;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			MandadoJudicialPs obPersistencia = new MandadoJudicialPs(obFabricaConexao.getConexao());
			tempList = obPersistencia.consultarMandadosAbertos(numero, idUsuarioServentia, idServentia);

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return tempList;
	}

	/**
	 * Consulta de mandados que tiveram o pagamento aprovado (status de Autorizado ou Enviado).
	 * @param numeroMandado
	 * @param nomeOficial
	 * @param idProcesso
	 * @param posicao
	 * @return String
	 * @throws Exception
	 * @author hrrosa
	 */
	public String consultarMandadosPagamentoAprovadoJson(String numeroMandado, String nomeOficial, String idProcesso, String posicao) throws Exception {
		FabricaConexao obFabricaConexao = null;
		String retorno = null;
		
		if(idProcesso == null || Funcoes.StringToInt(idProcesso)==0){
			throw new MensagemException("A identificação do processo é obrigatória para a consultar");
		}
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			MandadoJudicialPs obPersistencia = new MandadoJudicialPs(obFabricaConexao.getConexao());
			retorno = obPersistencia.consultarMandadosPagamentoAprovadoJson(numeroMandado, nomeOficial, idProcesso, posicao);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return retorno;
	}
	
	/**
	 * Consultar Mandado Judicial por Id da Pendência.
	 * 
	 * @param String
	 *            idPendencia
	 * @return MandadoJudicialDt
	 * @throws Exception
	 */
	public MandadoJudicialDt consultarPorIdPendencia(String idPendencia) throws Exception {
		MandadoJudicialDt mandadoJudicialDt = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			MandadoJudicialPs obPersistencia = new MandadoJudicialPs(obFabricaConexao.getConexao());
			mandadoJudicialDt = obPersistencia.consultarPorIdPendencia(idPendencia);

		} finally {
			obFabricaConexao.fecharConexao();
		}

		return mandadoJudicialDt;
	}

	/**
	 * Método que consulta a pendencia e valida se é do tipo Mandado de acordo com o
	 * idPendencia passado como parâmetro. Obs.: Ao invés de usar diretamente o
	 * PendenciaNe preferi criar este método no MandadoJudicialNe pelo fato de
	 * validar se a pendencia consultada é do tipo MANDADO. Caso contrário, eu teria
	 * de validar diretamente no Ct(Controle), o que não ficaria legal.
	 * 
	 * @param String
	 *            idPendencia
	 * @return PendenciaDt
	 * @throws Exception
	 */
	public PendenciaDt consultarPendenciaTipoMandado(Integer idPendencia) throws Exception {
		PendenciaDt pendenciaDt = null;

		pendenciaDt = this.pendenciaResponsavelNe.consultarPendenciaPorIdAbertaComResponsavel(idPendencia,
				PendenciaStatusDt.ID_AGUARDANDO_RETORNO);

		if (pendenciaDt != null) {
			// Valida se a pendência consultada é do tipo MANDADO
			if (Funcoes.StringToInt(pendenciaDt.getId_PendenciaTipo()) != PendenciaTipoDt.MANDADO) {
				// Não pode passar pendencia sem ser MANDADO
				pendenciaDt = null;
			}
		}

		return pendenciaDt;
	}

	/**
	 * Método para alterar o status do mandado judicial
	 * 
	 * @param MandadoJudicialDt
	 *            mandadoJudicialDt
	 * @param Integer
	 *            novoStatus
	 * @return boolean
	 * @throws Exception
	 */
	public boolean alterarStatusMandadoJudicial(MandadoJudicialDt mandadoJudicialDt, Integer novoStatus)
			throws Exception {
		boolean retorno = false;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			MandadoJudicialPs obPersistencia = new MandadoJudicialPs(obFabricaConexao.getConexao());
			retorno = obPersistencia.alterarStatusMandadoJudicial(mandadoJudicialDt, novoStatus);

		} finally {
			obFabricaConexao.fecharConexao();
		}

		return retorno;
	}

	/**
	 * Valida se os parametros sao numéricos.
	 * 
	 * @param String
	 *            numeroProcesso
	 * @return String
	 * @throws Exception
	 */
	public String verificarDados(String numero) {
		String retorno = "";

		if (!Funcoes.validaNumerico(numero))
			retorno = "Preencha o campo somente com números.";

		return retorno;
	}

	/**
	 * Método para alterar somente a data limite do mandado.
	 * 
	 * @param MandadoJudicialDt
	 *            mandadoJudicialDt
	 * @return boolean
	 * @throws Exception
	 */
	public boolean alterarDataLimite(MandadoJudicialDt mandadoJudicialDt) throws Exception {
		boolean retorno = false;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			MandadoJudicialPs obPersistencia = new MandadoJudicialPs(obFabricaConexao.getConexao());

			retorno = obPersistencia.alterarDataLimite(mandadoJudicialDt);

		} finally {
			obFabricaConexao.fecharConexao();
		}

		return retorno;
	}
	

	/**
	 * Método para alterar assistencia.
	 * 
	 * @param MandadoJudicialDt
	 *            mandadoJudicialDt
	 * @return boolean
	 * @throws Exception
	 */
	public int alterarAssistencia(String idMandJud, int isAssistencia, FabricaConexao obFabricaConexao) throws Exception {
		
		if(obFabricaConexao == null || idMandJud == null || (isAssistencia != 0 && isAssistencia != 1 ) ) {
			throw new MensagemException("Valor inválido de parâmetros");
		}
		
		MandadoJudicialPs obPersistencia = new MandadoJudicialPs(obFabricaConexao.getConexao());
		return obPersistencia.alterarAssistencia(idMandJud, isAssistencia);

	}


//	public void salvar(MandadoJudicialDt dados) throws Exception {
//
//		LogDt obLogDt;
//		FabricaConexao obFabricaConexao = null;
//
//		try {
//			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
//			obFabricaConexao.iniciarTransacao();
//			MandadoJudicialPs obPersistencia = new MandadoJudicialPs(obFabricaConexao.getConexao());
//			if (dados.getId().length() == 0) {
//				obPersistencia.inserir(dados);
//				obLogDt = new LogDt("MandadoJudicial", dados.getId(), dados.getId_UsuarioLog(),
//						dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir), "", dados.getPropriedades());
//			} else {
//				obPersistencia.alterar(dados);
//				obLogDt = new LogDt("MandadoJudicial", dados.getId(), dados.getId_UsuarioLog(),
//						dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar), obDados.getPropriedades(),
//						dados.getPropriedades());
//			}
//
//			obDados.copiar(dados);
//			obLog.salvar(obLogDt, obFabricaConexao);
//			obFabricaConexao.finalizarTransacao();
//
//		} catch (Exception e) {
//			obFabricaConexao.cancelarTransacao();
//			throw e;
//		} finally {
//			obFabricaConexao.fecharConexao();
//		}
//	}

//	public void salvar(MandadoJudicialDt dados, FabricaConexao obFabricaConexao) throws Exception {
//
//		LogDt obLogDt;
//
//		MandadoJudicialPs obPersistencia = new MandadoJudicialPs(obFabricaConexao.getConexao());
//		if (dados.getId().length() == 0) {
//			obPersistencia.inserir(dados);
//			obLogDt = new LogDt("MandadoJudicial", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(),
//					String.valueOf(LogTipoDt.Incluir), "", dados.getPropriedades());
//		} else {
//			obPersistencia.alterar(dados);
//			obLogDt = new LogDt("MandadoJudicial", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(),
//					String.valueOf(LogTipoDt.Alterar), obDados.getPropriedades(), dados.getPropriedades());
//		}
//
//		obDados.copiar(dados);
//		obLog.salvar(obLogDt, obFabricaConexao);
//	}
	
	public void inserir(MandadoJudicialDt dados, FabricaConexao obFabricaConexao) throws Exception {

		LogDt obLogDt;

		MandadoJudicialPs obPersistencia = new MandadoJudicialPs(obFabricaConexao.getConexao());
		
		obPersistencia.inserir(dados);
		obLogDt = new LogDt("MandadoJudicial", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(),
					String.valueOf(LogTipoDt.Incluir), "", dados.getPropriedades());
		

		obDados.copiar(dados);
		obLog.salvar(obLogDt, obFabricaConexao);
	}
	
	public void alterar(MandadoJudicialDt dados, FabricaConexao obFabricaConexao) throws Exception {

		LogDt obLogDt;

		MandadoJudicialPs obPersistencia = new MandadoJudicialPs(obFabricaConexao.getConexao());

		obPersistencia.alterar(dados);
		obLogDt = new LogDt("MandadoJudicial", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(),
					String.valueOf(LogTipoDt.Alterar), obDados.getPropriedades(), dados.getPropriedades());

		obDados.copiar(dados);
		obLog.salvar(obLogDt, obFabricaConexao);
	}
	



	/**
	 * Método para verificar se o Mandado Judicial é do tipo ESPECIAL
	 * 
	 * @param MandadoTipoDt
	 * @return boolean
	 */
	public boolean isMandadoJudicialEspecial(MandadoTipoDt mandadoTipoDt) {
		if (mandadoTipoDt.getMandadoTipoCodigo().equals(MandadoTipoDt.ESPECIAL))
			return true;

		return false;
	}

	/**
	 * Suspende os oficiais que estão com prazos de mandados judiciais vencidos.
	 * 
	 * @throws Exception
	 */
	// public void suspenderOficiaisPrazoExpirado() throws Exception {
	// FabricaConexao obFabricaConexao = null;
	// try{
	// obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
	// MandadoJudicialPs obPersistencia = new
	// MandadoJudicialPs(obFabricaConexao.getConexao());
	//
	// List listaMandados = obPersistencia.consultarMandadosPrazosExpirados();
	// if( listaMandados != null ) {
	// for( int i = 0; i < listaMandados.size(); i++ ) {
	// MandadoJudicialDt mandadoJudicialDt =
	// (MandadoJudicialDt)listaMandados.get(i);
	//
	// this.serventiaCargoEscalaNe.ativaDesativaUsuarioEscala(mandadoJudicialDt.getUsuarioServentiaDt_1(),
	// mandadoJudicialDt.getEscalaDt(), ServentiaCargoEscalaDt.INATIVO);
	// if( mandadoJudicialDt.getUsuarioServentiaDt_2() != null ) {
	// this.serventiaCargoEscalaNe.ativaDesativaUsuarioEscala(mandadoJudicialDt.getUsuarioServentiaDt_2(),
	// mandadoJudicialDt.getEscalaDt(), ServentiaCargoEscalaDt.INATIVO);
	// }
	// }
	// }
	//
	// }
	// finally{
	// obFabricaConexao.fecharConexao();
	// }
	// }

	/**
	 * Retira as suspensõs de oficiais com prazo expirados.
	 * 
	 * @throws Exception
	 */
	// public void retirarSuspencaoOficialPrazoExpirado() throws Exception {
	// FabricaConexao obFabricaConexao = null;
	// try{
	// obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
	// MandadoJudicialPs obPersistencia = new
	// MandadoJudicialPs(obFabricaConexao.getConexao());
	//
	// //Consulta mandados que retornaram e que está com prazo expirado
	// List listaMandados =
	// obPersistencia.consultarMandadosRetornadosPrazosExpirados();
	// if( listaMandados != null ) {
	// for( int i = 0; i < listaMandados.size(); i++ ) {
	// MandadoJudicialDt mandadoJudicialDt = (MandadoJudicialDt)
	// listaMandados.get(i);
	//
	// this.serventiaCargoEscalaNe.ativaDesativaUsuarioEscala(mandadoJudicialDt.getUsuarioServentiaDt_1(),
	// mandadoJudicialDt.getEscalaDt(), ServentiaCargoEscalaDt.ATIVO);
	// if( mandadoJudicialDt.getUsuarioServentiaDt_2() != null ) {
	// this.serventiaCargoEscalaNe.ativaDesativaUsuarioEscala(mandadoJudicialDt.getUsuarioServentiaDt_2(),
	// mandadoJudicialDt.getEscalaDt(), ServentiaCargoEscalaDt.ATIVO);
	// }
	// }
	// }
	//
	// }
	// finally{
	// obFabricaConexao.fecharConexao();
	// }
	// }

	public String consultarDescricaoAreaJSON(String tempNomeBusca, String PosicaoPaginaAtual) throws Exception {
		String stTemp = "";
		AreaNe Areane = new AreaNe();
		stTemp = Areane.consultarDescricaoJSON(tempNomeBusca, PosicaoPaginaAtual);
		return stTemp;
	}

	public String consultarDescricaoZonaJSON(String tempNomeBusca1, String tempNomeBusca2, String PosicaoPaginaAtual)
			throws Exception {
		String stTemp = null;
		ZonaNe Zonane = new ZonaNe();
		stTemp = Zonane.consultarDescricaoJSON(tempNomeBusca1, tempNomeBusca2, PosicaoPaginaAtual);
		return stTemp;
	}

	public String consultarDescricaoMandadoTipoJSON(String tempNomeBusca, String posicaoPaginaAtual) throws Exception {
		String stTemp = null;
		MandadoTipoNe objNE = new MandadoTipoNe();
		stTemp = objNE.consultarDescricaoJSON(tempNomeBusca, posicaoPaginaAtual);
		return stTemp;
	}

	public String consultarDescricaoEscalaJSON(String tempNomeBusca, String idServ, String PosicaoPaginaAtual)
			throws Exception {
		String stTemp = null;
		EscalaNe Escalane = new EscalaNe();
		stTemp = Escalane.consultarDescricaoJSON(tempNomeBusca, idServ, PosicaoPaginaAtual);
		return stTemp;
	}

	public int salvarDataRetorno(String dataFim, String idPendencia, FabricaConexao obFabricaConexao) throws Exception {
		int registros = 0;
		MandadoJudicialPs obPersistencia = new MandadoJudicialPs(obFabricaConexao.getConexao());
		registros = obPersistencia.salvarDataRetorno(dataFim, idPendencia);
		return registros;
	}

	public String consultarOficialJSON(String tempNomeBusca, String posicaoPaginaAtual, String idServentia)
			throws Exception {
		String stTemp = "";
		UsuarioServentiaNe usuarioServentiaNe = new UsuarioServentiaNe();
		stTemp = usuarioServentiaNe.consultarUsuariosServentiaGrupoJSON(tempNomeBusca, posicaoPaginaAtual, idServentia,
				String.valueOf(GrupoDt.OFICIAL_JUSTICA));
		return stTemp;
	}

	public PendenciaDt consultarPendenciaFinalizadaId(String id_Pendencia, UsuarioNe usuarioSessao) throws Exception {
		PendenciaNe pendenciaNe = new PendenciaNe();
		PendenciaDt pendenciaDt = new PendenciaDt();

		pendenciaDt = pendenciaNe.consultarPendenciaFinalizadaId(id_Pendencia, usuarioSessao);

		return pendenciaDt;
	}

	public void redistribuirPlantao(String[] mandados, String id_UsuarioLog, String ip_Computador, String motivo, String idMandTipoRedist) throws Exception {
		redistribuir(mandados, id_UsuarioLog, ip_Computador, true, motivo, idMandTipoRedist);
	}

	public void redistribuir(String[] mandados, String id_UsuarioLog, String ip_Computador, String motivo, String idMandTipoRedist) throws Exception {
		redistribuir(mandados, id_UsuarioLog, ip_Computador, false, motivo, idMandTipoRedist);
	}

	public void redistribuir(String[] mandados, String id_UsuarioLog, String ip_Computador, boolean isRedistribuirPlantao, String motivo, String idMandTipoRedist) throws Exception {
		MandadoJudicialDt mandadoJudicialDt = new MandadoJudicialDt();
		PendenciaResponsavelNe pendenciaResponsavelNe = new PendenciaResponsavelNe();
		PendenciaResponsavelDt pendenciaResponsavelDt = new PendenciaResponsavelDt();
		MandadoJudicialStatusNe mandadoJudicialStatusNe = new MandadoJudicialStatusNe();
		PendenciaNe pendenciaNe = new PendenciaNe();
		String id_MandadoJudicialStatus = "";
		String idEscalaPlantao;
		EscalaNe escalaNe = new EscalaNe();
		String[] oficial = new String[2];
		FabricaConexao obFabricaConexao = null;
		String idEscAnterior = ""; 
	
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			MandadoJudicialPs obPersistencia = new MandadoJudicialPs(obFabricaConexao.getConexao());
			obFabricaConexao.iniciarTransacao();
			for (int i = 0; i < mandados.length; i++) {
				mandadoJudicialDt = obPersistencia.consultarId(mandados[i]);
				id_MandadoJudicialStatus = mandadoJudicialStatusNe.consultarCodigo(String.valueOf(MandadoJudicialStatusDt.REDISTRIBUIDO), obFabricaConexao);
				mandadoJudicialDt.setId_MandadoJudicialStatus(id_MandadoJudicialStatus);
				mandadoJudicialDt.setId_UsuarioLog(id_UsuarioLog);
				mandadoJudicialDt.setIpComputadorLog(ip_Computador);
				// salvar(mandadoJudicialDt, obFabricaConexao);
				pendenciaResponsavelDt = pendenciaResponsavelNe.consultarIdPendenciaResponsavel(mandadoJudicialDt.getId_Pendencia(), obFabricaConexao);

				if(pendenciaResponsavelDt == null) {
					throw new MensagemException("Erro ao identificar o responsável atual pelo mandado. Entre em contato com o Gerenciamento de Sistemas.");
				}
				
				// Se for plantão, atribuir o id da escala de plantão da Central de Mandados
				// para que o mandado seja
				// redistribuído neste escala.
								
				idEscalaPlantao = null;				
				
				if (isRedistribuirPlantao) {
					idEscalaPlantao = escalaNe.consultarIdEscalaPlantao(mandadoJudicialDt.getId_Serventia(), obFabricaConexao);
					if(idEscalaPlantao == null){
						throw new MensagemException("Impossível localizar a escala de plantão da serventia.");
					}
					idEscAnterior = mandadoJudicialDt.getId_Escala();
					mandadoJudicialDt.setId_Escala(idEscalaPlantao);
				} else idEscAnterior = mandadoJudicialDt.getId_Escala();

				oficial = escalaNe.redistribuicaoEscala(mandadoJudicialDt.getId_Escala(), mandadoJudicialDt.getId_ServentiaCargoEscala(), obFabricaConexao);

				if (oficial[1] != null && !oficial[1].equalsIgnoreCase("")) {
					pendenciaResponsavelDt.setId_ServentiaCargo(oficial[1]);
					pendenciaResponsavelDt.setId_UsuarioLog(id_UsuarioLog);
					pendenciaResponsavelDt.setIpComputadorLog(ip_Computador);
					pendenciaResponsavelNe.salvar(pendenciaResponsavelDt, obFabricaConexao);
					pendenciaNe.alterarReserva(mandadoJudicialDt.getId_Pendencia(), oficial[0], obFabricaConexao);
					// 
					// gravar historico redistribuicao
					//					
					HistoricoRedistribuicaoMandadoNe objNe = new HistoricoRedistribuicaoMandadoNe();
					objNe.cadastraHistoricoRedistribuicao(mandadoJudicialDt.getId_UsuarioServentia_1(), oficial[0], 
							idEscAnterior, mandadoJudicialDt.getId_Escala(), mandadoJudicialDt.getId(), motivo, idMandTipoRedist, obFabricaConexao);
				    //				
					mandadoJudicialDt.setId_UsuarioServentia_1(oficial[0]);
					mandadoJudicialDt.setId_ServentiaCargoEscala(oficial[2]);
					// mandadoJudicialDt.setId("");
					mandadoJudicialDt.setDataDistribuicao(Funcoes.DataHora(new Date()));
					// mandadoJudicialDt.setDataLimite("");
					// mandadoJudicialDt.setId_MandadoJudicialStatus(id_MandadoJudicialStatus);
					// mandadoJudicialDt.setId_UsuarioLog(id_UsuarioLog);
					// mandadoJudicialDt.setIpComputadorLog(ip_Computador);
					alterar(mandadoJudicialDt, obFabricaConexao);
					mandadoJudicialDt = null;
					pendenciaResponsavelDt = null;
				}
			}
			obFabricaConexao.finalizarTransacao();
		} catch (Exception e) {
			obFabricaConexao.cancelarTransacao();
			throw e;
		} finally {
			obFabricaConexao.fecharConexao();
		}
	}

	public MandadoJudicialDt consultarIdFinalizado(String id_mandadojudicial) throws Exception {

		MandadoJudicialDt dtRetorno = null;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			MandadoJudicialPs obPersistencia = new MandadoJudicialPs(obFabricaConexao.getConexao());
			dtRetorno = obPersistencia.consultarIdFinalizado(id_mandadojudicial);
			obDados.copiar(dtRetorno);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}

	/**
	 * Verifica se algum dos mandados da lista não pertencem ao oficial
	 * especificado. Retorna true se todos os mandados forem do oficial e retorna
	 * false se algum não for deles.
	 * 
	 * @param listaMandadoId
	 * @param usuId
	 * @return boolean
	 * @throws Exception
	 * @author hrrosa
	 */
	public boolean testarPropriedadeMandado(String[] listaMandadoId, String usuId) throws Exception {
		FabricaConexao obFabricaConexao = null;
		if (listaMandadoId == null || usuId == null) {
			throw new MensagemException("Parâmetros inválidos");
		}
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			MandadoJudicialPs mandadoJudicialPs = new MandadoJudicialPs(obFabricaConexao.getConexao());
			return mandadoJudicialPs.testarPropriedadeMandado(listaMandadoId, usuId);
		} finally {
			obFabricaConexao.fecharConexao();
		}
	}

	/**
	 * Finaliza o mandado (mand_jud) quando o oficial finalizar a pendência do tipo
	 * mandado (pend). Utiliza como parâmetro, para saber qual mandado deve fechar,
	 * o id da pendência em questão.
	 * 
	 * @param idPend
	 * @param idOficialCompanheiro
	 * @param codigoMandJudStatus
	 * @throws Exception
	 * @author hrrosa
	 */
	public void finalizaMandadoAbertoIdPend(String idPend, String idOficialPrincipal, String idOficialCompanheiro, String codigoPendenciaStatus, FabricaConexao obFabricaConexao) throws Exception {
		
		MandadoJudicialPs obPersistencia = new MandadoJudicialPs(obFabricaConexao.getConexao());
		PendenciaNe pendenciaNe = new PendenciaNe();
		int codigoMandJudStatus;

		// Descobre qual deverá ser o status do mandado com base no
		// status da pendência que está sendo finalizada
		// TODO: Verificar junto à DGPE se em algum destes status devemos liberar as
		// locomoções
		// FRED                        
		
		switch (codigoPendenciaStatus) { 
		    case "Efetivada":
			   codigoMandJudStatus = MandadoJudicialStatusDt.CUMPRIDO;
               break;
			case "Não Efetivada":
			   codigoMandJudStatus = MandadoJudicialStatusDt.INFORMADO;
			   break;
			case "Efetivada em Parte":
			   codigoMandJudStatus = MandadoJudicialStatusDt.PARCIALMENTE_CUMPRIDO;
			   break;
			default:
			   codigoMandJudStatus = 0;
			   break;
			}

		if (codigoMandJudStatus == 0) {
			throw new MensagemException(
					"Status da pendência não mapeada para mandados. Por favor, entre em contato com a equipe de suporte");
		}
		
		//Caso este mandado possua um número determinado de locomoções ou mais, obriga a seleção de um oficial companheiro ao concluir.
		if(pendenciaNe.teraOficialCompanheiroMandado(idPend) && Funcoes.StringToInt(idOficialCompanheiro) == 0) {
			throw new MensagemException("É obrigatório selecionar um oficial companheiro para concluir este mandado.");
		}

		if(Funcoes.StringToInt(idOficialPrincipal) != 0 && Funcoes.StringToInt(idOficialCompanheiro) != 0 && idOficialPrincipal.equals(idOficialCompanheiro)){
			throw new MensagemException("O oficial companheiro não pode ser igual ao oficial principal do mandado.");
		}
		
		obPersistencia.finalizaMandadoAbertoIdPend(idPend, idOficialCompanheiro, codigoMandJudStatus);
	}

	/**
	 * Retorna todos os mandados expirados de oficiais que ainda não constam na
	 * tabela de afastamento. Utilizado para o serviço da execução automática que
	 * suspende automaticamente estes oficiais.
	 * 
	 * @return List<MandadoJudicialDt>
	 * @author hrrosa
	 * @throws Exception
	 */
	public List<MandadoJudicialDt> consultarMandadosAtrasadosExecucaoAutomatica(FabricaConexao obFabricaConexao)
			throws Exception {
		MandadoJudicialPs obPersistencia = new MandadoJudicialPs(obFabricaConexao.getConexao());
		return obPersistencia.consultarMandadosAtrasadosExecucaoAutomatica();
	}
	
	public List<MandadoJudicialDt> consultaOficiaisParaRetornoDeSuspensao(FabricaConexao obFabricaConexao)
			throws Exception {
		MandadoJudicialPs obPersistencia = new MandadoJudicialPs(obFabricaConexao.getConexao());
		return obPersistencia.consultaOficiaisParaRetornoDeSuspensao();
	}


	public String consultarMeusMandadosOficialJSON(String idUsuServ, String dataInicial, String dataFinal,
			String posicao) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			MandadoJudicialPs obPersistencia = new MandadoJudicialPs(obFabricaConexao.getConexao());
			// HELLENO
			// if(dataInicial == null || dataInicial.isEmpty()){
			// dataInicial = Funcoes.dateToStringSoData(Funcoes.getPrimeiroDiaMes());
			// }
			stTemp = obPersistencia.consultarMeusMandadosOficialJSON(idUsuServ, dataInicial, dataFinal, posicao);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}

	public String consultarSituacaoDistribuicaoCentral(String idServCentral, String idEsc, String posicao)
			throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			MandadoJudicialPs obPersistencia = new MandadoJudicialPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarSituacaoDistribuicaoCentral(idServCentral, idEsc, posicao);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}

	public List<EscalaDt> consultarServentiaCargoEscalaPorServentiaCargo(String idServentiaCargo) throws Exception {
		FabricaConexao obFabricaConexao = null;
		List<EscalaDt> liTemp = null;
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaCargoEscalaNe serventiaCargoEscalaNe = new ServentiaCargoEscalaNe();
			liTemp = serventiaCargoEscalaNe.consultarServentiaCargoEscalaPorServentiaCargo(idServentiaCargo,
					obFabricaConexao);
		} finally {
			if (obFabricaConexao != null) obFabricaConexao.fecharConexao();
		}

		return liTemp;
	}

	public List<MandadoJudicialDt> consultaMandadosPorDataRetornoCustas(String idServentiaSessao, String dataReferencia, String idStatusOrdem)
			throws Exception {

		MandadoJudicialDt mandadoJudicialDt;
		MandadoJudicialDt mandJudDt;
		List<MandadoJudicialDt> listaMandado = new ArrayList();
		FabricaConexao obFabricaConexao = null;
		obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
		try {
			
			MandadoJudicialPs obPersistencia = new MandadoJudicialPs(obFabricaConexao.getConexao());
			listaMandado = obPersistencia.consultaMandadosPorDataRetornoCustas(idServentiaSessao, dataReferencia, idStatusOrdem);
			for (int i = 0; i < listaMandado.size(); i++) {
				mandadoJudicialDt = listaMandado.get(i);
				mandadoJudicialDt = calculaValorLocomocao(mandadoJudicialDt);
				listaMandado.set(i, mandadoJudicialDt);
			}
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return listaMandado;
	}

	
	public List<MandadoJudicialDt> consultaMandadosPorDataRetornoGratuito(String idServentiaSessao, String dataReferencia, String idStatusOrdem)
			throws Exception {

		List<MandadoJudicialDt> listaMandado = new ArrayList<MandadoJudicialDt>();
		FabricaConexao obFabricaConexao = null;
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try {
			
			MandadoJudicialPs obPersistencia = new MandadoJudicialPs(obFabricaConexao.getConexao());
			listaMandado = obPersistencia.consultaMandadosPorDataRetornoGratuito(idServentiaSessao, dataReferencia, idStatusOrdem);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return listaMandado;
	}
	
	public List<MandadoJudicialDt> consultaMandadosGratuitosAdicionalAnalisado(String idServentiaSessao, String dataReferencia)
			throws Exception {

		List<MandadoJudicialDt> listaMandado = new ArrayList<MandadoJudicialDt>();
		FabricaConexao obFabricaConexao = null;
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try {
			
			MandadoJudicialPs obPersistencia = new MandadoJudicialPs(obFabricaConexao.getConexao());
			listaMandado = obPersistencia.consultaMandadosGratuitosAdicionalAnalisado(idServentiaSessao, dataReferencia);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return listaMandado;
	}
	
	public MandadoJudicialDt calculaValorLocomocao(MandadoJudicialDt mandadoJudicialDt) throws Exception {
		MandadoJudicialDt objDt;
		MandadoJudicialPs obPersistencia;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try {
			obPersistencia = new MandadoJudicialPs(obFabricaConexao.getConexao());
			objDt = obPersistencia.calculaValorLocomocao(mandadoJudicialDt.getId());
			if (objDt != null) {
				mandadoJudicialDt.setValorLocomocao(objDt.getValorLocomocao());
				mandadoJudicialDt.setQuantidadeLocomocao(objDt.getQuantidadeLocomocao());
			}
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return mandadoJudicialDt;
	}
	
	public MandadoJudicialDt consultarId(int id_mandadojudicial) throws Exception {

		MandadoJudicialDt mandadoJudicialDt;
		FabricaConexao obFabricaConexao = null;
		try {

			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			MandadoJudicialPs obPersistencia = new MandadoJudicialPs(obFabricaConexao.getConexao());
			mandadoJudicialDt = obPersistencia.consultarId(Integer.toString(id_mandadojudicial));
			if (mandadoJudicialDt != null) {
		        mandadoJudicialDt.setDataPagamentoEnvio(Funcoes.FormatarData(mandadoJudicialDt.getDataPagamentoEnvio()));
			    mandadoJudicialDt.setDataPagamentoStatus(Funcoes.FormatarData(mandadoJudicialDt.getDataPagamentoStatus()));
				mandadoJudicialDt = calculaValorLocomocao(mandadoJudicialDt);			
			}
			
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return mandadoJudicialDt;
	}

	public List<MandadoJudicialDt> consultaMandJudPagamentoStatus() throws Exception {

		List<MandadoJudicialDt> listaStatus = new ArrayList<MandadoJudicialDt>();
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try {
			MandadoJudicialPs obPersistencia = new MandadoJudicialPs(obFabricaConexao.getConexao());
			listaStatus = obPersistencia.consultaMandJudPagamentoStatus();
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return listaStatus;
	}
	
	public List<MandadoJudicialDt> consultaMandJudPagamentoStatusTelaAnaliseGratuitos() throws Exception {

		List<MandadoJudicialDt> listaStatus = new ArrayList<MandadoJudicialDt>();
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try {
			MandadoJudicialPs obPersistencia = new MandadoJudicialPs(obFabricaConexao.getConexao());
			listaStatus = obPersistencia.consultaMandJudPagamentoStatusTelaAnaliseGratuitos();
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return listaStatus;
	}
	
	public void alteraPagamentoStatus(String dataInicial, String dataFinal, String idUsuario) throws Exception {
		FabricaConexao obFabricaConexao = null;
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);		
		try {		
		MandadoJudicialPs mandadoJudicialPs = new MandadoJudicialPs(obFabricaConexao.getConexao());
			
		mandadoJudicialPs.alteraPagamentoStatus(dataInicial, dataFinal, idUsuario);		
		
		} catch(Exception e) {
			obFabricaConexao.cancelarTransacao();
		} finally {
			obFabricaConexao.fecharConexao();
		}
	}
	
	public void alteraPagamentoStatus(MandadoJudicialDt mandadoJudicialDt) throws Exception {
		FabricaConexao obFabricaConexao = null;
		LogDt obLogDt;
		String idCompanheiro = null;
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		obFabricaConexao.iniciarTransacao();
		
		try {	
		  MandadoJudicialPs mandadoJudicialPs = new MandadoJudicialPs(obFabricaConexao.getConexao());
		  
			
		//Verificar se houve diminuição de locomoção  
		
		int qtdLocomocaoOriginal = mandadoJudicialPs.retornaQtdLocomocaoVinculada(mandadoJudicialDt.getId());;
		int qtdLocomocaoDesejada = Funcoes.StringToInt(mandadoJudicialDt.getQuantidadeLocomocao());
		boolean isLiberado = true;
		
		//Verifica a consistência do oficial companheiro para mandados onde é obrigatório
		idCompanheiro = mandadoJudicialPs.consultaIdCompanheiroPorIdMandado(mandadoJudicialDt.getId());
		if(qtdLocomocaoOriginal >= MandadoJudicialDt.QTD_LOCOMOCAO_OFICIAL_COMPANHEIRO && Funcoes.StringToInt(idCompanheiro) == 0){
			throw new MensagemException("Para este mandado é necessário que haja um oficial companheiro cadastrado. Caso este tipo não exija oficial companheiro, entre em contato com o suporte para esclarecimento.");
		}
		
		if(qtdLocomocaoOriginal != qtdLocomocaoDesejada) 
    		isLiberado = this.liberarLocomocao(mandadoJudicialDt.getId(),  qtdLocomocaoOriginal - qtdLocomocaoDesejada,  mandadoJudicialDt.getId_UsuarioLog(), obFabricaConexao);
	   	if(!isLiberado)  
			throw new MensagemException("Erro a liberar locomoções do mandado.");
	   	else {
	      	mandadoJudicialPs.alteraPagamentoStatus(mandadoJudicialDt, Funcoes.FormatarData(new Date()));
		    mandadoJudicialPs.alterarStatusMandadoJudicial(mandadoJudicialDt);  /////////////////////melhorar este log  igual aos outros        
		    obLogDt = new LogDt("MandadoJudicial", mandadoJudicialDt.getId(), mandadoJudicialDt.getId_UsuarioLog(),
		    mandadoJudicialDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar), "",
		    mandadoJudicialDt.getPropriedades());
	        obLog.salvar(obLogDt, obFabricaConexao);
			
	        obFabricaConexao.finalizarTransacao();
 		  }		
		} catch(Exception e) {
			obFabricaConexao.cancelarTransacao();
			throw e;
		} finally {
			obFabricaConexao.fecharConexao();
		}		
	}

	public void alteraPagamentoStatusGratuito(MandadoJudicialDt mandadoJudicialDt) throws Exception {
		FabricaConexao obFabricaConexao = null;
		LogDt obLogDt;
		String idCompanheiro = null;
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		obFabricaConexao.iniciarTransacao();
		
		try {	
			MandadoJudicialPs mandadoJudicialPs = new MandadoJudicialPs(obFabricaConexao.getConexao());
		  
			if(Funcoes.StringToInt(mandadoJudicialDt.getResolutivo()) != MandadoJudicialDt.SIM_RESOLUTIVO && Funcoes.StringToInt(mandadoJudicialDt.getResolutivo()) != MandadoJudicialDt.NAO_RESOLUTIVO){
				throw new MensagemException("É obrigatório informar se o mandado foi resolutivo ou não.");
			}
		   	else {
		      	mandadoJudicialPs.alteraPagamentoStatus(mandadoJudicialDt, Funcoes.FormatarData(new Date()));
			    mandadoJudicialPs.alterarStatusMandadoJudicial(mandadoJudicialDt);  /////////////////////melhorar este log  igual aos outros        
			    
			    obLogDt = new LogDt("MandadoJudicial", mandadoJudicialDt.getId(), mandadoJudicialDt.getId_UsuarioLog(), mandadoJudicialDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar), "", mandadoJudicialDt.getPropriedades());
		        obLog.salvar(obLogDt, obFabricaConexao);
				
		        obFabricaConexao.finalizarTransacao();
	 		  }		
		} catch(Exception e) {
			obFabricaConexao.cancelarTransacao();
			throw e;
		} finally {
			obFabricaConexao.fecharConexao();
		}		
	}
	
	/**
	 * Método que retorna o conteúdo da certidão preenchida pelo oficial no momento
	 * da conclusão do mandado.
	 * 
	 * @param idPendencia
	 * @param usuarioSessao
	 * @return
	 * @throws Exception
	 */
	public String[] retornarConteudoArquivoMandado(String idPendencia, UsuarioNe usuarioSessao) throws Exception {
		ArquivoNe arqNe = new ArquivoNe();
		String html[] = null;
		
		PendenciaArquivoNe pendenciaArquivoNe = new PendenciaArquivoNe();
		List<ArquivoDt> listaArq = (List<ArquivoDt>) pendenciaArquivoNe.consultarArquivosPendenciaFinalizada(idPendencia);
		
		if(listaArq == null) {
			listaArq = (List<ArquivoDt>) pendenciaArquivoNe.consultarArquivosPendencia(idPendencia);
		}
		
		if (listaArq != null) {
			html = new String[listaArq.size()];
			LogDt logDt = new LogDt();
			logDt.setId_Usuario(usuarioSessao.getId_Usuario());
			
			int i = 0;
			for (ArquivoDt arq: listaArq) {
					arq = arqNe.consultarId(arq.getId());
					if(arq.isArquivoPodeSerPDF()) {
						html[i] = "<a target='_blank' href='MandadoJudicial?PaginaAtual=6&Fluxo=verCertidao&a="+arq.getId()+"'>" + arq.getNomeArquivo() + "</a>";
					} 
					else if(arq.isArquivoPodeSerHtml()) {
						html[i] = arqNe.retornarConteudoArquivo(arq.getId(), logDt);
					}
					else {
						html[i] = arq.getNomeArquivo() + " (ARQUIVO EM FORMATO DESCONHECIDO)";
					}
					i++;
			}
		}
		return html;
	}
	
	public void baixaArquivoMandado(String idMandado, UsuarioNe usuarioSessao, HttpServletResponse response) throws Exception {
		ArquivoNe arqNe = new ArquivoNe();
		
		LogDt logDt = new LogDt();
		logDt.setId_Usuario(usuarioSessao.getId_Usuario());
		
		arqNe.baixarArquivo(idMandado, response, logDt, false);

		
	}
	
	/**
	 * Método para liberar locomocao que está reservada para mandado judicial.
	 * @param String idMandadoJudicial
	 * @param String idUsuarioLog
	 * @param FabricaConexao obFabricaConexao
	 * 
	 * @return boolean
	 * 
	 * @throws Exception 
	 */
	public boolean liberarLocomocao(String idMandadoJudicial, int qtdLiberar, String idUsuarioLog, FabricaConexao obFabricaConexao) throws Exception {
		LocomocaoNe locomocaoNe = new LocomocaoNe();
		return locomocaoNe.liberarLocomocao(idMandadoJudicial, qtdLiberar, idUsuarioLog, obFabricaConexao);
	}
	
	/**
	 * Retorna o ID do mandado judicial consultando pelo ID da pendência.
	 * @param idPendencia
	 * @param obFabricaConexao
	 * @return
	 * @throws Exception
	 * @author hrrosa
	 */
	public String consultaIdMandadoPorIdPendencia(String idPendencia, FabricaConexao obFabricaConexao) throws Exception {
		return new MandadoJudicialPs(obFabricaConexao.getConexao()).consultaIdMandadoPorIdPendencia(idPendencia);
	}
	
	/**
	 * Retorna o próximo número de mandado (id da tabela mand_jud) do sequence.
	 * @return String
	 * @throws Exception
	 */
	public String reservarNumeroProximoMandado() throws Exception {
		String proxNumeroMandado = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try {
			MandadoJudicialPs mandadoJudicialPs = new MandadoJudicialPs(obFabricaConexao.getConexao());
			proxNumeroMandado = mandadoJudicialPs.reservarNumeroProximoMandado();
		} finally {
			obFabricaConexao.fecharConexao();
		}
		
		if(proxNumeroMandado == null){
			throw new MensagemException("Erro: Impossível obter o próximo número de mandado.");
		}
		
		return proxNumeroMandado;
	}
	
	/**
	 * Retorna o processoDt completo recebendo como parâmetro o id da pendência.
	 * @param idMandJud
	 * @return
	 * @throws Exception
	 * @author hrrosa
	 */
	public ProcessoDt retornarProcessoDtPorIdPend(String idPend) throws Exception {
		ProcessoNe processoNe = new ProcessoNe();
		PendenciaNe pendenciaNe = new PendenciaNe();
		ProcessoDt processoDt = null;
		PendenciaDt pendenciaDt = null;
		
		if(Funcoes.StringToInt(idPend) == 0) {
			throw new MensagemException("Não foi possível identificar a pendência do mandado.");
		}
		
		//TODO: Verificar se é possível otimizar a consulta do processoDt.
		pendenciaDt = pendenciaNe.consultarFinalizadaId(idPend);
		
		if(pendenciaDt == null) {
			pendenciaDt = pendenciaNe.consultarId(idPend);
		}
		
		processoDt = processoNe.consultarId(pendenciaDt.getId_Processo());
		
		return processoDt;
	}
	
	 /**
     * Retorna a data válida após contagem de prazo passado, e a partir da data
     * passada.
     * 
     * @param datainicial:
     *            data para início de contagem de prazo (data de leitura da
     *            intimação, por exemplo)
     * @param prazo:
     *            prazo para cumprimento
     * @param id_comarca:
     *            identificação da comarca
     * @param id_cidade:
     *            identificação da cidade
     * @param id_serventia:
     *            identificação da serventia
     * 
     * @author hrrosa
     */
    public String getPrazoMandadoDiasUteis(Date datainicial, int prazo, String idServentia, FabricaConexao fabricaConexao) throws Exception {
        String stRetorno = "";
        FabricaConexao obFabricaConexao = null;
        Calendar calCalendario = Calendar.getInstance();

        
        if(fabricaConexao == null) {
        	obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
        } else {
        	obFabricaConexao = fabricaConexao;
        }
        
        try {
	        // Atribue a data inicial
	        calCalendario.setTime(datainicial);
	        
	        ServentiaDt serventiaDt = new ServentiaPs(obFabricaConexao.getConexao()).consultarId(idServentia);
	        
	        if(serventiaDt == null){
	        	throw new MensagemException("Erro ao identificar serventia para obter o prazo do mandado");
	        }
	
	        boolean boTeste = false;
	        PrazoSuspensoPs obPersistencia = new PrazoSuspensoPs(obFabricaConexao.getConexao());
	        int intAux = 0;
	
	        do{
	        	// Proximo dia, a contagem se inicia no dia seguinte
	            calCalendario.add(Calendar.DATE, 1);
	            // Verifico se não é sábado ou domigo, passando para  segunda-feira
	            this.passouParaSegundaFeira(calCalendario);
	            // verifico se esta com prazo suspenso ou feriado
	            boTeste = obPersistencia.isDataValidaMandadoJudicialCentral(calCalendario.getTime(), serventiaDt);
	                                    
	            if (boTeste) {                           
	                intAux++;                                
	            }                        
	                        
	        }while(intAux < prazo);
	
	        stRetorno = Funcoes.dateToStringSoData(calCalendario.getTime())+" 23:59:59";

        }
        catch(Exception e) {
        	if(fabricaConexao == null)
        		obFabricaConexao.fecharConexao();
        	throw e;
        }
        finally {
        	if(fabricaConexao == null)
        		obFabricaConexao.fecharConexao();
		}
        
        return stRetorno;
    }
    
    /**
     * Método que verifica se determinada data é sabado ou domingo, retornando
     * assim o próximo dia útil.
     */
    private boolean passouParaSegundaFeira(Calendar calCalendario) {
        boolean boRetorno = false;
        // verico se o proximo dia é sabado
        if (calCalendario.get(Calendar.DAY_OF_WEEK) == 7) {
            // se for sabado passo para segunda, acrescentado 2 dias
            calCalendario.add(Calendar.DATE, 2);
            boRetorno = true;
        } else if (calCalendario.get(Calendar.DAY_OF_WEEK) == 1) {
            // se for domigo passo para segunda, acrescentado 1 dias
            calCalendario.add(Calendar.DATE, 1);
            boRetorno = true;
        }
        return boRetorno;
    }
    
    /**
     * De acordo com os parâmentos recebidos, retorna uma data (String) que é o prazo limite para a conclusão do mandado judicial.
     * Este método utilizou o PrazoSuspensoNe e sua definição de prazo processual como referência mas precisou ser alterado para
     * atender às especificidades da Central de Mandados.
     * @param codigoPrazoMandado
     * @param prazoEspecial
     * @param idServentiaExpedir
     * @param obFabricaConexao
     * @return
     * @throws Exception
     */
    public String retornaPrazoLimiteMandado(String codigoPrazoMandado, String prazoEspecial, String idServentiaExpedir, FabricaConexao obFabricaConexao) throws Exception {
    	int prazoMandadoDias = 0;
    	String retornoPrazoData = null;
    	if(codigoPrazoMandado != null) {
			if(MandadoTipoDt.isTipoEspecial(codigoPrazoMandado)){
				prazoMandadoDias = Funcoes.StringToInt(prazoEspecial);
			}
			else{
				prazoMandadoDias = Funcoes.StringToInt(MandadoTipoDt.getDiasPrazo(codigoPrazoMandado));
			}
		}
		if(prazoMandadoDias != 0){
			
			retornoPrazoData = this.getPrazoMandadoDiasUteis(new Date(), prazoMandadoDias, idServentiaExpedir, null);
		
		} else {
			throw new MensagemException("Erro ao determinar o prazo do mandado.");
		}

		return retornoPrazoData;
    }
   
    public MandadoJudicialDt consultaMandado(String idMandJud, String idUsuarioServentiaSessao, String codigoGrupoSessao, String idServentiaSessao ) throws Exception {
		
    	MandadoJudicialDt mandadoJudicialDt = new MandadoJudicialDt();    	
    	
    	int idUsuarioServentia;

    

		int idServentia;

		if (codigoGrupoSessao.equalsIgnoreCase(Integer.toString(GrupoDt.ANALISTA_FINANCEIRO))) {
			idUsuarioServentia = 0;
			idServentia = 0;			
		} else {
			if (codigoGrupoSessao.equalsIgnoreCase(Integer.toString(GrupoDt.OFICIAL_JUSTICA))) {
				idUsuarioServentia = Integer.parseInt(idUsuarioServentiaSessao);
				idServentia = 0;
			} else {
				if (codigoGrupoSessao.equalsIgnoreCase(Integer.toString(GrupoDt.COORDENADOR_CENTRAL_MANDADO))) {
					idServentia = Integer.parseInt(idServentiaSessao);
					idUsuarioServentia = 0;
				} else {
					return mandadoJudicialDt;
				}
			}
		}    	
    	
    	FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
    	
    	try{
			MandadoJudicialPs obPersistencia = new MandadoJudicialPs(obFabricaConexao.getConexao());
			mandadoJudicialDt = obPersistencia.consultaMandado(idMandJud, idServentia, idUsuarioServentia);
			mandadoJudicialDt = this.calculaValorLocomocao(mandadoJudicialDt);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return mandadoJudicialDt;
	}


/**
	 * Retorna a quantidade de mandados em aberto com oficial, reservados para ele.
	 * 
     * @param idUsuServOficial
     * @return
     * @throws Exception
     * @author hrrosa
     */
    public long retornaQtdMandadosAbertosReservadosOficial(String idUsuServOficial) throws Exception {
    	long qtd = 0;
    	
    	if(Funcoes.StringToInt(idUsuServOficial) == 0) {
    		throw new MensagemException("Erro ao identificar o parâmetro do oficial");
    	}
    	
    	FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
    	try {
    		qtd = new MandadoJudicialPs(obFabricaConexao.getConexao()).retornaQtdMandadosAbertosReservadosOficial(idUsuServOficial);
    	}
    	finally {
    		obFabricaConexao.fecharConexao();
    	}
    	return qtd;
    }


}
