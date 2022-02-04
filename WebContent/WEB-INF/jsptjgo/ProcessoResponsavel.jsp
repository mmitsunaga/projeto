<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaCargoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.UsuarioDt"%>
<%@page import="br.gov.go.tj.projudi.dt.GrupoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaTipoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaSubtipoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.UsuarioDt"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>

<jsp:useBean id="ProcessoResponsaveldt" class= "br.gov.go.tj.projudi.dt.ProcessoResponsavelDt" scope="session"/>
<jsp:useBean id="ProcessoResponsaveldtRevisor" class= "br.gov.go.tj.projudi.dt.ProcessoResponsavelDt" scope="session"/>
<jsp:useBean id="ProcessoResponsaveldtVogal" class= "br.gov.go.tj.projudi.dt.ProcessoResponsavelDt" scope="session"/>
<jsp:useBean id="UsuarioSessao" scope="session" class= "br.gov.go.tj.projudi.ne.UsuarioNe"/>

<html>
<head>
	<title>Troca de Responsável do Processo</title>	
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
			<div class="area"><h2>&raquo; Definição/Trocar Responsável de Processo</h2></div>
		
			<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input id="PassoEditar" name="PassoEditar" type="hidden" value="<%=request.getAttribute("PassoEditar")%>">
				<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />				
				<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />	
						
				<div id="divEditar" class="divEditar">
					<fieldset class="formEdicao">
						<legend>Definição/Trocar Responsável de Processo</legend>
						
						<%
							List processos = ProcessoResponsaveldt.getListaProcessos();
						%>

						<fieldset class="formEdicao">
							<legend>Processos </legend>
							<table class="Tabela" id="TabelaArquivos">
							<%
								boolean ehCamaraSegundoGrau = (request.getSession().getAttribute("UsuarioSessaoDt") != null) && ((UsuarioDt)request.getSession().getAttribute("UsuarioSessaoDt")).isSegundoGrau();
							%>
								<thead>
									<tr>
										<th></th>
										<th>N&uacute;mero Processo</th>
										<%if(ehCamaraSegundoGrau){ %>
											<th>Cargo Responsável Atual</th>
											<th>Nome Responsável Atual</th>											
										<%}else{ %>										
											<th colspan="2">Responsável Atual</th>										
										<%}%>
										<th width="30%">Serventia</th>										
										<th></th>
									</tr>									
								</thead>
								<%		
								String rowspan = "1";
								if(ehCamaraSegundoGrau) rowspan = "3";
								boolean boLinha=false;
								if (processos != null){
								for (int i=0;i<processos.size();i++){
									ProcessoDt processoDt = (ProcessoDt)processos.get(i);									
								%>
								<tbody>
									<tr class="TabelaLinha<%=(boLinha?1:2)%>">									
										<td align="center" rowspan="<%=rowspan%>"><%=i+1%></td>
										<td width="15%" rowspan="<%=rowspan%>" align="center">
											<a href="BuscaProcesso?Id_Processo=<%=processoDt.getId_Processo()%>"><%=processoDt.getProcessoNumero()%></a>
										</td>
										<%if(ehCamaraSegundoGrau){ %>
											<td><%=(processoDt.getServentiaCargoResponsavelDt() != null?processoDt.getServentiaCargoResponsavelDt().getCargoTipo():"")%></td>																														
										<%}else{%>
											<td><%=(processoDt.getServentiaCargoResponsavelDt() != null?processoDt.getServentiaCargoResponsavelDt().getServentiaCargo():"")%></td>											
										<%}%>				
										<td><%=(processoDt.getServentiaCargoResponsavelDt() != null?processoDt.getServentiaCargoResponsavelDt().getNomeUsuario():"")%></td>									
										<%
											if ( ehCamaraSegundoGrau && 
												(processoDt.getServentiaCargoResponsavelDt() != null) &&
												(processoDt.getServentiaCargoResponsavelDt().getServentia() != null) &&
												(processoDt.getServentiaCargoResponsavelDt().getServentia().trim().length() > 0)){%>												
												<td><%=processoDt.getServentiaCargoResponsavelDt().getServentia()%></td>												
										<%}else{%>										
											<td><%=processoDt.getServentia()%></td>												
										<%}%>	
										
										<% if (processos.size() > 1){ %>
				      					<td rowspan="<%=rowspan%>">
				      						<a href="<%=request.getAttribute("tempRetorno")%>?PaginaAtual=<%=Configuracao.LocalizarAutoPai%>&Id_Processo=<%=processoDt.getId()%>&posicaoLista=<%=i%>">
				      						<img name="btnRetirar" id="btnRetirar" title="Retirar esse processo" src="./imagens/imgExcluirPequena.png" />
				      						</a>
				      					</td>
				      					<% } %>
									</tr>
									<%if(ehCamaraSegundoGrau){ %>
										<tr class="TabelaLinha<%=(boLinha?1:2)%>">	
			      							<td><%=(processoDt.getServentiaCargoRevisorResponsavelDt() != null?processoDt.getServentiaCargoRevisorResponsavelDt().getCargoTipo():"")%></td>			
											<td><%=(processoDt.getServentiaCargoRevisorResponsavelDt() != null?processoDt.getServentiaCargoRevisorResponsavelDt().getNomeUsuario():"")%></td>									
											<%
												if ((processoDt.getServentiaCargoRevisorResponsavelDt() != null) &&
													(processoDt.getServentiaCargoRevisorResponsavelDt().getServentia() != null) &&
													(processoDt.getServentiaCargoRevisorResponsavelDt().getServentia().trim().length() > 0)){%>												
													<td><%=processoDt.getServentiaCargoRevisorResponsavelDt().getServentia()%></td>												
											<%}else{%>										
												<td><%=processoDt.getServentia()%></td>												
											<%}%>
			      						</tr>
			      						<tr class="TabelaLinha<%=(boLinha?1:2)%>">	
			      							<td><%=(processoDt.getServentiaCargoVogalResponsavelDt() != null?processoDt.getServentiaCargoVogalResponsavelDt().getCargoTipo():"")%></td>			
											<td><%=(processoDt.getServentiaCargoVogalResponsavelDt() != null?processoDt.getServentiaCargoVogalResponsavelDt().getNomeUsuario():"")%></td>									
											<%
												if ((processoDt.getServentiaCargoVogalResponsavelDt() != null) &&
													(processoDt.getServentiaCargoVogalResponsavelDt().getServentia() != null) &&
													(processoDt.getServentiaCargoVogalResponsavelDt().getServentia().trim().length() > 0)){%>												
													<td><%=processoDt.getServentiaCargoVogalResponsavelDt().getServentia()%></td>												
											<%}else{%>										
												<td><%=processoDt.getServentia()%></td>												
											<%}%>
			      						</tr>
			      					<%
			      						boLinha = !boLinha;
			      					  }
			      					%>
								</tbody>
								<% 	}
								} else { %>
								<tbody>
									<tr>
										<td><em>Selecione processo(s) para Troca de Responsável.</em></td>			  	  							    
								    </tr>
							  	</tbody>								    
								<% } %>
							</table>		
						</fieldset>	
						
						<% if(request.getAttribute("EhParaExibirSemAssistente") != null && String.valueOf(request.getAttribute("EhParaExibirSemAssistente")).equalsIgnoreCase("S")){%>								
								<label class="formEdicaoLabel" for="ProcessoSemAssistente"> 
		    						<input class="formEdicaoInput" name="ProcessoSemAssistente" id="ProcessoSemAssistente"  type="checkbox"  value="true" <% if(request.getAttribute("ProcessoSemAssistente") != null && String.valueOf(request.getAttribute("ProcessoSemAssistente")).equalsIgnoreCase("S")){%>  checked<%}%> onchange="processaExibicaoNovoReponsavel(this)" /> 
								Sem Assistente</label>		    								    				
			    			<%}%>
			    		<br />	
						<fieldset id="idNovoResponsavel" class="formEdicao">
							<legend>								
								Novo Responsável																
							</legend>
							
							<input name="Id_Serventia"  id="Id_Serventia"  type="hidden"  value="<%=request.getAttribute("Id_Serventia")%>"/>							
							<%if(UsuarioSessao.isMp()) {%>
								<label class="formEdicaoLabel" for="Id_Serventia">*Serventia 
								<input class="FormEdicaoimgLocalizar" id="imaLocalizarServentia" name="imaLocalizarServentia" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>');" >							 
								</label><br> 
								<input  class="formEdicaoInputSomenteLeitura"  readonly="readonly" name="Serventia" id="Serventia" type="text" size="60" maxlength="60" value="<%=request.getAttribute("Serventia")%>"/>
								<label for="Aviso" style="float:left;margin-left:25px;" ><small>Selecione a Serventia para ver os Cargos disponíveis.</small></label><br> <br />
							<%}%>								
						
							<label class="formEdicaoLabel" for="Id_ServentiaCargo">
								<%if(ehCamaraSegundoGrau){%>
									*Relator
								<%}else{ %>
									*Novo Responsável
								<%}%>
							 
			    			<input class="FormEdicaoimgLocalizar" id="imaLocalizarId_ServentiaCargo" name="imaLocalizarId_ServentiaCargo" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ServentiaCargoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>');AlterarValue('PassoEditar','0');" >  
			    			</label><br> 
			    			
				    	<%if(request.getAttribute("ServentiaCargoUsuario") != null){ %>
							<input class="formEdicaoInputSomenteLeitura"  readonly name="ServentiaCargo" id="ServentiaCargo" type="text" size="80" maxlength="100" value="<%=ProcessoResponsaveldt.getServentiaCargo()%> - <%=request.getAttribute("ServentiaCargoUsuario")%>"/>
						<% } else {%>
							<input class="formEdicaoInputSomenteLeitura"  readonly name="ServentiaCargo" id="ServentiaCargo" type="text" size="80" maxlength="100" value="<%=ProcessoResponsaveldt.getServentiaCargo()%>"/>
						<% } %>
			    			
			    			<br />
			    			
			    			<%if(ehCamaraSegundoGrau){%>		
					    		<label class="formEdicaoLabel">Revisor
					    						    		
					    		<input class="FormEdicaoimgLocalizar" id="imaLocalizarId_ServentiaCargoRevisor" name="imaLocalizarId_ServentiaCargoRevisor" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ServentiaCargoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>');AlterarValue('PassoEditar','1');" >
					    		<input class="FormEdicaoimgLocalizar" id="imaLimparServentiaCargoRevisor" name="imaLimparServentiaCargoRevisor" type="image"  src="./imagens/16x16/edit-clear.png"  onclick="LimparChaveEstrangeira('Id_ServentiaCargoRevisor','ServentiaCargoRevisor'); return false;" />
					    		</label><br>
					    		<input id="Id_ServentiaCargoRevisor" name="Id_ServentiaCargoRevisor" type="hidden" value="<%=ProcessoResponsaveldtRevisor.getId_ServentiaCargo()%>"/>
					    		<input class="formEdicaoInputSomenteLeitura"  readonly name="ServentiaCargoRevisor" id="ServentiaCargoRevisor" type="text" size="80" maxlength="100" value="<%=ProcessoResponsaveldtRevisor.getServentiaCargo()%>"/><br />	    		
					    		
								<label class="formEdicaoLabel">Vogal  
				    			<input class="FormEdicaoimgLocalizar" id="imaLocalizarId_ServentiaCargoVogal" name="imaLocalizarId_ServentiaCargoVogal" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ServentiaCargoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>');AlterarValue('PassoEditar','2');" >
				    			<input class="FormEdicaoimgLocalizar" id="imaLimparServentiaCargoVogal" name="imaLimparServentiaCargoVogal" type="image"  src="./imagens/16x16/edit-clear.png"  onclick="LimparChaveEstrangeira('Id_ServentiaCargoVogal','ServentiaCargoVogal'); return false;" />
				    			</label><br>
				    			<input id="Id_ServentiaCargoVogal" name="Id_ServentiaCargoVogal" type="hidden" value="<%=ProcessoResponsaveldtVogal.getId_ServentiaCargo()%>"/>  
				    			<input class="formEdicaoInputSomenteLeitura"  readonly name="ServentiaCargoVogal" id="ServentiaCargoVogal" type="text" size="80" maxlength="100" value="<%=ProcessoResponsaveldtVogal.getServentiaCargo()%>"/><br />				    		
			    			<%}%>
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