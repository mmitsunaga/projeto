package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.AudienciaTipoDt;
import br.gov.go.tj.projudi.dt.DescritorComboDt;
import br.gov.go.tj.projudi.ps.AudienciaTipoPs;
import br.gov.go.tj.utils.FabricaConexao;

public class AudienciaTipoNe extends AudienciaTipoNeGen {

	/**
     * 
     */
    private static final long serialVersionUID = 4163909008589015889L;

    public String Verificar(AudienciaTipoDt dados) {
		String stRetorno = "";

		if (dados.getAudienciaTipo().equalsIgnoreCase("")) {
			stRetorno += "O Campo AudienciaTipo é obrigatório.";
		}
		if (dados.getAudienciaTipoCodigo().equalsIgnoreCase("")) {
			stRetorno += "O Campo AudienciaTipoCodigo é obrigatório.";
		}
		return stRetorno;
	}

	/**
	 * Método responsável por buscar um objeto do tipo AudienciaTipoDt dado o código do tipo da audiência
	 * 
	 * @author Keila Sousa Silva
	 * @param audienciaTipoCodigo
	 * @return AudienciaTipoDt audienciaTipoDt
	 * @throws Exception
	 */
	public AudienciaTipoDt consultarAudienciaTipoCodigo(String audienciaTipoCodigo) throws Exception {
		AudienciaTipoDt audienciaTipoDt = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			AudienciaTipoPs obPersistencia = new  AudienciaTipoPs(obFabricaConexao.getConexao());
			audienciaTipoDt = obPersistencia.consultarAudienciaTipoCodigo(audienciaTipoCodigo);
			obDados.copiar(audienciaTipoDt);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return audienciaTipoDt;
	}
	
	public String consultarDescricaoJSON(String descricao, String posicao ) throws Exception {
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		String stTemp = "";

		try{

			AudienciaTipoPs obPersistencia = new  AudienciaTipoPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarDescricaoJSON(descricao, posicao);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;   
	}
	
	public String consultarDescricaoJSON(String descricao, String idUsuarioCejusc, String posicao ) throws Exception {
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		String stTemp = "";

		try{

			AudienciaTipoPs obPersistencia = new  AudienciaTipoPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarDescricaoJSON(descricao, idUsuarioCejusc, posicao);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;   
	}
	
	/**
	 * Lista os tipos de audiências de acordo com a paginação atual, filtro de pesquisa, campos de ordenação.
	 * Retorno de dados em JSON.
	 * @param descricao
	 * @param ordenacao
	 * @param posicao
	 * @param quantidadeRegistros
	 * @return
	 * @throws Exception
	 */
	public String consultarDescricaoJSON(String descricao, String posicao, String ordenacao, String quantidadeRegistros) throws Exception { 
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		String stTemp = "";
		
		try{

			AudienciaTipoPs obPersistencia = new  AudienciaTipoPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarDescricaoJSON(descricao, posicao, ordenacao, quantidadeRegistros);
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}
	
	/**
	 * 
	 * @param descricao
	 * @return
	 * @throws Exception
	 */
	public List<DescritorComboDt> consultarDescricaoCombo(String descricao) throws Exception {
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			AudienciaTipoPs obPersistencia = new  AudienciaTipoPs(obFabricaConexao.getConexao());
			return obPersistencia.consultarDescricaoCombo(descricao);
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
}