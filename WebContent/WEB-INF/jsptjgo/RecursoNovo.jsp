<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.AssuntoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoTipoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.RecursoParteDt"%>
<%@page import="br.gov.go.tj.projudi.dt.RecursoAssuntoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.RecursoNovoDt"%>

<jsp:useBean id="RecursoNovodt" scope="session" class= "br.gov.go.tj.projudi.dt.RecursoNovoDt"/>
<jsp:useBean id="Recursodt" scope="session" class= "br.gov.go.tj.projudi.dt.RecursoDt"/>
<jsp:useBean id="processoDt" scope="session" class= "br.gov.go.tj.projudi.dt.ProcessoDt"/>

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Incluir Novo Recurso  </title>
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />	
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type='text/javascript' src='./js/jquery.js'></script>
   	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>   
	<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js" ></script>
	<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; Incluir Novo Recurso</h2></div>
		<form action="RecursoNovo" method="post" name="Formulario" id="Formulario">
		
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
			<input id="PassoEditar" name="PassoEditar" type="hidden" value="<%=request.getAttribute("PassoEditar")%>">
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
			<input id="PaginaAnterior" name="PaginaAnterior" type="hidden" value="<%=request.getAttribute("PaginaAnterior")%>" />
			
			<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao"> 
					<legend class="formEdicaoLegenda">Incluir Novo Recurso </legend>

					<label class="formEdicaoLabel"> Processo </label><br>
					<span class="span"> <a href="BuscaProcesso?Id_Processo=<%=Recursodt.getId_Processo()%>"><%=processoDt.getProcessoNumero()%></a></span>
					<br />
					
					<label class="formEdicaoLabel" for="Id_ProcessoTipo">* Classe
				    <input class="FormEdicaoimgLocalizar" id="imaLocalizarProcessoTipo" name="imaLocalizarProcessoTipo" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ProcessoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" >
				    </label><br>  
				    <input type="hidden" name="Id_ProcessoTipo" id="Id_ProcessoTipo" value="<%=RecursoNovodt.getId_ProcessoTipo()%>">
				    <input type="hidden" name="ProcessoTipoCodigo" id="ProcessoTipoCodigo" value="<%=RecursoNovodt.getProcessoTipoCodigo()%>">
					<input class="formEdicaoInputSomenteLeitura"  readonly name="ProcessoTipo" id="ProcessoTipo" type="text" size="67" maxlength="100" value="<%=RecursoNovodt.getProcessoTipo()%>"/>
					<br />	
					
					<div id="divBotoesCentralizados" class="divBotoesCentralizados">
						<input name="imgInserir" type="submit" value="Adicionar Parte" onclick="AlterarAction('Formulario', 'ProcessoParte'); AlterarValue('PaginaAtual','<%=Configuracao.Novo%>');">
					</div>			
										
					<fieldset id="VisualizaDados" class="VisualizaDados">
						<legend> *Recorrente(s) </legend>
						<%
						List listaPartes = processoDt.getPartesProcesso();
 	    				for (int i=0;i < listaPartes.size();i++){
	  			  			ProcessoParteDt parteDt = (ProcessoParteDt)listaPartes.get(i);
	  			  			if (parteDt.getDataBaixa().length() == 0){
	 					%>
			   				<div>
				   				<input name="Recorrente" id="Recorrente" type="checkbox" value="<%=parteDt.getId_ProcessoParte()%>" 
				   				<%	
				   					List listaRecorrentes = (List) request.getAttribute("ListaRecorrentes");
									if (listaRecorrentes != null && listaRecorrentes.size()>0){
						            	for(int j = 0 ; j< (listaRecorrentes.size());j++) {
						              		RecursoParteDt obj = (RecursoParteDt) listaRecorrentes.get(j);
						                   	if (obj.getId_ProcessoParte().equals(parteDt.getId_ProcessoParte())){%> 
						                   	checked
						        <% 			}
						               	}
									}
								%>
								/>
			   				</div> 
			       			<span><%=parteDt.getNome()%> </span>
			       			<div> CPF </div>
			        		<span><%=parteDt.getCpfCnpjFormatado()%></span/><br />
						<%	}
 	    				}
						%>
					</fieldset>
					
					<fieldset id="VisualizaDados" class="VisualizaDados">
						<legend> *Recorridos(s) </legend>
						<%
 	    				for (int i=0;i < listaPartes.size();i++){
	  			  			ProcessoParteDt parteDt = (ProcessoParteDt)listaPartes.get(i);
	  			  			if (parteDt.getDataBaixa().length() == 0){
	 					%>
			   				<div>
				   				<input name="Recorrido" id="Recorrido" type="checkbox" value="<%=parteDt.getId_ProcessoParte()%>" 
				   				<%	
				   					List listaRecorridos = (List) request.getAttribute("ListaRecorridos");
									if (listaRecorridos != null && listaRecorridos.size()>0){
						            	for(int j = 0 ; j< (listaRecorridos.size());j++) {
						            		RecursoParteDt obj = (RecursoParteDt) listaRecorridos.get(j);
						                   	if (obj.getId_ProcessoParte().equals(parteDt.getId_ProcessoParte())){%> 
						                   	checked
						        <% 			}
						               	}
									}
								%>
								/>
			   				</div> 
			       			<span><%=parteDt.getNome()%> </span>
			       			<div> CPF </div>
			        		<span><%=parteDt.getCpfCnpjFormatado()%></span/><br />
						<%	}
 	    				}
						%>
					</fieldset>
					<br />
					<br />
		
					
					<br />
					
					<div id="divBotoesCentralizados" class="divBotoesCentralizados">
						<input name="imgInserir" type="submit" value="Salvar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Salvar%>');"> 
						<input name="imgInserir" type="submit" value="Limpar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Novo%>');">
					</div>
				</fieldset>

			<%@ include file="Padroes/ConfirmarOperacao.jspf"%>

			</div>

		</form>
	</div>
	<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>