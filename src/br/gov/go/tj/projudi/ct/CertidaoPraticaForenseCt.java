package br.gov.go.tj.projudi.ct;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.CertidaoPraticaForenseDt;
import br.gov.go.tj.projudi.dt.CidadeDt;
import br.gov.go.tj.projudi.dt.EstadoCivilDt;
import br.gov.go.tj.projudi.dt.ModeloDt;
import br.gov.go.tj.projudi.ne.CertidaoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.Certificado.Signer;
import br.gov.go.tj.utils.pdf.ConverterHtmlPdf;

public class CertidaoPraticaForenseCt extends Controle {

	private static final long serialVersionUID = 5850769893684390343L;

	public int Permissao() {
		return CertidaoPraticaForenseDt.CodigoPermissao;
	}

	@Override
	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String nomebusca, String posicaopaginaatual) throws Exception, ServletException, IOException {
		CertidaoPraticaForenseDt certidaoDt;
		CertidaoNe certidaoNe;
		ModeloDt modeloDt;
		String stAcao;
		String stNomeBusca1 = "";
		String stNomeBusca2 = "";

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");
		
		request.setAttribute("MensagemErro", "");
		request.setAttribute("TituloPagina", "Certidão de Pratica Forense");
		request.setAttribute("tempPrograma", "Certidão de Pratica Forense");
		request.setAttribute("tempNomeBusca", "Certidão de Pratica Forense");
		request.setAttribute("tempRetorno", "Certidão de PraticaForense");
		request.setAttribute("PaginaAnterior", paginaatual);

		certidaoNe = (CertidaoNe) request.getSession().getAttribute("certidaoNe");
		if (certidaoNe == null)	certidaoNe = new CertidaoNe();
		
		certidaoDt = (CertidaoPraticaForenseDt) request.getSession().getAttribute("certidaoPraticaForenseDt");
		if (certidaoDt == null)	certidaoDt = new CertidaoPraticaForenseDt();
		
		modeloDt = (ModeloDt) request.getSession().getAttribute("modeloDt");
		if (modeloDt == null) modeloDt = new ModeloDt();
		
		if (request.getParameter("Tipo") != null && !request.getParameter("Tipo").equals("")) {
			certidaoDt.setTipo(request.getParameter("Tipo"));
		}
		if (request.getParameter("OabNumero") != null && !request.getParameter("OabNumero").equals("")) {
			certidaoDt.setOab(request.getParameter("OabNumero"));
		}
		if (request.getParameter("OabComplemento") != null && !request.getParameter("OabComplemento").equals("")) {
			certidaoDt.setOabComplemento(request.getParameter("OabComplemento"));
		}
		if (request.getParameter("OabUf") != null && !request.getParameter("OabUf").equals("")) {
			String[] aux = request.getParameter("OabUf").split("-");
			certidaoDt.setOabUfCodigo(aux[0]);
			certidaoDt.setOabUf(aux[1]);
		}
		if (request.getParameter("Nome") != null && !request.getParameter("Nome").isEmpty()) {
			certidaoDt.setNome(request.getParameter("Nome"));
		}
		String cpf = request.getParameter("Cpf");
		if (cpf != null && !cpf.isEmpty()) {
			certidaoDt.setCpf(cpf);
		}
		String sexo = request.getParameter("Sexo");
		if (sexo != null && !cpf.isEmpty()) {
			certidaoDt.setSexo(sexo);
		}
		String rg = request.getParameter("Rg");
		if (rg != null && !rg.isEmpty()) {
			certidaoDt.setIdentidade(rg);
		}
		certidaoDt.setEstadoCivil(request.getParameter("EstadoCivil"));
		certidaoDt.setId_EstadoCivil(request.getParameter("Id_EstadoCivil"));
		certidaoDt.setNaturalidade(request.getParameter("Cidade"));
		certidaoDt.setId_Naturalidade(request.getParameter("Id_Cidade"));

		if (request.getParameter("MesInicial") != null && !request.getParameter("MesInicial").equals("")) {
			certidaoDt.setMesInicial(request.getParameter("MesInicial"));
		}
		if (request.getParameter("AnoInicial") != null && !request.getParameter("AnoInicial").equals("")) {
			certidaoDt.setAnoInicial(request.getParameter("AnoInicial"));
		}
		if (request.getParameter("MesFinal") != null && !request.getParameter("MesFinal").equals("")) {
			certidaoDt.setMesFinal(request.getParameter("MesFinal"));
		}
		if (request.getParameter("AnoFinal") != null && !request.getParameter("AnoFinal").equals("")) {
			certidaoDt.setAnoFinal(request.getParameter("AnoFinal"));
		}

		stAcao = "/WEB-INF/jsptjgo/CertidaoPraticaForense.jsp";

		certidaoDt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		certidaoDt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());

		switch (paginaatual) {

			case Configuracao.Localizar:
				if (certidaoDt.getOab() != null && !certidaoDt.getOab().isEmpty() && certidaoDt.getOabComplemento() != null && !certidaoDt.getOabComplemento().isEmpty() && certidaoDt.getOabUf() != null && !certidaoDt.getOabUf().isEmpty()) {
					boolean quantitativa = certidaoDt.getTipo().equals("Quantitativa");
					long quantidade = 0;
					if (quantitativa) {
						quantidade = certidaoNe.getQuantidadeProcessosAdvogado(certidaoDt);
						certidaoDt.setModeloCodigo(CertidaoPraticaForenseDt.PRATICA_FORENSE_QUANTITATIVA_MODELO_CODIGO);
						certidaoDt.setQuantidade((int) quantidade);
					} else {
						certidaoDt.setListaProcesso(certidaoNe.getListaProcesso(certidaoDt));
						certidaoDt.setModeloCodigo(CertidaoPraticaForenseDt.PRATICA_FORENSE_MODELO_CODIGO);
					}
	
					modeloDt = certidaoNe.consultarModeloCodigo(String.valueOf(certidaoDt.getModeloCodigo()));
					certidaoDt.setTexto(certidaoNe.montaModelo(certidaoDt, modeloDt, UsuarioSessao));
	
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					request.setAttribute("tempRetorno", "CertidaoPraticaForense");
	
				} else {
					String campo = "";
					if (certidaoDt.getOab() == null || certidaoDt.getOab().isEmpty()) {
						campo = " Número da OAB,";
					}
					if (certidaoDt.getOabComplemento() == null || certidaoDt.getOabComplemento().isEmpty()) {
						campo += " Oab Complemento,";
					}
					if (certidaoDt.getOabUf() == null || certidaoDt.getOabUf().isEmpty()) {
						campo += " Oab Uf, ";
					}
	
					request.setAttribute("MensagemErro", "Informe os dados do Advogado\nPreencha o(s) campo(s)" + campo + " e tente novamente.");
					certidaoDt = new CertidaoPraticaForenseDt();
					modeloDt = new ModeloDt();
				}
				break;
	
			case Configuracao.Novo:
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				request.setAttribute("tempRetorno", "CertidaoPraticaForense");
				stAcao = "/WEB-INF/jsptjgo/CertidaoPraticaForense.jsp";
				certidaoDt = new CertidaoPraticaForenseDt();
	
				modeloDt = new ModeloDt();
				break;
	
			case Configuracao.Imprimir: {
	
				if (certidaoDt == null || certidaoDt.getTexto().equals("")) {
					request.setAttribute("MensagemErro", "Informe os dados do Advogado\nE gere a certidão!");
				} else {
					byte[] byTemp = null;
					Signer.acceptSSL();
					// ByteArrayOutputStream baos = ConverterHtmlPdf.convert(certidaoDt.getTexto().getBytes());
					byTemp = ConverterHtmlPdf.converteHtmlPDF(certidaoDt.getTexto().getBytes(), false);
					
					String nome = Funcoes.iniciaisNome(certidaoDt.getNome()) + certidaoDt.getOabNumero() + "-" + certidaoDt.getOabUf() ;
					enviarPDF(response, byTemp, nome );					
											
					byTemp = null;
				}
				return;
	
			}
			case (EstadoCivilDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo") == null) {
					String[] lisNomeBusca = {"EstadoCivil" };
					String[] lisDescricao = {"EstadoCivil" };
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_EstadoCivil");
					request.setAttribute("tempBuscaDescricao", "EstadoCivil");
					request.setAttribute("tempBuscaPrograma", "EstadoCivil");
					request.setAttribute("tempRetorno", "CertidaoPraticaForense");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (EstadoCivilDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = "";
					stTemp = certidaoNe.consultarDescricaoEstadoCivilJSON(stNomeBusca1, posicaopaginaatual);
					
						enviarJSON(response, stTemp);
						
					
					return;
				}
				break;
	
			case (CidadeDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Cidade","Uf"};
					String[] lisDescricao = {"Cidade","Uf"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_Cidade");
					request.setAttribute("tempBuscaDescricao", "Cidade");
					request.setAttribute("tempBuscaPrograma", "Cidade");
					request.setAttribute("tempRetorno", "CertidaoPraticaForense");
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

		request.getSession().setAttribute("certidaoPraticaForenseDt", certidaoDt);
		request.getSession().setAttribute("certidaoNe", certidaoNe);
		request.getSession().setAttribute("modeloDt", modeloDt);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);

	}

}
