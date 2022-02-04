package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.BairroDt;
import br.gov.go.tj.projudi.dt.CidadeDt;
import br.gov.go.tj.projudi.dt.EnderecoDt;
import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteDt;
import br.gov.go.tj.projudi.dt.RgOrgaoExpedidorDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.dt.UsuarioParteDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaDt;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

/**
 * Servlet para controlar o cadastro de partes como usuários do projudi Se uma
 * parte não é cadastrada, será gerada um novo registro em usuário. Caso já seja
 * cadastrada será apenas habilitada nos processos que desejar, e passará a
 * responder pelas intimações/citações. No caso de pessoa jurídica, será
 * cadastrado um representante para essa (preposto).
 * 
 * @author msapaula
 */

public class UsuarioParteCt extends Controle {

    private static final long serialVersionUID = 3010787514933493559L;

    public int Permissao() {
        return UsuarioParteDt.CodigoPermissao;
    }

    public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

        UsuarioParteDt UsuarioPartedt;
        UsuarioNe UsuarioPartene;
        EnderecoDt Enderecodt;

        String Mensagem = "";
        String curinga = "";
        String id_ProcessoParte = "";
        int passoEditar = -1;
        String stAcao = "/WEB-INF/jsptjgo/UsuarioParte.jsp";
        
        String stNomeBusca1 = "";
		String stNomeBusca2 = "";
		String stNomeBusca3 = "";

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");
		if(request.getParameter("nomeBusca3") != null) stNomeBusca3 = request.getParameter("nomeBusca3");

        request.setAttribute("tempPrograma", "Usuario");
        request.setAttribute("Curinga", "vazio");
        request.setAttribute("tempRetorno", "UsuarioParte");

        UsuarioPartene = (UsuarioNe) request.getSession().getAttribute("UsuarioPartene");
        if (UsuarioPartene == null) UsuarioPartene = new UsuarioNe();

        UsuarioPartedt = (UsuarioParteDt) request.getSession().getAttribute("UsuarioPartedt");
        if (UsuarioPartedt == null) UsuarioPartedt = new UsuarioParteDt();

        Enderecodt = (EnderecoDt) request.getSession().getAttribute("Enderecodt");
        if (Enderecodt == null) Enderecodt = new EnderecoDt();

        if (request.getParameter("PassoEditar") != null) passoEditar = Funcoes.StringToInt(request.getParameter("PassoEditar"));
        id_ProcessoParte = request.getParameter("Id_ProcessoParte");
        // Variável curinga utilizada para auxiliar no cadastro:
        // - PJ: representa cadastro de representante de uma parte jurídica
        // - S: confirmação para salvar
        if (request.getParameter("Curinga") != null) curinga = request.getParameter("Curinga");

        // Quanto se tratar de cadastro do usuário deve capturar os dados
        if (passoEditar == 0) getDadosUsuarioParte(UsuarioPartedt, request, UsuarioSessao);

        request.setAttribute("PaginaAnterior", paginaatual);
        request.setAttribute("MensagemOk", "");
        request.setAttribute("MensagemErro", "");
        request.setAttribute("PaginaAtual", Configuracao.Editar);

        switch (paginaatual) {

        case Configuracao.Novo:
            ProcessoDt processoDt = (ProcessoDt) request.getSession().getAttribute("processoDt");

            // Verifica se usuário pode habilitar usuario para parte
            Mensagem = UsuarioPartene.podeHabilitarUsuarioParte(processoDt, UsuarioSessao.getUsuarioDt());
            if (Mensagem.length() > 0) {
                request.setAttribute("MensagemErro", Mensagem);
                redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&MensagemErro=" + Mensagem);
                return;
            }
            break;

        case Configuracao.Salvar:
            // Cadastro de usuário parte
            if (curinga.equalsIgnoreCase("S")) {
                request.setAttribute("Mensagem", "Clique para confirmar o cadastro do Usuário");
                stAcao = "/WEB-INF/jsptjgo/UsuarioParteCadastrar.jsp";
            } else {
                // Cadastro de Representante
                if (curinga.equalsIgnoreCase("PJ")) {
                    // Se o cadastro que está sendo feito é de um representante,
                    // realiza validações
                    String cpfRepresentante = request.getParameter("CpfRepresentante");
                    if (cpfRepresentante != null && cpfRepresentante.length() > 0 && Funcoes.testaCPFCNPJ(cpfRepresentante)) {
                        UsuarioServentiaDt usuarioServentiaDt = UsuarioPartene.consultarUsuarioServentiaParte(cpfRepresentante);
                        // Verifica se já existe um usuario cadastrado com o
                        // mesmo CPF do representante
                        if (usuarioServentiaDt != null) {
                            UsuarioPartedt.setId_UsuarioServentia(usuarioServentiaDt.getId());
                            UsuarioPartedt.setUsuario(usuarioServentiaDt.getUsuario());
                            request.setAttribute("Mensagem", "Já existe um usuário cadastrado com o CPF " + cpfRepresentante + ".Clique para habilitar o usuário " + usuarioServentiaDt.getUsuario() + " como representante para a Parte " + UsuarioPartedt.getParteDt().getNome());
                            curinga = "";
                            stAcao = "/WEB-INF/jsptjgo/UsuarioParteEditar.jsp";
                        } else {// Redireciona para tela de cadastro do
                                    // usuário (representante)
                            request.setAttribute("MensagemOk", "Informe os dados do Representante");
                            UsuarioPartedt.setUsuario(cpfRepresentante);
                            UsuarioPartedt.setCpf(cpfRepresentante);
                            stAcao = "/WEB-INF/jsptjgo/UsuarioParteCadastrar.jsp";
                        }
                    } else {
                        request.setAttribute("MensagemErro", "Digite um CPF válido para prosseguir");
                        stAcao = "/WEB-INF/jsptjgo/UsuarioRepresentanteParte.jsp";
                    }

                    // Se tem CPF trata-se de pessoa física, senão é pessoa
                    // jurídica e um representante será cadastrado
                } else if (UsuarioPartedt.getParteDt().getCpf().length() > 0) {
                    // Procura se já existem um usuário cadastrado com o mesmo
                    // CPF da parte
                    UsuarioServentiaDt usuarioServentiaDt = UsuarioPartene.consultarUsuarioServentiaParte(UsuarioPartedt.getParteDt().getCpf());
                    if (usuarioServentiaDt != null) {
                        UsuarioPartedt.setId_UsuarioServentia(usuarioServentiaDt.getId());
                        UsuarioPartedt.setUsuario(usuarioServentiaDt.getUsuario());
                        request.setAttribute("Mensagem", "Já existe um usuário cadastrado com o mesmo CPF. Clique para habilitar esse Usuário para a parte neste Processo ?");
                        stAcao = "/WEB-INF/jsptjgo/UsuarioParteEditar.jsp";
                    }
                    // Se parte não é cadastrada, redireciona para tela de
                    // cadastro de usuário
                    else {
                        setDadosUsuarioParte(request, UsuarioPartedt, UsuarioSessao);
                        stAcao = "/WEB-INF/jsptjgo/UsuarioParteCadastrar.jsp";
                    }
                } else if (UsuarioPartedt.getParteDt().getCnpj().length() > 0) {
                    curinga = "PJ";
                    stAcao = "/WEB-INF/jsptjgo/UsuarioRepresentanteParte.jsp";
                }
            }
            break;

        case Configuracao.SalvarResultado:
            // Se possui Id_UsuarioServentia é pq já é cadastrado, então somente
            // habilita
            if (UsuarioPartedt.getId_UsuarioServentia().length() > 0) {
                UsuarioPartene.habilitaUsuarioParte(UsuarioPartedt.getParteDt(), UsuarioPartedt.getId_UsuarioServentia(), new LogDt(UsuarioSessao.getId_Usuario(), UsuarioSessao.getIpComputadorLog()));

                request.setAttribute("MensagemOk", "Usuário " + UsuarioPartedt.getUsuario() + " da Parte " + UsuarioPartedt.getParteDt().getNome() + " habilitado no Processo");
                stAcao = "/WEB-INF/jsptjgo/UsuarioParteEditar.jsp";
            } else {
                Mensagem = UsuarioPartene.VerificarUsuarioParte(UsuarioPartedt);
                if (Mensagem.length() == 0) {
                    UsuarioPartene.salvarUsuarioParte(UsuarioPartedt);
                    request.setAttribute("MensagemOk", "Usuário " + UsuarioPartedt.getUsuario() + " cadastrado para Parte " + UsuarioPartedt.getParteDt().getNome() + " com sucesso (Senha 12345)");
                    stAcao = "/WEB-INF/jsptjgo/UsuarioParte.jsp";
                    request.setAttribute("PaginaAtual", Configuracao.Localizar);
                } else {
                    request.setAttribute("MensagemErro", Mensagem);
                    curinga = "C";
                    stAcao = "/WEB-INF/jsptjgo/UsuarioParteCadastrar.jsp";
                }
            }
            break;

        // Confirmação Desabilitação Usuário da Parte de um Processo
        case Configuracao.Excluir:
            request.setAttribute("Mensagem", "Clique para desabilitar o Usuário " + UsuarioPartedt.getUsuario() + " da Parte " + UsuarioPartedt.getParteDt().getNome() + " nesse Processo");
            stAcao = "/WEB-INF/jsptjgo/UsuarioParteEditar.jsp";
            break;

        // Desabilitação de Usuário da Parte de um Processo
        case Configuracao.ExcluirResultado:
            UsuarioPartene.desabilitaUsuarioParte(UsuarioPartedt.getParteDt(), new LogDt(UsuarioSessao.getId_Usuario(), UsuarioSessao.getIpComputadorLog()));
            request.setAttribute("MensagemOk", "Usuário " + UsuarioPartedt.getUsuario() + " da Parte " + UsuarioPartedt.getParteDt().getNome() + " desabilitado do Processo");
            stAcao = "/WEB-INF/jsptjgo/UsuarioParteEditar.jsp";
            break;

        // Limpar Senha da Parte
        case Configuracao.Curinga6:
            if (UsuarioPartedt.getParteDt().getId_Usuario().length() > 0) {
                UsuarioDt usuarioDt = new UsuarioDt();
                usuarioDt.setId(UsuarioPartedt.getParteDt().getId_Usuario());
                usuarioDt.setId_UsuarioLog(UsuarioPartedt.getId_UsuarioLog());
                usuarioDt.setIpComputadorLog(UsuarioPartedt.getIpComputadorLog());
                UsuarioPartene.limparSenha(usuarioDt);
                request.setAttribute("MensagemOk", "Senha do Usuário " + UsuarioPartedt.getUsuario() + " (Parte " + UsuarioPartedt.getParteDt().getNome() + ") alterada com sucesso.(Senha 12345)");
                stAcao = "/WEB-INF/jsptjgo/UsuarioParteEditar.jsp";
            } else
                request.setAttribute("MensagemErro", "Selecione o usuário que deseja limpar a senha.");

            break;

        // Consulta de Naturalidade
        case (CidadeDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
        	if (request.getParameter("Passo")==null){
				stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
				String[] lisNomeBusca = {"Cidade","Uf"};
				String[] lisDescricao = {"Cidade","Uf"};
				request.setAttribute("tempBuscaId", "Id_Naturalidade");
				request.setAttribute("tempBuscaDescricao", "Naturalidade");
				request.setAttribute("tempBuscaPrograma", "Cidade");
				request.setAttribute("tempRetorno", "UsuarioParte");
				request.setAttribute("tempDescricaoId", "Id");
				request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
				request.setAttribute("PaginaAtual", (CidadeDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
				request.setAttribute("PosicaoPaginaAtual", "0");
				request.setAttribute("QuantidadePaginas", "0");
				request.setAttribute("lisNomeBusca", lisNomeBusca);
				request.setAttribute("lisDescricao", lisDescricao);
				passoEditar = 0;
			}else{
				String stTemp = "";
				stTemp = UsuarioPartene.consultarDescricaoCidadeJSON(stNomeBusca1, stNomeBusca2, PosicaoPaginaAtual);
									
					enviarJSON(response, stTemp);
					
				
				return;
			}
        	
            break;

        // Consulta de Órgão Expedidor RG
        case (RgOrgaoExpedidorDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
			if (request.getParameter("Passo")==null){
				String[] lisNomeBusca = {"OrgaoExpedidor","Sigla"};
				String[] lisDescricao = {"Sigla","Nome","Estado"};
				stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
				String[] camposHidden = {"RgOrgaoExpedidorNome", "RgOrgaoExpedidorEstado"};
				request.setAttribute("camposHidden",camposHidden);
				request.setAttribute("tempBuscaId", "Id_RgOrgaoExpedidor");
				request.setAttribute("tempBuscaDescricao", "RgOrgaoExpedidor");
				request.setAttribute("tempBuscaPrograma", "RgOrgaoExpedidor");
				request.setAttribute("tempRetorno", "UsuarioParte");
				request.setAttribute("tempDescricaoId", "Id");
				request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
				request.setAttribute("PaginaAtual", (RgOrgaoExpedidorDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
				request.setAttribute("PosicaoPaginaAtual", "0");
				request.setAttribute("QuantidadePaginas", "0");
				request.setAttribute("lisNomeBusca", lisNomeBusca);
				request.setAttribute("lisDescricao", lisDescricao);
			}else{
				String stTemp = "";			
				stTemp = UsuarioPartene.consultarDescricaoRgOrgaoExpedidorJSON(stNomeBusca2, stNomeBusca1, PosicaoPaginaAtual);				
				enviarJSON(response, stTemp);						
				return;
			}
                
            break;

        // Consulta de bairros
        case (BairroDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
        	if (request.getParameter("Passo")==null){
				String[] lisNomeBusca = {"Bairro","Cidade","Uf"};
				String[] lisDescricao = {"Bairro","Cidade","Uf"};
				String[] camposHidden = {"BairroCidade","BairroUf"};
				request.setAttribute("camposHidden",camposHidden);
				stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
				request.setAttribute("tempBuscaId", "Id_Bairro");
				request.setAttribute("tempBuscaDescricao", "Bairro");
				request.setAttribute("tempBuscaPrograma", "Bairro");
				request.setAttribute("tempRetorno", "UsuarioParte");
				request.setAttribute("tempDescricaoId", "Id");
				request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
				request.setAttribute("PaginaAtual", (BairroDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
				request.setAttribute("PosicaoPaginaAtual", "0");
				request.setAttribute("QuantidadePaginas", "0");
				request.setAttribute("lisNomeBusca", lisNomeBusca);
				request.setAttribute("lisDescricao", lisDescricao);
				passoEditar = 0;
			} else{
				String stTemp = "";
				stTemp = UsuarioPartene.consultarDescricaoBairroJSON(stNomeBusca1, stNomeBusca2, stNomeBusca3, PosicaoPaginaAtual);
									
					enviarJSON(response, stTemp);
					
				
				return;
			}
			break;
        	
        	
        default:
            /**
             * Passo Editar: 0 - quando se trata de cadastro do usuário
             */
            switch (passoEditar) {
            case 0:
                stAcao = "/WEB-INF/jsptjgo/UsuarioParteCadastrar.jsp";
                break;

            default:
                if (id_ProcessoParte != null && !id_ProcessoParte.equalsIgnoreCase("")) {
                    UsuarioPartedt.limpar();
                    UsuarioPartedt = consultarDadosParte(request, id_ProcessoParte, UsuarioPartene, UsuarioSessao);
                    if (UsuarioPartedt.getParteDt().getCpf().length() == 0 && UsuarioPartedt.getParteDt().getCnpj().length() == 0) {
                        request.setAttribute("MensagemErro", "Parte deve ter CPF/CNPJ cadastrado para que seja habilitada como Usuário");
                    } else {
                        curinga = "";
                        stAcao = "/WEB-INF/jsptjgo/UsuarioParteEditar.jsp";
                    }
                } else
                    request.setAttribute("PaginaAtual", Configuracao.Localizar);

                break;
            }
        }

        request.setAttribute("Curinga", curinga);
        request.setAttribute("PassoEditar", passoEditar);
        request.getSession().setAttribute("UsuarioPartedt", UsuarioPartedt);
        request.getSession().setAttribute("UsuarioPartene", UsuarioPartene);
        request.getSession().setAttribute("Enderecodt", UsuarioPartedt.getEnderecoUsuario());

        RequestDispatcher dis = request.getRequestDispatcher(stAcao);
        dis.include(request, response);
    }

    /**
     * Obtem os dados da parte selecionada, e monta objeto UsuarioParte para
     * essa
     * 
     * @param request
     * @param id_ProcessoParte
     * @param usuarioNe
     * @param usuarioSessao
     * @return
     * @throws Exception
     */
    private UsuarioParteDt consultarDadosParte(HttpServletRequest request, String id_ProcessoParte, UsuarioNe usuarioNe, UsuarioNe usuarioSessao) throws Exception{

        ProcessoParteDt parteDt = usuarioNe.consultaProcessoParteId(id_ProcessoParte);

        // Dt UsuarioParte foi criado apenas para auxiliar
        UsuarioParteDt usuarioParteDt = new UsuarioParteDt();
        usuarioParteDt.setParteDt(parteDt);
        usuarioParteDt.setUsuario(parteDt.getUsuario());
        usuarioParteDt.setId_UsuarioLog(usuarioSessao.getId_Usuario());
        usuarioParteDt.setIpComputadorLog(usuarioSessao.getUsuarioDt().getIpComputadorLog());

        return usuarioParteDt;
    }

    /**
     * Método que monta o objeto UsuarioParteDt baseado nos dados da parte, ou
     * seja, aproveita os dados da parte para o cadastro de usuário.
     * 
     */
    private void setDadosUsuarioParte(HttpServletRequest request, UsuarioParteDt usuarioParteDt, UsuarioNe usuarioSessao){

        ProcessoParteDt parteDt = usuarioParteDt.getParteDt();
        if (parteDt.getCpf().length() > 0) {
            usuarioParteDt.setNome(parteDt.getNome());
            if (usuarioParteDt.getUsuario().length() == 0) usuarioParteDt.setUsuario(parteDt.getCpfCnpj());
            usuarioParteDt.setId_CtpsUf(parteDt.getId_CtpsUf());
            usuarioParteDt.setCtpsEstado(parteDt.getEstadoCtpsUf());
            usuarioParteDt.setId_Naturalidade(parteDt.getId_Naturalidade());
            usuarioParteDt.setNaturalidade(parteDt.getCidadeNaturalidade());
            usuarioParteDt.setId_Endereco(parteDt.getId_Endereco());
            usuarioParteDt.setEndereco(parteDt.getEndereco());
            usuarioParteDt.setId_RgOrgaoExpedidor(parteDt.getId_RgOrgaoExpedidor());
            usuarioParteDt.setRgOrgaoExpedidor(parteDt.getRgOrgaoExpedidor());
            usuarioParteDt.setSexo(parteDt.getSexo());
            usuarioParteDt.setDataNascimento(parteDt.getDataNascimento());
            usuarioParteDt.setRg(parteDt.getRg());
            usuarioParteDt.setRgDataExpedicao(parteDt.getRgDataExpedicao());
            usuarioParteDt.setCpf(parteDt.getCpf());
            usuarioParteDt.setTituloEleitor(parteDt.getTituloEleitor());
            usuarioParteDt.setTituloEleitorZona(parteDt.getTituloEleitorZona());
            usuarioParteDt.setTituloEleitorSecao(parteDt.getTituloEleitorSecao());
            usuarioParteDt.setCtps(parteDt.getCtps());
            usuarioParteDt.setCtpsSerie(parteDt.getCtpsSerie());
            usuarioParteDt.setPis(parteDt.getPis());
            usuarioParteDt.setServentiaCodigo(String.valueOf(ServentiaDt.Parte));
            usuarioParteDt.setGrupoCodigo(String.valueOf(GrupoDt.PARTES));
            usuarioParteDt.setEMail(parteDt.getEMail());
            usuarioParteDt.setTelefone(parteDt.getEMail());
            usuarioParteDt.setCelular(parteDt.getCelular());

            // ENDEREÇO DO USUÁRIO
            usuarioParteDt.setEnderecoUsuario(parteDt.getEnderecoParte());
            usuarioParteDt.getEnderecoUsuario().setId_UsuarioLog(usuarioSessao.getId_Usuario());
            usuarioParteDt.getEnderecoUsuario().setIpComputadorLog(usuarioSessao.getUsuarioDt().getIpComputadorLog());
        } else if (parteDt.getCnpj().length() == 0) {
            request.setAttribute("MensagemErro", "Parte deve ter CPF/CNPJ cadastrado para que seja habilitada como Usuário");
        }
    }

    /**
     * Método que captura os dados do usuário quando se tratar do cadastro
     * 
     * @param usuarioPartedt
     * @param request
     * @param usuarioSessao
     */
    private void getDadosUsuarioParte(UsuarioDt usuarioPartedt, HttpServletRequest request, UsuarioNe usuarioSessao) {
        usuarioPartedt.setUsuario(request.getParameter("Usuario"));
        usuarioPartedt.setId_CtpsUf(request.getParameter("Id_CtpsUf"));
        usuarioPartedt.setCtpsEstado(request.getParameter("CtpsEstado"));
        usuarioPartedt.setId_Naturalidade(request.getParameter("Id_Naturalidade"));
        usuarioPartedt.setNaturalidade(request.getParameter("Naturalidade"));
        usuarioPartedt.setId_Endereco(request.getParameter("Id_Endereco"));
        usuarioPartedt.setEndereco(request.getParameter("Endereco"));
        usuarioPartedt.setId_RgOrgaoExpedidor(request.getParameter("Id_RgOrgaoExpedidor"));
        usuarioPartedt.setRgOrgaoExpedidor(request.getParameter("RgOrgaoExpedidor"));
        usuarioPartedt.setSenha(request.getParameter("Senha"));
        usuarioPartedt.setNome(request.getParameter("Nome"));
        usuarioPartedt.setSexo(request.getParameter("Sexo"));
        usuarioPartedt.setDataNascimento(request.getParameter("DataNascimento"));
        usuarioPartedt.setRg(request.getParameter("Rg"));
        usuarioPartedt.setRgDataExpedicao(request.getParameter("RgDataExpedicao"));
        usuarioPartedt.setCpf(request.getParameter("Cpf"));
        usuarioPartedt.setTituloEleitor(request.getParameter("TituloEleitor"));
        usuarioPartedt.setTituloEleitorZona(request.getParameter("TituloEleitorZona"));
        usuarioPartedt.setTituloEleitorSecao(request.getParameter("TituloEleitorSecao"));
        usuarioPartedt.setCtps(request.getParameter("Ctps"));
        usuarioPartedt.setCtpsSerie(request.getParameter("CtpsSerie"));
        usuarioPartedt.setPis(request.getParameter("Pis"));
        usuarioPartedt.setMatriculaTjGo(request.getParameter("MatriculaTjGo"));
        usuarioPartedt.setNumeroConciliador(request.getParameter("NumeroConciliador"));
        usuarioPartedt.setDataCadastro(request.getParameter("DataCadastro"));
        usuarioPartedt.setId_Serventia(request.getParameter("Id_Serventia"));
        usuarioPartedt.setServentia(request.getParameter("Serventia"));
        usuarioPartedt.setId_Grupo(request.getParameter("Id_Grupo"));
        usuarioPartedt.setGrupo(request.getParameter("Grupo"));
        usuarioPartedt.setDataAtivo(request.getParameter("DataAtivo"));
        usuarioPartedt.setDataExpiracao(request.getParameter("DataExpiracao"));
        usuarioPartedt.setEMail(request.getParameter("EMail"));
        usuarioPartedt.setTelefone(request.getParameter("Telefone"));
        usuarioPartedt.setCelular(request.getParameter("Celular"));
        usuarioPartedt.setId_UsuarioLog(usuarioSessao.getId_Usuario());
        usuarioPartedt.setIpComputadorLog(usuarioSessao.getUsuarioDt().getIpComputadorLog());
        
        request.setAttribute("RgOrgaoExpedidorEstado", request.getParameter("RgOrgaoExpedidorEstado"));

        // ENDEREÇO DO USUÁRIO
        usuarioPartedt.getEnderecoUsuario().setLogradouro(request.getParameter("Logradouro"));
        usuarioPartedt.getEnderecoUsuario().setNumero(request.getParameter("Numero"));
        usuarioPartedt.getEnderecoUsuario().setComplemento(request.getParameter("Complemento"));
        usuarioPartedt.getEnderecoUsuario().setId_Bairro(request.getParameter("Id_Bairro"));
        usuarioPartedt.getEnderecoUsuario().setBairro(request.getParameter("Bairro"));
        usuarioPartedt.getEnderecoUsuario().setId_Cidade(request.getParameter("BairroId_Cidade"));
        usuarioPartedt.getEnderecoUsuario().setCidade(request.getParameter("BairroCidade"));
        usuarioPartedt.getEnderecoUsuario().setUf(request.getParameter("BairroUf"));
        usuarioPartedt.getEnderecoUsuario().setCep(request.getParameter("Cep"));
        usuarioPartedt.getEnderecoUsuario().setId_UsuarioLog(usuarioSessao.getId_Usuario());
        usuarioPartedt.getEnderecoUsuario().setIpComputadorLog(usuarioSessao.getUsuarioDt().getIpComputadorLog());
    }   

   

}
