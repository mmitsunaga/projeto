package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ServentiaCargoEscalaStatusDt;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.ne.ServentiaCargoEscalaStatusNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class ServentiaCargoEscalaStatusCtGen extends Controle {

    private static final long serialVersionUID = -4639183493195694696L;

    public  ServentiaCargoEscalaStatusCtGen() {

	} 
	public int Permissao(){
		return ServentiaCargoEscalaStatusDt.CodigoPermissao;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ServentiaCargoEscalaStatusDt ServentiaCargoEscalaStatusdt;
		ServentiaCargoEscalaStatusNe ServentiaCargoEscalaStatusne;

		List tempList=null; 
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/ServentiaCargoEscalaStatus.jsp";

		request.setAttribute("tempPrograma","ServentiaCargoEscalaStatus");

		ServentiaCargoEscalaStatusne =(ServentiaCargoEscalaStatusNe)request.getSession().getAttribute("ServentiaCargoEscalaStatusne");
		if (ServentiaCargoEscalaStatusne == null )  ServentiaCargoEscalaStatusne = new ServentiaCargoEscalaStatusNe();  


		ServentiaCargoEscalaStatusdt =(ServentiaCargoEscalaStatusDt)request.getSession().getAttribute("ServentiaCargoEscalaStatusdt");
		if (ServentiaCargoEscalaStatusdt == null )  ServentiaCargoEscalaStatusdt = new ServentiaCargoEscalaStatusDt();  

		ServentiaCargoEscalaStatusdt.setServentiaCargoEscalaStatus( request.getParameter("ServentiaCargoEscalaStatus")); 
		ServentiaCargoEscalaStatusdt.setServentiaCargoEscalaStatusCodigo( request.getParameter("ServentiaCargoEscalaStatusCodigo")); 
		if (request.getParameter("Ativo") != null)
			ServentiaCargoEscalaStatusdt.setAtivo( request.getParameter("Ativo")); 
		else ServentiaCargoEscalaStatusdt.setAtivo("false");

		ServentiaCargoEscalaStatusdt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		ServentiaCargoEscalaStatusdt.setIpComputadorLog(request.getRemoteAddr());

		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual",Configuracao.Editar);
		
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					ServentiaCargoEscalaStatusne.excluir(ServentiaCargoEscalaStatusdt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				stAcao="/WEB-INF/jsptjgo/ServentiaCargoEscalaStatusLocalizar.jsp";
				request.setAttribute("tempBuscaId_ServentiaCargoEscalaStatus","Id_ServentiaCargoEscalaStatus");
				request.setAttribute("tempBuscaServentiaCargoEscalaStatus","ServentiaCargoEscalaStatus");
				request.setAttribute("tempRetorno","ServentiaCargoEscalaStatus");
				tempList =ServentiaCargoEscalaStatusne.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
				if (tempList.size()>0){
					request.setAttribute("ListaServentiaCargoEscalaStatus", tempList); 
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
					request.setAttribute("QuantidadePaginas", ServentiaCargoEscalaStatusne.getQuantidadePaginas());
					ServentiaCargoEscalaStatusdt.limpar();
				}else{
					request.setAttribute("MensagemErro", "Dados Não Localizados"); 
				}
				break;
			case Configuracao.Novo: 
				ServentiaCargoEscalaStatusdt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=ServentiaCargoEscalaStatusne.Verificar(ServentiaCargoEscalaStatusdt); 
					if (Mensagem.length()==0){
						ServentiaCargoEscalaStatusne.salvar(ServentiaCargoEscalaStatusdt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;
			default:
				stId = request.getParameter("Id_ServentiaCargoEscalaStatus");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( ServentiaCargoEscalaStatusdt.getId()))){
						ServentiaCargoEscalaStatusdt.limpar();
						ServentiaCargoEscalaStatusdt = ServentiaCargoEscalaStatusne.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("ServentiaCargoEscalaStatusdt",ServentiaCargoEscalaStatusdt );
		request.getSession().setAttribute("ServentiaCargoEscalaStatusne",ServentiaCargoEscalaStatusne );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
