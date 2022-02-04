package br.gov.go.tj.projudi.ne;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.ProjetoParticipanteDt;
import br.gov.go.tj.projudi.ps.ProjetoParticipantePs;
import br.gov.go.tj.utils.FabricaConexao;
//---------------------------------------------------------
public abstract class ProjetoParticipanteNeGen extends Negocio{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2059996164582138312L;

	protected  ProjetoParticipanteDt obDados;

	protected List lisGeral = null; 
	public ProjetoParticipanteNeGen() {
		
		obLog = new LogNe(); 

		obDados = new ProjetoParticipanteDt(); 

	}


//---------------------------------------------------------
	public void salvarMultiplo(ProjetoParticipanteDt dados, String[] listaEditada) throws Exception {

		LogDt obLogDt;

		////System.out.println("..neProjetoParticipantesalvarMultiplo()");
		//verifico os ids as serem excluidos
		List lisExcluir = new ArrayList();
		//pego a lista geral e procuro os que tem id
		//somente os que tem id podem ser excluidos
		for (int i = 0; i < lisGeral.size(); i++) {
			ProjetoParticipanteDt obDt = (ProjetoParticipanteDt)lisGeral.get(i);
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

		//verifico os ids as serem incluidos
		List lisIncluir = new ArrayList();
		for (int i = 0; i < listaEditada.length; i++)
			for(int j=0; j< lisGeral.size(); j++){
				ProjetoParticipanteDt obDt = (ProjetoParticipanteDt)lisGeral.get(j);
				if (obDt.getId_ServentiaCargo().equalsIgnoreCase((String)listaEditada[i]) && obDt.getId().equalsIgnoreCase("")){
					lisIncluir.add(obDt);
					break;
				}
			}

		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ProjetoParticipantePs obPersistencia = new ProjetoParticipantePs(obFabricaConexao.getConexao());
			for(int i = 0; i < lisIncluir.size(); i++) {
				ProjetoParticipanteDt obDt = (ProjetoParticipanteDt)lisIncluir.get(i);
				obPersistencia.inserir(dados.getId_Projeto(), obDt);
				obLogDt = new LogDt("ProjetoParticipante", obDt.getId() , dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",obDt.getPropriedades());
				obLog.salvar(obLogDt, obFabricaConexao);
			}
			for(int i = 0; i < lisExcluir.size(); i++) {
				ProjetoParticipanteDt obDtTemp = (ProjetoParticipanteDt)lisExcluir.get(i); 

				obLogDt = new LogDt("ProjetoParticipante", obDtTemp.getId() , dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),obDtTemp.getPropriedades(),"");
				obPersistencia.excluir(obDtTemp.getId());
				obLog.salvar(obLogDt, obFabricaConexao);
			}
			obFabricaConexao.finalizarTransacao();

		}catch(Exception e){
			obFabricaConexao.cancelarTransacao();
			////System.out.println("..ne-salvar"+ e.getMessage()); 
			throw e;
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

//---------------------------------------------------------
	public String consultarServentiaCargoProjetoUlLiCheckBox(String id_projeto) throws Exception {

		lisGeral=null;
		FabricaConexao obFabricaConexao = null;
		String tempUlLi="<ul>";
		////System.out.println("..ne-consultarServentiaCargoProjetoUlLiCheckBoxProjetoParticipante" ); 

		try{
			
			//TODO   alterar a busca dos participantes aqui
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			ProjetoParticipantePs obPersistencia = new ProjetoParticipantePs(obFabricaConexao.getConexao());
			lisGeral=obPersistencia.consultarServentiaCargoProjetoGeral( id_projeto);
			for(int i = 0; i < lisGeral.size(); i++) {
				ProjetoParticipanteDt obDtTemp = (ProjetoParticipanteDt)lisGeral.get(i); 

				tempUlLi+= obDtTemp.getListaLiCheckBox();
			}
			tempUlLi+="</ul>";
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return tempUlLi;   
	}

//---------------------------------------------------------
	public void salvar(ProjetoParticipanteDt dados ) throws Exception {

		LogDt obLogDt;

		FabricaConexao obFabricaConexao = null;
		////System.out.println("..neProjetoParticipantesalvar()");
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ProjetoParticipantePs obPersistencia = new ProjetoParticipantePs(obFabricaConexao.getConexao());
			/* use o iu do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().length()==0) {
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("ProjetoParticipante",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {
				obPersistencia.alterar(dados); 
				obLogDt = new LogDt("ProjetoParticipante",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		}catch(Exception e){
			obFabricaConexao.cancelarTransacao();
			////System.out.println("..ne-salvar"+ e.getMessage()); 
			throw e;
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}


//---------------------------------------------------------
	 public abstract String Verificar(ProjetoParticipanteDt dados ); 


//---------------------------------------------------------

	public void excluir(ProjetoParticipanteDt dados) throws Exception {

		LogDt obLogDt;

		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ProjetoParticipantePs obPersistencia = new ProjetoParticipantePs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("ProjetoParticipante",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId());

			dados.limpar();
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

 //---------------------------------------------------------

	public ProjetoParticipanteDt consultarId(String id_projetoparticipante ) throws Exception {

		ProjetoParticipanteDt dtRetorno=null;
		FabricaConexao obFabricaConexao = null;
		////System.out.println("..ne-ConsultaId_ProjetoParticipante" );

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProjetoParticipantePs obPersistencia = new ProjetoParticipantePs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarId(id_projetoparticipante ); 
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
		////System.out.println("..ne-ConsultaProjetoParticipante" ); 

			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			try{

				ProjetoParticipantePs obPersistencia = new ProjetoParticipantePs(obFabricaConexao.getConexao());
				tempList=obPersistencia.consultarDescricao( descricao, posicao);
				QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
				tempList.remove(tempList.size()-1);
			} finally {
				obFabricaConexao.fecharConexao();
			}
		return tempList;   
	}

	public List consultarDescricaoProjeto(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		ProjetoNe Projetone = new ProjetoNe(); 
		tempList = Projetone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = Projetone.getQuantidadePaginas();
		Projetone = null;
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

}
