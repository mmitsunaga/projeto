package br.gov.go.tj.projudi.ps;

import br.gov.go.tj.projudi.dt.TarefaStatusDt;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

//---------------------------------------------------------
public class TarefaStatusPs extends TarefaStatusPsGen{


	/**
     * 
     */
    private static final long serialVersionUID = 6413889139984491129L;

    public TarefaStatusPs(Connection conexao){
    	Conexao = conexao;
	}

	/**
	 * Recupera a chave primaria do atributo Status de Tarefa
	 * com código igual à constante que indica o Status "Aberto" [codigo = 1].
	 * 
	 * @return String
	 * @throws Exception
	 */
	public String consultarIdStatusAguardandoVisto()  throws Exception {
		String Sql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		//System.out.println("....ps-ConsultaId_TarefaStatus)");
		
		Sql= "SELECT ID_TAREFA_STATUS FROM PROJUDI.VIEW_TAREFA_STATUS WHERE TAREFA_STATUS_CODIGO = ?";
		ps.adicionarLong(TarefaStatusDt.AG_VISTO);
		//System.out.println("....Sql  " + Sql  );
		
		try{
			//System.out.println("..ps-ConsultaId_TarefaStatus  " + Sql);
			rs1 = consultar(Sql, ps);
			if (rs1.next()) {
				return rs1.getString("ID_TAREFA_STATUS");
			}
			//System.out.println("..ps-ConsultaId");
		}finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return null;
	}

	/**
	 * Recupera a chave primaria do atributo Status de Tarefa
	 * com código igual à constante que indica o Status "Aberto" [codigo = 1].
	 * 
	 * @return String
	 * @throws Exception
	 */
	public String consultarIdStatusAberto()  throws Exception {
		String Sql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		//System.out.println("....ps-ConsultaId_TarefaStatus)");

		Sql= "SELECT ID_TAREFA_STATUS FROM PROJUDI.VIEW_TAREFA_STATUS WHERE TAREFA_STATUS_CODIGO = ?";
		ps.adicionarLong(TarefaStatusDt.ABERTA);
		//System.out.println("....Sql  " + Sql  );
		
		try{
			//System.out.println("..ps-ConsultaId_TarefaStatus  " + Sql);
			rs1 = consultar(Sql, ps);
			if (rs1.next()) {
				return rs1.getString("ID_TAREFA_STATUS");
			}
			//System.out.println("..ps-ConsultaId");
		}finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return null;
	}

	/**
	 * Recupera a chave primaria do atributo Status de Tarefa
	 * com código igual à constante que indica o Status "Em Andamento" [codigo = 2].
	 * 
	 * @return String
	 * @throws Exception
	 */
	public String consultarIdStatusEmAndamento()  throws Exception {
		String Sql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		//System.out.println("....ps-ConsultaId_TarefaStatus)");
		
		Sql= "SELECT ID_TAREFA_STATUS FROM PROJUDI.VIEW_TAREFA_STATUS WHERE TAREFA_STATUS_CODIGO = ?";
		ps.adicionarLong(TarefaStatusDt.EM_ANDAMENTO);
		//System.out.println("....Sql  " + Sql  );
		
		try{
			//System.out.println("..ps-ConsultaId_TarefaStatus  " + Sql);
			rs1 = consultar(Sql, ps);
			if (rs1.next()) {
				return rs1.getString("ID_TAREFA_STATUS");
			}
			//System.out.println("..ps-ConsultaId");
		}finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return null;
	}

	public String consultarDescricaoJSON(String descricao, String posicao ) throws Exception {

		String stSql="";
		String stTemp = "";
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 1;

		stSql= "SELECT ID_TAREFA_STATUS AS ID, TAREFA_STATUS AS DESCRICAO1 FROM PROJUDI.VIEW_TAREFA_STATUS WHERE TAREFA_STATUS LIKE ?";
		stSql+= " ORDER BY TAREFA_STATUS ";
		ps.adicionarString("%"+descricao+"%"); 

		try{

			rs1 = consultarPaginacao(stSql, ps, posicao);

			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_TAREFA_STATUS WHERE TAREFA_STATUS LIKE ?";
			rs2 = consultar(stSql,ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return stTemp; 
	}

}
