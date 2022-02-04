package br.gov.go.tj.projudi.ct;

 import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.ServentiaRelacionadaDt;
import br.gov.go.tj.projudi.ne.ServentiaRelacionadaNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class ServentiaRelacionadaCtGen extends Controle {


	/**
     * 
     */
    private static final long serialVersionUID = -5904921834571991920L;

    public  ServentiaRelacionadaCtGen() {

	} 
		public int Permissao(){
			return ServentiaRelacionadaDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ServentiaRelacionadaDt ServentiaRelacionadadt;
		ServentiaRelacionadaNe ServentiaRelacionadane;


		List tempList=null; 
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/ServentiaRelacionada.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","ServentiaRelacionada");
		request.setAttribute("tempBuscaId_ServentiaRelacionada","Id_ServentiaRelacionada");
		request.setAttribute("tempBuscaServentiaRelacionada","ServentiaRelacionada");
		
		request.setAttribute("tempBuscaId_ServentiaPrincipal","Id_ServentiaPrincipal");
		request.setAttribute("tempBuscaServentia","Serventia");
		request.setAttribute("tempBuscaId_ServentiaRelacao","Id_ServentiaRelacao");
		request.setAttribute("tempBuscaServentia","Serventia");

		request.setAttribute("tempRetorno","ServentiaRelacionada");



		ServentiaRelacionadane =(ServentiaRelacionadaNe)request.getSession().getAttribute("ServentiaRelacionadane");
		if (ServentiaRelacionadane == null )  ServentiaRelacionadane = new ServentiaRelacionadaNe();  


		ServentiaRelacionadadt =(ServentiaRelacionadaDt)request.getSession().getAttribute("ServentiaRelacionadadt");
		if (ServentiaRelacionadadt == null )  ServentiaRelacionadadt = new ServentiaRelacionadaDt();  

		ServentiaRelacionadadt.setServentiaRelacionada( request.getParameter("ServentiaRelacionada")); 
		ServentiaRelacionadadt.setId_ServentiaPrincipal( request.getParameter("Id_ServentiaPrincipal")); 
		ServentiaRelacionadadt.setServentiaPrincipal( request.getParameter("ServentiaPrincipal")); 
		ServentiaRelacionadadt.setId_ServentiaRelacao( request.getParameter("Id_ServentiaRelacao")); 
		ServentiaRelacionadadt.setServentiaRelacao( request.getParameter("ServentiaRelacao")); 

		ServentiaRelacionadadt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		ServentiaRelacionadadt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				ServentiaRelacionadane.excluir(ServentiaRelacionadadt); 
				request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				stAcao="/WEB-INF/jsptjgo/ServentiaRelacionadaLocalizar.jsp";
				tempList =ServentiaRelacionadane.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
				if (tempList.size()>0){
					request.setAttribute("ListaServentiaRelacionada", tempList); 
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
					request.setAttribute("QuantidadePaginas", ServentiaRelacionadane.getQuantidadePaginas());
					ServentiaRelacionadadt.limpar();
				}else{ 
					request.setAttribute("MensagemErro", "Dados Não Localizados"); 
					request.setAttribute("PaginaAtual", Configuracao.Editar);
				}
				break;
			case Configuracao.Novo: 
				ServentiaRelacionadadt.limpar();
				request.setAttribute("PaginaAtual",Configuracao.Editar);
				break;
			case Configuracao.SalvarResultado: 
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				Mensagem=ServentiaRelacionadane.Verificar(ServentiaRelacionadadt); 
				if (Mensagem.length()==0){
					ServentiaRelacionadane.salvar(ServentiaRelacionadadt); 
					request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
				}else	request.setAttribute("MensagemErro", Mensagem );
				break;

				case (ServentiaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					stAcao="/WEB-INF/jsptjgo/ServentiaLocalizar.jsp";
						tempList =ServentiaRelacionadane.consultarDescricaoServentia(tempNomeBusca, PosicaoPaginaAtual);
						if (tempList.size()>0){
							request.setAttribute("ListaServentia", tempList); 
							request.setAttribute("PaginaAtual", paginaatual);
							request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
							request.setAttribute("QuantidadePaginas", ServentiaRelacionadane.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
//--------------------------------------------------------------------------------//
			default:
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				stId = request.getParameter("Id_ServentiaRelacionada");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( ServentiaRelacionadadt.getId()))){
						ServentiaRelacionadadt.limpar();
						ServentiaRelacionadadt = ServentiaRelacionadane.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("ServentiaRelacionadadt",ServentiaRelacionadadt );
		request.getSession().setAttribute("ServentiaRelacionadane",ServentiaRelacionadane );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
