package br.gov.go.tj.projudi.ct;

 import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.CertidaoTipoProcessoTipoDt;
import br.gov.go.tj.projudi.ne.CertidaoTipoProcessoTipoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class CertidaoTipoProcessoTipoCtGen extends Controle {


	/**
     * 
     */
    private static final long serialVersionUID = 4130193739864308304L;

    public  CertidaoTipoProcessoTipoCtGen() {

	} 
		public int Permissao(){
			return CertidaoTipoProcessoTipoDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		CertidaoTipoProcessoTipoDt CertidaoTipoProcessoTipodt;
		CertidaoTipoProcessoTipoNe CertidaoTipoProcessoTipone;


		String Mensagem="";
		String stAcao="/WEB-INF/jsptjgo/CertidaoTipoProcessoTipo.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","CertidaoTipoProcessoTipo");
		request.setAttribute("ListaUlLiCertidaoTipoProcessoTipo","");




		CertidaoTipoProcessoTipone =(CertidaoTipoProcessoTipoNe)request.getSession().getAttribute("CertidaoTipoProcessoTipone");
		if (CertidaoTipoProcessoTipone == null )  CertidaoTipoProcessoTipone = new CertidaoTipoProcessoTipoNe();  


		CertidaoTipoProcessoTipodt =(CertidaoTipoProcessoTipoDt)request.getSession().getAttribute("CertidaoTipoProcessoTipodt");
		if (CertidaoTipoProcessoTipodt == null )  CertidaoTipoProcessoTipodt = new CertidaoTipoProcessoTipoDt();  

		CertidaoTipoProcessoTipodt.setId_CertidaoTipo( request.getParameter("Id_CertidaoTipo")); 
		CertidaoTipoProcessoTipodt.setCertidaoTipo( request.getParameter("CertidaoTipo")); 
		CertidaoTipoProcessoTipodt.setId_ProcessoTipo( request.getParameter("Id_ProcessoTipo")); 
		CertidaoTipoProcessoTipodt.setProcessoTipo( request.getParameter("ProcessoTipo")); 

		CertidaoTipoProcessoTipodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		CertidaoTipoProcessoTipodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.Localizar: //localizar
				localizar(request,CertidaoTipoProcessoTipodt.getId_CertidaoTipo() ,CertidaoTipoProcessoTipone, UsuarioSessao);
				break;
			case Configuracao.Novo: 
				CertidaoTipoProcessoTipodt.limpar();
				request.setAttribute("ListaUlLiCertidaoTipoProcessoTipo", ""); 
				break;
			case Configuracao.SalvarResultado: 
				String[] idsDados = request.getParameterValues("chkEditar");
				Mensagem=CertidaoTipoProcessoTipone.Verificar(CertidaoTipoProcessoTipodt); 
				if (Mensagem.length()==0){
					CertidaoTipoProcessoTipone.salvarMultiplo(CertidaoTipoProcessoTipodt, idsDados); 
					localizar(request,CertidaoTipoProcessoTipodt.getId_CertidaoTipo() ,CertidaoTipoProcessoTipone, UsuarioSessao);
					request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
				}else	request.setAttribute("MensagemErro", Mensagem );
				break;
//--------------------------------------------------------------------------------//
			default:
				break;
		}

		request.getSession().setAttribute("CertidaoTipoProcessoTipodt",CertidaoTipoProcessoTipodt );
		request.getSession().setAttribute("CertidaoTipoProcessoTipone",CertidaoTipoProcessoTipone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

	private void localizar(HttpServletRequest request, String id, CertidaoTipoProcessoTipoNe objNe, UsuarioNe UsuarioSessao) throws Exception{
				String tempDados =objNe.consultarProcessoTipoCertidaoTipoUlLiCheckBox( id);
				if (tempDados.length()>0){
					request.setAttribute("ListaUlLiCertidaoTipoProcessoTipo", tempDados); 
					//é gerado o código do pedido, assim o submit so pode ser executado uma unica vez
					request.setAttribute("__Pedido__", UsuarioSessao.getPedido());
				}else{ 
					request.setAttribute("MensagemErro", "Dados Não Localizados"); 
					request.setAttribute("PaginaAtual", Configuracao.Editar);
				}
	}
}
