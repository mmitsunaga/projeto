package br.gov.go.tj.projudi.ct;


import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.EmpresaTipoDt;
import br.gov.go.tj.projudi.ne.EmpresaTipoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class EmpresaTipoCtGen extends Controle {


	/**
     * 
     */
    private static final long serialVersionUID = 4013412231513571857L;

    public  EmpresaTipoCtGen() {

	} 
		public int Permissao(){
			return EmpresaTipoDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		EmpresaTipoDt EmpresaTipodt;
		EmpresaTipoNe EmpresaTipone;


		List tempList=null; 
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/EmpresaTipo.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","EmpresaTipo");




		EmpresaTipone =(EmpresaTipoNe)request.getSession().getAttribute("EmpresaTipone");
		if (EmpresaTipone == null )  EmpresaTipone = new EmpresaTipoNe();  


		EmpresaTipodt =(EmpresaTipoDt)request.getSession().getAttribute("EmpresaTipodt");
		if (EmpresaTipodt == null )  EmpresaTipodt = new EmpresaTipoDt();  

		EmpresaTipodt.setEmpresaTipoCodigo( request.getParameter("EmpresaTipoCodigo")); 
		EmpresaTipodt.setEmpresaTipo( request.getParameter("EmpresaTipo")); 

		EmpresaTipodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		EmpresaTipodt.setIpComputadorLog(request.getRemoteAddr());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					EmpresaTipone.excluir(EmpresaTipodt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Novo: 
				EmpresaTipodt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=EmpresaTipone.Verificar(EmpresaTipodt); 
					if (Mensagem.length()==0){
						EmpresaTipone.salvar(EmpresaTipodt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;
//--------------------------------------------------------------------------------//
			default:
				stId = request.getParameter("Id_EmpresaTipo");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( EmpresaTipodt.getId()))){
						EmpresaTipodt.limpar();
						EmpresaTipodt = EmpresaTipone.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("EmpresaTipodt",EmpresaTipodt );
		request.getSession().setAttribute("EmpresaTipone",EmpresaTipone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
