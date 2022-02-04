package br.gov.tj.teste;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

import br.gov.go.tj.projudi.dt.ArquivoDt;
import br.gov.go.tj.projudi.ne.ArquivoNe;
import br.gov.go.tj.projudi.util.GerarCabecalhoProcessoPDF;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;
import br.gov.go.tj.utils.pdf.ConverterHtmlPdf;
import br.gov.go.tj.utils.pdf.GerarPDF;

public class TesteIBGE {

	public void baixarArquivos() {
		String[] linhaArquivo = null;
		Path pathSaida = null;
		long tempoInicial = 0;
		int segundos = 0;
		int minutos = 0;
		int contador = 0;
		byte[] arquivoPDF = null;

//		Path pathEntrada = Paths.get("D:\\ibge_saida_ibge_id_arquivos_peti_2020.txt");
		Path pathEntrada = Paths.get("D:\\ibge_saida_ibge_id_arquivos_sent_2020.txt");
		try (Scanner scanner = new Scanner(pathEntrada)) {
			while (scanner.hasNextLine()) {
				tempoInicial = System.currentTimeMillis();
				try {
					linhaArquivo = scanner.nextLine().split(";");
					pathSaida = Paths.get("D:\\IBGE\\" + linhaArquivo[1] + "_Sentenca_" + linhaArquivo[2] + ".pdf");
					if (Files.notExists(pathSaida) && linhaArquivo.length > 3 && linhaArquivo[2] != null && linhaArquivo[2].length() > 0) {
						if (!linhaArquivo[2].equalsIgnoreCase("")) {
							arquivoPDF = gerarPdfPublicacao(linhaArquivo[2], linhaArquivo[1]);
							Files.write(pathSaida, arquivoPDF);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					arquivoPDF = null;
					segundos = (int) ((System.currentTimeMillis() - tempoInicial) / 1000);
					minutos = segundos / 60;
					segundos = segundos - (minutos * 60);
					System.out.println(++contador + " (" + (minutos < 10 ? "0" + minutos : minutos) + ":" + (segundos < 10 ? "0" + segundos : segundos) + ")");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
    public byte[] gerarPdfPublicacao(String stIdArquivo, String numeroProcesso) throws Exception {
        ArquivoNe arquivoNe = new ArquivoNe();
        ArquivoDt arquivoPublicacao = arquivoNe.consultarId(stIdArquivo, false);
        byte[] byTemp = null;
        boolean boHtml = false;
        String pathImage = ProjudiPropriedades.getInstance().getCaminhoAplicacao() + "imagens" + File.separator + "TesteChave.gif";
        
        byte[] out = null;
        String stNome = arquivoPublicacao.getNomeArquivo().toLowerCase();
        
        if (stNome.indexOf(".mp3") > 0) throw new MensagemException("Impossível converter um arquivo mp3 em pdf.");

        if (stNome.indexOf(".html") > 0) boHtml = true;

        out = arquivoPublicacao.getConteudo();               
               
        try {
        	if (boHtml) out = ConverterHtmlPdf.converteHtmlPDF(out, false);
		} catch (Exception e) {
			try {
	        	if (boHtml) out = ConverterHtmlPdf.converteHtmlPDFAlternativo(out);
			} catch (Exception e2) {
				out = GerarPDF.gerarMensagemPDF(arquivoPublicacao.getNomeArquivoFormatado(), "");
			}
		}
        
        String enderecoConferencia = ProjudiPropriedades.getInstance().getEnderecoValidacaoPublicacao();
        String textoPrimeiraLinhaPDF = "Tribunal de Justiça do Estado de Goiás";
        String textoSegundaLinhaPDF = "Documento extraído com autorização do PROAD número 202008000234343 e despacho número 340814315519";
        String textoTerceiraLinhaPDF = "que pode ser validado no endereço: " + enderecoConferencia;
        String textoQuartaLinhaPDF = "Assinado por " + arquivoPublicacao.getUsuarioAssinadorFormatado();
        
        byTemp = escreverTextoPDF(out, pathImage, textoPrimeiraLinhaPDF, textoSegundaLinhaPDF, textoTerceiraLinhaPDF, textoQuartaLinhaPDF, numeroProcesso);

        return byTemp;
    }
    
    public byte[] escreverTextoPDF(byte[] input, String pathImage ,String textoPrimeiraLinha, String textoSegundaLinha, String textoTerceiraLinha, String textoQuartaLinha,  String numeroProcesso)throws Exception {	
		ByteArrayOutputStream TextoPDF = new ByteArrayOutputStream();
		PdfReader reader = null;
		PdfStamper pdfStamper = null;
		byte[] temp= null;
		try{
			input = GerarCabecalhoProcessoPDF.redimensionar(input);
			reader = new PdfReader(input);
			if (reader.getNumberOfPages() == 0) return input;
			
			pdfStamper = new PdfStamper(reader,TextoPDF);
						
            PdfContentByte over;
            BaseFont bf = BaseFont.createFont(BaseFont.COURIER_BOLD, BaseFont.WINANSI, BaseFont.EMBEDDED);
            
			int totalPaginas = reader.getNumberOfPages() + 1;
			for (int i = 1; i < totalPaginas; i++) {				
                over = pdfStamper.getOverContent(i);
                over.beginText();
                
                imprimaCabecalhoLateralDireitaNaPagina(over, numeroProcesso, i, reader, null, 0, null, bf, null);
                
                GerarCabecalhoProcessoPDF.imprimaRodapeNaPagina(over, pathImage, bf, textoPrimeiraLinha, textoSegundaLinha, textoTerceiraLinha, textoQuartaLinha, reader.getPageSize(i).getBottom());
                
                over.endText();
			}
			pdfStamper.close();		
			TextoPDF.close();
			reader.close();
			temp = TextoPDF.toByteArray();
		} catch(IOException e1) {
			throw e1;
		} catch(Exception e) {
			try{if (pdfStamper!=null) pdfStamper.close(); } catch(Exception ex ) {};
			try{if (TextoPDF!=null) TextoPDF.close(); } catch(Exception ex ) {};
			try{if (reader!=null) reader.close(); } catch(Exception ex ) {};
			try{if (TextoPDF!=null) TextoPDF.close(); } catch(Exception ex ) {};
			throw e;
		}
		return temp;
	}
    
    public void imprimaCabecalhoLateralDireitaNaPagina(PdfContentByte over, String processoNumero, int numeroPagina, PdfReader reader, String contMovimentacao, int contArquivo, ArquivoDt arqDoc, BaseFont bf, String numeroProcesso) throws Exception
	{
		 over.setFontAndSize(bf, 9);
         over.setColorFill(BaseColor.RED);
         
         if (processoNumero != null)
         {
        	 over.showTextAligned(PdfContentByte.ALIGN_LEFT,"Processo: "+processoNumero, 20, (reader.getPageSize(numeroPagina).getHeight() + reader.getPageSize(numeroPagina).getBottom() - 20), 0);
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
         over.showTextAligned(PdfContentByte.ALIGN_LEFT, "Data: "+df.format(new Date()),  (reader.getPageSize(numeroPagina).getWidth() + reader.getPageSize(numeroPagina).getBottom() - 50), (reader.getPageSize(numeroPagina).getHeight() + reader.getPageSize(numeroPagina).getBottom() - 40), -90);
			
//         if (processo != null)
//         {
//	         over.setTextMatrix(30, 30);                
//	         over.showTextAligned(Element.ALIGN_LEFT, processo.getServentia().toUpperCase(),  (reader.getPageSize(numeroPagina).getWidth() - 40), (reader.getPageSize(numeroPagina).getHeight() + reader.getPageSize(numeroPagina).getBottom() - 40), -90);
//         }
	}
	
}

/* Processos de divórcio com sentença num determinado período */
//SELECT 
//c.comarca AS COMARCA, 
//s.serv AS SERVENTIA, pr.id_proc,
//LPAD(pr.proc_numero, 7, '0') || '.' || LPAD (pr.digito_verificador, 2, '0') || '.' || LPAD (pr.ano, 4, '0') || '.' || '8' || '.' || '09' || '.' || LPAD (pr.forum_codigo, 4, '0') AS PROCESSO,
//pt.proc_tipo AS CLASSE,
//pr.data_recebimento AS DATA_RECEBIMENTO,
//	pr.data_arquivamento AS DATA_ARQUIVAMENTO,
//pp.nome AS AUTOR, pp.sexo as SEXO_AUTOR, 
//NVL(pp.cnpj,pp.cpf) AS CNPJ_AUTOR,
//pp.data_nascimento as DATA_NASC_AUTOR,
//cid.cidade as CIDADE_NASCIMENTO_AUTOR,
//est.uf as UF_NASCIMENTO_AUTOR,
//cid1.cidade AS CIDADE_DOMICILIO_AUTOR,
//est1.uf as UF_DOMICILIO_AUTOR,
//pp1.nome AS REU, pp.sexo as SEXO_REU,
//NVL(pp1.cnpj, pp1.cpf) AS CNPJ_REU,
//pp1.data_nascimento as DATA_NASC_REU,
//cid2.cidade AS CIDADE_NASCIMENTO_REU,
//est2.uf as UF_NASCIMENTO_REU,
//cid3.cidade AS CIDADE_DOMICILIO_REU,
//est3.uf as UF_DOMICILIO_REU,
//tab.data_sentenca
//
//    FROM (
//      SELECT s.id_comarca, p.id_serv, p.id_proc, p.id_proc_tipo,
//      		(SELECT MIN(pp.id_proc_parte) 
//            	from projudi.proc_parte pp
//                where pp.id_proc = p.id_proc 
//                	and pp.id_proc_parte_tipo = 2 --promovente
//                	and pp.data_baixa is null --parte não baixada
//                ) as id_proc_promovente,
//            (SELECT MIN(pp.id_proc_parte)
//            	from projudi.proc_parte pp
//                where pp.id_proc = p.id_proc 
//                	and pp.id_proc_parte_tipo = 3 --promovido
//                    and pp.data_baixa is null --parte não baixada
//            	) as id_proc_promovido,
//              
//                (select data_realizacao from movi mv where data_realizacao >= To_Date('01/01/2020 00:00:00','dd/mm/yyyy hh24:mi:ss')
//                      and  data_realizacao <= To_Date('31/12/2020 00:00:00','dd/mm/yyyy hh24:mi:ss') and  
//                      mv.id_proc = p.id_proc and 
//                      mv.id_movi_tipo in(select id_movi_tipo from movi_tipo where id_cnj_movi in(
//                            select id_cnj_movi from cnj_movi where id_cnj_movi_pai in (193,385,210,241,12433,12326,212,973,12450,12661,12670,12321,12678,218,228,456,12709))) and rownum=1 ) as data_sentenca
//              
//      	FROM projudi.proc p
//        INNER JOIN projudi.serv s ON p.id_serv = s.id_serv
//        INNER JOIN projudi.comarca c on s.id_comarca = c.id_comarca
//        WHERE p.id_proc_tipo in (179,180,233,1400,1401,1416) and 
//        
//         p.id_proc in (select id_proc from movi where data_realizacao >= To_Date('01/01/2020 00:00:00','dd/mm/yyyy hh24:mi:ss')
//         and  data_realizacao <= To_Date('31/12/2020 00:00:00','dd/mm/yyyy hh24:mi:ss') and  id_movi_tipo in(
//                    select id_movi_tipo from movi_tipo where id_cnj_movi in(
//                            select id_cnj_movi from cnj_movi where id_cnj_movi_pai in (193,385,210,241,12433,12326,212,973,12450,12661,12670,12321,12678,218,228,456,12709))))
//        
//    )  tab 
//	 JOIN projudi.comarca c ON tab.id_comarca = c.id_comarca
//INNER JOIN projudi.serv s ON tab.id_serv = s.id_serv
//INNER JOIN projudi.proc pr ON tab.id_proc = pr.id_proc
//INNER JOIN projudi.proc_tipo pt ON tab.id_proc_tipo = pt.id_proc_tipo
//INNER JOIN projudi.proc_parte pp ON pp.id_proc_parte = tab.id_proc_promovente 
//INNER JOIN projudi.proc_parte pp1 ON pp1.id_proc_parte = tab.id_proc_promovido
//left join projudi.cidade cid on pp.id_naturalidade = cid.id_cidade 
//left join projudi.cidade cid1 on pp1.id_naturalidade = cid1.id_cidade
//left join projudi.estado est on cid.id_estado = est.id_estado
//left join projudi.estado est1 on cid1.id_estado = est1.id_estado
//left join projudi.endereco end1 on pp.id_endereco = end1.id_endereco
//left join projudi.endereco end2 on pp1.id_endereco = end2.id_endereco
//left join projudi.bairro br on end1.id_bairro = br.id_bairro
//left join projudi.bairro br1 on end2.id_bairro = br1.id_bairro
//left join projudi.cidade cid2 on br.id_cidade = cid2.id_cidade
//left join projudi.cidade cid3 on  br1.id_cidade = cid3.id_cidade
//left join projudi.estado est2 on cid2.id_estado = est2.id_estado
//left join projudi.estado est3 on cid3.id_estado = est3.id_estado
//    
//ORDER BY COMARCA, SERVENTIA, PROCESSO
