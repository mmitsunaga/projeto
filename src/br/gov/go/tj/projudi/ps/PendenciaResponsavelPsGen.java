package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.PendenciaResponsavelDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class PendenciaResponsavelPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = 7580884591280381692L;

	//---------------------------------------------------------
	public PendenciaResponsavelPsGen() {

	}



//---------------------------------------------------------
	public void inserir(PendenciaResponsavelDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psPendenciaResponsavelinserir()");
		stSqlCampos= "INSERT INTO PROJUDI.PEND_RESP ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getId_Pendencia().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_PEND " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Pendencia());  

			stVirgula=",";
		}
		if ((dados.getId_Serventia().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_SERV " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Serventia());  

			stVirgula=",";
		}
		if ((dados.getId_ServentiaTipo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_SERV_TIPO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_ServentiaTipo());  

			stVirgula=",";
		}
		if ((dados.getId_UsuarioResponsavel().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_USU_RESP " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_UsuarioResponsavel());  

			stVirgula=",";
		}
		if ((dados.getId_ServentiaCargo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_SERV_CARGO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_ServentiaCargo());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_PEND_RESP",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(PendenciaResponsavelDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psPendenciaResponsavelalterar()");

		stSql= "UPDATE PROJUDI.PEND_RESP SET  ";
		stSql+= "ID_PEND = ?";		 ps.adicionarLong(dados.getId_Pendencia());  

		stSql+= ",ID_SERV = ?";		 ps.adicionarLong(dados.getId_Serventia());  

		stSql+= ",ID_SERV_TIPO = ?";		 ps.adicionarLong(dados.getId_ServentiaTipo());  

		stSql+= ",ID_USU_RESP = ?";		 ps.adicionarLong(dados.getId_UsuarioResponsavel());  

		stSql+= ",ID_SERV_CARGO = ?";		 ps.adicionarLong(dados.getId_ServentiaCargo());  

		stSql += " WHERE ID_PEND_RESP  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psPendenciaResponsavelexcluir()");

		stSql= "DELETE FROM PROJUDI.PEND_RESP";
		stSql += " WHERE ID_PEND_RESP = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public PendenciaResponsavelDt consultarId(String id_pendenciaresponsavel )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		PendenciaResponsavelDt Dados=null;
		////System.out.println("....ps-ConsultaId_PendenciaResponsavel)");

		stSql= "SELECT * FROM PROJUDI.VIEW_PEND_RESP WHERE ID_PEND_RESP = ?";		ps.adicionarLong(id_pendenciaresponsavel); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_PendenciaResponsavel  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new PendenciaResponsavelDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( PendenciaResponsavelDt Dados, ResultSetTJGO rs )  throws Exception {
		Dados.setId(rs.getString("ID_PEND_RESP"));
		Dados.setPendencia(rs.getString("PEND"));
		Dados.setId_Pendencia( rs.getString("ID_PEND"));
		Dados.setId_Serventia( rs.getString("ID_SERV"));
		Dados.setServentia( rs.getString("SERV"));
		Dados.setId_ServentiaTipo( rs.getString("ID_SERV_TIPO"));
		Dados.setServentiaTipo( rs.getString("SERV_TIPO"));
		Dados.setId_UsuarioResponsavel( rs.getString("ID_USU_RESP"));
		Dados.setUsuarioResponsavel( rs.getString("USU_RESP"));
		Dados.setId_ServentiaCargo( rs.getString("ID_SERV_CARGO"));
		Dados.setServentiaCargo( rs.getString("SERV_CARGO"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoPendenciaResponsavel()");

		stSql= "SELECT ID_PEND_RESP, PEND FROM PROJUDI.VIEW_PEND_RESP WHERE PEND LIKE ?";
		stSql+= " ORDER BY PEND ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoPendenciaResponsavel  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				PendenciaResponsavelDt obTemp = new PendenciaResponsavelDt();
				obTemp.setId(rs1.getString("ID_PEND_RESP"));
				obTemp.setPendencia(rs1.getString("PEND"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_PEND_RESP WHERE PEND LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..PendenciaResponsavelPsGen.consultarDescricao() Operação realizada com sucesso");
		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return liTemp; 
	}

} 
