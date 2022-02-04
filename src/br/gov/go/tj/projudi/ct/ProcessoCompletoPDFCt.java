package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import br.gov.go.tj.projudi.dt.ArquivoDt;
import br.gov.go.tj.projudi.dt.ArquivoFisicoMetadadosDt;
import br.gov.go.tj.projudi.dt.MovimentacaoArquivoDt;
import br.gov.go.tj.projudi.dt.MovimentacaoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.ne.MovimentacaoArquivoNe;
import br.gov.go.tj.projudi.ne.ProcessoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.MensagemException;
import br.gov.go.tj.utils.ValidacaoUtil;

public class ProcessoCompletoPDFCt extends Controle {

	private static final long serialVersionUID = -4909054442108188718L;
	
	private static final int VOLUME_100MB = 102000; // deixar menos de 100 mb para margem caso o arquivo aumente de tamanho por causa da capa e etiquetas laterais
	
	private static final int HUM_MB_EM_BYTES = 1048576;
	
	public int Permissao() {
		return ProcessoDt.CodigoPermissaoPDF_COMPLETO;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws MensagemException, Exception, ServletException, IOException {
		
		ProcessoDt processoDt = new ProcessoDt();
		ProcessoNe processoNe = new ProcessoNe();
		String stId = request.getParameter("idProcesso");
		request.setAttribute("PaginaAtual", "-2");

		response.addCookie(new Cookie("fileDownload", "true"));
		// response.addCookie(new Cookie("path", "/"));

		if (request.getSession().getAttribute("processoDt") != null){
			processoDt = (ProcessoDt) request.getSession().getAttribute("processoDt");
		}	else{
			processoDt = new ProcessoDt();
		}
		
		String operacao = request.getParameter("operacao") != null ? request.getParameter("operacao") : ""; 
		
		switch (paginaatual) {
			case Configuracao.Imprimir:
				
				// Se foi passado Id_Processo efetua consulta
				if (stId != null && !stId.equalsIgnoreCase("")) {
					processoDt = processoNe.consultarDadosProcesso(stId, UsuarioSessao.getUsuarioDt(), false, false, UsuarioSessao.getNivelAcesso());									
					
					//para todos os usuários é necessário tem acesso ao processo guando ele é segredo de justiça
					if(processoDt.isSegredoJustica() || processoDt.isSigiloso()){				
						if (!processoNe.podeAcessarProcesso(UsuarioSessao.getUsuarioDt() ,processoDt, null )) {
							throw new MensagemException("Usuário sem permissão para gerar PDF");
						}					
					}
					
					Map mapMovimentacoesArquivos = (new MovimentacaoArquivoNe()).consultarArquivosMovimentacoes(processoDt.getId(), UsuarioSessao.getNivelAcesso());
					
					// Seta a lista de arquivos do processo
					request.setAttribute("mapMovimentacoesArquivos", mapMovimentacoesArquivos);
					
					// Procura por alguma movimentação que tem arquivos fisicos, armazenados no storage
					boolean isTemHistoricoFisico = isTemHistoricoFisico(mapMovimentacoesArquivos);
					
					// Indica que o processo tem arquivos no storage - histórico físico (controlar a exibição da 2a aba no jsp)
					request.setAttribute("temHistoricoFisico", isTemHistoricoFisico);
										
					if (isTemHistoricoFisico){
						
						// Cria uma nova lista com os arquivos fisicos e para cada arquivo, acrescentar o tamanho do mesmo, obtido pelo arquivo metadata.json
						List<Map<String, Object>> mapMovimentacoesArquivosHistoricoFisico = montarListaArquivosHistoricoFisico(processoDt, mapMovimentacoesArquivos);
						
						// Seta a lista de arquivos digitalizados - histórico fisico para exibir no jsp
						request.setAttribute("mapMovimentacoesArquivosHistoricoFisico", mapMovimentacoesArquivosHistoricoFisico);
						
						// Obtém a quantidade de volumes dos arquivos físicos
						request.setAttribute("totalVolumesFisicos", getTotalVolumesFisicos(mapMovimentacoesArquivosHistoricoFisico));
						
					}
					
					RequestDispatcher dis = request.getRequestDispatcher("/WEB-INF/jsptjgo/ListaArquivosProcesso2.jsp");
					dis.include(request, response);
			
					
				} else if (operacao.equals("Status")){
					
					// Verifica se a geração do PDF terminou.
					JSONObject json = new JSONObject();
					json.put("flag", request.getSession().getAttribute("flag") != null ? request.getSession().getAttribute("flag"): "0");
					response.setContentType("application/json"); 
			        response.setCharacterEncoding("utf-8"); 
					PrintWriter writer = response.getWriter();
					writer.append(json.toString());
					writer.flush();
					
					return;	
					
				} else if (operacao.equals("GerarPDF")) {
					
					// MONTA LISTA DE ARQUIVOS PARA PROCESSO EM PDF
					// *************************
					String codigosArquivos1 = request.getParameter("codigosArquivos");
					String codigosMovimentacoes1 = request.getParameter("codigosMovimentacoes");
					
					String[] codigosArquivos = codigosArquivos1.split(";");
					String[] codigosMovimentacoes = codigosMovimentacoes1.split(";");
					
					if (codigosArquivos != null) {
						List lisTemp = processoDt.getListaMovimentacoes();
						if (lisTemp == null || lisTemp.size() == 0) {
							return;
						}
	
						try {
							response.setContentType("application/pdf");
							response.setHeader("Content-Disposition", "attachment; filename=Report0"+System.currentTimeMillis()+".pdf");


							request.getSession().setAttribute("flag", 0);
							processoNe.gerarPdfProcessoIncompleto(processoDt, ProjudiPropriedades.getInstance().getCaminhoAplicacao() , UsuarioSessao.getUsuarioDt().getNome(), codigosArquivos, codigosMovimentacoes, response.getOutputStream(), UsuarioSessao);
							request.getSession().setAttribute("flag", 1);
							response.flushBuffer();
																
							return;
							// **********************************************************************************************
						} catch(Exception e) {
							request.setAttribute("Mensagem", "Erro ao Gerar Processo Completo.");
							RequestDispatcher dis = request.getRequestDispatcher("/WEB-INF/jsptjgo/Erro.jsp");
							dis.include(request, response);
						}
					} else {
						request.setAttribute("Mensagem", "Nenhum Arquivo Selecionado!");
						RequestDispatcher dis = request.getRequestDispatcher("/WEB-INF/jsptjgo/Erro.jsp");
						dis.include(request, response);
					}
				} else if (request.getParameter("operacaoFisico") != null) {
					String nomeDoArquivoFisico = request.getParameter("myradioFisico");
					if (nomeDoArquivoFisico == null || nomeDoArquivoFisico.trim().length() == 0) throw new MensagemException("É necessário selecionar um volume.");
					processoNe.baixarArquivoObjectStorageDigitalizacao(processoDt.getProcessoNumeroCompleto(), nomeDoArquivoFisico, response);
				}
				break;
			default:
				throw new MensagemException("Usuário sem permissão para gerar PDF");
			}
	}
	
	/**
	 * Verifica se existe arquivo de histórico físico em alguma movimentação
	 * Percorrer os campos values do mapa e verificar se na lista, os metodos isFisico() e temAcesso() 
	 * da classe MovimentacaoArquivoDt são verdadeiros.
	 * @param mapMovimentacoesArquivos
	 * @return
	 */
	protected boolean isTemHistoricoFisico(Map<String, List<MovimentacaoArquivoDt>> mapaArquivosMovimentacao){
		boolean flag = false;
		if (ValidacaoUtil.isNaoVazio(mapaArquivosMovimentacao)){			
			for (List<MovimentacaoArquivoDt> lista: mapaArquivosMovimentacao.values()){
				flag = lista.stream().filter(ma -> ma.isHistoricoFisico() && ma.temAcessoUsuario()).count() > 0;
				if (flag) break;
			}
		}
		return flag;
	}
	
	/**
	 * 
	 * @param processoDt
	 * @param mapaArquivosMovimentacao - Hashmap com a lista de arquivos por movimentação (key = id_movi, value = Lista de MovimentacaoArquivoDt)
	 */
	protected List<Map<String, Object>> montarListaArquivosHistoricoFisico(ProcessoDt processoDt, Map<String, List<MovimentacaoArquivoDt>> mapaArquivosMovimentacao){
		
		int volume = 1;
		long tamanhoTotal = 0;
		boolean flagBuscaUnica = false;
		
		ArquivoFisicoMetadadosDt metadados = null;
		
		List<Map<String, Object>> listaRetorno = null;
		
		// Percorre as movimentações do processo
		for (int i = 0; i < processoDt.getListaMovimentacoes().size(); i++){
			
			MovimentacaoDt movimentacaoDt = (MovimentacaoDt) processoDt.getListaMovimentacoes().get(i);
						
			List<MovimentacaoArquivoDt> listaMovimentacaoArquivoDt = mapaArquivosMovimentacao.get(movimentacaoDt.getId());
									
			if (listaMovimentacaoArquivoDt != null){
				
				// Filtra os arquivos que são fisicos e o usuario tem acesso para visualização
				List<MovimentacaoArquivoDt> listaFiltrada = listaMovimentacaoArquivoDt.stream().filter(ma -> ma.isFisico() && ma.temAcessoUsuario()).collect(Collectors.toList());
				
				if (!listaFiltrada.isEmpty()){
					
					if (ValidacaoUtil.isNulo(listaRetorno)) listaRetorno = new ArrayList<>();
					
					// Percorre os MovimentacaoArquivoDt e preenche os atributos tamanho e volume de cada ArquivoDt
					for (int j = 0; j < listaFiltrada.size(); j++ ){
						
						MovimentacaoArquivoDt movimentacaoArquivoDt = listaFiltrada.get(j);
						
						ArquivoDt arq = movimentacaoArquivoDt.getArquivoDt();
						arq.setArquivo(String.valueOf(HUM_MB_EM_BYTES));
												
						if (!flagBuscaUnica){
							flagBuscaUnica = true;
							try {
								metadados = new MovimentacaoArquivoNe().obtenhaStorageMetaDataJson(processoDt.getProcessoNumeroCompleto());
							} catch (IOException | MensagemException e){
								metadados = null; // Não encontrou o arquivo metadados.json
							}									
						}
						
						if (metadados != null){
							arq.setArquivo(String.valueOf(metadados.getTamanhoPorNomeArquivo(arq.getNomeArquivo())));
						}
						
						// Soma o tamanho do arquivo atual ao somatório geral e define qual o volume
						tamanhoTotal += arq.getTamanhoEmKbytes();
        		   		
						// De acordo com o tamanho total dos arquivos, define o volume que o arquivo atual pertence
						if (tamanhoTotal > VOLUME_100MB){
        		   			tamanhoTotal=arq.getTamanhoEmKbytes();
        		   			volume++;
        		   		}
						
        		   		arq.setVolume(volume);
        		   		
					}
					
					// Adiciona o mapa na listagem final
					Map<String, Object> mapa = new HashMap<>();
					mapa.put("mov", movimentacaoDt);
					mapa.put(movimentacaoDt.getId(), listaFiltrada);
					mapa.put("nivel", i+1);
					listaRetorno.add(mapa);
					
				}
			}			
		}
		
		return listaRetorno;
	}
	
	/**
	 * 
	 * @param listaMapaArquivosPorMovimentacao
	 * @return
	 */
	protected int getTotalVolumesFisicos(List<Map<String, Object>> listaMapaArquivosPorMovimentacao){
		
		/* Formato
		 * Mapa["mov"] - MovimentacaoDt
		 * Mapa["nivel"] - nível
		 * Mapa[12345] - Lista de ArquivosDt { ... }
		 */
		
		if (listaMapaArquivosPorMovimentacao != null){
			
			int size = listaMapaArquivosPorMovimentacao.size();
			
			// Obtém o último mapa da lista. Irá conter a movimentação e arquivos do último volume.
			Map<String, Object> mapaArquivosPorMovimentacao = listaMapaArquivosPorMovimentacao.get(size - 1);
			
			// Obtém a referencia ao movimentacaoDt do ultimo mapa
			MovimentacaoDt movimentacaoDt = (MovimentacaoDt) mapaArquivosPorMovimentacao.get("mov");			
			
			// Obtém a movimentação e pelo id, consulta os arquivos da movimentação
			List<MovimentacaoArquivoDt> arquivos = (List) mapaArquivosPorMovimentacao.get(movimentacaoDt.getId());
			
			// Acessa o último arquivo da lista e retorna o número do volume
			return arquivos.get(arquivos.size()-1).getArquivoDt().getVolume();
		}
		
		else return 1;
	}
	
}
