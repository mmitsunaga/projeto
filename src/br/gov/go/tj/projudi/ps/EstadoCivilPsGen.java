package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.EstadoCivilDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class EstadoCivilPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = -3532557258142069373L;

	//---------------------------------------------------------
	public EstadoCivilPsGen() {

	}



//---------------------------------------------------------
	public void inserir(EstadoCivilDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psEstadoCivilinserir()");
		stSqlCampos= "INSERT INTO PROJUDI.ESTADO_CIVIL ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getEstadoCivil().length()>0)) {
			 stSqlCampos+=   stVirgula + "ESTADO_CIVIL " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getEstadoCivil());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_ESTADO_CIVIL",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(EstadoCivilDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psEstadoCivilalterar()");

		stSql= "UPDATE PROJUDI.ESTADO_CIVIL SET  ";
		stSql+= "ESTADO_CIVIL = ?";		 ps.adicionarString(dados.getEstadoCivil());  

		stSql += " WHERE ID_ESTADO_CIVIL  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psEstadoCivilexcluir()");

		stSql= "DELETE FROM PROJUDI.ESTADO_CIVIL";
		stSql += " WHERE ID_ESTADO_CIVIL = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public EstadoCivilDt consultarId(String id_estadocivil )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		EstadoCivilDt Dados=null;
		////System.out.println("....ps-ConsultaId_EstadoCivil)");

		stSql= "SELECT * FROM PROJUDI.VIEW_ESTADO_CIVIL WHERE ID_ESTADO_CIVIL = ?";		ps.adicionarLong(id_estadocivil); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_EstadoCivil  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new EstadoCivilDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( EstadoCivilDt Dados, ResultSetTJGO rs )  throws Exception {

		Dados.setId(rs.getString("ID_ESTADO_CIVIL"));
		Dados.setEstadoCivil(rs.getString("ESTADO_CIVIL"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
			
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoEstadoCivil()");

		stSql= "SELECT ID_ESTADO_CIVIL, ESTADO_CIVIL FROM PROJUDI.VIEW_ESTADO_CIVIL WHERE ESTADO_CIVIL LIKE ?";
		stSql+= " ORDER BY ESTADO_CIVIL ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoEstadoCivil  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				EstadoCivilDt obTemp = new EstadoCivilDt();
				obTemp.setId(rs1.getString("ID_ESTADO_CIVIL"));
				obTemp.setEstadoCivil(rs1.getString("ESTADO_CIVIL"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_ESTADO_CIVIL WHERE ESTADO_CIVIL LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..EstadoCivilPsGen.consultarDescricao() Operação realizada com sucesso");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return liTemp; 
	}

} 
