package br.gov.go.tj.projudi.ne;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.GuiaCustaModeloDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.ps.GuiaCustaModeloPs;
import br.gov.go.tj.utils.FabricaConexao;
//---------------------------------------------------------
public abstract class GuiaCustaModeloNeGen extends Negocio{


	/**
	 * 
	 */
	private static final long serialVersionUID = 442099033756226559L;
	
	protected  GuiaCustaModeloDt obDados;

	protected List lisGeral = null; 
	public GuiaCustaModeloNeGen() {
		
		obLog = new LogNe(); 
		obDados = new GuiaCustaModeloDt(); 

	}


//---------------------------------------------------------
	public void salvarMultiplo(GuiaCustaModeloDt dados, String[] listaEditada) throws Exception {

		LogDt obLogDt;

		//verifico os ids as serem excluidos
		List lisExcluir = new ArrayList();
		//pego a lista geral e procuro os que tem id
		//somente os que tem id podem ser excluidos
		for (int i = 0; i < lisGeral.size(); i++) {
			GuiaCustaModeloDt obDt = (GuiaCustaModeloDt)lisGeral.get(i);
			boolean boEncontrado =false;
			//se tiver id vejo que o mesmo não esta mais na lista editada
			if (!obDt.getId().equalsIgnoreCase("")){
				//verifico qual id saiu da lista editada
				for(int j=0; j< listaEditada.length; j++)
					if (obDt.getId_Custa().equalsIgnoreCase((String)listaEditada[j])){
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
				GuiaCustaModeloDt obDt = (GuiaCustaModeloDt)lisGeral.get(j);
				if (obDt.getId_Custa().equalsIgnoreCase((String)listaEditada[i]) && obDt.getId().equalsIgnoreCase("")){
					lisIncluir.add(obDt);
					break;
				}
			}

		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			GuiaCustaModeloPs obPersistencia = new GuiaCustaModeloPs(obFabricaConexao.getConexao());
			for(int i = 0; i < lisIncluir.size(); i++) {
				GuiaCustaModeloDt obDt = (GuiaCustaModeloDt)lisIncluir.get(i);
				obPersistencia.inserir(obDt);
				obLogDt = new LogDt("GuiaCustaModelo", obDt.getId() , dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",obDt.getPropriedades());
				obLog.salvar(obLogDt, obFabricaConexao);
			}
			for(int i = 0; i < lisExcluir.size(); i++) {
				GuiaCustaModeloDt obDtTemp = (GuiaCustaModeloDt)lisExcluir.get(i); 

				obLogDt = new LogDt("GuiaCustaModelo", obDtTemp.getId() , dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),obDtTemp.getPropriedades(),"");
				obPersistencia.excluir(obDtTemp.getId());
				obLog.salvar(obLogDt, obFabricaConexao);
			}
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

//---------------------------------------------------------
	public String consultarCustaGuiaModeloUlLiCheckBox(String id_GuiaModelo ) throws Exception {

		lisGeral=null;
		String tempUlLi="<ul>";
		FabricaConexao obFabricaConexao = null;
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			try{

				GuiaCustaModeloPs obPersistencia = new GuiaCustaModeloPs(obFabricaConexao.getConexao());
				lisGeral=obPersistencia.consultarCustaGuiaModeloGeral( id_GuiaModelo);
				for(int i = 0; i < lisGeral.size(); i++) {
					GuiaCustaModeloDt obDtTemp = (GuiaCustaModeloDt)lisGeral.get(i); 

					tempUlLi+= obDtTemp.getListaLiCheckBox();
				}
				tempUlLi+="</ul>";			
			}finally{
				obFabricaConexao.fecharConexao();
			}
		return tempUlLi;   
	}

//---------------------------------------------------------
	public void salvar(GuiaCustaModeloDt dados ) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			GuiaCustaModeloPs obPersistencia = new GuiaCustaModeloPs(obFabricaConexao.getConexao());
			/* use o iu do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().length()==0) {
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("GuiaCustaModelo",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {
				obPersistencia.alterar(dados); 
				obLogDt = new LogDt("GuiaCustaModelo",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}


//---------------------------------------------------------
	 public abstract String Verificar(GuiaCustaModeloDt dados ); 


//---------------------------------------------------------

	public void excluir(GuiaCustaModeloDt dados) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			GuiaCustaModeloPs obPersistencia = new GuiaCustaModeloPs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("GuiaCustaModelo",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId());

			dados.limpar();
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

 //---------------------------------------------------------

	public GuiaCustaModeloDt consultarId(String id_GuiaCustaModelo ) throws Exception {

		GuiaCustaModeloDt dtRetorno=null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			GuiaCustaModeloPs obPersistencia = new GuiaCustaModeloPs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarId(id_GuiaCustaModelo ); 
			obDados.copiar(dtRetorno);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}

//---------------------------------------------------------

	public long getQuantidadePaginas() {
		return QuantidadePaginas;
	}
//---------------------------------------------------------

	public List consultarDescricao(String descricao, String posicao ) throws Exception{
		List tempList=null;
		FabricaConexao obFabricaConexao = null;
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			try{

				GuiaCustaModeloPs obPersistencia = new GuiaCustaModeloPs(obFabricaConexao.getConexao());
				tempList=obPersistencia.consultarDescricao( descricao, posicao);
				QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
				tempList.remove(tempList.size()-1);
			
			}finally{
				obFabricaConexao.fecharConexao();
			}
		return tempList;   
	}

	public List consultarDescricaoGuiaModelo(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;

		GuiaModeloNe GuiaModelone = new GuiaModeloNe(); 
		tempList = GuiaModelone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = GuiaModelone.getQuantidadePaginas();
		GuiaModelone = null;
		
		return tempList;
	}

	public List consultarDescricaoCusta(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;

		CustaNe Custane = new CustaNe(); 
		tempList = Custane.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = Custane.getQuantidadePaginas();
		Custane = null;

		return tempList;
	}

	}
