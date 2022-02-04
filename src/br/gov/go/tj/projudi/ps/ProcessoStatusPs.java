package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.ProcessoStatusDt;
import java.sql.Connection;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class ProcessoStatusPs extends ProcessoStatusPsGen {

	/**
     * 
     */
    private static final long serialVersionUID = -2147954259108648947L;

    public ProcessoStatusPs(Connection conexao){
    	Conexao = conexao;
	}

	/**
	 * Método que contém a sql que buscará os status de processo existentes
	 * 
	 * @author Keila
	 * @return listaProcessoStatus: lista contendo os status de processo
	 *         existentes
	 * @throws Exception
	 */
	public List getProcessoStatus() throws Exception {
		List listaProcessoStatus = new ArrayList();
		String sql;
		ResultSetTJGO rs1 = null;

		sql = "SELECT * FROM PROJUDI.VIEW_PROC_STATUS ps ORDER BY ps.PROC_STATUS";

		try{
			rs1 = consultarSemParametros(sql);
			while (rs1.next()) {
				ProcessoStatusDt processoStatusDt = new ProcessoStatusDt();
				processoStatusDt.setId(rs1.getString("ID_PROC_STATUS"));
				processoStatusDt.setProcessoStatus(rs1.getString("PROC_STATUS"));
				processoStatusDt.setCodigoTemp(rs1.getString("CODIGO_TEMP"));
				listaProcessoStatus.add(processoStatusDt);
			}
			//rs1.close();
		} finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        }
		return listaProcessoStatus;
	}

	/**
	 * Método que realiza a consulta para obter registro de ProcessoStatus 
	 * correspondente ao código passado
	 */
	public ProcessoStatusDt consultarProcessoStatusCodigo(int processoStatusCodigo) throws Exception {
		ProcessoStatusDt processoStatusDt = new ProcessoStatusDt();
		String sql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		try{

			sql = "SELECT * FROM PROJUDI.VIEW_PROC_STATUS ps WHERE ps.PROC_STATUS_CODIGO = ?";
			ps.adicionarLong(processoStatusCodigo);

			rs1 = consultar(sql,ps);
			if (rs1.next()) {
				processoStatusDt.setId(rs1.getString("ID_PROC_STATUS"));
				processoStatusDt.setProcessoStatus(rs1.getString("PROC_STATUS"));
				processoStatusDt.setProcessoStatusCodigo(rs1.getString("PROC_STATUS_CODIGO"));
				processoStatusDt.setCodigoTemp(rs1.getString("CODIGO_TEMP"));
			}
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return processoStatusDt;
	}

	/**
	 * Sobrescrevendo método para retornar também o ProcessoStatusCodigo
	 */
	public List consultarDescricao(String descricao, String posicao) throws Exception {

		String Sql;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		
		Sql = "SELECT ID_PROC_STATUS, PROC_STATUS, PROC_STATUS_CODIGO FROM PROJUDI.VIEW_PROC_STATUS WHERE PROC_STATUS LIKE  ?";
		ps.adicionarString("%"+ descricao +"%");
		Sql += " ORDER BY PROC_STATUS ";
		
		
		try{
			rs1 = consultar(Sql,ps);

			while (rs1.next()) {
				ProcessoStatusDt obTemp = new ProcessoStatusDt();
				obTemp.setId(rs1.getString("ID_PROC_STATUS"));
				obTemp.setProcessoStatus(rs1.getString("PROC_STATUS"));
				obTemp.setProcessoStatusCodigo(rs1.getString("PROC_STATUS_CODIGO"));
				liTemp.add(obTemp);
			}
			//rs1.close();
			Sql = "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_PROC_STATUS WHERE PROC_STATUS LIKE ?";
			rs2 = consultar(Sql,ps);
			if (rs2.next()) 
				liTemp.add(rs2.getLong("QUANTIDADE"));
			
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }
		return liTemp;
	}
	
	public String consultarDescricaoJSON(String descricao, String posicao) throws Exception {

		String Sql;
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 2;		
		
		Sql = "SELECT ID_PROC_STATUS AS ID, PROC_STATUS AS DESCRICAO1, PROC_STATUS_CODIGO AS DESCRICAO2 FROM PROJUDI.VIEW_PROC_STATUS WHERE PROC_STATUS LIKE  ?";
		ps.adicionarString("%"+ descricao +"%");
		Sql += " AND PROC_STATUS_CODIGO <> ? "; ps.adicionarLong(ProcessoStatusDt.SIGILOSO);
		Sql += " ORDER BY PROC_STATUS ";
		
		try{
			rs1 = consultar(Sql,ps);
			Sql = "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_PROC_STATUS WHERE PROC_STATUS LIKE ?";
			Sql += " AND PROC_STATUS_CODIGO <> ? ";
			rs2 = consultar(Sql,ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }
		return stTemp;
	}
	
	
	//Método duplicado, porém com assinatura diferente devido ter a quantidade de registros passado como paramêtro 09/03/2017 - jlsilva
	public String consultarDescricaoJSON(String descricao, String posicao, String ordenacao, String quantidadeRegistros) throws Exception {
		String Sql="";
		String SqlFrom ="";
		String SqlOrder ="";
		String Temp="";
		int qtdeColunas = 2;
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql = "SELECT ID_PROC_STATUS AS ID, PROC_STATUS AS DESCRICAO1, PROC_STATUS_CODIGO AS DESCRICAO2";
		SqlFrom = " FROM PROJUDI.VIEW_PROC_STATUS";
		SqlFrom += " WHERE PROC_STATUS LIKE  ?";
		ps.adicionarString("%"+ descricao +"%");
		SqlOrder += " ORDER BY "+ordenacao;
		
		try{
			rs1 = consultarPaginacao(Sql + SqlFrom + SqlOrder, ps, posicao, quantidadeRegistros);
			Sql = "SELECT COUNT(*) as QUANTIDADE";
			rs2 = consultar(Sql+SqlFrom, ps);
			rs2.next();
			Temp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }
		return Temp;
	}
	
}
