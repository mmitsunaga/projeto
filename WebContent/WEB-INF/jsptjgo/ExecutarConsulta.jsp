<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<jsp:useBean id="ResultaConsultadt" class= "br.gov.go.tj.projudi.dt.ResultadoConsultaDt" scope="session"/>
<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Executar Consulta  </title>
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
			}
			function redimensionar(){
				var objIframe = window.parent.document.getElementById('Principal');
				if (objIframe != null) {				
					var divTextoLog = document.getElementById('divTextoLog');				
					objIframe.height = divTextoLog.clientHeight + 80;					
				}			
			}	
		</script>		
</head>

<body onload="redimensionar();">
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; Executar Consulta</h2></div>
		<form action="Administrador" method="post" name="Formulario" id="Formulario">
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
			<div id="divPortaBotoes" class="divPortaBotoes">
				<input id="imgNovo" alt="Novo"  class="imgNovo" title="Novo - Limpa os campos da tela" name="imaNovo" type="image" src="./imagens/imgNovo.png"  onclick="javascript:limparTela();return false;" />
			</div>
			<br />
			<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao"  > 
					<legend class="formEdicaoLegenda">Executar Consulta</legend>
					<label class="formEdicaoLabel" for="comando">Consulta</label><br> 
					<textarea class="formEdicaoInput" cols="80" rows="15" name="comando" id="comando" type="text" size="200" maxlength="65535" onkeyup="autoTab(this,65535)"><%=request.getAttribute("comando")%></textarea><br />
										
					<br>
					<label class="formEdicaoLabel" for="">&nbsp;</label><br>
					<input id="PassoEditar" name="PassoEditar" type="hidden" value="<%=request.getAttribute("PassoEditar")%>" />
					<div id="divExecutar" >
						<input id="executar" alt="Executar" title="Executar" value="Executar" name="executar" class="imgNovo" type="button" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Curinga9)%>');AlterarValue('PassoEditar','2');submit();" />
					</div>				
					
					<fieldset>	
						<legend>Resultado da consulta </legend>														
						<div id="divTextoLog">
							<%=ResultaConsultadt.getTextoResultadoConsulta() %>							
						</div>
					</fieldset>	
					
				</fieldset>
				<%@ include file="Padroes/Mensagens.jspf"%>
			</div>
		</form>
	</div>
</body>
</html>
