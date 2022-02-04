package br.gov.go.tj.projudi.ps;

import br.gov.go.tj.projudi.dt.SpgMandadoDt;
import java.sql.Connection;
import br.gov.go.tj.utils.PreparedStatementTJGO;

public class SpgMandadoPs extends Persistencia{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8563033202546709995L;
	
	final static String TABELA_CAPITAL =  "V_SPGAMANDADOS";
	final static String TABELA_INTERIOR = "V_SPGAMANDADOS_REM";
	public final static int CAPITAL = 1;
	public final static int INTERIOR = 2;
	private String TABELA=TABELA_CAPITAL;
	
	public SpgMandadoPs(Connection conexao, int tipo) {
		Conexao = conexao;
		if (tipo==SpgMandadoPs.CAPITAL){
			TABELA = TABELA_CAPITAL;
		}else{
			TABELA = TABELA_INTERIOR;
		}
	}
	
	public void inserir(SpgMandadoDt dados ) throws Exception {
				
		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psCidadeinserir()");
		stSqlCampos= "INSERT INTO " + TABELA + " ("; 

		stSqlValores +=  " Values (";
 		 		 
		if ((dados.getComarcaCodigo().length()>0)) {
			 stSqlCampos+=   stVirgula + "CODG_COMARCA " ;
			 stSqlValores+=   stVirgula + "? " ;	 	ps.adicionarString(dados.getComarcaCodigo());  
			stVirgula=",";
		}
		if ((dados.getDataEmissao()!=null)) {
			 stSqlCampos+=   stVirgula + "DATA_EMISSAO " ;
			 stSqlValores+=   stVirgula + "? " ;		 ps.adicionarDate(dados.getDataEmissao());  

			stVirgula=",";
		}
		if ((dados.getRegiaoCodigo().length()>0)) {
			 stSqlCampos+=   stVirgula + "CODG_REGIAO " ;
			 stSqlValores+=   stVirgula + "? " ;	 	ps.adicionarLong(dados.getRegiaoCodigo());  

			stVirgula=",";
		}
		
		if ((dados.getZonaCodigo().length()>0)) {
			 stSqlCampos+=   stVirgula + "CODG_ZONA " ;
			 stSqlValores+=   stVirgula + "? " ;		 ps.adicionarLong(dados.getZonaCodigo());  

			stVirgula=",";
		}
		
		if ((dados.getMandadoNome().length()>0)) {
			 stSqlCampos+=   stVirgula + "NOME_MANDADO " ;
			 stSqlValores+=   stVirgula + "? " ;		 ps.adicionarString(dados.getMandadoNome());  

			stVirgula=",";
		}
		
		if ((dados.getMandadoTipo()>0)) {
			 stSqlCampos+=   stVirgula + "TIPO_MANDADO " ;
			 stSqlValores+=   stVirgula + "? " ;		 ps.adicionarLong(dados.getMandadoTipo());  

			stVirgula=",";
		}

		if ((dados.getTipoConsultaEscrivania()>0)) {
			 stSqlCampos+=   stVirgula + "TIPO_CONSULTA_ESC " ;
			 stSqlValores+=   stVirgula + "? " ;		 ps.adicionarLong(dados.getTipoConsultaEscrivania());  

			stVirgula=",";
		}
		
		if ((dados.getDataRecebimentoCentral()!=null)) {
			 stSqlCampos+=   stVirgula + "DATA_RECEB_CENTRAL " ;
			 stSqlValores+=   stVirgula + "? " ;		 ps.adicionarDate(dados.getDataRecebimentoCentral());  

			stVirgula=",";
		}
		
		if ((dados.getNumeroProcessoProjudi().length()>0)) {
			 stSqlCampos+=   stVirgula + "NUMR_PROCESSO_PROJUDI " ;
			 stSqlValores+=   stVirgula + "? " ;		 ps.adicionarString(dados.getNumeroProcessoProjudi());  

			stVirgula=",";
		}
		
		if ((dados.getEscrivaniaCodigo().length()>0)) {
			 stSqlCampos+=   stVirgula + "CODG_ESCRIVANIA " ;
			 stSqlValores+=   stVirgula + "? " ;		 ps.adicionarLong(dados.getEscrivaniaCodigo());  

			stVirgula=",";
		}
		if ((dados.getMandadoNumero().length()>0)) {
			 stSqlCampos+=   stVirgula + "NUMR_MANDADO " ;
			 stSqlValores+=   stVirgula + "? " ;		 ps.adicionarLong(dados.getMandadoNumero());  

			stVirgula=",";
		}
		if ((dados.getUrgente().length()>0)) {
			 stSqlCampos+=   stVirgula + "STAT_URGENTE " ;
			 stSqlValores+=   stVirgula + "? " ;		 ps.adicionarLong(dados.getUrgente());  

			stVirgula=",";
		}
		
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 

		executarUpdateDelete(stSql,ps); 
	} 	
}
