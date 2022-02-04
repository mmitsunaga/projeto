<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.projudi.dt.ProcessoTemaDt"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<jsp:useBean id="ProcessoTemadt" scope="session" class= "br.gov.go.tj.projudi.dt.ProcessoTemaDt"/>
<jsp:useBean id="processoDt" class= "br.gov.go.tj.projudi.dt.ProcessoDt" scope="session"/>

<%@page import="br.gov.go.tj.projudi.dt.ProcessoTemaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.TemaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoDt"%>
<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Busca de ProcessoTema  </title>
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type='text/javascript' src='./js/jquery.js'> </script>
	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>
	<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>
	<%/*<script language="javascript" type="text/javascript" src="./js/EsconderDiv.js" ></script>*/%>
	<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		<script language="javascript" type="text/javascript">
		function VerificarCampos() {
			with(document.Formulario) {
				if (SeNulo(ProcNumero, "O Campo ProcNumero é obrigatório!")) return false;
				if (SeNulo(TemaCodigo, "O Campo TemaCodigo é obrigatório!")) return false;
				submit();
			}
		}
		</script>

	<%}%>
	
	<style>
		.fieldset-motivo label {	    			   
		    font-family: Verdana, Arial, Helvetica, sans-serif;
		    font-size: 9px;
		    font-weight: normal;
		}
	</style>
	
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; Cadastro de ProcessoTema</h2></div>
<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		<form action="ProcessoTema" method="post" name="Formulario" id="Formulario" OnSubmit="JavaScript:return VerificarCampos()">
<%} else {%>
		<form action="ProcessoTema" method="post" name="Formulario" id="Formulario">
<%}%>
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
			<input name="podeSuspender" type="hidden" value="<%=request.getAttribute("podeSuspender")%>" />
			<input name="blocoMotivo" type="hidden" value="<%=request.getAttribute("blocoMotivo")%>" />
			
			<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao"  > 
					<legend class="formEdicaoLegenda">Edita os dados ProcessoTema </legend>
					<input class="formEdicaoInputSomenteLeitura" name="Id_ProcTema"  id="Id_ProcTema"  type="hidden"  readonly="true" value="<%=ProcessoTemadt.getId()%>">
					
					<!-- DADOS DE SOBRESTAMENTO -->
					<div style="width:100%; height:40px; clear:both;">
						<div>
							<span style="width: 20%; display: block; float: left;">
								<label class="formEdicaoLabel">N&uacute;mero Processo</label><br>
								<span class="spanDestaque"><a href="BuscaProcesso?Id_Processo=<%=processoDt.getId_Processo()%>&atualiza=true"><%=processoDt.getProcessoNumero()%></a></span>
							</span>							
						</div>
					</div>
					
					<br />
					
					<!-- TEMAS DO PROCESSO -->
					<input type="hidden" id="posicaoLista" name="posicaoLista">
					<fieldset id="VisualizaDados">  
	   					<legend> 
	   						*Tema(s) do Processo
	   						<input class="FormEdicaoimgLocalizar" id="imaLocalizarProcessoTema" name="imaLocalizarProcessoTema" type="image"  src="./imagens/imgLocalizarPequena.png"
	   							onclick="AlterarValue('PaginaAtual','<%=String.valueOf(TemaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" > 
	   					</legend>
	   					<c:choose>
							<c:when test="${ListaTemas.size() > 0 }">
			   					<table width="98%" border="0" cellpadding="2" cellspacing="2" style="font-size: 10px !important;" class="Tabela">
					   				<thead align="left">
					   					<tr>
						   					<th width="75%">Descrição</th>
						   					<th>Origem</th>
						   					<th>Tipo</th>
						   					<th>Situação</th>
						   					<th>Data Sobrestado</th>
						   					<th align="center">Excluir</th>
					   					</tr>
					   				</thead>
					   				<tbody>
					   					<c:forEach items="${ListaTemas}" var="item" varStatus="index">
   											<tr>
   												<td>${item.temaCodigoTitulo}</td>
   												<td>${item.temaOrigem}</td>
   												<td>${item.temaTipoCnj}</td>
   												<td>${item.temaSituacao}</td>
						       					<td>${item.dataSobrestado}</td>
						       	 				<td align="center"><input name="imgExcluir" class="imgExcluir" type="image" src="./imagens/imgExcluirPequena.png"					       	 					 
						       	 				onclick="AlterarValue('PaginaAtual','<%=Configuracao.Excluir%>');AlterarValue('PassoEditar','6');AlterarValue('posicaoLista','${index.getIndex()}')" title="Excluir tema"/></td>
   											</tr>
	   									</c:forEach>
					   				</tbody>
					   			</table>
							</c:when>
							<c:otherwise>
								<em> Nenhum tema cadastrado </em>
							</c:otherwise>
						</c:choose>
			   		</fieldset>
					
					<!-- Somente mostrar esse fieldset se o processo estiver suspenso -->
					<% if ((boolean) request.getAttribute("mostrarMotivo"))  {%>
						<fieldset class="fieldset-motivo">
							<legend>* Informe o motivo que justifique a exclusão do tema</legend>
							<div>
								<label><%= request.getAttribute("temaSelecionado") %></label><br />
								<textarea rows="4" id="motivo" name="motivo" style="width: 100%; margin-top:5px;"></textarea>					    		
							</div>
						</fieldset>		
					<%}%>
					
					<!-- HISTÓRICO DE TEMAS JÁ CONCLUIDOS -->
					<c:if test="${ListaTemasHistorico.size() > 0}">
						<fieldset id="VisualizaDados">
							<legend>
		   						Histórico de Tema(s)
		   					</legend>
		   					<div>
		   						<table class="Tabela" style="font-size: 10px !important; background-color: #FAFAFA">
		   							<thead>
						           		<tr class="TituloColuna">
						           			<th class="colunaMinima" width="5%">&nbsp;</th>
						           			<th width="5%">Código</th>
							               	<th width="5%">Origem</th>
							               	<th width="60%">Título</th>
							               	<th width="15%">Data Sobrestado</th>
							               	<th width="15%">Data Sobr.Final</th>						               	
						    	        </tr>
		   							</thead>
		   							<tbody>
		   								<c:forEach items="${ListaTemasHistorico}" var="item" varStatus="index">		   									
	   										<tr>
		   										<td></td>
		   										<td>${item.temaCodigo}</td>
		   										<td>${item.temaOrigem}</td>
		   										<td>${item.titulo}</td>
		   										<td>${item.dataSobrestado}</td>
		   										<td>${item.dataFinalSobrestado}</td>
		   									</tr>
		   								</c:forEach>
		   							</tbody>
		   						</table>
		   					</div>
						</fieldset>
					</c:if>
					
					<!-- Botão SaLVAr -->
					<% if ((int) request.getAttribute("PaginaAnterior") != Configuracao.Excluir && (int) request.getAttribute("PaginaAnterior") != Configuracao.Salvar){ %>
						<div id="divBotoesCentralizados" class="divBotoesCentralizados">
							<% if ((boolean) request.getAttribute("podeSuspender"))  {%>
								<input name="imgInserir" type="submit" value="Salvar e Suspender" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Salvar%>');">
							<%} else {%>
								<input name="imgInserir" type="submit" value="Salvar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Salvar%>');">
							<%}%>
			    		</div>
					<%}%>					
			    	
				</fieldset>

			<%@ include file="Padroes/ConfirmarOperacao.jspf"%>

			</div>

		</form>
	</div>
	<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>
