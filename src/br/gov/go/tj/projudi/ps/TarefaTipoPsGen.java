package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.TarefaTipoDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class TarefaTipoPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = 7640198289729470953L;

	//---------------------------------------------------------
	public TarefaTipoPsGen() {

	}



//---------------------------------------------------------
	public void inserir(TarefaTipoDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psTarefaTipoinserir()");
		stSqlCampos= "INSERT INTO PROJUDI.TAREFA_TIPO ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getTarefaTipo().length()>0)) {
			 stSqlCampos+=   stVirgula + "TAREFA_TIPO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getTarefaTipo());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_TAREFA_TIPO",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(TarefaTipoDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psTarefaTipoalterar()");

		stSql= "UPDATE PROJUDI.TAREFA_TIPO SET  ";
		stSql+= "TAREFA_TIPO = ?";		 ps.adicionarString(dados.getTarefaTipo());  

		stSql += " WHERE ID_TAREFA_TIPO  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psTarefaTipoexcluir()");

		stSql= "DELETE FROM PROJUDI.TAREFA_TIPO";
		stSql += " WHERE ID_TAREFA_TIPO = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public TarefaTipoDt consultarId(String id_tarefatipo )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		TarefaTipoDt Dados=null;
		////System.out.println("....ps-ConsultaId_TarefaTipo)");

		stSql= "SELECT * FROM PROJUDI.VIEW_TAREFA_TIPO WHERE ID_TAREFA_TIPO = ?";		ps.adicionarLong(id_tarefatipo); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_TarefaTipo  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new TarefaTipoDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( TarefaTipoDt Dados, ResultSetTJGO rs )  throws Exception {
		Dados.setId(rs.getString("ID_TAREFA_TIPO"));
		Dados.setTarefaTipo(rs.getString("TAREFA_TIPO"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoTarefaTipo()");

		stSql= "SELECT ID_TAREFA_TIPO, TAREFA_TIPO FROM PROJUDI.VIEW_TAREFA_TIPO WHERE TAREFA_TIPO LIKE ?";
		stSql+= " ORDER BY TAREFA_TIPO ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoTarefaTipo  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				TarefaTipoDt obTemp = new TarefaTipoDt();
				obTemp.setId(rs1.getString("ID_TAREFA_TIPO"));
				obTemp.setTarefaTipo(rs1.getString("TAREFA_TIPO"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_TAREFA_TIPO WHERE TAREFA_TIPO LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..TarefaTipoPsGen.consultarDescricao() Operação realizada com sucesso");
		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return liTemp; 
	}

} 
