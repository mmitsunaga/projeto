package br.gov.go.tj.projudi.ct;

 import br.gov.go.tj.projudi.dt.BancoDt;
 import br.gov.go.tj.projudi.dt.ComarcaDt;
 import br.gov.go.tj.projudi.dt.ServentiaDt;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import java.io.IOException;

import br.gov.go.tj.projudi.ne.ContaJudicialNe;
import br.gov.go.tj.projudi.dt.ContaJudicialDt;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class ContaJudicialCtGen extends Controle {


	public  ContaJudicialCtGen() {

	} 
		public int Permissao() {
			return ContaJudicialDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ContaJudicialDt ContaJudicialdt;
		ContaJudicialNe ContaJudicialne;


		String stNomeBusca1="";
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/ContaJudicial.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","ContaJudicial");




		ContaJudicialne =(ContaJudicialNe)request.getSession().getAttribute("ContaJudicialne");
		if (ContaJudicialne == null )  ContaJudicialne = new ContaJudicialNe();  


		ContaJudicialdt =(ContaJudicialDt)request.getSession().getAttribute("ContaJudicialdt");
		if (ContaJudicialdt == null )  ContaJudicialdt = new ContaJudicialDt();  

		ContaJudicialdt.setId_ProcParte( request.getParameter("Id_ProcParte")); 
		ContaJudicialdt.setNome( request.getParameter("Nome")); 
		ContaJudicialdt.setProcNumero( request.getParameter("ProcNumero")); 
		ContaJudicialdt.setId_Banco( request.getParameter("Id_Banco")); 
		ContaJudicialdt.setBanco( request.getParameter("Banco")); 
		ContaJudicialdt.setContaJudicialNumero( request.getParameter("ContaJudicialNumero")); 
		ContaJudicialdt.setId_Comarca( request.getParameter("Id_Comarca")); 
		ContaJudicialdt.setComarca( request.getParameter("Comarca")); 
		ContaJudicialdt.setId_Serv( request.getParameter("Id_Serv")); 
		ContaJudicialdt.setServ( request.getParameter("Serv")); 
		ContaJudicialdt.setPessoaTipoDepositante( request.getParameter("PessoaTipoDepositante")); 
		ContaJudicialdt.setCpfCnpjDepositante( request.getParameter("CpfCnpjDepositante")); 
		ContaJudicialdt.setNomeDepositante( request.getParameter("NomeDepositante")); 
		ContaJudicialdt.setPessoaTipoReclamado( request.getParameter("PessoaTipoReclamado")); 
		ContaJudicialdt.setCpfCnpjReclamado( request.getParameter("CpfCnpjReclamado")); 
		ContaJudicialdt.setNomeReclamado( request.getParameter("NomeReclamado")); 
		ContaJudicialdt.setPessoalTipoReclamante( request.getParameter("PessoalTipoReclamante")); 
		ContaJudicialdt.setCpfCnpjReclamante( request.getParameter("CpfCnpjReclamante")); 
		ContaJudicialdt.setNomeReclamante( request.getParameter("NomeReclamante")); 
		ContaJudicialdt.setPessoaTipoAdvReclamado( request.getParameter("PessoaTipoAdvReclamado")); 
		ContaJudicialdt.setCpfCnpjAdvReclamado( request.getParameter("CpfCnpjAdvReclamado")); 
		ContaJudicialdt.setNomeAdvReclamado( request.getParameter("NomeAdvReclamado")); 
		ContaJudicialdt.setPessoaTipoAdvReclamante( request.getParameter("PessoaTipoAdvReclamante")); 
		ContaJudicialdt.setCpfCnpjAdvReclamante( request.getParameter("CpfCnpjAdvReclamante")); 
		ContaJudicialdt.setNomeAdvReclamante( request.getParameter("NomeAdvReclamante")); 

		ContaJudicialdt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		ContaJudicialdt.setIpComputadorLog(request.getRemoteAddr());


		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					ContaJudicialne.excluir(ContaJudicialdt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"ContaJudicial"};
					String[] lisDescricao = {"ContaJudicial"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_ContaJudicial");
					request.setAttribute("tempBuscaDescricao","ContaJudicial");
					request.setAttribute("tempBuscaPrograma","ContaJudicial");
					request.setAttribute("tempRetorno","ContaJudicial");
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = ContaJudicialne.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
					try{
						
						enviarJSON(response, stTemp);
						
					}catch(Exception e) {}
					return;
				}
				break;
			case Configuracao.Novo: 
				ContaJudicialdt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=ContaJudicialne.Verificar(ContaJudicialdt); 
					if (Mensagem.length()==0){
						ContaJudicialne.salvar(ContaJudicialdt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;
//				case (ProcessoParteDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
//				if (request.getParameter("Passo")==null){
//					String[] lisNomeBusca = {"ProcessoParte"};
//					String[] lisDescricao = {"ProcessoParte"};
//					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
//					request.setAttribute("tempBuscaId","Id_ProcParte");
//					request.setAttribute("tempBuscaDescricao","Nome");
//					request.setAttribute("tempBuscaPrograma","ProcessoParte");
//					request.setAttribute("tempRetorno","ContaJudicial");
//					request.setAttribute("tempDescricaoId","Id");
//					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
//					request.setAttribute("PaginaAtual", (ProcessoParteDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar));
//					request.setAttribute("PosicaoPaginaAtual", "0");
//					request.setAttribute("QuantidadePaginas", "0");
//					request.setAttribute("lisNomeBusca", lisNomeBusca);
//					request.setAttribute("lisDescricao", lisDescricao);
//				} else {
//					String stTemp = ContaJudicialne.consultarDescricaoProcessoParteJSON(stNomeBusca1, PosicaoPaginaAtual);
//					try{
//						
//						enviarJSON(response, stTemp);
//						
//					}catch(Exception e) {}
//					return;
//				}
//					break;
				case (BancoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Banco"};
					String[] lisDescricao = {"Banco"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_Banco");
					request.setAttribute("tempBuscaDescricao","Banco");
					request.setAttribute("tempBuscaPrograma","Banco");
					request.setAttribute("tempRetorno","ContaJudicial");
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (BancoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = ContaJudicialne.consultarDescricaoBancoJSON(stNomeBusca1, PosicaoPaginaAtual);
					try{
						
						enviarJSON(response, stTemp);
						
					}catch(Exception e) {}
					return;
				}
					break;
				case (ComarcaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Comarca"};
					String[] lisDescricao = {"Comarca"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_Comarca");
					request.setAttribute("tempBuscaDescricao","Comarca");
					request.setAttribute("tempBuscaPrograma","Comarca");
					request.setAttribute("tempRetorno","ContaJudicial");
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (ComarcaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = ContaJudicialne.consultarDescricaoComarcaJSON(stNomeBusca1, PosicaoPaginaAtual);
					try{
						
						enviarJSON(response, stTemp);
						
					}catch(Exception e) {}
					return;
				}
					break;
				case (ServentiaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Serventia"};
					String[] lisDescricao = {"Serventia"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_Serv");
					request.setAttribute("tempBuscaDescricao","Serv");
					request.setAttribute("tempBuscaPrograma","Serventia");
					request.setAttribute("tempRetorno","ContaJudicial");
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (ServentiaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = ContaJudicialne.consultarDescricaoServentiaJSON(stNomeBusca1, PosicaoPaginaAtual);
					try{
						
						enviarJSON(response, stTemp);
						
					}catch(Exception e) {}
					return;
				}
					break;
//--------------------------------------------------------------------------------//
			default:
				stId = request.getParameter("Id_ContaJudicial");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( ContaJudicialdt.getId()))){
						ContaJudicialdt.limpar();
						ContaJudicialdt = ContaJudicialne.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("ContaJudicialdt",ContaJudicialdt );
		request.getSession().setAttribute("ContaJudicialne",ContaJudicialne );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
