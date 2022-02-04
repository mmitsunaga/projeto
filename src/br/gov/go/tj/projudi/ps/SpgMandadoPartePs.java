package br.gov.go.tj.projudi.ps;

import br.gov.go.tj.projudi.dt.SpgMandadoParteDt;
import java.sql.Connection;
import br.gov.go.tj.utils.PreparedStatementTJGO;

public class SpgMandadoPartePs extends Persistencia{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8563033202546709995L;
	
	final static String TABELA_CAPITAL =  "V_SPGAPARTESMANDADO";
	final static String TABELA_INTERIOR = "V_SPGAPARTESMANDADOS_REM";
	public final static int CAPITAL = 1;
	public final static int INTERIOR = 2;
	private String TABELA=TABELA_CAPITAL;
	
	public SpgMandadoPartePs(Connection conexao, int tipo) {
		Conexao = conexao;
		if (tipo==SpgMandadoPartePs.CAPITAL){
			TABELA = TABELA_CAPITAL;
		}else{
			TABELA = TABELA_INTERIOR;
		}
	}
	
	public void inserir(SpgMandadoParteDt dados ) throws Exception {
				
		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psCidadeinserir()");
		stSqlCampos= "INSERT INTO " + TABELA + " ("; 

		stSqlValores +=  " Values (";
 		 		 
		if ((dados.getNomeParte().length()>0)) {
			 stSqlCampos+=   stVirgula + "NOME_PARTE " ;
			 stSqlValores+=   stVirgula + "? " ;	 	ps.adicionarString(dados.getNomeParte());  
			stVirgula=",";
		}
		if ((dados.getRua()!=null)) {
			 stSqlCampos+=   stVirgula + "INFO_RUA " ;
			 stSqlValores+=   stVirgula + "? " ;		 ps.adicionarString(dados.getRua());  

			stVirgula=",";
		}
		if ((dados.getNumero().length()>0)) {
			 stSqlCampos+=   stVirgula + "INFO_NUMERO " ;
			 stSqlValores+=   stVirgula + "? " ;	 	ps.adicionarLong(dados.getNumero());  

			stVirgula=",";
		}
		
		if ((dados.getLote().length()>0)) {
			 stSqlCampos+=   stVirgula + "INFO_LOTE " ;
			 stSqlValores+=   stVirgula + "? " ;		 ps.adicionarString(dados.getLote());  

			stVirgula=",";
		}
		
		if ((dados.getQuadra().length()>0)) {
			 stSqlCampos+=   stVirgula + "INFO_QUADRA " ;
			 stSqlValores+=   stVirgula + "? " ;		 ps.adicionarString(dados.getQuadra());  

			stVirgula=",";
		}
		
		if ((dados.getBairroCodigo().length()>0)) {
			 stSqlCampos+=   stVirgula + "CODG_BAIRRO " ;
			 stSqlValores+=   stVirgula + "? " ;		 ps.adicionarLong(dados.getBairroCodigo());  

			stVirgula=",";
		}

		if ((dados.getMunicipioCodigo().length()>0)) {
			 stSqlCampos+=   stVirgula + "CODG_MUNICIPIO " ;
			 stSqlValores+=   stVirgula + "? " ;		 ps.adicionarLong(dados.getMunicipioCodigo());  

			stVirgula=",";
		}
		
		if ((dados.getComplemento().length()>0)) {
			 stSqlCampos+=   stVirgula + "INFO_COMPLEMENTO " ;
			 stSqlValores+=   stVirgula + "? " ;		 ps.adicionarString(dados.getComplemento());  

			stVirgula=",";
		}
				
		if ((dados.getCep().length()>0)) {
			 stSqlCampos+=   stVirgula + "NUMR_CEP " ;
			 stSqlValores+=   stVirgula + "? " ;		 ps.adicionarLong(dados.getCep());  

			stVirgula=",";
		}
		
		if ((dados.getComplementar().length()>0)) {
			 stSqlCampos+=   stVirgula + "INFO_COMPLEMENTAR " ;
			 stSqlValores+=   stVirgula + "? " ;		 ps.adicionarString(dados.getComplementar());  

			stVirgula=",";
		}
		
		if ((dados.getParteTipo().length()>0)) {
			 stSqlCampos+=   stVirgula + "TIPO_PARTE " ;
			 stSqlValores+=   stVirgula + "? " ;		 ps.adicionarString(dados.getParteTipo());  

			stVirgula=",";
		}
		if ((dados.getNumeroOab().length()>0)) {
			 stSqlCampos+=   stVirgula + "NUMR_OAB " ;
			 stSqlValores+=   stVirgula + "? " ;		 ps.adicionarString(dados.getNumeroOab());  

			stVirgula=",";
		}
		
		if ((dados.getNumeroProcessoProjudi().length()>0)) {
			 stSqlCampos+=   stVirgula + "NUMR_PROCESSO_PROJUDI " ;
			 stSqlValores+=   stVirgula + "? " ;		 ps.adicionarString(dados.getNumeroProcessoProjudi());  

			stVirgula=",";
		}
		if ((dados.getMandadoNumero().length()>0)) {
			 stSqlCampos+=   stVirgula + "NUMR_MANDADO " ;
			 stSqlValores+=   stVirgula + "? " ;		 ps.adicionarLong(dados.getMandadoNumero());  

			stVirgula=",";
		}
		
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		
		executarUpdateDelete(stSql, ps); 
	} 
	
}
