package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.ComarcaCidadeDt;
import br.gov.go.tj.projudi.dt.EstadoDt;
import java.sql.Connection;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

//---------------------------------------------------------
public class ComarcaCidadePs extends ComarcaCidadePsGen{

/**
     * 
     */
    private static final long serialVersionUID = -7808050367861442247L;

    public ComarcaCidadePs(Connection conexao){
    	Conexao = conexao;
    }

	//
  //---------------------------------------------------------
    public List consultarCidadeComarcaGeral(String id_comarca ) throws Exception {

        String Sql;
        List liTemp = new ArrayList();
        ResultSetTJGO rs = null;
        PreparedStatementTJGO ps = new PreparedStatementTJGO();

        Sql= "SELECT t2.ID_COMARCA_CIDADE, t1.ID_CIDADE, t1.CIDADE, t3.ID_COMARCA, t3.COMARCA";
        Sql+= " FROM PROJUDI.CIDADE t1 ";
        Sql+= " INNER JOIN PROJUDI.ESTADO est ON est.ID_ESTADO = t1.ID_ESTADO";
        Sql+= " LEFT JOIN PROJUDI.COMARCA_CIDADE t2 ON t1.ID_CIDADE = t2.ID_CIDADE AND t2.ID_COMARCA = ?";
        ps.adicionarLong(id_comarca);
        Sql+= " LEFT JOIN PROJUDI.COMARCA t3 ON t3.ID_COMARCA = t2.ID_COMARCA";
        Sql+= " WHERE  est.ESTADO_CODIGO = ?";
        ps.adicionarLong(EstadoDt.ESTADOCODIGOGOIAS);
        Sql+= " ORDER BY t1.CIDADE";
        try{

            rs = consultar(Sql,ps);

            while (rs.next()) {
                ComarcaCidadeDt obTemp = new ComarcaCidadeDt();
                obTemp.setId(rs.getString("ID_COMARCA_CIDADE"));
                obTemp.setComarca(rs.getString("COMARCA"));
                obTemp.setId_Cidade (rs.getString("ID_CIDADE"));
                obTemp.setCidade(rs.getString("CIDADE"));
                obTemp.setId_Comarca (id_comarca);
                obTemp.setComarca(rs.getString("COMARCA"));
                liTemp.add(obTemp);
            }
        } finally{
            try{if (rs != null) rs.close();} catch(Exception e1) {}
        }
        return liTemp; 
    }
    
    public ComarcaCidadeDt consultarIdCidade(String id_cidade )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ComarcaCidadeDt Dados=null;

		stSql= "SELECT * FROM PROJUDI.VIEW_COMARCA_CIDADE WHERE ID_CIDADE = ?";		ps.adicionarLong(id_cidade); 

		try{
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new ComarcaCidadeDt();
				associarDt(Dados, rs1);
			}
		} finally {
			try {if (rs1 != null) rs1.close();} catch (Exception e) {}
		}
		return Dados; 
	}
}
