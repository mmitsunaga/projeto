package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class LogPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = 8692359385646454836L;

	//---------------------------------------------------------
	public LogPsGen() {

	}



//---------------------------------------------------------
	public void inserir(LogDt dados ) throws Exception{

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psLoginserir()");
		stSqlCampos= "INSERT INTO PROJUDI.LOG ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getTabela().length()>0)) {
			 stSqlCampos+=   stVirgula + "TABELA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getTabela());  

			stVirgula=",";
		}
		if ((dados.getId_LogTipo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_LOG_TIPO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_LogTipo());  

			stVirgula=",";
		}
		if ((dados.getData().length()>0)) {
			 stSqlCampos+=   stVirgula + "DATA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarDate(dados.getData());  

			stVirgula=",";
		}
		if ((dados.getHora().length()>0)) {
			 stSqlCampos+=   stVirgula + "HORA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarDate(dados.getHora());  

			stVirgula=",";
		}
		if ((dados.getId_Usuario().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_USU " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Usuario());  

			stVirgula=",";
		}
		if ((dados.getIpComputador().length()>0)) {
			 stSqlCampos+=   stVirgula + "IP_COMPUTADOR " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getIpComputador());  

			stVirgula=",";
		}
		if ((dados.getValorAtual().length()>0)) {
			 stSqlCampos+=   stVirgula + "VALOR_ATUAL " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getValorAtual());  

			stVirgula=",";
		}
		if ((dados.getValorNovo().length()>0)) {
			 stSqlCampos+=   stVirgula + "VALOR_NOVO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getValorNovo());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_LOG",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(LogDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psLogalterar()");

		stSql= "UPDATE PROJUDI.LOG SET  ";
		stSql+= "TABELA = ?";		 ps.adicionarString(dados.getTabela());  

		stSql+= ",ID_LOG_TIPO = ?";		 ps.adicionarLong(dados.getId_LogTipo());  

		stSql+= ",DATA = ?";		 ps.adicionarDate(dados.getData());  

		stSql+= ",HORA = ?";		 ps.adicionarString(dados.getHora());  

		stSql+= ",ID_USU = ?";		 ps.adicionarLong(dados.getId_Usuario());  

		stSql+= ",IP_COMPUTADOR = ?";		 ps.adicionarString(dados.getIpComputador());  

		stSql+= ",VALOR_ATUAL = ?";		 ps.adicionarString(dados.getValorAtual());  

		stSql+= ",VALOR_NOVO = ?";		 ps.adicionarString(dados.getValorNovo());  

		stSql += " WHERE ID_LOG  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psLogexcluir()");

		stSql= "DELETE FROM PROJUDI.LOG";
		stSql += " WHERE ID_LOG = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public LogDt consultarId(String id_log )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		LogDt Dados=null;
		////System.out.println("....ps-ConsultaId_Log)");

		stSql= "SELECT * FROM PROJUDI.VIEW_LOG WHERE ID_LOG = ?";		ps.adicionarLong(id_log); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_Log  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new LogDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( LogDt Dados, ResultSetTJGO rs )  throws Exception {

			Dados.setId(rs.getString("ID_LOG"));
			Dados.setTabela(rs.getString("TABELA"));
			Dados.setId_LogTipo( rs.getString("ID_LOG_TIPO"));
			Dados.setLogTipo( rs.getString("LOG_TIPO"));
			Dados.setData( Funcoes.FormatarData(rs.getDateTime("DATA")));
			Dados.setHora( rs.getString("HORA"));
			Dados.setId_Usuario( rs.getString("ID_USU"));
			Dados.setUsuario( rs.getString("USU"));
			Dados.setIpComputador( rs.getString("IP_COMPUTADOR"));
			Dados.setValorAtual( rs.getString("VALOR_ATUAL"));
			Dados.setValorNovo( rs.getString("VALOR_NOVO"));
			Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
			Dados.setId_Tabela( rs.getString("ID_TABELA"));
			Dados.setLogTipoCodigo( rs.getString("LOG_TIPO_CODIGO"));
			Dados.setHash(rs.getString("HASH"));
			Dados.setQtdErrosDia(rs.getLong("QTD_ERROS_DIA"));		
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoLog()");

		stSql= "SELECT ID_LOG, TABELA FROM PROJUDI.VIEW_LOG WHERE TABELA LIKE ?";
		stSql+= " ORDER BY TABELA ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoLog  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				LogDt obTemp = new LogDt();
				obTemp.setId(rs1.getString("ID_LOG"));
				obTemp.setTabela(rs1.getString("TABELA"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_LOG WHERE TABELA LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..LogPsGen.consultarDescricao() Operação realizada com sucesso");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return liTemp; 
	}

} 
