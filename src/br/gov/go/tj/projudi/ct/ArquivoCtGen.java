package br.gov.go.tj.projudi.ct;

 import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ArquivoDt;
import br.gov.go.tj.projudi.ne.ArquivoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class ArquivoCtGen extends Controle {


	/**
     * 
     */
    private static final long serialVersionUID = 2620609152803991029L;

    public  ArquivoCtGen() {

	} 
		public int Permissao(){
			return ArquivoDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ArquivoDt Arquivodt;
		ArquivoNe Arquivone;


		List tempList=null; 
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/Arquivo.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","Arquivo");
		request.setAttribute("tempBuscaId_Arquivo","Id_Arquivo");
		request.setAttribute("tempBuscaArquivo","Arquivo");
		request.setAttribute("tempBuscaId_ArquivoTipo","Id_ArquivoTipo");
		request.setAttribute("tempBuscaArquivoTipo","ArquivoTipo");

		request.setAttribute("tempRetorno","Arquivo");



		Arquivone =(ArquivoNe)request.getSession().getAttribute("Arquivone");
		if (Arquivone == null )  Arquivone = new ArquivoNe();  


		Arquivodt =(ArquivoDt)request.getSession().getAttribute("Arquivodt");
		if (Arquivodt == null )  Arquivodt = new ArquivoDt();  

		Arquivodt.setNomeArquivo( request.getParameter("NomeArquivo")); 
		Arquivodt.setId_ArquivoTipo( request.getParameter("Id_ArquivoTipo")); 
		Arquivodt.setArquivoTipo( request.getParameter("ArquivoTipo")); 
		Arquivodt.setContentType( request.getParameter("ContentType")); 
		Arquivodt.setArquivo( request.getParameter("Arquivo")); 
		Arquivodt.setCaminho( request.getParameter("Caminho")); 
		Arquivodt.setDataInsercao( request.getParameter("DataInsercao")); 
		Arquivodt.setUsuarioAssinador( request.getParameter("UsuarioAssinador")); 
		if (request.getParameter("Recibo") != null)
			Arquivodt.setRecibo( request.getParameter("Recibo")); 
		else Arquivodt.setRecibo("false");
		Arquivodt.setArquivoTipoCodigo( request.getParameter("ArquivoTipoCodigo")); 

		Arquivodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		Arquivodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					Arquivone.excluir(Arquivodt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				stAcao="/WEB-INF/jsptjgo/ArquivoLocalizar.jsp";
				tempList =Arquivone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
				if (tempList.size()>0){
					request.setAttribute("ListaArquivo", tempList); 
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
					request.setAttribute("QuantidadePaginas", Arquivone.getQuantidadePaginas());
					Arquivodt.limpar();
				}else{
					request.setAttribute("MensagemErro", "Dados Não Localizados"); 
				}
				break;
			case Configuracao.Novo: 
				Arquivodt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=Arquivone.Verificar(Arquivodt); 
					if (Mensagem.length()==0){
						Arquivone.salvar(Arquivodt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;
//--------------------------------------------------------------------------------//
			default:
				stId = request.getParameter("Id_Arquivo");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( Arquivodt.getId()))){
						Arquivodt.limpar();
						Arquivodt = Arquivone.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("Arquivodt",Arquivodt );
		request.getSession().setAttribute("Arquivone",Arquivone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
