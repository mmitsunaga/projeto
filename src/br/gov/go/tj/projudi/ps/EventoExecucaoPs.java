package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.EventoExecucaoDt;
import java.sql.Connection;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


//---------------------------------------------------------
public class EventoExecucaoPs extends EventoExecucaoPsGen{

	/**
     * 
     */
    private static final long serialVersionUID = -6280516878780223255L;

    public EventoExecucaoPs(Connection conexao){
    	Conexao = conexao;
	}

	/**
	 * Sobrescreveu o método do gerador. Consulta a descrição dos eventos, exceto o evento Trânsito em Julgado, evento Guia de Recolhimento Provisória e modalidades da Pena Restritiva de Direito.
	 * @author wcsilva
	 */
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String Sql;
		String SqlFrom;		
		String SqlOrder;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql= "SELECT ID_EVENTO_EXE, EVENTO_EXE, EVENTO_EXE_TIPO, OBSERVACAO";
		SqlFrom = " FROM PROJUDI.VIEW_EVENTO_EXE ";
		
		SqlFrom += " WHERE EVENTO_EXE LIKE ? ";
		ps.adicionarString("%" + descricao + "%");
		SqlFrom += " AND ID_EVENTO_EXE NOT IN (?,?,?,?,?,?,?,?,?) ";
		ps.adicionarLong(EventoExecucaoDt.TRANSITO_JULGADO);
		ps.adicionarLong(EventoExecucaoDt.GUIA_RECOLHIMENTO_PROVISORIA);		
		ps.adicionarLong(EventoExecucaoDt.CONCESSAO_SURSIS);
		ps.adicionarLong(EventoExecucaoDt.LIMITACAO_FIM_SEMANA);
		ps.adicionarLong(EventoExecucaoDt.PERDA_BENS_VALORES);
		ps.adicionarLong(EventoExecucaoDt.PRESTACAO_PECUNIARIA);
		ps.adicionarLong(EventoExecucaoDt.CESTA_BASICA);
		ps.adicionarLong(EventoExecucaoDt.PRESTACAO_SERVICO_COMUNIDADE);
		ps.adicionarLong(EventoExecucaoDt.INTERDICAO_TEMPORARIA_DIREITOS);

		SqlOrder = " ORDER BY EVENTO_EXE ";
					
		try{

			rs1 = consultarPaginacao(Sql + SqlFrom + SqlOrder, ps, posicao);
			while (rs1.next()) {
				EventoExecucaoDt obTemp = new EventoExecucaoDt();
				obTemp.setId(rs1.getString("ID_EVENTO_EXE"));
				obTemp.setEventoExecucao(rs1.getString("EVENTO_EXE"));
				obTemp.setEventoExecucaoTipo(rs1.getString("EVENTO_EXE_TIPO"));
				obTemp.setObservacao(rs1.getString("OBSERVACAO"));
				liTemp.add(obTemp);
			}
			Sql= "SELECT COUNT(*) as QUANTIDADE";			
			rs2 = consultar(Sql + SqlFrom, ps);			
			if (rs2.next())	liTemp.add(rs2.getLong("QUANTIDADE"));			
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
		int qtdeColunas = 3;

		stSql= "SELECT ID_EVENTO_EXE AS ID, EVENTO_EXE AS DESCRICAO1, OBSERVACAO AS DESCRICAO2, EVENTO_EXE_TIPO AS DESCRICAO3";
		stSqlFrom = " FROM PROJUDI.VIEW_EVENTO_EXE";		
		
		stSqlFrom += " WHERE EVENTO_EXE LIKE ? ";
		ps.adicionarString("%" + descricao + "%");
		stSqlFrom += " AND ID_EVENTO_EXE NOT IN (?,?,?,?,?,?,?,?,?) ";
		ps.adicionarLong(EventoExecucaoDt.TRANSITO_JULGADO);
		ps.adicionarLong(EventoExecucaoDt.GUIA_RECOLHIMENTO_PROVISORIA);		
		ps.adicionarLong(EventoExecucaoDt.CONCESSAO_SURSIS);
		ps.adicionarLong(EventoExecucaoDt.LIMITACAO_FIM_SEMANA);
		ps.adicionarLong(EventoExecucaoDt.PERDA_BENS_VALORES);
		ps.adicionarLong(EventoExecucaoDt.PRESTACAO_PECUNIARIA);
		ps.adicionarLong(EventoExecucaoDt.CESTA_BASICA);
		ps.adicionarLong(EventoExecucaoDt.PRESTACAO_SERVICO_COMUNIDADE);
		ps.adicionarLong(EventoExecucaoDt.INTERDICAO_TEMPORARIA_DIREITOS);
		
		stSqlOrder = " ORDER BY EVENTO_EXE ";
		
		try{
			rs1 = consultarPaginacao(stSql + stSqlFrom + stSqlOrder, ps, posicao);
			stSql= "SELECT COUNT(*) as QUANTIDADE";			
			rs2 = consultar(stSql + stSqlFrom, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
		} finally {
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return stTemp; 
	}
	
	public String consultarIdJSON(String id) throws Exception {

		String stSql="";
		String stSqlFrom ="";
		String stSqlOrder ="";
		String stTemp = "";
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 3;
		//Como o método busca por ID, sempre retornará apenas um registro.
		String posicao = "0";

		stSql= "SELECT ID_EVENTO_EXE AS ID, EVENTO_EXE AS DESCRICAO1, OBSERVACAO AS DESCRICAO2, EVENTO_EXE_TIPO AS DESCRICAO3";
		stSqlFrom = " FROM PROJUDI.VIEW_EVENTO_EXE ";
		
		stSqlFrom += " WHERE ID_EVENTO_EXE = ? ";
		ps.adicionarLong(id);		
		stSqlOrder = " ORDER BY EVENTO_EXE ";
		
		try{
			rs1 = consultarPaginacao(stSql + stSqlFrom + stSqlOrder, ps, posicao);
			stSql= "SELECT COUNT(*) as QUANTIDADE";			
			rs2 = consultar(stSql + stSqlFrom, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
		} finally {
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return stTemp; 
	}
	
}
