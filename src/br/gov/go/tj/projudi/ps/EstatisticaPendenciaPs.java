package br.gov.go.tj.projudi.ps;

import java.text.NumberFormat;

import br.gov.go.tj.projudi.dt.relatorios.DetalhesPendenciaDt;
import br.gov.go.tj.projudi.dt.relatorios.EstatisticaPendenciaDt;
import java.sql.Connection;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class EstatisticaPendenciaPs extends Persistencia {
	/**
     * 
     */
    private static final long serialVersionUID = -5443301562845179285L;
    //private static final long UM_DIAS_EM_SEGUNDOS = 60L * 60  * 24;

public EstatisticaPendenciaPs(Connection conexao){
		Conexao = conexao;
	}
	//---------------------------------------------------------
	public EstatisticaPendenciaDt consultarDadosEstatisticaPendenciaServentia(EstatisticaPendenciaDt pendencia) throws Exception {

		String Sql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();	
		
		Sql =  " SELECT DISTINCT "; 
		Sql += " 	vpe.ID_PEND_TIPO, vpe.PEND, "; 
		Sql += " 	(SELECT COUNT(*) ";
		Sql += " 		FROM VIEW_PEND_ESTATISTICA pe ";
		Sql += " 		WHERE  pe.ID_PEND_TIPO = vpe.ID_PEND_TIPO AND pe.ID_SERV = ? ";
		ps.adicionarLong(pendencia.getId_Serventia());
					if (!pendencia.getDataInicial().equalsIgnoreCase("")){
						Sql += " AND pe.DATA_INICIO  >= ? ";
						ps.adicionarDate(pendencia.getDataInicial());
					}						
					if (!pendencia.getDataFinal().equalsIgnoreCase("")){
						Sql += " AND pe.DATA_INICIO  < ? ";
						ps.adicionarDate(pendencia.getDataFinal());
					}						
		Sql += "	) AS QUANTIDADE_TOTAL, ";
		Sql += " 	(SELECT COUNT(*) ";
		Sql += "    	FROM VIEW_PEND_ESTATISTICA pe ";
		Sql += "    	WHERE  pe.ID_PEND_TIPO = vpe.ID_PEND_TIPO AND pe.DATA_FIM IS NOT NULL AND pe.ID_SERV = ?";
		ps.adicionarLong(pendencia.getId_Serventia());
					if (!pendencia.getDataInicial().equalsIgnoreCase("")){
						Sql += " AND pe.DATA_INICIO  >= ? ";
						ps.adicionarDate(pendencia.getDataInicial());
					}
					if (!pendencia.getDataFinal().equalsIgnoreCase("")){
						Sql += " AND pe.DATA_INICIO  < ? ";
						ps.adicionarDate(pendencia.getDataFinal());
					}
					Sql += "	) AS QUANTIDADE_FINALIZADA, ";
		Sql += " 	(SELECT COUNT(*) ";
		Sql += "     	FROM VIEW_PEND_ESTATISTICA pe ";
		Sql += "     	WHERE  pe.ID_PEND_TIPO = vpe.ID_PEND_TIPO AND pe.DATA_FIM IS NULL AND pe.ID_SERV = ?";
		ps.adicionarLong(pendencia.getId_Serventia());
					if (!pendencia.getDataInicial().equalsIgnoreCase("")){
						Sql += " AND pe.DATA_INICIO  >= ? ";
						ps.adicionarDate(pendencia.getDataInicial());
					}
					if (!pendencia.getDataFinal().equalsIgnoreCase("")){
						Sql += " AND pe.DATA_INICIO  < ? ";
						ps.adicionarDate(pendencia.getDataFinal());
					}					
					Sql += "	) AS QUANTIDADE_PENDENTE, ";
		Sql += " 	(SELECT AVG(DATA_FIM - DATA_INICIO) ";
		Sql += "    	FROM VIEW_PEND_ESTATISTICA pe ";
		Sql += "    	WHERE pe.ID_PEND_TIPO = vpe.ID_PEND_TIPO AND pe.ID_SERV = ?";
		ps.adicionarLong(pendencia.getId_Serventia());
					if (!pendencia.getDataInicial().equalsIgnoreCase("")){
						Sql += " AND pe.DATA_INICIO  >= ? ";
						ps.adicionarDate(pendencia.getDataInicial());
					}					
					if (!pendencia.getDataFinal().equalsIgnoreCase("")){
						Sql += " AND pe.DATA_INICIO  < ? ";
						ps.adicionarDate(pendencia.getDataFinal());
					}
					Sql += "	) AS MEDIAS_DIAS ";
		Sql += " FROM ";
		Sql += "	VIEW_PEND_ESTATISTICA vpe ";
		Sql += " WHERE ";
		Sql += "	vpe.ID_SERV = ? ";
		ps.adicionarLong(pendencia.getId_Serventia());
		if (!pendencia.getDataInicial().equalsIgnoreCase("")){
			Sql += " AND vpe.DATA_INICIO  >= ? ";
			ps.adicionarDate(pendencia.getDataInicial());	
		}		
		if (!pendencia.getDataFinal().equalsIgnoreCase("")){
			Sql += " AND vpe.DATA_INICIO  < ? ";
			ps.adicionarDate(pendencia.getDataFinal());
		}
		Sql += " GROUP BY vpe.ID_PEND_TIPO, vpe.PEND";
        Sql += " ORDER BY vpe.PEND";
		try{
			rs1 = consultar(Sql, ps);
			pendencia.setListaDetalhesPendenciaServentia(null);
			while (rs1.next()) {
				double mediaDias = 0.00;
				DetalhesPendenciaDt detalhesPendenciaServentia = new DetalhesPendenciaDt();
				detalhesPendenciaServentia.setPendenciaTipo(rs1.getString("PEND"));
				detalhesPendenciaServentia.setQtdTotal(rs1.getString("QUANTIDADE_TOTAL"));
				detalhesPendenciaServentia.setQtdFinalizada(rs1.getString("QUANTIDADE_FINALIZADA"));
				detalhesPendenciaServentia.setQtdPendente(rs1.getString("QUANTIDADE_PENDENTE"));
				detalhesPendenciaServentia.setPercFinalizada(Funcoes.getPercentual(rs1.getLong("QUANTIDADE_FINALIZADA"), rs1.getLong("QUANTIDADE_TOTAL")));
				
				mediaDias = rs1.getDouble("MEDIAS_DIAS");				
				NumberFormat formatadorDeNumeros = NumberFormat.getInstance();
				formatadorDeNumeros.setMaximumFractionDigits(2);
				detalhesPendenciaServentia.setTempMedioFinalizar(formatadorDeNumeros.format(mediaDias));
				
				pendencia.getListaDetalhesPendenciaServentia().add(detalhesPendenciaServentia);
			}
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        }  
		return pendencia;
	}
	
	public EstatisticaPendenciaDt consultarDadosEstatisticaPendenciaUsuario(EstatisticaPendenciaDt pendencia) throws Exception {

		String Sql;
		ResultSetTJGO rs1 = null;		
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		Sql =  " SELECT DISTINCT "; 
		Sql += " 	vpe.ID_PEND_TIPO, vpe.PEND, "; 
		Sql += " 	(SELECT COUNT(*) ";
		Sql += " 		FROM VIEW_PEND_ESTATISTICA pe ";
		Sql += " 		WHERE  pe.ID_PEND_TIPO = vpe.ID_PEND_TIPO AND pe.ID_USU = ?";
		ps.adicionarLong(pendencia.getUsuario().getId());
					if (!pendencia.getDataInicial().equalsIgnoreCase("")){
						Sql += " AND pe.DATA_INICIO  >= ?";
						ps.adicionarDate(pendencia.getDataInicial());						
					}						
					if (!pendencia.getDataFinal().equalsIgnoreCase("")){
						Sql += " AND pe.DATA_INICIO  < ?";
						ps.adicionarDate(pendencia.getDataFinal());
					}
					Sql += "	) AS QUANTIDADE_TOTAL, ";
		Sql += " 	(SELECT COUNT(*) ";
		Sql += "    	FROM VIEW_PEND_ESTATISTICA pe ";
		Sql += "    	WHERE  pe.ID_PEND_TIPO = vpe.ID_PEND_TIPO AND pe.DATA_FIM IS NOT NULL AND pe.ID_USU = ?";
		ps.adicionarLong(pendencia.getUsuario().getId());
					if (!pendencia.getDataInicial().equalsIgnoreCase("")){
						Sql += " AND pe.DATA_INICIO  >= ?";
						ps.adicionarDate(pendencia.getDataInicial());	
					}
					if (!pendencia.getDataFinal().equalsIgnoreCase("")){
						Sql += " AND pe.DATA_INICIO  < ?";
						ps.adicionarDate(pendencia.getDataFinal());
					}
					Sql += "	) AS QUANTIDADE_FINALIZADA, ";
		Sql += " 	(SELECT COUNT(*) ";
		Sql += "     	FROM VIEW_PEND_ESTATISTICA pe ";
		Sql += "     	WHERE  pe.ID_PEND_TIPO = vpe.ID_PEND_TIPO AND pe.DATA_FIM IS NULL AND pe.ID_USU = ?";
		ps.adicionarLong(pendencia.getUsuario().getId());
					if (!pendencia.getDataInicial().equalsIgnoreCase("")){
						Sql += " AND pe.DATA_INICIO  >= ?";
						ps.adicionarDate(pendencia.getDataInicial());	
					}
					if (!pendencia.getDataFinal().equalsIgnoreCase("")){
						Sql += " AND pe.DATA_INICIO  < ?";
						ps.adicionarDate(pendencia.getDataFinal());
					}
					Sql += "	) AS QUANTIDADE_PENDENTE, ";
		Sql += " 	(SELECT AVG(DATA_FIM - DATA_INICIO) ";
		Sql += "    	FROM VIEW_PEND_ESTATISTICA pe ";
		Sql += "    	WHERE pe.ID_PEND_TIPO = vpe.ID_PEND_TIPO AND pe.ID_USU = ?";
		ps.adicionarLong(pendencia.getUsuario().getId());
					if (!pendencia.getDataInicial().equalsIgnoreCase("")){
						Sql += " AND pe.DATA_INICIO  >= ?";
						ps.adicionarDate(pendencia.getDataInicial());
					}						
					if (!pendencia.getDataFinal().equalsIgnoreCase("")){
						Sql += " AND pe.DATA_INICIO  < ?";
						ps.adicionarDate(pendencia.getDataFinal());
					}
					Sql += "	) AS MEDIAS_DIAS ";
		Sql += " FROM ";
		Sql += "	VIEW_PEND_ESTATISTICA vpe ";
		Sql += " WHERE ";
		Sql += "	vpe.ID_USU = ?";
		ps.adicionarLong(pendencia.getUsuario().getId());
        if (!pendencia.getDataInicial().equalsIgnoreCase("")){
			Sql += " AND vpe.DATA_INICIO  >= ?";
			ps.adicionarDate(pendencia.getDataInicial());
        }
		if (!pendencia.getDataFinal().equalsIgnoreCase("")){
			Sql += " AND vpe.DATA_INICIO  < ?";
			ps.adicionarDate(pendencia.getDataFinal());
		}
		Sql += " GROUP BY vpe.ID_PEND_TIPO, vpe.PEND";
		Sql += " ORDER BY vpe.PEND";

		try{
			rs1 = consultar(Sql, ps);
			pendencia.setListaDetalhesPendenciaServentia(null);
			while (rs1.next()) {
				double mediaDias = 0.00;
				DetalhesPendenciaDt detalhesPendenciaServentia = new DetalhesPendenciaDt();
				detalhesPendenciaServentia.setPendenciaTipo(rs1.getString("PEND"));
				detalhesPendenciaServentia.setQtdTotal(rs1.getString("QUANTIDADE_TOTAL"));
				detalhesPendenciaServentia.setQtdFinalizada(rs1.getString("QUANTIDADE_FINALIZADA"));
				detalhesPendenciaServentia.setQtdPendente(rs1.getString("QUANTIDADE_PENDENTE"));
				detalhesPendenciaServentia.setPercFinalizada(Funcoes.getPercentual(rs1.getLong("QUANTIDADE_FINALIZADA"), rs1.getLong("QUANTIDADE_TOTAL")));
				
				mediaDias = rs1.getDouble("MEDIAS_DIAS");				
				NumberFormat formatadorDeNumeros = NumberFormat.getInstance();
				formatadorDeNumeros.setMaximumFractionDigits(2);
				detalhesPendenciaServentia.setTempMedioFinalizar(formatadorDeNumeros.format(mediaDias));
				
				pendencia.getListaDetalhesPendenciaServentia().add(detalhesPendenciaServentia);
			}
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        }  
		return pendencia;
	}
	
	public EstatisticaPendenciaDt consultarDadosEstatisticaPendenciaUsuarioServentia(EstatisticaPendenciaDt pendencia) throws Exception {

		String Sql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
				
		Sql =  " SELECT DISTINCT "; 
		Sql += " 	vpe.ID_PEND_TIPO, vpe.PEND, "; 
		Sql += " 	(SELECT COUNT(*) ";
		Sql += " 		FROM VIEW_PEND_ESTATISTICA pe ";
		Sql += " 		WHERE  pe.ID_PEND_TIPO = vpe.ID_PEND_TIPO AND pe.ID_USU_SERV_RESP = ?";
		ps.adicionarLong(pendencia.getUsuario().getId_UsuarioServentia());
					if (!pendencia.getDataInicial().equalsIgnoreCase("")){
						Sql += " AND pe.DATA_INICIO  >= ?";				
						ps.adicionarDate(pendencia.getDataInicial());
					}
					if (!pendencia.getDataFinal().equalsIgnoreCase("")){
						Sql += " AND pe.DATA_INICIO  < ?";
						ps.adicionarDate(pendencia.getDataFinal());
					}
					Sql += "	) AS QUANTIDADE_TOTAL, ";
		Sql += " 	(SELECT COUNT(*) ";
		Sql += "    	FROM VIEW_PEND_ESTATISTICA pe ";
		Sql += "    	WHERE  pe.ID_PEND_TIPO = vpe.ID_PEND_TIPO AND pe.DATA_FIM IS NOT NULL AND pe.ID_USU_SERV_RESP = ?";
		ps.adicionarLong(pendencia.getUsuario().getId_UsuarioServentia());
					if (!pendencia.getDataInicial().equalsIgnoreCase("")){
						Sql += " AND pe.DATA_INICIO  >= ?";
						ps.adicionarDate(pendencia.getDataInicial());
					}
					if (!pendencia.getDataFinal().equalsIgnoreCase("")){
						Sql += " AND pe.DATA_INICIO  < ?";
						ps.adicionarDate(pendencia.getDataFinal());
					}
					Sql += "	) AS QUANTIDADE_FINALIZADA, ";
		Sql += " 	(SELECT COUNT(*) ";
		Sql += "     	FROM VIEW_PEND_ESTATISTICA pe ";
		Sql += "     	WHERE  pe.ID_PEND_TIPO = vpe.ID_PEND_TIPO AND pe.DATA_FIM IS NULL AND pe.ID_USU_SERV_RESP = ?";
		ps.adicionarLong(pendencia.getUsuario().getId_UsuarioServentia());
					if (!pendencia.getDataInicial().equalsIgnoreCase("")){
						Sql += " AND pe.DATA_INICIO  >= ?";
						ps.adicionarDate(pendencia.getDataInicial());
					}
					if (!pendencia.getDataFinal().equalsIgnoreCase("")){
						Sql += " AND pe.DATA_INICIO  < ?";
						ps.adicionarDate(pendencia.getDataFinal());
					}
					Sql += "	) AS QUANTIDADE_PENDENTE, ";
		Sql += " 	(SELECT AVG(DATA_FIM - DATA_INICIO) ";
		Sql += "    	FROM VIEW_PEND_ESTATISTICA pe ";
		Sql += "    	WHERE pe.ID_PEND_TIPO = vpe.ID_PEND_TIPO AND pe.ID_USU_SERV_RESP = ?";
		ps.adicionarLong(pendencia.getUsuario().getId_UsuarioServentia());
					if (!pendencia.getDataInicial().equalsIgnoreCase("")){
						Sql += " AND pe.DATA_INICIO  >= ?";
						ps.adicionarDate(pendencia.getDataInicial());
					}
					if (!pendencia.getDataFinal().equalsIgnoreCase("")){
						Sql += " AND pe.DATA_INICIO  < ?";
						ps.adicionarDate(pendencia.getDataFinal());
					}					
					Sql += "	) AS MEDIAS_DIAS ";
		Sql += " FROM ";
		Sql += "	VIEW_PEND_ESTATISTICA vpe ";
		Sql += " WHERE ";
		Sql += "	vpe.ID_USU_SERV_RESP = ?";
		ps.adicionarLong(pendencia.getUsuario().getId_UsuarioServentia());
		if (!pendencia.getDataInicial().equalsIgnoreCase("")){
			Sql += " AND vpe.DATA_INICIO  >= ?";
			ps.adicionarDate(pendencia.getDataInicial());
		}
		if (!pendencia.getDataFinal().equalsIgnoreCase("")){
			Sql += " AND vpe.DATA_INICIO  < ?";
			ps.adicionarDate(pendencia.getDataFinal());
		}
		Sql += " GROUP BY vpe.ID_PEND_TIPO, vpe.PEND";
		Sql += " ORDER BY vpe.PEND";
		try{
			rs1 = consultar(Sql, ps);
			pendencia.setListaDetalhesPendenciaServentia(null);
			while (rs1.next()) {
				double mediaDias = 0.00;
				DetalhesPendenciaDt detalhesPendenciaServentia = new DetalhesPendenciaDt();
				detalhesPendenciaServentia.setPendenciaTipo(rs1.getString("PEND"));
				detalhesPendenciaServentia.setQtdTotal(rs1.getString("QUANTIDADE_TOTAL"));
				detalhesPendenciaServentia.setQtdFinalizada(rs1.getString("QUANTIDADE_FINALIZADA"));
				detalhesPendenciaServentia.setQtdPendente(rs1.getString("QUANTIDADE_PENDENTE"));
				detalhesPendenciaServentia.setPercFinalizada(Funcoes.getPercentual(rs1.getLong("QUANTIDADE_FINALIZADA"), rs1.getLong("QUANTIDADE_TOTAL")));
				
				mediaDias = rs1.getDouble("MEDIAS_DIAS");				
				NumberFormat formatadorDeNumeros = NumberFormat.getInstance();
				formatadorDeNumeros.setMaximumFractionDigits(2);
				detalhesPendenciaServentia.setTempMedioFinalizar(formatadorDeNumeros.format(mediaDias));
				
				pendencia.getListaDetalhesPendenciaServentia().add(detalhesPendenciaServentia);
			}
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        }  
		return pendencia;
	}
	
} 
