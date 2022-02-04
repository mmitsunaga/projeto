package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.AudienciaTipoDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class AudienciaTipoPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = -3795549759831303567L;

	//---------------------------------------------------------
	public AudienciaTipoPsGen() {

	}



//---------------------------------------------------------
	public void inserir(AudienciaTipoDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psAudienciaTipoinserir()");
		stSqlCampos= "INSERT INTO PROJUDI.AUDI_TIPO ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getAudienciaTipo().length()>0)) {
			 stSqlCampos+=   stVirgula + "AUDI_TIPO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getAudienciaTipo());  

			stVirgula=",";
		}
		if ((dados.getAudienciaTipoCodigo().length()>0)) {
			 stSqlCampos+=   stVirgula + "AUDI_TIPO_CODIGO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getAudienciaTipoCodigo());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_AUDI_TIPO",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(AudienciaTipoDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psAudienciaTipoalterar()");

		stSql= "UPDATE PROJUDI.AUDI_TIPO SET  ";
		stSql+= "AUDI_TIPO = ?";		 ps.adicionarString(dados.getAudienciaTipo());  

		stSql+= ",AUDI_TIPO_CODIGO = ?";		 ps.adicionarLong(dados.getAudienciaTipoCodigo());  

		stSql += " WHERE ID_AUDI_TIPO  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psAudienciaTipoexcluir()");

		stSql= "DELETE FROM PROJUDI.AUDI_TIPO";
		stSql += " WHERE ID_AUDI_TIPO = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public AudienciaTipoDt consultarId(String id_audienciatipo )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		AudienciaTipoDt Dados=null;
		////System.out.println("....ps-ConsultaId_AudienciaTipo)");

		stSql= "SELECT * FROM PROJUDI.VIEW_AUDI_TIPO WHERE ID_AUDI_TIPO = ?";		ps.adicionarLong(id_audienciatipo); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_AudienciaTipo  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new AudienciaTipoDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( AudienciaTipoDt Dados, ResultSetTJGO rs )  throws Exception {

		Dados.setId(rs.getString("ID_AUDI_TIPO"));
		Dados.setAudienciaTipo(rs.getString("AUDI_TIPO"));
		Dados.setAudienciaTipoCodigo( rs.getString("AUDI_TIPO_CODIGO"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoAudienciaTipo()");

		stSql= "SELECT ID_AUDI_TIPO, AUDI_TIPO FROM PROJUDI.VIEW_AUDI_TIPO WHERE AUDI_TIPO LIKE ?";
		stSql+= " ORDER BY AUDI_TIPO ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoAudienciaTipo  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				AudienciaTipoDt obTemp = new AudienciaTipoDt();
				obTemp.setId(rs1.getString("ID_AUDI_TIPO"));
				obTemp.setAudienciaTipo(rs1.getString("AUDI_TIPO"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_AUDI_TIPO WHERE AUDI_TIPO LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..AudienciaTipoPsGen.consultarDescricao() Operação realizada com sucesso");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return liTemp; 
	}

} 
