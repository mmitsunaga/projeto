<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"  pageEncoding="ISO-8859-1"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.projudi.dt.CertidaoGuiaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.UsuarioServentiaDt"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>

<jsp:useBean id="certidaoGuiaDt" scope="session" class= "br.gov.go.tj.projudi.dt.CertidaoGuiaDt"/>
<jsp:useBean id="modeloDt" scope="session" class= "br.gov.go.tj.projudi.dt.ModeloDt"/>

<html>
<head>	
	<title>Certid&atilde;o de Prática Forense</title>
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
	<script type='text/javascript' src='./js/Digitacao/DigitarSoNumero.js'></script>
	<script type='text/javascript' src='./js/Digitacao/AutoTab.js'></script>
	<script type='text/javascript' src='./js/jscalendar/dhtmlgoodies_calendar.js?random=20060118'></script>
	<script type="text/javascript" src="./js/Digitacao/DigitarData.js"></script>
</head>
	
	<body>
		<div id="divCorpo" class="divCorpo">
		  	<div class="area"><h2>&raquo; <%=request.getAttribute("TituloPagina")%> </h2></div>
		  	
			<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
				
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input id="PaginaAnterior" name="PaginaAnterior" type="hidden" value="<%=request.getAttribute("PaginaAnterior")%>">
				<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
				
				<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao"> 
			    	<legend class="formEdicaoLegenda">Dados da Certidão</legend>
			    	
			    	<div class="col45 clear">
						<label class="formEdicaoLabel" for="numeroGuia">Digite o Número da Guia:</label><br>
						
						<input name="numeroGuia" id="numeroGuia" class="formEdicaoInputSomenteLeitura" readonly type="text" size="60" maxlength="255" value="<%=certidaoGuiaDt.getNumeroGuia()%>"><br>
						
						<% if(certidaoGuiaDt.getGuiaEmissaoDt() != null && !certidaoGuiaDt.getGuiaEmissaoDt().isGuiaPaga()) { %>	             	
		             		<font color="#FF0000"><b>&nbsp;Esta guia não está paga.</b></font>
		             		<br />
		             	<% } %>
		             	<br />
		             	
		             	<input name="imgLimpar" type="submit" value="Nova Busca" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Novo%>');" />
					</div>
				</fieldset>

<!-- *********************** DADOS DA CERTIDÃO ******************************************************************************************************************************************************************************************	 -->
                	
				<!-- IF - Para Mostrar ou Não as Informações do Requerente. -->
               	<% if(!certidaoGuiaDt.getOabNumero().isEmpty() && certidaoGuiaDt.getOabNumero() != null) { %>
                	
	               	<fieldset id="VisualizaDados" class="VisualizaDados">
	               		<legend>Dados da Certidão</legend>
						
						<table border=0 width="100%" style="font-size:12px">
							<tr>
								<td width="16%" valign=top>Período</td>
								<td valign=top>
									<b>
										<%=(certidaoGuiaDt.getMesInicial()!=null)?certidaoGuiaDt.getMesInicial():""%>/<%=(certidaoGuiaDt.getAnoInicial()!=null)?certidaoGuiaDt.getAnoInicial():""%>  a  
										<%=(certidaoGuiaDt.getMesFinal()!=null)?certidaoGuiaDt.getMesFinal():""%>/<%=(certidaoGuiaDt.getAnoFinal()!=null)?certidaoGuiaDt.getAnoFinal():""%>
									</b>
								</td>
							</tr>						
							<tr>
								<td width="16%" valign=top>Tipo</td>
								<td valign=top>
									<b>
										<%=(certidaoGuiaDt.getTipo()!=null  && certidaoGuiaDt.getTipo().trim().equalsIgnoreCase("quantitativa"))?"Quantitativa":"Normal (Detalhada)"%>
									</b>
								</td>
							</tr>
						</table>
					</fieldset>				
				
<!-- *********************** INFORMAÇÕES DO REQUERENTE ******************************************************************************************************************************************************************************************	 -->
               
	               	<fieldset id="VisualizaDados" class="VisualizaDados">
	               		<legend>Dados do Advogado</legend>
	               		
						<table border=0 width="100%" style="font-size:12px">
						
						<tr><td width="16%" valign=top>Nome</td>
						<td width="34%" valign=top><b><%=(certidaoGuiaDt.getNome()!=null)?certidaoGuiaDt.getNome():""%></b></td>
						
						<td width="16%" valign=top>Número OAB/Matrícula</td>
						<td width="34%" valign=top><b><%=(certidaoGuiaDt.getOabNumero()!=null)?certidaoGuiaDt.getOabNumero():""%></b></td></tr>
						
						<tr><td width="16%" valign=top>CPF</td>
						<td width="34%" valign=top><b><%=(certidaoGuiaDt.getCpf()!=null)?certidaoGuiaDt.getCpf():""%></b></td>
						
						<td width="16%" valign=top>OAB Complemento</td>
						<td width="34%" valign=top><b><%=(certidaoGuiaDt.getOabComplemento()!=null)?certidaoGuiaDt.getOabComplemento():""%></b></td></tr>
						
						<tr><td width="16%" valign=top>Sexo</td>
						<td width="34%" valign=top><b><%=(certidaoGuiaDt.getSexo()!=null  && certidaoGuiaDt.getSexo().trim().equalsIgnoreCase("F"))?"Feminino":"Masculino"%></b></td>
						
						<td width="16%" valign=top>OAB UF</td>
						<td width="34%" valign=top><b><%=(certidaoGuiaDt.getOabUf()!=null)?certidaoGuiaDt.getOabUf():""%></b></td></tr>
						
						<tr><td colspan=4>&nbsp;</td></tr>
						
						<tr><td width="16%" valign=top>RG</td>
						<td width="34%" valign=top><b><%=(certidaoGuiaDt.getRg()!=null)?certidaoGuiaDt.getRg():""%></b></td>
						
						<td width="16%" valign=top>Estado Civil</td>
						<td width="34%" valign=top><b><%=(certidaoGuiaDt.getEstadoCivil()!=null)?certidaoGuiaDt.getEstadoCivil():""%></b></td></tr>
						
						<tr><td width="16%" valign=top>Órgão Expedidor</td>
						<td width="34%" valign=top><b><%=(certidaoGuiaDt.getRgOrgaoExpedidor()!=null)?certidaoGuiaDt.getRgOrgaoExpedidor():""%></b></td>
						
						<td width="16%" valign=top>Naturalidade</td>
						<td width="34%" valign=top><b><%=(certidaoGuiaDt.getNaturalidade()!=null)?certidaoGuiaDt.getNaturalidade():""%></b></td></tr>
						
						</table>
					</fieldset>
					
					<% if(certidaoGuiaDt.getGuiaEmissaoDt() != null && certidaoGuiaDt.getGuiaEmissaoDt().isGuiaPaga()) { %>	             	
		            <!-- *********************** INFORMAÇÕES DO REQUERENTE ******************************************************************************************************************************************************************************************	 -->
               
	               	<fieldset id="VisualizaDados" class="VisualizaDados">
	               		<legend>Escrivão(ã) Responsável</legend>
	               		<label class="formEdicaoLabel" for="Id_UsuarioServentia">*Usuário
		    				<input class="FormEdicaoimgLocalizar" id="imaLocalizarId_UsuarioServentia" name="imaLocalizarId_UsuarioServentia" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(UsuarioServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" >  
		    			</label><br> 
		    			<input class="formEdicaoInputSomenteLeitura" readonly name="UsuarioServentia" id="UsuarioServentia" type="text" size="80" maxlength="100" value="<%=certidaoGuiaDt.getNomeUsuarioEscrivaoResponsavel()%>"/><br />
	               		 
					</fieldset>
					<!-- *********************** TEXTO DA CERTIDÃO ******************************************************************************************************************************************************************************************	 -->

					<fieldset> 
	               		<legend>Certidão de Prática Forense</legend>
						
						<br><input name="imgPrevia" type="submit" value="Gerar Pré-Visualização da Certidão" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga6%>')"><br><br>							
														
					</fieldset>
					<% } %>
				<% } %>
								
				<% if (certidaoGuiaDt.getTexto() != null && !certidaoGuiaDt.getTexto().equals("")) {%>
					<fieldset class="formEdicao">
						<legend class="formEdicaoLegenda">Pré-Visualização da Certidão</legend>
						<div id="divTextoEditor" class="divTextoEditor">
							<%=certidaoGuiaDt.getTexto() %>
						</div><br><br>
						<div>	
							<input name="imgImprimir" type="submit" value="Salvar Certidão / Gerar PDF" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Salvar%>')">
						</div>
					</fieldset>
				<%} %>
				<%@ include file="Padroes/Mensagens.jspf"%>
				</div>			
			</form>
		</div>
	</body>
</html>