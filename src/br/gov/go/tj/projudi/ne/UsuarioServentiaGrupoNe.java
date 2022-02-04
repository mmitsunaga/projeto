package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaGrupoDt;
import br.gov.go.tj.projudi.ps.UsuarioServentiaGrupoPs;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;

// ---------------------------------------------------------
public class UsuarioServentiaGrupoNe extends UsuarioServentiaGrupoNeGen {

	/**
     * 
     */
    private static final long serialVersionUID = 5858337603590778392L;

    public String Verificar(UsuarioServentiaGrupoDt dados) {

		String stRetorno = "";
		if (dados.getId_Usuario().equalsIgnoreCase("")) stRetorno += "É necessário selecionar um Usuário. \n";
		if (dados.getId_Serventia().equalsIgnoreCase("")) stRetorno += "É necessário selecionar uma Serventia. \n";
		if (dados.getId_Grupo().equalsIgnoreCase("")) stRetorno += "É necessário selecionar um Grupo. \n";
		return stRetorno;

	}

	/**
	 * Verificar Campos Obrigatórios ao habilitar um usuário em serventia e grupo
	 * 
	 * @author leandro
	 */
	public String VerificarServidorJudiciario(UsuarioDt dados) throws Exception {
		String stRetorno = "";
		if (dados.getId().equalsIgnoreCase("")) stRetorno += "É necessário selecionar um Usuário. \n";
		if (dados.getId_Serventia().equalsIgnoreCase("")) stRetorno += "Selecione uma Serventia para Habilitação. \n";
		if (dados.getId_Grupo().equalsIgnoreCase("")) stRetorno += "Selecione o Grupo do usuário. \n";
		return stRetorno;
	}

	/**
	 * Busca serventias e grupos vinculados a um usuário no momento após o logon, para que usuário
	 * possa definir em qual serventia e grupo irá trabalhar
	 * 
	 * @param id_Usuario, identificação do usuário que está logando
	 * @return lista de Usuários com todos os dados de serventia e grupo setados
	 */
	public List consultarServentiasGrupos(String id_usuario) throws Exception {
		List liTemp = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			UsuarioServentiaGrupoPs obPersistencia = new UsuarioServentiaGrupoPs(obFabricaConexao.getConexao());
			liTemp = obPersistencia.consultarServentiasGrupos(id_usuario);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return liTemp;
	}
	
	public List consultarServentiasGrupos(String id_usuario, FabricaConexao obFabricaConexao) throws Exception {
		UsuarioServentiaGrupoPs obPersistencia = new UsuarioServentiaGrupoPs(obFabricaConexao.getConexao());
		return obPersistencia.consultarServentiasGrupos(id_usuario);
	}
	
	public List consultarServentiasGrupos(String id_usuario,
										  String id_usuarioServentia, 
										  String grupoCodigo,
										  String id_serventiaCargo,
									      String id_serventiaCargoUsuarioChefe,
									      String id_usuarioServentiaChefe) throws Exception {
		List liTemp = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			UsuarioServentiaGrupoPs obPersistencia = new UsuarioServentiaGrupoPs(obFabricaConexao.getConexao());
			liTemp = obPersistencia.consultarServentiasGrupos(id_usuario,
															id_usuarioServentia,
															grupoCodigo,
															id_serventiaCargo,
															id_serventiaCargoUsuarioChefe,
															id_usuarioServentiaChefe);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return liTemp;
	}

	public void salvar(UsuarioServentiaGrupoDt dados, FabricaConexao obFabricaConexao) throws Exception {
		LogDt obLogDt;
		
		UsuarioServentiaGrupoPs obPersistencia = new UsuarioServentiaGrupoPs(obFabricaConexao.getConexao());
		if (dados.getId().equalsIgnoreCase("")) {				
			obPersistencia.inserir(dados);
			obLogDt = new LogDt("UsuarioServentiaGrupo", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir), "", dados.getPropriedades());
		} else {				
			obPersistencia.alterar(dados);
			obLogDt = new LogDt("UsuarioServentiaGrupo", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar), obDados.getPropriedades(), dados.getPropriedades());
		}

		obDados.copiar(dados);
		obLog.salvar(obLogDt, obFabricaConexao);

	}

	/**
	 * Salva um UsuarioServentiaGrupo baseado nos parametros passados
	 * 
	 * @param usuarioDt, objeto com dados do usuário, serventia e grupo
	 * @param logDt, objeto com dados do log
	 * @param conexao, conexão ativa
	 * 
	 * @author msapaula
	 */
	public void salvarUsuarioServentiaGrupo(UsuarioServentiaGrupoDt usuarioServentiaGrupoDt, UsuarioDt usuarioDt, LogDt logDt, FabricaConexao conexao) throws Exception {
		usuarioServentiaGrupoDt.setId(usuarioDt.getId_UsuarioServentiaGrupo());
		usuarioServentiaGrupoDt.setId_UsuarioServentia(usuarioDt.getId_UsuarioServentia());
		usuarioServentiaGrupoDt.setId_Grupo(usuarioDt.getId_Grupo());
		usuarioServentiaGrupoDt.setAtivo(String.valueOf(UsuarioServentiaGrupoDt.ATIVO));
		usuarioServentiaGrupoDt.setId_UsuarioLog(logDt.getId_Usuario());
		usuarioServentiaGrupoDt.setIpComputadorLog(logDt.getIpComputador());
		this.salvar(usuarioServentiaGrupoDt, conexao);
	}

	/**
	 * Consulta que retorna os usuários de um determinado grupo que podem ocupar um tipo de cargo em
	 * uma serventia
	 * 
	 * @param id_CargoTipo, identificação do tipo de cargo para filtrar os usuários
	 * @param id_Serventia, serventia para filtrar os usuários
	 * @param descricao, filtro para usuário
	 * @param posicao, página a ser exibida
	 * 
	 * @author msapaula
	 * @since 09/02/2009 08:53
	 */
	public List consultarUsuarioServentiaCargo(String id_CargoTipo, String id_Serventia, String descricao, String posicao) throws Exception {
		List tempList = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			UsuarioServentiaGrupoPs obPersistencia = new UsuarioServentiaGrupoPs(obFabricaConexao.getConexao());
			tempList = obPersistencia.consultarUsuarioServentiaCargo(id_CargoTipo, id_Serventia, descricao, posicao);
			QuantidadePaginas = (Long) tempList.get(tempList.size() - 1);
			tempList.remove(tempList.size() - 1);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;
	}

	
	
	/**
	 * Método responsável em verificar se um determinado advogado já está habilitado em uma Serventia
	 * 
	 * @param usuarioServentiaGrupoDt, objeto com dados do usuário, serventia 
	 * 
	 * @author msapaula
	 */
	public boolean verificarHabilitacaoAdvogado(UsuarioServentiaGrupoDt usuarioServentiaGrupoDt) throws Exception {
		boolean boRetorno = false;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			UsuarioServentiaGrupoPs obPersistencia = new UsuarioServentiaGrupoPs(obFabricaConexao.getConexao());
			boRetorno = obPersistencia.verificarHabilitacaoAdvogado(usuarioServentiaGrupoDt);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return boRetorno;
	}

	
	/**
	 * Método responsável em verificar se um determinado usuário já está habilitado em uma Serventia
	 * e Grupo passados
	 * 
	 * @param usuarioServentiaGrupoDt, objeto com dados do usuário, serventia e grupo a serem
	 *        verificados
	 * 
	 * @author msapaula
	 */
	public boolean verificarHabilitacaoUsuario(UsuarioServentiaGrupoDt usuarioServentiaGrupoDt) throws Exception {
		boolean boRetorno = false;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			UsuarioServentiaGrupoPs obPersistencia = new UsuarioServentiaGrupoPs(obFabricaConexao.getConexao());
			boRetorno = obPersistencia.verificarHabilitacaoUsuario(usuarioServentiaGrupoDt);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return boRetorno;
	}

	/**
	 * Consulta os grupos e serventias onde um usuário está habilitado. Retorna ATIVOS ou INATIVOS
	 * RETORNA SOMENTE AS HABILITAÇÕES DE SERVIDOR JUDICIÁRIO, RETIRANDO O CASO DE ADVOGADO
	 * @param id_Usuario, identificação do usuário para o qual serão consultados serventias e grupos
	 * @author msapaula
	 * @return lista de UsuarioServentiaGrupo
	 */
	public List consultarServentiasGruposServidorJudiciario(String id_usuario) throws Exception {
		List liTemp = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			UsuarioServentiaGrupoPs obPersistencia = new UsuarioServentiaGrupoPs(obFabricaConexao.getConexao());
			liTemp = obPersistencia.consultarServentiasGruposServidorJudiciario(id_usuario);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return liTemp;
	}
	
	/**
	 * Consulta os grupos e serventias onde um usuário está habilitado como promotor. Retorna ATIVOS ou INATIVOS
	 * RETORNA SOMENTE AS HABILITAÇÕES DE UM PROMOTOR
	 * @param id_Usuario, identificação do usuário para o qual serão consultados serventias e grupos
	 * @author lsbernardes
	 * @return lista de UsuarioServentiaGrupo
	 */
	public List consultarServentiasGruposPromotores(String id_usuario) throws Exception {
		List liTemp = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			UsuarioServentiaGrupoPs obPersistencia = new UsuarioServentiaGrupoPs(obFabricaConexao.getConexao());
			liTemp = obPersistencia.consultarServentiasGruposPromotores(id_usuario);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return liTemp;
	}

	/**
	 * Ativa um usuário em uma serventia e grupo
	 * 
	 * @param usuarioDt, conexao objeto com dados para ativação
	 * @author jrcorrea
	 * 24/08/2016
	 */
	public void ativarUsuarioServentiaGrupo(UsuarioServentiaGrupoDt usuarioServentiaGrupoDt,FabricaConexao fabricaConexao ) throws Exception {
	    						
		UsuarioServentiaGrupoPs obPersistencia = new UsuarioServentiaGrupoPs(fabricaConexao.getConexao());

		String valorAtual = "[Id_UsuarioServentiaGrupo:" + usuarioServentiaGrupoDt.getId() + ";Ativo:" + UsuarioServentiaGrupoDt.INATIVO + "]";
		String valorNovo = "[Id_UsuarioServentiaGrupo:" + usuarioServentiaGrupoDt.getId() + ";Ativo:" + UsuarioServentiaGrupoDt.ATIVO + "]";

		LogDt obLogDt = new LogDt("UsuarioServentiaGrupo",usuarioServentiaGrupoDt.getId(), usuarioServentiaGrupoDt.getId_UsuarioLog(), usuarioServentiaGrupoDt.getIpComputadorLog(), String.valueOf(LogTipoDt.HabilitacaoUsuario), valorAtual, valorNovo);
		obPersistencia.alterarStatusUsuarioServentiaGrupo(usuarioServentiaGrupoDt.getId(), UsuarioServentiaGrupoDt.ATIVO);

		obLog.salvar(obLogDt, fabricaConexao);
		
	}
	
	/**
	 * Ativa um usuário em uma serventia e grupo
	 * 
	 * @param usuarioDt, objeto com dados para ativação
	 * @author msapaula
	 */
	public void ativarUsuarioServentiaGrupo(UsuarioServentiaGrupoDt usuarioServentiaGrupoDt) throws Exception {
	    FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			UsuarioServentiaGrupoPs obPersistencia = new UsuarioServentiaGrupoPs(obFabricaConexao.getConexao());

			String valorAtual = "[Id_UsuarioServentiaGrupo:" + usuarioServentiaGrupoDt.getId() + ";Ativo:" + UsuarioServentiaGrupoDt.INATIVO + "]";
			String valorNovo = "[Id_UsuarioServentiaGrupo:" + usuarioServentiaGrupoDt.getId() + ";Ativo:" + UsuarioServentiaGrupoDt.ATIVO + "]";

			LogDt obLogDt = new LogDt("UsuarioServentiaGrupo",usuarioServentiaGrupoDt.getId(), usuarioServentiaGrupoDt.getId_UsuarioLog(), usuarioServentiaGrupoDt.getIpComputadorLog(), String.valueOf(LogTipoDt.HabilitacaoUsuario), valorAtual, valorNovo);
			obPersistencia.alterarStatusUsuarioServentiaGrupo(usuarioServentiaGrupoDt.getId(), UsuarioServentiaGrupoDt.ATIVO);

			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}

	/**
	 * Ativa um advogado em uma serventia e grupo
	 * 
	 * @param usuarioDt, objeto com dados para ativação
	 * @author Jesus
	 */
	public void ativarAdvogado(String id_UsuarioServentia, String id_UsuarioLog, String ipComputadorLog, FabricaConexao obFabricaConexao) throws Exception {
	    
		try{
						
			UsuarioServentiaGrupoPs obPersistencia = new UsuarioServentiaGrupoPs(obFabricaConexao.getConexao());

			String valorAtual = "[Id_UsuarioServentia:" + id_UsuarioServentia + ";Ativo:" + UsuarioServentiaGrupoDt.INATIVO + "]";
			String valorNovo = "[Id_UsuarioServentia:" + id_UsuarioServentia + ";Ativo:" + UsuarioServentiaGrupoDt.ATIVO + "]";

			LogDt obLogDt = new LogDt("UsuarioServentiaGrupo",id_UsuarioServentia, id_UsuarioLog, ipComputadorLog, String.valueOf(LogTipoDt.HabilitacaoUsuario), valorAtual, valorNovo);
			obPersistencia.AtivarAdvogado(id_UsuarioServentia);
			
			obLog.salvar(obLogDt, obFabricaConexao);
			
		
		} finally{
			
		}
	}
	/**
	 * Desativa um usuário em uma serventia e grupo
	 * 
	 * @param usuarioDt, objeto com dados para desativar
	 * 
	 * @author msapaula
	 */
	public void desativarUsuarioServentiaGrupo(UsuarioServentiaGrupoDt usuarioServentiaGrupoDt) throws Exception {
	    FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			UsuarioServentiaGrupoPs obPersistencia = new UsuarioServentiaGrupoPs(obFabricaConexao.getConexao());

			String valorAtual = "[Id_UsuarioServentiaGrupo:" + usuarioServentiaGrupoDt.getId() + ";Ativo:" + UsuarioServentiaGrupoDt.ATIVO + "]";
			String valorNovo = "[Id_UsuarioServentiaGrupo:" + usuarioServentiaGrupoDt.getId() + ";Ativo:" + UsuarioServentiaGrupoDt.INATIVO + "]";

			LogDt obLogDt = new LogDt("UsuarioServentiaGrupo", usuarioServentiaGrupoDt.getId(), usuarioServentiaGrupoDt.getId_UsuarioLog(), usuarioServentiaGrupoDt.getIpComputadorLog(), String.valueOf(LogTipoDt.DesabilitacaoUsuario), valorAtual, valorNovo);
			obPersistencia.alterarStatusUsuarioServentiaGrupo(usuarioServentiaGrupoDt.getId(), UsuarioServentiaGrupoDt.INATIVO);

			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}

	/**
	 * Método responsável em desativar todos os UsuarioServentiaGrupo de um usuário
	 * em virtude da desativação do usuário
	 * 
	 * @param id_Usuario, identificação do usuário
	 * @author msapaula
	 */
	public void desativarUsuarioServentiasGrupos(String id_Usuario, FabricaConexao obFabricaConexao) throws Exception {
		
		UsuarioServentiaGrupoPs obPersistencia = new UsuarioServentiaGrupoPs(obFabricaConexao.getConexao());
		obPersistencia.alterarStatusTodosUsuarioServentiaGrupo(id_Usuario, UsuarioServentiaDt.INATIVO);

	}

	/**
	 * Método responsável em ativar todos os UsuarioServentiaGrupo de um usuário
	 * em virtude da ativação do usuário
	 * 
	 * @param id_Usuario, identificação do usuário
	 * @author msapaula
	 */
	public void ativarUsuarioServentiasGrupos(String id_Usuario, FabricaConexao obFabricaConexao) throws Exception {

		UsuarioServentiaGrupoPs obPersistencia = new UsuarioServentiaGrupoPs(obFabricaConexao.getConexao());
		obPersistencia.alterarStatusTodosUsuarioServentiaGrupo(id_Usuario, UsuarioServentiaDt.ATIVO);

	}

	/**
	 * Método responsável em Habilitar um Usuário em uma Serventia e Grupo. Para isso, insere
	 * registro em UsuarioServentia e UsuarioServentiaGrupo.
	 * 
	 * @param usuarioServentiaGrupoDt, objeto com dados do usuário, serventia e grupo
	 * @author leandro, msapaula
	 */
	public void salvarUsuarioServentiaGrupo(UsuarioServentiaGrupoDt usuarioServentiaGrupoDt) throws Exception {
	    FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();

			// Primeiramente insere ou Atualiza registro em UsuarioServentia
			UsuarioServentiaNe usuarioServentiaNe = new UsuarioServentiaNe();
			UsuarioServentiaDt usuarioServentiaDt = new UsuarioServentiaDt();
			usuarioServentiaDt.setId(usuarioServentiaGrupoDt.getId_UsuarioServentia());
			usuarioServentiaDt.setId_Serventia(usuarioServentiaGrupoDt.getId_Serventia());
			usuarioServentiaDt.setId_Usuario(usuarioServentiaGrupoDt.getId_Usuario());
			usuarioServentiaDt.setAtivo(String.valueOf(UsuarioServentiaDt.ATIVO));
			usuarioServentiaDt.setId_UsuarioLog(usuarioServentiaGrupoDt.getId_UsuarioLog());
			usuarioServentiaDt.setIpComputadorLog(usuarioServentiaGrupoDt.getIpComputadorLog());
			usuarioServentiaNe.salvar(usuarioServentiaDt, obFabricaConexao);

			// Insere ou Atualiza UsuarioServentiaGrupo
			if (usuarioServentiaGrupoDt.getId_UsuarioServentia().equalsIgnoreCase("")) usuarioServentiaGrupoDt.setId_UsuarioServentia(usuarioServentiaDt.getId());
			usuarioServentiaGrupoDt.setId_Grupo(usuarioServentiaGrupoDt.getId_Grupo());
			usuarioServentiaGrupoDt.setId_UsuarioLog(usuarioServentiaGrupoDt.getId_UsuarioLog());
			usuarioServentiaGrupoDt.setIpComputadorLog(usuarioServentiaGrupoDt.getIpComputadorLog());
			usuarioServentiaGrupoDt.setAtivo(String.valueOf(UsuarioServentiaGrupoDt.ATIVO));
			this.salvar(usuarioServentiaGrupoDt, obFabricaConexao);

			//Limpa id's
			usuarioServentiaDt.setId("");
			usuarioServentiaGrupoDt.setId("");

			obFabricaConexao.finalizarTransacao();
		
		} finally{
			obFabricaConexao.fecharConexao();
		}

	}

	/**
	 * Chama método que irá consultar os grupos para habilitação de Servidores
	 * 
	 * @param tempNomeBusca
	 * @param posicaoPaginaAtual
	 * @return
	 */
	public List consultarGruposHabilitacaoServidores(String tempNomeBusca, String posicaoPaginaAtual, String grupoCodigo) throws Exception {
		List tempList = null;
		
		GrupoNe grupoNe = new GrupoNe();
		tempList = grupoNe.consultarGruposHabilitacaoServidores(tempNomeBusca, posicaoPaginaAtual, grupoCodigo);
		QuantidadePaginas = grupoNe.getQuantidadePaginas();
		grupoNe = null;
		
		return tempList;
	}

	public String consultarGruposHabilitacaoServidoresJSON(String tempNomeBusca, String PosicaoPaginaAtual, String grupoCodigo) throws Exception {
		String stTemp = "";
		
		GrupoNe grupone = new GrupoNe();
		stTemp = grupone.consultarGruposHabilitacaoServidoresJSON(tempNomeBusca, PosicaoPaginaAtual, grupoCodigo);
		
		return stTemp;
	}
	
	/**
	 * Retorna usuarios da serventia
	 * @since 25/11/2009
	 * @param usuarioNe, usuário logado para permitir consulta e geração de Hash 
	 * @return List
	 * @throws Exception
	 */
	public List consultarTodosUsuarioServentiaGrupo(UsuarioNe usuarioNe) throws Exception {
		List lista = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			UsuarioServentiaGrupoPs obPersistencia = new UsuarioServentiaGrupoPs(obFabricaConexao.getConexao());
			lista = obPersistencia.consultarTodosUsuarioServentiaGrupo(usuarioNe);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return lista;
	}

	/**
	 * Chama método que irá consultar as serventias ativas de um determinado tipo
	 * 
	 * @param tempNomeBusca
	 * @param posicaoPaginaAtual
	 * @return
	 */
	public List consultarServentiasAtivas(String tempNomeBusca, String posicaoPaginaAtual, String serventiaTipoCodigo) throws Exception {
		List tempList = null;
		
		ServentiaNe serventiaNe = new ServentiaNe();
		tempList = serventiaNe.consultarServentiasAtivas(tempNomeBusca, posicaoPaginaAtual, serventiaTipoCodigo);
		QuantidadePaginas = serventiaNe.getQuantidadePaginas();
		serventiaNe = null;

		return tempList;
	}
	
	public String consultarServentiasAtivasJSON(String descricao, String posicao, String serventiaTipoCodigo) throws Exception {
		String stTemp = "";
		
		ServentiaNe ServentiaNe = new ServentiaNe(); 
		stTemp = ServentiaNe.consultarServentiasAtivasJSON(descricao,  posicao, serventiaTipoCodigo);
		
		return stTemp;
	}

	/**
	 * Chama método que irá consultar
	 * 
	 * @param tempNomeBusca
	 * @param posicaoPaginaAtual
	 * @return
	 */
	public String consultarIdUsuarioServentiaGrupo(String idGrupo, String idServentia, String idUsuario) throws Exception {
		String stRetorno = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			UsuarioServentiaGrupoPs obPersistencia = new UsuarioServentiaGrupoPs(obFabricaConexao.getConexao());
			stRetorno = obPersistencia.consultarIdUsuarioServentiaGrupo(idGrupo, idServentia, idUsuario);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stRetorno;
	}
	
	/**
	 * Chama método que irá consultar os usuários
	 * 
	 * @param tempNomeBusca
	 * @param posicaoPaginaAtual
	 * @return
	 */
	public List consultarTodosUsuarios(String nome, String usuario, String posicaoPaginaAtual) throws Exception {
		List tempList = null;
		
		UsuarioNe usuarioNe = new UsuarioNe();
		tempList = usuarioNe.consultarTodosUsuarios(nome, usuario, posicaoPaginaAtual);
		QuantidadePaginas = usuarioNe.getQuantidadePaginas();
		usuarioNe = null;
		
		return tempList;
	}
	
	public String consultarTodosUsuariosJSON(String nome, String usuario, UsuarioNe UsuarioSessao, String posicaoPaginaAtual) throws Exception {
		String stTemp = "";

		UsuarioNe usuarioNe = new UsuarioNe();

		if (UsuarioSessao.isCoordenadorHabilitacaoMp()) {
			stTemp = usuarioNe.consultarTodosPromotoresJSON(nome, usuario, posicaoPaginaAtual);
		} else if (UsuarioSessao.isCoordenadorHabilitacaoSsp()) {
			 stTemp = usuarioNe.consultarTodosPoliciaisJSON(nome, usuario, posicaoPaginaAtual);
		} else {
			stTemp = usuarioNe.consultarTodosUsuariosJSON(nome, usuario, posicaoPaginaAtual);
		}

		return stTemp;
	}
	
	/**
	 * Chama método que irá consultar os promotores
	 * 
	 * @param tempNomeBusca
	 * @param posicaoPaginaAtual
	 * @return
	 */
	public List consultarTodosPromotores(String nome, String usuario, String posicaoPaginaAtual) throws Exception {
		List tempList = null;
		
		UsuarioNe usuarioNe = new UsuarioNe();
		tempList = usuarioNe.consultarTodosPromotores(nome, usuario, posicaoPaginaAtual);
		QuantidadePaginas = usuarioNe.getQuantidadePaginas();
		usuarioNe = null;
		
		return tempList;
	}	
	
	public String consultarTodosPromotoresJSON(String nome, String usuario, String posicaoPaginaAtual) throws Exception {
		String stTemp = "";
		
		UsuarioNe usuarioNe = new UsuarioNe();
		stTemp = usuarioNe.consultarTodosPromotoresJSON(nome, usuario, posicaoPaginaAtual);
		
		return stTemp;
	}
	
	public String consultarUsuarioServentiaCargoJSON(String id_CargoTipo, String id_Serventia, String descricao, String posicao) throws Exception {
		String stTemp;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			UsuarioServentiaGrupoPs obPersistencia = new UsuarioServentiaGrupoPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarUsuarioServentiaCargoJSON(id_CargoTipo, id_Serventia, descricao, posicao);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}

	public String consultarDescricaoJSON(String tempNomeBusca, String posicaoPaginaAtual) throws Exception {
		String stTemp;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			UsuarioServentiaGrupoPs obPersistencia = new UsuarioServentiaGrupoPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarDescricaoJSON(tempNomeBusca, posicaoPaginaAtual);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}

	public void alteraGrupoAdvogados(String idServentia, String tipoCodigo, FabricaConexao obFabricaConexao) throws Exception {
		UsuarioServentiaGrupoPs obPersistencia = new UsuarioServentiaGrupoPs(obFabricaConexao.getConexao());
		String id_Grupo = new GrupoNe().consultarId_GrupoAdvogadoServentiaTipo(Funcoes.StringToInt(tipoCodigo), obFabricaConexao);
		obPersistencia.alteraGrupoAdvogados(idServentia, id_Grupo);
	}

	public List consultarServentiasGruposPoliciais(String id_usuario) throws Exception {
		List liTemp = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			UsuarioServentiaGrupoPs obPersistencia = new UsuarioServentiaGrupoPs(obFabricaConexao.getConexao());
			liTemp = obPersistencia.consultarServentiasGruposPoliciais(id_usuario);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return liTemp;
	}

}
