package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.PendenciaResponsavelHistoricoDt;
import br.gov.go.tj.projudi.ps.PendenciaResponsavelHistoricoPs;
import br.gov.go.tj.utils.FabricaConexao;
//---------------------------------------------------------
public abstract class PendenciaResponsavelHistoricoNeGen extends Negocio{


	/**
	 * 
	 */
	private static final long serialVersionUID = 6236933731304395831L;
	protected  PendenciaResponsavelHistoricoDt obDados;


	public PendenciaResponsavelHistoricoNeGen() {

		obLog = new LogNe(); 

		obDados = new PendenciaResponsavelHistoricoDt(); 

	}


//---------------------------------------------------------
	public void salvar(PendenciaResponsavelHistoricoDt dados ) throws Exception {

		LogDt obLogDt;

		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
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
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}


//---------------------------------------------------------
	 public abstract String Verificar(PendenciaResponsavelHistoricoDt dados ); 


//---------------------------------------------------------

	public void excluir(PendenciaResponsavelHistoricoDt dados) throws Exception {

		LogDt obLogDt;

		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			PendenciaResponsavelHistoricoPs obPersistencia = new  PendenciaResponsavelHistoricoPs(obFabricaConexao.getConexao()); 

			obLogDt = new LogDt("PendenciaResponsavelHistorico",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId());

			dados.limpar();
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

 //---------------------------------------------------------

 /**

  * Método para lista as area processuais

 * @author jrcorrea

 */

	public String consultarDescricaoJSON(String descricao, String posicao ) throws Exception {
		FabricaConexao obFabricaConexao = null;
		String stTemp = "";
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			PendenciaResponsavelHistoricoPs obPersistencia = new  PendenciaResponsavelHistoricoPs(obFabricaConexao.getConexao()); 

			stTemp = obPersistencia.consultarDescricaoJSON(descricao, posicao);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}
 //---------------------------------------------------------

	public PendenciaResponsavelHistoricoDt consultarId(String id_pendenciaresponsavelhistorico ) throws Exception {

		PendenciaResponsavelHistoricoDt dtRetorno=null;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			PendenciaResponsavelHistoricoPs obPersistencia = new  PendenciaResponsavelHistoricoPs(obFabricaConexao.getConexao()); 

			dtRetorno= obPersistencia.consultarId(id_pendenciaresponsavelhistorico ); 
			obDados.copiar(dtRetorno);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}

//---------------------------------------------------------

	public long getQuantidadePaginas(){
		return QuantidadePaginas;
	}
//---------------------------------------------------------

	public List consultarDescricao(String descricao, String posicao ) throws Exception {
		List tempList=null;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			PendenciaResponsavelHistoricoPs obPersistencia = new  PendenciaResponsavelHistoricoPs(obFabricaConexao.getConexao()); 

			tempList=obPersistencia.consultarDescricao( descricao, posicao);
			QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
			tempList.remove(tempList.size()-1);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return tempList;   
	}

	public List consultarDescricaoPendencia(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		PendenciaNe Pendenciane = new PendenciaNe(); 
		tempList = Pendenciane.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = Pendenciane.getQuantidadePaginas();
		Pendenciane = null;
		return tempList;
	}

	public List consultarDescricaoServentiaCargo(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		ServentiaCargoNe ServentiaCargone = new ServentiaCargoNe(); 
		tempList = ServentiaCargone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = ServentiaCargone.getQuantidadePaginas();
		ServentiaCargone = null;
		return tempList;
	}
	
	public List consultarHistoricosPendencia(String id_Pendencia) throws Exception {
		List tempList=null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			PendenciaResponsavelHistoricoPs obPersistencia = new  PendenciaResponsavelHistoricoPs(obFabricaConexao.getConexao()); 

			tempList=obPersistencia.consultarHistoricosPendencia(id_Pendencia);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return tempList;   
	}
}