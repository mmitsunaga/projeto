package br.gov.go.tj.projudi.ct;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.MandadoPrisaoOrigemDt;
import br.gov.go.tj.projudi.ne.MandadoPrisaoOrigemNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class MandadoPrisaoOrigemCtGen extends Controle {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1172636632541994863L;

	public  MandadoPrisaoOrigemCtGen() {

	} 
		public int Permissao() {
			return MandadoPrisaoOrigemDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		MandadoPrisaoOrigemDt MandadoPrisaoOrigemdt;
		MandadoPrisaoOrigemNe MandadoPrisaoOrigemne;


		List nomeBusca=null; 
		List descricao=null; 
		String stNomeBusca1="";
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/MandadoPrisaoOrigem.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","MandadoPrisaoOrigem");




		MandadoPrisaoOrigemne =(MandadoPrisaoOrigemNe)request.getSession().getAttribute("MandadoPrisaoOrigemne");
		if (MandadoPrisaoOrigemne == null )  MandadoPrisaoOrigemne = new MandadoPrisaoOrigemNe();  


		MandadoPrisaoOrigemdt =(MandadoPrisaoOrigemDt)request.getSession().getAttribute("MandadoPrisaoOrigemdt");
		if (MandadoPrisaoOrigemdt == null )  MandadoPrisaoOrigemdt = new MandadoPrisaoOrigemDt();  

		MandadoPrisaoOrigemdt.setMandadoPrisaoOrigemCodigo( request.getParameter("MandadoPrisaoOrigemCodigo")); 
		MandadoPrisaoOrigemdt.setMandadoPrisaoOrigem( request.getParameter("MandadoPrisaoOrigem")); 

		MandadoPrisaoOrigemdt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		MandadoPrisaoOrigemdt.setIpComputadorLog(request.getRemoteAddr());


		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					MandadoPrisaoOrigemne.excluir(MandadoPrisaoOrigemdt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				if (request.getParameter("Passo")==null){
					nomeBusca = new ArrayList();
					descricao = new ArrayList();
					nomeBusca.add("MandadoPrisaoOrigem");
					descricao.add("MandadoPrisaoOrigem");
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_MandadoPrisaoOrigem");
					request.setAttribute("tempBuscaDescricao","MandadoPrisaoOrigem");
					request.setAttribute("tempBuscaPrograma","MandadoPrisaoOrigem");
					request.setAttribute("tempRetorno","MandadoPrisaoOrigem");
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("PaginaAtual", String.valueOf(Configuracao.Localizar));
					request.setAttribute("tempPaginaAtualJSON", String.valueOf(Configuracao.Editar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("nomeBusca", nomeBusca);
					request.setAttribute("descricao", descricao);
				} else {
					String stTemp="";
					request.setAttribute("tempRetorno","MandadoPrisaoOrigem");
					stTemp = MandadoPrisaoOrigemne.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
					
					try{
						enviarJSON(response, stTemp);
						
					}catch(Exception e){}
					return;
				}
				break;
			case Configuracao.Novo: 
				MandadoPrisaoOrigemdt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=MandadoPrisaoOrigemne.Verificar(MandadoPrisaoOrigemdt); 
					if (Mensagem.length()==0){
						MandadoPrisaoOrigemne.salvar(MandadoPrisaoOrigemdt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;
//--------------------------------------------------------------------------------//
			default:
				stId = request.getParameter("Id_MandadoPrisaoOrigem");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( MandadoPrisaoOrigemdt.getId()))){
						MandadoPrisaoOrigemdt.limpar();
						MandadoPrisaoOrigemdt = MandadoPrisaoOrigemne.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("MandadoPrisaoOrigemdt",MandadoPrisaoOrigemdt );
		request.getSession().setAttribute("MandadoPrisaoOrigemne",MandadoPrisaoOrigemne );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
