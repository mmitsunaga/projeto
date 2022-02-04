package br.gov.go.tj.projudi.ct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.CertidaoGuiaDt;
import br.gov.go.tj.projudi.dt.CertidaoValidacaoDt;
import br.gov.go.tj.projudi.dt.GuiaTipoDt;
import br.gov.go.tj.projudi.dt.ModeloDt;
import br.gov.go.tj.projudi.dt.MovimentacaoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.ne.CertidaoGuiaNe;
import br.gov.go.tj.projudi.ne.CertidaoNe;
import br.gov.go.tj.projudi.ne.ModeloNe;
import br.gov.go.tj.projudi.ne.ProcessoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;
import br.gov.go.tj.utils.TJDataHora;
import br.gov.go.tj.utils.Certificado.Signer;
import br.gov.go.tj.utils.pdf.ConverterHtmlPdf;

public class CertidaoNarrativaSemCustasCt extends Controle {

	private static final long serialVersionUID = 5850769893684390343L;
	public int Permissao() {
		return CertidaoGuiaDt.CodigoPermissao;
	}

	@Override
	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe usuarioSessao, int paginaatual, String nomebusca, String posicaopaginaatual) throws Exception, ServletException, IOException {
		
		CertidaoGuiaDt certidaoGuiaDt;
		CertidaoGuiaNe certidaoGuiaNe;
		CertidaoNe certidaoNe = new CertidaoNe();
		ProcessoDt processoDt = null;
		String idProcesso = "";
		String id_GuiaTipo = "";
				
		String stAcao = "/WEB-INF/jsptjgo/CertidaoNarrativaSemCustas.jsp";
		
		request.setAttribute("MensagemErro", "");
		request.setAttribute("tempRetorno", "CertidaoNarrativaSemCustas");
		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("TituloPagina", "Certidão Narrativa Sem Custas");
		request.setAttribute("tempPrograma", "Certidão Narrativa Sem Custas");
		request.setAttribute("tempNomeBusca", "Certidão Narrativa Sem Custas");

		certidaoGuiaNe = (CertidaoGuiaNe) request.getSession().getAttribute("certidaoNe");
		if (certidaoGuiaNe == null){
			certidaoGuiaNe = new CertidaoGuiaNe();
		}
		
		certidaoGuiaDt = (CertidaoGuiaDt) request.getSession().getAttribute("certidaoGuiaDt");
		if (certidaoGuiaDt == null){
			certidaoGuiaDt = new CertidaoGuiaDt();
		}
		
		processoDt = (ProcessoDt) request.getSession().getAttribute("processoDt");		
		if ( processoDt != null ) {
			request.setAttribute("idProcesso", processoDt.getId());
			idProcesso = processoDt.getId();
		}
		certidaoGuiaDt.setIdProcesso(idProcesso);

		id_GuiaTipo = new GuiaTipoDt().ID_GUIA_DE_CERTIDAO_NARRATIVA;
		certidaoGuiaDt.setId_GuiaTipo(id_GuiaTipo);
		request.getSession().setAttribute("id_GuiaTipo", id_GuiaTipo);
				
		if(certidaoGuiaDt != null){
			certidaoGuiaDt.setId_UsuarioLog(usuarioSessao.getId_Usuario());
			certidaoGuiaDt.setIpComputadorLog(usuarioSessao.getUsuarioDt().getIpComputadorLog());
		}
		
		certidaoGuiaDt.setNome(request.getParameter("Nome"));
		certidaoGuiaDt.setCpf(request.getParameter("Cpf"));

		switch (paginaatual) {
			case Configuracao.Novo:

				request.getSession().removeAttribute("certidaoGuiaDt");
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				request.setAttribute("tempRetorno", "CertidaoNarrativaSemCustas");
				certidaoGuiaDt = new CertidaoGuiaDt();
				break;

			// Localizar Guia
			case Configuracao.Localizar:
				
				certidaoGuiaDt = certidaoGuiaNe.consultarDadosProcesso(idProcesso);
				certidaoGuiaDt.setIdProcesso(idProcesso);
				certidaoGuiaDt.setMovimentacoesProcesso(levantarMovimentacoes(idProcesso));
				
				certidaoGuiaDt.setCustaCertidao("00,00");
				certidaoGuiaDt.setCustaTaxaJudiciaria("00,00");
				certidaoGuiaDt.setCustaTotal("00,00");
				certidaoGuiaDt.setId_UsuarioLog(usuarioSessao.getId_Usuario());
				certidaoGuiaDt.setIpComputadorLog(usuarioSessao.getUsuarioDt().getIpComputadorLog());
				certidaoGuiaDt.setSexo("");
				
				break;
				
			// Gerar Pré-Visualização da Certidão
			case Configuracao.Curinga6:
				
				if((request.getParameter("Nome").equalsIgnoreCase("")) || (request.getParameter("Cpf").equalsIgnoreCase(""))){
					throw new MensagemException("Preecha todos os campos.");					
				} else {
					if(!Funcoes.testaCPFCNPJ(certidaoGuiaDt.getCpf())){
						throw new MensagemException("CPF inválido.");
					} else {
						certidaoGuiaDt.setTextoCertidao(request.getParameter("textoCertidao")); // Campo texto usado na certidão narrativa.						
						certidaoGuiaDt.setMovimentacoesProcesso(levantarMovimentacoes(idProcesso)); // INSERÇÃO, NA CERTIDÃO, DAS MOVIMENTAÇÕES DO PROCESSO
						certidaoGuiaDt.setTexto(new ModeloNe().montaConteudoPorModelo(certidaoGuiaDt, usuarioSessao.getUsuarioDt(), ModeloDt.NARRATIVA_MODELO_CODIGO));
					}
				}
				break;
			
			// Salvar / Gerar PDF (Utilizado para salvar e imprimir a certidão)
			case Configuracao.Salvar:
				
				gerarCertidao(request, response, usuarioSessao, certidaoGuiaDt, certidaoNe);
				break;
		}
		request.getSession().setAttribute("certidaoGuiaDt", certidaoGuiaDt);
		request.getSession().setAttribute("certidaoGuiaNe", certidaoGuiaNe);
		
		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
	
	protected void gerarCertidao(HttpServletRequest request, HttpServletResponse response, UsuarioNe usuarioSessao, CertidaoGuiaDt certidaoGuiaDt, CertidaoNe certidaoNe) throws Exception {
		
		String numeroGuia;
		if (certidaoGuiaDt == null || certidaoGuiaDt.getTexto() == null || certidaoGuiaDt.getTexto().equalsIgnoreCase("") || certidaoGuiaDt.getTexto().equals("")){
			request.setAttribute("MensagemErro", "Informe o número da guia!\nE busque os processos!");
		}
		else {
			byte[] byTemp = null;
			Signer.acceptSSL();
			byTemp = ConverterHtmlPdf.converteHtmlPDF(certidaoGuiaDt.getTexto().getBytes(), true);
			TJDataHora tjdathora = new TJDataHora();
			tjdathora.adicioneDia(30); // Data de validade = 30 dias.
			CertidaoValidacaoDt cdt = new CertidaoValidacaoDt(byTemp, tjdathora.getDataFormatadaddMMyyyyHHmmss(), new TJDataHora().getDataFormatadaddMMyyyyHHmmss());
			
			// Preechimento dos dados da certidão
			cdt.setId_UsuarioLog(usuarioSessao.getId_Usuario());
			cdt.setIpComputadorLog(usuarioSessao.getUsuarioDt().getIpComputadorLog());
			cdt.setId_Modelo(certidaoGuiaDt.getId_Modelo());
			cdt.setId_Comarca(certidaoGuiaDt.getId_Comarca());
			
			// Persistir certidão
			certidaoNe.salvar(cdt);			
			byTemp = certidaoNe.gerarPdfPublicacao(ProjudiPropriedades.getInstance().getCaminhoAplicacao() , cdt);							
			enviarPDF(response, byTemp, "Certidao");			
			byTemp = null;
		}
	}
	
	protected String levantarMovimentacoes(String idProcesso) throws Exception{
		
		// INSERÇÃO DAS MOVIMENTAÇÕES DO PROCESSO COM A DATA DO OCORRIDO E O ANDAMENTO/TIPO DE MOVIMENTAÇÃO (SEPARADO POR ; E ESPAÇO)								
		List listaMovimentacoes = new ArrayList<MovimentacaoDt>();
		listaMovimentacoes = new ProcessoNe().consultarMovimentacoesProcesso(idProcesso);
		
		String strMovimentacoes = "";
		MovimentacaoDt movimentacaoDt = new MovimentacaoDt();
		
		if(!listaMovimentacoes.isEmpty()){
			for(int i = 0; i < listaMovimentacoes.size(); i++){								
				movimentacaoDt = (MovimentacaoDt) listaMovimentacoes.get(i);
				if(!movimentacaoDt.getComplemento().isEmpty()){
					strMovimentacoes += "Em " + movimentacaoDt.getDataRealizacao() + ", " + movimentacaoDt.getMovimentacaoTipo() + " - " + movimentacaoDt.getComplemento();					
				} else {
					strMovimentacoes += "Em " + movimentacaoDt.getDataRealizacao() + ", " + movimentacaoDt.getMovimentacaoTipo();
				}
				if(i == listaMovimentacoes.size() - 1){
					strMovimentacoes += ".";
				} else {
					strMovimentacoes += ";  ";
				}
			}
		}						
		return strMovimentacoes;
	}
}
