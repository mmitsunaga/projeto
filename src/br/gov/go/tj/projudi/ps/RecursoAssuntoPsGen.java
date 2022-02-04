package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.RecursoAssuntoDt;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class RecursoAssuntoPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = -6551837144592650899L;

	//---------------------------------------------------------
	public RecursoAssuntoPsGen() {

	}



//---------------------------------------------------------
	public void inserir(RecursoAssuntoDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psRecursoAssuntoinserir()");
		stSqlCampos= "INSERT INTO PROJUDI.RECURSO_ASSUNTO ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getId_Recurso().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_RECURSO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Recurso());  

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

		
			dados.setId(executarInsert(stSql,"ID_RECURSO_ASSUNTO",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(RecursoAssuntoDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psRecursoAssuntoalterar()");

		stSql= "UPDATE PROJUDI.RECURSO_ASSUNTO SET  ";
		stSql+= "ID_RECURSO = ?";		 ps.adicionarLong(dados.getId_Recurso());  

		stSql+= ",ID_ASSUNTO = ?";		 ps.adicionarLong(dados.getId_Assunto());
		
		stSql += " WHERE ID_RECURSO_ASSUNTO  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps); 
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psRecursoAssuntoexcluir()");

		stSql= "DELETE FROM PROJUDI.RECURSO_ASSUNTO";
		stSql += " WHERE ID_RECURSO_ASSUNTO = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public RecursoAssuntoDt consultarId(String id_recursoassunto )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		RecursoAssuntoDt Dados=null;
		////System.out.println("....ps-ConsultaId_RecursoAssunto)");

		stSql= "SELECT * FROM PROJUDI.VIEW_RECURSO_ASSUNTO WHERE ID_RECURSO_ASSUNTO = ?";		ps.adicionarLong(id_recursoassunto); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_RecursoAssunto  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new RecursoAssuntoDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( RecursoAssuntoDt Dados, ResultSetTJGO rs )  throws Exception {
		Dados.setId(rs.getString("ID_RECURSO_ASSUNTO"));
		Dados.setAssunto(rs.getString("ASSUNTO"));
		Dados.setId_Recurso( rs.getString("ID_RECURSO"));
		Dados.setDataRecebimento( Funcoes.FormatarDataHora(rs.getDateTime("DATA_RECEBIMENTO")));
		Dados.setId_Assunto( rs.getString("ID_ASSUNTO"));
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoRecursoAssunto()");

		stSql= "SELECT ID_RECURSO_ASSUNTO, ASSUNTO FROM PROJUDI.VIEW_RECURSO_ASSUNTO WHERE ASSUNTO LIKE ?";
		stSql+= " ORDER BY ASSUNTO ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoRecursoAssunto  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				RecursoAssuntoDt obTemp = new RecursoAssuntoDt();
				obTemp.setId(rs1.getString("ID_RECURSO_ASSUNTO"));
				obTemp.setAssunto(rs1.getString("ASSUNTO"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_RECURSO_ASSUNTO WHERE ASSUNTO LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..RecursoAssuntoPsGen.consultarDescricao() Operação realizada com sucesso");
		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return liTemp; 
	}

} 
