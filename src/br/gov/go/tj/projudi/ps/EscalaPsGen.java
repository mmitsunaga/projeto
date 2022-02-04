package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.EscalaDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class EscalaPsGen extends Persistencia {

	private static final long serialVersionUID = -6368594833877402304L;

	public EscalaPsGen() {

	}

	public void inserir(EscalaDt dados ) throws Exception { 

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		stSqlCampos= "INSERT INTO PROJUDI.ESC ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getEscala().length()>0)) {
			 stSqlCampos+=   stVirgula + "ESC " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getEscala());  
			stVirgula=",";
		}
		if ((dados.getId_MandadoTipo().length()>0)) {
			stSqlCampos+=   stVirgula + "ID_MAND_TIPO " ;
			stSqlValores+=   stVirgula + "? " ;
			ps.adicionarLong(dados.getId_MandadoTipo());  
			stVirgula=",";
		}
		if ((dados.getId_EscalaTipo().length()>0)) {
			stSqlCampos+=   stVirgula + "ID_ESCALA_TIPO " ;
			stSqlValores+=   stVirgula + "? " ;
			ps.adicionarLong(dados.getId_EscalaTipo());  
			stVirgula=",";
		}
		if ((dados.getId_Regiao().length()>0)) {
			stSqlCampos+=   stVirgula + "ID_REGIAO " ;
			stSqlValores+=   stVirgula + "? " ;
			ps.adicionarLong(dados.getId_Regiao());  
			stVirgula=",";
		}
		if ((dados.getId_Serventia().length()>0)) {
			stSqlCampos+=   stVirgula + "ID_SERV " ;
			stSqlValores+=   stVirgula + "? " ;
			ps.adicionarLong(dados.getId_Serventia());  
			stVirgula=",";
		}
		if ((dados.getQuantidadeMandado().length()>0)) {
			stSqlCampos+=   stVirgula + "QUANTIDADE_MAND " ;
			stSqlValores+=   stVirgula + "? " ;
			ps.adicionarLong(dados.getQuantidadeMandado());  
			stVirgula=",";
		}
		if ((dados.getAtivo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ATIVO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarBoolean(dados.getAtivo());  
		}
		if ((dados.getTipoEspecial().length()>0)) {
			 stSqlCampos+=   stVirgula + "TIPO_ESPECIAL " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarDecimal(dados.getTipoEspecial());  
		}
		stSqlValores+= ")";

		stSqlCampos+= ")";
		stSql+= stSqlCampos + stSqlValores; 

		dados.setId(executarInsert(stSql,"ID_ESC",ps)); 
	} 

	public void alterar(EscalaDt dados) throws Exception{ 
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="UPDATE PROJUDI.ESC SET  ";
		stSql+= "ESC = ?";		 ps.adicionarString(dados.getEscala());  
		stSql+= ",ID_MAND_TIPO = ?";		 ps.adicionarLong(dados.getId_MandadoTipo());  
		stSql+= ",ID_ESCALA_TIPO = ?";		 ps.adicionarLong(dados.getId_EscalaTipo());  
		stSql+= ",ID_REGIAO = ?";		 ps.adicionarLong(dados.getId_Regiao());  
		stSql+= ",ID_SERV = ?";		 ps.adicionarLong(dados.getId_Serventia());  
		stSql+= ",QUANTIDADE_MAND = ?";		 ps.adicionarLong(dados.getQuantidadeMandado());  
		stSql+= ",ATIVO = ?";		 ps.adicionarBoolean(dados.getAtivo());
		stSql+= ",TIPO_ESPECIAL = ?";		 ps.adicionarDecimal(dados.getTipoEspecial());
		stSql += " WHERE ID_ESC  = ? "; 		ps.adicionarLong(dados.getId()); 
		
		executarUpdateDelete(stSql,ps); 
	
	} 

	public void excluir( String chave) throws Exception { 
		String stSql="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		stSql= "DELETE FROM PROJUDI.ESC";
		stSql += " WHERE ID_ESC = ?";		ps.adicionarLong(chave); 
		
		executarUpdateDelete(stSql,ps);

	} 

	public EscalaDt consultarId(String id_escala )  throws Exception {
		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		EscalaDt Dados=null;

		stSql= "SELECT * FROM PROJUDI.VIEW_ESCALA WHERE ID_ESC = ?";		ps.adicionarLong(id_escala); 
		try{
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new EscalaDt();
				associarDt(Dados, rs1);
			}
		} finally {
			try {if (rs1 != null) rs1.close();} catch (Exception e) {}
		}
		return Dados; 
	}

	protected void associarDt( EscalaDt Dados, ResultSetTJGO rs1 )  throws Exception {
		Dados.setId(rs1.getString("ID_ESC"));
		Dados.setEscala(rs1.getString("ESC"));
		Dados.setId_Serventia( rs1.getString("ID_SERV"));
		Dados.setServentia( rs1.getString("SERV"));
		Dados.setId_EscalaTipo( rs1.getString("ID_ESCALA_TIPO"));
		Dados.setEscalaTipo( rs1.getString("ESCALA_TIPO"));
		Dados.setId_MandadoTipo( rs1.getString("ID_MAND_TIPO"));
		Dados.setMandadoTipo( rs1.getString("MAND_TIPO"));
		Dados.setId_Regiao( rs1.getString("ID_REGIAO"));
		Dados.setRegiao( rs1.getString("REGIAO"));
		Dados.setQuantidadeMandado( rs1.getString("QUANTIDADE_MAND"));
		Dados.setCodigoTemp( rs1.getString("CODIGO_TEMP"));
		Dados.setServentiaCodigo( rs1.getString("SERV_CODIGO"));
		Dados.setEscalaTipoCodigo( rs1.getString("ESCALA_TIPO_CODIGO"));
		Dados.setMandadoTipoCodigo( rs1.getString("MAND_TIPO_CODIGO"));
		Dados.setRegiaoCodigo( rs1.getString("REGIAO_CODIGO"));
		Dados.setAtivo( rs1.getString("ATIVO"));
		Dados.setTipoEspecial( rs1.getString("TIPO_ESPECIAL"));
	}

	public List consultarDescricao(String descricao, String posicao ) throws Exception {
		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSql= "SELECT ID_ESC, ESC FROM PROJUDI.VIEW_ESCALA WHERE ESC LIKE ?";
		stSql+= " ORDER BY ESC ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			rs1 = consultarPaginacao(stSql, ps, posicao);
			while (rs1.next()) {
				EscalaDt obTemp = new EscalaDt();
				obTemp.setId(rs1.getString("ID_ESC"));
				obTemp.setEscala(rs1.getString("ESC"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_ESCALA WHERE ESC LIKE ?";
			rs2 = consultar(stSql,ps);
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
		} finally {
			try {if (rs1 != null) rs1.close();} catch (Exception e) {}
			try {if (rs2 != null) rs2.close();} catch (Exception e) {}
		}
		return liTemp; 
	}
} 
