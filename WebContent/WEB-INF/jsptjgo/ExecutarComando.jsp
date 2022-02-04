<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page  import="br.gov.go.tj.utils.Configuracao"%>

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Executar Comando  </title>
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
		
		
		<script type='text/javascript' src='./js/checks.js'></script>
		<script type='text/javascript' src='./js/Funcoes.js'></script>
		<script type='text/javascript' src='./js/jquery.js'></script>
		<script type='text/javascript' src='./js/DivFlutuante.js'></script>
		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script> 
		<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js" ></script>
		<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>
		
		<script language="javascript" type="text/javascript">

		function limparTela(){
			document.getElementById('comando').value = "";
			document.getElementById('comandoAntigo').value = "";
			document.getElementById('motivo').value = "";
			document.getElementById('resposta').value = "";
			
			document.getElementById('divExecutar').style.display = "block"; 
			document.getElementById('divCancelar').style.display = "none"; 
		}

		</script>
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; Executar Comando</h2></div>
		<form action="Administrador" method="post" name="Formulario" id="Formulario">
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
			<div id="divPortaBotoes" class="divPortaBotoes">
				<input id="imgNovo" alt="Novo"  class="imgNovo" title="Novo - Limpa os campos da tela" name="imaNovo" type="image" src="./imagens/imgNovo.png"  onclick="javascript:limparTela();return false;" />
			</div/><br />
			<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao"  > 
					<legend class="formEdicaoLegenda">Executar Comando</legend>
					<label class="formEdicaoLabel" for="comando">Comando</label><br> 
					<textarea class="formEdicaoInput" cols="80" rows="15" name="comando" id="comando" type="text" size="200" ><%=request.getAttribute("comando")%></textarea><br />
					
					<label class="formEdicaoLabel" for="motivo">Motivo</label><br> 
					<textarea class="formEdicaoInput" cols="80" rows="5" name="motivo" id="motivo" type="text" size="200" ><%=request.getAttribute("motivo")%></textarea><br />
					
					<input id="comandoAntigo" name="comandoAntigo" type="hidden" value="<%=request.getAttribute("comandoAntigo")%>" />
					
					<br>
					<label class="formEdicaoLabel" for="">&nbsp;</label><br>
					<input id="PassoEditar" name="PassoEditar" type="hidden" value="<%=request.getAttribute("PassoEditar")%>" /> 
					<% String comandoSqlAntigo = String.valueOf(request.getAttribute("comandoAntigo"));
						if(comandoSqlAntigo != null && (comandoSqlAntigo.equals("null") || comandoSqlAntigo.equals(""))){%>
						<div id="divExecutar" style="display:block;">
							<input id="executar" alt="Executar" title="Executar" value="Executar" name="executar" class="imgNovo" type="button" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Curinga8)%>');AlterarValue('PassoEditar','2');submit();" />
						</div>
						<div id="divCancelar" style="display:none;">
							<input id="cancelar" alt="Cancelar/Limpar" title="Cancelar/Limpar" value="Cancelar/Limpar" name="Cancelar/Limpar" class="imgNovo" type="button" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Curinga8)%>');AlterarValue('PassoEditar','1');submit();" />
							<input id="comitar" alt="Comitar" title="Commit" value="Commit" name="comitar" class="imgNovo" type="button" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Curinga8)%>');AlterarValue('PassoEditar','4');submit();" />
						</div>						
					<% } else {%>
						<div id="divExecutar" style="display:none;">
							<input id="executar" alt="Executar" title="Executar" value="Executar" name="executar" class="imgNovo" type="button" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Curinga8)%>');AlterarValue('PassoEditar','2');submit();" />
						</div>
						<div id="divCancelar" style="display:block;">
							<input id="cancelar" alt="Cancelar/Limpar" title="Cancelar/Limpar" value="Cancelar/Limpar" name="Cancelar/Limpar" class="imgNovo" type="button" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Curinga8)%>');AlterarValue('PassoEditar','1');submit();" />
							<input id="comitar" alt="Comitar" title="Commit" value="Commit" name="comitar" class="imgNovo" type="button" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Curinga8)%>');AlterarValue('PassoEditar','4');submit();" />
						</div>	
					<%} %>
					<br/>
					
					<br>
					<label class="formEdicaoLabel" for="resposta">Resposta</label><br> 
					<textarea class="formEdicaoInput" cols="80" rows="2" name="resposta" id="resposta" type="text" size="200" ><%=request.getAttribute("resposta")%></textarea><br />
					
					
				</fieldset>
				<%@ include file="Padroes/Mensagens.jspf"%>
			</div>
			<br />	
			<div id="divPortaBotoes" class="divPortaBotoes">
				<fieldset class="formEdicao"  > 
					<legend class="formEdicaoLegenda">Testar envio de e-mail</legend>
				    <label class="formEdicaoLabel" for="Email">E-Mail</label><br> 
				    <input class="formEdicaoInput" name="Email" id="Email"  type="text" size="45" maxlength="50" value="" onkeyup=" autoTab(this,50)"/>
	          		<input id="formLocalizarBotao" class="formLocalizarBotao" type="submit" name="TestarEmail" value="Testar E-mail"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Curinga8)%>')" />
          		</fieldset>
			</div/>
		</form>
	</div>
</body>
</html>
