package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.CertificadoDt;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class CertificadoPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = 8814008877570518913L;

	//---------------------------------------------------------
	public CertificadoPsGen() {

	}



//---------------------------------------------------------
	public void inserir(CertificadoDt dados ) throws Exception{

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psCertificadoinserir()");
		stSqlCampos= "INSERT INTO PROJUDI.CERTIFICADO ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getRaiz().length()>0)) {
			 stSqlCampos+=   stVirgula + "RAIZ " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarBoolean(dados.getRaiz());  

			stVirgula=",";
		}
		if ((dados.getEmissor().length()>0)) {
			 stSqlCampos+=   stVirgula + "EMISSOR " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarBoolean(dados.getEmissor());  

			stVirgula=",";
		}
		if ((dados.getLiberado().length()>0)) {
			 stSqlCampos+=   stVirgula + "LIBERADO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarBoolean(dados.getLiberado());  

			stVirgula=",";
		}
		if ((dados.getDataEmissao().length()>0)) {
			 stSqlCampos+=   stVirgula + "DATA_EMIS " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarDateTime(dados.getDataEmissao());  

			stVirgula=",";
		}
		if ((dados.getDataExpiracao().length()>0)) {
			 stSqlCampos+=   stVirgula + "DATA_EXPIRACAO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarDateTime(dados.getDataExpiracao());  

			stVirgula=",";
		}
		if ((dados.getDataRevogacao().length()>0)) {
			 stSqlCampos+=   stVirgula + "DATA_REVOGACAO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarDateTime(dados.getDataRevogacao());  

			stVirgula=",";
		}
		if ((dados.getMotivoRevogacao().length()>0)) {
			 stSqlCampos+=   stVirgula + "MOTIVO_REVOGACAO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getMotivoRevogacao());  

			stVirgula=",";
		}
		if ((dados.getId_UsuarioCertificado().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_USU_CERTIFICADO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_UsuarioCertificado());  

			stVirgula=",";
		}
		if ((dados.getNomeUsuario().length()>0)) {
			 stSqlCampos+=   stVirgula + "NOME_USU " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getNomeUsuario());  

			stVirgula=",";
		}
		if ((dados.getId_UsuarioLiberador().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_USU_LIBERADOR " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_UsuarioLiberador());  

			stVirgula=",";
		}
		if ((dados.getId_UsuarioRevogador().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_USU_REVOGADOR " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_UsuarioRevogador());  

			stVirgula=",";
		}
		if ((dados.getCertificado().length()>0)) {
			 stSqlCampos+=   stVirgula + "CERTIFICADO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getCertificado());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_CERTIFICADO",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(CertificadoDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psCertificadoalterar()");

		stSql= "UPDATE PROJUDI.CERTIFICADO SET  ";
		stSql+= "RAIZ = ?";		 ps.adicionarBoolean(dados.getRaiz());  

		stSql+= ",EMISSOR = ?";		 ps.adicionarBoolean(dados.getEmissor());  

		stSql+= ",LIBERADO = ?";		 ps.adicionarBoolean(dados.getLiberado());  

		stSql+= ",DATA_EMIS = ?";		 ps.adicionarDateTime(dados.getDataEmissao());  

		stSql+= ",DATA_EXPIRACAO = ?";		 ps.adicionarDateTime(dados.getDataExpiracao());  

		stSql+= ",DATA_REVOGACAO = ?";		 ps.adicionarDateTime(dados.getDataRevogacao());  

		stSql+= ",MOTIVO_REVOGACAO = ?";		 ps.adicionarString(dados.getMotivoRevogacao());  

		stSql+= ",ID_USU_CERTIFICADO = ?";		 ps.adicionarLong(dados.getId_UsuarioCertificado());  

		stSql+= ",NOME_USU = ?";		 ps.adicionarString(dados.getNomeUsuario());  

		stSql+= ",ID_USU_LIBERADOR = ?";		 ps.adicionarLong(dados.getId_UsuarioLiberador());  

		stSql+= ",ID_USU_REVOGADOR = ?";		 ps.adicionarLong(dados.getId_UsuarioRevogador());  

		stSql+= ",CERTIFICADO = ?";		 ps.adicionarString(dados.getCertificado());  

		stSql += " WHERE ID_CERTIFICADO  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psCertificadoexcluir()");

		stSql= "DELETE FROM PROJUDI.CERTIFICADO";
		stSql += " WHERE ID_CERTIFICADO = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public CertificadoDt consultarId(String id_certificado )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		CertificadoDt Dados=null;
		////System.out.println("....ps-ConsultaId_Certificado)");

		stSql= "SELECT * FROM PROJUDI.VIEW_CERTIFICADO WHERE ID_CERTIFICADO = ?";		ps.adicionarLong(id_certificado); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_Certificado  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new CertificadoDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( CertificadoDt Dados, ResultSetTJGO rs1 )  throws Exception {
		Dados.setId(rs1.getString("ID_CERTIFICADO"));
		Dados.setUsuarioCertificado(rs1.getString("USU_CERTIFICADO"));
		Dados.setRaiz( Funcoes.FormatarLogico(rs1.getString("RAIZ")));
		Dados.setEmissor( Funcoes.FormatarLogico(rs1.getString("EMISSOR")));
		Dados.setLiberado( Funcoes.FormatarLogico(rs1.getString("LIBERADO")));
		Dados.setDataEmissao( Funcoes.FormatarDataHora(rs1.getDateTime("DATA_EMIS")));
		Dados.setDataExpiracao( Funcoes.FormatarDataHora(rs1.getDateTime("DATA_EXPIRACAO")));
		Dados.setDataRevogacao( Funcoes.FormatarDataHora(rs1.getDateTime("DATA_REVOGACAO")));
		Dados.setMotivoRevogacao( rs1.getString("MOTIVO_REVOGACAO"));
		Dados.setId_UsuarioCertificado( rs1.getString("ID_USU_CERTIFICADO"));
		Dados.setId_UsuarioLiberador( rs1.getString("ID_USU_LIBERADOR"));
		Dados.setUsuarioLiberador( rs1.getString("USU_LIBERADOR"));
		Dados.setId_UsuarioRevogador( rs1.getString("ID_USU_REVOGADOR"));
		Dados.setUsuarioRevogador( rs1.getString("USU_REVOGADOR"));
		Dados.setCertificado(new String(rs1.getBytes("CERTIFICADO")));
		Dados.setConteudo(rs1.getBytes("CERTIFICADO"));
		Dados.setCodigoTemp( rs1.getString("CODIGO_TEMP"));
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoCertificado()");

		stSql= "SELECT ID_CERTIFICADO, USU_CERTIFICADO FROM PROJUDI.VIEW_CERTIFICADO WHERE USU_CERTIFICADO LIKE ?";
		stSql+= " ORDER BY USU_CERTIFICADO ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoCertificado  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				CertificadoDt obTemp = new CertificadoDt();
				obTemp.setId(rs1.getString("ID_CERTIFICADO"));
				obTemp.setUsuarioCertificado(rs1.getString("USU_CERTIFICADO"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_CERTIFICADO WHERE USU_CERTIFICADO LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..CertificadoPsGen.consultarDescricao() Operação realizada com sucesso");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return liTemp; 
	}

} 
