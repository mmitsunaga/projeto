package br.gov.go.tj.projudi.ct;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.PendenciaArquivoDt;
import br.gov.go.tj.projudi.dt.PendenciaDt;
import br.gov.go.tj.projudi.ne.PendenciaArquivoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.MensagemException;

//---------------------------------------------------------
public class PendenciaArquivoCt extends PendenciaArquivoCtGen {

	/**
     * 
     */
    private static final long serialVersionUID = -5019173031130250363L;

    public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws MensagemException, Exception, ServletException, IOException {

		PendenciaArquivoDt pendenciaArquivodt;
		PendenciaArquivoNe pendenciaArquivone;

		String Mensagem = "";
		String stId = "";

		request.setAttribute("tempPrograma", "PendenciaArquivo");
		request.setAttribute("tempBuscaId_PendenciaArquivo", "Id_PendenciaArquivo");
		request.setAttribute("tempBuscaPendenciaArquivo", "PendenciaArquivo");
		request.setAttribute("tempBuscaId_ArquivoTipo", request.getParameter("tempBuscaId_ArquivoTipo"));
		request.setAttribute("tempBuscaArquivoTipo", request.getParameter("tempBuscaArquivoTipo"));
		request.setAttribute("tempBuscaId_Pendencia", request.getParameter("tempBuscaId_Pendencia"));
		request.setAttribute("tempBuscaPendencia", request.getParameter("tempBuscaPendencia"));

		pendenciaArquivone = (PendenciaArquivoNe) request.getSession().getAttribute("PendenciaArquivone");
		if (pendenciaArquivone == null) pendenciaArquivone = new PendenciaArquivoNe();

		pendenciaArquivodt = (PendenciaArquivoDt) request.getSession().getAttribute("PendenciaArquivodt");
		if (pendenciaArquivodt == null) pendenciaArquivodt = new PendenciaArquivoDt();

		pendenciaArquivodt.setNomeArquivo(request.getParameter("NomeArquivo"));
		//Reescrever
		/*PendenciaArquivodt.setId_ArquivoTipo(request.getParameter("Id_ArquivoTipo"));
		PendenciaArquivodt.setArquivoTipo(request.getParameter("ArquivoTipo"));
		PendenciaArquivodt.setId_Pendencia(request.getParameter("Id_Pendencia"));
		PendenciaArquivodt.setDataInicio(request.getParameter("DataInicio"));
		PendenciaArquivodt.setContentType(request.getParameter("ContentType"));
		PendenciaArquivodt.setArquivo(request.getParameter("Arquivo"));
		PendenciaArquivodt.setDataInsercao(request.getParameter("DataInsercao"));
		PendenciaArquivodt.setUsuarioAssinador(request.getParameter("UsuarioAssinador"));
		if (request.getParameter("Resposta") != null) PendenciaArquivodt.setResposta(request.getParameter("Resposta"));
		else PendenciaArquivodt.setResposta("false");
		PendenciaArquivodt.setArquivoTipoCodigo(request.getParameter("ArquivoTipoCodigo"));*/
		pendenciaArquivodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		pendenciaArquivodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());

		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//--------------------------------------------------------------------------------//
		switch (paginaatual) {
		case Configuracao.Curinga6:			
			pendenciaArquivodt.setId(request.getParameter("Id_PendenciaArquivo"));
			
			if (UsuarioSessao.VerificarCodigoHash(pendenciaArquivodt.getId(), request.getParameter("hash"))){
				boolean recibo = false;
				boolean pendenciaFinalizada = false;
				
				if (request.getParameter("recibo") != null && request.getParameter("recibo").equals("true")) {
					recibo = true;
				}
				
				if (request.getParameter("finalizado") != null && request.getParameter("finalizado").equals("true") || pendenciaArquivone.isPendenciaArquivoMovido(pendenciaArquivodt.getId())){ 
					pendenciaFinalizada = true;
				}
				
				if (request.getParameter("CodigoVerificacao") != null && request.getParameter("CodigoVerificacao").equals("true")){
					byte[] arquivoPDF = pendenciaArquivone.gerarPdfPublicacao(ProjudiPropriedades.getInstance().getCaminhoAplicacao() , pendenciaArquivodt.getId(),  UsuarioSessao.getUsuarioDt(), new LogDt(UsuarioSessao.getId_Usuario(), UsuarioSessao.getIpComputadorLog()));
					enviarPDF(response, arquivoPDF, "relatorio");
					return;
				} else {				
					pendenciaArquivone.baixarArquivo(pendenciaArquivodt, response,  pendenciaFinalizada);
				}
				
			} else {
				super.exibaMensagemInconsistenciaErro(request, "Acesso negado.");
			}
			break;
		case Configuracao.Curinga7:
			pendenciaArquivodt.setId(request.getParameter("Id_PendenciaArquivo"));
			pendenciaArquivone.baixarPublicacaoPublica(pendenciaArquivodt, response);
			break;
		case Configuracao.Curinga8:
			stId = request.getParameter("Id_PendenciaArquivo");
			String novoStatus = request.getParameter("Status");
			PendenciaDt pendenciaDt = null;
			if (stId != null && stId.length() > 0 && novoStatus != null) {
				pendenciaArquivodt = pendenciaArquivone.consultarId(stId);
				pendenciaArquivodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
				pendenciaArquivodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
				pendenciaArquivone.alterarStatusPendenciaArquivo(pendenciaArquivodt, novoStatus);

				Mensagem = "Arquivo " + pendenciaArquivodt.getNomeArquivo() + " teve o Status modificado com sucesso.";
				//Recupera o processo da sessão e redireciona para tela com mensagem de sucesso
				pendenciaDt = (PendenciaDt) request.getSession().getAttribute("Pendenciadt");
				redireciona(response, "Pendencia?Id_Pendencia=" + pendenciaDt.getId() + "&MensagemOk=" + Mensagem);
				return;
			} else 
				Mensagem = "Não foi possível Validar/Invalidar Arquivo. Selecione novamente.";
			
			redireciona(response, "Pendencia?MensagemErro=" + Mensagem);
			return;
		default:
			super.executar(request, response, UsuarioSessao, paginaatual, tempNomeBusca, PosicaoPaginaAtual);
			return;
		}
	}

}
