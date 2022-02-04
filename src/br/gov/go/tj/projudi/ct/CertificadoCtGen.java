package br.gov.go.tj.projudi.ct;

// import br.gov.go.tj.projudi.dt.UsuarioCertificadoDt;
// import br.gov.go.tj.projudi.dt.UsuarioLiberadorDt;
// import br.gov.go.tj.projudi.dt.UsuarioRevogadorDt;

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
import br.gov.go.tj.utils.Funcoes;

public class CertificadoCtGen extends Controle {


	/**
     * 
     */
    private static final long serialVersionUID = 4957142687400297251L;

    public  CertificadoCtGen() {

	} 
		public int Permissao(){
			return CertificadoDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		CertificadoDt Certificadodt;
		CertificadoNe Certificadone;


		List tempList=null; 
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/Certificado.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","Certificado");




		Certificadone =(CertificadoNe)request.getSession().getAttribute("Certificadone");
		if (Certificadone == null )  Certificadone = new CertificadoNe();  


		Certificadodt =(CertificadoDt)request.getSession().getAttribute("Certificadodt");
		if (Certificadodt == null )  Certificadodt = new CertificadoDt();  

		if (request.getParameter("Raiz") != null)
			Certificadodt.setRaiz( request.getParameter("Raiz")); 
		else Certificadodt.setRaiz("false");
		if (request.getParameter("Emissor") != null)
			Certificadodt.setEmissor( request.getParameter("Emissor")); 
		else Certificadodt.setEmissor("false");
		if (request.getParameter("Liberado") != null)
			Certificadodt.setLiberado( request.getParameter("Liberado")); 
		else Certificadodt.setLiberado("false");
		Certificadodt.setDataEmissao( request.getParameter("DataEmissao")); 
		Certificadodt.setDataExpiracao( request.getParameter("DataExpiracao")); 
		Certificadodt.setDataRevogacao( request.getParameter("DataRevogacao")); 
		Certificadodt.setMotivoRevogacao( request.getParameter("MotivoRevogacao")); 
		Certificadodt.setId_UsuarioCertificado( request.getParameter("Id_UsuarioCertificado")); 
		Certificadodt.setUsuarioCertificado( request.getParameter("UsuarioCertificado")); 
		Certificadodt.setId_UsuarioLiberador( request.getParameter("Id_UsuarioLiberador")); 
		Certificadodt.setUsuarioLiberador( request.getParameter("UsuarioLiberador")); 
		Certificadodt.setId_UsuarioRevogador( request.getParameter("Id_UsuarioRevogador")); 
		Certificadodt.setUsuarioRevogador( request.getParameter("UsuarioRevogador")); 
		Certificadodt.setCertificado( request.getParameter("Certificado")); 

		Certificadodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		Certificadodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					Certificadone.excluir(Certificadodt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				stAcao="/WEB-INF/jsptjgo/CertificadoLocalizar.jsp";
				request.setAttribute("tempBuscaId_Certificado","Id_Certificado");
				request.setAttribute("tempBuscaCertificado","Certificado");
				request.setAttribute("tempRetorno","Certificado");
				tempList =Certificadone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
				if (tempList.size()>0){
					request.setAttribute("ListaCertificado", tempList); 
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
					request.setAttribute("QuantidadePaginas", Certificadone.getQuantidadePaginas());
					Certificadodt.limpar();
				}else{
					request.setAttribute("MensagemErro", "Dados Não Localizados"); 
				}
				break;
			case Configuracao.Novo: 
				Certificadodt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=Certificadone.Verificar(Certificadodt); 
					if (Mensagem.length()==0){
						Certificadone.salvar(Certificadodt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;
//				case (UsuarioCertificadoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
//					request.setAttribute("tempBuscaId_UsuarioCertificado","Id_UsuarioCertificado");
//					request.setAttribute("tempBuscaUsuarioCertificado","UsuarioCertificado");
//					request.setAttribute("tempRetorno","Certificado");
//					stAcao="/WEB-INF/jsptjgo/UsuarioCertificadoLocalizar.jsp";
//					tempList =Certificadone.consultarDescricaoUsuarioCertificado(tempNomeBusca, PosicaoPaginaAtual);
//					if (tempList.size()>0){
//						request.setAttribute("ListaUsuarioCertificado", tempList); 
//						request.setAttribute("PaginaAtual", paginaatual);
//						request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
//						request.setAttribute("QuantidadePaginas", Certificadone.getQuantidadePaginas());
//					}else{ 
//						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
//						request.setAttribute("PaginaAtual", Configuracao.Editar);
//					}
//					break;
//				case (UsuarioLiberadorDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
//					request.setAttribute("tempBuscaId_UsuarioLiberador","Id_UsuarioLiberador");
//					request.setAttribute("tempBuscaUsuarioLiberador","UsuarioLiberador");
//					request.setAttribute("tempRetorno","Certificado");
//					stAcao="/WEB-INF/jsptjgo/UsuarioLiberadorLocalizar.jsp";
//					tempList =Certificadone.consultarDescricaoUsuarioLiberador(tempNomeBusca, PosicaoPaginaAtual);
//					if (tempList.size()>0){
//						request.setAttribute("ListaUsuarioLiberador", tempList); 
//						request.setAttribute("PaginaAtual", paginaatual);
//						request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
//						request.setAttribute("QuantidadePaginas", Certificadone.getQuantidadePaginas());
//					}else{ 
//						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
//						request.setAttribute("PaginaAtual", Configuracao.Editar);
//					}
//					break;
//				case (UsuarioRevogadorDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
//					request.setAttribute("tempBuscaId_UsuarioRevogador","Id_UsuarioRevogador");
//					request.setAttribute("tempBuscaUsuarioRevogador","UsuarioRevogador");
//					request.setAttribute("tempRetorno","Certificado");
//					stAcao="/WEB-INF/jsptjgo/UsuarioRevogadorLocalizar.jsp";
//					tempList =Certificadone.consultarDescricaoUsuarioRevogador(tempNomeBusca, PosicaoPaginaAtual);
//					if (tempList.size()>0){
//						request.setAttribute("ListaUsuarioRevogador", tempList); 
//						request.setAttribute("PaginaAtual", paginaatual);
//						request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
//						request.setAttribute("QuantidadePaginas", Certificadone.getQuantidadePaginas());
//					}else{ 
//						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
//						request.setAttribute("PaginaAtual", Configuracao.Editar);
//					}
//					break;
//--------------------------------------------------------------------------------//
			default:
				stId = request.getParameter("Id_Certificado");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( Certificadodt.getId()))){
						Certificadodt.limpar();
						Certificadodt = Certificadone.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("Certificadodt",Certificadodt );
		request.getSession().setAttribute("Certificadone",Certificadone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
