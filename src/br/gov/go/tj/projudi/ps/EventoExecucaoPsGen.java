package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.EventoExecucaoDt;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class EventoExecucaoPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = 8192367261072946247L;

	//---------------------------------------------------------
	public EventoExecucaoPsGen() {

	}



//---------------------------------------------------------
	public void inserir(EventoExecucaoDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psEventoExecucaoinserir()");
		stSqlCampos= "INSERT INTO PROJUDI.EVENTO_EXE ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getEventoExecucao().length()>0)) {
			 stSqlCampos+=   stVirgula + "EVENTO_EXE " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getEventoExecucao());  

			stVirgula=",";
		}
		if ((dados.getAlteraLocal().length()>0)) {
			 stSqlCampos+=   stVirgula + "ALTERA_LOCAL " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getAlteraLocal().toUpperCase());  

			stVirgula=",";
		}
		if ((dados.getAlteraRegime().length()>0)) {
			 stSqlCampos+=   stVirgula + "ALTERA_REGIME " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getAlteraRegime().toUpperCase());  

			stVirgula=",";
		}
		if ((dados.getValorNegativo().length()>0)) {
			 stSqlCampos+=   stVirgula + "VALOR_NEGATIVO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarBoolean(dados.getValorNegativo());  

			stVirgula=",";
		}
		if ((dados.getEventoExecucaoCodigo().length()>0)) {
			 stSqlCampos+=   stVirgula + "EVENTO_EXE_CODIGO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getEventoExecucaoCodigo());  

			stVirgula=",";
		}
		if ((dados.getId_EventoExecucaoTipo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_EVENTO_EXE_TIPO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_EventoExecucaoTipo());  

			stVirgula=",";
		}
		if ((dados.getId_EventoExecucaoStatus().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_EVENTO_EXE_STATUS " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_EventoExecucaoStatus());  

			stVirgula=",";
		}
		
		stSqlCampos+=   stVirgula + "OBSERVACAO " ;
		stSqlValores+=   stVirgula + "? " ;
		ps.adicionarString(dados.getObservacao());  
		stVirgula=",";
		
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 

		
			dados.setId(executarInsert(stSql,"ID_EVENTO_EXE",ps));

		 
	} 

//---------------------------------------------------------
	public void alterar(EventoExecucaoDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psEventoExecucaoalterar()");

		stSql= "UPDATE PROJUDI.EVENTO_EXE SET  ";
		stSql+= "EVENTO_EXE = ?";		 ps.adicionarString(dados.getEventoExecucao());  

		stSql+= ",ALTERA_LOCAL = ?";		 ps.adicionarString(dados.getAlteraLocal().toUpperCase());  

		stSql+= ",ALTERA_REGIME = ?";		 ps.adicionarString(dados.getAlteraRegime().toUpperCase());
		
		stSql+= ",OBSERVACAO = ?";		 ps.adicionarString(dados.getObservacao());  

		stSql+= ",VALOR_NEGATIVO = ?";		 ps.adicionarBoolean(dados.getValorNegativo());  

		stSql+= ",EVENTO_EXE_CODIGO = ?";		 ps.adicionarLong(dados.getEventoExecucaoCodigo());  

		stSql+= ",ID_EVENTO_EXE_TIPO = ?";		 ps.adicionarLong(dados.getId_EventoExecucaoTipo());  

		stSql+= ",ID_EVENTO_EXE_STATUS = ?";		 ps.adicionarLong(dados.getId_EventoExecucaoStatus());  

		stSql += " WHERE ID_EVENTO_EXE  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSql= "DELETE FROM PROJUDI.EVENTO_EXE";
		stSql += " WHERE ID_EVENTO_EXE = ?";		ps.adicionarLong(chave); 
			executarUpdateDelete(stSql,ps);

		

	} 

//---------------------------------------------------------
	public EventoExecucaoDt consultarId(String id_eventoexecucao )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		EventoExecucaoDt Dados=null;

		stSql= "SELECT * FROM PROJUDI.VIEW_EVENTO_EXE WHERE ID_EVENTO_EXE = ?";		ps.adicionarLong(id_eventoexecucao); 

		try{
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new EventoExecucaoDt();
				associarDt(Dados, rs1);
			}
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( EventoExecucaoDt Dados, ResultSetTJGO rs )  throws Exception {

		Dados.setId(rs.getString("ID_EVENTO_EXE"));
		Dados.setEventoExecucao(rs.getString("EVENTO_EXE"));
		Dados.setAlteraLocal( rs.getString("ALTERA_LOCAL"));
		Dados.setAlteraRegime( rs.getString("ALTERA_REGIME"));
		Dados.setObservacao( rs.getString("OBSERVACAO"));
		Dados.setValorNegativo( Funcoes.FormatarLogico(rs.getString("VALOR_NEGATIVO")));
		Dados.setEventoExecucaoCodigo( rs.getString("EVENTO_EXE_CODIGO"));
		Dados.setId_EventoExecucaoTipo( rs.getString("ID_EVENTO_EXE_TIPO"));
		Dados.setEventoExecucaoTipo( rs.getString("EVENTO_EXE_TIPO"));
		Dados.setId_EventoExecucaoStatus( rs.getString("ID_EVENTO_EXE_STATUS"));
		Dados.setEventoExecucaoStatus( rs.getString("EVENTO_EXE_STATUS"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSql= "SELECT ID_EVENTO_EXE, EVENTO_EXE, OBSERVACAO FROM PROJUDI.VIEW_EVENTO_EXE WHERE EVENTO_EXE LIKE ?";
		stSql+= " ORDER BY EVENTO_EXE ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			rs1 = consultarPaginacao(stSql, ps, posicao);

			while (rs1.next()) {
				EventoExecucaoDt obTemp = new EventoExecucaoDt();
				obTemp.setId(rs1.getString("ID_EVENTO_EXE"));
				obTemp.setEventoExecucao(rs1.getString("EVENTO_EXE"));
				obTemp.setObservacao(rs1.getString("OBSERVACAO"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_EVENTO_EXE WHERE EVENTO_EXE LIKE ?";
			rs2 = consultar(stSql,ps);
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return liTemp; 
	}

} 
