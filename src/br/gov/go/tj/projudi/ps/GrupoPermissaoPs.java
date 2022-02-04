package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.GrupoPermissaoDt;
import java.sql.Connection;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class GrupoPermissaoPs extends GrupoPermissaoPsGen {

    /**
     * 
     */
    private static final long serialVersionUID = 3656827654627351360L;

    @SuppressWarnings("unused")
	private GrupoPermissaoPs( ) {}
    
    public GrupoPermissaoPs(Connection conexao){
    	Conexao = conexao;
	}

	/**
     * Método responsável por construir e executar a sql que irá consultar quais
     * grupos possuem uma dada permissão
     * 
     * @author Keila Sousa Silva
     * @param id_Permissao
     * @return listaGruposPermissao = lista contendo os grupos que possuem a
     *         permissão em questão
     * @throws Exception
     */
    public List consultarGruposPermissao(String id_Permissao) throws Exception {
        String sql;
        List listaGruposPermissao = new ArrayList();
        ResultSetTJGO rs1 = null;
        PreparedStatementTJGO ps =  new PreparedStatementTJGO();
        sql = "SELECT ID_GRUPO_PERM,ID_PERM FROM PROJUDI.GRUPO_PERM WHERE ID_PERM = ?";
        ps.adicionarLong(id_Permissao );
        try{
            rs1 = consultar(sql, ps);
            while (rs1.next()) {
                GrupoPermissaoDt grupoPermissaoDt = new GrupoPermissaoDt();
                grupoPermissaoDt.setId(rs1.getString("ID_GRUPO_PERM"));
                grupoPermissaoDt.setId_Permissao(rs1.getString("ID_PERM"));
                listaGruposPermissao.add(grupoPermissaoDt);
            }
        
        }
        finally{
			try{if(rs1!=null) rs1.close(); } catch(Exception e) {}
		}
        return listaGruposPermissao;
    }

    public List consultarPermissoesIdGrupo(String id_grupo) throws Exception {
        String Sql;
        List liTemp = new ArrayList();
        ResultSetTJGO rs1 = null;
        PreparedStatementTJGO ps =  new PreparedStatementTJGO();
        ////System.out.println("..ps-consultarPermissoesIdGrupo()");

        Sql = "SELECT  *  FROM VIEW_GRUPO_PERM WHERE ID_GRUPO = ?";
        ps.adicionarLong(id_grupo);
        ////System.out.println("....Sql  " + Sql);

        try{
            rs1 = consultar(Sql, ps);
            ////System.out.println("....Execução Query OK");

            while (rs1.next()) {
                GrupoPermissaoDt obTemp = new GrupoPermissaoDt();
                obTemp.setId(rs1.getString("ID_GRUPO_PERM"));
                obTemp.setGrupo(rs1.getString("GRUPO"));
                obTemp.setId_Grupo(rs1.getString("ID_GRUPO"));
                obTemp.setId_Permissao(rs1.getString("ID_PERM"));
                obTemp.setPermissao(rs1.getString("PERM"));
                obTemp.setCodigoTemp(rs1.getString("CODIGO_TEMP"));
                liTemp.add(obTemp);
            }
            //rs1.close();
            ////System.out.println("..ps-consultarPermissoesIdGrupo Operação realizada com sucesso");
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        }
        return liTemp;
    }

    private String getSubPermissao(int id_permissaopai, String id_grupo) throws Exception{
        String stListaPermissao = "";
        String stSql;
        ResultSetTJGO rs1 = null;
        PreparedStatementTJGO ps =  new PreparedStatementTJGO();
        ////System.out.println("..ps-getSubPermissao()");
        try{
            stSql = "SELECT m.*,  upg.ID_GRUPO as id ";
            stSql += "FROM PROJUDI.VIEW_LISTA_PERM_COMPOSTA m ";
            stSql += "LEFT JOIN PROJUDI.VIEW_GRUPO_PERM_GERAL upg ";
            stSql += "on m.ID_PERM = upg.ID_PERM";
            stSql += "WHERE  m.ID_PERM_PAI=?";
            ps.adicionarLong(id_permissaopai);            
            // stSql += " AND upg.ID_GRUPO=" + id_grupo ;
            stSql += " ORDER BY m.ORDENACAO, m.PERM";

            ////System.out.println("..ps-getSubPermissao Conexao aberta com sucesso " + stSql);

            ////System.out.println("...1");
            rs1 = consultar(stSql, ps);
            ////System.out.println("...Query ok");
            while (rs1.next()) {
                // if (rs1.getString("ListaPermissao").indexOf("{SubPermissao}")
                // != 0){
                String stPermissao = rs1.getString("LISTA_PERMISSAO");

                if (rs1.getString("id") == null)
                    stPermissao = stPermissao.replace("checked", "");

                stListaPermissao += stPermissao.replace("{SubPermissao}", getSubPermissao(rs1.getInt("ID_PERM"), id_grupo));
                // }else stListaPermissao += rs1.getString("ListaPermissao");
                // ////System.out.println("...3 " + rs1.getInt("PERM_CODIGO"));
            }
            ////System.out.println("...4 fim do while");

            //rs1.close();
            ////System.out.println("..ps-getSubPermissao Conexao fechada com sucesso");
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        }
        ////System.out.println("..ps-getSubPermissao OK ");
        return stListaPermissao;
    }

    
    public String getPermissao(String id_grupo) throws Exception {
        String stListaPermissao = "";
        String stSql;
        ResultSetTJGO rs1 = null;
        PreparedStatementTJGO ps =  new PreparedStatementTJGO();
        ////System.out.println("..getPermissao()");
        try{
            stSql = "SELECT m.*, upg.ID_PERM as id ";
            stSql += "FROM PROJUDI.VIEW_LISTA_PERM_SIMPLES m ";
            stSql += "LEFT JOIN PROJUDI.VIEW_GRUPO_PERM_GERAL upg ";
            stSql += "on m.ID_PERM = upg.ID_PERM AND upg.ID_GRUPO=?";
            ps.adicionarLong(id_grupo);
            stSql += " ORDER BY m.ORDENACAO, m.PERM";

            ////System.out.println("..ps-getPermissao Conexao aberta com sucesso " + stSql);

            ////System.out.println("...1");
            rs1 = consultar(stSql, ps);
            ////System.out.println("...Query ok");
            while (rs1.next()) {
                String stPermissao = rs1.getString("LISTA_PERMISSAO");

                if (rs1.getString("id") == null)
                    stPermissao = stPermissao.replace("checked", "");

                stListaPermissao += stPermissao.replace("{SubPermissao}", getSubPermissao(rs1.getInt("ID_PERM"), id_grupo));
                // ////System.out.println("...3 " + rs1.getInt("PERM_CODIGO"));
            }
            ////System.out.println("...4 fim do while");
            //rs1.close();
            ////System.out.println("..ps-getPermissao Conexao fechada com sucesso");
        
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}                 
            ////System.out.println("..ps-getPermissao OK ");
        }
        return stListaPermissao.replaceAll("<ul></ul>", "");
    }
}
