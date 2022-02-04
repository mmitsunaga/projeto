package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.EventoExecucaoStatusDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class EventoExecucaoStatusPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = 8667424828181060497L;

	//---------------------------------------------------------
	public EventoExecucaoStatusPsGen() {

	}



//---------------------------------------------------------
	public void inserir(EventoExecucaoStatusDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psEventoExecucaoStatusinserir()");
		stSqlCampos= "INSERT INTO PROJUDI.EVENTO_EXE_STATUS ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getEventoExecucaoStatus().length()>0)) {
			 stSqlCampos+=   stVirgula + "EVENTO_EXE_STATUS " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getEventoExecucaoStatus());  

			stVirgula=",";
		}
		if ((dados.getEventoExecucaoStatusCodigo().length()>0)) {
			 stSqlCampos+=   stVirgula + "EVENTO_EXE_STATUS_CODIGO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getEventoExecucaoStatusCodigo());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_EVENTO_EXE_STATUS",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(EventoExecucaoStatusDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psEventoExecucaoStatusalterar()");

		stSql= "UPDATE PROJUDI.EVENTO_EXE_STATUS SET  ";
		stSql+= "EVENTO_EXE_STATUS = ?";		 ps.adicionarString(dados.getEventoExecucaoStatus());  

		stSql+= ",EVENTO_EXE_STATUS_CODIGO = ?";		 ps.adicionarLong(dados.getEventoExecucaoStatusCodigo());  

		stSql += " WHERE ID_EVENTO_EXE_STATUS  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psEventoExecucaoStatusexcluir()");

		stSql= "DELETE FROM PROJUDI.EVENTO_EXE_STATUS";
		stSql += " WHERE ID_EVENTO_EXE_STATUS = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public EventoExecucaoStatusDt consultarId(String id_eventoexecucaostatus )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		EventoExecucaoStatusDt Dados=null;
		////System.out.println("....ps-ConsultaId_EventoExecucaoStatus)");

		stSql= "SELECT * FROM PROJUDI.VIEW_EVENTO_EXE_STATUS WHERE ID_EVENTO_EXE_STATUS = ?";		ps.adicionarLong(id_eventoexecucaostatus); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_EventoExecucaoStatus  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new EventoExecucaoStatusDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( EventoExecucaoStatusDt Dados, ResultSetTJGO rs )  throws Exception {
		
		Dados.setId(rs.getString("ID_EVENTO_EXE_STATUS"));
		Dados.setEventoExecucaoStatus(rs.getString("EVENTO_EXE_STATUS"));
		Dados.setEventoExecucaoStatusCodigo( rs.getString("EVENTO_EXE_STATUS_CODIGO"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
		
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao, boolean isMostrarNaoAplica ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoEventoExecucaoStatus()");

		stSql= "SELECT ID_EVENTO_EXE_STATUS, EVENTO_EXE_STATUS FROM PROJUDI.VIEW_EVENTO_EXE_STATUS WHERE EVENTO_EXE_STATUS LIKE ?";
		stSql+= " ORDER BY EVENTO_EXE_STATUS ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoEventoExecucaoStatus  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				EventoExecucaoStatusDt obTemp = new EventoExecucaoStatusDt();
				obTemp.setId(rs1.getString("ID_EVENTO_EXE_STATUS"));
				obTemp.setEventoExecucaoStatus(rs1.getString("EVENTO_EXE_STATUS"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_EVENTO_EXE_STATUS WHERE EVENTO_EXE_STATUS LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..EventoExecucaoStatusPsGen.consultarDescricao() Operação realizada com sucesso");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return liTemp; 
	}

} 
