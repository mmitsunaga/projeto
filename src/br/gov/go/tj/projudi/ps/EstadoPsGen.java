package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.EstadoDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class EstadoPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = 390189824765775158L;

	//---------------------------------------------------------
	public EstadoPsGen() {

	}



//---------------------------------------------------------
	public void inserir(EstadoDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psEstadoinserir()");
		stSqlCampos= "INSERT INTO PROJUDI.ESTADO ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getEstado().length()>0)) {
			 stSqlCampos+=   stVirgula + "ESTADO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getEstado());  

			stVirgula=",";
		}
		if ((dados.getEstadoCodigo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ESTADO_CODIGO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getEstadoCodigo());  

			stVirgula=",";
		}
		if ((dados.getId_Pais().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_PAIS " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Pais());  

			stVirgula=",";
		}
		if ((dados.getUf().length()>0)) {
			 stSqlCampos+=   stVirgula + "UF " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getUf());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_ESTADO",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(EstadoDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psEstadoalterar()");

		stSql= "UPDATE PROJUDI.ESTADO SET  ";
		stSql+= "ESTADO = ?";		 ps.adicionarString(dados.getEstado());  

		stSql+= ",ESTADO_CODIGO = ?";		 ps.adicionarLong(dados.getEstadoCodigo());  

		stSql+= ",ID_PAIS = ?";		 ps.adicionarLong(dados.getId_Pais());  

		stSql+= ",UF = ?";		 ps.adicionarString(dados.getUf());  

		stSql += " WHERE ID_ESTADO  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psEstadoexcluir()");

		stSql= "DELETE FROM PROJUDI.ESTADO";
		stSql += " WHERE ID_ESTADO = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public EstadoDt consultarId(String id_estado )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		EstadoDt Dados=null;
		////System.out.println("....ps-ConsultaId_Estado)");

		stSql= "SELECT * FROM PROJUDI.VIEW_ESTADO WHERE ID_ESTADO = ?";		ps.adicionarLong(id_estado); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_Estado  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new EstadoDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( EstadoDt Dados, ResultSetTJGO rs )  throws Exception {
		
		Dados.setId(rs.getString("ID_ESTADO"));
		Dados.setEstado(rs.getString("ESTADO"));
		Dados.setEstadoCodigo( rs.getString("ESTADO_CODIGO"));
		Dados.setId_Pais( rs.getString("ID_PAIS"));
		Dados.setPais( rs.getString("PAIS"));
		Dados.setUf( rs.getString("UF"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
		Dados.setPaisCodigo( rs.getString("PAIS_CODIGO"));
		
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoEstado()");

		stSql= "SELECT ID_ESTADO, ESTADO FROM PROJUDI.VIEW_ESTADO WHERE ESTADO LIKE ?";
		stSql+= " ORDER BY ESTADO ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoEstado  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				EstadoDt obTemp = new EstadoDt();
				obTemp.setId(rs1.getString("ID_ESTADO"));
				obTemp.setEstado(rs1.getString("ESTADO"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_ESTADO WHERE ESTADO LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..EstadoPsGen.consultarDescricao() Operação realizada com sucesso");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return liTemp; 
	}

} 
