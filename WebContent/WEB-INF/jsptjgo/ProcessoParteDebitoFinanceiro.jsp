<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoDebitoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteTipoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.GuiaEmissaoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.GuiaStatusDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoDebitoStatusDt"%>
<%@page import="br.gov.go.tj.projudi.ne.GuiaEmissaoNe"%>

<jsp:useBean id="ProcessoParteDebitodt" scope="session" class= "br.gov.go.tj.projudi.dt.ProcessoParteDebitoDt"/>
<jsp:useBean id="processoDebitoDt" scope="session" class="br.gov.go.tj.projudi.dt.ProcessoDt"/>


<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteDebitoDt"%><html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Busca de Débitos  </title>
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
		function alterarStatus(codigo){
			if (codigo == "<%=ProcessoDebitoStatusDt.BAIXADO%>") {
				$("#divDataBaixa").show();				
			} else {
				$("#divDataBaixa").hide();				
			}			
		}
	</script>
</head>

<body>
	<div id="divCorpo" class="divCorpo">
		<div class="area"><h2>&raquo; Manutenção de Débitos - Diretoria Financeira</h2></div>
		<form action="ProcessoParteDebitoFinanceiro" method="post" name="Formulario" id="Formulario">
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input id="__Pedido__" name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
			<input id="TituloPagina" name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
			<input id="Id_ProcessoParteDebito" name="Id_ProcessoParteDebito" type="hidden" value="<%=ProcessoParteDebitodt.getId()%>" />
			
			<div id="divPortaBotoes" class="divPortaBotoes">
				<input id="imgNovo" alt="Novo"  class="imgNovo" title="Novo - Limpa os campos da tela" name="imaNovo" type="image" src="./imagens/imgNovo.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Curinga7)%>')" />
				<input id="imgsalvar" alt="Salvar" class="imgsalvar" title="salvar - Salva os dados digitados" name="imasalvar" type="image"  src="./imagens/imgSalvar.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Salvar)%>')" />  
				<input id="imgLocalizar" alt="Localizar" class="imgLocalizar" title="Localizar - Localiza um registro no banco" name="imaLocalizar" type="image"  src="./imagens/imgLocalizar.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Localizar)%>')" />
				<input id="imgAtualizar" alt="Atualizar" class="imgAtualizar" title="Escolhe novo processo" name="imaAtualizar" type="image"  src="./imagens/imgAtualizar.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Novo)%>')" />				
			</div>
			<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao"  > 
					<legend class="formEdicaoLegenda">Manutenção de Débitos - Diretoria Financeira</legend>
					
					<% if (ProcessoParteDebitodt.getId_Processo().length() == 0){ %>
					<label class="formEdicaoLabel" for="ProcessoNumero">*Número do Processo</label><br> 
					<input class="formEdicaoInput" name="ProcessoNumero" id="ProcessoNumero"  type="text" size="30" maxlength="25" value="<%=ProcessoParteDebitodt.getProcessoNumeroCompleto()%>" onkeyup="mascara(this, '#######.##.####.#.##.####'); autoTab(this,25)" onkeypress="return DigitarNumeroProcesso(this, event)">
					<em><strong> Nova Numeração</strong>:  Digite o Número do Processo completo. Ex. <strong>5000280.28.2010.8.09.0059</strong></em><br />   		
					
					<div id="divBotoesCentralizados" class="divBotoesCentralizados">
						<input id="btnConsultar" type="submit" name="Consultar" value="Consultar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Editar%>');" />
					</div>
					
					<% } else { %>
					
					<label class="formEdicaoLabel">Número do Processo</label><br> 
					<span class="destaque"><a href="BuscaProcesso?Id_Processo=<%=ProcessoParteDebitodt.getId_Processo()%>"><%=ProcessoParteDebitodt.getProcessoNumeroCompleto()%></a></span>
					<br />					
					<label class="formEdicaoLabel" for="DataTransitoEmJulgado">Data Trânsito em Julgado</label><br> 
					<input class="formEdicaoInputSomenteLeitura" disabled name="DataTransitoEmJulgado" id="DataTransitoEmJulgado" type="text" size="15" maxlength="15" value="<%=processoDebitoDt.getDataTransitoJulgado()%>">					
					<br />	
					<%
						ProcessoParteDt processoParte = processoDebitoDt.getParte(ProcessoParteDebitodt.getId_ProcessoParte());
					%>				
					<label class="formEdicaoLabel" for="NomeProcessoParte">Parte</label><br> 
					<input class="formEdicaoInputSomenteLeitura"  disabled name="NomeProcessoParte" id="NomeProcessoParte" type="text" size="60" maxlength="60" value="<%=ProcessoParteDebitodt.getNome()%><%=ProcessoParteDebitodt.getId_ProcessoParte() != null && ProcessoParteDebitodt.getId_ProcessoParte().trim().length() > 0 ? (ProcessoParteDebitodt.getTipoParte().equalsIgnoreCase(String.valueOf(ProcessoParteTipoDt.POLO_ATIVO_CODIGO)) ? " - Polo Ativo" : " - Polo Passivo") : ""%>">
					<%if (processoParte != null && processoParte.getId() != null && processoParte.getId().trim().length() > 0) {%>					
					<input name="inputEndereco" type="image" src="./imagens/16x16/btn_endereco.png" onClick="MostrarOcultar('sub<%=processoParte.getId()%>');return false;" title="Mostrar/Ocultar Endereço" />
					<br />
					<div id="sub<%=processoParte.getId()%>"  class="DivInvisivel">
				  		<fieldset class="fieldsetEndereco">
				  			<legend> Endereço </legend>
							<%=processoParte.getEnderecoParte().getLogradouro() + " nº " + processoParte.getEnderecoParte().getNumero() + " " + processoParte.getEnderecoParte().getComplemento()%><br />
				    		<%=processoParte.getEnderecoParte().getBairro() + " " + processoParte.getEnderecoParte().getCidade() + " " + processoParte.getEnderecoParte().getUf()%><br />
				    		<%=Funcoes.formatarCep( processoParte.getEnderecoParte().getCep() )%><br />
					    	<%=processoParte.getEMail() + " " + processoParte.getTelefone()%>  	   		    		
						</fieldset>						
					</div>					
					<% } else { %>
					<br />
					<% } %>
					<label class="formEdicaoLabel" for="CPFParte">CPF</label><br> 
					<input class="formEdicaoInputSomenteLeitura"  disabled name="CPFParte" id="CPFParte" type="text" size="15" maxlength="15" value="<%=(processoParte != null && processoParte.getCpf() != null && processoParte.getCpf().trim().length() > 0 ? processoParte.getCpfCnpjFormatado() : "")%>">
					<br />					
					<label class="formEdicaoLabel" for="ProcessoDebito">Débito</label><br>
					<input class="formEdicaoInputSomenteLeitura"  disabled name="ProcessoDebito" id="ProcessoDebito" type="text" size="60" maxlength="60" value="<%=ProcessoParteDebitodt.getProcessoDebito()%>">
					<br />
					<label class="formEdicaoLabel" for="NumeroGuia">Guia - Valor</label><br />
					<span class="destaque"><%if(ProcessoParteDebitodt.getId_GuiaEmissao() != null && ProcessoParteDebitodt.getId_GuiaEmissao().trim().length() > 0){%>
					<a href="GuiaEmissao?hash=<%=Funcoes.GeraHashMd5(ProcessoParteDebitodt.getId_GuiaEmissao() + GuiaEmissaoNe.NUMERO_SERIE_GUIA)%>&PaginaAtual=<%=Configuracao.Curinga6%>&PassoEditar=<%=Configuracao.Curinga8%>&Id_GuiaEmissao=<%=ProcessoParteDebitodt.getId_GuiaEmissao()%>&Id_GuiaTipo=<%=ProcessoParteDebitodt.getId_GuiaTipo()%>&EhGuiaSPG_SSG=N&comandoOnClickBotaoVoltar=PaginaAtual=<%=Configuracao.Curinga7%>&tempRetorno=ProcessoParteDebitoFinanceiro"><%=Funcoes.FormatarNumeroSerieGuia(ProcessoParteDebitodt.getNumeroGuiaEmissao())%> - R$ <%=Funcoes.FormatarDecimal(ProcessoParteDebitodt.getValorTotalGuia())%></a>
					<%}%>
					</span> 	
					<br />
					<label class="formEdicaoLabel" for="ProcessoNumeroProad">Número PROAD</label><br> 
					<input class="formEdicaoInputSomenteLeitura"  disabled name="ProcessoNumeroProad" id="ProcessoNumeroProad" type="text" size="15" maxlength="15" value="<%=ProcessoParteDebitodt.getProcessoNumeroPROAD()%>">
					<br />	
					<label class="formEdicaoLabel" for="Id_ProcessoDebito">*Status</label>
					<br>					
					<input type="hidden" name="ProcessoDebitoStatus" id="ProcessoDebitoStatus" value="<%=ProcessoParteDebitodt.getProcessoDebitoStatus()%>">
					<select name="Id_ProcessoDebitoStatus" id="Id_ProcessoDebitoStatus" size="1" onchange="AlterarValue('ProcessoDebitoStatus',this.options[this.selectedIndex].text); alterarStatus(this.options[this.selectedIndex].value);" style="min-width:300px;">
						<%
						List listaDebitosStatus = ProcessoParteDebitodt.getListaProcessoDebitoStatus();
						if (listaDebitosStatus == null || listaDebitosStatus.size() != 1) {
							%>
							<option value="">----------Selecione o Status do Débito----------</option>
							<%
						}
						if(listaDebitosStatus != null && !listaDebitosStatus.isEmpty()){ 	    					
							for (int i=0;i < listaDebitosStatus.size();i++){
								ProcessoDebitoStatusDt debito = (ProcessoDebitoStatusDt)listaDebitosStatus.get(i);
							%>
								<option value="<%=debito.getId()%>"  <%=(debito.getId().equals(ProcessoParteDebitodt.getId_ProcessoDebitoStatus()) || listaDebitosStatus.size() == 1 ? "selected=\"selected\"" : "")%>><%=debito.getProcessoDebitoStatus()%></option>									
							<%			   				
							}
						}
						%> 	    										 	  						   
					</select>
										
					<div id="divDataBaixa" style="display: <%=ProcessoParteDebitodt.isBaixado() ? "block" : "none"%>">
						<br />
		       			<label class="formEdicaoLabel" for="DataBaixa"> *Data Baixa</label><br />
			    		<input class="formEdicaoInput" name="DataBaixa" id="DataBaixa"  type="text" size="10" maxlength="10" value="<%=ProcessoParteDebitodt.getDataBaixa()%>" onkeyup="mascara_data(this)" onblur="verifica_data(this)" onkeypress="return DigitarSoNumero(this, event)">			    		 
			    		<img id="calendarioDataBaixa" src="./imagens/dlcalendar_2.gif" height="13" width="13" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].DataBaixa,'dd/mm/yyyy',this)">	
			    		<input class="FormEdicaoimgLocalizar" name="imaLimparDataBaixa" type="image"  src="./imagens/16x16/edit-clear.png" onclick="LimparChaveEstrangeira('DataBaixa','DataBaixa'); return false;" title="Limpar Data Baixa">				    		
		       		</div>
		       		<br />
		       		<label class="formEdicaoLabel" for="DataBaixa"> Observação do Status do Débito (Diretoria Financeira)</label><br />
		       		<textarea id="editor" name="editor" onkeypress="if (this.value.length > 1000 && event.keyCode != 8 && event.keyCode != 46 && event.keyCode != 37 && event.keyCode != 39) { return false; }" style="width:100%;height:80px;"><%=ProcessoParteDebitodt.getObservacaoProcessoDebitoStatus()%></textarea>
		       		<br />		       		
					<% } %>
				</fieldset>
				
				<% if (ProcessoParteDebitodt.getId_Processo().length() != 0 && ProcessoParteDebitodt.getListaPartesComDebito() != null && ProcessoParteDebitodt.getListaPartesComDebito().size() > 0){ %> 
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
			          		for (int i=0; i < ProcessoParteDebitodt.getListaPartesComDebito().size();i++){
			          			ProcessoParteDebitoDt aux = (ProcessoParteDebitoDt)ProcessoParteDebitodt.getListaPartesComDebito().get(i);
			          		%>
					      		<tr>
					      			<td><%=i+1%></td>
				          			<td align="center"><%=aux.getId()%></td>
				          			<td align="left"><%=aux.getProcessoDebito()%></td>
				          			<td align="left"><%=aux.getNome()%></td>
				          			<td align="center"><%=aux.getCpfParte()%></td>
				          			<%if(aux.getNumeroGuiaEmissao() == null || aux.getNumeroGuiaEmissao().trim().length() == 0) {%>
				          			<td align="center"></td>
				          			<td align="center"></td>
				          			<% } else { %>
				          			<td align="center">
				          				<%if( aux.getId_GuiaStatus() != null && !aux.getId_GuiaStatus().equals(String.valueOf(GuiaStatusDt.CANCELADA)) ) { %>
					          				<a href="GuiaEmissao?hash=<%=Funcoes.GeraHashMd5(aux.getId_GuiaEmissao() + GuiaEmissaoNe.NUMERO_SERIE_GUIA)%>&PaginaAtual=<%=Configuracao.Curinga6%>&PassoEditar=<%=Configuracao.Curinga8%>&Id_GuiaEmissao=<%=aux.getId_GuiaEmissao()%>&Id_GuiaTipo=<%=aux.getId_GuiaTipo()%>&EhGuiaSPG_SSG=N&comandoOnClickBotaoVoltar=PaginaAtual=<%=Configuracao.Curinga7%>&tempRetorno=ProcessoParteDebito">
				          				<% } %>
			          					<%=Funcoes.FormatarNumeroSerieGuia(aux.getNumeroGuiaEmissao())%>
				          				<%if( aux.getId_GuiaStatus() != null && !aux.getId_GuiaStatus().equals(String.valueOf(GuiaStatusDt.CANCELADA)) ) { %>
				          					</a>
				          				<% } %>
				          			</td>
				          			<td>
				          				<b>R$ <%= Funcoes.FormatarDecimal(aux.getValorTotalGuia()) %></b>
				          			</td>
				          			<% } %>
				          			<td class="Centralizado"><input name="formLocalizarimgEditar" type="image"  title="Excluir Débito" src="./imagens/imgExcluirPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Excluir%>'); AlterarValue('Id_ProcessoParteDebito','<%=aux.getId()%>')"> </td>
						     		<td class="Centralizado"><input name="formLocalizarimgEditar" type="image"  title="Editar Débito" src="./imagens/imgEditarPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga6%>');AlterarValue('Id_ProcessoParteDebito','<%=aux.getId()%>')"></td>
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
		
// 		$(document).ready(function(){
// 			alterarValorRadio($('input[name=Status]:checked', '#Formulario').val());		    
// 		});
	</script>
</body>
</html>