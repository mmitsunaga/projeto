package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ArrecadacaoCustaDt;
import br.gov.go.tj.projudi.dt.CustaDt;
import br.gov.go.tj.projudi.ne.ArrecadacaoCustaNe;
import br.gov.go.tj.projudi.ne.CustaNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class CustaCt extends CustaCtGen{

    private static final long serialVersionUID = 5946085415216064268L;

    public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		CustaDt Custadt;
		CustaNe Custane;
		ArrecadacaoCustaNe arrecadacaoCustaNe = new ArrecadacaoCustaNe();

		List tempList=null; 
		String Mensagem="";
		String stId="";
		String stNomeBusca1 = "";

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		String stAcao="/WEB-INF/jsptjgo/Custa.jsp";

		request.setAttribute("tempPrograma","Custa");


		Custane =(CustaNe)request.getSession().getAttribute("Custane");
		if (Custane == null )  Custane = new CustaNe();  

		Custadt =(CustaDt)request.getSession().getAttribute("Custadt");
		if (Custadt == null )  Custadt = new CustaDt();  

		Custadt.setCusta( request.getParameter("Custa")); 
		Custadt.setCustaCodigo( request.getParameter("CustaCodigo")); 
		Custadt.setCodigoRegimento( request.getParameter("CodigoRegimento")); 
		Custadt.setCodigoRegimentoValor( request.getParameter("CodigoRegimentoValor")); 
		Custadt.setPorcentagem( request.getParameter("Porcentagem")); 
		Custadt.setMinimo( request.getParameter("Minimo")); 
		Custadt.setId_ArrecadacaoCusta( request.getParameter("Id_ArrecadacaoCusta")); 
		Custadt.setArrecadacaoCusta( request.getParameter("ArrecadacaoCusta")); 

		Custadt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		Custadt.setIpComputadorLog(request.getRemoteAddr());

		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual",Configuracao.Editar);
			switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					Custane.excluir(Custadt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Descrição"};
					String[] lisDescricao = {"Descrição", "Código Regimento"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_Custa");
					request.setAttribute("tempBuscaDescricao","Custa");
					request.setAttribute("tempBuscaPrograma","Custa");			
					request.setAttribute("tempRetorno","Custa");		
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp="";
					stTemp = Custane.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
						
					enviarJSON(response, stTemp);
											
					return;								
				}
				break;
			case Configuracao.Novo: 
				Custadt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=Custane.Verificar(Custadt); 
					if (Mensagem.length()==0){
						Custane.salvar(Custadt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;
			case (ArrecadacaoCustaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"ArrecadacaoCusta"};
					String[] lisDescricao = {"ArrecadacaoCusta"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_ArrecadacaoCusta");
					request.setAttribute("tempBuscaDescricao", "ArrecadacaoCusta");
					request.setAttribute("tempBuscaPrograma", "ArrecadacaoCusta");
					request.setAttribute("tempRetorno", "ArrecadacaoCusta");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (ArrecadacaoCustaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				}else{
					String stTemp = "";
					stTemp = Custane.consultarArrecadacaoCustaJSON(stNomeBusca1, PosicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;
				}
				break;
			default:
				stId = request.getParameter("Id_Custa");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( Custadt.getId()))){
						Custadt.limpar();
						Custadt = Custane.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("Custadt",Custadt );
		request.getSession().setAttribute("Custane",Custane );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
}
