package br.gov.go.tj.projudi.ct;

 import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.BairroDt;
import br.gov.go.tj.projudi.dt.ObjetoSubtipoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoObjetoArquivoDt;
import br.gov.go.tj.projudi.dt.RgOrgaoExpedidorDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.ne.ProcessoObjetoArquivoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class ProcessoObjetoArquivoCtGen extends Controle { 


	/**
	 * 
	 */
	private static final long serialVersionUID = -1400562723698208988L;

	public  ProcessoObjetoArquivoCtGen() { 

	} 
		public int Permissao(){
			return ProcessoObjetoArquivoDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ProcessoObjetoArquivoDt ProcessoObjetoArquivodt;
		ProcessoObjetoArquivoNe ProcessoObjetoArquivone;


		String stNomeBusca1="";
		String stNomeBusca2="";
		String stNomeBusca3="";
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/ProcessoObjetoArquivo.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","ProcessoObjetoArquivo");




		ProcessoObjetoArquivone =(ProcessoObjetoArquivoNe)request.getSession().getAttribute("ProcessoObjetoArquivone");
		if (ProcessoObjetoArquivone == null )  ProcessoObjetoArquivone = new ProcessoObjetoArquivoNe();  


		ProcessoObjetoArquivodt =(ProcessoObjetoArquivoDt)request.getSession().getAttribute("ProcessoObjetoArquivodt");
		if (ProcessoObjetoArquivodt == null )  ProcessoObjetoArquivodt = new ProcessoObjetoArquivoDt();  

		ProcessoObjetoArquivodt.setProcObjetoArq( request.getParameter("ProcObjetoArq")); 
		ProcessoObjetoArquivodt.setId_ObjetoSubtipo( request.getParameter("Id_ObjetoSubtipo")); 
		ProcessoObjetoArquivodt.setObjetoSubtipo( request.getParameter("ObjetoSubtipo")); 
		ProcessoObjetoArquivodt.setNomeDepositante( request.getParameter("NomeDepositante")); 
		ProcessoObjetoArquivodt.setId_Delegacia( request.getParameter("Id_Delegacia")); 
		ProcessoObjetoArquivodt.setDelegacia( request.getParameter("Delegacia")); 
		ProcessoObjetoArquivodt.setInquerito( request.getParameter("Inquerito")); 
		ProcessoObjetoArquivodt.setId_Processo( request.getParameter("Id_Processo")); 
		ProcessoObjetoArquivodt.setProcNumero( request.getParameter("Processo")); 
		ProcessoObjetoArquivodt.setCodigoLote( request.getParameter("CodigoLote")); 
		ProcessoObjetoArquivodt.setPlaca( request.getParameter("Placa")); 
		ProcessoObjetoArquivodt.setChassi( request.getParameter("Chassi")); 
		ProcessoObjetoArquivodt.setId_ServArquivo( UsuarioSessao.getId_Serventia()); 
		ProcessoObjetoArquivodt.setServentiaarquivo( UsuarioSessao.getServentia()); 
		ProcessoObjetoArquivodt.setModulo( request.getParameter("Modulo")); 
		ProcessoObjetoArquivodt.setPerfil( request.getParameter("Perfil")); 
		ProcessoObjetoArquivodt.setNivel( request.getParameter("Nivel")); 
		ProcessoObjetoArquivodt.setUnidade( request.getParameter("Unidade")); 
		ProcessoObjetoArquivodt.setLeilao( request.getParameter("Leilao")); 
		ProcessoObjetoArquivodt.setStatusLeilao( request.getParameter("StatusLeilao")); 
		ProcessoObjetoArquivodt.setNumeroRegistro( request.getParameter("NumeroRegistro")); 
		ProcessoObjetoArquivodt.setDataEntrada( request.getParameter("DataEntrada")); 
		ProcessoObjetoArquivodt.setDataBaixa( request.getParameter("DataBaixa"));
		ProcessoObjetoArquivodt.setStatusBaixa( request.getParameter("StatusBaixa")); 
		ProcessoObjetoArquivodt.setNomeRecebedor( request.getParameter("NomeRecebedor")); 
		ProcessoObjetoArquivodt.setCpfRecebedor( request.getParameter("CpfRecebedor")); 
		ProcessoObjetoArquivodt.setRgRecebedor( request.getParameter("RgRecebedor")); 
		ProcessoObjetoArquivodt.setId_RgOrgaoExpRecebedor( request.getParameter("Id_Rgorgaoexpedidor")); 
		ProcessoObjetoArquivodt.setRgOrgaoExp( request.getParameter("Rgorgaoexpedidor"));
		
		ProcessoObjetoArquivodt.setLogradouro( request.getParameter("Logradouro")); 
		ProcessoObjetoArquivodt.setNumero( request.getParameter("Numero")); 
		ProcessoObjetoArquivodt.setComplemento( request.getParameter("Complemento")); 
		ProcessoObjetoArquivodt.setCep( request.getParameter("Cep")); 
		ProcessoObjetoArquivodt.setBairro( request.getParameter("Bairro")); 
		ProcessoObjetoArquivodt.setId_Bairro(request.getParameter("Id_Bairro"));
		ProcessoObjetoArquivodt.setCidade( request.getParameter("Cidade")); 
		ProcessoObjetoArquivodt.setUf( request.getParameter("Uf")); 
		
		ProcessoObjetoArquivodt.setId_Id_ProcessoObjetoArquivoMovimentacao(request.getParameter("Id_ProcessoObjetoArquivoMovimentacao"));
		ProcessoObjetoArquivodt.setProcessoObjetoArquivoMovimentacao( request.getParameter("ProcessoObjetoArquivoMovimentacao"));
		ProcessoObjetoArquivodt.setDataMovimentacao(request.getParameter("DataMovimentacao"));
		ProcessoObjetoArquivodt.setDataRetorno(request.getParameter("DataRetorno"));
		
		
		ProcessoObjetoArquivodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		ProcessoObjetoArquivodt.setIpComputadorLog(request.getRemoteAddr());


		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");
		if(request.getParameter("nomeBusca3") != null) stNomeBusca3 = request.getParameter("nomeBusca3");
		
		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					ProcessoObjetoArquivone.excluir(ProcessoObjetoArquivodt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Descrição do Objeto Arquivado"};
					String[] lisDescricao = {"ProcessoObjetoArquivo"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_ProcObjetoArq");
					request.setAttribute("tempBuscaDescricao","ProcessoObjetoArquivo");
					request.setAttribute("tempBuscaPrograma","Objeto Arquivo");
					request.setAttribute("tempRetorno","ProcessoObjetoArquivo");
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = ProcessoObjetoArquivone.consultarDescricaoJSON(stNomeBusca1, stNomeBusca2, stNomeBusca3, UsuarioSessao.getId_Serventia(), PosicaoPaginaAtual);
					try {
						response.setContentType("text/x-json");
						response.getOutputStream().write(stTemp.getBytes());
						response.flushBuffer();
					}catch(Exception e) { }
					return;
				}
				break;
			case Configuracao.Novo: 
				ProcessoObjetoArquivodt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=ProcessoObjetoArquivone.verificar(ProcessoObjetoArquivodt); 
					if (Mensagem.length()==0){
						ProcessoObjetoArquivone.salvar(ProcessoObjetoArquivodt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	{
						request.setAttribute("MensagemErro", Mensagem );
					}
				break;
			case (ObjetoSubtipoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"ObjetoSubtipo"};
					String[] lisDescricao = {"ObjetoSubtipo"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_ObjetoSubtipo");
					request.setAttribute("tempBuscaDescricao","ObjetoSubtipo");
					request.setAttribute("tempBuscaPrograma","ObjetoSubtipo");
					request.setAttribute("tempRetorno","ProcessoObjetoArquivo");
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (ObjetoSubtipoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = ProcessoObjetoArquivone.consultarDescricaoObjetoSubtipoJSON(stNomeBusca1, PosicaoPaginaAtual);
					try {
						response.setContentType("text/x-json");
						response.getOutputStream().write(stTemp.getBytes());
						response.flushBuffer();
					}catch(Exception e) { }
					return;
				}
					break;
			case (ServentiaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Serventia"};
					String[] lisDescricao = {"Serventia"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_Delegacia");
					request.setAttribute("tempBuscaDescricao","Delegacia");
					request.setAttribute("tempBuscaPrograma","Serventia");
					request.setAttribute("tempRetorno","ProcessoObjetoArquivo");
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (ServentiaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = ProcessoObjetoArquivone.consultarDelegaciasAtivasJSON(stNomeBusca1, PosicaoPaginaAtual);
					try {
						response.setContentType("text/x-json");
						response.getOutputStream().write(stTemp.getBytes());
						response.flushBuffer();
					}catch(Exception e) { }
					return;
				}
					break;
			case (ProcessoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Processo"};
					String[] lisDescricao = {"Processo"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_Proc");
					request.setAttribute("tempBuscaDescricao","ProcNumero");
					request.setAttribute("tempBuscaPrograma","Processo");
					request.setAttribute("tempRetorno","ProcessoObjetoArquivo");
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (ProcessoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = ProcessoObjetoArquivone.consultarDescricaoProcessoJSON(stNomeBusca1, PosicaoPaginaAtual);
					try {
						response.setContentType("text/x-json");
						response.getOutputStream().write(stTemp.getBytes());
						response.flushBuffer();
					}catch(Exception e) { }
					return;
				}
					break;

			case (RgOrgaoExpedidorDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"RgOrgaoExpedidor"};
					String[] lisDescricao = {"RgOrgaoExpedidor"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_RgOrgaoExpRecebedor");
					request.setAttribute("tempBuscaDescricao","RgOrgaoExp");
					request.setAttribute("tempBuscaPrograma","RgOrgaoExpedidor");
					request.setAttribute("tempRetorno","ProcessoObjetoArquivo");
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (RgOrgaoExpedidorDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = ProcessoObjetoArquivone.consultarDescricaoRgOrgaoExpedidorJSON(stNomeBusca1, stNomeBusca2, PosicaoPaginaAtual);
					try {
						response.setContentType("text/x-json");
						response.getOutputStream().write(stTemp.getBytes());
						response.flushBuffer();
					}catch(Exception e) { }
					return;
				}
				break;
			case (BairroDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Bairro","Cidade","Uf"};
					String[] lisDescricao = {"Bairro","Cidade","Uf"};
					String[] camposHidden = {"Cidade","Uf"};
					request.setAttribute("camposHidden",camposHidden);
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_Bairro");
					request.setAttribute("tempBuscaDescricao", "Bairro");
					request.setAttribute("tempBuscaPrograma", "Bairro");
					request.setAttribute("tempRetorno", "ProcessoObjetoArquivo");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (BairroDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else{
					String stTemp = "";
					stTemp = ProcessoObjetoArquivone.consultarDescricaoBairroJSON(stNomeBusca1, stNomeBusca2, stNomeBusca3, PosicaoPaginaAtual);
					enviarJSON(response, stTemp);
					return;
				}
				break;

//--------------------------------------------------------------------------------//
			default:
				stId = request.getParameter("Id_ProcObjetoArq");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( ProcessoObjetoArquivodt.getId()))){
						ProcessoObjetoArquivodt.limpar();
						ProcessoObjetoArquivodt = ProcessoObjetoArquivone.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("ProcessoObjetoArquivodt",ProcessoObjetoArquivodt );
		request.getSession().setAttribute("ProcessoObjetoArquivone",ProcessoObjetoArquivone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
