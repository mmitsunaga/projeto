package br.gov.go.tj.projudi.ne;

import br.gov.go.tj.projudi.dt.FinanceiroConsultarRepassePrefeituraDt;
import br.gov.go.tj.projudi.dt.RelatorioRepassePrefeituraDt;
import br.gov.go.tj.projudi.ps.FinanceiroConsultarRepassePrefeituraPs;
import br.gov.go.tj.utils.FabricaConexao;

/**
 * 
 * Classe:     FinanceiroConsultarRepassePrefeituraNe.java
 * Autor:      Márcio Mendonça Gomes 
 * Data:       06/2015
 *             
 */
public class FinanceiroConsultarRepassePrefeituraNe extends Negocio{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4808316464969494581L;

	public RelatorioRepassePrefeituraDt obtenhaRelatorioPrevisaoRepassePrefeitura(FinanceiroConsultarRepassePrefeituraDt financeiroConsultarRepassePrefeituraDt) throws Exception
	{		
		FinanceiroConsultarRepassePrefeituraPs persistencia = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try
		{
			 persistencia = new  FinanceiroConsultarRepassePrefeituraPs(obFabricaConexao.getConexao());
			
			 return persistencia.obtenhaRelatorioPrevisaoRepassePrefeitura(financeiroConsultarRepassePrefeituraDt);			 
		}		
		finally{
			obFabricaConexao.fecharConexao();
		}	
	}
}
