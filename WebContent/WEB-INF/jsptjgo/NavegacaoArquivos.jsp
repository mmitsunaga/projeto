<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<jsp:useBean id="processoDt" scope="session" class="br.gov.go.tj.projudi.dt.ProcessoDt"/>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Navegação de Arquivos do Processo</title>


	<%@ include file="js/buscarArquivos.js"%>
	<script type='text/javascript' src='./js/Funcoes.js'></script>		
	<script type='text/javascript' src='./js/jquery.js'></script>
	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>   
	
	<style type="text/css">
		@import url('./css/Principal.css');
	</style>	
	
	<script type="text/javascript">
		var dadosIndice; 
		var DadosNavegacao=null;
		$(document).ready(function() {			
			gerarMenuNavegacao();
// 			$("#indice").show();
// 			$(".menuNavegacao").width("20%");
// 			var altura = document.body.clientHeight;
// 			var objIframe = document.getElementById('iframe');			
// 			objIframe.height = altura;
// 			var objNavegacao = document.getElementById('menuNavegacao');			
// 			objNavegacao.height = altura;

		});
	</script>
		
</head>
<body>	
	<div id="divCorpo" class="divNavegacao" >
		<h2> Movimentações Processo <span class="bold"> <%=processoDt.getProcessoNumeroCompleto()%></span></h2>
		<div class='verticalText'>Índice</div>
		<div id="menuNavegacao" class="menuNavegacao"> </div>	
		<div id='iframe' class='portaIframe'> <iframe width="100%" height="100%" frameborder="0" scrolling="auto" id="iframe"  id="arquivo" name="arquivo"> </iframe></div>	
	</div>
		<%@include file="Padroes/Mensagens.jspf"%>
</body>
</html>