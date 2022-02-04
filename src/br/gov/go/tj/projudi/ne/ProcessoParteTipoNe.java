package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.ProcessoParteTipoDt;
import br.gov.go.tj.projudi.ps.ProcessoParteTipoPs;
import br.gov.go.tj.utils.FabricaConexao;

//---------------------------------------------------------
public class ProcessoParteTipoNe extends ProcessoParteTipoNeGen {

	//

	/**
     * 
     */
    private static final long serialVersionUID = 631146775395815464L;

    // ---------------------------------------------------------
	public String Verificar(ProcessoParteTipoDt dados) {

		String stRetorno = "";

		////System.out.println("..neParteTipoVerificar()");
		return stRetorno;

	}

	/**
	 * Consultar objeto ProcessoParteTipo equivalente ao código passado
	 */
	public ProcessoParteTipoDt consultarProcessoParteTipoCodigo(String processoParteTipoCodigo) throws Exception {

		ProcessoParteTipoDt dtRetorno = null;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoParteTipoPs obPersistencia = new ProcessoParteTipoPs(obFabricaConexao.getConexao());
			dtRetorno = obPersistencia.consultarProcessoParteTipoCodigo(processoParteTipoCodigo);
			obDados.copiar(dtRetorno);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}

	/**
	 * Consultar processo parte tipo pela descricao
	 * @author Ronneesley Moura Teles
	 * @since 11/04/2008 - 10:52
	 * @return List
	 */
	public List consultarDescricao(String descricao, String posicao) throws Exception {
		List lista = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoParteTipoPs obPersistencia = new ProcessoParteTipoPs(obFabricaConexao.getConexao());
			lista = obPersistencia.consultarDescricao(descricao, posicao);

			//Remove quantidade de resultados
			lista.remove(lista.size() - 1);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}

		return lista;
	}

	/**
	 * Consultar outros tipos de parte disponíveis para cadastro, ou seja,
	 * todas as partes que não sejam promovente e promovido
	 * 
	 * @param descricao, filtro de consulta
	 * @param posicao, parâmetro para paginação
	 * @author msapaula
	 */
	public List consultarOutrosTiposPartes(String descricao, String posicao) throws Exception {
		List lista = null;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoParteTipoPs obPersistencia = new ProcessoParteTipoPs(obFabricaConexao.getConexao());
			lista = obPersistencia.consultarOutrosTiposPartes(descricao, posicao);
			QuantidadePaginas = (Long)lista.get(lista.size()-1);
			lista.remove(lista.size()-1);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}

		return lista;
	}
	
	public String consultarDescricaoJSON(String descricao, String posicao) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoParteTipoPs obPersistencia = new ProcessoParteTipoPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarDescricaoJSON(descricao, posicao);

		
		} finally{
			obFabricaConexao.fecharConexao();
		}

		return stTemp;
	}
	
	public String consultarOutrosTiposPartesJSON(String descricao, String posicao) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoParteTipoPs obPersistencia = new ProcessoParteTipoPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarOutrosTiposPartesJSON(descricao, posicao);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}

		return stTemp;
	}
}
