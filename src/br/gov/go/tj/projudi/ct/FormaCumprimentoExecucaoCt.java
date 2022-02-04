package br.gov.go.tj.projudi.ct;


import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.FormaCumprimentoExecucaoDt;
import br.gov.go.tj.projudi.ne.FormaCumprimentoExecucaoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class FormaCumprimentoExecucaoCt extends Controle {

    private static final long serialVersionUID = 6287221117260790495L;

    public  FormaCumprimentoExecucaoCt() {

	} 
		public int Permissao() {
			return FormaCumprimentoExecucaoDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		FormaCumprimentoExecucaoDt FormaCumprimentoExecucaodt;
		FormaCumprimentoExecucaoNe FormaCumprimentoExecucaone;

		List tempList=null; 
		String Mensagem="";
		String stId="";

		String stNomeBusca1 = "";
//		String stNomeBusca2 = "";

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
//		if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");
		
		String stAcao="/WEB-INF/jsptjgo/FormaCumprimentoExecucao.jsp";

		request.setAttribute("tempPrograma","FormaCumprimentoExecucao");

		FormaCumprimentoExecucaone = (FormaCumprimentoExecucaoNe)request.getSession().getAttribute("FormaCumprimentoExecucaone");
		if (FormaCumprimentoExecucaone == null )  FormaCumprimentoExecucaone = new FormaCumprimentoExecucaoNe();  

		FormaCumprimentoExecucaodt =(FormaCumprimentoExecucaoDt)request.getSession().getAttribute("FormaCumprimentoExecucaodt");
		if (FormaCumprimentoExecucaodt == null )  FormaCumprimentoExecucaodt = new FormaCumprimentoExecucaoDt();  

		FormaCumprimentoExecucaodt.setFormaCumprimentoExecucao( request.getParameter("FormaCumprimentoExecucao")); 
		FormaCumprimentoExecucaodt.setFormaCumprimentoExecucaoCodigo( request.getParameter("FormaCumprimentoExecucaoCodigo")); 

		FormaCumprimentoExecucaodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		FormaCumprimentoExecucaodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual",Configuracao.Editar);

		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					FormaCumprimentoExecucaone.excluir(FormaCumprimentoExecucaodt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
//			case Configuracao.Localizar: //localizar
//				stAcao="/WEB-INF/jsptjgo/FormaCumprimentoExecucaoLocalizar.jsp";
//				request.setAttribute("tempBuscaId_FormaCumprimentoExecucao","Id_FormaCumprimentoExecucao");
//				request.setAttribute("tempBuscaFormaCumprimentoExecucao","FormaCumprimentoExecucao");
//				request.setAttribute("tempRetorno","FormaCumprimentoExecucao");
//				tempList =FormaCumprimentoExecucaone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
//				if (tempList.size()>0){
//					request.setAttribute("ListaFormaCumprimentoExecucao", tempList); 
//					request.setAttribute("PaginaAtual", Configuracao.Localizar);
//					request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
//					request.setAttribute("QuantidadePaginas", FormaCumprimentoExecucaone.getQuantidadePaginas());
//					FormaCumprimentoExecucaodt.limpar();
//				}else{
//					request.setAttribute("MensagemErro", "Dados Não Localizados"); 
//					stAcao="/WEB-INF/jsptjgo/FormaCumprimentoExecucao.jsp";
//				}
//				break;
			case Configuracao.Localizar:
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Forma de Cumprimento da Pena"};
					String[] lisDescricao = {"Forma de Cumprimento da Pena", "Código"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_FormaCumprimentoExecucao");
					request.setAttribute("tempBuscaDescricao", "FormaCumprimentoExecucao");
					request.setAttribute("tempBuscaPrograma", "FormaCumprimentoExecucao");
					request.setAttribute("tempRetorno", "FormaCumprimentoExecucao");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
					FormaCumprimentoExecucaodt.limpar();
				}else{
					String stTemp = "";
					stTemp = FormaCumprimentoExecucaone.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;
				}
				break;
				
			case Configuracao.Novo: 
				FormaCumprimentoExecucaodt.limpar();
				break;
				
			case Configuracao.SalvarResultado: 
					Mensagem=FormaCumprimentoExecucaone.Verificar(FormaCumprimentoExecucaodt); 
					if (Mensagem.length()==0){
						FormaCumprimentoExecucaone.salvar(FormaCumprimentoExecucaodt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;

			default:
				stId = request.getParameter("Id_FormaCumprimentoExecucao");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( FormaCumprimentoExecucaodt.getId()))){
						FormaCumprimentoExecucaodt.limpar();
						FormaCumprimentoExecucaodt = FormaCumprimentoExecucaone.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("FormaCumprimentoExecucaodt",FormaCumprimentoExecucaodt );
		request.getSession().setAttribute("FormaCumprimentoExecucaone",FormaCumprimentoExecucaone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
