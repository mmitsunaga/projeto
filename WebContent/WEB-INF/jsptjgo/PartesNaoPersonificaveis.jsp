<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Busca de Partes sem Personalidade Jurídica ou Civil </title>
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
	<link href="./css/Paginacao.css"  type="text/css"  rel="stylesheet" />
	
	
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type='text/javascript'>
		function seleciona(nome, endereco){		 
			AlterarValue('Nome', nome); 
			AlterarValue('Id_Endereco', endereco); 	
			FormSubmit('Formulario');		
		}
	</script>
</head>

<body>

	<div id="divCorpo" class="divCorpo">
		<div class="area"><h2>&raquo; |<%=request.getAttribute("tempPrograma")%>| Busca Partes sem Personalidade Jurídica ou Civil </h2></div>
		
		<div id="divLocalizar" class="divLocalizar">
		
			<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario"> 
		   		<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>"/>
		   		<input id="Nome" name="Nome" type="hidden" value="" />
<!-- 		   		<input id="Id_Endereco" name="Id_Endereco" type="hidden" value="" /> -->
		
			   	<div id="divTabela" class="divTabela"> 
			    	<table id="Tabela" class="Tabela">
			        	<thead>
			            	<tr>
			                	<th></th>
			                  	<th>Selecione uma Parte</th>	                	
			               	</tr>
			           	</thead>
			           	<tbody id="tabListaPartes">
						<%@ include file="PartesNaoPersonificaveis.inclusao"%> 
			           	</tbody>
			       	</table>
				</div>
			</form>
		</div>
	</div>
</body>
</html>