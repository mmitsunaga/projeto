package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.TemaDt;
import br.gov.go.tj.projudi.ps.TemaPs;
import br.gov.go.tj.utils.FabricaConexao;
//---------------------------------------------------------
public abstract class TemaNeGen extends Negocio{


	/**
	 * 
	 */
	private static final long serialVersionUID = -5064705722721013341L;
	protected  TemaDt obDados;

	public TemaNeGen() {

		obLog = new LogNe(); 

		obDados = new TemaDt(); 

	}
	
//---------------------------------------------------------
	public void salvar(TemaDt dados ) throws Exception {

		LogDt obLogDt;

		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			TemaPs obPersistencia = new  TemaPs(obFabricaConexao.getConexao()); 

			/* use o iu do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().length()==0) {
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("Tema",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {
				obPersistencia.alterar(dados); 
				obLogDt = new LogDt("Tema",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}


//---------------------------------------------------------
	 public abstract String Verificar(TemaDt dados ); 


//---------------------------------------------------------

	public void excluir(TemaDt dados) throws Exception {

		LogDt obLogDt;

		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			TemaPs obPersistencia = new  TemaPs(obFabricaConexao.getConexao()); 

			obLogDt = new LogDt("Tema",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
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

			TemaPs obPersistencia = new  TemaPs(obFabricaConexao.getConexao()); 

			stTemp = obPersistencia.consultarDescricaoJSON(descricao, posicao);		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}
 //---------------------------------------------------------

	public TemaDt consultarId(String id_tema ) throws Exception {

		TemaDt dtRetorno=null;
		FabricaConexao obFabricaConexao = null;


		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			TemaPs obPersistencia = new  TemaPs(obFabricaConexao.getConexao()); 

			dtRetorno= obPersistencia.consultarId(id_tema ); 
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

		TemaPs obPersistencia = new  TemaPs(obFabricaConexao.getConexao()); 

			tempList=obPersistencia.consultarDescricao( descricao, posicao);
			QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
			tempList.remove(tempList.size()-1);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return tempList;   
	}

	public List consultarDescricaoTemaSituacao(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;

		TemaSituacaoNe TemaSituacaone = new TemaSituacaoNe(); 
		tempList = TemaSituacaone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = TemaSituacaone.getQuantidadePaginas();
		TemaSituacaone = null;

		return tempList;
	}

	public String consultarDescricaoTemaSituacaoJSON(String descricao, String PosicaoPaginaAtual ) throws Exception{
		String stTemp="";

		stTemp = (new TemaSituacaoNe()).consultarDescricaoJSON(descricao, PosicaoPaginaAtual);

		return stTemp;
	}

	public List consultarDescricaoTemaOrigem(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		
		TemaOrigemNe TemaOrigemne = new TemaOrigemNe(); 
		tempList = TemaOrigemne.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = TemaOrigemne.getQuantidadePaginas();
		TemaOrigemne = null;
		
		return tempList;
	}

	public String consultarDescricaoTemaOrigemJSON(String descricao, String PosicaoPaginaAtual ) throws Exception{
		String stTemp="";

		stTemp = (new TemaOrigemNe()).consultarDescricaoJSON(descricao, PosicaoPaginaAtual);

		return stTemp;
	}

	public List consultarDescricaoTemaTipo(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		
		TemaTipoNe TemaTipone = new TemaTipoNe(); 
		tempList = TemaTipone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = TemaTipone.getQuantidadePaginas();
		TemaTipone = null;

		return tempList;
	}

	public String consultarDescricaoTemaTipoJSON(String descricao, String PosicaoPaginaAtual ) throws Exception{
		String stTemp="";

		stTemp = (new TemaTipoNe()).consultarDescricaoJSON(descricao, PosicaoPaginaAtual);

		return stTemp;
	}
}
