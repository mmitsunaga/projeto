<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">
<%@page import="br.gov.go.tj.projudi.dt.ProcessoCertidaoSegundoGrauPositivaNegativaDt"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"  pageEncoding="ISO-8859-1"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.ModeloDt"%>
<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteDt"%>
<%@page import="br.gov.go.tj.projudi.dt.CertidaoSegundoGrauNegativaPositivaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.CidadeDt"%>
<%@page import="br.gov.go.tj.projudi.dt.EstadoCivilDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProfissaoDt"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>


<jsp:useBean id="certidaoSegundoGrauNegativaPositivaDt" scope="session" class= "br.gov.go.tj.projudi.dt.CertidaoSegundoGrauNegativaPositivaDt"/>
<jsp:useBean id="modeloDt" scope="session" class= "br.gov.go.tj.projudi.dt.ModeloDt"/>

<html>
	<head>
	
	<title>Certid&atilde;o Negativa/Positiva do Segundo Grau</title>
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
      	<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>
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
			
			
			<form action="CertidaoSegundoGrauNegativaPositiva" method="post" name="Formulario" id="Formulario">
			
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
					
					<label class="formEdicaoLabel" for="Nome">*Nome: </label><br> 
	    			<input class="formEdicaoInput" name="Nome" id="Nome" type="text" size="60" maxlength="255" value="<%=certidaoSegundoGrauNegativaPositivaDt.getNome()%>" onkeyup=" autoTab(this,255)"/>
	    			<br />
					<label class="formEdicaoLabel" for="Nome">Pessoa: </label><br> 
					<input type="radio" name="PessoaTipo" value="Física" <%=certidaoSegundoGrauNegativaPositivaDt.getPessoaTipo().equals("Física") || certidaoSegundoGrauNegativaPositivaDt.getPessoaTipo().isEmpty() ? "checked" : ""%> />Física
					<input type="radio" name="PessoaTipo" value="Jurídica" <%=certidaoSegundoGrauNegativaPositivaDt.getPessoaTipo().equals("Jurídica") ? "checked" : ""%>/>Jurídica
					<br />
										
					<label class="formEdicaoLabel" for="Cpf">CPF/CNPJ: </label><br> 
	    			<input class="formEdicaoInput" name="Cpf" id="Cpf"  type="text" size="30" maxlength="18" value="<%=certidaoSegundoGrauNegativaPositivaDt.getCpfCnpj()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,60)" title="Digite o CPF/CNPJ do requerente."/>
	    			<br />
	    			
	    				    			
					<label class="formEdicaoLabel" for="NomeMae">Nome da M&atilde;e: </label><br> 
	    			<input class="formEdicaoInput" name="NomeMae" id="NomeMae" type="text" size="60" maxlength="255" value="<%=certidaoSegundoGrauNegativaPositivaDt.getNomeMae()%>" onkeyup=" autoTab(this,255)"/>
	    			<br />
	    			
	    			<label class="formEdicaoLabel" for="DataNascimento">*Data de Nascimento: </label><br> 
					<input class="formEdicaoInput" name="DataNascimento" id="DataNascimento"  type="text" size="10" maxlength="10" value="<%=certidaoSegundoGrauNegativaPositivaDt.getDataNascimento()%>" onkeyup="mascara_data(this)" onblur="verifica_data(this)"> 
					<img id="calendarioDataNascimento" src="./imagens/dlcalendar_2.gif" height="13" width="13" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].DataNascimento,'dd/mm/yyyy',this)"/>
	    			<br />
	    			
	    			<label class="formEdicaoLabel" for="Area">Área: </label><br> 
					<input type="radio" name="Area" value="Civel" <%=certidaoSegundoGrauNegativaPositivaDt.getArea().equals("Civel") ? "checked" : ""%> />Cível
					<input type="radio" name="Area" value="Criminal" <%=certidaoSegundoGrauNegativaPositivaDt.getArea().equals("Criminal") ? "checked" : ""%>/>Criminal
					<input type="radio" name="Area" value="Todos" <%=certidaoSegundoGrauNegativaPositivaDt.getArea().equals("Todos") || certidaoSegundoGrauNegativaPositivaDt.getArea().isEmpty() ? "checked" : ""%>/>Todos
					<br />
							
	    			
		
					<div id="divBotoesCentralizados" class="divBotoesCentralizados">
					<input name="imgSubmeter" type="submit" value="Gerar Certid&atilde;o" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Localizar%>')">
					 <input name="imgLimpar" type="submit" value="Limpar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Novo%>');"> 
					</div>
				</fieldset>
				
			<fieldset class="formEdicao">
		<legend class ="formEdicaoLegenda">Lista de Processos</legend> 	

			<% 			
						List processos = null; 
						if(certidaoSegundoGrauNegativaPositivaDt !=null) 
						processos = certidaoSegundoGrauNegativaPositivaDt.getListaProcesso(); 
					%>
									<%
						if (processos != null && processos.size() > 0) { 
 							%> 
				<p><strong>Atenção:</strong> Certifique-se que a lista de processos está correta, remova processos se necessário.</p>
				<table class="Tabela" id="TabelaArquivos">
					<thead>
						<tr>
							<th></th>
							<th width="10%">N&uacute;mero Processo</th>
							<th width="5%">Sistema</th>
							<th width="25%">Nome</th>
							<th width="20%">CPF/CNPJ</th>
							<th width="10%">Data de Nascimento</th>
							<th width="25%">Nome da Mãe</th>
							<th width="5%">Remover</th>
						</tr>
					</thead>
					<%
						for (int i = 0; i < processos.size(); i++) {
											ProcessoCertidaoSegundoGrauPositivaNegativaDt ProcessoCNPDt = (ProcessoCertidaoSegundoGrauPositivaNegativaDt) processos.get(i); 
											String numeroProcesso = "";
											if(ProcessoCNPDt.getSistema().equals("SSG")) {
												numeroProcesso = ((String[])ProcessoCNPDt.getProcessoNumeroDigito().split("\\("))[0];
											} else {
												 numeroProcesso = ProcessoCNPDt.getProcessoNumeroCompleto();
											}
		
		%> 
					<tbody>
						<tr class="primeiraLinha">
						<%
								if (!ProcessoCNPDt.getSistema().equals("SSG") ) {
							%>
							<td align="center"><%=i + 1%></td>
							<td align="center"><a
								href="BuscaProcesso?Id_Processo=<%=ProcessoCNPDt.getId_Processo()%>"><%=ProcessoCNPDt.getProcessoNumeroDigito()%></a>
							</td>
							<%
								} else {
							%>
									<td align="center"><%=i + 1%></td>
									<td align="center"><b><%=numeroProcesso%></b>
									</td>
							<%	
								}
							%>
								<td align="center">
							 				<%=ProcessoCNPDt.getSistema()%>
							
							</td>
							<td align="center">
							 				<%=ProcessoCNPDt.getRequerente()%>
							
							</td>
							<td align="center">
							
							 <% 
							 	String cpf = ProcessoCNPDt.getPromovidoCpfCnpj(); 
						 	%>
							 <%=cpf == null ? "-" : cpf %> 
							</td>
							<td align="center">
							<%String dataNascimento = ProcessoCNPDt.getPromovidoDataNascimento();%>
							<%=dataNascimento == null ? "-": Funcoes.FormatarData(dataNascimento) %>
							</td>
							<td align="center">
							<%String nomeMae = ProcessoCNPDt.getPromovidoNomeMae();%>
							<%=nomeMae == null ? "-": nomeMae %>
							</td>
							<%
							if (processos.size() >= 1) { 
							%> 
							<td align="center"><a 	href="<%=request.getAttribute("tempRetorno")%>?PaginaAtual=<%=Configuracao.LocalizarAutoPai%>&amp;posicaoLista=<%=i%>">
							<img name="btnRetirar<%=i%>" id="btnRetirar<%=i%>" title="Retirar esse processo" src="./imagens/imgExcluirPequena.png" /> </a></td>
							<%
								} 
						%> 
						</tr>
					</tbody>
					<%
 						} 
						} else { 
				%> 
					<tbody>
						<tr>
							<td><em>Não foram encontrados processos.</em></td>
						</tr>
					</tbody>
					<%
				} 
				%> 
					</table>
				</fieldset>
				<%@ include file="Padroes/Mensagens.jspf"%>
				<% if (certidaoSegundoGrauNegativaPositivaDt.getTexto() != null && !certidaoSegundoGrauNegativaPositivaDt.getTexto().equals("")) {%>
				<fieldset class="formEdicao">
					<legend class="formEdicaoLegenda">Certidão</legend>
						<div id="divTextoEditor" class="divTextoEditor">
							<%=certidaoSegundoGrauNegativaPositivaDt.getTexto() %>
						</div>
				</fieldset>
				<%} %>						
				</div>			
			</form>
		</div>
	</body>
</html>