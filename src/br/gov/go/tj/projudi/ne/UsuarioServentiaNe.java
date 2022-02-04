package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaDt;
import br.gov.go.tj.projudi.ps.ClassificadorPs;
import br.gov.go.tj.projudi.ps.LogPs;
import br.gov.go.tj.projudi.ps.UsuarioServentiaPs;
import br.gov.go.tj.utils.FabricaConexao;

public class UsuarioServentiaNe extends UsuarioServentiaNeGen {

	private static final long serialVersionUID = 985463884106099991L;

	/**
	 * VERIFICAR CAMPOS OBRIGATÓRIOS
	 */
	public String Verificar(UsuarioServentiaDt dados) {

		String stRetorno = "";
		if (dados.getId_Serventia().equalsIgnoreCase(""))
			stRetorno += "Serventia é campo obrigatório. ";
		if (dados.getId_Usuario().equalsIgnoreCase(""))
			stRetorno += "Usuário é campo obrigatório. ";
		return stRetorno;

	}

	/**
	 * Consultar usuarios da serventia
	 * 
	 * @author lsbernardes
	 * @param String nomeBusca
	 * @param String posicaoPaginaAtual
	 * @param String id_serventia, id da serventia
	 * @return List
	 * @throws Exception
	 */
	public List consultarUsuariosServentia(String nomeBusca, String posicaoPaginaAtual, String id_Serventia) throws Exception {
		List listaUsuariosServentia = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			UsuarioServentiaPs obPersistencia = new UsuarioServentiaPs(obFabricaConexao.getConexao());

			listaUsuariosServentia = obPersistencia.consultarUsuariosServentia(nomeBusca, posicaoPaginaAtual, id_Serventia);
			QuantidadePaginas = (Long) listaUsuariosServentia.get(listaUsuariosServentia.size() - 1);
			listaUsuariosServentia.remove(listaUsuariosServentia.size() - 1);

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return listaUsuariosServentia;
	}

	/**
	 * Método que consulta uma lista de usuários serventia filtrados pelo grupo e serventia deles.
	 * 
	 * @param nomeUsuario - nome do usuário
	 * @param idServentia - ID da serventia dos usuários
	 * @param grupoCodigo - código do grupo dos usuários
	 * @return lista de usuários
	 * @throws Exception
	 * @author hmgodinho
	 */
	public String consultarGrupoEspecificoServentiaJSON(String nomeUsuario, String idServentia, String grupoCodigo, String posicaoPaginaAtual) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			UsuarioServentiaPs obPersistencia = new UsuarioServentiaPs(obFabricaConexao.getConexao());

			stTemp = obPersistencia.consultarGrupoEspecificoServentiaJSON(nomeUsuario, idServentia, grupoCodigo, posicaoPaginaAtual);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}

	/**
	 * Consultar usuarios da serventia dos grupos de advogados
	 * 
	 * @author mmgomes
	 * @param String nomeBusca
	 * @param String posicaoPaginaAtual
	 * @param String id_serventia, id da serventia
	 * @return List
	 * @throws Exception
	 */
	public String consultarUsuariosServentiaAdvogadosJSON(String nomeBusca, String posicaoPaginaAtual, String id_Serventia) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			UsuarioServentiaPs obPersistencia = new UsuarioServentiaPs(obFabricaConexao.getConexao());

			stTemp = obPersistencia.consultarUsuariosServentiaAdvogadosJSON(nomeBusca, posicaoPaginaAtual, id_Serventia);

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}
	
	/**
	 * Consultar usuarios da serventia de todos os grupos
	 * 
	 * @author mmgomes
	 * @param String nomeBusca
	 * @param String posicaoPaginaAtual
	 * @param String id_serventia, id da serventia
	 * @return List
	 * @throws Exception
	 */
	public String consultarUsuariosServentiaTodosJSON(String nomeBusca, String posicaoPaginaAtual, String id_Serventia) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			UsuarioServentiaPs obPersistencia = new UsuarioServentiaPs(obFabricaConexao.getConexao());

			stTemp = obPersistencia.consultarUsuariosServentiaTodosJSON(nomeBusca, posicaoPaginaAtual, id_Serventia);

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}

	public String consultarUsuariosServentiaGrupoJSON(String nomeBusca, String posicaoPaginaAtual, String id_Serventia, String grupo) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			UsuarioServentiaPs obPersistencia = new UsuarioServentiaPs(obFabricaConexao.getConexao());

			stTemp = obPersistencia.consultarUsuariosServentiaGrupoJSON(nomeBusca, posicaoPaginaAtual, id_Serventia, grupo);

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}

	/**
	 * Obter registro em UsuarioServentia de acordo com usuário e serventia passados
	 */
	public UsuarioServentiaDt buscaUsuarioServentiaId(String id_Usuario, String id_Serventia) throws Exception {
		UsuarioServentiaDt dtRetorno = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			UsuarioServentiaPs obPersistencia = new UsuarioServentiaPs(obFabricaConexao.getConexao());
			dtRetorno = obPersistencia.buscaUsuarioServentiaId(id_Usuario, id_Serventia);
			obDados.copiar(dtRetorno);

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}
	
	/**
	 * Obter registro em UsuarioServentia de acordo com id_UsuarioServentia passados
	 */
	public UsuarioServentiaDt buscaUsuarioServentiaIdUsuServ(String id_UsuServ) throws Exception {
		UsuarioServentiaDt dtRetorno = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			UsuarioServentiaPs obPersistencia = new UsuarioServentiaPs(obFabricaConexao.getConexao());
			dtRetorno = obPersistencia.buscaUsuarioServentiaIdUsuServ(id_UsuServ);
			obDados.copiar(dtRetorno);

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}


	/**
	 * Obter registro em UsuarioServentia de acordo com usuário e serventia passados
	 */
	public UsuarioServentiaDt buscaUsuarioServentiaId(String id_Usuario, String id_Serventia, String id_UsuarioServentiaChefe) throws Exception {
		UsuarioServentiaDt dtRetorno = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			UsuarioServentiaPs obPersistencia = new UsuarioServentiaPs(obFabricaConexao.getConexao());
			dtRetorno = obPersistencia.buscaUsuarioServentiaId(id_Usuario, id_Serventia, id_UsuarioServentiaChefe);
			obDados.copiar(dtRetorno);

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}

	public void salvar(UsuarioServentiaDt usuarioServentiaDt, FabricaConexao obFabricaConexao) throws Exception {
		LogDt obLogDt = null;
		UsuarioServentiaDt objDt = new UsuarioServentiaDt();
		UsuarioServentiaPs obPersistencia = new UsuarioServentiaPs(obFabricaConexao.getConexao());
		if (usuarioServentiaDt.getId().equalsIgnoreCase("")) {
			objDt = this.buscaUsuarioServentiaId(usuarioServentiaDt.getId_Usuario(), usuarioServentiaDt.getId_Serventia());
			if (objDt == null || objDt.getId().isEmpty() ||  ( usuarioServentiaDt.isAssessor() && !objDt.isMesmoChefe(usuarioServentiaDt.getId_UsuarioServentiaChefe()))
														 ||  ( objDt.isAssessor() && !usuarioServentiaDt.isAssessor())) {
				obPersistencia.inserir(usuarioServentiaDt);
				obLogDt = new LogDt("UsuarioServentia", usuarioServentiaDt.getId(), usuarioServentiaDt.getId_UsuarioLog(), usuarioServentiaDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir), "", usuarioServentiaDt.getPropriedades());
				obLog.salvar(obLogDt, obFabricaConexao);
			} else {
				usuarioServentiaDt.copiar(objDt);
			}
		} else {
			obPersistencia.alterar(usuarioServentiaDt);
			obLogDt = new LogDt("UsuarioServentia", usuarioServentiaDt.getId(), usuarioServentiaDt.getId_UsuarioLog(), usuarioServentiaDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar), obDados.getPropriedades(), usuarioServentiaDt.getPropriedades());
			obLog.salvar(obLogDt, obFabricaConexao);
		}
		obDados.copiar(usuarioServentiaDt);
	}

	/**
	 * Monta objeto UsuarioServentia baseado no objeto Usuario passado e chama método salvar
	 * 
	 * @param usuarioDt, objeto com dados do usuário
	 * @param logDt, objeto com dados do log
	 * @param conexao, conexão ativa
	 * 
	 * @author msapaula
	 */
//
//	public String salvarUsuarioServentia(UsuarioServentiaDt usuarioServentiaDt, UsuarioDt usuarioDt, LogDt logDt, FabricaConexao conexao) throws Exception {
//
//		usuarioServentiaDt.setId(usuarioDt.getId_UsuarioServentia());
//		usuarioServentiaDt.setId_Serventia(usuarioDt.getId_Serventia());
//		usuarioServentiaDt.setId_Usuario(usuarioDt.getId());
//		usuarioServentiaDt.setAtivo(String.valueOf(UsuarioServentiaDt.ATIVO));
//		usuarioServentiaDt.setId_UsuarioLog(logDt.getId_Usuario());
//		usuarioServentiaDt.setIpComputadorLog(logDt.getIpComputador());
//		this.salvar(usuarioServentiaDt, conexao);
//
//		return usuarioServentiaDt.getId();
//	}
//

	// CONSULTA (UsuarioServentia) ATIVOS OU INATIVOS POR ID USUARIO
	public List consultarDescricaoUsuarioServentia(String id_Usuario) throws Exception {
		List tempList = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			UsuarioServentiaPs obPersistencia = new UsuarioServentiaPs(obFabricaConexao.getConexao());
			tempList = obPersistencia.consultarDescricaoUsuarioServentia(id_Usuario);

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return tempList;
	}

	/**
	 * Método responsável em consultar o id_UsuarioServentia para uma parte de acordo com o CPF passado
	 * 
	 * @param String cpf, cpf da parte
	 */
	public UsuarioServentiaDt consultarUsuarioServentiaParte(String cpf) throws Exception {
		UsuarioServentiaDt usuarioServentiaDt = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			UsuarioServentiaPs obPersistencia = new UsuarioServentiaPs(obFabricaConexao.getConexao());
			usuarioServentiaDt = obPersistencia.consultarUsuarioServentiaParte(cpf);

		} finally {
			obFabricaConexao.fecharConexao();
		}

		return usuarioServentiaDt;
	}

	/**
	 * HABILITA OU DESABILITA USUARIO EM UMA SERVENTIA (UsuarioServentia)
	 */
	public void habilitarUsuarioServentia(UsuarioDt usuarioDt) throws Exception {
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();

			// ATUALIZAR UsuarioServentia
			UsuarioServentiaNe us = new UsuarioServentiaNe();
			UsuarioServentiaDt usuarioServentiaDt = new UsuarioServentiaDt();
			usuarioServentiaDt.setId(usuarioDt.getId_UsuarioServentia());

			// HABILITA E DESABILITA USUARIOSERVENTIA (ADVOGADO OAB)
			if (!usuarioDt.getUsuarioServentiaAtivo().equalsIgnoreCase(""))
				usuarioServentiaDt.setAtivo(usuarioDt.getUsuarioServentiaAtivo());

			usuarioServentiaDt.setId_UsuarioLog(usuarioDt.getId_UsuarioLog());
			usuarioServentiaDt.setIpComputadorLog(usuarioDt.getIpComputadorLog());
			us.salvar(usuarioServentiaDt, obFabricaConexao);

			obFabricaConexao.finalizarTransacao();
		} catch (Exception e) {
			obFabricaConexao.cancelarTransacao();
			throw e;
		} finally {
			obFabricaConexao.fecharConexao();
		}
	}

	/**
	 * Ativa um usuário em uma serventia
	 * 
	 * @param usuarioDt, objeto com dados para ativação
	 * @author msapaula
	 */
	public void ativarUsuarioServentia(UsuarioServentiaDt usuarioServentiaDt) throws Exception {
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ativarUsuarioServentia(usuarioServentiaDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();
		} catch (Exception e) {
			obFabricaConexao.cancelarTransacao();
			throw e;
		} finally {
			obFabricaConexao.fecharConexao();
		}
	}

	public void ativarUsuarioServentia(UsuarioServentiaDt usuarioServentiaDt, FabricaConexao obFabricaConexao) throws Exception {
		UsuarioServentiaPs obPersistencia = new UsuarioServentiaPs(obFabricaConexao.getConexao());

		String valorAtual = "[Id_UsuarioServentia:" + usuarioServentiaDt.getId() + ";Ativo:" + UsuarioServentiaDt.INATIVO + "]";
		String valorNovo = "[Id_UsuarioServentia:" + usuarioServentiaDt.getId() + ";Ativo:" + UsuarioServentiaDt.ATIVO + "]";

		LogDt obLogDt = new LogDt("UsuarioServentia", usuarioServentiaDt.getId(), usuarioServentiaDt.getId_UsuarioLog(), usuarioServentiaDt.getIpComputadorLog(), String.valueOf(LogTipoDt.HabilitacaoUsuario), valorAtual, valorNovo);
		obPersistencia.alterarStatusUsuarioServentia(usuarioServentiaDt.getId(), UsuarioServentiaDt.ATIVO);

		obLog.salvar(obLogDt, obFabricaConexao);
	}

	/**
	 * Ativa um usuário em uma serventia
	 * 
	 * @data 19/07/2013
	 * @param usuarioDt, objeto com dados para ativação
	 * @author jrcorrea
	 */
	public void ativarAdvogado(UsuarioServentiaDt usuarioServentiaDt) throws Exception {
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			UsuarioServentiaPs obPersistencia = new UsuarioServentiaPs(obFabricaConexao.getConexao());
			UsuarioServentiaGrupoNe obUsuarioServentiaGrupo = new UsuarioServentiaGrupoNe();

			String valorAtual = "[Id_UsuarioServentia:" + usuarioServentiaDt.getId() + ";Ativo:" + UsuarioServentiaDt.INATIVO + "]";
			String valorNovo = "[Id_UsuarioServentia:" + usuarioServentiaDt.getId() + ";Ativo:" + UsuarioServentiaDt.ATIVO + "]";

			LogDt obLogDt = new LogDt("UsuarioServentia", usuarioServentiaDt.getId(), usuarioServentiaDt.getId_UsuarioLog(), usuarioServentiaDt.getIpComputadorLog(), String.valueOf(LogTipoDt.HabilitacaoUsuario), valorAtual, valorNovo);
			obPersistencia.alterarStatusUsuarioServentia(usuarioServentiaDt.getId(), UsuarioServentiaDt.ATIVO);

			obUsuarioServentiaGrupo.ativarAdvogado(usuarioServentiaDt.getId(), usuarioServentiaDt.getId_UsuarioLog(), usuarioServentiaDt.getIpComputadorLog(), obFabricaConexao);

			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();
		} catch (Exception e) {
			obFabricaConexao.cancelarTransacao();
			throw e;
		} finally {
			obFabricaConexao.fecharConexao();
		}
	}

	/**
	 * Desativa um usuário em uma serventia
	 * 
	 * @param usuarioDt, objeto com dados para desativar
	 * 
	 * @author msapaula
	 */
	public void desativarUsuarioServentia(UsuarioServentiaDt usuarioServentiaDt) throws Exception {
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			desativarUsuarioServentia(usuarioServentiaDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();
		} catch (Exception e) {
			obFabricaConexao.cancelarTransacao();
			throw e;
		} finally {
			obFabricaConexao.fecharConexao();
		}
	}

	public void desativarUsuarioServentia(UsuarioServentiaDt usuarioServentiaDt, FabricaConexao obFabricaConexao) throws Exception {
		UsuarioServentiaPs obPersistencia = new UsuarioServentiaPs(obFabricaConexao.getConexao());

		String valorAtual = "[Id_UsuarioServentia:" + usuarioServentiaDt.getId() + ";Ativo:" + UsuarioServentiaDt.ATIVO + "]";
		String valorNovo = "[Id_UsuarioServentia:" + usuarioServentiaDt.getId() + ";Ativo:" + UsuarioServentiaDt.INATIVO + "]";

		LogDt obLogDt = new LogDt("UsuarioServentia", usuarioServentiaDt.getId(), usuarioServentiaDt.getId_UsuarioLog(), usuarioServentiaDt.getIpComputadorLog(), String.valueOf(LogTipoDt.DesabilitacaoUsuario), valorAtual, valorNovo);
		obPersistencia.alterarStatusUsuarioServentia(usuarioServentiaDt.getId(), UsuarioServentiaDt.INATIVO);

		obLog.salvar(obLogDt, obFabricaConexao);
	}

	/**
	 * Método responsável em desativar todos os UsuarioServentia de um usuário em virtude da desativação do usuário
	 * 
	 * @param id_Usuario, identificação do usuário
	 * @author msapaula
	 */
	public void desativarUsuarioServentias(String id_Usuario, FabricaConexao obFabricaConexao) throws Exception {

		UsuarioServentiaPs obPersistencia = new UsuarioServentiaPs(obFabricaConexao.getConexao());
		obPersistencia.alterarStatusId_Usuario(id_Usuario, UsuarioServentiaDt.INATIVO);

	}

	/**
	 * Método responsável em ativar todos os UsuarioServentia de um usuário em virtude da ativação do usuário
	 * 
	 * @param id_Usuario, identificação do usuário
	 * @author msapaula
	 */
	public void ativarUsuarioServentias(String id_Usuario, FabricaConexao obFabricaConexao) throws Exception {

		UsuarioServentiaPs obPersistencia = new UsuarioServentiaPs(obFabricaConexao.getConexao());
		obPersistencia.alterarStatusId_Usuario(id_Usuario, UsuarioServentiaDt.ATIVO);

	}

	/**
	 * Busca serventias vinculadas a um usuário
	 */
	public List consultarServentiasUsuario(String usuario) throws Exception {
		List tempList = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			UsuarioServentiaPs obPersistencia = new UsuarioServentiaPs(obFabricaConexao.getConexao());
			tempList = obPersistencia.consultarServentiasUsuario(usuario);

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return tempList;
	}

	public String consultarDescricaoJSON(String descricao, String posicao) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			UsuarioServentiaPs obPersistencia = new UsuarioServentiaPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarDescricaoJSON(descricao, posicao);

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}

	/**
	 * Método para verificar se usuário é contador.
	 * 
	 * @param String idUsurio
	 * @return boolean
	 * @throws Exception
	 */
	public boolean isUsuarioContador(String idUsuario) throws Exception {
		boolean retorno = false;

		FabricaConexao obFabricaConexao = null;
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			UsuarioServentiaPs obPersistencia = new UsuarioServentiaPs(obFabricaConexao.getConexao());
			retorno = obPersistencia.isUsuarioContador(idUsuario);

		} finally {
			obFabricaConexao.fecharConexao();
		}

		return retorno;
	}
	
	/**
	 * Método para verificar se usuário é advogado.
	 * 
	 * @param String idUsurio
	 * @param String idServentia
	 * @return boolean
	 * @throws Exception
	 * 
	 * @author fasoares
	 */
	public boolean isUsuarioAdvogado(String idUsuario, String idServentia) throws Exception {
		boolean retorno = false;

		FabricaConexao obFabricaConexao = null;
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			UsuarioServentiaPs obPersistencia = new UsuarioServentiaPs(obFabricaConexao.getConexao());
			retorno = obPersistencia.isUsuarioAdvogado(idUsuario, idServentia);

		} finally {
			obFabricaConexao.fecharConexao();
		}

		return retorno;
	}
	
	/**
	 * Método para verificar se usuário é Gerenciamento Projudi.
	 * 
	 * @param String idUsurio
	 * @return boolean
	 * @throws Exception
	 */
	public boolean isUsuarioGerenciamentoProjudi(String idUsuario) throws Exception {
		boolean retorno = false;

		FabricaConexao obFabricaConexao = null;
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			UsuarioServentiaPs obPersistencia = new UsuarioServentiaPs(obFabricaConexao.getConexao());
			retorno = obPersistencia.isUsuarioGerenciamentoProjudi(idUsuario);

		} finally {
			obFabricaConexao.fecharConexao();
		}

		return retorno;
	}

	/**
	 * Consulta de usuarios da serventia que podem analisar pendências em uma determinada serventia.
	 * 
	 * @param id_Serventia
	 * @return
	 * @throws Exception
	 */
	public List consultarTodosUsuariosAnalisamPendencias(String id_Serventia) throws Exception {
		List retorno = null;

		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			UsuarioServentiaPs obPersistencia = new UsuarioServentiaPs(obFabricaConexao.getConexao());
			retorno = obPersistencia.consultarTodosUsuariosAnalisamPendencias(id_Serventia);

		} finally {
			obFabricaConexao.fecharConexao();
		}

		return retorno;
	}

	/**
	 * Método para verificar se foi um magistrado que gerou determinada movimentação
	 * 
	 * @param id_Serventia
	 * @return boolean
	 * @throws Exception
	 */
	public boolean consultarGrupoUsuarioServentia(String id_usuarioserventia) throws Exception {
		boolean retorno = false;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			UsuarioServentiaPs obPersistencia = new UsuarioServentiaPs(obFabricaConexao.getConexao());
			retorno = obPersistencia.consultarGrupoUsuarioServentia(id_usuarioserventia);

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return retorno;
	}

	public List consultarProcuradoresServentia(String posicaoPaginaAtual, String id_Serventia) throws Exception {
		List listaUsuariosServentia = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			UsuarioServentiaPs obPersistencia = new UsuarioServentiaPs(obFabricaConexao.getConexao());

			listaUsuariosServentia = obPersistencia.consultarProcuradoresServentia(posicaoPaginaAtual, id_Serventia);
			QuantidadePaginas = (Long) listaUsuariosServentia.get(listaUsuariosServentia.size() - 1);
			listaUsuariosServentia.remove(listaUsuariosServentia.size() - 1);

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return listaUsuariosServentia;
	}

	public String consultarDescricaoCidadeJSON(String cidade, String uf, String PosicaoPaginaAtual) throws Exception {
		String stTemp = "";

		CidadeNe Cidadene = new CidadeNe();
		stTemp = Cidadene.consultarDescricaoJSON(cidade, uf, PosicaoPaginaAtual);

		return stTemp;
	}

	public String consultarDescricaoServidorJudiciarioServentiaJSON(String nome, String usuario, String posicao, String idServentia) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			UsuarioServentiaPs obPersistencia = new UsuarioServentiaPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarDescricaoServidorJudiciarioServentiaJSON(nome, usuario, posicao, idServentia);

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}

	public void libereAssistenteGuardarParaAssinar(UsuarioDt usuarioDt) throws Exception {
		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{


			obFabricaConexao.iniciarTransacao();

			UsuarioServentiaPs obPersistencia = new UsuarioServentiaPs(obFabricaConexao.getConexao());

			UsuarioServentiaDt usuarioServentiaDt = obPersistencia.consultarId(usuarioDt.getId_UsuarioServentia());

			String valoresAnteriores = usuarioServentiaDt.getPropriedades();

			usuarioServentiaDt.setPodeGuardarAssinarUsuarioServentiaChefe("true");

			obPersistencia.alterar(usuarioServentiaDt);

			obLogDt = new LogDt("UsuarioServentia", usuarioServentiaDt.getId(), usuarioDt.getId_UsuarioLog(), usuarioDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar), valoresAnteriores, usuarioServentiaDt.getPropriedades());

			obLog.salvar(obLogDt, obFabricaConexao);

			usuarioDt.setPodeGuardarAssinarUsuarioServentiaChefe("true");

			obFabricaConexao.finalizarTransacao();
		} catch (Exception e) {
			obFabricaConexao.cancelarTransacao();
			throw e;
		} finally {
			obFabricaConexao.fecharConexao();
		}
	}

	public void bloqueieAssistenteGuardarParaAssinar(UsuarioDt usuarioDt) throws Exception {
		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{


			obFabricaConexao.iniciarTransacao();

			UsuarioServentiaPs obPersistencia = new UsuarioServentiaPs(obFabricaConexao.getConexao());

			UsuarioServentiaDt usuarioServentiaDt = obPersistencia.consultarId(usuarioDt.getId_UsuarioServentia());

			String valoresAnteriores = usuarioServentiaDt.getPropriedades();

			usuarioServentiaDt.setPodeGuardarAssinarUsuarioServentiaChefe("false");

			obPersistencia.alterar(usuarioServentiaDt);

			obLogDt = new LogDt("UsuarioServentia", usuarioServentiaDt.getId(), usuarioDt.getId_UsuarioLog(), usuarioDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar), valoresAnteriores, usuarioServentiaDt.getPropriedades());

			obLog.salvar(obLogDt, obFabricaConexao);

			usuarioDt.setPodeGuardarAssinarUsuarioServentiaChefe("false");

			obFabricaConexao.finalizarTransacao();
		} catch (Exception e) {
			obFabricaConexao.cancelarTransacao();
			throw e;
		} finally {
			obFabricaConexao.fecharConexao();
		}
	}

	public UsuarioServentiaDt consultarId(String id_usuarioserventia, FabricaConexao obFabricaConexao) throws Exception {
		UsuarioServentiaDt dtRetorno = null;
		UsuarioServentiaPs obPersistencia = new UsuarioServentiaPs(obFabricaConexao.getConexao());
		dtRetorno = obPersistencia.consultarId(id_usuarioserventia);
		obDados.copiar(dtRetorno);
		return dtRetorno;
	}

	// metodo que retorna o nome e o grupo do responsável pela pendência, quando houver alteração. Busca com base em id_ServentiaCargo.
	public String consultarNomePendenciaResponsavel(String idServentiaCargo) throws Exception {

		FabricaConexao obFabricaConexao = null;
		String pendenciaResponsavelNome = "";
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);

			UsuarioServentiaPs obPersistencia = new UsuarioServentiaPs(obFabricaConexao.getConexao());

			pendenciaResponsavelNome = obPersistencia.consultarNomePendenciaResponsavel(idServentiaCargo);

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return pendenciaResponsavelNome;
	}
	
	// jvosantos - 26/11/2019 14:21 - Método para buscar o ID_USU_SERV usando o ID_SERV_CARGO
	public String consultarIdUsuarioServentiaPorIdServentiaCargo(String idServentiaCargo) throws Exception {

		FabricaConexao obFabricaConexao = null;
		String pendenciaResponsavelNome = "";
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);

			UsuarioServentiaPs obPersistencia = new UsuarioServentiaPs(obFabricaConexao.getConexao());

			pendenciaResponsavelNome = obPersistencia.consultarIdUsuarioServentiaPorIdServentiaCargo(idServentiaCargo);

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return pendenciaResponsavelNome;
	}
	
	/**
	 * Desativa um usuário em uma serventia
	 * 
	 * @param usuarioServentiaDt, objeto com dados para ativar ou desativar
	 * @param statusAtual, status atual do usuário serventia
	 * @param statusNovo, status novo do usuário serventia
	 * @param logTipo, tipo de log que será utilizado
	 * 
	 * 
	 * @author lsbernardes
	 */
	public void ativarDesativarUsuarioServentiaAdvogadoProcurador(UsuarioServentiaDt usuarioServentiaDt, int statusAtual, int statusNovo, int logTipo) throws Exception {
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			UsuarioServentiaPs obPersistencia = new UsuarioServentiaPs(obFabricaConexao.getConexao());

			String valorAtual = "[Id_UsuarioServentia:" + usuarioServentiaDt.getId() + ";Ativo:" + statusAtual + "]";
			String valorNovo = "[Id_UsuarioServentia:" + usuarioServentiaDt.getId() + ";Ativo:" + statusNovo + "]";

			LogDt obLogDt = new LogDt("UsuarioServentia", usuarioServentiaDt.getId(), usuarioServentiaDt.getId_UsuarioLog(), usuarioServentiaDt.getIpComputadorLog(), String.valueOf(logTipo), valorAtual, valorNovo);
			obPersistencia.alterarStatusUsuarioServentia(usuarioServentiaDt.getId(), statusNovo);

			obLog.salvar(obLogDt, obFabricaConexao);
		} finally {
			obFabricaConexao.fecharConexao();
		}
	}
	
	/**
	 * Método responsável em alterar status do UsuarioServentia a partir de um usuario serventia grupo
	 * 
	 * @param id_UsuServGrupo, identificação do usuário serventia grupo
	 * @author asrocha
	 */
	public void ativarUsuarioServentia(String id_UsuServGrupo, FabricaConexao obFabricaConexao) throws Exception {

		UsuarioServentiaPs obPersistencia = new UsuarioServentiaPs(obFabricaConexao.getConexao());
		obPersistencia.ativarUsuarioServentia(id_UsuServGrupo);

	}
	
	/**
	 * Obter registro em UsuarioServentia de acordo com usuário e serventia passados
	 */
	public UsuarioServentiaDt consultaUsuarioServentiaId(String id_Usuario, String id_Serventia) throws Exception {
		UsuarioServentiaDt dtRetorno = null;
		FabricaConexao obFabricaConexao = null;
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			UsuarioServentiaPs obPersistencia = new UsuarioServentiaPs(obFabricaConexao.getConexao());
			dtRetorno = obPersistencia.consultaUsuarioServentiaId(id_Usuario, id_Serventia);
			obDados.copiar(dtRetorno);

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}

	

}
