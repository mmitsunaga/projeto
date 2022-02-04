package br.gov.go.tj.projudi.ne;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.PendenciaResponsavelDt;
import br.gov.go.tj.projudi.dt.ServentiaTipoDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaOabDt;
import br.gov.go.tj.projudi.ps.UsuarioServentiaOabPs;
import br.gov.go.tj.utils.FabricaConexao;

//---------------------------------------------------------
public class UsuarioServentiaOabNe extends UsuarioServentiaOabNeGen {

	/**
     * 
     */
    private static final long serialVersionUID = 1590031307708981088L;

    /**
	 *  Verificar Campos Obrigatórios
	 */
	public String Verificar(UsuarioServentiaOabDt dados) {

		String stRetorno = "";

		if (dados.getOabNumero().length() == 0) stRetorno += "O Campo OabNumero é obrigatório.";
		if (dados.getOabComplemento().length() == 0) stRetorno += "O Campo OabComplemento é obrigatório.";

		return stRetorno;

	}

	/**
	 *  Verificar Campos Obrigatórios ao inserir uma OAB para advogado
	 */
	public String VerificarOAB(UsuarioDt dados) throws Exception {
		String stRetorno = "";

		if (dados.getUsuarioServentiaOab().getOabNumero().equalsIgnoreCase("")) stRetorno += "Número da OAB é campo obrigatório. \n";
		if (dados.getUsuarioServentiaOab().getOabComplemento().equalsIgnoreCase("")) stRetorno += "Complemento da OAB é campo obrigatório. \n";
		if (dados.getId_Serventia().equalsIgnoreCase("")) stRetorno += "Selecione a Serventia desejada. ";
		return stRetorno;
	}

	public void salvar(UsuarioServentiaOabDt usarioServentiaOabDt, FabricaConexao obFabricaConexao) throws Exception {
		LogDt obLogDt;
		
		UsuarioServentiaOabPs obPersistencia = new UsuarioServentiaOabPs(obFabricaConexao.getConexao());
		if (usarioServentiaOabDt.getId().equalsIgnoreCase("")) {				
			obPersistencia.inserir(usarioServentiaOabDt);
			obLogDt = new LogDt("UsuarioServentiaOab", usarioServentiaOabDt.getId(), usarioServentiaOabDt.getId_UsuarioLog(), usarioServentiaOabDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir), "", usarioServentiaOabDt.getPropriedades());
		} else {				
			obPersistencia.alterar(usarioServentiaOabDt);
			obLogDt = new LogDt("UsuarioServentiaOab", usarioServentiaOabDt.getId(), usarioServentiaOabDt.getId_UsuarioLog(), usarioServentiaOabDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar), obDados.getPropriedades(), usarioServentiaOabDt.getPropriedades());
		}

		obDados.copiar(usarioServentiaOabDt);
		obLog.salvar(obLogDt, obFabricaConexao);
				
	}

	/**
	 * Salva um UsuarioServentiaOab baseado nos parametros passados
	 * 
	 * @param usuarioDt, objeto com dados do usuário e oab
	 * @param logDt, objeto com dados do log
	 * @param conexao, conexão ativa
	 * 
	 * @author msapaula
	 */
	public void salvarUsuarioServentiaOab(UsuarioServentiaOabDt usuarioServentiaOabDt, UsuarioDt usuarioDt, LogDt logDt, FabricaConexao conexao) throws Exception {
		usuarioServentiaOabDt.setId(usuarioDt.getUsuarioServentiaOab().getId());
		usuarioServentiaOabDt.setOabNumero(usuarioDt.getUsuarioServentiaOab().getOabNumero());
		usuarioServentiaOabDt.setOabComplemento(usuarioDt.getUsuarioServentiaOab().getOabComplemento());
		usuarioServentiaOabDt.setId_UsuarioServentia(usuarioDt.getId_UsuarioServentia());
		usuarioServentiaOabDt.setId_UsuarioLog(logDt.getId_Usuario());
		usuarioServentiaOabDt.setIpComputadorLog(logDt.getIpComputador());
		this.salvar(usuarioServentiaOabDt, conexao);
		usuarioDt.getUsuarioServentiaOab().setId(usuarioServentiaOabDt.getId());
		usuarioDt.getUsuarioServentiaOab().setId_UsuarioServentia(usuarioServentiaOabDt.getId_UsuarioServentia());
	}
	
	/**
	 * Consultar um advogado de acordo com OAB, complemento e estado passados 
	 * 
	 * @param oabNumero, número da oab
	 * @param oabComplemento, complemento da oab
	 * @param oabUf, estado de representação da serventia OAB
	 * 
	 * @author msapaula
	 */
	public String consultarAdvogado(String oabNumero, String oabComplemento, String oabUf, int codigoInstituicao) throws Exception {
		String id_UsuarioServentia = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			UsuarioServentiaOabPs obPersistencia = new UsuarioServentiaOabPs(obFabricaConexao.getConexao());
			switch (codigoInstituicao) {
			case 2:
				codigoInstituicao = ServentiaTipoDt.PROCURADORIA_GERAL_ESTADO;
				break;
			case 3:
				codigoInstituicao = ServentiaTipoDt.PROCURADORIA_GERAL_MUNICIPAL;
				break;
			case 4:
				codigoInstituicao = ServentiaTipoDt.ORDEM_ADVOGADOS_BRASIL;
				break;
			case 5:
				codigoInstituicao = ServentiaTipoDt.PROCURADORIA_UNIAO;
				break;
			case 6:
				codigoInstituicao = ServentiaTipoDt.ADVOGADO_PUBLICO_AUTARQUIAS;
				break;
			default:
				break;
			}
			id_UsuarioServentia = obPersistencia.consultarAdvogado(oabNumero, oabComplemento, oabUf, codigoInstituicao);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return id_UsuarioServentia;
	}
	
	/**
	 * Consultar um advogado de acordo com OAB, complemento e estado passados 
	 * 
	 * @param oabNumero, número da oab
	 * @param oabComplemento, complemento da oab
	 * @param oabUf, estado de representação da serventia OAB
	 * 
	 * @author msapaula
	 */
	public UsuarioServentiaOabDt consultarUsuarioServentiaOab(String oabNumero, String oabComplemento, String oabUf) throws Exception {
		UsuarioServentiaOabDt usuarioServentiaOabDt = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			UsuarioServentiaOabPs obPersistencia = new UsuarioServentiaOabPs(obFabricaConexao.getConexao());
			usuarioServentiaOabDt = obPersistencia.consultarUsuarioServentiaOab(oabNumero, oabComplemento, oabUf);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return usuarioServentiaOabDt;
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
	
	/**
	 * Chama método que irá salvar Nova Oab
	 */
	public void salvarUsuarioServentiaOab(UsuarioDt usuarioDt) throws Exception {
		
		UsuarioNe usuarioNe = new UsuarioNe();
		usuarioNe.salvarUsuarioServentiaOab(usuarioDt);
		usuarioNe = null;
				
	}
	
	/**
	 * Consultar todos os advogados de uma serventia  
	 * 
	 * @param id_serventia
	 * 
	 * @author jrcorrea
	 * @ 18/11/2014
	 */
	public List consultarUsuariosServentiaOab(String id_serventia) throws Exception {
		List liTemp = new ArrayList();
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			UsuarioServentiaOabPs obPersistencia = new UsuarioServentiaOabPs(obFabricaConexao.getConexao());
			liTemp = obPersistencia.consultarUsuariosServentiaOab(id_serventia);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return liTemp;
	}

	public List<UsuarioServentiaOabDt> consultarUsuarioServentiaOab(String id_Usuario) throws Exception  {
		List<UsuarioServentiaOabDt> lista = new ArrayList();
		FabricaConexao obFabricaConexao = null;
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			UsuarioServentiaOabPs obPersistencia = new UsuarioServentiaOabPs(obFabricaConexao.getConexao());
			lista = obPersistencia.consultarUsuarioServentiaOab(id_Usuario);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return lista;
	}
	
	 /**
     * Verifica se o usuário advogado/procurador/defensor que será intimado ou citado é master ou não.
     * 
     * @author lsbernardes
     
     * @param id_Usu_Serv,
     *            id do usuário serventia
     * @param fabrica,
     *            fabrica de conexao para continuar uma transacao
     *            
     * @return boolean,
     *          se o usuário é master ou não 
     * 
     * @throws Exception
     */
	public boolean isUsuarioServentiaOabMaster(String id_Usu_Serv, FabricaConexao fabrica) throws Exception {
		UsuarioServentiaOabPs obPersistencia = new UsuarioServentiaOabPs(fabrica.getConexao());
		return obPersistencia.isUsuarioServentiaOabMaster(id_Usu_Serv);
	}
	
	 /**
     * Consulta possíveis usuários (advogado/procurador/defensor) master na serventia do usuário (advogado/procurador/defensor) que será intimado ou citado.
     * 
     * @author lsbernardes
     
     * @param id_Serv,
     *            id da serventia
     * @param fabrica,
     *            fabrica de conexao para continuar uma transacao
     * @return List,
     *           lista contendo objetos do tipo PendenciaResponsavelDt para compor a lista de responsáveis pela intimação ou citação
     *            
     * @throws Exception
     */
	public List<PendenciaResponsavelDt> consultarUsuarioServentiaOabMaster(String id_Serv, FabricaConexao fabrica) throws Exception { 
		UsuarioServentiaOabPs obPersistencia = new UsuarioServentiaOabPs(fabrica.getConexao());
		return obPersistencia.consultarUsuarioServentiaOabMaster(id_Serv);
	}

}
