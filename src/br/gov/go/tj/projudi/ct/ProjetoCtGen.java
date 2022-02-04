package br.gov.go.tj.projudi.ct;


import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ProjetoDt;
import br.gov.go.tj.projudi.ne.ProjetoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class ProjetoCtGen extends Controle {


	/**
     * 
     */
    private static final long serialVersionUID = -567161073710424116L;

    public  ProjetoCtGen() {

	} 
		public int Permissao(){
			return ProjetoDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ProjetoDt Projetodt;
		ProjetoNe Projetone;


		List tempList=null; 
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/Projeto.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","Projeto");




		Projetone =(ProjetoNe)request.getSession().getAttribute("Projetone");
		if (Projetone == null )  Projetone = new ProjetoNe();  


		Projetodt =(ProjetoDt)request.getSession().getAttribute("Projetodt");
		if (Projetodt == null )  Projetodt = new ProjetoDt();  

		Projetodt.setProjeto( request.getParameter("Projeto")); 

		Projetodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		Projetodt.setIpComputadorLog(request.getRemoteAddr());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					Projetone.excluir(Projetodt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				request.setAttribute("tempBuscaId_Projeto","Id_Projeto");
				request.setAttribute("tempBuscaProjeto","Projeto");
				request.setAttribute("tempRetorno","Projeto");
				tempList =Projetone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
				if (tempList.size()>0){
					request.setAttribute("ListaProjeto", tempList); 
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
					request.setAttribute("QuantidadePaginas", Projetone.getQuantidadePaginas());
					Projetodt.limpar();
				}else{
					request.setAttribute("MensagemErro", "Dados Não Localizados"); 
				}
				break;
			case Configuracao.Novo: 
				Projetodt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=Projetone.Verificar(Projetodt); 
					if (Mensagem.length()==0){
						Projetone.salvar(Projetodt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;
//--------------------------------------------------------------------------------//
			default:
				stId = request.getParameter("Id_Projeto");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( Projetodt.getId()))){
						Projetodt.limpar();
						Projetodt = Projetone.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("Projetodt",Projetodt );
		request.getSession().setAttribute("Projetone",Projetone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
