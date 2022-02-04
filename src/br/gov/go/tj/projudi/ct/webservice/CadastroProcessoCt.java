package br.gov.go.tj.projudi.ct.webservice;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import br.gov.go.tj.projudi.ct.InsercaoArquivoCt;
import br.gov.go.tj.projudi.dt.ArquivoDt;
import br.gov.go.tj.projudi.dt.ArquivoTipoDt;
import br.gov.go.tj.projudi.dt.GrupoTipoDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.ProcessoCadastroDt;
import br.gov.go.tj.projudi.dt.ProcessoParteDt;
import br.gov.go.tj.projudi.ne.ProcessoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.util.ControleImportacaoDados;
import br.gov.go.tj.projudi.util.GerenciaArquivo;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.Certificado.Base64Utils;

public class CadastroProcessoCt extends InsercaoArquivoCt {

	private static final long serialVersionUID = 8141613937174127200L;
	PrintWriter out;

	public int Permissao() {
		return 444;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String nomebusca, String posicaopaginaatual) throws Exception, ServletException, IOException {
		String stOperacao = "";
		String stAcao = "/WEB-INF/jsptjgo/Padroes/MensagemXML.jsp";
		String mensagem = "";
		String senhaCertificado = "";
		String contentType = "";
		String stVersao = "1";
		String stAjuda = "1";
		
		response.setContentType("text/xml; charset=UTF-8");

		ProcessoCadastroDt processoDt = (ProcessoCadastroDt) request.getSession().getAttribute("ProcessoCadastroDt");
		if (processoDt == null) processoDt = new ProcessoCadastroDt();

		processoDt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		processoDt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());

		try{
			switch (paginaatual) {

			case 1: //IMPORTAR ARQUIVO TCO
				if (request.getParameter("h") != null && !request.getParameter("h").equalsIgnoreCase("")) {
					stAjuda = request.getParameter("h");
					switch (stAjuda) {
					case "1":
					default:
						request.setAttribute("Ajuda", "SERVIÇO 31 – IMPORTAR ARQUIVO TCO (VERSÃO 1)\nEfetua a leitura e validação dos dados existentes no arquivo.Esse arquivo deve conter os dados do processo.\nURL:\nhttps://projudi.tjgo.jus.br/CadastroProcesso\nPARÂMETROS:\nPaginaAtual=1\na=arquivo .tco\nh=número versão(ajuda)\nv=número versão(versão)\nRETORNO:\nArquivo TCO lido com sucesso");
						break;
					}
					response.setContentType("text/plain; charset=UTF-8");
					stAcao = "/WEB-INF/jsptjgo/Padroes/AjudaWebservice.jsp";					
				} else {
					if (request.getParameter("v") != null && !request.getParameter("v").equalsIgnoreCase("")) 
						stVersao = request.getParameter("v");
					switch (stVersao) {
					case "1":
					default:
        				String arquivoTcoBase64 = (String) request.getParameter("a");
        				String conteudoArquivo = new String(Base64Utils.base64Decode(arquivoTcoBase64));
        
        				if (conteudoArquivo != null && conteudoArquivo.length() > 0) {
        					LogDt logDt = new LogDt(UsuarioSessao.getId_Usuario(), UsuarioSessao.getIpComputadorLog());
        					ControleImportacaoDados controleImportacaoDados = new ControleImportacaoDados(conteudoArquivo, logDt);
        					if (controleImportacaoDados.getMensagem().length() == 0) {
        						processoDt = controleImportacaoDados.getProcessoDt();
        					} else
        						throw new Exception(controleImportacaoDados.getMensagem());
        				} else
        					throw new Exception("Selecione um Arquivo para prosseguir");
        
        				request.setAttribute("Mensagem", "Arquivo TCO lido com sucesso");
        				request.setAttribute("RespostaTipo", "OK");
        				request.setAttribute("Operacao", "31");
        				stAcao = "/WEB-INF/jsptjgo/Padroes/MensagemXML.jsp";
					}
				}
				break;

			case 2: //ANEXAR ARQUIVOS AO PROCESSO
				if (request.getParameter("h") != null && !request.getParameter("h").equalsIgnoreCase("")) {
					stAjuda = request.getParameter("h");
					switch (stAjuda) {
					case "1":
					default:
						request.setAttribute("Ajuda", "SERVIÇO 32 – ANEXAR ARQUIVOS AO PROCESSO (VERSÃO 1)\nInsere arquivos na petição inicial. Os arquivos devem estar assinados.\nURL:\nhttps://projudi.tjgo.jus.br/CadastroProcesso\nPARÂMETROS:\nPaginaAtual=2\na=nome do arquivo com extensão\nb=arquivo assinado(base64)\nc=CodigoArquivoTipo\nd=senha do certificado\n\nh=número versão(ajuda)\nv=número versão(versão)\nRETORNO:\nArquivo inserido com sucesso");
						break;
					}
					response.setContentType("text/plain; charset=UTF-8");
					stAcao = "/WEB-INF/jsptjgo/Padroes/AjudaWebservice.jsp";					
				} else {
					if (request.getParameter("v") != null && !request.getParameter("v").equalsIgnoreCase("")) 
						stVersao = request.getParameter("v");
					switch (stVersao) {
					case "1":
					default:
        				String stNomeArquivo = "";
        				String arquivoBase64 = "";
        				String stIdArquivoTipo = "";
        				ArquivoDt arquivoDt = new ArquivoDt();
        
        				if (request.getParameter("a") != null && !request.getParameter("a").equals("")) stNomeArquivo = request.getParameter("a");
        				else throw new Exception("Nome do Arquivo não encontrado.");
        				if (request.getParameter("b") != null && !request.getParameter("b").equals("")) arquivoBase64 = request.getParameter("b");
        				else throw new Exception("Arquivo não encontrado.");
        				if (request.getParameter("c") != null && !request.getParameter("c").equals("")) stIdArquivoTipo = request.getParameter("c");
        				if (request.getParameter("d") != null && !request.getParameter("d").equals("")) senhaCertificado = request.getParameter("d");
        				
       					arquivoDt.setNomeArquivo(stNomeArquivo);
        				arquivoDt.setArquivo(arquivoBase64);
        				if(!stIdArquivoTipo.equalsIgnoreCase("")) 
        					arquivoDt.setId_ArquivoTipo(stIdArquivoTipo);
        				else {
        					switch (UsuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt()) {
        					case GrupoTipoDt.AUTORIDADE_POLICIAL:
        						arquivoDt.setId_ArquivoTipo(String.valueOf(ArquivoTipoDt.TERMO_CIRCUNSTANCIADO));
        						break;
        					default:
        						arquivoDt.setId_ArquivoTipo(String.valueOf(ArquivoTipoDt.PETICAO_INICIAL));
        						break;
        					}
        				}
        				contentType = GerenciaArquivo.getInstancia().getTipoConteudo(arquivoDt.getNomeArquivo());
        				arquivoDt.setContentType(contentType);
        				if(senhaCertificado.length() > 0) {
        					arquivoDt.setAssinado(false);
        					arquivoDt.setSenhaCertificado(senhaCertificado);
        					arquivoDt.setSalvarSenha(false);
        					arquivoDt.setGerarAssinatura(true);
        				} else {
        					arquivoDt.setAssinado(true);
        					arquivoDt.setGerarAssinatura(false);
        				}
        				
        				setArquivo(arquivoDt, request);
        				
        				request.setAttribute("Mensagem", "Arquivo inserido com sucesso");
        				request.setAttribute("RespostaTipo", "OK");
        				request.setAttribute("Operacao", "32");
        				stAcao = "/WEB-INF/jsptjgo/Padroes/MensagemXML.jsp";
					}
				}
				break;

			case 3: //CONCLUIR CADASTRO DE PROCESSO
				if (request.getParameter("h") != null && !request.getParameter("h").equalsIgnoreCase("")) {
					stAjuda = request.getParameter("h");
					switch (stAjuda) {
					case "1":
					default:
						request.setAttribute("Ajuda", "SERVIÇO 33 – CONCLUIR CADASTRO DE PROCESSO (VERSÃO 1)\nFinaliza o cadastramento do processo com base nos dados fornecidos nos serviços 31 e 32. Retorna os dados do processo cadastrado.\nURL:\nhttps://projudi.tjgo.jus.br/CadastroProcesso\nPARÂMETROS:\nPaginaAtual=3\nh=número versão(ajuda)\nv=número versão(versão)\nRETORNO:\nDados do processo cadastrado");
						break;
					}
					response.setContentType("text/plain; charset=UTF-8");
					stAcao = "/WEB-INF/jsptjgo/Padroes/AjudaWebservice.jsp";					
				} else {
					if (request.getParameter("v") != null && !request.getParameter("v").equalsIgnoreCase("")) 
						stVersao = request.getParameter("v");
					switch (stVersao) {
					case "1":
					default:
        				if (processoDt != null && processoDt.getPartesProcesso().size() > 0) {
        					Map mapArquivos = (Map) request.getSession().getAttribute("ListaArquivos");
            				List lista = Funcoes.converterMapParaList(mapArquivos);
            				processoDt.setListaArquivos(lista);
        					if (processoDt.getListaArquivos() != null) {
        						List partesProcesso = processoDt.getListaPolosAtivos();
        						for (int i = 0; i < partesProcesso.size(); i++) {
        							ProcessoParteDt parte = (ProcessoParteDt) partesProcesso.get(i);
        							processoDt.addListaPartesIntimadas(parte);
        						}
        						new ProcessoNe().cadastrarProcessoImportacao(processoDt, UsuarioSessao.getUsuarioDt());
        						for (Iterator iterator = processoDt.getListaArquivos().iterator(); iterator.hasNext();) {
        							ArquivoDt arquivoTempDt  = (ArquivoDt) iterator.next();
        							arquivoTempDt.setHash(UsuarioSessao.getCodigoHash(arquivoTempDt.getId()));
        						}
        						request.setAttribute("Operacao", "33");
        						request.setAttribute("ProcessoCadastroDt", processoDt);
        						processoDt = new ProcessoCadastroDt();
        						request.getSession(false).removeAttribute("ProcessoCadastroDt");
        						request.getSession(false).removeAttribute("ListaArquivos");
        						stAcao = "/WEB-INF/jsptjgo/DadosProcessoCadastradoXml.jsp";
        					} else
        						throw new Exception("Nenhum arquivo foi inserido. Verifique o passo de \"Anexar Arquivos\"");
        				} else
        					throw new Exception("Não há processo a ser cadastrado. Verifique o passo de \"Inserir Arquivo TCO\"");
					}
				}
				break;
				
			case 8: //CONCLUIR CADASTRO DE PROCESSO SEM RETORNO
				if (request.getParameter("h") != null && !request.getParameter("h").equalsIgnoreCase("")) {
					stAjuda = request.getParameter("h");
					switch (stAjuda) {
					case "1":
					default:
						request.setAttribute("Ajuda", "SERVIÇO 79 – CONCLUIR CADASTRO DE PROCESSO SEM RETORNO (VERSÃO 1)\nFinaliza o cadastro de processo de acordo com os dados recebidos nas operações 31 e 32. Não retorna os dados do processo cadastrado. É necessário a utilização do serviço 80 para isso.\nURL:\nhttps://projudi.tjgo.jus.br/CadastroProcesso\nPARÂMETROS:\nPaginaAtual=8\nh=número versão(ajuda)\nv=número versão(versão)\nRETORNO:\nProcesso vai ser cadastrado");
						break;
					}
					response.setContentType("text/plain; charset=UTF-8");
					stAcao = "/WEB-INF/jsptjgo/Padroes/AjudaWebservice.jsp";					
				} else {
					if (request.getParameter("v") != null && !request.getParameter("v").equalsIgnoreCase("")) 
						stVersao = request.getParameter("v");
					switch (stVersao) {
					case "1":
					default:
        				mensagem = "Processo vai ser cadastrado";
        				if (processoDt != null && processoDt.getPartesProcesso().size() > 0) {
        					Map mapArquivos = (Map) request.getSession().getAttribute("ListaArquivos");
            				List lista = Funcoes.converterMapParaList(mapArquivos);
            				processoDt.setListaArquivos(lista);
        					if (processoDt.getListaArquivos() != null) {
        						if (request.getSession().getAttribute("Cadastrando") == null) {
        							ThreadCadastro exec = new ThreadCadastro(processoDt, request.getSession(), UsuarioSessao);
        							new Thread(exec).start();
        						} else {
        							mensagem = "Já existe um processo sendo cadastrado";
        						}
        					} else {
        						throw new Exception("Nenhum arquivo foi inserido. Verifique o passo de \"Anexar Arquivos\"");
        					}
        				} else {
        					throw new Exception("Não há processo a ser cadastrado. Verifique o passo de \"Inserir Arquivo TCO\"");
        				}
        				request.getSession(false).removeAttribute("ListaArquivos");
        				request.setAttribute("Mensagem", mensagem);
        				request.setAttribute("RespostaTipo", "OK");
        				request.setAttribute("Operacao", "79");
        				stAcao = "/WEB-INF/jsptjgo/Padroes/MensagemXML.jsp";
					}
				}
				break;
				
			case 9: //VISUALIZAR DADOS DE PROCESSO CADASTRADO
				if (request.getParameter("h") != null && !request.getParameter("h").equalsIgnoreCase("")) {
					stAjuda = request.getParameter("h");
					switch (stAjuda) {
					case "1":
					default:
						request.setAttribute("Ajuda", "SERVIÇO 80 – VISUALIZAR DADOS DE PROCESSO CADASTRADO (VERSÃO 1)\nRetorna os dados do processo cadastrado com serviço 79.\nURL:\nhttps://projudi.tjgo.jus.br/CadastroProcesso\nPARÂMETROS:\nPaginaAtual=9\nh=número versão(ajuda)\nv=número versão(versão)\nRETORNO:\nDados do Processo");
						break;
					}
					response.setContentType("text/plain; charset=UTF-8");
					stAcao = "/WEB-INF/jsptjgo/Padroes/AjudaWebservice.jsp";					
				} else {
					if (request.getParameter("v") != null && !request.getParameter("v").equalsIgnoreCase("")) 
						stVersao = request.getParameter("v");
					switch (stVersao) {
					case "1":
					default:
        				ProcessoCadastroDt processoDt01 = (ProcessoCadastroDt) request.getSession().getAttribute("ProcessoCadastradoDt");
        				if(processoDt01 != null) {
        					request.getSession().removeAttribute("ProcessoCadastradoDt");
        					request.setAttribute("Operacao", request.getSession().getAttribute("Operacao"));
        					request.setAttribute("ProcessoCadastroDt", processoDt01);
        					processoDt = new ProcessoCadastroDt();    					
        					stAcao = "/WEB-INF/jsptjgo/DadosProcessoCadastradoXml.jsp";
        				}else if (request.getSession().getAttribute("Cadastrando") != null) {
        					mensagem = "Cadastro em andamento";
        					request.setAttribute("Mensagem", mensagem);
        					request.setAttribute("RespostaTipo", "OK");
        					request.setAttribute("Operacao", "80");
        					stAcao = "/WEB-INF/jsptjgo/Padroes/MensagemXML.jsp";
        				} else {
        					mensagem = "Não existe processo cadastrado";
        					request.setAttribute("Mensagem", mensagem);
        					request.setAttribute("RespostaTipo", "OK");
        					request.setAttribute("Operacao", "80");
        					stAcao = "/WEB-INF/jsptjgo/Padroes/MensagemXML.jsp";
        				}
					}
				}
				break;
				
			default:
				request.setAttribute("Mensagem", "Operação não definida");
				request.setAttribute("RespostaTipo", "ERRO");
				request.setAttribute("Operacao", "-1");

			}
		} catch(Exception e) {
			request.setAttribute("RespostaTipo", "ERRO");
			request.setAttribute("Mensagem", e.getMessage());
			request.setAttribute("Operacao", stOperacao);
			stAcao = "/WEB-INF/jsptjgo/Padroes/MensagemXML.jsp";
		}
		request.getSession().setAttribute("ProcessoCadastroDt", processoDt);
		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
		return;

	}

	class ThreadCadastro implements Runnable {
						
		private HttpSession Session;		
		private ProcessoCadastroDt processoDt;
		private UsuarioNe UsuarioSessao;
		
		public ThreadCadastro(ProcessoCadastroDt processoDt, HttpSession session,UsuarioNe UsuarioSessao) {
			
			Session = session;
			this.processoDt = processoDt;
			this.UsuarioSessao = UsuarioSessao;
		}
		
		@Override
		public void run() {
			byte[] byTemp = null;
			Session.setAttribute("Cadastrando", 1);			
			try {
				List partesProcesso = processoDt.getListaPolosAtivos();
				for (int i = 0; i < partesProcesso.size(); i++) {
					ProcessoParteDt parte = (ProcessoParteDt) partesProcesso.get(i);
					processoDt.addListaPartesIntimadas(parte);
				}

				try { Thread.sleep (60000); } catch (InterruptedException ex) {}
				
				new ProcessoNe().cadastrarProcessoImportacao(processoDt, UsuarioSessao.getUsuarioDt());
				
				for (Iterator iterator = processoDt.getListaArquivos().iterator(); iterator.hasNext();) {
					ArquivoDt arquivoTempDt  = (ArquivoDt) iterator.next();
					arquivoTempDt.setHash(UsuarioSessao.getCodigoHash(arquivoTempDt.getId()));
				}
				
				Session.setAttribute("Operacao", "33");
				Session.setAttribute("ProcessoCadastradoDt", processoDt);
				    		    			
			} catch (Exception e) {				
			} finally {
				Session.removeAttribute("Cadastrando");
				Session.removeAttribute("ProcessoCadastroDt");
			}
		}
	};
}
