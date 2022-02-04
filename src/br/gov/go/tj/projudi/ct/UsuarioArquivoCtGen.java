package br.gov.go.tj.projudi.ct;

 import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ArquivoDt;
import br.gov.go.tj.projudi.dt.UsuarioArquivoDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.ne.UsuarioArquivoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class UsuarioArquivoCtGen extends Controle {


	/**
     * 
     */
    private static final long serialVersionUID = -8871551272759047755L;

    public  UsuarioArquivoCtGen() {

	} 
		public int Permissao(){
			return UsuarioArquivoDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		UsuarioArquivoDt UsuarioArquivodt;
		UsuarioArquivoNe UsuarioArquivone;


		List tempList=null; 
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/UsuarioArquivo.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","UsuarioArquivo");




		UsuarioArquivone =(UsuarioArquivoNe)request.getSession().getAttribute("UsuarioArquivone");
		if (UsuarioArquivone == null )  UsuarioArquivone = new UsuarioArquivoNe();  


		UsuarioArquivodt =(UsuarioArquivoDt)request.getSession().getAttribute("UsuarioArquivodt");
		if (UsuarioArquivodt == null )  UsuarioArquivodt = new UsuarioArquivoDt();  

		UsuarioArquivodt.setId_Usuario( request.getParameter("Id_Usuario")); 
		UsuarioArquivodt.setUsuario( request.getParameter("Usuario")); 
		UsuarioArquivodt.setId_Arquivo( request.getParameter("Id_Arquivo")); 
		UsuarioArquivodt.setNomeArquivo( request.getParameter("NomeArquivo")); 

		UsuarioArquivodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		UsuarioArquivodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					UsuarioArquivone.excluir(UsuarioArquivodt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				stAcao="/WEB-INF/jsptjgo/UsuarioArquivoLocalizar.jsp";
				request.setAttribute("tempBuscaId_UsuarioArquivo","Id_UsuarioArquivo");
				request.setAttribute("tempBuscaUsuarioArquivo","UsuarioArquivo");
				request.setAttribute("tempRetorno","UsuarioArquivo");
				tempList =UsuarioArquivone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
				if (tempList.size()>0){
					request.setAttribute("ListaUsuarioArquivo", tempList); 
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
					request.setAttribute("QuantidadePaginas", UsuarioArquivone.getQuantidadePaginas());
					UsuarioArquivodt.limpar();
				}else{
					request.setAttribute("MensagemErro", "Dados Não Localizados"); 
				}
				break;
			case Configuracao.Novo: 
				UsuarioArquivodt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=UsuarioArquivone.Verificar(UsuarioArquivodt); 
					if (Mensagem.length()==0){
						UsuarioArquivone.salvar(UsuarioArquivodt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;
				case (UsuarioDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					request.setAttribute("tempBuscaId_Usuario","Id_Usuario");
					request.setAttribute("tempBuscaUsuario","Usuario");
					request.setAttribute("tempRetorno","UsuarioArquivo");
					tempList =UsuarioArquivone.consultarDescricaoUsuario(tempNomeBusca, PosicaoPaginaAtual);
					if (tempList.size()>0){
						request.setAttribute("ListaUsuario", tempList); 
						request.setAttribute("PaginaAtual", paginaatual);
						request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
						request.setAttribute("QuantidadePaginas", UsuarioArquivone.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
				case (ArquivoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					request.setAttribute("tempBuscaId_Arquivo","Id_Arquivo");
					request.setAttribute("tempBuscaArquivo","Arquivo");
					request.setAttribute("tempRetorno","UsuarioArquivo");
					stAcao="/WEB-INF/jsptjgo/ArquivoLocalizar.jsp";
					tempList =UsuarioArquivone.consultarDescricaoArquivo(tempNomeBusca, PosicaoPaginaAtual);
					if (tempList.size()>0){
						request.setAttribute("ListaArquivo", tempList); 
						request.setAttribute("PaginaAtual", paginaatual);
						request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
						request.setAttribute("QuantidadePaginas", UsuarioArquivone.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
//--------------------------------------------------------------------------------//
			default:
				stId = request.getParameter("Id_UsuarioArquivo");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( UsuarioArquivodt.getId()))){
						UsuarioArquivodt.limpar();
						UsuarioArquivodt = UsuarioArquivone.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("UsuarioArquivodt",UsuarioArquivodt );
		request.getSession().setAttribute("UsuarioArquivone",UsuarioArquivone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
