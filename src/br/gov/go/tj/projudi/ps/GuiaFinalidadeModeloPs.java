package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.GuiaFinalidadeModeloDt;
import br.gov.go.tj.projudi.ne.FinalidadeNe;
import br.gov.go.tj.projudi.ne.GuiaTipoNe;
import java.sql.Connection;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class GuiaFinalidadeModeloPs extends Persistencia {

	private static final long serialVersionUID = 2510040649304072003L;
	
	@SuppressWarnings("unused")
	private GuiaFinalidadeModeloPs( ) {
		
	}
	
	public GuiaFinalidadeModeloPs(Connection conexao){
		Conexao = conexao;
	}

	protected void associarDt( GuiaFinalidadeModeloDt Dados, ResultSetTJGO rs )  throws Exception {
		 		
		Dados.setId(rs.getString("ID_GUIA_FINALIDADE_MODELO"));
		Dados.setGuiaFinalidadeModelo(rs.getString("GUIA_FINALIDADE_MODELO"));
		Dados.setGuiaFinalidadeModeloCodigo( rs.getString("GUIA_FINALIDADE_MODELO_CODIGO"));
		Dados.setGuiaTipoDt(new GuiaTipoNe().consultarId(rs.getString("ID_GUIA_TIPO")));
		Dados.setFinalidadeDt(new FinalidadeNe().consultarId(rs.getString("ID_FINALIDADE")));
		Dados.setAcrescimoLocomocao( rs.getString("ACRESCIMO_LOCOMOCAO"));
		Dados.setPenhoraLocomocao( rs.getString("PENHORA_LOCOMOCAO"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
		
	}

	/**
	 * Método para consultar lista de GuiaFinalidadeModelo pelo idGuiaTipo.
	 * @param String idGuiaTipo
	 * @param String idProcessoTipo
	 * @return List
	 * @throws Exception
	 */
	public List consultarGuiaFinalidadeModelo(String idGuiaTipo)  throws Exception {
		
		String Sql;
		ResultSetTJGO rs1 = null;
		List listaGuiaFinalidadeModeloDt = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		String ClausulaWhere = " WHERE ";
		
		Sql= "SELECT * FROM projudi.VIEW_GUIA_FINALIDADE_MODELO ";
		if( idGuiaTipo != null ) {
			Sql += ClausulaWhere + " ID_GUIA_TIPO = ?";
			ps.adicionarLong(idGuiaTipo);
		}

		try{
			rs1 = consultar(Sql, ps);
			if( rs1 != null ) {
				while(rs1.next()) {
					if( listaGuiaFinalidadeModeloDt == null ) {
						listaGuiaFinalidadeModeloDt = new ArrayList();
					}
					
					GuiaFinalidadeModeloDt guiaFinalidadeModeloDt = new GuiaFinalidadeModeloDt();
					
					guiaFinalidadeModeloDt.setId(rs1.getString("ID_GUIA_FINALIDADE_MODELO"));
					
					listaGuiaFinalidadeModeloDt.add(guiaFinalidadeModeloDt);
				}
			}
		
		}
		finally{
			 rs1.close();
		}
		return listaGuiaFinalidadeModeloDt; 
	}
	
	/**
	 * Método para consultar GuiaFinalidadeModeloDt pelo idGuiaTipo e idFinalidade.
	 * @param String idGuiaTipo
	 * @param String idProcessoTipo
	 * @return List
	 * @throws Exception
	 */
	public GuiaFinalidadeModeloDt consultarGuiaFinalidadeModelo(String idGuiaTipo, String idFinalidade)  throws Exception {
		
		String Sql;
		ResultSetTJGO rs1 = null;
		GuiaFinalidadeModeloDt Dados=null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		String ClausulaWhere = " WHERE ";
		
		Sql= "SELECT * FROM projudi.VIEW_GUIA_FINALIDADE_MODELO ";
		if( idGuiaTipo != null ) {
			Sql += ClausulaWhere + " ID_GUIA_TIPO = ?";
			ps.adicionarLong(idGuiaTipo);
			ClausulaWhere = " AND ";
		}
		if( idFinalidade != null ) {
			Sql += ClausulaWhere + " ID_FINALIDADE = ?";
			ps.adicionarLong(idFinalidade);
		}

		try{
			rs1 = consultar(Sql,ps);
			if (rs1.next()) {
				Dados= new GuiaFinalidadeModeloDt();
				this.associarDt(Dados, rs1);
			}
		
		}
		finally{
			 rs1.close();
		}
		return Dados; 
	}
}
