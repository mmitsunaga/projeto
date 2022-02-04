package br.gov.go.tj.projudi.ps;

import java.util.Date;

import br.gov.go.tj.projudi.dt.CertidaoValidacaoDt;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class CertidaoPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = 6903650516014216355L;

	//---------------------------------------------------------
	public CertidaoPsGen() {


	}



//---------------------------------------------------------
	public void inserir(CertidaoValidacaoDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSqlCampos= "INSERT INTO PROJUDI.CERT ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getCertidao().length >0)) {
			 stSqlCampos+=   stVirgula + "CERT " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarByte(dados.getCertidao());  

			stVirgula=",";
		}
		if ((dados.getDataValidade().length()>0)) {
			 stSqlCampos+=   stVirgula + "DATA_VALIDADE " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarDateTime(Funcoes.BancoDataHora(dados.getDataValidade()).replaceAll("-", ""));  

			stVirgula=",";
		}
		if ((dados.getDataEmissao().length()>0)) {
			 stSqlCampos+=   stVirgula + "DATA_EMISSAO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarDateTime(new Date());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 

		dados.setId(executarInsert(stSql,"ID_CERT",ps));
	} 

//---------------------------------------------------------
	public void alterar(CertidaoValidacaoDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";



		stSql= "UPDATE PROJUDI.CERT SET  ";
		stSql+= "CERT = ?";		 ps.adicionarString(new String(dados.getCertidao()));  

		stSql+= ",DATA_VALIDADE = ?";		 ps.adicionarDateTime(dados.getDataValidade());  

		stSql += " WHERE ID_CERT  = ? "; 		ps.adicionarLong(dados.getId()); 



		executarUpdateDelete(stSql,ps);
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "DELETE FROM PROJUDI.CERT";
		stSql += " WHERE ID_CERT = ?";		ps.adicionarLong(chave); 


		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public CertidaoValidacaoDt consultarId(String id_certidao )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		CertidaoValidacaoDt Dados=null;


		stSql= "SELECT * FROM PROJUDI.VIEW_CERT WHERE ID_CERT = ?";		ps.adicionarLong(id_certidao); 

		try{

			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new CertidaoValidacaoDt();
				associarDt(Dados, rs1);
			}

		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( CertidaoValidacaoDt Dados, ResultSetTJGO rs )  throws Exception {
		Dados.setId(rs.getString("ID_CERT"));
		Dados.setCertidao(rs.getBytes("CERT"));
		Dados.setDataValidade(Funcoes.FormatarDataHora(rs.getDateTime("DATA_VALIDADE")));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
		Dados.setDataEmissao(Funcoes.FormatarDataHora(rs.getDateTime("DATA_EMISSAO")));
	}

//---------------------------------------------------------
	public String consultarDescricaoJSON(String descricao, String posicao ) throws Exception {

		String stSql="";
		String stTemp="";
		int qtdeColunas = 1;
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "SELECT ID_CERT as id, CERT as descricao1 FROM PROJUDI.VIEW_CERT WHERE CERT LIKE ?";
		stSql+= " ORDER BY CERT ";
		ps.adicionarString("%"+descricao+"%"); 

		try{


			rs1 = consultarPaginacao(stSql, ps, posicao);
			stSql= "SELECT Count(*) as Quantidade  FROM PROJUDI.VIEW_CERT WHERE CERT LIKE ?";
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
