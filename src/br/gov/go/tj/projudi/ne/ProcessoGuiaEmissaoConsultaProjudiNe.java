package br.gov.go.tj.projudi.ne;

import br.gov.go.tj.projudi.dt.NumeroProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoGuiaEmissaoConsultaDt;
import br.gov.go.tj.projudi.ps.ProcessoGuiaEmissaoConsultaPs;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.MensagemException;

public class ProcessoGuiaEmissaoConsultaProjudiNe extends Negocio {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8502858131504840707L;
	
	public ProcessoGuiaEmissaoConsultaDt obtenhaProcesso(NumeroProcessoDt numeroProcessoDt) throws MensagemException, Exception {
		ProcessoGuiaEmissaoConsultaDt processoDt = null;
		
		if (numeroProcessoDt != null && numeroProcessoDt.isValido())
		{
			FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);	
			try{
				processoDt = new ProcessoGuiaEmissaoConsultaPs(obFabricaConexao.getConexao()).obtenhaProcesso(numeroProcessoDt);
			}finally{
				obFabricaConexao.fecharConexao();
			}
		}	
		
		return processoDt;			
	}
}
