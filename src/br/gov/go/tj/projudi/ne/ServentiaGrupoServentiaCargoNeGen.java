package br.gov.go.tj.projudi.ne;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.ServentiaGrupoDt;
import br.gov.go.tj.projudi.dt.ServentiaGrupoServentiaCargoDt;
import br.gov.go.tj.projudi.ps.ServentiaGrupoServentiaCargoPs;
import br.gov.go.tj.utils.FabricaConexao;
//---------------------------------------------------------
public abstract class ServentiaGrupoServentiaCargoNeGen extends Negocio{


	/**
	 * 
	 */
	private static final long serialVersionUID = -2010579479648732011L;

	protected  ServentiaGrupoServentiaCargoDt obDados;

	protected List lisGeral = null; 
	public ServentiaGrupoServentiaCargoNeGen() {

		obLog = new LogNe(); 

		obDados = new ServentiaGrupoServentiaCargoDt(); 

	}


//---------------------------------------------------------
	public void salvarMultiplo(ServentiaGrupoServentiaCargoDt dados, String[] listaEditada) throws Exception {

		LogDt obLogDt;


		//verifico os ids as serem excluidos
		List lisExcluir = new ArrayList();
		//verifico os ids as serem incluidos
		List lisIncluir = new ArrayList();
		//pego a lista geral e procuro os que tem id
		//somente os que tem id podem ser excluidos
		
		if (listaEditada == null){
			for (int i = 0; i < lisGeral.size(); i++) {
				ServentiaGrupoServentiaCargoDt obDt = (ServentiaGrupoServentiaCargoDt)lisGeral.get(i);
				if (!obDt.getId().equalsIgnoreCase(""))	lisExcluir.add(obDt);				 
			}
		} else {
			for (int i = 0; i < lisGeral.size(); i++) {
				ServentiaGrupoServentiaCargoDt obDt = (ServentiaGrupoServentiaCargoDt)lisGeral.get(i);
				boolean boEncontrado =false;
				//se tiver id vejo que o mesmo não esta mais na lista editada
				if (!obDt.getId().equalsIgnoreCase("")){
					//verifico qual id saiu da lista editada
					for(int j=0; j< listaEditada.length; j++)
						if (obDt.getId_ServentiaCargo().equalsIgnoreCase((String)listaEditada[j])){
							boEncontrado = true;
							break;
						}
				//se o id do objeto não foi encontrado na lista editada coloco o objeto para ser excluido
				if (!boEncontrado) lisExcluir.add(obDt);
				}
			}
			
			for (int i = 0; i < listaEditada.length; i++)
				for(int j=0; j< lisGeral.size(); j++){
					ServentiaGrupoServentiaCargoDt obDt = (ServentiaGrupoServentiaCargoDt)lisGeral.get(j);
					if (obDt.getId_ServentiaCargo().equalsIgnoreCase((String)listaEditada[i]) && obDt.getId().equalsIgnoreCase("")){
						lisIncluir.add(obDt);
						break;
					}
				}
		}

		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ServentiaGrupoServentiaCargoPs obPersistencia = new  ServentiaGrupoServentiaCargoPs(obFabricaConexao.getConexao()); 

			for(int i = 0; i < lisIncluir.size(); i++) {
				ServentiaGrupoServentiaCargoDt obDt = (ServentiaGrupoServentiaCargoDt)lisIncluir.get(i);
				obDt.setId_ServentiaGrupo(dados.getId_ServentiaGrupo());
				obPersistencia.inserir(obDt);
				obLogDt = new LogDt("ServentiaGrupoServentiaCargo", obDt.getId() , dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",obDt.getPropriedades());
				obLog.salvar(obLogDt, obFabricaConexao);
			}
			for(int i = 0; i < lisExcluir.size(); i++) {
				ServentiaGrupoServentiaCargoDt obDtTemp = (ServentiaGrupoServentiaCargoDt)lisExcluir.get(i); 
				obLogDt = new LogDt("ServentiaGrupoServentiaCargo", obDtTemp.getId() , dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),obDtTemp.getPropriedades(),"");
				obPersistencia.excluir(obDtTemp.getId());
				obLog.salvar(obLogDt, obFabricaConexao);
			}
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

//---------------------------------------------------------
	public String consultarServentiaCargoServentiaGrupoUlLiCheckBox(String id_serv_grupo ) throws Exception {

		lisGeral=null;
		FabricaConexao obFabricaConexao = null;
		String tempUlLi="<ul>";

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

		    ServentiaGrupoServentiaCargoPs obPersistencia = new  ServentiaGrupoServentiaCargoPs(obFabricaConexao.getConexao()); 
		    
			ServentiaGrupoDt serventiaGrupoDt = new ServentiaGrupoNe().consultarId(id_serv_grupo);

			lisGeral=obPersistencia.consultarServentiaCargoServentiaGrupoGeral( serventiaGrupoDt.getId(), serventiaGrupoDt.getId_Serventia());
			
			for(int i = 0; i < lisGeral.size(); i++) {
				ServentiaGrupoServentiaCargoDt obDtTemp = (ServentiaGrupoServentiaCargoDt)lisGeral.get(i); 

				tempUlLi+= obDtTemp.getListaLiCheckBox();
			}
			tempUlLi+="</ul>";
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return tempUlLi;   
	}

//---------------------------------------------------------
	public void salvar(ServentiaGrupoServentiaCargoDt dados ) throws Exception {

		LogDt obLogDt;

		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ServentiaGrupoServentiaCargoPs obPersistencia = new  ServentiaGrupoServentiaCargoPs(obFabricaConexao.getConexao()); 

			/* use o iu do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().length()==0) {
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("ServentiaGrupoServentiaCargo",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {
				obPersistencia.alterar(dados); 
				obLogDt = new LogDt("ServentiaGrupoServentiaCargo",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}


//---------------------------------------------------------
	 public abstract String Verificar(ServentiaGrupoServentiaCargoDt dados ); 


//---------------------------------------------------------

	public void excluir(ServentiaGrupoServentiaCargoDt dados) throws Exception {

		LogDt obLogDt;

		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ServentiaGrupoServentiaCargoPs obPersistencia = new  ServentiaGrupoServentiaCargoPs(obFabricaConexao.getConexao()); 

			obLogDt = new LogDt("ServentiaGrupoServentiaCargo",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
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

		ServentiaGrupoServentiaCargoPs obPersistencia = new  ServentiaGrupoServentiaCargoPs(obFabricaConexao.getConexao()); 

			stTemp = obPersistencia.consultarDescricaoJSON(descricao, posicao);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}
 //---------------------------------------------------------

	public ServentiaGrupoServentiaCargoDt consultarId(String id_serventiacargoserventiagrupo ) throws Exception {

		ServentiaGrupoServentiaCargoDt dtRetorno=null;
		FabricaConexao obFabricaConexao = null;


		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaGrupoServentiaCargoPs obPersistencia = new  ServentiaGrupoServentiaCargoPs(obFabricaConexao.getConexao()); 

			dtRetorno= obPersistencia.consultarId(id_serventiacargoserventiagrupo ); 
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

		ServentiaGrupoServentiaCargoPs obPersistencia = new  ServentiaGrupoServentiaCargoPs(obFabricaConexao.getConexao()); 

			tempList=obPersistencia.consultarDescricao( descricao, posicao);
			QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
			tempList.remove(tempList.size()-1);
		} finally {
			obFabricaConexao.fecharConexao();
		}
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

	public List consultarDescricaoServentiaGrupo(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		ServentiaGrupoNe ServentiaGrupone = new ServentiaGrupoNe(); 
		tempList = ServentiaGrupone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = ServentiaGrupone.getQuantidadePaginas();
		ServentiaGrupone = null;
		return tempList;
	}
}
