package br.gov.go.tj.projudi.ct;


import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.PaisDt;
import br.gov.go.tj.projudi.ne.PaisNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class PaisCtGen extends Controle {


	/**
     * 
     */
    private static final long serialVersionUID = 6479926071260391704L;

    public  PaisCtGen() {

	} 
		public int Permissao(){
			return PaisDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		PaisDt Paisdt;
		PaisNe Paisne;


		List tempList=null; 
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/Pais.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","Pais");




		Paisne =(PaisNe)request.getSession().getAttribute("Paisne");
		if (Paisne == null )  Paisne = new PaisNe();  


		Paisdt =(PaisDt)request.getSession().getAttribute("Paisdt");
		if (Paisdt == null )  Paisdt = new PaisDt();  

		Paisdt.setPais( request.getParameter("Pais")); 
		Paisdt.setPaisCodigo( request.getParameter("PaisCodigo")); 

		Paisdt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		Paisdt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					Paisne.excluir(Paisdt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				break;
			case Configuracao.Novo: 
				Paisdt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=Paisne.Verificar(Paisdt); 
					if (Mensagem.length()==0){
						Paisne.salvar(Paisdt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;
//--------------------------------------------------------------------------------//
			default:
				stId = request.getParameter("Id_Pais");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( Paisdt.getId()))){
						Paisdt.limpar();
						Paisdt = Paisne.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("Paisdt",Paisdt );
		request.getSession().setAttribute("Paisne",Paisne );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
