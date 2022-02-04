package br.gov.go.tj.projudi.ct;

 import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.PartesIsentaDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaDt;
import br.gov.go.tj.projudi.ne.PartesIsentaNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class PartesIsentaCt extends PartesIsentaCtGen{


	/**
	 * 
	 */
	private static final long serialVersionUID = 7108735800200341120L;

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe usuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		PartesIsentaDt PartesIsentadt;
		PartesIsentaNe PartesIsentane;


		String stNomeBusca1="";
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/PartesIsenta.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","Partes Isentas");




		PartesIsentane =(PartesIsentaNe)request.getSession().getAttribute("PartesIsentane");
		if (PartesIsentane == null )  PartesIsentane = new PartesIsentaNe();  


		PartesIsentadt =(PartesIsentaDt)request.getSession().getAttribute("PartesIsentadt");
		if (PartesIsentadt == null )  PartesIsentadt = new PartesIsentaDt();  

		PartesIsentadt.setNome( request.getParameter("Nome")); 
		PartesIsentadt.setCpf( request.getParameter("Cpf")); 
		PartesIsentadt.setCnpj( request.getParameter("Cnpj")); 
		PartesIsentadt.setId_UsuarioCadastrador( request.getParameter("Id_UsuarioCadastrador")); 
		PartesIsentadt.setNomeUsuarioCadastrador( request.getParameter("NomeUsuarioCadastrador")); 
		PartesIsentadt.setId_ServentiaUsuarioCadastrador( request.getParameter("Id_ServentiaUsuarioCadastrador")); 
		PartesIsentadt.setServentiaUsuarioCadastrador( request.getParameter("ServentiaUsuarioCadastrador")); 
		PartesIsentadt.setDataCadastro( request.getParameter("DataCadastro")); 
		PartesIsentadt.setId_UsuarioBaixa( request.getParameter("Id_UsuarioBaixa")); 
		PartesIsentadt.setNomeUsuarioBaixa( request.getParameter("NomeUsuarioBaixa")); 
		PartesIsentadt.setId_ServentiaUsuarioBaixa( request.getParameter("Id_ServentiaUsuarioBaixa")); 
		PartesIsentadt.setServentiaUsuarioBaixa( request.getParameter("ServentiaUsuarioBaixa")); 
		PartesIsentadt.setDataBaixa( request.getParameter("DataBaixa")); 

		PartesIsentadt.setId_UsuarioLog(usuarioSessao.getId_Usuario());
		PartesIsentadt.setIpComputadorLog(request.getRemoteAddr());


		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);

		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
				Mensagem=PartesIsentane.verificarExclusao(PartesIsentadt); 
				if (Mensagem.length()==0){
					PartesIsentadt.setId_UsuarioBaixa(usuarioSessao.getId_UsuarioServentia());
					PartesIsentane.BaixarParteIsenta(PartesIsentadt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				} else {	
					request.setAttribute("MensagemErro", Mensagem );
				}
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Partes Isentas"};
					String[] lisDescricao = {"Partes Isentas", "Data Cadastro", "Usuário Cadastrador", "Data Baixa", "Usuário Baixa"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_PartesIsentas");
					request.setAttribute("tempBuscaDescricao","Partes Isentas");
					request.setAttribute("tempBuscaPrograma","Partes Isentas");
					request.setAttribute("tempRetorno","PartesIsenta");
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = PartesIsentane.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
					try {
						response.setContentType("text/x-json");
						response.getOutputStream().write(stTemp.getBytes());
						response.flushBuffer();
					}catch(Exception e) { }
					return;
				}
				break;
			case Configuracao.Novo: 
				PartesIsentadt.limpar();
				break;
			case Configuracao.SalvarResultado: 
				    PartesIsentadt.setId_UsuarioCadastrador(usuarioSessao.getId_UsuarioServentia());
					Mensagem=PartesIsentane.Verificar(PartesIsentadt); 
					if (Mensagem.length()==0){
						PartesIsentane.salvar(PartesIsentadt); 
						PartesIsentadt.limpar();
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					} else {
						request.setAttribute("MensagemErro", Mensagem );
					}
				break;

//--------------------------------------------------------------------------------//
			default:
				stId = request.getParameter("Id_PartesIsentas");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( PartesIsentadt.getId()))){
						PartesIsentadt.limpar();
						PartesIsentadt = PartesIsentane.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("PartesIsentadt",PartesIsentadt );
		request.getSession().setAttribute("PartesIsentane",PartesIsentane );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
