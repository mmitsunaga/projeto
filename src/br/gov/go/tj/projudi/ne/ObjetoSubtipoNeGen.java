package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.ObjetoSubtipoDt;
import br.gov.go.tj.projudi.ps.ObjetoSubtipoPs;
import br.gov.go.tj.utils.FabricaConexao;
//---------------------------------------------------------
public abstract class ObjetoSubtipoNeGen extends Negocio{


	/**
	 * 
	 */
	private static final long serialVersionUID = -39335047295967142L;
	protected  ObjetoSubtipoDt obDados;


	public ObjetoSubtipoNeGen() {

		obLog = new LogNe(); 

		obDados = new ObjetoSubtipoDt(); 

	}


//---------------------------------------------------------
	public void salvar(ObjetoSubtipoDt dados ) throws Exception {

		LogDt obLogDt;

		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try {
			obFabricaConexao.iniciarTransacao();
			ObjetoSubtipoPs obPersistencia = new ObjetoSubtipoPs(obFabricaConexao.getConexao()); 

			/* use o id do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().length()==0) {
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("ObjetoSubtipo",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {
				obPersistencia.alterar(dados); 
				obLogDt = new LogDt("ObjetoSubtipo",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		}finally{
			obFabricaConexao.fecharConexao();
		}
	}


//---------------------------------------------------------
	 public abstract String Verificar(ObjetoSubtipoDt dados ); 


//---------------------------------------------------------

	public void excluir(ObjetoSubtipoDt dados) throws Exception {

		LogDt obLogDt;

		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try {
			obFabricaConexao.iniciarTransacao();
			ObjetoSubtipoPs obPersistencia = new ObjetoSubtipoPs(obFabricaConexao.getConexao()); 

			obLogDt = new LogDt("ObjetoSubtipo",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
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
			try {
			ObjetoSubtipoPs obPersistencia = new ObjetoSubtipoPs(obFabricaConexao.getConexao()); 

			stTemp = obPersistencia.consultarDescricaoJSON(descricao, posicao);
			} finally {
				obFabricaConexao.fecharConexao();
			}
		return stTemp;
	}
 //---------------------------------------------------------

	public ObjetoSubtipoDt consultarId(String id_objetosubtipo ) throws Exception {

		ObjetoSubtipoDt dtRetorno=null;
		FabricaConexao obFabricaConexao = null;


		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{ 
			ObjetoSubtipoPs obPersistencia = new ObjetoSubtipoPs(obFabricaConexao.getConexao()); 

			dtRetorno= obPersistencia.consultarId(id_objetosubtipo ); 
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
			ObjetoSubtipoPs obPersistencia = new ObjetoSubtipoPs(obFabricaConexao.getConexao()); 

			tempList=obPersistencia.consultarDescricao( descricao, posicao);
			QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
			tempList.remove(tempList.size()-1);
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;   
	}

	public List consultarDescricaoObjetoTipo(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception { 
		List tempList=null;
			ObjetoTipoNe ObjetoTipone = new ObjetoTipoNe(); 
			tempList = ObjetoTipone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
			QuantidadePaginas = ObjetoTipone.getQuantidadePaginas();
			ObjetoTipone = null;
		return tempList;
	}

	public String consultarDescricaoObjetoTipoJSON(String descricao, String PosicaoPaginaAtual ) throws Exception { 
		String stTemp = (new ObjetoTipoNe()).consultarDescricaoJSON(descricao, PosicaoPaginaAtual);
		return stTemp;
	}

	}
