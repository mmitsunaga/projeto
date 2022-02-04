package br.gov.go.tj.projudi.ps;

import br.gov.go.tj.projudi.dt.relatorios.DetalhesMovimentacaoDt;
import br.gov.go.tj.projudi.dt.relatorios.EstatisticaMovimentacaoDt;
import java.sql.Connection;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class EstatisticaMovimentacaoPs extends Persistencia {
	
/**
     * 
     */
    private static final long serialVersionUID = 3091188441804654841L;

    public EstatisticaMovimentacaoPs(Connection conexao){
		Conexao = conexao;
	}

	public EstatisticaMovimentacaoDt consultarDadosEstatisticaMovimentacaoServentia(EstatisticaMovimentacaoDt movimentacao) throws Exception {

		String Sql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String id = movimentacao.getId_Serventia();
		
		Sql =  " SELECT DISTINCT "; 
		Sql += " 	 vme.ID_MOVI_TIPO, vme.MOVI_TIPO,  "; 
		Sql += " 	(SELECT COUNT(*) ";
		Sql += " 		FROM VIEW_MOV_ESTATISTICA me ";
		Sql += " 		WHERE  me.ID_MOVI_TIPO = vme.ID_MOVI_TIPO AND me.ID_SERV = ? ";
		ps.adicionarLong(id);
 					if (!movimentacao.getDataInicial().equalsIgnoreCase("")){
						Sql += " AND me.DATA_REALIZACAO  >= ? ";
						ps.adicionarDate(movimentacao.getDataInicial());
 					}
					if (!movimentacao.getDataFinal().equalsIgnoreCase("")){
						Sql += " AND me.DATA_REALIZACAO  < ? ";
						ps.adicionarDate(movimentacao.getDataFinal());
					}					
		Sql += "	) AS QUANTIDADE_TOTAL ";
		Sql += " FROM ";
		Sql += "	VIEW_MOV_ESTATISTICA vme ";
		Sql += " WHERE ";
		Sql += "	vme.ID_SERV = ? ";
		ps.adicionarLong(id);
		if (!movimentacao.getDataInicial().equalsIgnoreCase("")){
			Sql += " AND vme.DATA_REALIZACAO  >= ? ";
			ps.adicionarDate(movimentacao.getDataInicial());
		}
		if (!movimentacao.getDataFinal().equalsIgnoreCase("")){
			Sql += " AND vme.DATA_REALIZACAO  < ? ";
			ps.adicionarDate(movimentacao.getDataFinal());
		}
		Sql += " ORDER BY vme.MOVI_TIPO";
		try{
			rs1 = consultar(Sql, ps);
			movimentacao.setListaDetalhesMovimentacao(null);
			while (rs1.next()) {
				DetalhesMovimentacaoDt detalhesMovimentacao = new DetalhesMovimentacaoDt();
				detalhesMovimentacao.setMovimentacaoTipo(rs1.getString("MOVI_TIPO"));
				detalhesMovimentacao.setQtdTotal(rs1.getString("QUANTIDADE_TOTAL"));
				
				movimentacao.getListaDetalhesMovimentacao().add(detalhesMovimentacao);
			}
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        }  
		return movimentacao;
	}
	
	public EstatisticaMovimentacaoDt consultarDadosEstatisticaMovimentacaoUsuario(EstatisticaMovimentacaoDt movimentacao) throws Exception {

		String Sql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String id = movimentacao.getUsuario().getId();
		
		Sql =  " SELECT DISTINCT "; 
		Sql += " 	 vme.ID_MOVI_TIPO, vme.MOVI_TIPO,  "; 
		Sql += " 	(SELECT COUNT(*) ";
		Sql += " 		FROM VIEW_MOV_ESTATISTICA me ";
		Sql += " 		WHERE  me.ID_MOVI_TIPO = vme.ID_MOVI_TIPO AND me.ID_USU = ? ";
		ps.adicionarLong(id);
					if (!movimentacao.getDataInicial().equalsIgnoreCase("")){
						Sql += " AND me.DATA_REALIZACAO  >= ? ";
						ps.adicionarDate(movimentacao.getDataInicial());
					}
					if (!movimentacao.getDataFinal().equalsIgnoreCase("")){
						Sql += " AND me.DATA_REALIZACAO  < ? ";
						ps.adicionarDate(movimentacao.getDataFinal());
					}
					Sql += " ) AS QUANTIDADE_TOTAL ";
		Sql += " FROM ";
		Sql += "	VIEW_MOV_ESTATISTICA vme ";
		Sql += " WHERE ";
		Sql += "	vme.ID_USU = ? ";
		ps.adicionarLong(id);
        if (!movimentacao.getDataInicial().equalsIgnoreCase("")){
			Sql += " AND vme.DATA_REALIZACAO  >= ? ";
			ps.adicionarDate(movimentacao.getDataInicial());
        }
		if (!movimentacao.getDataFinal().equalsIgnoreCase("")){
			Sql += " AND vme.DATA_REALIZACAO  < ? ";
			ps.adicionarDate(movimentacao.getDataFinal());
		}
		 Sql += " ORDER BY vme.MOVI_TIPO";

		try{
			rs1 = consultar(Sql, ps);
			movimentacao.setListaDetalhesMovimentacao(null);
			while (rs1.next()) {
				DetalhesMovimentacaoDt detalhesMovimentacao = new DetalhesMovimentacaoDt();
				detalhesMovimentacao.setMovimentacaoTipo(rs1.getString("MOVI_TIPO"));
				detalhesMovimentacao.setQtdTotal(rs1.getString("QUANTIDADE_TOTAL"));
				movimentacao.getListaDetalhesMovimentacao().add(detalhesMovimentacao);
			}
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        }  
		return movimentacao;
	}
	
	public EstatisticaMovimentacaoDt consultarDadosEstatisticaMovimentacaoUsuarioServentia(EstatisticaMovimentacaoDt movimentacao) throws Exception {

		String Sql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String id = movimentacao.getUsuario().getId_UsuarioServentia();
		
		Sql =  " SELECT DISTINCT "; 
		Sql += " 	 vme.ID_MOVI_TIPO, vme.MOVI_TIPO,  ";  
		Sql += " 	(SELECT COUNT(*) ";
		Sql += " 		FROM VIEW_MOV_ESTATISTICA me ";
		Sql += " 		WHERE  me.ID_MOVI_TIPO = vme.ID_MOVI_TIPO AND me.ID_USU_REALIZADOR = ? ";
		ps.adicionarLong(id);
					if (!movimentacao.getDataInicial().equalsIgnoreCase("")){
						Sql += " AND me.DATA_REALIZACAO  >= ? ";
						ps.adicionarDate(movimentacao.getDataInicial());
					}
					if (!movimentacao.getDataFinal().equalsIgnoreCase("")){
						Sql += " AND me.DATA_REALIZACAO  < ? ";
						ps.adicionarDate(movimentacao.getDataFinal());
					}
					Sql += "	) AS QUANTIDADE_TOTAL ";
		Sql += " FROM ";
		Sql += "	VIEW_MOV_ESTATISTICA vme ";
		Sql += " WHERE ";
		Sql += "	vme.ID_USU_REALIZADOR = ? ";
		ps.adicionarLong(id);
		if (!movimentacao.getDataInicial().equalsIgnoreCase("")){
			Sql += " AND vme.DATA_REALIZACAO  >= ? ";
			ps.adicionarDate(movimentacao.getDataInicial());
		}
		if (!movimentacao.getDataFinal().equalsIgnoreCase("")){
			Sql += " AND vme.DATA_REALIZACAO  < ? ";
			ps.adicionarDate(movimentacao.getDataFinal());
		}
        Sql += " ORDER BY vme.MOVI_TIPO";
		try{
			rs1 = consultar(Sql, ps);
			movimentacao.setListaDetalhesMovimentacao(null);
			while (rs1.next()) {
				DetalhesMovimentacaoDt detalhesMovimentacao = new DetalhesMovimentacaoDt();
				detalhesMovimentacao.setMovimentacaoTipo(rs1.getString("MOVI_TIPO"));
				detalhesMovimentacao.setQtdTotal(rs1.getString("QUANTIDADE_TOTAL"));
				movimentacao.getListaDetalhesMovimentacao().add(detalhesMovimentacao);
			}
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        }  
		return movimentacao;
	}
	
} 
