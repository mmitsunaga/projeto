package br.gov.go.tj.projudi.ps;

import java.sql.Connection;

import br.gov.go.tj.projudi.dt.ProcessoObjetoArquivoMovimentacaoDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;
//---------------------------------------------------------
public class ProcessoObjetoArquivoMovimentacaoPs extends ProcessoObjetoArquivoMovimentacaoPsGen{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3630480548058426425L;
	private ProcessoObjetoArquivoMovimentacaoPs( ) {}
	public ProcessoObjetoArquivoMovimentacaoPs(Connection conexao) {
		Conexao = conexao;
	}
//
	public String consultarListaMovimentosObjetoJSON(String stId_Obejto) throws Exception {
		String stSql, stSqlFrom;
		String stTemp="";
		
		ResultSetTJGO rs1=null;
		
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSql= "SELECT * ";
		stSqlFrom= " FROM VIEW_PROC_OBJETO_ARQ_MOVI WHERE ID_PROC_OBJETO_ARQ = ? ";		ps.adicionarLong(stId_Obejto); 
		
		try{
			ProcessoObjetoArquivoMovimentacaoDt dt;
			rs1 = consultar(stSql+stSqlFrom, ps );
			stTemp="[";
			if (rs1.next()){	
				dt = new ProcessoObjetoArquivoMovimentacaoDt();
				associarDt(dt, rs1); 
				stTemp+=dt.toJson();											
			}
			//if e while para não precisar testar toda hora se posso ou nao colocar a virgula
			while (rs1.next()){									
				dt = new ProcessoObjetoArquivoMovimentacaoDt();
				associarDt(dt, rs1); 
				stTemp+="," + dt.toJson();					
			}
			stTemp+="]";							

		}finally{
			try {if (rs1 != null) rs1.close();} catch (Exception e) {}
		}
		return stTemp; 
	}

}
