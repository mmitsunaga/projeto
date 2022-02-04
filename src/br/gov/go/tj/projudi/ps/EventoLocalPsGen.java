package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.EventoLocalDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class EventoLocalPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = -1418598558649908536L;

	//---------------------------------------------------------
	public EventoLocalPsGen() {

	}



//---------------------------------------------------------
	public void inserir(EventoLocalDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psEventoLocalinserir()");
		stSqlCampos= "INSERT INTO PROJUDI.EVENTO_LOCAL ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getId_ProcessoEventoExecucao().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_PROC_EVENTO_EXE " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_ProcessoEventoExecucao());  

			stVirgula=",";
		}
		if ((dados.getId_LocalCumprimentoPena().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_LOCAL_CUMP_PENA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_LocalCumprimentoPena());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_EVENTO_LOCAL",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(EventoLocalDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psEventoLocalalterar()");

		stSql= "UPDATE PROJUDI.EVENTO_LOCAL SET  ";
		stSql+= "ID_PROC_EVENTO_EXE = ?";		 ps.adicionarLong(dados.getId_ProcessoEventoExecucao());  

		stSql+= ",ID_LOCAL_CUMP_PENA = ?";		 ps.adicionarLong(dados.getId_LocalCumprimentoPena());  

		stSql += " WHERE ID_EVENTO_LOCAL  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps); 
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psEventoLocalexcluir()");

		stSql= "DELETE FROM PROJUDI.EVENTO_LOCAL";
		stSql += " WHERE ID_EVENTO_LOCAL = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public EventoLocalDt consultarId(String id_eventolocal )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		EventoLocalDt Dados=null;
		////System.out.println("....ps-ConsultaId_EventoLocal)");

		stSql= "SELECT * FROM PROJUDI.VIEW_EVENTO_LOCAL WHERE ID_EVENTO_LOCAL = ?";		ps.adicionarLong(id_eventolocal); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_EventoLocal  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new EventoLocalDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( EventoLocalDt Dados, ResultSetTJGO rs )  throws Exception {
		Dados.setId(rs.getString("ID_EVENTO_LOCAL"));
		Dados.setLocalCumprimentoPena(rs.getString("LOCAL_CUMP_PENA"));
		Dados.setId_ProcessoEventoExecucao( rs.getString("ID_PROC_EVENTO_EXE"));
		Dados.setEventoExecucao( rs.getString("EVENTO_EXE"));
		Dados.setId_LocalCumprimentoPena( rs.getString("ID_LOCAL_CUMP_PENA"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoEventoLocal()");

		stSql= "SELECT ID_EVENTO_LOCAL, LOCAL_CUMP_PENA FROM PROJUDI.VIEW_EVENTO_LOCAL WHERE LOCAL_CUMP_PENA LIKE ?";
		stSql+= " ORDER BY LOCAL_CUMP_PENA ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoEventoLocal  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				EventoLocalDt obTemp = new EventoLocalDt();
				obTemp.setId(rs1.getString("ID_EVENTO_LOCAL"));
				obTemp.setLocalCumprimentoPena(rs1.getString("LOCAL_CUMP_PENA"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_EVENTO_LOCAL WHERE LOCAL_CUMP_PENA LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..EventoLocalPsGen.consultarDescricao() Operação realizada com sucesso");
		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return liTemp; 
	}

} 
