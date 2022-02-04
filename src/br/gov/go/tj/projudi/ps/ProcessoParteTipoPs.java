package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.ProcessoParteTipoDt;
import java.sql.Connection;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

//---------------------------------------------------------
public class ProcessoParteTipoPs extends ProcessoParteTipoPsGen {

	/**
     * 
     */
    private static final long serialVersionUID = -7545539688975520406L;

    public ProcessoParteTipoPs(Connection conexao){
    	Conexao = conexao;
	}

	/**
	 * Consultar objeto ProcessoParteTipo equivalente ao código passado 
	 */
	public ProcessoParteTipoDt consultarProcessoParteTipoCodigo(String processoParteTipoCodigo) throws Exception {
		String Sql;
		ProcessoParteTipoDt Dados = new ProcessoParteTipoDt();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql = "SELECT * FROM PROJUDI.VIEW_PROC_PARTE_TIPO WHERE PROC_PARTE_TIPO_CODIGO = ?";
		ps.adicionarLong(processoParteTipoCodigo);

		try{
			rs1 = consultar(Sql, ps);
			if (rs1.next()) {
				Dados.setId(rs1.getString("ID_PROC_PARTE_TIPO"));
				Dados.setProcessoParteTipo(rs1.getString("PROC_PARTE_TIPO"));
				Dados.setProcessoParteTipoCodigo(rs1.getString("PROC_PARTE_TIPO_CODIGO"));
				Dados.setCodigoTemp(rs1.getString("CODIGO_TEMP"));
			}
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return Dados;
	}

	/**
	 * Consultar outros tipos de parte disponíveis para cadastro, ou seja,
	 * todas as partes que não sejam promovente e promovido
	 * 
	 * @param descricao, filtro de consulta
	 * @param posicao, parâmetro para paginação
	 * @author msapaula
	 */
	public List consultarOutrosTiposPartes(String descricao, String posicao) throws Exception {
		
		String Sql;
		String SqlFrom;
		String SqlOrder;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		Sql = "SELECT ID_PROC_PARTE_TIPO, PROC_PARTE_TIPO ";
		SqlFrom = " FROM PROJUDI.VIEW_PROC_PARTE_TIPO ";
		SqlFrom += " WHERE PROC_PARTE_TIPO LIKE ? ";
		ps.adicionarString("%"+ descricao +"%");
		SqlFrom += " AND PROC_PARTE_TIPO_CODIGO <> ?";
		ps.adicionarLong(ProcessoParteTipoDt.POLO_ATIVO_CODIGO);
		SqlFrom += " AND PROC_PARTE_TIPO_CODIGO <> ?";
		ps.adicionarLong(ProcessoParteTipoDt.POLO_PASSIVO_CODIGO);
		SqlFrom += " AND PROC_PARTE_TIPO_CODIGO <> ?";
		ps.adicionarLong(ProcessoParteTipoDt.POLO_ATIVO_CODIGO);
		SqlFrom += " AND PROC_PARTE_TIPO_CODIGO <> ?"; 
		ps.adicionarLong(ProcessoParteTipoDt.POLO_PASSIVO_CODIGO);
		SqlOrder = " ORDER BY PROC_PARTE_TIPO ";
		try{
			rs1 = consultarPaginacao(Sql + SqlFrom + SqlOrder, ps, posicao);
			while (rs1.next()) {
				ProcessoParteTipoDt obTemp = new ProcessoParteTipoDt();
				obTemp.setId(rs1.getString("ID_PROC_PARTE_TIPO"));
				obTemp.setProcessoParteTipo(rs1.getString("PROC_PARTE_TIPO"));
				liTemp.add(obTemp);
			}
			//rs1.close();
			Sql = "SELECT COUNT(*) as QUANTIDADE";
			rs2 = consultar(Sql + SqlFrom, ps);
			if (rs2.next())
				liTemp.add(rs2.getLong("QUANTIDADE"));
			
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }
		return liTemp;
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

		stSql = "SELECT ID_PROC_PARTE_TIPO AS ID, PROC_PARTE_TIPO AS DESCRICAO1 ";
		stSqlFrom = " FROM PROJUDI.VIEW_PROC_PARTE_TIPO ";
		stSqlFrom += " WHERE PROC_PARTE_TIPO LIKE ?";
		stSqlOrder = " ORDER BY PROC_PARTE_TIPO ";
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

	public String consultarOutrosTiposPartesJSON(String descricao, String posicao) throws Exception {
		
		String Sql;
		String SqlFrom;
		String SqlOrder;
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 1;
		
		Sql = "SELECT ID_PROC_PARTE_TIPO AS ID, PROC_PARTE_TIPO AS DESCRICAO1";
		SqlFrom = " FROM PROJUDI.VIEW_PROC_PARTE_TIPO ";
		SqlFrom += " WHERE PROC_PARTE_TIPO LIKE ? ";
		ps.adicionarString("%"+ descricao +"%");
		SqlFrom += " AND PROC_PARTE_TIPO_CODIGO <> ?";
		ps.adicionarLong(ProcessoParteTipoDt.POLO_ATIVO_CODIGO);
		SqlFrom += " AND PROC_PARTE_TIPO_CODIGO <> ?";
		ps.adicionarLong(ProcessoParteTipoDt.POLO_PASSIVO_CODIGO);
		SqlFrom += " AND PROC_PARTE_TIPO_CODIGO <> ?";
		ps.adicionarLong(ProcessoParteTipoDt.POLO_ATIVO_CODIGO);
		SqlFrom += " AND PROC_PARTE_TIPO_CODIGO <> ?"; 
		ps.adicionarLong(ProcessoParteTipoDt.POLO_PASSIVO_CODIGO);
		SqlOrder = " ORDER BY PROC_PARTE_TIPO ";
		
		try{
			rs1 = consultarPaginacao(Sql + SqlFrom + SqlOrder, ps, posicao);
			Sql = "SELECT COUNT(*) as QUANTIDADE";
			rs2 = consultar(Sql + SqlFrom, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }
		return stTemp;
	}
}
