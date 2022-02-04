<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<html>
	<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE" />
	<style type="text/css">
		@import url('./css/Principal.css');
		@import url('./css/Paginacao.css');
		@import url('js/jscalendar/dhtmlgoodies_calendar.css?random=20051112');
	</style>
	<title>Recuperação de Senha</title>
	<link rel="shortcut icon" href="./imagens/favicon.png" / >
	<link href="./css/Principal.css" type="text/css" rel="stylesheet" />
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type='text/javascript' src='./js/Digitacao/DigitarData.js'></script>
	<script type='text/javascript' src='./js/jquery.js'></script>
	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>
	<script type="text/javascript" src="./js/tabelas.js"></script>
	<script type="text/javascript" src="./js/tabelaArquivos.js"></script>
	<%@ include file="js/buscarArquivos.js"%>
	<script type="text/javascript" src="./js/jscalendar/dhtmlgoodies_calendar.js"></script>
	<style type="text/css">
		#bkg_projudi {display: none}
	</style>

	
</head>
<body class="fundo" onload="alterarValorRadioFiltro('<%=request.getAttribute("ConsultaFiltroRadio")%>');">
	<%@ include	file="/CabecalhoPublico.html"%>
	<div id="divCorpo" class="divCorpo">
	
		<%if( !request.getAttribute("fluxo").equals("linkInvalido") ){%>
			
			<div class="area"><h2>&raquo; Recuperação de Senha</h2></div>
	
		<%}%>	
		
		
		<form action="RecuperaSenha" method="post" >
		
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%= request.getAttribute("paginaatual") %>" />
			<input  name="tokenParam" type="hidden" value="<%=request.getAttribute("tokenParam")%>" />
			
			<%if( request.getAttribute("fluxo").equals("inicio") ){%>
			<div>
				
				<label for="usuario" style="position: relative">Usuário:</label>
				<br/>
				<input id="usuario" name="usuario" type="text" maxlength="20" class="textinput" />
				
				<br/>
				<br/>
				
				<div style="color:red"> <%= request.getAttribute("msg") %> </div>
				<br/>
				<div>
				<input type="submit" value="Recuperar Senha" />
				</div>
				
			</div>
			<%}
			
			if( request.getAttribute("fluxo").equals("emailEnviado") ){%>
			
				<div>
				Um e-mail foi enviado com instruções para continuar a recuperação da sua senha.
				</div>

			<%}
			
			if( request.getAttribute("fluxo").equals("mostrarFormulario") ){%>
			
				<div>
				<label for="novaSenha1" style="position: relative">Nova senha:</label>
				<br/>
				<input id="novaSenha1" name="novaSenha1" type="password" maxlength="20" class="textinput"/>
				<br/>
				<br/>
				
				<label for="novaSenha2" style="position: relative">Confirmar senha:</label>
				<br/>
				<input id="novaSenha2" name="novaSenha2" type="password" maxlength="20" class="textinput"/>
				<div style="color:red"> <%= request.getAttribute("msg") %> </div>
				<br/>
				
				<div>
				<input type="submit" value="Salvar" />
				</div>
				</div>
			
			<%}
			if( request.getAttribute("fluxo").equals("senhaAlterada") ){
			%>
			
				<div>
				A sua senha foi alterada com sucesso.
				</div>
			
			<%}
			if( request.getAttribute("fluxo").equals("linkInvalido") ){%>
			
			<div>
			ERRO, VOCÊ TENTOU UTILIZAR UM LINK INVÁLIDO. 
			</div>
	
			<%}
			if( request.getAttribute("fluxo").equals("semEmail") ){%>
			
			<div>
			Sem e-mail cadastrado. 
			</div>
			
			<%}%>
		</form>
	</div>
	<%@ include file="Padroes/Mensagens.jspf"%>
</body>
</html>