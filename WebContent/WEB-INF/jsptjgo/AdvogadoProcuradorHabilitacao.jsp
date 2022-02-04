<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.projudi.dt.UsuarioDt"%>
<%@page  import="br.gov.go.tj.projudi.dt.AdvogadoDt"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.CidadeDt"%>
<%@page import="br.gov.go.tj.projudi.dt.RgOrgaoExpedidorDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaDt"%>

<jsp:useBean id="Advogadodt" scope="session" class= "br.gov.go.tj.projudi.dt.UsuarioDt"/>
<jsp:useBean id="UsuarioServentiaOabdt" scope="session" class= "br.gov.go.tj.projudi.dt.UsuarioServentiaOabDt"/>

<html>

<head>
	<title>Advogados</title>

      <meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./css/Paginacao.css');
			@import url('js/jscalendar/dhtmlgoodies_calendar.css?random=20051112');
		</style>
      
      
      <script type='text/javascript' src='./js/Funcoes.js'></script>
      <script type='text/javascript' src='./js/jquery.js'></script>
      <script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
	  <script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js"></script>
	  <script type="text/javascript" src="./js/Digitacao/AutoTab.js"></script>
	  <script type="text/javascript" src="./js/jscalendar/dhtmlgoodies_calendar.js?random=20060118"></script>
	  <script type="text/javascript" src="./js/Digitacao/DigitarData.js"></script>		
</head>

<body>
	<div id="divCorpo" class="divCorpo">
  		<div class="area"><h2>&raquo; Habiltação de Advogado/Def. Público/Procurador </h2></div>
		
		<form action="Advogado" method="post" name="Formulario" id="Formulario" <% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>OnSubmit="JavaScript:return VerificarCampos()"<%}%>>
		
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>"/>
			<input name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>">
			<input id="Curinga" name="Curinga" type="hidden" value="<%=request.getAttribute("Curinga")%>">
			<input type="hidden" id="<%=request.getAttribute("tempBuscaId_Usuario").toString()%>" name="<%=request.getAttribute("tempBuscaId_Usuario").toString()%>">
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>"/>
			<input id="tempFluxo1" name="tempFluxo1" type="hidden" value="<%=request.getAttribute("tempFluxo1")%>">
			  
			<div id="divPortaBotoes" class="divPortaBotoes">
				<input id="imgAtualizar" class="imgAtualizar" title="Voltar para tela principal" name="imaAtualizar" type="image"  src="./imagens/imgVoltar.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Curinga9)%>')">
				<input id="imgNovo"  class="imgNovo" title="Novo - Limpa os campos da tela" name="imaNovo" type="image" src="./imagens/imgNovo.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Curinga9)%>'); AlterarValue('tempFluxo1','LA1'); " >
			</div>
			  
		    <div id="divEditar" class="divEditar">
		  		<fieldset class="formEdicao"> 
			    	<legend class="formEdicaoLegenda">Habiltação de Advogado/Def. Público/Procurador</legend>

					<div class="col15">
					<label class="formEdicaoLabel" for="Cpf">*CPF</label><br> 
					<input class="formEdicaoInput" name="Cpf" id="Cpf"  type="text" size="20" maxlength="20" value="<%=Advogadodt.getCpf()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,20)"/>
			    	</div>
			    	<br>
			   			    	
			    	<!-- OAB's do Advogado -->
			    	<fieldset>
			    	<legend class="formEdicaoLegenda">Dados OAB</legend>
			    		<div class="clear"></div>
			    		<div class="col15">
			    		<label class="formEdicaoLabel" for="OabNumero">*Número</label><br> <input class="formEdicaoInput" name="OabNumero" id="OabNumero"  type="text" size="12" maxlength="11" value="<%=Advogadodt.getOabNumero()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,11)">
			   			</div>
			   			<div class="col15">
			   			<label class="formEdicaoLabel" for="OabComplemento">*Complemento</label><br> 
			   			<select name="OabComplemento" id="OabComplemento" class="formEdicaoCombo">
				        		<option>N</option>
					       		<option>A</option>
					        	<option>B</option>
			   		        	<option>S</option>
				    	    	<option selected><%=Advogadodt.getOabComplemento()%></option/>
				    	</select>
				    	</div>
				    	<div class="col45">
				    	<label class="formEdicaoLabel" for="EstadoOab">*UF</label><br>    
				    	<input  class="formEdicaoInput" name="EstadoOab" id="EstadoOab" type="text" size="2" maxlength="2" value="<%=Advogadodt.getOabEstado()%>">
			    		</div>
			    	</fieldset>
				</fieldset>
				
				<%if (!request.getAttribute("Curinga").toString().equalsIgnoreCase("LA2")){%>
					<div id="divConsultar" class="divBotoesCentralizados">
						<button name="imgConsultar" value="Consultar" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Curinga9)%>');  AlterarValue('tempFluxo1','LA2'); AlterarValue('Curinga','vazio');">
		               		<img src="./imagens/imgLocalizar.png" alt="Consultar Advogado/Procurador">
		               		Consultar Advogado/Procurador
	            		</button> <br /> <br />
      				</div>
      			<%}%>
				
			    <%if (request.getAttribute("Curinga").toString().equalsIgnoreCase("LA2")){%>
					<div id="divConfirmarSalvar" class="ConfirmarSalvar">
						<button name="imgConfirmarSalvar" value="Confirmar Salvar" onclick="Ocultar('divConfirmarSalvar'); AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Curinga9)%>');  AlterarValue('tempFluxo1','LA3'); AlterarValue('Curinga','vazio');">
		               		<img src="./imagens/imgSalvar.png" alt="Confirmar Salvar">
		               		<% if (request.getAttribute("MensagemConfirmacao") != null && !request.getAttribute("MensagemConfirmacao").equals("")) { %>
		               			<%=request.getAttribute("MensagemConfirmacao")%>
		               		<% } else { %>
		               			Confirmar Salvar
		               		<% }%> 
	            		</button> <br /> <br />
						<% if (request.getAttribute("Mensagem") != null && !request.getAttribute("Mensagem").equals("")) { %>
			        		<div class="divMensagemsalvar"><%=request.getAttribute("Mensagem")%></div>
			           	<% } else { %>
			           		<div class="divMensagemsalvar">Clique para confirmar os dados </div>
			           	<% }%> 
      			</div>
			   <%}%>
			   </div>
  		</form>
  		<%@ include file="Padroes/Mensagens.jspf" %>
	</div>
</body>
</html>
