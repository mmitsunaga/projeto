package br.gov.go.tj.projudi.ct;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import java.io.IOException;

import br.gov.go.tj.projudi.ne.MandadoFaixaValorNe;
import br.gov.go.tj.projudi.dt.MandadoFaixaValorDt;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class MandadoFaixaValorCtGen extends Controle { 


	/**
	 * 
	 */
	private static final long serialVersionUID = -7983671750960345060L;

	public  MandadoFaixaValorCtGen() { 

	} 
		public int Permissao(){
			return MandadoFaixaValorDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe usuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		MandadoFaixaValorDt MandadoFaixaValordt;
		MandadoFaixaValorNe MandadoFaixaValorne;


		String stNomeBusca1="";
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/MandadoFaixaValor.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","MandadoFaixaValor");




		MandadoFaixaValorne =(MandadoFaixaValorNe)request.getSession().getAttribute("MandadoFaixaValorne");
		if (MandadoFaixaValorne == null )  MandadoFaixaValorne = new MandadoFaixaValorNe();  


		MandadoFaixaValordt =(MandadoFaixaValorDt)request.getSession().getAttribute("MandadoFaixaValordt");
		if (MandadoFaixaValordt == null )  MandadoFaixaValordt = new MandadoFaixaValorDt();  

		MandadoFaixaValordt.setFaixaInicio( request.getParameter("FaixaInicio")); 
		MandadoFaixaValordt.setFaixaFim( request.getParameter("FaixaFim")); 
		MandadoFaixaValordt.setDataVigenciaInicio( request.getParameter("DataVigenciaInicio")); 
		MandadoFaixaValordt.setDataVigenciaFim( request.getParameter("DataVigenciaFim")); 
		MandadoFaixaValordt.setValor( request.getParameter("Valor")); 

		MandadoFaixaValordt.setId_UsuarioLog(usuarioSessao.getId_Usuario());
		MandadoFaixaValordt.setIpComputadorLog(request.getRemoteAddr());


		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					MandadoFaixaValorne.excluir(MandadoFaixaValordt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"MandadoFaixaValor"};
					String[] lisDescricao = {"MandadoFaixaValor"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_MandadoFaixaValor");
					request.setAttribute("tempBuscaDescricao","MandadoFaixaValor");
					request.setAttribute("tempBuscaPrograma","MandadoFaixaValor");
					request.setAttribute("tempRetorno","MandadoFaixaValor");
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = MandadoFaixaValorne.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
					try {
						response.setContentType("text/x-json");
						response.getOutputStream().write(stTemp.getBytes());
						response.flushBuffer();
					}catch(Exception e) { }
					return;
				}
				break;
			case Configuracao.Novo: 
				MandadoFaixaValordt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=MandadoFaixaValorne.Verificar(MandadoFaixaValordt); 
					if (Mensagem.length()==0){
						MandadoFaixaValorne.salvar(MandadoFaixaValordt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;
//--------------------------------------------------------------------------------//
			default:
				stId = request.getParameter("Id_MandadoFaixaValor");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( MandadoFaixaValordt.getId()))){
						MandadoFaixaValordt.limpar();
						MandadoFaixaValordt = MandadoFaixaValorne.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("MandadoFaixaValordt",MandadoFaixaValordt );
		request.getSession().setAttribute("MandadoFaixaValorne",MandadoFaixaValorne );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
