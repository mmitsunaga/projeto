package br.gov.go.tj.projudi.temp;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.CertificadoDt;
import br.gov.go.tj.projudi.ps.Persistencia;
import java.sql.Connection;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class AtualizacaoPs extends Persistencia {

	/**
     * 
     */
    private static final long serialVersionUID = 6117496206367332025L;

    public AtualizacaoPs(Connection conexao){
    	Conexao = conexao;
	}

	/**
     * Consultar dados de todos os arquivos inseridos no Projudi
     * 
     */
	public List consultarArquivosProjudi(long id) throws Exception {
		
		List listObjetos = new ArrayList();
		String Sql;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		Sql = "  ";
		Sql += " (SELECT ID_ARQ, ID_MOVI, PROC_NUMERO_COMPLETO,  DATA_REALIZACAO, NOME_ARQUIVO, CAMINHO,TABELA_ORIGEM ";
		Sql += " FROM VIEW_ATUALIZACAO00 " ;
		Sql += " WHERE ID_ARQ >= ? AND ID_ARQ < ? )  ";
		ps.adicionarLong(id);
		ps.adicionarLong((id + 1500));
		Sql += " UNION ";
	    Sql += " (SELECT ID_ARQ, ID_MOVI, PROC_NUMERO_COMPLETO,  DATA_REALIZACAO, NOME_ARQUIVO, CAMINHO,TABELA_ORIGEM ";
	    Sql += " FROM VIEW_ATUALIZACAO01 " ;
	    Sql += " WHERE ID_ARQ >= ? AND ID_ARQ < ?)  ";
	    ps.adicionarLong(id);
		ps.adicionarLong((id + 1500));
		Sql += " UNION ";
        Sql += " (SELECT ID_ARQ, ID_MOVI, PROC_NUMERO_COMPLETO,  DATA_REALIZACAO, NOME_ARQUIVO, CAMINHO,TABELA_ORIGEM ";
        Sql += " FROM VIEW_ATUALIZACAO02 " ;
        Sql += " WHERE ID_ARQ >= ? AND ID_ARQ < ?)  ";
        ps.adicionarLong(id);
		ps.adicionarLong((id + 1500));
//		Sql += " LIMIT 0, 4500 ";

        ResultSetTJGO rs1 = null;
		try{
			rs1 = consultar(Sql, ps);
			
			while (rs1.next()) {
				AtualizacaoDt atualizacaoDt = new AtualizacaoDt();
				atualizacaoDt.setId(rs1.getString("ID_ARQ"));
				atualizacaoDt.setId_Movimentacao(rs1.getString("ID_MOVI"));
				atualizacaoDt.setProcessoNumeroCompleto(rs1.getString("PROC_NUMERO_COMPLETO"));
				atualizacaoDt.setDataInsercao(rs1.getString("DATA_REALIZACAO"));
				atualizacaoDt.setNomeArquivo(rs1.getString("NOME_ARQUIVO"));
				atualizacaoDt.setCaminho(rs1.getString("CAMINHO"));
				atualizacaoDt.setTabelaOrigem(rs1.getString("TABELA_ORIGEM"));
				listObjetos.add(atualizacaoDt);
			}
			
			//rs1.close();
		
		}
		finally{
			if( rs1 != null )
				rs1.close();
		}
		
		return listObjetos;
	}
	
	public List consultarCertificadosProjudi() throws Exception {
		
		List listObjetos = new ArrayList<CertificadoDt>();
		String Sql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		Sql = "SELECT ID_CERTIFICADO, CAMINHO_TEMP ";
		Sql += " FROM  PROJUDI.CERTIFICADO" ;
		Sql += " WHERE RAIZ = ? AND EMISSOR = ? AND CODIGO_TEMP = ? ";
		ps.adicionarLong(0);
		ps.adicionarLong(0);
		ps.adicionarLong(-1);
		Sql += " AND DATA_REVOGACAO is null ";
		Sql += " AND ROWNUM <= ? ";
		ps.adicionarLong(500);

		try{
			 rs1 = consultar(Sql, ps);
			
			while (rs1.next()) {
				CertificadoDt atualizacaoDt = new CertificadoDt();
				atualizacaoDt.setId(rs1.getString("CERTIFICADO"));
				atualizacaoDt.setCodigoTemp(rs1.getString("CAMINHO_TEMP"));
				listObjetos.add(atualizacaoDt);
			}
			
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        }  
		return listObjetos;
	}
	
	public long consultarQtdTotalCertificados() throws Exception {
		long loTemp = 0;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String Sql = "SELECT COUNT(*) AS QUANTIDADE  FROM PROJUDI.CERTIFICADO";		
		Sql += " WHERE RAIZ = ? AND EMISSOR = ? AND CODIGO_TEMP = ? ";
		ps.adicionarLong(0);
		ps.adicionarLong(0);
		ps.adicionarLong(-1);		
		Sql += " AND DATA_REVOGACAO is null ";
		
		ResultSetTJGO rs1 = consultar(Sql, ps);
		while (rs1.next()) {
			loTemp = rs1.getLong("QUANTIDADE");
		}
		rs1.close();		

		return loTemp;
	}

	   public long consultarIdInicial() throws Exception {
	        long loTemp =0;
	        ResultSetTJGO rs1 = null;
	        PreparedStatementTJGO ps = new PreparedStatementTJGO();
	        String Sql = "SELECT MIN(ID_ARQ) as ID_MENOR  FROM ARQ WHERE CODIGO_TEMP = ? ";
	        ps.adicionarLong(-1);
//	            Sql += "((SELECT ID_ARQ, ID_MOVI, PROC_NUMERO_COMPLETO,  DATA_REALIZACAO, NOME_ARQUIVO, CAMINHO,TABELA_ORIGEM FROM VIEW_ATUALIZACAO00) " ;
//	          Sql += " UNION ";
//	          Sql += "(SELECT ID_ARQ, ID_MOVI, PROC_NUMERO_COMPLETO,  DATA_REALIZACAO, NOME_ARQUIVO, CAMINHO,TABELA_ORIGEM FROM VIEW_ATUALIZACAO01 )) TAB" ;
	        try{
	             rs1 = consultar(Sql, ps);
	        while (rs1.next()) {
	            
	            loTemp = rs1.getLong("MENOR");
	        }
	        //rs1.close();
	        
	        } finally{
	             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
	        }
	        return loTemp;
	    }
	   
	public long consultarQtdTotalArquivos() throws Exception {
		long loTemp=0;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String Sql = "SELECT COUNT(*) AS QUANTIDADE  FROM ARQ WHERE CODIGO_TEMP = ? ";
		ps.adicionarLong(-1);
//	          Sql += "((SELECT ID_ARQ, ID_MOVI, PROC_NUMERO_COMPLETO,  DATA_REALIZACAO, NOME_ARQUIVO, CAMINHO,TABELA_ORIGEM FROM VIEW_ATUALIZACAO00) " ;
//	        Sql += " UNION ";
//	        Sql += "(SELECT ID_ARQ, ID_MOVI, PROC_NUMERO_COMPLETO,  DATA_REALIZACAO, NOME_ARQUIVO, CAMINHO,TABELA_ORIGEM FROM VIEW_ATUALIZACAO01 )) TAB" ;
		
		ResultSetTJGO rs1 = consultar(Sql, ps);
		while (rs1.next()) {
			loTemp = rs1.getLong("QUANTIDADE");
			
		}
		rs1.close();
				
		return loTemp;
	}

	public void migracaoConteudoArquivoP12(CertificadoDt dados) throws Exception {	    
	    PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		String sql = "UPDATE CERTIFICADO set CERTIFICADO = ?  ";	
		if (dados.getConteudo() == null || dados.getConteudo().length == 0) 
			ps.adicionarByteNull();
		else 
			ps.adicionarByte(dados.getConteudo());
		sql += " , CODIGO_TEMP = ? ";
		ps.adicionarLong(dados.getCodigoTemp());			
		sql += " WHERE ID_CERTIFICADO = ?";		
		ps.adicionarLong(dados.getId());

		executarUpdateDelete(sql, ps);
				
	}
	
}
