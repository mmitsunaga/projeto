package br.gov.go.tj.projudi.ne;

import java.util.List;
import java.util.ArrayList;

import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoAssuntoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteAssuntoDt;
import br.gov.go.tj.projudi.ps.ProcessoParteAssuntoPs;

 import br.gov.go.tj.projudi.ne.ProcessoParteNe;
 import br.gov.go.tj.projudi.ne.AssuntoNe;
//---------------------------------------------------------
public abstract class ProcessoParteAssuntoNeGen extends Negocio{


	/**
	 * 
	 */
	private static final long serialVersionUID = 8685092941236334310L;
	protected  ProcessoParteAssuntoDt obDados;


	public ProcessoParteAssuntoNeGen() {

		obLog = new LogNe(); 

		obDados = new ProcessoParteAssuntoDt(); 

	}





//---------------------------------------------------------
	 public abstract String Verificar(ProcessoParteAssuntoDt dados ); 




 //---------------------------------------------------------

 /**

  * Método para lista as area processuais

 * @author jrcorrea

 */

	public String consultarDescricaoJSON(String descricao, String posicao ) throws Exception {
		FabricaConexao obFabricaConexao = null;
		String stTemp = "";
			try {
				obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			ProcessoParteAssuntoPs obPersistencia = new ProcessoParteAssuntoPs(obFabricaConexao.getConexao()); 

				stTemp = obPersistencia.consultarDescricaoJSON(descricao, posicao);
			} finally {
				obFabricaConexao.fecharConexao();
			}
		return stTemp;
	}
 //---------------------------------------------------------

	public ProcessoParteAssuntoDt consultarId(String id_procparteassunto ) throws Exception {

		ProcessoParteAssuntoDt dtRetorno=null;
		FabricaConexao obFabricaConexao = null;


		try{ 
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			ProcessoParteAssuntoPs obPersistencia = new ProcessoParteAssuntoPs(obFabricaConexao.getConexao()); 

			dtRetorno= obPersistencia.consultarId(id_procparteassunto ); 
			obDados.copiar(dtRetorno);
		}finally{
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

			ProcessoParteAssuntoPs obPersistencia = new ProcessoParteAssuntoPs(obFabricaConexao.getConexao()); 

				tempList=obPersistencia.consultarDescricao( descricao, posicao);
				QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
				tempList.remove(tempList.size()-1);
			}finally{
				obFabricaConexao.fecharConexao();
			}
		return tempList;   
	}

	public List consultarDescricaoProcessoParte(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception { 
		List tempList=null;
			ProcessoParteNe ProcessoPartene = new ProcessoParteNe(); 
			tempList = ProcessoPartene.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
			QuantidadePaginas = ProcessoPartene.getQuantidadePaginas();
			ProcessoPartene = null;
		return tempList;
	}

//	public String consultarDescricaoProcessoParteJSON(String descricao, String PosicaoPaginaAtual ) throws Exception { 
//		String stTemp = (new ProcessoParteNe()).consultarDescricaoJSON(descricao, PosicaoPaginaAtual);
//		return stTemp;
//	}

	public List consultarDescricaoAssunto(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception { 
		List tempList=null;
			AssuntoNe Assuntone = new AssuntoNe(); 
			tempList = Assuntone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
			QuantidadePaginas = Assuntone.getQuantidadePaginas();
			Assuntone = null;
		return tempList;
	}

}
