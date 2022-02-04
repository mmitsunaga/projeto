package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.ResultadoConsultaDt;
import java.sql.Connection;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class AdministrarPs extends Persistencia{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5691673526026731084L;
	
	
	public AdministrarPs(Connection conexao){
		Conexao = conexao;
	}
	
	public int executarComando(String sql)throws Exception {
		int loRetorno=-1;
		
		loRetorno=super.executarComando(sql);

		return loRetorno;		
	}
	
	public int[] executarComandos(String sql)throws Exception {
		int loRetorno[];
		
		loRetorno = super.executarComandos(sql);
			    
		return loRetorno;		
	}

	/**
	 * Método que executa um comando de consulta SQL.
	 * 
	 * @param sql
	 * @return
	 * @throws Exception
	 * @author mmgomes
	 */
	public ResultadoConsultaDt executarConsulta(String sql) throws Exception {		
		ResultadoConsultaDt retorno = new ResultadoConsultaDt();		
		ResultSetTJGO rs1 = null;
		boolean primeiraLinha = true;
		int quantidadeDeColunas = 0;
		try{
			rs1 = consultar(sql, new PreparedStatementTJGO());
			while (rs1.next()) {
				if (primeiraLinha) {	
					quantidadeDeColunas = rs1.getColumnCount();					
					for(int i = 1; i <= quantidadeDeColunas; i++)
						retorno.setNomeCampo(rs1.getColumnName(i));					
					primeiraLinha = false;
				}
				
				List<String> listaDeCampos = new ArrayList<String>();				
				
				for(int i = 1; i <= quantidadeDeColunas; i++)				
					listaDeCampos.add(rs1.getString(i));
				
				retorno.setValoresCampo(listaDeCampos);
			}
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}             
        }		
		
		return retorno;
	}
	

}
