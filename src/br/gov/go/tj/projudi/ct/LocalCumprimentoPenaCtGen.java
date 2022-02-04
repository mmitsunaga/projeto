package br.gov.go.tj.projudi.ct;

 import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.EnderecoDt;
import br.gov.go.tj.projudi.dt.LocalCumprimentoPenaDt;
import br.gov.go.tj.projudi.ne.LocalCumprimentoPenaNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class LocalCumprimentoPenaCtGen extends Controle {


	/**
     * 
     */
    private static final long serialVersionUID = 6052691184011537020L;

    public  LocalCumprimentoPenaCtGen() {

	} 
		public int Permissao(){
			return LocalCumprimentoPenaDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		LocalCumprimentoPenaDt LocalCumprimentoPenadt;
		LocalCumprimentoPenaNe LocalCumprimentoPenane;


		List tempList=null; 
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/LocalCumprimentoPena.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","LocalCumprimentoPena");




		LocalCumprimentoPenane =(LocalCumprimentoPenaNe)request.getSession().getAttribute("LocalCumprimentoPenane");
		if (LocalCumprimentoPenane == null )  LocalCumprimentoPenane = new LocalCumprimentoPenaNe();  


		LocalCumprimentoPenadt =(LocalCumprimentoPenaDt)request.getSession().getAttribute("LocalCumprimentoPenadt");
		if (LocalCumprimentoPenadt == null )  LocalCumprimentoPenadt = new LocalCumprimentoPenaDt();  

		LocalCumprimentoPenadt.setLocalCumprimentoPena( request.getParameter("LocalCumprimentoPena")); 
		LocalCumprimentoPenadt.setId_Endereco( request.getParameter("Id_Endereco")); 
		LocalCumprimentoPenadt.setEndereco( request.getParameter("Endereco")); 

		LocalCumprimentoPenadt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		LocalCumprimentoPenadt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					LocalCumprimentoPenane.excluir(LocalCumprimentoPenadt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				stAcao="/WEB-INF/jsptjgo/LocalCumprimentoPenaLocalizar.jsp";
				request.setAttribute("tempBuscaId_LocalCumprimentoPena","Id_LocalCumprimentoPena");
				request.setAttribute("tempBuscaLocalCumprimentoPena","LocalCumprimentoPena");
				request.setAttribute("tempRetorno","LocalCumprimentoPena");
				tempList =LocalCumprimentoPenane.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
				if (tempList.size()>0){
					request.setAttribute("ListaLocalCumprimentoPena", tempList); 
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
					request.setAttribute("QuantidadePaginas", LocalCumprimentoPenane.getQuantidadePaginas());
					LocalCumprimentoPenadt.limpar();
				}else{
					request.setAttribute("MensagemErro", "Dados Não Localizados"); 
				}
				break;
			case Configuracao.Novo: 
				LocalCumprimentoPenadt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=LocalCumprimentoPenane.Verificar(LocalCumprimentoPenadt); 
					if (Mensagem.length()==0){
						LocalCumprimentoPenane.salvar(LocalCumprimentoPenadt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;
				case (EnderecoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					request.setAttribute("tempBuscaId_Endereco","Id_Endereco");
					request.setAttribute("tempBuscaEndereco","Endereco");
					request.setAttribute("tempRetorno","LocalCumprimentoPena");
					stAcao="/WEB-INF/jsptjgo/EnderecoLocalizar.jsp";
					tempList =LocalCumprimentoPenane.consultarDescricaoEndereco(tempNomeBusca, PosicaoPaginaAtual);
					if (tempList.size()>0){
						request.setAttribute("ListaEndereco", tempList); 
						request.setAttribute("PaginaAtual", paginaatual);
						request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
						request.setAttribute("QuantidadePaginas", LocalCumprimentoPenane.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
//--------------------------------------------------------------------------------//
			default:
				stId = request.getParameter("Id_LocalCumprimentoPena");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( LocalCumprimentoPenadt.getId()))){
						LocalCumprimentoPenadt.limpar();
						LocalCumprimentoPenadt = LocalCumprimentoPenane.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("LocalCumprimentoPenadt",LocalCumprimentoPenadt );
		request.getSession().setAttribute("LocalCumprimentoPenane",LocalCumprimentoPenane );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
