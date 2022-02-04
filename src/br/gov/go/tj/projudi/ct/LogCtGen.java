package br.gov.go.tj.projudi.ct;

 import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.ne.LogNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class LogCtGen extends Controle {


	/**
     * 
     */
    private static final long serialVersionUID = -2743316507559757893L;

    public  LogCtGen() {

	} 
		public int Permissao(){
			return LogDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		LogDt Logdt;
		LogNe Logne;


		List tempList=null; 
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/Log.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","Log");




		Logne =(LogNe)request.getSession().getAttribute("Logne");
		if (Logne == null )  Logne = new LogNe();  


		Logdt =(LogDt)request.getSession().getAttribute("Logdt");
		if (Logdt == null )  Logdt = new LogDt();  

		Logdt.setTabela( request.getParameter("Tabela")); 
		Logdt.setId_LogTipo( request.getParameter("Id_LogTipo")); 
		Logdt.setLogTipo( request.getParameter("LogTipo")); 
		Logdt.setData( request.getParameter("Data")); 
		Logdt.setHora( request.getParameter("Hora")); 
		Logdt.setId_Usuario( request.getParameter("Id_Usuario")); 
		Logdt.setUsuario( request.getParameter("Usuario")); 
		Logdt.setIpComputador( request.getParameter("IpComputador")); 
		Logdt.setValorAtual( request.getParameter("ValorAtual")); 
		Logdt.setValorNovo( request.getParameter("ValorNovo")); 
		Logdt.setLogTipoCodigo( request.getParameter("LogTipoCodigo")); 

		Logdt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		Logdt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					Logne.excluir(Logdt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				stAcao="/WEB-INF/jsptjgo/LogLocalizar.jsp";
				request.setAttribute("tempBuscaId_Log","Id_Log");
				request.setAttribute("tempBuscaLog","Log");
				request.setAttribute("tempRetorno","Log");
				tempList =Logne.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
				if (tempList.size()>0){
					request.setAttribute("ListaLog", tempList); 
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
					request.setAttribute("QuantidadePaginas", Logne.getQuantidadePaginas());
					Logdt.limpar();
				}else{
					request.setAttribute("MensagemErro", "Dados Não Localizados"); 
				}
				break;
			case Configuracao.Novo: 
				Logdt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=Logne.Verificar(Logdt); 
					if (Mensagem.length()==0){
						Logne.salvar(Logdt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;

				case (UsuarioDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					request.setAttribute("tempBuscaId_Usuario","Id_Usuario");
					request.setAttribute("tempBuscaUsuario","Usuario");
					request.setAttribute("tempRetorno","Log");
					tempList =Logne.consultarDescricaoUsuario(tempNomeBusca, PosicaoPaginaAtual);
					if (tempList.size()>0){
						request.setAttribute("ListaUsuario", tempList); 
						request.setAttribute("PaginaAtual", paginaatual);
						request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
						request.setAttribute("QuantidadePaginas", Logne.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
//--------------------------------------------------------------------------------//
			default:
				stId = request.getParameter("Id_Log");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( Logdt.getId()))){
						Logdt.limpar();
						Logdt = Logne.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("Logdt",Logdt );
		request.getSession().setAttribute("Logne",Logne );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
