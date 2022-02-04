<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="br.gov.go.tj.utils.Configuracao"%>

<html>
	<head>
		<title>Confirma&ccedil;&atilde;o de Consulta</title>
		<link rel="shortcut icon" href="./imagens/favicon.png">
		<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<script type='text/javascript' src='./js/Funcoes.js'></script>
		<script type='text/javascript' src='./js/jquery.js'></script>
   		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>   
		<style type="text/css">
			@import url('./css/Principal.css');
		</style>						
	</head>
	<body class="fundo">
		        			
		<% 	
			String nomeSistema = "Projudi";
			if (request.getSession().getAttribute("TipoConsulta") != null && request.getSession().getAttribute("TipoConsulta").equals("Publica")) { %>
			<% if (request.getAttribute("Sistema") != null && request.getAttribute("Sistema").equals("2")) { 
				nomeSistema = "PJe";
			%>
  				<p>
	    			<img width="870" height="160" alt="bnr pje2" src="imagens/bnr_pje2.png"></img>
				</p>
				<p></p>
  			<% } else { %>
  				<style type="text/css"> #bkg_projudi{ display:none } </style>
  				<%@ include file="/CabecalhoPublico.html" %>
  			<% }  %>
  		<% } %>
  		<div class="divCorpo">
		<form name="form" method="post" action="<%=request.getAttribute("action")%>">
			<input type="hidden" name="<%=request.getAttribute("nome")%>" value="<%=request.getAttribute("valor")%>">
			<input type="hidden" name="<%=request.getAttribute("nome2")%>" value="<%=request.getAttribute("valor2")%>">	
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>">
			<input id="PassoBusca" name="PassoBusca" type="hidden" value="<%=request.getAttribute("PassoBusca")%>">
			<input id="TipoConsulta" name="TipoConsulta" type="hidden" value="<%=request.getAttribute("TipoConsulta")%>">
			<input id="Sistema" name="Sistema" type="hidden" value="<%=request.getAttribute("Sistema")%>">
			<input id="ServletRedirect" name="ServletRedirect" type="hidden" value="<%=request.getAttribute("ServletRedirect")%>">
			
			<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao"> 
	    			<legend class="formEdicaoLegenda">Confirmar Consulta</legend>
	
					<div id="divCaptcha" class="divCaptcha">
						<span>C&oacute;digo de confer&ecirc;ncia </span><br />
						<img src="CaptchaProjudiServlet?v=<%=System.currentTimeMillis()%>" alt="codigo">										
						<!-- style="position: relative; left: -26px;" -->
						
						<audio id="audio" src="./CaptchaServlet?Tipo=SOM&v=<%=System.currentTimeMillis()%>"></audio>
						<div>
						  <img title="Reproduzir o áudio" alt="Reproduzir o áudio" src='./imagens/audio_icon.gif' onclick="document.getElementById('audio').play();"/> 						  
						  <img title="Aumentar o volume" alt="Aumentar o volume"  src='./imagens/16x16/Mais.png' onclick="document.getElementById('audio').volume+=0.1;">
						  <img title="Diminuir o volume"  alt="Diminuir o volume" src='./imagens/16x16/Menos.png' onclick="document.getElementById('audio').volume-=0.1;">
						</div>					 
						<!--<a href="#" OnClick="javascript:DoPlayApplet('CaptchaServlet?Tipo=SOM',null)" alt="Clique para ouvir o código de conferência">     
							<img src="./imagens/audio_icon.gif" style="border-width: 0pt;" alt="Ouvir o código de conferência" width="22,5" height="24">
						</a>
						<a href="CaptchaServlet?Tipo=SOM">     
							<img src="./imagens/audio_icon.gif" style="border-width: 0pt;" alt="Ouvir o código de conferência" width="22,5" height="24">
						</a>						
						--><br />				
												
						<span>Digite o código de confer&ecirc;ncia acima para completar a consulta:</span> 
						<br />
						
			 			<input class="formEdicaoInput" type="text" id="textoDigitado" name="textoDigitado" value=""> <br /> 
						<span><font size="1">Se n&atilde;o conseguir visualizar a imagem, clique em Submeter sem digitar o c&oacute;digo</font> </span>
			  		</div>
  	
				  	<div id="divBotoesCentralizados" class="divBotoesCentralizados">
							<input name="imgSubmeter" type="submit" value="Submeter" onclick="AlterarValue('PaginaAtual', '<%=Configuracao.Editar%>');AlterarValue('PassoBusca','3');">
					</div>
					
					<p class="notificacao">
					   	Este código de conferência ajuda o Tribunal de Justiça do Estado de Goiás a evitar a consulta por programas automáticos, que dificultam a utilização do Projudi pelos demais usuários.
					   	Enquanto um usuário faz uma consulta por minuto, um programa pode fazer milhares, congestionando assim o sistema.
				   	</p>
				</fieldset>
			</div>
			<span id=dummyspan></span>
		</form>
		<%@ include file="Mensagens.jspf"%>
		</div>
		
		<script language="JavaScript1.2">
		<!-- Não retirar pois é utilizado para reproduzir som
			function DoPlayApplet(url, nomeArquivo){
				// document.applets["ReproduzirSom"].reload();				
				document.ReproduzirSom.setParametros(url, nomeArquivo, true);
				document.ReproduzirSom.init();
				// document.applets["ReproduzirSom"].play();
				// alert("obtendo o som!");
			}
			//-->			
		</script>	
			
		<script type="text/javascript">
	    	if( document.getElementById("textoDigitado") ) {
	    		document.getElementById("textoDigitado").focus();
	    	}
		</script>
	</body>
</html>