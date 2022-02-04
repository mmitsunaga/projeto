package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.PendenciaTipoRelacionadaDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class PendenciaTipoRelacionadaPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = 6572674476675209404L;

	//---------------------------------------------------------
	public PendenciaTipoRelacionadaPsGen() {

	}



//---------------------------------------------------------
	public void inserir(PendenciaTipoRelacionadaDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psPendenciaTipoRelacionadainserir()");
		stSqlCampos= "INSERT INTO PROJUDI.PEND_TIPO_RELACIONADA ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getId_PendenciaTipoPrincipal().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_PEND_TIPO_PRINC " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_PendenciaTipoPrincipal());  

			stVirgula=",";
		}
		if ((dados.getId_PendenciaTipoRelacao().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_PEND_TIPO_REL " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_PendenciaTipoRelacao());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_PEND_TIPO_RELACIONADA",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(PendenciaTipoRelacionadaDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psPendenciaTipoRelacionadaalterar()");

		stSql= "UPDATE PROJUDI.PEND_TIPO_RELACIONADA SET  ";
		stSql+= "ID_PEND_TIPO_PRINC = ?";		 ps.adicionarLong(dados.getId_PendenciaTipoPrincipal());  

		stSql+= ",ID_PEND_TIPO_REL = ?";		 ps.adicionarLong(dados.getId_PendenciaTipoRelacao());  

		stSql += " WHERE ID_PEND_TIPO_RELACIONADA  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psPendenciaTipoRelacionadaexcluir()");

		stSql= "DELETE FROM PROJUDI.PEND_TIPO_RELACIONADA";
		stSql += " WHERE ID_PEND_TIPO_RELACIONADA = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public PendenciaTipoRelacionadaDt consultarId(String id_pendenciatiporelacionada )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		PendenciaTipoRelacionadaDt Dados=null;
		////System.out.println("....ps-ConsultaId_PendenciaTipoRelacionada)");

		stSql= "SELECT * FROM PROJUDI.VIEW_PEND_TIPO_RELACIONADA WHERE ID_PEND_TIPO_RELACIONADA = ?";		ps.adicionarLong(id_pendenciatiporelacionada); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_PendenciaTipoRelacionada  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new PendenciaTipoRelacionadaDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( PendenciaTipoRelacionadaDt Dados, ResultSetTJGO rs )  throws Exception {

		Dados.setId(rs.getString("ID_PEND_TIPO_RELACIONADA"));
		Dados.setPendenciaTipoPrincipal(rs.getString("PEND_TIPO_PRINC"));
		Dados.setId_PendenciaTipoPrincipal( rs.getString("ID_PEND_TIPO_PRINC"));
		Dados.setId_PendenciaTipoRelacao( rs.getString("ID_PEND_TIPO_REL"));
		Dados.setPendenciaTipoRelacao( rs.getString("PEND_TIPO_RELACAO"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
		
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoPendenciaTipoRelacionada()");

		stSql= "SELECT ID_PEND_TIPO_RELACIONADA, PEND_TIPO_PRINC FROM PROJUDI.VIEW_PEND_TIPO_RELACIONADA WHERE PEND_TIPO_PRINC LIKE ?";
		stSql+= " ORDER BY PEND_TIPO_PRINC ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoPendenciaTipoRelacionada  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				PendenciaTipoRelacionadaDt obTemp = new PendenciaTipoRelacionadaDt();
				obTemp.setId(rs1.getString("ID_PEND_TIPO_RELACIONADA"));
				obTemp.setPendenciaTipoPrincipal(rs1.getString("PEND_TIPO_PRINC"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_PEND_TIPO_RELACIONADA WHERE PEND_TIPO_PRINC LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..PendenciaTipoRelacionadaPsGen.consultarDescricao() Operação realizada com sucesso");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return liTemp; 
	}

} 
