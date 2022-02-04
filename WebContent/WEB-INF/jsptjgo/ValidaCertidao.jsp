<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" 
   "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>
<%@page import="br.gov.go.tj.projudi.dt.ArquivoPalavraDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaDt"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>

<html>
   <head>
		<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./css/Paginacao.css');
			@import url('js/jscalendar/dhtmlgoodies_calendar.css?random=20051112');
		</style>
		
		<title> |<%=request.getAttribute("tempPrograma")%>| Validar Certidão  </title>
		<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
			
		<script type='text/javascript' src='./js/Funcoes.js'></script>
		<script type='text/javascript' src='./js/jquery.js'></script>
	   	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script> 
	   	<script type="text/javascript" src="./js/tabelas.js"></script>
		<script type="text/javascript" src="./js/tabelaArquivos.js"></script>
		<%@ include file="js/buscarArquivos.js"%>
		
		
		<script type="text/javascript" src="./js/jscalendar/dhtmlgoodies_calendar.js"></script>
		<script type='text/javascript' src='./js/jquery.maskedinput.js'></script>
		<%@ include file="./js/Paginacao.js"%>
		<style type="text/css"> #bkg_projudi{ display:none } </style>
	</head>
	
	<body>
	 <%@ include file="/Cabecalho.html" %>  
	  <div  id="divCorpo" class="divCorpo">
		      
		<div class="area"><h2>&raquo; Validar Certidão</h2></div>
		
		<div id="divLocalizar" class="divLocalizar" >
			<form method="post" action="ValidaCertidao" id="Formulario" target=""> 
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input id="PaginaAnterior" name="PaginaAnterior" type="hidden" value="<%=request.getAttribute("PaginaAnterior")%>" />
			    
			    <fieldset id="formLocalizar" class="formLocalizar"> 
			    	<legend id="formLocalizarLegenda" class="formLocalizarLegenda">Validar Certidão</legend>
			       	<label class="formLocalizarLabel">Código de Validação</label><br>
			       	<input class="formLocalizarInput" name="codCertidao" id="codCertidao" type="text" value="" maxlength="12" title="Código da certidão para validação" />
			   	   	<br />
			   	   	<button type="submit" name="operacao" value="VerificarPublicacao" onclick="$('#Formulario').attr('target','_blank'); AlterarValue('PaginaAtual', '3');">
						<!-- <img src="imagens/22x22/ico_sucesso.png" alt="Verificar Certidão" /> -->
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