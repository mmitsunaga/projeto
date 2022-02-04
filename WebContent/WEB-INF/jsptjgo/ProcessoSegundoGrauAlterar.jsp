<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoTipoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoFaseDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoPrioridadeDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ClassificadorDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoAssuntoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.AssuntoDt"%>

<jsp:useBean id="processoDt" scope="session" class= "br.gov.go.tj.projudi.dt.ProcessoDt"/>

<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteTipoDt"%>
<html>
	<head>
		<title>Processo</title>
	
    	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./css/Paginacao.css');
		</style>
      	
      	
      	<script type='text/javascript' src='./js/Funcoes.js'></script>
      	<script type='text/javascript' src='./js/Digitacao/DigitarSoNumero.js'></script>
		<script type='text/javascript' src='./js/Digitacao/MascararValor.js'></script>      	
		<script type='text/javascript' src='./js/Digitacao/AutoTab.js'></script>
		<script type='text/javascript' src='./js/jquery.js'></script>
   		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>   
	</head>

	<body>
  		<div id="divCorpo" class="divCorpo" >
			<div class="area"><h2>&raquo; |<%=request.getAttribute("tempPrograma")%>| Modificar Dados de Processo de 2º Grau</h2></div>
			
			<form action="Processo" method="post" name="Formulario" id="Formulario">
			
				<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input id="PassoEditar" name="PassoEditar" type="hidden" value="<%=request.getAttribute("PassoEditar")%>">
				<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
				<input type="hidden" id="posicaoLista" name="posicaoLista">
			
				<input type="hidden" id="Id_ProcessoParte" name="Id_ProcessoParte">
	
				<div id="divEditar" class="divEditar">
					<fieldset class="formEdicao"> 
					    <legend class="formEdicaoLegenda">Modificar Dados do Processo de 2º Grau</legend>
					    
					    <label class="formEdicaoLabel"> Processo </label><br>
					    <span class="span"> <a href="BuscaProcesso?Id_Processo=<%=processoDt.getId_Processo()%>&atualiza=true"><%=processoDt.getProcessoNumero()%></a> </span>
						<br />
						<br />
						
					    <label class="formEdicaoLabel" for="Id_ProcessoTipo">*Classe 
					    <input class="FormEdicaoimgLocalizar" id="imaLocalizarProcessoTipo" name="imaLocalizarProcessoTipo" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ProcessoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" >  
					   </label><br> 
					    <input class="formEdicaoInputSomenteLeitura"  readonly name="ProcessoTipo" id="ProcessoTipo" type="text" size="100" maxlength="100" value="<%=processoDt.getProcessoTipo()%>"/><br />

			    		<!-- ASSUNTOS DO PROCESSO -->
			    		<fieldset id="VisualizaDados" class="VisualizaDados" style="width: 780px;margin-left: 75px;">  
  								<legend> 
	  								*Assunto(s)
  						    		<input class="FormEdicaoimgLocalizar" id="imaLocalizarProcessoTipo" name="imaLocalizarProcessoTipo" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(AssuntoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" > 
  								</legend>
						   		<%
						   			List listaAssuntos = (List) request.getSession().getAttribute("ListaAssuntos");
						   	    	if (listaAssuntos != null && listaAssuntos.size() > 0){ %>
						   			<table width="98%" border="0" cellpadding="0" cellspacing="0" style="font-size: 10px !important;">
						   				<thead align="left">
						   					<th width="90%">Descrição</th>
						   					<th>Excluir</th>
						   				</thead>
									<%
						   	    		for (int i=0;i < listaAssuntos.size();i++){
						   	    			ProcessoAssuntoDt assuntoDt = (ProcessoAssuntoDt)listaAssuntos.get(i); %>
							   			<tbody>
											<tr>
						       					<td><%=assuntoDt.getAssunto()%></td>
						       	 				<td><input name="imgExcluir" class="imgExcluir" type="image" src="./imagens/imgExcluirPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Editar%>');AlterarValue('posicaoLista','<%=i%>')" title="Excluir assunto"/></td>
						       	 			</tr>
						       	 		</tbody>
						       		<%	} %>
						       	</table>
						   		<% } else { %> <em> Nenhum assunto cadastrado </em> <% } %>
						</fieldset>
				    	<br />
				    	<br />
				    	
			    		<div id="divBotoesCentralizados" class="divBotoesCentralizados">
							<input name="imgInserir" type="submit" value="Salvar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Salvar%>');"> 
				    	</div>
		    		</fieldset>
					<%@ include file="Padroes/ConfirmarOperacao.jspf"%>
				</div>
			</form>
			<%@ include file="Padroes/Mensagens.jspf" %>
		</div>
	</body>
</html>