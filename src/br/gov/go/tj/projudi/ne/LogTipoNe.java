package br.gov.go.tj.projudi.ne;

import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.ps.LogTipoPs;
import br.gov.go.tj.utils.FabricaConexao;

public class LogTipoNe extends LogTipoNeGen{

    private static final long serialVersionUID = 8883153940248633406L;

	public  String Verificar(LogTipoDt dados ) {

		String stRetorno="";
		return stRetorno;
	}

	public String consultarDescricaoJSON(String tempNomeBusca, String posicaoPaginaAtual) throws Exception {

		String stTemp = "";
		FabricaConexao obFabricaConexao = null;		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			LogTipoPs persistencia = new  LogTipoPs(obFabricaConexao.getConexao());
			stTemp = persistencia.consultarDescricaoJSON( tempNomeBusca, posicaoPaginaAtual);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}
}