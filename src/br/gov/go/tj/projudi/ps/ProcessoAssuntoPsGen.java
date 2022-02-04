package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.ProcessoAssuntoDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class ProcessoAssuntoPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = -7384633887760119018L;

	//---------------------------------------------------------
	public ProcessoAssuntoPsGen() {

	}



//---------------------------------------------------------
	public void inserir(ProcessoAssuntoDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psProcessoAssuntoinserir()");
		stSqlCampos= "INSERT INTO PROJUDI.PROC_ASSUNTO ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getId_Processo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_PROC " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Processo());  

			stVirgula=",";
		}
		if ((dados.getId_Assunto().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_ASSUNTO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Assunto());  

			stVirgula=",";
		}		
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_PROC_ASSUNTO",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(ProcessoAssuntoDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psProcessoAssuntoalterar()");

		stSql= "UPDATE PROJUDI.PROC_ASSUNTO SET  ";
		stSql+= "ID_PROC = ?";		 ps.adicionarLong(dados.getId_Processo());  

		stSql+= ",ID_ASSUNTO = ?";		 ps.adicionarLong(dados.getId_Assunto());	

		stSql += " WHERE ID_PROC_ASSUNTO  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps); 
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psProcessoAssuntoexcluir()");

		stSql= "DELETE FROM PROJUDI.PROC_ASSUNTO";
		stSql += " WHERE ID_PROC_ASSUNTO = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public ProcessoAssuntoDt consultarId(String id_processoassunto )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ProcessoAssuntoDt Dados=null;
		////System.out.println("....ps-ConsultaId_ProcessoAssunto)");

		stSql= "SELECT * FROM PROJUDI.VIEW_PROC_ASSUNTO WHERE ID_PROC_ASSUNTO = ?";		ps.adicionarLong(id_processoassunto); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_ProcessoAssunto  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new ProcessoAssuntoDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( ProcessoAssuntoDt Dados, ResultSetTJGO rs )  throws Exception {
		Dados.setId(rs.getString("ID_PROC_ASSUNTO"));
		Dados.setProcessoNumero(rs.getString("PROC_NUMERO"));
		Dados.setId_Processo( rs.getString("ID_PROC"));
		Dados.setId_Assunto( rs.getString("ID_ASSUNTO"));
		Dados.setAssunto( rs.getString("ASSUNTO"));
		if (rs.contains("ID_CNJ_ASSUNTO")) {
			Dados.setIdCNJAssunto(rs.getString("ID_CNJ_ASSUNTO"));
		}		
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoProcessoAssunto()");

		stSql= "SELECT ID_PROC_ASSUNTO, PROC_NUMERO FROM PROJUDI.VIEW_PROC_ASSUNTO WHERE PROC_NUMERO LIKE ?";
		stSql+= " ORDER BY PROC_NUMERO ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoProcessoAssunto  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				ProcessoAssuntoDt obTemp = new ProcessoAssuntoDt();
				obTemp.setId(rs1.getString("ID_PROC_ASSUNTO"));
				obTemp.setProcessoNumero(rs1.getString("PROC_NUMERO"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_PROC_ASSUNTO WHERE PROC_NUMERO LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..ProcessoAssuntoPsGen.consultarDescricao() Operação realizada com sucesso");
		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return liTemp; 
	}

} 
