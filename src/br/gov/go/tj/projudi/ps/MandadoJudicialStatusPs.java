package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.MandadoJudicialStatusDt;
import java.sql.Connection;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

//---------------------------------------------------------
public class MandadoJudicialStatusPs extends MandadoJudicialStatusPsGen{

/**
     * 
     */
    private static final long serialVersionUID = -340680740106918989L;

    @SuppressWarnings("unused")
	private MandadoJudicialStatusPs( ) {}
    
    public MandadoJudicialStatusPs(Connection conexao){
    	Conexao = conexao;
    }

	//
	/**
	 * Consultar todos os status possíveis de mandado judicial.
	 * @param List listaMandadoStatusExcluir
	 * @return List
	 * @throws Exception
	 */
	public List consultarListaStatus(List listaMandadoStatusExcluir) throws Exception {
		List listaStatusMandadoJudicial = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		//A opção de Distribuído deve ser somente na hora que cadastra, então não pode ser disponibilizada para ser escolhida posteriormente
		StringBuffer sql = new StringBuffer("SELECT * FROM PROJUDI.MAND_JUD_STATUS ");
		
		try{
			if( listaMandadoStatusExcluir != null && listaMandadoStatusExcluir.size() > 0 ) {				
				sql.append(" WHERE ID_MAND_JUD_STATUS NOT IN (?");
				ps.adicionarLong(0);
				for(int i = 0; i < listaMandadoStatusExcluir.size(); i++) {
					sql.append(",?");
					ps.adicionarLong(String.valueOf(listaMandadoStatusExcluir.get(i)));
				}
				sql.append(") ");
			}
			sql.append("ORDER BY MAND_JUD_STATUS");
			
			MandadoJudicialStatusDt dados = null;
			rs1 = consultar(sql.toString(), ps);
			while(rs1.next()) {
				if( listaStatusMandadoJudicial == null )
					listaStatusMandadoJudicial = new ArrayList();
				
				dados = new MandadoJudicialStatusDt();
				dados.setId(rs1.getString("ID_MAND_JUD_STATUS"));
				dados.setMandadoJudicialStatus(rs1.getString("MAND_JUD_STATUS"));
				
				listaStatusMandadoJudicial.add(dados);
			}
			
		}finally{
			 rs1.close();
		}
		
		return listaStatusMandadoJudicial;
	}
	
	public List consultarListaStatus() throws Exception {
		List listaStatusMandadoJudicial = new ArrayList();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		StringBuffer sql = new StringBuffer();
		
		//A opçoões de Distribuído e Redistribuído não podem ser disponibilizadas para opcoes de alteração.
		
		try{

			sql.append("SELECT * FROM PROJUDI.MAND_JUD_STATUS"
					+ "  WHERE MAND_JUD_STATUS_CODIGO NOT IN (?,?)");
			
			ps.adicionarLong(MandadoJudicialStatusDt.DISTRIBUIDO);
			ps.adicionarLong(MandadoJudicialStatusDt.REDISTRIBUIDO);
			sql.append(" ORDER BY MAND_JUD_STATUS");
			
			MandadoJudicialStatusDt dados = null;
			rs1 = consultar(sql.toString(), ps);
			while(rs1.next()) {				
				dados = new MandadoJudicialStatusDt();
				dados.setId(rs1.getString("ID_MAND_JUD_STATUS"));
				dados.setMandadoJudicialStatus(rs1.getString("MAND_JUD_STATUS"));				
				listaStatusMandadoJudicial.add(dados);
			}
			
		}finally{
			 rs1.close();
		}
		
		return listaStatusMandadoJudicial;
	}
	
	
	public String consultarCodigo(String codigo)  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String dados = "";

		stSql= "SELECT ID_MAND_JUD_STATUS FROM MAND_JUD_STATUS WHERE MAND_JUD_STATUS_CODIGO = ?";		ps.adicionarLong(codigo); 

		try{
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				dados = rs1.getString("ID_MAND_JUD_STATUS");
			}
		} finally {
			try {if (rs1 != null) rs1.close();} catch (Exception e) {}
		}
		return dados; 
	}
}
