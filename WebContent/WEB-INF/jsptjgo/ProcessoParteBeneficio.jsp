<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoBeneficioDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteDt"%>

<jsp:useBean id="ProcessoParteBeneficiodt" scope="session" class= "br.gov.go.tj.projudi.dt.ProcessoParteBeneficioDt"/>


<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteBeneficioDt"%><html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Busca de Benefícios  </title>
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type='text/javascript' src='./js/jquery.js'></script>
   	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
	<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js" ></script>
	<script type='text/javascript' src='./js/Digitacao/DigitarNumeroProcesso.js'></script>
	<link type="text/css" rel="stylesheet" href="js/jscalendar/dhtmlgoodies_calendar.css?random=20051112" media="screen"></link>
	<script type="text/javascript" src="./js/jscalendar/dhtmlgoodies_calendar.js?random=20060118"></script>
	<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>
</head>

<body>
	<div id="divCorpo" class="divCorpo">
		<div class="area"><h2>&raquo; Consulta de Benefícios</h2></div>
		<form action="ProcessoParteBeneficio" method="post" name="Formulario" id="Formulario">
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
			<input id="Id_ProcessoParteBeneficio" name="Id_ProcessoParteBeneficio" type="hidden" value="<%=ProcessoParteBeneficiodt.getId()%>" />
			
			<div id="divPortaBotoes" class="divPortaBotoes">				  
				<input id="imgLocalizar" alt="Localizar" class="imgLocalizar" title="Localizar - Localiza um registro no banco" name="imaLocalizar" type="image"  src="./imagens/imgLocalizar.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Localizar)%>')" /> 
				<a class="divPortaBotoesLink" href="Ajuda/ProcessoParteBeneficioAjuda.html" target="_blank">  <img id="imgAjuda" class="imgAjuda" title="Ajuda" src="./imagens/imgAjuda.png" /> </a>
			</div>
			<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao"  > 
					<legend class="formEdicaoLegenda">Cadastro de Benefícios </legend>
					
					<% if (ProcessoParteBeneficiodt.getId_Processo().length() != 0){ %>
					
					<label class="formEdicaoLabel" for="ProcessoNumero">Número do Processo</label><br> 
					<span class="destaque"><a href="BuscaProcesso?Id_Processo=<%=ProcessoParteBeneficiodt.getId_Processo()%>"><%=ProcessoParteBeneficiodt.getProcessoNumero()%></a></span>
					
					<fieldset class="formEdicao">
						<legend> *Polo Ativo </legend>
						<%
						List listaPartes = ProcessoParteBeneficiodt.getListaPartesPromoventes();
 	    				for (int i=0;i < listaPartes.size();i++){
	  			  			ProcessoParteDt parteDt = (ProcessoParteDt)listaPartes.get(i);
	 					%>
			   				<div>
				   				<input name="partesBeneficio" type="checkbox" value="<%=parteDt.getId_ProcessoParte()%>" 
				   				<%	
				   					String[] partesBeneficio = ProcessoParteBeneficiodt.getPartesBeneficio();
									if (partesBeneficio != null && partesBeneficio.length >0){
						            	for(int j = 0 ; j< partesBeneficio.length;j++) {
						            		String id_ProcessoParte = (String) partesBeneficio[j];
						                   	if (id_ProcessoParte.equals(parteDt.getId_ProcessoParte())){%> 
						                   	checked
						        <% 			}
						               	}
									}
								%>
								 />
								 <%=parteDt.getNome() %>
			   				</div> 
						<%	
 	    				}
						%>
					</fieldset>
					
					<fieldset class="formEdicao">
						<legend> *Polo Passivo </legend>
						<%
						listaPartes = ProcessoParteBeneficiodt.getListaPartesPromovidas();
 	    				for (int i=0;i < listaPartes.size();i++){
	  			  			ProcessoParteDt parteDt = (ProcessoParteDt)listaPartes.get(i);
	 					%>
			   				<div>
				   				<input name="partesBeneficio" type="checkbox" value="<%=parteDt.getId_ProcessoParte()%>"
				   				<%	
				   					String[] partesBeneficio = ProcessoParteBeneficiodt.getPartesBeneficio();
									if (partesBeneficio != null && partesBeneficio.length >0){
					            		for(int j = 0 ; j< partesBeneficio.length;j++) {
					            		String id_ProcessoParte = (String) partesBeneficio[j];
						                   	if (id_ProcessoParte.equals(parteDt.getId_ProcessoParte())){%> 
						                   	checked
						        <% 			}
						               	}
									}
								%>
								 />
								 <%=parteDt.getNome() %>
			   				</div> 
						<%	
 	    				}
						%>
					</fieldset>
					<br />
					
					
					<label class="formEdicaoLabel" for="DataInicial">Data Inicial</label><br> 
					<input class="formEdicaoInputSomenteLeitura"  readonly="true" name="DataInicial" id="DataInicial"  type="text" size="10" maxlength="10" value="<%=ProcessoParteBeneficiodt.getDataInicial()%>">
					<img id="calendarioDataInicial" src="./imagens/dlcalendar_2.gif" height="13" width="13" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].DataInicial,'dd/mm/yyyy',this)" /><br />
					<div class="clear"></div>
					<label class="formEdicaoLabel" for="Id_ProcessoBeneficio">*Benefício  
					<input class="FormEdicaoimgLocalizar" id="imaLocalizarProcessoBeneficio" name="imaLocalizarProcessoBeneficio" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ProcessoBeneficioDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar  )%>')" > 					
					</label><br>
					<input class="formEdicaoInputSomenteLeitura"  readonly="true" name="ProcessoBeneficio" id="ProcessoBeneficio" type="text" size="60" maxlength="60" value="<%=ProcessoParteBeneficiodt.getProcessoBeneficio()%>">
					<% } %>
				</fieldset>
				
				<% if (ProcessoParteBeneficiodt.getListaPartesComBeneficio() != null && ProcessoParteBeneficiodt.getListaPartesComBeneficio().size() > 0){
			          	%> <br /> <br />
		          		<fieldset id="VisualizaDados" class="VisualizaDados">
			      			<legend>Débitos Cadastrados Para o Processo Selecionado </legend>
			    			<table id="Tabela" class="Tabela">
					        	<thead>
					           		<tr class="TituloColuna">
						           		<td width="5%" align="center">&nbsp;</td>
						               	<td width="2%" align="center">Id</td>
						               	<td width="15%" align="center">Benefício</td>
						               	<td width="15%" align="center">Data Início</td>
						               	<td width="45%" align="center">Parte</td>
						               	<td width="30%" align="center">CPF/CNPJ</td>
						               	
						               	<th>Excluir</th>
				                  		<th>Editar</th>
				                  		
					    	        </tr>
					           	</thead>
					          	<tbody>
				          		<%
				          		for (int i=0; i < ProcessoParteBeneficiodt.getListaPartesComBeneficio().size();i++){
				          			ProcessoParteBeneficioDt aux = (ProcessoParteBeneficioDt)ProcessoParteBeneficiodt.getListaPartesComBeneficio().get(i);
				          		%>
						      		<tr>
						      			<td><%=i+1%></td>
					          			<td align="center"><%=aux.getId()%></td>
					          			<td align="center"><%=aux.getProcessoBeneficio()%></td>
					          			<td align="center"><%=aux.getDataInicial()%></td>
					          			<td align="center"><%=aux.getNome()%></td>
					          			<td align="center"><%=aux.getCpfParte()%></td>
					          			
					          			<td class="Centralizado"><input name="formLocalizarimgEditar" type="image"  title="Excluir Benefício" src="./imagens/imgExcluirPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Excluir%>'); AlterarValue('Id_ProcessoParteBeneficio','<%=aux.getId()%>')"> </td>
							     		<td class="Centralizado"><input name="formLocalizarimgEditar" type="image"  title="Editar Benefício" src="./imagens/imgEditarPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga6%>');AlterarValue('Id_ProcessoParteBeneficio','<%=aux.getId()%>')"></td>
					          			
					          		</tr>
							     <% } %>
								</tbody>
							</table>
						</fieldset>
					
			 <% } %> 

			<%@ include file="Padroes/ConfirmarOperacao.jspf"%>

			</div>

		</form>
	</div>
	<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>