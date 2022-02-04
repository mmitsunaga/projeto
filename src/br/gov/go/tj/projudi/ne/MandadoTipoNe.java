package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.MandadoTipoDt;
import br.gov.go.tj.projudi.ps.MandadoTipoPs;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;

//---------------------------------------------------------
public class MandadoTipoNe extends MandadoTipoNeGen {

	//

	/**
	 * 
	 */
	private static final long serialVersionUID = -9110972586844981383L;

	// ---------------------------------------------------------
	public String Verificar(MandadoTipoDt dados) {

		String stRetorno = "";

		if (dados.getMandadoTipo().equalsIgnoreCase("")) {
			stRetorno += "Descrição é é obrigatório.";
		}
		return stRetorno;

	}

	/**
	 * Consulta os Tipos de Mandado
	 * 
	 * @return List
	 * @throws Exception
	 */
	public List consultarListaTiposMandados() throws Exception {
		List listaMandadoTipo = null;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			MandadoTipoPs obPersistencia = new MandadoTipoPs(obFabricaConexao.getConexao());

			listaMandadoTipo = obPersistencia.consultarTiposMandados();
		
		} finally{
			obFabricaConexao.fecharConexao();
		}

		return listaMandadoTipo;
	}

	public String consultarDescricaoJSON(String descricao, String posicao) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			MandadoTipoPs obPersistencia = new MandadoTipoPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarDescricaoJSON(descricao, posicao);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}

	/**
	 * Retorna verdadeiro caso o id do mandadoTipo faça referência à um mandado do tipo Execução Fiscal.
	 * @param idMandadoTipo
	 * @param fabricaConexao
	 * @return
	 * @throws Exception
	 */
	public boolean isTipoExecucaoFiscal(String idMandadoTipo, FabricaConexao fabricaConexao) throws Exception {
		FabricaConexao obFabricaConexao = null;
		MandadoTipoPs mandadoTipoPs = null;
		MandadoTipoDt mandadoTipoDt = null;
		
		try {
			
			if(Funcoes.StringToInt(idMandadoTipo) != 0) {
				
				if(fabricaConexao == null) {
					obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
				} else {
					obFabricaConexao = fabricaConexao;
				}
				
				mandadoTipoPs = new MandadoTipoPs(obFabricaConexao.getConexao());
				mandadoTipoDt = mandadoTipoPs.consultarId(idMandadoTipo);
				
				if(mandadoTipoDt != null) {
					if(Funcoes.StringToInt(mandadoTipoDt.getMandadoTipoCodigo()) == MandadoTipoDt.EXECUCAO_FISCAL)
						return true;
				}
			}
		
		} finally {
			if(fabricaConexao == null)
				obFabricaConexao.fecharConexao();
		}
		
		return false;
	}
}
