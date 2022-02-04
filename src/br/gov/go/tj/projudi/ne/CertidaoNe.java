package br.gov.go.tj.projudi.ne;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import br.gov.go.tj.projudi.dt.AreaDt;
import br.gov.go.tj.projudi.dt.ArquivoTipoDt;
import br.gov.go.tj.projudi.dt.BeneficioCertidaoAntecedenteCriminalDt;
import br.gov.go.tj.projudi.dt.CertidaoAntecedenteCriminalDt;
import br.gov.go.tj.projudi.dt.CertidaoDt;
import br.gov.go.tj.projudi.dt.CertidaoExecucaoCPCDt;
import br.gov.go.tj.projudi.dt.CertidaoGuiaDt;
import br.gov.go.tj.projudi.dt.CertidaoNegativaPositivaDt;
import br.gov.go.tj.projudi.dt.CertidaoNegativaPositivaPublicaDt;
import br.gov.go.tj.projudi.dt.CertidaoPraticaForenseDt;
import br.gov.go.tj.projudi.dt.CertidaoSegundoGrauNegativaPositivaDt;
import br.gov.go.tj.projudi.dt.CertidaoValidacaoDt;
import br.gov.go.tj.projudi.dt.CidadeDt;
import br.gov.go.tj.projudi.dt.ComarcaDt;
import br.gov.go.tj.projudi.dt.GuiaEmissaoDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.ModeloDt;
import br.gov.go.tj.projudi.dt.ProcessoAntecedenteCriminalDt;
import br.gov.go.tj.projudi.dt.ProcessoCertidaoPositivaNegativaDt;
import br.gov.go.tj.projudi.dt.ProcessoCertidaoPraticaForenseDt;
import br.gov.go.tj.projudi.dt.ProcessoCertidaoSegundoGrauPositivaNegativaDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteDt;
import br.gov.go.tj.projudi.dt.ProcessoSubtipoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.ps.CertidaoPs;
import br.gov.go.tj.projudi.ps.ProcessoPs;
import br.gov.go.tj.projudi.util.EscreverTextoPDF;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.Cifrar;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;
import br.gov.go.tj.utils.Relatorios;
import br.gov.go.tj.utils.TJDataHora;
import br.gov.go.tj.utils.UriBuilder;
/**
 * @author jpcpresa
 *
 */
public class CertidaoNe extends CertidaoNeGen {
	
	/** 10.0.10.90
     * 
     */
    private static final long serialVersionUID = 7589686641921232250L;
  //  83749772
    //Webservice das Guias das Certidões.
 //   public static final String PJDI003T = "http://sv-natweb-p00.tjgo.jus.br/cgi-bin/tjg-guia/forpspgi/pjdi003T?guia=";
    
    //public static final String host = "sv-natweb-p00.tjgo.jus.br";
    public static final String host = "http://"+ProjudiPropriedades.getInstance().getSPGHost();
    //public static final String ipDesenvolvimento = "10.0.10.90";
    //public static final String ipProducao = "192.168.200.87";
    //public static final String endereco = ipDesenvolvimento;
    //public static final String endereco = ipProducao;
    	
    //guia 
    public static final String PJDI003T = "http://"+ProjudiPropriedades.getInstance().getUrlServidorSPG_SSG()+"/cgi-bin/tjg-guia/"+ProjudiPropriedades.getInstance().getSPGNomeServico()+"/SPG0003P?numeroguia=";
    public static final String PJDI003T2 = "http://"+ProjudiPropriedades.getInstance().getUrlServidorSPG_SSG()+"/cgi-bin/tjg-guia/"+ProjudiPropriedades.getInstance().getSPGNomeServico()+"/SPG0003P";    
    
    //Webservice da Certidão Negativa/Positiva OBS: Substituir <numeroGuia> pelo nomero da guia.
    //public static final String URA0004T = "http://10.0.11.40/cgi-bin/tjg-capital/fordspgi/URA0004T?numeroguia=<numeroGuia>&banco=001";
    public static final String URA0004T = "http://"+ProjudiPropriedades.getInstance().getUrlServidorSPG_SSG()+"/cgi-bin/tjg-guia/"+ProjudiPropriedades.getInstance().getSPGNomeServico()+"/URA0004T?numeroguia=<numeroGuia>&banco=001";
	
    public static final String URA0006T = "http://"+ProjudiPropriedades.getInstance().getUrlServidorSPG_SSG()+"/cgi-bin/tjg-guia/"+ProjudiPropriedades.getInstance().getSPGNomeServico()+"/URA0006T?";   
    
    //Webservice da certidão pública
    public static final String SPG0021T = "http://"+ProjudiPropriedades.getInstance().getUrlServidorSPG_SSG()+"/cgi-bin/tjg-guia/"+ProjudiPropriedades.getInstance().getSPGNomeServico()+"/SPG0021T?";
    
    //Webservice da certidão pública, com retorno de comarcas
    public static final String SPG0029T = "http://"+ProjudiPropriedades.getInstance().getUrlServidorSPG_SSG()+"/cgi-bin/tjg-guia/"+ProjudiPropriedades.getInstance().getSPGNomeServico()+"/SPG0029T?";
    
    //Certidão de Prática Forense para guias emitidas pelo SPG(retorna a quantidade ou a lista de processos dos quais um determinado advogado participou)
    public static final String SPG3140T = "http://"+ProjudiPropriedades.getInstance().getUrlServidorSPG_SSG()+"/cgi-bin/tjg-guia/"+ProjudiPropriedades.getInstance().getSPGNomeServico()+"/SPG3140T?guia=";
    
    //Certidão de Prática Forense para guias emitidas pelo Projudi(retorna a lista de processos dos quais um determinado advogado participou)
    //Exemplo: https://natweb.tjgo.jus.br/cgi-bin/tjg-guia/forpspgi/SPG3142T?oab=35586&oabuf=GO&dataini=19900101&datafim=20201230&serventia=039022
    public static final String SPG3142T = "http://"+ProjudiPropriedades.getInstance().getUrlServidorSPG_SSG()+"/cgi-bin/tjg-guia/"+ProjudiPropriedades.getInstance().getSPGNomeServico()+"/SPG3142T?oab=%s&oabuf=%s&dataini=%s&datafim=%s&serventia=%s";
        
    //antecedente criminal
    public static final String SPG0008T_PATH =  "/cgi-bin/tjg-guia/"+ProjudiPropriedades.getInstance().getSPGNomeServico()+"/SPG0008T";
    public static final String SPG0009T_PATH =  "/cgi-bin/tjg-guia/"+ProjudiPropriedades.getInstance().getSPGNomeServico()+"/SPG0009T";
    
    //antecedente criminal de menor
    public static final String SPG0012T_PATH =  "/cgi-bin/tjg-guia/"+ProjudiPropriedades.getInstance().getSPGNomeServico()+"/SPG0012T";
    
    public static final int DEFESA_DIREITOS  						   = 1;
	public static final int CONCURSO_PUBLICO 			               = 2;
	public static final int CONTRATACAO_EMPREGO 	                   = 3;
	public static final int ELEITORAL 			                       = 4;
	public static final int MILITAR 	                               = 5;
	public static final int ESCLARECIMENTO_SITUACOES_INTERESSE_PESSOAL = 6;
	public static final int OUTROS = 7;
	    
    /**
     * Gerar pdf de uma publicação
     * 
     * @since 
     * @param String
     *            stIdArquivo, id de um arquivo de uma publicação (pendencia do
     *            tipo publicação)
     * @return byte[] , retorna bytes contendo a publicação em pdf
     * @throws Exception
     */    
    public byte[] gerarPdfPublicacao(String diretorioProjeto, CertidaoValidacaoDt certidaoValidacao) throws Exception {
        byte[] byTemp = null;
        String pathImage = diretorioProjeto + "imagens" + File.separator + "TesteChave3.gif";
        ByteArrayOutputStream out = null;
        
        try{
        	out = new ByteArrayOutputStream();       

        	out.write(certidaoValidacao.getCertidao());
        
        	String textoPrimeiraLinhaPDF = "Tribunal de Justiça do Estado de Goiás";
        	String textoSegundaLinhaPDF = "Documento Publicado Digitalmente em " + certidaoValidacao.getDataEmissao();
        	String textoTerceiraLinhaPDF = "Validação pelo código: " + Cifrar.codificarId_certidao(certidaoValidacao.getId()) + ", no endereço: " + ProjudiPropriedades.getInstance().getEnderecoValidacaoCertidao();
        	String textoQuartaLinhaPDF = "";

        	byTemp = EscreverTextoPDF.escreverTextoPDF(out.toByteArray(), pathImage, textoPrimeiraLinhaPDF, textoSegundaLinhaPDF, textoTerceiraLinhaPDF, textoQuartaLinhaPDF, false);
        	out.close();
        	out = null;
		} catch(Exception e) {
			try{if (out!=null) out.close();  }catch(Exception e2) {		}
			throw e;
		} 

        return byTemp;
    }
    
	/**
	 * Metodo que consulta os processos de uma certidão de antecedentes
	 * criminais
	 * 
	 * @param certidaoAntecedenteCriminalDt
	 *            - DT com os dados do requerente
	 * @return Lista com todos os processos encontrados do requerente em questão
	 * @throws Exception
	 * @author jpcpresa
	 */
	public List consultarProcessoCertidaoAntecedenteCriminal(String nome, String cpfCnpj, String dataNascimento, String nomeMae, String nomePai, String idNaturalidade, String rg, boolean menorInfrator) throws Exception {
		List listaProcessos = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			CertidaoPs obPersistencia = new CertidaoPs(obFabricaConexao.getConexao());
			 listaProcessos = obPersistencia.consultarProcessoCertidaoAntecedenteCriminal(nome, cpfCnpj, dataNascimento, nomeMae, nomePai, idNaturalidade, rg, menorInfrator);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return listaProcessos;
	}
	
    public void salvar(CertidaoValidacaoDt dados) throws Exception{
	    FabricaConexao obFabricaConexao = null;
	    LogDt obLogDt;
		try{			
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			obFabricaConexao.iniciarTransacao();
			CertidaoPs obPersistencia = new  CertidaoPs(obFabricaConexao.getConexao());
			
			obLogDt = new LogDt("Certidao", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			
			if (dados.getId() == null || dados.getId().trim().length() == 0)			
				obPersistencia.Inserir(dados);
			else 
				obPersistencia.alterar(dados);
			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();
			
		
		} finally{
			obFabricaConexao.fecharConexao();			
		}
    	
    }
    
	/**
	 * Lista as partes que possuem processo de execução penal, conforme parâmetros informados.
	 * @param cpf: parâmetro para pesquisa - cpf da parte
	 * @param nome: parâmetro para pesquisa - nome da parte
	 * @param mae: parâmetro para pesquisa - nome da mãe da parte
	 * @param dataNascimento: parâmetro para pesquisa - data de nascimento da parte
	 * @return lista: lista com partes encontradas
	 * @throws Exception
	 */
	public List getListaParteProcessoExecucao(String cpf, String nome, String mae, String dataNascimento, String numeroProcesso, String idServentia) throws Exception{
		List lista = null;
		
		lista = new ProcessoParteNe().listarPartesProcessoExecucao(cpf, nome, mae, dataNascimento, numeroProcesso, idServentia, null);
						
		return lista;
	}

	/**
	 * consulta os dados da parte
	 * @param idProcessoParte: identificação da parte
	 * @return processoParteDt
	 * @throws Exception
	 */
	public ProcessoParteDt consultarParte(String idProcessoParte) throws Exception {
		ProcessoParteDt processoParteDt = null;
		ProcessoParteNe processoParteNe = new ProcessoParteNe();
		
		processoParteDt = processoParteNe.consultarIdCompleto(idProcessoParte);

		processoParteNe = null;
		return processoParteDt;
	}
	
	/**
	 * consulta os dados do processo
	 * @param idProcesso: identificação do processo
	 * @return processoDt
	 * @throws Exception
	 */
	public ProcessoDt consultarProcesso(String idProcesso) throws Exception {
		ProcessoDt processoDt = null;
		ProcessoNe processoNe = new ProcessoNe();
		
		processoDt = processoNe.consultarIdSimples(idProcesso);

		processoNe = null;
		return processoDt;
	}
	

	public List consultarModelo(UsuarioDt usuarioDt, String id_ArquivoTipo, String tempNomeBusca, String posicaoPaginaAtual) throws Exception {
		List tempList = null;
		ModeloNe neObjeto = new ModeloNe();
		
		tempList = neObjeto.consultarModelos(tempNomeBusca, posicaoPaginaAtual, id_ArquivoTipo, usuarioDt);
		QuantidadePaginas = neObjeto.getQuantidadePaginas();

		neObjeto = null;
		return tempList;
	}
	
	public String consultarModeloJSON(UsuarioDt usuarioDt, String id_ArquivoTipo, String tempNomeBusca, String posicaoPaginaAtual) throws Exception {
		String stTemp = "";
		ModeloNe neObjeto = new ModeloNe();
		stTemp = neObjeto.consultarModelosJSON(tempNomeBusca, posicaoPaginaAtual, id_ArquivoTipo, usuarioDt);
		
		return stTemp;
	}
	
	public ModeloDt consultarModeloCodigo( String ModeloCodigo) throws Exception{
		ModeloDt tempMod = null;
		ModeloNe neObjeto = new ModeloNe();
		
		tempMod = neObjeto.consultarModeloCodigo(ModeloCodigo);
		
		neObjeto = null;
		return tempMod;
	}
	
	
	public String montaModelo(CertidaoNegativaPositivaDt certidaoDt, ModeloDt modeloDt, UsuarioNe usuarioNe) throws Exception{
        ModeloNe modeloNe = new ModeloNe();

        return modeloNe.montaConteudoPorModelo(certidaoDt, usuarioNe.getUsuarioDt(), modeloDt.getId());
    }
	
	/**
	 * Monta o modelo de certidão circunstanciada
	 * @param modeloDt
	 * @param usuarioDt
	 * @param processoDt
	 * @return String
	 * @throws Exception
	 */
	public String montaModeloCertidaoCircunstanciada(String idModelo, UsuarioDt usuarioDt, ProcessoDt processoDt) throws Exception {
        ModeloNe modeloNe = new ModeloNe();
        String texto = modeloNe.montaConteudo(idModelo, processoDt, usuarioDt, String.valueOf(ArquivoTipoDt.CERTIDAO_CIRCUNSTANCIADA_CODIGO));
        return texto;
    }

	public List consultarDescricaoEstadoCivil(String tempNomeBusca,	String posicaoPaginaAtual) throws Exception{
		List tempList = null;
		EstadoCivilNe neObjeto = new EstadoCivilNe();
		
		tempList = neObjeto.consultarDescricao(tempNomeBusca, posicaoPaginaAtual);
		QuantidadePaginas = neObjeto.getQuantidadePaginas();
		
		neObjeto = null;
		return tempList;
	}
	
	/**
	 * Realiza chamada ao objeto que efetuará a consulta
	 * @throws Exception 
	 */
	public List consultarDescricaoProfissao(String tempNomeBusca, String posicaoPaginaAtual) throws Exception{
		List tempList = null;
		ProfissaoNe neObjeto = new ProfissaoNe();
		
		tempList = neObjeto.consultarDescricao(tempNomeBusca, posicaoPaginaAtual);
		QuantidadePaginas = neObjeto.getQuantidadePaginas();
		
		neObjeto = null;
		return tempList;
	}

	/**
	 * Realiza chamada ao objeto que efetuará a consulta
	 * @throws Exception 
	 */
	public List consultarDescricaoCidade(String tempNomeBusca, String posicaoPaginaAtual) throws Exception{
		List tempList = null;
		CidadeNe neObjeto = new CidadeNe();
		
		tempList = neObjeto.consultarDescricao(tempNomeBusca, posicaoPaginaAtual);
		QuantidadePaginas = neObjeto.getQuantidadePaginas();
		
		neObjeto = null;
		return tempList;
	}
	
	public String consultarComarca(String comarcaCodigo) throws Exception{
		String temp = null;
		ComarcaNe neObjeto = new ComarcaNe();
		
		temp = neObjeto.consultarCodigo(comarcaCodigo);
		
		neObjeto = null;
		return temp;
	}
	
	 /**
     * Retorna o id de arquivo tipo para uma certidao
     * @param arquivoTipoCodigo
     * @author jpcpresa
     * @return String
     * @throws Exception
     */
    public String consultarIdArquivoTipoCertidao(int arquivoTipoCodigo) throws Exception {
        String id = "-1";
        
        ArquivoTipoNe arquivoTipoNe = new ArquivoTipoNe();
        List ids = arquivoTipoNe.consultarPeloArquivoTipoCodigo(String.valueOf(arquivoTipoCodigo));
        if (ids!=null && ids.size() > 0) id = (String) ids.get(0);
        
        return id;
    }
    
    /**
     * Retorna o Id do Arquivo Tipo
     * @param String: idArquivoCodigo
     * @return String: idArquivoTipo
     * @throws Exception
     * @author wcsilva
     */
    public String consultarIdArquivoTipo(String idArquivoTipoCodigo) throws Exception{
        String id = "";

		List ids = new ArquivoTipoNe().consultarPeloArquivoTipoCodigo(idArquivoTipoCodigo);
		if (ids.size() > 0) id = (String) ids.get(0);


        return id;
    }

    /**
     * JEsus Rodrigo
     * 19/07/2016
     * Alteração para usar o lerURL do Funções e fazer o tratamento de erros
     * @param HttpGetUrl
     * @return
     * @throws MensagemException
     */
    private Element getElementoRootXML(String HttpGetUrl)throws MensagemException{   
    	SAXBuilder builder = new SAXBuilder();
    	String xml ="";
    	Document doc = null;
		//String xml = this.getXml(HttpGetUrl);
    	try{
    		 xml = Funcoes.lerURL(HttpGetUrl);
    	}catch (Exception e){
    		throw new MensagemException("Não foi possível obter a lista dos processos SPG/SSG");
    	}
		try{
			doc = builder.build(new StringReader(xml));
		}catch(Exception e){
			throw new MensagemException("Não foi possível fazer o parse da lista de arquivos do SPG/SSG");
		}
		
		return doc.getRootElement();		
	    
    }

//	private String getXml(String HttpGetUrl)throws Exception{
//		
//			
//		DefaultHttpClient httpclient = new DefaultHttpClient();
//			HttpGet httpget = new HttpGet(HttpGetUrl);
//	//		httpget.getParams().setParameter("http.socket.timeout",new Integer(99920000));
//		
//			HttpResponse response;
//			String xml = null;
//			
//			response = httpclient.execute(httpget);
//			HttpEntity entity = response.getEntity();
//			if (entity != null) {
//				xml = EntityUtils.toString(entity);
//			}
//			
//			if(HouveErroNaChamada(xml)) 
//	
//			return xml;
//		
//	}
	
//	private boolean HouveErroNaChamada(String xml)	{
//		if (xml == null) return true;
//		
//		if (xml.trim().toUpperCase().contains("HTML")) return true;
//		
//		return !xml.trim().toUpperCase().contains("XML");
//	}

	public  CertidaoNegativaPositivaDt getDtGuia(String numeroGuia) throws Exception  {
		CertidaoNegativaPositivaDt cnp = null;
		try{
			Element dadosCertidao = getElementoRootXML(PJDI003T + numeroGuia);
						
			List elements = dadosCertidao.getChildren();
			
			if (elements.size() > 1) {
						
				String nome = dadosCertidao.getChild("NomeAutor").getText();
				String cpfCnpj = dadosCertidao.getChild("CpfCnpj").getText();
				String aux = cpfCnpj;
				cpfCnpj = cpfCnpj.replaceAll("-", "");
				cpfCnpj = cpfCnpj.replaceAll("/", "");
				cpfCnpj = cpfCnpj.replaceAll("\\.", "");
				
				String tipoPessoa = dadosCertidao.getChild("TipoPessoa").getText();
				tipoPessoa = Funcoes.removeEspacosExcesso(tipoPessoa);
				String dataNascimento = dadosCertidao.getChild("DataNascimento").getText().trim();
				String nomeMae = dadosCertidao.getChild("NomeMae").getText().trim();
				String nomePai = dadosCertidao.getChild("NomePai").getText();
				String sexo = dadosCertidao.getChild("Sexo").getText();
				String rg = dadosCertidao.getChild("NumeroIdentidade").getText();
				String estadoCivil = dadosCertidao.getChild("EstadoCivil").getText();
				String profissao = dadosCertidao.getChild("NomeProfissao").getText();
				String domicilio = dadosCertidao.getChild("NomeMunicipio").getText() + " - " + dadosCertidao.getChild("NomeEstado").getText();
				String nacionalidade = dadosCertidao.getChild("Nacionalidade").getText();
				String area = dadosCertidao.getChild("Area").getText();
				area = Funcoes.removeEspacosExcesso(area);
				area = area.matches("[Cc][rR][iI][mM][iI][nN][aA][Ll]") ? String.valueOf(AreaDt.CRIMINAL) : String.valueOf(AreaDt.CIVEL);
				String serventia = dadosCertidao.getChild("Escrivania").getText();
				String comarca = dadosCertidao.getChild("Comarca").getText();
				String comarcaCodigo = dadosCertidao.getChild("CodigoComarca").getText();			 
				 
				cnp = new CertidaoNegativaPositivaDt(nome,cpfCnpj,tipoPessoa,dataNascimento,nomeMae,nomePai,sexo,rg,estadoCivil,profissao,domicilio,nacionalidade,area,serventia,comarca);
				cnp.setComarcaCodigo(comarcaCodigo);
				cnp.setCnpjCompleto(aux);
			}
		}catch(Exception e) {
			throw new MensagemException("Erro ao acessar o sistema SPG, favor tentar novamente mais tarde!\n" + e.toString() );
		}
		
		return cnp;
	}

	public List getListaProcesso(CertidaoNegativaPositivaDt certidaoNegativaPositivaDt) throws Exception {
		List lista = null;
		
		ProcessoNe processoNe = new ProcessoNe();
		lista = processoNe.consultarProcessoCertdiaoNegativaPositiva(certidaoNegativaPositivaDt);
				
		return lista;
	}
	


		public String montaModeloMemoria(CertidaoNegativaPositivaDt certidaoNegativaPositivaDt, ModeloDt modeloDt, UsuarioNe usuario) throws Exception {
			 ModeloNe modeloNe = new ModeloNe();
			 String texto = null;
		        
			texto =  modeloNe.montaConteudoPorModeloMemoria(certidaoNegativaPositivaDt, usuario.getUsuarioDt(), modeloDt.getId());
							
			return texto;
		}

	
		public String montaModelo(CertidaoAntecedenteCriminalDt certidaoAntecedenteCriminalDt, ModeloDt modeloDt, UsuarioNe usuarioNe) throws Exception {
			 ModeloNe modeloNe = new ModeloNe();
		       
			return modeloNe.montaConteudoPorModelo(certidaoAntecedenteCriminalDt, usuarioNe.getUsuarioDt(), modeloDt.getId());
				
				
		}
		
		/**
		 * Método que gera o texto da certidão de Prática Forense retorna o modelo com o texto carregado.
		 * 
		 * @param certidaoDt
		 * @param modeloDt
		 * @param usuarioSessao
		 * @return Modelo com texto setado
		 */
		public String montaModelo(CertidaoPraticaForenseDt certidaoDt, ModeloDt modeloDt, UsuarioNe usuarioNe) throws Exception {
			ModeloNe modeloNe = new ModeloNe();
		        
			return modeloNe.montaConteudoPorModelo(certidaoDt, usuarioNe.getUsuarioDt(), modeloDt.getId());
							
		}

		public String montaModeloMemoria(CertidaoAntecedenteCriminalDt certidaoAntecedenteCriminalDt, ModeloDt modeloDt, UsuarioNe usuarioSessao) throws Exception {
			ModeloNe modeloNe = new ModeloNe();
			 String texto = null;
		        
				texto =  modeloNe.montaConteudoPorModeloMemoria(certidaoAntecedenteCriminalDt, usuarioSessao.getUsuarioDt(), modeloDt);
				
				return texto;
		}

		public List getListaProcesso(String nome, String cpfCnpj, String dataNascimento, String nomeMae, String nomePai, String idNaturalidade, String rg, boolean menorInfrator) throws Exception {
			List lista = null;
			
			CertidaoNe processoNe = new CertidaoNe();
			lista = processoNe.consultarProcessoCertidaoAntecedenteCriminal(nome, cpfCnpj, dataNascimento, nomeMae, nomePai, idNaturalidade, rg, menorInfrator);
			
			return lista;
		}
		
		public List getListaProcesso(CertidaoPraticaForenseDt certidaoDt) throws Exception {
			List lista = null;
			
			ProcessoNe processoNe = new ProcessoNe();
			lista = processoNe.consultarProcessoPraticaForenseAdvogado(certidaoDt);
			
			return lista;
		}
		
		
	public List listarProcessosNP(List processolist) throws Exception {
		List lista = null;
		
		ProcessoNe processoNe = new ProcessoNe();
		
		if (processolist.size() < 1000) {

			lista = processoNe.listarProcessoIdNP(processolist, ProcessoSubtipoDt.NAO_GERAR_POSITIVACAO);
			
		} else {
			
			lista = processoNe.listarProcessoIdNP(processolist.subList(0, 1000), ProcessoSubtipoDt.NAO_GERAR_POSITIVACAO);
			
			Iterator it = processolist.subList(0, 1000).iterator();
			StringBuffer str = new StringBuffer();
			while (it.hasNext()) {
				String aux = ((ProcessoCertidaoPositivaNegativaDt)it.next()).getId_Processo();
				str.append(aux + ", ");
			}
			int tamanho = str.length();
			str.delete(tamanho - 2, tamanho - 1);
			//System.out.println(str.toString());
		}

		return lista;
	}
	
	public List listarProcessosNPSegundoGrau(List processolist) throws Exception {
		List lista = null;
		
		ProcessoNe processoNe = new ProcessoNe();
		
		if (processolist.size() < 1000) {

			lista = processoNe.listarProcessoIdNP(processolist, ProcessoSubtipoDt.NAO_GERAR_POSITIVACAO_SEGUNDO_GRAU);
			
		} else {
			
			lista = processoNe.listarProcessoIdNP(processolist.subList(0, 1000), ProcessoSubtipoDt.NAO_GERAR_POSITIVACAO_SEGUNDO_GRAU);
			
			Iterator it = processolist.subList(0, 1000).iterator();
			StringBuffer str = new StringBuffer();
			while (it.hasNext()) {
				String aux = ((ProcessoCertidaoPositivaNegativaDt)it.next()).getId_Processo();
				str.append(aux + ", ");
			}
			int tamanho = str.length();
			str.delete(tamanho - 2, tamanho - 1);
			//System.out.println(str.toString());
			
		}

		return lista;
	}
		
	public List listarProcessoCertidaoNP(CertidaoNegativaPositivaDt certidaoDt) throws Exception {
		
		List listRetorno = null;
		FabricaConexao obFabricaConexao = null;
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			CertidaoPs obPersistencia = new CertidaoPs(obFabricaConexao.getConexao());
			listRetorno = obPersistencia.consultarProcessosCertidaoNP(certidaoDt);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return listRetorno;
	}
	
	// Válido para CertidaoNegativaPositivaPublicaDt
	public List listarProcessoCertidaoNP(CertidaoNegativaPositivaPublicaDt certidaoDt) throws Exception {
		
		List listRetorno = null;
		FabricaConexao obFabricaConexao = null;
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			CertidaoPs obPersistencia = new CertidaoPs(obFabricaConexao.getConexao());
			listRetorno = obPersistencia.consultarProcessosCertidaoNP(certidaoDt);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return listRetorno;
	}
	
	/**
	 * Consulta de processos de primeiro grau para a certidão negativa positiva.
	 * @param certidaoDt - dados da certidão a ser consultada
	 * @return lista de processos da certidão
	 * @throws Exception
	 * @author hmgodinho, jrcorrea
	 */
	public List consultarProcessosPrimeiroGrauCertidaoNP(CertidaoNegativaPositivaDt certidaoDt) throws Exception {

		List listRetorno = null;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			CertidaoPs obPersistencia = new CertidaoPs(obFabricaConexao.getConexao());
			listRetorno = obPersistencia.consultarProcessosPrimeiroGrauCertidaoNP(certidaoDt);
		} catch (Exception e) {
			throw new MensagemException("Erro ao acessar a base de dados do Projudi, favor tentar novamente mais tarde!");
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return listRetorno;
	}
		
	
	public boolean fimJudicial(List id_Processo) throws Exception {
		boolean fimJudicial = true;
		FabricaConexao obFabricaConexao = null;			
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			CertidaoPs obPersistencia = new CertidaoPs(obFabricaConexao.getConexao());
			fimJudicial = obPersistencia.temFimJudicial(id_Processo);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return fimJudicial;
	}
	
	//Consulta processos do SPG para certidão pública 
	public CertidaoNegativaPositivaDt getListaProcessoSPGPublica(CertidaoNegativaPositivaDt certidao2) throws Exception{

		String nomePessoa = "nomepessoa=";
		String nomeMae = "nomemae=";
		String cpf = "cpf="; 
		String dataNasc = "nascimento=";
		String area = "area=";
		String comarca = "comarca=";
		String separador = "&";
		String areaCivelCriminal = "0";
		
		String urlAux = "";
		if (certidao2.getNome() != null && !certidao2.getNome().isEmpty()) {
			urlAux+=nomePessoa + Funcoes.retirarAcentos(certidao2.getNome().trim());
		}
		if (certidao2.getNomeMae() != null && !certidao2.getNomeMae().isEmpty()) {
			if (!urlAux.isEmpty()) urlAux = urlAux + separador;			
			urlAux = urlAux.concat(nomeMae + Funcoes.retirarAcentos(certidao2.getNomeMae().trim()));
		}
		if (certidao2.getCpfCnpj() != null && !certidao2.getCpfCnpj().isEmpty()) {
			if (!urlAux.isEmpty()) urlAux = urlAux + separador;
			urlAux = urlAux.concat(cpf+certidao2.getCpfCnpj().trim());
		}
		if (certidao2.getDataNascimento() != null && !certidao2.getDataNascimento().isEmpty()) {				
			String data = certidao2.getDataNascimento().trim();
			data = Funcoes.BancoData(data);
			data = data.replaceAll("-", "");
			data = data.replaceAll("'", "");
			
			if (!urlAux.isEmpty()) urlAux = urlAux + separador;
			urlAux = urlAux.concat(dataNasc+data);				
		}
		
		if (!urlAux.isEmpty()) urlAux = urlAux + separador;
		if (certidao2.isCivel() || certidao2.isCriminal()){			
			urlAux = urlAux.concat(area + Funcoes.StringToInt(certidao2.getAreaCodigo()));			
		}else{ 
			urlAux = urlAux.concat(area+areaCivelCriminal);
		}
		
		if (certidao2.getComarcaCodigo() != null && Funcoes.StringToInt(certidao2.getComarcaCodigo()) > 0) {
			if (!urlAux.isEmpty()) urlAux = urlAux + separador;			
			urlAux = urlAux.concat(comarca + certidao2.getComarcaCodigo().trim());
		}
		
		urlAux = SPG0021T + urlAux.replaceAll(" ", "%20");
			
		List lista = new ArrayList();
					
		Element certidao = getElementoRootXML(urlAux);
		
		List elements = certidao.getChildren("Processo");
		Iterator i = elements.iterator();
		
		certidao2.setValorCertidao(certidao.getChildText("ValorCertidao"));
		certidao2.setAbrange(certidao.getChildText("Abrange"));
		certidao2.setValorTaxa(certidao.getChildText("ValorTaxa"));
		if(certidao2.temFimJudical())
			certidao2.setFimJudicial(certidao.getChildText("Fins") != null && !certidao.getChildText("Fins").isEmpty());
		certidao2.setValortotal(certidao.getChildText("Valortotal"));
		certidao2.setDataApresentacao(certidao.getChildText("DataApresentacao"));
		
		while (i.hasNext()) {
			ProcessoCertidaoPositivaNegativaDt p = new ProcessoCertidaoPositivaNegativaDt(); 
			Element element = (Element) i.next();
			p.setProcessoNumero(element.getChildText("Numero"));
			p.setServentia(element.getChildText("Juizo"));
			p.setPromovidoNome(element.getChildText("Requerido"));
			p.addPromovente(element.getChildText("Requerente"));
			p.addPromoventeAdvogado(element.getChildText("AdvRequerente"));
			p.addPromovidoAdvogado(element.getChildText("AdvRequerido"));
			p.setProcessoTipo(element.getChildText("Natureza"));
			p.setDataRecebimento(element.getChildText("Distribuicao"));
			p.setValor(element.getChildText("ValorAcao"));
			p.setPromovidoNomeMae(element.getChildText("Mae"));
			p.setPromovidoDataNascimento(element.getChildText("DataNascimento"));
			p.setPromovidoCpf(element.getChildText("Cpf"));
			p.addAssunto(element.getChildText("Assunto"));
			p.setTipo(element.getChildText("Tipo"));
			p.setAverbacao(element.getChildText("Averbacao"));
			p.setDesmembrado(element.getChildText("Desmembrado"));
			p.setConversao(element.getChildText("Conversao"));
			try
			{
				p.setComarca(element.getChildText("Comarca"));
			} catch(Exception e) {					
				p.setComarca("");
			}					
			p.setSistema("SPG");
			lista.add(p);
		}
											 					
		certidao2.getListaProcessos().addAll(lista);		
		
		return certidao2;						
	}
	
	
	//Consulta processos do SPG para certidão pública 
	// Válido para CertidaoNegativaPositivaPublicaDt
	public CertidaoNegativaPositivaDt getListaProcessoSPGPublica(CertidaoNegativaPositivaPublicaDt certidao2) throws Exception{

		String nomePessoa = "nomepessoa=";
		String nomeMae = "nomemae=";
		String cpf = "cpf="; 
		String dataNasc = "nascimento=";
		String area = "area=";
		String comarca = "comarca=";
		String separador = "&";
		String areaCivelCriminal = "0";
		
		String urlAux = "";
		if (certidao2.getNome() != null && !certidao2.getNome().isEmpty()) {
			urlAux+=nomePessoa + Funcoes.retirarAcentos(certidao2.getNome().trim());
		}
		if (certidao2.getNomeMae() != null && !certidao2.getNomeMae().isEmpty()) {
			if (!urlAux.isEmpty()) urlAux = urlAux + separador;			
			urlAux = urlAux.concat(nomeMae + Funcoes.retirarAcentos(certidao2.getNomeMae().trim()));
		}
		if (certidao2.getCpfCnpj() != null && !certidao2.getCpfCnpj().isEmpty()) {
			if (!urlAux.isEmpty()) urlAux = urlAux + separador;
			urlAux = urlAux.concat(cpf+certidao2.getCpfCnpj().trim());
		}
		if (certidao2.getDataNascimento() != null && !certidao2.getDataNascimento().isEmpty()) {				
			String data = certidao2.getDataNascimento().trim();
			data = Funcoes.BancoData(data);
			data = data.replaceAll("-", "");
			data = data.replaceAll("'", "");
			
			if (!urlAux.isEmpty()) urlAux = urlAux + separador;
			urlAux = urlAux.concat(dataNasc+data);				
		}
		
		if (!urlAux.isEmpty()) urlAux = urlAux + separador;
		if (certidao2.isCivel() || certidao2.isCriminal()){			
			urlAux = urlAux.concat(area + Funcoes.StringToInt(certidao2.getAreaCodigo()));			
		}else{ 
			urlAux = urlAux.concat(area+areaCivelCriminal);
		}
		
		if (certidao2.getComarcaCodigo() != null && Funcoes.StringToInt(certidao2.getComarcaCodigo()) > 0) {
			if (!urlAux.isEmpty()) urlAux = urlAux + separador;			
			urlAux = urlAux.concat(comarca + certidao2.getComarcaCodigo().trim());
		}
		
		urlAux = SPG0021T + urlAux.replaceAll(" ", "%20");
			
		List lista = new ArrayList();
					
		Element certidao = getElementoRootXML(urlAux);
		
		List elements = certidao.getChildren("Processo");
		Iterator i = elements.iterator();
		
		certidao2.setValorCertidao(certidao.getChildText("ValorCertidao"));
		certidao2.setAbrange(certidao.getChildText("Abrange"));
		certidao2.setValorTaxa(certidao.getChildText("ValorTaxa"));
		if(certidao2.temFimJudical())
			certidao2.setFimJudicial(certidao.getChildText("Fins") != null && !certidao.getChildText("Fins").isEmpty());
		certidao2.setValortotal(certidao.getChildText("Valortotal"));
		certidao2.setDataApresentacao(certidao.getChildText("DataApresentacao"));
		
		while (i.hasNext()) {
			ProcessoCertidaoPositivaNegativaDt p = new ProcessoCertidaoPositivaNegativaDt(); 
			Element element = (Element) i.next();
			p.setProcessoNumero(element.getChildText("Numero"));
			p.setServentia(element.getChildText("Juizo"));
			p.setPromovidoNome(element.getChildText("Requerido"));
			p.addPromovente(element.getChildText("Requerente"));
			p.addPromoventeAdvogado(element.getChildText("AdvRequerente"));
			p.addPromovidoAdvogado(element.getChildText("AdvRequerido"));
			p.setProcessoTipo(element.getChildText("Natureza"));
			p.setDataRecebimento(element.getChildText("Distribuicao"));
			p.setValor(element.getChildText("ValorAcao"));
			p.setPromovidoNomeMae(element.getChildText("Mae"));
			p.setPromovidoDataNascimento(element.getChildText("DataNascimento"));
			p.setPromovidoCpf(element.getChildText("Cpf"));
			p.addAssunto(element.getChildText("Assunto"));
			p.setTipo(element.getChildText("Tipo"));
			p.setAverbacao(element.getChildText("Averbacao"));
			p.setDesmembrado(element.getChildText("Desmembrado"));
			p.setConversao(element.getChildText("Conversao"));
			try
			{
				p.setComarca(element.getChildText("Comarca"));
			} catch(Exception e) {					
				p.setComarca("");
			}					
			p.setSistema("SPG");
			lista.add(p);
		}
											 					
		certidao2.getListaProcessos().addAll(lista);		
		
		return certidao2;						
	}
	
	public List<String> getListaNomesComarcasComProcessoSPGPublica(CertidaoNegativaPositivaPublicaDt certidao2) throws MensagemException, Exception{

		String nomePessoa = "nomepessoa=";
		String nomeMae = "nomemae=";
		String cpf = "cpf="; 
		String dataNasc = "nascimento=";
		String area = "area=";
		String comarca = "comarca=";
		String separador = "&";
		String areaCivelCriminal = "0";
		
		List<String> listaNomesComarcasComProcessoSPGPublica = null;
		try{
			String urlAux = "";
			if (certidao2.getNome() != null && !certidao2.getNome().isEmpty()) {
				urlAux+=nomePessoa + Funcoes.retirarAcentos(certidao2.getNome().trim());
			}
			if (certidao2.getNomeMae() != null && !certidao2.getNomeMae().isEmpty()) {
				if (!urlAux.isEmpty()) urlAux = urlAux + separador;			
				urlAux = urlAux.concat(nomeMae + Funcoes.retirarAcentos(certidao2.getNomeMae().trim()));
			}
			if (certidao2.getCpfCnpj() != null && !certidao2.getCpfCnpj().isEmpty()) {
				if (!urlAux.isEmpty()) urlAux = urlAux + separador;
				urlAux = urlAux.concat(cpf+certidao2.getCpfCnpj().trim());
			}
			if (certidao2.getDataNascimento() != null && !certidao2.getDataNascimento().isEmpty()) {				
				String data = certidao2.getDataNascimento().trim();
				
				if (!urlAux.isEmpty()) urlAux = urlAux + separador;
				urlAux = urlAux.concat(dataNasc+data);				
			}
			
			if (!urlAux.isEmpty()) urlAux = urlAux + separador;
			
			if (certidao2.isCivel() || certidao2.isCriminal()){			
				urlAux = urlAux.concat(area + Funcoes.StringToInt(certidao2.getAreaCodigo()));			
			}else{ 
				urlAux = urlAux.concat(area+areaCivelCriminal);
			}
			
			if (certidao2.getComarcaCodigo() != null && Funcoes.StringToInt(certidao2.getComarcaCodigo()) > 0) {
				if (!urlAux.isEmpty()) urlAux = urlAux + separador;			
				urlAux = urlAux.concat(comarca + certidao2.getComarcaCodigo().trim());
			}
			
			urlAux = SPG0029T + urlAux.replaceAll(" ", "%20");
				
			listaNomesComarcasComProcessoSPGPublica = new ArrayList<String>();
							
			Element certidao = getElementoRootXML(urlAux);
			
			Element comarcas = certidao.getChild("Comarcas");
			
			List elements = comarcas.getChildren("Comarca");
			Iterator i = elements.iterator();
			
			while (i.hasNext()) {
				Element element = (Element) i.next();
				if (!listaNomesComarcasComProcessoSPGPublica.contains(element.getText()))
					listaNomesComarcasComProcessoSPGPublica.add(element.getText());					
			}
		} catch(Exception e) {									
			throw new MensagemException("Erro ao acessar o sistema SPG, favor tentar novamente mais tarde!");
		}
												 												
		return listaNomesComarcasComProcessoSPGPublica;						
	}
	
	//Consulta processos do SPG para a função dos distribuidores
	public CertidaoNegativaPositivaDt getListaProcessoSPG(CertidaoNegativaPositivaDt certidao2) throws Exception{
		List lista = new ArrayList();
		
		if (certidao2.getGuiaEmissaoCertidao() == null) 
			certidao2.setGuiaEmissaoCertidao(ObtenhaGuiaEmissaoCertidao(certidao2.getNumeroGuia()));		
		
		String numeroDaGuia = "";		
		if (certidao2.getGuiaEmissaoCertidao() == null)
			numeroDaGuia = certidao2.getNumeroGuia() + "09";
		else 
			numeroDaGuia = certidao2.getGuiaEmissaoCertidao().getNumeroGuiaCompleto();
					
		Element certidao = getElementoRootXML(URA0004T.replaceAll("<numeroGuia>", numeroDaGuia));
	
		//tratamento de mensagem de erro que pode ser retornada pelo SPG
		if(certidao.getChild("msg") != null) {
			String msg = certidao.getChildText("msg");
			throw new MensagemException("ErroSPG=" +"SPG: "+ msg);
		}
		
		List elements = certidao.getChildren("Processo");
		Iterator i = elements.iterator();
		
		certidao2.setValorCertidao(certidao.getChildText("ValorCertidao"));
		certidao2.setAbrange(certidao.getChildText("Abrange"));
		certidao2.setValorTaxa(certidao.getChildText("ValorTaxa"));
		if(certidao2.temFimJudical())
			certidao2.setFimJudicial(certidao.getChildText("Fins") != null && !certidao.getChildText("Fins").isEmpty());
		certidao2.setValortotal(certidao.getChildText("Valortotal"));
		certidao2.setDataApresentacao(certidao.getChildText("DataApresentacao"));
		
		while (i.hasNext()) {
			ProcessoCertidaoPositivaNegativaDt p = new ProcessoCertidaoPositivaNegativaDt(); 
			Element element = (Element) i.next();
			p.setProcessoNumero(element.getChildText("Numero"));
			p.setServentia(element.getChildText("Juizo"));
			p.setPromovidoNome(element.getChildText("Requerido"));
			p.setCertidaoPromoventeNome(element.getChildText("Requerente"));
			p.setNomeAdvogadoPromovente(element.getChildText("AdvRequerente"));
			p.setNomeAdvogadoPromovido(element.getChildText("AdvRequerido"));
			p.setProcessoTipo(element.getChildText("Natureza"));
			p.setDataRecebimento(element.getChildText("Distribuicao"));
			p.setValor(element.getChildText("ValorAcao"));
			p.setPromovidoNomeMae(element.getChildText("Mae"));
			p.setPromovidoDataNascimento(element.getChildText("DataNascimento"));
			p.setPromovidoCpf(element.getChildText("Cpf"));
			p.addAssunto(element.getChildText("Assunto"));
			p.setAverbacao(element.getChildText("Averbacao"));
			p.setSistema("SPG");
			lista.add(p);
		}
					
		certidao2.getListaProcessos().addAll(lista);
		
		return certidao2;
		
	}
	
	
	//Consulta os processos de antecedente criminal do SPG
	
	
	public List getListaProcessoSPG(String nome, String cpfCnpj, String dataNascimento, String nomeMae, String nomePai, String naturalidade, String rg) throws Exception{
		//Os atributos referentes ao ISN servem para controlar a repetição da consulta. A intenção é trazer, no máximo,
		//500 registros por vez. Quando trazia mais que isso de uma vez só, dava problema no SPG.
		String ISN = "isn";
		boolean isnValidador = true;
		int isnValor = 0;
		
		List lista = new ArrayList();
		String msgErro = "";

		SAXBuilder builder = new SAXBuilder();
		UriBuilder ub = new UriBuilder(host, SPG0009T_PATH);
		
		while(isnValidador) {
			
			nome = nome.toUpperCase();
			nome = Funcoes.retirarAcentos(nome);
			ub.adicionarArgumento("nome",	nome);
			
			if (nomeMae != null && !nomeMae.isEmpty()) {
				nomeMae = nomeMae.toUpperCase();
				nomeMae = Funcoes.retirarAcentos(nomeMae);
				ub.adicionarArgumento("nomemae", nomeMae);
			}
			
			if (dataNascimento != null && !dataNascimento.isEmpty()) {
				ub.adicionarArgumento("datanasc", dataNascimento);
			}
			
			String cpf = cpfCnpj;
			if (cpf != null && !cpf.isEmpty()) {
				ub.adicionarArgumento("cpf", cpf);
			}
			
			if (nomePai != null && !nomePai.isEmpty()) {
				nomePai = nomePai.toUpperCase();
				nomePai = Funcoes.retirarAcentos(nomePai);
				ub.adicionarArgumento("nomepai", nomePai);
			}
			
			if (naturalidade != null && !naturalidade.isEmpty()) {
				ub.adicionarArgumento("naturalidade", naturalidade);
			}
			
			if (rg != null && !rg.isEmpty()) {
				ub.adicionarArgumento("rg", rg);
			}
			
			ub.adicionarArgumento(ISN, String.valueOf(isnValor));
			
			Element certidao = this.getElementoRootXML(ub.getURI());
			if(certidao.getChild("msgErro") != null) {
				msgErro = certidao.getChildText("msgErro");
				throw new MensagemException("ErroSPG=" +"SPG: "+ msgErro);
			}
			List elements = certidao.getChildren("processo");
			Iterator i = elements.iterator();
											
			while (i.hasNext()) {
				ProcessoAntecedenteCriminalDt p = new ProcessoAntecedenteCriminalDt(); 
				Element element = (Element) i.next();
				p.setProcessoNumero(element.getChildText("numeroProcesso"));
				p.setServentia(element.getChildText("serventia"));
				p.setComarca(element.getChildText("comarca"));
				p.setPromovidoNome(element.getChildText("promovido"));
				p.addPromovente(element.getChildText("promovente"));
				p.setProcessoTipo(element.getChildText("processoTipo"));
				p.setProcessoResponsavel(element.getChildText("juiz"));
				p.setDataRecebimento(element.getChildText("dataDistribuicao"));
				p.setDataTransitoJulgado(element.getChildText("dataTransitoJulgado"));
				p.setPromovidoNomeMae(element.getChildText("filiacao"));
				p.setPromovidoCpf(element.getChildText("cpf"));
				p.setPromovidoDataNascimento(element.getChildText("nascimento"));
				p.setNomeVitima(element.getChildText("vitima"));
				p.addAssunto(element.getChildText("assunto"));
				p.addInfracao(element.getChildText("lei"));
				p.setFase(element.getChildText("fase"));
				p.setSentenca(element.getChildText("sentenca"));
				p.setDataFato(element.getChildText("DataFato").trim());
				p.setDataBaixa(element.getChildText("dataBaixa").trim());
				p.setMotivoBaixa(element.getChildText("motivoBaixa").trim());
				String transacao = element.getChildText("transacao").trim();
				String sursis = element.getChildText("sursis").trim();

				isnValor++;
				if(!transacao.isEmpty()) {
					p.addBeneficio(new BeneficioCertidaoAntecedenteCriminalDt(transacao, "Transação Penal", "Não informado"));
				} else if(!sursis.isEmpty()) {
					BeneficioCertidaoAntecedenteCriminalDt bene = new BeneficioCertidaoAntecedenteCriminalDt(sursis, "Sursis", "Não informado");
					bene.setCondicional(element.getChildText("condicoes").trim());
					bene.setCondicoes(element.getChildText("condicional").trim());
					p.addBeneficio(bene);
				}
				p.setSistema("SPG");
				lista.add(p);
			}
			
			//se o valor do ISN chegar aqui zerado ou com resultado da divisão diferente de 0, deve sair 
			if(isnValor == 0 || isnValor%100 > 0) {
				isnValidador = false;
			} else {
				//se o resultado da divisão for zero (número múltiplo de 100), vai executar novamente a consulta.
				//é preciso reinicializar essas variáveis para não dar erro na próxima execução.
				builder = new SAXBuilder();
				ub = new UriBuilder(host, SPG0009T_PATH);
			}					
				
		}
											 
		return lista;
						
	}
	
	public List getListaProcessoMenorInfratorSPG(String nome, String cpfCnpj, String dataNascimento, String nomeMae, String nomePai, String naturalidade, String rg) throws Exception{
		//Os atributos referentes ao ISN servem para controlar a repetição da consulta. A intenção é trazer, no máximo,
		//500 registros por vez. Quando trazia mais que isso de uma vez só, dava problema no SPG.
		String ISN = "isn";
		boolean isnValidador = true;
		int isnValor = 0;
		
		List lista = new ArrayList();
		String msgErro = "";
		

		SAXBuilder builder = new SAXBuilder();
		UriBuilder ub = new UriBuilder(host, SPG0012T_PATH);
		
		while(isnValidador) {
			
			nome = nome.toUpperCase();
			nome = Funcoes.retirarAcentos(nome);
			ub.adicionarArgumento("nome",	nome);
			
			if (nomeMae != null && !nomeMae.isEmpty()) {
				nomeMae = nomeMae.toUpperCase();
				nomeMae = Funcoes.retirarAcentos(nomeMae);
				ub.adicionarArgumento("nomemae", nomeMae);
			}
			
			if (dataNascimento != null && !dataNascimento.isEmpty()) {
				ub.adicionarArgumento("datanasc", dataNascimento);
			}
			
			String cpf = cpfCnpj;
			if (cpf != null && !cpf.isEmpty()) {
				ub.adicionarArgumento("cpf", cpf);
			}
			
			if (nomePai != null && !nomePai.isEmpty()) {
				nomePai = nomePai.toUpperCase();
				nomePai = Funcoes.retirarAcentos(nomePai);
				ub.adicionarArgumento("nomepai", nomePai);
			}
			
			if (naturalidade != null && !naturalidade.isEmpty()) {
				ub.adicionarArgumento("naturalidade", naturalidade);
			}
			
			if (rg != null && !rg.isEmpty()) {
				ub.adicionarArgumento("rg", rg);
			}
			
			ub.adicionarArgumento(ISN, String.valueOf(isnValor));
			
			Element certidao = this.getElementoRootXML(ub.getURI());
			if(certidao.getChild("msgErro") != null) {
				msgErro = certidao.getChildText("msgErro");
				throw new MensagemException("ErroSPG=" +"SPG: "+ msgErro);
			}
			List elements = certidao.getChildren("processo");
			Iterator i = elements.iterator();
											
			while (i.hasNext()) {
				ProcessoAntecedenteCriminalDt p = new ProcessoAntecedenteCriminalDt(); 
				Element element = (Element) i.next();
				p.setProcessoNumero(element.getChildText("numeroProcesso"));
				p.setServentia(element.getChildText("serventia"));
				p.setComarca(element.getChildText("comarca"));
				p.setPromovidoNome(element.getChildText("promovido"));
				p.addPromovente(element.getChildText("promovente"));
				p.setProcessoTipo(element.getChildText("processoTipo"));
				p.setProcessoResponsavel(element.getChildText("juiz"));
				p.setDataRecebimento(element.getChildText("dataDistribuicao"));
				p.setDataTransitoJulgado(element.getChildText("dataTransitoJulgado"));
				p.setPromovidoNomeMae(element.getChildText("filiacao"));
				p.setNomeVitima(element.getChildText("vitima"));
				p.setPromovidoCpf(element.getChildText("cpf"));
				p.setPromovidoDataNascimento(element.getChildText("nascimento"));
				p.addAssunto(element.getChildText("assunto"));
				p.addInfracao(element.getChildText("lei"));
				p.setFase(element.getChildText("fase"));
				p.setSentenca(element.getChildText("sentenca"));
				p.setDataRecebimentoDenuncia(element.getChildText("RecebeDenuncia"));
				p.setDataFato(element.getChildText("DataFato").trim());
				p.setDataBaixa(element.getChildText("dataBaixa").trim());
				p.setMotivoBaixa(element.getChildText("motivoBaixa").trim());
				String transacao = element.getChildText("transacao").trim();
				String sursis = element.getChildText("sursis").trim();

				isnValor++;
				if(!transacao.isEmpty()) {
					p.addBeneficio(new BeneficioCertidaoAntecedenteCriminalDt(transacao, "Transação Penal", "Não informado"));
				} else if(!sursis.isEmpty()) {
					BeneficioCertidaoAntecedenteCriminalDt bene = new BeneficioCertidaoAntecedenteCriminalDt(sursis, "Sursis", "Não informado");
					bene.setCondicional(element.getChildText("condicoes").trim());
					bene.setCondicoes(element.getChildText("condicional").trim());
					p.addBeneficio(bene);
				}
				p.setSistema("SPG");
				lista.add(p);
			}
			
			//se o valor do ISN chegar aqui zerado ou com resultado da divisão diferente de 0, deve sair 
			if(isnValor == 0 || isnValor%100 > 0) {
				isnValidador = false;
			} else {
				//se o resultado da divisão for zero (número múltiplo de 100), vai executar novamente a consulta.
				//é preciso reinicializar essas variáveis para não dar erro na próxima execução.
				builder = new SAXBuilder();
				ub = new UriBuilder(host, SPG0012T_PATH);
			}					
				
		}
											 
		return lista;
						
	}

	public long getQuantidadeProcessosAdvogado(CertidaoPraticaForenseDt certidaoDt) throws Exception {
		long quantidade = 0;
					
		ProcessoNe processoNe = new ProcessoNe();
		quantidade =  processoNe.consultarQuantidadeProcessosAdvogado(certidaoDt);
					
		return quantidade;
	}

	public String montaModelo(CertidaoExecucaoCPCDt certidaoExecucaoCPCDt, ModeloDt modeloDt, UsuarioNe usuarioNe) throws Exception {
		ModeloNe modeloNe = new ModeloNe();
	        
		return modeloNe.montaConteudoPorModelo(certidaoExecucaoCPCDt, usuarioNe.getUsuarioDt(), modeloDt.getId());
							
	}

	public CertidaoExecucaoCPCDt getProcessoExecucaoCPC(String numero) throws Exception {
		
		CertidaoExecucaoCPCDt certidao = null;

		ProcessoNe processoNe = new ProcessoNe();
		certidao =  processoNe.getProcessoExecucaoCPC(numero);
		
		return certidao;
		
	}

	public List getPartes(List aux) throws Exception {
		List listRetorno = null;
		FabricaConexao obFabricaConexao = null;			
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			CertidaoPs obPersistencia = new CertidaoPs(obFabricaConexao.getConexao());
			listRetorno = obPersistencia.getPartes(aux);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return listRetorno;		
		}

	public List listarProcessoCertidaoNPSegundoGrau(CertidaoSegundoGrauNegativaPositivaDt certidaoDt) throws Exception {
		
		List listRetorno = null;
		FabricaConexao obFabricaConexao = null;
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			CertidaoPs obPersistencia = new  CertidaoPs(obFabricaConexao.getConexao());
			
			listRetorno = obPersistencia.consultarProcessosCertidaoNPSegundoGrau(certidaoDt);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return listRetorno;
	}
	
	public boolean negativarPFisicaPJuricaSegundoGrau(CertidaoNegativaPositivaPublicaDt certidaoDt) throws Exception {
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			CertidaoPs obPersistencia = new  CertidaoPs(obFabricaConexao.getConexao());
			return obPersistencia.negativarPFisicaPJuricaSegundoGrau(certidaoDt);
		} catch (Exception e) {
			throw new Exception(" <{ Erro ao listar os processos da Certidao NP do Segundo Grau Pública.}> Local Exception: " + this.getClass().getName() + ".listarProcessoCertidaoNPSegundoGrauPublica(): " + e.getMessage(), e);
		} finally {
			obFabricaConexao.fecharConexao();
		}
	}

	public String montaModelo(CertidaoSegundoGrauNegativaPositivaDt certidaoDt, ModeloDt modeloDt, UsuarioNe usuarioNe) throws Exception {
        ModeloNe modeloNe = new ModeloNe();
        return modeloNe.montaConteudoPorModelo(certidaoDt, usuarioNe.getUsuarioDt(), modeloDt.getId());
    }

	public String montaModeloMemoria(
			CertidaoSegundoGrauNegativaPositivaDt certidaoSegundoGrauNegativaPositivaDt,
			ModeloDt modeloDt, UsuarioNe usuarioSessao) {
		
		return null;
	}
	
	public List getListaProcessoSSG(CertidaoSegundoGrauNegativaPositivaDt certidaoDt) throws Exception{
		String nomePessoa = "nomepessoa=";
		String nomeMae = "&nomemae=";
		String cpf = "&cpf="; 
		//campos comentados não estão sendo usados na consulta.
//			String dataIni = "&dataini=";
//			String dataFim = "&datafini=";
		String area = "&area=";
		String dataNasc = "&nascimento="; 
		
		String urlAux = URA0006T;
		if (certidaoDt.getNome() != null && !certidaoDt.getNome().isEmpty()) {
			urlAux += nomePessoa + Funcoes.retirarAcentos(certidaoDt.getNome());
		}
		if (certidaoDt.getNomeMae() != null && !certidaoDt.getNomeMae().isEmpty()) {
			urlAux += nomeMae + certidaoDt.getNomeMae();
		}
		if (certidaoDt.getCpfCnpj() != null && !certidaoDt.getCpfCnpj().isEmpty()) {
			urlAux += cpf + certidaoDt.getCpfCnpj();
		}
		if (certidaoDt.getDataNascimento() != null && !certidaoDt.getDataNascimento().isEmpty()) {
			urlAux += dataNasc + Funcoes.FormatarDataSemBarra(certidaoDt.getDataNascimento());
		}
//			if (certidaoDt.getDataInicial() != null && !certidaoDt.getDataInicial().isEmpty()) {
//				urlAux += dataIni + certidaoDt.getDataInicial();
//			}
//			if(certidaoDt.getDataFinal() != null && !certidaoDt.getDataFinal().isEmpty() ) {
//				urlAux += dataFim + certidaoDt.getDataFinal();
//			}
		if (!certidaoDt.getArea().equals("Todos")) {
			String areaAux = "";
			if (certidaoDt.getArea().equals("Civel")) {
				areaAux = String.valueOf(AreaDt.CIVEL);
			} else {
				areaAux = String.valueOf(AreaDt.CRIMINAL);

			}
			urlAux += area + areaAux;
		}
	
		urlAux = urlAux.replaceAll(" ", "%20");
		
		List listaProcesso = new ArrayList(5);
		
		Element certidao = getElementoRootXML(urlAux);
		
				
		List elements = certidao.getChildren("Processo");
		Iterator i = elements.iterator();

		while (i.hasNext()) {
			ProcessoCertidaoSegundoGrauPositivaNegativaDt p = new ProcessoCertidaoSegundoGrauPositivaNegativaDt(); 
			Element element = (Element) i.next();
			p.setProcessoNumero(element.getChildText("Numero"));
			p.setProcessoTipo(element.getChildText("Feito"));
			p.setPromovidoNome(element.getChildText("Apelado"));
			p.setPromoventeNome(element.getChildText("Apelante"));
			p.setPromovidoNomeMae(element.getChildText("Mae"));
			p.setPromovidoDataNascimento(element.getChildText("DataNascimento"));
			p.setPromovidoCpf(element.getChildText("Cpf"));
			p.setRequerente(element.getChildText("nomeFonetico"));
			p.setFaseAtual(element.getChildText("FaseAtual"));
			p.setCamara(element.getChildText("Camara"));
			p.setRelator(element.getChildText("Relator"));
			p.setSistema("SSG");
			element.getChildText("Tipo");
			element.getChildText("Averbacao");
			element.getChildText("Desmembrado");
			element.getChildText("Conversao");
			listaProcesso.add(p);
		}
											 						
		return listaProcesso;
	}	
	
	private List getListaProcessoSSG(String nomePessoa, String nomeMae, String cpf, String dataNascimento, String areaCodigo, boolean consideraArquivados) throws Exception{
		String parametroNomePessoa = "nomepessoa=";
		String parametroNomeMae = "&nomemae=";
		String parametroCPF = "&cpf="; 
		//campos comentados não estão sendo usados na consulta.
//			String dataIni = "&dataini=";
//			String dataFim = "&datafini=";
		String parametroArea = "&area=";
		String parametroDataNascimento = "&nascimento=";
		String parametroConsideraArquivados = "&tramitacao=1";
		
		String urlAux = URA0006T;
		if (nomePessoa != null && !nomePessoa.isEmpty()) {
			urlAux += parametroNomePessoa + Funcoes.retirarAcentos(nomePessoa);
		}
		if (nomeMae != null && !nomeMae.isEmpty()) {
			urlAux += parametroNomeMae + nomeMae;
		}
		if (cpf != null && !cpf.isEmpty()) {
			urlAux += parametroCPF + cpf;
		}
		if (dataNascimento != null && !dataNascimento.isEmpty()) {
			urlAux += parametroDataNascimento + Funcoes.FormatarDataSemBarra(dataNascimento);
		}
//			if (certidaoDt.getDataInicial() != null && !certidaoDt.getDataInicial().isEmpty()) {
//				urlAux += dataIni + certidaoDt.getDataInicial();
//			}
//			if(certidaoDt.getDataFinal() != null && !certidaoDt.getDataFinal().isEmpty() ) {
//				urlAux += dataFim + certidaoDt.getDataFinal();
//			}
		if (areaCodigo != null && !areaCodigo.isEmpty()) {
			urlAux += parametroArea + areaCodigo;
		}
		
		if (consideraArquivados) {
			urlAux += parametroConsideraArquivados;
		}
		
		urlAux = urlAux.replaceAll(" ", "%20");
		
		List listaProcesso = new ArrayList(5);
		
		Element certidao = getElementoRootXML(urlAux);
						
		List elements = certidao.getChildren("Processo");
		Iterator i = elements.iterator();

		while (i.hasNext()) {
			ProcessoAntecedenteCriminalDt p = new ProcessoAntecedenteCriminalDt(); 
			Element element = (Element) i.next();
			//p.setRequerente(element.getChildText("nomeFonetico"));
			//ProtocoloOrigem
			p.setProcessoNumero(element.getChildText("Numero"));
			p.setServentia(element.getChildText("Camara"));
			p.setPromovidoNome(element.getChildText("Apelado"));
			p.addPromovente(element.getChildText("Apelante"));
			p.setProcessoTipo(element.getChildText("Feito"));
			p.setProcessoResponsavel(element.getChildText("Relator"));
			p.setPromovidoNomeMae(element.getChildText("MaeApelado"));
			p.setPromovidoCpf(element.getChildText("CpfApelado"));
			p.setPromovidoDataNascimento(element.getChildText("NascApelado"));
			p.setFase(element.getChildText("FaseAtual"));
//			p.setNomeVitima(element.getChildText("vitima"));
//			p.addAssunto(element.getChildText("assunto"));
//			p.addInfracao(element.getChildText("lei"));			
//			p.setDataRecebimento(element.getChildText("dataDistribuicao"));
//			p.setDataTransitoJulgado(element.getChildText("dataTransitoJulgado"));
//			p.setComarca(element.getChildText("comarca"));
//			p.setSentenca(element.getChildText("sentenca"));
//			p.setDataFato(element.getChildText("DataFato").trim());
//			p.setDataBaixa(element.getChildText("dataBaixa").trim());
//			p.setMotivoBaixa(element.getChildText("motivoBaixa").trim());
//			String transacao = element.getChildText("transacao").trim();
//			String sursis = element.getChildText("sursis").trim();
//
//			isnValor++;
//			if(!transacao.isEmpty()) {
//				p.addBeneficio(new BeneficioCertidaoAntecedenteCriminalDt(transacao, "Transação Penal", "Não informado"));
//			} else if(!sursis.isEmpty()) {
//				BeneficioCertidaoAntecedenteCriminalDt bene = new BeneficioCertidaoAntecedenteCriminalDt(sursis, "Sursis", "Não informado");
//				bene.setCondicional(element.getChildText("condicoes").trim());
//				bene.setCondicoes(element.getChildText("condicional").trim());
//				p.addBeneficio(bene);
//			}	
			
			p.setSistema("SSG");
			listaProcesso.add(p);
		}
											 						
		return listaProcesso;
	}
	
	public boolean negativarPFisicaPJuricaSSG(CertidaoNegativaPositivaPublicaDt certidaoNegativaPositivaDt) throws Exception {
		String nomePessoa = "nomepessoa=";
		String nomeMae = "&nomemae=";
		String cpf = "&cpf="; 
		String area = "&area=";
		String dataNasc = "&nascimento="; 
		boolean retorno;
		
		String urlAux = URA0006T;
		if (certidaoNegativaPositivaDt.getNome() != null && !certidaoNegativaPositivaDt.getNome().isEmpty()) {
			urlAux += nomePessoa+ Funcoes.retirarAcentos(certidaoNegativaPositivaDt.getNome());
		}
		if (certidaoNegativaPositivaDt.getNomeMae() != null && !certidaoNegativaPositivaDt.getNomeMae().isEmpty()) {
			urlAux += nomeMae + certidaoNegativaPositivaDt.getNomeMae();
		}
		if (certidaoNegativaPositivaDt.getCpfCnpj() != null && !certidaoNegativaPositivaDt.getCpfCnpj().isEmpty()) {
			urlAux += cpf + certidaoNegativaPositivaDt.getCpfCnpj();
		}
		if (certidaoNegativaPositivaDt.getDataNascimento() != null && !certidaoNegativaPositivaDt.getDataNascimento().isEmpty()) {
			urlAux += dataNasc + Funcoes.FormatarDataSemBarra(certidaoNegativaPositivaDt.getDataNascimento());
		}
			
		String areaAux = "";
		if (certidaoNegativaPositivaDt.isCivel()) {
			areaAux = String.valueOf(AreaDt.CIVEL);
		} else {
			areaAux = String.valueOf(AreaDt.CRIMINAL);
		}
		urlAux += area + areaAux;
	
		urlAux = urlAux.replaceAll(" ", "%20");
		
		List listaProcesso = new ArrayList(5);
		
		Element certidao = getElementoRootXML(urlAux);
		
		try {			
			List elements = certidao.getChildren("Processo");
			Iterator i = elements.iterator();
	
			if (i.hasNext()) {
				retorno = false;
			} else {
				retorno = true;
			}
											 
		} catch (Exception e) {
			
			throw new Exception("<{Erro ao obter a lista de processos do SSG.}> Local Exception: " +  this.getClass().getName() + ". getListaProcessoSSGPublica(): " + e.getMessage(), e);
		}
		return retorno;
	}

	@Override
	public String Verificar(CertidaoDt dados) {
		
		return null;
	}

	public String consultarDescricaoComarcaJSON(String tempNomeBusca, String PosicaoPaginaAtual) throws Exception {
		String stTemp = "";
		ComarcaNe Comarcane = new ComarcaNe();
		stTemp = Comarcane.consultarDescricaoJSON(tempNomeBusca, PosicaoPaginaAtual);
		return stTemp;
	}
	
	public String VerificarCertidaoNegativaPositivaPublica(CertidaoNegativaPositivaPublicaDt certidao, boolean isPrimeiroGrau)  {
		String mensagem = "";

		if (certidao.getNome() == null || certidao.getNome().trim().length() == 0)
			mensagem += "Informe o Nome.\n";

		if (certidao.getCpfCnpj() == null
				|| certidao.getCpfCnpj().trim().length() == 0)
			mensagem += "Informe o CPF.\n";
		else if (certidao.getCpfCnpj().length() != 11 || !Funcoes.testaCPFCNPJ(certidao.getCpfCnpj()))
			mensagem += "CPF inválido. \n";
		
		if (certidao.getNomeMae() == null || certidao.getNomeMae().trim().length() == 0)
			mensagem += "Informe o Nome da Mãe.\n";

		if (certidao.getDataNascimento() == null || certidao.getDataNascimento().trim().length() == 0)
			mensagem += "Informe a Data de nascimento.\n";
		else if (!Funcoes.validaData(certidao.getDataNascimento()))
			mensagem += "Data de Nascimento em formato incorreto. \n";
		else {
			Date data;
			try{
				data = Funcoes.StringToDate(certidao.getDataNascimento());				
				if (data.after(new Date())) mensagem += "Data de Nascimento não pode ser uma data futura. \n";
			} catch(Exception e) {
				mensagem += "Data de Nascimento em formato incorreto. \n";
			}
		}
		
		if (certidao.getAreaCodigo() == null || certidao.getAreaCodigo().trim().length() == 0)
			mensagem += "Informe o Tipo de Área.\n";
		
		if (isPrimeiroGrau) {
			if (certidao.getTerritorio().trim().length() == 0) {
				mensagem += "Informe o Território.\n";	
			} else if (certidao.getTerritorio().trim().equalsIgnoreCase("C") && certidao.getId_Comarca().trim().length() == 0) {
				mensagem += "Informe a Comarca.\n";
			}
			
			if (!CertidaoDt.isGratuitaEmissaoPrimeiroGrau) {
				if (certidao.getFinalidade().trim().length() == 0) {
					mensagem += "Informe a Finalidade.\n";	
				} else if (Funcoes.StringToInt(certidao.getFinalidade()) == OUTROS) {
					mensagem = "<b>Atenção:</b> Essa certidão não pode ser emitida de forma automática.\nA finalidade escolhida exige análise para emissão, portanto dirija-se ao Cartório Distribuidor Cível do Fórum local.";
				}
				
				if (mensagem.length() == 0) {
					if (certidao.getDocumento() == null || certidao.getDocumento().length == 0) {
						mensagem += "Selecione um Documento.\n";	
					} else if (certidao.getDocumento().length > 2048 * 1024) {
						mensagem += "Selecione um Documento com no máximo 2MB.\n";
						certidao.limparDocumento();
						certidao.setNomeDocumento("");
					}
				}
			}
		}

		return mensagem;
	}
	
	public String VerificarCertidaoNegativaPositivaPublicaGuia(CertidaoNegativaPositivaPublicaDt certidao) throws Exception  {
		String mensagem = "";
		
		if (certidao.getNumeroGuiaCertidao() == null || certidao.getNumeroGuiaCertidao().trim().length() == 0) {
			mensagem += "Informe o Número do Requerimento (Guia).\n";
		} else {
			GuiaEmissaoDt guiaEmissaoDt = ObtenhaGuiaEmissaoCertidao(certidao.getNumeroGuiaCertidao());
			if (guiaEmissaoDt == null) {
				mensagem += "Guia não encontrada, verifique o número! \nDigite somente o número sem a série, exemplo guia NÚMERO: 18680149 - 1, favor digitar: 186801491.\n";
			} else if (!guiaEmissaoDt.isGuiaCertidaoSPG()) {
				mensagem += "A guia informada não é do tipo certidão.\n";
			} else if (!guiaEmissaoDt.isGuiaPaga()) {
				mensagem += "Ainda não identificamos o pagamento da guia.\n";
			} else if (!guiaEmissaoDt.isPessoalFisicaSPG()) {
				mensagem += "A guia emitida não é para pessoa física.\n";
			} else if (guiaEmissaoDt.getDataApresentacaoSPG() != null && guiaEmissaoDt.getDataApresentacaoSPG().trim().length() > 0) {
				CertidaoValidacaoDt certidaoEmitida = this.consultarNumeroGuia(guiaEmissaoDt.getNumeroGuiaCompleto());
				if (certidaoEmitida == null)
					mensagem += "Guia já utilizada para emissão de certidão no distribuidor cível.\n";
				else
					certidao.setCertidaoEmitida(certidaoEmitida);
			}
			
			if (mensagem.length() == 0){
				certidao.setGuiaEmissaoCertidao(guiaEmissaoDt);
			}
		}				
		
		return mensagem;
	}
	
	public GuiaEmissaoDt ObtenhaGuiaEmissaoCertidao(String numeroDoRequerimento) throws Exception {
		GuiaSPGNe guiaSPGNe = new GuiaSPGNe();
		
		GuiaEmissaoDt guiaEmissaoDt = guiaSPGNe.consultarGuiaEmissaoSPG(numeroDoRequerimento);
		
		if (guiaEmissaoDt != null && guiaEmissaoDt.isGuiaCertidaoSPG()) return guiaEmissaoDt;
		
		guiaEmissaoDt = guiaSPGNe.consultarGuiaEmissaoSPG(numeroDoRequerimento + "09");
		
		if (guiaEmissaoDt != null && guiaEmissaoDt.isGuiaCertidaoSPG()) return guiaEmissaoDt;
		
		guiaEmissaoDt = guiaSPGNe.consultarGuiaEmissaoSPG(numeroDoRequerimento + "06");
		
		if (guiaEmissaoDt != null && guiaEmissaoDt.isGuiaCertidaoSPG()) return guiaEmissaoDt;
		
		guiaEmissaoDt = guiaSPGNe.consultarGuiaEmissaoSPG(numeroDoRequerimento + "50");
		
		return guiaEmissaoDt;
	}
	
	public String VerificarCertidaoNegativaPositivaPublicaPJ(CertidaoNegativaPositivaPublicaDt certidao, boolean isPrimeiroGrau) throws Exception {
		String mensagem = "";
		
		if (certidao.getAreaCodigo() == null || certidao.getAreaCodigo().trim().length() == 0)
			mensagem += "Informe o Tipo de Área.\n";
		
		if (mensagem.trim().length() == 0) {
			if (certidao.getAreaCodigo().equalsIgnoreCase(String.valueOf(AreaDt.CIVEL)) && isPrimeiroGrau && !CertidaoDt.isGratuitaEmissaoPrimeiroGrau) {
				if (certidao.getNumeroGuiaCertidao() == null || certidao.getNumeroGuiaCertidao().trim().length() == 0) {
					mensagem += "Informe o Número do Requerimento (Guia).\n";
				} else {
					GuiaEmissaoDt guiaEmissaoDt = ObtenhaGuiaEmissaoCertidao(certidao.getNumeroGuiaCertidao());
					if (guiaEmissaoDt == null) {
						mensagem += "Guia não encontrada, verifique o número! \nDigite somente o número sem a série, exemplo guia NÚMERO: 18680149 - 1, favor digitar: 186801491.\n";
					} else if (!guiaEmissaoDt.isGuiaCertidaoSPG()) {
						mensagem += "A guia informada não é do tipo certidão.\n";
					} else if (!guiaEmissaoDt.isGuiaPaga()) {
						mensagem += "Ainda não identificamos o pagamento da guia.\n";
					} else if (guiaEmissaoDt.isPessoalFisicaSPG()) {
						mensagem += "A guia emitida não é para pessoa jurídica.\n";
					} else if (guiaEmissaoDt.getDataApresentacaoSPG() != null && guiaEmissaoDt.getDataApresentacaoSPG().trim().length() > 0) {
						CertidaoValidacaoDt certidaoEmitida = this.consultarNumeroGuia(guiaEmissaoDt.getNumeroGuiaCompleto());
						if (certidaoEmitida == null)
							mensagem += "Guia já utilizada para emissão de certidão no distribuidor cível.\n";
						else
							certidao.setCertidaoEmitida(certidaoEmitida);
					}
					
					if (mensagem.length() == 0) {
						certidao.setGuiaEmissaoCertidao(guiaEmissaoDt);
					}
				}	
			} else {
				if (certidao.getNome() == null || certidao.getNome().trim().length() == 0)
					mensagem += "Informe a Razão Social.\n";

				if (certidao.getCpfCnpj() == null
						|| certidao.getCpfCnpj().trim().length() == 0)
					mensagem += "Informe o CNPJ.\n";
				else if (certidao.getCpfCnpj().length() != 14 || !Funcoes.testaCPFCNPJ(certidao.getCpfCnpj()))
					mensagem += "CNPJ inválido. \n";
				
				if (isPrimeiroGrau && (certidao.getAreaCodigo().equalsIgnoreCase(String.valueOf(AreaDt.CRIMINAL)) || CertidaoDt.isGratuitaEmissaoPrimeiroGrau)) {
					if (certidao.getTerritorio().trim().length() == 0) {
						mensagem += "Informe o Território.\n";	
					} else if (certidao.getTerritorio().trim().equalsIgnoreCase("C") && certidao.getId_Comarca().trim().length() == 0) {
						mensagem += "Informe a Comarca.\n";
					}
				}
			}				
		}

		return mensagem;
	}
	
	public String VerificarCertidaoNegativaPositivaPublica(CertidaoNegativaPositivaDt certidao) {
		String mensagem = "";

		if (certidao.getNome() == null || certidao.getNome().trim().length() == 0)
			mensagem += "Informe o nome.\n";

		if (certidao.getCpfCnpj() == null
				|| certidao.getCpfCnpj().trim().length() == 0)
			mensagem += "Informe o CPF.\n";
		else if (certidao.getCpfCnpj().length() != 11 || !Funcoes.testaCPFCNPJ(certidao.getCpfCnpj()))
			mensagem += "CPF inválido. \n";
		
		if (certidao.getNomeMae() == null || certidao.getNomeMae().trim().length() == 0)
			mensagem += "Informe o nome da mãe.\n";

		if (certidao.getDataNascimento() == null || certidao.getDataNascimento().trim().length() == 0)
			mensagem += "Informe a data de nascimento.\n";
		else if (!Funcoes.validaData(certidao.getDataNascimento()))
			mensagem += "Data de Nascimento em formato incorreto. \n";
		else {
			Date data;
			try{
				data = Funcoes.StringToDate(certidao.getDataNascimento());				
				if (data.after(new Date())) mensagem += "Data de Nascimento não pode ser uma data futura. \n";
			} catch(Exception e) {
				mensagem += "Data de Nascimento em formato incorreto. \n";
			}
		}
		
		if (certidao.getAreaCodigo() == null || certidao.getAreaCodigo().trim().length() == 0)
			mensagem += "Informe o Tipo de Área.\n";

		return mensagem;
	}
	   
	
	public List recuperarListaDeProcesso(String nome, String cpfCnpj, String dataNascimento, String nomeMae, String nomePai, String idNaturalidade, String rg, boolean menorInfrator) throws Exception {
		List listaProcesso = new ArrayList();
		List listaProcessoSemDataNascimento = new ArrayList();
		List listaProcessoComDataNascimento = new ArrayList();

		if( (nome == null || nome.isEmpty())){
			throw new MensagemException("Não é possível emitir os Antecedentes Criminais, pois os dados informados são insuficientes. Informe o nome.");
		}
		
//		if( (cpfCnpj == null || cpfCnpj.isEmpty()) && 
//				(dataNascimento == null || dataNascimento.isEmpty()) &&
//				(nomeMae == null || nomeMae.isEmpty()) &&
//				(nomePai == null || nomePai.isEmpty()) &&
//				(idNaturalidade == null || idNaturalidade.isEmpty()) &&
//				(rg == null || rg.isEmpty())) {
//			throw new MensagemException("Não é possível emitir os Antecedentes Criminais, pois os dados informados são insuficientes. Além do nome, ao menos um destes "
//					+ "campos deve ser informado: Nome da mãe, Nome do Pai, RG, CPF/CNPJ, Data de nascimento ou Naturalidade. Favor revisar os dados informados.");
//		}
		
		String naturalidade = "";
		if(idNaturalidade != null && !idNaturalidade.isEmpty()) {
			CidadeDt cidadeNaturalidadeDt = new CidadeNe().consultarId(idNaturalidade);
			if(cidadeNaturalidadeDt != null){
				naturalidade = cidadeNaturalidadeDt.getCidade();
			}
		}
		
		listaProcesso.addAll(getListaProcesso(nome, cpfCnpj, dataNascimento, nomeMae, nomePai, idNaturalidade, rg, menorInfrator));
		//A consulta no SPG de menor infrator é diferente da certidão para maior de idade
		if(menorInfrator) {
			listaProcesso.addAll(getListaProcessoMenorInfratorSPG(nome, cpfCnpj, dataNascimento, nomeMae, nomePai, naturalidade, rg));
		} else {
			listaProcesso.addAll(getListaProcessoSPG(nome, cpfCnpj, dataNascimento, nomeMae, nomePai, naturalidade, rg));
			listaProcesso.addAll(getListaProcessoSSG(nome, nomeMae, cpfCnpj, dataNascimento, null, true));
		}
		
		return listaProcesso;
	}


	/**
	 * Método que gera a certidão de antecedentes criminais usando o jasper reports para criação do arquivo.
	 * @param certidaoAntecedenteCriminalDt - dados da certidão
	 * @param listaProcesssosSelecionadosCertidao - lista de processos selecionados a serem apresentados na certidão
	 * @param usuarioSessao - usuário que está gerando a certidão
	 * @param procParteDt - dados da parte (consultados quando o usuário tenta imprimir a certidão pela capa do processo)
	 * @param chkMenorInfrator - se a certidão é de menor infrator ou não
	 * @return byte[] com o conteúdo do arquivo gerado pelo jasper
	 * @throws Exception
	 * @author hmgodinho
	 */
	public byte[] gerarCertidaoAntecedentesJasper(CertidaoAntecedenteCriminalDt certidaoAntecedenteCriminalDt, String[] listaProcesssosSelecionadosCertidao, UsuarioNe usuarioSessao, ProcessoParteDt procParteDt, boolean chkMenorInfrator, boolean impressaoTelaConsultaCertidao) throws Exception {
		List listaProcessosImprimir = new ArrayList();
		
		if (impressaoTelaConsultaCertidao) {
			//Se esse atributo estiver diferente de null, significa que o usuário selecionou processos a serem impressos na certidão
			if( listaProcesssosSelecionadosCertidao != null) {
				for (int i = 0; i < listaProcesssosSelecionadosCertidao.length; i++) {
					String processoNumero = listaProcesssosSelecionadosCertidao[i];
					
					for (int j = 0; j < certidaoAntecedenteCriminalDt.getListaProcesso().size(); j++) {
						ProcessoAntecedenteCriminalDt processoDt = (ProcessoAntecedenteCriminalDt) certidaoAntecedenteCriminalDt.getListaProcesso().get(j);
						
						if (processoDt.isProcessoFisico()) {
							if (processoDt.getProcessoNumero().split("\\(")[0].equals(processoNumero)) {
								listaProcessosImprimir.add(processoDt);
								break;
							}
						} else if (processoDt.getSistema().equals("Projudi")) {
							if (processoDt.getProcessoNumeroCompleto().equals(processoNumero)) {
								listaProcessosImprimir.add(processoDt);
								break;
							}
						}
					}
				}
			}
		} else {
			// Se for a impressão direta (via capa do processo), imprime todos os processos localizados. Não será permitido 
			// editar os processos a serem impressos.
			for (int i = 0; i < certidaoAntecedenteCriminalDt.getListaProcesso().size(); i++) {
				ProcessoAntecedenteCriminalDt procDt = (ProcessoAntecedenteCriminalDt) certidaoAntecedenteCriminalDt.getListaProcesso().get(i);
				listaProcessosImprimir.add(procDt);
			}
		}
		
		//Ordenando a lista de processos a serem impressos pela diferença de dias entre as datas de recebimento de processos.
		//Usou-se dias para evitar problemas, caso algum processo não tenha informação de horas, minutos e segundos na data de
		//recebimento (cenário comum no SPG).
	    Collections.sort (listaProcessosImprimir, new Comparator() {   
            public int compare(Object o1, Object o2) {   
            	ProcessoAntecedenteCriminalDt c1 = (ProcessoAntecedenteCriminalDt) o1;    
            	ProcessoAntecedenteCriminalDt c2 = (ProcessoAntecedenteCriminalDt) o2;    
            	Integer diferenca = 0;
            	try{
            		SimpleDateFormat FormatoData = new SimpleDateFormat("dd/MM/yyyy");
            		Date dataProc1 = null;
            		Date dataProc2 = null;
            		try {
            			dataProc1 = FormatoData.parse(c1.getDataRecebimento());
            			dataProc2 = FormatoData.parse(c2.getDataRecebimento());
            		} catch (ParseException e) {}
            		long dif = dataProc1.getTime() - dataProc2.getTime();
			        Long diferencaEmHoras = new Long((dif /1000) / 60 / 60 / 24);  
			        diferenca = diferencaEmHoras.intValue();
                	if(diferenca != 0){
                		return diferenca;
                	}
            	} catch(Exception e) {}
            	return diferenca;
              }  
	    });

		byte[] temp = null;
		//setando os dados para localização do arquivo jasper que gerará o arquivo da certidão
		String diretorioProjeto = ProjudiPropriedades.getInstance().getCaminhoAplicacao();
		String pathRelatorio = Relatorios.getPathRelatorio(diretorioProjeto);
		String nomeRelatorio = "";
		//Setando os parâmetros do relatório
		ServentiaDt serventia = new ServentiaNe().consultarId(usuarioSessao.getId_Serventia());
		Map parametros = new HashMap();

		if (chkMenorInfrator) {
			if (!listaProcessosImprimir.isEmpty()) {
				nomeRelatorio = "antecedenteCriminalPositivo";				
				parametros.put("titulo", "Informação de Antecedentes Criminais de Menor Infrator");
				parametros.put("paragrafoPositivoMenor", true);	
				parametros.put("nomeSolicitante", "Informação emitida por " + usuarioSessao.getUsuarioDt().getNome());				
			}else {
				nomeRelatorio = "antecedenteCriminalNegativo";
				parametros.put("titulo", "Informação de Antecedentes Criminais de Menor Infrator");
				parametros.put("paragrafoNegativoMenor", true);	
				parametros.put("nomeSolicitante", "Informação emitida por " + usuarioSessao.getUsuarioDt().getNome());
			}
		}else {
			if (!listaProcessosImprimir.isEmpty()) {
				nomeRelatorio = "antecedenteCriminalPositivo";
				if(impressaoTelaConsultaCertidao) {
					//Certidão para maior positiva
					parametros.put("titulo", "Certidão de Antecedentes Criminais");
					parametros.put("cabecalhoCertidao", "Dr.(a), escrivão(ã) do Cartório Distribuidor da Comarca de " + serventia.getComarca() + ", Estado de Goiás, na forma da lei, etc.");
					parametros.put("paragrafoPositivoMaior", true);
					parametros.put("nomeSolicitante", "Certidão emitida por " + usuarioSessao.getUsuarioDt().getNome());
				}else {
					//informação para maior positiva
					parametros.put("titulo", "Informação de Antecedentes Criminais");
					parametros.put("paragrafoPositivoMenor", true);	
					parametros.put("nomeSolicitante", "Informação emitida por " + usuarioSessao.getUsuarioDt().getNome());
				}
			}else {
				nomeRelatorio = "antecedenteCriminalNegativo";
				if(impressaoTelaConsultaCertidao) {
					parametros.put("titulo", "Certidão de Antecedentes Criminais");
					parametros.put("cabecalhoCertidao", "Dr.(a), escrivão(ã) do Cartório Distribuidor da Comarca de " + serventia.getComarca() + ", Estado de Goiás, na forma da lei, etc.");
					parametros.put("paragrafoNegativoMaior", true);
					parametros.put("nomeSolicitante", "Certidão emitida por " + usuarioSessao.getUsuarioDt().getNome());
				}else {
					parametros.put("titulo", "Informação de Antecedentes Criminais");
					parametros.put("paragrafoNegativoMenor", true);	
					parametros.put("nomeSolicitante", "Informação emitida por " + usuarioSessao.getUsuarioDt().getNome());
				}
			}
		}
		
		parametros.put("caminhoLogo", Relatorios.getCaminhoLogotipoRelatorio(diretorioProjeto));
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		parametros.put("dataAtual", sdf.format(new Date()));
		parametros.put("dataAtualExtenso", Funcoes.dataAtualPorExtenso());
		parametros.put("comarca", serventia.getComarca());
		parametros.put("serventia", serventia.getServentia());
		parametros.put("enderecoServentia", serventia.getEnderecoDt().getLogradouro() + ", " + serventia.getEnderecoDt().getNumero() + ", " + serventia.getEnderecoDt().getBairro());
		parametros.put("telefoneServentia", serventia.getTelefone());
		if(procParteDt != null) {
			parametros.put("nomeRequerente", procParteDt.getNome());
			parametros.put("nomePaiRequerente", procParteDt.getNomePai());
			parametros.put("nomeMaeRequerente", procParteDt.getNomeMae());
			parametros.put("dataNascimentoRequerente", procParteDt.getDataNascimento());
			parametros.put("nacionalidadeRequerente", "");
			parametros.put("profissaoRequerente", procParteDt.getProfissao());
			parametros.put("estadoCivilRequerente", procParteDt.getEstadoCivil());
			parametros.put("sexoRequerente", Funcoes.getSexoDescricao(procParteDt.getSexo()));
			parametros.put("rgRequerente", procParteDt.getRg());
			parametros.put("cpfRequerente", procParteDt.getCpfCnpj());
			parametros.put("naturalidadeRequerente", procParteDt.getCidadeNaturalidade());
		} else {
			parametros.put("nomeRequerente", certidaoAntecedenteCriminalDt.getNome());
			parametros.put("nomePaiRequerente", certidaoAntecedenteCriminalDt.getNomePai());
			parametros.put("nomeMaeRequerente", certidaoAntecedenteCriminalDt.getNomeMae());
			parametros.put("dataNascimentoRequerente", certidaoAntecedenteCriminalDt.getDataNascimento());
			parametros.put("nacionalidadeRequerente", certidaoAntecedenteCriminalDt.getNacionalidade());
			parametros.put("profissaoRequerente", certidaoAntecedenteCriminalDt.getProfissao());
			parametros.put("estadoCivilRequerente", certidaoAntecedenteCriminalDt.getEstadoCivil());
			parametros.put("sexoRequerente", Funcoes.getSexoDescricao(certidaoAntecedenteCriminalDt.getSexo()));
			parametros.put("cpfRequerente", certidaoAntecedenteCriminalDt.getCpfCnpj());
			parametros.put("rgRequerente", certidaoAntecedenteCriminalDt.getRg());
			parametros.put("naturalidadeRequerente", certidaoAntecedenteCriminalDt.getNaturalidade());
		}
		
		//Gerando o arquivo da certidão. Se a listaProcessosImprimir estiver vazia,
		//o relatório não possui detail e não precisa passar uma lista de fields.
		if(listaProcessosImprimir.isEmpty()) {
			temp = Relatorios.gerarRelatorioPdfSemDetail(pathRelatorio, nomeRelatorio, parametros); 
		} else {
			temp = Relatorios.gerarRelatorioPdf(pathRelatorio, nomeRelatorio, parametros, listaProcessosImprimir); 
		}
		
		return temp;
	}
	
	public String consultarDescricaoCidadeJSON(String tempNomeBusca1, String tempNomeBusca2, String posicaoPaginaAtual) throws Exception{
		String stTemp = "";
		CidadeNe neObjeto = new CidadeNe();
		
		stTemp = neObjeto.consultarDescricaoJSON(tempNomeBusca1, tempNomeBusca2, posicaoPaginaAtual);

		neObjeto = null;
		return stTemp;
	}
	
	public String consultarDescricaoProfissaoJSON(String tempNomeBusca, String posicaoPaginaAtual) throws Exception {
		String stTemp = "";
		ProfissaoNe neObjeto = new ProfissaoNe();
		
		stTemp = neObjeto.consultarDescricaoJSON(tempNomeBusca, posicaoPaginaAtual);

		neObjeto = null;
		return stTemp;
	}
	public String consultarDescricaoEstadoCivilJSON(String tempNomeBusca, String posicaoPaginaAtual) throws Exception {
		String stTemp = "";
		EstadoCivilNe neObjeto = new EstadoCivilNe();
		
		stTemp = neObjeto.consultarDescricaoJSON(tempNomeBusca, posicaoPaginaAtual);

		neObjeto = null;
		return stTemp;
	}
	
	public String montaModelo(CertidaoNegativaPositivaPublicaDt certidaoDt, ModeloDt modeloDt, UsuarioNe usuarioNe) throws Exception {
        ModeloNe modeloNe = new ModeloNe();

        return modeloNe.montaConteudoPorModelo(certidaoDt, usuarioNe.getUsuarioDt(), modeloDt.getId());
    }
	
//	public List<String> listarNomesDeComarcasComProcessoCertidaoPublicaNP(CertidaoNegativaPositivaPublicaDt certidaoDt) throws Exception {
//		
//		List listRetorno = null;
//		FabricaConexao obFabricaConexao = null;
//		
//		try{
//			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
//			CertidaoPs obPersistencia = new CertidaoPs(obFabricaConexao.getConexao());
//			listRetorno = consultarProcessosPrimeiroGrauCertidaoNP(certidaoDt);
//		} catch(Exception e) {								
//			throw new MensagemException("Erro ao acessar a base de dados do Projudi, favor tentar novamente mais tarde!");	
//		} finally{
//			obFabricaConexao.fecharConexao();
//		}
//		return listRetorno;
//	}
	
	public ComarcaDt consultarIdComarca(String id_comarca) throws Exception {
		ComarcaNe neObjeto = new ComarcaNe();		
		return neObjeto.consultarId(id_comarca);
	}
	
	public CertidaoValidacaoDt consultarNumeroGuia(String numeroGuiaCompleto) throws Exception {

		CertidaoValidacaoDt dtRetorno=null;
		FabricaConexao obFabricaConexao = null;


		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			CertidaoPs obPersistencia = new  CertidaoPs(obFabricaConexao.getConexao()); 

			dtRetorno= obPersistencia.consultarNumeroGuia(numeroGuiaCompleto ); 
			obDados.copiar(dtRetorno);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}
	
	public ComarcaDt consultarComarcaCodigo(String comarcaCodigo) throws Exception {
		ComarcaNe neObjeto = new ComarcaNe();		
		return neObjeto.consultarComarcaCodigo(comarcaCodigo);
	}
	
	public List getProcessoAverbacaoCusta(CertidaoNegativaPositivaDt certidao) throws Exception {

		List dtRetorno=null;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			CertidaoPs obPersistencia = new  CertidaoPs(obFabricaConexao.getConexao()); 

			dtRetorno= obPersistencia.getProcessoAverbacaoCusta(certidao );
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}
	
	/**
	 * Mï¿½todo que verifica se guia já foi utilizada para outra certidão.
	 * @param String numeroGuia
	 * @return boolean
	 * @throws Exception
	 */
	public boolean isGuiaJaUtilizada(String numeroGuia) throws Exception {
		
		boolean retorno = false;
		FabricaConexao obFabricaConexao = null;
		
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			CertidaoPs obPersistencia = new CertidaoPs(obFabricaConexao.getConexao());
			
			//Valida se numeroGuia está preenchido
			if( numeroGuia != null && numeroGuia.length() > 0 ) {
				retorno = obPersistencia.isGuiaJaUtilizada(numeroGuia);
			}
		} finally {
			if( obFabricaConexao != null ) {
				obFabricaConexao.fecharConexao();
			}
		}
		
		return retorno;
	}
	
	private static final String SEPARADOR_CERTIDAO_PRATICA_FORENSE = ";@;";
	private static final String SEPARADOR_PROCESSO_CERTIDAO_PRATICA_FORENSE = ";#;";
	private static final String SEPARADOR_DADOS_PROCESSO_CERTIDAO_PRATICA_FORENSE = ";%;";
	private static final String SEPARADOR_CERTIDAO_PRATICA_FORENSE_OABS = "/";
	private static final String SEPARADOR_CERTIDAO_PRATICA_FORENSE_DADOS_OAB = "-";
	
	public void preenchaDadosCertidaoPraticaForenseSPG(CertidaoGuiaDt certidaoGuiaDt, ServentiaDt serventiaDt) throws Exception {
		if (certidaoGuiaDt == null || certidaoGuiaDt.getGuiaEmissaoDt() == null || certidaoGuiaDt.isGuiaJaUtilizada()) return;
		preenchaDadosCertidaoPraticaForenseSPGGuiaSPG(certidaoGuiaDt);
		preenchaDadosCertidaoPraticaForenseSPGGuiaProjudi(certidaoGuiaDt, serventiaDt);
	}
	
	private enum EnumDadosHeaderCertidaoPraticaForense
	{
		QUANTIDADE_PROCESSOS , // Quantidade de processos encontrados para o período informado ;@; Ex: 51
		NOME_ADVOGADO , // Nome do advogado ;@; Ex: JOAO JOSE PEREIRA DA SILVA
		NATURALIDADE , // Naturalidade ;@;  /* informação extraída da guia emitida
		PROFISSAO, // Profissão ;@; /* informação extraída da guia emitida
		ESTADO_CIVIL, // Estado CIVIL ;@; /* informação extraída da guia emitida
		SEXO, // Sexo ;@; /* informação extraída da guia emitida
		RG, // RG ;@; /* informação extraída da guia emitida. Ex: 1 - 1
		CPF, // CPF ;@; /* informação extraída da guia emitida Ex: 000.000.001-91
		OABS, // OABs ;@; /* todas as oabs encontradas na tabela de advogado SPG Ex: 35586 - GO / - / - / - 
		CUSTA_CERTIDAO, 
		CUSTA_TAXA_JUDICIARIA, //Taxa Judiciária Ex: 1514 
		COMARCA_PROCESSOS, // Nome da comarca e processos
		SIZE
	}
	
	private enum EnumDadosHeaderCertidaoPraticaForenseProjudi
	{
		OABS, // OABs ;@; /* todas as oabs encontradas na tabela de advogado SPG Ex: 35586 - GO / - / - / - 
		COMARCA_PROCESSOS, // Nome da comarca e processos
		SIZE
	}
	
	// AS INFORMAÇÕES ABAIXO SE REPETEM PARA CADA PROCESSO ONDE O 	ADVOGADO ATUOU NO PERÍODO, CONTEMPLA ATÉ 250 PROCESSOS
	private enum EnumDadosProcessoCertidaoPraticaForense
	{
		PROCESSO , // Processo ;%; Ex: 201500164067
		SERVENTIA , // Serventia ;%; Ex: 1A VARA CRIMINAL DOS CRIMES PUNIDOS COM RECLUSAO
		NATUREZA , // Natureza ;%; Ex: ACAO PENAL - PROCEDIMENTO ORDINARIO
		NOME_AUTOR, // Autor ;%; // SP 
		NOME_ADVOGADO_AUTOR, // Nome Adv. Autor ;%; 
		NOME_REU, // Nome Réu ;%; Ex: JOSE DOS REIS PIRES
		NOME_ADVOGADO_REU, // Nome Adv. Réu ;%; Ex: JOAO JOSE PEREIRA DA SILVA
		DATA_DISTRIBUICAO, // Data de Distribuição ;%; Ex: 19/01/2015
		DATA_INICIO_ATUACAO, // Data Início de Atuação ;%;
		DATA_FIM_ATUACAO, // Data Fim de Atuação ;%;
		SIZE
	}
	
	// Comarca - ÚLTIMA POSIÇÃO	
	
	private static final String MENSAGEM_ERRO_CERTIDAO_PRATICA_FORENSE = "Ocorreu um erro ao consultar o dados da certidão de prática forense no SPG. Retorno(%s): %s.";
	
	private void preenchaDadosCertidaoPraticaForenseSPGGuiaSPG(CertidaoGuiaDt certidaoGuiaDt) throws Exception {
		if (certidaoGuiaDt.getGuiaEmissaoDt().isSerie50()) return;	
		
		String textoRetornoSPG = getTextoResposta(SPG3140T + Funcoes.obtenhaSomenteNumeros(certidaoGuiaDt.getNumeroGuia()));
		certidaoGuiaDt.setTextoRetornoSPG(textoRetornoSPG);
		
		if (textoRetornoSPG == null || textoRetornoSPG.trim().length() == 0) return;		
		
		String[] vetorPrincipalSPG = textoRetornoSPG.split(SEPARADOR_CERTIDAO_PRATICA_FORENSE);	
		
		if (vetorPrincipalSPG == null || 
			vetorPrincipalSPG.length < EnumDadosHeaderCertidaoPraticaForense.SIZE.ordinal() ||
			Funcoes.StringToInt(vetorPrincipalSPG[EnumDadosHeaderCertidaoPraticaForense.QUANTIDADE_PROCESSOS.ordinal()].trim(), -1) == -1) {

			if (certidaoGuiaDt.getGuiaEmissaoDt().isSerie50()) return;	
			
			throw new MensagemException(String.format(MENSAGEM_ERRO_CERTIDAO_PRATICA_FORENSE, "count", textoRetornoSPG.trim()));
		}
		
		certidaoGuiaDt.setQuantidadeSPG(Funcoes.StringToInt(vetorPrincipalSPG[EnumDadosHeaderCertidaoPraticaForense.QUANTIDADE_PROCESSOS.ordinal()].trim()));
		
		// Identificação do Requerente
		certidaoGuiaDt.setNome(vetorPrincipalSPG[EnumDadosHeaderCertidaoPraticaForense.NOME_ADVOGADO.ordinal()]);
		certidaoGuiaDt.setNaturalidade(vetorPrincipalSPG[EnumDadosHeaderCertidaoPraticaForense.NATURALIDADE.ordinal()]);
		certidaoGuiaDt.setProfissao(vetorPrincipalSPG[EnumDadosHeaderCertidaoPraticaForense.PROFISSAO.ordinal()]);
		certidaoGuiaDt.setEstadoCivil(vetorPrincipalSPG[EnumDadosHeaderCertidaoPraticaForense.ESTADO_CIVIL.ordinal()]);
		certidaoGuiaDt.setSexo(vetorPrincipalSPG[EnumDadosHeaderCertidaoPraticaForense.SEXO.ordinal()]);
		certidaoGuiaDt.setRg(vetorPrincipalSPG[EnumDadosHeaderCertidaoPraticaForense.RG.ordinal()]);
		certidaoGuiaDt.setCpf(vetorPrincipalSPG[EnumDadosHeaderCertidaoPraticaForense.CPF.ordinal()]);
		
		// Certidão de Prática Forense
		String[] vetorOABSsPG = vetorPrincipalSPG[EnumDadosHeaderCertidaoPraticaForense.OABS.ordinal()].split(SEPARADOR_CERTIDAO_PRATICA_FORENSE_OABS);	
		if (vetorOABSsPG.length > 0) {
			String[] vetorDadosPrimeiraOABSPG = vetorOABSsPG[0].split(SEPARADOR_CERTIDAO_PRATICA_FORENSE_DADOS_OAB);	
			if (vetorDadosPrimeiraOABSPG.length > 0) {
				certidaoGuiaDt.setOab(Funcoes.obtenhaSomenteNumeros(vetorDadosPrimeiraOABSPG[0]));
			}
			if (vetorDadosPrimeiraOABSPG.length > 1) {
				certidaoGuiaDt.setOabUf(vetorDadosPrimeiraOABSPG[1]);
			}
			//certidaoGuiaDt.setOabComplemento(jsonObject.getString("OabComplemento"));
			//certidaoGuiaDt.setOabUfCodigo(jsonObject.getString("OabUfCodigo"));
		}
		
		if (certidaoGuiaDt.getGuiaEmissaoDt().getDataIniCertidaoSPG() != null &&
			Funcoes.validaData_yyyy_MM_ddHHmmss(certidaoGuiaDt.getGuiaEmissaoDt().getDataIniCertidaoSPG())) {
			TJDataHora dataInicial = new TJDataHora();
			dataInicial.setDataaaaa_MM_ddHHmmss(certidaoGuiaDt.getGuiaEmissaoDt().getDataIniCertidaoSPG());
			certidaoGuiaDt.setMesInicial(String.valueOf(dataInicial.getMes()));
			certidaoGuiaDt.setAnoInicial(String.valueOf(dataInicial.getAno()));							
		}
		
		if (certidaoGuiaDt.getGuiaEmissaoDt().getDataFimCertidaoSPG() != null &&
			Funcoes.validaData_yyyy_MM_ddHHmmss(certidaoGuiaDt.getGuiaEmissaoDt().getDataFimCertidaoSPG())) {
			TJDataHora dataFinal = new TJDataHora();
			dataFinal.setDataaaaa_MM_ddHHmmss(certidaoGuiaDt.getGuiaEmissaoDt().getDataFimCertidaoSPG());
			certidaoGuiaDt.setMesFinal(String.valueOf(dataFinal.getMes()));
			certidaoGuiaDt.setAnoFinal(String.valueOf(dataFinal.getAno()));			
		}		

		//Descritiva e Quantitativa
		if (certidaoGuiaDt.getGuiaEmissaoDt().getTipoGuiaCertidaoSPG() != null &&
			certidaoGuiaDt.getGuiaEmissaoDt().getTipoGuiaCertidaoSPG().equalsIgnoreCase("Q")) {
			certidaoGuiaDt.setTipo("Quantitativa");
		} else {
			certidaoGuiaDt.setTipo("Normal");
		}
		
		if (certidaoGuiaDt.getGuiaEmissaoDt().getInfoLocalCertidaoSPG() != null) {
			ComarcaNe comarcaNe = new ComarcaNe();
			String comarcaCodigo = Funcoes.completarZeros(certidaoGuiaDt.getGuiaEmissaoDt().getInfoLocalCertidaoSPG().trim(), 6);
			comarcaCodigo = comarcaCodigo.trim().substring(0, 3);
			ComarcaDt comarcaDt = comarcaNe.consultarComarcaCodigo(comarcaCodigo);
			if (comarcaDt != null) {
				certidaoGuiaDt.setId_Comarca(comarcaDt.getId());
			}			
		}
		
		if (certidaoGuiaDt.getGuiaEmissaoDt().getInfoAreaSPG() != null) {
			certidaoGuiaDt.setInfoArea(certidaoGuiaDt.getGuiaEmissaoDt().getInfoAreaSPG());
		}
		
		int custaCertidao = Funcoes.StringToInt(vetorPrincipalSPG[EnumDadosHeaderCertidaoPraticaForense.CUSTA_CERTIDAO.ordinal()].trim());
		int custaTaxaJudiciaria = Funcoes.StringToInt(vetorPrincipalSPG[EnumDadosHeaderCertidaoPraticaForense.CUSTA_TAXA_JUDICIARIA.ordinal()].trim());
		int custaTotal = custaCertidao + custaTaxaJudiciaria;
		
		certidaoGuiaDt.setCustaCertidao(String.valueOf((double)custaCertidao / 100));
		certidaoGuiaDt.setCustaTaxaJudiciaria(String.valueOf((double)custaTaxaJudiciaria / 100));
		certidaoGuiaDt.setCustaTotal(String.valueOf((double)custaTotal / 100));			
	}
	
	private void preenchaDadosCertidaoPraticaForenseSPGGuiaProjudi(CertidaoGuiaDt certidaoGuiaDt, ServentiaDt serventiaDt) throws Exception {
		if (!certidaoGuiaDt.getGuiaEmissaoDt().isSerie50()) return;	
		
		String codigoExterno = serventiaDt.getServentiaCodigoExterno();
		
		if (codigoExterno != null && codigoExterno.trim().length() == 0) {
			codigoExterno = certidaoGuiaDt.getGuiaEmissaoDt().getInfoLocalCertidaoSPG();
		}
		
		String url = String.format(SPG3142T, 
				                   Funcoes.obtenhaSomenteNumeros(certidaoGuiaDt.getOabNumero()),
				                   certidaoGuiaDt.getOabUf(),
				                   certidaoGuiaDt.getDataInicialAAAAMMDD(),
				                   certidaoGuiaDt.getDataFinalAAAAMMDD(),
				                   codigoExterno);
		
		String textoRetornoSPG = getTextoResposta(url);
		certidaoGuiaDt.setTextoRetornoSPG(textoRetornoSPG);
		
		if (textoRetornoSPG == null || textoRetornoSPG.trim().length() == 0) return;	
		
		String[] vetorPrincipalSPG = textoRetornoSPG.split(SEPARADOR_CERTIDAO_PRATICA_FORENSE);	
		
		if (vetorPrincipalSPG == null || 
			vetorPrincipalSPG.length < EnumDadosHeaderCertidaoPraticaForenseProjudi.SIZE.ordinal()) {
			throw new MensagemException(String.format(MENSAGEM_ERRO_CERTIDAO_PRATICA_FORENSE, "count", textoRetornoSPG.trim()));
		}
		
		String[] vetorProcessos = vetorPrincipalSPG[EnumDadosHeaderCertidaoPraticaForenseProjudi.COMARCA_PROCESSOS.ordinal()].split(SEPARADOR_PROCESSO_CERTIDAO_PRATICA_FORENSE);
		
		certidaoGuiaDt.setComarcaSPG(vetorProcessos[0].trim());
				
		certidaoGuiaDt.setQuantidadeSPG(vetorProcessos.length - 1);		
	}
	
	public List<ProcessoCertidaoPraticaForenseDt> consultarProcessosSPGPraticaForenseAdvogado(CertidaoGuiaDt certidaoGuiaDt) throws MensagemException {
		List<ProcessoCertidaoPraticaForenseDt> listaProcessos = new ArrayList<ProcessoCertidaoPraticaForenseDt>();
		
		if (certidaoGuiaDt.getQuantidadeSPG() == 0 || certidaoGuiaDt.getTextoRetornoSPG() == null || certidaoGuiaDt.getTextoRetornoSPG().trim().length() == 0)
			return listaProcessos;	
		
		String[] vetorProcessos = null;
		if (certidaoGuiaDt.getGuiaEmissaoDt().isSerie50()) {
			String[] vetorPrincipalSPG = certidaoGuiaDt.getTextoRetornoSPG().split(SEPARADOR_CERTIDAO_PRATICA_FORENSE);	
			
			if (vetorPrincipalSPG == null || 
				vetorPrincipalSPG.length < EnumDadosHeaderCertidaoPraticaForenseProjudi.SIZE.ordinal())
					throw new MensagemException(String.format(MENSAGEM_ERRO_CERTIDAO_PRATICA_FORENSE, "proc", certidaoGuiaDt.getTextoRetornoSPG().trim()));	
			
			vetorProcessos = vetorPrincipalSPG[EnumDadosHeaderCertidaoPraticaForenseProjudi.COMARCA_PROCESSOS.ordinal()].split(SEPARADOR_PROCESSO_CERTIDAO_PRATICA_FORENSE);
		} else {
			String[] vetorPrincipalSPG = certidaoGuiaDt.getTextoRetornoSPG().split(SEPARADOR_CERTIDAO_PRATICA_FORENSE);	
			
			if (vetorPrincipalSPG == null || 
				vetorPrincipalSPG.length < EnumDadosHeaderCertidaoPraticaForense.SIZE.ordinal() ||
				Funcoes.StringToInt(vetorPrincipalSPG[EnumDadosHeaderCertidaoPraticaForense.QUANTIDADE_PROCESSOS.ordinal()].trim(), -1) == -1)
					throw new MensagemException(String.format(MENSAGEM_ERRO_CERTIDAO_PRATICA_FORENSE, "proc", certidaoGuiaDt.getTextoRetornoSPG().trim()));	
			
			vetorProcessos = vetorPrincipalSPG[EnumDadosHeaderCertidaoPraticaForense.COMARCA_PROCESSOS.ordinal()].split(SEPARADOR_PROCESSO_CERTIDAO_PRATICA_FORENSE);
		}
		
		if (vetorProcessos != null && vetorProcessos.length > 1) {
			certidaoGuiaDt.setComarcaSPG(vetorProcessos[0].trim());
			for(int i = 1; i < vetorProcessos.length; i++) {
				if (vetorProcessos[i] != null && vetorProcessos[i].trim().length() > 0) {
					String[] vetorDadosProcesso = vetorProcessos[i].split(SEPARADOR_DADOS_PROCESSO_CERTIDAO_PRATICA_FORENSE);
					if (vetorDadosProcesso == null || vetorDadosProcesso.length < EnumDadosProcessoCertidaoPraticaForense.SIZE.ordinal()) {
						throw new MensagemException(String.format(MENSAGEM_ERRO_CERTIDAO_PRATICA_FORENSE, "proc", certidaoGuiaDt.getTextoRetornoSPG().trim()));	
					}	
					
					ProcessoCertidaoPraticaForenseDt processo = new ProcessoCertidaoPraticaForenseDt();
					
					processo.setProcessoSPG(true);
					processo.setProcessoNumero(Funcoes.retiraEspacosInicioFim(vetorDadosProcesso[EnumDadosProcessoCertidaoPraticaForense.PROCESSO.ordinal()]));
					processo.setServentia(Funcoes.retiraEspacosInicioFim(vetorDadosProcesso[EnumDadosProcessoCertidaoPraticaForense.SERVENTIA.ordinal()]));
					processo.setProcessoTipo(Funcoes.retiraEspacosInicioFim(vetorDadosProcesso[EnumDadosProcessoCertidaoPraticaForense.NATUREZA.ordinal()]));
					processo.addPromovente(Funcoes.retiraEspacosInicioFim(vetorDadosProcesso[EnumDadosProcessoCertidaoPraticaForense.NOME_AUTOR.ordinal()]));	
					processo.addPromoventeAdvogado(Funcoes.retiraEspacosInicioFim(vetorDadosProcesso[EnumDadosProcessoCertidaoPraticaForense.NOME_ADVOGADO_AUTOR.ordinal()]));	
					processo.addPromovido(Funcoes.retiraEspacosInicioFim(vetorDadosProcesso[EnumDadosProcessoCertidaoPraticaForense.NOME_REU.ordinal()]));	
					processo.addPromovidoAdvogado(Funcoes.retiraEspacosInicioFim(vetorDadosProcesso[EnumDadosProcessoCertidaoPraticaForense.NOME_ADVOGADO_REU.ordinal()]));
					processo.setDataRecebimento(Funcoes.retiraEspacosInicioFim(vetorDadosProcesso[EnumDadosProcessoCertidaoPraticaForense.DATA_DISTRIBUICAO.ordinal()]));
					processo.setInicioAtuacao(Funcoes.retiraEspacosInicioFim(vetorDadosProcesso[EnumDadosProcessoCertidaoPraticaForense.DATA_INICIO_ATUACAO.ordinal()]));
					
					listaProcessos.add(processo);
				}
			}
		}	
		
		return listaProcessos;
	}
	
	@SuppressWarnings("deprecation")
	private String getTextoResposta(String HttpGetUrl) throws Exception {
		
		try(org.apache.http.impl.client.DefaultHttpClient httpclient = new org.apache.http.impl.client.DefaultHttpClient()){
			HttpGet httpget = new HttpGet(HttpGetUrl);
		
			HttpResponse response;
			String textoResposta = null;
			response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();
			if (entity != null) textoResposta = EntityUtils.toString(entity);
				
			return textoResposta;
		}
	}	
}