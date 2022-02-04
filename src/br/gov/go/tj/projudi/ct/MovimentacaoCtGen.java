package br.gov.go.tj.projudi.ct;

 import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.MovimentacaoDt;
import br.gov.go.tj.projudi.dt.MovimentacaoTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoPrioridadeDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaDt;
import br.gov.go.tj.projudi.ne.MovimentacaoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class MovimentacaoCtGen extends Controle {


	/**
     * 
     */
    private static final long serialVersionUID = -3440133637472693204L;

    public  MovimentacaoCtGen() {

	} 
		public int Permissao(){
			return MovimentacaoDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		MovimentacaoDt Movimentacaodt;
		MovimentacaoNe Movimentacaone;


		List tempList=null; 
		String Mensagem="";
		String stId="";
		int passoBusca=0;

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","Movimentacao");
		request.setAttribute("tempBuscaId_Movimentacao","Id_Movimentacao");
		request.setAttribute("tempBuscaMovimentacao","Movimentacao");
		request.setAttribute("tempBuscaId_MovimentacaoTipo",request.getParameter("tempBuscaId_MovimentacaoTipo"));
		request.setAttribute("tempBuscaMovimentacaoTipo",request.getParameter("tempBuscaMovimentacaoTipo"));
		request.setAttribute("tempBuscaId_Processo",request.getParameter("tempBuscaId_Processo"));
		request.setAttribute("tempBuscaProcesso",request.getParameter("tempBuscaProcesso"));
		request.setAttribute("tempBuscaId_UsuarioRealizador",request.getParameter("tempBuscaId_UsuarioRealizador"));
		request.setAttribute("tempBuscaUsuarioServentia",request.getParameter("tempBuscaUsuarioServentia"));
		request.setAttribute("tempBuscaId_ProcessoPrioridade",request.getParameter("tempBuscaId_ProcessoPrioridade"));
		request.setAttribute("tempBuscaProcessoPrioridade",request.getParameter("tempBuscaProcessoPrioridade"));



		Movimentacaone =(MovimentacaoNe)request.getSession().getAttribute("Movimentacaone");
		if (Movimentacaone == null )  Movimentacaone = new MovimentacaoNe();  


		Movimentacaodt =(MovimentacaoDt)request.getSession().getAttribute("Movimentacaodt");
		if (Movimentacaodt == null )  Movimentacaodt = new MovimentacaoDt();  

		/** Varíavel passoBusca: usada na consulta para possibilitar limpar campo resultante de busca */
		if (request.getParameter("PassoBusca") != null && !request.getParameter("PassoBusca").equals("null")) passoBusca = Funcoes.StringToInt((String) request.getParameter("PassoBusca"));
		Movimentacaodt.setMovimentacao( request.getParameter("Movimentacao")); 

		Movimentacaodt.setId_MovimentacaoTipo( request.getParameter("Id_MovimentacaoTipo")); 

		Movimentacaodt.setMovimentacaoTipo( request.getParameter("MovimentacaoTipo")); 

		Movimentacaodt.setId_Processo( request.getParameter("Id_Processo")); 

		Movimentacaodt.setProcessoNumero( request.getParameter("ProcessoNumero")); 

		Movimentacaodt.setId_UsuarioRealizador( request.getParameter("Id_UsuarioRealizador")); 

		Movimentacaodt.setUsuarioRealizador( request.getParameter("UsuarioRealizador")); 

		Movimentacaodt.setComplemento( request.getParameter("Complemento")); 

		Movimentacaodt.setId_ProcessoPrioridade( request.getParameter("Id_ProcessoPrioridade")); 

		Movimentacaodt.setProcessoPrioridade( request.getParameter("ProcessoPrioridade")); 

		Movimentacaodt.setDataRealizacao( request.getParameter("DataRealizacao")); 

		Movimentacaodt.setPalavraChave( request.getParameter("PalavraChave")); 

		Movimentacaodt.setMovimentacaoTipoCodigo( request.getParameter("MovimentacaoTipoCodigo")); 

		Movimentacaodt.setProcessoPrioridadeCodigo( request.getParameter("ProcessoPrioridadeCodigo")); 


		Movimentacaodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		Movimentacaodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				Movimentacaone.excluir(Movimentacaodt); 
				request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				tempList =Movimentacaone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
				if (tempList.size()>0){
					request.setAttribute("ListaMovimentacao", tempList); 
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
					request.setAttribute("QuantidadePaginas", Movimentacaone.getQuantidadePaginas());
					Movimentacaodt.limpar();
				}else{ 
					request.setAttribute("MensagemErro", "Dados Não Localizados"); 
					request.setAttribute("PaginaAtual", Configuracao.Editar);
				}
				break;
			case Configuracao.LocalizarDWR: 
				break;
			case Configuracao.Novo: 
				Movimentacaodt.limpar();
				request.setAttribute("PaginaAtual",Configuracao.Editar);
				break;
			case Configuracao.SalvarResultado: 
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				Mensagem=Movimentacaone.Verificar(Movimentacaodt); 
				if (Mensagem.length()==0){
					Movimentacaone.salvar(Movimentacaodt); 
					request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
				}else	request.setAttribute("MensagemErro", Mensagem );
				break;
				case (MovimentacaoTipoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
						tempList =Movimentacaone.consultarDescricaoMovimentacaoTipo(tempNomeBusca, PosicaoPaginaAtual);
						if (tempList.size()>0){
							request.setAttribute("ListaMovimentacaoTipo", tempList); 
							request.setAttribute("PaginaAtual", paginaatual);
							request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
							request.setAttribute("QuantidadePaginas", Movimentacaone.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
				case (ProcessoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
						tempList =Movimentacaone.consultarDescricaoProcesso(tempNomeBusca, PosicaoPaginaAtual);
						if (tempList.size()>0){
							request.setAttribute("ListaProcesso", tempList); 
							request.setAttribute("PaginaAtual", paginaatual);
							request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
							request.setAttribute("QuantidadePaginas", Movimentacaone.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
				case (UsuarioServentiaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
						tempList =Movimentacaone.consultarDescricaoUsuarioServentia(tempNomeBusca, PosicaoPaginaAtual);
						if (tempList.size()>0){
							request.setAttribute("ListaUsuarioServentia", tempList); 
							request.setAttribute("PaginaAtual", paginaatual);
							request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
							request.setAttribute("QuantidadePaginas", Movimentacaone.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
				case (ProcessoPrioridadeDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					if (passoBusca == 1) {
						Movimentacaodt.setId_ProcessoPrioridade("");
						Movimentacaodt.setProcessoPrioridade("");
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}else {
						tempList =Movimentacaone.consultarDescricaoProcessoPrioridade(tempNomeBusca, PosicaoPaginaAtual);
						if (tempList.size()>0){
							request.setAttribute("ListaProcessoPrioridade", tempList); 
							request.setAttribute("PaginaAtual", paginaatual);
							request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
							request.setAttribute("QuantidadePaginas", Movimentacaone.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
				}
					break;
//--------------------------------------------------------------------------------//
			default:
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				stId = request.getParameter("Id_Movimentacao");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( Movimentacaodt.getId()))){
						Movimentacaodt.limpar();
						Movimentacaodt = Movimentacaone.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("Movimentacaodt",Movimentacaodt );
		request.getSession().setAttribute("Movimentacaone",Movimentacaone );

		RequestDispatcher dis =	request.getRequestDispatcher("/WEB-INF/jsptjgo/Movimentacao.jsp");
		dis.include(request, response);
	}
}
