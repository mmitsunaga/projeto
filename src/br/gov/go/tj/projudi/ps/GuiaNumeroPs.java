package br.gov.go.tj.projudi.ps;

import java.util.Date;

import br.gov.go.tj.projudi.dt.GuiaNumeroDt;
import br.gov.go.tj.utils.Configuracao;
import java.sql.Connection;
import br.gov.go.tj.utils.PreparedStatementTJGO;

public class GuiaNumeroPs extends Persistencia {

	/**
     * 
     */
    private static final long serialVersionUID = 4498502428214954483L;

    @SuppressWarnings("unused")
	private GuiaNumeroPs( ) {}
    
    public GuiaNumeroPs(Connection conexao){
    	Conexao = conexao;
	}

	public void inserir(GuiaNumeroDt dados ) throws Exception {
    	String SqlCampos;
		String SqlValores;
		String Sql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		SqlCampos = "INSERT INTO projudi.GUIA_NUMERO (";
		SqlValores  = " Values (";
		
		SqlCampos += "LOCALIZADOR " ;
		SqlValores += "?";
		ps.adicionarDateTime(new Date());
		
		if (dados.getCodigoTemp() != null && dados.getCodigoTemp().length()>0) {
			SqlCampos+= ",CODIGO_TEMP ";
			SqlValores+=",?";
			ps.adicionarLong(dados.getCodigoTemp());
		}
		
		SqlCampos+=")";
 		SqlValores+=")"; 		

 		Sql = SqlCampos.replace("(,", "(") + SqlValores.replace("(,", "(");	
		
		dados.setId(executarInsert(Sql, "ID_GUIA_NUMERO", ps));

	}
	
    public void limparTabela() throws Exception{
        String Sql;  
        
        Sql= "DELETE FROM projudi.GUIA_NUMERO";
        Sql = Sql + "  WHERE ID_GUIA_NUMERO IS NOT NULL";

        executarUpdateDeleteSemParametros(Sql);

    }

    public void reiniciarTabela() throws Exception {
        //String Sql;   
        //PreparedStatementTJGO ps =  new PreparedStatementTJGO();
                  
    	//Sql= "ALTER TABLE GuiaNumero AUTO_INCREMENT = ?";
    	//ps.adicionarLong(Configuracao.NUMERO_INICIAL_GUIA);
		//executarUpdateDelete(Sql, ps);
    	super.reiniciarSequenciaNoOracle("GUIANUMERO_ID_GUIANUMERO_SEQ", Configuracao.NUMERO_INICIAL_GUIA);
             
    }

}
