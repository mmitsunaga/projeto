<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoDebitoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteTipoDt"%>
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
		$(document).ready(function() {
	 		mostrarOpcao();	 		
		});
	 	
	 	function mostrarOpcao(){
	 		var varCheck = $("input[name=dividaSolidaria]:checked");
	 		if (varCheck.val() == 'true') {
	 			Ocultar('divPartePolo');	
	 		} else {
	 			Mostrar('divPartePolo');	
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
					
					<%if (ProcessoParteDebitoFisicodt.getId() != null && ProcessoParteDebitoFisicodt.getId().length() > 0){%>					
					<label class="formEdicaoLabel" for="Stat">*Status</label><br/> 
					<span class="destaque"><%=ProcessoParteDebitoFisicodt.getProcessoDebitoStatus()%></span>
					<br/>
					<%}%>
					
					<label class="formEdicaoLabel" for="Stat"><%=(ProcessoParteDebitoFisicodt.getId().length() > 0) ? "*Número" : "Situação"%></label><br/> 
					<span class="destaque"><%=(ProcessoParteDebitoFisicodt.getId().length() > 0) ? ProcessoParteDebitoFisicodt.getId() : "NOVO CADASTRO"%></span>
					<br/>
					
					<label class="formEdicaoLabel" for="dividaSolidaria">*Dívida Solidária</label><br/>
					<input type="radio" name="dividaSolidaria" value="true" id="dividaSolidariaSim" <%=ProcessoParteDebitoFisicodt.isDividaSolidaria() ? "checked" : ""%> onChange="mostrarOpcao()" />Sim 
		       		<input type="radio" name="dividaSolidaria" value="false" id="dividaSolidariaNao" <%=ProcessoParteDebitoFisicodt.NotIsDividaSolidaria() ? "checked" : ""%> onChange="mostrarOpcao()" />Não					
					<br />
					
					<label class="formEdicaoLabel" for="tipoDeParte">*Tipo de Parte</label><br/>
					<input type="radio" name="tipoDeParte" value="1" id="poloAtivo" <%=ProcessoParteDebitoFisicodt.getTipoParte().equalsIgnoreCase(String.valueOf(ProcessoParteTipoDt.POLO_ATIVO_CODIGO)) ? "checked" : ""%> onclick="mostrarVarios('divPartePoloAtivo');ocultarVarios('divPartePoloPassivo');" />Polo Ativo 
		       		<input type="radio" name="tipoDeParte" value="0" id="poloPassivo" <%=ProcessoParteDebitoFisicodt.getTipoParte().equalsIgnoreCase(String.valueOf(ProcessoParteTipoDt.POLO_PASSIVO_CODIGO)) ? "checked" : ""%> onclick="mostrarVarios('divPartePoloPassivo');ocultarVarios('divPartePoloAtivo');" />Polo Passivo					
					<br />
					
					<div id="divPartePolo" style="display: <%=ProcessoParteDebitoFisicodt.NotIsDividaSolidaria() ? "none" : "block"%>">
						<div id="divPartePoloAtivo" style="display: <%=ProcessoParteDebitoFisicodt.getTipoParte().equalsIgnoreCase(String.valueOf(ProcessoParteTipoDt.POLO_ATIVO_CODIGO)) ? "block" : "none"%>">
							<label class="formEdicaoLabel">*Parte - Polo Ativo </label>
							<br /> 	
							<input type="hidden" name="nomeProcessoPartePoloAtivo" id="nomeProcessoPartePoloAtivo" value="<%=(ProcessoParteDebitoFisicodt.getListaPartesPromoventes() != null && ProcessoParteDebitoFisicodt.getListaPartesPromoventes().size() == 1 && ProcessoParteDebitoFisicodt.getListaPartesPromoventes().get(0) != null ? ((PoloSPGDt)ProcessoParteDebitoFisicodt.getListaPartesPromoventes().get(0)).getNome() : ProcessoParteDebitoFisicodt.getNome())%>">
							<select name="idProcessoPartePoloAtivo" id="idProcessoPartePoloAtivo" size="1" onchange="AlterarValue('nomeProcessoPartePoloAtivo',this.options[this.selectedIndex].text)" style="min-width:400px;">
								<%
								List listaPartesPoloAtivo = ProcessoParteDebitoFisicodt.getListaPartesPromoventes();
								if (listaPartesPoloAtivo == null || listaPartesPoloAtivo.size() != 1) {
								%>
								<option value="">----------Selecione a Parte----------</option>
								<%
								}
								if(listaPartesPoloAtivo != null && !listaPartesPoloAtivo.isEmpty()){
									for (int i=0;i < listaPartesPoloAtivo.size();i++){
										PoloSPGDt parteDt = (PoloSPGDt)listaPartesPoloAtivo.get(i);										
									%>
										<option value="<%=parteDt.getId()%>" <%=(parteDt.getId().equals(ProcessoParteDebitoFisicodt.getId_ProcessoParte()) ? "selected=\"selected\"" : "")%>><%=parteDt.getNome()%> - Polo Ativo</option>											
									<%			   				
									}
								}
								%>			
							</select>
							<br />
						</div>					
						
						<div id="divPartePoloPassivo" style="display: <%=ProcessoParteDebitoFisicodt.getTipoParte().equalsIgnoreCase(String.valueOf(ProcessoParteTipoDt.POLO_PASSIVO_CODIGO)) ? "block" : "none"%>">
							<label class="formEdicaoLabel" for="Id_ProcessoDebito">*Parte - Polo Passivo </label>
							<br>					
							<input type="hidden" name="nomeProcessoPartePoloPassivo" id="nomeProcessoPartePoloPassivo" value="<%=(ProcessoParteDebitoFisicodt.getListaPartesPromovidas() != null && ProcessoParteDebitoFisicodt.getListaPartesPromovidas().size() == 1 && ProcessoParteDebitoFisicodt.getListaPartesPromovidas().get(0) != null ? ((PoloSPGDt)ProcessoParteDebitoFisicodt.getListaPartesPromovidas().get(0)).getNome() : ProcessoParteDebitoFisicodt.getNome())%>">
							<select name="idProcessoPartePoloPassivo" id="idProcessoPartePoloPassivo" size="1" onchange="AlterarValue('nomeProcessoPartePoloPassivo',this.options[this.selectedIndex].text)" style="min-width:400px;">
								<%
								List listaPartesPoloPassivo = ProcessoParteDebitoFisicodt.getListaPartesPromovidas();
								if (listaPartesPoloPassivo == null || listaPartesPoloPassivo.size() != 1) {
									%>
									<option value="">----------Selecione a Parte----------</option>
									<%
								}
								if(listaPartesPoloPassivo != null && !listaPartesPoloPassivo.isEmpty()){ 	    					
									for (int i=0;i < listaPartesPoloPassivo.size();i++){
										PoloSPGDt parteDt = (PoloSPGDt)listaPartesPoloPassivo.get(i);
									%>
										<option value="<%=parteDt.getId()%>" <%=(parteDt.getId().equals(ProcessoParteDebitoFisicodt.getId_ProcessoParte()) ? "selected=\"selected\"" : "")%>><%=parteDt.getNome()%> - Polo Passivo</option>									
									<%			   				
									}
								}
								%> 	    										 	  						   
							</select>
							<br />
						</div>	
					</div>
					
					<label class="formEdicaoLabel" for="Id_ProcessoDebito">*Débito</label>
					<br>					
					<input type="hidden" name="ProcessoDebito" id="ProcessoDebito" value="<%=ProcessoParteDebitoFisicodt.getProcessoDebito()%>">
					<select name="Id_ProcessoDebito" id="Id_ProcessoDebito" size="1" onchange="AlterarValue('ProcessoDebito',this.options[this.selectedIndex].text)" style="min-width:300px;">
						<%
						List listaDebitos = ProcessoParteDebitoFisicodt.getListaProcessoDebito();
						if (listaDebitos == null || listaDebitos.size() != 1) {
							%>
							<option value="">----------Selecione o Débito----------</option>
							<%
						}
						if(listaDebitos != null && !listaDebitos.isEmpty()){ 	    					
							for (int i=0;i < listaDebitos.size();i++){
								ProcessoDebitoDt debito = (ProcessoDebitoDt)listaDebitos.get(i);
							%>
								<option value="<%=debito.getId()%>" <%=(debito.getId().equals(ProcessoParteDebitoFisicodt.getId_ProcessoDebito()) || listaDebitos.size() == 1 ? "selected=\"selected\"" : "")%>><%=debito.getProcessoDebito()%></option>									
							<%			   				
							}
						}
						%> 	    										 	  						   
					</select>
					<br />
					
					<label class="formEdicaoLabel">*Guia Final </label>
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