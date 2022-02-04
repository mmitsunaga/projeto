<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<jsp:useBean id="ProcessoCadastroDt" scope="session" class= "br.gov.go.tj.projudi.dt.ProcessoCadastroDt"/>
<jsp:useBean id="UsuarioSessao" scope="session" class="br.gov.go.tj.projudi.ne.UsuarioNe"/>

<html>
	<head>
		<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
		<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
		<link href="./css/Paginacao.css"  type="text/css"  rel="stylesheet" />
		
		
		<script type='text/javascript' src='./js/Funcoes.js'></script>
	    <script type='text/javascript' src='./js/jquery.js'></script>
	   	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>   
		<script type='text/javascript' src='./js/Digitacao/DigitarSoNumero.js'></script>
		<script type='text/javascript' src='./js/Digitacao/AutoTab.js'></script>
	</head>

	<body onLoad="iniciar();fechar();">
		<div id="divCorpo" class="divCorpo">
			<div class="area"><h2>&raquo; |<%=request.getAttribute("tempPrograma")%>| Inserção de Arquivos </h2></div>
		
			<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
   		
   				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
   				<input id="PaginaAnterior" name="PaginaAnterior" type="hidden" value="<%=request.getAttribute("PaginaAnterior")%>">
   				<input id="PassoEditar" name="PassoEditar" type="hidden" value="<%=request.getAttribute("PassoEditar")%>">
   				<!-- Variáveis para controlar Passos do Cadastro -->
				<input id="Passo1" name="Passo1" type="hidden" value="<%=ProcessoCadastroDt.getPasso1()%>">
				<input id="Passo2" name="Passo2" type="hidden" value="<%=ProcessoCadastroDt.getPasso2()%>">
				<input id="Passo3" name="Passo3" type="hidden" value="<%=ProcessoCadastroDt.getPasso3()%>">

		   		<div id="divEditar" class="divEditar">
		   			<% if (!ProcessoCadastroDt.getPasso1().equals("")){ %>
					<input name="imgPasso1" type="submit" value="<%=ProcessoCadastroDt.getPasso1()%>" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Editar%>');AlterarValue('PassoEditar','-1');">
					<% } if (!ProcessoCadastroDt.getPasso2().equals("")){ %>				
					<input name="imgPasso2" type="submit" value="<%=ProcessoCadastroDt.getPasso2()%>" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Editar%>');AlterarValue('PassoEditar','0');">
					<% } if (!ProcessoCadastroDt.getPasso3().equals("")){ %>				
					<input name="imgPasso3" type="submit" value="<%=ProcessoCadastroDt.getPasso3()%>" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Editar%>');AlterarValue('PassoEditar','2');">
					<% } %>

					<fieldset class="formEdicao">
						<legend>Passo 2 </legend>
						
						<!--  Inserção de Arquivos com opção de usar Editor de Modelos -->
						<br />
						<%@ include file="./Padroes/InsercaoArquivosAssinador.jspf"%>

						<div id="divBotoesCentralizados" class="divBotoesCentralizados">
							<input name="imgConcluir" type="submit" value="Concluir" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Editar%>');AlterarValue('PassoEditar','3');"> 
						</div>
					</fieldset>
				</div>
				
				<%@ include file="Padroes/Mensagens.jspf" %>
			</form>
		</div>

	</body>
	<script type="text/javascript">
		function fechar(valor){
			$('#divEditor').hide();
		}
	</script>
</html>	
