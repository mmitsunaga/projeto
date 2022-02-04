package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.gov.go.tj.projudi.dt.PendenciaTipoRelacionadaDt;
import java.sql.Connection;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

//---------------------------------------------------------
public class PendenciaTipoRelacionadaPs extends PendenciaTipoRelacionadaPsGen {

	/**
     * 
     */
    private static final long serialVersionUID = -7587531379261716420L;

    public PendenciaTipoRelacionadaPs(Connection conexao){
    	Conexao = conexao;
	}

	public Map consultaPendenciaTipoRelacionadas() throws Exception {
		Map mapPendencias = new HashMap();
		String stSql;
		ResultSetTJGO rs1 = null;
		String id_PendenciaPrincipal = "";
		String id_PendenciaAnterior = "";
		List pendenciasRelacionadas = null;
		try{
			stSql = "SELECT * FROM PROJUDI.VIEW_PEND_TIPO_RELACIONADA ORDER BY ID_PEND_TIPO_PRINC";
			rs1 = consultarSemParametros(stSql);

			while (rs1.next()) {
				id_PendenciaPrincipal = rs1.getString("PEND_TIPO_CODIGO_PRINC");

				//Se mudou o tipo da pendência principal deve iniciar outra lista
				if (!id_PendenciaPrincipal.equals(id_PendenciaAnterior)) {
					pendenciasRelacionadas = new ArrayList();
					mapPendencias.put(id_PendenciaPrincipal, pendenciasRelacionadas);
					
				}
				pendenciasRelacionadas.add(rs1.getString("PEND_TIPO_CODIGO_RELACAO"));

				id_PendenciaAnterior = rs1.getString("PEND_TIPO_CODIGO_PRINC");
			}

		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return mapPendencias;
	}
	
    /*
     Este método foi criado com o intuito de retornar uma lista ordenada pelo atributo PendenciaTipo     
	 @Autor Márcio Gomes
	 @Data 27/08/2010
    */
	public List consultarPendenciaTipoPendenciaTipoGeral(String id_pendenciatipoprincipal ) throws Exception {

		String Sql;
		List liTemp = new ArrayList();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		//System.out.println("..ps-consultarPendenciaTipoPendenciaTipoGeralPendenciaTipoRelacionada()");

		Sql= "SELECT t2.ID_PEND_TIPO_RELACIONADA, t1.ID_PEND_TIPO as ID_PEND_TIPO_REL, t1.PEND_TIPO as PEND_TIPO_RELACAO, t3.ID_PEND_TIPO as ID_PEND_TIPO_PRINC, t3.PEND_TIPO as PEND_TIPO_PRINC";
		Sql+= " FROM PROJUDI.PEND_TIPO t1 ";
		Sql+= " LEFT JOIN PROJUDI.PEND_TIPO_RELACIONADA t2 ON t1.ID_PEND_TIPO = t2.ID_PEND_TIPO_REL AND t2.ID_PEND_TIPO_PRINC = ?";
		ps.adicionarLong(id_pendenciatipoprincipal);
		Sql+= " LEFT JOIN PROJUDI.PEND_TIPO t3 ON t3.ID_PEND_TIPO = t2.ID_PEND_TIPO_PRINC";
		Sql+= " ORDER BY t1.PEND_TIPO";
		try{
			//System.out.println("..ps-consultarPendenciaTipoPendenciaTipoGeralPendenciaTipoRelacionada  " + Sql);

			rs = consultar(Sql, ps);
			//System.out.println("....Execução Query OK"  );

			while (rs.next()) {
				PendenciaTipoRelacionadaDt obTemp = new PendenciaTipoRelacionadaDt();
				obTemp.setId(rs.getString("ID_PEND_TIPO_RELACIONADA"));
				obTemp.setPendenciaTipoPrincipal(rs.getString("PEND_TIPO_PRINC"));
				obTemp.setId_PendenciaTipoRelacao (rs.getString("ID_PEND_TIPO_REL"));
				obTemp.setPendenciaTipoRelacao(rs.getString("PEND_TIPO_RELACAO"));
				obTemp.setId_PendenciaTipoPrincipal (id_pendenciatipoprincipal);
				obTemp.setPendenciaTipoPrincipal(rs.getString("PEND_TIPO_PRINC"));
				liTemp.add(obTemp);
			}
			//System.out.println("..PendenciaTipoRelacionadaPsGen.consultarPendenciaTipoPendenciaTipoGeral() Operação realizada com sucesso");
		}finally{
				 rs.close();
			}
		
		return liTemp; 
	}
}
