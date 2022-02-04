package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class LogTipoPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = 3546845184794307455L;

	//---------------------------------------------------------
	public LogTipoPsGen() {

	}



//---------------------------------------------------------
	public void inserir(LogTipoDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psLogTipoinserir()");
		stSqlCampos= "INSERT INTO PROJUDI.LOG_TIPO ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getLogTipo().length()>0)) {
			 stSqlCampos+=   stVirgula + "LOG_TIPO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getLogTipo());  

			stVirgula=",";
		}
		if ((dados.getLogTipoCodigo().length()>0)) {
			 stSqlCampos+=   stVirgula + "LOG_TIPO_CODIGO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getLogTipoCodigo());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_LOG_TIPO",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(LogTipoDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psLogTipoalterar()");

		stSql= "UPDATE PROJUDI.LOG_TIPO SET  ";
		stSql+= "LOG_TIPO = ?";		 ps.adicionarString(dados.getLogTipo());  

		stSql+= ",LOG_TIPO_CODIGO = ?";		 ps.adicionarLong(dados.getLogTipoCodigo());  

		stSql += " WHERE ID_LOG_TIPO  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psLogTipoexcluir()");

		stSql= "DELETE FROM PROJUDI.LOG_TIPO";
		stSql += " WHERE ID_LOG_TIPO = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public LogTipoDt consultarId(String id_logtipo )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		LogTipoDt Dados=null;
		////System.out.println("....ps-ConsultaId_LogTipo)");

		stSql= "SELECT * FROM PROJUDI.VIEW_LOG_TIPO WHERE ID_LOG_TIPO = ?";		ps.adicionarLong(id_logtipo); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_LogTipo  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new LogTipoDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( LogTipoDt Dados, ResultSetTJGO rs )  throws Exception {
		Dados.setId(rs.getString("ID_LOG_TIPO"));
		Dados.setLogTipo(rs.getString("LOG_TIPO"));
		Dados.setLogTipoCodigo( rs.getString("LOG_TIPO_CODIGO"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoLogTipo()");

		stSql= "SELECT ID_LOG_TIPO, LOG_TIPO FROM PROJUDI.VIEW_LOG_TIPO WHERE LOG_TIPO LIKE ?";
		stSql+= " ORDER BY LOG_TIPO ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoLogTipo  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				LogTipoDt obTemp = new LogTipoDt();
				obTemp.setId(rs1.getString("ID_LOG_TIPO"));
				obTemp.setLogTipo(rs1.getString("LOG_TIPO"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_LOG_TIPO WHERE LOG_TIPO LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..LogTipoPsGen.consultarDescricao() Operação realizada com sucesso");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return liTemp; 
	}

} 
