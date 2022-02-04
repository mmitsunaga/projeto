package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.ProcessoFaseDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class ProcessoFasePsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = 75110767596252492L;

	//---------------------------------------------------------
	public ProcessoFasePsGen() {

	}



//---------------------------------------------------------
	public void inserir(ProcessoFaseDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psProcessoFaseinserir()");
		stSqlCampos= "INSERT INTO PROJUDI.PROC_FASE ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getProcessoFase().length()>0)) {
			 stSqlCampos+=   stVirgula + "PROC_FASE " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getProcessoFase());  

			stVirgula=",";
		}
		if ((dados.getProcessoFaseCodigo().length()>0)) {
			 stSqlCampos+=   stVirgula + "PROC_FASE_CODIGO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getProcessoFaseCodigo());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_PROC_FASE",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(ProcessoFaseDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psProcessoFasealterar()");

		stSql= "UPDATE PROJUDI.PROC_FASE SET  ";
		stSql+= "PROC_FASE = ?";		 ps.adicionarString(dados.getProcessoFase());  

		stSql+= ",PROC_FASE_CODIGO = ?";		 ps.adicionarLong(dados.getProcessoFaseCodigo());  

		stSql += " WHERE ID_PROC_FASE  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psProcessoFaseexcluir()");

		stSql= "DELETE FROM PROJUDI.PROC_FASE";
		stSql += " WHERE ID_PROC_FASE = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public ProcessoFaseDt consultarId(String id_processofase )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ProcessoFaseDt Dados=null;
		////System.out.println("....ps-ConsultaId_ProcessoFase)");

		stSql= "SELECT * FROM PROJUDI.VIEW_PROC_FASE WHERE ID_PROC_FASE = ?";		ps.adicionarLong(id_processofase); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_ProcessoFase  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new ProcessoFaseDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( ProcessoFaseDt Dados, ResultSetTJGO rs )  throws Exception {

		Dados.setId(rs.getString("ID_PROC_FASE"));
		Dados.setProcessoFase(rs.getString("PROC_FASE"));
		Dados.setProcessoFaseCodigo( rs.getString("PROC_FASE_CODIGO"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));

	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoProcessoFase()");

		stSql= "SELECT ID_PROC_FASE, PROC_FASE FROM PROJUDI.VIEW_PROC_FASE WHERE PROC_FASE LIKE ?";
		stSql+= " ORDER BY PROC_FASE ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoProcessoFase  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				ProcessoFaseDt obTemp = new ProcessoFaseDt();
				obTemp.setId(rs1.getString("ID_PROC_FASE"));
				obTemp.setProcessoFase(rs1.getString("PROC_FASE"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_PROC_FASE WHERE PROC_FASE LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..ProcessoFasePsGen.consultarDescricao() Operação realizada com sucesso");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return liTemp; 
	}

} 
