package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.EstatisticaProdutividadeItemDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class EstatisticaProdutividadeItemPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = 3820626901072729395L;

	//---------------------------------------------------------
	public EstatisticaProdutividadeItemPsGen() {

	}



//---------------------------------------------------------
	public void inserir(EstatisticaProdutividadeItemDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		//////System.out.println("....psEstatisticaProdutividadeIteminserir()");
		stSqlCampos= "INSERT INTO projudi.EST_PROD_ITEM ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getEstatisticaProdutividadeItem().length()>0)) {
			 stSqlCampos+=   stVirgula + "EST_PROD_ITEM " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getEstatisticaProdutividadeItem());  

			stVirgula=",";
		}
		if ((dados.getDadoCodigo().length()>0)) {
			 stSqlCampos+=   stVirgula + "DADOS_CODIGO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getDadoCodigo());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		dados.setId(executarInsert(stSql,"EST_PROD_ITEM",ps)); 
	} 

//---------------------------------------------------------
	public void alterar(EstatisticaProdutividadeItemDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		//////System.out.println("....psEstatisticaProdutividadeItemalterar()");

		stSql= "UPDATE projudi.EST_PROD_ITEM SET  ";
		stSql+= "EST_PROD_ITEM = ?";		 ps.adicionarString(dados.getEstatisticaProdutividadeItem());  

		stSql+= ",DADOS_CODIGO = ?";		 ps.adicionarLong(dados.getDadoCodigo());  

		stSql += " WHERE ID_EST_PROD_ITEM  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		//////System.out.println("....psEstatisticaProdutividadeItemexcluir()");

		stSql= "DELETE FROM projudi.EST_PROD_ITEM";
		stSql += " WHERE ID_EST_PROD_ITEM = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public EstatisticaProdutividadeItemDt consultarId(String id_estatisticaprodutividadeitem )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		EstatisticaProdutividadeItemDt Dados=null;
		//////System.out.println("....ps-ConsultaId_EstatisticaProdutividadeItem)");

		stSql= "SELECT * FROM projudi.VIEW_EST_PROD_ITEM WHERE ID_EST_PROD_ITEM = ?";		ps.adicionarLong(id_estatisticaprodutividadeitem); 

		//////System.out.println("....Sql  " + stSql  );

		try{
			//////System.out.println("..ps-ConsultaId_EstatisticaProdutividadeItem  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new EstatisticaProdutividadeItemDt();
				associarDt(Dados, rs1);
			}
			//////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( EstatisticaProdutividadeItemDt Dados, ResultSetTJGO rs )  throws Exception {
		
		Dados.setId(rs.getString("ID_EST_PROD_ITEM"));
		Dados.setEstatisticaProdutividadeItem(rs.getString("EST_PROD_ITEM"));
		Dados.setDadoCodigo( rs.getString("DADOS_CODIGO"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
		
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		//////System.out.println("..ps-ConsultaDescricaoEstatisticaProdutividadeItem()");

		stSql= "SELECT ID_EST_PROD_ITEM, EST_PROD_ITEM FROM projudi.VIEW_EST_PROD_ITEM WHERE EST_PROD_ITEM LIKE ?";
		stSql+= " ORDER BY EST_PROD_ITEM ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			//////System.out.println("..ps-ConsultaDescricaoEstatisticaProdutividadeItem  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			//////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				EstatisticaProdutividadeItemDt obTemp = new EstatisticaProdutividadeItemDt();
				obTemp.setId(rs1.getString("ID_EST_PROD_ITEM"));
				obTemp.setEstatisticaProdutividadeItem(rs1.getString("EST_PROD_ITEM"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM projudi.VIEW_EST_PROD_ITEM WHERE EST_PROD_ITEM LIKE ?";
			rs2 = consultar(stSql,ps);
			//////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			//////System.out.println("..EstatisticaProdutividadeItemPsGen.consultarDescricao() Operação realizada com sucesso");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return liTemp; 
	}

} 
