package br.gov.go.tj.projudi.ne;


import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.PendenciaResponsavelHistoricoDt;
import br.gov.go.tj.projudi.ps.PendenciaResponsavelHistoricoPs;
import br.gov.go.tj.utils.FabricaConexao;

//---------------------------------------------------------
public class PendenciaResponsavelHistoricoNe extends PendenciaResponsavelHistoricoNeGen{

//

/**
	 * 
	 */
	private static final long serialVersionUID = 5742055590579680777L;

	//---------------------------------------------------------
	public  String Verificar(PendenciaResponsavelHistoricoDt dados ) {

		String stRetorno="";

		if (dados.getPendenciaResponsavelHistorico().length()==0)
			stRetorno += "O Campo PendenciaResponsavelHistorico é obrigatório.";
		if (dados.getPendendencia().length()==0)
			stRetorno += "O Campo Pendendencia é obrigatório.";
		if (dados.getServentiaCargo().length()==0)
			stRetorno += "O Campo ServentiaCargo é obrigatório.";
		if (dados.getDataInicio().length()==0)
			stRetorno += "O Campo DataInicio é obrigatório.";

		return stRetorno;

	}
	
	public void salvar(PendenciaResponsavelHistoricoDt dados, FabricaConexao conexao ) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = conexao;
		PendenciaResponsavelHistoricoPs obPersistencia = new  PendenciaResponsavelHistoricoPs(obFabricaConexao.getConexao()); 

		/* use o iu do objeto para saber se os dados ja estão ou não salvos */
		if (dados.getId().length()==0) {
			obPersistencia.inserir(dados);
			obLogDt = new LogDt("PendenciaResponsavelHistorico",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
		}else {
			obPersistencia.alterar(dados); 
			obLogDt = new LogDt("PendenciaResponsavelHistorico",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
		}

		obDados.copiar(dados);
		obLog.salvar(obLogDt, obFabricaConexao);

	
	}
	
	public void fecharHistorico(String id_Pendencia, String data_Fim, String id_UsuarioLog, String IpComputadorLog, FabricaConexao conexao ) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;

	    obFabricaConexao = conexao;
		PendenciaResponsavelHistoricoPs obPersistencia = new  PendenciaResponsavelHistoricoPs(obFabricaConexao.getConexao()); 
		obPersistencia.fecharHistorico(id_Pendencia, data_Fim);
		obLogDt = new LogDt("PendenciaResponsavelHistorico",id_Pendencia, id_UsuarioLog, IpComputadorLog, String.valueOf(LogTipoDt.Alterar),"Data_Fim:","Data_Fim:"+data_Fim);
		obLog.salvar(obLogDt, obFabricaConexao);
	
	}

	public void atualizaHistoricoPendenciaFilha(String idPendenciaPai,String idPendenciaFilha, String id_UsuarioLog, String IpComputadorLog, FabricaConexao conexao) throws Exception {
		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = conexao;
		PendenciaResponsavelHistoricoPs obPersistencia = new  PendenciaResponsavelHistoricoPs(obFabricaConexao.getConexao()); 
		obPersistencia.atualizaHistoricoPendenciaFilha(idPendenciaPai, idPendenciaFilha);
		obLogDt = new LogDt("PendenciaResponsavelHistorico",idPendenciaFilha, id_UsuarioLog, IpComputadorLog, String.valueOf(LogTipoDt.Alterar),"idPendenciaPai: "+idPendenciaPai,"idPendenciaFilha: "+idPendenciaFilha);
		obLog.salvar(obLogDt, obFabricaConexao);
	}
	
	
	public List consultarHistoricosPendenciaFinal(String id_Pendencia) throws Exception {
		List tempList=null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			PendenciaResponsavelHistoricoPs obPersistencia = new  PendenciaResponsavelHistoricoPs(obFabricaConexao.getConexao()); 

			tempList=obPersistencia.consultarHistoricosPendenciaFinal(id_Pendencia);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return tempList;   
	}

	public void salvarHistorioco(String id_pendencia, String data_inicio, String id_serventiaCargo,String id_serventiaGrupo, String id_usuarioLog, String ip_computador, FabricaConexao obFabricaConexao) throws Exception {
		PendenciaResponsavelHistoricoDt pendenciaResponsavelHistoricoDistribuidorDt   = new PendenciaResponsavelHistoricoDt();
		pendenciaResponsavelHistoricoDistribuidorDt.setId_Pendencia(id_pendencia);
		pendenciaResponsavelHistoricoDistribuidorDt.setDataInicio(data_inicio);
		pendenciaResponsavelHistoricoDistribuidorDt.setId_ServentiaCargo(id_serventiaCargo);
		pendenciaResponsavelHistoricoDistribuidorDt.setId_ServentiaGrupo(id_serventiaGrupo);
		pendenciaResponsavelHistoricoDistribuidorDt.setId_UsuarioLog(id_usuarioLog);
		pendenciaResponsavelHistoricoDistribuidorDt.setIpComputadorLog(ip_computador);
		salvar(pendenciaResponsavelHistoricoDistribuidorDt, obFabricaConexao);
		
	}
	
	/**
	 * Método que realiza consulta do último histórico de conclusão aberto.
	 * @param id_Pendencia - ID da pendência
	 * @return dt com a informação do histórico
	 * @throws Exception
	 * @author hmgodinho
	 */
	public PendenciaResponsavelHistoricoDt consultarHistoricoAbertoPendencia(String id_Pendencia) throws Exception {
		PendenciaResponsavelHistoricoDt dtRetorno=null;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			PendenciaResponsavelHistoricoPs obPersistencia = new  PendenciaResponsavelHistoricoPs(obFabricaConexao.getConexao()); 

			dtRetorno= obPersistencia.consultarHistoricoAbertoPendencia(id_Pendencia); 
			obDados.copiar(dtRetorno);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}

	public PendenciaResponsavelHistoricoDt consultarResponsavelAnteriorPendencia(String id_Pendencia) throws Exception {
		PendenciaResponsavelHistoricoDt dtRetorno=null;
		FabricaConexao obFabricaConexao = null;
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			PendenciaResponsavelHistoricoPs obPersistencia = new  PendenciaResponsavelHistoricoPs(obFabricaConexao.getConexao()); 

			dtRetorno= obPersistencia.consultarResponsavelAnteriorPendencia(id_Pendencia); 
			obDados.copiar(dtRetorno);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}
}
