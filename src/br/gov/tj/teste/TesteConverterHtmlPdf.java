package br.gov.tj.teste;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.gov.go.tj.utils.pdf.ConverterHtmlPdf;

public class TesteConverterHtmlPdf {
	
	private static String html = null;
		
	@Before
	public void tearUp(){
		html = 
			"<p style=\"text-align:center\"><img src=\"https://projudi.tjgo.jus.br/imagens/logoEstadoGoias02.jpg\" /></p>" + 
			"<p style=\"text-align:center\"><img src=\"https://projudi.tjgo.jus.br/imagens/brasaoGoias.png\" /></p>" +
			"<p style=\"text-align:center\"><img src=\"https://projudi.dsi.teste.tjgo.jus.br/imagens/brasaoGoias.png\" /></p>" +
			"<p style=\"text-align:center\"><img src=\"http://www.tj.go.gov.br/projudi/imagens/brasaoPetroBranco.jpg\" /></p>" +
			"<p style=\"text-align:center\"><img src=\"https://homolog.tjgo.jus.br/imagens/logoEstadoGoias02.jpg\" /></p>" +
			"<p style=\"text-align:center\"><img src=\"https://projudi.dsi.teste.tjgo.jus.br/imagens/logoEstadoGoias02.jpg\" /></p>" +
			"<p style=\"text-align:center\"><img src=\"https://localhost//imagens/logoEstadoGoias02.jpg\" /></p>" +
			"<p style=\"text-align:center\"><img src=\"https://pjd.tjgo.jus.br/imagens/logoEstadoGoias02.jpg\" /></p>" +				
			"<p>Stack Overflow em Português é um site de perguntas e respostas para programadores profissionais e entusiastas. Registre-se, leva apenas um minuto.</p>" + 
			"<table border=\"1\" cellpadding=\"1\" cellspacing=\"1\" style=\"width:500px\">" +
			"<tbody>" +
			"<tr><td>Coluna1</td><td>Coluna 2</td></tr>" +
			"<tr><td>Maria</td><td>Jose</td></tr>" +
			"<tr><td>Pedro</td><td>Laura</td></tr>"	 +	
			"</tbody>" +
			"</table>" +
			"<p>&nbsp;</p>" +
			"<p style=\"background: 123-transparent;\">background-color:trasparent 1o caso</p>" + 
			"<p style=\"background: 123-transparent\"\">background-color:trasparent 2o caso</p>" + 
			"<p style=\"background: 123-transparent-xzy;\">background-color:trasparent 3o caso</p>" + 
			"<p style=\"background-color:rgba(1,2,3);\">background-color:rgb 4o caso</p>" + 
			"<p style=\"background-color: 123-transparent-zyx\">background-color -> 123-transparent-zyx</p>" + 
			"<p style=\"border-color:rgb-xyz;\">background-color -> 123-transparent-zyx</p>" + 
			"<hr style=\"line-height: 10px; line-height: 20px\"/>" + 
			"<table border=\"1\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n</table>";
		System.out.println(html);
	}
	
	@Test
	public void quandoLimparHtmlDeveRetornarTagsHtmlHeadBody() throws Exception {
		byte[] output = ConverterHtmlPdf.limparHtml(html.getBytes(), false);
		String texto = new String(output);
		Assert.assertTrue(texto.contains("<html>"));
		Assert.assertTrue(texto.contains("</html>"));
		Assert.assertTrue(texto.contains("<head>"));
		Assert.assertTrue(texto.contains("</head>"));
		Assert.assertTrue(texto.contains("<body>"));
		Assert.assertTrue(texto.contains("</body>"));
	}
		
	@Test
	public void quandoExistirTagTableDeveRetornarStyleComMargemZerada() throws Exception {
		byte[] output = ConverterHtmlPdf.limparHtml(html.getBytes(), false);
		String texto = new String(output);
		Assert.assertTrue(texto.indexOf("<style>@page {margin: 0px 0px 0px 0px; }</style>") > -1);
	}
	
	@Test
	public void quandoExistirTagTableComBorderEWidthCemPorcentoDeveExcluirTagTable() throws Exception {
		// <table border=\"1\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n</table> -> Branco		
		byte[] output = ConverterHtmlPdf.limparHtml(html.getBytes(), false);
		String texto = new String(output);
		Assert.assertTrue(texto.indexOf("<table border=\"1\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n</table>") == -1);
	}
	
	@Test
	public void quandoExistirTabelaComTamanhoEmPixelDeveTrocarPorPorcentagem() throws Exception{
		String input = "<table border=\"1\" cellpadding=\"1\" cellspacing=\"1\" style=\"width:100px; border:1px;\">";
		byte[] output = ConverterHtmlPdf.limparHtml(input.getBytes(), false);
		String texto = new String(output);
		Assert.assertTrue(texto.indexOf("style=\"width:100%;\"") > -1);
	}
	
	@Test
	public void quandoHouverAtributoLineHeightEmStyleDeveRetornarStyleEmBranco() throws Exception {		
		byte[] output = ConverterHtmlPdf.limparHtml(html.getBytes(), false);
		String texto = new String(output);
		Assert.assertTrue(texto.indexOf("line-height:") == -1);
	}
	
	@Test
	public void quandoExistirImgBrasaoTJDeveRetornarSrcBase64() throws Exception {
		byte[] output = ConverterHtmlPdf.limparHtml(html.getBytes(), false);
		String texto = new String(output);
		Assert.assertTrue(texto.indexOf("https://projudi.tjgo.jus.br/imagens/logoEstadoGoias02.jpg") == -1);
		Assert.assertTrue(texto.indexOf("https://projudi.tjgo.jus.br/imagens/brasaoGoias.png") == -1);
		Assert.assertTrue(texto.indexOf("https://projudi.dsi.teste.tjgo.jus.br/imagens/brasaoGoias.png") == -1);
		Assert.assertTrue(texto.indexOf("http://www.tj.go.gov.br/projudi/imagens/brasaoPetroBranco.jpg") == -1);
		Assert.assertTrue(texto.indexOf("https://homolog.tjgo.jus.br/imagens/logoEstadoGoias02.jpg") == -1);
		Assert.assertTrue(texto.indexOf("https://projudi.dsi.teste.tjgo.jus.br/imagens/logoEstadoGoias02.jpg") == -1);
		Assert.assertTrue(texto.indexOf("https://localhost//imagens/logoEstadoGoias02.jpg") == -1);
		Assert.assertTrue(texto.indexOf("https://pjd.tjgo.jus.br/imagens/logoEstadoGoias02.jpg") == -1);
	}
	
}
