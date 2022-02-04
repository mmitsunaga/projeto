package br.gov.go.tj.projudi.ct;


import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.ne.LogTipoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class LogTipoCtGen extends Controle {


	/**
     * 
     */
    private static final long serialVersionUID = 3119907382308536019L;

    public  LogTipoCtGen() {

	} 
		public int Permissao(){
			return LogTipoDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		LogTipoDt LogTipodt;
		LogTipoNe LogTipone;


		List tempList=null; 
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/LogTipo.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","LogTipo");




		LogTipone =(LogTipoNe)request.getSession().getAttribute("LogTipone");
		if (LogTipone == null )  LogTipone = new LogTipoNe();  


		LogTipodt =(LogTipoDt)request.getSession().getAttribute("LogTipodt");
		if (LogTipodt == null )  LogTipodt = new LogTipoDt();  

		LogTipodt.setLogTipo( request.getParameter("LogTipo")); 
		LogTipodt.setLogTipoCodigo( request.getParameter("LogTipoCodigo")); 

		LogTipodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		LogTipodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					LogTipone.excluir(LogTipodt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			
			case Configuracao.Novo: 
				LogTipodt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=LogTipone.Verificar(LogTipodt); 
					if (Mensagem.length()==0){
						LogTipone.salvar(LogTipodt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;
//--------------------------------------------------------------------------------//
			default:
				stId = request.getParameter("Id_LogTipo");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( LogTipodt.getId()))){
						LogTipodt.limpar();
						LogTipodt = LogTipone.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("LogTipodt",LogTipodt );
		request.getSession().setAttribute("LogTipone",LogTipone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
