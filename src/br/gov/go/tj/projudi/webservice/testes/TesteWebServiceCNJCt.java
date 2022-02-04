package br.gov.go.tj.projudi.webservice.testes;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.webservice.IntercomunicacaoService;
import br.gov.go.tj.utils.Funcoes;
import br.jus.cnj.intercomunicacao.beans.CabecalhoProcessual;
import br.jus.cnj.intercomunicacao.beans.DocumentoProcessual;
import br.jus.cnj.intercomunicacao.beans.Identificador;
import br.jus.cnj.intercomunicacao.beans.ManifestacaoProcessual;
import br.jus.cnj.intercomunicacao.beans.NumeroUnico;
import br.jus.cnj.intercomunicacao.beans.Parametro;
import br.jus.cnj.intercomunicacao.beans.RequisicaoConsultaAlteracao;
import br.jus.cnj.intercomunicacao.beans.RequisicaoConsultaAvisosPendentes;
import br.jus.cnj.intercomunicacao.beans.RequisicaoConsultaProcesso;
import br.jus.cnj.intercomunicacao.beans.RequisicaoConsultarTeorComunicacao;
import br.jus.cnj.intercomunicacao.beans.RespostaConsultaAlteracao;
import br.jus.cnj.intercomunicacao.beans.RespostaConsultaAvisosPendentes;
import br.jus.cnj.intercomunicacao.beans.RespostaConsultaProcesso;
import br.jus.cnj.intercomunicacao.beans.RespostaConsultarTeorComunicacao;
import br.jus.cnj.intercomunicacao.beans.RespostaManifestacaoProcessual;

public class TesteWebServiceCNJCt extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2175573918264353489L;

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
		doPost(req, resp);
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		
		IntercomunicacaoService servico = new IntercomunicacaoService();
		servico.setRequestDebug(request);
		RespostaConsultaProcesso respostaConsultaProcesso = null;
		RespostaConsultaAlteracao respostaConsultaAlteracao = null;
		RespostaConsultaAvisosPendentes respostaConsultaAvisosPendentes = null;
		RespostaConsultarTeorComunicacao tipoConsultarTeorComunicacaoResposta = null;
		RespostaManifestacaoProcessual respostaManifestacaoProcessual = null;
		
		int paginaatual = 0;		
		try{
			paginaatual = Integer.valueOf(request.getParameter("PaginaAtual"));
		} catch (Exception ex){
			paginaatual = -1;
		}
		
		switch (paginaatual) {			
			case 1: // Consultar Processo
				RequisicaoConsultaProcesso requisicaoConsultaProcesso = new RequisicaoConsultaProcesso();
				requisicaoConsultaProcesso.setIdConsultante("testeana");
				requisicaoConsultaProcesso.setSenhaConsultante("123456");
				requisicaoConsultaProcesso.setIncluirDocumentos(true);
				requisicaoConsultaProcesso.setMovimentos(true);
				
				NumeroUnico numeroProcesso = new NumeroUnico();
				numeroProcesso.setValue("5000383.64.2012.8.09.0059");
				requisicaoConsultaProcesso.setNumeroProcesso(numeroProcesso);
				
				respostaConsultaProcesso = servico.consultarProcesso(requisicaoConsultaProcesso);
				
				if (respostaConsultaProcesso != null)
				{
					
				}
				
				break;
				
			case 2: // Consultar Alteração
				RequisicaoConsultaAlteracao requisicaoConsultaAlteracao = new RequisicaoConsultaAlteracao();
				requisicaoConsultaAlteracao.setIdConsultante("testeana");
				requisicaoConsultaAlteracao.setSenhaConsultante("123456");
				//requisicaoConsultaAlteracao.setNumeroProcesso("5000061.44.2012.8.09.0059");
				requisicaoConsultaAlteracao.setNumeroProcesso("5000014.70.2012.8.09.0059");
								
				respostaConsultaAlteracao = servico.consultarAlteracao(requisicaoConsultaAlteracao);
				
				if (respostaConsultaAlteracao != null)
				{
					
				}
				
				break;
				
			case 3: // Consultar Avisos Pendentes
				RequisicaoConsultaAvisosPendentes requisicaoConsultaAvisosPendentes = new RequisicaoConsultaAvisosPendentes();
				requisicaoConsultaAvisosPendentes.setIdConsultante("testeana");
				requisicaoConsultaAvisosPendentes.setSenhaConsultante("123456");
				requisicaoConsultaAvisosPendentes.setIdRepresentado("testepro");
								
				respostaConsultaAvisosPendentes = servico.consultarAvisosPendentes(requisicaoConsultaAvisosPendentes);
				
				if (respostaConsultaAvisosPendentes != null)
				{
					
				}
				
				break;
			case 4: // consultarTeorComunicacao											
				tipoConsultarTeorComunicacaoResposta = servico.consultarTeorComunicacao(null);		
				
				if (tipoConsultarTeorComunicacaoResposta != null)
				{
					
				}
				
				break;
			case 5: // consultarTeorComunicacaoTJGO
				RequisicaoConsultarTeorComunicacao requisicaoconsultarTeorComunicacaoTJGO = new RequisicaoConsultarTeorComunicacao();
				requisicaoconsultarTeorComunicacaoTJGO.setIdConsultante("testeana");
				requisicaoconsultarTeorComunicacaoTJGO.setSenhaConsultante("123456");
				Identificador numeroPendencia = new Identificador();
				numeroPendencia.setValue("4109125");
				requisicaoconsultarTeorComunicacaoTJGO.setIdentificadorAviso(numeroPendencia);
				NumeroUnico numeroProcessoConsultarTeorComunicacaoTJGO = new NumeroUnico();					
				numeroProcessoConsultarTeorComunicacaoTJGO.setValue("7175150.64.2011.8.09.0000");
				requisicaoconsultarTeorComunicacaoTJGO.setNumeroProcesso(numeroProcessoConsultarTeorComunicacaoTJGO);
								
				tipoConsultarTeorComunicacaoResposta = servico.consultarTeorComunicacao(requisicaoconsultarTeorComunicacaoTJGO);
				
				break;
			case 6: // entregarManifestacaoProcessual incluir peticionamento em um processo existente
				ManifestacaoProcessual requisicaoManifestacaoProcessual = new ManifestacaoProcessual();
				requisicaoManifestacaoProcessual.setIdManifestante("testeana");
				requisicaoManifestacaoProcessual.setSenhaManifestante("123456");	
				
				NumeroUnico numeroProcessoManifestacaoProcessual = new NumeroUnico();					
				numeroProcessoManifestacaoProcessual.setValue("5000383.64.2012.8.09.0059");
				requisicaoManifestacaoProcessual.setNumeroProcesso(numeroProcessoManifestacaoProcessual);
				
				Parametro parametroPedidoDeUrgencia = new Parametro();
				parametroPedidoDeUrgencia.setNome("INDICADOR_PEDIDO_URGENCIA");
				parametroPedidoDeUrgencia.setValor("S");
				requisicaoManifestacaoProcessual.getParametros().add(parametroPedidoDeUrgencia);
				
				Parametro parametroIdMovimentacaoTipo = new Parametro();
				parametroIdMovimentacaoTipo.setNome("ID_MOVIMENTACAO_TIPO");
				parametroIdMovimentacaoTipo.setValor("167");
				requisicaoManifestacaoProcessual.getParametros().add(parametroIdMovimentacaoTipo);
				
				Parametro parametroComplemento = new Parametro();
				parametroComplemento.setNome("COMPLEMENTO");				
				parametroComplemento.setValor("PETIÇÃO ENVIADA PELO WEBSERVICE MP");
				requisicaoManifestacaoProcessual.getParametros().add(parametroComplemento);
				
				try 
				{							
					requisicaoManifestacaoProcessual.getDocumento().add(obtenhaDocumento("E:\\Projetos\\Testes MNI\\Arquivos\\5_alteracaosimonetti.pdf.p7s", "5_alteracaosimonetti.pdf"));
					
					//requisicaoManifestacaoProcessual.getDocumento().add(obtenhaDocumento("E:\\Projetos\\Testes MNI\\Arquivos\\5_alteracaosimonetti_homolog.pdf.p7s", "5_alteracaosimonetti_homolog.pdf"));
					
					requisicaoManifestacaoProcessual.getDocumento().add(obtenhaDocumento("E:\\Projetos\\Testes MNI\\Arquivos\\5_alteracaosimonetti2.pdf.p7s", "5_alteracaosimonetti2.pdf"));
					
					respostaManifestacaoProcessual = servico.entregarManifestacaoProcessual(requisicaoManifestacaoProcessual);
					
					if (respostaManifestacaoProcessual != null)
					{
						
					}
					
				} catch (Exception e) {
					
					e.printStackTrace();
				}	
				break;
				
			case 7: // entregarManifestacaoProcessual petição inicial
				ManifestacaoProcessual requisicaoManifestacaoProcessualInicial = new ManifestacaoProcessual();
				requisicaoManifestacaoProcessualInicial.setIdManifestante("testeana");
				requisicaoManifestacaoProcessualInicial.setSenhaManifestante("123456");	
				
				Parametro parametroPedidoDeUrgenciaInicial = new Parametro();
				parametroPedidoDeUrgenciaInicial.setNome("INDICADOR_PEDIDO_URGENCIA");
				parametroPedidoDeUrgenciaInicial.setValor("S");
				requisicaoManifestacaoProcessualInicial.getParametros().add(parametroPedidoDeUrgenciaInicial);
				
				Parametro parametroIdMovimentacaoTipoInicial = new Parametro();
				parametroIdMovimentacaoTipoInicial.setNome("ID_MOVIMENTACAO_TIPO");
				parametroIdMovimentacaoTipoInicial.setValor("167");
				requisicaoManifestacaoProcessualInicial.getParametros().add(parametroIdMovimentacaoTipoInicial);
				
				Parametro parametroComplementoInicial = new Parametro();
				parametroComplementoInicial.setNome("COMPLEMENTO");				
				parametroComplementoInicial.setValor("PETIÇÃO ENVIADA PELO WEBSERVICE MP");
				requisicaoManifestacaoProcessualInicial.getParametros().add(parametroComplementoInicial);
				
				requisicaoManifestacaoProcessualInicial.getDocumento().add(obtenhaDocumento("E:\\Projetos\\Testes MNI\\Arquivos\\5_alteracaosimonetti.pdf.p7s", "5_alteracaosimonetti.pdf"));
				
				requisicaoManifestacaoProcessualInicial.getDocumento().add(obtenhaDocumento("E:\\Projetos\\Testes MNI\\Arquivos\\5_alteracaosimonetti2.pdf.p7s", "5_alteracaosimonetti2.pdf"));
				
				// Teste Validação de parâmetros número do processo (petição em processo existente) ou dados básicos (petição incial)... Sem parâmetros obrigatórios				
				respostaManifestacaoProcessual = servico.entregarManifestacaoProcessual(requisicaoManifestacaoProcessualInicial);
				// Fim do teste
				
				requisicaoManifestacaoProcessualInicial.setDadosBasicos(ObtenhaDadosBasicosPeticaoInicial());
								
				// Teste Validação de parâmetros número do processo (petição em processo existente) ou dados básicos (petição incial)... Com os dois parâmetros exclusivos
				NumeroUnico numeroProcessoManifestacaoProcessualInicial = new NumeroUnico();					
				numeroProcessoManifestacaoProcessualInicial.setValue("5000383.64.2012.8.09.0059");
				requisicaoManifestacaoProcessualInicial.setNumeroProcesso(numeroProcessoManifestacaoProcessualInicial);
				
				respostaManifestacaoProcessual = servico.entregarManifestacaoProcessual(requisicaoManifestacaoProcessualInicial);
				requisicaoManifestacaoProcessualInicial.setNumeroProcesso(null);				
				// Fim do teste				
				
				
				try 
				{				
					// Teste petição inicial
					respostaManifestacaoProcessual = servico.entregarManifestacaoProcessual(requisicaoManifestacaoProcessualInicial);
					
					if (respostaManifestacaoProcessual != null)
					{
						
					}
					
				} catch (Exception e) {
					
					e.printStackTrace();
				}	
				break;
				
			case 8: // Consultar Processo SPG
				RequisicaoConsultaProcesso requisicaoConsultaProcessoSPG = new RequisicaoConsultaProcesso();
				requisicaoConsultaProcessoSPG.setIdConsultante("testeana");
				requisicaoConsultaProcessoSPG.setSenhaConsultante("123456");
				requisicaoConsultaProcessoSPG.setIncluirDocumentos(true);
				requisicaoConsultaProcessoSPG.setMovimentos(true);
				
				NumeroUnico numeroProcessoSPG = new NumeroUnico();
				//numeroProcessoSPG.setValue("40.73.2011.8.09.0000");
				//numeroProcessoSPG.setValue("4073");
				requisicaoConsultaProcessoSPG.setNumeroProcesso(numeroProcessoSPG);
				
				respostaConsultaProcesso = servico.consultarProcesso(requisicaoConsultaProcessoSPG);
				
				if (respostaConsultaProcesso != null)
				{
					
				}
				
				break;
			default:
				
		}
	}
	
	private CabecalhoProcessual ObtenhaDadosBasicosPeticaoInicial() {
		CabecalhoProcessual peticao = new CabecalhoProcessual();
		
		
		return peticao;
	}
	
	private DocumentoProcessual obtenhaDocumento(String caminhoCompleto, String nomeArquivo) throws IOException
	{
		DocumentoProcessual documento = new DocumentoProcessual();
		
		if (arquivoExiste(caminhoCompleto)) {
			documento.setConteudo(Funcoes.converterParaDataHandler(getBytesArquivo(caminhoCompleto)));
		}
		
		Parametro parametroNomeDoArquivo = new Parametro();
		parametroNomeDoArquivo.setNome("NOME_ARQUIVO");
		parametroNomeDoArquivo.setValor(nomeArquivo);
		documento.getOutroParametro().add(parametroNomeDoArquivo);
		
		Parametro parametroIdArquivoTipo = new Parametro();
		parametroIdArquivoTipo.setNome("ID_ARQUIVO_TIPO");
		parametroIdArquivoTipo.setValor("19");
		documento.getOutroParametro().add(parametroIdArquivoTipo);
		
		return documento;
	}
	
	public boolean arquivoExiste(String fileName){
		return new File(fileName).exists();
	}
	
	 public byte[] getBytesArquivo(String fileName) throws IOException {
		return getBytesArquivo(new File(fileName));
	 }

	 public byte[] getBytesArquivo(File file) throws IOException {
		try(InputStream inputStream = new FileInputStream(file)){
			long length = file.length();
			if (length > Integer.MAX_VALUE) {
				throw new IOException("Arquivo E muito grande");
			}
		
			byte[] bytes = new byte[(int)length];
			int tamanho = 0;
			int leitura = 0;
			while (tamanho < bytes.length
					&& (leitura=inputStream.read(bytes, tamanho, bytes.length-tamanho)) >= 0) {
				tamanho += leitura;
			}
			if (tamanho < bytes.length) {
				throw new IOException("Falaha na leitura do arquivo"+file.getName());
			}
			return bytes;
		}
	}
}
