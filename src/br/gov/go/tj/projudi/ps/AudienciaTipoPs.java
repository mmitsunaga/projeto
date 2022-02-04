package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.AudienciaTipoDt;
import br.gov.go.tj.projudi.ne.ConfirmacaoEmailInscricaoNe;
import java.sql.Connection;
import br.gov.go.tj.projudi.dt.DescritorComboDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ProjudiConstantes;
import br.gov.go.tj.utils.ResultSetTJGO;

public class AudienciaTipoPs extends AudienciaTipoPsGen {

	/**
     * 
     */
    private static final long serialVersionUID = -583019138185924096L;

    public AudienciaTipoPs(Connection conexao){
    	Conexao = conexao;
	}

	/**
	 * Método responsável por buscar um objeto do tipo AudienciaTipoDt dado o código do tipo da audiência.
	 * 
	 * @throws Exception
	 */
	public AudienciaTipoDt consultarAudienciaTipoCodigo(String audienciaTipoCodigo) throws Exception {
		AudienciaTipoDt audienciaTipoDt = new AudienciaTipoDt();
		String sql;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		sql = "SELECT * FROM PROJUDI.VIEW_AUDI_TIPO WHERE AUDI_TIPO_CODIGO = ?";
		ps.adicionarLong(audienciaTipoCodigo);
		
		ResultSetTJGO rs1 = null;
		try{
			rs1 = consultar(sql,ps);
			if (rs1.next()) {
				audienciaTipoDt.setId(rs1.getString("ID_AUDI_TIPO"));
				audienciaTipoDt.setAudienciaTipo(rs1.getString("AUDI_TIPO"));
				audienciaTipoDt.setAudienciaTipoCodigo(rs1.getString("AUDI_TIPO_CODIGO"));
				audienciaTipoDt.setCodigoTemp(rs1.getString("CODIGO_TEMP"));
			}
		
		}
		finally{
			try{if(rs1!=null) rs1.close(); } catch(Exception e) {}
		}
		return audienciaTipoDt;
	}

	/**
	 * Consulta de Tipos de Audiência
	 * Sobrescrevendo método do Gen para retornar também o AudienciaTipoCodigo
	 */
	public List consultarDescricao(String descricao, String posicao) throws Exception {
		
		String Sql;
		String SqlFrom;
		String SqlOrder;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		Sql = "SELECT ID_AUDI_TIPO, AUDI_TIPO, AUDI_TIPO_CODIGO";
		SqlFrom = " FROM PROJUDI.VIEW_AUDI_TIPO";
		SqlFrom += " WHERE AUDI_TIPO LIKE ?";
				ps.adicionarString("%"+ descricao +"%");
		SqlOrder = " ORDER BY AUDI_TIPO ";
	
		try{
			rs1 = consultarPaginacao(Sql + SqlFrom + SqlOrder, ps, posicao);

			while (rs1.next()) {
				AudienciaTipoDt obTemp = new AudienciaTipoDt();
				obTemp.setId(rs1.getString("ID_AUDI_TIPO"));
				obTemp.setAudienciaTipo(rs1.getString("AUDI_TIPO"));
				obTemp.setAudienciaTipoCodigo(rs1.getString("AUDI_TIPO_CODIGO"));
				liTemp.add(obTemp);
			}
			//rs1.close();
			Sql = "SELECT COUNT(*) as QUANTIDADE";
			rs2 = consultar(Sql + SqlFrom, ps);
			if (rs2.next())	liTemp.add(rs2.getLong("QUANTIDADE"));
			
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }  
		return liTemp;
	}

	/**
	 * Consulta de Tipos de Audiência
	 */
	public String consultarDescricaoJSON(String descricao, String posicao) throws Exception {
		
		String Sql;
		String SqlFrom;
		String SqlOrder;
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 2;
		

		Sql = "SELECT ID_AUDI_TIPO as id, AUDI_TIPO as descricao1, AUDI_TIPO_CODIGO as descricao2";
		SqlFrom = " FROM PROJUDI.VIEW_AUDI_TIPO";
		SqlFrom += " WHERE AUDI_TIPO LIKE ?";

		ps.adicionarString("%"+ descricao +"%");
		SqlOrder = " ORDER BY AUDI_TIPO ";
	
		try{
			rs1 = consultarPaginacao(Sql + SqlFrom + SqlOrder, ps, posicao);
			Sql = "SELECT COUNT(*) as QUANTIDADE";
			rs2 = consultar(Sql + SqlFrom, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
			
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }  
		return stTemp;
	}
	
	/**
	 * Consulta de Tipos de Audiência
	 */
	public String consultarDescricaoJSON(String descricao, String idUsuarioCejusc, String posicao) throws Exception {
		String Sql;
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 1;
		
		Sql = "SELECT ID_AUDI_TIPO as id, AUDI_TIPO as descricao1 ";
		Sql += " FROM PROJUDI.AUDI_TIPO ";
		Sql += " WHERE AUDI_TIPO LIKE ?";
		ps.adicionarString("%"+ descricao +"%");
		Sql += " AND ";
		Sql += "  ( ";
		Sql += "  (AUDI_TIPO_CODIGO IN (?, ?, ?) AND EXISTS (SELECT 1 FROM PROJUDI.USU_CEJUSC WHERE ID_USU_CEJUSC = ? AND OPCAO_CONCILIADOR = ?)) "; 
		ps.adicionarLong(AudienciaTipoDt.Codigo.CONCILIACAO.getCodigo());
		ps.adicionarLong(AudienciaTipoDt.Codigo.CONCILIACAO_CEJUSC.getCodigo());
		ps.adicionarLong(AudienciaTipoDt.Codigo.CONCILIACAO_CEJUSC_DPVAT.getCodigo());
		ps.adicionarLong(idUsuarioCejusc);
		ps.adicionarLong(ConfirmacaoEmailInscricaoNe.CODIGO_PERFIL_APROVADO);
		Sql += " OR (AUDI_TIPO_CODIGO = ? AND EXISTS (SELECT 1 FROM PROJUDI.USU_CEJUSC WHERE ID_USU_CEJUSC = ? AND OPCAO_MEDIADOR = ?)) "; 
		ps.adicionarLong(AudienciaTipoDt.Codigo.MEDIACAO_CEJUSC.getCodigo());
		ps.adicionarLong(idUsuarioCejusc);
		ps.adicionarLong(ConfirmacaoEmailInscricaoNe.CODIGO_PERFIL_APROVADO);
		Sql += "  ) ";
		Sql += " ORDER BY AUDI_TIPO ";
	
		try{
			rs1 = consultarPaginacao(Sql, ps, posicao);
			Sql = "SELECT COUNT(*) as QUANTIDADE FROM (" + Sql + ")";
			rs2 = consultar(Sql,ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
			
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }  
		return stTemp;
	}	
	/**
	 * 
	 * @param descricao
	 * @param ordenacao
	 * @param posicao
	 * @param quantidadeRegistros
	 * @return
	 * @throws Exception
	 */
	public String consultarDescricaoJSON(String descricao, String posicao, String ordenacao, String quantidadeRegistros) throws Exception {
		String Sql;
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 1;
		
		Sql = "SELECT ID_AUDI_TIPO as id, AUDI_TIPO as descricao1 FROM PROJUDI.VIEW_AUDI_TIPO WHERE AUDI_TIPO LIKE ?";
		Sql += " ORDER BY " + ordenacao;
		ps.adicionarString("%" + descricao + "%");
		
		try {
			rs1 = consultarPaginacao(Sql, ps, posicao, quantidadeRegistros);
			Sql = "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_AUDI_TIPO WHERE AUDI_TIPO LIKE ?";
			rs2 = consultar(Sql,ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
			
		} finally{
             try {if (rs1 != null) rs1.close();} catch (Exception e1) {}
             try {if (rs2 != null) rs2.close();} catch (Exception e1) {}
        }  
		return stTemp;
	}
	
	public List<DescritorComboDt> consultarDescricaoCombo(String descricao) throws Exception {
		
		String Sql;
		List<DescritorComboDt> listaTemp = new ArrayList<DescritorComboDt>();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();		
		String valorFiltro = super.obtenhaValorFiltroConsultarDescricaoCombo(descricao);
		
		Sql = "SELECT * FROM PROJUDI.VIEW_AUDI_TIPO";
		if (valorFiltro != null) {
			Sql += " WHERE AUDI_TIPO LIKE ?"; 
			ps.adicionarString(valorFiltro);	
		}		 
		Sql += " ORDER BY AUDI_TIPO";		
		
		try {
			rs1 = consultar(Sql, ps);

			while (rs1.next() && listaTemp.size() <= ProjudiConstantes.QUANTIDADE_OBJETOS_COMBO) {
				DescritorComboDt obTemp = new DescritorComboDt();
				obTemp.setId(rs1.getString("ID_AUDI_TIPO"));
				obTemp.setDescricao(rs1.getString("AUDI_TIPO"));				
				listaTemp.add(obTemp);
			}			
		} finally{
             try {if (rs1 != null) rs1.close();} catch (Exception e1) {}             
        }
		return listaTemp;
	}
	
	
	
}