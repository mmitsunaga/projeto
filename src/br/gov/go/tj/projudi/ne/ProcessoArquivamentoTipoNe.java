package br.gov.go.tj.projudi.ne;

import br.gov.go.tj.projudi.dt.ProcessoArquivamentoTipoDt;
import br.gov.go.tj.projudi.ps.ProcessoArquivamentoTipoPs;
import br.gov.go.tj.utils.FabricaConexao;

public class ProcessoArquivamentoTipoNe extends ProcessoArquivamentoTipoNeGen {

	private static final long serialVersionUID = 9187731792045962018L;

	public String Verificar(ProcessoArquivamentoTipoDt dados) {
		String stRetorno = "";
		if (dados.getProcessoArquivamentoTipo().length() == 0)
			stRetorno += "O campo Tipo de Arquivamento é obrigatório.";

		return stRetorno;
	}

	public String consultarDescricaoJSON(String stNomeBusca1, boolean boTodos, String posicaoPaginaAtual) throws Exception {
		FabricaConexao obFabricaConexao = null;
		String stTemp = "";
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoArquivamentoTipoPs obPersistencia = new ProcessoArquivamentoTipoPs(obFabricaConexao.getConexao());

			stTemp = obPersistencia.consultarDescricaoJSON(stNomeBusca1, boTodos, posicaoPaginaAtual);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}

}
