package br.gov.go.tj.projudi.ps;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.gov.go.tj.projudi.dt.MovimentacaoComplementoDt;
import br.gov.go.tj.projudi.dt.MovimentacaoDt;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class MovimentacaoComplementoPs extends MovimentacaoComplementoPsGen {

    private static final long serialVersionUID = 20879048627150109L;
    private static final String ULTIMA_MOVIMENTACAO = "128024956"; //só para a primeira execução

    public MovimentacaoComplementoPs(Connection conexao){
    	Conexao = conexao;
	}

	/**
	 * Consultar Complementos de uma determinada movimentação
	 * 
	 * @param id_Movimentacao, identificação da movimentação
	 * @author acbloureiro
	 */
	public List consultarComplementosMovimentacao(String id_Movimentacao) throws Exception {
		List <MovimentacaoComplementoDt> Complementos = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String Sql = " SELECT m.id_movi_complemento, m.Id_Movi, m.Id_complemento, m.id_complemento_tabelado, m.valor_identificador, m.valor_texto  FROM PROJUDI.MOVI_COMPLEMENTO m";

		Sql += " WHERE m.ID_MOVI = ?  ";
		ps.adicionarLong(id_Movimentacao);

		try{
			rs1 = consultar(Sql, ps);

			while (rs1.next()) {
				if (Complementos == null) Complementos = new ArrayList <MovimentacaoComplementoDt>();
				
				MovimentacaoComplementoDt movimentacaoComplementoDt = new MovimentacaoComplementoDt();
				
				movimentacaoComplementoDt.setId(rs1.getString("ID_MOVI_COMPLEMENTO"));
				movimentacaoComplementoDt.setId_Complemento(rs1.getString("ID_COMPLEMENTO"));				 
				movimentacaoComplementoDt.setId_Movimentacao(rs1.getString("ID_MOVI"));
				movimentacaoComplementoDt.setId_ComplementoTabelado(rs1.getString("ID_COMPLEMENTO_TABELADO"));
				movimentacaoComplementoDt.setValorIdentificador(rs1.getString("VALOR_IDENTIFICADOR"));
				movimentacaoComplementoDt.setValorTexto(rs1.getString("VALOR_TEXTO"));

				Complementos.add(movimentacaoComplementoDt);
			}
			//rs1.close();

		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return Complementos;
	}
	/**
	 * Consultar Complementos de uma determinada movimentação 
	 * 
	 * @param id_Movimentacao, identificação da movimentação e id_Complemento
	 * @author acbloureiro
	 */
	public List consultarComplementosMovimentacao(String id_Movimentacao, String id_Complemento) throws Exception {
		List <MovimentacaoComplementoDt> Complementos = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String Sql = " SELECT m.id_movi_complemento, m.Id_Movi, m.Id_complemento, m.id_complemento_tabelado, m.valor_identificador, m.valor_texto  FROM PROJUDI.MOVI_COMPLEMENTO m";

		Sql += " WHERE m.ID_MOVI = ? and m.ID_COMPLEMENTO = ? ";
		ps.adicionarLong(id_Movimentacao);
		ps.adicionarLong(id_Complemento);

		try{
			rs1 = consultar(Sql, ps);

			while (rs1.next()) {
				if (Complementos == null) Complementos = new ArrayList <MovimentacaoComplementoDt>();
				
				MovimentacaoComplementoDt movimentacaoComplementoDt = new MovimentacaoComplementoDt();
				
				movimentacaoComplementoDt.setId(rs1.getString("ID_MOVI_COMPLEMENTO"));
				movimentacaoComplementoDt.setId_Complemento(rs1.getString("ID_COMPLEMENTO"));				 
				movimentacaoComplementoDt.setId_Movimentacao(rs1.getString("ID_MOVI"));
				movimentacaoComplementoDt.setId_ComplementoTabelado(rs1.getString("ID_COMPLEMENTO_TABELADO"));
				movimentacaoComplementoDt.setValorIdentificador(rs1.getString("VALOR_IDENTIFICADOR"));
				movimentacaoComplementoDt.setValorTexto(rs1.getString("VALOR_TEXTO"));

				Complementos.add(movimentacaoComplementoDt);
			}
			//rs1.close();

		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return Complementos;
	}
	
	/**
	 * Consultar ultima movimentação gerada
	 * 
	 * @param 
	 * @author acbloureiro
	 */
	public String consultarUltimoIdMov() throws Exception {
		String retorno = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String Sql = " SELECT max(Id_Movi) as LAST_MOVI FROM PROJUDI.MOVI_COMPLEMENTO WHERE ORIGEM is null";

		try{
			rs1 = consultar(Sql, ps);

			while (rs1.next()) {
				
				retorno = rs1.getString("LAST_MOVI");
					
			}
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        }
		
		if (retorno == null) retorno = ULTIMA_MOVIMENTACAO; //limitar a primeira execução
		
		return retorno;
	}
	
		
	public void inserir(MovimentacaoComplementoDt dados) throws Exception {

		String stSqlCampos="";				
		
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		//
		stSqlCampos= "INSERT INTO PROJUDI.MOVI_COMPLEMENTO (ID_COMPLEMENTO, ID_MOVI, ID_COMPLEMENTO_TABELADO, VALOR_IDENTIFICADOR, VALOR_TEXTO, ORIGEM) "; 
		
		stSqlCampos +=  " Values ( ?, ?, ?, ?, ?, ?)";
		
		 ps.adicionarLong(dados.getId_Complemento());  			
		 ps.adicionarLong(dados.getId_Movimentacao());		 
		 ps.adicionarLong(dados.getId_ComplementoTabelado());
		 ps.adicionarString(dados.getValorIdentificador());
		 ps.adicionarString(dados.getValorTexto());
		 ps.adicionarString(dados.getOrigem());
		
		dados.setId(executarInsert(stSqlCampos,"ID_MOVI_COMPLEMENTO",ps));
		 
	}
	
	public void alterar(MovimentacaoComplementoDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psMovimentacaoComplementoalterar()");

		stSql= "UPDATE PROJUDI.MOVI_COMPLEMENTO SET  ";
		stSql+= "ID_COMPLEMENTO = ?";		 			ps.adicionarLong(dados.getId_Complemento());  
		stSql+= ",ID_MOVI = ?";		 			ps.adicionarLong(dados.getId_Movimentacao());
		stSql+= ",ID_COMPLEMENTO_TABELADO = ?";		ps.adicionarLong(dados.getId_ComplementoTabelado());
		stSql+= ",VALOR_IDENTIFICADOR = ?";		ps.adicionarLong(dados.getValorIdentificador());
		stSql+= ",VALOR_TEXTO = ?";		ps.adicionarLong(dados.getValorTexto());
		stSql+= ",ORIGEM = ?";		ps.adicionarLong(dados.getOrigem());

		stSql += " WHERE ID_MOVI_COMPLEMENTO  = ? "; 	ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);
	
	} 
	
	public void excluir(String chave) throws Exception {

		String stSql = "";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSql = "DELETE FROM projudi.MOVI_COMPLEMENTO";
		stSql += " WHERE ID_MOVI_COMPLEMENTO = ?";
		ps.adicionarLong(chave);

			executarUpdateDelete(stSql, ps);

	}


}
