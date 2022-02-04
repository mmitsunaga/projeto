package br.gov.go.tj.projudi.ps;

import br.gov.go.tj.projudi.dt.CejuscDisponibilidadeDt;
import java.sql.Connection;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class CejuscDisponibilidadePs extends CejuscDisponibilidadePsGen{

	private static final long serialVersionUID = -6409129441079199778L;
	
	public CejuscDisponibilidadePs(Connection conexao){
		Conexao = conexao;
	}
	
	public String consultarDescricaoJSON(String descricao, String posicao ) throws Exception {

		String stSql="";
		String stSqlFrom ="";
		String stSqlOrder ="";
		String stTemp="";
		int qtdeColunas = 4;
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "SELECT ID_CEJUSC_DISPONIBILIDADE as id, NOME as descricao1, AUDI_TIPO as descricao2, SERV as descricao3, STATUS_USU_CEJUSC as descricao4 ";
		stSqlFrom = " FROM PROJUDI.VIEW_CEJUSC_DISPONIBILIDADE";
		stSqlFrom += " WHERE SERV LIKE ?";
		stSqlOrder = " ORDER BY SERV ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			rs1 = consultarPaginacao(stSql + stSqlFrom + stSqlOrder, ps, posicao);
			stSql= "SELECT Count(*) as Quantidade";
			rs2 = consultar(stSql + stSqlFrom, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);

		}finally{
			try {if (rs1 != null) rs1.close();} catch (Exception e) {}
			try {if (rs2 != null) rs2.close();} catch (Exception e) {}
		}
			return stTemp; 
	}
	
	public String consultarDescricaoJSON(String nome, String serventia, String idUsuario, String posicao ) throws Exception {

		String stSql="";
		String stSqlFrom ="";
		String stSqlOrder ="";
		String stTemp="";
		int qtdeColunas = 3;
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSql = "SELECT ID_CEJUSC_DISPONIBILIDADE as id, NOME as descricao1, AUDI_TIPO as descricao2, SERV as descricao3";
		stSqlFrom = " FROM PROJUDI.VIEW_CEJUSC_DISPONIBILIDADE CD";
		stSqlFrom += " INNER JOIN PROJUDI.USU_CEJUSC UC ON UC.ID_USU_CEJUSC = CD.ID_USU_CEJUSC";
		stSqlFrom += " WHERE SERV LIKE ?";
		ps.adicionarString("%"+serventia+"%");
		stSqlFrom += " AND NOME LIKE ? ";
		ps.adicionarString("%"+nome+"%");
		
		if(idUsuario != null && !idUsuario.isEmpty()) {
			stSqlFrom += " AND ID_USU = ? ";
			ps.adicionarLong(idUsuario);
		}
		stSqlOrder = " ORDER BY SERV ";		

		try{

			rs1 = consultarPaginacao(stSql + stSqlFrom + stSqlOrder, ps, posicao);
			stSql= "SELECT Count(*) as Quantidade";
			rs2 = consultar(stSql + stSqlFrom, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);

		}finally{
			try {if (rs1 != null) rs1.close();} catch (Exception e) {}
			try {if (rs2 != null) rs2.close();} catch (Exception e) {}
		}
		return stTemp; 
	}
	
	public CejuscDisponibilidadeDt consultarUsuarioCejuscDt(String idUsuarioCejusc, String idAudienciaTipo, String idServentia)  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		CejuscDisponibilidadeDt Dados=null;

		stSql= "SELECT * FROM PROJUDI.VIEW_CEJUSC_DISPONIBILIDADE WHERE ID_USU_CEJUSC = ? AND ID_AUDI_TIPO = ? AND ID_SERV = ?";
		ps.adicionarLong(idUsuarioCejusc);
		ps.adicionarLong(idAudienciaTipo);
		ps.adicionarLong(idServentia);

		try{

			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new CejuscDisponibilidadeDt();
				associarDt(Dados, rs1);
			}

		}finally{
			try {if (rs1 != null) rs1.close();} catch (Exception e) {}
		}
		return Dados; 
	}
}
