<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.AudienciaProcessoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaCargoDt"%>

<jsp:useBean id="AudienciaProcessoDt" class= "br.gov.go.tj.projudi.dt.AudienciaProcessoDt" scope="session"/>

<html>
<head>
	<title>Troca de Respons�vel da Audi�ncia</title>	
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
	      
</head>

	<body>
		<div id="divCorpo" class="divCorpo">
			<div class="area"><h2>&raquo; Trocar Respons�vel de Audi�ncia</h2></div>
		
			<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />				
				<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />	
						
				<div id="divEditar" class="divEditar">
					<fieldset class="formEdicao">
						<legend>Trocar Respons�vel de Audi�ncia</legend>
						
						<%
							List audienciaProcessos = (List) request.getSession().getAttribute("ListaAudienciaProcessos");
						%>

						<fieldset>
							<legend>Audi�ncias </legend>
							<table class="Tabela" id="TabelaArquivos">
								<thead>
									<tr>
										<th></th>
										<th width="14%">Data Audi�ncia</th>
										<th>N&uacute;mero Processo</th>
										<th>Cargo</th>
										<th>Nome Respons�vel</th>
										<th></th>
									</tr>
								</thead>
								<%		
								if (audienciaProcessos != null){
								for (int i=0; i < audienciaProcessos.size(); i++){
									AudienciaProcessoDt audienciaProcessoDt = (AudienciaProcessoDt)audienciaProcessos.get(i);
								%>
								<tbody>
									<tr class="primeiraLinha">
										<td align="center"><%=i+1%></td>
										<td><%=audienciaProcessoDt.getAudienciaDt().getDataAgendada()%></td>
										<td width="15%" align="center">
											<a href="BuscaProcesso?Id_Processo=<%=audienciaProcessoDt.getId_Processo()%>"><%=audienciaProcessoDt.getProcessoNumero()%></a>
										</td>
										<td><%=audienciaProcessoDt.getServentiaCargo()%></td>
										<td><%=audienciaProcessoDt.getNomeResponsavel()%></td>
										<% if (audienciaProcessos.size() > 1){ %>
				      					<td>
				      						<a href="<%=request.getAttribute("tempRetorno")%>?PaginaAtual=<%=Configuracao.LocalizarAutoPai%>&Id_Processo=<%=audienciaProcessoDt.getId()%>&posicaoLista=<%=i%>">
				      						<img name="btnRetirar" id="btnRetirar" title="Retirar esse processo" src="./imagens/imgExcluirPequena.png" />
				      						</a>
				      					</td>
				      					<% } %>
									</tr>
								</tbody>
								<% 	}
								} else { %>
								<tbody>
									<tr>
										<td><em>Selecione Audi�ncia(s) para Troca de Respons�vel.</em></td>			  	  							    
								    </tr>
							  	</tbody>								    
								<% } %>
							</table>		
						</fieldset>	
						<br />
						<fieldset>
							<legend>Novo Respons�vel</legend>
						
							<label class="formEdicaoLabel" for="Id_ServentiaCargo">*Novo Respons�vel
							<input class="FormEdicaoimgLocalizar" id="imaLocalizarId_ServentiaCargo" name="imaLocalizarId_ServentiaCargo" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ServentiaCargoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" >  
							</label><br>  
			    			
			    			<%if(request.getAttribute("ServentiaCargoUsuario") != null){ %>
							<input class="formEdicaoInputSomenteLeitura"  readonly name="ServentiaCargo" id="ServentiaCargo" type="text" size="80" maxlength="100" value="<%=AudienciaProcessoDt.getServentiaCargo()%> - <%=request.getAttribute("ServentiaCargoUsuario")%>"/>
						<% } else {%>							
							<input class="formEdicaoInputSomenteLeitura"  readonly name="ServentiaCargo" id="ServentiaCargo" type="text" size="80" maxlength="100" value="<%=AudienciaProcessoDt.getServentiaCargo()%>"/>
						<% } %>
			    			
			    		</fieldset>
				    		
						<div id="divBotoesCentralizados" class="divBotoesCentralizados">
							<input name="imgConcluir" type="submit" value="Concluir" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Salvar%>');"> 
						</div>
					</fieldset>
					
					<%@ include file="Padroes/ConfirmarOperacao.jspf"%>
				</div>
			</form>
			<%@ include file="Padroes/Mensagens.jspf" %>
 		</div>
	</body>
</html>