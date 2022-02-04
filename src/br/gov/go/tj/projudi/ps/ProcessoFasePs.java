package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.ProcessoFaseDt;
import java.sql.Connection;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class ProcessoFasePs extends ProcessoFasePsGen {

	/**
     * 
     */
    private static final long serialVersionUID = -6032979595380509218L;

    public ProcessoFasePs(Connection conexao){
    	Conexao = conexao;
	}

	/**
	 * Sobrescrevendo método para retornar somenete as fases ativas, onde CodigoTemp é nulo
	 * @author msapaula
	 */
	public List consultarDescricao(String descricao, String posicao) throws Exception {
		
		String Sql;
		String SqlFrom;
		String SqlOrder;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		Sql = "SELECT ID_PROC_FASE, PROC_FASE";
		SqlFrom = " FROM PROJUDI.VIEW_PROC_FASE";
		SqlFrom += " WHERE PROC_FASE LIKE  ?";
		ps.adicionarString("%"+ descricao +"%");
		SqlFrom += " AND CODIGO_TEMP IS NULL";
		SqlOrder = " ORDER BY PROC_FASE ";
		
		try{
			rs1 = consultarPaginacao(Sql + SqlFrom + SqlOrder, ps, posicao);

			while (rs1.next()) {
				ProcessoFaseDt obTemp = new ProcessoFaseDt();
				obTemp.setId(rs1.getString("ID_PROC_FASE"));
				obTemp.setProcessoFase(rs1.getString("PROC_FASE"));
				liTemp.add(obTemp);
			}
			//rs1.close();
			Sql = "SELECT COUNT(*) as QUANTIDADE";
			rs2 = consultar(Sql + SqlFrom, ps);
			if (rs2.next())	liTemp.add(rs2.getLong("QUANTIDADE"));
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }
		return liTemp;
	}

	/**
	 * Método que realiza a consulta para obter registro de ProcessoFase 
	 * correspondente ao código passado
	 */
	public ProcessoFaseDt consultarProcessoFaseCodigo(int processoFaseCodigo) throws Exception {
		ProcessoFaseDt processoFaseDt = new ProcessoFaseDt();
		ResultSetTJGO rs1 = null;
		String sql;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		try{
			sql = "SELECT * FROM PROJUDI.VIEW_PROC_FASE pf WHERE pf.PROC_FASE_CODIGO = ?";
				ps.adicionarLong(processoFaseCodigo);

			rs1 = consultar(sql,ps);
			if (rs1.next()) {
				processoFaseDt.setId(rs1.getString("ID_PROC_FASE"));
				processoFaseDt.setProcessoFaseCodigo(rs1.getString("PROC_FASE_CODIGO"));
				processoFaseDt.setProcessoFase(rs1.getString("PROC_FASE"));
				processoFaseDt.setCodigoTemp(rs1.getString("CODIGO_TEMP"));
			}
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return processoFaseDt;
	}
	
	public String consultarDescricaoJSON(String descricao, String posicao) throws Exception {
		
		String Sql;
		String SqlFrom;
		String SqlOrder;
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 1;
		
		Sql = "SELECT ID_PROC_FASE AS ID, PROC_FASE DESCRICAO1";
		SqlFrom = " FROM PROJUDI.VIEW_PROC_FASE";
		SqlFrom += " WHERE PROC_FASE LIKE  ?";
		ps.adicionarString("%"+ descricao +"%");
		SqlFrom += " AND CODIGO_TEMP IS NULL";
		SqlOrder = " ORDER BY PROC_FASE ";
		
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
