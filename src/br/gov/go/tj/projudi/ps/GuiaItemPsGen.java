package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.GuiaItemDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class GuiaItemPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = -2121359057751784404L;

	//---------------------------------------------------------
	public GuiaItemPsGen() {

	}



//---------------------------------------------------------
	public void inserir(GuiaItemDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psGuiaIteminserir()");
		stSqlCampos= "INSERT INTO projudi.GUIA_ITEM ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getId_GuiaEmissao().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_GUIA_EMIS " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_GuiaEmissao());  

			stVirgula=",";
		}
		if ((dados.getGuiaItemCodigo().length()>0)) {
			 stSqlCampos+=   stVirgula + "GUIA_ITEM_CODIGO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getGuiaItemCodigo());  

			stVirgula=",";
		}
		if ((dados.getQuantidade().length()>0)) {
			 stSqlCampos+=   stVirgula + "QUANTIDADE " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getQuantidade());  

			stVirgula=",";
		}
		if ((dados.getValorCalculado().length()>0)) {
			 stSqlCampos+=   stVirgula + "VALOR_CALCULADO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getValorCalculado());  

			stVirgula=",";
		}
		if ((dados.getValorReferencia().length()>0)) {
			 stSqlCampos+=   stVirgula + "VALOR_REFERENCIA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getValorReferencia());  

			stVirgula=",";
		}
		if ((dados.getParcelas().length()>0)) {
			 stSqlCampos+=   stVirgula + "PARCELAS " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getParcelas());  

			stVirgula=",";
		}
		if ((dados.getParcelaCorrente().length()>0)) {
			 stSqlCampos+=   stVirgula + "PARCELA_CORRENTE " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getParcelaCorrente());  

			stVirgula=",";
		}
		if ((dados.getGuiaItem().length()>0)) {
			 stSqlCampos+=   stVirgula + "GUIA_ITEM " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getGuiaItem());  

			stVirgula=",";
		}
		if ((dados.getId_Custa().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_CUSTA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Custa());  

			stVirgula=",";
		}
		if ((dados.getCodigoOficial().length()>0)) {
			 stSqlCampos+=   stVirgula + "CODIGO_OFICIAL " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getCodigoOficial());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_GUIA_ITEM",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(GuiaItemDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psGuiaItemalterar()");

		stSql= "UPDATE projudi.GUIA_ITEM SET  ";
		stSql+= "ID_GUIA_EMIS = ?";		 ps.adicionarLong(dados.getId_GuiaEmissao());  

		stSql+= ",QUANTIDADE = ?";		 ps.adicionarString(dados.getQuantidade());  

		stSql+= ",VALOR_CALCULADO = ?";		 ps.adicionarString(dados.getValorCalculado());  

		stSql+= ",VALOR_REFERENCIA = ?";		 ps.adicionarString(dados.getValorReferencia());  

		stSql+= ",PARCELAS = ?";		 ps.adicionarLong(dados.getParcelas());  

		stSql+= ",PARCELA_CORRENTE = ?";		 ps.adicionarLong(dados.getParcelaCorrente());  

		stSql+= ",GUIA_ITEM = ?";		 ps.adicionarString(dados.getGuiaItem());  

		stSql+= ",ID_CUSTA = ?";		 ps.adicionarLong(dados.getId_Custa());
		
		stSql+= ",GUIA_ITEM_CODIGO = ?";		 ps.adicionarLong(dados.getGuiaItemCodigo());
		
		stSql+= ",CODIGO_OFICIAL = ?";		 ps.adicionarLong(dados.getCodigoOficial());

		stSql += " WHERE ID_GUIA_ITEM  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psGuiaItemexcluir()");

		stSql= "DELETE FROM projudi.GUIA_ITEM";
		stSql += " WHERE ID_GUIA_ITEM = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public GuiaItemDt consultarId(String id_guiaitem )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		GuiaItemDt Dados=null;
		////System.out.println("....ps-ConsultaId_GuiaItem)");

		stSql= "SELECT * FROM projudi.VIEW_GUIA_ITEM WHERE ID_GUIA_ITEM = ?";		ps.adicionarLong(id_guiaitem); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_GuiaItem  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new GuiaItemDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( GuiaItemDt Dados, ResultSetTJGO rs )  throws Exception {

		Dados.setId(rs.getString("ID_GUIA_ITEM"));
		Dados.setGuiaItem(rs.getString("GUIA_ITEM"));
		Dados.setId_GuiaEmissao( rs.getString("ID_GUIA_EMIS"));
		Dados.setId_Custa( rs.getString("ID_CUSTA"));
		Dados.setCusta( rs.getString("CUSTA"));
		Dados.setGuiaItemCodigo( rs.getString("GUIA_ITEM_CODIGO"));
		Dados.setQuantidade( rs.getString("QUANTIDADE"));
		Dados.setValorCalculado( rs.getString("VALOR_CALCULADO"));
		Dados.setValorReferencia( rs.getString("VALOR_REFERENCIA"));
		Dados.setParcelas( rs.getString("PARCELAS"));
		Dados.setParcelaCorrente( rs.getString("PARCELA_CORRENTE"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
	
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoGuiaItem()");

		stSql= "SELECT ID_GUIA_ITEM, GUIA_ITEM FROM projudi.VIEW_GUIA_ITEM WHERE GUIA_ITEM LIKE ?";
		stSql+= " ORDER BY GUIA_ITEM ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoGuiaItem  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				GuiaItemDt obTemp = new GuiaItemDt();
				obTemp.setId(rs1.getString("ID_GUIA_ITEM"));
				obTemp.setGuiaItem(rs1.getString("GUIA_ITEM"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE FROM projudi.VIEW_GUIA_ITEM WHERE GUIA_ITEM LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..GuiaItemPsGen.consultarDescricao() Operação realizada com sucesso");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return liTemp; 
	}

} 
