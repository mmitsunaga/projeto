package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.ProcessoPrioridadeDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class ProcessoPrioridadePsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = 1051346264549755844L;

	//---------------------------------------------------------
	public ProcessoPrioridadePsGen() {

	}



//---------------------------------------------------------
	public void inserir(ProcessoPrioridadeDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psProcessoPrioridadeinserir()");
		stSqlCampos= "INSERT INTO PROJUDI.PROC_PRIOR ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getProcessoPrioridade().length()>0)) {
			 stSqlCampos+=   stVirgula + "PROC_PRIOR " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getProcessoPrioridade());  

			stVirgula=",";
		}
		if ((dados.getProcessoPrioridadeCodigo().length()>0)) {
			 stSqlCampos+=   stVirgula + "PROC_PRIOR_CODIGO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getProcessoPrioridadeCodigo());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_PROC_PRIOR",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(ProcessoPrioridadeDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psProcessoPrioridadealterar()");

		stSql= "UPDATE PROJUDI.PROC_PRIOR SET  ";
		stSql+= "PROC_PRIOR = ?";		 ps.adicionarString(dados.getProcessoPrioridade());  

		stSql+= ",PROC_PRIOR_CODIGO = ?";		 ps.adicionarLong(dados.getProcessoPrioridadeCodigo());  

		stSql += " WHERE ID_PROC_PRIOR  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psProcessoPrioridadeexcluir()");

		stSql= "DELETE FROM PROJUDI.PROC_PRIOR";
		stSql += " WHERE ID_PROC_PRIOR = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public ProcessoPrioridadeDt consultarId(String id_processoprioridade )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ProcessoPrioridadeDt Dados=null;
		////System.out.println("....ps-ConsultaId_ProcessoPrioridade)");

		stSql= "SELECT * FROM PROJUDI.VIEW_PROC_PRIOR WHERE ID_PROC_PRIOR = ?";		ps.adicionarLong(id_processoprioridade); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_ProcessoPrioridade  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new ProcessoPrioridadeDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( ProcessoPrioridadeDt Dados, ResultSetTJGO rs )  throws Exception {
		
		Dados.setId(rs.getString("ID_PROC_PRIOR"));
		Dados.setProcessoPrioridade(rs.getString("PROC_PRIOR"));
		Dados.setProcessoPrioridadeCodigo( rs.getString("PROC_PRIOR_CODIGO"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
		
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoProcessoPrioridade()");

		stSql= "SELECT ID_PROC_PRIOR, PROC_PRIOR FROM PROJUDI.VIEW_PROC_PRIOR WHERE PROC_PRIOR LIKE ?";
		stSql+= " ORDER BY PROC_PRIOR ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoProcessoPrioridade  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				ProcessoPrioridadeDt obTemp = new ProcessoPrioridadeDt();
				obTemp.setId(rs1.getString("ID_PROC_PRIOR"));
				obTemp.setProcessoPrioridade(rs1.getString("PROC_PRIOR"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_PROC_PRIOR WHERE PROC_PRIOR LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..ProcessoPrioridadePsGen.consultarDescricao() Operação realizada com sucesso");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return liTemp; 
	}

} 
