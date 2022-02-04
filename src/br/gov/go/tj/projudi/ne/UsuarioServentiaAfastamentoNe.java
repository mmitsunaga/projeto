package br.gov.go.tj.projudi.ne;

import java.util.Date;
import java.util.List;

import org.hamcrest.core.Is;

import java.util.ArrayList;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.MandadoJudicialDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaAfastamentoDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaDt;
import br.gov.go.tj.projudi.ps.UsuarioServentiaAfastamentoPs;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;

public class UsuarioServentiaAfastamentoNe extends UsuarioServentiaAfastamentoNeGen {

	private static final long serialVersionUID = -8082686745213464336L;

	public String Verificar(UsuarioServentiaAfastamentoDt dados) {

		String stRetorno = "";

		if (dados.getId_UsuarioServentia().equalsIgnoreCase(""))
			stRetorno += "O Campo Usuario � obrigat�rio.";
		if (dados.getId_Afastamento().equalsIgnoreCase(""))
			stRetorno += "O Campo Afastamento � obrigat�rio.";

		return stRetorno;

	}

	public String consultarDescricaoJSON(String descricao, String posicao) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			UsuarioServentiaAfastamentoPs obPersistencia = new  UsuarioServentiaAfastamentoPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarDescricaoJSON(descricao, posicao);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}
	
	public String consultarDescricaoAfastamentosAbertosJSON(String descricao, String posicao, String id_serv) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			UsuarioServentiaAfastamentoPs obPersistencia = new  UsuarioServentiaAfastamentoPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarDescricaoAfastamentosAbertosJSON(descricao, posicao, id_serv);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}
	
	public String consultarDescricaoUsuarioJSON(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		String stTemp = "";
		
		UsuarioNe Usuarione = new UsuarioNe(); 
		stTemp = Usuarione.consultarDescricaoJSON(tempNomeBusca, PosicaoPaginaAtual);
		
		return stTemp;
	}

	public String consultarDescricaoAfastamentoJSON(String tempNomeBusca, String PosicaoPaginaAtual) throws Exception {
		String stTemp = "";
		
		AfastamentoNe afastamentoNe = new AfastamentoNe();
		stTemp = afastamentoNe.consultarDescricaoJSON(tempNomeBusca, PosicaoPaginaAtual);
		
		return stTemp;
	}
	
	/**
	 * Retira o afastamento de um usu�rio. Criada para que o coordanador da central de mandados retire
	 * o afastamento de um oficial de justi�a.
	 * @param usuServAfastamentoDt
	 * @param idUsuServFinalizador
	 * @author hrrosa
	 * @throws Exception
	 */
	public String desafastar(UsuarioServentiaAfastamentoDt  usuServAfastamentoDt, String idUsuServFinalizador) throws Exception {
		String mensagem = null;
		FabricaConexao obFabricaConexao = null;
		try{
			
			mensagem = this.verificarDesafastamento(usuServAfastamentoDt);
			
			if(mensagem == null) {
				
				obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
				obFabricaConexao.iniciarTransacao();
				try {
					ServentiaCargoEscalaNe servCargoEscala = new ServentiaCargoEscalaNe();
					servCargoEscala.ativaServCargoTodasEscalas(usuServAfastamentoDt.getId_UsuarioServentia(), obFabricaConexao);
					
					usuServAfastamentoDt.setDataFim(Funcoes.DataHora(new Date()));
					usuServAfastamentoDt.setId_UsuServFinalizador(idUsuServFinalizador);
					// TODO: Verificar se � a melhor op��o usar este m�todo "salvar" abaixo.
					this.salvar(usuServAfastamentoDt);
					
					obFabricaConexao.finalizarTransacao();
				} catch (Exception e) {
					obFabricaConexao.cancelarTransacao();
					throw e;
				}
			}
			
			return mensagem;
		} finally{
			if(obFabricaConexao != null) {
				obFabricaConexao.fecharConexao();
			}
		}
	}
	
	public String verificarDesafastamento(UsuarioServentiaAfastamentoDt  usuServAfastamentoDt) throws Exception {
		String mensagem = null;

		if(usuServAfastamentoDt == null || usuServAfastamentoDt.getId() == null || usuServAfastamentoDt.getId().isEmpty()){
			mensagem = "N�o foi poss�vel registrar o afastamento. Dados inv�lidos.";
		}
		if(usuServAfastamentoDt.getMotivoFim() == null || usuServAfastamentoDt.getMotivoFim().isEmpty() ){
			mensagem = "Especifique o motivo do fim do afastamento";
		}
		
		return mensagem;
	}
	
	/**
	 * JOB - Registra o afastamento dos oficiais que n�o tiverem cumprido o prazo de algum mandado.
	 * Modifica o status da escala dos oficiais para "inativo" e inclui um registro na usu_serv_afastamento
	 * identificando o motivo da suspens�o.
	 * @throws Exception
	 * @author hrrosa
	 */
	public void afastarOficiaisAtrasados() throws Exception {
		List<MandadoJudicialDt> listaMandadosAtrasados = null;
		
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			
			UsuarioServentiaAfastamentoPs usuAfastamentoPs = new UsuarioServentiaAfastamentoPs(obFabricaConexao.getConexao());
			
			MandadoJudicialNe mandadoJudicialNe = new MandadoJudicialNe();
			ServentiaCargoEscalaNe servCargoEscNe = new ServentiaCargoEscalaNe();
			String idUsuServ1Temp = null;
			String mandJudExpirados = "";
			
			listaMandadosAtrasados = mandadoJudicialNe.consultarMandadosAtrasadosExecucaoAutomatica(obFabricaConexao);
			// A lista vir� ordenada pelo id_serv do oficial. Portanto, percorre a lista acumulando os n�meros de mandados
			// que causaram a suspens�o. Quando muda o id do oficial, suspende e come�a a acumular a lista para o novo
			// oficial em quest�o.
			if(listaMandadosAtrasados != null) {
				
				for(MandadoJudicialDt mandJudDt: listaMandadosAtrasados){
					// TODO: Otimizar este la�o
					if(!mandJudDt.getId_UsuarioServentia_1().equals(idUsuServ1Temp) && idUsuServ1Temp != null || listaMandadosAtrasados.indexOf(mandJudDt) == listaMandadosAtrasados.size()-1) {
						
						if(listaMandadosAtrasados.indexOf(mandJudDt) == listaMandadosAtrasados.size()-1){
							mandJudExpirados += mandJudDt.getId() + ";";
							idUsuServ1Temp = mandJudDt.getId_UsuarioServentia_1();
						}
						
						servCargoEscNe.desativaServCargoTodasEscalas(idUsuServ1Temp, obFabricaConexao);
						usuAfastamentoPs.registrarUsuarioAfastamento(idUsuServ1Temp, UsuarioDt.SistemaProjudi, mandJudExpirados);
	
						obLog.salvar(new LogDt("UsuarioAfastamento", idUsuServ1Temp, UsuarioServentiaDt.SistemaProjudi, "Servidor", String.valueOf(LogTipoDt.OficialAfastado), "", "Oficial id_usu_serv="+idUsuServ1Temp+" afastado. Mandados expirados id: " + mandJudExpirados), obFabricaConexao);
						
						idUsuServ1Temp = mandJudDt.getId_UsuarioServentia_1();
						
						//sobrescreve e come�a novamente a lista
						mandJudExpirados = mandJudDt.getId() + ";";
					
					} else {
						mandJudExpirados += mandJudDt.getId() + ";";
						idUsuServ1Temp = mandJudDt.getId_UsuarioServentia_1();
					}
				}
			
			}
			
			obFabricaConexao.finalizarTransacao();
		} catch (Exception e) {
			obFabricaConexao.cancelarTransacao();
			throw e;
		} finally{
			obFabricaConexao.fecharConexao();
		}
		
	}
	
	/**
	 * Retorna a quantidade de afastamentos em aberto para a serventia. Usado no quantitativo da
	 * p�gina inicial do Coordenador da Central de Mandado
	 * @param idServ
	 * @return String
	 * @author hrrosa
	 */
	public String consultarQuantidadeAfastamentosAbertos(String idServ) throws Exception {
		String qtd = null;
		FabricaConexao obFabricaConexao = null;
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            UsuarioServentiaAfastamentoPs usuServAfastamentoPs = new UsuarioServentiaAfastamentoPs(obFabricaConexao.getConexao());
            qtd =  usuServAfastamentoPs.consultarQuantidadeAfastamentosAbertos(idServ);
        } finally {
            obFabricaConexao.fecharConexao();
        }
		return qtd; 
	}
	/**
	 * Retorna lista de afastamentos por oficial e serventia.
	 * 
	 * @param idUsuario
	 * @param idServentia
	 * @return List
	 * @author Fernando Meireles
	 */

	public List consultarAfastamentoPorOficialServentia(String idUsuario, String idServentia) throws Exception {
		String qtd = null;
		List listaOficialAfastamento = new ArrayList();
		UsuarioServentiaAfastamentoDt objDt;
		FabricaConexao obFabricaConexao = null;
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			UsuarioServentiaAfastamentoPs usuServAfastamentoPs = new UsuarioServentiaAfastamentoPs(
					obFabricaConexao.getConexao());
			listaOficialAfastamento = usuServAfastamentoPs.consultaAfastamentoPorOficialServentia(idUsuario,
					idServentia);
			if (listaOficialAfastamento.isEmpty()) {
				UsuarioDt usuarioDt;
				UsuarioNe usuarioNe = new UsuarioNe();
				usuarioDt = usuarioNe.consultarId(idUsuario);
				if (usuarioDt == null) {
					throw new Exception("Usuario n�o encontrado!");
				}
				objDt = new UsuarioServentiaAfastamentoDt();
				objDt.setNomeUsuario(usuarioDt.getNome());
				listaOficialAfastamento.add(objDt);
			}			
			
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return listaOficialAfastamento;
	}

	/**
	 * 
	 * Retorna oficial de uma serventia com afastamento aberto. Quando nao tiver
	 * afastamento aberto, retorna um registro com o id, nome e cpf do oficial
	 * 
	 * 
	 * @param idUsuario
	 * @param idServentia
	 * @return Objeto
	 * @author Fernando Meireles
	 */

	public UsuarioServentiaAfastamentoDt consultarAfastamentoAbertoPorOficialServentia(String idUsuario,
			String idServentia) throws Exception {
		UsuarioServentiaAfastamentoDt objDt;
		FabricaConexao obFabricaConexao = null;
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			UsuarioServentiaAfastamentoPs usuServAfastamentoPs = new UsuarioServentiaAfastamentoPs(
					obFabricaConexao.getConexao());
			objDt = usuServAfastamentoPs.consultarAfastamentoAbertoPorOficialServentia(idUsuario, idServentia);
			if (objDt == null) {
				UsuarioDt usuarioDt;
				UsuarioNe usuarioNe = new UsuarioNe();
				usuarioDt = usuarioNe.consultarId(idUsuario);
				if (usuarioDt == null) {
					throw new Exception("Usuario n�o encontrado!");
				}
				objDt = new UsuarioServentiaAfastamentoDt();
				objDt.setId_Usuario(idUsuario);
				objDt.setNomeUsuario(usuarioDt.getNome());
				objDt.setCpfUsuario(usuarioDt.getCpf());
				objDt.setAcao("Afastar");
			}

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return objDt;
	}

	/**
	 * Afasta / Retorna oficial de justi�a.
	 * 
	 * @param UsuarioServentiaAfastamento
	 * @param idUsuarioSessao
	 * @param idServentiaSessao
	 * @param ipComputador
	 * @author Fernando Meireles
	 * @throws Exception
	 */

	public void afastamentoRetornoRetornaOficial(UsuarioServentiaAfastamentoDt objDt, String idUsuarioServentiaSessao,
			String idServentiaSessao, String ipComputador, String idUsuarioSessao) throws Exception {

		FabricaConexao objFc = null;

		try {

			UsuarioServentiaDt usuarioServentiaDt;
			UsuarioServentiaNe usuarioServentiaNe = new UsuarioServentiaNe();
			usuarioServentiaDt = usuarioServentiaNe.consultaUsuarioServentiaId(objDt.getId_Usuario(), idServentiaSessao);
			if (usuarioServentiaDt == null)
				throw new Exception("Usuario Serventia n�o encontrado!");

			objFc = new FabricaConexao(FabricaConexao.PERSISTENCIA);

			objFc.iniciarTransacao();

			if (objDt.getId_UsuarioServentiaAfastamento().equalsIgnoreCase("")) {
				this.afastamentoOficial(objDt.getId_Afastamento(), usuarioServentiaDt.getId(), objDt.getMotivoInicio(),
						idUsuarioServentiaSessao, idUsuarioSessao,  ipComputador, objFc);
			} else {
				this.retornoOficial(objDt.getId_UsuarioServentiaAfastamento(), usuarioServentiaDt.getId(),
						objDt.getMotivoFim(), idUsuarioServentiaSessao, idUsuarioSessao, ipComputador, objFc);
			}

			objFc.finalizarTransacao();

		} finally {
			if (objFc != null) {
				objFc.fecharConexao();
			}
 		}
	}

	/**
	 * Afasta oficial de justi�a e desativa suas escalas de acordo com o
	 * afastamento.
	 * 
	 * @param idAfastamento
	 * @param idUsuServ
	 * @param idMotivoInicio
	 * @param idUsuCadastrador
	 * @param idComputador
	 * @param fabricaConexao
	 * @author Fernando Meireles
	 * @throws Exception
	 */

	public void afastamentoOficial(String idAfastamento, String idUsuServ, String motivoInicio,
	    String idUsuServCadastrador,  String idUsuarioSessao, String ipComputador, FabricaConexao  objFc) throws Exception {

		String tipoAfastamento = "";

		ServentiaCargoEscalaNe serventiaCargoEscalaNe = new ServentiaCargoEscalaNe();

		serventiaCargoEscalaNe.desativaServCargoEscAfastamento(idUsuServ, idAfastamento, objFc);

		UsuarioServentiaAfastamentoPs usuarioServentiaAfastamentoPs = new UsuarioServentiaAfastamentoPs(
				objFc.getConexao());

		String id = usuarioServentiaAfastamentoPs.afastamentoOficial(idAfastamento, idUsuServ, motivoInicio, idUsuServCadastrador);

		obLog.salvar(new LogDt("UsuarioServentiaAfastamento", id, idUsuarioSessao, ipComputador,
				String.valueOf(LogTipoDt.OficialAfastado), "",
				"Id Usuario Serventia = " + idUsuServ + "Id Afastamento = " + idAfastamento), objFc);
	}

	/**
	 * Retorna oficial de justi�a afastado e ativa todas as suas escalas.
	 * 
	 * @param idUsuarioServentiaAfastamento
	 * @param idUsuServ
	 * @param idMotivoFim
	 * @param idUsuServFinalizador
	 * @param idComputador
	 * @param fabricaConexao
	 * @author Fernando Meireles
	 * @throws Exception
	 */

	public void retornoOficial(String idUsuarioServentiaAfastamento, String idUsuServ, String motivoFim,
		String idUsuServFinalizador, String idUsuarioSessao, String ipComputador,  FabricaConexao objFc) throws Exception {

		ServentiaCargoEscalaNe serventiaCargoEscalaNe = new ServentiaCargoEscalaNe();

		serventiaCargoEscalaNe.ativaServentiaCargoTodasEscalas(idUsuServ, objFc);

		UsuarioServentiaAfastamentoPs usuarioServentiaAfastamentoPs = new UsuarioServentiaAfastamentoPs(
				objFc.getConexao());

		usuarioServentiaAfastamentoPs.retornoUsuario(idUsuarioServentiaAfastamento, idUsuServFinalizador, motivoFim);

		obLog.salvar(new LogDt("UsuarioServentiaAfastamento", idUsuarioServentiaAfastamento, idUsuarioSessao,
				ipComputador, String.valueOf(LogTipoDt.RetornarOficialAfastado), "",
				"Id Usuario Serventia = " + idUsuServ), objFc);
 	}
}
