package br.gov.go.tj.projudi.ne;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.GerenciaUsuarios;
import br.gov.go.tj.projudi.dt.GrupoPermissaoDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.PermissaoDt;
import br.gov.go.tj.projudi.ps.GrupoPermissaoPs;
import br.gov.go.tj.utils.FabricaConexao;

public class GrupoPermissaoNe extends GrupoPermissaoNeGen {
    private static final long serialVersionUID = -7198949881770954804L;

    public String Verificar(GrupoPermissaoDt dados) {
        String stRetorno = "";
        return stRetorno;
    }

    /**
     * Método responsável por criar uma conexão com o bando de dados para que
     * sejam consultados quais grupos possuem uma dada permissão
     * 
     * @author Keila Sousa Silva
     * @param id_Permissao
     * @return listaGruposPermissao = lista contendo os grupos que possuem a
     *         permissão em questão
     * @throws Exception
     */
    public List consultarGruposPermissao(String id_Permissao) throws Exception {
        List listaGruposPermissao = new ArrayList();
        FabricaConexao obFabricaConexao = null;
        try{
            // Criar conexão com o banco de dados
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            GrupoPermissaoPs obPersistencia = new GrupoPermissaoPs(obFabricaConexao.getConexao());
            listaGruposPermissao = obPersistencia.consultarGruposPermissao(id_Permissao);
        
        } finally{
            obFabricaConexao.fecharConexao();
        }
        return listaGruposPermissao;
    }

    public List consultarPermissoesIdGrupo(String id_grupo) throws Exception {
        List tempList = null;
        FabricaConexao obFabricaConexao = null;

        try{
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            GrupoPermissaoPs obPersistencia = new GrupoPermissaoPs(obFabricaConexao.getConexao());
            tempList = obPersistencia.consultarPermissoesIdGrupo(id_grupo);
        
        } finally{
            obFabricaConexao.fecharConexao();
        }
        return tempList;
    }

    public boolean atualizaListaPermissoesGrupo(List dadosInserir, List dadosExcluir) throws Exception {
        boolean boRetorno = false;
        String Id_Grupo = "";
        LogDt obLogDt;
        GrupoPermissaoDt grupoPermissaoDt = null;
        FabricaConexao obFabricaConexao = null;

        try{
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            obFabricaConexao.iniciarTransacao();
            GrupoPermissaoPs obPersistencia = new GrupoPermissaoPs(obFabricaConexao.getConexao());

            if (dadosInserir.size() > 0) {
                grupoPermissaoDt = (GrupoPermissaoDt) dadosInserir.get(0);
                Id_Grupo = grupoPermissaoDt.getId_Grupo();
            } else if (dadosExcluir.size() > 0) {
                grupoPermissaoDt = (GrupoPermissaoDt) dadosExcluir.get(0);
                Id_Grupo = grupoPermissaoDt.getId_Grupo();
            }

            for (int i = 0; i < dadosInserir.size(); i++) {
                grupoPermissaoDt = (GrupoPermissaoDt) dadosInserir.get(i);                
                obPersistencia.inserir(grupoPermissaoDt);
                obLogDt = new LogDt("GrupoPermissao", grupoPermissaoDt.getId(), grupoPermissaoDt.getId_UsuarioLog(), grupoPermissaoDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir).toString(), "", grupoPermissaoDt.getPropriedades());
                obLog.salvar(obLogDt, obFabricaConexao);
            }

            for (int i = 0; i < dadosExcluir.size(); i++) {
                grupoPermissaoDt = (GrupoPermissaoDt) dadosExcluir.get(i);
                obLogDt = new LogDt("GrupoPermissao",grupoPermissaoDt.getId(), grupoPermissaoDt.getId_UsuarioLog(), grupoPermissaoDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir).toString(), grupoPermissaoDt.getPropriedades(), "");
                obPersistencia.excluir(grupoPermissaoDt.getId());
                obLog.salvar(obLogDt, obFabricaConexao);
            }

            // atualizo todas as permissões dos usuários conectados
            GerenciaUsuarios.getInstancia().atualizarPermissoesGrupo(Id_Grupo);

            obFabricaConexao.finalizarTransacao();
            boRetorno = true;

        } catch(Exception e) {
            obFabricaConexao.cancelarTransacao();
            throw e;
        } finally{
            obFabricaConexao.fecharConexao();
        }
        return boRetorno;
    }

    /**
     * 
     * @author Ronneesley Moura Teles
     * @since 30/05/2008 14:37 Correcoes de identacao
     * @param List
     *            lista, lista de permissoes
     * @return String
     */
    private String listaPermissoes(List lista) {
        String stTemp = "";
        List liPermissaoPai = new ArrayList();

        // guardo todos os objeto que não tem pais, ou seja, as permissões de primeiro nivel
        for (int i = 0; i < lista.size(); i++) {
            PermissaoDt tempPermissaoDt = (PermissaoDt) lista.get(i);

            if (tempPermissaoDt.getId_PermissaoPai().length() == 0) {
                liPermissaoPai.add(tempPermissaoDt);
            } else {
                // Atribuo os filhos aos pais
                for (int k = 0; k < lista.size(); k++) {
                    PermissaoDt tempPermissaoDt2 = (PermissaoDt) lista.get(k);

                    if (tempPermissaoDt2.getId().equals(tempPermissaoDt.getId_PermissaoPai()))
                        tempPermissaoDt2.incluirPermissao(tempPermissaoDt);
                }
            }
        }

        // pego os menus dos pais, e eles pegam os dos filhos
        for (int i = 0; i < liPermissaoPai.size(); i++)
            stTemp += ((PermissaoDt) liPermissaoPai.get(i)).getListaUlLi();

        return stTemp;
    }

    public String getListaPermissoes(String stGrupoId) throws Exception {

        List tempList = null;
        PermissaoNe obPermissaoNe = new PermissaoNe();
        // o codigotemp tem o id_grupo
        
        tempList = obPermissaoNe.consultarTodasPermissoes(stGrupoId);
        obPermissaoNe = null;
        
        return listaPermissoes(tempList);
    }
    
    public String consultarDescricaoGrupoJSON(String tempNomeBusca, String PosicaoPaginaAtual) throws Exception {
    	String stTemp = "";
		
		GrupoNe Grupone = new GrupoNe(); 
		stTemp = Grupone.consultarDescricaoJSON(tempNomeBusca, PosicaoPaginaAtual);
		
		return stTemp;
	}

}
