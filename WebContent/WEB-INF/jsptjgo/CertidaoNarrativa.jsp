<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.projudi.dt.CertidaoGuiaDt"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>

<jsp:useBean id="certidaoGuiaDt" scope="session" class= "br.gov.go.tj.projudi.dt.CertidaoGuiaDt"/>
<jsp:useBean id="modeloDt" scope="session" class= "br.gov.go.tj.projudi.dt.ModeloDt"/>

<html>
<head>	
	<title>Certid&atilde;o Narrativa</title>
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
</head>
	
	<body>
		<% if (request.getSession().getAttribute("TipoConsulta") != null && request.getSession().getAttribute("TipoConsulta").equals("Publica")) { %>
		<%@ include file="/CabecalhoPublico.html" %>
		<% } %>
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
					
				<fieldset class="formEdicao"> 
			    	<legend class="formEdicaoLegenda">Dados da Certid&atilde;o</legend>
			    	
			    	<div class="col45 clear">
			    	
						<label class="formEdicaoLabel" for="numeroGuia">Número da Guia:</label><br>
						
						<input name="numeroGuia" id="numeroGuia" 
						<% if( request.getAttribute("numeroGuiaOk") != null && !request.getAttribute("numeroGuiaOk").toString().equalsIgnoreCase("ok") ) { %> class="formEdicaoInput" <% }
						else { %> class="formEdicaoInputSomenteLeitura" readonly <% } %>						
						type="text" size="60" maxlength="255" value="<%=certidaoGuiaDt.getNumeroGuia()%>"><br>
						
						<%
		               	// Verifica se o número da guia foi encontrado. Se não foi, apresenta uma mensagem explicativa.
		               	if(request.getAttribute("numeroGuiaOk") != null && request.getAttribute("numeroGuiaOk").toString().equalsIgnoreCase("inexistente")) {
		             	%>	             	
		             		<font color="#FF0000"><b>&nbsp;Guia não encontrada.</b></font>
		             	<% } %>
		             	
		             	<%
		               	// Verifica se o número da guia foi paga.
		               	if(request.getAttribute("numeroGuiaOk") != null && request.getAttribute("numeroGuiaOk").toString().equalsIgnoreCase("nao_paga")) {
		             	%>	             	
		             		<font color="#FF0000"><b>&nbsp;Esta guia não está paga.</b></font>
		             	<% } %>
		             	
						<br><br>
						<%
						if( request.getAttribute("numeroGuiaOk") != null && !request.getAttribute("numeroGuiaOk").toString().equalsIgnoreCase("ok") ) {
						%>
							<input name="imgLocalizar" type="submit" value="Localizar Guia" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Localizar%>')" >
						<% } %>
						
						<input name="imgLimpar" type="submit"
						 
						<% if( request.getAttribute("numeroGuiaOk") != null && !request.getAttribute("numeroGuiaOk").toString().equalsIgnoreCase("ok") ) {
						%>
							value="Limpar"
						<%	} else { %>
							value="Nova Busca"
						<% } %>
						
						onclick="AlterarValue('PaginaAtual','<%=Configuracao.Novo%>');">
						 
					</div>
				</fieldset>
				
<!-- *********************** INFORMAÇÕES DO REQUERENTE ******************************************************************************************************************************************************************************************	 -->
               	<%
               	//IF - Para Mostrar ou Não as Informações do Requerente.
               	if(!certidaoGuiaDt.getNome().isEmpty() && certidaoGuiaDt.getNome() != null) {
             	%>
                	<fieldset id="VisualizaDados" class="VisualizaDados">
                		<legend>Informações do Requerente</legend>
                		
						<br>
						<table border=0 width="81%" align="center">
						
							<tr><td width="16%" valign=top>Nome</td>
							<td width="84%" valign=top><b><%=(certidaoGuiaDt.getNome()!=null)?certidaoGuiaDt.getNome():""%></b></td></tr>
							
							<tr><td width="16%" valign=top>CPF/CNPJ</td>
							<td width="84%" valign=top><b><%=(certidaoGuiaDt.getCpf()!=null)?certidaoGuiaDt.getCpf():""%></b></td></tr>
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
						<% if (!(request.getSession().getAttribute("TipoConsulta") != null && request.getSession().getAttribute("TipoConsulta").equals("Publica"))) { %>
		                		<legend>Texto da Certidão</legend><br>
		                		
		                		<div id="Editor" class="Editor">                									
									<textarea class="ckeditor" name="textoCertidao" id="textoCertidao" rows="15" cols="80">
										<%=(certidaoGuiaDt.getTextoCertidao() == null)?"Certifica mais que, ":certidaoGuiaDt.getTextoCertidao()%>
									</textarea>								
								</div>
						<% } %>
						<br><input align="center" name="imgPrevia" type="submit" value="Gerar Pré-Visualização da Certidão" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga6%>')"><br><br>
					</fieldset>
				<% } %>
								
				<% if (certidaoGuiaDt.getTexto() != null && !certidaoGuiaDt.getTexto().equals("")) {%>
				<fieldset class="formEdicao">
					<legend class="formEdicaoLegenda">Pré-Visualização da Certidão</legend>
					<div>
						<%=certidaoGuiaDt.getTexto() %>
					</div><br><br>
					<div>	
						<input name="imgImprimir" type="submit" value="Salvar Certidão / Gerar PDF" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Salvar%>')">
					</div>
				</fieldset>
				<%} %>
				
				</div>			
			<%@ include file="Padroes/reCaptcha.jspf" %>			
			</form>
				<%@ include file="Padroes/Mensagens.jspf"%>
		</div>
		<div style="height: 50px;"></div>
	</body>
</html>