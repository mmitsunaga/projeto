package br.gov.go.tj.projudi.ps;

import java.sql.Connection;

import br.gov.go.tj.projudi.dt.ProcessoPartePrisaoDt;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;
//---------------------------------------------------------
public class ProcessoPartePrisaoPs extends ProcessoPartePrisaoPsGen{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2512169891950642339L;
	private ProcessoPartePrisaoPs( ) {}
	public ProcessoPartePrisaoPs(Connection conexao) {
		Conexao = conexao;
	}

	//---------------------------------------------------------
	public String consultarPrisoesParteJSON(String id_processoParte ) throws Exception {

		String stSql, stSqlFrom, stSqlOrder;
		String stTemp="";
		
		ResultSetTJGO rs1=null;
		
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSql= "SELECT * ";
		stSqlFrom= " FROM view_proc_parte_prisao WHERE ID_PROC_PARTE = ? ";		ps.adicionarLong(id_processoParte); 

		stSqlOrder= " ORDER BY PRISAO_TIPO ";
		try{


			rs1 = consultar(stSql+stSqlFrom+stSqlOrder, ps );
			stTemp="[";
			if (rs1.next()){	
				stTemp+=atribuirProcessoPartePrissao( rs1);											
			}
			//if e while para não precisar testar toda hora se posso ou nao colocar a virgula
			while (rs1.next()){											
				stTemp+="," + atribuirProcessoPartePrissao( rs1);					
			}
			stTemp+="]";							

		}finally{
			try {if (rs1 != null) rs1.close();} catch (Exception e) {}
		}
		return stTemp; 
			
	}
	
	
	private String atribuirProcessoPartePrissao( ResultSetTJGO rs1) throws Exception {
		ProcessoPartePrisaoDt dt = new ProcessoPartePrisaoDt();
		dt.setId(rs1.getString("ID_PROC_PARTE_PRISAO"));
		dt.setId_ProcessoParte(rs1.getString("ID_PROC_PARTE"));
		dt.setId_PrisaoTipo(rs1.getString("ID_PRISAO_TIPO"));
		dt.setId_LocalCumpPena(rs1.getString("ID_LOCAL_CUMP_PENA"));
		dt.setId_EventoTipo(rs1.getString("ID_EVENTO_TIPO"));
		dt.setId_MoviEvento(rs1.getString("ID_MOVI_EVENTO"));
		dt.setId_MoviPrisao(rs1.getString("ID_MOVI_PRISAO"));
		
		dt.setDataPrisao(Funcoes.FormatarData(rs1.getDate("DATA_PRISAO")));
		dt.setDataEvento(Funcoes.FormatarData(rs1.getDate("DATA_EVENTO")));
		
		dt.setEventoTipo(Funcoes.limparStringToJSON( rs1.getString("EVENTO_TIPO")));
				
		dt.setPrazoPrisao(rs1.getString("PRAZO_PRISAO"));
		
		dt.setLocalCumpPena(Funcoes.limparStringToJSON(rs1.getString("LOCAL_CUMP_PENA")));
		dt.setMoviEvento(Funcoes.limparStringToJSON(rs1.getString("MOVI_EVENTO")));
		dt.setMoviPrisao(Funcoes.limparStringToJSON(rs1.getString("MOVI_PRISAO")));
		dt.setObservacao(Funcoes.limparStringToJSON(rs1.getString("OBSERVACAO")));
		dt.setPrisaoTipo(Funcoes.limparStringToJSON(rs1.getString("PRISAO_TIPO")));
		
		
		return dt.toJson();
	}
	
	public ProcessoPartePrisaoDt consultarUltimaPrisoesParte(String id_proc_parte) throws Exception {
		String stSql, stSqlFrom;
		ProcessoPartePrisaoDt stTemp= null;
		
		ResultSetTJGO rs1=null;
		
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSql= "SELECT * ";
		stSqlFrom= " FROM view_proc_parte_prisao WHERE ";		
		stSqlFrom+= "  id_proc_parte_prisao = (Select MAX(id_proc_parte_prisao) FROM PROC_PARTE_PRISAO_NOVO WHERE ID_PROC_PARTE = ? and ID_MOVI_EVENTO is null)";		ps.adicionarLong(id_proc_parte); 
				
		rs1 = consultar(stSql+stSqlFrom, ps );
		
		try{
			
			if (rs1.next()){
				stTemp = new ProcessoPartePrisaoDt();
				associarDt(stTemp, rs1);													
			}								

		}finally{
			try {if (rs1 != null) rs1.close();} catch (Exception e) {}
		}
		return stTemp; 
	}

}
