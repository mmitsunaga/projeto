package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.CustaDt;
import br.gov.go.tj.projudi.dt.GuiaFinalidadeModeloCustaDt;
import br.gov.go.tj.projudi.dt.GuiaFinalidadeModeloDt;
import br.gov.go.tj.projudi.ne.GuiaTipoNe;
import java.sql.Connection;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class GuiaFinalidadeModeloCustaPs extends Persistencia {

	private static final long serialVersionUID = -4114379556341645679L;

	@SuppressWarnings("unused")
	private GuiaFinalidadeModeloCustaPs( ) {}
	
	public GuiaFinalidadeModeloCustaPs(Connection conexao){
		Conexao = conexao;
	}

	/**
	 * Método que consulta o cálculo e seus itens trazendo o código de arrecadação e sua descrição para a apresentação na guia.
	 * @param String idGuiaTipo
	 * @param String idFinalidade
	 * @return List
	 * @throws Exception
	 */
	public List consultarItensGuia(String idGuiaTipo, String idFinalidade) throws Exception {
		List listaGuiaCustaModelo = null;
		
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql = "SELECT ID_CUSTA, CUSTA, CODIGO_REGIMENTO, CODIGO_REGIMENTO_VALOR, PORCENTAGEM, MINIMO, VALOR_ACRESCIMO, VALOR_MAXIMO, ID_ARRECADACAO_CUSTA, ARRECADACAO_CUSTA, CODIGO_ARRECADACAO, ID_GUIA_FINALID_MODELO_CUSTA, GUIA_FINALIDADE_MODELO_CUSTA, REFERENCIA_CALCULO, ID_GUIA_FINALIDADE_MODELO, GUIA_FINALIDADE_MODELO, ID_GUIA_TIPO FROM projudi.VIEW_ITENS_FINALID_CALC_ARREC WHERE ID_GUIA_TIPO = ? AND ID_FINALIDADE = ?";
		ps.adicionarLong(idGuiaTipo);
		ps.adicionarLong(idFinalidade);
		
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
				
				GuiaFinalidadeModeloCustaDt guiaCustaModeloDt = new GuiaFinalidadeModeloCustaDt();
				
				guiaCustaModeloDt.setCustaDt(custaDt);
				guiaCustaModeloDt.setId(						rs1.getString("ID_GUIA_FINALID_MODELO_CUSTA"));
				guiaCustaModeloDt.setGuiaFinalidadeModeloCusta(		rs1.getString("GUIA_FINALIDADE_MODELO_CUSTA"));
				
				GuiaFinalidadeModeloDt guiaModeloDt = new GuiaFinalidadeModeloDt();
				guiaModeloDt.setId(				rs1.getString("ID_GUIA_FINALIDADE_MODELO"));
				guiaModeloDt.setGuiaFinalidadeModelo( 	rs1.getString("GUIA_FINALIDADE_MODELO"));
				guiaModeloDt.setGuiaTipoDt(new GuiaTipoNe().consultarId(rs1.getString("ID_GUIA_TIPO")));
				
				guiaCustaModeloDt.setGuiaFinalidadeModeloDt(guiaModeloDt);
				
				listaGuiaCustaModelo.add(guiaCustaModeloDt);
			}
		
		}
		finally{
			 rs1.close();
		}
		
		return listaGuiaCustaModelo;
	}
}
