package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.EventoExecucaoStatusDt;
import java.sql.Connection;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

//---------------------------------------------------------
public class EventoExecucaoStatusPs extends EventoExecucaoStatusPsGen{

/**
     * 
     */
    private static final long serialVersionUID = -6308162724387324140L;

    public EventoExecucaoStatusPs(Connection conexao){
    	Conexao = conexao;
    }

	//
	public List consultarDescricao(String descricao, String posicao, boolean isMostrarNaoAplica ) throws Exception {

		String Sql;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		//Não mostrar o item 4 = Não se aplica
		Sql= "SELECT ID_EVENTO_EXE_STATUS, EVENTO_EXE_STATUS FROM PROJUDI.VIEW_EVENTO_EXE_STATUS WHERE EVENTO_EXE_STATUS LIKE ? ";
		ps.adicionarString("%"+ descricao +"%");
		if (!isMostrarNaoAplica){
			Sql+= " AND ID_EVENTO_EXE_STATUS <> ? ";
			ps.adicionarLong(EventoExecucaoStatusDt.NAO_APLICA);	
		}
		Sql += 	"ORDER BY EVENTO_EXE_STATUS ";
		
		try{
			rs1 = consultar(Sql, ps);

			while (rs1.next()) {
				EventoExecucaoStatusDt obTemp = new EventoExecucaoStatusDt();
				obTemp.setId(rs1.getString("ID_EVENTO_EXE_STATUS"));
				obTemp.setEventoExecucaoStatus(rs1.getString("EVENTO_EXE_STATUS"));
				liTemp.add(obTemp);
			}
//			if (posicao.length() > 0){
//				Sql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_EVENTO_EXE_STATUS WHERE EVENTO_EXE_STATUS LIKE ? ";
//				if (!isMostrarNaoAplica){
//					Sql += "AND ID_EVENTO_EXE_STATUS <> ?";
//				}
//				rs2 = consultar(Sql, ps);
//				if (rs2.next()) liTemp.add(rs2.getLong("QUANTIDADE"));	
//			}
						
		} finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }
		return liTemp; 
	}

	public String consultarDescricaoJSON(String descricao, String posicao, boolean isMostrarNaoAplica ) throws Exception {
		
		String Sql;
		String SqlFrom;
		String SqlOrder;
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		int qtdeColunas = 1;
		
		//Não mostrar o item 4 = Não se aplica
		Sql= "SELECT ID_EVENTO_EXE_STATUS  AS ID, EVENTO_EXE_STATUS AS DESCRICAO1";
		SqlFrom = " FROM PROJUDI.VIEW_EVENTO_EXE_STATUS";
		SqlFrom += " WHERE EVENTO_EXE_STATUS LIKE ? ";
		
		ps.adicionarString("%"+ descricao +"%");
		if (!isMostrarNaoAplica){
			SqlFrom += " AND ID_EVENTO_EXE_STATUS <> ? ";
			ps.adicionarLong(EventoExecucaoStatusDt.NAO_APLICA);	
		}
		SqlOrder = " ORDER BY EVENTO_EXE_STATUS ";
		
		try{
			rs1 = consultar(Sql + SqlFrom + SqlOrder, ps);
			Sql= "SELECT COUNT(*) as QUANTIDADE";				
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
