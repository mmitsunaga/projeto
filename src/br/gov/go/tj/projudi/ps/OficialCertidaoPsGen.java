package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.OficialCertidaoDt;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class OficialCertidaoPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = 987038398189478987L;

	//---------------------------------------------------------
	public OficialCertidaoPsGen() {


	}



//---------------------------------------------------------
	public void inserir(OficialCertidaoDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSqlCampos= "INSERT INTO PROJUDI.OFICIAL_CERT ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getCertidaoNome().length()>0)) {
			 stSqlCampos+=   stVirgula + "CERT_NOME " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getCertidaoNome());  

			stVirgula=",";
		}
		if ((dados.getId_UsuarioServentia().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_USU_SERV " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_UsuarioServentia());  

			stVirgula=",";
		}
		if ((dados.getStatus().length()>0)) {
			 stSqlCampos+=   stVirgula + "STATUS " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getStatus());  

			stVirgula=",";
		}
		if ((dados.getNumeroMandado().length()>0)) {
			 stSqlCampos+=   stVirgula + "NUM_MANDADO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getNumeroMandado());  

			stVirgula=",";
		}
		if ((dados.getDataEmissao().length()>0)) {
			 stSqlCampos+=   stVirgula + "DATA_EMISSAO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarDateTime(dados.getDataEmissao());  

			stVirgula=",";
		}
		if ((dados.getTexto().length()>0)) {
			 stSqlCampos+=   stVirgula + "TEXTO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getTexto());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 

		dados.setId(executarInsert(stSql,"ID_OFICIAL_CERT",ps));
	} 

//---------------------------------------------------------
	public void alterar(OficialCertidaoDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";



		stSql= "UPDATE PROJUDI.OFICIAL_CERT SET  ";
		stSql+= "CERT_NOME = ?";		 ps.adicionarString(dados.getCertidaoNome());  

		stSql+= ",ID_USU_SERV = ?";		 ps.adicionarLong(dados.getId_UsuarioServentia());  

		stSql+= ",STATUS = ?";		 ps.adicionarString(dados.getStatus());  

		stSql+= ",NUM_MANDADO = ?";		 ps.adicionarString(dados.getNumeroMandado());  

		stSql+= ",DATA_EMISSAO = ?";		 ps.adicionarDateTime(dados.getDataEmissao());  

		stSql+= ",TEXTO = ?";		 ps.adicionarString(dados.getTexto());  

		stSql += " WHERE ID_OFICIAL_CERT  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "DELETE FROM PROJUDI.OFICIAL_CERT";
		stSql += " WHERE ID_OFICIAL_CERT = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);
	} 

//---------------------------------------------------------
	public OficialCertidaoDt consultarId(String id_oficialcertidao )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		OficialCertidaoDt Dados=null;


		stSql= "SELECT * FROM PROJUDI.VIEW_OFICIAL_CERT WHERE ID_OFICIAL_CERT = ?";		ps.adicionarLong(id_oficialcertidao); 



		try{

			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new OficialCertidaoDt();
				associarDt(Dados, rs1);
			}

		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return Dados; 
	}

	protected void associarDt( OficialCertidaoDt Dados, ResultSetTJGO rs )  throws Exception {
		Dados.setId(rs.getString("ID_OFICIAL_CERT"));
		Dados.setCertidaoNome(rs.getString("CERT_NOME"));
		Dados.setId_UsuarioServentia( rs.getString("ID_USU_SERV"));
		Dados.setNome( rs.getString("NOME"));
		Dados.setStatus( rs.getString("STATUS"));
		Dados.setNumeroMandado( rs.getString("NUM_MANDADO"));
		Dados.setDataEmissao( Funcoes.FormatarDataHora(rs.getDateTime("DATA_EMISSAO")));
		Dados.setTexto( rs.getString("TEXTO"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
		Dados.setGrupo( rs.getString("GRUPO"));
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "SELECT ID_OFICIAL_CERT, CERT_NOME FROM PROJUDI.VIEW_OFICIAL_CERT WHERE CERT_NOME LIKE ?"; ps.adicionarString(descricao+"%");
		stSql+= " ORDER BY CERT_NOME ";
		 

		try{


			rs1 = consultarPaginacao(stSql, ps, posicao);


			while (rs1.next()) {
				OficialCertidaoDt obTemp = new OficialCertidaoDt();
				obTemp.setId(rs1.getString("ID_OFICIAL_CERT"));
				obTemp.setCertidaoNome(rs1.getString("CERT_NOME"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT Count(*) as Quantidade  FROM PROJUDI.VIEW_OFICIAL_CERT WHERE CERT_NOME LIKE ?";
			rs2 = consultar(stSql,ps);

			while (rs2.next()) {
				liTemp.add(rs2.getLong("Quantidade"));
			}

		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return liTemp; 
	}

//---------------------------------------------------------
	public String consultarDescricaoJSON(String descricao, String posicao ) throws Exception {

		String stSql="";
		String stTemp="";
		int qtdeColunas = 2;
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "SELECT ID_OFICIAL_CERT as id, NUM_MANDADO as descricao1, DATA_EMISSAO as descricao2 FROM PROJUDI.VIEW_OFICIAL_CERT WHERE NUM_MANDADO LIKE ?";
		stSql+= " ORDER BY NUM_MANDADO ";
		ps.adicionarString("%"+descricao+"%"); 

		try{


			rs1 = consultarPaginacao(stSql, ps, posicao);
			stSql= "SELECT Count(*) as Quantidade  FROM PROJUDI.VIEW_OFICIAL_CERT WHERE NUM_MANDADO LIKE ?";
			rs2 = consultar(stSql,ps);
			rs2.next();
			stTemp = gerarJSONOficialCertidao(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);

		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return stTemp; 
	}

	public String gerarJSONOficialCertidao( long qtdPaginas, String posicaoAtual, ResultSetTJGO rs, int qtdeColunas) throws Exception{
		StringBuilder stTemp = new StringBuilder();
		stTemp.append("[");
		stTemp.append("{\"id\":\"-50000\",\"desc1\":\"").append(qtdPaginas);
		for (int i = 2; i <= qtdeColunas; i++) {
			stTemp.append("\",\"desc" + i + "\":\"");
		}
		stTemp.append("\"}");
		stTemp.append(",{\"id\":\"-60000\",\"desc1\":\"").append(posicaoAtual);
		for (int i = 2; i <= qtdeColunas; i++) {
			stTemp.append("\",\"desc" + i + "\":\"");
		}
		stTemp.append("\"}");
		while (rs.next()){			
			stTemp.append(",{\"id\":\"").append(rs.getString("Id")).append("\",\"desc1\":\"").append(rs.getString("descricao1"));
		//	for (int i = 2; i <= qtdeColunas; i++) {
				stTemp.append("\",\"desc1" + "\":\"").append(rs.getString("descricao1"));
				stTemp.append("\",\"desc2\":\"").append(Funcoes.FormatarData(rs.getDateTime("descricao2")));
		//	}
			stTemp.append("\"}");
		}
		stTemp.append("]");
		return stTemp.toString();
	}
	
} 
