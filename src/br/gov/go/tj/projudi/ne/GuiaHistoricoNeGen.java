package br.gov.go.tj.projudi.ne;

import java.util.List;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.GuiaHistoricoDt;
import br.gov.go.tj.projudi.ps.GuiaHistoricoPs;

 import br.gov.go.tj.projudi.ne.GuiaEmissaoNe;
//---------------------------------------------------------
public abstract class GuiaHistoricoNeGen extends Negocio{


	/**
	 * 
	 */
	private static final long serialVersionUID = 7085067696561890642L;
	protected  GuiaHistoricoDt obDados;


	public GuiaHistoricoNeGen() {

		obLog = new LogNe(); 

		obDados = new GuiaHistoricoDt(); 

	}


//---------------------------------------------------------
	public void salvar(GuiaHistoricoDt dados ) throws Exception {

		LogDt obLogDt;

		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			GuiaHistoricoPs obPersistencia = new  GuiaHistoricoPs(obFabricaConexao.getConexao()); 

			/* use o iu do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().length()==0) {
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("GuiaHistorico",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {
				obPersistencia.alterar(dados); 
				obLogDt = new LogDt("GuiaHistorico",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}


//---------------------------------------------------------
	 public abstract String Verificar(GuiaHistoricoDt dados ); 


//---------------------------------------------------------

	public void excluir(GuiaHistoricoDt dados) throws Exception {

		LogDt obLogDt;

		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			GuiaHistoricoPs obPersistencia = new  GuiaHistoricoPs(obFabricaConexao.getConexao()); 

			obLogDt = new LogDt("GuiaHistorico",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
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

			GuiaHistoricoPs obPersistencia = new  GuiaHistoricoPs(obFabricaConexao.getConexao()); 

				stTemp = obPersistencia.consultarDescricaoJSON(descricao, posicao);
			}finally {
				obFabricaConexao.fecharConexao();
			}
		return stTemp;
	}
 //---------------------------------------------------------

	public GuiaHistoricoDt consultarId(String id_guiahistorico ) throws Exception {

		GuiaHistoricoDt dtRetorno=null;
		FabricaConexao obFabricaConexao = null;


		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			GuiaHistoricoPs obPersistencia = new  GuiaHistoricoPs(obFabricaConexao.getConexao()); 

			dtRetorno= obPersistencia.consultarId(id_guiahistorico ); 
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

			GuiaHistoricoPs obPersistencia = new  GuiaHistoricoPs(obFabricaConexao.getConexao()); 

			tempList=obPersistencia.consultarDescricao( descricao, posicao);
			QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
			tempList.remove(tempList.size()-1);
		} finally { 
			obFabricaConexao.fecharConexao();
		}
		return tempList;   
	}

	public List consultarDescricaoGuiaemissao(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		GuiaEmissaoNe GuiaEmissaoNe = new GuiaEmissaoNe(); 
		tempList = GuiaEmissaoNe.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = GuiaEmissaoNe.getQuantidadePaginas();
		GuiaEmissaoNe = null;
		return tempList;
	}
}
