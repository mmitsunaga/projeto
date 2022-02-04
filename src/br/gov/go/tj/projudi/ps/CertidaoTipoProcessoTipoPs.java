package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.CertidaoTipoProcessoTipoDt;
import java.sql.Connection;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

//---------------------------------------------------------
public class CertidaoTipoProcessoTipoPs extends CertidaoTipoProcessoTipoPsGen{

//
	
	/**
     * 
     */
    private static final long serialVersionUID = 1181994462441285108L;

    public CertidaoTipoProcessoTipoPs(Connection conexao){
    	Conexao = conexao;
	}

	//---------------------------------------------------------
	/*
     Este método foi criado com o intuito de retornar uma lista ordenada pelo atributo ProcessoTipo     
	 @Autor Márcio Gomes
	 @Data 27/08/2010
    */
	public List consultarProcessoTipoCertidaoTipoGeral(String id_certidaotipo ) throws Exception {

		String Sql;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		Sql= "SELECT t2.ID_CERT_TIPO_PROC_TIPO, t1.ID_PROC_TIPO, t1.PROC_TIPO, t1.ID_CNJ_CLASSE, t3.ID_CERT_TIPO, t3.CERT_TIPO";
		Sql+= " FROM PROJUDI.PROC_TIPO t1 ";
		Sql+= " LEFT JOIN PROJUDI.CERT_TIPO_PROC_TIPO t2 ON t1.ID_PROC_TIPO = t2.ID_PROC_TIPO AND t2.ID_CERT_TIPO = ?";		ps.adicionarLong(id_certidaotipo);
		Sql+= " LEFT JOIN PROJUDI.CERT_TIPO t3 ON t3.ID_CERT_TIPO = t2.ID_CERT_TIPO";
		Sql+= " WHERE t1.ATIVO = ? ";																					ps.adicionarLong(1);
		Sql+= " ORDER BY t1.PROC_TIPO";
		try{
			
			rs1 = consultar(Sql, ps);
			
			while (rs1.next()) {
				CertidaoTipoProcessoTipoDt obTemp = new CertidaoTipoProcessoTipoDt();
				obTemp.setId(rs1.getString("ID_CERT_TIPO_PROC_TIPO"));
				obTemp.setCertidaoTipo(rs1.getString("CERT_TIPO"));
				obTemp.setId_ProcessoTipo (rs1.getString("ID_PROC_TIPO"));
				obTemp.setProcessoTipo(rs1.getString("PROC_TIPO"));
				obTemp.setIdProcessoTipoCNJClasse(rs1.getString("ID_CNJ_CLASSE"));
				obTemp.setId_CertidaoTipo (id_certidaotipo);
				obTemp.setCertidaoTipo(rs1.getString("CERT_TIPO"));
				liTemp.add(obTemp);
			}			
		}finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        }  
		return liTemp; 
	}
	
	
}
