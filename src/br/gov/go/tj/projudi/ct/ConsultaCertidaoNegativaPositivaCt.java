package br.gov.go.tj.projudi.ct;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.AreaDt;
import br.gov.go.tj.projudi.dt.CertidaoNegativaPositivaDt;
import br.gov.go.tj.projudi.ne.CertidaoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class ConsultaCertidaoNegativaPositivaCt extends Controle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8780793487744036188L;

	public int Permissao() {
		return  CertidaoNegativaPositivaDt.CodigoPermissao;
	}

	@Override
	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String nomebusca, String posicaopaginaatual) throws Exception, ServletException, IOException {		
										
		CertidaoNe certidaoNe = (CertidaoNe) request.getSession().getAttribute("certidaoNe");
		if (certidaoNe == null) certidaoNe = new CertidaoNe();
		
		CertidaoNegativaPositivaDt certidaoNegativaPositivaDt = (CertidaoNegativaPositivaDt) request.getSession().getAttribute("certidaoNegativaPositivaDt");
		if (certidaoNegativaPositivaDt == null) certidaoNegativaPositivaDt = new CertidaoNegativaPositivaDt();	
			
		super.limpeMensagens(request);	
		
		request.setAttribute("TituloPagina", "Certidão Negativa/Positiva");
		request.setAttribute("tempPrograma", "ConsultaCertidaoNegativaPositiva");
		request.setAttribute("tempNomeBusca", "ConsultaCertidaoNegativaPositiva");
		request.setAttribute("tempRetorno", "ConsultaCertidaoNegativaPositiva");
		request.setAttribute("PaginaAnterior", paginaatual);	
		
		certidaoNegativaPositivaDt.setId_Comarca(request.getParameter("Id_Comarca"));
		certidaoNegativaPositivaDt.setComarca(request.getParameter("Comarca"));
		certidaoNegativaPositivaDt.setNome(request.getParameter("Nome"));
		certidaoNegativaPositivaDt.setCpfCnpj(request.getParameter("Cpf"));
		certidaoNegativaPositivaDt.setNomeMae(request.getParameter("NomeMae"));
		certidaoNegativaPositivaDt.setDataNascimento(request.getParameter("DataNascimento"));
		certidaoNegativaPositivaDt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		certidaoNegativaPositivaDt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
		
		switch (paginaatual) {
		
			case Configuracao.Novo:		
				request.setAttribute("PaginaAtual", Configuracao.Editar);	
				certidaoNegativaPositivaDt.limpar();	
				certidaoNegativaPositivaDt.setAreaCodigo(String.valueOf(AreaDt.CIVEL));
				break;
			
			case Configuracao.Localizar:
				String mensagem = certidaoNe.VerificarCertidaoNegativaPositivaPublica(certidaoNegativaPositivaDt); 
				if (mensagem != null && mensagem.trim().length() > 0) {
					super.exibaMensagemInconsistenciaErro(request, mensagem);					
				} else {
					certidaoNegativaPositivaDt.limparParcial();	
					certidaoNegativaPositivaDt.setAreaCodigo(String.valueOf(AreaDt.CIVEL));
					certidaoNegativaPositivaDt.setListaProcessos(certidaoNe.listarProcessoCertidaoNP(certidaoNegativaPositivaDt));
					
					certidaoNegativaPositivaDt = certidaoNe.getListaProcessoSPGPublica(certidaoNegativaPositivaDt);
					certidaoNegativaPositivaDt.getValorCertidao();	
					
					if (certidaoNegativaPositivaDt.getListaProcessos() == null || certidaoNegativaPositivaDt.getListaProcessos().size() == 0)					
						super.exibaMensagemSucesso(request, "Não foram encontrados processos com os parâmetros informados.");					
				}			
				break;	
		}
		
		request.getSession().setAttribute("certidaoNegativaPositivaDt", certidaoNegativaPositivaDt);
		request.getSession().setAttribute("certidaoNe", certidaoNe);			
		
		RequestDispatcher dis = request.getRequestDispatcher("/WEB-INF/jsptjgo/ConsultaCertidaoNegativaPositiva.jsp");
		dis.include(request, response);
	}

}
