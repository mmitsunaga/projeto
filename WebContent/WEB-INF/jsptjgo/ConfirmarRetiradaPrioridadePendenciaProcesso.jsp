<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="br.gov.go.tj.projudi.dt.UsuarioDt"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="java.util.List"%>
<%@page import="br.gov.go.tj.projudi.dt.AudienciaDt"%>
<jsp:useBean id="retirarPrioridadePendenciaDt" scope="session" class= "br.gov.go.tj.projudi.dt.PendenciaDt" />

<html>
	<head>
		<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
		<title> Reserva ou Exclusão de Agendas para Audiências </title>
		<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />	
		<script type="text/javascript" src="./js/jquery.js"> </script>
		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
		<script type='text/javascript' src='./js/Funcoes.js'></script>
		<style>
			.VisualizaDados div{
				width:140px
			}
		</style>
	</head>
	
	<body>
		<div id="divCorpo" class="divCorpo">
			<div class="area">
				<h2>&raquo; Confirmar Retirada de Prioridade da Pendência </h2>
			</div>
			
			<div id="divLocalizar" class="divLocalizar">
				<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
					<input id="PassoEditar" name="PassoEditar" type="hidden" value="" />
					<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
					<fieldset id="VisualizaDados" class="VisualizaDados">
						<legend>
							Dados da Pendência
						</legend>
						
						<div>
							Tipo
						</div>
						<span>
							<%=retirarPrioridadePendenciaDt.getPendencia()%>
						</span>
						<br>
						
						<div>
							Prioridade
						</div>
						<span>
							<%=retirarPrioridadePendenciaDt.getPendenciaPrioridadeCodigoTexto()%>
						</span>
						<br>
						
						<div>
							Status
						</div>
						<span>
							<%=retirarPrioridadePendenciaDt.getPendenciaStatus()%>
						</span>
						<br>
													
						<div>
							N&uacute;mero do Processo
						</div>
						<span>
							<%=retirarPrioridadePendenciaDt.getProcessoNumeroCompleto()%>
						</span>							
						<br>
													
						<div>
							Prioridade do Processo
						</div>
						<span>
							<%=retirarPrioridadePendenciaDt.getProcessoPrioridadeCodigoTexto()%>
						</span>							
						<br>
						<br>
						
					</fieldset>
					<br>
		   			<br>
		   			
					<div id="divSalvar" class="divSalvar" class="divsalvar" align="center">
		    			<input class="img" type="image" src="./imagens/imgExcluir.png" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Curinga8)%>');AlterarValue('PassoEditar','<%=String.valueOf(Configuracao.Excluir)%>');AlterarValue('retirarPrioridadePendencia_Id','<%=String.valueOf(retirarPrioridadePendenciaDt.getId())%>')" >
		    			<br>
	        			<div class="divMensagemsalvar">
	        				Clique para Confirmar a retirada da Prioridade da Pendência.
	        			</div>
					</div>				
				</form>
			</div>  
		</div>
	</body>
</html>