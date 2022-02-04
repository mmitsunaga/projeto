<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE" />
<title>|<%=request.getAttribute("tempPrograma")%>| Correção
	Mandado Central
</title>
<link href="./css/Principal.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="./js/jquery.js">
	
</script>
<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>
<script type='text/javascript' src='./js/Funcoes.js'></script>
<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js"></script>
<script type="text/javascript" src="./js/Digitacao/AutoTab.js"></script>

<%@page import="br.gov.go.tj.utils.Configuracao"%>

</head>

<body>
	<div id="divCorpo" class="divCorpo">
		<div class="area">
			<h2>&raquo; Correção Mandado Central</h2>
		</div>

		<form action="Administrador" method="post" name="Formulario"
			id="Formulario">

			<input id="PaginaAtual" name="PaginaAtual" type="hidden"
				value="<%=request.getAttribute("PaginaAtual")%>" /> <input
				name=__Pedido__ type="hidden"
				value="<%=request.getAttribute("__Pedido__")%>"> <input
				name="TituloPagina" type="hidden"
				value="<%=request.getAttribute("tempTituloPagina")%>" /> <input
				type="hidden" id="Fluxo" name="Fluxo"
				value="<%=request.getAttribute("Fluxo")%>" />

			<div id="divPortaBotoes" class="divPortaBotoes">
			
			<input id="imgSalvar" class="imgSalvar" title="Salvar alteração mandado"
					name="imgSalvar" type="image" src="./imagens/imgSalvar.png"
					onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Curinga6)%>');
					         AlterarValue('Fluxo','correcaoMandadoCentral')">						 		

			</div>
			<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao">
					<legend class="formEdicaoLegenda">Verifica se o processo
						do mandado tem saldo ou locomoção reservada</legend>

					<label class="formEdicaoLabel" for="QuantidadeMandado">*Número Mandado</label><br> 
					<input class="formEdicaoInput"
						name="idMandado" id="idMandado" type="text" size="11"
						maxlength="11" value=""
						onkeypress="return DigitarSoNumero(this, event)" /> <br> <br>
					<label class="formEdicaoLabel" for="QuantidadeMandado">*Quantidade Locomoções</label><br>
					<input class="formEdicaoInput"
						name="numeroLocomocao" id="numeroLocomocao" type="text" size="11"
						maxlength="11" value=""
						onkeypress="return DigitarSoNumero(this, event)" /> <br> <br>
						
			</div>

		</form>

		<%@ include file="Padroes/Mensagens.jspf"%>


	</div>

</body>

</html>
