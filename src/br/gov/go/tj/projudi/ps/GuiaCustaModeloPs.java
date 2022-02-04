package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.CustaDt;
import br.gov.go.tj.projudi.dt.GuiaCustaModeloDt;
import br.gov.go.tj.projudi.dt.GuiaModeloDt;
import java.sql.Connection;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

//---------------------------------------------------------
public class GuiaCustaModeloPs extends GuiaCustaModeloPsGen{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5968289200671706163L;

	@SuppressWarnings("unused")
	private GuiaCustaModeloPs( ) {}
	
	public GuiaCustaModeloPs(Connection conexao){
		Conexao = conexao;
	}

	//
	public List consultarCustaGuiaModeloGeral(String id_GuiaModelo ) throws Exception {

		String Sql;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql= "SELECT t2.ID_GUIA_CUSTA_MODELO, t2.GUIA_CUSTA_MODELO, t1.ID_CUSTA, t1.CUSTA, t1.CODIGO_REGIMENTO, t3.ID_GUIA_MODELO, t3.GUIA_MODELO";
		Sql+= " FROM projudi.CUSTA t1 ";
		Sql+= " LEFT JOIN projudi.GUIA_CUSTA_MODELO t2 ON t1.ID_CUSTA = t2.ID_CUSTA AND t2.ID_GUIA_MODELO = ? AND t1.CODIGO_REGIMENTO <> ? ";
		ps.adicionarLong(id_GuiaModelo);
		ps.adicionarString("0");
		Sql+= " LEFT JOIN projudi.GUIA_MODELO t3 ON t3.ID_GUIA_MODELO = t2.ID_GUIA_MODELO";
		Sql+= " WHERE t1.CODIGO_REGIMENTO <> ?";
		ps.adicionarString("0");
		try{

			rs1 = consultar(Sql, ps);

			while (rs1.next()) {
				GuiaCustaModeloDt obTemp = new GuiaCustaModeloDt();
				obTemp.setId(rs1.getString("ID_GUIA_CUSTA_MODELO"));
				obTemp.setGuiaCustaModelo(rs1.getString("GUIA_CUSTA_MODELO"));
				obTemp.setId_Custa (rs1.getString("ID_CUSTA"));
				obTemp.setCusta(rs1.getString("CUSTA"));
				obTemp.setCodigoRegimento(rs1.getString("CODIGO_REGIMENTO"));
				obTemp.setId_GuiaModelo(id_GuiaModelo);
				obTemp.setGuiaModelo(rs1.getString("GUIA_MODELO"));
				liTemp.add(obTemp);
			}
		
		}
		finally{
			 rs1.close();
		}
			return liTemp; 
	}

	/**
	 * Método que consulta o cálculo e seus itens trazendo o código de arrecadação e sua descrição para a apresentação na guia.
	 * @param String idGuiaTipo
	 * @param String idProcessoTipo
	 * @return List
	 * @throws Exception
	 */
	public List<GuiaCustaModeloDt> consultarItensGuiaProcessoTipo(String idGuiaTipo, String idProcessoTipo) throws Exception {
		return consultarItensGuia(idGuiaTipo, idProcessoTipo, "");
	}
	
	/**
	 * Método criado para o novo regimento.
	 * 
	 * Método que consulta o cálculo e seus itens trazendo o código de arrecadação e sua descrição para a apresentação na guia.
	 * @param FabricaConexao obFabricaConexao
	 * @param String idGuiaTipo
	 * @param String idProcessoTipo
	 * @return List
	 * @throws Exception
	 */
	public List<GuiaCustaModeloDt> consultarItensGuiaProcessoTipoNovoRegimento(String idGuiaTipo, String idProcessoTipo) throws Exception {
		List<GuiaCustaModeloDt> listaGuiaCustaModelo = null;
		
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql = "SELECT ID_CUSTA, CUSTA, CODIGO_REGIMENTO, CODIGO_REGIMENTO_VALOR, PORCENTAGEM, ";
		sql += "MINIMO, VALOR_ACRESCIMO, VALOR_MAXIMO, ID_ARRECADACAO_CUSTA, ARRECADACAO_CUSTA, CODIGO_ARRECADACAO, ";
		sql += "ID_GUIA_CUSTA_MODELO, GUIA_CUSTA_MODELO, REFERENCIA_CALCULO, ID_GUIA_MODELO, GUIA_MODELO, ID_GUIA_TIPO, ID_PROC_TIPO, ID_NATUREZA_SPG, CODIGO_TEMP_MODELO ";
		sql += "FROM projudi.VIEW_ITENS_CALC_ARRECADACAO ";
		
		sql += "WHERE ID_GUIA_TIPO = ? "; ps.adicionarLong(idGuiaTipo);
		
		//Regra para novo regimento: Verifica se CODIGO_TEMP no modelo é igual 1, este 1 identifica os novos modelos
		sql += "AND CODIGO_TEMP_MODELO = 1 ";
		
		if (Funcoes.StringToLong(idProcessoTipo) > 0) {
			sql += "AND ID_PROC_TIPO = ?"; ps.adicionarLong(idProcessoTipo);
		}
		
		try{
			rs1 = consultar(sql, ps);
			while (rs1.next()) {
				if( listaGuiaCustaModelo == null ) {
					listaGuiaCustaModelo = new ArrayList();
				}
				
				CustaDt custaDt = new CustaDt();
				custaDt.setId(						rs1.getString("ID_CUSTA"));
				custaDt.setCusta(					rs1.getString("CUSTA"));
				custaDt.setCodigoRegimento(			rs1.getString("CODIGO_REGIMENTO"));
				custaDt.setCodigoRegimentoValor( 	rs1.getString("CODIGO_REGIMENTO_VALOR"));
				custaDt.setId_ArrecadacaoCusta(		rs1.getString("ID_ARRECADACAO_CUSTA"));
				custaDt.setArrecadacaoCusta(		rs1.getString("ARRECADACAO_CUSTA"));
				custaDt.setCodigoArrecadacao(		rs1.getString("CODIGO_ARRECADACAO"));
				custaDt.setReferenciaCalculo(		rs1.getString("REFERENCIA_CALCULO"));
				custaDt.setPorcentagem( 			rs1.getString("PORCENTAGEM"));
				custaDt.setMinimo( 					rs1.getString("MINIMO"));
				custaDt.setValorAcrescimo( 			rs1.getString("VALOR_ACRESCIMO"));
				custaDt.setValorMaximo( 			rs1.getString("VALOR_MAXIMO"));
				
				GuiaCustaModeloDt guiaCustaModeloDt = new GuiaCustaModeloDt();
				
				guiaCustaModeloDt.setCustaDt(custaDt);
				guiaCustaModeloDt.setId(						rs1.getString("ID_GUIA_CUSTA_MODELO"));
				guiaCustaModeloDt.setGuiaCustaModelo(		rs1.getString("GUIA_CUSTA_MODELO"));
				
				
				GuiaModeloDt guiaModeloDt = new GuiaModeloDt();
				guiaModeloDt.setId(				rs1.getString("ID_GUIA_MODELO"));
				guiaModeloDt.setGuiaModelo( 	rs1.getString("GUIA_MODELO"));
				guiaModeloDt.setId_GuiaTipo(	rs1.getString("ID_GUIA_TIPO"));
				guiaModeloDt.setId_ProcessoTipo(rs1.getString("ID_PROC_TIPO"));
				guiaModeloDt.setId_NaturezaSPG(	rs1.getString("ID_NATUREZA_SPG"));
				if( rs1.getString("CODIGO_TEMP_MODELO") != null ) {
					guiaModeloDt.setCodigoTemp( 			rs1.getString("CODIGO_TEMP_MODELO"));
				}
				
				guiaCustaModeloDt.setGuiaModeloDt(guiaModeloDt);
				
				listaGuiaCustaModelo.add(guiaCustaModeloDt);
			}
		
		}
		finally{
			 if (rs1 != null) rs1.close();
		}
		
		return listaGuiaCustaModelo;
	}
	
	/**
	 * Método que consulta o cálculo e seus itens trazendo o código de arrecadação e sua descrição para a apresentação na guia pela natureza SPG.
	 * @param String idGuiaTipo
	 * @param String idNaturezaSPG
	 * @return List
	 * @throws Exception
	 */
	public List<GuiaCustaModeloDt> consultarItensGuiaNaturezaSPG(String idGuiaTipo, String idNaturezaSPG) throws Exception {
		return consultarItensGuia(idGuiaTipo, "", idNaturezaSPG);
	}
	
	
	private List<GuiaCustaModeloDt> consultarItensGuia(String idGuiaTipo, String idProcessoTipo, String idNaturezaSPG) throws Exception {
		List<GuiaCustaModeloDt> listaGuiaCustaModelo = null;
		
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql = "SELECT ID_CUSTA, CUSTA, CODIGO_REGIMENTO, CODIGO_REGIMENTO_VALOR, PORCENTAGEM, ";
		sql += "MINIMO, VALOR_ACRESCIMO, VALOR_MAXIMO, ID_ARRECADACAO_CUSTA, ARRECADACAO_CUSTA, CODIGO_ARRECADACAO, ";
		sql += "ID_GUIA_CUSTA_MODELO, GUIA_CUSTA_MODELO, REFERENCIA_CALCULO, ID_GUIA_MODELO, GUIA_MODELO, ID_GUIA_TIPO, ID_PROC_TIPO, ID_NATUREZA_SPG ";
		sql += "FROM projudi.VIEW_ITENS_CALC_ARRECADACAO ";
		
		sql += "WHERE ID_GUIA_TIPO = ? ";
		ps.adicionarLong(idGuiaTipo);
		
		if (Funcoes.StringToLong(idProcessoTipo) > 0) {
			sql += "AND ID_PROC_TIPO = ?";
			ps.adicionarLong(idProcessoTipo); 	
		} else if (Funcoes.StringToLong(idNaturezaSPG) > 0) {
			sql += "AND ID_NATUREZA_SPG = ?";
			ps.adicionarLong(idNaturezaSPG);
		}
		
		try{
			rs1 = consultar(sql, ps);
			while (rs1.next()) {
				if( listaGuiaCustaModelo == null ) {
					listaGuiaCustaModelo = new ArrayList();
				}
				
				CustaDt custaDt = new CustaDt();
				custaDt.setId(						rs1.getString("ID_CUSTA"));
				custaDt.setCusta(					rs1.getString("CUSTA"));
				custaDt.setCodigoRegimento(			rs1.getString("CODIGO_REGIMENTO"));
				custaDt.setCodigoRegimentoValor( 	rs1.getString("CODIGO_REGIMENTO_VALOR"));
				custaDt.setId_ArrecadacaoCusta(		rs1.getString("ID_ARRECADACAO_CUSTA"));
				custaDt.setArrecadacaoCusta(		rs1.getString("ARRECADACAO_CUSTA"));
				custaDt.setCodigoArrecadacao(		rs1.getString("CODIGO_ARRECADACAO"));
				custaDt.setReferenciaCalculo(		rs1.getString("REFERENCIA_CALCULO"));
				custaDt.setPorcentagem( 			rs1.getString("PORCENTAGEM"));
				custaDt.setMinimo( 					rs1.getString("MINIMO"));
				custaDt.setValorAcrescimo( 			rs1.getString("VALOR_ACRESCIMO"));
				custaDt.setValorMaximo( 			rs1.getString("VALOR_MAXIMO"));
				
				GuiaCustaModeloDt guiaCustaModeloDt = new GuiaCustaModeloDt();
				
				guiaCustaModeloDt.setCustaDt(custaDt);
				guiaCustaModeloDt.setId(						rs1.getString("ID_GUIA_CUSTA_MODELO"));
				guiaCustaModeloDt.setGuiaCustaModelo(		rs1.getString("GUIA_CUSTA_MODELO"));
				
				GuiaModeloDt guiaModeloDt = new GuiaModeloDt();
				guiaModeloDt.setId(				rs1.getString("ID_GUIA_MODELO"));
				guiaModeloDt.setGuiaModelo( 	rs1.getString("GUIA_MODELO"));
				guiaModeloDt.setId_GuiaTipo(	rs1.getString("ID_GUIA_TIPO"));
				guiaModeloDt.setId_ProcessoTipo(rs1.getString("ID_PROC_TIPO"));
				guiaModeloDt.setId_NaturezaSPG(	rs1.getString("ID_NATUREZA_SPG"));
				
				guiaCustaModeloDt.setGuiaModeloDt(guiaModeloDt);
				
				listaGuiaCustaModelo.add(guiaCustaModeloDt);
			}
		
		}
		finally{
			 if (rs1 != null) rs1.close();
		}
		
		return listaGuiaCustaModelo;
	}
}
