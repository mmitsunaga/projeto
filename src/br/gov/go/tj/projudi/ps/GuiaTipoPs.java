package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.GuiaTipoDt;
import java.sql.Connection;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class GuiaTipoPs extends GuiaTipoPsGen{

	/**
     * 
     */
    private static final long serialVersionUID = -6812300525562401260L;

    public GuiaTipoPs(Connection conexao){
    	Conexao = conexao;
    }

	protected void associarDt( GuiaTipoDt Dados, ResultSetTJGO rs)  throws Exception {
		
		Dados.setId(rs.getString("ID_GUIA_TIPO"));
		Dados.setGuiaTipo(rs.getString("GUIA_TIPO"));
		Dados.setGuiaTipoCodigo( rs.getString("GUIA_TIPO_CODIGO"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
		Dados.setGuiaTipoCodigoExterno(rs.getString("GUIA_TIPO_CODIGO_EXTERNO"));
		Dados.setFlagGrau( rs.getString("FLAG_GRAU") );
		
	}
	
	public GuiaTipoDt consultarId(String id_guiatipo )  throws Exception {

		String Sql;
		ResultSetTJGO rs1 = null;
		GuiaTipoDt Dados=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql= "SELECT * FROM projudi.VIEW_GUIA_TIPO WHERE ATIVO = ?";
		ps.adicionarLong(GuiaTipoDt.ATIVO);
		Sql+=" AND ID_GUIA_TIPO = ?";
		ps.adicionarLong(id_guiatipo);

		try{
			rs1 = consultar(Sql,ps);
			if (rs1.next()) {
				Dados= new GuiaTipoDt();
				this.associarDt(Dados, rs1);
			}
		} finally {
			 if (rs1 != null) rs1.close();
		}
		return Dados; 
	}
	
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String Sql;
		String SqlFrom;
		String SqlOrder;
		List liTemp = new ArrayList();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql= "SELECT ID_GUIA_TIPO, GUIA_TIPO, FLAG_GRAU";
		SqlFrom = " FROM projudi.VIEW_GUIA_TIPO";
		SqlFrom += " WHERE ATIVO = ?";
		ps.adicionarLong(GuiaTipoDt.ATIVO);
		SqlFrom += " AND GUIA_TIPO LIKE ?";
		ps.adicionarString( descricao +"%");
		SqlOrder = " ORDER BY GUIA_TIPO ";
	
		try{

			rs = consultarPaginacao(Sql + SqlFrom + SqlOrder, ps, posicao);

			while (rs.next()) {
				GuiaTipoDt obTemp = new GuiaTipoDt();
				obTemp.setId(rs.getString("ID_GUIA_TIPO"));
				obTemp.setGuiaTipo(rs.getString("GUIA_TIPO"));
				obTemp.setFlagGrau(rs.getString("FLAG_GRAU"));
				liTemp.add(obTemp);
			}
			Sql= "SELECT COUNT(*) as QUANTIDADE";
			rs = consultar(Sql + SqlFrom, ps);
			while (rs.next()) {
				liTemp.add(rs.getLong("QUANTIDADE"));
			}
		} finally {
			if (rs != null) rs.close();
		}
		return liTemp; 
	}
	
	public List consultarDescricao() throws Exception {

		String Sql;
		List liTemp = null;
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql= "SELECT ID_GUIA_TIPO, GUIA_TIPO, FLAG_GRAU FROM projudi.VIEW_GUIA_TIPO WHERE ATIVO = ? ORDER BY GUIA_TIPO";
		ps.adicionarLong(GuiaTipoDt.ATIVO);
	
		try{

			rs = consultar(Sql, ps);

			while (rs.next()) {
				if( liTemp == null ) {
					liTemp = new ArrayList();
				}
				GuiaTipoDt obTemp = new GuiaTipoDt();
				obTemp.setId(rs.getString("ID_GUIA_TIPO"));
				obTemp.setGuiaTipo(rs.getString("GUIA_TIPO"));
				obTemp.setFlagGrau(rs.getString("FLAG_GRAU"));
				liTemp.add(obTemp);
			}
		} finally {
			if (rs != null) rs.close();
		}
		return liTemp; 
	}
	
	/**
	 * Método para consultar somente a Descrição pelo Id.
	 * @param String idGuiaTipo
	 * @return String
	 * @throws Exception
	 */
	public String consultarDescricao(String idGuiaTipo) throws Exception {
		
		String guiaTipo = null;
		
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		try{
			if(idGuiaTipo != null) {
				String sql = "SELECT GUIA_TIPO FROM projudi.GUIA_TIPO WHERE ID_GUIA_TIPO = ?";
				ps.adicionarLong(idGuiaTipo);
				rs1 = consultar(sql,ps);
				if( rs1 != null ) {
					while(rs1.next()) {
						guiaTipo = rs1.getString("GUIA_TIPO");
					}
				}
			}
		
		}
		finally{
			if (rs1 != null) rs1.close();
		}
		
		return guiaTipo;
	}
	
	/**
	 * Método para retornar uma lista de id de guia tipo.
	 * @param String itemExcluir
	 * @return List<String>
	 * @throws Exception
	 */
	public List consultarListaId_GuiaTipo(String itemExcluir) throws Exception {
		List listaId_GuiaTipo = null;
		
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		try{
			String sql = "SELECT ID_GUIA_TIPO FROM projudi.GUIA_TIPO ";
			if( itemExcluir != null && itemExcluir.length() > 0 ) {
				sql += " WHERE ID_GUIA_TIPO <> ?";
				ps.adicionarLong(itemExcluir);
			}
			rs1 = consultar(sql,ps);
			if( rs1 != null ) {
				
				if( listaId_GuiaTipo == null )
					listaId_GuiaTipo = new ArrayList();
				while(rs1.next()) {
					listaId_GuiaTipo.add(rs1.getString("ID_GUIA_TIPO"));
				}
			}
		
		}
		finally{
			if (rs1 != null) rs1.close();
		}
		
		return listaId_GuiaTipo;
	}
	
	public String consultarDescricaoJSON(String descricao, String posicao) throws Exception {

		String Sql;
		String SqlFrom;
		String SqlOrder;
		//String Sql2;
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 1;

		Sql = "SELECT ID_GUIA_TIPO AS ID, GUIA_TIPO AS DESCRICAO1";
		SqlFrom = " FROM PROJUDI.VIEW_GUIA_TIPO";
		SqlFrom += " WHERE ATIVO = ?";
		ps.adicionarLong(GuiaTipoDt.ATIVO);
		SqlFrom += " AND GUIA_TIPO LIKE ?";
		ps.adicionarString( descricao +"%");
		SqlOrder = " ORDER BY GUIA_TIPO ";
	
		try{

			rs1 = consultarPaginacao(Sql + SqlFrom + SqlOrder, ps, posicao);
			Sql= "SELECT COUNT(*) as QUANTIDADE";
			rs2 = consultar(Sql + SqlFrom, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
			
		} finally {
			try{if (rs1 != null) rs1.close();
			} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();
			} catch(Exception e) {}
		}
		return stTemp; 
	}
	
	public String consultarIdCodigo(String guiaTipoCodigo) throws Exception {
		
		String idGuiaTipo = null;
		
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		try{
			if(guiaTipoCodigo != null) {
				String sql = "SELECT ID_GUIA_TIPO FROM PROJUDI.GUIA_TIPO WHERE GUIA_TIPO_CODIGO = ?";
				ps.adicionarLong(guiaTipoCodigo);
				rs1 = consultar(sql, ps);
				
				if( rs1 != null ) {
					while(rs1.next()) {
						idGuiaTipo = rs1.getString("ID_GUIA_TIPO");
					}
				}
			}		
		}
		finally{
			if (rs1 != null) rs1.close();
		}		
		return idGuiaTipo;
	}
}
