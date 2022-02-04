package br.gov.go.tj.projudi.ct;

 import br.gov.go.tj.projudi.dt.UsuarioDt;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import java.io.IOException;

import br.gov.go.tj.projudi.ne.UsuarioFoneNe;
import br.gov.go.tj.projudi.dt.UsuarioFoneDt;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class UsuarioFoneCtGen extends Controle { 


	/**
	 * 
	 */
	private static final long serialVersionUID = 4422581470614489531L;

	public  UsuarioFoneCtGen() { 

	} 
		public int Permissao(){
			return UsuarioFoneDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		UsuarioFoneDt UsuarioFonedt;
		UsuarioFoneNe UsuarioFonene;


		String stNomeBusca1="";
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/CelularUsuario.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","UsuarioFone");




		UsuarioFonene =(UsuarioFoneNe)request.getSession().getAttribute("UsuarioFonene");
		if (UsuarioFonene == null )  UsuarioFonene = new UsuarioFoneNe();  


		UsuarioFonedt =(UsuarioFoneDt)request.getSession().getAttribute("UsuarioFonedt");
		if (UsuarioFonedt == null )  UsuarioFonedt = new UsuarioFoneDt();  

		UsuarioFonedt.setId_Usuario( request.getParameter("Id_Usuario")); 
		UsuarioFonedt.setUsuario( request.getParameter("Usuario")); 
		UsuarioFonedt.setImei( request.getParameter("Imei")); 
		UsuarioFonedt.setFone( request.getParameter("Fone")); 
		UsuarioFonedt.setCodigo( request.getParameter("Codigo")); 
		UsuarioFonedt.setCodigoValidade( request.getParameter("CodigoValidade")); 
		UsuarioFonedt.setDataPedido( request.getParameter("DataPedido")); 
		UsuarioFonedt.setDataLiberacao( request.getParameter("DataLiberacao")); 

		UsuarioFonedt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		UsuarioFonedt.setIpComputadorLog(request.getRemoteAddr());


		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					UsuarioFonene.excluir(UsuarioFonedt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"UsuarioFone"};
					String[] lisDescricao = {"UsuarioFone"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_UsuFone");
					request.setAttribute("tempBuscaDescricao","UsuarioFone");
					request.setAttribute("tempBuscaPrograma","UsuarioFone");
					request.setAttribute("tempRetorno","UsuarioFone");
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = UsuarioFonene.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
					try {
						response.setContentType("text/x-json");
						response.getOutputStream().write(stTemp.getBytes());
						response.flushBuffer();
					}catch(Exception e) { }
					return;
				}
				break;
			case Configuracao.Novo: 
				UsuarioFonedt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=UsuarioFonene.Verificar(UsuarioFonedt); 
					if (Mensagem.length()==0){
						UsuarioFonene.salvar(UsuarioFonedt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;
				case (UsuarioDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Usuario"};
					String[] lisDescricao = {"Usuario"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_Usuario");
					request.setAttribute("tempBuscaDescricao","Usuario");
					request.setAttribute("tempBuscaPrograma","Usuario");
					request.setAttribute("tempRetorno","UsuarioFone");
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (UsuarioDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = UsuarioFonene.consultarDescricaoUsuarioJSON(stNomeBusca1, PosicaoPaginaAtual);
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
				stId = request.getParameter("Id_UsuFone");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( UsuarioFonedt.getId()))){
						UsuarioFonedt.limpar();
						UsuarioFonedt = UsuarioFonene.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("UsuarioFonedt",UsuarioFonedt );
		request.getSession().setAttribute("UsuarioFonene",UsuarioFonene );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
