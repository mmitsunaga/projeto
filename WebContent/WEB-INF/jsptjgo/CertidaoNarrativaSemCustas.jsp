<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.projudi.dt.CertidaoGuiaDt"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>

<jsp:useBean id="certidaoGuiaDt" scope="session" class= "br.gov.go.tj.projudi.dt.CertidaoGuiaDt"/>
<jsp:useBean id="modeloDt" scope="session" class= "br.gov.go.tj.projudi.dt.ModeloDt"/>
<jsp:useBean id="processoDt" scope="session" class="br.gov.go.tj.projudi.dt.ProcessoDt"/>

<html>
<head>	
	<title>Certid&atilde;o Narrativa Sem Custas</title>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
	<style type="text/css">
		@import url('./css/Principal.css');
		@import url('./css/menusimples.css');
		@import url('./js/jscalendar/dhtmlgoodies_calendar.css?random=20051112');
	</style>
    <script type='text/javascript' src='./js/Digitacao/DigitarSoNumero.js'></script>
    
	
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type='text/javascript' src='./js/jquery.js'></script>
	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
	<script type='text/javascript' src='./js/Digitacao/AutoTab.js'></script>
	<script type='text/javascript' src='./js/ckeditor/ckeditor.js?v=24092018'></script>
	<script type='text/javascript' src='./js/DivFlutuante.js'></script>
	<script type='text/javascript' src='./js/Mensagens.js'></script>
	<script type='text/javascript' src='./js/jscalendar/dhtmlgoodies_calendar.js?random=20060118'></script>
	<script type="text/javascript" src="./js/Digitacao/DigitarData.js"></script>
	
	<script type="text/javascript">
		$(document).ready(function() {
			CKEDITOR.on('instanceReady', function(e){
				var cabecalhoTela = 75;
				var objeto = window.parent.document.getElementById('Principal');															
				var altura = objeto.contentDocument.body.offsetHeight;
				objeto.height = altura + cabecalhoTela;
			});
		});
	</script>
		
</head>	
<body>
	<div id="divCorpo" class="divCorpo">
	  	<div class="area"><h2>&raquo; <%=request.getAttribute("TituloPagina")%> </h2></div>
	  	
		<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
		
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input id="PaginaAnterior" name="PaginaAnterior" type="hidden" value="<%=request.getAttribute("PaginaAnterior")%>">
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
			
			<div id="divEditar" class="divEditar">
				<div id="divPortaBotoes" class="divPortaBotoes">
					<input id="imgNovo" alt="Novo"  class="imgNovo" title="Novo - Limpa os campos da tela" name="imaNovo" type="image" src="./imagens/imgNovo.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Novo)%>')" />
					<input id="imgLocalizar" alt="Localizar" class="imgLocalizar" title="Localizar - Localiza um registro no banco" name="imaLocalizar" type="image"  src="./imagens/imgLocalizar.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Localizar)%>')" /> 
					<input id="imgImprimir" alt="Imprimir"  class="imgImprimir" title="Imprimir - Gerar relatorio em pdf" name="imaImprimir" type="image" src="./imagens/imgImprimir.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Imprimir)%>')" />
				</div>
				
				<fieldset id="VisualizaDados" class="VisualizaDados">
                	<legend>Processo</legend>
                	
                		<br>
	                	<table border=0 width="81%" align="center" >                	
							<tr>
								<td width="16%" align=left>
									Número
								</td>
								<td width="34%" align=left>						
		                			<a href="BuscaProcesso?Id_Processo=<%=processoDt.getId_Processo()%>&PassoBusca=2"><%=processoDt.getProcessoNumero()%></a>		                		                	
	                			</td>
	                			<td width="16%" valign=top>&nbsp;</td>						
								<td width="34%" valign=top>&nbsp;</td>	                	
	                		</tr>
	                	</table>
                </fieldset>

<!-- *********************** INFORMAÇÕES DO REQUERENTE ******************************************************************************************************************************************************************************************	 -->
               	<fieldset id="VisualizaDados" class="VisualizaDados">
               		<legend>Informações do Requerente</legend>
               		
               		<br><table border=0 width="81%" align="center">
					<tr><td><label class="formEdicaoLabel" for="Nome">*Nome</label></td>
					<td><input class="formEdicaoInput" name="Nome" id="Nome" type="text" size="60" maxlength="255" value="<%=(certidaoGuiaDt.getNome()!=null)?certidaoGuiaDt.getNome():""%>"></td></tr>
					
					<tr><td width="10%" valign=top><label class="formEdicaoLabel" for="Cpf">*CPF/CNPJ</label></td>
					<td width="90%" valign=top><input class="formEdicaoInput" name="Cpf" id="Cpf" type="text" size="20" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,20)" value="<%=(certidaoGuiaDt.getCpf()!=null)?certidaoGuiaDt.getCpf():""%>"></td></tr>
					</table>
               		
				</fieldset>
		
<!-- *********************** INFORMAÇÕES DO PROCESSO ******************************************************************************************************************************************************************************************	 -->
               	<fieldset id="VisualizaDados" class="VisualizaDados">
               		<legend>Informações do Processo</legend>
					
						<br>
						<table border=0 width="81%" align="center">
						<tr><td width="16%" valign=top>Protocolo</td>
						<td width="34%" valign=top><b><%=(certidaoGuiaDt.getProtocolo()!=null)?certidaoGuiaDt.getProtocolo():""%></b></td>
						
						<td width="16%" valign=top>Juízo</td>
						<td width="34%" valign=top><b><%=(certidaoGuiaDt.getJuizo()!=null)?certidaoGuiaDt.getJuizo():""%></b></td></tr>
						
						<tr><td width="16%" valign=top>Natureza</td>
						<td width="34%" valign=top><b><%=(certidaoGuiaDt.getNatureza()!=null)?certidaoGuiaDt.getNatureza():""%></b></td>
						
						<td width="16%" valign=top>Valor da Ação</td>
						<td width="34%" valign=top><b><%=(certidaoGuiaDt.getValorAcao()!=null)?certidaoGuiaDt.getValorAcao():""%></b></td></tr>
						
						<tr><td width="16%" valign=top>Requerente</td>
						<td width="34%" valign=top><b><%=(certidaoGuiaDt.getRequerenteProcesso()!=null)?certidaoGuiaDt.getRequerenteProcesso():""%></b></td>
						
						<td width="16%" valign=top>Adv. Requerente</td>
						<td width="34%" valign=top><b><%=(certidaoGuiaDt.getAdvogadoRequerente()!=null)?certidaoGuiaDt.getAdvogadoRequerente():""%></b></td></tr>
						
						<tr><td width="16%" valign=top>Requerido</td>
						<td width="34%" valign=top><b><%=(certidaoGuiaDt.getRequerido()!=null)?certidaoGuiaDt.getRequerido():""%></b></td></tr>
						</table>
				</fieldset>
			
<!-- *********************** MOVIMENTAÇÕES DO PROCESSO ******************************************************************************************************************************************************************************************	 -->
				<fieldset> 
               		<legend>Movimentações do Processo</legend><br>
               		
               		<div id="Editor" class="Editor" align="justify">                									
						<%=(certidaoGuiaDt.getMovimentacoesProcesso() == null)?"":certidaoGuiaDt.getMovimentacoesProcesso()%>
					</div>
				</fieldset>
		
<!-- *********************** TEXTO DA CERTIDÃO ******************************************************************************************************************************************************************************************	 -->
				<fieldset> 
               		<legend>Texto da Certidão</legend><br>
               		
               		<div id="Editor" class="Editor">                									
						<textarea class="ckeditor" name="textoCertidao" id="textoCertidao" rows="15" cols="80">
							<%=(certidaoGuiaDt.getTextoCertidao() == null)?"Certifica mais que, ":certidaoGuiaDt.getTextoCertidao()%>
						</textarea>								
					</div>
					
					<br><input name="imgPrevia" type="submit" value="Gerar Pré-Visualização da Certidão" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga6%>')"><br><br>							
													
				</fieldset>
					
				<% if (certidaoGuiaDt.getTexto() != null && !certidaoGuiaDt.getTexto().equals("")) {%>
					<fieldset class="formEdicao">
						<legend class="formEdicaoLegenda">Pré-Visualização da Certidão</legend>
						<div>
							<%=certidaoGuiaDt.getTexto()%>
						</div>
						<div>	
							<input name="imgImprimir" type="submit" value="Salvar Certidão / Gerar PDF" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Salvar%>')">
						</div>
					</fieldset>
				<% } %>
		
				<%@ include file="Padroes/Mensagens.jspf"%>
			</div>			
		</form>
	</div>
</body>
</html>