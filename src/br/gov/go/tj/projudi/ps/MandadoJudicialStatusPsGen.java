package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.MandadoJudicialStatusDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class MandadoJudicialStatusPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = 178265073527160056L;

	//---------------------------------------------------------
	public MandadoJudicialStatusPsGen() {

	}



//---------------------------------------------------------
	public void inserir(MandadoJudicialStatusDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psMandadoJudicialStatusinserir()");
		stSqlCampos= "INSERT INTO PROJUDI.MAND_JUD_STATUS ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getMandadoJudicialStatus().length()>0)) {
			 stSqlCampos+=   stVirgula + "MAND_JUD_STATUS " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getMandadoJudicialStatus());  

			stVirgula=",";
		}
		if ((dados.getMandadoJudicialStatusCodigo().length()>0)) {
			 stSqlCampos+=   stVirgula + "MAND_JUD_STATUS_CODIGO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getMandadoJudicialStatusCodigo());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_MAND_JUD_STATUS",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(MandadoJudicialStatusDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psMandadoJudicialStatusalterar()");

		stSql= "UPDATE PROJUDI.MAND_JUD_STATUS SET  ";
		stSql+= "MAND_JUD_STATUS = ?";		 ps.adicionarString(dados.getMandadoJudicialStatus());  

		stSql+= ",MAND_JUD_STATUS_CODIGO = ?";		 ps.adicionarLong(dados.getMandadoJudicialStatusCodigo());  

		stSql += " WHERE ID_MAND_JUD_STATUS  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps); 
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psMandadoJudicialStatusexcluir()");

		stSql= "DELETE FROM PROJUDI.MAND_JUD_STATUS";
		stSql += " WHERE ID_MAND_JUD_STATUS = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public MandadoJudicialStatusDt consultarId(String id_mandadojudicialstatus )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		MandadoJudicialStatusDt Dados=null;
		////System.out.println("....ps-ConsultaId_MandadoJudicialStatus)");

		stSql= "SELECT * FROM PROJUDI.VIEW_MAND_JUD_STATUS WHERE ID_MAND_JUD_STATUS = ?";		ps.adicionarLong(id_mandadojudicialstatus); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_MandadoJudicialStatus  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new MandadoJudicialStatusDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( MandadoJudicialStatusDt Dados, ResultSetTJGO rs )  throws Exception {


		Dados.setId(rs.getString("ID_MAND_JUD_STATUS"));
		Dados.setMandadoJudicialStatus(rs.getString("MAND_JUD_STATUS"));
		Dados.setMandadoJudicialStatusCodigo( rs.getString("MAND_JUD_STATUS_CODIGO"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));

	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoMandadoJudicialStatus()");

		stSql= "SELECT ID_MAND_JUD_STATUS, MAND_JUD_STATUS FROM PROJUDI.VIEW_MAND_JUD_STATUS WHERE MAND_JUD_STATUS LIKE ?";
		stSql+= " ORDER BY MAND_JUD_STATUS ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoMandadoJudicialStatus  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				MandadoJudicialStatusDt obTemp = new MandadoJudicialStatusDt();
				obTemp.setId(rs1.getString("ID_MAND_JUD_STATUS"));
				obTemp.setMandadoJudicialStatus(rs1.getString("MAND_JUD_STATUS"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_MAND_JUD_STATUS WHERE MAND_JUD_STATUS LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..MandadoJudicialStatusPsGen.consultarDescricao() Operação realizada com sucesso");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return liTemp; 
	}

} 
