package br.gov.go.tj.projudi.ps;

import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

import java.util.List;
import java.util.ArrayList;

import br.gov.go.tj.projudi.dt.TemaDt;


public class TemaPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = -8230798882520635302L;

	//---------------------------------------------------------
	public TemaPsGen() {


	}



//---------------------------------------------------------
	public void inserir(TemaDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSqlCampos= "INSERT INTO PROJUDI.TEMA ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getTitulo().length()>0)) {
			 stSqlCampos+=   stVirgula + "TITULO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getTitulo());  

			stVirgula=",";
		}
		if ((dados.getTemaCodigo().length()>0)) {
			 stSqlCampos+=   stVirgula + "TEMA_CODIGO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getTemaCodigo());  

			stVirgula=",";
		}
		if ((dados.getQuesDireito().length()>0)) {
			 stSqlCampos+=   stVirgula + "QUES_DIREITO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getQuesDireito());  

			stVirgula=",";
		}
		if ((dados.getVinculantes().length()>0)) {
			 stSqlCampos+=   stVirgula + "VINCULANTES " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getVinculantes());  

			stVirgula=",";
		}
		if ((dados.getId_TemaSituacao().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_TEMA_SITUACAO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_TemaSituacao());  

			stVirgula=",";
		}
		if ((dados.getId_TemaTipo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_TEMA_TIPO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_TemaTipo());  

			stVirgula=",";
		}
		if ((dados.getId_TemaOrigem().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_TEMA_ORIGEM " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getId_TemaOrigem());  

			stVirgula=",";
		}
		if ((dados.getDataTransito().length()>0)) {
			 stSqlCampos+=   stVirgula + "DATA_TRANSITO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarDate(dados.getDataTransito());
			 stVirgula=",";
		}
		
		if (dados.getDataAdmissao().length()>0){
			stSqlCampos+=   stVirgula + "DATA_ADMISSAO " ;
			stSqlValores+=   stVirgula + "? " ;
			ps.adicionarDate(dados.getDataAdmissao());
			stVirgula=",";
		}
		
		if (dados.getInfoLegislativa().length()>0){
			stSqlCampos+=   stVirgula + "INFO_LEGISLATIVA " ;
			stSqlValores+=   stVirgula + "? " ;
			ps.adicionarString(dados.getInfoLegislativa());
			stVirgula=",";
		}
			
		if (dados.getNumeroIrdrCnj().length()>0){
			stSqlCampos+=   stVirgula + "NUMR_IRDR_CNJ " ;
			stSqlValores+=   stVirgula + "? " ;
			ps.adicionarString(dados.getNumeroIrdrCnj());
			stVirgula=",";
		}
		
		if (dados.getTeseFirmada().length()>0){
			stSqlCampos+=   stVirgula + "TESE_FIRMADA " ;
			stSqlValores+=   stVirgula + "? " ;
			ps.adicionarString(dados.getTeseFirmada());
		}
		
    	if (dados.getOpcaoProcessual().length()>=0){
        	stSqlCampos+=   stVirgula + "INFO_PROCESSUAL " ;
			stSqlValores+=   stVirgula + "? " ;
			ps.adicionarString(dados.getOpcaoProcessual());
		}
		
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 

		dados.setId(executarInsert(stSql,"ID_TEMA",ps));
	} 

//---------------------------------------------------------
	public void alterar(TemaDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		stSql= "UPDATE PROJUDI.TEMA SET  ";
		stSql+= "TITULO = ?";		 ps.adicionarString(dados.getTitulo());  

		stSql+= ",TEMA_CODIGO = ?";		 ps.adicionarLong(dados.getTemaCodigo());  

		stSql+= ",QUES_DIREITO = ?";		 ps.adicionarString(dados.getQuesDireito());  

		stSql+= ",VINCULANTES = ?";		 ps.adicionarString(dados.getVinculantes());  

		stSql+= ",ID_TEMA_SITUACAO = ?";		 ps.adicionarLong(dados.getId_TemaSituacao());  

		stSql+= ",ID_TEMA_TIPO = ?";		 ps.adicionarLong(dados.getId_TemaTipo());  

		stSql+= ",ID_TEMA_ORIGEM = ?";		 ps.adicionarString(dados.getId_TemaOrigem());  
		
		stSql+= ",DATA_TRANSITO = ?";		 ps.adicionarDate(dados.getDataTransito());
		
		stSql+= ",DATA_ADMISSAO = ?";		 ps.adicionarDate(dados.getDataAdmissao());
		
		stSql+= ",TESE_FIRMADA = ?";		 ps.adicionarString(dados.getTeseFirmada());
		
		stSql+= ",INFO_PROCESSUAL = ?";		 ps.adicionarString(dados.getOpcaoProcessual());
		
		stSql+= ",INFO_LEGISLATIVA = ?";	ps.adicionarString(dados.getInfoLegislativa());
		
		stSql+= ",NUMR_IRDR_CNJ = ?";	ps.adicionarString(dados.getNumeroIrdrCnj());
		
		stSql += " WHERE ID_TEMA  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "DELETE FROM PROJUDI.TEMA";
		stSql += " WHERE ID_TEMA = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public TemaDt consultarId(String id_tema )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		TemaDt Dados=null;


		stSql= "SELECT * FROM PROJUDI.VIEW_TEMA WHERE ID_TEMA = ?";		ps.adicionarLong(id_tema); 



		try{

			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new TemaDt();
				associarDt(Dados, rs1);
			}

		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return Dados; 
	}

	protected void associarDt( TemaDt Dados, ResultSetTJGO rs )  throws Exception {
		Dados.setId(rs.getString("ID_TEMA"));
		Dados.setTitulo(rs.getString("TITULO"));
		Dados.setTemaCodigo( rs.getString("TEMA_CODIGO"));
		Dados.setQuesDireito( rs.getString("QUES_DIREITO"));
		Dados.setVinculantes( rs.getString("VINCULANTES"));
		Dados.setId_TemaSituacao( rs.getString("ID_TEMA_SITUACAO"));
		Dados.setId_TemaOrigem( rs.getString("ID_TEMA_ORIGEM"));
		Dados.setId_TemaTipo( rs.getString("ID_TEMA_TIPO"));
		Dados.setTemaSituacao( rs.getString("TEMA_SITUACAO"));
		Dados.setTemaSituacaoCnj( rs.getString("TEMA_SITUACAO_CNJ"));
		Dados.setTemaOrigem( rs.getString("TEMA_ORIGEM"));
		Dados.setTemaTipo( rs.getString("TEMA_TIPO"));
		Dados.setTemaTipoCnj( rs.getString("TEMA_TIPO_CNJ"));
		Dados.setDataTransito(Funcoes.FormatarData(rs.getDateTime("DATA_TRANSITO")));
		Dados.setNumeroIrdrCnj(rs.getString("NUMR_IRDR_CNJ"));
		Dados.setTeseFirmada(rs.getString("TESE_FIRMADA"));
 		Dados.setOpcaoProcessual(rs.getString("INFO_PROCESSUAL"));
		Dados.setInfoLegislativa(rs.getString("INFO_LEGISLATIVA"));		
		Dados.setDataAdmissao(Funcoes.FormatarData(rs.getDateTime("DATA_ADMISSAO")));
		Dados.setSuspensao(rs.getString("INFO_SUSPENSAO"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "SELECT ID_TEMA, TITULO FROM PROJUDI.VIEW_TEMA WHERE TITULO LIKE ?";
		stSql+= " ORDER BY TITULO ";
		ps.adicionarString("%"+descricao+"%"); 

		try{


			rs1 = consultarPaginacao(stSql, ps, posicao);


			while (rs1.next()) {
				TemaDt obTemp = new TemaDt();
				obTemp.setId(rs1.getString("ID_TEMA"));
				obTemp.setTitulo(rs1.getString("TITULO"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT Count(*) as Quantidade  FROM PROJUDI.VIEW_TEMA WHERE TITULO LIKE ?";
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
		int qtdeColunas = 1;
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "SELECT ID_TEMA as id, TEMA_CODIGO || ' - ' || TITULO as descricao1 FROM PROJUDI.VIEW_TEMA WHERE TITULO LIKE ?";
		stSql+= " ORDER BY TITULO ";
		ps.adicionarString("%"+descricao+"%"); 

		try{


			rs1 = consultarPaginacao(stSql, ps, posicao);
			stSql= "SELECT Count(*) as Quantidade  FROM PROJUDI.VIEW_TEMA WHERE TITULO LIKE ?";
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
