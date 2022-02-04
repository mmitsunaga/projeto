package br.gov.go.tj.projudi.ne;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ArquivoDt;
import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.UsuarioArquivoDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.ps.UsuarioArquivoPs;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;

//---------------------------------------------------------
public class UsuarioArquivoNe extends UsuarioArquivoNeGen {

    /**
     * 
     */
    private static final long serialVersionUID = 1297398221471436152L;

    public String Verificar(UsuarioArquivoDt dados) {

        String stRetorno = "";

        if (dados.getUsuarioDt().getId().length() == 0) stRetorno += "� necess�rio selecionar um Usu�rio. \n";
        if (dados.getArquivosInseridos() == null || dados.getArquivosInseridos().size() == 0) stRetorno += "� necess�rio inserir ao menos um Documento.";
        return stRetorno;

    }

    public void salvar(UsuarioArquivoDt dados, FabricaConexao obFabricaConexao) throws Exception {
        LogDt obLogDt;
        try{
            UsuarioArquivoPs obPersistencia = new UsuarioArquivoPs(obFabricaConexao.getConexao());
            if (dados.getId().equalsIgnoreCase("")) {               
                obPersistencia.inserir(dados);
                obLogDt = new LogDt("UsuarioArquivo", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir), dados.getId(), dados.getPropriedades());
            } else {               
                obPersistencia.alterar(dados);
                obLogDt = new LogDt("UsuarioArquivo", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar), obDados.getPropriedades(), dados.getPropriedades());
            }
            obDados.copiar(dados);
            obLog.salvar(obLogDt, obFabricaConexao);
        } catch(Exception e) {
            dados.setId("");
            throw e;
        }
    }

    /**
     * Salva os documentos inseridos para um usu�rio
     * 
     * @param usuarioArquivoDt,
     *            objeto com dados do usu�rios e documentos a serem salvos
     * 
     * @author msapaula
     * 
     */
    public void salvarDocumentosUsuario(UsuarioArquivoDt usuarioArquivoDt) throws Exception {
        FabricaConexao obFabricaConexao = null;
        try{
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            obFabricaConexao.iniciarTransacao();

            LogDt logDt = new LogDt(usuarioArquivoDt.getId_UsuarioLog(), usuarioArquivoDt.getIpComputadorLog());

            UsuarioDt usuario = usuarioArquivoDt.getUsuarioDt();
            List documentos = usuarioArquivoDt.getArquivosInseridos();

            ArquivoNe arquivoNe = new ArquivoNe();
            // Para cada documento adicionado, salva e vincula com usu�rio
            for (int i = 0; i < documentos.size(); i++) {
                ArquivoDt arquivoDt = (ArquivoDt) documentos.get(i);
                arquivoDt.setId_UsuarioLog(logDt.getId_Usuario());
                arquivoDt.setIpComputadorLog(logDt.getIpComputador());
                arquivoNe.inserir(arquivoDt, logDt, obFabricaConexao);

                usuarioArquivoDt = new UsuarioArquivoDt();
                usuarioArquivoDt.setId_Usuario(usuario.getId());
                usuarioArquivoDt.setId_Arquivo(arquivoDt.getId());
                usuarioArquivoDt.setId_UsuarioLog(logDt.getId_Usuario());
                usuarioArquivoDt.setIpComputadorLog(logDt.getIpComputador());
                this.salvar(usuarioArquivoDt, obFabricaConexao);
            }

            obFabricaConexao.finalizarTransacao();

        } catch(Exception e) {
            obFabricaConexao.cancelarTransacao();
            throw e;
        } finally{
            obFabricaConexao.fecharConexao();
        }
    }

	/**
	 * Consultar arquivos de um determinado usu�rio
	 * 
	 * @param id_Usuario, identifica��o do usu�rio
	 * @param usuarioNe, usu�rio que est� baixando arquivo
	 * @author msapaula
	 */
	public List consultarArquivosUsuario(String id_Usuario, UsuarioNe usuarioNe) throws Exception {
		List tempList = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			UsuarioArquivoPs obPersistencia = new UsuarioArquivoPs(obFabricaConexao.getConexao());
			tempList = obPersistencia.consultarArquivosUsuario(id_Usuario);

            if (tempList != null) {
                for (int i = 0; i < tempList.size(); i++) {
                    UsuarioArquivoDt dt = (UsuarioArquivoDt) tempList.get(i);
                    dt.setHash(usuarioNe.getCodigoHash(dt.getId()));
                }
            }
        
        } finally{
            obFabricaConexao.fecharConexao();
        }
        return tempList;
    }

    /**
     * Consultar dados detalhados de um arquivo de usu�rio
     * 
     * @param id_UsuarioArquivo,
     *            identifica��o do arquivo de usu�rio
     * @author msapaula
     * @throws Exception 
     */
    public UsuarioArquivoDt consultarIdCompleto(String id_UsuarioArquivo) throws Exception{
        return this.consultarIdCompleto(id_UsuarioArquivo, null);
    }

    /**
     * Consultar dados detalhados de um arquivo de usu�rio
     * 
     * @param id_UsuarioArquivo,
     *            identifica��o do arquivo de usu�rio
     * @param conexao,
     *            conex�o ativa
     * @author msapaula
     */
    public UsuarioArquivoDt consultarIdCompleto(String id_UsuarioArquivo, FabricaConexao conexao) throws Exception {
		UsuarioArquivoDt dtRetorno = null;
		FabricaConexao obFabricaConexao = null;
		try{
			if (conexao == null) {
				obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			} else {
				obFabricaConexao = conexao;
			}
			UsuarioArquivoPs obPersistencia = new UsuarioArquivoPs(obFabricaConexao.getConexao());
			dtRetorno = obPersistencia.consultarIdCompleto(id_UsuarioArquivo);
			obDados.copiar(dtRetorno);
		
		} finally{
			if (conexao == null)
				obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}

    /**
     * M�todo que permite baixar um arquivo de usu�rio. Efetua a verifica��o se
     * o usu�rio em quest�o pode realmente visualizar esse arquivo.
     * 
     * @param id_UsuarioArquivo,
     *            identifica��o do arquivo do usu�rio
     * @param usuarioDt
     *            usuario que est� baixando arquivo
     * @param response
     * @param logDt
     * @throws Exception
     */
    public void baixarArquivoUsuario(String id_UsuarioArquivo, UsuarioDt usuarioDt, HttpServletResponse response, LogDt logDt)throws Exception{
        ArquivoNe arquivoNe = null;
        
        if (podeBaixarArquivo(usuarioDt)) {
            UsuarioArquivoDt usuarioArquivoDt = this.consultarId(id_UsuarioArquivo);
            arquivoNe = new ArquivoNe();
            arquivoNe.baixarArquivo(usuarioArquivoDt.getId_Arquivo(), response, logDt, false);
        } else {
            throw new MensagemException("Voc� n�o possui permiss�o para visualizar este arquivo.");
        }
        
    }

    /**
     * M�todo que verifica se um usu�rio pode fazer download de arquivos de
     * usu�rio. (Somente administradores e cadastradores)
     * 
     * @param usuarioDt:
     *            objeto de usu�rio
     * @author msapaula
     */
    public boolean podeBaixarArquivo(UsuarioDt usuarioDt){
        boolean boRetorno = false;

        int grupo = Funcoes.StringToInt(usuarioDt.getGrupoCodigo());

        if (grupo == GrupoDt.ADMINISTRADORES || grupo == GrupoDt.CADASTRADORES || grupo == GrupoDt.CADASTRADOR_MASTER) {
            boRetorno = true;
        }

        return boRetorno;
    }

    /**
     * M�todo respons�vel em excluir um documento de usu�rio. Deve deletar o
     * registro em UsuarioArquivo e Arquivo.
     * 
     * @param dados,
     *            objeto UsuarioArquivo
     * 
     * @author msapaula
     */
    public void excluirDocumentoUsuario(String id_UsuarioArquivo, LogDt logDt) throws Exception {
        FabricaConexao obFabricaConexao = null;
        try{
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            obFabricaConexao.iniciarTransacao();
            UsuarioArquivoPs obPersistencia = new UsuarioArquivoPs(obFabricaConexao.getConexao());

            // Consulta dados completos do UsuarioArquivo para que possa ser
            // gerado log corretamente, e para possbilitar a
            // exclus�o do arquivo vinculado
            UsuarioArquivoDt usuarioArquivoDt = this.consultarIdCompleto(id_UsuarioArquivo, obFabricaConexao);

            // Deleta UsuarioArquivo
            LogDt obLogDt = new LogDt("UsuarioArquivo", usuarioArquivoDt.getId(), logDt.getId_Usuario(), logDt.getIpComputador(), String.valueOf(LogTipoDt.Excluir), usuarioArquivoDt.getPropriedades(), "");
            obPersistencia.excluir(id_UsuarioArquivo);
            obLog.salvar(obLogDt, obFabricaConexao);

            ArquivoNe arquivoNe = new ArquivoNe();
            ArquivoDt arquivo = usuarioArquivoDt.getArquivoDt();
            arquivo.setId_UsuarioLog(logDt.getId_Usuario());
            arquivo.setIpComputadorLog(logDt.getIpComputador());
            arquivoNe.excluir(arquivo, obFabricaConexao);

            obFabricaConexao.finalizarTransacao();

        } catch(Exception e) {
            obFabricaConexao.cancelarTransacao();
            throw e;
        } finally{
            obFabricaConexao.fecharConexao();
        }
    }

    /**
     * Realiza chamada ao objeto que efetuar� a consulta
     */
    public List consultarGrupoArquivoTipo(String grupoCodigo, String tempNomeBusca, String posicaoPaginaAtual) throws Exception {
        List tempList = null;
        ArquivoTipoNe neObjeto = new ArquivoTipoNe();
        
        tempList = neObjeto.consultarGrupoArquivoTipo(grupoCodigo, tempNomeBusca, posicaoPaginaAtual);
        QuantidadePaginas = neObjeto.getQuantidadePaginas();
        
        neObjeto = null;
        return tempList;
    }
    
    public String consultarGrupoArquivoTipoJSON(String grupoCodigo, String tempNomeBusca, String posicaoPaginaAtual) throws Exception {
		String stTemp = "";
		ArquivoTipoNe neObjeto = new ArquivoTipoNe();
		
		stTemp = neObjeto.consultarGrupoArquivoTipoJSON(grupoCodigo, tempNomeBusca, posicaoPaginaAtual);
		QuantidadePaginas = neObjeto.getQuantidadePaginas();
		
		neObjeto = null;
		return stTemp;
	}
    
    public String consultarDescricaoJSON(String descricao, String posicao ) throws Exception{
		String stTemp = "";
		FabricaConexao obFabricaConexao = null;
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			try{

				UsuarioArquivoPs obPersistencia = new UsuarioArquivoPs(obFabricaConexao.getConexao());
				stTemp = obPersistencia.consultarDescricaoJSON(descricao, posicao);			
			}finally{
				obFabricaConexao.fecharConexao();
			}
		return stTemp;   
	}

}
