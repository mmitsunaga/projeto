package br.gov.go.tj.projudi.ne;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.ServentiaSubtipoAssuntoDt;
import br.gov.go.tj.projudi.ps.ServentiaSubtipoAssuntoPs;
import br.gov.go.tj.utils.FabricaConexao;
//---------------------------------------------------------
public abstract class ServentiaSubtipoAssuntoNeGen extends Negocio{


	/**
	 * 
	 */
	private static final long serialVersionUID = -6212268754856314075L;
	protected  ServentiaSubtipoAssuntoDt obDados;

	protected List lisGeral = null; 
	public ServentiaSubtipoAssuntoNeGen() {
		
		obLog = new LogNe(); 

		obDados = new ServentiaSubtipoAssuntoDt(); 

	}


//---------------------------------------------------------
	public void salvarMultiplo(ServentiaSubtipoAssuntoDt dados, String[] listaEditada) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao =null;

		//////System.out.println("..neServentiaSubtipoAssuntosalvarMultiplo()");
		//verifico os ids as serem excluidos
		List lisExcluir = new ArrayList();
		//pego a lista geral e procuro os que tem id
		//somente os que tem id podem ser excluidos
		for (int i = 0; i < lisGeral.size(); i++) {
			ServentiaSubtipoAssuntoDt obDt = (ServentiaSubtipoAssuntoDt)lisGeral.get(i);
			boolean boEncontrado =false;
			//se tiver id vejo que o mesmo não esta mais na lista editada
			if (!obDt.getId().equalsIgnoreCase("")){
				//verifico qual id saiu da lista editada
				for(int j=0; j< listaEditada.length; j++)
					if (obDt.getId_Assunto().equalsIgnoreCase((String)listaEditada[j])){
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
				ServentiaSubtipoAssuntoDt obDt = (ServentiaSubtipoAssuntoDt)lisGeral.get(j);
				if (obDt.getId_Assunto().equalsIgnoreCase((String)listaEditada[i]) && obDt.getId().equalsIgnoreCase("")){
					lisIncluir.add(obDt);
					break;
				}
			}

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ServentiaSubtipoAssuntoPs obPersistencia = new ServentiaSubtipoAssuntoPs(obFabricaConexao.getConexao());
			for(int i = 0; i < lisIncluir.size(); i++) {
				ServentiaSubtipoAssuntoDt obDt = (ServentiaSubtipoAssuntoDt)lisIncluir.get(i);				
				obPersistencia.inserir(obDt);
				obLogDt = new LogDt("ServentiaSubtipoAssunto", obDt.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",obDt.getPropriedades());
				obLog.salvar(obLogDt, obFabricaConexao);
			}
			for(int i = 0; i < lisExcluir.size(); i++) {
				ServentiaSubtipoAssuntoDt obDtTemp = (ServentiaSubtipoAssuntoDt)lisExcluir.get(i); 

				obLogDt = new LogDt("ServentiaSubtipoAssunto", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),obDtTemp.getPropriedades(),"");
				obPersistencia.excluir(obDtTemp.getId());
				obLog.salvar(obLogDt, obFabricaConexao);
			}
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

//---------------------------------------------------------
	public String consultarAssuntoServentiaSubtipoUlLiCheckBox(String id_serventiasubtipo ) throws Exception {

		lisGeral=null;
		FabricaConexao obFabricaConexao =null;
		String tempUlLi="<ul>";
		//////System.out.println("..ne-consultarAssuntoServentiaSubtipoUlLiCheckBoxServentiaSubtipoAssunto" ); 

			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			try{

				ServentiaSubtipoAssuntoPs obPersistencia = new ServentiaSubtipoAssuntoPs(obFabricaConexao.getConexao());
				lisGeral=obPersistencia.consultarAssuntoServentiaSubtipoGeral( id_serventiasubtipo);
				for(int i = 0; i < lisGeral.size(); i++) {
					ServentiaSubtipoAssuntoDt obDtTemp = (ServentiaSubtipoAssuntoDt)lisGeral.get(i); 

					tempUlLi+= obDtTemp.getListaLiCheckBox();
				}
				tempUlLi+="</ul>";
			
			}finally{
				obFabricaConexao.fecharConexao();
			}
		return tempUlLi;   
	}

//---------------------------------------------------------
	public void salvar(ServentiaSubtipoAssuntoDt dados ) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao =null;

		//////System.out.println("..neServentiaSubtipoAssuntosalvar()");
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ServentiaSubtipoAssuntoPs obPersistencia = new ServentiaSubtipoAssuntoPs(obFabricaConexao.getConexao());
			/* use o iu do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().length()==0) {				
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("ServentiaSubtipoAssunto", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {				
				obPersistencia.alterar(dados);
				obLogDt = new LogDt("ServentiaSubtipoAssunto", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}


//---------------------------------------------------------
	 public abstract String Verificar(ServentiaSubtipoAssuntoDt dados ); 


//---------------------------------------------------------

	public void excluir(ServentiaSubtipoAssuntoDt dados) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao =null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ServentiaSubtipoAssuntoPs obPersistencia = new ServentiaSubtipoAssuntoPs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("ServentiaSubtipoAssunto", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId()); dados.limpar();

			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

 //---------------------------------------------------------

	public ServentiaSubtipoAssuntoDt consultarId(String id_serventiasubtipoassunto ) throws Exception {

		ServentiaSubtipoAssuntoDt dtRetorno=null;
		FabricaConexao obFabricaConexao =null;
		//////System.out.println("..ne-ConsultaId_ServentiaSubtipoAssunto" );

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaSubtipoAssuntoPs obPersistencia = new ServentiaSubtipoAssuntoPs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarId(id_serventiasubtipoassunto ); 
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
		FabricaConexao obFabricaConexao =null;
		//////System.out.println("..ne-ConsultaServentiaSubtipoAssunto" ); 

			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			try{

				ServentiaSubtipoAssuntoPs obPersistencia = new ServentiaSubtipoAssuntoPs(obFabricaConexao.getConexao());
				tempList=obPersistencia.consultarDescricao( descricao, posicao);
				QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
				tempList.remove(tempList.size()-1);
			
			}finally{
				obFabricaConexao.fecharConexao();
			}
		return tempList;   
	}

	public List consultarDescricaoServentiaSubtipo(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		
		ServentiaSubtipoNe ServentiaSubtipone = new ServentiaSubtipoNe(); 
		tempList = ServentiaSubtipone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = ServentiaSubtipone.getQuantidadePaginas();
		ServentiaSubtipone = null;
		
		return tempList;
	}

	public List consultarDescricaoAssunto(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		
			AssuntoNe Assuntone = new AssuntoNe(); 
			tempList = Assuntone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
			QuantidadePaginas = Assuntone.getQuantidadePaginas();
			Assuntone = null;
		
		return tempList;
	}
	
	/**
	 * Realiza chamada ao objeto que efetuará a consulta
	 */
	public List consultarDescricaoAssunto(String tempNomeBusca, String id_Serventia, String posicaoPaginaAtual) throws Exception {
		List tempList = null;
		ServentiaSubtipoAssuntoNe neObjeto = new ServentiaSubtipoAssuntoNe();
		AreaDistribuicaoNe areaDistribuicaoNe = new AreaDistribuicaoNe();

		List areasDistribuicoes = areaDistribuicaoNe.consultarAreasDistribuicaoServentia(id_Serventia);
		tempList = neObjeto.consultarAssuntosAreasDistribuicoes(areasDistribuicoes, tempNomeBusca, posicaoPaginaAtual);
		QuantidadePaginas = neObjeto.getQuantidadePaginas();
		
		neObjeto = null;
		return tempList;
	}

	}
