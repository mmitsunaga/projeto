package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.PermissaoDt;
import br.gov.go.tj.projudi.dt.PermissaoEspecialDt;
import br.gov.go.tj.projudi.ne.PermissaoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class PermissaoCtGen extends Controle {


	/**
     * 
     */
    private static final long serialVersionUID = -8694652515539259695L;

    public  PermissaoCtGen() {

	} 
		public int Permissao(){
			return PermissaoDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		PermissaoDt Permissaodt;
		PermissaoNe Permissaone;


		List tempList=null; 
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/Permissao.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","Permissao");
		request.setAttribute("tempBuscaId_Permissao","Id_Permissao");
		request.setAttribute("tempBuscaPermissao","Permissao");
		
		request.setAttribute("tempBuscaId_PermissaoPai","Id_PermissaoPai");
		request.setAttribute("tempBuscaPermissao","Permissao");
		request.setAttribute("tempBuscaId_PermissaoEspecial","Id_PermissaoEspecial");
		request.setAttribute("tempBuscaPermissaoEspecial","PermissaoEspecial");

		request.setAttribute("tempRetorno","Permissao");



		Permissaone =(PermissaoNe)request.getSession().getAttribute("Permissaone");
		if (Permissaone == null )  Permissaone = new PermissaoNe();  


		Permissaodt =(PermissaoDt)request.getSession().getAttribute("Permissaodt");
		if (Permissaodt == null )  Permissaodt = new PermissaoDt();  

		Permissaodt.setPermissao( request.getParameter("Permissao")); 
		Permissaodt.setPermissaoCodigo( request.getParameter("PermissaoCodigo")); 
		if (request.getParameter("EMenu") != null)
			Permissaodt.setEMenu( request.getParameter("EMenu")); 
		else Permissaodt.setEMenu("false");
		Permissaodt.setLink( request.getParameter("Link")); 
		Permissaodt.setIrPara( request.getParameter("IrPara")); 
		Permissaodt.setTitulo( request.getParameter("Titulo")); 
		Permissaodt.setId_PermissaoPai( request.getParameter("Id_PermissaoPai")); 
		Permissaodt.setPermissaoPai( request.getParameter("PermissaoPai")); 
		Permissaodt.setId_PermissaoEspecial( request.getParameter("Id_PermissaoEspecial")); 
		Permissaodt.setPermissaoEspecial( request.getParameter("PermissaoEspecial")); 
		Permissaodt.setOrdenacao( request.getParameter("Ordenacao")); 
		Permissaodt.setPermissaoCodigoPai( request.getParameter("PermissaoCodigoPai")); 
		Permissaodt.setPermissaoEspecialCodigo( request.getParameter("PermissaoEspecialCodigo")); 

		Permissaodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		Permissaodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				Permissaone.excluir(Permissaodt); 
				request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				stAcao="/WEB-INF/jsptjgo/PermissaoLocalizar.jsp";
				tempList =Permissaone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
				if (tempList.size()>0){
					request.setAttribute("ListaPermissao", tempList); 
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
					request.setAttribute("QuantidadePaginas", Permissaone.getQuantidadePaginas());
					Permissaodt.limpar();
				}else{ 
					request.setAttribute("MensagemErro", "Dados Não Localizados"); 
					request.setAttribute("PaginaAtual", Configuracao.Editar);
				}
				break;
			case Configuracao.Novo: 
				Permissaodt.limpar();
				request.setAttribute("PaginaAtual",Configuracao.Editar);
				break;
			case Configuracao.SalvarResultado: 
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				Mensagem=Permissaone.Verificar(Permissaodt); 
				if (Mensagem.length()==0){
					Permissaone.salvar(Permissaodt); 
					request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
				}else	request.setAttribute("MensagemErro", Mensagem );
				break;
				case (PermissaoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					stAcao="/WEB-INF/jsptjgo/PermissaoLocalizar.jsp";
						tempList =Permissaone.consultarDescricaoPermissao(tempNomeBusca, PosicaoPaginaAtual);
						if (tempList.size()>0){
							request.setAttribute("ListaPermissao", tempList); 
							request.setAttribute("PaginaAtual", paginaatual);
							request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
							request.setAttribute("QuantidadePaginas", Permissaone.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
				case (PermissaoEspecialDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					stAcao="/WEB-INF/jsptjgo/PermissaoEspecialLocalizar.jsp";
						tempList =Permissaone.consultarDescricaoPermissaoEspecial(tempNomeBusca, PosicaoPaginaAtual);
						if (tempList.size()>0){
							request.setAttribute("ListaPermissaoEspecial", tempList); 
							request.setAttribute("PaginaAtual", paginaatual);
							request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
							request.setAttribute("QuantidadePaginas", Permissaone.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
//--------------------------------------------------------------------------------//
			default:
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				stId = request.getParameter("Id_Permissao");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( Permissaodt.getId()))){
						Permissaodt.limpar();
						Permissaodt = Permissaone.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("Permissaodt",Permissaodt );
		request.getSession().setAttribute("Permissaone",Permissaone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
