<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaCargoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.GrupoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.PendenciaDt"%>

<jsp:useBean id="PendenciaResponsaveldt" class= "br.gov.go.tj.projudi.dt.PendenciaResponsavelDt" scope="session"/>
<jsp:useBean id="Pendenciadt" class= "br.gov.go.tj.projudi.dt.PendenciaDt" scope="session"/>
<jsp:useBean id="UsuarioSessao" class= "br.gov.go.tj.projudi.ne.UsuarioNe" scope="session"/>
<jsp:useBean id="ListaDePendenciasDt" class= "java.util.ArrayList<br.gov.go.tj.projudi.dt.PendenciaDt>" scope="session"/>

<html>
<head>
	<title>Troca de Responsável da Pendência</title>	
    <meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
	<style type="text/css">
		@import url('./css/Principal.css');
		@import url('./css/Paginacao.css');
	</style>
    
    	  
    <script type='text/javascript' src='./js/Funcoes.js'></script>
    <script type='text/javascript' src='./js/jquery.js'></script>
   	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>   
	<!--<script type="text/javascript" src="./js/ui/jquery.tabs.min.js"></script>-->
	<link rel='stylesheet' href='./css/jquery.tabs.css' type='text/css' media='print, projection, screen'>
	<script type="text/javascript" src="./js/Digitacao/AutoTab.js"></script>	
	<script type="text/javascript">
		function processaExibicaoNovoReponsavel(check){					
			if (check.checked){
				Ocultar('idNovoResponsavel');									
			}else{
				Mostrar('idNovoResponsavel');					
			}				
		}
	</script>
	<script type="text/javascript">
		//<![CDATA[
		onload = function()
		{				
			var check = document.getElementById('ProcessoSemAssistente');
			if(check) processaExibicaoNovoReponsavel(check);
		}
		//]]>
	</script>      
</head>

	<body>
		<div id="divCorpo" class="divCorpo">
			<div class="area"><h2>&raquo; Trocar Responsável da Pendência</h2></div>
		
			<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />				
				<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />	
						
				<div id="divEditar" class="divEditar">
					<fieldset class="formEdicao">
						<legend>Trocar Responsável da Pendência</legend>
						<fieldset class="formEdicao">
							<legend>Processos </legend>
							<table class="Tabela" id="TabelaArquivos">
								<thead>
									<tr>
										<th></th>
										<th width="30%">Pendência</th>
										<th width="20%">N&uacute;mero Processo</th>
										<th colspan="2">Responsável Atual</th>
										
									</tr>
								</thead>							
								<tbody>
								<%if (ListaDePendenciasDt != null && ListaDePendenciasDt.size() > 0) { 
									boolean boLinha=false;
									for (PendenciaDt pendenciaAtuaDt : ListaDePendenciasDt) {
									%>
										<tr class="TabelaLinha<%=(boLinha?1:2)%>">
										<td align="center"><%=pendenciaAtuaDt.getId()%></td>
										<td ><%=pendenciaAtuaDt.getPendencia()%></td>
										<td width="15%" align="center">
											<a href="BuscaProcesso?Id_Processo=<%=pendenciaAtuaDt.getId_Processo()%>"><%=pendenciaAtuaDt.getProcessoNumero()%></a>
										</td>
										<%if (pendenciaAtuaDt.getResponsavel() != null){%>
											<%if (pendenciaAtuaDt.getResponsavel().getServentiaCargo() != null
													&& pendenciaAtuaDt.getResponsavel().getServentiaCargo().length()>0) {%>
												<td><%if (pendenciaAtuaDt.getResponsavel() != null && pendenciaAtuaDt.getResponsavel().getServentiaCargo() != null){%><%=pendenciaAtuaDt.getResponsavel().getServentiaCargo()%><%}%></td>
											<td><%if (pendenciaAtuaDt.getResponsavel().getServentiaCargo() != null){%><%=pendenciaAtuaDt.getResponsavel().getNomeUsuarioServentiaCargo()%><%}%></td>
											<% } else if (pendenciaAtuaDt.getResponsavel().getId_UsuarioResponsavel() != null
													&& pendenciaAtuaDt.getResponsavel().getId_UsuarioResponsavel().length() > 0) {%>
												<td>MP-GO</td>
												<td><%=pendenciaAtuaDt.getResponsavel().getNomeUsuarioResponsavel()%></td>
											<%} else { %>
												<td colspan="2" align="center">-</td>
											<%} %>
										<%} else { %>
											<td colspan="2" align="center">-</td>
										<%} %>
										</tr>
									<%
									}
								%>
								
								<% } else { %>								
									<tr class="primeiraLinha">
										<td align="center"><%=Pendenciadt.getId()%></td>
										<td ><%=Pendenciadt.getPendencia()%></td>
										<td width="15%" align="center">
											<a href="BuscaProcesso?Id_Processo=<%=Pendenciadt.getId_Processo()%>"><%=Pendenciadt.getProcessoNumero()%></a>
										</td>
										<%if (Pendenciadt.getResponsavel() != null){%>
											<%if (Pendenciadt.getResponsavel().getServentiaCargo() != null
													&& Pendenciadt.getResponsavel().getServentiaCargo().length()>0) {%>
												<td><%if (Pendenciadt.getResponsavel() != null && Pendenciadt.getResponsavel().getServentiaCargo() != null){%><%=Pendenciadt.getResponsavel().getServentiaCargo()%><%}%></td>
												<td><%if (Pendenciadt.getResponsavel().getServentiaCargo() != null){%><%=Pendenciadt.getResponsavel().getNomeUsuarioServentiaCargo()%><%}%></td>
											<% } else if (Pendenciadt.getResponsavel().getId_UsuarioResponsavel() != null
													&& Pendenciadt.getResponsavel().getId_UsuarioResponsavel().length() > 0) {%>
												<td>MP-GO</td>
												<td><%=Pendenciadt.getResponsavel().getNomeUsuarioResponsavel()%></td>
											<%} else { %>
												<td colspan="2" align="center">-</td>
											<%} %>
										<%} else { %>
											<td colspan="2" align="center">-</td>
										<%} %>
									</tr>
									<% } %>
								</tbody>
							</table>		
						</fieldset>
						<%if(request.getSession().getAttribute("TrocouResponsavel") == null || 
						 request.getSession().getAttribute("TrocouResponsavel").toString().trim().toUpperCase() != "S"){%>
											
						<% if(request.getAttribute("EhParaExibirSemAssistente") != null && String.valueOf(request.getAttribute("EhParaExibirSemAssistente")).equalsIgnoreCase("S")){%>								
							<label class="formEdicaoLabel" for="ProcessoSemAssistente"> 
	    						<input class="formEdicaoInput" name="ProcessoSemAssistente" id="ProcessoSemAssistente"  type="checkbox"  value="true" <% if(request.getAttribute("ProcessoSemAssistente") != null && String.valueOf(request.getAttribute("ProcessoSemAssistente")).equalsIgnoreCase("S")){%>  checked<%}%> onchange="processaExibicaoNovoReponsavel(this)" />
	    						Sem Assistente
	    					</label> <br />			    				
		    			<%}%>
						<fieldset>
						
							<% if (UsuarioSessao.getUsuarioDt().getGrupoCodigo().equals(String.valueOf(GrupoDt.COORDENADOR_PROMOTORIA))){%>			
							
								<legend>Novo Responsável</legend>
					
									<input name="Id_Serventia"  id="Id_Serventia"  type="hidden"  value="<%=PendenciaResponsaveldt.getId_Serventia()%>"/>						
									<label class="formEdicaoLabel" for="Id_Serventia">*Serventia 
									<input class="FormEdicaoimgLocalizar" id="imaLocalizarServentia" name="imaLocalizarServentia" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>');" >							 
									</label><br> 
									<input  class="formEdicaoInputSomenteLeitura"  readonly="readonly" name="Serventia" id="Serventia" type="text" size="60" maxlength="60" value="<%=PendenciaResponsaveldt.getServentia()%>"/>
									<label for="Aviso" style="float:left;margin-left:25px;" ><small>Selecione a Serventia para ver os Cargos disponíveis.</small></label><br> <br />							
								
									<label class="formEdicaoLabel" for="Id_ServentiaCargo">*Novo Responsável 
					    			<input class="FormEdicaoimgLocalizar" id="imaLocalizarId_ServentiaCargo" name="imaLocalizarId_ServentiaCargo" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ServentiaCargoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" >  
					    			</label><br> 
					    			
					    			
						    	<%if(request.getAttribute("ServentiaCargoUsuario") != null){ %>
									<input class="formEdicaoInputSomenteLeitura"  readonly name="ServentiaCargo" id="ServentiaCargo" type="text" size="80" maxlength="100" value="<%=PendenciaResponsaveldt.getServentiaCargo()%> - <%=request.getAttribute("ServentiaCargoUsuario")%>"/>
								<% } else {%>
									<input class="formEdicaoInputSomenteLeitura"  readonly name="ServentiaCargo" id="ServentiaCargo" type="text" size="80" maxlength="100" value="<%=PendenciaResponsaveldt.getServentiaCargo()%>"/>
								<% } %><br />
					    		</fieldset>
												
			    			<%} else{%>
			    			
								<fieldset id="idNovoResponsavel" class="formEdicao">
									<legend>Novo Responsável</legend>					
								
									<label class="formEdicaoLabel" for="Id_ServentiaCargo">*Novo Responsável 
					    			<input class="FormEdicaoimgLocalizar" id="imaLocalizarId_ServentiaCargo" name="imaLocalizarId_ServentiaCargo" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ServentiaCargoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" >  
					    			
					    			</label><br> 
					    			
					    			<%if(request.getAttribute("ServentiaCargoUsuario") != null){ %>
						    			<input class="formEdicaoInputSomenteLeitura"  readonly name="ServentiaCargo" id="ServentiaCargo" type="text" size="80" maxlength="100" value="<%=PendenciaResponsaveldt.getServentiaCargo()%> - <%=request.getAttribute("ServentiaCargoUsuario")%>"/>
									<% } else {%>
										<input class="formEdicaoInputSomenteLeitura"  readonly name="ServentiaCargo" id="ServentiaCargo" type="text" size="80" maxlength="100" value="<%=PendenciaResponsaveldt.getServentiaCargo()%>"/>
									<% } %>
									<br />
						    			
					    		</fieldset>
			    			
						
							<%}%>

					    		
							<div id="divBotoesCentralizados" class="divBotoesCentralizados">
								<input name="imgConcluir" type="submit" value="Concluir" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Salvar%>');"> 
							</div>
						<%}%>
					</fieldset>
					
					<%@ include file="Padroes/ConfirmarOperacao.jspf"%>
				</div>
			</form>
			<%@ include file="Padroes/Mensagens.jspf" %>
 		</div>
	</body>
</html>