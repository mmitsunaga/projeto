package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ArquivoTipoDt;
import br.gov.go.tj.projudi.dt.CertidaoDt;
import br.gov.go.tj.projudi.dt.ModeloDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.ne.CertidaoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class CertidaoExecucaoCt extends Controle {

	/**
     * 
     */
    private static final long serialVersionUID = 7882641098232568140L;

    public int Permissao() {
		return CertidaoDt.CodigoPermissao;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao,	int paginaatual, String nomebusca, 
			String posicaopaginaatual) throws Exception, ServletException, IOException {
		
		ProcessoDt processoDt = new ProcessoDt();
		CertidaoDt certidaoDt;
		CertidaoNe certidaoNe;
		ModeloDt modeloDt;
		List listaProcessoParte = null;
		int passoEditar = 0;
		String stAcao = "/WEB-INF/jsptjgo/CertidaoExecucao.jsp";
		String mensagem = "";
		
		String stNomeBusca1 = "";
		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		
		request.setAttribute("TituloPagina", "Certidão Circunstanciada");
		request.setAttribute("tempPrograma", "CertidaoExecucao");
		request.setAttribute("tempRetorno", "CertidaoExecucao");
		
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		
		certidaoNe = (CertidaoNe) request.getSession().getAttribute("certidaoNe");
		if (certidaoNe == null)	certidaoNe = new CertidaoNe();

		certidaoDt = (CertidaoDt) request.getSession().getAttribute("certidaoDt");
		if (certidaoDt == null)	certidaoDt = new CertidaoDt();

		modeloDt = (ModeloDt) request.getSession().getAttribute("modeloDt");
		if (modeloDt == null) modeloDt = new ModeloDt();
		setModelo(request, modeloDt);
		
		if (request.getParameter("PassoEditar") != null && !request.getParameter("PassoEditar").toString().equals("null")) 
			passoEditar = Funcoes.StringToInt(request.getParameter("PassoEditar"));
		
		certidaoDt.setCertidaoTipo(request.getParameter("modelo"));
		certidaoDt.setProcessoNumeroCompleto(request.getParameter("NumeroProcesso"));
		certidaoDt.setCpfCnpj(request.getParameter("Cpf"));
		certidaoDt.setRequerente(request.getParameter("Nome"));
		certidaoDt.setNomeMae(request.getParameter("NomeMae"));
		certidaoDt.setDataNascimento(request.getParameter("DataNascimento"));
		certidaoDt.setComarcaCodigo(UsuarioSessao.getUsuarioDt().getComarcaCodigo());
		certidaoDt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		certidaoDt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
		request.setAttribute("displayEditor", "none");
		
		switch (paginaatual) {
			case Configuracao.Novo:		
				stAcao = "/WEB-INF/jsptjgo/CertidaoExecucao.jsp";
				certidaoDt.limpar();
				modeloDt.limpar();
				break;
				
			case Configuracao.Localizar:
				mensagem = verificarDadosLocalizar(certidaoDt);
				if (mensagem.length() > 0){
					request.setAttribute("MensagemErro", mensagem);
					certidaoDt.limpar();
					modeloDt.limpar();
				} else {
					listaProcessoParte = certidaoNe.getListaParteProcessoExecucao(certidaoDt.getCpfCnpj(), certidaoDt.getRequerente(),
							certidaoDt.getNomeMae(), certidaoDt.getDataNascimento(), certidaoDt.getProcessoNumeroCompleto(), UsuarioSessao.getUsuarioDt().getId_Serventia());
					if (listaProcessoParte != null && listaProcessoParte.size() > 0) {
						request.setAttribute("ListaProcessoParte", listaProcessoParte);
					} else {
						request.setAttribute("MensagemErro","Não foi encontrado Sentenciado para o(s) parâmetro(s) informado(s). \n");
					}
				}
				break;
			
			case (ModeloDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
			if (request.getParameter("Passo")==null){
				String[] lisNomeBusca = {"Modelo"};
				String[] lisDescricao = {"Modelo","Serventia","Tipo Modelo"};
				stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
				request.setAttribute("tempBuscaId", "Id_Modelo");
				request.setAttribute("tempBuscaDescricao", "Modelo");
				request.setAttribute("tempBuscaPrograma", "Modelo");
				request.setAttribute("tempRetorno", "CertidaoExecucao");
				request.setAttribute("tempDescricaoId", "Id");
				request.setAttribute("tempDescricaoDescricao", "Modelo");
				request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
				request.setAttribute("PaginaAtual", (ModeloDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
				request.setAttribute("PosicaoPaginaAtual", "0");
				request.setAttribute("QuantidadePaginas", "0");
				request.setAttribute("lisNomeBusca", lisNomeBusca);
				request.setAttribute("lisDescricao", lisDescricao);
//				passoEditar = 0;
			}else{
				String stTemp = "";
				String idArquivoTipo = certidaoNe.consultarIdArquivoTipo(String.valueOf(ArquivoTipoDt.CERTIDAO_CIRCUNSTANCIADA_CODIGO));
				stTemp = certidaoNe.consultarModeloJSON(UsuarioSessao.getUsuarioDt(), idArquivoTipo, stNomeBusca1, posicaopaginaatual);
					
					enviarJSON(response, stTemp);
					
				
				return;
			}
				break;
				
			default:
				switch(passoEditar){
				//carrega os dados do processo do sentenciado informado (permaneceu passoEditar=9, para reutilização da jsp: DadosProcessoParteExecucao.jsp
				case 9:
					processoDt = certidaoNe.consultarProcesso(request.getParameter("Id_Processo"));
					processoDt.addListaPolosPassivos(certidaoNe.consultarParte(request.getParameter("Id_ProcessoParte")));
					setTextoModelo(UsuarioSessao, certidaoDt, certidaoNe, modeloDt, processoDt);
					
					request.setAttribute("displayEditor", "");
					request.setAttribute("modelo", request.getParameter("modelo"));
					request.setAttribute("NumeroProcesso", request.getParameter("NumeroProcesso"));
					request.setAttribute("Cpf", request.getParameter("Cpf"));
					request.setAttribute("Nome", request.getParameter("Nome"));
					request.setAttribute("NomeMae", request.getParameter("NomeMae"));
					request.setAttribute("DataNascimento", request.getParameter("DataNascimento"));
					stAcao = "/WEB-INF/jsptjgo/CertidaoExecucaoEditar.jsp";
					break;
				}
				break;
		}

		request.setAttribute("TextoEditor", modeloDt != null?modeloDt.getTexto():"");
		request.getSession().setAttribute("certidaoDt", certidaoDt);
		request.getSession().setAttribute("certidaoNe", certidaoNe);
		request.getSession().setAttribute("modeloDt", modeloDt);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
	
	
	protected void setModelo(HttpServletRequest request, ModeloDt modeloDt){
		if (request.getParameter("Id_Modelo") != null)
			if (request.getParameter("Id_Modelo").equalsIgnoreCase("null")){
				modeloDt.setId("");
		        modeloDt.setModelo("");
		        modeloDt.limpar();
			} else {
				modeloDt.setId( request.getParameter("Id_Modelo") );
			    modeloDt.setModelo(request.getParameter("Modelo"));
			}
	}
	
	protected void setTextoModelo(UsuarioNe UsuarioSessao, CertidaoDt certidaoDt, CertidaoNe certidaoNe, ModeloDt modeloDt, ProcessoDt processoDt) throws Exception{
		modeloDt.setTexto( certidaoNe.montaModeloCertidaoCircunstanciada(modeloDt.getId(), UsuarioSessao.getUsuarioDt(), processoDt) );	
	}
	
	protected String verificarDadosLocalizar(CertidaoDt certidaoDt){
		String stRetorno = ""; 
		if ((certidaoDt.getCertidaoTipo()==null) || (certidaoDt.getCertidaoTipo().length()==0))
			stRetorno = "O campo Modelo da Certidão é obrigatório.";
		if (certidaoDt.getRequerente().length() == 0 && certidaoDt.getCpfCnpj().length() == 0 
				&& certidaoDt.getNomeMae().length() == 0 && certidaoDt.getDataNascimento().length() == 0 && certidaoDt.getProcessoNumeroCompleto().length() == 0)
			stRetorno += "Informe o(s) parâmetro(s) de consulta do Sentenciado.";
			
		return stRetorno;
	}
	
	protected boolean consultarModelo(HttpServletRequest request,	String tempNomeBusca, String posicaoPaginaAtual, CertidaoNe certidaoNe, 
			int paginaatual, UsuarioNe usuarioSessao) throws Exception{
		boolean boRetorno = false;

		String idArquivoTipo = certidaoNe.consultarIdArquivoTipo(String.valueOf(ArquivoTipoDt.CERTIDAO_CIRCUNSTANCIADA_CODIGO));
		List tempList = certidaoNe.consultarModelo(usuarioSessao.getUsuarioDt(), idArquivoTipo, tempNomeBusca, posicaoPaginaAtual);
		if (tempList != null && tempList.size() > 0) {
			request.setAttribute("ListaModelo", tempList);
			request.setAttribute("PaginaAtual", paginaatual);
			request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(posicaoPaginaAtual));
			request.setAttribute("QuantidadePaginas", certidaoNe.getQuantidadePaginas());
			request.setAttribute("tempBuscaId_Modelo", "Id_Modelo");
			request.setAttribute("tempBuscaModelo", "Modelo");
			boRetorno = true;
		} else
			request.setAttribute("MensagemErro", "Nenhum Modelo foi localizado.");
		return boRetorno;
	}
}