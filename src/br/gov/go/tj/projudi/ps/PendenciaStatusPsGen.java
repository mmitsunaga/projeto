package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.PendenciaStatusDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class PendenciaStatusPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = -2569582531167760182L;

	//---------------------------------------------------------
	public PendenciaStatusPsGen() {

	}



//---------------------------------------------------------
	public void inserir(PendenciaStatusDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psPendenciaStatusinserir()");
		stSqlCampos= "INSERT INTO PROJUDI.PEND_STATUS ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getPendenciaStatus().length()>0)) {
			 stSqlCampos+=   stVirgula + "PEND_STATUS " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getPendenciaStatus());  

			stVirgula=",";
		}
		if ((dados.getPendenciaStatusCodigo().length()>0)) {
			 stSqlCampos+=   stVirgula + "PEND_STATUS_CODIGO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getPendenciaStatusCodigo());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_PEND_STATUS",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(PendenciaStatusDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psPendenciaStatusalterar()");

		stSql= "UPDATE PROJUDI.PEND_STATUS SET  ";
		stSql+= "PEND_STATUS = ?";		 ps.adicionarString(dados.getPendenciaStatus());  

		stSql+= ",PEND_STATUS_CODIGO = ?";		 ps.adicionarLong(dados.getPendenciaStatusCodigo());  

		stSql += " WHERE ID_PEND_STATUS  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psPendenciaStatusexcluir()");

		stSql= "DELETE FROM PROJUDI.PEND_STATUS";
		stSql += " WHERE ID_PEND_STATUS = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public PendenciaStatusDt consultarId(String id_pendenciastatus )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		PendenciaStatusDt Dados=null;
		////System.out.println("....ps-ConsultaId_PendenciaStatus)");

		stSql= "SELECT * FROM PROJUDI.VIEW_PEND_STATUS WHERE ID_PEND_STATUS = ?";		ps.adicionarLong(id_pendenciastatus); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_PendenciaStatus  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new PendenciaStatusDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( PendenciaStatusDt Dados, ResultSetTJGO rs )  throws Exception {
		Dados.setId(rs.getString("ID_PEND_STATUS"));
		Dados.setPendenciaStatus(rs.getString("PEND_STATUS"));
		Dados.setPendenciaStatusCodigo( rs.getString("PEND_STATUS_CODIGO"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoPendenciaStatus()");

		stSql= "SELECT ID_PEND_STATUS, PEND_STATUS FROM PROJUDI.VIEW_PEND_STATUS WHERE PEND_STATUS LIKE ?";
		stSql+= " ORDER BY PEND_STATUS ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoPendenciaStatus  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				PendenciaStatusDt obTemp = new PendenciaStatusDt();
				obTemp.setId(rs1.getString("ID_PEND_STATUS"));
				obTemp.setPendenciaStatus(rs1.getString("PEND_STATUS"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_PEND_STATUS WHERE PEND_STATUS LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..PendenciaStatusPsGen.consultarDescricao() Operação realizada com sucesso");
		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return liTemp; 
	}

} 
