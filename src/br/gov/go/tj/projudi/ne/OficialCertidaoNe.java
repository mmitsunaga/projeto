package br.gov.go.tj.projudi.ne;


import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import net.sourceforge.barbecue.Barcode;
import net.sourceforge.barbecue.BarcodeFactory;
import net.sourceforge.barbecue.BarcodeImageHandler;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.OficialCertidaoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.ps.OficialCertidaoPs;
import br.gov.go.tj.utils.Cifrar;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.Relatorios;
import br.gov.go.tj.utils.pdf.ConcatenatePDF;
import br.gov.go.tj.utils.pdf.ConverterHtmlPdf;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;


//---------------------------------------------------------
public class OficialCertidaoNe extends OficialCertidaoNeGen{

//

/**
	 * 
	 */
	private static final long serialVersionUID = 5021199505212807181L;

	
	//---------------------------------------------------------
	public void salvar(OficialCertidaoDt dados ) throws Exception {

		LogDt obLogDt;

		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			OficialCertidaoPs obPersistencia = new  OficialCertidaoPs(obFabricaConexao.getConexao()); 

			/* use o iu do objeto para saber se os dados ja est�o ou n�o salvos */
			if (dados.getId().length()==0) {
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("OficialCertidao",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {
				obPersistencia.alterar(dados); 
				obLogDt = new LogDt("OficialCertidao",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	public void excluir(OficialCertidaoDt dados) throws Exception {

		LogDt obLogDt;

		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			OficialCertidaoPs obPersistencia = new  OficialCertidaoPs(obFabricaConexao.getConexao()); 

			obLogDt = new LogDt("OficialCertidao",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId());

			dados.limpar();
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	//---------------------------------------------------------
	public  String Verificar(OficialCertidaoDt dados ) {

		String stRetorno="";

		if (dados.getModelo().length()==0){
			dados.limpar();
			stRetorno += "O Campo modelo de certid�o � um campo obrigat�rio. \n";

		}else{
			if (dados.getNumeroMandado().length()==0){
				//Limpa o objeto
				String idModelo = dados.getId_Modelo();
				String Modelo = dados.getModelo();
				dados.limpar();
				dados.setId_Modelo(idModelo);
				dados.setModelo(Modelo);
				stRetorno += "O Campo n�mero do mandado � um campo obrigat�rio. \n";
			}
		}
		return stRetorno;

	}
	
	public  String VerificarTexto(OficialCertidaoDt dados ) {

		String stRetorno="";

		if (dados.getTexto().length()==0)
			stRetorno += "Favor incluir a certid�o. \n";
		return stRetorno;

	}
	
	
	
	public String consultarMandado(OficialCertidaoDt OficialCertidaodt) throws Exception{
		String mensagem = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			OficialCertidaoPs obPersistencia = new  OficialCertidaoPs(obFabricaConexao.getConexao());
			
			mensagem = obPersistencia.consultarMandado(OficialCertidaodt);
			
			if (mensagem.equals("")){//se n�o retornou preencher o texto do objeto  OficialCertidaodt
				//Verifica se o promovido e o promovente est�o em branco, caso esteja, fazer consulta das partes no projudi passando o n�mero completo do processo(CNJ)'
				if(OficialCertidaodt.getProcessoPromoventeNome().equals("") && OficialCertidaodt.getProcessoPromovidoNome().equals("")){
					String exemploNumeroProcesso = "5000002.56.2012.8.09.0059"; //Substituir este pelo n�mero do processos
					ProcessoNe processoNe = new ProcessoNe();
					ProcessoDt processoDt = new ProcessoDt();
					processoDt = processoNe.consultarProcessoNumeroCompleto(exemploNumeroProcesso,null);
					
					if(processoDt!=null){
						OficialCertidaodt.setProcessoPromoventeNome(((ProcessoParteDt)processoDt.getListaPolosAtivos().get(0)).getNome());
						OficialCertidaodt.setProcessoPromovidoNome(((ProcessoParteDt)processoDt.getListaPolosPassivos().get(0)).getNome());
					}

					
					
					//	OficialCertidaodt.setProcessoPromoventeNome("Promovente Jelves Projudi");
				//	OficialCertidaodt.setProcessoPromovidoNome("Promovido Lopes Projudi");
					
					//jelveslopesdasilva
					
				}
			}
			
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return mensagem;
	}
	
	public List consultarModelos2(String descricao, String posicao, String id_ArquivoTipo, UsuarioDt usuarioDt) throws Exception {
		ModeloNe Modelone = new ModeloNe();
		return Modelone.consultarModelos(descricao, posicao, id_ArquivoTipo, usuarioDt);
	}
	
	
	//Criada para trazer somente as certid�es especifica da serventia do usu�rio. jlsilva 04/09/2012
	public List consultarModeloTipoArquivo(UsuarioDt usuarioDt, String CodigoArquivoTipo, String tempNomeBusca, String posicaoPaginaAtual) throws Exception{
		List tempList = null;
		ArquivoTipoNe arquivoTipoNe = new ArquivoTipoNe();
		String id_ArquivoTipo  = arquivoTipoNe.consultarIdArquivoTipo(CodigoArquivoTipo); //Retorna o id do arquivo, no qual foi passado o ArquivoTipoCodigo
		ModeloNe neObjeto = new ModeloNe();
		
		tempList = neObjeto.consultarModelos(tempNomeBusca, posicaoPaginaAtual, id_ArquivoTipo, usuarioDt);
		QuantidadePaginas = neObjeto.getQuantidadePaginas();
		
		neObjeto = null;
		return tempList;
	}
	
	public String consultarModeloTipoArquivoJSON(UsuarioDt usuarioDt, String CodigoArquivoTipo, String tempNomeBusca, String posicaoPaginaAtual) throws Exception{
		String stTemp = "";
		ArquivoTipoNe arquivoTipoNe = new ArquivoTipoNe();
		String id_ArquivoTipo  = arquivoTipoNe.consultarIdArquivoTipo(CodigoArquivoTipo); //Retorna o id do arquivo, no qual foi passado o ArquivoTipoCodigo
		ModeloNe neObjeto = new ModeloNe();
		
		stTemp = neObjeto.consultarModelosJSON(tempNomeBusca, posicaoPaginaAtual, id_ArquivoTipo, usuarioDt);
		
		return stTemp;
	}
	
	public List consultarDescricao(String descricao, String posicao ) throws Exception {
		List tempList=null;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

		OficialCertidaoPs obPersistencia = new  OficialCertidaoPs(obFabricaConexao.getConexao()); 

			tempList=obPersistencia.consultarDescricao( descricao, posicao);
			QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
			tempList.remove(tempList.size()-1);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return tempList;   
	}
	
	
	public List consultarMandados(String numeroMandado, String DataInicial, String DataFinal, String id_Usuario, String posicao ) throws Exception {
		List tempList=null;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

		OficialCertidaoPs obPersistencia = new  OficialCertidaoPs(obFabricaConexao.getConexao()); 

			tempList=obPersistencia.consultarMandados( numeroMandado, DataInicial, DataFinal, id_Usuario, posicao);
			QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
			tempList.remove(tempList.size()-1);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		
		return tempList;   
	}
	
	
	
	public String consultarMandadosJSON(String numeroMandado, String DataInicial, String DataFinal, String id_Usuario, String posicao ) throws Exception {
		FabricaConexao obFabricaConexao = null;
		String stTemp = "";
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

		OficialCertidaoPs obPersistencia = new  OficialCertidaoPs(obFabricaConexao.getConexao()); 

		stTemp = obPersistencia.consultarMandadosJSON(numeroMandado, DataInicial, DataFinal, id_Usuario, posicao);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}
	
	
	
	/**
	 * M�todo utilizado para trazer uma lista de certid�es selecionadas pelo o usu�rio.
	 * 
	 * @author Jelves Lopes - 13/02/2013
	 */
	public List consultarMandadosSelecionados(String[] idsDados, String id_Usuario, String posicao ) throws Exception {
		List tempList=null;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

		OficialCertidaoPs obPersistencia = new  OficialCertidaoPs(obFabricaConexao.getConexao()); 

			tempList=obPersistencia.consultarMandadosSelecionados( idsDados, id_Usuario, posicao);
			QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
			tempList.remove(tempList.size()-1);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return tempList;   
	}
	
	
	/**
	 * M�todo utilizado para imprimir uma lista de certid�es definida pelo o usu�rio
	 * 
	 * @param diretorioProjeto - caminho do diret�rio raiz do projeto 
	 * @return lista de certid�es
	 * @author Jelves
	 * @throws Exception 
	 */
	public byte[] relCertidao(String diretorioProjeto, List listaCertidao ) throws Exception{
		byte[] temp = null;
		String pathRelatorio = Relatorios.getPathRelatorio(diretorioProjeto);
		String nomeRelatorio = "oficialCertidao";

		// PAR�METROS DO RELAT�RIO
		Map parametros = new HashMap();
//		parametros.put("caminhoLogo", Relatorios.getCaminhoLogotipoRelatorio(diretorioProjeto));
//		parametros.put("titulo", "Relat�rio de certid�es");
		//parametros.put("dataAtual", new Date());
		//parametros.put("dataInicial", Funcoes.identificarNomeMes(Funcoes.StringToInt(mesInicial)) + " de " + anoInicial);
		//parametros.put("dataFinal", Funcoes.identificarNomeMes(Funcoes.StringToInt(mesFinal)) + " de " + anoFinal);
//		parametros.put("usuarioResponsavelRelatorio", "Jelves");
		
		temp = Relatorios.gerarRelatorioPdf(pathRelatorio, nomeRelatorio, parametros, listaCertidao);
		return temp;
	}


	
	/**
	 * M�todo utilizado para imprimir uma lista de certid�es definida pelo o usu�rio
	 * 
	 * @param diretorioProjeto - caminho do diret�rio raiz do projeto 
	 * @return lista de certid�es
	 * @author Jelves
	 */
	public byte[] gerarVariasCertidoes(String diretorioProjeto, List listaCertidao ) throws Exception {
	//	byte[] temp = null;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] resultado = null;
		//gerarCapaCertidaoPDF(out);
		//out.writeTo(resultado);
		
		//PdfContentByte over;
		byte[] arquivoHTML = null;
		try{
			//Caso exista uma lista de mandado � preenchida
	        if (listaCertidao != null) {
	        	
	            for (int i=0; i<listaCertidao.size(); i++) {
	            	OficialCertidaoDt oficialCertidaoDt = (OficialCertidaoDt) listaCertidao.get(i);
	            	
	//            	String pagina = "<style> @page {size: 17.18in 15.88in; margin: 0.25in; -fs-flow-top:header; -fs-flow-bottom: footer;"+
	//            	" -fs-flow-left: left; -fs-flow-right: right; border: thin solid black; padding: 1em; } </style>";
	          //  	this.GeraCodigoBarras(oficialCertidaoDt.getNumeroMandado()); //Gera imagem para cada mandado, gravando em projetos/eclipse com numero mandado.jpg
	
	            	String pagina = "<style> @page {" +
	            	"size: 10.80in 15.88in; " +//Largura x Altura
	            	"margin: 0.50in; " +
	            	//"font-size:300px"+
	//            	"-fs-flow-top:header; " +
	//            	"-fs-flow-bottom: footer;"+
	//            	" -fs-flow-left: left; " +
	//            	"-fs-flow-right: right; " +
	//            	//"border: thin solid black; " +
	            	//"padding: 1em; " +
	            	"}" +
	            	"</style>";
	//            	pagina = pagina + "<style> p{" +
	//            	//"font-size:21px;" +
	//            	"font-size:137%;" +
	//                	"}" +
	//                	"</style>";
	
	            	oficialCertidaoDt.setTexto(pagina+oficialCertidaoDt.getTexto());
	               	out = new ByteArrayOutputStream();
	            	out.write(oficialCertidaoDt.getTexto().getBytes());
	                        	          	            	
	            	//Converte o texto em um arquivo html
	            	arquivoHTML = ConverterHtmlPdf.converteHtmlPDF(oficialCertidaoDt.getTexto().getBytes(), false);
	            	
	            	if (arquivoHTML.length > 0) {
	            		arquivoHTML = this.geraCodigoBarraPDF(oficialCertidaoDt.getNumeroMandado(), oficialCertidaoDt.getId(), arquivoHTML);
	            		
	            		//Pega o id da certid�o e transforma em um c�digo Hash
	            		//arquivoHTML = this.geraCodigoHashPDF(oficialCertidaoDt.getId(), arquivoHTML.toByteArray());
	            		if (resultado != null && resultado.length>0){
	            			//Antes de concatenar, insiro o c�digo de barra, ou seja, antes de montar o pdf completo
	
	            			resultado = ConcatenatePDF.concatenaPdf(resultado, arquivoHTML);
	//            			resultado = this.geraCodigoBarraPDF(oficialCertidaoDt.getNumeroMandado(), resultado.toByteArray());
	            		}else{
	            			resultado = arquivoHTML;            	
	  //          			resultado = this.geraCodigoBarraPDF(oficialCertidaoDt.getNumeroMandado(), resultado.toByteArray());
	            		}
	
	            	}	            	
	            }
	        }
	        out.close();
	        
		}catch(Exception e){
			try{if (out!=null) out.close(); } catch(Exception ex ) {};
			//try{if (arquivoHTML!=null) arquivoHTML.close(); 			
		}
			
			//return temp;
       
        return resultado; 
        
	}

	
	/**
	 * M�todo utilizado para imprimir uma certid�o
	 * 
	 * @param diretorioProjeto - caminho do diret�rio raiz do projeto 
	 * @return Usado para validar uma certid�o
	 * @author Jelves
	 */
	public byte[] gerarCertidao(String diretorioProjeto, OficialCertidaoDt oficialCertidaoDt ) throws Exception {
	//	byte[] temp = null;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		//gerarCapaCertidaoPDF(out);
		//out.writeTo(resultado);
				
		byte[] arquivoHTML = null;
		try{
			
			//Caso exista uma lista de mandado � preenchida
	        if (oficialCertidaoDt != null) {
	        	
	//            	String pagina = "<style> @page {size: 17.18in 15.88in; margin: 0.25in; -fs-flow-top:header; -fs-flow-bottom: footer;"+
	//            	" -fs-flow-left: left; -fs-flow-right: right; border: thin solid black; padding: 1em; } </style>";
	          //  	this.GeraCodigoBarras(oficialCertidaoDt.getNumeroMandado()); //Gera imagem para cada mandado, gravando em projetos/eclipse com numero mandado.jpg
	
	            	String pagina = "<style> @page {" +
	            	"size: 10.80in 15.88in; " +//Largura x Altura
	            	"margin: 0.50in; " +
	            	//"font-size:300px"+
	//            	"-fs-flow-top:header; " +
	//            	"-fs-flow-bottom: footer;"+
	//            	" -fs-flow-left: left; " +
	//            	"-fs-flow-right: right; " +
	//            	//"border: thin solid black; " +
	            	//"padding: 1em; " +
	            	"}" +
	            	"</style>";
	//            	pagina = pagina + "<style> p{" +
	//            	//"font-size:21px;" +
	//            	"font-size:137%;" +
	//                	"}" +
	//                	"</style>";
	
	            	oficialCertidaoDt.setTexto(pagina+oficialCertidaoDt.getTexto());
	               	out = new ByteArrayOutputStream();
	            	out.write(oficialCertidaoDt.getTexto().getBytes());
	                        	          	            	
	            	//Converte o texto em um arquivo html
	            	arquivoHTML = ConverterHtmlPdf.converteHtmlPDF(oficialCertidaoDt.getTexto().getBytes(), false);
	            	
	            	if (arquivoHTML.length > 0) {
	            		arquivoHTML = this.geraCodigoBarraPDF(oficialCertidaoDt.getNumeroMandado(), oficialCertidaoDt.getId(), arquivoHTML);
	            	}
	            	
	        }
	        out.close();
        
		}catch(Exception e){
			try{if (out!=null) out.close();  }catch(Exception e2) {		}			
		}
			return arquivoHTML; 
		}
	
	
//	private void gerarCapaCertidaoPDF(OutputStream outputStream)throws Exception {
//		Document document = new Document(PageSize.A4, 50, 50, 50, 50);
//	PdfWriter writer = PdfWriter.getInstance(document,outputStream);
//	document.open();
//	document.close();
//		
//	}
	
	
	   /** 
	   * M�todo respons�vel por gerar c�digo de barras Code39 
	   * @param value Valor que ser� transformado em c�digo de barras 
	   */  
	   public Barcode GeraCodigoBarras(String value){ 
		   Barcode barcode = null;
		   try{ 
			   barcode = BarcodeFactory.create3of9(value, false);  
			   BufferedImage image = new BufferedImage(220, 130, BufferedImage.TYPE_BYTE_GRAY);  
			   Graphics2D g = (Graphics2D) image.getGraphics();  
			   g.setBackground(Color.BLUE);  
			   barcode.draw(g, 10, 56);  
			   File f = new File(value + ".jpg");  
	           // Let the barcode image handler do the hard work  
			   BarcodeImageHandler.saveJPEG(barcode, f);  
		   }catch(Exception ex){ 
			   ex.getMessage();  
		   }
		return barcode;  
	  }  
	   
	   
	   //Recebe o pdf e o n�mero do mandado e inclui o c�digo barra e um c�digo Hash dentro do pdf
		public  byte[] geraCodigoBarraPDF(String NumeroMandado, String IdCertidao, byte[] input) throws Exception {	

			ByteArrayOutputStream arquivoCabecalho = new ByteArrayOutputStream();
			PdfReader reader = null;
			PdfStamper pdfStamper = null;
			byte[] temp= null;
			
			try{
				reader = new PdfReader(input);
				pdfStamper = new PdfStamper(reader,arquivoCabecalho);
				
				//PdfContentByte under;
	            PdfContentByte over;
	            
	            BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.WINANSI, BaseFont.EMBEDDED);
	            
				int totalPaginas = reader.getNumberOfPages() + 1;
				for (int i = 1; i < totalPaginas; i++) {
					//under = pdfStamper.getUnderContent(i);
	                over = pdfStamper.getOverContent(i);
	                over.beginText();
	                over.setFontAndSize(bf, 20);
	                over.setColorFill(BaseColor.RED);

	                //Gera o c�digo de barras
					if(i==1){//Imprime somente na primeira p�gina de cada certid�o
						String CodigoValidacao = "Valida��o pelo c�digo: "+Cifrar.codificarId_certidao(IdCertidao)+", no endere�o: https://projudi.tjgo.jus.br/ValidaCertidao";
						
						Image logo =  Funcoes.CodigoBarraGeraImagem(NumeroMandado,"3of9");
		                over.addImage( com.itextpdf.text.Image.getInstance(logo , null), 150, 0, 0, 65,610, 1060);
		                
						// over.addImage( com.lowagie.text.Image.getInstance(logo , null), 200, 0, 0, 72,550, 750);
		                //1. (Largura da Imagem na horizontal) 2. (Alinhamento transversal para cima) 3. (Alinhamento transversal para cima deitado) 4. (Largura da imagem na vertical)
		                //5. (Alinhamento da imagem na horizontal)  6. (Alinhamento da imagem na vertical)

		                //Incluir texto
		                over.setFontAndSize(bf, 10);
		                over.setColorFill(BaseColor.BLUE);
		                over.setTextMatrix(30, 30);
		                over.showTextAligned(Element.ALIGN_LEFT, CodigoValidacao, 62, 45, 0);

		                
					}
	                over.endText();
				}
				pdfStamper.close();	
				reader.close();
				temp = arquivoCabecalho.toByteArray();
				arquivoCabecalho.close();
			} catch(Exception e) {
				try{if (pdfStamper!=null) pdfStamper.close(); } catch(Exception ex ) {};
				try{if (reader!=null) reader.close(); } catch(Exception ex ) {};
				try{if (arquivoCabecalho!=null) arquivoCabecalho.close();  }catch(Exception e2) {		}
			}
			return temp;
		}
	
		
	
		
		
		
		
		//Transforma o id da certid�o em c�digo hash para ser v�lidado mais tarde.
		
		public static byte[] escreverTextoPDF(byte[] input, String pathImage ,String textoPrimeiraLinha, String textoSegundaLinha, String terceiraLinha, String quartaLinha) throws Exception {	
			ByteArrayOutputStream TextoPDF = new ByteArrayOutputStream();
			PdfReader reader = null;
			PdfStamper pdfStamper = null;
			byte[] temp= null;
			try{
				 reader = new PdfReader(input);
				 pdfStamper = new PdfStamper(reader,TextoPDF);
				
				//PdfContentByte under;
	            PdfContentByte over;
	            BaseFont bf = BaseFont.createFont(BaseFont.COURIER_BOLD, BaseFont.WINANSI, BaseFont.EMBEDDED);
	            
				int totalPaginas = reader.getNumberOfPages() + 1;
				for (int i = 1; i < totalPaginas; i++) {
					//under = pdfStamper.getUnderContent(i);
	                over = pdfStamper.getOverContent(i);
	                over.beginText();
	                
	                Image logo =  ImageIO.read(new File(pathImage));
	                over.addImage(com.itextpdf.text.Image.getInstance(logo , null), 591, 0, 0, 60,2, 5);
	                
	                over.setFontAndSize(bf, 10);
	                over.setColorFill(BaseColor.BLUE);
	                over.setTextMatrix(30, 30);
	                over.showTextAligned(Element.ALIGN_LEFT, textoPrimeiraLinha, 62, 45, 0);
	                
	                over.setFontAndSize(bf, 9);
	                over.setColorFill(BaseColor.BLUE);
	                over.setTextMatrix(30, 30);
	                over.showTextAligned(Element.ALIGN_LEFT, textoSegundaLinha, 62, 35, 0);
	                
	                over.setFontAndSize(bf, 9);
	                over.setColorFill(BaseColor.BLUE);
	                over.setTextMatrix(30, 30);
	                over.showTextAligned(Element.ALIGN_LEFT, terceiraLinha, 62, 25, 0);
	                
	                over.setFontAndSize(bf, 9);
	                over.setColorFill(BaseColor.BLUE);
	                over.setTextMatrix(30, 30);
	                over.showTextAligned(Element.ALIGN_LEFT, quartaLinha, 62, 15, 0);
	                
	                over.endText();
				}
				pdfStamper.close();	
				reader.close();
				temp = TextoPDF.toByteArray();
				TextoPDF.close();
			} catch(Exception e) {
				try{if (pdfStamper!=null) pdfStamper.close(); } catch(Exception ex ) {};
				try{if (reader!=null) reader.close(); } catch(Exception ex ) {};
				try{if (TextoPDF!=null) TextoPDF.close();  }catch(Exception e2) {		}
			}
			return temp;
		}
		
	  
		public OficialCertidaoDt consultarCertidao(String idCertidao) throws Exception{
			OficialCertidaoDt oficialCertidaoDt = null;
			FabricaConexao obFabricaConexao = null;
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			try{

				OficialCertidaoPs obPersistencia = new  OficialCertidaoPs(obFabricaConexao.getConexao()); 
				oficialCertidaoDt = obPersistencia.consultarId(idCertidao);
			
			}finally{
				obFabricaConexao.fecharConexao();
			}
			return oficialCertidaoDt;   
		}
		
		

	
}
