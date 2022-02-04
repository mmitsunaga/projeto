package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.AreaDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class AreaPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = 9013312049210016609L;

	//---------------------------------------------------------
	public AreaPsGen() {

	}



//---------------------------------------------------------
	public void inserir(AreaDt dados ) throws Exception{

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psAreainserir()");
		stSqlCampos= "INSERT INTO PROJUDI.AREA ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getArea().length()>0)) {
			 stSqlCampos+=   stVirgula + "AREA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getArea());  

			stVirgula=",";
		}
		if ((dados.getAreaCodigo().length()>0)) {
			 stSqlCampos+=   stVirgula + "AREA_CODIGO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getAreaCodigo());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_AREA",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(AreaDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psAreaalterar()");

		stSql= "UPDATE PROJUDI.AREA SET  ";
		stSql+= "AREA = ?";		 ps.adicionarString(dados.getArea());  

		stSql+= ",AREA_CODIGO = ?";		 ps.adicionarLong(dados.getAreaCodigo());  

		stSql += " WHERE ID_AREA  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception{

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psAreaexcluir()");

		stSql= "DELETE FROM PROJUDI.AREA";
		stSql += " WHERE ID_AREA = ?";		ps.adicionarLong(chave); 

		////System.out.println("....Sql  " + stSql  );

		executarUpdateDelete(stSql,ps);
		////System.out.println("....Execução OK  ");


	} 

//---------------------------------------------------------
	public AreaDt consultarId(String ID_AREA )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		AreaDt Dados=null;
		////System.out.println("....ps-ConsultaId_Area)");

		stSql= "SELECT * FROM PROJUDI.VIEW_AREA WHERE ID_AREA = ?";		ps.adicionarLong(ID_AREA); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_Area  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new AreaDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( AreaDt Dados, ResultSetTJGO rs )  throws Exception {
		Dados.setId(rs.getString("ID_AREA"));
		Dados.setArea(rs.getString("AREA"));
		Dados.setAreaCodigo( rs.getString("AREA_CODIGO"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception{

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoArea()");

		stSql= "SELECT ID_AREA, AREA FROM PROJUDI.VIEW_AREA WHERE AREA LIKE ?";
		stSql+= " ORDER BY AREA ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoArea  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				AreaDt obTemp = new AreaDt();
				obTemp.setId(rs1.getString("ID_AREA"));
				obTemp.setArea(rs1.getString("AREA"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_AREA WHERE AREA LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta QUANTIDADE OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..AreaPsGen.consultarDescricao() Operação realizada com sucesso");
		
		}
			finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return liTemp; 
	}

} 
