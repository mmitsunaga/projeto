package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.TarefaStatusDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class TarefaStatusPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = 2288264780779514867L;

	//---------------------------------------------------------
	public TarefaStatusPsGen() {

	}



//---------------------------------------------------------
	public void inserir(TarefaStatusDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psTarefaStatusinserir()");
		stSqlCampos= "INSERT INTO PROJUDI.TAREFA_STATUS ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getTarefaStatus().length()>0)) {
			 stSqlCampos+=   stVirgula + "TAREFA_STATUS " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getTarefaStatus());  

			stVirgula=",";
		}
		if ((dados.getTarefaStatusCodigo().length()>0)) {
			 stSqlCampos+=   stVirgula + "TAREFA_STATUS_CODIGO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getTarefaStatusCodigo());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_TAREFA_STATUS",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(TarefaStatusDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psTarefaStatusalterar()");

		stSql= "UPDATE PROJUDI.TAREFA_STATUS SET  ";
		stSql+= "TAREFA_STATUS = ?";		 ps.adicionarString(dados.getTarefaStatus());  

		stSql+= ",TAREFA_STATUS_CODIGO = ?";		 ps.adicionarLong(dados.getTarefaStatusCodigo());  

		stSql += " WHERE ID_TAREFA_STATUS  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psTarefaStatusexcluir()");

		stSql= "DELETE FROM PROJUDI.TAREFA_STATUS";
		stSql += " WHERE ID_TAREFA_STATUS = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public TarefaStatusDt consultarId(String id_tarefastatus )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		TarefaStatusDt Dados=null;
		////System.out.println("....ps-ConsultaId_TarefaStatus)");

		stSql= "SELECT * FROM PROJUDI.VIEW_TAREFA_STATUS WHERE ID_TAREFA_STATUS = ?";		ps.adicionarLong(id_tarefastatus); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_TarefaStatus  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new TarefaStatusDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( TarefaStatusDt Dados, ResultSetTJGO rs )  throws Exception {

		Dados.setId(rs.getString("ID_TAREFA_STATUS"));
		Dados.setTarefaStatus(rs.getString("TAREFA_STATUS"));
		Dados.setTarefaStatusCodigo( rs.getString("TAREFA_STATUS_CODIGO"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));

	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoTarefaStatus()");

		stSql= "SELECT ID_TAREFA_STATUS, TAREFA_STATUS FROM PROJUDI.VIEW_TAREFA_STATUS WHERE TAREFA_STATUS LIKE ?";
		stSql+= " ORDER BY TAREFA_STATUS ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoTarefaStatus  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				TarefaStatusDt obTemp = new TarefaStatusDt();
				obTemp.setId(rs1.getString("ID_TAREFA_STATUS"));
				obTemp.setTarefaStatus(rs1.getString("TAREFA_STATUS"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_TAREFA_STATUS WHERE TAREFA_STATUS LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..TarefaStatusPsGen.consultarDescricao() Operação realizada com sucesso");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return liTemp; 
	}

} 
