package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.EventoRegimeDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class EventoRegimePsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = -6500237345768989066L;

	//---------------------------------------------------------
	public EventoRegimePsGen() {

	}



//---------------------------------------------------------
	public void inserir(EventoRegimeDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psEventoRegimeinserir()");
		stSqlCampos= "INSERT INTO PROJUDI.EVENTO_REGIME ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getId_ProcessoEventoExecucao().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_PROC_EVENTO_EXE " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_ProcessoEventoExecucao());  

			stVirgula=",";
		}
		if ((dados.getId_RegimeExecucao().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_REGIME_EXE " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getId_RegimeExecucao());  

			stVirgula=",";
		}
		
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_EVENTO_REGIME",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(EventoRegimeDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psEventoRegimealterar()");

		stSql= "UPDATE PROJUDI.EVENTO_REGIME SET  ";
		stSql+= "ID_PROC_EVENTO_EXE = ?";		 ps.adicionarLong(dados.getId_ProcessoEventoExecucao());  

		stSql+= ",ID_REGIME_EXE = ?";		 ps.adicionarString(dados.getId_RegimeExecucao());  

		stSql += " WHERE ID_EVENTO_REGIME  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps); 
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psEventoRegimeexcluir()");

		stSql= "DELETE FROM PROJUDI.EVENTO_REGIME";
		stSql += " WHERE ID_EVENTO_REGIME = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public EventoRegimeDt consultarId(String id_eventoregime )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		EventoRegimeDt Dados=null;
		////System.out.println("....ps-ConsultaId_EventoRegime)");

		stSql= "SELECT * FROM PROJUDI.VIEW_EVENTO_REGIME WHERE ID_EVENTO_REGIME = ?";		ps.adicionarLong(id_eventoregime); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_EventoRegime  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new EventoRegimeDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( EventoRegimeDt Dados, ResultSetTJGO rs )  throws Exception {
		
		Dados.setId(rs.getString("ID_EVENTO_REGIME"));
		Dados.setRegimeExecucao(rs.getString("REGIME_EXE"));
		Dados.setId_ProcessoEventoExecucao( rs.getString("ID_PROC_EVENTO_EXE"));
		Dados.setEventoExecucao( rs.getString("EVENTO_EXE"));
		Dados.setId_RegimeExecucao( rs.getString("ID_REGIME_EXE"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
		Dados.setId_ProximoRegimeExecucao(rs.getString("ID_PROXIMO_REGIME_EXE"));
		
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoEventoRegime()");

		stSql= "SELECT ID_EVENTO_REGIME, REGIME_EXE FROM PROJUDI.VIEW_EVENTO_REGIME WHERE REGIME_EXE LIKE ?";
		stSql+= " ORDER BY REGIME_EXE ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoEventoRegime  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				EventoRegimeDt obTemp = new EventoRegimeDt();
				obTemp.setId(rs1.getString("ID_EVENTO_REGIME"));
				obTemp.setRegimeExecucao(rs1.getString("REGIME_EXE"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_EVENTO_REGIME WHERE REGIME_EXE LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..EventoRegimePsGen.consultarDescricao() Operação realizada com sucesso");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return liTemp; 
	}

} 
