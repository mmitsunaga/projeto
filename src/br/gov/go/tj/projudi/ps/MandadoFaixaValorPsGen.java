package br.gov.go.tj.projudi.ps;

import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;
import java.util.List;
import java.util.ArrayList;
import java.sql.Connection;
import br.gov.go.tj.projudi.dt.MandadoFaixaValorDt;


public class MandadoFaixaValorPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = -7307121376705774199L;

	//---------------------------------------------------------
	public MandadoFaixaValorPsGen() {


	}



//---------------------------------------------------------
	public void inserir(MandadoFaixaValorDt dados ) throws Exception { 

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSqlCampos= "INSERT INTO PROJUDI.MANDADO_FAIXA_VALOR ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getFaixaInicio().length()>0)) {
			 stSqlCampos+=   stVirgula + "FAIXA_INICIO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getFaixaInicio());  

			stVirgula=",";
		}
		if ((dados.getFaixaFim().length()>0)) {
			 stSqlCampos+=   stVirgula + "FAIXA_FIM " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getFaixaFim());  

			stVirgula=",";
		}
		if ((dados.getDataVigenciaInicio().length()>0)) {
			 stSqlCampos+=   stVirgula + "DATA_VIGENCIA_INICIO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarDateTime(dados.getDataVigenciaInicio());  

			stVirgula=",";
		}
		if ((dados.getDataVigenciaFim().length()>0)) {
			 stSqlCampos+=   stVirgula + "DATA_VIGENCIA_FIM " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarDateTime(dados.getDataVigenciaFim());  

			stVirgula=",";
		}
		if ((dados.getValor().length()>0)) {
			 stSqlCampos+=   stVirgula + "VALOR " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getValor());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 


		try {
			dados.setId(executarInsert(stSql,"ID_MANDADO_FAIXA_VALOR",ps));


		}catch(Exception e){
			System.out.println("....psinserir: " + e.getMessage());
			throw new Exception(" <{Erro:.....}> MandadoFaixaValorPsGen.inserir() " + e.getMessage() );
			} 
	} 

//---------------------------------------------------------
	public void alterar(MandadoFaixaValorDt dados) throws Exception{ 

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";



		stSql= "UPDATE PROJUDI.MANDADO_FAIXA_VALOR SET  ";
		stSql+= "FAIXA_INICIO = ?";		 ps.adicionarLong(dados.getFaixaInicio());  

		stSql+= ",FAIXA_FIM = ?";		 ps.adicionarLong(dados.getFaixaFim());  

		stSql+= ",DATA_VIGENCIA_INICIO = ?";		 ps.adicionarDateTime(dados.getDataVigenciaInicio());  

		stSql+= ",DATA_VIGENCIA_FIM = ?";		 ps.adicionarDateTime(dados.getDataVigenciaFim());  

		stSql+= ",VALOR = ?";		 ps.adicionarLong(dados.getValor());  

		stSql += " WHERE ID_MANDADO_FAIXA_VALOR  = ? "; 		ps.adicionarLong(dados.getId()); 



			executarUpdateDelete(stSql,ps);


	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception { 

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "DELETE FROM PROJUDI.MANDADO_FAIXA_VALOR";
		stSql += " WHERE ID_MANDADO_FAIXA_VALOR = ?";		ps.adicionarLong(chave); 



			executarUpdateDelete(stSql,ps);


	} 

//---------------------------------------------------------
	public MandadoFaixaValorDt consultarId(String id_mandadofaixavalor )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		MandadoFaixaValorDt Dados=null;


		stSql= "SELECT * FROM PROJUDI.VIEW_MANDADO_FAIXA_VALOR WHERE ID_MANDADO_FAIXA_VALOR = ?";		ps.adicionarLong(id_mandadofaixavalor); 



		try{

			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new MandadoFaixaValorDt();
				associarDt(Dados, rs1);
			}

		}finally{
			try {if (rs1 != null) rs1.close();} catch (Exception e) {}
		}
			return Dados; 
	}

	protected void associarDt( MandadoFaixaValorDt Dados, ResultSetTJGO rs )  throws Exception {

			Dados.setId(rs.getString("ID_MANDADO_FAIXA_VALOR"));
			Dados.setFaixaInicio(rs.getString("FAIXA_INICIO"));
			Dados.setFaixaFim( rs.getString("FAIXA_FIM"));
			Dados.setDataVigenciaInicio( Funcoes.FormatarDataHora(rs.getString("DATA_VIGENCIA_INICIO")));
			Dados.setDataVigenciaFim( Funcoes.FormatarDataHora(rs.getString("DATA_VIGENCIA_FIM")));
			Dados.setValor( rs.getString("VALOR"));
			Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql,stSqlOrder, stSqlFrom;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "SELECT ID_MANDADO_FAIXA_VALOR, FAIXA_INICIO ";
		stSqlFrom= " FROM PROJUDI.VIEW_MANDADO_FAIXA_VALOR WHERE FAIXA_INICIO LIKE ?";
		stSqlOrder = " ORDER BY FAIXA_INICIO ";
		ps.adicionarString(descricao+"%"); 

		try{


			rs1 = consultarPaginacao(stSql + stSqlFrom + stSqlOrder, ps, posicao);


			while (rs1.next()) {
				MandadoFaixaValorDt obTemp = new MandadoFaixaValorDt();
				obTemp.setId(rs1.getString("ID_MANDADO_FAIXA_VALOR"));
				obTemp.setFaixaInicio(rs1.getString("FAIXA_INICIO"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT Count(*) as Quantidade";
			rs2 = consultar(stSql + stSqlFrom,ps);

			while (rs2.next()) {
				liTemp.add(rs2.getLong("Quantidade"));
			}

		}finally{
			try {if (rs1 != null) rs1.close();} catch (Exception e) {}
			try {if (rs2 != null) rs2.close();} catch (Exception e) {}
		}
			return liTemp; 
	}

//---------------------------------------------------------
	public String consultarDescricaoJSON(String descricao, String posicao ) throws Exception {

		String stSql, stSqlFrom, stSqlOrder;
		String stTemp="";
		int qtdeColunas = 1;
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "SELECT ID_MANDADO_FAIXA_VALOR as id, FAIXA_INICIO as descricao1 ";
		stSqlFrom= " FROM PROJUDI.VIEW_MANDADO_FAIXA_VALOR WHERE FAIXA_INICIO LIKE ?";
		ps.adicionarString(descricao+"%"); 

		stSqlOrder= " ORDER BY FAIXA_INICIO ";
		try{


			rs1 = consultarPaginacao(stSql+stSqlFrom+stSqlOrder, ps, posicao);
			stSql= "SELECT Count(*) as Quantidade";
			rs2 = consultar(stSql+stSqlFrom,ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);

		}finally{
			try {if (rs1 != null) rs1.close();} catch (Exception e) {}
			try {if (rs2 != null) rs2.close();} catch (Exception e) {}
		}
			return stTemp; 
	}

} 
