package br.gov.go.tj.projudi.ne;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.GrupoPendenciaTipoDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.ps.GrupoPendenciaTipoPs;
import br.gov.go.tj.utils.FabricaConexao;
//---------------------------------------------------------
public abstract class GrupoPendenciaTipoNeGen extends Negocio{


	/**
	 * 
	 */
	private static final long serialVersionUID = -3011334546270346128L;
	
	protected  GrupoPendenciaTipoDt obDados;

	protected List lisGeral = null; 
	public GrupoPendenciaTipoNeGen() {
		
		obLog = new LogNe(); 

		obDados = new GrupoPendenciaTipoDt(); 

	}


//---------------------------------------------------------
	public void salvarMultiplo(GrupoPendenciaTipoDt dados, String[] listaEditada) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;

		//////System.out.println("..neGrupoPendenciaTiposalvarMultiplo()");
		//verifico os ids as serem excluidos
		List lisExcluir = new ArrayList();
		//pego a lista geral e procuro os que tem id
		//somente os que tem id podem ser excluidos
		for (int i = 0; i < lisGeral.size(); i++) {
			GrupoPendenciaTipoDt obDt = (GrupoPendenciaTipoDt)lisGeral.get(i);
			boolean boEncontrado =false;
			//se tiver id vejo que o mesmo não esta mais na lista editada
			if (!obDt.getId().equalsIgnoreCase("")){
				//verifico qual id saiu da lista editada
				for(int j=0; j< listaEditada.length; j++)
					if (obDt.getId_PendenciaTipo().equalsIgnoreCase((String)listaEditada[j])){
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
				GrupoPendenciaTipoDt obDt = (GrupoPendenciaTipoDt)lisGeral.get(j);
				if (obDt.getId_PendenciaTipo().equalsIgnoreCase((String)listaEditada[i]) && obDt.getId().equalsIgnoreCase("")){
					lisIncluir.add(obDt);
					break;
				}
			}

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			GrupoPendenciaTipoPs obPersistencia = new GrupoPendenciaTipoPs(obFabricaConexao.getConexao());
			for(int i = 0; i < lisIncluir.size(); i++) {
				GrupoPendenciaTipoDt obDt = (GrupoPendenciaTipoDt)lisIncluir.get(i);
				
				obPersistencia.inserir(obDt);
				obLogDt = new LogDt("GrupoPendenciaTipo", obDt.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",obDt.getPropriedades());
				obLog.salvar(obLogDt, obFabricaConexao);
			}
			for(int i = 0; i < lisExcluir.size(); i++) {
				GrupoPendenciaTipoDt obDtTemp = (GrupoPendenciaTipoDt)lisExcluir.get(i); 

				obLogDt = new LogDt("GrupoPendenciaTipo", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),obDtTemp.getPropriedades(),"");
				obPersistencia.excluir(obDtTemp.getId());
				obLog.salvar(obLogDt, obFabricaConexao);
			}
			obFabricaConexao.finalizarTransacao();

		}catch(Exception e){
			obFabricaConexao.cancelarTransacao();			 
			throw e;
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

//---------------------------------------------------------
	public String consultarPendenciaTipoGrupoUlLiCheckBox(String id_grupo ) throws Exception {

		lisGeral=null;
		String tempUlLi="<ul>";
		FabricaConexao obFabricaConexao = null;
		//////System.out.println("..ne-consultarPendenciaTipoGrupoUlLiCheckBoxGrupoPendenciaTipo" ); 

			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			try{

				GrupoPendenciaTipoPs obPersistencia = new GrupoPendenciaTipoPs(obFabricaConexao.getConexao());
				lisGeral=obPersistencia.consultarPendenciaTipoGrupoGeral( id_grupo);
				for(int i = 0; i < lisGeral.size(); i++) {
					GrupoPendenciaTipoDt obDtTemp = (GrupoPendenciaTipoDt)lisGeral.get(i); 

					tempUlLi+= obDtTemp.getListaLiCheckBox();
				}
				tempUlLi+="</ul>";
			
			}finally{
				obFabricaConexao.fecharConexao();
			}
		return tempUlLi;   
	}

//---------------------------------------------------------
	public void salvar(GrupoPendenciaTipoDt dados ) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;

		//////System.out.println("..neGrupoPendenciaTiposalvar()");
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			GrupoPendenciaTipoPs obPersistencia = new GrupoPendenciaTipoPs(obFabricaConexao.getConexao());
			/* use o iu do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().equalsIgnoreCase("" ) ) {				
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("GrupoPendenciaTipo", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {				
				obPersistencia.alterar(dados);
				obLogDt = new LogDt("GrupoPendenciaTipo", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}


//---------------------------------------------------------
	 public abstract String Verificar(GrupoPendenciaTipoDt dados ); 


//---------------------------------------------------------

	public void excluir(GrupoPendenciaTipoDt dados) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			GrupoPendenciaTipoPs obPersistencia = new GrupoPendenciaTipoPs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("GrupoPendenciaTipo", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId()); dados.limpar();

			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

 //---------------------------------------------------------

	public GrupoPendenciaTipoDt consultarId(String id_grupopendenciatipo ) throws Exception {

		GrupoPendenciaTipoDt dtRetorno=null;
		FabricaConexao obFabricaConexao = null;
		//////System.out.println("..ne-ConsultaId_GrupoPendenciaTipo" );

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			GrupoPendenciaTipoPs obPersistencia = new GrupoPendenciaTipoPs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarId(id_grupopendenciatipo ); 
			obDados.copiar(dtRetorno);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}

//---------------------------------------------------------

	public List consultarDescricao(String descricao, String posicao ) throws Exception {
		List tempList=null;
		FabricaConexao obFabricaConexao = null;
		//////System.out.println("..ne-ConsultaGrupoPendenciaTipo" ); 

			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			try{

				GrupoPendenciaTipoPs obPersistencia = new GrupoPendenciaTipoPs(obFabricaConexao.getConexao());
				tempList=obPersistencia.consultarDescricao( descricao, posicao);
				//stUltimaConsulta=descricao;
				QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
				tempList.remove(tempList.size()-1);
			
			}finally{
				obFabricaConexao.fecharConexao();
			}
		return tempList;   
	}

	public List consultarDescricaoGrupo(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		
		GrupoNe Grupone = new GrupoNe(); 
		tempList = Grupone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = Grupone.getQuantidadePaginas();
		Grupone = null;
		
		return tempList;
	}

	public List consultarDescricaoPendenciaTipo(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		
			PendenciaTipoNe PendenciaTipone = new PendenciaTipoNe(); 
			tempList = PendenciaTipone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
			QuantidadePaginas = PendenciaTipone.getQuantidadePaginas();
			PendenciaTipone = null;
		
		return tempList;
	}

	}
