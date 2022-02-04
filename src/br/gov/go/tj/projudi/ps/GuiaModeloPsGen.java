package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.GuiaModeloDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class GuiaModeloPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = 1774614900967827148L;

	//---------------------------------------------------------
	public GuiaModeloPsGen() {

	}



//---------------------------------------------------------
	public void inserir(GuiaModeloDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psGuiaModeloinserir()");
		stSqlCampos= "INSERT INTO projudi.GUIA_MODELO ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getGuiaModelo().length()>0)) {
			 stSqlCampos+=   stVirgula + "GUIA_MODELO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getGuiaModelo());  

			stVirgula=",";
		}
		if ((dados.getGuiaModeloCodigo().length()>0)) {
			 stSqlCampos+=   stVirgula + "GUIA_MODELO_CODIGO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getGuiaModeloCodigo());  

			stVirgula=",";
		}
		if ((dados.getId_GuiaTipo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_GUIA_TIPO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_GuiaTipo());  

			stVirgula=",";
		}
		if ((dados.getId_ProcessoTipo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_PROC_TIPO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_ProcessoTipo());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_GUIA_MODELO",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(GuiaModeloDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psGuiaModeloalterar()");

		stSql= "UPDATE projudi.GUIA_MODELO SET  ";
		stSql+= "GUIA_MODELO = ?";		 ps.adicionarString(dados.getGuiaModelo());  

		stSql+= ",GUIA_MODELO_CODIGO = ?";		 ps.adicionarLong(dados.getGuiaModeloCodigo());  

		stSql+= ",ID_GUIA_TIPO = ?";		 ps.adicionarLong(dados.getId_GuiaTipo());  

		stSql+= ",ID_PROC_TIPO = ?";		 ps.adicionarLong(dados.getId_ProcessoTipo());  

		stSql += " WHERE ID_GUIA_MODELO  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psGuiaModeloexcluir()");

		stSql= "DELETE FROM projudi.GUIA_MODELO";
		stSql += " WHERE ID_GUIA_MODELO = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public GuiaModeloDt consultarId(String id_guiamodelo )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		GuiaModeloDt Dados=null;
		////System.out.println("....ps-ConsultaId_GuiaModelo)");

		stSql= "SELECT * FROM projudi.VIEW_GUIA_MODELO WHERE ID_GUIA_MODELO = ?";		ps.adicionarLong(id_guiamodelo); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_GuiaModelo  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new GuiaModeloDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( GuiaModeloDt Dados, ResultSetTJGO rs )  throws Exception {

		Dados.setId(rs.getString("ID_GUIA_MODELO"));
		Dados.setGuiaModelo(rs.getString("GUIA_MODELO"));
		Dados.setGuiaModeloCodigo( rs.getString("GUIA_MODELO_CODIGO"));
		Dados.setId_GuiaTipo( rs.getString("ID_GUIA_TIPO"));
		Dados.setGuiaTipo( rs.getString("GUIA_TIPO"));
		Dados.setFlagGrau( rs.getString("FLAG_GRAU") );
		Dados.setId_ProcessoTipo( rs.getString("ID_PROC_TIPO"));
		Dados.setProcessoTipo( rs.getString("PROC_TIPO"));
		Dados.setId_NaturezaSPG(rs.getString("ID_NATUREZA_SPG"));
		Dados.setNaturezaSPG(rs.getString("NATUREZA_SPG"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));

	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoGuiaModelo()");

		stSql= "SELECT ID_GUIA_MODELO, GUIA_MODELO FROM projudi.VIEW_GUIA_MODELO WHERE GUIA_MODELO LIKE ?";
		stSql+= " ORDER BY GUIA_MODELO ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoGuiaModelo  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				GuiaModeloDt obTemp = new GuiaModeloDt();
				obTemp.setId(rs1.getString("ID_GUIA_MODELO"));
				obTemp.setGuiaModelo(rs1.getString("GUIA_MODELO"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM projudi.VIEW_GUIA_MODELO WHERE GUIA_MODELO LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..GuiaModeloPsGen.consultarDescricao() Operação realizada com sucesso");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return liTemp; 
	}

} 
