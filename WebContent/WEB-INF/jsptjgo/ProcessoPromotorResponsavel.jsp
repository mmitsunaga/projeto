<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaCargoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.UsuarioDt"%>
<%@page import="br.gov.go.tj.projudi.dt.GrupoDt"%>

<jsp:useBean id="ProcessoResponsaveldt" class= "br.gov.go.tj.projudi.dt.ProcessoResponsavelDt" scope="session"/>

<html>
<head>
	<title>Troca de MP Responsável do Processo</title>	
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
			<div class="area"><h2>&raquo; Trocar MP Responsável de Processo</h2></div>
		
			<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />				
				<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
				<input name="idUsuServTemp"  id="idUsuServTemp" type="hidden" value="<%=request.getSession().getAttribute("idUsuServTemp")%>" />	
				<input name="idProcessoTemp"  id="idProcessoTemp" type="hidden" value="<%=request.getSession().getAttribute("idProcessoTemp")%>" />
				<input name="idServentiaCargoTemp"  id="idServentiaCargoTemp" type="hidden" value="<%=request.getSession().getAttribute("idServentiaCargoTemp") %>" />		
				<input name="idServSubtipoTemp"  id="idServSubtipoTemp" type="hidden" value="<%=request.getSession().getAttribute("idServSubtipoTemp")%>" />
						
						
				<div id="divEditar" class="divEditar">
					<fieldset class="formEdicao">
						<legend>Trocar MP Responsável de Processo</legend>
						
						<%
							List processos = ProcessoResponsaveldt.getListaProcessos();
						
							List<ServentiaCargoDt> promotores = (ArrayList<ServentiaCargoDt>) request.getSession().getAttribute("listaPromotoresResponsaveis");
						%>

						<fieldset>
							<legend>Processos </legend>
							<table class="Tabela" id="TabelaArquivos">
							
								<thead>
									<tr>
										<th>N&uacute;mero Processo</th>
										<th colspan="2">Promotor Responsável Atual</th>
										<th width="30%">Serventia</th>
										<th>Desabilitar/Habilitar</th>
										<th>Trocar</th>
										<th></th>
									</tr>
								</thead>
								<%		
								if (processos != null){
									if(!(promotores.size() == 0)){
									
									
								for (int i=0; i<processos.size(); i++){
									ProcessoDt processoDt = (ProcessoDt)processos.get(i);
								%>
								<tbody>
									<%	
									
										
										
										
										
									
										for(int j = 0; j < promotores.size(); j++){
										
									
									
										
								%>
									
								
									<tr class="primeiraLinha">
										<td width="15%" align="center">
											<a href="BuscaProcesso?Id_Processo=<%=processoDt.getId_Processo()%>"><%=processoDt.getProcessoNumero()%></a>
										</td>
										<td><%=(promotores != null? promotores.get(j).getServentiaCargo():"")%></td>
										<td><%=(promotores != null? promotores.get(j).getNomeUsuario():"")%></td>										
										<td><%=(promotores != null? promotores.get(j).getServentia():"")%></td>
										<td align="center">

											<%
											
											
											if (promotores.get(j).getCodigoTemp().equals("0")){ %>
													<a onclick="AlterarValue('id_usu_serv',<%=promotores.get(j).getId_UsuarioServentia()%>);
													AlterarValue('idProcessoTemp',<%=processoDt.getId()%>);
													AlterarValue('idServentiaCargoTemp',<%=promotores.get(j).getId()%>);
													AlterarValue('idServSubtipoTemp',<%=promotores.get(j).getId_ServentiaSubtipo()%>);
													 AlterarValue('PaginaAtual',<%=Configuracao.Excluir%>); $('#Formulario').submit()"  title="Desabilitar Promotor Responsável no Processo" >	Desabilitar</a>									
				      						<% } else{ %>


											<a value="Habilitar" onclick="AlterarValue('id_usu_serv',<%=promotores.get(j).getId_UsuarioServentia()%>);
													AlterarValue('idProcessoTemp',<%=processoDt.getId()%>);
													AlterarValue('idServentiaCargoTemp',<%=promotores.get(j).getId()%>);
													AlterarValue('idServSubtipoTemp',<%=promotores.get(j).getId_ServentiaSubtipo()%>);
													 AlterarValue('PaginaAtual',<%=Configuracao.Atualizar%>); $('#Formulario').submit()"  title="Habilitar Promotor Responsável no Processo" > Habilitar</a>

											<% } %>
										
										
										</td>
										<td align="center">
											<input type="radio" name="trocaResponsavel" onclick="
												AlterarValue('idUsuServTemp',<%=promotores.get(j).getId_UsuarioServentia()%>);
												AlterarValue('idProcessoTemp',<%=processoDt.getId()%>);
												AlterarValue('idServentiaCargoTemp',<%=promotores.get(j).getId()%>);
												AlterarValue('idServSubtipoTemp',<%=promotores.get(j).getId_ServentiaSubtipo()%>);"> 
										</td>
										
										<% if (processos.size() > 1){ %>
				      					<td>
				      						<a href="<%=request.getAttribute("tempRetorno")%>?PaginaAtual=<%=Configuracao.LocalizarAutoPai%>&Id_Processo=<%=processoDt.getId()%>&posicaoLista=<%=i%>">
				      							<img name="btnRetirar" id="btnRetirar" title="Retirar esse processo" src="./imagens/imgExcluirPequena.png" />
				      						</a>
				      					</td>
				      					<% } %>
									</tr>
									
								<% }
								  %>
								</tbody>
								<% 	}
								
									}else{										
										out.println("<td><em>Não há promotor habilitado no processo.</em></td>");										
									}
									
									
								} else { %>
								<tbody>
									<tr>
										<td><em>Selecione processo(s) para Troca de MP Responsável.</em></td>			  	  							    
								    </tr>
							  	</tbody>								    
								<% } %>
							</table>		
						</fieldset>	
						<br />
						<fieldset>
							<legend>Novo MP Responsável</legend>
							
							<input name="Id_Serventia"  id="Id_Serventia"  type="hidden"  value="<%=request.getAttribute("Id_Serventia")%>"/>						
							<label class="formEdicaoLabel" for="Id_Serventia">*Serventia 
							<input class="FormEdicaoimgLocalizar" id="imaLocalizarServentia" name="imaLocalizarServentia" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>');" >							 
							</label><br> 
							<input  class="formEdicaoInputSomenteLeitura"  readonly="readonly" name="Serventia" id="Serventia" type="text" size="60" maxlength="60" value="<%=request.getAttribute("Serventia")%>"/>
							<label for="Aviso" style="float:left;margin-left:25px;" ><small>Selecione a Serventia para ver os Cargos disponíveis.</small></label><br> <br />							
						
							<label class="formEdicaoLabel" for="Id_ServentiaCargo">*Novo MP Responsável 
			    			<input class="FormEdicaoimgLocalizar" id="imaLocalizarId_ServentiaCargo" name="imaLocalizarId_ServentiaCargo" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ServentiaCargoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" >  
			    			</label><br> 
			    			
			    			
				    	<%if(request.getAttribute("ServentiaCargoUsuario") != null){ %>
							<input class="formEdicaoInputSomenteLeitura"  readonly name="ServentiaCargo" id="ServentiaCargo" type="text" size="80" maxlength="100" value="<%=ProcessoResponsaveldt.getServentiaCargo()%> - <%=request.getAttribute("ServentiaCargoUsuario")%>"/>
						<% } else {%>
							<input class="formEdicaoInputSomenteLeitura"  readonly name="ServentiaCargo" id="ServentiaCargo" type="text" size="80" maxlength="100" value="<%=ProcessoResponsaveldt.getServentiaCargo()%>"/>
						<% } %><br />
			    		</fieldset>
				    		
						<div id="divBotoesCentralizados" class="divBotoesCentralizados">
							<input name="imgConcluir" type="submit" value="Concluir" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Salvar%>');"> 
						</div>
					</fieldset>
					
					
					 	<%if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Atualizar))) {%> 	  
				    	<div id="divConfirmarAtualizar" class="ConfirmarExcluir">
							<button name="imgPreviaCalculo" value="Confirmar Excluir" onclick="Ocultar('divConfirmarAtualizar');AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Curinga6)%>')">
				               	Confirmar Atualizar
				            </button> <br />
				        	<% if (request.getAttribute("Mensagem") != null && !request.getAttribute("Mensagem").equals("")) { %>
								<div class="divMensagemexcluir"><%=request.getAttribute("Mensagem")%></div>
							<% } else { %>        	        	 
				           	<div class="divMensagemexcluir">Clique para confirmar a alteração dos dados </div>
				           	<% }%> 
				           	
				      	</div>
				 	<%}%>
									
					
					
					<%@ include file="Padroes/ConfirmarOperacao.jspf"%>
				</div>
			</form>
			<%@ include file="Padroes/Mensagens.jspf" %>
 		</div>
	</body>
</html>