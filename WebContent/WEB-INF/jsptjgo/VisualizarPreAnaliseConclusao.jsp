<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.PendenciaDt"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>

<jsp:useBean id="AnalisePendenciadt" class= "br.gov.go.tj.projudi.dt.AnaliseConclusaoDt" scope="session"/>

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Visualizar Pr�-An�lise  </title>
	<style type="text/css">
		@import url('./css/Principal.css');
		@import url('./css/Paginacao.css');
	</style>
   	
    	  
    <script type='text/javascript' src='./js/Funcoes.js'></script>
    <script type='text/javascript' src='./js/jquery.js'></script>
    <script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>
	<%@ include file="js/buscarArquivos.js"%>
		
    <%@ include file="./js/MovimentacaoProcesso.js" %>
</head>

<body onload="atualizarPendencias();">
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; |<%=request.getAttribute("tempPrograma")%>| Visualizar Pr�-An�lise </h2></div>
		<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
			
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>">
			<input id="__Pedido__" name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>">
			<input id="PaginaAnterior" name="PaginaAnterior" type="hidden" value="<%=request.getAttribute("PaginaAnterior")%>" />
			<input id="tempFluxo1" name="tempFluxo1" type="hidden" value="<%=request.getAttribute("tempFluxo1")%>" />
						
			<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao">
					<legend> Dados Pr�-An�lise</legend>				
					<table width="100%" >	
						<thead>	
							<tr>
								<th width="10%">Processo(s)</th>
								<th width="20%">Data </th>
								<th width="20%">Usu�rio </th>
								<th width="15%">Tipo Movimenta��o </th>  
								<th width="35%">Classificador</th> 								
							</tr>
						</thead>
						<tbody>			
							<tr>
								<td class="formEdicaoInputSomenteLeitura">
								<%	List pendencias = AnalisePendenciadt.getListaPendenciasFechar(); 
									for (int i=0;i<pendencias.size();i++){
										PendenciaDt objPendencia = (PendenciaDt)pendencias.get(i);	%>
										<%=objPendencia.getProcessoNumero()%>
								<%	} %>
								</td>															
								<td class="formEdicaoInputSomenteLeitura"><%=AnalisePendenciadt.getDataPreAnalise()%></td>												
								<td class="formEdicaoInputSomenteLeitura"><%=AnalisePendenciadt.getUsuarioPreAnalise()%></td>																		 
								<td class="formEdicaoInputSomenteLeitura"><%=AnalisePendenciadt.getMovimentacaoTipo()%></td>												 
								<td class="formEdicaoInputSomenteLeitura"><%=(!AnalisePendenciadt.getClassificador().equals("")?AnalisePendenciadt.getClassificador():"Nenhum")%></td>																																						
							</tr>
						</tbody>
					</table>	
					<div class="spanDestaque"> Complemento:<span class="formEdicaoInputSomenteLeitura"><%=AnalisePendenciadt.getComplementoMovimentacao()%></span></div>		
						
					<fieldset class="formLocalizar">	
						<legend>Texto Pr�-An�lise</legend>
						<div id="divTextoEditor" class="divTextoEditor">
							<%=AnalisePendenciadt.getTextoEditor() %>
						</div>
					</fieldset>
						
					<br />
					<div id="divLocalizar" class="divLocalizar" >		
						<fieldset class="formLocalizar"> 	
							<legend>Lista das pend�ncias</legend>
							<div id="divTabela" class="divTabela" >
								<table class="Tabela">
									<thead>
								    	<tr>
								      		<th>Tipo</th>
								      		<th>Destinat�rio</th>
									  		<th style="display:none">Serventia/Usu�rio</th>
									  		<th>Prazo</th>
									  		<th class="colunaMinima">Urgente</th>
									  		<th style="display:none">on-line</th>
									  		<th class="colunaMinima">Pessoal/Advogados</th>
								    	</tr>
								  	</thead>
								  	<tbody id="corpoTabela">
								    	<tr id="identificador" style="display:none;">
								      		<td><span id="tableTipo">Tipo</span> </td>
								      		<td><span id="tableDestinatario">Destinat�rio</span></td>
								  	  		<td style="display:none"><span id="tableSerUsu">Usu�rio/Serventia</span></td>
								     		<td><span id="tablePrazo">Prazo</span></td>
									  		<td><span id="tableUrgente">Urgente</span></td>
								  	  		<td style="display:none"><span id="tableOnLine">Online</span></td>
								  	  		<td><span id="tablePessoalAdvogados">PessoalAdvogados</span></td>
								    	</tr>
								  	</tbody>
								</table>
							</div>				
						</fieldset>
					</div>
				</fieldset>
			</div>

			<%@ include file="Padroes/ConfirmarOperacao.jspf"%>
			</div>
		</form>
	</div>
<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>
