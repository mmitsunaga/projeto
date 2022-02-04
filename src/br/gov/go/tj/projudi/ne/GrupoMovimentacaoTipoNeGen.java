package br.gov.go.tj.projudi.ne;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.GrupoMovimentacaoTipoDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.ps.GrupoMovimentacaoTipoPs;
import br.gov.go.tj.utils.FabricaConexao;
//---------------------------------------------------------
public abstract class GrupoMovimentacaoTipoNeGen extends Negocio{


	/**
	 * 
	 */
	private static final long serialVersionUID = 5653463247918284667L;
	 
	protected  GrupoMovimentacaoTipoDt obDados;

	protected List lisGeral = null; 
	public GrupoMovimentacaoTipoNeGen() {
		

		obLog = new LogNe(); 

		obDados = new GrupoMovimentacaoTipoDt(); 

	}


//---------------------------------------------------------
	public void salvarMultiplo(GrupoMovimentacaoTipoDt dados, String[] listaEditada) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;

		//////System.out.println("..neGrupoMovimentacaoTiposalvarMultiplo()");
		//verifico os ids as serem excluidos
		List lisExcluir = new ArrayList();
		//pego a lista geral e procuro os que tem id
		//somente os que tem id podem ser excluidos
		for (int i = 0; i < lisGeral.size(); i++) {
			GrupoMovimentacaoTipoDt obDt = (GrupoMovimentacaoTipoDt)lisGeral.get(i);
			boolean boEncontrado =false;
			//se tiver id vejo que o mesmo não esta mais na lista editada
			if (!obDt.getId().equalsIgnoreCase("")){
				//verifico qual id saiu da lista editada
				for(int j=0; listaEditada!=null && j< listaEditada.length; j++)
					if (obDt.getId_MovimentacaoTipo().equalsIgnoreCase((String)listaEditada[j])){
						boEncontrado = true;
						break;
					}
			//se o id do objeto não foi encontrado na lista editada coloco o objeto para ser excluido
			if (!boEncontrado) lisExcluir.add(obDt);
			}
		}

		//verifico os ids as serem incluidos
		List lisIncluir = new ArrayList();
		for (int i = 0;listaEditada!=null &&  i < listaEditada.length; i++)
			for(int j=0; j< lisGeral.size(); j++){
				GrupoMovimentacaoTipoDt obDt = (GrupoMovimentacaoTipoDt)lisGeral.get(j);
				if (obDt.getId_MovimentacaoTipo().equalsIgnoreCase((String)listaEditada[i]) && obDt.getId().equalsIgnoreCase("")){
					lisIncluir.add(obDt);
					break;
				}
			}

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			GrupoMovimentacaoTipoPs obPersistencia = new GrupoMovimentacaoTipoPs(obFabricaConexao.getConexao());
			for(int i = 0; i < lisIncluir.size(); i++) {
				GrupoMovimentacaoTipoDt obDt = (GrupoMovimentacaoTipoDt)lisIncluir.get(i);
				
				obPersistencia.inserir(obDt);
				obLogDt = new LogDt("GrupoMovimentacaoTipo", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",obDt.getPropriedades());
				obLog.salvar(obLogDt, obFabricaConexao);
			}
			for(int i = 0; i < lisExcluir.size(); i++) {
				GrupoMovimentacaoTipoDt obDtTemp = (GrupoMovimentacaoTipoDt)lisExcluir.get(i); 

				obLogDt = new LogDt("GrupoMovimentacaoTipo", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),obDtTemp.getPropriedades(),"");
				obPersistencia.excluir(obDtTemp.getId());
				obLog.salvar(obLogDt, obFabricaConexao);
			}
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

//---------------------------------------------------------
	public String consultarMovimentacaoTipoGrupoUlLiCheckBox(String id_grupo ) throws Exception {

		lisGeral=null;
		String tempUlLi="<ul>";
		FabricaConexao obFabricaConexao = null;
		//////System.out.println("..ne-consultarMovimentacaoTipoGrupoUlLiCheckBoxGrupoMovimentacaoTipo" ); 

			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			try{

				GrupoMovimentacaoTipoPs obPersistencia = new GrupoMovimentacaoTipoPs(obFabricaConexao.getConexao());
				lisGeral=obPersistencia.consultarMovimentacaoTipoGrupoGeral( id_grupo);
				for(int i = 0; i < lisGeral.size(); i++) {
					GrupoMovimentacaoTipoDt obDtTemp = (GrupoMovimentacaoTipoDt)lisGeral.get(i); 

					tempUlLi+= obDtTemp.getListaLiCheckBox();
				}
				tempUlLi+="</ul>";
			
			}finally{
				obFabricaConexao.fecharConexao();
			}
		return tempUlLi;   
	}

//---------------------------------------------------------
	public void salvar(GrupoMovimentacaoTipoDt dados ) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;

		//////System.out.println("..neGrupoMovimentacaoTiposalvar()");
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			GrupoMovimentacaoTipoPs obPersistencia = new GrupoMovimentacaoTipoPs(obFabricaConexao.getConexao());
			/* use o iu do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().equalsIgnoreCase("" ) ) {				
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("GrupoMovimentacaoTipo", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {				
				obPersistencia.alterar(dados);
				obLogDt = new LogDt("GrupoMovimentacaoTipo", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}


//---------------------------------------------------------
	 public abstract String Verificar(GrupoMovimentacaoTipoDt dados ); 


//---------------------------------------------------------

	public void excluir(GrupoMovimentacaoTipoDt dados) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			GrupoMovimentacaoTipoPs obPersistencia = new GrupoMovimentacaoTipoPs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("GrupoMovimentacaoTipo", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId()); dados.limpar();

			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

 //---------------------------------------------------------

	public GrupoMovimentacaoTipoDt consultarId(String id_grupomovimentacaotipo ) throws Exception {

		GrupoMovimentacaoTipoDt dtRetorno=null;
		FabricaConexao obFabricaConexao = null;
		//////System.out.println("..ne-ConsultaId_GrupoMovimentacaoTipo" );

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			GrupoMovimentacaoTipoPs obPersistencia = new GrupoMovimentacaoTipoPs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarId(id_grupomovimentacaotipo ); 
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
		//////System.out.println("..ne-ConsultaGrupoMovimentacaoTipo" ); 

			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			try{

				GrupoMovimentacaoTipoPs obPersistencia = new GrupoMovimentacaoTipoPs(obFabricaConexao.getConexao());
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

	public List consultarDescricaoMovimentacaoTipo(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		
			MovimentacaoTipoNe MovimentacaoTipone = new MovimentacaoTipoNe(); 
			tempList = MovimentacaoTipone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
			QuantidadePaginas = MovimentacaoTipone.getQuantidadePaginas();
			MovimentacaoTipone = null;
		
		return tempList;
	}

	}
