package br.gov.go.tj.projudi.util;

/**
*
* @author Leandro de Souza Bernardes
* @email lsbernardes@tj.go.gov.br
*/

import java.io.ByteArrayOutputStream;
import java.util.Iterator;

import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteDt;
import br.gov.go.tj.utils.Funcoes;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.PdfWriter;

//import com.lowagie.text.Chapter;
//import com.lowagie.text.Document;
//import com.lowagie.text.DocumentException;
//import com.lowagie.text.Font;
//import com.lowagie.text.FontFactory;
//import com.lowagie.text.PageSize;
//import com.lowagie.text.Paragraph;
//import com.lowagie.text.Section;
//import com.lowagie.text.pdf.PdfWriter;

public class GerarCapaProcessoPDF {
	
	/**
	 * Método responsável em gerar a capa do processo em pdf.
	 * 
	 *@param processo
	 *
	 *@param outStream é um OutPutStream que recebera o resultado da operação
	 *
	 */
	public static byte[] gerarCapaProcessoPDF(ProcessoDt processo)throws Exception {
		Document document = new Document(PageSize.A4, 50, 50, 50, 50);
		PdfWriter writer = null;
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		byte[] temp= null;
		try{
			writer = PdfWriter.getInstance(document,outputStream);
			document.open();
			
			Paragraph numProcesso = new Paragraph("Processo Nº: "+Funcoes.formataNumeroProcesso(processo.getProcessoNumeroCompleto()), FontFactory.getFont(FontFactory.HELVETICA,18, Font.BOLDITALIC, BaseColor.BLUE));
			
			numProcesso.setAlignment(Paragraph.ALIGN_CENTER);
			Chapter principal = new Chapter(numProcesso, 1);
			principal.setNumberDepth(0);
			
			Paragraph dadosProcesso = new Paragraph("Dados Processo",FontFactory.getFont(FontFactory.HELVETICA, 16,Font.BOLD, BaseColor.BLACK));
			Section section = principal.addSection(dadosProcesso);
			Paragraph juizo = new Paragraph("Juízo...............................: "+processo.getServentia());
			section.add(juizo);
			Paragraph Prioridade = new Paragraph("Prioridade.......................: "+processo.getProcessoPrioridade());
			section.add(Prioridade);
			Paragraph tipoAcao = new Paragraph("Tipo Ação.......................: "+processo.getProcessoTipo());
			section.add(tipoAcao);
			Paragraph segredoJusticao;
			if( processo.getSegredoJustica().equalsIgnoreCase("false"))
				 segredoJusticao = new Paragraph("Segredo de Justiça.........: NÃO");
			else
				 segredoJusticao = new Paragraph("Segredo de Justiça.........: SIM");
			section.add(segredoJusticao);
			Paragraph faseProcessual = new Paragraph("Fase Processual.............: "+processo.getProcessoFase());
			section.add(faseProcessual);
			Paragraph dataRecebimento = new Paragraph("Data recebimento...........: "+processo.getDataRecebimento());
			section.add(dataRecebimento);
			Paragraph valor = new Paragraph("Valor da Causa...............: R$ "+processo.getValor());
			section.add(valor);
			Paragraph ultimoEvento = new Paragraph("Classificador...................: "+processo.getClassificador());
			section.add(ultimoEvento);
			
			Paragraph partes = new Paragraph("Partes Processos:",FontFactory.getFont(FontFactory.HELVETICA, 16,Font.BOLD, BaseColor.BLACK));
			Section section1 = principal.addSection(partes);
			
			//Iterator iterator = processo.getPartesProcessoPromoventes().iterator();
			if (processo.getListaPolosAtivos() != null) {
				Iterator listaPromoventes = processo.getListaPolosAtivos().iterator();
				Paragraph promovente = new Paragraph("Polo Ativo");
				section1.add(promovente);
				while (listaPromoventes.hasNext()) {
					ProcessoParteDt parte = (ProcessoParteDt) listaPromoventes.next();
					Paragraph someSectionText = new Paragraph(parte.getNome().toUpperCase());
					section1.add(someSectionText);
				}
			}

			Paragraph espaco = new Paragraph(" ");
			section1.add(espaco);

			if (processo.getListaPolosPassivos() != null){
				Iterator listaPromovidos = processo.getListaPolosPassivos().iterator();
				Paragraph promovidas = new Paragraph("Polo Passivo");
				section1.add(promovidas);
				while (listaPromovidos.hasNext()) {
					ProcessoParteDt parte = (ProcessoParteDt) listaPromovidos.next();
					Paragraph someSectionText = new Paragraph(parte.getNome().toUpperCase());
					section1.add(someSectionText);
				}
			}
			document.add(principal);
			document.close();
			writer.close();
			temp = outputStream.toByteArray();
			outputStream.close();
			
			
		} catch(DocumentException e) {
			try{if (document!=null) document.close(); } catch(Exception ex ) {};
			try{if (writer!=null) writer.close(); } catch(Exception ex ) {};
			try{if (outputStream!=null) outputStream.close(); } catch(Exception ex ) {};
			throw e;
		}
		return temp;
	}
}
