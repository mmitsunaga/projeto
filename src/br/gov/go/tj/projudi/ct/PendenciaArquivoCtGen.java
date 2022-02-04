package br.gov.go.tj.projudi.ct;

 import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ArquivoDt;
import br.gov.go.tj.projudi.dt.PendenciaArquivoDt;
import br.gov.go.tj.projudi.dt.PendenciaDt;
import br.gov.go.tj.projudi.ne.PendenciaArquivoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class PendenciaArquivoCtGen extends Controle {


	/**
     * 
     */
    private static final long serialVersionUID = -9154033523260544624L;

    public  PendenciaArquivoCtGen() {

	} 
		public int Permissao(){
			return PendenciaArquivoDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		PendenciaArquivoDt PendenciaArquivodt;
		PendenciaArquivoNe PendenciaArquivone;


		List tempList=null; 
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/PendenciaArquivo.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","PendenciaArquivo");
		request.setAttribute("tempBuscaId_PendenciaArquivo","Id_PendenciaArquivo");
		request.setAttribute("tempBuscaPendenciaArquivo","PendenciaArquivo");
		request.setAttribute("tempBuscaId_Arquivo","Id_Arquivo");
		request.setAttribute("tempBuscaArquivo","Arquivo");
		request.setAttribute("tempBuscaId_Pendencia","Id_Pendencia");
		request.setAttribute("tempBuscaPendencia","Pendencia");

		request.setAttribute("tempRetorno","PendenciaArquivo");



		PendenciaArquivone =(PendenciaArquivoNe)request.getSession().getAttribute("PendenciaArquivone");
		if (PendenciaArquivone == null )  PendenciaArquivone = new PendenciaArquivoNe();  


		PendenciaArquivodt =(PendenciaArquivoDt)request.getSession().getAttribute("PendenciaArquivodt");
		if (PendenciaArquivodt == null )  PendenciaArquivodt = new PendenciaArquivoDt();  

		PendenciaArquivodt.setId_Arquivo( request.getParameter("Id_Arquivo")); 
		PendenciaArquivodt.setNomeArquivo( request.getParameter("NomeArquivo")); 
		PendenciaArquivodt.setId_Pendencia( request.getParameter("Id_Pendencia")); 
		PendenciaArquivodt.setPendenciaTipo( request.getParameter("PendenciaTipo")); 
		if (request.getParameter("Resposta") != null)
			PendenciaArquivodt.setResposta( request.getParameter("Resposta")); 
		else PendenciaArquivodt.setResposta("false");

		PendenciaArquivodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		PendenciaArquivodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					PendenciaArquivone.excluir(PendenciaArquivodt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				stAcao="/WEB-INF/jsptjgo/PendenciaArquivoLocalizar.jsp";
				tempList =PendenciaArquivone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
				if (tempList.size()>0){
					request.setAttribute("ListaPendenciaArquivo", tempList); 
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
					request.setAttribute("QuantidadePaginas", PendenciaArquivone.getQuantidadePaginas());
					PendenciaArquivodt.limpar();
				}else{
					request.setAttribute("MensagemErro", "Dados Não Localizados"); 
				}
				break;
			case Configuracao.Novo: 
				PendenciaArquivodt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=PendenciaArquivone.Verificar(PendenciaArquivodt); 
					if (Mensagem.length()==0){
						PendenciaArquivone.salvar(PendenciaArquivodt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;
				case (ArquivoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					stAcao="/WEB-INF/jsptjgo/ArquivoLocalizar.jsp";
						tempList =PendenciaArquivone.consultarDescricaoArquivo(tempNomeBusca, PosicaoPaginaAtual);
						if (tempList.size()>0){
							request.setAttribute("ListaArquivo", tempList); 
							request.setAttribute("PaginaAtual", paginaatual);
							request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
							request.setAttribute("QuantidadePaginas", PendenciaArquivone.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
				case (PendenciaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					stAcao="/WEB-INF/jsptjgo/PendenciaLocalizar.jsp";
						tempList =PendenciaArquivone.consultarDescricaoPendencia(tempNomeBusca, PosicaoPaginaAtual);
						if (tempList.size()>0){
							request.setAttribute("ListaPendencia", tempList); 
							request.setAttribute("PaginaAtual", paginaatual);
							request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
							request.setAttribute("QuantidadePaginas", PendenciaArquivone.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
//--------------------------------------------------------------------------------//
			default:
				stId = request.getParameter("Id_PendenciaArquivo");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( PendenciaArquivodt.getId()))){
						PendenciaArquivodt.limpar();
						PendenciaArquivodt = PendenciaArquivone.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("PendenciaArquivodt",PendenciaArquivodt );
		request.getSession().setAttribute("PendenciaArquivone",PendenciaArquivone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
