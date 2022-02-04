package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.GuiaTipoDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class GuiaTipoPsGen extends Persistencia {


	/**
	 * 
	 */
	private static final long serialVersionUID = 3825904583052539911L;

	public GuiaTipoPsGen() {

	}

	public void inserir(GuiaTipoDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psGuiaTipoinserir()");
		stSqlCampos= "INSERT INTO projudi.GUIA_TIPO ("; 

		stSqlValores +=  " Values (";
 
		if (dados.getGuiaTipo().length()>0) {
			 stSqlCampos+=   stVirgula + "GUIA_TIPO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getGuiaTipo());  

			stVirgula=",";
		}
		if (dados.getGuiaTipoCodigo().length()>0) {
			 stSqlCampos+=   stVirgula + "GUIA_TIPO_CODIGO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getGuiaTipoCodigo());  

			stVirgula=",";
		}
		if (dados.getGuiaTipoCodigoExterno() != null && dados.getGuiaTipoCodigoExterno().length()>0) {
			 stSqlCampos+=   stVirgula + "GUIA_TIPO_CODIGO_EXTERNO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getGuiaTipoCodigoExterno());  

			stVirgula=",";
		}
		if (dados.getAtivo().length()>0) {
			 stSqlCampos+=   stVirgula + "ATIVO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarBoolean(dados.getAtivo());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 

		
			dados.setId(executarInsert(stSql,"ID_GUIA_TIPO",ps));
		 
	} 

	public void alterar(GuiaTipoDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		stSql= "UPDATE projudi.GUIA_TIPO SET  ";
		stSql+= "GUIA_TIPO = ?";		 ps.adicionarString(dados.getGuiaTipo());  

		stSql+= ",GUIA_TIPO_CODIGO = ?";		 ps.adicionarLong(dados.getGuiaTipoCodigo());  

		stSql+= ",GUIA_TIPO_CODIGO_EXTERNO = ?";		 ps.adicionarLong(dados.getGuiaTipoCodigoExterno());  

		stSql+= ",ATIVO = ?";		 ps.adicionarBoolean(dados.getAtivo());  

		stSql += " WHERE ID_GUIA_TIPO  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);
	
	} 

	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSql= "DELETE FROM projudi.GUIA_TIPO";
		stSql += " WHERE ID_GUIA_TIPO = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

	public GuiaTipoDt consultarId(String id_guiatipo )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		GuiaTipoDt Dados=null;

		stSql= "SELECT * FROM projudi.VIEW_GUIA_TIPO WHERE ID_GUIA_TIPO = ?";		ps.adicionarLong(id_guiatipo); 

		try{
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new GuiaTipoDt();
				associarDt(Dados, rs1);
			}
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( GuiaTipoDt Dados, ResultSetTJGO rs )  throws Exception {

		Dados.setId(rs.getString("ID_GUIA_TIPO"));
		Dados.setGuiaTipo(rs.getString("GUIA_TIPO"));
		Dados.setGuiaTipoCodigo( rs.getString("GUIA_TIPO_CODIGO"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));		
	}

	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSql= "SELECT ID_GUIA_TIPO, GUIA_TIPO FROM projudi.VIEW_GUIA_TIPO WHERE GUIA_TIPO LIKE ?";
		stSql+= " ORDER BY GUIA_TIPO ";
		ps.adicionarString("%"+descricao+"%"); 

		try{

			rs1 = consultarPaginacao(stSql, ps, posicao);

			while (rs1.next()) {
				GuiaTipoDt obTemp = new GuiaTipoDt();
				obTemp.setId(rs1.getString("ID_GUIA_TIPO"));
				obTemp.setGuiaTipo(rs1.getString("GUIA_TIPO"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM projudi.VIEW_GUIA_TIPO WHERE GUIA_TIPO LIKE ?";
			rs2 = consultar(stSql,ps);
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return liTemp; 
	}
	
	public String consultarDescricaoJSON(String descricao, String posicao ) throws Exception {

		String stSql="";
		String stTemp="";
		int qtdeColunas = 1;
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "SELECT ID_GUIA_TIPO as id, GUIA_TIPO as descricao1 FROM PROJUDI.VIEW_GUIA_TIPO WHERE GUIA_TIPO LIKE ?";
		stSql+= " ORDER BY GUIA_TIPO ";
		ps.adicionarString("%"+descricao+"%"); 

		try{


			rs1 = consultarPaginacao(stSql, ps, posicao);
			stSql= "SELECT Count(*) as Quantidade  FROM PROJUDI.VIEW_GUIA_TIPO WHERE GUIA_TIPO LIKE ?";
			rs2 = consultar(stSql,ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);

		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return stTemp; 
	}

} 
