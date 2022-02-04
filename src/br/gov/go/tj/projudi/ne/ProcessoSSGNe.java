package br.gov.go.tj.projudi.ne;

import br.gov.go.tj.projudi.dt.NumeroProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteEnderecoSPGDt;
import br.gov.go.tj.projudi.dt.ProcessoSSGDt;
import br.gov.go.tj.projudi.ps.ProcessoSSGPs;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.MensagemException;

public class ProcessoSSGNe extends Negocio {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6346756960927863709L;

	public ProcessoSSGDt consulteProcesso(String numeroProcessoCompleto) throws Exception{
		ProcessoSSGDt processoDt = null;
		
		if (numeroProcessoCompleto != null && numeroProcessoCompleto.trim().length() > 0){
			FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.ADABASPROCESSOS);	
			try{
				NumeroProcessoDt numeroProcessoCompletoDt = new NumeroProcessoDt(numeroProcessoCompleto);
				
				processoDt = new ProcessoSSGPs(obFabricaConexao).consulteProcesso(numeroProcessoCompletoDt);
			}
			finally{
				obFabricaConexao.fecharConexao();
			}
		}	
		
		return processoDt;
	}
	
	public ProcessoSSGDt consulteProcesso(String numeroProcessoCompleto, FabricaConexao obFabricaConexao) throws Exception
	{
		ProcessoSSGDt processoDt = null;
		
		if (numeroProcessoCompleto != null && numeroProcessoCompleto.trim().length() > 0)
		{
			NumeroProcessoDt numeroProcessoCompletoDt = new NumeroProcessoDt(numeroProcessoCompleto);
			
			processoDt = new ProcessoSSGPs(obFabricaConexao).consulteProcesso(numeroProcessoCompletoDt);
		}	
		
		return processoDt;
	}
	
	public ProcessoSSGDt consulteProcessoNumeroSSG(String numeroProcessoSSG) throws Exception
	{
		ProcessoSSGDt processoDt = null;
		
		if (numeroProcessoSSG != null && numeroProcessoSSG.trim().length() > 0)
		{
			
			if( numeroProcessoSSG.contains(".") ) {
				throw new MensagemException("Número do Processo informado não pode conter pontos(.) [Erro3]");
			}
			
			FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.ADABASPROCESSOS);	
			try
			{
				processoDt = new ProcessoSSGPs(obFabricaConexao).consulteProcesso(numeroProcessoSSG);
			}
			finally
			{
				obFabricaConexao.fecharConexao();
			}
		}	
		
		return processoDt;
	}
	
	public ProcessoSSGDt consulteProcessoNumeroSSG(String numeroProcessoSSG, FabricaConexao obFabricaConexao) throws Exception
	{
		ProcessoSSGDt processoDt = null;
		
		if (numeroProcessoSSG != null && numeroProcessoSSG.trim().length() > 0)
		{
			
			if( numeroProcessoSSG.contains(".") ) {
				throw new MensagemException("Número do Processo informado não pode conter pontos(.) [Erro4]");
			}
			
			processoDt = new ProcessoSSGPs(obFabricaConexao).consulteProcesso(numeroProcessoSSG);
		}	
		
		return processoDt;
	}
	
	public ProcessoParteEnderecoSPGDt obtenhaEnderecoParte(String isnParte, FabricaConexao obFabricaConexao) throws Exception
	{
		ProcessoParteEnderecoSPGDt dados = null;
		
		if (isnParte != null && isnParte.trim().length() > 0)
		{
			dados = new ProcessoSSGPs(obFabricaConexao).obtenhaEnderecoParte(isnParte);
		}	
		
		return dados;
	}
}
