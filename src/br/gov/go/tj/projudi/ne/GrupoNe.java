package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.ServentiaTipoDt;
import br.gov.go.tj.projudi.ps.GrupoPs;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;

// ---------------------------------------------------------
public class GrupoNe extends GrupoNeGen {

	/**
     * 
     */
    private static final long serialVersionUID = -5894117854694330630L;

    public String Verificar(GrupoDt dados) {

		String stRetorno = "";

		////System.out.println("..neGrupoVerificar()");
		return stRetorno;

	}
	public String consultarId_GrupoAdvogadoServentiaTipo(int serventiaTipo, FabricaConexao obFabricaConexao) throws Exception {
		String stIdGrupo = null;
		int grupoCodigo;
		
		if(serventiaTipo == ServentiaTipoDt.PROCURADORIA_GERAL_MUNICIPAL){
			grupoCodigo = GrupoDt.ADVOGADO_PUBLICO_MUNICIPAL;
		}else if(serventiaTipo == ServentiaTipoDt.PROCURADORIA_GERAL_ESTADO){
			grupoCodigo = GrupoDt.ADVOGADO_PUBLICO_ESTADUAL;
		}else if(serventiaTipo == ServentiaTipoDt.PROCURADORIA_UNIAO){
			grupoCodigo = GrupoDt.ADVOGADO_PUBLICO_UNIAO;
		}else if(serventiaTipo == ServentiaTipoDt.ESCRITORIO_JURIDICO){
			grupoCodigo = GrupoDt.ADVOGADO_PARTICULAR_ESCRITORIO_JURIDICO;
		}else if(serventiaTipo == ServentiaTipoDt.DEFENSORIA_PUBLICA){
			grupoCodigo = GrupoDt.ADVOGADO_DEFENSOR_PUBLICO;
		}else if(serventiaTipo == ServentiaTipoDt.ADVOGADO_PUBLICO_AUTARQUIAS){
			grupoCodigo = GrupoDt.ADVOGADO_PUBLICO;
		}else if(serventiaTipo == ServentiaTipoDt.ORDEM_ADVOGADOS_BRASIL){
			grupoCodigo = GrupoDt.ADVOGADO_PARTICULAR;
		}else{
			throw new MensagemException("Esse tipo de serventia não está associado a nenhum tipo de Advogado.");
		}
		
		
		GrupoPs obPersistencia = new GrupoPs(obFabricaConexao.getConexao());
		stIdGrupo = obPersistencia.consultarCodigo(grupoCodigo);
		
		return stIdGrupo;
	}
	
	public String consultarCodigo(int grupoCodigo) throws Exception {
		String stIdGrupo = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			GrupoPs obPersistencia = new GrupoPs(obFabricaConexao.getConexao());
			stIdGrupo = obPersistencia.consultarCodigo(grupoCodigo);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stIdGrupo;
	}

	/**
	 * Consultar determinado grupo de acordo com Código (GrupoCodigo)
	 */
	public GrupoDt consultarGrupoCodigo(String grupoCodigo) throws Exception {

		GrupoDt dtRetorno = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			GrupoPs obPersistencia = new GrupoPs(obFabricaConexao.getConexao());
			dtRetorno = obPersistencia.consultarGrupoCodigo(grupoCodigo);
			obDados.copiar(dtRetorno);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}

	/**
	 * Consulta os grupos que poderão ser utilizados na habilitação de Servidores Judiciários. Somente se o
	 * usuário logado for um administrador poderá ver todos os grupos, caso contrário verá somente
	 * os grupos diferentes de Administrador, Advogado e Assistente.
	 * 
	 * @param descricao
	 * @param posicao
	 * @param grupoCodigo, grupo do usuário logado
	 * 
	 * @author msapaula
	 */
	public List consultarGruposHabilitacaoServidores(String descricao, String posicao, String grupoCodigo) throws Exception {
		List tempList = null;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			GrupoPs obPersistencia = new GrupoPs(obFabricaConexao.getConexao());
			int grupo = Funcoes.StringToInt(grupoCodigo);

			if (grupo == GrupoDt.ADMINISTRADORES) {
				tempList = obPersistencia.consultarDescricao(descricao, posicao);
			}else if (grupo == GrupoDt.COORDENADOR_HABILITACAO_MP) {
				tempList = obPersistencia.consultarGruposHabilitacaoPromotores(descricao, posicao);
			}else if (grupo == GrupoDt.COORDENADOR_HABILITACAO_SSP) {
				//completar o codigo
				tempList = obPersistencia.consultarGruposHabilitacaoPoliciais(descricao, posicao);
			}else {
				tempList = obPersistencia.consultarGruposHabilitacaoServidores(descricao, posicao);
			}
			
			if (tempList.size() > 0) {
				QuantidadePaginas = (Long) tempList.get(tempList.size() - 1);
				tempList.remove(tempList.size() - 1);
			}

		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;
	}
	
	public List consultarGruposListaUsuarios(String descricao, String posicao, String grupoCodigo) throws Exception {
		List tempList = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			GrupoPs obPersistencia = new GrupoPs(obFabricaConexao.getConexao());
			int grupo = Funcoes.StringToInt(grupoCodigo);

			if (grupo == GrupoDt.ADMINISTRADORES) tempList = obPersistencia.consultarDescricao(descricao, posicao);
			else tempList = obPersistencia.consultarGruposListaUsuarios(descricao, posicao);

			if (tempList.size() > 0) {
				QuantidadePaginas = (Long) tempList.get(tempList.size() - 1);
				tempList.remove(tempList.size() - 1);
			}

		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;
	}
	
	public String consultarGruposListaUsuariosJSON(String descricao, String posicao, String grupoCodigo ) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			GrupoPs obPersistencia = new  GrupoPs(obFabricaConexao.getConexao());
			int grupo = Funcoes.StringToInt(grupoCodigo);
			if (grupo == GrupoDt.ADMINISTRADORES) stTemp = obPersistencia.consultarDescricaoJSON(descricao, posicao);
			else stTemp = obPersistencia.consultarGruposListaUsuariosJSON(descricao, posicao);
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;   
	}
	
	public String consultarDescricaoJSON(String descricao, String posicao ) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			GrupoPs obPersistencia = new  GrupoPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarDescricaoJSON(descricao, posicao);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;   
	}
	
	public String consultarGruposHabilitacaoServidoresJSON(String descricao, String posicao, String grupoCodigo) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			GrupoPs obPersistencia = new GrupoPs(obFabricaConexao.getConexao());
			int grupo = Funcoes.StringToInt(grupoCodigo);

			if (grupo == GrupoDt.ADMINISTRADORES) {
				stTemp = obPersistencia.consultarDescricaoServentiaJSON(descricao, posicao);
			}else if (grupo == GrupoDt.COORDENADOR_HABILITACAO_MP) {
				stTemp = obPersistencia.consultarGruposHabilitacaoPromotoresJSON(descricao, posicao);
			}else if (grupo == GrupoDt.COORDENADOR_HABILITACAO_SSP) {
				stTemp = obPersistencia.consultarGruposHabilitacaoSspJSON(descricao, posicao);
			}else if (grupo == GrupoDt.CADASTRADORES) {
				stTemp = obPersistencia.consultarDescricaoGrupoCadastradoresJSON(descricao, posicao);
			}else if (grupo == GrupoDt.CADASTRADOR_MASTER) {
				stTemp = obPersistencia.consultarDescricaoGrupoCadastradorMasterJSON(descricao, posicao);
			}else if (grupo == GrupoDt.CADASTRADOR_INTELIGENCIA) {
				stTemp = obPersistencia.consultarDescricaoGrupoCadastradorInteligenciaJSON(descricao, posicao);
			}else { 
				stTemp = obPersistencia.consultarGruposHabilitacaoServidoresJSON(descricao, posicao);
			}

		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}
	
	public String consultarDescricaoServentiaTipoJSON(String tempNomeBusca ) throws Exception {
		String stTemp = "";
		
		ServentiaTipoNe ServentiaTipone = new ServentiaTipoNe(); 
		stTemp = ServentiaTipone.consultarDescricaoJSON(tempNomeBusca);
		
		return stTemp;
	}
	
	public String consultarDescricaoGrupoTipoJSON(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		String stTemp = "";
		
		GrupoTipoNe GrupoTipone = new GrupoTipoNe(); 
		stTemp = GrupoTipone.consultarDescricaoJSON(tempNomeBusca, PosicaoPaginaAtual);
		
		return stTemp;
	}
	
	public List consultarGruposUsuarioServentia(String idUsuarioServentia) throws Exception {
		List tempList = null;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			GrupoPs obPersistencia = new  GrupoPs(obFabricaConexao.getConexao());
			tempList = obPersistencia.consultarGruposUsuarioServentia(idUsuarioServentia);		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;   
	}

}
