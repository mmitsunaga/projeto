package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.ZonaDt;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class ZonaPsGen extends Persistencia {

	private static final long serialVersionUID = -582842164152906647L;

	public ZonaPsGen() {
	}

	public void inserir(ZonaDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		stSqlCampos= "INSERT INTO PROJUDI.ZONA ("; 

		stSqlValores +=  " Values (";
		
		if (dados.getZonaCodigo() != null && dados.getZonaCodigo().length()>0) {
			 stSqlCampos+=   stVirgula + "ZONA_CODIGO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getZonaCodigo());  

			stVirgula=",";
		} 
		if (dados.getZona() != null && dados.getZona().length()>0) {
			 stSqlCampos+=   stVirgula + "ZONA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getZona());  

			stVirgula=",";
		}		
		if (dados.getValorCivel() != null && dados.getValorCivel().length()>0) {
			 stSqlCampos+=   stVirgula + "VALOR_CIVEL" ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarBigDecimal(dados.getValorCivel());  

			stVirgula=",";
		}
		if (dados.getValorCriminal() != null && dados.getValorCriminal().length()>0) {
			 stSqlCampos+=   stVirgula + "VALOR_CRIMINAL " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarBigDecimal(dados.getValorCriminal());  

			stVirgula=",";
		}
		if (dados.getValorSegundoGrau() != null && dados.getValorSegundoGrau().length()>0) {
			 stSqlCampos+=   stVirgula + "VALOR_SEGUNDO_GRAU " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarBigDecimal(dados.getValorSegundoGrau());  

			stVirgula=",";
		}
		if (dados.getValorContaVinculada() != null && dados.getValorContaVinculada().length()>0) {
			 stSqlCampos+=   stVirgula + "VALOR_CONTA_VINCULADA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarBigDecimal(dados.getValorContaVinculada());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 

		
			dados.setId(executarInsert(stSql,"ID_ZONA",ps));

		 
	} 

	public void alterar(ZonaDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		stSql= "UPDATE PROJUDI.ZONA SET  ";
		
		if ((dados.getZonaCodigo() != null && dados.getZonaCodigo().length()>0)) {
			stSql+= "ZONA_CODIGO = ?";		 
			ps.adicionarLong(dados.getZonaCodigo());
		} 
		
		if ((dados.getZona() != null && dados.getZona().length()>0)) {
			stSql+= ",ZONA = ?";		 
			ps.adicionarString(dados.getZona());		}		  
		
		if ((dados.getValorCivel() != null && dados.getValorCivel().length()>0)) {
			stSql+= ",VALOR_CIVEL = ?";		 
			ps.adicionarBigDecimal(dados.getValorCivel());
		}
		
		if ((dados.getValorSegundoGrau() != null && dados.getValorSegundoGrau().length()>0)) {
			stSql+= ",VALOR_SEGUNDO_GRAU = ?";		 
			ps.adicionarBigDecimal(dados.getValorSegundoGrau());  
		}
		
		if ((dados.getValorContaVinculada() != null && dados.getValorContaVinculada().length()>0)) {
			stSql+= ",VALOR_CONTA_VINCULADA = ?";		 
			ps.adicionarBigDecimal(dados.getValorContaVinculada());
		}

		stSql += " WHERE ID_ZONA  = ? "; 		
		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps); 
	
	} 

	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSql= "DELETE FROM PROJUDI.ZONA";
		stSql += " WHERE ID_ZONA = ?";		ps.adicionarLong(chave); 
			executarUpdateDelete(stSql,ps);

		

	} 

	public ZonaDt consultarId(String id_zona )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ZonaDt Dados=null;

		stSql= "SELECT * FROM PROJUDI.VIEW_ZONA WHERE ID_ZONA = ?";		ps.adicionarLong(id_zona); 

		try{
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new ZonaDt();
				associarDt(Dados, rs1);
			}
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( ZonaDt Dados, ResultSetTJGO rs )  throws Exception {
		Dados.setId(rs.getString("ID_ZONA"));
		Dados.setZona(rs.getString("ZONA"));
		Dados.setZonaCodigo( rs.getString("ZONA_CODIGO"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
		Dados.setValorCivel(Funcoes.FormatarDecimal(rs.getString("VALOR_CIVEL")));
		Dados.setValorCriminal(Funcoes.FormatarDecimal(rs.getString("VALOR_CRIMINAL")));
		Dados.setValorSegundoGrau(Funcoes.FormatarDecimal(rs.getString("VALOR_SEGUNDO_GRAU")));
		Dados.setValorContaVinculada(Funcoes.FormatarDecimal(rs.getString("VALOR_CONTA_VINCULADA")));
	}

	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSql= "SELECT ID_ZONA, ZONA FROM PROJUDI.VIEW_ZONA WHERE ZONA LIKE ?";
		stSql+= " ORDER BY ZONA ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			rs1 = consultarPaginacao(stSql, ps, posicao);

			while (rs1.next()) {
				ZonaDt obTemp = new ZonaDt();
				obTemp.setId(rs1.getString("ID_ZONA"));
				obTemp.setZona(rs1.getString("ZONA"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_ZONA WHERE ZONA LIKE ?";
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
