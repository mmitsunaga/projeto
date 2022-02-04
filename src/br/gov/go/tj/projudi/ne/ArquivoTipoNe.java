package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.ArquivoTipoDt;
import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.ps.ArquivoTipoPs;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;

//---------------------------------------------------------
public class ArquivoTipoNe extends ArquivoTipoNeGen {

	//

	/**
	 * 
	 */
	private static final long serialVersionUID = -4758438414997898797L;

	// ---------------------------------------------------------
	public String Verificar(ArquivoTipoDt dados) {

		String stRetorno = "";

		////System.out.println("..neArquivoTipoVerificar()");
		return stRetorno;

	}

	/**
	 * Consulta um tipo de arquivo pelo  seu codigo de descricao
	 * @author Ronneesley Moura Teles 
	 * @since 13/10/2008 10:53
	 * @param String ArquivoTipoCodigo, codigo od arquivo tipo
	 * @return List
	 * @throws Exception
	 */
	public List consultarPeloArquivoTipoCodigo(String arquivoTipoCodigo) throws Exception {
		List ids = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ArquivoTipoPs  obPersistencia = new  ArquivoTipoPs(obFabricaConexao.getConexao());
			ids = obPersistencia.consultarPeloArquivoTipoCodigo(arquivoTipoCodigo);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return ids;
	}
	
	/**
	 * Verifica se o arquivo é publico ou não
	 * @param String ArquivoTipoCodigo
	 * @return boolean
	 *  
	 */
	public boolean consultarPublico(String arquivoTipoCodigo) throws Exception {
		boolean publico = false;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ArquivoTipoPs  obPersistencia = new  ArquivoTipoPs(obFabricaConexao.getConexao());
			publico = obPersistencia.consultarPublicoArquivoTipo(arquivoTipoCodigo);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}		
		return publico;
	}

	/**
	 * Consultar descrição de tipo de arquivo de acordo com o código passado
	 * 
	 * @param arquivoTipoCodigo:
	 *            código do tipo de arquivo
	 */
	public String consultarDescricaoArquivoTipo(String arquivoTipoCodigo) throws Exception {

		String descricao = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ArquivoTipoPs  obPersistencia = new  ArquivoTipoPs(obFabricaConexao.getConexao());
			descricao = obPersistencia.consultarDescricaoArquivoTipo(arquivoTipoCodigo);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return descricao;
	}
	
	/**
	 * Consultar id de tipo de arquivo de acordo com o código passado
	 * @param arquivoTipoCodigo: código do tipo de arquivo
	 */
	public String consultarIdArquivoTipo(String arquivoTipoCodigo) throws Exception {

		String descricao = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ArquivoTipoPs  obPersistencia = new  ArquivoTipoPs(obFabricaConexao.getConexao());
			descricao = obPersistencia.consultarIdArquivoTipo(arquivoTipoCodigo);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return descricao;
	}

	/**
	 * Consulta de Tipos de Arquivo:
	 * Se usuário é Administrador, lista todos os tipos - chama a consulta consultarDescricao()
	 * Se não é Administrador, chama GrupoArquivoTipoNe e filtra os tipos de arquivo de acordo com o grupo do usuário
	 */
	public List consultarGrupoArquivoTipo(String grupoCodigo, String descricao, String posicao) throws Exception {
		List tempList = null;
		
		switch (Funcoes.StringToInt(grupoCodigo)) {
			case GrupoDt.ADMINISTRADORES:
				tempList = consultarDescricao(descricao, posicao);
				break;
			default:
				GrupoArquivoTipoNe grupoArquivoTipoNe = new GrupoArquivoTipoNe();
				tempList = grupoArquivoTipoNe.consultarGrupoArquivoTipo(grupoCodigo, descricao, posicao);
				QuantidadePaginas = grupoArquivoTipoNe.getQuantidadePaginas();
				break;
		}

		return tempList;
	}
	
	public List<ArquivoTipoDt> consultarTodos() throws Exception {
		List<ArquivoTipoDt> tempList=null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ArquivoTipoPs obPersistencia = new ArquivoTipoPs(obFabricaConexao.getConexao());
			tempList=obPersistencia.consultarTodos();			
		} finally { 
			obFabricaConexao.fecharConexao();
		}
		return tempList;
	}
	
	public String consultarGrupoArquivoTipoJSON(String grupoCodigo, String descricao, String posicao) throws Exception {
		String stTemp = null;
		
		switch (Funcoes.StringToInt(grupoCodigo)) {
			case GrupoDt.ADMINISTRADORES:
				stTemp = consultarDescricaoJSON(descricao, posicao);
				break;
			default:
				GrupoArquivoTipoNe grupoArquivoTipoNe = new GrupoArquivoTipoNe();
				stTemp = grupoArquivoTipoNe.consultarGrupoArquivoTipoJSON(grupoCodigo, descricao, posicao);
				QuantidadePaginas = grupoArquivoTipoNe.getQuantidadePaginas();
				break;
		}
		
		
		return stTemp;
	}
	
	/**
	 * Consulta de Tipos de Arquivo:
	 * Se usuário é Assistente de Advogado ou Promotor, lista todos os tipos de acordo com o grupo do usuário chefe.
	 * Caso contrário, lista de acordo com o grupo do usuário.
	 */
	public List consultarGrupoArquivoTipo(UsuarioDt usuarioDt, String descricao, String posicao) throws Exception {
		List tempList = null;
		
		GrupoArquivoTipoNe grupoArquivoTipoNe = new GrupoArquivoTipoNe();
		switch (Funcoes.StringToInt(usuarioDt.getGrupoCodigo())) {

			case GrupoDt.ASSESSOR_ADVOGADOS:
			case GrupoDt.ASSESSOR_MP:
				tempList = grupoArquivoTipoNe.consultarGrupoArquivoTipo(usuarioDt.getGrupoUsuarioChefe(), descricao, posicao);
				break;

			default:
				tempList = grupoArquivoTipoNe.consultarGrupoArquivoTipo(usuarioDt.getGrupoCodigo(), descricao, posicao);
				break;
		}
		QuantidadePaginas = grupoArquivoTipoNe.getQuantidadePaginas();
		
		return tempList;
	}
	
	public String consultarDescricaoJSON(String descricao, String posicao ) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		
		try{

			ArquivoTipoPs  obPersistencia = new  ArquivoTipoPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarDescricaoJSON(descricao, posicao);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		
		return stTemp;   
	}

	public boolean isPublico(String id_ArquivoTipo) throws Exception {
		boolean boRetorno = false;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		
		try{

			ArquivoTipoPs  obPersistencia = new  ArquivoTipoPs(obFabricaConexao.getConexao());
			boRetorno = obPersistencia.isPublico(id_ArquivoTipo);
		}catch(Exception e){
			throw new Exception("<{Não foi possível verificar se o arquivo é público}>" + e.getMessage());				
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return boRetorno;   
	}

	public ArquivoTipoDt consultarId(String idArquivoTipo, FabricaConexao fabrica) throws Exception {
		ArquivoTipoPs obPersistencia = new ArquivoTipoPs(fabrica.getConexao());
		return obPersistencia.consultarId(idArquivoTipo);
	}

	public String consultarIdArquivoTipo(String arquivoTipoCodigo, FabricaConexao obFabricaConexao) throws Exception {
		ArquivoTipoPs  obPersistencia = new  ArquivoTipoPs(obFabricaConexao.getConexao());
		return obPersistencia.consultarIdArquivoTipo(arquivoTipoCodigo);
	}
	
	
}
