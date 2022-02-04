<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoResponsavelDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaCargoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.UsuarioDt"%>
<%@page import="br.gov.go.tj.projudi.dt.GrupoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaTipoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaSubtipoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.UsuarioDt"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>

<jsp:useBean id="ProcessoResponsaveldt" class= "br.gov.go.tj.projudi.dt.ProcessoResponsavelDt" scope="session"/>

<html>
<head>
	<title>Inclusão de Relator Inativo ao Processo</title>	
    <meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
	<style type="text/css">
		@import url('./css/Principal.css');
		@import url('./css/Paginacao.css');
	</style>
    
    	  
    <script type='text/javascript' src='./js/Funcoes.js'></script>
    <script type='text/javascript' src='./js/jquery.js'></script>
   	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>   
	<link rel='stylesheet' href='./css/jquery.tabs.css' type='text/css' media='print, projection, screen'>
	<script type="text/javascript" src="./js/Digitacao/AutoTab.js"></script>	
</head>

	<body>
		<div id="divCorpo" class="divCorpo">
			<div class="area"><h2>&raquo; Inclusão de Relator Desabilitado ao Processo</h2></div>
		
			<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input id="PassoEditar" name="PassoEditar" type="hidden" value="<%=request.getAttribute("PassoEditar")%>">
				<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />				
				<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />	
				<input name="idProcessoResponsavel"  id="idProcessoResponsavel" type="hidden" value="<%=request.getAttribute("idProcessoResponsavel")%>" />	
				<input name="habilitrDesabilitar"  id="habilitrDesabilitar" type="hidden" value="<%=request.getAttribute("habilitrDesabilitar")%>" />
						
				<div id="divEditar" class="divEditar">
					<fieldset class="formEdicao">
						<legend>Inclusão de Relator Desabilitado ao Processo</legend>
						
						<br />
				      	<div> Número Processo </div>
						<span><a href="BuscaProcesso?Id_Processo=<%=ProcessoResponsaveldt.getId_Processo()%>"><%=ProcessoResponsaveldt.getProcessoNumero()%></a></span/><br />
						<br />
						
						<fieldset id="idNovoResponsavel" class="formEdicao">
							<legend> Novo Relator Desabilitado </legend>
							<label class="formEdicaoLabel" for="Id_ServentiaCargo">*Relator 
			    			<input class="FormEdicaoimgLocalizar" id="imaLocalizarId_ServentiaCargo" name="imaLocalizarId_ServentiaCargo" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ServentiaCargoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>');" >  
							</label><br> 
							<input class="formEdicaoInputSomenteLeitura"  readonly name="ServentiaCargo" id="ServentiaCargo" type="text" size="120" maxlength="100" value="<%=ProcessoResponsaveldt.getServentiaCargo()%>"/>
			    			<input name="Id_ServentiaCargo" id="Id_ServentiaCargo" type="hidden" />
			    			<br />
			    		</fieldset>			    					    		
						<div id="divBotoesCentralizados" class="divBotoesCentralizados">
							<input name="imgConcluir" type="submit" value="Concluir" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Salvar%>');"> 
						</div>
					</fieldset>
					
					<br />
					<br />
					<br />
					
					<%
						List<ProcessoResponsavelDt> listaResponsaveisProcesso = (ArrayList<ProcessoResponsavelDt>) request.getSession().getAttribute("listaResponsaveisProcesso");
					%>
						
					<fieldset>
							<legend>Alterar status de responsáveis pelo processo</legend>
							<br />
							<table class="Tabela" id="TabelaArquivos">
							
								<thead>
									<tr>
										<th width="30%">Nome do Responsável</th>
										<th width="30%">Cargo</th>
										<th width="30%">Serventia</th>
										<th>Desabilitar/Habilitar</th>
									</tr>
								</thead>
								<%		
									if(listaResponsaveisProcesso.size() > 0){
								%>
								<tbody>
									<%	
										for(int j = 0; j < listaResponsaveisProcesso.size(); j++){
											ProcessoResponsavelDt responsavelDt = (ProcessoResponsavelDt) listaResponsaveisProcesso.get(j);
									%>
									<tr class="primeiraLinha">
										<td><%=responsavelDt.getNomeUsuario()%></td>
										<td><%=responsavelDt.getCargoTipo()%></td>										
										<td><%=responsavelDt.getServentiaCargo()%></td>
										<td align="center">
											<%
											if (responsavelDt.getCodigoTemp().equals("0")){ %>
												<a onclick="AlterarValue('idProcessoResponsavel',<%=responsavelDt.getId()%>);
													AlterarValue('habilitrDesabilitar',<%=ProcessoResponsavelDt.INATIVO%>);
													AlterarValue('PaginaAtual',<%=Configuracao.Salvar%>); $('#Formulario').submit()"  title="Desabilitar Responsável no Processo" >	Desabilitar</a>									
				      						<% } else{ %>
												<a onclick="AlterarValue('idProcessoResponsavel',<%=responsavelDt.getId()%>);
													AlterarValue('habilitrDesabilitar',<%=ProcessoResponsavelDt.ATIVO%>);
													AlterarValue('PaginaAtual',<%=Configuracao.Salvar%>); $('#Formulario').submit()"  title="Habilitar Responsável no Processo" > Habilitar</a>

											<% } %>
										</td>
									</tr>
								<% }
								  %>
								</tbody>
								<% 	}else{										
										out.println("<td><em>Não há responsáveis habilitados no processo.</em></td>");										
									}
								%>
							</table>
							<br />		
						</fieldset>	
					
					<%if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar))) {%> 	
				    	<div id="divConfirmarSalvar" class="ConfirmarSalvar">
							<button name="imgConfirmarSalvar" value="Confirmar Excluir" onclick="Ocultar('divConfirmarSalvar');AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.SalvarResultado)%>')">
				               	<img src="./imagens/imgSalvar.png" alt="Confirmar Salvar">
				               	Confirmar Salvar
				            </button> <br />
							<% if (request.getAttribute("Mensagem") != null && !request.getAttribute("Mensagem").equals("")) { %>
				        		<div class="divMensagemsalvar"><%=request.getAttribute("Mensagem")%></div>
				           	<% } else { %>
				           		<div class="divMensagemsalvar">Clique para confirmar os dados </div>
				           	<% }%> 
				      	</div>
				 	<%}%>
				</div>
			</form>
			<%@ include file="Padroes/Mensagens.jspf" %>
 		</div>
	</body>
</html>