package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.DescritorComboDt;
import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.ServentiaTipoDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import java.sql.Connection;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ProjudiConstantes;
import br.gov.go.tj.utils.ResultSetTJGO;

//---------------------------------------------------------
public class ServentiaTipoPs extends ServentiaTipoPsGen {

/**
     * 
     */
    private static final long serialVersionUID = 6426016418657781034L;

    public ServentiaTipoPs(Connection conexao){
    	Conexao = conexao;
    }

	//
	public String consultarCodigoServentiaTipo(String id_serventiatipo) throws Exception {

		String Sql;
		String serventiaTipoCodigo = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		////System.out.println("....ps-consultarServentiaTipoCodigo)");

		Sql = "SELECT * FROM PROJUDI.SERV_TIPO WHERE ID_SERV_TIPO = ?";
		ps.adicionarLong(id_serventiatipo);
		////System.out.println("....Sql  " + Sql  );

		try{
			////System.out.println("..ps-consultarServentiaTipoCodigo  " + Sql);
			rs1 = consultar(Sql, ps);
			if (rs1.next()) {
				serventiaTipoCodigo = rs1.getString("SERV_TIPO_CODIGO");
			}
			//rs1.close();
			////System.out.println("..ps-consultarServentiaTipoCodigo");
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return serventiaTipoCodigo;
	}

	public ServentiaTipoDt consultarServentiaTipoCodigo(int codigo_serventiatipo) throws Exception {

		String Sql;
		ServentiaTipoDt serventiaTipoDt = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "SELECT * FROM PROJUDI.SERV_TIPO WHERE SERV_TIPO_CODIGO = ?";
		ps.adicionarLong(codigo_serventiatipo);

		try{
			rs1 = consultar(Sql, ps);
			if (rs1.next()) {
				serventiaTipoDt = new ServentiaTipoDt();
				associarDt(serventiaTipoDt, rs1);
			}
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return serventiaTipoDt;
	}
	
	/**
	 * Consulta tipos de serventia para solucionar pendencia
	 * @author Leandro Bernardes
	 * @param String descricao, descricao do tipo serventia
	 * @return List
	 * @throws Exception
	 */

	public List consultarServentiaTipo(String descricao) throws Exception {

		String Sql;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoServentiaTipo()");

		Sql = "SELECT ID_SERV_TIPO, SERV_TIPO FROM PROJUDI.VIEW_SERV_TIPO WHERE SERV_TIPO LIKE ?";
		ps.adicionarString( descricao +"%");
		Sql += " AND SERV_TIPO_CODIGO <> ? AND SERV_TIPO_CODIGO <> ? AND SERV_TIPO_CODIGO <> ? AND SERV_TIPO_CODIGO <> ? AND  CODIGO_TEMP IS NULL ";
		ps.adicionarLong(ServentiaTipoDt.ORDEM_ADVOGADOS_BRASIL);
		ps.adicionarLong(ServentiaTipoDt.SISTEMA_PROJUDI);
		ps.adicionarLong(ServentiaTipoDt.PARTE);
		ps.adicionarLong(ServentiaTipoDt.ADMINISTRACAO_SISTEMA_PROJUDI);		
		Sql += " ORDER BY SERV_TIPO ";
		try{
			////System.out.println("..ps-ConsultaDescricaoServentiaTipo  " + Sql);

			rs1 = consultar(Sql, ps);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				ServentiaTipoDt obTemp = new ServentiaTipoDt();
				obTemp.setId(rs1.getString("ID_SERV_TIPO"));
				obTemp.setServentiaTipo(rs1.getString("SERV_TIPO"));
				liTemp.add(obTemp);
			}
			//rs1.close();
			////System.out.println("..ServentiaTipoPs.consultarServentiaTipo() Operação realizada com sucesso");
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return liTemp;
	}

	/**
	 * Consulta tipos de serventia
	 * @author asrocha
	 * @param String id_Serventia, Id do tipo de serventia
	 * @return List
	 * @throws Exception
	 */
	public List consultarServentiaTipoId(String id_Serventia) throws Exception {

		String Sql;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "SELECT ID_SERV_TIPO, SERV_TIPO FROM PROJUDI.VIEW_SERV_TIPO WHERE ID_SERV_TIPO LIKE ?";
		ps.adicionarString( id_Serventia +"%");
		Sql += " ORDER BY SERV_TIPO ";
		try{
			rs1 = consultar(Sql, ps);
			while (rs1.next()) {
				ServentiaTipoDt obTemp = new ServentiaTipoDt();
				obTemp.setId(rs1.getString("ID_SERV_TIPO"));
				obTemp.setServentiaTipo(rs1.getString("SERV_TIPO"));
				liTemp.add(obTemp);
			}
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return liTemp;
	}
	
	public List consultarDescricao(String descricao) throws Exception {

		String Sql;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "SELECT ID_SERV_TIPO, SERV_TIPO, SERV_TIPO_CODIGO FROM PROJUDI.VIEW_SERV_TIPO WHERE SERV_TIPO LIKE ?";
		ps.adicionarString( descricao +"%");
		Sql += " ORDER BY SERV_TIPO ";
		try{
			rs1 = consultar(Sql, ps);

			while (rs1.next()) {
				ServentiaTipoDt obTemp = new ServentiaTipoDt();
				obTemp.setId(rs1.getString("ID_SERV_TIPO"));
				obTemp.setServentiaTipo(rs1.getString("SERV_TIPO"));
				obTemp.setServentiaTipoCodigo(rs1.getString("SERV_TIPO_CODIGO"));
				liTemp.add(obTemp);
			}
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return liTemp;
	}

	public List consultarServentiaTipo(String descricao, UsuarioDt usuarioDt ) throws Exception {

		String Sql;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoServentiaTipo()");

		Sql= "SELECT ID_SERV_TIPO, SERV_TIPO FROM PROJUDI.VIEW_SERV_TIPO WHERE SERV_TIPO LIKE ?";
		ps.adicionarString( descricao +"%");
		
		if ((usuarioDt != null && usuarioDt.getGrupoCodigo().length() > 0 ) && 
				(Funcoes.StringToInt(usuarioDt.getGrupoCodigo()) == GrupoDt.ADMINISTRADORES 
				|| (Funcoes.StringToInt(usuarioDt.getGrupoCodigo()) == GrupoDt.ANALISTA_TI)
				||  (Funcoes.StringToInt(usuarioDt.getGrupoCodigo()) == GrupoDt.ANALISTA_COORDENARODIRA_JUDICIARIA) 
				||  (Funcoes.StringToInt(usuarioDt.getGrupoCodigo()) == GrupoDt.ESTATISTICA) )){
			Sql += " AND SERV_TIPO_CODIGO <> ?";
			ps.adicionarLong(ServentiaTipoDt.PARTE);
			Sql +=  " AND  CODIGO_TEMP IS NULL ";
			
		} else {
			Sql += " AND SERV_TIPO_CODIGO <> ?"  
				+ " AND SERV_TIPO_CODIGO <> ?" 
				+ " AND SERV_TIPO_CODIGO <> ?" 
				+ " AND SERV_TIPO_CODIGO <> ?" 
				+ " AND  CODIGO_TEMP IS NULL ";
			ps.adicionarLong(ServentiaTipoDt.ORDEM_ADVOGADOS_BRASIL);
			ps.adicionarLong(ServentiaTipoDt.SISTEMA_PROJUDI);
			ps.adicionarLong(ServentiaTipoDt.PARTE);
			ps.adicionarLong(ServentiaTipoDt.ADMINISTRACAO_SISTEMA_PROJUDI);
		}
		Sql+= " ORDER BY SERV_TIPO ";
		try{
			////System.out.println("..ps-ConsultaDescricaoServentiaTipo  " + Sql);

			rs1 = consultar(Sql, ps);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				ServentiaTipoDt obTemp = new ServentiaTipoDt();
				obTemp.setId(rs1.getString("ID_SERV_TIPO"));
				obTemp.setServentiaTipo(rs1.getString("SERV_TIPO"));
				liTemp.add(obTemp);
			}
			//rs1.close();
			////System.out.println("..ServentiaTipoPs.consultarServentiaTipo() Operação realizada com sucesso");
		}finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return liTemp; 
	}
	
	public String consultarDescricaoJSON(String descricao) throws Exception {

		String Sql;
		String stSql;
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		int qtdeColunas = 2;

		Sql = "SELECT ID_SERV_TIPO AS ID, SERV_TIPO AS DESCRICAO1, SERV_TIPO_CODIGO AS DESCRICAO2 FROM PROJUDI.VIEW_SERV_TIPO WHERE SERV_TIPO LIKE ?";
		ps.adicionarString( descricao +"%");
		Sql += " ORDER BY SERV_TIPO ";
		try{
			rs1 = consultar(Sql, ps);
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_SERV_TIPO WHERE SERV_TIPO LIKE ?";
			rs2 = consultar(stSql,ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), "0", rs1, qtdeColunas);
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return stTemp;
	}
	
	public String consultarDescricaoJSONPJD(String descricao) throws Exception {

		String Sql;
		String stSql;
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		int qtdeColunas = 2;

		Sql = "SELECT ID_SERV_TIPO AS ID, SERV_TIPO AS DESCRICAO1, SERV_TIPO_CODIGO AS DESCRICAO2 FROM PROJUDI.VIEW_SERV_TIPO WHERE SERV_TIPO LIKE ?";
		ps.adicionarString( descricao +"%");
		Sql += " ORDER BY SERV_TIPO ";
		try{
			rs1 = consultar(Sql, ps);
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_SERV_TIPO WHERE SERV_TIPO LIKE ?";
			rs2 = consultar(stSql,ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), "0", rs1, qtdeColunas);
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return stTemp;
	}
	
	/**
	 * Consulta de código e descrição para o select2 
	 * @param descricao
	 * @return
	 * @throws Exception
	 */
	public List<DescritorComboDt> consultarDescricaoCombo(String descricao) throws Exception {
		String Sql;
		List<DescritorComboDt> listaTemp = new ArrayList<DescritorComboDt>();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();		
		String valorFiltro = super.obtenhaValorFiltroConsultarDescricaoCombo(descricao);
		
		Sql = "SELECT ID_SERV_TIPO, SERV_TIPO FROM PROJUDI.VIEW_SERV_TIPO";
		if (valorFiltro != null) {
			Sql += " WHERE SERV_TIPO LIKE ?"; 
			ps.adicionarString(valorFiltro);	
		}		 
		Sql += " ORDER BY SERV_TIPO";		
		
		try {
			rs1 = consultar(Sql, ps);

			while (rs1.next() && listaTemp.size() <= ProjudiConstantes.QUANTIDADE_OBJETOS_COMBO) {
				DescritorComboDt obTemp = new DescritorComboDt();
				obTemp.setId(rs1.getString("ID_SERV_TIPO"));
				obTemp.setDescricao(rs1.getString("ID_SERV_TIPO") + " - " + rs1.getString("SERV_TIPO"));				
				listaTemp.add(obTemp);
			}			
		} catch (Exception e) {
			throw new Exception("<{Erro ao Consultar Descrição Tipo Serventia.}> Local Exception: " + this.getClass().getName() + ".consultarDescricaoCombo(): " + e.getMessage(), e);
        } finally{
             try {if (rs1 != null) rs1.close();} catch (Exception e1) {}             
        }
		return listaTemp;
	}
	
}
