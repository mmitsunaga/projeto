<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.PendenciaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ClassificadorDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoDt"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>
<html>
	<head>
		<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
		<title> |<%=request.getAttribute("tempPrograma")%>| Busca de Pendências Não Analisadas  </title>
		<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
		<link href="./css/Paginacao.css"  type="text/css"  rel="stylesheet" />
		<link href="./css/menusimples.css" type="text/css"  rel="stylesheet" />
		
		
		<script type='text/javascript' src='./js/Funcoes.js'></script>
    	<script type='text/javascript' src='./js/checks.js'></script>	
	    <script type='text/javascript' src='./js/jquery.js'></script>
	    <script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>      
	    <script type="text/javascript" src="./js/acessibilidadeMenu.js"></script>   	 	
	    <script type="text/javascript">
	    	function submeter(action, id_Pendencia, paginaAtual){
	    		AlterarAction('Formulario', action);
	    		AlterarValue('PaginaAtual', paginaAtual);
	    		AlterarValue('Id_Pendencia', id_Pendencia);
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
		<div id="divCorpo" class="divCorpo">
			<div class="area"><h2>&raquo; |<%=request.getAttribute("tempPrograma")%>| Busca de Pendências Não Analisadas </h2></div>
				
				<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
					
					<div id="divEditar" class="divEditar">
						<fieldset class="formEdicao"> 
						    <legend class="formEdicaoLegenda"> Consulta de de Pendências Não Analisadas </legend>
							<label id="labelProcesso" class="formLocalizarLabel"> Número do Processo </label><br> 
							<input id="numeroProcesso" class="formLocalizarInput" name="numeroProcesso" type="text" value="<%=request.getAttribute("numeroProcesso")%>" size="30" maxlength="60" />
							
							<br />
							
							<div id="divBotoesCentralizados" class="divBotoesCentralizados">
								<input id="btnLocalizar" class="formLocalizarBotao" type="submit" name="Localizar" value="Consultar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Localizar%>');AlterarValue('PassoEditar','1');AlterarAction('Formulario','<%=(request.getAttribute("podeAnalisar").toString().equalsIgnoreCase("true")?"AnalisarPendencia":"PreAnalisarPendencia")%>');" />
								<input id="btnLimpar" class="formLocalizarBotao" type="submit" name="Limpar" value="Limpar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Localizar%>');AlterarValue('PassoEditar','0');AlterarAction('Formulario','<%=(request.getAttribute("podeAnalisar").toString().equalsIgnoreCase("true")?"AnalisarPendencia":"PreAnalisarPendencia")%>');" />
							</div>
						</fieldset>
					</div>
					
					<div id="divLocalizar" class="divLocalizar"> 
			
					<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>"/>
					<input id="PassoEditar" name="PassoEditar" type="hidden" value="<%=request.getAttribute("PassoEditar")%>"/>
					<input id="Id_Pendencia" name="Id_Pendencia" type="hidden" />
					
					<div id="divTabela" class="divTabela">
						<% 	if (request.getAttribute("podeAnalisar").toString().equalsIgnoreCase("true")){ %>
						<input name="imgMultipla" type="submit" value="Análise Múltipla" onclick="AlterarAction('Formulario','AnalisarPendencia');AlterarValue('PaginaAtual','<%=Configuracao.Novo%>');"/>
						<% } %>
						<input name="imgMultipla" type="submit" value="Pré-Análise Múltipla" onclick="AlterarAction('Formulario','PreAnalisarPendencia');AlterarValue('PaginaAtual','<%=Configuracao.Novo%>');"/>
					 
						<table id="Tabela" class="Tabela">
							<thead>
								<tr class="TituloColuna">
									<th width="4%">
	            						<input type="checkbox" id="chkSelTodos" value="" onclick="atualizarChecks(this, 'divTabela')"
				    						title="Marcar/Desmarcar todos os itens da lista" />
				    				</th>
									<th width="16%">Processo</th>
									<th width="15%">Data/Hora</th>
									<th>Tipo da Ação</th>
									<th width="20%" colspan="5">Ações</th>									
								</tr>
							</thead>
							<%
							List liTemp = (List)request.getAttribute("ListaPendencias");
							PendenciaDt pendenciaDt;
							ProcessoDt processoDt;
							boolean boLinha=false; 
							long tipoPendenciaTemp = -10;
							long tipoPendencia = 0;
							long tipoClassificadortemp = -10;
							long tipoClassificador = 0;
							if (liTemp !=null){
								for(int i = 0 ; i< liTemp.size();i++) {
									pendenciaDt = (PendenciaDt)liTemp.get(i); 
									processoDt = pendenciaDt.getProcessoDt();
									tipoPendencia = Funcoes.StringToLong(pendenciaDt.getId_PendenciaTipo()); 
									
									int j=i+1;
									
									//Testa a necessidade de abrir uma linha para o tipo de pendência
									if (tipoPendenciaTemp == -10){
										tipoPendenciaTemp = tipoPendencia;
									%>
									<tr>
										<th colspan="9" class="linhaDestaqueTitulo"> <%= pendenciaDt.getPendenciaTipo()%> </th>
									</tr>
									<%
									} else if (tipoPendenciaTemp != tipoPendencia){
										tipoPendenciaTemp = tipoPendencia;
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
										<th colspan="8" class="linhaDestaqueSubTitulo"> <%= (pendenciaDt.getClassificador().length()>0?pendenciaDt.getClassificador():"Sem classificador")%> </th>
									</tr>
									<%
									}else if (tipoClassificadortemp != tipoClassificador){
										tipoClassificadortemp = tipoClassificador;
									%>		
									<tr>
										<th colspan="8" class="linhaDestaqueSubTitulo"> <%= (pendenciaDt.getClassificador().length()>0?pendenciaDt.getClassificador():"Sem classificador")%> </th>
									</tr>
									<%
									}
									
									%>
					   				<tr class="TabelaLinha<%=(boLinha?1:2)%>"> 
										<td align="center">
											<input class="formEdicaoCheckBox" name="pendencias" type="checkbox" value="<%=pendenciaDt.getId()%>">
										</td>
										<% 
											boolean boUrgente = pendenciaDt.isProcessoPrioridade();
										%>
										
	                   					<td align="center">
	                   						<a href="BuscaProcesso?Id_Processo=<%=processoDt.getId_Processo()%>" title="<%=(boUrgente?pendenciaDt.getProcessoPrioridadeCodigoTexto():"")%>">
		                   						<%	if (boUrgente){ %>		 
		                   						<img src='./imagens/16x16/imgPrioridade<%=pendenciaDt.getNumeroImagemPrioridade()%>.png' alt="<%=pendenciaDt.getProcessoPrioridadeCodigoTexto()%>" title="<%=pendenciaDt.getProcessoPrioridadeCodigoTexto()%>"/>
		                   						<% } %>
		                   						<%=processoDt.getProcessoNumero()%>
	                   						</a>
					  					</td>
					  					<td><%= pendenciaDt.getDataInicio()%></td>
	                   		           	<td><%= pendenciaDt.getProcessoDt().getProcessoTipo()%></td>
	                   		           	<td aling="center">
			                   				<div id="opcoes" class="menuEspecial"> 
			                   					<ul> <li> Opções<ul>	  	            					
						  						<% 	if (request.getAttribute("podeAnalisar").toString().equalsIgnoreCase("true")){ %>
				                   					<li> <a href="#" onclick="javascript: submeter('AnalisarPendencia','<%=pendenciaDt.getId()%>','<%=Configuracao.Novo%>');return false;">Analisar</a></li>     			                   			
				                   				<%	}  %>
				                   				 <li> <a href="#" onclick="javascript: submeter('PreAnalisarPendencia', '<%=pendenciaDt.getId()%>','<%=Configuracao.Novo%>');return false;">Pré-Analisar</a></li>    			                   			
					                   			<% 	if (request.getAttribute("podeAnalisar").toString().equalsIgnoreCase("true")){ %>
						                   			<li> <a href="#" onclick="javascript: submeter('AnalisarPendencia', '<%=pendenciaDt.getId()%>','<%=Configuracao.Excluir%>');return false;">Descartar</a></li>     			                   			
				                   				<%	} %>
<%-- 				                   				<% 	if (request.getAttribute("podeGerarVotoEmenta") != null && request.getAttribute("podeGerarVotoEmenta").toString().equalsIgnoreCase("true")){ %>						                   					                   			 --%>
<%-- 				                   			   		<li><a href="#" onclick="javascript: submeter('AnalisarPendencia', '<%=pendenciaDt.getId()%>','<%=Configuracao.Curinga8%>');return false;">Voto e Ementa</a></li> --%>
<%-- 				                   				<%} %> --%>
				                   				<li> <a href="#" onclick="javascript: submeter('DescartarPendenciaProcesso','<%=pendenciaDt.getId()%>','<%=Configuracao.LocalizarDWR%>');return false;">Classificar</a></li>
			                   					</ul></li>
			                   					</ul>
			                   				</div>
			                   			</td>
					  	     		</tr>
									<%
								}								
							}
							%>
						</table>
						<div style="height:250px"> </div>
					</div> 
				</form>
			</div>
			<%@ include file="Padroes/Mensagens.jspf" %>  
		</div>
		<div style="margin:25px 0">&nbsp;</div>
	</body>
</html>
