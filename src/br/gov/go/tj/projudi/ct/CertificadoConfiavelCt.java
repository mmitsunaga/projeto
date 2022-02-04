package br.gov.go.tj.projudi.ct;

 import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.CertificadoDt;
import br.gov.go.tj.projudi.ne.CertificadoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class CertificadoConfiavelCt extends Controle {


	/**
     * 
     */
    private static final long serialVersionUID = 6260594630114145486L;

    public  CertificadoConfiavelCt() {

	} 
		public int Permissao(){
			return CertificadoDt.CodigoPermissaoCertificadoConfiavel;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		CertificadoDt certificadoDt;
		CertificadoNe certificadoNe;


		List tempList=null; 
		String Mensagem="";
		String certRaiz = "0";

		String stAcao="/WEB-INF/jsptjgo/CertificadoConfiavel.jsp";
		request.setAttribute("tempPrograma","CertificadoConfiavel");

		certificadoNe =(CertificadoNe)request.getSession().getAttribute("certificadoNe");
		if (certificadoNe == null )  certificadoNe = new CertificadoNe();  


		certificadoDt =(CertificadoDt)request.getSession().getAttribute("certificadoDt");
		if (certificadoDt == null )  certificadoDt = new CertificadoDt();  

		if (certificadoDt.getConteudo() == null || certificadoDt.getConteudo().length == 0)
			certificadoDt.setConteudo(getArquivoBytes(request)); 
		
		if (request.getAttribute("certRaiz") != null)
			certRaiz = (String )request.getAttribute("certRaiz");
		
		certificadoDt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		certificadoDt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
		
		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
		
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: 
				stAcao="/WEB-INF/jsptjgo/CertificadoConfiavelLocalizar.jsp";
				tempList =certificadoNe.consultaDescricaoCertificadosConfiaveis(tempNomeBusca);
				if (tempList.size()>0){
					request.setAttribute("ListaCaConfiaveis", tempList); 
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					certificadoDt.limpar();
				}else{
					request.setAttribute("MensagemErro", "Dados Não Localizados"); 
				}
				break;
			case Configuracao.Novo:
				certRaiz = "0";
				certificadoDt.limpar();
				break;
			case Configuracao.Salvar:
				if (certificadoDt.getConteudo() != null && certificadoDt.getConteudo().length > 0){
					Mensagem = certificadoNe.VerificarCAConfiavel(certificadoDt, certRaiz);
					if (Mensagem.equals("")){
						request.setAttribute("PaginaAtual", Configuracao.SalvarResultado);
						request.setAttribute("Mensagem", "Para confirmar a operação salvar certificado \""+certificadoDt.getDescricao()+"\" clique no botão \"Confirmar\".");
					} else {
						certificadoDt.limpar();
						request.setAttribute("MensagemErro", Mensagem);
					}
					
				}else
					request.setAttribute("MensagemErro", "Selecione um Certificado");
				break;
			case Configuracao.SalvarResultado: 

				if (certificadoDt.getConteudo() != null && certificadoDt.getConteudo().length > 0){
					certificadoNe.salvarCaConfiavel(certificadoDt); 
					certificadoDt.limpar();
					certRaiz = "0";
					request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
				}else
					request.setAttribute("MensagemErro", "Selecione um Certificado");
				break;
//--------------------------------------------------------------------------------//
			default:
			
				break;
		}
		
		request.setAttribute("certRaiz", certRaiz );
		request.getSession().setAttribute("certificadoDt", certificadoDt );
		request.getSession().setAttribute("certificadoNe", certificadoNe );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
