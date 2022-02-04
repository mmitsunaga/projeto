<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.PendenciaDt"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>

<jsp:useBean id="AnalisePendenciadt" class= "br.gov.go.tj.projudi.dt.AnaliseConclusaoDt" scope="session"/>

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Visualizar Pré-Análise  </title>
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
		<div class="area"><h2>&raquo; |<%=request.getAttribute("tempPrograma")%>| Visualizar Pré-Análise </h2></div>
		<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
			
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>">
			<input id="__Pedido__" name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>">
			<input id="PaginaAnterior" name="PaginaAnterior" type="hidden" value="<%=request.getAttribute("PaginaAnterior")%>" />
			
			<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao">
					<legend> Dados Pré-Análise</legend>
					
					<label class="formEdicaoLabel">Processo(s) </label><br>  
					<%
					List pendencias = AnalisePendenciadt.getListaPendenciasFechar(); 
						for (int i=0;i<pendencias.size();i++){
							PendenciaDt objPendencia = (PendenciaDt)pendencias.get(i);
					%>
						<span class="spanDestaque"><%=objPendencia.getProcessoNumero()%></span>
					<%	} %>
					<br />
				
					<label class="formEdicaoLabel"> Data Pré-Análise </label><br>
					<span class="spanDestaque"><%=AnalisePendenciadt.getDataPreAnalise()%></span>
					<label> Usuário Pré-Análise</label><br>
					<span class="spanDestaque"><%=AnalisePendenciadt.getUsuarioPreAnalise()%></span>
					<br />				

					<label class="formEdicaoLabel">Classificador</label><br>   
					<span class="spanDestaque"><%=(!AnalisePendenciadt.getClassificador().equals("")?AnalisePendenciadt.getClassificador():"Nenhum")%></span>
					<br />
					
					<label class="formEdicaoLabel">Apreciada Admissibilidade e/ou Mérito do Processo/Recurso Principal</label><br>  
					<span class="destaque"><%=AnalisePendenciadt.getJulgadoMeritoProcessoPrincipal().equalsIgnoreCase("true")?"Sim":"Não"%></span>
					<br />
							
					<fieldset class="formLocalizar">	
						<legend>Pré-Análise Relatório e Voto <input class="FormEdicaoimgLocalizar" name="imaLocalizarArquivoTipo" type="image"  src="./imagens/imgEditorTextoPequena.png" 
						                                          onclick="MostrarOcultar('divTextoEditor'); return false;" title="Abrir Editor de Texto" />
						</legend>			
						<div id="divTextoEditor" class="divTextoEditor" style="display:none">
							<%=AnalisePendenciadt.getTextoEditor() %>
						</div>
					</fieldset>
					
					<fieldset class="formLocalizar">	
						<legend>Pré-Análise Ementa <input class="FormEdicaoimgLocalizar" name="imaLocalizarArquivoTipo" type="image"  src="./imagens/imgEditorTextoPequena.png" 
						 								onclick="MostrarOcultar('divTextoEditorEmenta'); return false;" title="Abrir Editor de Texto" />
						 </legend>						
						<div id="divTextoEditorEmenta" class="divTextoEditor" style="display:none">
							<%=AnalisePendenciadt.getTextoEditorEmenta() %>
						</div>
					</fieldset>
						
					<br />
					<div id="divLocalizar" class="divLocalizar" >		
						<fieldset class="formLocalizar"> 	
							<legend>Lista das pendências</legend>
							<div id="divTabela" class="divTabela" >
								<table class="Tabela">
									<thead>
								    	<tr>
								      		<th>Tipo</th>
								      		<th>Destinatário</th>
									  		<th style="display:none">Serventia/Usuário</th>
									  		<th>Prazo</th>
									  		<th class="colunaMinima">Urgente</th>
									  		<th style="display:none">on-line</th>
									  		<th class="colunaMinima">Pessoal/Advogados</th>
								    	</tr>
								  	</thead>
								  	<tbody id="corpoTabela">
								    	<tr id="identificador" style="display:none;">
								      		<td><span id="tableTipo">Tipo</span> </td>
								      		<td><span id="tableDestinatario">Destinatário</span></td>
								  	  		<td style="display:none"><span id="tableSerUsu">Usuário/Serventia</span></td>
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
