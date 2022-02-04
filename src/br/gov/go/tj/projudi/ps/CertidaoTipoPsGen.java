package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.CertidaoTipoDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class CertidaoTipoPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = 5053055346390350609L;

	//---------------------------------------------------------
	public CertidaoTipoPsGen() {

	}



//---------------------------------------------------------
	public void inserir(CertidaoTipoDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psCertidaoTipoinserir()");
		stSqlCampos= "INSERT INTO PROJUDI.CERT_TIPO ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getCertidaoTipoCodigo().length()>0)) {
			 stSqlCampos+=   stVirgula + "CERT_TIPO_CODIGO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getCertidaoTipoCodigo());  

			stVirgula=",";
		}
		if ((dados.getCertidaoTipo().length()>0)) {
			 stSqlCampos+=   stVirgula + "CERT_TIPO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getCertidaoTipo());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_CERT_TIPO",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(CertidaoTipoDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psCertidaoTipoalterar()");

		stSql= "UPDATE PROJUDI.CERT_TIPO SET  ";
		stSql+= "CERT_TIPO_CODIGO = ?";		 ps.adicionarLong(dados.getCertidaoTipoCodigo());  

		stSql+= ",CERT_TIPO = ?";		 ps.adicionarString(dados.getCertidaoTipo());  

		stSql += " WHERE ID_CERT_TIPO  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps); 
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psCertidaoTipoexcluir()");

		stSql= "DELETE FROM PROJUDI.CERT_TIPO";
		stSql += " WHERE ID_CERT_TIPO = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public CertidaoTipoDt consultarId(String id_certidaotipo )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		CertidaoTipoDt Dados=null;
		////System.out.println("....ps-ConsultaId_CertidaoTipo)");

		stSql= "SELECT * FROM PROJUDI.VIEW_CERT_TIPO WHERE ID_CERT_TIPO = ?";		ps.adicionarLong(id_certidaotipo); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_CertidaoTipo  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new CertidaoTipoDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( CertidaoTipoDt Dados, ResultSetTJGO rs )  throws Exception {
		Dados.setId(rs.getString("ID_CERT_TIPO"));
		Dados.setCertidaoTipo(rs.getString("CERT_TIPO"));
		Dados.setCertidaoTipoCodigo( rs.getString("CERT_TIPO_CODIGO"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoCertidaoTipo()");

		stSql= "SELECT ID_CERT_TIPO, CERT_TIPO FROM PROJUDI.VIEW_CERT_TIPO WHERE CERT_TIPO LIKE ?";
		stSql+= " ORDER BY CERT_TIPO ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoCertidaoTipo  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				CertidaoTipoDt obTemp = new CertidaoTipoDt();
				obTemp.setId(rs1.getString("ID_CERT_TIPO"));
				obTemp.setCertidaoTipo(rs1.getString("CERT_TIPO"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_CERT_TIPO WHERE CERT_TIPO LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..CertidaoTipoPsGen.consultarDescricao() Operação realizada com sucesso");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return liTemp; 
	}

} 
