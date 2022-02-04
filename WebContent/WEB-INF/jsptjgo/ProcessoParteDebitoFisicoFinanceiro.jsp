<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoDebitoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteDt"%>
<%@page import="br.gov.go.tj.projudi.dt.GuiaEmissaoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.GuiaStatusDt"%>
<%@page import="br.gov.go.tj.projudi.ne.GuiaEmissaoNe"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteDebitoFisicoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.PoloSPGDt"%>

<jsp:useBean id="ProcessoParteDebitoFisicodt" scope="session" class= "br.gov.go.tj.projudi.dt.ProcessoParteDebitoFisicoDt"/>

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Busca de Débitos - Processo Físico </title>
	<style type="text/css">
		@import url('./css/Principal.css');
		@import url('./css/Paginacao.css');
		@import url('js/jscalendar/dhtmlgoodies_calendar.css?random=20051112');
	</style>
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type='text/javascript' src='./js/jquery.js'></script>
   	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
	<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js" ></script>
	<script type='text/javascript' src='./js/Digitacao/DigitarNumeroProcesso.js'></script>
	<script type='text/javascript' src='./js/Digitacao/MascararValor.js'></script>
	<script type='text/javascript' src='./js/Digitacao/DigitarData.js'></script>  
	<link type="text/css" rel="stylesheet" href="js/jscalendar/dhtmlgoodies_calendar.css?random=20051112" media="screen"></link>
	<script type="text/javascript" src="./js/jscalendar/dhtmlgoodies_calendar.js?random=20060118"></script>
	<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>
	
	<script type="text/javascript">
		function alterarValorRadio(tipoConsulta){
			if (tipoConsulta == "0") {
				$("#divDataBaixa").show();				
			} else {
				$("#divDataBaixa").hide();				
			}			
		}
	</script>
</head>

<body>
	<div id="divCorpo" class="divCorpo">
		<div class="area"><h2>&raquo; Cadastro de Débitos - Processo Físico</h2></div>
		<form action="ProcessoParteDebitoFisico" method="post" name="Formulario" id="Formulario">
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input id="__Pedido__" name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
			<input id="TituloPagina" name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
			<input id="Id_ProcessoParteDebitoFisico" name="Id_ProcessoParteDebitoFisico" type="hidden" value="<%=ProcessoParteDebitoFisicodt.getId()%>" />
			
			<div id="divPortaBotoes" class="divPortaBotoes">
				<input id="imgNovo" alt="Novo"  class="imgNovo" title="Novo - Limpa os campos da tela" name="imaNovo" type="image" src="./imagens/imgNovo.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Curinga7)%>')" />
				<input id="imgsalvar" alt="Salvar" class="imgsalvar" title="salvar - Salva os dados digitados" name="imasalvar" type="image"  src="./imagens/imgSalvar.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Salvar)%>')" />  
				<input id="imgLocalizar" alt="Localizar" class="imgLocalizar" title="Localizar - Localiza um registro no banco" name="imaLocalizar" type="image"  src="./imagens/imgLocalizar.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Localizar)%>')" />
				<input id="imgAtualizar" alt="Atualizar" class="imgAtualizar" title="Escolhe novo processo" name="imaAtualizar" type="image"  src="./imagens/imgAtualizar.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Novo)%>')" />				
			</div>
			<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao"  > 
					<legend class="formEdicaoLegenda">Cadastro de Débitos - Processo Físico</legend>
					
					<% if (ProcessoParteDebitoFisicodt.getProcessoNumeroCompleto().length() == 0){ %>
					<label class="formEdicaoLabel" for="ProcessoNumero">*Número do Processo</label><br> 
					<input class="formEdicaoInput" name="ProcessoNumero" id="ProcessoNumero"  type="text" size="30" maxlength="25" value="<%=ProcessoParteDebitoFisicodt.getProcessoNumeroCompleto()%>" onkeyup="mascara(this, '#######.##.####.#.##.####'); autoTab(this,25)" onkeypress="return DigitarNumeroProcesso(this, event)">
					<em><strong> Nova Numeração</strong>:  Digite o Número do Processo completo. Ex. <strong>5000280.28.2010.8.09.0059</strong></em><br />   		
					
					<div id="divBotoesCentralizados" class="divBotoesCentralizados">
						<input id="btnConsultar" type="submit" name="Consultar" value="Consultar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Editar%>');" />
					</div>
					
					<% } else { %>
					
					<label class="formEdicaoLabel" for="ProcessoNumero">Número do Processo</label><br> 
					<span class="destaque"><b><%=Funcoes.formataNumeroCompletoProcesso(ProcessoParteDebitoFisicodt.getProcessoNumeroCompleto())%></b></span>
					
					<br />
					<label class="formEdicaoLabel">*Parte </label>
					<br /> 	
					<input type="hidden" name="nomeProcessoParte" id="nomeProcessoParte" value="<%=ProcessoParteDebitoFisicodt.getNome()%>">
					<select name="idProcessoParte" id="idProcessoParte" size="1" onchange="AlterarValue('nomeProcessoParte',this.options[this.selectedIndex].text)">
						<option value="">--Selecione a Parte--</option>
						<%
						List listaPartes = ProcessoParteDebitoFisicodt.getListaPartesPromoventes();
 	    				if(listaPartes != null && !listaPartes.isEmpty()){
							for (int i=0;i < listaPartes.size();i++){
								PoloSPGDt parteDt = (PoloSPGDt)listaPartes.get(i);
		 					%>
		 						<option value="<%=parteDt.getId()%>" <%=(parteDt.getId().equals(ProcessoParteDebitoFisicodt.getId_ProcessoParte()) ? "selected=\"selected\"" : "")%>><%=parteDt.getNome()%> - Polo Ativo</option>	
		 					<%			   				
	 	    				}
 	    				}
 	    				%>
 	    				<%
						listaPartes = ProcessoParteDebitoFisicodt.getListaPartesPromovidas();
 	    				if(listaPartes != null && !listaPartes.isEmpty()){
							for (int i=0;i < listaPartes.size();i++){
								PoloSPGDt parteDt = (PoloSPGDt)listaPartes.get(i);
		 					%>
		 						<option value="<%=parteDt.getId()%>" <%=(parteDt.getId().equals(ProcessoParteDebitoFisicodt.getId_ProcessoParte()) ? "selected=\"selected\"" : "")%>><%=parteDt.getNome()%> - Polo Passivo</option>	
		 					<%			   				
	 	    				}
 	    				}
 	    				%>						 	  						   
					</select>
					<br />
					
					<label class="formEdicaoLabel" for="Id_ProcessoDebito">*Débito  
					<input class="FormEdicaoimgLocalizar" id="imaLocalizarProcessoDebito" name="imaLocalizarProcessoDebito" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ProcessoDebitoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar  )%>')" > 					
					</label><br>
					<input class="formEdicaoInputSomenteLeitura"  readonly="true" name="ProcessoDebito" id="ProcessoDebito" type="text" size="60" maxlength="60" value="<%=ProcessoParteDebitoFisicodt.getProcessoDebito()%>">
					
					<br />
					<label class="formEdicaoLabel">Guia </label>
					<br /> 	
					<select name="NumeroGuia" id="NumeroGuia" size="1">
						<option value="">-- Selecione a Guia --</option>
						<%
						List listaGuias = ProcessoParteDebitoFisicodt.getListaGuiasProcesso();
 	    				if(listaGuias != null && !listaGuias.isEmpty()){
							for (int i=0;i < listaGuias.size();i++){
		  			  			GuiaEmissaoDt guiaEmissaoDt = (GuiaEmissaoDt)listaGuias.get(i);
		 					%>
		 						<option value="<%=guiaEmissaoDt.getNumeroGuiaCompleto()%>" <%=(guiaEmissaoDt.getNumeroGuiaCompleto().equals(ProcessoParteDebitoFisicodt.getNumeroGuia()) ? "selected=\"selected\"" : "")%>><%=guiaEmissaoDt.getNumeroGuiaCompleto()%></option>	
		 					<%			   				
	 	    				}
 	    				}
 	    				%> 	    									 	  						   
					</select>
					
					<br />
					<label class="formEdicaoLabel" for="ProcessoNumeroProad">Número PROAD</label><br> 
					<input class="formEdicaoInput" name="ProcessoNumeroProad" id="ProcessoNumeroProad" type="text" size="30" maxlength="15" value="<%=ProcessoParteDebitoFisicodt.getProcessoNumeroPROAD()%>" onkeyup="mascara(this, '###############'); autoTab(this,15)" onkeypress="return DigitarSoNumero(this, event)">
					
					<br />						
					<label class="formEdicaoLabel" for="Status"> *Status </label><br>  
<%-- 					input type="radio" name="Status" value="1" onclick="alterarValorRadio('1')" <%=ProcessoParteDebitoFisicodt.isEmAberto() ? "checked" : ""%> />Em Aberto --%>
<%-- 		       		<input type="radio" name="Status" value="0" onclick="alterarValorRadio('0')" <%=ProcessoParteDebitoFisicodt.isBaixado() ? "checked" : ""%> />Baixado --%>
		       		
		       		<div id="divDataBaixa">
		       			<label class="formEdicaoLabel" for="DataBaixa"> *Data Baixa</label><br />
			    		<input class="formEdicaoInput" name="DataBaixa" id="DataBaixa"  type="text" size="10" maxlength="10" value="<%=ProcessoParteDebitoFisicodt.getDataBaixa()%>" onkeyup="mascara_data(this)" onblur="verifica_data(this)" onkeypress="return DigitarSoNumero(this, event)">			    		 
			    		<img id="calendarioDataBaixa" src="./imagens/dlcalendar_2.gif" height="13" width="13" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].DataBaixa,'dd/mm/yyyy',this)">	
			    		<input class="FormEdicaoimgLocalizar" name="imaLimparDataTransitoJulgado" type="image"  src="./imagens/16x16/edit-clear.png" onclick="LimparChaveEstrangeira('DataBaixa','DataBaixa'); return false;" title="Limpar Data Baixa">				    		
		       		</div>
					<% } %>
				</fieldset>
				
				<% if (ProcessoParteDebitoFisicodt.getProcessoNumeroCompleto().length() != 0 && ProcessoParteDebitoFisicodt.getListaPartesComDebito() != null && ProcessoParteDebitoFisicodt.getListaPartesComDebito().size() > 0){ %> 
	          		<fieldset id="VisualizaDados" class="VisualizaDados">
		      			<legend>Débitos Cadastrados Para o Processo Selecionado </legend>
		    			<table id="Tabela" class="Tabela">
				        	<thead>
				           		<tr class="TituloColuna">
					           		<td width="3%">&nbsp;</td>
					               	<td width="7%">Número</td>
					               	<td width="18%">Débito</td>
					               	<td width="35%">Parte</td>
					               	<td width="15%">CPF/CNPJ</td>
					               	<td width="11%">Guia</td>
					               	<td width="16%">Valor</td>
					               	<th>Excluir</th>
				                  	<th>Editar</th>
				    	        </tr>
				           	</thead>
				          	<tbody>
			          		<%
			          		for (int i=0; i < ProcessoParteDebitoFisicodt.getListaPartesComDebito().size();i++){
			          			ProcessoParteDebitoFisicoDt aux = (ProcessoParteDebitoFisicoDt)ProcessoParteDebitoFisicodt.getListaPartesComDebito().get(i);
			          		%>
					      		<tr>
					      			<td><%=i+1%></td>
				          			<td align="center"><%=aux.getId()%></td>
				          			<td align="left"><%=aux.getProcessoDebito()%></td>
				          			<td align="left"><%=aux.getNome()%></td>
				          			<td align="center"><%=aux.getCpfParte()%></td>
				          			<%if(aux.getNumeroGuia() == null || aux.getNumeroGuia().trim().length() == 0) {%>
				          			<td align="center"></td>
				          			<td align="center"></td>
				          			<% } else { %>
				          			<td align="center">
				          				<a href="GuiaEmissao?hash=<%=Funcoes.GeraHashMd5(aux.getNumeroGuia() + GuiaEmissaoNe.NUMERO_SERIE_GUIA)%>&PaginaAtual=<%=Configuracao.Curinga6%>&PassoEditar=<%=Configuracao.Curinga8%>&NumeroGuia=<%=aux.getNumeroGuia()%>&EhGuiaSPG_SSG=S&comandoOnClickBotaoVoltar=PaginaAtual=<%=Configuracao.Curinga7%>&tempRetorno=ProcessoParteDebitoFisico">
			          					<%=Funcoes.FormatarNumeroSerieGuia(aux.getNumeroGuia())%>
				          				</a>
				          			</td>
				          			<td>
				          				<b>R$ <%= Funcoes.FormatarDecimal(aux.getValorTotalGuia()) %></b>
				          			</td>
				          			<% } %>
				          			<td class="Centralizado"><input name="formLocalizarimgEditar" type="image"  title="Excluir Débito" src="./imagens/imgExcluirPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Excluir%>'); AlterarValue('Id_ProcessoParteDebitoFisico','<%=aux.getId()%>')"> </td>
						     		<td class="Centralizado"><input name="formLocalizarimgEditar" type="image"  title="Editar Débito" src="./imagens/imgEditarPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga6%>');AlterarValue('Id_ProcessoParteDebitoFisico','<%=aux.getId()%>')"></td>
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
	<script type="text/javascript">
		function fechar(valor){
			if (valor!=null){
				$('.corpo').hide();
			}
		}
		
		$(document).ready(function(){
			alterarValorRadio($('input[name=Status]:checked', '#Formulario').val());		    
		});
	</script>
</body>
</html>