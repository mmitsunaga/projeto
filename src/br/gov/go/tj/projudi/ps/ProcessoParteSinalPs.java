package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.ProcessoParteSinalDt;
import java.sql.Connection;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

//---------------------------------------------------------
public class ProcessoParteSinalPs extends ProcessoParteSinalPsGen{

	public ProcessoParteSinalPs(Connection conexao){
    	Conexao = conexao;
	}

	/**
     * 
     */
    private static final long serialVersionUID = -1206094377474068782L;

    public List listarSinal(String idProcessoParte)  throws Exception {
		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		List lista = new ArrayList();

		stSql= "SELECT * FROM PROJUDI.VIEW_PROC_PARTE_SINAL WHERE ID_PROC_PARTE = ?";
		ps.adicionarLong(idProcessoParte); 

		try{
			rs1 = consultar(stSql,ps);
			while(rs1.next()) {
				ProcessoParteSinalDt dados = new ProcessoParteSinalDt();
				associarDt(dados, rs1);
				lista.add(dados);
			}
		}finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return lista; 
	}
}
