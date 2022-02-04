package br.gov.go.tj.projudi.ps;

import java.sql.Connection;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class UsuarioAfastamentoPs extends UsuarioAfastamentoPsGen {

	public UsuarioAfastamentoPs(Connection conexao){
		Conexao = conexao;
	}

	private static final long serialVersionUID = -7996625065440878914L;

	public String consultarDescricaoJSON(String descricao, String posicao ) throws Exception {

		String stSql = "";
		String stSqlFrom = "";
		String stSqlOrder = "";
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 1;

		stSql= "SELECT ID_USU_AFASTAMENTO AS ID, USU AS DESCRICAO1";
		stSqlFrom = " FROM PROJUDI.VIEW_USU_AFASTAMENTO";
		stSqlFrom += " WHERE USU LIKE ?";
		stSqlOrder = " ORDER BY USU ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			rs1 = consultarPaginacao(stSql + stSqlFrom + stSqlOrder, ps, posicao);
			stSql= "SELECT COUNT(*) as QUANTIDADE";
			rs2 = consultar(stSql + stSqlFrom, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return stTemp; 
	}
	
	public void registrarUsuarioAfastamento( String idUsuServAfastado, String idUsuServCadastrador, String mandJudExpiradosComplemento ) throws Exception {
		String sql = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		//sql = 	" SELECT ID_USU_AFASTAMENTO FROM USU_AFASTAMENTO WHERE ID_USU = ? "; ps.adicionarLong(idUsuServAfastado);
		//sql +=	" AND ID_USU_FINALIZADOR IS NULL";
		
		sql = 	" INSERT INTO PROJUDI.USU_SERV_AFASTAMENTO ";
		sql +=	" (ID_USU_SERV, ID_AFASTAMENTO, DATA_INICIO, ID_USU_SERV_CADASTRADOR, MOTIVO_INICIO) VALUES (";
		sql += 	" 		?,"; 		ps.adicionarLong(idUsuServAfastado);
		sql +=	"	    9,";
		sql +=	"	    SYSDATE,";
		sql +=	"	    ?,"; 		ps.adicionarLong(idUsuServCadastrador);
		sql +=	"   	?)";		ps.adicionarString(mandJudExpiradosComplemento);
		
		this.executarUpdateDelete(sql, ps);
	}
	
	public void registrarFinalUsuarioAfastamento(String idUsuServAfastado, String idUsuServFinalizador) throws Exception {
		StringBuffer sql = new StringBuffer();
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		sql.append("UPDATE projudi.usu_serv_afastamento SET  data_fim = SYSDATE, id_usu_serv_finalizador = ?,"
				+ " motivo_fim = ? WHERE id_usu_serv = ? AND id_usu_serv_finalizador is NULL");
		
		ps.adicionarLong(idUsuServFinalizador);		
		ps.adicionarString("Todos Mandados Cumpridos");
		ps.adicionarLong(idUsuServAfastado);
	
		this.executarUpdateDelete(sql.toString(), ps);
	}	
}
