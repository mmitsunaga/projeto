package br.gov.go.tj.projudi.ps;

import br.gov.go.tj.projudi.dt.ServentiaSubtipoDt;
import br.gov.go.tj.projudi.dt.ServentiaTipoDt;
import java.sql.Connection;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class GuiaRecursoPs extends Persistencia {

	private static final long serialVersionUID = -2490261547084059097L;
	
	public GuiaRecursoPs(Connection conexao){
		Conexao = conexao;
	}

	/**
	 * Método para validar se advogado pode emitir esta guia.
	 * Caso o processo esteja em uma serventia de juizado especial, não pode emitir esta guia, somente pode de 
	 * recurto inominado.
	 * @param String idServentia
	 * @return boolean
	 * @throws Exception
	 */
	public boolean validaAcessoEmissaoGuia(String idServentia) throws Exception {
		boolean retorno = false;
		
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		ResultSetTJGO rs1 = null;
		try{
			String sql = "SELECT COUNT(ID_SERV) AS QUANTIDADE FROM PROJUDI.SERV WHERE ID_SERV_TIPO = ? AND ID_SERV_SUBTIPO IN (SELECT ID_SERV_SUBTIPO FROM PROJUDI.SERV_SUBTIPO WHERE SERV_SUBTIPO_CODIGO IN (?,?,?,?,?,?,?)) AND ID_SERV = ?";
			ps.adicionarLong(ServentiaTipoDt.VARA);
			ps.adicionarLong(ServentiaSubtipoDt.JUIZADO_ESPECIAL_CIVEL);			
			ps.adicionarLong(ServentiaSubtipoDt.JUIZADO_ESPECIAL_CRIMINAL);
			ps.adicionarLong(ServentiaSubtipoDt.JUIZADO_ESPECIAL_CIVEL_CRIMINAL);
			ps.adicionarLong(ServentiaSubtipoDt.JUIZADO_VIOLENCIA_DOMESTICA);
			ps.adicionarLong(ServentiaSubtipoDt.JUIZADO_ESPECIAL_CRIMINAL_FAZENDA_PUBLICA);
			ps.adicionarLong(ServentiaSubtipoDt.JUIZADO_ESPECIAL_FAZENDA_PUBLICA);
			ps.adicionarLong(ServentiaSubtipoDt.UPJ_JUIZADO_ESPECIAL_FAZENDA_PUBLICA);
			ps.adicionarLong(idServentia);
			    
			rs1 = consultar(sql.toString(), ps);
			
			if( rs1 != null ) {
				while (rs1.next()) {
					if( rs1.getInt("QUANTIDADE") == 0 ) {
						retorno = true;
					}
				}
			}
		
		}
		finally{
			 rs1.close();
		}
		
		return retorno;
	}
}
