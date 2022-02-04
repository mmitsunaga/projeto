<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.projudi.dt.AgenciaDt"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>


<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> Dupla Verificação </title>
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />	
	<script type="text/javascript" src="./js/jquery.js"> </script>
	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  	

</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>Informe o código de dupla verificação</h2></div>
		<form action="Usuario" method="post" name="Formulario" id="Formulario">
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="0" />			
			<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao"  > 
					<legend class="formEdicaoLegenda">Digite o Código de Validação </legend>
										
					<label class="formEdicaoLabel" for="codigo">*Código</label><br> <input class="formEdicaoInput" name="codigo" id="codigo"  type="text" size="30" maxlength="30" value="" /><br />
					 										
					<input type="submit" name="Enviar">
				</fieldset>
			</div>

		</form>
	</div>	
</body>
</html>
