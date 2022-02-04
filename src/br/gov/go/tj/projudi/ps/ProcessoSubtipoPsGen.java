package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.ProcessoSubtipoDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class ProcessoSubtipoPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = 6559854070281528229L;

	//---------------------------------------------------------
	public ProcessoSubtipoPsGen() {

	}



//---------------------------------------------------------
	public void inserir(ProcessoSubtipoDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psProcessoSubtipoinserir()");
		stSqlCampos= "INSERT INTO PROJUDI.PROC_SUBTIPO ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getProcessoSubtipo().length()>0)) {
			 stSqlCampos+=   stVirgula + "PROC_SUBTIPO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getProcessoSubtipo());  

			stVirgula=",";
		}
		if ((dados.getProcessoSubtipoCodigo().length()>0)) {
			 stSqlCampos+=   stVirgula + "PROC_SUBTIPO_CODIGO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getProcessoSubtipoCodigo());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_PROC_SUBTIPO",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(ProcessoSubtipoDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psProcessoSubtipoalterar()");

		stSql= "UPDATE PROJUDI.PROC_SUBTIPO SET  ";
		stSql+= "PROC_SUBTIPO = ?";		 ps.adicionarString(dados.getProcessoSubtipo());  

		stSql+= ",PROC_SUBTIPO_CODIGO = ?";		 ps.adicionarLong(dados.getProcessoSubtipoCodigo());  

		stSql += " WHERE ID_PROC_SUBTIPO  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psProcessoSubtipoexcluir()");

		stSql= "DELETE FROM PROJUDI.PROC_SUBTIPO";
		stSql += " WHERE ID_PROC_SUBTIPO = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public ProcessoSubtipoDt consultarId(String id_processosubtipo )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ProcessoSubtipoDt Dados=null;
		////System.out.println("....ps-ConsultaId_ProcessoSubtipo)");

		stSql= "SELECT * FROM PROJUDI.VIEW_PROC_SUBTIPO WHERE ID_PROC_SUBTIPO = ?";		ps.adicionarLong(id_processosubtipo); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_ProcessoSubtipo  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new ProcessoSubtipoDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( ProcessoSubtipoDt Dados, ResultSetTJGO rs )  throws Exception {
	
		Dados.setId(rs.getString("ID_PROC_SUBTIPO"));
		Dados.setProcessoSubtipo(rs.getString("PROC_SUBTIPO"));
		Dados.setProcessoSubtipoCodigo( rs.getString("PROC_SUBTIPO_CODIGO"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
		
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoProcessoSubtipo()");

		stSql= "SELECT ID_PROC_SUBTIPO, PROC_SUBTIPO FROM PROJUDI.VIEW_PROC_SUBTIPO WHERE PROC_SUBTIPO LIKE ?";
		stSql+= " ORDER BY PROC_SUBTIPO ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoProcessoSubtipo  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				ProcessoSubtipoDt obTemp = new ProcessoSubtipoDt();
				obTemp.setId(rs1.getString("ID_PROC_SUBTIPO"));
				obTemp.setProcessoSubtipo(rs1.getString("PROC_SUBTIPO"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_PROC_SUBTIPO WHERE PROC_SUBTIPO LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..ProcessoSubtipoPsGen.consultarDescricao() Operação realizada com sucesso");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return liTemp; 
	}

} 
