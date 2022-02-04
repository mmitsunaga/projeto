package br.gov.go.tj.projudi.ps;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.CustaDt;
import br.gov.go.tj.projudi.dt.GuiaCustaModeloDt;
import java.sql.Connection;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

//---------------------------------------------------------
public class CustaPs extends CustaPsGen{

//

	/**
     * 
     */
    private static final long serialVersionUID = -222978992502658550L;

    public CustaPs(Connection conexao){
    	Conexao = conexao;
	}

	/**
	 * Método para consultar Custa e Arrecadação usando a view auxiliar de um cálculo específico.
	 * @param String idGuiaTipo
	 * @param String idProcessoTipo
	 * @param String codigoRegimento
	 * @return GuiaCustaModeloDt
	 * @throws Exception
	 */
	public CustaDt consultarItemGuiaPorCodigoRegimento(String codigoRegimento) throws Exception {
		CustaDt custaDt = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
			
		ResultSetTJGO rs1 = null;
		try{
			String sql = "SELECT c.* ";
			sql += " FROM (projudi.CUSTA c join projudi.ARRECADACAO_CUSTA ac on((c.ID_ARRECADACAO_CUSTA = ac.ID_ARRECADACAO_CUSTA))) ";
			sql += " WHERE CODIGO_REGIMENTO = ?";
			ps.adicionarString(codigoRegimento);
			
			rs1 = consultar(sql.toString(), ps);
			
			while (rs1.next()) {
				
				custaDt = new CustaDt();

				this.associarDt(custaDt, rs1);
			}
		
		}
		finally{
			if(rs1 != null) rs1.close();
		}
		
		return custaDt;
	}
	
	/**
	 * Método de consultar Custa pelo código da arrecadação.
	 */
	public CustaDt consultarCodigoArrecadacao(String codigoArrecadacao)  throws Exception {

		String Sql;
		ResultSetTJGO rs1 = null;
		CustaDt Dados=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql= "SELECT * FROM projudi.VIEW_CUSTA WHERE CODIGO_ARRECADACAO = ?";
		ps.adicionarLong(codigoArrecadacao);

		try{
			rs1 = consultar(Sql, ps);
			if (rs1.next()) {
				Dados= new CustaDt();
				
				this.associarDt(Dados, rs1);
			}
		} finally {
			if (rs1 != null) rs1.close();
		}
		return Dados; 
	}
	
	/**
	 * Método de consultar Custa por Id.
	 */
	public CustaDt consultarId(String id_custa)  throws Exception {

		String Sql;
		ResultSetTJGO rs1 = null;
		CustaDt Dados=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql= "SELECT * FROM projudi.VIEW_CUSTA WHERE ID_CUSTA = ?";
		ps.adicionarLong(id_custa);

		try{
			rs1 = consultar(Sql, ps);
			if (rs1.next()) {
				Dados= new CustaDt();
				
				this.associarDt(Dados, rs1);
			}
		} finally {
			if (rs1 != null) rs1.close();
		}
		return Dados; 
	}
	
	/**
	 * Método que consulta a descrição de Custa.
	 * @param String descricao
	 * @param String posicao
	 * @return List
	 */
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String Sql;
		String SqlFrom;
		String SqlOrder;
		List liTemp = new ArrayList();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql= "SELECT *";
		SqlFrom = " FROM projudi.VIEW_CUSTA ";
		SqlFrom += " WHERE CUSTA LIKE ? ";
		SqlFrom += " AND CODIGO_REGIMENTO <> ? ";
		ps.adicionarString( descricao +"%");
		ps.adicionarString("0");
		SqlOrder = " ORDER BY ID_CUSTA ";
		
		try{

			rs = consultarPaginacao(Sql + SqlFrom + SqlOrder, ps, posicao);

			while (rs.next()) {
				CustaDt obTemp = new CustaDt();
				
				this.associarDt(obTemp, rs);
				
				liTemp.add(obTemp);
			}
			Sql= "SELECT COUNT(*) as QUANTIDADE";
			rs = consultar(Sql + SqlFrom, ps);
			if (rs.next()) {
				liTemp.add(rs.getLong("QUANTIDADE"));
			}
		} finally {
			 if (rs != null) rs.close();
		}
		return liTemp; 
	}
	
	public String consultarDescricaoJSON(String descricao, String posicao ) throws Exception {

		String Sql;
		String SqlFrom;
		String SqlOrder;
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 2;

		Sql= "SELECT ID_CUSTA AS ID, CUSTA AS DESCRICAO1, CODIGO_REGIMENTO as DESCRICAO2";
		SqlFrom = " FROM projudi.VIEW_CUSTA ";
		SqlFrom += " WHERE CUSTA LIKE ?";
		SqlFrom += " AND CODIGO_REGIMENTO <> ? ";
		ps.adicionarString( descricao +"%");
		ps.adicionarString("0");
		SqlOrder = " ORDER BY ID";
		
		try{

			rs1 = consultarPaginacao(Sql + SqlFrom + SqlOrder, ps, posicao);

			Sql= "SELECT COUNT(*) as QUANTIDADE";
			rs2 = consultar(Sql + SqlFrom,ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
			
		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return stTemp; 
	}
	
	/**
	 * Método para consultar itens de Custa para o Cálculo.
	 * @param List listaCustaDt
	 * @return List
	 * @throws Exception
	 */
	public List consultarDescricao(List listaCustaDt) throws Exception {
		
		String Sql;
		List listaGuiaCustaModelo = null;
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		Sql = "SELECT * FROM projudi.VIEW_CUSTA ";
		if( listaCustaDt != null && listaCustaDt.size() > 0 ) {
			Sql += " WHERE ID_CUSTA IN (";
			
			for(int i = 0; i < listaCustaDt.size(); i++) {
				CustaDt custaDtAux = (CustaDt)listaCustaDt.get(i);
					
				Sql +=" ?,";
				ps.adicionarLong(custaDtAux.getId());
				
			}
			Sql = Sql.substring(0, Sql.length() - 1);
			Sql += ")";
		
			try{
				rs = consultar(Sql,ps);
				
				while (rs.next()) {
					if( listaGuiaCustaModelo == null ) {
						listaGuiaCustaModelo = new ArrayList();
					}
					
					CustaDt custaDt = new CustaDt();
					
					this.associarDt(custaDt, rs);
					
					GuiaCustaModeloDt guiaCustaModeloDt = new GuiaCustaModeloDt();
					
					guiaCustaModeloDt.setCustaDt(custaDt);
					
					listaGuiaCustaModelo.add(guiaCustaModeloDt);
				}
			
			}
			finally{
				if (rs != null) rs.close();
			}
		}
		
		return listaGuiaCustaModelo;
	}
	
	@Override
	protected void associarDt( CustaDt Dados, ResultSetTJGO rs )  throws Exception {
		Dados.setId(rs.getString("ID_CUSTA"));
		Dados.setCusta(rs.getString("CUSTA"));
		Dados.setCustaCodigo( rs.getString("CUSTA_CODIGO"));
		Dados.setCodigoRegimento( rs.getString("CODIGO_REGIMENTO"));
		Dados.setCodigoRegimentoValor( rs.getString("CODIGO_REGIMENTO_VALOR"));
		Dados.setPorcentagem( rs.getString("PORCENTAGEM"));
		Dados.setMinimo( rs.getString("MINIMO"));
		Dados.setId_ArrecadacaoCusta( rs.getString("ID_ARRECADACAO_CUSTA"));
		Dados.setId_ArrecadacaoCustaGenerica( rs.getString("ID_ARRECADACAO_CUSTA_GENERICA"));
		Dados.setArrecadacaoCusta( rs.getString("ARRECADACAO_CUSTA"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
		Dados.setReferenciaCalculo( rs.getString("REFERENCIA_CALCULO") );
		Dados.setCodigoArrecadacao(		rs.getString("CODIGO_ARRECADACAO"));
		Dados.setValorAcrescimo( 			rs.getString("VALOR_ACRESCIMO"));
		Dados.setValorMaximo( 			rs.getString("VALOR_MAXIMO"));
	}	
	
	public void testeInserirBigDecimal(String valorBigDecimal, String valorFloat ) throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String sql = "INSERT INTO TESTE_BIGDECIMAL(TIPO, VALOR1, VALOR2) VALUES (?, ?, ?)";
		ps.adicionarString("String");
		ps.adicionarBigDecimal(valorBigDecimal);
		ps.adicionarDecimal(valorFloat);
		executarUpdateDelete(sql, ps);				
	}
	
	public void testeInserirBigDecimal(BigDecimal valorBigDecimal, double valorDouble) throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String sql = "INSERT INTO TESTE_BIGDECIMAL(TIPO, VALOR1, VALOR2) VALUES (?, ?, ?)";
		ps.adicionarString("Inteiro");
		ps.adicionarBigDecimal(valorBigDecimal);
		ps.adicionarDecimal(String.valueOf(valorDouble));
		executarUpdateDelete(sql, ps);		
	}
	
	public void testeCalculo(String valor1, String valor2) throws Exception{
		BigDecimal valor1bd = new BigDecimal(Funcoes.BancoDecimal(valor1));
		BigDecimal valor2bd = new BigDecimal(Funcoes.BancoDecimal(valor2));
		
		BigDecimal resultado = valor1bd.add(valor2bd);
		
		resultado = resultado.divide(new BigDecimal(Funcoes.BancoDecimal("0,36")), BigDecimal.ROUND_UP); // Arredondando para cima considenrando 4 casas decimais, evitando erro de java.lang.ArithmeticException.
		resultado = resultado.multiply(new BigDecimal(Funcoes.BancoDecimal("9,36")));		
		resultado = resultado.subtract(new BigDecimal(Funcoes.BancoDecimal("10.2563,36")));
		
		resultado = resultado.pow(2);		
			
		String valor = Funcoes.FormatarBigDecimal(resultado.toString());
		
		testeInserirBigDecimal(valor, valor);
	}
}
