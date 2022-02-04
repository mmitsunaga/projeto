<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>
<%@page import="br.gov.go.tj.projudi.dt.ArquivoPalavraDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaDt"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>

<html>
   <head>
		<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<link rel="shortcut icon" href="./imagens/favicon.png">
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./css/Paginacao.css');
			@import url('js/jscalendar/dhtmlgoodies_calendar.css?random=20051112');
		</style>
		
		<title> |<%=request.getAttribute("tempPrograma")%>| Validar Documento  </title>
		<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
			
		<script type='text/javascript' src='./js/Funcoes.js'></script>
		<script type='text/javascript' src='./js/jquery.js'></script>
	   	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script> 

		<style type="text/css"> #bkg_projudi{ display:none } </style>
	</head>
	
	<body class="fundo">
	<%@ include file="/CabecalhoPublico.html" %>   
	  <div  id="divCorpo" class="divCorpo">
		<div class="area"><h2>&raquo; Validar Documento</h2></div>
		<div id="divLocalizar" class="divLocalizar" >
			<form method="post" action="PendenciaPublica" id="Formulario" target=""> 
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input id="PaginaAnterior" name="PaginaAnterior" type="hidden" value="<%=request.getAttribute("PaginaAnterior")%>" />
			    <fieldset id="formLocalizar" class="formLocalizar"> 
			    	<legend id="formLocalizarLegenda" class="formLocalizarLegenda">Validar Documento</legend>
			       	<label class="formLocalizarLabel">Código de Validação</label><br>
			       	<input class="formLocalizarInput" name="codPublicacao" id="codPublicacao" type="text" value="" maxlength="17" title="Código do documento para validação" />
			   	   	<br />
			   	   	<button type="submit" name="operacao" value="VerificarPublicacao" onclick="$('#Formulario').attr('target','_blank'); AlterarValue('PaginaAtual', '1');">
						Validar
					</button>
			   	</fieldset>
			   	<%@ include file="Padroes/reCaptcha.jspf" %>
			</form>
		</div>
		<%@ include file="Padroes/Mensagens.jspf"%>
	  </div>
	</body>
</html>