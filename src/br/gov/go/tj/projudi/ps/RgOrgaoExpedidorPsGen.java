package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.RgOrgaoExpedidorDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class RgOrgaoExpedidorPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = -655696798130839318L;

	//---------------------------------------------------------
	public RgOrgaoExpedidorPsGen() {

	}



//---------------------------------------------------------
	public void inserir(RgOrgaoExpedidorDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psRgOrgaoExpedidorinserir()");
		stSqlCampos= "INSERT INTO PROJUDI.RG_ORGAO_EXP ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getRgOrgaoExpedidor().length()>0)) {
			 stSqlCampos+=   stVirgula + "RG_ORGAO_EXP " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getRgOrgaoExpedidor());  

			stVirgula=",";
		}
		if ((dados.getSigla().length()>0)) {
			 stSqlCampos+=   stVirgula + "SIGLA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getSigla());  

			stVirgula=",";
		}
		if ((dados.getId_Estado().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_ESTADO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Estado());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_RG_ORGAO_EXP",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(RgOrgaoExpedidorDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psRgOrgaoExpedidoralterar()");

		stSql= "UPDATE PROJUDI.RG_ORGAO_EXP SET  ";
		stSql+= "RG_ORGAO_EXP = ?";		 ps.adicionarString(dados.getRgOrgaoExpedidor());  

		stSql+= ",SIGLA = ?";		 ps.adicionarString(dados.getSigla());  

		stSql+= ",ID_ESTADO = ?";		 ps.adicionarLong(dados.getId_Estado());  

		stSql += " WHERE ID_RG_ORGAO_EXP  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psRgOrgaoExpedidorexcluir()");

		stSql= "DELETE FROM PROJUDI.RG_ORGAO_EXP";
		stSql += " WHERE ID_RG_ORGAO_EXP = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public RgOrgaoExpedidorDt consultarId(String id_rgorgaoexpedidor )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		RgOrgaoExpedidorDt Dados=null;
		////System.out.println("....ps-ConsultaId_RgOrgaoExpedidor)");

		stSql= "SELECT * FROM PROJUDI.VIEW_RGORGAO_EXP WHERE ID_RG_ORGAO_EXP = ?";		ps.adicionarLong(id_rgorgaoexpedidor); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_RgOrgaoExpedidor  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new RgOrgaoExpedidorDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( RgOrgaoExpedidorDt Dados, ResultSetTJGO rs )  throws Exception {
	
		Dados.setId(rs.getString("ID_RG_ORGAO_EXP"));
		Dados.setRgOrgaoExpedidor(rs.getString("RG_ORGAO_EXP"));
		Dados.setSigla( rs.getString("SIGLA"));
		Dados.setId_Estado( rs.getString("ID_ESTADO"));
		Dados.setEstado( rs.getString("ESTADO"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
		Dados.setEstadoCodigo( rs.getString("ESTADO_CODIGO"));
		Dados.setUf( rs.getString("UF"));

	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoRgOrgaoExpedidor()");

		stSql= "SELECT ID_RG_ORGAO_EXP, RG_ORGAO_EXP FROM PROJUDI.VIEW_RGORGAO_EXP WHERE RG_ORGAO_EXP LIKE ?";
		stSql+= " ORDER BY RG_ORGAO_EXP ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoRgOrgaoExpedidor  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				RgOrgaoExpedidorDt obTemp = new RgOrgaoExpedidorDt();
				obTemp.setId(rs1.getString("ID_RG_ORGAO_EXP"));
				obTemp.setRgOrgaoExpedidor(rs1.getString("RG_ORGAO_EXP"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_RGORGAO_EXP WHERE RG_ORGAO_EXP LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..RgOrgaoExpedidorPsGen.consultarDescricao() Operação realizada com sucesso");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return liTemp; 
	}

} 
