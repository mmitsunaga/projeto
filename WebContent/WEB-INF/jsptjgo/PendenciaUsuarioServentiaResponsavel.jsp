<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>
<%@page import="br.gov.go.tj.projudi.dt.UsuarioServentiaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.PendenciaResponsavelDt"%>

<jsp:useBean id="PendenciaResponsaveldt" class= "br.gov.go.tj.projudi.dt.PendenciaResponsavelDt" scope="session"/>
<jsp:useBean id="Pendenciadt" class= "br.gov.go.tj.projudi.dt.PendenciaDt" scope="session"/>

<html>
<head>
	<title>Troca de Responsável da Intimação</title>	
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
			<div class="area"><h2>&raquo; Trocar Responsável da Intimação/Citação</h2></div>
		
			<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />				
				<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />	
						
				<div id="divEditar" class="divEditar">
					<fieldset class="formEdicao">
						<legend>Trocar Responsável da Pendência</legend>
						<fieldset class="formEdicao">
							<legend>Intimação: <%=Pendenciadt.getId()%>;   N&uacute;mero Processo:  <a href="BuscaProcessoUsuarioExterno?Id_Processo=<%=Pendenciadt.getId_Processo()%>&PassoBusca=2"><%=Pendenciadt.getProcessoNumero()%></a> </legend>
							<table class="Tabela" id="TabelaArquivos">
								<thead>
									<tr>
										<th></th>
										<th>Procurado(res) no Processo</th>
										<th width="30%">Usuário</th>
										<th> Parte Processo</th>
										<th></th>
									</tr>
								</thead>							
								<tbody>
									<%
										List liProcuradores = Pendenciadt.getResponsaveis();
										PendenciaResponsavelDt objTempProcurador;
										String stTempNome="";
										for(int f = 0 ; liProcuradores!=null && f< liProcuradores.size();f++) {
											objTempProcurador = (PendenciaResponsavelDt)liProcuradores.get(f); %>
											<%if (stTempNome.equalsIgnoreCase("")) { stTempNome="a";%> 
							                   	<tr class="TabelaLinha1"> 
											<%}else{ stTempNome=""; %>    
							                   	<tr class="TabelaLinha2">
											<%}%>
							                   		<td> <%=f+1%></td>
						                    	<td>
						                    		<%= objTempProcurador.getNomeUsuarioResponsavel()%>
						                    	</td>
						                   		<td align="center">
						                   			<%= objTempProcurador.getUsuarioResponsavel()%>
						                   		</td>
						                   		<td>
						                   			<%= objTempProcurador.getCodigoTemp()%>
						                   		</td>
						                   		<%  boolean checked = false;
						                   			if (request.getSession().getAttribute("id_UsuarioServentiaAtual") != null 
						                   					&& request.getSession().getAttribute("id_UsuarioServentiaAtual").toString().equals(objTempProcurador.getId_UsuarioResponsavel())){ 
						                   				checked = true;
						                   			}
						                   		%> 
						                   		<td align="center">
													<input type="radio" <%=checked?"checked":"" %> name="id_UsuarioServentiaAtual" value="<%=objTempProcurador.getId_UsuarioResponsavel()%>" />
												</td>
						                   		</tr>
											<%}%>
								</tbody>
							</table>		
						</fieldset>
						<%if(request.getSession().getAttribute("TrocouResponsavel") == null || 
							 request.getSession().getAttribute("TrocouResponsavel").toString().trim().toUpperCase() != "S"){%>
							<br />
							<fieldset class="formEdicao">
								<legend>Novo Responsável</legend>
							
								<label class="formEdicaoLabel" for="Id_UsuarioServentia">*Novo Responsável 
				    			<input class="FormEdicaoimgLocalizar" id="imaLocalizarId_UsuarioServentia" name="imaLocalizarId_ServentiaCargo" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(UsuarioServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" >  
				    			</label><br> 
				    			<input class="formEdicaoInputSomenteLeitura"  readonly name="UsuarioServentia" id="UsuarioServentia" type="text" size="80" maxlength="100" value="<%=PendenciaResponsaveldt.getUsuarioResponsavel()%>"/><br />
				    		</fieldset>
					    		
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