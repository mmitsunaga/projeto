package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.AudienciaProcessoStatusDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.ServentiaSubtipoDt;
import br.gov.go.tj.projudi.dt.ServentiaTipoDt;
import br.gov.go.tj.projudi.ps.AudienciaProcessoStatusPs;
import br.gov.go.tj.projudi.ps.ServentiaSubtipoPs;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;

public class AudienciaProcessoStatusNe extends AudienciaProcessoStatusNeGen {

	/**
     * 
     */
    private static final long serialVersionUID = -1004823486494888716L;

    /**
	 * @param dados
	 * @return String stRetorno
	 */
	public String Verificar(AudienciaProcessoStatusDt dados) {
		String stRetorno = "";

		if (dados.getAudienciaProcessoStatus().length() == 0) {
			stRetorno += "O campo Status da Audiência de Processo é obrigatório.";
		}
		if (dados.getAudienciaProcessoStatusCodigo().length() == 0) {
			stRetorno += "O campo Código do Status da Audiência de Processo é obrigatório.";
		}
		return stRetorno;
	}

	/**
	 * Método responsável por criar um objeto do tipo "AudienciaProcessoDt", "alimentar" suas propriedades (id, código,
	 * descrição e log) e retorná-lo.
	 * 
	 * @author Keila Sousa Silva
	 * @since 07/08/2009
	 * @param id_AudienciaProcessoStatus
	 * @param audienciaProcessoStatusCodigo
	 * @param audienciaProcessoStatus
	 * @param logDt
	 * @return AudienciaProcessoStatusDt audienciaProcessoStatusDt
	 */
	public AudienciaProcessoStatusDt instanciarAudienciaProcessoStatusDt(String id_AudienciaProcessoStatus, String audienciaProcessoStatusCodigo,
			String audienciaProcessoStatus, LogDt logDt) {
		AudienciaProcessoStatusDt audienciaProcessoStatusDt = null;
		audienciaProcessoStatusDt = new AudienciaProcessoStatusDt(id_AudienciaProcessoStatus, audienciaProcessoStatusCodigo, audienciaProcessoStatus,
				logDt);
		return audienciaProcessoStatusDt;
	}

	/**
	 * Consulta os Status de Audiência disponíveis para o caso de movimentação de Audiências.
	 * 
	 * @param serventiaSubtipoCodigo, tipo do subtipo da serventia para o qual devem ser retornados os status
	 * 
	 * @author msapaula
	 */
	public List consultarAudienciaProcessoStatusMovimentacao(String serventiaSubtipoCodigo) throws Exception {
		List tempList = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			AudienciaProcessoStatusPs obPersistencia = new  AudienciaProcessoStatusPs(obFabricaConexao.getConexao());
			
			//Se consulta está sendo feita a partir de uma Serventia Gabinete deve modificar para a consulta ser feita para o Segundo Grau
			if (ServentiaSubtipoDt.isGabineteSegundoGrau(serventiaSubtipoCodigo) || 
				ServentiaSubtipoDt.isTurma(serventiaSubtipoCodigo) ||
				ServentiaSubtipoDt.isSegundoGrau(serventiaSubtipoCodigo) || 
				ServentiaSubtipoDt.isUPJTurmaRecursal(serventiaSubtipoCodigo) || 
				ServentiaSubtipoDt.isGabineteFluxoUPJ(serventiaSubtipoCodigo)) {
				tempList = obPersistencia.consultarAudienciaProcessoStatusMovimentacao(String.valueOf(ServentiaTipoDt.SEGUNDO_GRAU));
			} else {
				tempList = obPersistencia.consultarAudienciaProcessoStatusMovimentacao(String.valueOf(ServentiaTipoDt.VARA));
			}
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;
	}

	/**
	 * Consulta os Status de Audiência disponíveis de acordo com o tipo da Serventia
	 * 
	 * @param descricao
	 * @param serventiaTipoCodigo
	 * @param posicao
	 * @return
	 * @throws Exception
	 */
	public List consultarAudienciaProcessoStatus(String descricao, String serventiaTipoCodigo, String posicao) throws Exception {
		List tempList = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			AudienciaProcessoStatusPs obPersistencia = new  AudienciaProcessoStatusPs(obFabricaConexao.getConexao());
			tempList = obPersistencia.consultarDescricao(descricao, serventiaTipoCodigo, posicao);
			QuantidadePaginas = (Long) tempList.get(tempList.size() - 1);
			tempList.remove(tempList.size() - 1);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;

	}
	
	
	public String consultarAudienciaProcessoStatusJSON(String descricao, String serventiaTipoCodigo, String posicao) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			AudienciaProcessoStatusPs obPersistencia = new  AudienciaProcessoStatusPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarDescricaoJSON(descricao, serventiaTipoCodigo, posicao);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;   
	}
	
	public String consultarDescricaoJSON(String descricao, String posicao ) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			AudienciaProcessoStatusPs obPersistencia = new  AudienciaProcessoStatusPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarDescricaoJSON(descricao, posicao);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}
	
	/**
	 * Consulta os Status de Audiência disponíveis para o caso de movimentação de Audiências de um determinado código.
	 * 
	 * @param serventiaTipoCodigo, tipo da serventia para o qual devem ser retornados os status
	 * 
	 * @author mmgomes
	 */
	public AudienciaProcessoStatusDt consultarAudienciaProcessoStatusCodigoMovimentacao(String serventiaTipoCodigo, String AudienciaProcessoStatusCodigo) throws Exception {
		FabricaConexao obFabricaConexao =  new FabricaConexao(FabricaConexao.PERSISTENCIA);
		AudienciaProcessoStatusDt audienciaProcessoStatusDt = null;
	
		try{

			AudienciaProcessoStatusPs obPersistencia = new  AudienciaProcessoStatusPs(obFabricaConexao.getConexao());
			
			//Se consulta está sendo feita a partir de uma Serventia Gabinete deve modificar para a consulta ser feita para o Segundo Grau
			if (Funcoes.StringToInt(serventiaTipoCodigo) == ServentiaTipoDt.GABINETE) {
				serventiaTipoCodigo = String.valueOf(ServentiaTipoDt.SEGUNDO_GRAU); 
			}
			audienciaProcessoStatusDt = obPersistencia.consultarAudienciaProcessoStatusCodigoMovimentacao(serventiaTipoCodigo, AudienciaProcessoStatusCodigo);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return audienciaProcessoStatusDt;
	}

	public String consultarDescricaoServentiaTipoJSON(String tempNomeBusca, String posicaoPaginaAtual) throws Exception {
		String stTemp = "";
		ServentiaTipoNe neObjeto = new ServentiaTipoNe();
		
		stTemp = neObjeto.consultarDescricaoJSON(tempNomeBusca);
		
		neObjeto = null;
		return stTemp;
	}
	
	/**
	 * Método que contém a SQL responsável por consultar um objeto do tipo "AUDI_PROC_STATUS" dado o id do
	 * status da audiência de processo.
	 * 
	 * @author hrrosa
	 * @param audienciaProcessoStatusId
	 * @return AudienciaProcessoStatusDt audienciaProcessoStatusDt
	 * @throws Exception
	 */
	public AudienciaProcessoStatusDt consultarAudienciaProcessoStatusCodigo(int audienciaProcessoStatusId) throws Exception {
		AudienciaProcessoStatusDt audienciaProcessoStatusDt = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			AudienciaProcessoStatusPs obPersistencia = new  AudienciaProcessoStatusPs(obFabricaConexao.getConexao());
			audienciaProcessoStatusDt = obPersistencia.consultarAudienciaProcessoStatusCodigo(audienciaProcessoStatusId);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		
		return audienciaProcessoStatusDt;
	}
	
	/**
	 * Método que contém a SQL responsável por consultar um objeto do tipo "AUDI_PROC_STATUS" dado o codigo do
	 * status da audiência de processo.
	 * 
	 * @author jvosantos
	 * @param audienciaProcessoStatusId
	 * @return AudienciaProcessoStatusDt audienciaProcessoStatusDt
	 * @throws Exception
	 */
	public AudienciaProcessoStatusDt consultarAudienciaProcessoStatusPorCodigo(String codigo) throws Exception {
		AudienciaProcessoStatusDt audienciaProcessoStatusDt = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			AudienciaProcessoStatusPs obPersistencia = new  AudienciaProcessoStatusPs(obFabricaConexao.getConexao());
			audienciaProcessoStatusDt = obPersistencia.consultarAudienciaProcessoStatusPorCodigo(codigo);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		
		return audienciaProcessoStatusDt;
	}
}
