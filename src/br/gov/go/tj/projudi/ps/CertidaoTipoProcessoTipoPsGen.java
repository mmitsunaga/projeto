package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.CertidaoTipoProcessoTipoDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class CertidaoTipoProcessoTipoPsGen extends Persistencia {


/**
     * 
     */
    private static final long serialVersionUID = 7969605308664522098L;

    //---------------------------------------------------------
	public CertidaoTipoProcessoTipoPsGen() {

	}



//---------------------------------------------------------
	public void inserir(CertidaoTipoProcessoTipoDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		//////System.out.println("....psCertidaoTipoProcessoTipoinserir()");
		stSqlCampos= "INSERT INTO PROJUDI.CERT_TIPO_PROC_TIPO ("; 
		stSqlValores +=  " Values (";
		if ((dados.getId_CertidaoTipo().length()>0)){
			stSqlCampos+=   stVirgula + "ID_CERT_TIPO " ;
			stSqlValores+=   stVirgula + "? " ;
			ps.adicionarLong(dados.getId_CertidaoTipo());
			
			stVirgula=",";
		}
		if ((dados.getId_ProcessoTipo().length()>0)){
			stSqlCampos+=   stVirgula + "ID_PROC_TIPO " ;
			stSqlValores+=   stVirgula + "? " ;
			ps.adicionarLong(dados.getId_ProcessoTipo());
			
			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		dados.setId(executarInsert(stSql,"ID_CERT_TIPO_PROC_TIPO",ps));
	} 

//---------------------------------------------------------
	public void alterar(CertidaoTipoProcessoTipoDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		//////System.out.println("....psCertidaoTipoProcessoTipoalterar()");

		stSql= "UPDATE PROJUDI.CERT_TIPO_PROC_TIPO SET  ";
		stSql+= "ID_CERT_TIPO  = ? "; ps.adicionarLong(dados.getId_CertidaoTipo()); 
		stSql+= ",ID_PROC_TIPO  = ? "; ps.adicionarLong(dados.getId_ProcessoTipo()); 
		stSql+= " WHERE ID_CERT_TIPO_PROC_TIPO  = ? "; ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		//////System.out.println("....psCertidaoTipoProcessoTipoexcluir()");

		stSql= "DELETE FROM PROJUDI.CERT_TIPO_PROC_TIPO";
		stSql +="  WHERE ID_CERT_TIPO_PROC_TIPO = ? "; ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public CertidaoTipoProcessoTipoDt consultarId(String id_certidaotipoprocessotipo )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		CertidaoTipoProcessoTipoDt Dados=null;
		//////System.out.println("....ps-ConsultaId_CertidaoTipoProcessoTipo)");

		stSql= "SELECT * FROM PROJUDI.VIEW_CERT_TIPO_PROC_TIPO WHERE ID_CERT_TIPO_PROC_TIPO = ? "; ps.adicionarLong(id_certidaotipoprocessotipo);
		//////System.out.println("....Sql  " + Sql  );

		try{
			//////System.out.println("..ps-ConsultaId_CertidaoTipoProcessoTipo  " + Sql);
			 rs1 = consultar(stSql, ps);
			if (rs1.next()) {
				Dados= new CertidaoTipoProcessoTipoDt();
				associarDt(Dados, rs1);
			}
			//rs1.close();
			//////System.out.println("..ps-ConsultaId");
		}finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        }  
		return Dados; 
	}

	protected void associarDt( CertidaoTipoProcessoTipoDt Dados, ResultSetTJGO rs1 )  throws Exception {
		Dados.setId(rs1.getString("ID_CERT_TIPO_PROC_TIPO"));
		Dados.setCertidaoTipo(rs1.getString("CERT_TIPO"));
		Dados.setId_CertidaoTipo( rs1.getString("ID_CERT_TIPO"));
		Dados.setId_ProcessoTipo( rs1.getString("ID_PROC_TIPO"));
		Dados.setProcessoTipo( rs1.getString("PROC_TIPO"));
		Dados.setCodigoTemp( rs1.getString("CODIGO_TEMP"));
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		//////System.out.println("..ps-ConsultaDescricaoCertidaoTipoProcessoTipo()");

		stSql= "SELECT ID_CERT_TIPO_PROC_TIPO, CERT_TIPO FROM PROJUDI.VIEW_CERT_TIPO_PROC_TIPO WHERE CERT_TIPO LIKE ? ";
		stSql+= " ORDER BY CERT_TIPO ";
		ps.adicionarString("%"+descricao+"%");
		
		try{
			//////System.out.println("..ps-ConsultaDescricaoCertidaoTipoProcessoTipo  " + Sql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			//////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				CertidaoTipoProcessoTipoDt obTemp = new CertidaoTipoProcessoTipoDt();
				obTemp.setId(rs1.getString("ID_CERT_TIPO_PROC_TIPO"));
				obTemp.setCertidaoTipo(rs1.getString("CERT_TIPO"));
				liTemp.add(obTemp);
			}
			//rs1.close();
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_CERT_TIPO_PROC_TIPO WHERE CERT_TIPO LIKE ? ";
			rs2 = consultar(stSql, ps);
			//////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			//rs1.close();
			//////System.out.println("..CertidaoTipoProcessoTipoPsGen.consultarDescricao() Operação realizada com sucesso");
		} finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }
		return liTemp; 
	}

} 
