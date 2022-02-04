package br.gov.go.tj.projudi.ne;


import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.ProjetoParticipanteDt;
import br.gov.go.tj.projudi.ps.ProjetoParticipantePs;
import br.gov.go.tj.projudi.ps.ProjetoPs;
import br.gov.go.tj.utils.FabricaConexao;

@SuppressWarnings("unchecked")
public class ProjetoParticipanteNe extends ProjetoParticipanteNeGen{

    private static final long serialVersionUID = 9196046798125651702L;

	public  String Verificar(ProjetoParticipanteDt dados ) {
		String stRetorno="";
		if (dados.getId_Projeto().length()==0)
			stRetorno += "O Campo Id_Projeto é obrigatório.";
		return stRetorno;
	}
	
	/**
	 * Recupera o ID da participante do projeto com base na serventiaCargo.
	 * @param idServentiaCargo
	 * @param cn
	 * @return
	 * @throws Exception
	 */
	public String consultarIdParticipanteByServentiaCargo(String idServentiaCargo, FabricaConexao cn) throws Exception {
		FabricaConexao obFabricaConexao = null;
		try{
			if (cn == null) 
				obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			else obFabricaConexao = cn;
			ProjetoParticipantePs obPersistencia = new ProjetoParticipantePs(obFabricaConexao.getConexao());
			return obPersistencia.consultarId_ParticipanteServentiaCargo(idServentiaCargo); 
		
		}finally{
			if (cn == null)
				obFabricaConexao.fecharConexao();
		}
	}
	
	/**
	 * Recupera somente os analistas de TI.
	 * @param tempNomeBusca
	 * @param PosicaoPaginaAtual
	 * @return
	 * @throws Exception
	 */
	public List consultarDescricaoProjetoParticipante(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		//ProjetoParticipanteNe ProjetoParticipantene = new ProjetoParticipanteNe(); 
		tempList = this.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = this.getQuantidadePaginas();
		//ProjetoParticipantene = null;
		return tempList;
	}
	
	/**
	 * Consulta os participantes do projeto segundo as informações repassadas.
	 * @param tempNomeBusca - nome do projeto
	 * @param idProjeto - id do projeto
	 * @param posicao - posição da página
	 * @return lista de participantes
	 * @throws Exception
	 * @author hmgodinho
	 */
	public List consultarParticipanteDescricaoProjeto(String tempNomeBusca, String idProjeto, String posicaoPaginaAtual) throws Exception {
		List tempList=null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProjetoParticipantePs obPersistencia = new ProjetoParticipantePs(obFabricaConexao.getConexao());
			tempList = obPersistencia.consultarParticipanteDescricaoProjeto(tempNomeBusca, idProjeto, posicaoPaginaAtual);
			QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
			tempList.remove(tempList.size()-1);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;
	}
	
	/**
	 * Recupera todos os registros vinculados a serventia.
	 * @param id_projeto
	 * @param idServentia
	 * @return
	 * @throws Exception
	 */
	public String consultarServentiaCargoProjetoUlLiCheckBox(String id_projeto, String idServentia) throws Exception {

		lisGeral = null;
		FabricaConexao obFabricaConexao = null;
		String tempUlLi = "<ul>";
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProjetoParticipantePs obPersistencia = new ProjetoParticipantePs(obFabricaConexao.getConexao());
			
			//Primeiro tenta consultar os usuários da serventia cadastrados no projeto
			lisGeral = obPersistencia.consultarServentiaCargoProjetoGeral(id_projeto, idServentia);
			//Se a lista vier vazia, traz todos os usuários vinculados à serventia, indpendente do projeto
			List listaTodosUsuariosServentia = obPersistencia.consultarServentiaCargoGeral(idServentia);
			//Insere na lisGeral os usuários que pertencem à serventia mas nao estão no projeto
			for (int i = 0; i < listaTodosUsuariosServentia.size(); i++) {
				ProjetoParticipanteDt usuario = (ProjetoParticipanteDt)listaTodosUsuariosServentia.get(i);
				boolean inserirLista = true;
				for (int j = 0; j < lisGeral.size(); j++) {
					ProjetoParticipanteDt usuarioLista = (ProjetoParticipanteDt)lisGeral.get(j);
					if(usuarioLista.getId_ServentiaCargo().equals(usuario.getId_ServentiaCargo())){
						inserirLista = false;
					}
				}
				if(inserirLista){
					lisGeral.add(usuario);
				}
			}
			
			for (int i = 0; i < lisGeral.size(); i++) {
				ProjetoParticipanteDt obDtTemp = (ProjetoParticipanteDt) lisGeral.get(i);
				tempUlLi += obDtTemp.getListaLiCheckBox(id_projeto);
			}
			tempUlLi += "</ul>";
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return tempUlLi;
	}
	
	/**
	 * Salva a alteração múltipla de usuários de um projeto.
	 * @param dados - dados do usuário
	 * @param listaEditada - lista editada na tela
	 * @return mensagem de erro se houver
	 * @throws Exception
	 */
	public String salvarMultiplos(ProjetoParticipanteDt dados, String[] listaEditada) throws Exception{

		LogDt obLogDt;
		String mensagemErro = "";

		//verifico os ids as serem excluidos
		List lisExcluir = new ArrayList();
		//pego a lista geral e procuro os que tem id
		//somente os que tem id podem ser excluidos
		for (int i = 0; i < lisGeral.size(); i++) {
			ProjetoParticipanteDt obDt = (ProjetoParticipanteDt)lisGeral.get(i);
			boolean boEncontrado =false;
			//se tiver id vejo que o mesmo não esta mais na lista editada
			if (!obDt.getId().equalsIgnoreCase("")){
				//se a listaEditada for null é porque todos os registros da tela
				//não estão selecionados e todos deverão ser excluídos
				if(listaEditada != null){
					//verifico qual id saiu da lista editada
					for(int j=0; j< listaEditada.length; j++)
						if (obDt.getId_ServentiaCargo().equalsIgnoreCase((String)listaEditada[j])){
							boEncontrado = true;
							break;
						}
				} else {
					boEncontrado = false;
				}
			//se o id do objeto não foi encontrado na lista editada coloco o objeto para ser excluido
			if (!boEncontrado) lisExcluir.add(obDt);
			}
		}

		//verifico os ids as serem incluidos
		List lisIncluir = new ArrayList();
		//se a listaEditada for null, é porque todos os registros
		//da tela estão desmarcados e não há o que inserir
		if(listaEditada != null) {
			for (int i = 0; i < listaEditada.length; i++)
				for(int j=0; j< lisGeral.size(); j++){
					ProjetoParticipanteDt obDt = (ProjetoParticipanteDt)lisGeral.get(j);
					if (obDt.getId_ServentiaCargo().equalsIgnoreCase((String)listaEditada[i]) && obDt.getId().equalsIgnoreCase("")){
						lisIncluir.add(obDt);
						break;
					}
				}
		}

		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ProjetoParticipantePs obPersistencia = new ProjetoParticipantePs(obFabricaConexao.getConexao());
			for(int i = 0; i < lisExcluir.size(); i++) {
				ProjetoParticipanteDt obDtTemp = (ProjetoParticipanteDt)lisExcluir.get(i); 
				if(!obPersistencia.consultarExisteTarefasUsuarioProjeto(obDtTemp.getId_Projeto(), obDtTemp.getId())) {
					obLogDt = new LogDt("ProjetoParticipante", obDtTemp.getId() , dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),obDtTemp.getPropriedades(),"");
					obPersistencia.excluir(obDtTemp.getId());
					obLog.salvar(obLogDt, obFabricaConexao);
				} else {
					mensagemErro = "Usuário "+ obDtTemp.getServentiaCargo() +" não pode ser excluído pois possui tarefas cadastradas no seu perfil.";
					//Se der erro, interrrompe a exclusão e inclusão aqui mesmo
					break;
				}
			}
			for(int i = 0; i < lisIncluir.size(); i++) {
				ProjetoParticipanteDt obDt = (ProjetoParticipanteDt)lisIncluir.get(i);
				obPersistencia.inserir(dados.getId_Projeto(), obDt);
				obLogDt = new LogDt("ProjetoParticipante", obDt.getId() , dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",obDt.getPropriedades());
				obLog.salvar(obLogDt, obFabricaConexao);
			}
			obFabricaConexao.finalizarTransacao();
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		
		return mensagemErro;
	}
	
	public String consultarServentiaCargoProjetoUlLiCheckBox(String id_projeto) throws Exception {

		lisGeral=null;
		FabricaConexao obFabricaConexao = null;
		String tempUlLi="<ul>";

			try{
				
				//TODO   alterar a busca dos participantes aqui
				obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
				ProjetoParticipantePs obPersistencia = new ProjetoParticipantePs(obFabricaConexao.getConexao());
				lisGeral=obPersistencia.consultarServentiaCargoProjetoGeral( id_projeto);
				for(int i = 0; i < lisGeral.size(); i++) {
					ProjetoParticipanteDt obDtTemp = (ProjetoParticipanteDt)lisGeral.get(i); 

					tempUlLi+= obDtTemp.getListaLiCheckBox(id_projeto);
				}
				tempUlLi+="</ul>";
			
			}finally{
				obFabricaConexao.fecharConexao();
			}
		return tempUlLi;   
	}

	public String consultarDescricaoProjetoJSON(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		String stTemp = "";
		ProjetoNe Projetone = new ProjetoNe(); 
		stTemp = Projetone.consultarDescricaoJSON(tempNomeBusca, PosicaoPaginaAtual);
		Projetone = null;
		return stTemp;
	}

}
