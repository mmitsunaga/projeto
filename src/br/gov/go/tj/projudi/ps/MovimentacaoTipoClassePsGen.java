package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.MovimentacaoTipoClasseDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class MovimentacaoTipoClassePsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = 3525157900978023064L;

	//---------------------------------------------------------
	public MovimentacaoTipoClassePsGen() {

	}



//---------------------------------------------------------
	public void inserir(MovimentacaoTipoClasseDt dados ) throws Exception{

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		//System.out.println("....psMovimentacaoTipoClasseinserir()");
		stSqlCampos= "INSERT INTO projudi.MOVI_TIPO_CLASSE ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getMovimentacaoTipoClasse().length()>0)) {
			 stSqlCampos+=   stVirgula + "MOVI_TIPO_CLASSE " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getMovimentacaoTipoClasse());  

			stVirgula=",";
		}
		if ((dados.getMovimentacaoTipoClasseCodigo().length()>0)) {
			 stSqlCampos+=   stVirgula + "MOVI_TIPO_CLASSE_CODIGO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getMovimentacaoTipoClasseCodigo());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		//System.out.println("....Sql " + stSql);
		
		dados.setId(executarInsert(stSql,"ID_MOVI_TIPO_CLASSE",ps)); 
	} 

//---------------------------------------------------------
	public void alterar(MovimentacaoTipoClasseDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		//System.out.println("....psMovimentacaoTipoClassealterar()");

		stSql= "UPDATE projudi.MOVI_TIPO_CLASSE SET  ";
		stSql+= "MOVI_TIPO_CLASSE = ?";		 ps.adicionarString(dados.getMovimentacaoTipoClasse());  

		stSql+= ",MOVI_TIPO_CLASSE_CODIGO = ?";		 ps.adicionarLong(dados.getMovimentacaoTipoClasseCodigo());  

		stSql += " WHERE ID_MOVI_TIPO_CLASSE  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		//System.out.println("....psMovimentacaoTipoClasseexcluir()");

		stSql= "DELETE FROM projudi.MOVI_TIPO_CLASSE";
		stSql += " WHERE ID_MOVI_TIPO_CLASSE = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);
	} 

//---------------------------------------------------------
	public MovimentacaoTipoClasseDt consultarId(String id_movimentacaotipoclasse )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		MovimentacaoTipoClasseDt Dados=null;
		//System.out.println("....ps-ConsultaId_MovimentacaoTipoClasse)");

		stSql= "SELECT * FROM projudi.VIEW_MOVI_TIPO_CLASSE WHERE ID_MOVI_TIPO_CLASSE = ?";		ps.adicionarLong(id_movimentacaotipoclasse); 

		//System.out.println("....Sql  " + stSql  );

		try{
			//System.out.println("..ps-ConsultaId_MovimentacaoTipoClasse  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new MovimentacaoTipoClasseDt();
				associarDt(Dados, rs1);
			}
			//System.out.println("..ps-ConsultaId");
		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return Dados; 
	}

	protected void associarDt( MovimentacaoTipoClasseDt Dados, ResultSetTJGO rs )  throws Exception {
		Dados.setId(rs.getString("ID_MOVI_TIPO_CLASSE"));
		Dados.setMovimentacaoTipoClasse(rs.getString("MOVI_TIPO_CLASSE"));			
		Dados.setMovimentacaoTipoClasseCodigo( rs.getString("MOVI_TIPO_CLASSE_CODIGO"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		//System.out.println("..ps-ConsultaDescricaoMovimentacaoTipoClasse()");

		stSql= "SELECT ID_MOVI_TIPO_CLASSE, MOVI_TIPO_CLASSE FROM projudi.viewVIEW_MOVI_TIPO_CLASSE WHERE MOVI_TIPO_CLASSE LIKE ?";
		stSql+= " ORDER BY MOVI_TIPO_CLASSE ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			//System.out.println("..ps-ConsultaDescricaoMovimentacaoTipoClasse  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			//System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				MovimentacaoTipoClasseDt obTemp = new MovimentacaoTipoClasseDt();
				obTemp.setId(rs1.getString("ID_MOVI_TIPO_CLASSE"));
				obTemp.setMovimentacaoTipoClasse(rs1.getString("MOVI_TIPO_CLASSE"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT Count(*) as Quantidade  FROM projudi.viewVIEW_MOVI_TIPO_CLASSE WHERE MOVI_TIPO_CLASSE LIKE ?";
			rs2 = consultar(stSql,ps);
			//System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("Quantidade"));
			}
			//System.out.println("..MovimentacaoTipoClassePsGen.consultarDescricao() Operação realizada com sucesso");
		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return liTemp; 
	}
	
	public String consultarDescricaoJSON(String descricao, String posicao ) throws Exception {

		String stSql="";
		String stTemp="";
		int qtdeColunas = 1;
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "SELECT ID_MOVI_TIPO_CLASSE as id, MOVI_TIPO_CLASSE as descricao1 FROM PROJUDI.MOVI_TIPO_CLASSE WHERE MOVI_TIPO_CLASSE LIKE ?";
		stSql+= " ORDER BY MOVI_TIPO_CLASSE ";
		ps.adicionarString("%"+descricao+"%"); 

		try{


			rs1 = consultarPaginacao(stSql, ps, posicao);
			stSql= "SELECT Count(*) as Quantidade  FROM PROJUDI.MOVI_TIPO_CLASSE WHERE MOVI_TIPO_CLASSE LIKE ?";
			rs2 = consultar(stSql,ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);

		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return stTemp; 
	}

} 
