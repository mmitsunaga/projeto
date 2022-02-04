package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.ProcessoTipoProcessoSubtipoDt;
import java.sql.Connection;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

//---------------------------------------------------------
public class ProcessoTipoProcessoSubtipoPs extends ProcessoTipoProcessoSubtipoPsGen{

	/**
     * 
     */
    private static final long serialVersionUID = -1759422599480667633L;

    public ProcessoTipoProcessoSubtipoPs(Connection conexao){
    	Conexao = conexao;
	}

	public boolean consultarProcessoTipoCodigoProcessoSubtipoCodigo(int processoTipoCodigo, int processoSubtipoCodigo)  throws Exception {

		String sql;
		boolean retorno = false;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		

		sql= "SELECT * FROM PROJUDI.VIEW_PROC_TIPO_PROC_SUBTIPO WHERE PROC_SUBTIPO_CODIGO = ?";
		ps.adicionarLong(processoSubtipoCodigo);
		sql += " AND PROC_TIPO_CODIGO = ?";
		ps.adicionarLong(processoTipoCodigo);

		try{
			rs1 = consultar(sql,ps);
			if (rs1.next()) {
				retorno = true;
			} 
		}finally{
				try{
					if (rs1 != null) 
						rs1.close();
					}
				catch(Exception e) {
					
				}
			}
			return retorno; 
	}
  
  //---------------------------------------------------------
	public List consultarProcessoTipoProcessoSubtipoGeral(String id_processosubtipo ) throws Exception {

		String Sql;
		List liTemp = new ArrayList();
		ResultSetTJGO rs=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql= "SELECT t2.ID_PROC_TIPO_PROC_SUBTIPO, t1.ID_PROC_TIPO, t1.PROC_TIPO, t3.ID_PROC_SUBTIPO, t3.PROC_SUBTIPO";
		Sql+= " FROM PROJUDI.PROC_TIPO t1 ";
		Sql+= " LEFT JOIN PROJUDI.PROC_TIPO_PROC_SUBTIPO t2 ON t1.ID_PROC_TIPO = t2.ID_PROC_TIPO AND t2.ID_PROC_SUBTIPO = ?";
		ps.adicionarLong(id_processosubtipo);
		Sql+= " LEFT JOIN PROJUDI.PROC_SUBTIPO t3 ON t3.ID_PROC_SUBTIPO = t2.ID_PROC_SUBTIPO";
		try{


			rs = consultar(Sql, ps);


			while (rs.next()) {
				ProcessoTipoProcessoSubtipoDt obTemp = new ProcessoTipoProcessoSubtipoDt();
				obTemp.setId(rs.getString("ID_PROC_TIPO_PROC_SUBTIPO"));
				obTemp.setProcessoSubtipo(rs.getString("PROC_SUBTIPO"));
				obTemp.setId_ProcessoTipo (rs.getString("ID_PROC_TIPO"));
				obTemp.setProcessoTipo(rs.getString("PROC_TIPO"));
				obTemp.setId_ProcessoSubtipo (id_processosubtipo);
				obTemp.setProcessoSubtipo(rs.getString("PROC_SUBTIPO"));
				liTemp.add(obTemp);
			}

		}finally{
				try{if (rs != null) rs.close();} catch(Exception e) {}
			}
			return liTemp; 
	}

}
