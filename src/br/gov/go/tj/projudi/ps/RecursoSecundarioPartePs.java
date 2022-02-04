package br.gov.go.tj.projudi.ps;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.PendenciaDt;
import br.gov.go.tj.projudi.dt.RecursoSecundarioParteDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class RecursoSecundarioPartePs extends Persistencia {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1940076549690313426L;
	
	public RecursoSecundarioPartePs(Connection conexao) {
		this.Conexao = conexao;
	}
	
	public String inserir(RecursoSecundarioParteDt ob) throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
	
		String sql = "INSERT INTO PROJUDI.RECURSO_SECUNDARIO_PARTE (ID_PROC_PARTE, ID_PROC_PARTE_TIPO, ID_PROC_TIPO, ID_AUDI_PROC, ID_PROC_TIPO_RECURSO_SEC, ORDEM_PARTE) VALUES (?, ?, ?, ?, ?, ?)";

		ps.adicionarLong(ob.getId_ProcessoParte());
		ps.adicionarLong(ob.getId_ProcessoParteTipo());
		ps.adicionarLong(ob.getId_ProcessoTipo());
		ps.adicionarLong(ob.getId_AudienciaProcesso());
		ps.adicionarLong(ob.getId_ProcessoTipoRecursoSecundario());
		ps.adicionarLong(ob.getOrdemParte());
		
		return executarInsert(sql, "ID_RECURSO_SECUNDARIO_PARTE", ps);
	}
	
	public List<RecursoSecundarioParteDt> consultarPorAudienciaProcesso(String idAudienciaProcesso) throws Exception{
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ResultSetTJGO rs1 = null;
		ArrayList<RecursoSecundarioParteDt> res = new ArrayList<RecursoSecundarioParteDt>();
		
		String sql = "SELECT * FROM RECURSO_SECUNDARIO_PARTE WHERE ID_AUDI_PROC = ?";
		ps.adicionarLong(idAudienciaProcesso);
		
		try {
			rs1 = this.consultar(sql, ps);

			while (rs1.next()) {
				RecursoSecundarioParteDt dt = new RecursoSecundarioParteDt();
				
				dt.setId(rs1.getString("ID_RECURSO_SECUNDARIO_PARTE"));
				dt.setId_ProcessoParte(rs1.getString("ID_PROC_PARTE"));
				dt.setId_ProcessoParteTipo(rs1.getString("ID_PROC_PARTE_TIPO"));
				dt.setId_ProcessoTipo(rs1.getString("ID_PROC_TIPO"));
				dt.setId_AudienciaProcesso(rs1.getString("ID_AUDI_PROC"));
				dt.setId_ProcessoTipoRecursoSecundario(rs1.getString("ID_PROC_TIPO_RECURSO_SEC"));
				dt.setOrdemParte(rs1.getString("ORDEM_PARTE"));
				
				res.add(dt);
			}
		} finally {
			try{ if(rs1 != null) rs1.close(); } catch (Exception e) {}
		}
		
		return res;
	}

}
