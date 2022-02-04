package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.ProcessoBeneficioDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class ProcessoBeneficioPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = -3302224993161347294L;

	//---------------------------------------------------------
	public ProcessoBeneficioPsGen() {

	}



//---------------------------------------------------------
	public void inserir(ProcessoBeneficioDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psProcessoBeneficioinserir()");
		stSqlCampos= "INSERT INTO PROJUDI.PROC_BENEFICIO ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getProcessoBeneficio().length()>0)) {
			 stSqlCampos+=   stVirgula + "PROC_BENEFICIO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getProcessoBeneficio());  

			stVirgula=",";
		}
		if ((dados.getPrazo().length()>0)) {
			 stSqlCampos+=   stVirgula + "PRAZO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getPrazo());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_PROC_BENEFICIO",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(ProcessoBeneficioDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psProcessoBeneficioalterar()");

		stSql= "UPDATE PROJUDI.PROC_BENEFICIO SET  ";
		stSql+= "PROC_BENEFICIO = ?";		 ps.adicionarString(dados.getProcessoBeneficio());  

		stSql+= ",PRAZO = ?";		 ps.adicionarLong(dados.getPrazo());  

		stSql += " WHERE ID_PROC_BENEFICIO  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psProcessoBeneficioexcluir()");

		stSql= "DELETE FROM PROJUDI.PROC_BENEFICIO";
		stSql += " WHERE ID_PROC_BENEFICIO = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public ProcessoBeneficioDt consultarId(String id_processobeneficio )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ProcessoBeneficioDt Dados=null;
		////System.out.println("....ps-ConsultaId_ProcessoBeneficio)");

		stSql= "SELECT * FROM PROJUDI.VIEW_PROC_BENEFICIO WHERE ID_PROC_BENEFICIO = ?";		ps.adicionarLong(id_processobeneficio); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_ProcessoBeneficio  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new ProcessoBeneficioDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( ProcessoBeneficioDt Dados, ResultSetTJGO rs )  throws Exception {

		Dados.setId(rs.getString("ID_PROC_BENEFICIO"));
		Dados.setProcessoBeneficio(rs.getString("PROC_BENEFICIO"));
		Dados.setPrazo( rs.getString("PRAZO"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));

	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoProcessoBeneficio()");

		stSql= "SELECT ID_PROC_BENEFICIO, PROC_BENEFICIO FROM PROJUDI.VIEW_PROC_BENEFICIO WHERE PROC_BENEFICIO LIKE ?";
		stSql+= " ORDER BY PROC_BENEFICIO ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoProcessoBeneficio  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				ProcessoBeneficioDt obTemp = new ProcessoBeneficioDt();
				obTemp.setId(rs1.getString("ID_PROC_BENEFICIO"));
				obTemp.setProcessoBeneficio(rs1.getString("PROC_BENEFICIO"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_PROC_BENEFICIO WHERE PROC_BENEFICIO LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..ProcessoBeneficioPsGen.consultarDescricao() Operação realizada com sucesso");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return liTemp; 
	}

} 
