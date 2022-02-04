package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.EmpresaTipoDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class EmpresaTipoPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = -3715387312983267112L;

	//---------------------------------------------------------
	public EmpresaTipoPsGen() {

	}



//---------------------------------------------------------
	public void inserir(EmpresaTipoDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psEmpresaTipoinserir()");
		stSqlCampos= "INSERT INTO PROJUDI.EMPRESA_TIPO ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getEmpresaTipoCodigo().length()>0)) {
			 stSqlCampos+=   stVirgula + "EMPRESA_TIPO_CODIGO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getEmpresaTipoCodigo());  

			stVirgula=",";
		}
		if ((dados.getEmpresaTipo().length()>0)) {
			 stSqlCampos+=   stVirgula + "EMPRESA_TIPO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getEmpresaTipo());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_EMPRESA_TIPO",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(EmpresaTipoDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psEmpresaTipoalterar()");

		stSql= "UPDATE PROJUDI.EMPRESA_TIPO SET  ";
		stSql+= "EMPRESA_TIPO_CODIGO = ?";		 ps.adicionarLong(dados.getEmpresaTipoCodigo());  

		stSql+= ",EMPRESA_TIPO = ?";		 ps.adicionarString(dados.getEmpresaTipo());  

		stSql += " WHERE ID_EMPRESA_TIPO  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps); 
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psEmpresaTipoexcluir()");

		stSql= "DELETE FROM PROJUDI.EMPRESA_TIPO";
		stSql += " WHERE ID_EMPRESA_TIPO = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public EmpresaTipoDt consultarId(String id_empresatipo )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		EmpresaTipoDt Dados=null;
		////System.out.println("....ps-ConsultaId_EmpresaTipo)");

		stSql= "SELECT * FROM PROJUDI.VIEW_EMPRESA_TIPO WHERE ID_EMPRESA_TIPO = ?";		ps.adicionarLong(id_empresatipo); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_EmpresaTipo  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new EmpresaTipoDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( EmpresaTipoDt Dados, ResultSetTJGO rs )  throws Exception {
		
		Dados.setId(rs.getString("ID_EMPRESA_TIPO"));
		Dados.setEmpresaTipo(rs.getString("EMPRESA_TIPO"));
		Dados.setEmpresaTipoCodigo( rs.getString("EMPRESA_TIPO_CODIGO"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
		
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoEmpresaTipo()");

		stSql= "SELECT ID_EMPRESA_TIPO, EMPRESA_TIPO FROM PROJUDI.VIEW_EMPRESA_TIPO WHERE EMPRESA_TIPO LIKE ?";
		stSql+= " ORDER BY EMPRESA_TIPO ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoEmpresaTipo  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				EmpresaTipoDt obTemp = new EmpresaTipoDt();
				obTemp.setId(rs1.getString("ID_EMPRESA_TIPO"));
				obTemp.setEmpresaTipo(rs1.getString("EMPRESA_TIPO"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_EMPRESA_TIPO WHERE EMPRESA_TIPO LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..EmpresaTipoPsGen.consultarDescricao() Operação realizada com sucesso");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return liTemp; 
	}

} 
