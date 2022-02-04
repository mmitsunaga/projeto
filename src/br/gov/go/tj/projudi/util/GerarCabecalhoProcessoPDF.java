package br.gov.go.tj.projudi.util;

/**
*
* @author Leandro de Souza Bernardes
* @email lsbernardes@tj.go.gov.br
*/

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import br.gov.go.tj.projudi.dt.ArquivoDt;
import br.gov.go.tj.projudi.dt.PendenciaArquivoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.utils.Cifrar;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;
import br.gov.go.tj.utils.ValidacaoUtil;
import br.gov.go.tj.utils.pdf.GerarPDF;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;

public class GerarCabecalhoProcessoPDF {
	
	/**
	 * Método responsável em gerar o cabeçalho e a mensagem vertical em cada Pdf
	 * 
	 *@param processo para qual será gerado o cabeçalho e mensagem vertical
	 *
	 *@param arqDoc informações do arquivo que seram adicionados ao pdf
	 *
	 *@param contMovimentacao informa o número da movimentação no processo
	 *
	 *@param contArquivo informa o número do arquivo na Movimentação do processo
	 *
	 *@param input é um InputStream que representa o arquivo no qual será adicionado o cabeçalho e mensagem vertical	 
	 */
	public static byte[] geraCabecalhoProcessoPDF(ProcessoDt processo,String pathImage, ArquivoDt arqDoc, String nomeUsuarioLogado, String contMovimentacao, int contArquivo,	byte[] input, boolean processoMigradoPje)throws Exception{
		ByteArrayOutputStream arquivoCabecalho = new ByteArrayOutputStream();
		PdfReader reader = null;
		PdfStamper pdfStamper = null;
		byte[] temp = null;
		
		try{
			input = redimensionar(input);			
			reader = new PdfReader(input);
			if (reader.getNumberOfPages() == 0) return input;
			
			pdfStamper = new PdfStamper(reader, arquivoCabecalho);
			
            PdfContentByte over;
            BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.WINANSI, BaseFont.EMBEDDED);
            
            String enderecoConferencia = ProjudiPropriedades.getInstance().getEnderecoValidacaoPublicacao();
   	     	String textoPrimeiraLinha = "Tribunal de Justiça do Estado de Goiás";
   	     	String textoSegundaLinha;
   	     	String textoTerceiraLinha;
   	     	
   	     	String textoQuartaLinha = "Validação pelo código: " + Cifrar.codificar(arqDoc.getId(), PendenciaArquivoDt.CodigoPermissao) + ", no endereço: " + enderecoConferencia;
   	     	
   	     	if(processoMigradoPje){
   	     		textoSegundaLinha = "Documento Publicado Digitalmente em " + arqDoc.getDataInsercao();
	     		textoTerceiraLinha = "Assinado no Sistema PJE";
   	     	} else {
   	     		if (!arqDoc.isECarta()){
   	     			textoSegundaLinha = "Documento Assinado e Publicado Digitalmente em " + arqDoc.getDataInsercao();
   	     			textoTerceiraLinha = "Assinado por " + arqDoc.getUsuarioAssinadorFormatado();
   	     		} else {
   	     			textoSegundaLinha = "Documento Publicado Digitalmente em " + arqDoc.getDataInsercao();
   	     			textoTerceiraLinha = "Documento sem valor jurídico, pois não possui código nos termos do provimento 10/2013 da CGJ";
   	     			textoQuartaLinha = "Sem código de validação";
   	     		}   	     		
   	     	}   	     	   	     
   	     	
			int totalPaginas = reader.getNumberOfPages() + 1;			
			
			for (int i = 1; i < totalPaginas; i++) {
                over = pdfStamper.getOverContent(i);
                
            	over.fill();
                over.saveState();
                
                over.beginText();
                
                imprimaCabecalhoLateralDireitaNaPagina(over, processo, i, reader, contMovimentacao, contArquivo, arqDoc, nomeUsuarioLogado, bf, null);
      
                imprimaRodapeNaPagina(over, pathImage, bf, textoPrimeiraLinha, textoSegundaLinha, textoTerceiraLinha, textoQuartaLinha, reader.getPageSize(i).getBottom());
                                
                over.endText();
            }
			
			pdfStamper.close();
			reader.close();
			temp = arquivoCabecalho.toByteArray();
			arquivoCabecalho.close();
		} catch(Exception e) {
			try{if (reader!=null) reader.close(); } catch(Exception ex ) {};
			try{if (pdfStamper!=null) pdfStamper.close(); } catch(Exception ex ) {};
			try{if (arquivoCabecalho!=null) arquivoCabecalho.close(); } 
			catch(Exception ex ) {};
			
			temp = GerarPDF.gerarMensagemPDF(arqDoc.getNomeArquivoFormatado(), "Não foi possível abrir arquivo "+contArquivo+" da Movimentação "+contMovimentacao+". Verifique o arquivo no processo, pois o mesmo pode estar corrompido.");
			return temp;
		}
		
		return temp;
	}
	
	/**
	 * 
	 * @param bis
	 * @return
	 * @throws Exception
	 */
	public static byte[] redimensionarArquivoHistoricoFisico(BufferedInputStream bis) throws Exception{		
		PdfReader reader = null;
		boolean acessoOK = false;
		try{
			reader = new PdfReader(bis);
			acessoOK = true;
		} catch(Exception e) {
			e.printStackTrace();
		}finally{
			if(!acessoOK) {
				throw new IOException("Erro ao acessar arquivo. Verifique se o arquivo possui proteções de acesso ou se está corrompido.");
			}
		}
		if (reader.getNumberOfPages() == 0) return null;
		
		Rectangle tamanhoOriginal = reader.getPageSize(1);
		if (tamanhoOriginal.getLeft() < PageSize.A4.getLeft() ||
			tamanhoOriginal.getBottom() < PageSize.A4.getBottom() ||
			tamanhoOriginal.getRight() < PageSize.A4.getRight() ||
			tamanhoOriginal.getTop() < PageSize.A4.getTop())
		{
			tamanhoOriginal = PageSize.A4;
		}
		
		Rectangle tamanhoNovo = new Rectangle(tamanhoOriginal.getLeft(), tamanhoOriginal.getBottom(), tamanhoOriginal.getRight(), tamanhoOriginal.getTop());
		tamanhoNovo.setRotation(tamanhoOriginal.getRotation());		
		Document document = new Document(tamanhoNovo);
        document.setMarginMirroringTopBottom(true);
        
        ByteArrayOutputStream arquivoCabecalho = new ByteArrayOutputStream();
        PdfWriter writer = PdfWriter.getInstance(document, arquivoCabecalho);
        document.open();
        
        PdfContentByte cb = writer.getDirectContent();
        for(int i = 1 ; i <= reader.getNumberOfPages(); i++){
          PdfImportedPage page = writer.getImportedPage(reader, i);
          document.newPage();        
          //As margens abaixo foram setadas para respeitar as marcas d'agua com
          //as informações do assinador do documento. 
          cb.addTemplate(page, 25, 70 + tamanhoOriginal.getBottom(), true);
        }
        document.close();
		
		return arquivoCabecalho.toByteArray();
	}
	
	public static byte[] redimensionar(byte[] input) throws Exception{		
		PdfReader reader = null;
		boolean acessoOK = false;
		try{
			reader = new PdfReader(input);
			acessoOK = true;
		} catch(Exception e) {
			e.printStackTrace();
		}finally{
			if(!acessoOK) {
				throw new IOException("Erro ao acessar arquivo. Verifique se o arquivo possui proteções de acesso ou se está corrompido.");
			}
		}
		if (reader.getNumberOfPages() == 0) return input;
		
		Rectangle tamanhoOriginal = reader.getPageSize(1);
		if (tamanhoOriginal.getLeft() < PageSize.A4.getLeft() ||
			tamanhoOriginal.getBottom() < PageSize.A4.getBottom() ||
			tamanhoOriginal.getRight() < PageSize.A4.getRight() ||
			tamanhoOriginal.getTop() < PageSize.A4.getTop())
		{
			tamanhoOriginal = PageSize.A4;
		}
		
		//O fatorDeAumento acima era usado para determinar o tamanho do documento que seria inserido no PDF final. O problema é que o fatorDeAumento era variável
		//e, dependendo do tamanho do documento, poderia não ficar correto por conta tamanho do arquivo.
		//Foi alterado por um tamanho fixo (54f e 74f) para que, independente do tamanho interno, a margem da marca d'agua com os detalhes do assinador seguirão no mesmo lugar
		//e sem invasão do documento interno. A margem fixa também garante que o topo deo documento ficará de bom tamanho para que os documentos tenham todo seu conteúdo 
		//apresentado.
		Rectangle tamanhoNovo = new Rectangle(tamanhoOriginal.getLeft(), tamanhoOriginal.getBottom(), tamanhoOriginal.getRight() + 54f, tamanhoOriginal.getTop() + 84f);
		tamanhoNovo.setRotation(tamanhoOriginal.getRotation());		
		Document document = new Document(tamanhoNovo);
        document.setMarginMirroringTopBottom(true);
        
        ByteArrayOutputStream arquivoCabecalho = new ByteArrayOutputStream();
        PdfWriter writer = PdfWriter.getInstance(document, arquivoCabecalho);
        document.open();
        
        PdfContentByte cb = writer.getDirectContent();
        for(int i = 1 ; i <= reader.getNumberOfPages(); i++){
          PdfImportedPage page = writer.getImportedPage(reader, i);
          document.newPage();        
          //As margens abaixo foram setadas para respeitar as marcas d'agua com
          //as informações do assinador do documento. 
          cb.addTemplate(page, 25, 70 + tamanhoOriginal.getBottom(), true);
        }
        document.close();
		
		return arquivoCabecalho.toByteArray();
	}
	
	public static void imprimaCabecalhoLateralDireitaNaPagina(PdfContentByte over, ProcessoDt processo, int numeroPagina, PdfReader reader, String contMovimentacao, int contArquivo, ArquivoDt arqDoc, String nomeUsuarioLogado, BaseFont bf, String numeroProcesso) throws Exception
	{
		 over.setFontAndSize(bf, 9);
         over.setColorFill(BaseColor.RED);
         
         if (processo != null)
         {
        	 over.showTextAligned(PdfContentByte.ALIGN_LEFT,"Processo: "+processo.getProcessoNumeroCompleto(), 20, (reader.getPageSize(numeroPagina).getHeight() + reader.getPageSize(numeroPagina).getBottom() - 20), 0);
         }
         else if (numeroProcesso != null && numeroProcesso.length() > 0)
         {
             over.setFontAndSize(bf, 10);
             over.setColorFill(BaseColor.RED);
             over.showTextAligned(PdfContentByte.ALIGN_LEFT,"Processo: "+Funcoes.formataNumeroProcesso(numeroProcesso), 20, (reader.getPageSize(numeroPagina).getHeight() + reader.getPageSize(numeroPagina).getBottom() - 20), 0);
         }
         
         if (contMovimentacao != null)
        	 over.showTextAligned(PdfContentByte.ALIGN_LEFT,"Movimentacao "+contMovimentacao, 20, (reader.getPageSize(numeroPagina).getHeight() + reader.getPageSize(numeroPagina).getBottom() - 30), 0);
         
         if (arqDoc != null)
        	 over.showTextAligned(PdfContentByte.ALIGN_LEFT,"Arquivo "+contArquivo+" : "+arqDoc.getNomeArquivoFormatado(), 20, (reader.getPageSize(numeroPagina).getHeight() + reader.getPageSize(numeroPagina).getBottom() - 40), 0);
         
         over.setColorFill(BaseColor.RED);
         SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");				
         over.showTextAligned(PdfContentByte.ALIGN_LEFT,"Usuário: "+nomeUsuarioLogado+" - Data: "+df.format(new Date()),  (reader.getPageSize(numeroPagina).getWidth() + reader.getPageSize(numeroPagina).getBottom() - 50), (reader.getPageSize(numeroPagina).getHeight() + reader.getPageSize(numeroPagina).getBottom() - 40), -90);
			
         if (processo != null)
         {
	         over.setTextMatrix(30, 30);                
	         over.showTextAligned(Element.ALIGN_LEFT, processo.getServentia().toUpperCase(),  (reader.getPageSize(numeroPagina).getWidth() - 40), (reader.getPageSize(numeroPagina).getHeight() + reader.getPageSize(numeroPagina).getBottom() - 40), -90);
	         over.showTextAligned(Element.ALIGN_LEFT, processo.getProcessoTipo(),  (reader.getPageSize(numeroPagina).getWidth() - 30), (reader.getPageSize(numeroPagina).getHeight() + reader.getPageSize(numeroPagina).getBottom() - 40), -90);
	         over.showTextAligned(Element.ALIGN_LEFT, "Valor: R$ "+processo.getValor()+" | "+"Classificador: "+processo.getClassificador(),  (reader.getPageSize(numeroPagina).getWidth() - 20), (reader.getPageSize(numeroPagina).getHeight() + reader.getPageSize(numeroPagina).getBottom() - 40), -90);
         }
	}
	
	public static void imprimaCabecalhoNaPagina(PdfContentByte over, PdfReader reader, BaseFont bf, ProcessoDt processo, int numeroPagina)
	{
		if (processo != null){
			over.setFontAndSize(bf, 9);
	        over.setColorFill(BaseColor.RED);
	        over.showTextAligned(PdfContentByte.ALIGN_LEFT,"Processo: "+processo.getProcessoNumeroCompleto(), 20, (reader.getPageSize(numeroPagina).getHeight() + reader.getPageSize(numeroPagina).getBottom() - 20), 0);
	        if (processo.getServentia() != null){
	        	over.showTextAligned(PdfContentByte.ALIGN_LEFT, processo.getServentia().toUpperCase(), 20, (reader.getPageSize(numeroPagina).getHeight() + reader.getPageSize(numeroPagina).getBottom() - 30), 0);
	        }
		}
	}
	
	
	public static void imprimaRodapeNaPagina(PdfContentByte over, String pathImage, BaseFont bf, String textoPrimeiraLinha, String textoSegundaLinha, String textoTerceiraLinha, String textoQuartaLinha, float tamanhoBotton) throws MalformedURLException, IOException, DocumentException
	{
		com.itextpdf.text.Image logo =  com.itextpdf.text.Image.getInstance(pathImage);
        logo.setAbsolutePosition(0f, tamanhoBotton);
        over.addImage(logo);
        
        over.setFontAndSize(bf, 9);
        over.setColorFill(BaseColor.BLUE);
        over.setTextMatrix(30, 30);
        over.showTextAligned(Element.ALIGN_LEFT, textoPrimeiraLinha, 65, 35 + tamanhoBotton, 0);
        
        over.setFontAndSize(bf, 8);
        over.setColorFill(BaseColor.BLUE);
        over.setTextMatrix(30, 30);
        over.showTextAligned(Element.ALIGN_LEFT, textoSegundaLinha, 65, 25 + tamanhoBotton, 0);
        
        over.setFontAndSize(bf, 8);
        over.setColorFill(BaseColor.BLUE);
        over.setTextMatrix(30, 30);
        over.showTextAligned(Element.ALIGN_LEFT, textoTerceiraLinha, 65, 15 + tamanhoBotton, 0);
        
        over.setFontAndSize(bf, 8);
        over.setColorFill(BaseColor.BLUE);
        over.setTextMatrix(30, 30);
        over.showTextAligned(Element.ALIGN_LEFT, textoQuartaLinha, 65, 5 + tamanhoBotton, 0);
	}
	
	public static byte[] geraCabecalhoProcessoPDF2(ProcessoDt processo,String pathImage, ArquivoDt arqDoc, String nomeUsuarioLogado, String contMovimentacao, int contArquivo,	byte[] input)throws Exception{
		return GerarCabecalhoProcessoPDF.geraCabecalhoProcessoPDF2(processo, pathImage, arqDoc, nomeUsuarioLogado, contMovimentacao, contArquivo, GerarCabecalhoProcessoPDF.redimensionar(input));
	}
		
	public static byte[] geraCabecalhoProcessoSemArquivoPDF(ProcessoDt processo, String nomeUsuarioLogado, String contMovimentacao, byte[] input)throws Exception{
		ByteArrayOutputStream arquivoCabecalho = new ByteArrayOutputStream();
		byte[] temp= null;
		PdfReader reader = null;
		PdfStamper pdfStamper = null;
		try{
			input = redimensionar(input);
			reader = new PdfReader(input);
			pdfStamper = new PdfStamper(reader,arquivoCabecalho);

            PdfContentByte over;
            BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.WINANSI, BaseFont.EMBEDDED);
            
			int totalPaginas = reader.getNumberOfPages() + 1;
			for (int i = 1; i < totalPaginas; i++) {
				over = pdfStamper.getOverContent(i);
                
            	over.fill();
                over.saveState();
                
                over.beginText();
                
                imprimaCabecalhoLateralDireitaNaPagina(over, processo, i, reader, contMovimentacao, 0, null, nomeUsuarioLogado, bf, null);
                
                over.endText();
			}
			pdfStamper.close();	
			reader.close();
			temp = arquivoCabecalho.toByteArray();
			arquivoCabecalho.close();
		} catch(Exception e) {
			try{if (pdfStamper!=null) pdfStamper.close(); } catch(Exception ex ) {};
			try{if (reader!=null) reader.close(); } catch(Exception ex ) {};
			try{if (arquivoCabecalho!=null) arquivoCabecalho.close(); } catch(Exception ex ) {};
			throw e;
		}
		return temp;
	}
	
	public static byte[] geraCabecalhoArquivoPDF(String pathImage, ArquivoDt arqDoc, String nomeUsuarioLogado, String numeroProcesso, byte[] input, int contArquivo)throws Exception{
		ByteArrayOutputStream arquivoCabecalho = new ByteArrayOutputStream();
		PdfReader reader = null;
		PdfStamper pdfStamper = null;
		byte[] temp = null;
		try{
			input = redimensionar(input);			
			
			reader = new PdfReader(input);
			if (reader.getNumberOfPages() == 0) return input;
			
			pdfStamper = new PdfStamper(reader,arquivoCabecalho);			
			
            PdfContentByte over;
            BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.WINANSI, BaseFont.EMBEDDED);
            pathImage = pathImage + "imagens" + File.separator + "TesteChave.gif";
            
            String enderecoConferencia = ProjudiPropriedades.getInstance().getEnderecoValidacaoPublicacao();
   	     	String textoPrimeiraLinha = "Tribunal de Justiça do Estado de Goiás";
   	     	String textoSegundaLinha = "Documento Assinado e Publicado Digitalmente em " + arqDoc.getDataInsercao();
   	     	String textoTerceiraLinha = "Assinado por " + arqDoc.getUsuarioAssinadorFormatado();
   	     	String textoQuartaLinha = "Validação pelo código: " + Cifrar.codificar(arqDoc.getId(),  PendenciaArquivoDt.CodigoPermissao) + ", no endereço: " + enderecoConferencia;
			int totalPaginas = reader.getNumberOfPages() + 1;
			for (int i = 1; i < totalPaginas; i++) {
                over = pdfStamper.getOverContent(i);
                
                over.beginText();
                
                imprimaCabecalhoLateralDireitaNaPagina(over, null, i, reader, null, contArquivo, arqDoc, nomeUsuarioLogado, bf, numeroProcesso);                
				
                imprimaRodapeNaPagina(over, pathImage, bf, textoPrimeiraLinha, textoSegundaLinha, textoTerceiraLinha, textoQuartaLinha, reader.getPageSize(i).getBottom());
                
                over.endText();
			}
			pdfStamper.close();
			reader.close();
			temp = arquivoCabecalho.toByteArray();
			arquivoCabecalho.close();
		} catch(Exception e) {
			try{if (reader!=null) reader.close(); } catch(Exception ex ) {};
			try{if (pdfStamper!=null) pdfStamper.close(); } catch(Exception ex ) {};
			try{if (arquivoCabecalho!=null) arquivoCabecalho.close(); } catch(Exception ex ) {};
			throw e;
		}
		
		return temp;
	}
	
	/**
	 * Método que verifica se as permissões de alteração e acesso no arquivo PDF estão liberadas.
	 * Caso não esteja, retorna a mensagem no finally.
	 * 
	 * @param input - conteúdo do arquivo
	 * @throws Exception
	 * @author hmgodinho
	 */
	public static void validarAcessoPDF(byte[] input)throws Exception{		
		PdfReader reader = null;
		boolean acessoOK = false;
		try{
			reader = new PdfReader(input);
			acessoOK = true;
		} catch(Exception e) {
			e.printStackTrace();
		}finally{
			if(!acessoOK) {
				throw new MensagemException("Erro ao acessar arquivo. Verifique se o arquivo possui proteções de acesso ou se está corrompido.");
			}
		}
	}

	public static void imprimirEstampaLateralPagina(PdfContentByte over, float width, float height, float bottom, ProcessoDt processo, String nomeUsuarioLogado, String tipoMovimentacao, String nomeArquivo) {
		try {
			BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.WINANSI, BaseFont.EMBEDDED);
			over.setFontAndSize(bf, 9);
	        over.setColorFill(BaseColor.RED);
	        if (ValidacaoUtil.isNaoNulo(processo)) {
	        	over.showTextAligned(PdfContentByte.ALIGN_LEFT, "Processo: " + processo.getProcessoNumeroCompleto(), 20, (height + bottom - 20), 0);
	        }
	        if (ValidacaoUtil.isNaoVazio(tipoMovimentacao)) {
	        	over.showTextAligned(PdfContentByte.ALIGN_LEFT, "Movimentacao " + tipoMovimentacao, 20, (height + bottom - 30), 0);
	        }
	        if (ValidacaoUtil.isNaoVazio(nomeArquivo)) {
	        	over.showTextAligned(PdfContentByte.ALIGN_LEFT, "Arquivo " + nomeArquivo , 20, (height + bottom - 40), 0);
	        }
	        over.setColorFill(BaseColor.RED);
	        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	        over.showTextAligned(PdfContentByte.ALIGN_LEFT, "Usuário: " + nomeUsuarioLogado + " - Data: " + df.format(new Date()), (width + bottom - 50), (height + bottom - 40), -90);
	        
	        if (ValidacaoUtil.isNaoNulo(processo)){
	        	over.setTextMatrix(30, 30);
	            over.showTextAligned(Element.ALIGN_LEFT, processo.getServentia().toUpperCase(),  (width - 40), (height + bottom - 40), -90);
	            over.showTextAligned(Element.ALIGN_LEFT, processo.getProcessoTipo(),  (width - 30), (height + bottom - 40), -90);
	            over.showTextAligned(Element.ALIGN_LEFT, "Valor: R$ "+processo.getValor()+" | "+"Classificador: "+processo.getClassificador(),  (width - 20), (height + bottom - 40), -90);
	        }
		}
		catch (DocumentException e) {} 
		catch (IOException e) {}
	}
	
}
