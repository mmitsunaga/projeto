package br.gov.go.tj.projudi.ps;

import br.gov.go.tj.projudi.dt.RacaDt;
import java.sql.Connection;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;
//---------------------------------------------------------
public class RacaPs extends RacaPsGen{

	public RacaPs(Connection conexao){
		Conexao = conexao;
	}
//

	public void inserir(RacaDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSqlCampos= "INSERT INTO PROJUDI.RACA ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getRaca().length()>0)) {
			 stSqlCampos+=   stVirgula + "RACA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getRaca());  

			stVirgula=",";
		}
		
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 

		dados.setId(executarInsert(stSql,"ID_RACA",ps));
	} 
	
	public String consultarDescricaoJSON(String descricao, String posicao, String ordenacao, String quantidadeRegistros) throws Exception {
		
		String Sql="";
		String stTemp="";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 1;
		
		Sql = "SELECT ID_RACA AS ID, RACA AS DESCRICAO1 FROM PROJUDI.VIEW_RACA WHERE RACA LIKE ?";
		Sql += " ORDER BY " + ordenacao;
		ps.adicionarString("%" + descricao + "%");
		try {
			rs1 = consultarPaginacao(Sql, ps, posicao, quantidadeRegistros);
			Sql = "SELECT COUNT(*) AS QUANTIDADE FROM PROJUDI.VIEW_RACA WHERE RACA LIKE ?";
			
			rs2 = consultar(Sql, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
			
		} finally{
             try {if (rs1 != null) rs1.close();} catch (Exception e1) {}
             try {if (rs2 != null) rs2.close();} catch (Exception e1) {}
        }
		return stTemp;
	}
	
}
