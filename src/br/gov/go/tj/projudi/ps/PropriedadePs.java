package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.PropriedadeDt;
import java.sql.Connection;

import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


//---------------------------------------------------------
public class PropriedadePs extends PropriedadePsGen{

    /**
     * 
     */
    private static final long serialVersionUID = -1221311154754773818L;

    public PropriedadePs(Connection conexao){
    	Conexao = conexao;
	}

	public List<PropriedadeDt> getPropriedades() throws Exception {
        String Sql;
        List<PropriedadeDt> liTemp = new ArrayList<>();
        ResultSetTJGO rs1 = null;
        ////System.out.println("..ps-ConsultaDescricaoPropriedade()");

        Sql= "SELECT * FROM projudi.VIEW_PROPRIEDADE";
        Sql+= " ORDER BY PROPRIEDADE_CODIGO ";
        
        try{
            ////System.out.println("..ps-ConsultaDescricaoPropriedade  " + Sql);

            rs1 = consultarSemParametros(Sql);
            ////System.out.println("....Execução Query OK"  );

            while (rs1.next()) {
                PropriedadeDt Dados = new PropriedadeDt();
                Dados= new PropriedadeDt();
                associarDt(Dados, rs1);               
                liTemp.add(Dados);
            }
            //rs1.close();
            
            ////System.out.println("..PropriedadePsGen.consultarDescricao() Operação realizada com sucesso");
        }finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}            
        }
        return liTemp; 
    }

    public List getPropriedade(String codigo) throws Exception{
        String Sql;
        List liTemp = new ArrayList();
        ResultSetTJGO rs1 = null;
        
        ////System.out.println("..ps-ConsultaDescricaoPropriedade()");

        Sql= "SELECT * FROM projudi.VIEW_PROPRIEDADE WHERE PROPRIEDADE_CODIGO=?"; 
        Sql+= " ORDER BY PROPRIEDADE_CODIGO ";
        
        try{
            ////System.out.println("..ps-ConsultaDescricaoPropriedade  " + Sql);

            rs1 = consultarSemParametros(Sql);
            ////System.out.println("....Execução Query OK"  );

            while (rs1.next()) {
                PropriedadeDt Dados = new PropriedadeDt();
                Dados= new PropriedadeDt();
                associarDt(Dados, rs1);               
                liTemp.add(Dados);
            }
            //rs1.close();
            
            ////System.out.println("..PropriedadePsGen.consultarDescricao() Operação realizada com sucesso");
        
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}            
        }
        return liTemp; 
    }

	public PropriedadeDt consultarCodigo(String codigo) throws Exception {

		String stSql = "";
		ResultSetTJGO rs1 = null;
		PropriedadeDt Dados = null;

		stSql = "SELECT * FROM PROJUDI.VIEW_PROPRIEDADE WHERE PROPRIEDADE_CODIGO = ?";

		try {
			PreparedStatementTJGO ps = new PreparedStatementTJGO();

			ps.adicionarLong(Long.valueOf(codigo));
			rs1 = consultar(stSql, ps);
			if (rs1.next()) {
				Dados = new PropriedadeDt();
				associarDt(Dados, rs1);
			}
		} finally {
			try {
				if (rs1 != null)
					rs1.close();
			} catch (Exception e) {
			}
		}
		return Dados;
	}

}
