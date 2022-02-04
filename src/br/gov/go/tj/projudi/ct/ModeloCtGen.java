package br.gov.go.tj.projudi.ct;

 import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ModeloDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaDt;
import br.gov.go.tj.projudi.ne.ModeloNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class ModeloCtGen extends Controle {


	/**
     * 
     */
    private static final long serialVersionUID = 7570540287959613729L;

    public  ModeloCtGen() {

	} 
		public int Permissao(){
			return ModeloDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ModeloDt Modelodt;
		ModeloNe Modelone;


		List tempList=null; 
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/Modelo.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","Modelo");




		Modelone =(ModeloNe)request.getSession().getAttribute("Modelone");
		if (Modelone == null )  Modelone = new ModeloNe();  


		Modelodt =(ModeloDt)request.getSession().getAttribute("Modelodt");
		if (Modelodt == null )  Modelodt = new ModeloDt();  

		Modelodt.setModelo( request.getParameter("Modelo")); 
		Modelodt.setId_Serventia( request.getParameter("Id_Serventia")); 
		Modelodt.setServentia( request.getParameter("Serventia")); 
		Modelodt.setId_ArquivoTipo( request.getParameter("Id_ArquivoTipo")); 
		Modelodt.setArquivoTipo( request.getParameter("ArquivoTipo")); 
		Modelodt.setId_UsuarioServentia( request.getParameter("Id_UsuarioServentia")); 
		Modelodt.setUsuarioServentia( request.getParameter("UsuarioServentia")); 
		Modelodt.setTexto( request.getParameter("Texto")); 
		Modelodt.setServentiaCodigo( request.getParameter("ServentiaCodigo")); 

		Modelodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		Modelodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					Modelone.excluir(Modelodt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Novo: 
				Modelodt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=Modelone.Verificar(Modelodt); 
					if (Mensagem.length()==0){
						Modelone.salvar(Modelodt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;
				case (ServentiaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					request.setAttribute("tempBuscaId_Serventia","Id_Serventia");
					request.setAttribute("tempBuscaServentia","Serventia");
					request.setAttribute("tempRetorno","Modelo");
					stAcao="/WEB-INF/jsptjgo/ServentiaLocalizar.jsp";
					tempList =Modelone.consultarDescricaoServentia(tempNomeBusca, PosicaoPaginaAtual);
					if (tempList.size()>0){
						request.setAttribute("ListaServentia", tempList); 
						request.setAttribute("PaginaAtual", paginaatual);
						request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
						request.setAttribute("QuantidadePaginas", Modelone.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
				case (UsuarioServentiaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					request.setAttribute("tempBuscaId_UsuarioServentia","Id_UsuarioServentia");
					request.setAttribute("tempBuscaUsuarioServentia","UsuarioServentia");
					request.setAttribute("tempRetorno","Modelo");
					stAcao="/WEB-INF/jsptjgo/UsuarioServentiaLocalizar.jsp";
					tempList =Modelone.consultarDescricaoUsuarioServentia(tempNomeBusca, PosicaoPaginaAtual);
					if (tempList.size()>0){
						request.setAttribute("ListaUsuarioServentia", tempList); 
						request.setAttribute("PaginaAtual", paginaatual);
						request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
						request.setAttribute("QuantidadePaginas", Modelone.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
//--------------------------------------------------------------------------------//
			default:
				stId = request.getParameter("Id_Modelo");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( Modelodt.getId()))){
						Modelodt.limpar();
						Modelodt = Modelone.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("Modelodt",Modelodt );
		request.getSession().setAttribute("Modelone",Modelone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
