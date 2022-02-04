package br.gov.go.tj.projudi.ps;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.ProjetoDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

//---------------------------------------------------------
public class ProjetoPs extends ProjetoPsGen{

	public ProjetoPs(Connection conexao){		
    	Conexao = conexao;
	}

	/**
     * 
     */
    private static final long serialVersionUID = -7603186279902653168L;

//
    public String consultarDescricaoJSON(String descricao, String posicao ) throws Exception {

		String stSql="";
		String stTemp = "";
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 1;
		
		stSql= "SELECT ID_PROJETO AS ID, PROJETO AS DESCRICAO1 FROM PROJUDI.VIEW_PROJETO WHERE PROJETO LIKE ?";
		stSql+= " ORDER BY PROJETO ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			
			rs1 = consultarPaginacao(stSql, ps, posicao);
			
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_PROJETO WHERE PROJETO LIKE ?";
			rs2 = consultar(stSql,ps);
			rs2.next();
				stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
			
			
		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return stTemp; 
	}

}
