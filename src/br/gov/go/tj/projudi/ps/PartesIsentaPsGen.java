package br.gov.go.tj.projudi.ps;

import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;
import java.util.List;
import java.util.ArrayList;
import java.sql.Connection;
import br.gov.go.tj.projudi.dt.PartesIsentaDt;


public class PartesIsentaPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = -2771392580208924861L;

	//---------------------------------------------------------
	public PartesIsentaPsGen() {


	}



//---------------------------------------------------------
	public void inserir(PartesIsentaDt dados ) throws Exception { 

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSqlCampos= "INSERT INTO PROJUDI.PARTES_ISENTAS ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getNome().length()>0)) {
			 stSqlCampos+=   stVirgula + "NOME " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getNome());  

			stVirgula=",";
		}
		if ((dados.getCpf().length()>0)) {
			 stSqlCampos+=   stVirgula + "CPF " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getCpf());  

			stVirgula=",";
		}
		if ((dados.getCnpj().length()>0)) {
			 stSqlCampos+=   stVirgula + "CNPJ " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getCnpj());  

			stVirgula=",";
		}
		if ((dados.getId_UsuarioCadastrador().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_USU_CADASTRADOR " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_UsuarioCadastrador());  

			stVirgula=",";
		}
		if ((dados.getId_ServentiaUsuarioCadastrador().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_SERV_USU_CADASTRADOR " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_ServentiaUsuarioCadastrador());  

			stVirgula=",";
		}
		if ((dados.getDataCadastro().length()>0)) {
			 stSqlCampos+=   stVirgula + "DATA_CADASTRO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarDateTime(dados.getDataCadastro());  

			stVirgula=",";
		}
		if ((dados.getId_UsuarioBaixa().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_USU_BAIXA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_UsuarioBaixa());  

			stVirgula=",";
		}
		if ((dados.getId_ServentiaUsuarioBaixa().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_SERV_USU_BAIXA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_ServentiaUsuarioBaixa());  

			stVirgula=",";
		}
		if ((dados.getDataBaixa().length()>0)) {
			 stSqlCampos+=   stVirgula + "DATA_BAIXA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarDateTime(dados.getDataBaixa());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 


		try {
			dados.setId(executarInsert(stSql,"ID_PARTES_ISENTAS",ps));


		}catch(Exception e){
			System.out.println("....psinserir: " + e.getMessage());
			throw new Exception(" <{Erro:.....}> PartesIsentaPsGen.inserir() " + e.getMessage() );
			} 
	} 

//---------------------------------------------------------
	public void alterar(PartesIsentaDt dados) throws Exception{ 

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";



		stSql= "UPDATE PROJUDI.PARTES_ISENTAS SET  ";
		stSql+= "NOME = ?";		 ps.adicionarString(dados.getNome());  

		stSql+= ",CPF = ?";		 ps.adicionarLong(dados.getCpf());  

		stSql+= ",CNPJ = ?";		 ps.adicionarLong(dados.getCnpj());  

		stSql+= ",ID_USU_CADASTRADOR = ?";		 ps.adicionarLong(dados.getId_UsuarioCadastrador());  

		stSql+= ",ID_SERV_USU_CADASTRADOR = ?";		 ps.adicionarLong(dados.getId_ServentiaUsuarioCadastrador());  

		stSql+= ",DATA_CADASTRO = ?";		 ps.adicionarDateTime(dados.getDataCadastro());  

		stSql+= ",ID_USU_BAIXA = ?";		 ps.adicionarLong(dados.getId_UsuarioBaixa());  

		stSql+= ",ID_SERV_USU_BAIXA = ?";		 ps.adicionarLong(dados.getId_ServentiaUsuarioBaixa());  

		stSql+= ",DATA_BAIXA = ?";		 ps.adicionarDateTime(dados.getDataBaixa());  

		stSql += " WHERE ID_PARTES_ISENTAS  = ? "; 		ps.adicionarLong(dados.getId()); 



			executarUpdateDelete(stSql,ps);


	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception { 

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "DELETE FROM PROJUDI.PARTES_ISENTAS";
		stSql += " WHERE ID_PARTES_ISENTAS = ?";		ps.adicionarLong(chave); 



			executarUpdateDelete(stSql,ps);


	} 

//---------------------------------------------------------
	public PartesIsentaDt consultarId(String id_partesisentas )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		PartesIsentaDt Dados=null;


		stSql= "SELECT * FROM PROJUDI.VIEW_PARTES_ISENTAS WHERE ID_PARTES_ISENTAS = ?";		ps.adicionarLong(id_partesisentas); 



		try{

			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new PartesIsentaDt();
				associarDt(Dados, rs1);
			}

		}finally{
			try {if (rs1 != null) rs1.close();} catch (Exception e) {}
		}
			return Dados; 
	}

	protected void associarDt( PartesIsentaDt Dados, ResultSetTJGO rs )  throws Exception {

			Dados.setId(rs.getString("ID_PARTES_ISENTAS"));
			Dados.setNome(rs.getString("NOME"));
			Dados.setCpf( rs.getString("CPF"));
			Dados.setCnpj( rs.getString("CNPJ"));
			Dados.setId_UsuarioCadastrador( rs.getString("ID_USU_CADASTRADOR"));
			Dados.setNomeUsuarioCadastrador( rs.getString("NOME_USU_CADASTRADOR"));
			Dados.setId_ServentiaUsuarioCadastrador( rs.getString("ID_SERV_USU_CADASTRADOR"));
			Dados.setServentiaUsuarioCadastrador( rs.getString("SERV_USU_CADASTRADOR"));
			Dados.setDataCadastro( Funcoes.FormatarDataHora(rs.getString("DATA_CADASTRO")));
			Dados.setId_UsuarioBaixa( rs.getString("ID_USU_BAIXA"));
			Dados.setNomeUsuarioBaixa( rs.getString("NOME_USU_BAIXA"));
			Dados.setId_ServentiaUsuarioBaixa( rs.getString("ID_SERV_USU_BAIXA"));
			Dados.setServentiaUsuarioBaixa( rs.getString("SERV_USU_BAIXA"));
			Dados.setDataBaixa( Funcoes.FormatarDataHora(rs.getString("DATA_BAIXA")));
			Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql,stSqlOrder, stSqlFrom;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "SELECT ID_PARTES_ISENTAS, NOME ";
		stSqlFrom= " FROM PROJUDI.VIEW_PARTES_ISENTAS WHERE NOME LIKE ?";
		stSqlOrder = " ORDER BY NOME ";
		ps.adicionarString(descricao+"%"); 

		try{


			rs1 = consultarPaginacao(stSql + stSqlFrom + stSqlOrder, ps, posicao);


			while (rs1.next()) {
				PartesIsentaDt obTemp = new PartesIsentaDt();
				obTemp.setId(rs1.getString("ID_PARTES_ISENTAS"));
				obTemp.setNome(rs1.getString("NOME"));
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


		stSql= "SELECT ID_PARTES_ISENTAS as id, NOME as descricao1 ";
		stSqlFrom= " FROM PROJUDI.VIEW_PARTES_ISENTAS WHERE NOME LIKE ?";
		ps.adicionarString(descricao+"%"); 

		stSqlOrder= " ORDER BY NOME ";
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
