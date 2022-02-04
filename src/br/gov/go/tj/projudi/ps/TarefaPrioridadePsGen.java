package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.TarefaPrioridadeDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class TarefaPrioridadePsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = -2465806284367581663L;

	//---------------------------------------------------------
	public TarefaPrioridadePsGen() {

	}



//---------------------------------------------------------
	public void inserir(TarefaPrioridadeDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psTarefaPrioridadeinserir()");
		stSqlCampos= "INSERT INTO PROJUDI.TAREFA_PRIOR ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getTarefaPrioridade().length()>0)) {
			 stSqlCampos+=   stVirgula + "TAREFA_PRIOR " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getTarefaPrioridade());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_TAREFA_PRIOR",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(TarefaPrioridadeDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psTarefaPrioridadealterar()");

		stSql= "UPDATE PROJUDI.TAREFA_PRIOR SET  ";
		stSql+= "TAREFA_PRIOR = ?";		 ps.adicionarString(dados.getTarefaPrioridade());  

		stSql += " WHERE ID_TAREFA_PRIOR  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps); 
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psTarefaPrioridadeexcluir()");

		stSql= "DELETE FROM PROJUDI.TAREFA_PRIOR";
		stSql += " WHERE ID_TAREFA_PRIOR = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);
	} 

//---------------------------------------------------------
	public TarefaPrioridadeDt consultarId(String id_TarefaPrioridade )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		TarefaPrioridadeDt Dados=null;
		////System.out.println("....ps-ConsultaId_TarefaPrioridade)");

		stSql= "SELECT * FROM PROJUDI.VIEW_TAREFA_PRIOR WHERE ID_TAREFA_PRIOR = ?";		ps.adicionarLong(id_TarefaPrioridade); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_TarefaPrioridade  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new TarefaPrioridadeDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( TarefaPrioridadeDt Dados, ResultSetTJGO rs )  throws Exception {
		Dados.setId(rs.getString("ID_TAREFA_PRIOR"));
		Dados.setTarefaPrioridade(rs.getString("TAREFA_PRIOR"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoTarefaPrioridade()");

		stSql= "SELECT ID_TAREFA_PRIOR, TAREFA_PRIOR FROM PROJUDI.VIEW_TAREFA_PRIOR WHERE TAREFA_PRIOR LIKE ?";
		stSql+= " ORDER BY TAREFA_PRIOR ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoTarefaPrioridade  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				TarefaPrioridadeDt obTemp = new TarefaPrioridadeDt();
				obTemp.setId(rs1.getString("ID_TAREFA_PRIOR"));
				obTemp.setTarefaPrioridade(rs1.getString("TAREFA_PRIOR"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_TAREFA_PRIOR WHERE TAREFA_PRIOR LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..TarefaPrioridadePsGen.consultarDescricao() Operação realizada com sucesso");
		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return liTemp; 
	}

} 
