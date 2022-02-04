package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.PonteiroLogDt;
import br.gov.go.tj.projudi.ps.PonteiroLogPs;
import br.gov.go.tj.utils.FabricaConexao;
//---------------------------------------------------------
public abstract class PonteiroLogNeGen extends Negocio{


	/**
	 * 
	 */
	private static final long serialVersionUID = -3552279810163381780L;
	protected  PonteiroLogDt obDados;


	public PonteiroLogNeGen() {

		obLog = new LogNe(); 

		obDados = new PonteiroLogDt(); 

	}




//---------------------------------------------------------
	 public abstract String Verificar(PonteiroLogDt dados ); 






	public String consultarDescricaoJSON(String descricao, String posicao ) throws Exception {
		FabricaConexao obFabricaConexao = null;
		String stTemp = "";
			try {
				obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			PonteiroLogPs obPersistencia = new  PonteiroLogPs(obFabricaConexao.getConexao()); 

				stTemp = obPersistencia.consultarDescricaoJSON(descricao, posicao);
			} finally {
				obFabricaConexao.fecharConexao();
			}
		return stTemp;
	}
 //---------------------------------------------------------

	public PonteiroLogDt consultarId(String id_ponteirolog ) throws Exception {

		PonteiroLogDt dtRetorno=null;
		FabricaConexao obFabricaConexao = null;


		try{ 
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			PonteiroLogPs obPersistencia = new  PonteiroLogPs(obFabricaConexao.getConexao()); 

			dtRetorno= obPersistencia.consultarId(id_ponteirolog ); 
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

			PonteiroLogPs obPersistencia = new  PonteiroLogPs(obFabricaConexao.getConexao()); 

				tempList=obPersistencia.consultarDescricao( descricao, posicao);
				QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
				tempList.remove(tempList.size()-1);
			}finally{
				obFabricaConexao.fecharConexao();
			}
		return tempList;   
	}

	public List consultarDescricaoAreaDistribuicao(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception { 
		List tempList=null;
			AreaDistribuicaoNe AreaDistribuicaone = new AreaDistribuicaoNe(); 
			tempList = AreaDistribuicaone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
			QuantidadePaginas = AreaDistribuicaone.getQuantidadePaginas();
			AreaDistribuicaone = null;
		return tempList;
	}

	public String consultarDescricaoAreaDistribuicaoJSON(String descricao, String PosicaoPaginaAtual ) throws Exception { 
		String stTemp = (new AreaDistribuicaoNe()).consultarDescricaoJSON(descricao, PosicaoPaginaAtual);
		return stTemp;
	}

	public List consultarDescricaoPonteiroLogTipo(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception { 
		List tempList=null;
			PonteiroLogTipoNe PonteiroLogTipone = new PonteiroLogTipoNe(); 
			tempList = PonteiroLogTipone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
			QuantidadePaginas = PonteiroLogTipone.getQuantidadePaginas();
			PonteiroLogTipone = null;
		return tempList;
	}

	public String consultarDescricaoPonteiroLogTipoJSON(String descricao, String PosicaoPaginaAtual ) throws Exception { 
		String stTemp = (new PonteiroLogTipoNe()).consultarDescricaoJSON(descricao, PosicaoPaginaAtual);
		return stTemp;
	}

	public List consultarDescricaoProcesso(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception { 
		List tempList=null;
			ProcessoNe Processone = new ProcessoNe(); 
			tempList = Processone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
			QuantidadePaginas = Processone.getQuantidadePaginas();
			Processone = null;
		return tempList;
	}


	public List consultarDescricaoServentia(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception { 
		List tempList=null;
			ServentiaNe Serventiane = new ServentiaNe(); 
			tempList = Serventiane.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
			QuantidadePaginas = Serventiane.getQuantidadePaginas();
			Serventiane = null;
		return tempList;
	}

	public String consultarDescricaoServentiaJSON(String descricao, String PosicaoPaginaAtual ) throws Exception { 
		String stTemp = (new ServentiaNe()).consultarDescricaoJSON(descricao, PosicaoPaginaAtual);
		return stTemp;
	}

	public List consultarDescricaoUsuarioServentia(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception { 
		List tempList=null;
			UsuarioServentiaNe UsuarioServentiane = new UsuarioServentiaNe(); 
			tempList = UsuarioServentiane.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
			QuantidadePaginas = UsuarioServentiane.getQuantidadePaginas();
			UsuarioServentiane = null;
		return tempList;
	}

	public String consultarDescricaoUsuarioServentiaJSON(String descricao, String PosicaoPaginaAtual ) throws Exception { 
		String stTemp = (new UsuarioServentiaNe()).consultarDescricaoJSON(descricao, PosicaoPaginaAtual);
		return stTemp;
	}

	}
