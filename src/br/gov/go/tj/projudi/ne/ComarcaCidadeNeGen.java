package br.gov.go.tj.projudi.ne;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.ComarcaCidadeDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.ps.ComarcaCidadePs;
import br.gov.go.tj.utils.FabricaConexao;
//---------------------------------------------------------
public abstract class ComarcaCidadeNeGen extends Negocio{


	private static final long serialVersionUID = -2418094627741978902L;	
	protected  ComarcaCidadeDt obDados;

	protected List lisGeral = null; 
	public ComarcaCidadeNeGen() {
		
		obLog = new LogNe(); 
		obDados = new ComarcaCidadeDt(); 

	}


//---------------------------------------------------------
	public void salvarMultiplo(ComarcaCidadeDt dados, String[] listaEditada) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null; 

		//verifico os ids as serem excluidos
		List lisExcluir = new ArrayList();
		//pego a lista geral e procuro os que tem id
		//somente os que tem id podem ser excluidos
		for (int i = 0; i < lisGeral.size(); i++) {
			ComarcaCidadeDt obDt = (ComarcaCidadeDt)lisGeral.get(i);
			boolean boEncontrado =false;
			//se tiver id vejo que o mesmo não esta mais na lista editada
			if (!obDt.getId().equalsIgnoreCase("")){
				//verifico qual id saiu da lista editada
				for(int j=0; j< listaEditada.length; j++)
					if (obDt.getId_Cidade().equalsIgnoreCase((String)listaEditada[j])){
						boEncontrado = true;
						break;
					}
			//se o id do objeto não foi encontrado na lista editada coloco o objeto para ser excluido
			if (!boEncontrado) lisExcluir.add(obDt);
			}
		}

		//verifico os ids as serem incluidos
		List lisIncluir = new ArrayList();
		for (int i = 0; i < listaEditada.length; i++)
			for(int j=0; j< lisGeral.size(); j++){
				ComarcaCidadeDt obDt = (ComarcaCidadeDt)lisGeral.get(j);
				if (obDt.getId_Cidade().equalsIgnoreCase((String)listaEditada[i]) && obDt.getId().equalsIgnoreCase("")){
					lisIncluir.add(obDt);
					break;
				}
			}

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ComarcaCidadePs obPersistencia = new ComarcaCidadePs(obFabricaConexao.getConexao());
			for(int i = 0; i < lisIncluir.size(); i++) {
				ComarcaCidadeDt obDt = (ComarcaCidadeDt)lisIncluir.get(i);
				obPersistencia.inserir(obDt);
				obLogDt = new LogDt("ComarcaCidade", obDt.getId() , dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",obDt.getPropriedades());
				obLog.salvar(obLogDt, obFabricaConexao);
			}
			for(int i = 0; i < lisExcluir.size(); i++) {
				ComarcaCidadeDt obDtTemp = (ComarcaCidadeDt)lisExcluir.get(i); 

				obLogDt = new LogDt("ComarcaCidade", obDtTemp.getId() , dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),obDtTemp.getPropriedades(),"");
				obPersistencia.excluir(obDtTemp.getId());
				obLog.salvar(obLogDt, obFabricaConexao);
			}
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

//---------------------------------------------------------
	public String consultarCidadeComarcaUlLiCheckBox(String id_comarca ) throws Exception {

		lisGeral=null;
		FabricaConexao obFabricaConexao = null; 
		String tempUlLi="<ul>";

			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			try{

				ComarcaCidadePs obPersistencia = new ComarcaCidadePs(obFabricaConexao.getConexao());
				lisGeral=obPersistencia.consultarCidadeComarcaGeral( id_comarca);
				for(int i = 0; i < lisGeral.size(); i++) {
					ComarcaCidadeDt obDtTemp = (ComarcaCidadeDt)lisGeral.get(i); 

					tempUlLi+= obDtTemp.getListaLiCheckBox();
				}
				tempUlLi+="</ul>";
			} finally {
				obFabricaConexao.fecharConexao();
			}
		return tempUlLi;   
	}

//---------------------------------------------------------
	public void salvar(ComarcaCidadeDt dados ) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null; 

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ComarcaCidadePs obPersistencia = new ComarcaCidadePs(obFabricaConexao.getConexao());
			/* use o iu do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().length()==0) {
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("ComarcaCidade",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {
				obPersistencia.alterar(dados); 
				obLogDt = new LogDt("ComarcaCidade",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}


//---------------------------------------------------------
	 public abstract String Verificar(ComarcaCidadeDt dados ); 


//---------------------------------------------------------

	public void excluir(ComarcaCidadeDt dados) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null; 

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ComarcaCidadePs obPersistencia = new ComarcaCidadePs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("ComarcaCidade",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId());

			dados.limpar();
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

 //---------------------------------------------------------

	public ComarcaCidadeDt consultarId(String id_comarcacidade ) throws Exception {

		ComarcaCidadeDt dtRetorno=null;
		FabricaConexao obFabricaConexao = null; 

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ComarcaCidadePs obPersistencia = new ComarcaCidadePs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarId(id_comarcacidade ); 
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

			ComarcaCidadePs obPersistencia = new ComarcaCidadePs(obFabricaConexao.getConexao());
			tempList=obPersistencia.consultarDescricao( descricao, posicao);
			QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
			tempList.remove(tempList.size()-1);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return tempList;   
	}

	public List consultarDescricaoComarca(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		ComarcaNe Comarcane = new ComarcaNe(); 
		tempList = Comarcane.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = Comarcane.getQuantidadePaginas();
		Comarcane = null;
		return tempList;
	}

	public List consultarDescricaoCidade(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		CidadeNe Cidadene = new CidadeNe(); 
		tempList = Cidadene.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = Cidadene.getQuantidadePaginas();
		Cidadene = null;
		return tempList;
	}
}