package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.MovimentacaoTipoDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.ps.MovimentacaoTipoPs;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;

//---------------------------------------------------------
public class MovimentacaoTipoNe extends MovimentacaoTipoNeGen {

	//

	/**
     * 
     */
    private static final long serialVersionUID = -2828240392651099809L;

    // ---------------------------------------------------------
	public String Verificar(MovimentacaoTipoDt dados) {

		String stRetorno = "";
		if(dados.getMovimentacaoTipo().length() > 100) {
			stRetorno = "O tamanho máximo da coluna Descrição é 100 caracteres.";
		}
		
		return stRetorno;

	}

	/**
	 * Método responsável em buscar o objeto correspondente ao código do tipo de movimentação
	 */
	public MovimentacaoTipoDt consultaMovimentacaoTipoCodigo(int movimentacaoTipoCodigo) throws Exception {
		MovimentacaoTipoDt dtRetorno = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			MovimentacaoTipoPs obPersistencia = new MovimentacaoTipoPs(obFabricaConexao.getConexao());
			dtRetorno = obPersistencia.consultaMovimentacaoTipoCodigo(movimentacaoTipoCodigo);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}

	/**
	 * Consulta de Tipos de Movimentação
	 * Se grupo é Administrador e Estatística, lista todos os tipos cadastrados.
	 * Caso contrário, serão listados os tipos de acordo com o grupo.
	 */
	public List consultarGrupoMovimentacaoTipo(String grupoCodigo, String descricao, String posicao) throws Exception {
		List tempList = null;

		switch (Funcoes.StringToInt(grupoCodigo)) {
			case GrupoDt.ADMINISTRADORES:
			case GrupoDt.ESTATISTICA:
			case GrupoDt.JUIZ_CORREGEDOR:
				tempList = consultarDescricao(descricao, posicao);
				break;
			default:
				GrupoMovimentacaoTipoNe grupoMovimentacaoTipoNe = new GrupoMovimentacaoTipoNe();
				tempList = grupoMovimentacaoTipoNe.consultarGrupoMovimentacaoTipo(grupoCodigo, descricao, posicao);
				QuantidadePaginas = grupoMovimentacaoTipoNe.getQuantidadePaginas();
				break;
		}
		
		return tempList;
	}
	
	/**
	 * Consulta de Tipos de Movimentação
	 * Se grupo é Assistente de Promotor ou Advogado, deve listar os tipos definidos para o chefe.
	 * Caso contrário, serão listados os tipos de acordo com o grupo.
	 */
	public List consultarGrupoMovimentacaoTipo(UsuarioDt usuarioDt, String descricao, String posicao) throws Exception {
		List tempList = null;

		GrupoMovimentacaoTipoNe grupoMovimentacaoTipoNe = new GrupoMovimentacaoTipoNe();
		switch (Funcoes.StringToInt(usuarioDt.getGrupoCodigo())) {
			case GrupoDt.ASSESSOR_ADVOGADOS:
			case GrupoDt.ASSESSOR_MP:
				tempList = grupoMovimentacaoTipoNe.consultarGrupoMovimentacaoTipo(usuarioDt.getGrupoUsuarioChefe(), descricao, posicao);
				break;
				
			default:
				tempList = grupoMovimentacaoTipoNe.consultarGrupoMovimentacaoTipo(usuarioDt.getGrupoCodigo(), descricao, posicao);
				break;
		}
		QuantidadePaginas = grupoMovimentacaoTipoNe.getQuantidadePaginas();
		
		return tempList;
	}
	
	public String consultarCodigoMovimentacaoTipo(String id_movimentacaotipo, FabricaConexao obFabricaConexao) throws Exception {

		String codMovimentacaoTipo=null;
		
		MovimentacaoTipoPs obPersistencia = new MovimentacaoTipoPs(obFabricaConexao.getConexao());
		codMovimentacaoTipo = obPersistencia.consultarCodigoMovimentacaoTipo(id_movimentacaotipo ); 
		
		return codMovimentacaoTipo;
	}
	
	public String consultarDescricaoJSON(String descricao, String posicao ) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			MovimentacaoTipoPs obPersistencia = new MovimentacaoTipoPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarDescricaoJSON(descricao, posicao);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;   
	}
	
	public String consultarGrupoMovimentacaoTipoJSON(String grupoCodigo, String descricao, String posicao) throws Exception {
		String stTemp = "";

		switch (Funcoes.StringToInt(grupoCodigo)) {
			case GrupoDt.ADMINISTRADORES:
			case GrupoDt.ESTATISTICA:
			case GrupoDt.JUIZ_CORREGEDOR:
				stTemp = consultarDescricaoJSON(descricao, posicao);
				break;
			default:
				GrupoMovimentacaoTipoNe grupoMovimentacaoTipoNe = new GrupoMovimentacaoTipoNe();
				stTemp = grupoMovimentacaoTipoNe.consultarGrupoMovimentacaoTipoJSON(grupoCodigo, descricao, posicao);
				break;
		}
		
		return stTemp;
	}
	
	public String consultaTodosMarcadosJSON(String descricao, String posicao, String id_Usuario, String grupoCodigo) throws Exception{
				
		String stTemp = "";
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			MovimentacaoTipoPs obPersistencia = new MovimentacaoTipoPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultaTodosMarcadosJSON(descricao, posicao, id_Usuario, grupoCodigo, true);
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;   
	}
	
	public String consultarGrupoMovimentacaoTipoSentencaJSON(String grupoCodigo, String descricao, String posicao) throws Exception {
		String stTemp = "";

		GrupoMovimentacaoTipoNe grupoMovimentacaoTipoNe = new GrupoMovimentacaoTipoNe();
		stTemp = grupoMovimentacaoTipoNe.consultarGrupoMovimentacaoTipoSentencaJSON(grupoCodigo, descricao, posicao);
		
		return stTemp;
	}
	
	

}
