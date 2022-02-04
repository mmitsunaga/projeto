package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.PrazoSuspensoTipoDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class PrazoSuspensoTipoPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = 226448793869035025L;

	//---------------------------------------------------------
	public PrazoSuspensoTipoPsGen() {

	}



//---------------------------------------------------------
	public void inserir(PrazoSuspensoTipoDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psPrazoSuspensoTipoinserir()");
		stSqlCampos= "INSERT INTO PROJUDI.PRAZO_SUSPENSO_TIPO ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getPrazoSuspensoTipo().length()>0)) {
			 stSqlCampos+=   stVirgula + "PRAZO_SUSPENSO_TIPO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getPrazoSuspensoTipo());  

			stVirgula=",";
		}
		if ((dados.getPrazoSuspensoTipoCodigo().length()>0)) {
			 stSqlCampos+=   stVirgula + "PRAZO_SUSPENSO_TIPO_CODIGO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getPrazoSuspensoTipoCodigo());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_PRAZO_SUSPENSO_TIPO",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(PrazoSuspensoTipoDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psPrazoSuspensoTipoalterar()");

		stSql= "UPDATE PROJUDI.PRAZO_SUSPENSO_TIPO SET  ";
		stSql+= "PRAZO_SUSPENSO_TIPO = ?";		 ps.adicionarString(dados.getPrazoSuspensoTipo());  

		stSql+= ",PRAZO_SUSPENSO_TIPO_CODIGO = ?";		 ps.adicionarLong(dados.getPrazoSuspensoTipoCodigo());  

		stSql += " WHERE ID_PRAZO_SUSPENSO_TIPO  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psPrazoSuspensoTipoexcluir()");

		stSql= "DELETE FROM PROJUDI.PRAZO_SUSPENSO_TIPO";
		stSql += " WHERE ID_PRAZO_SUSPENSO_TIPO = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public PrazoSuspensoTipoDt consultarId(String id_prazosuspensotipo )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		PrazoSuspensoTipoDt Dados=null;
		////System.out.println("....ps-ConsultaId_PrazoSuspensoTipo)");

		stSql= "SELECT * FROM PROJUDI.VIEW_PRAZO_SUSPENSO_TIPO WHERE ID_PRAZO_SUSPENSO_TIPO = ?";		ps.adicionarLong(id_prazosuspensotipo); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_PrazoSuspensoTipo  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new PrazoSuspensoTipoDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( PrazoSuspensoTipoDt Dados, ResultSetTJGO rs )  throws Exception {

		Dados.setId(rs.getString("ID_PRAZO_SUSPENSO_TIPO"));
		Dados.setPrazoSuspensoTipo(rs.getString("PRAZO_SUSPENSO_TIPO"));
		Dados.setPrazoSuspensoTipoCodigo( rs.getString("PRAZO_SUSPENSO_TIPO_CODIGO"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));

	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoPrazoSuspensoTipo()");

		stSql= "SELECT ID_PRAZO_SUSPENSO_TIPO, PRAZO_SUSPENSO_TIPO FROM PROJUDI.VIEW_PRAZO_SUSPENSO_TIPO WHERE PRAZO_SUSPENSO_TIPO LIKE ?";
		stSql+= " ORDER BY PRAZO_SUSPENSO_TIPO ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoPrazoSuspensoTipo  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				PrazoSuspensoTipoDt obTemp = new PrazoSuspensoTipoDt();
				obTemp.setId(rs1.getString("ID_PRAZO_SUSPENSO_TIPO"));
				obTemp.setPrazoSuspensoTipo(rs1.getString("PRAZO_SUSPENSO_TIPO"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_PRAZO_SUSPENSO_TIPO WHERE PRAZO_SUSPENSO_TIPO LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..PrazoSuspensoTipoPsGen.consultarDescricao() Operação realizada com sucesso");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return liTemp; 
	}

} 
