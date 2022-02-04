package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.ServentiaCargoEscalaStatusDt;
import java.sql.Connection;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class ServentiaCargoEscalaStatusPs extends ServentiaCargoEscalaStatusPsGen {

	
    private static final long serialVersionUID = 1790610294621051515L;

    public ServentiaCargoEscalaStatusPs(Connection conexao){
    	Conexao = conexao;
	}

	public List consultarUsuarioServentiaEscalaStatus(String descricao, String posicao) throws Exception {
		String Sql;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		Sql = "SELECT * FROM PROJUDI.VIEW_SERV_CARGO_ESC_STATUS WHERE SERV_CARGO_ESC_STATUS LIKE ? ORDER BY SERV_CARGO_ESC_STATUS";
		ps.adicionarString( descricao +"%");
		
		try{
			rs1 = consultar(Sql, ps);
			while (rs1.next()) {
				ServentiaCargoEscalaStatusDt serventiaCargoEscalaStatusDt = new ServentiaCargoEscalaStatusDt();
				
				serventiaCargoEscalaStatusDt.setId(rs1.getString("ID_SERV_CARGO_ESC_STATUS"));
				serventiaCargoEscalaStatusDt.setServentiaCargoEscalaStatus(rs1.getString("SERV_CARGO_ESC_STATUS"));
				serventiaCargoEscalaStatusDt.setServentiaCargoEscalaStatusCodigo(rs1.getString("SERV_CARGO_ESC_STATUS_CODIGO"));
				serventiaCargoEscalaStatusDt.setAtivo(rs1.getString("ATIVO"));
				serventiaCargoEscalaStatusDt.setCodigoTemp(rs1.getString("CODIGO_TEMP"));
				
				liTemp.add(serventiaCargoEscalaStatusDt);
			}
			
			Sql = "SELECT COUNT(*) AS QUANTIDADE  FROM PROJUDI.VIEW_SERV_CARGO_ESC_STATUS WHERE SERV_CARGO_ESC_STATUS LIKE ?";			
			rs2 = consultar(Sql, ps);
			if (rs2.next()) liTemp.add(rs2.getLong("QUANTIDADE"));			
			
		}finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }
		return liTemp; 
	}
	
	/**
	 * Consulta o ID do UsuarioServentiaEscalaStatus pelo código.
	 * @param statusCodigo - codigo do status
	 * @return ID do status
	 * @throws Exception
	 * @author hmgodinho
	 */
	public String consultarIdStatusPorCodigo(String statusCodigo) throws Exception {
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		String Sql = "SELECT ID_SERV_CARGO_ESC_STATUS FROM PROJUDI.SERV_CARGO_ESC_STATUS WHERE SERV_CARGO_ESC_STATUS_CODIGO = ? ";
		ps.adicionarLong(statusCodigo);
		try{
			rs1 = consultar(Sql, ps);
			while (rs1.next()) {
				return rs1.getString("ID_SERV_CARGO_ESC_STATUS");
			}
			
		} finally {
             try {if (rs1 != null) rs1.close();} catch (Exception e1) {}
        }
		return ""; 
	}
	
	public String consultarDescricaoJSON(String descricao, String posicao ) throws Exception {

		String stSql="";
		String stTemp = "";
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 1;

		stSql= "SELECT ID_SERV_CARGO_ESC_STATUS AS ID, SERV_CARGO_ESC_STATUS AS DESCRICAO1 FROM PROJUDI.VIEW_SERV_CARGO_ESC_STATUS WHERE SERV_CARGO_ESC_STATUS LIKE ?";
		stSql+= " ORDER BY SERV_CARGO_ESC_STATUS ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			rs1 = consultarPaginacao(stSql, ps, posicao);
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_SERV_CARGO_ESC_STATUS WHERE SERV_CARGO_ESC_STATUS LIKE ?";
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
