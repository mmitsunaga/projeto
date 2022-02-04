package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ArquivoTipoDt;
import br.gov.go.tj.projudi.dt.EnderecoDt;
import br.gov.go.tj.projudi.dt.GrupoTipoDt;
import br.gov.go.tj.projudi.dt.MandadoPrisaoDt;
import br.gov.go.tj.projudi.dt.MandadoPrisaoOrigemDt;
import br.gov.go.tj.projudi.dt.MandadoPrisaoStatusDt;
import br.gov.go.tj.projudi.dt.ModeloDt;
import br.gov.go.tj.projudi.dt.PrisaoTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoAssuntoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteDt;
import br.gov.go.tj.projudi.dt.ProcessoPartePrisaoDt;
import br.gov.go.tj.projudi.dt.RegimeExecucaoDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.ne.MandadoPrisaoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

//---------------------------------------------------------
public class MandadoPrisaoCt extends MandadoPrisaoCtGen{

	private static final long serialVersionUID = 1847289560058179477L;

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		MandadoPrisaoDt mandadoPrisaoDt;
		MandadoPrisaoNe mandadoPrisaoNe;
		ProcessoDt processoDt = null;
		String mensagem="";
				
		String stNomeBusca1 = "";
		String stNomeBusca2 = "";

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");
		
		int fluxo = -1;
		boolean isJuiz = false;
		boolean isServentia = false;
		String stAcao="";
		int paginaAnterior = 0;
		Map mapArquivos = null;
		List listaArquivos = null;
		
		if (UsuarioSessao.getUsuarioDt().getGrupoTipoCodigo().equals(String.valueOf(GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU))){
			isJuiz = true;
			request.setAttribute("usuarioTipo", "juiz");
		} else {
			isServentia = true;
			request.setAttribute("usuarioTipo", "serventia");
		}

		List listaMandadoPrisao = null;
		String posicaoLista = request.getParameter("posicaoLista");
		
		request.setAttribute("tempPrograma","MandadoPrisao");

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");

		if (request.getParameter("tempFluxo1") != null && !request.getParameter("tempFluxo1").equalsIgnoreCase("null") && request.getParameter("tempFluxo1").length() > 0) 
			fluxo = Funcoes.StringToInt(request.getParameter("tempFluxo1"));
		
		if (request.getParameter("tempFluxo2") != null) paginaAnterior = Funcoes.StringToInt(request.getParameter("tempFluxo2"));
		
		mandadoPrisaoNe =(MandadoPrisaoNe)request.getSession().getAttribute("MandadoPrisaone");
		if (mandadoPrisaoNe == null )  mandadoPrisaoNe = new MandadoPrisaoNe();  

		mandadoPrisaoDt =(MandadoPrisaoDt)request.getSession().getAttribute("MandadoPrisaodt");
		if (mandadoPrisaoDt == null )  mandadoPrisaoDt = new MandadoPrisaoDt();  

		if (request.getSession().getAttribute("processoDt") != null)
			processoDt = (ProcessoDt) request.getSession().getAttribute("processoDt");
		
		if (request.getSession().getAttribute("ListaMandadoPrisao") != null)
			listaMandadoPrisao = (List) request.getSession().getAttribute("ListaMandadoPrisao");
		
		if (request.getParameter("nomeArquivo")!= null ){
			request.setAttribute("nomeArquivo", request.getParameter("nomeArquivo"));
		}
					
		//seta os dados do obejto Mandado de Prisão
//        switch (paginaatual){
//        	case Configuracao.Salvar:
//        	case Configuracao.SalvarResultado:
//        	case Configuracao.Curinga6:
//        	case Configuracao.Curinga8:
//        	case Configuracao.Excluir:
//        	case Configuracao.Editar:
        		setDadosMandadoPrisao(mandadoPrisaoDt, request, processoDt, UsuarioSessao.getId_Usuario(), mandadoPrisaoNe, paginaatual);
//        		break;
//        }
		//parâmetros para inclusão de arquivos.
        setParametrosAuxiliares(mandadoPrisaoDt, paginaAnterior, paginaatual, request, mandadoPrisaoNe, UsuarioSessao, processoDt);
        		
		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual",Configuracao.Editar);
		request.setAttribute("displayOrigem", " display: none");
		request.setAttribute("displayPrisaoTipo", " display: none"); //request.getParameter("Id_PrisaoTipo");

		switch (paginaatual) {
		
			case Configuracao.Excluir:
				stAcao="/WEB-INF/jsptjgo/MandadoPrisaoEditar.jsp";
				request.setAttribute("__Pedido__", UsuarioSessao.getPedido());
				break;
			
			case Configuracao.ExcluirResultado: //Excluir
				mensagem = mandadoPrisaoNe.verificarExcluir(mandadoPrisaoDt);
				if (mensagem.length() == 0){
					mandadoPrisaoNe.excluir(mandadoPrisaoDt);
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
					redireciona(response, "MandadoPrisao?PaginaAtual="+Configuracao.Localizar);
				} else {
					request.setAttribute("MensagemErro", mensagem);
				}
				stAcao="/WEB-INF/jsptjgo/MandadoPrisaoEditar.jsp";
				break;
			
			case Configuracao.Imprimir: 
				if (mandadoPrisaoDt.getDataImpressao().length() == 0){
					mandadoPrisaoNe.imprimirMandadoPrisao(mandadoPrisaoDt, UsuarioSessao.getUsuarioDt().getId_UsuarioServentia());
					request.getSession().setAttribute("MandadoPrisaodt",mandadoPrisaoDt );
				}
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				//atributo necessário para a jsp direcionar para a opção de impressão 
				request.setAttribute("Imprimir", "true");
				if (mandadoPrisaoDt.getMandadoPrisaoOrigemCodigo().equals(String.valueOf(MandadoPrisaoOrigemDt.OUTRO))){
					request.setAttribute("displayOrigem", "display: block");
				} else request.setAttribute("displayOrigem", " display: none");
				
				if (mandadoPrisaoDt.getPrisaoTipoCodigo().equals(String.valueOf(PrisaoTipoDt.TEMPORARIA))){
					request.setAttribute("displayPrisaoTipo", "display: block");
				} else request.setAttribute("displayPrisaoTipo", " display: none");
				stAcao="/WEB-INF/jsptjgo/MandadoPrisaoEditar.jsp";
				break;
				
			case Configuracao.Localizar: //localizar
				boolean todos = false;
				if (isJuiz) todos = true;
				listaMandadoPrisao = mandadoPrisaoNe.listarMandadoPrisaoProcesso(processoDt.getId(), todos);
				stAcao="/WEB-INF/jsptjgo/MandadoPrisaoProcesso.jsp";
				break;
				
			case Configuracao.Novo: 
				//altera dados do mandado
				if (posicaoLista != null && !posicaoLista.equalsIgnoreCase("null") && posicaoLista.length() > 0){
					mandadoPrisaoDt = null;
					mandadoPrisaoDt = (MandadoPrisaoDt)listaMandadoPrisao.get(Funcoes.StringToInt(posicaoLista));
					if (mandadoPrisaoDt.getMandadoPrisaoOrigemCodigo().equals(String.valueOf(MandadoPrisaoOrigemDt.OUTRO))){
						request.setAttribute("displayOrigem", "display: block");
					} else request.setAttribute("displayOrigem", " display: none");
					
					if (mandadoPrisaoDt.getPrisaoTipoCodigo().equals(String.valueOf(PrisaoTipoDt.TEMPORARIA))){
						request.setAttribute("displayPrisaoTipo", "display: block");
					} else request.setAttribute("displayPrisaoTipo", " display: none");
					
					if (processoDt.getId() == null || processoDt.getId().isEmpty()){
						processoDt = mandadoPrisaoNe.consultarDadosProcesso(mandadoPrisaoDt.getId_Processo());
						request.getSession().setAttribute("processoDt", processoDt);
					}
					setDadosProcesso(mandadoPrisaoDt, processoDt);
						
				//novo mandado
				} else {
					limparMandadoPrisaoDt(mandadoPrisaoDt, processoDt);
					if (isServentia) mandadoPrisaoDt.setSigilo("false");
				}
				mandadoPrisaoDt.setListaMandadoPrisaoOrigem(mandadoPrisaoNe.listarMandadoPrisaoOrigem());
				mandadoPrisaoDt.setListaPrisaoTipo(mandadoPrisaoNe.listarPrisaoTipo());
				mandadoPrisaoDt.setListaRegime(mandadoPrisaoNe.listarRegime());
				stAcao="/WEB-INF/jsptjgo/MandadoPrisaoEditar.jsp";
				break;
				
//			case Configuracao.Salvar:
//				mensagem = mandadoPrisaoNe.Verificar(mandadoPrisaoDt, isJuiz); 
//				if (mensagem.length()>0){
//					request.setAttribute("MensagemErro", mensagem );
//					request.setAttribute("PaginaAnterior", String.valueOf(Configuracao.Novo));
//				}
//				stAcao="/WEB-INF/jsptjgo/MandadoPrisaoEditar.jsp";
//				request.setAttribute("__Pedido__", UsuarioSessao.getPedido());
//				break;
//				
//			case Configuracao.SalvarResultado: 
//				mensagem = mandadoPrisaoNe.Verificar(mandadoPrisaoDt, isJuiz); 
//				if (mensagem.length()==0){
//					mandadoPrisaoNe.salvarMandadoPrisao(mandadoPrisaoDt); 
//					request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
//				}else	request.setAttribute("MensagemErro", mensagem );
//				stAcao="/WEB-INF/jsptjgo/MandadoPrisaoEditar.jsp";
//				break;
				
			//SERVENTIA:
			//fluxo 1: Emitir mandado de prisão
			//fluxo 2: listar mandados de prisão aguardando impressão (página inicial)
			case Configuracao.Curinga6:
				List lista = null;
				switch(fluxo){
					case 1: //Emitir mandado de prisão
						if (mandadoPrisaoDt.getMandadoPrisaoStatusCodigo() != null && !mandadoPrisaoDt.getMandadoPrisaoStatusCodigo().isEmpty()){
							request.setAttribute("MensagemErro", "Não é possível emitir o mandado de prisão. (Motivo: Mandado " + mandadoPrisaoDt.getMandadoPrisaoStatus() + ")"); 
						} else {
							mensagem = mandadoPrisaoNe.Verificar(mandadoPrisaoDt, isJuiz); 
							if (mensagem.length()==0){
								mandadoPrisaoNe.emitirMandadoPrisao(mandadoPrisaoDt, UsuarioSessao.getUsuarioDt().getId_UsuarioServentia());
								request.setAttribute("MensagemOk", "Mandado de Prisão Emitido!"); 
							}else	request.setAttribute("MensagemErro", mensagem );
						}
						stAcao="/WEB-INF/jsptjgo/MandadoPrisaoEditar.jsp";
						break;
						
					case 2: //listar mandados de prisão aguardando impressão (página inicial)
						lista = new ArrayList();
						lista.add(String.valueOf(MandadoPrisaoStatusDt.EXPEDIDO));
						listaMandadoPrisao = mandadoPrisaoNe.listarMandadoPrisaoServentia(UsuarioSessao.getUsuarioDt().getId_Serventia(), lista, false);
						request.setAttribute("tempLegenda", "Mandado de Prisão aguardando Impressão");
						stAcao="/WEB-INF/jsptjgo/MandadoPrisaoPendencia.jsp";
						break;
						
					case 3: //listar mandados de prisão aguardando cumprimento (página inicial)
						lista = new ArrayList();
						lista.add(String.valueOf(MandadoPrisaoStatusDt.EXPEDIDO));
						lista.add(String.valueOf(MandadoPrisaoStatusDt.IMPRESSO));
						listaMandadoPrisao = mandadoPrisaoNe.listarMandadoPrisaoServentia(UsuarioSessao.getUsuarioDt().getId_Serventia(), lista, false);
						request.setAttribute("tempLegenda", "Mandado de Prisão aguardando Cumprimento");
						stAcao="/WEB-INF/jsptjgo/MandadoPrisaoPendencia.jsp";
						break;
				}
				break;

				
			//JUIZ:
			//fluxo 1: lista mandados de prisão para Expedir (página inicial)
			//fluxo 2: lista mandado de prisão expedido (página inicial)
			//fluxo 3: lista mandado de prisão impresso (página inicial)
			//fluxo 4: Expedir manadado: assinar mandado de prisão a ser expedido 
			case Configuracao.Curinga7:
				List list = null;
				switch(fluxo){
					case 1: //lista mandados de prisão para Expedir (página inicial)
						listaMandadoPrisao = mandadoPrisaoNe.listarMandadoPrisaoEmitidoServentiaCargo(UsuarioSessao.getUsuarioDt().getId_ServentiaCargo());
						request.setAttribute("tempLegenda", "Mandado de Prisão para Expedir");
						stAcao="/WEB-INF/jsptjgo/MandadoPrisaoPendencia.jsp";
						break;
						
					case 2: //lista mandado de prisão expedido (página inicial)
						list = new ArrayList();
						list.add(String.valueOf(MandadoPrisaoStatusDt.EXPEDIDO));
						listaMandadoPrisao = mandadoPrisaoNe.listarMandadoPrisaoServentiaCargo(UsuarioSessao.getUsuarioDt().getId_ServentiaCargo(), list);
						request.setAttribute("tempLegenda", "Mandado de Prisão aguardando Impressão");
						stAcao="/WEB-INF/jsptjgo/MandadoPrisaoPendencia.jsp";
						break;
						
					case 3: //lista mandado de prisão impresso (página inicial)
						list = new ArrayList();
						list.add(String.valueOf(MandadoPrisaoStatusDt.EXPEDIDO));
						list.add( String.valueOf(MandadoPrisaoStatusDt.IMPRESSO));
						listaMandadoPrisao = mandadoPrisaoNe.listarMandadoPrisaoServentiaCargo(UsuarioSessao.getUsuarioDt().getId_ServentiaCargo(), list);
						request.setAttribute("tempLegenda", "Mandado de Prisão aguardando Cumprimento");
						stAcao="/WEB-INF/jsptjgo/MandadoPrisaoPendencia.jsp";
						break;
						
					case 4: //Expedir manadado: assinar mandado de prisão a ser expedido
						mensagem = mandadoPrisaoNe.Verificar(mandadoPrisaoDt, isJuiz);
						stAcao="/WEB-INF/jsptjgo/MandadoPrisaoEditar.jsp";
						if (mensagem.length()==0){
							//gera número do mandado de prisão
							if (mandadoPrisaoDt.getMandadoPrisaoNumero().length() == 0)
								mandadoPrisaoDt.setMandadoPrisaoNumero(mandadoPrisaoNe.gerarNumeroMandadoPrisao(mandadoPrisaoDt.getId_Processo()));
							mandadoPrisaoNe.salvarMandadoPrisao(mandadoPrisaoDt); 
							String texto = gerarModeloMandadoPrisao(mandadoPrisaoDt, mandadoPrisaoNe, UsuarioSessao.getUsuarioDt());
							if (texto.length() == 0){
								request.setAttribute("MensagemErro", "Modelo de geração do mandado não cadastrado!");
							} else{
								stAcao="/WEB-INF/jsptjgo/MandadoPrisaoAssinar.jsp";
							}
							String idArquivoTipo = mandadoPrisaoNe.consultarIdArquivoTipo(String.valueOf(ArquivoTipoDt.MANDADO_PRISAO));
							request.setAttribute("Id_ArquivoTipo", idArquivoTipo);
							request.setAttribute("ArquivoTipo", "Mandado de Prisão");
							//crio um idetificador unico na session para salvar o arquivo
							String id_arquivo_session = "arq" + System.currentTimeMillis(); 
							request.setAttribute("textoEditor", texto);
							request.setAttribute("id_arquivo_session", id_arquivo_session);
							request.getSession().setAttribute(id_arquivo_session,texto );
							
							if (request.getParameter("nomeArquivo")!= null ){
								request.setAttribute("nomeArquivo", request.getParameter("nomeArquivo"));
							} 
							mandadoPrisaoDt.setIdArquivoTipo(idArquivoTipo);
							mandadoPrisaoDt.setArquivoTipo("Mandado de Prisão");
							mandadoPrisaoDt.setTextoEditor(texto);
							request.getSession().setAttribute("MandadoPrisaodt",mandadoPrisaoDt );
						}else {
							request.setAttribute("MensagemErro", mensagem );
						}
						break;
				}
				break;

			// Menu "Cumprimento"
			case Configuracao.Curinga8:
				if (mandadoPrisaoDt.getListaMandadoPrisaoStatus() == null){
					mandadoPrisaoDt.setListaMandadoPrisaoStatus(mandadoPrisaoNe.listarMandadoPrisaoStatus());
					mandadoPrisaoDt.setListaPrisaoTipo(mandadoPrisaoNe.listarPrisaoTipo());
				}
				
				switch(fluxo){
					case 1: //tela inicial
						listaMandadoPrisao = null;
						listaMandadoPrisao = new ArrayList();
						request.setAttribute("tempBuscaPrograma", "MandadoPrisao");
						request.setAttribute("tempRetorno", "MandadoPrisao");
						request.setAttribute("tempBuscaId", "Id_MandadoPrisao");
						request.setAttribute("tempFluxo1", "2");
						request.setAttribute("PaginaAtual", (Configuracao.Curinga8));
						request.setAttribute("PosicaoPaginaAtual", "0");
						request.setAttribute("QuantidadePaginas", "0");
						stAcao="/WEB-INF/jsptjgo/MandadoPrisaoCumprimento.jsp";
						break;
						
					case 2: //consulta
						String numeroProcesso = request.getParameter("nomeBusca1");
						String dataInicial = request.getParameter("nomeBusca2");
						String dataFinal = request.getParameter("nomeBusca3");
						String mandadoPrisaoStatusCodigo = request.getParameter("nomeBusca4");
						String mandadoPrisaoTipoCodigo = request.getParameter("nomeBusca5");
						boolean listarTodos = false;
						if (isJuiz) listarTodos = true;
						
						String stTemp = "";
						stTemp = mandadoPrisaoNe.listarMandadoPrisaoServentiaJSON(numeroProcesso, dataInicial, dataFinal, mandadoPrisaoStatusCodigo, mandadoPrisaoTipoCodigo, listarTodos, PosicaoPaginaAtual, UsuarioSessao.getUsuarioDt().getId_Serventia());
													
							enviarJSON(response, stTemp);
							
						
						return;
						
					case 3: //visualizar mandado de prisão
						List listaRegime = mandadoPrisaoDt.getListaRegime();
						List listaOrigem = mandadoPrisaoDt.getListaMandadoPrisaoOrigem();
						List listaPrisaoTipo = mandadoPrisaoDt.getListaPrisaoTipo();

						mandadoPrisaoDt = null;
						mandadoPrisaoDt = mandadoPrisaoNe.consultarId(request.getParameter("Id_MandadoPrisao"));
						processoDt = mandadoPrisaoNe.consultarDadosProcesso(mandadoPrisaoDt.getId_Processo());
						
						if (listaRegime == null) mandadoPrisaoDt.setListaRegime(mandadoPrisaoNe.listarRegime());
						else mandadoPrisaoDt.setListaRegime(listaRegime);
						if (listaOrigem == null) mandadoPrisaoDt.setListaMandadoPrisaoOrigem(mandadoPrisaoNe.listarMandadoPrisaoOrigem());
						else mandadoPrisaoDt.setListaMandadoPrisaoOrigem(listaOrigem);
						if (listaPrisaoTipo == null) mandadoPrisaoDt.setListaPrisaoTipo(mandadoPrisaoNe.listarPrisaoTipo());
						else mandadoPrisaoDt.setListaPrisaoTipo(listaPrisaoTipo);
						
						request.getSession().setAttribute("processoDt", processoDt);
						stAcao="/WEB-INF/jsptjgo/MandadoPrisaoEditar.jsp";
						break;
				}

				break;
				
			//finalizar mandado de prisão
			//fluxo 1: cumprir mandado de prisão
			//fluxo 2: revogar mandado de prisão
			//fluxo 3: retirar caráter sigiloso
			case Configuracao.Curinga9:
				mandadoPrisaoDt.setDataCumprimento(Funcoes.dateToStringSoData(new Date()));
				mandadoPrisaoDt.setDataPrisao(request.getParameter("DataPrisao"));
				mapArquivos = (Map) request.getSession().getAttribute("ListaArquivos");
				listaArquivos = Funcoes.converterMapParaList(mapArquivos);				
				
				switch(fluxo){
					//cumprir mandado de prisão
					case 1: 
						request.setAttribute("titulo", "Cumprir Mandado de Prisão");
						if (request.getParameter("concluir") != null && request.getParameter("concluir").equalsIgnoreCase("true")){
							mensagem = mandadoPrisaoNe.verificarCumprimento(listaArquivos, mandadoPrisaoDt);
							if (mensagem.length() > 0){
								request.setAttribute("MensagemErro", mensagem);
								stAcao="/WEB-INF/jsptjgo/MandadoPrisaoFinalizar.jsp";
//								request.setAttribute("titulo", "Cumprir Mandado de Prisão");
								request.setAttribute("descBotao", "Cumprir Mandado");
								request.setAttribute("tempFluxo1", "1"); //utilizado no retorno das consultas JSON
								break;
							} else {
								mandadoPrisaoNe.cumprir_revogar_retirarSigilo_MandadoPrisao(mandadoPrisaoDt, listaArquivos, UsuarioSessao.getUsuarioDt(), processoDt.getId_Serventia(), 1);
								
								//Limpa as listas de arquivos
								mapArquivos.clear();								
								
								redireciona(response, "BuscaProcesso?Id_Processo=" + mandadoPrisaoDt.getId_Processo() + "&MensagemOk=Mandado de Prisão cumprido com sucesso!");
							}
						
						} else {//tela inicial
							stAcao="/WEB-INF/jsptjgo/MandadoPrisaoFinalizar.jsp";
							request.setAttribute("descBotao", "Cumprir Mandado");
							request.setAttribute("tempFluxo1", "1"); //utilizado no retorno das consultas JSON				
							//Limpa as listas de arquivos
							
						}
						break;
						
					//revogar mandado de prisão
					case 2:
						request.setAttribute("titulo", "Revogar Mandado de Prisão");
						if (request.getParameter("concluir") != null && request.getParameter("concluir").equalsIgnoreCase("true")){
							if (listaArquivos == null || listaArquivos.size() == 0){
								request.setAttribute("MensagemErro", "Insira a decisão de revogação do Mandado de Prisão!");
								stAcao="/WEB-INF/jsptjgo/MandadoPrisaoFinalizar.jsp";
								request.setAttribute("descBotao", "Revogar Mandado");
								request.setAttribute("tempFluxo1", "2"); //utilizado no retorno das consultas JSON
								break;
							} else {
								mandadoPrisaoNe.cumprir_revogar_retirarSigilo_MandadoPrisao(mandadoPrisaoDt, listaArquivos, UsuarioSessao.getUsuarioDt(), processoDt.getId_Serventia(), 2);
								
								//Limpa as listas de arquivos
								mapArquivos.clear();
								
								
								redireciona(response, "BuscaProcesso?Id_Processo=" + mandadoPrisaoDt.getId_Processo() + "&MensagemOk=Mandado de Prisão revogado com sucesso!");
							}
							
						} else {//tela inicial
							stAcao="/WEB-INF/jsptjgo/MandadoPrisaoFinalizar.jsp";
							request.setAttribute("descBotao", "Revogar Mandado");
							request.setAttribute("tempFluxo1", "2"); //utilizado no retorno das consultas JSON

						}
						break;
						
					//retirar caráter sigiloso
					case 3: 
						request.setAttribute("titulo", "Retirar Caráter Sigiloso do Mandado de Prisão");
						if (request.getParameter("concluir") != null && request.getParameter("concluir").equalsIgnoreCase("true")){
							if (listaArquivos == null || listaArquivos.size() == 0){
								request.setAttribute("MensagemErro", "Insira a decisão para retirada do caráter sigiloso do Mandado de Prisão!");
								stAcao="/WEB-INF/jsptjgo/MandadoPrisaoFinalizar.jsp";
								request.setAttribute("descBotao", "Retirar Caráter Sigiloso");
								request.setAttribute("tempFluxo1", "3"); //utilizado no retorno das consultas JSON
								break;
							} else {
								mandadoPrisaoNe.cumprir_revogar_retirarSigilo_MandadoPrisao(mandadoPrisaoDt, listaArquivos, UsuarioSessao.getUsuarioDt(), processoDt.getId_Serventia(), 3);
								
								//Limpa as listas de arquivos
								mapArquivos.clear();								
								
								redireciona(response, "BuscaProcesso?Id_Processo=" + mandadoPrisaoDt.getId_Processo() + "&MensagemOk=Retirada do caráter sigiloso efetuada com sucesso!");
							}
						} else {//tela inicial
							stAcao="/WEB-INF/jsptjgo/MandadoPrisaoFinalizar.jsp";
							request.setAttribute("descBotao", "Retirar Caráter Sigiloso");
							request.setAttribute("tempFluxo1", "3"); //utilizado no retorno das consultas JSON

						}
						break;
				}
				break;
				
			// Consultar tipos de Arquivo
			case (ArquivoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					//lisNomeBusca = new ArrayList();
					//lisDescricao = new ArrayList();
					String[] lisNomeBusca = {"ArquivoTipo"};
					String[] lisDescricao = {"ArquivoTipo"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_ArquivoTipo");
					request.setAttribute("tempBuscaDescricao", "ArquivoTipo");
					request.setAttribute("tempBuscaPrograma", "ArquivoTipo");
					request.setAttribute("tempRetorno", "MandadoPrisao");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempDescricaoDescricao", "ArquivoTipo");
					request.setAttribute("tempFluxo1", request.getParameter("tempFluxo1"));
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Curinga9);
					request.setAttribute("PaginaAtual", (ArquivoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				}else{
					String stTemp = "";
					stTemp = mandadoPrisaoNe.consultarGrupoArquivoTipoJSON(UsuarioSessao.getUsuarioDt().getGrupoCodigo(), stNomeBusca1, PosicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;
				}
			break;
				
			// Consultar Modelos do Usuário
			case (ModeloDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Modelo"};
					String[] lisDescricao = {"Modelo","Serventia","Tipo de Arquivo"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_Modelo");
					request.setAttribute("tempBuscaDescricao", "Modelo");
					request.setAttribute("tempBuscaPrograma", "Modelo");
					request.setAttribute("tempRetorno", "MandadoPrisao");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempDescricaoDescricao", "Modelo");
					request.setAttribute("tempFluxo1", request.getParameter("tempFluxo1"));
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Curinga9);
					request.setAttribute("PaginaAtual", (ModeloDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				}else{
					String stTemp = "";
					request.setAttribute("tempRetorno", "MandadoPrisao");
					stTemp = mandadoPrisaoNe.consultarModeloJSON(UsuarioSessao.getUsuarioDt(), mandadoPrisaoDt.getIdArquivoTipo(), stNomeBusca1,  PosicaoPaginaAtual);
					
					enviarJSON(response, stTemp);
											
					return;
				}
			break;
				
			default:
				// fluxo 1: calcula validade do mandado de prisão - JSON
				// fluxo 2: expedir e expedir/imprimir mandado de prisão
				// fluxo 3: visualizar arquivo assinado (.html)
				// fluxo 4: direciona para o .jsp de edição do mandado
				switch (fluxo) {
				case 1:
					String stTemp = "";
					request.setAttribute("tempRetorno", "MandadoPrisao");
					stAcao="/WEB-INF/jsptjgo/MandadoPrisaoEditar.jsp";
					String tempoEmDias = Funcoes.converterParaDias(request.getParameter("tempoAno"), request.getParameter("tempoMes"), request.getParameter("tempoDia"));
					String dataNascimento = "";
					for (ProcessoParteDt parte : (List<ProcessoParteDt>)processoDt.getListaPolosPassivos()) {
						if (parte.getId().equals(request.getParameter("idProcessoParte"))){
							dataNascimento = parte.getDataNascimento();
						}
					}
					if (dataNascimento.length() == 0) request.setAttribute("MensagemErro", "Não é possível fazer o cálculo. Motivo: Não foi informada a data de nascimento do sentenciado!" );
					stTemp = mandadoPrisaoNe.calcularValidadeMandadoPrisaoJSON(tempoEmDias, dataNascimento, Funcoes.dateToStringSoData(new Date()));
					
					enviarJSON(response, stTemp);					
					
					return;
					
				//expedir mandado de prisão
				case 2:
					mapArquivos = (Map) request.getSession().getAttribute("ListaArquivos");
					listaArquivos = Funcoes.converterMapParaList(mapArquivos);
					
					if (listaArquivos == null || listaArquivos.size() == 0){
						request.setAttribute("MensagemErro", "Assine o Mandado de Prisão a ser expedido!");
						request.setAttribute("textoEditor", mandadoPrisaoDt.getTextoEditor());
						stAcao="/WEB-INF/jsptjgo/MandadoPrisaoAssinar.jsp";
						break;
					} else {
						//Limpa as listas de arquivos
						mapArquivos.clear();						
						
						mandadoPrisaoNe.expedirMandadoPrisao(mandadoPrisaoDt, listaArquivos, UsuarioSessao.getUsuarioDt());
						if (request.getParameter("expedirImprimir") != null && request.getParameter("expedirImprimir").equals("true")){
							mandadoPrisaoNe.imprimirMandadoPrisao(mandadoPrisaoDt, UsuarioSessao.getUsuarioDt().getId_UsuarioServentia());
							redireciona(response, "BuscaProcesso?Id_Processo=" + mandadoPrisaoDt.getId_Processo() + "&MensagemOk=Mandado de Prisão expedido com sucesso!&expedirImprimir=true");
						} else 
							redireciona(response, "BuscaProcesso?Id_Processo=" + mandadoPrisaoDt.getId_Processo() + "&MensagemOk=Mandado de Prisão expedido com sucesso!");
						break;
					}
					
				//visualizar arquivo assinado (.html)
				case 3:
					if (!mandadoPrisaoNe.baixarArquivo(mandadoPrisaoDt.getId(), response, UsuarioSessao.getId_Usuario(), request.getRemoteAddr())){
						//se baixou mandar para a jsp de não permitido baixar o arquivo
						RequestDispatcher dis = request.getRequestDispatcher("/WEB-INF/jsptjgo/Padroes/ArquivoNaoPermitido.jsp");
						dis.include(request, response);
					}
					return;
					
				//gera arquivo impresso (.pdf")
				case 4:
					if (request.getParameter("expedirImprimir") != null && request.getParameter("expedirImprimir").equals("true")){
						request.setAttribute("tempRetorno", "BuscaProcesso");
					}
					String stIdArquivo = mandadoPrisaoNe.consultarIdArquivo(mandadoPrisaoDt.getId());
					if (stIdArquivo != null && stIdArquivo.length() > 0) {
						if (!stIdArquivo.equalsIgnoreCase("")) {
							
							response.setContentType("application/pdf");
							// gerar pdf como arquivos da publicação
							byte[] byTemp=mandadoPrisaoNe.gerarPdfMAndadoPrissaoAtivo(ProjudiPropriedades.getInstance().getCaminhoAplicacao() ,stIdArquivo);						
							enviarPDF(response, byTemp, "MandadoPressao");

							return;
							
						}
					} 
					else {
						request.setAttribute("Mensagem", "Erro ao efetuar consulta do arquivo.");
						RequestDispatcher dis = request.getRequestDispatcher("/WEB-INF/jsptjgo/Erro.jsp");
						dis.include(request, response);
						return;
					}
				}
				break;
		}

		//controle de JSP
		if (mandadoPrisaoDt.getMandadoPrisaoStatusCodigo() != null && !mandadoPrisaoDt.getMandadoPrisaoStatusCodigo().isEmpty() && !mandadoPrisaoDt.getMandadoPrisaoStatusCodigo().equals(String.valueOf(MandadoPrisaoStatusDt.EMITIDO))){
			request.setAttribute("bloquearEdicao", "true");
		}
//		if (mandadoPrisaoDt.getMandadoPrisaoStatusCodigo() == null || mandadoPrisaoDt.getMandadoPrisaoStatusCodigo().isEmpty() || mandadoPrisaoDt.getMandadoPrisaoStatusCodigo().equals(String.valueOf(MandadoPrisaoStatusDt.EMITIDO))){
//			request.setAttribute("mostarImgSalvar", "true");
//		}
		if (isServentia){
			if (!mandadoPrisaoDt.isSigilo() &&
					(mandadoPrisaoDt.getMandadoPrisaoStatusCodigo().equals(String.valueOf(MandadoPrisaoStatusDt.EXPEDIDO)) || mandadoPrisaoDt.getMandadoPrisaoStatusCodigo().equals(String.valueOf(MandadoPrisaoStatusDt.IMPRESSO)))){
				request.setAttribute("mostarImgVisualizaMandado", "true");
				request.setAttribute("mostarImgImprimir", "true");
				request.setAttribute("mostarImgExcluir", "false");
			}
			if (mandadoPrisaoDt.getMandadoPrisaoStatusCodigo() == null || mandadoPrisaoDt.getMandadoPrisaoStatusCodigo().isEmpty()){
				request.setAttribute("mostarBotaoEmitir", "true");
				request.setAttribute("mostarImgExcluir", "true");
			}
		} 
		if (isJuiz){
			if (mandadoPrisaoDt.getMandadoPrisaoStatusCodigo().equals(String.valueOf(MandadoPrisaoStatusDt.EXPEDIDO)) || mandadoPrisaoDt.getMandadoPrisaoStatusCodigo().equals(String.valueOf(MandadoPrisaoStatusDt.IMPRESSO))){
				request.setAttribute("mostarImgVisualizaMandado", "true");
				request.setAttribute("mostarImgImprimir", "true");
				request.setAttribute("mostarImgExcluir", "false");
			}
			if (mandadoPrisaoDt.getMandadoPrisaoStatusCodigo() == null || mandadoPrisaoDt.getMandadoPrisaoStatusCodigo().isEmpty() || mandadoPrisaoDt.getMandadoPrisaoStatusCodigo().equals(String.valueOf(MandadoPrisaoStatusDt.EMITIDO))){
				request.setAttribute("mostarBotaoExpedir", "true");
				request.setAttribute("mostarImgExcluir", "true");
			}
		}
		if (mandadoPrisaoDt.getMandadoPrisaoStatusCodigo().equals(String.valueOf(MandadoPrisaoStatusDt.EXPEDIDO)) || mandadoPrisaoDt.getMandadoPrisaoStatusCodigo().equals(String.valueOf(MandadoPrisaoStatusDt.IMPRESSO))){
			request.setAttribute("mostarBotaoFinalizar", "true");
		}

		request.getSession().setAttribute("MandadoPrisaodt",mandadoPrisaoDt );
		request.getSession().setAttribute("MandadoPrisaone",mandadoPrisaoNe );
		request.getSession().setAttribute("ListaMandadoPrisao", listaMandadoPrisao); //utilizado na jsp

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
	
	public void setDadosMandadoPrisao(MandadoPrisaoDt mandadoPrisaoDt, HttpServletRequest request, ProcessoDt processoDt, String idUsuario, MandadoPrisaoNe mandadoPrisaoNe, int paginaAtual) throws Exception{
		mandadoPrisaoDt.setMandadoPrisaoNumero(request.getParameter("NumeroMandado")); 
		mandadoPrisaoDt.setDataValidade(request.getParameter("DataValidade"));
		mandadoPrisaoDt.setOrigem(request.getParameter("DescricaoOrigem")); //descrição do "Procedimento Origem", quando o MandadoPrisaoOrigem = "Outro"
		mandadoPrisaoDt.setNumeroOrigem(request.getParameter("NumeroDocumentoOrigem"));
		mandadoPrisaoDt.setMandadoPrisaoOrigemCodigo( request.getParameter("MandadoPrisaoOrigemCodigo")); //  
		mandadoPrisaoDt.setPrisaoTipoCodigo( request.getParameter("PrisaoTipoCodigo")); 
		mandadoPrisaoDt.setLocalRecolhimento(request.getParameter("LocalRecolhimento"));
		mandadoPrisaoDt.setSigilo( request.getParameter("Sigilo"));
		mandadoPrisaoDt.setPrazoPrisao( request.getParameter("PrazoPrisao")); 
		mandadoPrisaoDt.setValorFianca( request.getParameter("ValorFianca")); 
		mandadoPrisaoDt.setSinteseDecisao( request.getParameter("SinteseDecisao"));
		mandadoPrisaoDt.setId_ProcessoParte( request.getParameter("Id_ProcessoParte")); 
		mandadoPrisaoDt.setDataPrisao(request.getParameter("DataPrisao"));
//		mandadoPrisaoDt.setProcessoParte( request.getParameter("ProcessoParte"));
		
		//***codigo novo***************************************************************************************************************************************************************************
		ProcessoPartePrisaoDt processoPartePrisaoDt = new ProcessoPartePrisaoDt();  

		processoPartePrisaoDt.setId_ProcessoParte( request.getParameter("Id_ProcessoParte"));
		processoPartePrisaoDt.setNome( request.getParameter("Nome")); 
		processoPartePrisaoDt.setDataPrisao( request.getParameter("DataPrisao")); 
		
		if (request.getParameter("Id_PrisaoTipo") != null && request.getParameter("Id_PrisaoTipo").length()>0){
			processoPartePrisaoDt.setId_PrisaoTipo( request.getParameter("Id_PrisaoTipo")); 
			processoPartePrisaoDt.setPrisaoTipo( request.getParameter("PrisaoTipo"));
			
		} else if (mandadoPrisaoDt.getId_PrisaoTipo() != null && mandadoPrisaoDt.getId_PrisaoTipo().length()>0){
			processoPartePrisaoDt.setId_PrisaoTipo(mandadoPrisaoDt.getId_PrisaoTipo()); 
			processoPartePrisaoDt.setPrisaoTipo(mandadoPrisaoDt.getPrisaoTipo());
		}
		
		processoPartePrisaoDt.setId_LocalCumpPena( request.getParameter("Id_LocalCumpPena")); 
		processoPartePrisaoDt.setLocalCumpPena( request.getParameter("LocalCumpPena"));
		processoPartePrisaoDt.setPrazoPrisao( request.getParameter("PrazoPrisao")); 
		//processoPartePrisaoDt.setObservacao( request.getParameter("Observacao")); 

		processoPartePrisaoDt.setId_UsuarioLog(idUsuario);
		processoPartePrisaoDt.setIpComputadorLog(request.getRemoteAddr());
		
		mandadoPrisaoDt.setProcessoPartePrisaoDt(processoPartePrisaoDt);
		
		//***codigo novo***************************************************************************************************************************************************************************
		
		mandadoPrisaoDt.setId_RegimeExecucao( request.getParameter("Id_Regime"));
		if (mandadoPrisaoDt.getListaRegime() != null){
			for (int i=0; i<mandadoPrisaoDt.getListaRegime().size(); i++){
				if (mandadoPrisaoDt.getId_RegimeExecucao().equals(((RegimeExecucaoDt)mandadoPrisaoDt.getListaRegime().get(i)).getId()))
					mandadoPrisaoDt.setRegimeExecucao(((RegimeExecucaoDt)mandadoPrisaoDt.getListaRegime().get(i)).getRegimeExecucao());
			}
		}
		
		mandadoPrisaoDt.setId_Assunto( request.getParameter("Id_Assunto"));
		if (processoDt != null && processoDt.getListaAssuntos() != null){
			for (int i=0; i<processoDt.getListaAssuntos().size(); i++){
				if (mandadoPrisaoDt.getId_Assunto().equals(((ProcessoAssuntoDt)processoDt.getListaAssuntos().get(i)).getId()));
					mandadoPrisaoDt.setAssunto(((ProcessoAssuntoDt)processoDt.getListaAssuntos().get(i)).getAssunto());
			}
		}
		 
		mandadoPrisaoDt.setTempoPenaAno(request.getParameter("Ano"));
		mandadoPrisaoDt.setTempoPenaMes(request.getParameter("Mes"));
		mandadoPrisaoDt.setTempoPenaDia(request.getParameter("Dia"));
		String tempo = Funcoes.converterParaDias(mandadoPrisaoDt.getTempoPenaAno(), mandadoPrisaoDt.getTempoPenaMes(), mandadoPrisaoDt.getTempoPenaDia());
		if (Funcoes.StringToInt(tempo) == 0){
			mandadoPrisaoDt.setTempoPenaTotalDias("");
		} else mandadoPrisaoDt.setTempoPenaTotalDias(tempo);
		
		//seta os dados apenas uma vez
		if (processoDt != null && (mandadoPrisaoDt.getId_Processo().length() == 0 || !mandadoPrisaoDt.getId_Processo().equals(processoDt.getId()))){
			setDadosProcesso(mandadoPrisaoDt, processoDt);
		}
		
		mandadoPrisaoDt.setIdModelo(request.getParameter("Id_Modelo"));
		mandadoPrisaoDt.setModelo(request.getParameter("Modelo"));
		mandadoPrisaoDt.setIdArquivoTipo(request.getParameter("Id_ArquivoTipo"));
		mandadoPrisaoDt.setArquivoTipo(request.getParameter("ArquivoTipo"));
		mandadoPrisaoDt.setTextoEditor(request.getParameter("TextoEditor"));
		
		mandadoPrisaoDt.setId_UsuarioLog(idUsuario);
		mandadoPrisaoDt.setIpComputadorLog(request.getRemoteAddr());
	}
	
	public void setDadosProcesso(MandadoPrisaoDt mandadoPrisaoDt, ProcessoDt processoDt){
		mandadoPrisaoDt.setId_Processo(processoDt.getId()); 
		mandadoPrisaoDt.setProcessoTipo(processoDt.getProcessoTipo()); 
		mandadoPrisaoDt.setProcessoNumero( processoDt.getProcessoNumero()); 
		mandadoPrisaoDt.setDigitoVerificador( processoDt.getDigitoVerificador()); 
		mandadoPrisaoDt.setAno( processoDt.getAno());
		mandadoPrisaoDt.setProcessoNumeroCompleto(processoDt.getProcessoNumeroCompleto());
	}
	
	/**
	 * Tratamentos necessários ao realizar uma movimentação
	 * @throws Exception 
	 */
	protected void setParametrosAuxiliares(MandadoPrisaoDt mandadoPrisaoDt, int paginaAnterior, int paginaatual, HttpServletRequest request, MandadoPrisaoNe mandadoPrisaoNe, UsuarioNe usuarioNe, ProcessoDt processoDt) throws Exception{

		// Quando modelo foi selecionado monta conteúdo para aparecer no editor e já carrego o tipo do arquivo
		if (!mandadoPrisaoDt.getIdModelo().equals("") && paginaAnterior == (ModeloDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)) {
			ModeloDt modeloDt = mandadoPrisaoNe.consultarModeloId(mandadoPrisaoDt.getIdModelo(), processoDt, usuarioNe.getUsuarioDt());
			mandadoPrisaoDt.setIdArquivoTipo(modeloDt.getId_ArquivoTipo());
			mandadoPrisaoDt.setArquivoTipo(modeloDt.getArquivoTipo());
			mandadoPrisaoDt.setTextoEditor(modeloDt.getTexto());
		}

		request.setAttribute("tempFluxo2", paginaatual);
		request.setAttribute("TextoEditor", mandadoPrisaoDt.getTextoEditor());
		request.setAttribute("Id_ArquivoTipo", mandadoPrisaoDt.getIdArquivoTipo());
		request.setAttribute("ArquivoTipo", mandadoPrisaoDt.getArquivoTipo());
		request.setAttribute("Modelo", mandadoPrisaoDt.getModelo());
	}
	
	/**
	 * Lima os dados do objeto, mantendo os dados do processo
	 * @param mandadoPrisaoDt
	 */
	public void limparMandadoPrisaoDt(MandadoPrisaoDt mandadoPrisaoDt, ProcessoDt processoDt){
		mandadoPrisaoDt.limpar();
		mandadoPrisaoDt.setId_Processo(processoDt.getId()); 
		mandadoPrisaoDt.setProcessoTipo(processoDt.getProcessoTipo()); 
		mandadoPrisaoDt.setProcessoNumero( processoDt.getProcessoNumero()); 
		mandadoPrisaoDt.setDigitoVerificador( processoDt.getDigitoVerificador()); 
		mandadoPrisaoDt.setAno( processoDt.getAno());
		mandadoPrisaoDt.setProcessoNumeroCompleto(processoDt.getProcessoNumeroCompleto());
	}

	public String gerarModeloMandadoPrisao(MandadoPrisaoDt mandadoPrisaoDt, MandadoPrisaoNe mandadoPrisaoNe, UsuarioDt usuarioDt) throws Exception{
		ProcessoParteDt parte = mandadoPrisaoNe.consultarProcessoParte(mandadoPrisaoDt.getId_ProcessoParte());
		mandadoPrisaoDt.setDataNascimento(parte.getDataNascimento());
		mandadoPrisaoDt.setNaturalidade(parte.getCidadeNaturalidade());
//		mandadoPrisaoDt.setUfNaturalidade(parte.get);
		mandadoPrisaoDt.setNomeMae(parte.getNomeMae());
		mandadoPrisaoDt.setNomePai(parte.getNomePai());
		mandadoPrisaoDt.setProcessoParte(parte.getNome());
		mandadoPrisaoDt.setSexo(parte.getSexo());

		String end = "";		
		if (parte != null){
			EnderecoDt endereco = parte.getEnderecoParte();
			end = endereco.getLogradouro() + ", " + endereco.getNumero() + ", " + endereco.getComplemento() + ", " + endereco.getBairro() + ", " + endereco.getCidade() + " - " + endereco.getUf() + ". CEP: " + endereco.getCep() + ".";
		}
		
		mandadoPrisaoDt.setEnderecoCompleto(end);
		return mandadoPrisaoNe.montaModeloMandadoPrisao(mandadoPrisaoDt, usuarioDt);
	}
	
}
