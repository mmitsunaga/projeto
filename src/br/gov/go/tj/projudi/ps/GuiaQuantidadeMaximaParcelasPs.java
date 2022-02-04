package br.gov.go.tj.projudi.ps;

import java.sql.Connection;

import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class GuiaQuantidadeMaximaParcelasPs extends Persistencia {
	
	private static final long serialVersionUID = 21139577451774404L;
	
	public GuiaQuantidadeMaximaParcelasPs(Connection conexao){
		Conexao = conexao;
	}
	
	public String inserir(String idGuiaReferencia, String quantidadeMaximaParcelas, String motivo, String idUsuario) throws Exception {
		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		stSqlCampos= "INSERT INTO projudi.GUIA_QUANT_MAX_PARCELAS (";
		stSqlValores +=  " Values (";
		
		if(idGuiaReferencia != null && !idGuiaReferencia.isEmpty()) {
			stSqlCampos +=   stVirgula + "ID_GUIA_EMIS " ;
			stSqlValores +=   stVirgula + "? " ;
			ps.adicionarLong(idGuiaReferencia);
			
			stVirgula=",";
		}
		
		if(quantidadeMaximaParcelas != null && !quantidadeMaximaParcelas.isEmpty()) {
			stSqlCampos +=   stVirgula + "QUANT_MAX_PARCELAS " ;
			stSqlValores +=   stVirgula + "? " ;
			ps.adicionarLong(quantidadeMaximaParcelas);
			
			stVirgula=",";
		}
		
		if(motivo != null && !motivo.isEmpty()) {
			stSqlCampos +=   stVirgula + "MOTIVO " ;
			stSqlValores +=   stVirgula + "? " ;
			ps.adicionarString(motivo);
			
			stVirgula=",";
		}
		
		if(motivo != null && !motivo.isEmpty()) {
			stSqlCampos +=   stVirgula + "ID_USU " ;
			stSqlValores +=   stVirgula + "? " ;
			ps.adicionarLong(idUsuario);
			
			stVirgula=",";
		}
		
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores;
		
		return executarInsert(stSql,"ID_GUIA_QUANT_MAX_PARCELAS",ps);
	}
	
	public boolean alterar(String idGuiaQuantidadeMaximaParcela, String idGuiaReferencia, String quantidadeMaximaParcelas, String motivo, String idUsuario) throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String sql = "UPDATE projudi.GUIA_QUANT_MAX_PARCELAS set QUANT_MAX_PARCELAS = ?, MOTIVO = ?, ID_USU = ? WHERE ID_GUIA_QUANT_MAX_PARCELAS = ? AND ID_GUIA_EMIS = ?";
		ps.adicionarLong(quantidadeMaximaParcelas);
		ps.adicionarString(motivo);
		ps.adicionarLong(idUsuario);
		ps.adicionarLong(idGuiaQuantidadeMaximaParcela);
		ps.adicionarLong(idGuiaReferencia);
		
		return executarUpdateDelete(sql, ps) == 1;
	}
	
	public String consultaQuantidadeMaximaParcelamento(String idGuiaReferencia) throws Exception {
		String quantidade = null;
		ResultSetTJGO rs1 = null;
		String sql = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		try {
			sql = "SELECT QUANT_MAX_PARCELAS FROM projudi.GUIA_QUANT_MAX_PARCELAS WHERE ID_GUIA_EMIS = ?";
			ps.adicionarLong(idGuiaReferencia);
			
			rs1 = consultar(sql, ps);
			if(rs1 != null ) {
				if(rs1.next()) {
					quantidade = rs1.getString("QUANT_MAX_PARCELAS");
				}
			}
		}
		finally {
			if( rs1 != null ) rs1.close();
		}
		
		return quantidade;
	}
	
	public String consultaIdQuantidadeMaximaParcelamento(String idGuiaReferencia) throws Exception {
		String idQuantidadeMaximaParcelamento = null;
		ResultSetTJGO rs1 = null;
		String sql = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		try {
			sql = "SELECT ID_GUIA_QUANT_MAX_PARCELAS FROM projudi.GUIA_QUANT_MAX_PARCELAS WHERE ID_GUIA_EMIS = ?";
			ps.adicionarLong(idGuiaReferencia);
			
			rs1 = consultar(sql, ps);
			if(rs1 != null ) {
				if(rs1.next()) {
					idQuantidadeMaximaParcelamento = rs1.getString("ID_GUIA_QUANT_MAX_PARCELAS");
				}
			}
		}
		finally {
			if( rs1 != null ) rs1.close();
		}
		
		return idQuantidadeMaximaParcelamento;
	}

}
