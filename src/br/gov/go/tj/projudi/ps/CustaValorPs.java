package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.CustaDt;
import br.gov.go.tj.projudi.dt.CustaValorDt;
import br.gov.go.tj.projudi.dt.GuiaItemDt;

import java.math.BigDecimal;
import java.sql.Connection;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

//---------------------------------------------------------
public class CustaValorPs extends CustaValorPsGen{

//

	/**
     * 
     */
    private static final long serialVersionUID = 3490253425639323409L;

    public CustaValorPs(Connection conexao){
    	Conexao = conexao;
	}
    
    public void alterar(CustaValorDt dados) throws Exception {

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		stSql= "UPDATE projudi.CUSTA_VALOR SET  ";
//		stSql+= "CUSTA_VALOR = ?";		 ps.adicionarString(dados.getCustaValor());  
//
//		stSql+= ",CUSTA_VALOR_CODIGO = ?";		 ps.adicionarLong(dados.getCustaValorCodigo());  

		stSql+= "LIMITE_MIN = ?";		 ps.adicionarDecimal(dados.getLimiteMin());  

		stSql+= ",LIMITE_MAX = ?";		 ps.adicionarDecimal(dados.getLimiteMax());  

		stSql+= ",VALOR_CUSTA = ?";		 ps.adicionarDecimal(dados.getValorCusta());  

		stSql+= ",ID_CUSTA = ?";		 ps.adicionarLong(dados.getId_Custa());  

		stSql += " WHERE ID_CUSTA_VALOR  = ? "; 		ps.adicionarLong(dados.getId()); 
		
		executarUpdateDelete(stSql, ps);
		
	
	}

	public List consultarPorDescricao(String descricao, String idCusta, String posicao ) throws Exception {

		StringBuffer Sql = new StringBuffer();		
		List liTemp = new ArrayList();
		ResultSetTJGO rs = null;
		String sqlFrom = " FROM projudi.VIEW_CUSTA_VALOR ";
		String ComplementoSQL = " WHERE ";
		String sqlWhere = " CUSTA_VALOR LIKE ? ";
		String sqlAnd = " ID_CUSTA = ? ";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql.append("SELECT ID_CUSTA_VALOR, CUSTA_VALOR, LIMITE_MIN, LIMITE_MAX, VALOR_CUSTA, CUSTA " + sqlFrom);
		
		if( !descricao.equals("") ){
			Sql.append(ComplementoSQL + sqlWhere);
			ps.adicionarString( descricao +"%");
			ComplementoSQL = " AND ";
		}
		if( idCusta != null && !idCusta.equals("") ){
			Sql.append(ComplementoSQL + sqlAnd);
			ps.adicionarLong(idCusta);
			ComplementoSQL = " AND ";
		}
		
		Sql.append(" ORDER BY CUSTA_VALOR ");
		try{

			rs = consultarPaginacao(Sql.toString(), ps, posicao);

			while (rs.next()) {
				CustaValorDt obTemp = new CustaValorDt();
				
				obTemp.setId(rs.getString("ID_CUSTA_VALOR"));
				obTemp.setCustaValor(rs.getString("CUSTA_VALOR"));
				obTemp.setLimiteMin(rs.getString("LIMITE_MIN"));
				obTemp.setLimiteMax(rs.getString("LIMITE_MAX"));
				obTemp.setValorCusta(rs.getString("VALOR_CUSTA"));
				obTemp.setCusta(rs.getString("CUSTA"));
				
				liTemp.add(obTemp);
			}
			
			ComplementoSQL = " WHERE ";
			Sql = new StringBuffer("SELECT COUNT(*) as QUANTIDADE " + sqlFrom);
			if( !descricao.equals("") ){
				Sql.append(ComplementoSQL + sqlWhere);
				ComplementoSQL = " AND ";
			}
			if( idCusta != null && !idCusta.equals("") ){
				Sql.append(ComplementoSQL + sqlAnd);
				ComplementoSQL = " AND ";
			}
			
			rs = consultar(Sql.toString(), ps);
			while (rs.next()) {
				liTemp.add(rs.getLong("QUANTIDADE"));
			}
		} finally {
			 if (rs != null) rs.close();
		}
		return liTemp; 
	}

	public String consultarPorDescricaoJSON(String descricao, String idCusta, String posicao ) throws Exception {

		StringBuffer Sql = new StringBuffer();		
		String stTemp = "";
		ResultSetTJGO rs = null;
		ResultSetTJGO rs2=null;
		String ComplementoSQL = " WHERE ";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 5;

		Sql.append("SELECT ID_CUSTA_VALOR AS ID, CUSTA_VALOR AS DESCRICAO1, 'R$ ' || LIMITE_MIN AS DESCRICAO2,  'R$ ' || LIMITE_MAX AS DESCRICAO3, 'R$ ' || VALOR_CUSTA AS DESCRICAO4, CUSTA AS DESCRICAO5 FROM projudi.VIEW_CUSTA_VALOR ");
		
		if( !descricao.equals("") ){
			Sql.append(ComplementoSQL + " CUSTA_VALOR LIKE ? ");
			ps.adicionarString( descricao +"%");
			ComplementoSQL = " AND ";
		}
		if( idCusta != null && !idCusta.equals("") ){
			Sql.append(ComplementoSQL + " ID_CUSTA = ? ");
			ps.adicionarLong(idCusta);
			ComplementoSQL = " AND ";
		}
		
		Sql.append(" ORDER BY CUSTA_VALOR ");
		try{
			rs = consultarPaginacao(Sql.toString(), ps, posicao);

			ComplementoSQL = " WHERE ";
			Sql = new StringBuffer("SELECT COUNT(*) as QUANTIDADE FROM projudi.VIEW_CUSTA_VALOR ");
			if( !descricao.equals("") ){
				Sql.append(ComplementoSQL + " CUSTA_VALOR LIKE ? ");
				ComplementoSQL = " AND ";
			}
			if( idCusta != null && !idCusta.equals("") ){
				Sql.append(ComplementoSQL + " ID_CUSTA = ? ");
				ComplementoSQL = " AND ";
			}
			
			rs2 = consultar(Sql.toString(), ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs, qtdeColunas);
		} finally {
			try{if (rs != null) rs.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return stTemp; 
	}
	
	/**
	 * Método de consulta do valor da custa.
	 * @param String idCusta
	 * @return GuiaItemDt
	 * @throws Exception
	 */
	public GuiaItemDt consultaValorCusta(String idCusta) throws Exception {
		GuiaItemDt guiaItemDt = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		ResultSetTJGO rs1 = null;
		try{
			if( idCusta.length() > 0 ) {
				
				String sql = "SELECT cv.ID_CUSTA_VALOR, cv.VALOR_CUSTA, c.ID_CUSTA FROM projudi.CUSTA c, projudi.CUSTA_VALOR cv WHERE cv.ID_CUSTA = c.ID_CUSTA ";
				sql += " AND c.ID_CUSTA = ? ";
				ps.adicionarLong(idCusta);
				
				rs1 = consultar(sql, ps);
				if( rs1 != null ) {
					while(rs1.next()) {
						guiaItemDt = new GuiaItemDt();
						guiaItemDt.setId(rs1.getString("ID_CUSTA_VALOR"));
						guiaItemDt.setValorReferencia(rs1.getString("VALOR_CUSTA"));
						
						CustaDt custaDt = new CustaDt();
						custaDt.setId(rs1.getString("ID_CUSTA"));
						
						guiaItemDt.setCustaDt(custaDt);
					}
				}
			}
		
		}
		finally{
			 rs1.close();
		}
		
		return guiaItemDt;
	}
	
	/**
	 * Método Genérico para consultar a lista de valores a serem cobrados de acordo com o intervalo do valor de referência que pode ser <<VALOR DA CAUSA>> ou <<VALOR DOS BENS>> ou <<OUTROS>>.
	 * @param List listaCustaDt
	 * @param String valorReferenciaCalculo
	 * @return List<GuiaItemDt>
	 * @throws Exception
	 */
	public List consultaValorIntevaloCusta(List listaCustaDt, String valorReferenciaCalculo) throws Exception {
		List listaGuiaItemDt = null;
		
		if( valorReferenciaCalculo == null || (valorReferenciaCalculo.length() > 0 && Funcoes.StringToDouble(Funcoes.BancoDecimal(valorReferenciaCalculo)) == 0.00) ) {
			valorReferenciaCalculo = "0,01";
		}
		
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		List listaCustaDt_REFERENCIA_DIFERENTE = null;
		boolean itensReferenciaFinal = false; //se item faz referência a ele mesmo, ou seja, não referência a outro item para calculo e valor
		boolean itemRegimentoNovo = false;
		try{
			if( listaCustaDt != null && listaCustaDt.size() > 0 ) {
				
//				for(int k = 0; k < listaCustaDt.size(); k++ ) {
					ps.limpar();
					
					String sql = "SELECT cv.ID_CUSTA_VALOR, cv.VALOR_CUSTA, c.ID_CUSTA FROM projudi.CUSTA c, projudi.CUSTA_VALOR cv WHERE cv.ID_CUSTA = c.ID_CUSTA ";
					sql += " AND c.CODIGO_REGIMENTO IN (";
					boolean primeiroItem = true;
					for(int i = 0; i < listaCustaDt.size(); i++ ) {
						CustaDt custaDt = (CustaDt)listaCustaDt.get(i);
						
						if( custaDt != null && custaDt.getId() != null && Funcoes.StringToDouble(custaDt.getId()) > 9000 ) {
							itemRegimentoNovo = true;
						}
						
						//ATENÇÃO: Caso faça referência a outra outra Custa Pai
						if( custaDt.getCodigoRegimento().equals( custaDt.getCodigoRegimentoValor() ) ) {
							if (!primeiroItem) sql += ",";
							sql += "?";
							ps.adicionarString(custaDt.getCodigoRegimento());												
							itensReferenciaFinal = true;
							primeiroItem = false;
						}
						else {
							if( listaCustaDt_REFERENCIA_DIFERENTE == null ) {
								listaCustaDt_REFERENCIA_DIFERENTE = new ArrayList();
							}
							listaCustaDt_REFERENCIA_DIFERENTE.add(custaDt);
						}
					}
					sql += ")";
					if( valorReferenciaCalculo != null && valorReferenciaCalculo.length() > 0 ){
						sql += " AND ((cv.LIMITE_MIN < ? AND cv.LIMITE_MAX > ?) OR (cv.LIMITE_MAX = ?)) ";
						ps.adicionarDecimal(valorReferenciaCalculo);
						ps.adicionarDecimal(valorReferenciaCalculo);
						ps.adicionarDecimal(valorReferenciaCalculo);
					}
					if( itemRegimentoNovo ) {
						sql += " AND c.ID_CUSTA >= 9000 ";
					}
									
					if( itensReferenciaFinal ) {
						rs1 = consultar(sql, ps);
						if( rs1 != null ) {
							while(rs1.next()) {
								if( listaGuiaItemDt == null )
									listaGuiaItemDt = new ArrayList();
								
								GuiaItemDt guiaItemDt = new GuiaItemDt();
								guiaItemDt.setId(rs1.getString("ID_CUSTA_VALOR"));
								guiaItemDt.setValorReferencia(rs1.getString("VALOR_CUSTA"));
								
								CustaDt custaDt = new CustaDt();
								custaDt.setId(rs1.getString("ID_CUSTA"));
								
								guiaItemDt.setCustaDt(custaDt);
								
								listaGuiaItemDt.add( guiaItemDt );
							}
						}
					}
					
					if( listaCustaDt_REFERENCIA_DIFERENTE != null && listaCustaDt_REFERENCIA_DIFERENTE.size() > 0 ) {					
						for( int i =0; i < listaCustaDt_REFERENCIA_DIFERENTE.size(); i++ ) {
							ps.limpar();
							CustaDt custaDt = (CustaDt)listaCustaDt_REFERENCIA_DIFERENTE.get(i);
							String sql2 = "SELECT cv.ID_CUSTA_VALOR, (cv.VALOR_CUSTA * (? / ?)) AS VALOR_CUSTA, c.ID_CUSTA FROM PROJUDI.CUSTA c, projudi.CUSTA_VALOR cv WHERE ";
							ps.adicionarDecimal(Funcoes.FormatarDecimal(custaDt.getPorcentagem()));
							ps.adicionarLong(100);
							sql2 += "c.CODIGO_REGIMENTO = ? AND cv.ID_CUSTA = c.ID_CUSTA ";
							ps.adicionarString(custaDt.getCodigoRegimentoValor());
							if( valorReferenciaCalculo != null && valorReferenciaCalculo.length() > 0 ){
								sql2 += " AND ((cv.LIMITE_MIN < ? AND cv.LIMITE_MAX > ?) OR (cv.LIMITE_MAX = ?)) ";
								ps.adicionarDecimal(valorReferenciaCalculo);
								ps.adicionarDecimal(valorReferenciaCalculo);
								ps.adicionarDecimal(valorReferenciaCalculo);
							}
							if( itemRegimentoNovo ) {
								sql2 += " AND c.ID_CUSTA >= 9000 ";
							}
							
							rs1 = consultar(sql2, ps);
							if( rs1 != null ) {
								while(rs1.next()) {
									if( listaGuiaItemDt == null )
										listaGuiaItemDt = new ArrayList();
									
									GuiaItemDt guiaItemDt = new GuiaItemDt();
									guiaItemDt.setId(rs1.getString("ID_CUSTA_VALOR"));
									guiaItemDt.setValorReferencia(rs1.getString("VALOR_CUSTA"));
									
									//ATENÇÃO: Pega o ID do custaDt original e não da referência de cálculo pai
									CustaDt custaDtAux = new CustaDt();
									custaDtAux.setId(custaDt.getId());
									
									guiaItemDt.setCustaDt(custaDtAux);
									
									listaGuiaItemDt.add(guiaItemDt );
								}
							}
						}
					}
					
					if( listaGuiaItemDt == null && listaCustaDt != null ) {
						for( int i = 0; i < listaCustaDt.size(); i++ ) {
							CustaDt custaDt = (CustaDt)listaCustaDt.get(i);
							
							if( custaDt != null && custaDt.getId() != null && custaDt.getCodigoRegimento().equals(custaDt.getCodigoRegimentoValor()) ) {
								
								ps.limpar();
								
								String sql2 = "SELECT (? * (? / ?)) AS VALOR_CUSTA, c.ID_CUSTA FROM PROJUDI.CUSTA c WHERE ";
								ps.adicionarDecimal(valorReferenciaCalculo);
								ps.adicionarDecimal(Funcoes.FormatarDecimal(custaDt.getPorcentagem()));
								ps.adicionarLong(100);
								
								sql2 += " c.CODIGO_REGIMENTO = ? ";
								ps.adicionarString(custaDt.getCodigoRegimentoValor());
								
								if( itemRegimentoNovo ) {
									sql2 += " AND c.ID_CUSTA >= 9000 ";
								}
								
								rs1 = consultar(sql2, ps);
								if( rs1 != null ) {
									while(rs1.next()) {
										if( listaGuiaItemDt == null )
											listaGuiaItemDt = new ArrayList();
										
										GuiaItemDt guiaItemDt = new GuiaItemDt();
										
										guiaItemDt.setValorReferencia(rs1.getString("VALOR_CUSTA"));
										
										//ATENÇÃO: Pega o ID do custaDt original e não da referência de cálculo pai
										CustaDt custaDtAux = new CustaDt();
										custaDtAux.setId(custaDt.getId());
										
										guiaItemDt.setCustaDt(custaDtAux);
										
										listaGuiaItemDt.add(guiaItemDt );
									}
								}
							}
						}
					}
//				}
			}
		
		}
		finally{
			 rs1.close();
		}
		
		return listaGuiaItemDt;
	}
	
	/**
	 * Método para consultar valor específico de um dado código de regimento.
	 * @param String codigoRegimento
	 * @return String valor
	 * @throws Exception
	 */
	public String consultaValorEspecificoCodigoRegimento(String codigoRegimento) throws Exception {
		String valor = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		ResultSetTJGO rs1 = null;
		try{
			if( codigoRegimento.length() > 0 ) {
				
				String sql = "SELECT cv.VALOR_CUSTA VALOR_CUSTA FROM projudi.CUSTA_VALOR cv, projudi.CUSTA c WHERE c.ID_CUSTA = cv.ID_CUSTA AND c.CODIGO_REGIMENTO = ?";
				ps.adicionarString(codigoRegimento);
				
				rs1 = consultar(sql, ps);
				if( rs1 != null ) {
					while(rs1.next()) {
						valor = rs1.getString("VALOR_CUSTA");
					}
				}
			}
		
		}
		finally{
			 rs1.close();
		}
		
		return valor;
	}
}
