<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaCargoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaGrupoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.PendenciaResponsavelHistoricoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.PendenciaDt"%>

<jsp:useBean id="PendenciaResponsaveldt" class= "br.gov.go.tj.projudi.dt.PendenciaResponsavelDt" scope="session"/>
<html>
<head>
	<title>Distribuição de Conclusão por Unidade de Trabalho</title>	
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

		function serventiaCargoPresidente(check){		
			if (check.checked){
				Ocultar('divLocalizar');
				AlterarValue('conclusoMagistrado', 'true');			
			}else{
				Mostrar('divLocalizar');
				AlterarValue('conclusoMagistrado', 'false');	
			}				
		}

		function semRegraDistribuicao(check){
			if (check.checked){
				Mostrar('divLocalizar2');
				AlterarValue('semRegra', 'true');			
			}else{
				Ocultar('divLocalizar2');
				AlterarValue('semRegra', 'false');	
			}				
		}

	</script>
	
	<script type="text/javascript">
		//<![CDATA[
		onload = function()
		{				
			var check = document.getElementById('chkSubst');
			if(check) serventiaCargoPresidente(check);

			var check2 = document.getElementById('chkSemRegra');
			if(check2) semRegraDistribuicao(check2);
		}
		//]]>
	</script>         
</head>

	<body>
		<div id="divCorpo" class="divCorpo">
			<div class="area"><h2>&raquo; Distribuir Conclusão UPJ</h2></div>
		
			<form action="<%=request.getAttribute("tempRetorno")%>" method="post"  name="Formulario" id="Formulario">
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input id="conclusoMagistrado" name="conclusoMagistrado" type="hidden" value="<%=request.getAttribute("conclusoMagistrado")%>" />
				<input id="semRegra" name="semRegra" type="hidden" value="<%=request.getAttribute("semRegra")%>" />
				<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />				
				<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />	
						
				<div id="divEditar" class="divEditar">
					<fieldset class="formEdicao">
						<legend>Distribuir Conclusão para uma Unidade de Trabalho</legend>
						<fieldset class="formEdicao">
							<legend>Processos </legend>
							<table class="Tabela" id="TabelaArquivos">
								<thead>
									<tr>
										<th width="1%"></th>
										<th align="center" width="20%">N&uacute;mero Processo</th>
										<th align="center" width="5%">Id</th>
										<th align="center" width="30%">Pendência</th>
										<th align="center">Responsável Atual</th>
										<th></th>
									</tr>
								</thead>							
								<tbody>
								    <%PendenciaDt pendenciaDt = new PendenciaDt();%>
									<%int j = 0; %>
									<%String stTempNome="";%>
									<%for (Iterator iterator = PendenciaResponsaveldt.getlistaPendenciaDt().iterator(); iterator.hasNext();) { %>
										<%pendenciaDt = (PendenciaDt) iterator.next();%>
										<%if (stTempNome.equalsIgnoreCase("")) {%>
											<%stTempNome="a";%> 
							            	<tr class="TabelaLinha1"> 
										<%}else{%> 
											<% stTempNome=""; %>    
							                <tr class="TabelaLinha2">
										<%}%>
								                <td align="center"> <%=j=j+1%></td>
												<td width="15%" align="center">
													<a href="BuscaProcesso?Id_Processo=<%=pendenciaDt.getId_Processo()%>"><%=pendenciaDt.getProcessoNumero()%></a>
												</td>
												<td align="center"><%=pendenciaDt.getId()%></td>
												<td ><%=pendenciaDt.getPendencia()%></td>
												<%if (pendenciaDt.getResponsavel() != null){%>
													<%if (pendenciaDt.getResponsavel().getServentiaCargo() != null && pendenciaDt.getResponsavel().getServentiaCargo().length()>0) {%>
														<td><%=pendenciaDt.getResponsavel().getServentiaCargo()+" - "+pendenciaDt.getResponsavel().getNomeUsuarioServentiaCargo()%></td>
													<% } %>
												<%} %>
												<td  align="center"> <img src="./imagens/16x16/Mais.png" onClick="MostrarOcultar('linha<%=pendenciaDt.getId()%>');return false;" title="Mostrar/Ocultar Endereço" /></td>
											</tr>
										
										<tr >
											<td colspan="5">
									    	<table style='display:none' id='linha<%=pendenciaDt.getId()%>' class="Tabela">
									        	<thead>
									            	<tr>
													<th width="1%"></th>
													<th align="center" width="2%">Ident.</th>
													<th colspan="2" width="25%">Serventia Cargo / Nome</th>
													<th width="10%">Unid. de Trabalho</th>
													<th align="center" width="7%">Data Início</th>
													<th align="center" width="7%">Data Final</th>
													
												</tr>
									           	</thead>
									           	<tbody id="tabListaHistorico">
									           		<%PendenciaResponsavelHistoricoDt objTemp = new PendenciaResponsavelHistoricoDt(); %>
													<%int i = 0; %>
													<%String stTempNome2="";%>
													<%for (Iterator iteratorHistorico = pendenciaDt.getListaHistoricoPendencia().iterator(); iteratorHistorico.hasNext();) { %>
														<%objTemp = (PendenciaResponsavelHistoricoDt) iteratorHistorico.next();%>
														<%if (stTempNome2.equalsIgnoreCase("")) {%>
															<%stTempNome2="a";%> 
											            	<tr class="TabelaLinha1"> 
														<%}else{%> 
															<% stTempNome2=""; %>    
											                <tr class="TabelaLinha2">
														<%}%>
											                	<td align="center"> <%=i=i+1%></td>                   
											                   	<td align="center"><%= objTemp.getId_ServentiaCargo()%></td>               
											                   	<td width="6%"><%= objTemp.getServentiaCargo()%></td>
											                   	<td width="19%"><%= objTemp.getNome() %></td>
											                   	<td><%= objTemp.getServentiaGrupo() %></td>
											                   	<td align="center"><%= objTemp.getDataInicio()%></td>               
											                   	<td align="center"><%= objTemp.getDataFim()%></td>
										               		</tr>
									               <%} %>
									           </tbody>
									       </table>
											</td>
										</tr>

									<%} %>
								</tbody>
							</table>		
						</fieldset>
						
						<%if(PendenciaResponsaveldt.isConclusoMagistrado()){ %>
							<br />
							<input type="checkbox" class="formEdicaoInput"  name= "chkSubst" id="chkSubst" value="Enviar ao Presidente" onchange="serventiaCargoPresidente(this)" title="Enviar ao Presidente" <%if(PendenciaResponsaveldt.isConclusoMagistrado()){%> checked="true" <%}%> />Enviar para o Magistrado
							<br />
						<%} %>
						
						<%if (!(request.getSession().getAttribute("DistribuicaoEfetuada") != null && 
							 request.getSession().getAttribute("DistribuicaoEfetuada").toString().trim().toUpperCase() == "DE")){%>
							
							<br />
							<input type="checkbox" class="formEdicaoInput"  name= "chkSemRegra" id="chkSemRegra" value="Sem Regra" onchange="semRegraDistribuicao(this);LimparCampo('Id_ServentiaGrupo','');LimparCampo('ServentiaGrupo','')" title="Sem Regra de Distribuição" <%if(PendenciaResponsaveldt.isSemRegra()){%> checked="true" <%}%> />Sem Regra de Distribuição
							
							<br />
							<div id="divLocalizar" class="divLocalizar" >							
								<fieldset id="idNovoResponsavel" class="formEdicao">
									<legend>Nova Unidade de Trabalho</legend>
									<div id="divLocalizar2" class="divLocalizar2">		
										<label class="formEdicaoLabel" for="Id_ServentiaCargo">*Responsável 
				    					<input class="FormEdicaoimgLocalizar" id="imaLocalizarId_ServentiaCargo" name="imaLocalizarId_ServentiaCargo" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ServentiaCargoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" >  
				    					</label><br> 
				    					<input class="formEdicaoInputSomenteLeitura"  readonly name="ServentiaCargo" id="ServentiaCargo" type="text" size="80" maxlength="100" value="<%=PendenciaResponsaveldt.getServentiaCargo()%>"/><br />					
									</div>
									<label class="formEdicaoLabel" for="Id_ServentiaGrupo">*Serventia Grupo
					    			<input class="FormEdicaoimgLocalizar" id="imaLocalizarId_ServentiaGrupo" name="imaLocalizarId_ServentiaGrupo" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ServentiaGrupoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" >  
					    			</label><br>  
					    			<input id="Id_ServentiaGrupo" name="Id_ServentiaGrupo" type="hidden" value="<%=PendenciaResponsaveldt.getId_ServentiaGrupo()%>" />
					    			<input class="formEdicaoInputSomenteLeitura"  readonly name="ServentiaGrupo" id="ServentiaGrupo" type="text" size="80" maxlength="100" value="<%=PendenciaResponsaveldt.getServentiaGrupo()%>"/><br />
					    		</fieldset>
							</div>
							
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