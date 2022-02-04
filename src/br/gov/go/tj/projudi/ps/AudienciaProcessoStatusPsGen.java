package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.AudienciaProcessoStatusDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class AudienciaProcessoStatusPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = -4125869460414883190L;

	//---------------------------------------------------------
	public AudienciaProcessoStatusPsGen() {

	}



//---------------------------------------------------------
	public void inserir(AudienciaProcessoStatusDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psAudienciaProcessoStatusinserir()");
		stSqlCampos= "INSERT INTO PROJUDI.AUDI_PROC_STATUS ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getAudienciaProcessoStatus().length()>0)) {
			 stSqlCampos+=   stVirgula + "AUDI_PROC_STATUS " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getAudienciaProcessoStatus());  

			stVirgula=",";
		}
		if ((dados.getAudienciaProcessoStatusCodigo().length()>0)) {
			 stSqlCampos+=   stVirgula + "AUDI_PROC_STATUS_CODIGO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getAudienciaProcessoStatusCodigo());  

			stVirgula=",";
		}
		if ((dados.getId_ServentiaTipo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_SERV_TIPO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_ServentiaTipo());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_AUDI_PROC_STATUS",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(AudienciaProcessoStatusDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psAudienciaProcessoStatusalterar()");

		stSql= "UPDATE PROJUDI.AUDI_PROC_STATUS SET  ";
		stSql+= "AUDI_PROC_STATUS = ?";		 ps.adicionarString(dados.getAudienciaProcessoStatus());  

		stSql+= ",AUDI_PROC_STATUS_CODIGO = ?";		 ps.adicionarLong(dados.getAudienciaProcessoStatusCodigo());  

		stSql+= ",ID_SERV_TIPO = ?";		 ps.adicionarLong(dados.getId_ServentiaTipo());  

		stSql += " WHERE ID_AUDI_PROC_STATUS  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps); 
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psAudienciaProcessoStatusexcluir()");

		stSql= "DELETE FROM PROJUDI.AUDI_PROC_STATUS";
		stSql += " WHERE ID_AUDI_PROC_STATUS = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public AudienciaProcessoStatusDt consultarId(String id_audienciaprocessostatus )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		AudienciaProcessoStatusDt Dados=null;
		////System.out.println("....ps-ConsultaId_AudienciaProcessoStatus)");

		stSql= "SELECT * FROM PROJUDI.VIEW_AUDI_PROC_STATUS WHERE ID_AUDI_PROC_STATUS = ?";		ps.adicionarLong(id_audienciaprocessostatus); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_AudienciaProcessoStatus  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new AudienciaProcessoStatusDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( AudienciaProcessoStatusDt Dados, ResultSetTJGO rs )  throws Exception {
		
		Dados.setId(rs.getString("ID_AUDI_PROC_STATUS"));
		Dados.setAudienciaProcessoStatus(rs.getString("AUDI_PROC_STATUS"));
		Dados.setAudienciaProcessoStatusCodigo( rs.getString("AUDI_PROC_STATUS_CODIGO"));
		Dados.setId_ServentiaTipo( rs.getString("ID_SERV_TIPO"));
		Dados.setServentiaTipo( rs.getString("SERV_TIPO"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
		
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoAudienciaProcessoStatus()");

		stSql= "SELECT ID_AUDI_PROC_STATUS, AUDI_PROC_STATUS FROM PROJUDI.VIEW_AUDI_PROC_STATUS WHERE AUDI_PROC_STATUS LIKE ?";
		stSql+= " ORDER BY AUDI_PROC_STATUS ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoAudienciaProcessoStatus  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				AudienciaProcessoStatusDt obTemp = new AudienciaProcessoStatusDt();
				obTemp.setId(rs1.getString("ID_AUDI_PROC_STATUS"));
				obTemp.setAudienciaProcessoStatus(rs1.getString("AUDI_PROC_STATUS"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_AUDI_PROC_STATUS WHERE AUDI_PROC_STATUS LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..AudienciaProcessoStatusPsGen.consultarDescricao() Operação realizada com sucesso");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return liTemp; 
	}

} 
