<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="br.gov.go.tj.projudi.dt.UsuarioDt"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="java.util.List"%>
<%@page import="br.gov.go.tj.projudi.dt.AudienciaDt"%>

<!-- BEANS -->
<jsp:useBean id="processoDt" scope="session" class= "br.gov.go.tj.projudi.dt.ProcessoDt" />
<!-- FIM BEANS -->

<html>
	<head>
		<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
		<title> Reserva ou Exclusão de Agendas para Audiências </title>
	
		<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />	
		<script type="text/javascript" src="./js/jquery.js"> </script>
		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
		<script type='text/javascript' src='./js/Funcoes.js'></script>
	</head>

	<body>
		<div id="divCorpo" class="divCorpo">
			<div class="area"><h2>&raquo; Confirmar Retirada de Prioridade do Processo </h2></div>
			<div id="divLocalizar" class="divLocalizar">
				<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
					<input id="PassoEditar" name="PassoEditar" type="hidden" value="" />
					<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
						<br />
						<br />
						<fieldset id="VisualizaDados" class="VisualizaDados">
							<legend> Dados do Processo </legend>
							
							<div> N&uacute;mero</div>
							<span> <%=processoDt.getProcessoNumeroCompleto()%></span>
							
							<br />
							
							<div> Prioridade</div>
							<span> <%=processoDt.getProcessoPrioridade()%></span>
							
						</fieldset>
						<br />
			   			<br />
			   			<fieldset id="alerta" class="alerta">
							<label for="Aviso" style="float:left;margin-left:25px;font-weight:bold" ><small>Antes de retirar a prioridade de um processo, verifique se outra prioridade não se aplica ao mesmo.</small></label><br> 
						</fieldset>
			   			<br />
			   			<br />
						<div id="divSalvar" class="divSalvar" class="divsalvar" align="center">
			    			<input class="img" type="image" src="./imagens/imgExcluir.png" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Curinga8)%>');AlterarValue('PassoEditar','<%=String.valueOf(Configuracao.Excluir)%>')" >
			    			<br />
		        			<div class="divMensagemsalvar">Clique para Confirmar a retirada da Propriedade do Processo.</div>
						</div>
				
				</form>
			</div>  
		</div>
	</body>
</html>