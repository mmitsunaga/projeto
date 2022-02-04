package br.gov.go.tj.projudi.ps;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;

import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.ResultSetTJGO;

//---------------------------------------------------------
public class ProcessoNumeroPs extends ProcessoNumeroPsGen{

    /**
     * 
     */
    private static final long serialVersionUID = -1942035551107934506L;

    public ProcessoNumeroPs(Connection conexao){
    	Conexao = conexao;
	}

	
	public String pegarProximoNumero() throws Exception{
		String stSql="SELECT SEQ_PROCESSO_NUMERO.NEXTVAL numero  FROM DUAL";
		String stNumero ="";
		
		ResultSetTJGO rs1 = null;
		
		try{
			////System.out.println("..ps-ConsultaId_ProcessoNumero  " + stSql);
			rs1 = consultarSemParametros(stSql);
			if (rs1.next()) {
				stNumero= rs1.getString("numero");				
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}			
		
		return stNumero;
	}
	
    public void reiniciarSequencia() throws Exception {
        String Sql;        
        ResultSetTJGO rs1 = null;
        long loAnoBanco = -1;
        
        /*Calendar cal = Calendar.getInstance();
        
        if (cal.get(com.ibm.icu.util.Calendar.DAY_OF_YEAR)!=1) {
        	throw new MessageException("A reincialização da sequência dos número de processo só é possível no primeiro dia do ano.");
        }*/
        
        Sql = "SELECT MAX(P.ANO) as ANO FROM PROC p";
        try{   
            rs1 = consultarSemParametros(Sql);
    
            if (rs1.next())             
                loAnoBanco = rs1.getLong("ANO");
            
            SimpleDateFormat FormatoData = new SimpleDateFormat("yyyy");
            long loAnoSistema = Funcoes.StringToLong(FormatoData.format(new Date()));
            
            if (loAnoBanco!=-1 && loAnoBanco!=loAnoSistema){
                //Sql= "ALTER TABLE ProcessoNumero AUTO_INCREMENT =" +  Configuracao.NUMERO_INICIAL_PROCESSO;                       
                
                //executar(Sql);            	
            	super.reiniciarSequenciaNoOracle("SEQ_PROCESSO_NUMERO", Configuracao.NUMERO_INICIAL_PROCESSO);
            }

        } finally {
                try{if (rs1 != null) rs1.close();} catch(Exception e1) {}               
         }            
    }

}
