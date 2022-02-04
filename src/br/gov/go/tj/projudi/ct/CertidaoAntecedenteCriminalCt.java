package br.gov.go.tj.projudi.ct;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.CertidaoAntecedenteCriminalDt;
import br.gov.go.tj.projudi.dt.CertidaoValidacaoDt;
import br.gov.go.tj.projudi.dt.CidadeDt;
import br.gov.go.tj.projudi.dt.EstadoCivilDt;
import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.ModeloDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteDt;
import br.gov.go.tj.projudi.dt.ProfissaoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.ne.CertidaoNe;
import br.gov.go.tj.projudi.ne.ProcessoNe;
import br.gov.go.tj.projudi.ne.ProcessoParteNe;
import br.gov.go.tj.projudi.ne.ServentiaNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.TJDataHora;
import br.gov.go.tj.utils.Certificado.Signer;

public class CertidaoAntecedenteCriminalCt extends Controle {

	private static final long serialVersionUID = 7991541641929161456L;

	@Override
	public int Permissao() {
		return CertidaoAntecedenteCriminalDt.CodigoPermissao;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String nomebusca, String posicaopaginaatual) throws Exception, ServletException, IOException {
		CertidaoAntecedenteCriminalDt certidaoAntecedenteCriminalDt;
		CertidaoNe certidaoNe;
		ModeloDt modeloDt;
		String stAcao;
		String posicaoLista = "";
		String stNomeBusca1 = "";
		String stNomeBusca2 = "";

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");
		
		certidaoNe = (CertidaoNe) request.getSession().getAttribute("certidaoNe");
		if (certidaoNe == null)
			certidaoNe = new CertidaoNe();

		certidaoAntecedenteCriminalDt = (CertidaoAntecedenteCriminalDt) request.getSession().getAttribute("certidaoAntecedenteCriminalDt");
		if (certidaoAntecedenteCriminalDt == null)
			certidaoAntecedenteCriminalDt = new CertidaoAntecedenteCriminalDt();

		modeloDt = (ModeloDt) request.getSession().getAttribute("modeloDt");
		if (modeloDt == null)
			modeloDt = new ModeloDt();		
	
		request.setAttribute("MensagemErro","");
		
		request.setAttribute("TituloPagina", "Certidão/Informação de Antecedentes Infracionais / Menor");
		request.setAttribute("tempPrograma", "Certidão de Antecedentes Criminais");
		request.setAttribute("tempNomeBusca", "Certidão de Antecedentes Criminais");
		request.setAttribute("tempRetorno", "Certidão de Antecedentes Criminais");
		request.setAttribute("PaginaAnterior", paginaatual);
	
		certidaoAntecedenteCriminalDt.setNome(request.getParameter("Nome"));
		certidaoAntecedenteCriminalDt.setCpfCnpj(request.getParameter("Cpf"));
		certidaoAntecedenteCriminalDt.setSexo(request.getParameter("Sexo"));
		certidaoAntecedenteCriminalDt.setNacionalidade(request.getParameter("Nacionalidade"));
		certidaoAntecedenteCriminalDt.setEstadoCivil(request.getParameter("EstadoCivil"));
		certidaoAntecedenteCriminalDt.setId_EstadoCivil(request.getParameter("Id_EstadoCivil"));
		certidaoAntecedenteCriminalDt.setProfissao(request.getParameter("Profissao"));
		certidaoAntecedenteCriminalDt.setId_Profissao(request.getParameter("Id_Profissao"));
		String dataNascimento = request.getParameter("DataNascimento");
		certidaoAntecedenteCriminalDt.setDataNascimento(dataNascimento);
		certidaoAntecedenteCriminalDt.setNomeMae(request.getParameter("NomeMae"));
		certidaoAntecedenteCriminalDt.setNomePai(request.getParameter("NomePai"));
		certidaoAntecedenteCriminalDt.setId_Naturalidade(request.getParameter("Id_Naturalidade"));
		certidaoAntecedenteCriminalDt.setNaturalidade(request.getParameter("Naturalidade"));
		certidaoAntecedenteCriminalDt.setRg(request.getParameter("Rg"));
		certidaoAntecedenteCriminalDt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		certidaoAntecedenteCriminalDt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
		certidaoAntecedenteCriminalDt.setComarcaCodigo(UsuarioSessao.getUsuarioDt().getComarcaCodigo());
		certidaoAntecedenteCriminalDt.setComarca(certidaoNe.consultarComarca(certidaoAntecedenteCriminalDt.getComarcaCodigo()));
		certidaoAntecedenteCriminalDt.setChkMenorInfrator(request.getParameter("chkMenorInfrator"));
		request.setAttribute("bloquearCampos", false);

		//lista de certidões que são marcadas no checkbox para inserir posteriomente na certidão
		String[] listaProcesssosSelecionadosCertidao = request.getParameterValues("listaProcessosCertidao");
		
		stAcao = "/WEB-INF/jsptjgo/CertidaoAntecedenteCriminal.jsp";
		
		switch (paginaatual) {
		
		case Configuracao.Localizar:
			
			String cpf = certidaoAntecedenteCriminalDt.getCpfCnpj();
			if(certidaoAntecedenteCriminalDt.getNome().equals("")) {
				request.setAttribute("MensagemErro","Informe o Nome!");
				certidaoAntecedenteCriminalDt = new CertidaoAntecedenteCriminalDt();
				modeloDt = new ModeloDt();

			} else if (cpf != null && !cpf.isEmpty() && !Funcoes.testaCPFCNPJ(cpf)) {
				request.setAttribute("MensagemErro","Informe um cpf ou cnpj valido com todos dígitos, inclusive zero!");
			} else if (dataNascimento != null && !dataNascimento.isEmpty() && !Funcoes.validaData(dataNascimento)) {
				request.setAttribute("MensagemErro","A data de nascimento informada é inválida: " + dataNascimento);
			} else {
				//Se o check estiver marcado, é certidão de menor infrator. Se não, é certidão comum.
				if(certidaoAntecedenteCriminalDt.getChkMenorInfrator() !=  null && certidaoAntecedenteCriminalDt.getChkMenorInfrator().equals("true")){
					try{
						//Certidão para menor infrator
						certidaoAntecedenteCriminalDt.setListaProcesso(certidaoNe.recuperarListaDeProcesso(certidaoAntecedenteCriminalDt.getNome(), certidaoAntecedenteCriminalDt.getCpfCnpj(), certidaoAntecedenteCriminalDt.getDataNascimento(), certidaoAntecedenteCriminalDt.getNomeMae(), certidaoAntecedenteCriminalDt.getNomePai(), certidaoAntecedenteCriminalDt.getId_Naturalidade(), certidaoAntecedenteCriminalDt.getRg(), true));
						//Após realizar a consulta de processos, deve bloquear os cmapos da tela para evitar alteração indevida dos mesmos
						request.setAttribute("bloquearCampos", true);
					} catch(Exception e) {
						if(e.getMessage().split("ErroSPG=").length > 1)
							request.setAttribute("MensagemErro", e.getMessage().split("ErroSPG=")[1]);
						certidaoAntecedenteCriminalDt = new CertidaoAntecedenteCriminalDt();
						modeloDt = new ModeloDt();
						throw new Exception(e);
					}
				} else {
					try{
						//Certidão comum
						certidaoAntecedenteCriminalDt.setListaProcesso(certidaoNe.recuperarListaDeProcesso(certidaoAntecedenteCriminalDt.getNome(), certidaoAntecedenteCriminalDt.getCpfCnpj(), certidaoAntecedenteCriminalDt.getDataNascimento(), certidaoAntecedenteCriminalDt.getNomeMae(), certidaoAntecedenteCriminalDt.getNomePai(), certidaoAntecedenteCriminalDt.getId_Naturalidade(), certidaoAntecedenteCriminalDt.getRg(), false));
						//Após realizar a consulta de processos, deve bloquear os cmapos da tela para evitar alteração indevida dos mesmos
						request.setAttribute("bloquearCampos", true);
					} catch(Exception e) {
						if(e.getMessage().split("ErroSPG=").length > 1)
							request.setAttribute("MensagemErro", e.getMessage().split("ErroSPG=")[1]);
						certidaoAntecedenteCriminalDt = new CertidaoAntecedenteCriminalDt();
						modeloDt = new ModeloDt();
						throw new Exception(e);						
					}
				}
				
				request.setAttribute("chkMenorInfrator", certidaoAntecedenteCriminalDt.getChkMenorInfrator());
				request.setAttribute("PaginaAtual", Configuracao.Localizar);
				request.setAttribute("tempRetorno", "CertidaoAntecedenteCriminal");	
				
			}
			break;
		case Configuracao.Novo:		
			request.setAttribute("PaginaAtual", Configuracao.Editar);
			request.setAttribute("tempRetorno", "CertidaoAntecedenteCriminal");
			stAcao = "/WEB-INF/jsptjgo/CertidaoAntecedenteCriminal.jsp";
			certidaoAntecedenteCriminalDt = new CertidaoAntecedenteCriminalDt();
					
			modeloDt = new ModeloDt();
			break;
		case Configuracao.LocalizarAutoPai:
			posicaoLista = request.getParameter("posicaoLista");
			int posicao = Funcoes.StringToInt(posicaoLista,-1);
			if (posicao!=-1) {				
				certidaoAntecedenteCriminalDt.removeProcesso(posicao);
				modeloDt = certidaoNe.consultarModeloCodigo(certidaoAntecedenteCriminalDt.getModeloCodigo(UsuarioSessao, Boolean.valueOf(certidaoAntecedenteCriminalDt.getChkMenorInfrator())));
				certidaoAntecedenteCriminalDt.setTexto(certidaoNe.montaModelo(certidaoAntecedenteCriminalDt, modeloDt, UsuarioSessao));
			}
			
			stAcao = "/WEB-INF/jsptjgo/CertidaoAntecedenteCriminal.jsp";
			request.setAttribute("PaginaAtual", Configuracao.Editar);
			request.setAttribute("tempRetorno", "CertidaoAntecedenteCriminal");
			break;
			
		case Configuracao.Imprimir: {
			ProcessoParteDt procParteDt = null;
			boolean menorInfrator = false;
			boolean impressaoTelaConsultaCertidao = false;
			if(request.getParameter("impressaoTelaConsultaCertidao") != null) {
				impressaoTelaConsultaCertidao = Boolean.valueOf(request.getParameter("impressaoTelaConsultaCertidao"));
			}
				
			//Só ocorrerá este cenário quando a impressão da certidão ocorrer a partir da capa do processo
			if(impressaoTelaConsultaCertidao) {
				menorInfrator = Boolean.valueOf(certidaoAntecedenteCriminalDt.getChkMenorInfrator());
			} else {
				procParteDt = new ProcessoParteNe().consultarIdCompleto(request.getParameter("id_Parte"));
				ProcessoDt procDt = new ProcessoNe().consultarId(procParteDt.getId_Processo()); 
				ServentiaDt servProcDt = new ServentiaNe().consultarId(procDt.getId_Serventia());
				if(servProcDt.isJuizadoInfanciaJuventudeInfracional()) {
					menorInfrator = true;
				}
				certidaoAntecedenteCriminalDt.setListaProcesso(certidaoNe.recuperarListaDeProcesso(procParteDt.getNome(), procParteDt.getCpfCnpj(), procParteDt.getDataNascimento(), procParteDt.getNomeMae(), procParteDt.getNomePai(), procParteDt.getId_Naturalidade(), procParteDt.getRg(), menorInfrator));
			}
			
			byte[] byTemp = null;
			Signer.acceptSSL();
			byTemp = certidaoNe.gerarCertidaoAntecedentesJasper(certidaoAntecedenteCriminalDt, listaProcesssosSelecionadosCertidao, UsuarioSessao, procParteDt, menorInfrator, impressaoTelaConsultaCertidao);		
			if(!menorInfrator && impressaoTelaConsultaCertidao) {
				TJDataHora tjdathora = new TJDataHora();
				tjdathora.adicioneDia(30);
				CertidaoValidacaoDt cdt = new CertidaoValidacaoDt(byTemp,tjdathora.getDataFormatadaddMMyyyyHHmmss(),new TJDataHora().getDataFormatadaddMMyyyyHHmmss());
				cdt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
				cdt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
				certidaoNe.salvar(cdt);
				byTemp = certidaoNe.gerarPdfPublicacao(ProjudiPropriedades.getInstance().getCaminhoAplicacao() , cdt);
			}
				
			String nome="CertidaoAntecedenteCriminal" + Funcoes.iniciaisNome(certidaoAntecedenteCriminalDt.getNome()) + certidaoAntecedenteCriminalDt.getCpfCnpj()  ;
			enviarPDFDownload(response, byTemp,nome);
			byTemp = null;
			return;
		}
		
		// Consulta de Estado Civil - Usado no cadastro de partes
		case (EstadoCivilDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
			if (request.getParameter("Passo")==null){
				String[] lisNomeBusca = {"EstadoCivil"};
				String[] lisDescricao = {"EstadoCivil"};
				stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
				request.setAttribute("tempBuscaId", "Id_EstadoCivil");
				request.setAttribute("tempBuscaDescricao", "EstadoCivil");
				request.setAttribute("tempBuscaPrograma", "EstadoCivil");
				request.setAttribute("tempRetorno", "CertidaoAntecedenteCriminal");
				request.setAttribute("tempDescricaoId", "Id");
				request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
				request.setAttribute("PaginaAtual", (EstadoCivilDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
				request.setAttribute("PosicaoPaginaAtual", "0");
				request.setAttribute("QuantidadePaginas", "0");
				request.setAttribute("lisNomeBusca", lisNomeBusca);
				request.setAttribute("lisDescricao", lisDescricao);
			}else{
				String stTemp = "";
				stTemp = certidaoNe.consultarDescricaoEstadoCivilJSON(stNomeBusca1, posicaopaginaatual);
									
					enviarJSON(response, stTemp);
					
				
				return;
			}
			break;
			
			// Consulta de Profissão - Usado no cadastro de partes
		case (ProfissaoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
			if (request.getParameter("Passo")==null){
				String[] lisNomeBusca = {"Profissao"};
				String[] lisDescricao = {"Profissao"};
				stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
				request.setAttribute("tempBuscaId", "Id_Profissao");
				request.setAttribute("tempBuscaDescricao", "Profissao");
				request.setAttribute("tempBuscaPrograma", "Profissao");
				request.setAttribute("tempRetorno", "CertidaoAntecedenteCriminal");
				request.setAttribute("tempDescricaoId", "Id");
				request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
				request.setAttribute("PaginaAtual", (ProfissaoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
				request.setAttribute("PosicaoPaginaAtual", "0");
				request.setAttribute("QuantidadePaginas", "0");
				request.setAttribute("lisNomeBusca", lisNomeBusca);
				request.setAttribute("lisDescricao", lisDescricao);
			}else{
				String stTemp = "";
				stTemp = certidaoNe.consultarDescricaoProfissaoJSON(stNomeBusca1, posicaopaginaatual);
									
					enviarJSON(response, stTemp);
					
				
				return;
			}
			break;
		
		case (CidadeDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
			if (request.getParameter("Passo")==null){
				String[] lisNomeBusca = {"Cidade","Uf"};
				String[] lisDescricao = {"Cidade","Uf"};
				stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
				request.setAttribute("tempBuscaId", "Id_Naturalidade");
				request.setAttribute("tempBuscaDescricao", "Naturalidade");
				request.setAttribute("tempBuscaPrograma", "Cidade");
				request.setAttribute("tempRetorno", "CertidaoAntecedenteCriminal");
				request.setAttribute("tempDescricaoId", "Id");
				request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
				request.setAttribute("PaginaAtual", (CidadeDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
				request.setAttribute("PosicaoPaginaAtual", "0");
				request.setAttribute("QuantidadePaginas", "0");
				request.setAttribute("lisNomeBusca", lisNomeBusca);
				request.setAttribute("lisDescricao", lisDescricao);
			}else{
				String stTemp = "";
				stTemp = certidaoNe.consultarDescricaoCidadeJSON(stNomeBusca1, stNomeBusca2, posicaopaginaatual);
									
					enviarJSON(response, stTemp);
									
				
				return;
			}
			break;
			
		default:
			break;
		}
		

		if( UsuarioSessao.isJuventudeInfracional() || UsuarioSessao.isGrupoCodigo(new int[]{GrupoDt.MINISTERIO_PUBLICO ,GrupoDt.ANALISTA_JUDICIARIO_INFANCIA_JUVENTUDE_INFRACIONAL}) ){						
			request.getSession().setAttribute("exibirCheckMenorInfrator", true);
		}
		
		request.getSession().setAttribute("certidaoAntecedenteCriminalDt", certidaoAntecedenteCriminalDt);
		request.getSession().setAttribute("certidaoNe", certidaoNe);
		request.getSession().setAttribute("modeloDt", modeloDt);			
		
		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
		
	}

}
