package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.ServentiaSubtipoDt;
import br.gov.go.tj.projudi.dt.ServentiaTipoDt;
import br.gov.go.tj.projudi.dt.relatorios.RelatorioImplantacaoDt;
import java.sql.Connection;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class RelatorioImplantacaoPs extends Persistencia{
		
    private static final long serialVersionUID = 5043469610214961208L;

    public RelatorioImplantacaoPs(Connection conexao){
    	Conexao = conexao;
	}

	public List relImplantacaoServentias(String idComarca, String idServentiaTipo) throws Exception {

		List listaServentias = new ArrayList();
		StringBuffer sql = new StringBuffer();
		ResultSetTJGO rs = null;
		boolean clausulaWHERE = false;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		sql.append("SELECT COMARCA, SERV_TIPO, SERV FROM PROJUDI.VIEW_COMARCA_SERV ");

		if (idComarca != null && !idComarca.equals("") && !idComarca.equals("null")) {
			sql.append(" WHERE ");
			sql.append("ID_COMARCA = ? ");
			ps.adicionarLong(idComarca);
			clausulaWHERE = true;
		}
		
		if (idServentiaTipo != null && !idServentiaTipo.equals("") && !idServentiaTipo.equals("null")) {
			if(clausulaWHERE == true)
				sql.append(" AND ");
			else
				sql.append(" WHERE ");
			sql.append("ID_SERV_TIPO = ? ");
			ps.adicionarLong(idServentiaTipo);
		}
		
			sql.append(" ORDER BY COMARCA, SERV_TIPO, SERV");
		
		try{
			rs = consultar(sql.toString(), ps);
			while (rs.next()) {
				RelatorioImplantacaoDt obTemp = new RelatorioImplantacaoDt();
				obTemp.setComarca(rs.getString("COMARCA"));
				obTemp.setServentiaTipo(rs.getString("SERV_TIPO"));
				obTemp.setServentia(rs.getString("SERV"));
				listaServentias.add(obTemp);
			}
	
		
		} finally{
			try{
				if (rs != null) rs.close();
			} catch(Exception e) {
			}
		}
		return listaServentias;
	}
	
	public List relImplantacaoServentiasPublico() throws Exception {

		List listaServentias = new ArrayList();
		StringBuffer sql = new StringBuffer();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		sql.append("SELECT COMARCA, SERV, DATA_IMPLANTACAO FROM PROJUDI.VIEW_SERV "
				+ "		WHERE ID_SERV_TIPO = ? "); ps.adicionarLong(ServentiaTipoDt.VARA);
		sql.append(" 		AND CODIGO_TEMP = ? "); ps.adicionarLong(ServentiaDt.ATIVO);
		sql.append(" 		AND SERV_SUBTIPO_CODIGO NOT IN (?,?,?) "); ps.adicionarLong(ServentiaSubtipoDt.EXECPENWEB); ps.adicionarLong(ServentiaSubtipoDt.PREPROCESSUAL); ps.adicionarLong(ServentiaSubtipoDt.PLANTAO_SEGUNDO_GRAU); 	
		sql.append(" ORDER BY COMARCA, SERV");
		
		try{
			rs = consultar(sql.toString(), ps);
			while (rs.next()) {
				RelatorioImplantacaoDt obTemp = new RelatorioImplantacaoDt();
				obTemp.setComarca(rs.getString("COMARCA"));
				obTemp.setServentia(rs.getString("SERV"));
				obTemp.setData(rs.getDateTime("DATA_IMPLANTACAO"));
				listaServentias.add(obTemp);
			}
	
		} finally{
			try{
				if (rs != null) rs.close();
			} catch(Exception e) {
			}
		}
		return listaServentias;
	}
}
