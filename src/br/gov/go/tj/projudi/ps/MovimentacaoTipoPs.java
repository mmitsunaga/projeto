package br.gov.go.tj.projudi.ps;

import br.gov.go.tj.projudi.dt.MovimentacaoTipoDt;
import java.sql.Connection;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

//---------------------------------------------------------
public class MovimentacaoTipoPs extends MovimentacaoTipoPsGen {


	/**
     * 
     */
    private static final long serialVersionUID = 6291120705562143753L;

    public MovimentacaoTipoPs(Connection conexao){
    	Conexao = conexao;
	}

	/**
	 *  Método responsável em buscar o objeto MovimentacaoTipoDt correspondente ao código passado
	 */
	public MovimentacaoTipoDt consultaMovimentacaoTipoCodigo(int movimentacaoTipoCodigo) throws Exception {
		String Sql;
		MovimentacaoTipoDt Dados = new MovimentacaoTipoDt();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		Sql = "SELECT * FROM VIEW_MOVI_TIPO m WHERE m.MOVI_TIPO_CODIGO= ?";
		ps.adicionarLong(movimentacaoTipoCodigo);
		try{
			rs1 = consultar(Sql,ps);
			if (rs1.next()) {
				Dados.setId(rs1.getString("ID_MOVI_TIPO"));
				Dados.setMovimentacaoTipo(rs1.getString("MOVI_TIPO"));
				Dados.setMovimentacaoTipoCodigo(rs1.getString("MOVI_TIPO_CODIGO"));
				Dados.setCodigoTemp(rs1.getString("CODIGO_TEMP"));
				Dados.setId_CNJ(rs1.getString("ID_CNJ_MOVI"));
			}
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return Dados;
	}

	public String consultarCodigoMovimentacaoTipo(String id_movimentacaotipo )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String codMovimentacaoTipo=null;

		stSql= "SELECT MOVI_TIPO_CODIGO FROM PROJUDI.VIEW_MOVI_TIPO WHERE ID_MOVI_TIPO = ?";		ps.adicionarLong(id_movimentacaotipo); 

		try{
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				codMovimentacaoTipo = rs1.getString("MOVI_TIPO_CODIGO");
			}
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return codMovimentacaoTipo; 
	}
	
	public String consultarDescricaoJSON(String descricao, String posicao ) throws Exception {

		String stSql="";
		String stSqlFrom ="";
		String stSqlOrder ="";
		String stTemp = "";
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 1;

		stSql= "SELECT ID_MOVI_TIPO AS ID, MOVI_TIPO || ' (CNJ:' || ID_CNJ_MOVI || ')' AS DESCRICAO1";
		stSqlFrom = " FROM PROJUDI.VIEW_MOVI_TIPO";
		stSqlFrom += " WHERE MOVI_TIPO LIKE ?";
		stSqlOrder = " ORDER BY MOVI_TIPO ";
		ps.adicionarString("%"+descricao+"%"); 

		try{

			rs1 = consultarPaginacao(stSql + stSqlFrom + stSqlOrder, ps, posicao);
			stSql= "SELECT COUNT(*) as QUANTIDADE";
			rs2 = consultar(stSql + stSqlFrom,ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
			
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return stTemp; 
	}
	
	public String consultaTodosMarcadosJSON(String descricao, String posicao, String id_Usuario, String grupoCodigo, boolean somenteMarcados)throws Exception {

		somenteMarcados = true;
		String Sql;
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		Sql= "SELECT UMT.ID_USU_MOVI_TIPO AS ID, MT.MOVI_TIPO || ' (CNJ:' || MT.ID_CNJ_MOVI || ')' AS DESCRICAO1, GCC.ID_MOVI_TIPO AS DESCRICAO2";
		Sql+= " FROM (((PROJUDI.GRUPO_MOVI_TIPO GCC";
		Sql+= " JOIN PROJUDI.GRUPO G ON((GCC.ID_GRUPO = G.ID_GRUPO)))";
		Sql+= " JOIN PROJUDI.MOVI_TIPO MT ON((GCC.ID_MOVI_TIPO = MT.ID_MOVI_TIPO)))";
		Sql+= (somenteMarcados ? "" : " LEFT");
		Sql+= " JOIN PROJUDI.USU_MOVI_TIPO UMT ON ((MT.ID_MOVI_TIPO = UMT.ID_MOVI_TIPO AND G.GRUPO_CODIGO = UMT.GRUPO_CODIGO AND UMT.ID_USU = ?)))";
		ps.adicionarLong(id_Usuario);
		Sql+= " WHERE G.GRUPO_CODIGO = ?";
		ps.adicionarLong(grupoCodigo);
		Sql+= " AND MT.MOVI_TIPO LIKE ?";
		ps.adicionarString("%"+descricao+"%");
		Sql+= " AND MT.ID_CNJ_MOVI is not null";	
		Sql+= " ORDER BY MT.MOVI_TIPO";		
		
		try{
			
			rs1 = consultarPaginacao(Sql, ps, posicao);

			Sql= "SELECT COUNT(*) as QUANTIDADE";
			Sql+= " FROM (((PROJUDI.GRUPO_MOVI_TIPO GCC";
			Sql+= " JOIN PROJUDI.GRUPO G ON((GCC.ID_GRUPO = G.ID_GRUPO)))";
			Sql+= " JOIN PROJUDI.MOVI_TIPO MT ON((GCC.ID_MOVI_TIPO = MT.ID_MOVI_TIPO)))";
			Sql+= (somenteMarcados ? "" : " LEFT");
			Sql+= " JOIN PROJUDI.USU_MOVI_TIPO UMT ON ((MT.ID_MOVI_TIPO = UMT.ID_MOVI_TIPO AND G.GRUPO_CODIGO = UMT.GRUPO_CODIGO AND UMT.ID_USU = ?)))";
			Sql+= " WHERE G.GRUPO_CODIGO = ?";
			Sql+= " AND MT.MOVI_TIPO LIKE ?";
			Sql+= " AND MT.ID_CNJ_MOVI is not null";				
			
			rs2 = consultar(Sql,ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, 2);
		
		} finally{
	         try {if (rs1 != null) rs1.close();} catch (Exception e1) {}
	    } 
		return stTemp; 
	}	
}