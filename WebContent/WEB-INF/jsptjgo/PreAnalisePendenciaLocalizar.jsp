<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.PendenciaDt"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ClassificadorDt"%>
<%@page import="br.gov.go.tj.projudi.dt.PendenciaArquivoDt"%>

<jsp:useBean id="AnalisePendenciadt" class= "br.gov.go.tj.projudi.dt.AnalisePendenciaDt" scope="session"/>
<jsp:useBean id="UsuarioSessao" class= "br.gov.go.tj.projudi.ne.UsuarioNe" scope="session"/>

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Busca de Pr?-An?lises  </title>
	
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
	<link href="./css/Paginacao.css"  type="text/css"  rel="stylesheet" />
	<link href="./css/menusimples.css" type="text/css"  rel="stylesheet" />
	
	
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type='text/javascript' src='./js/jquery.js'></script>
	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>
	<script type="text/javascript" src="./js/acessibilidadeMenu.js"></script>  
	 <script type="text/javascript">
	    	function submeter(action, id_Pendencia, paginaAtual, preAnalise, fluxoEditar){
	    		AlterarAction('Formulario', action);
	    		AlterarValue('PaginaAtual', paginaAtual);
	    		AlterarValue('Id_Pendencia', id_Pendencia);
	    		AlterarValue('preAnalise', preAnalise);
	    		AlterarValue('FluxoEditar', fluxoEditar);	    		
	    		document.Formulario.submit();
	    	}
	    </script>
	    <script type="text/javascript">
			$(document).ready(function() {			
				criarMenu('opcoes','Principal','menuA','menuAHover');
			});			
		</script>
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; |<%=request.getAttribute("tempPrograma")%>| Busca de Pr?-An?lises Simples </h2></div>
		<div id="divLocalizar" class="divLocalizar"> 
			<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
			
				<div id="divEditar" class="divEditar">
					<fieldset class="formEdicao"> 
						<legend class="formEdicaoLegenda"> Consulta de Pr?-An?lises </legend>
					
						<label id="formLocalizarLabel" class="formLocalizarLabel"> N?mero do Processo </label><br> 
						<input id="numeroProcesso" class="formLocalizarInput" name="numeroProcesso" type="text" value="<%=request.getAttribute("numeroProcesso")%>" size="30" maxlength="60" />
						
						
						<br />
						
						<div id="divBotoesCentralizados" class="divBotoesCentralizados">
							<input id="formLocalizarBotao" class="formLocalizarInput" type="submit" name="Localizar" value="Consultar" onclick="AlterarValue('PaginaAtual','<%=AnalisePendenciadt.getFluxo()%>');" />
						</div>
					</fieldset>
				</div>
		
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input id="Id_Pendencia" name="Id_Pendencia" type="hidden" />
				<input id="preAnalise" name="preAnalise" type="hidden" />
				<input id="FluxoEditar" name="FluxoEditar" type="hidden" value="<%=request.getAttribute("FluxoEditar")%>" />
				<input id="CodigoHash" name="CodigoHash" type="hidden" value="<%=request.getAttribute("CodigoHash")%>" />
				<input id="__Pedido__" name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />

				<div id="divTabela" class="divTabela" > 
					<table id="Tabela" class="Tabela">
						<thead>
							<tr class="TituloColuna">
								<th width="1%"></th>
								<th width="13%">Processo</th>
								<th width="10%">Data In?cio</th>								
								<th width="10%">Data Pr?-An?lise</th>
								<th>Tipo da A??o</th>
								<th width="15%">A??es</th>	
								<th width="5%" colspan="3">Visualizar</th>							
							</tr>
						</thead>
					<tbody>
						<%
						List liTemp = (List)request.getAttribute("ListaPreAnalises");
						long tipoConclusaotemp = -10;
						long tipoConclusao = 0;
						long tipoClassificadortemp = -10;
						long tipoClassificador = 0;
						ProcessoDt processoDt;
						String estiloLinha="TabelaLinha1";
						if (liTemp !=null){
							for(int i = 0 ; i< liTemp.size();i++) {
							PendenciaArquivoDt pendenciaArquivoDt = (PendenciaArquivoDt)liTemp.get(i);
							PendenciaDt pendenciaDt = pendenciaArquivoDt.getPendenciaDt();
							processoDt = pendenciaDt.getProcessoDt();
							tipoConclusao = Funcoes.StringToLong(pendenciaDt.getId_PendenciaTipo()); 
							
							int j=i+1;
							
							//Testa a necessidade de abrir uma linha para o tipo de conclus?o
							if (tipoConclusaotemp == -10){
								tipoConclusaotemp = tipoConclusao;
							%>
							<tr>
								<th colspan="9" class="linhaDestaqueTitulo"> <%= pendenciaDt.getPendenciaTipo()%> </th>
							</tr>
							<%
							} else if (tipoConclusaotemp != tipoConclusao){
								tipoConclusaotemp = tipoConclusao;
								tipoClassificadortemp = -10;	
							%>
							<tr>
								<th colspan="9" class="linhaDestaqueTitulo"> <%= pendenciaDt.getPendenciaTipo()%> </th>
							</tr>
							<%
							}//fim else
							
							if (pendenciaDt.getId_Classificador().length() > 0){
								tipoClassificador = Funcoes.StringToLong(pendenciaDt.getId_Classificador());
							} else tipoClassificador = 0;
							
							//Testa a necessidade de abrir uma linha para o tipo de classificador
							if (tipoClassificadortemp == -10){
								tipoClassificadortemp = tipoClassificador;	
							%>
							<tr>
								<th colspan="9" class="linhaDestaqueSubTitulo"> <%= (pendenciaDt.getClassificador().length()>0?pendenciaDt.getClassificador():"Sem classificador")%> </th>
							</tr>
							<%
							}else if (tipoClassificadortemp != tipoClassificador){
								tipoClassificadortemp = tipoClassificador;
							%>		
							<tr>
								<th colspan="9" class="linhaDestaqueSubTitulo"> <%= (pendenciaDt.getClassificador().length()>0?pendenciaDt.getClassificador():"Sem classificador")%> </th>
							</tr>
							<%
							}
							
							%>
							
							<tr class="<%=estiloLinha%>">
								<td><%=i+1 %></td>
								<% 
									boolean boUrgente = pendenciaDt.isProcessoPrioridade();
								%>
	                   			<td align="center">
	                   				<a href="BuscaProcesso?Id_Processo=<%=pendenciaDt.getProcessoDt().getId()%>" title="<%=(boUrgente?pendenciaDt.getProcessoPrioridadeCodigoTexto():"")%>">
		                   				<%	if (boUrgente){ %>		 
			                   			<img src='./imagens/16x16/imgPrioridade<%=pendenciaDt.getNumeroImagemPrioridade()%>.png' alt="<%=pendenciaDt.getProcessoPrioridadeCodigoTexto()%>" title="<%=pendenciaDt.getProcessoPrioridadeCodigoTexto()%>"/>
			                   			<% } %>
			                   			<%=pendenciaDt.getProcessoDt().getProcessoNumero()%></a>
	                   				</a>
	                   			</td>	
	                   			<td align="center"><%=pendenciaArquivoDt.getPendenciaDt().getDataInicio()%></td>
	                   			<td align="center"><%=pendenciaArquivoDt.getDataPreAnalise()%></td>
	                   			<td><%= pendenciaDt.getProcessoDt().getProcessoTipo()%></td>
	                   			
	                   			<td aling="center">
			                   		<div id="opcoes" class="menuEspecial"> 
			                   			<ul> <li> Op??es<ul>	  	            					
						  					<% 	if (request.getAttribute("podeAnalisar").toString().equalsIgnoreCase("true")){ %>
				                   					<li> <a href="#" onclick="javascript: submeter('AnalisarPendencia','<%=pendenciaDt.getId()%>','<%=Configuracao.Novo%>','true', '');return false;">Analisar</a></li>     			                   			
				                   					
				                   			<%	}  %>
				                   				 
				                   				 <li> <a href="#" onclick="javascript: submeter('PreAnalisarPendencia', '<%=pendenciaDt.getId()%>','<%=Configuracao.Novo%>','false', '');return false;">Refazer</a></li>    			                   			
						                   	     <li> <a href="#" onclick="javascript: submeter('PreAnalisarPendencia', '<%=pendenciaDt.getId()%>','<%=Configuracao.Excluir%>','false', '');return false;">Descartar</a></li>     			                   			
				                   			
<%-- 				                   			<% 	if (request.getAttribute("podeGerarVotoEmenta") != null && request.getAttribute("podeGerarVotoEmenta").toString().equalsIgnoreCase("true")){ %>						                   		    			                   			 --%>
<%-- 			                   			  		<li><a href="#" onclick="javascript: submeter('AnalisarPendencia', '<%=pendenciaDt.getId()%>','<%=Configuracao.Curinga8%>','true', '');return false;">Voto e Ementa</a></li> --%>
<%-- 				                   			<%} %> --%>
				                   			
				                   			<li> <a href="#" onclick="javascript: submeter('DescartarPendenciaProcesso','<%=pendenciaDt.getId()%>','<%=Configuracao.LocalizarDWR%>','true', '');return false;">Classificar</a></li>
				                   			<%if (UsuarioSessao.isPodeExibirPendenciaAssinatura(false, Funcoes.StringToInt(pendenciaDt.getPendenciaStatusCodigo()))){ %>				                   			
				                   				<li><a href="#" onclick="javascript: submeter('PreAnalisarPendencia', '<%=pendenciaDt.getId()%>','<%=Configuracao.Salvar%>', 'true', '2');return false;">Guardar para Assinar</a></li>				                   																
											<%} %>
			                   			</ul></li>
			                   			</ul>
			                   		</div>
			                   	</td>
			                   	
			                   	<td align="center"><a target="_blank" title="Visualizar Pr?-An?lise" href="PendenciaArquivo?PaginaAtual=6&Id_PendenciaArquivo=<%=pendenciaArquivoDt.getId()%>&hash=<%=pendenciaArquivoDt.getHash()%>"><img src="./imagens/32x32/btn_localizar.png" alt="Visualizar Pr?-An?lise" title="Visualizar Pr?-An?lise" /></a></td>	                   			
               				<% } 
               				}
               			%>
               			</tbody>
					</table>
					<%@ include file="Padroes/ConfirmarOperacao.jspf"%>
				</div> 
			</form>
		</div>
		<%@ include file="Padroes/Mensagens.jspf" %>  
	</div>
</body>
</html>