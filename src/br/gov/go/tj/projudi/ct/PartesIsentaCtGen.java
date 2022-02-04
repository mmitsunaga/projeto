package br.gov.go.tj.projudi.ct;

 import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.PartesIsentaDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaDt;
import br.gov.go.tj.projudi.ne.PartesIsentaNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class PartesIsentaCtGen extends Controle { 


	/**
	 * 
	 */
	private static final long serialVersionUID = -3437854812936966841L;

	public  PartesIsentaCtGen() { 

	} 
		public int Permissao(){
			return PartesIsentaDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe usuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		PartesIsentaDt PartesIsentadt;
		PartesIsentaNe PartesIsentane;


		String stNomeBusca1="";
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/PartesIsenta.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","PartesIsenta");




		PartesIsentane =(PartesIsentaNe)request.getSession().getAttribute("PartesIsentane");
		if (PartesIsentane == null )  PartesIsentane = new PartesIsentaNe();  


		PartesIsentadt =(PartesIsentaDt)request.getSession().getAttribute("PartesIsentadt");
		if (PartesIsentadt == null )  PartesIsentadt = new PartesIsentaDt();  

		PartesIsentadt.setNome( request.getParameter("Nome")); 
		PartesIsentadt.setCpf( request.getParameter("Cpf")); 
		PartesIsentadt.setCnpj( request.getParameter("Cnpj")); 
		PartesIsentadt.setId_UsuarioCadastrador( request.getParameter("Id_UsuarioCadastrador")); 
		PartesIsentadt.setNomeUsuarioCadastrador( request.getParameter("NomeUsuarioCadastrador")); 
		PartesIsentadt.setId_ServentiaUsuarioCadastrador( request.getParameter("Id_ServentiaUsuarioCadastrador")); 
		PartesIsentadt.setServentiaUsuarioCadastrador( request.getParameter("ServentiaUsuarioCadastrador")); 
		PartesIsentadt.setDataCadastro( request.getParameter("DataCadastro")); 
		PartesIsentadt.setId_UsuarioBaixa( request.getParameter("Id_UsuarioBaixa")); 
		PartesIsentadt.setNomeUsuarioBaixa( request.getParameter("NomeUsuarioBaixa")); 
		PartesIsentadt.setId_ServentiaUsuarioBaixa( request.getParameter("Id_ServentiaUsuarioBaixa")); 
		PartesIsentadt.setServentiaUsuarioBaixa( request.getParameter("ServentiaUsuarioBaixa")); 
		PartesIsentadt.setDataBaixa( request.getParameter("DataBaixa")); 

		PartesIsentadt.setId_UsuarioLog(usuarioSessao.getId_Usuario());
		PartesIsentadt.setIpComputadorLog(request.getRemoteAddr());


		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					PartesIsentane.excluir(PartesIsentadt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"PartesIsenta"};
					String[] lisDescricao = {"PartesIsenta"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_PartesIsentas");
					request.setAttribute("tempBuscaDescricao","PartesIsenta");
					request.setAttribute("tempBuscaPrograma","PartesIsenta");
					request.setAttribute("tempRetorno","PartesIsenta");
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = PartesIsentane.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
					try {
						response.setContentType("text/x-json");
						response.getOutputStream().write(stTemp.getBytes());
						response.flushBuffer();
					}catch(Exception e) { }
					return;
				}
				break;
			case Configuracao.Novo: 
				PartesIsentadt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=PartesIsentane.Verificar(PartesIsentadt); 
					if (Mensagem.length()==0){
						PartesIsentane.salvar(PartesIsentadt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;

				case (ServentiaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Serventia"};
					String[] lisDescricao = {"Serventia"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_ServentiaUsuarioCadastrador");
					request.setAttribute("tempBuscaDescricao","ServentiaUsuarioCadastrador");
					request.setAttribute("tempBuscaPrograma","Serventia");
					request.setAttribute("tempRetorno","PartesIsenta");
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (ServentiaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = PartesIsentane.consultarDescricaoServentiaJSON(stNomeBusca1, PosicaoPaginaAtual);
					try {
						response.setContentType("text/x-json");
						response.getOutputStream().write(stTemp.getBytes());
						response.flushBuffer();
					}catch(Exception e) { }
					return;
				}
					break;
				case (UsuarioServentiaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"UsuarioServentia"};
					String[] lisDescricao = {"UsuarioServentia"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_UsuarioBaixa");
					request.setAttribute("tempBuscaDescricao","NomeUsuarioBaixa");
					request.setAttribute("tempBuscaPrograma","UsuarioServentia");
					request.setAttribute("tempRetorno","PartesIsenta");
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (UsuarioServentiaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = PartesIsentane.consultarDescricaoUsuarioServentiaJSON(stNomeBusca1, PosicaoPaginaAtual);
					try {
						response.setContentType("text/x-json");
						response.getOutputStream().write(stTemp.getBytes());
						response.flushBuffer();
					}catch(Exception e) { }
					return;
				}
				break;

//--------------------------------------------------------------------------------//
			default:
				stId = request.getParameter("Id_PartesIsentas");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( PartesIsentadt.getId()))){
						PartesIsentadt.limpar();
						PartesIsentadt = PartesIsentane.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("PartesIsentadt",PartesIsentadt );
		request.getSession().setAttribute("PartesIsentane",PartesIsentane );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
