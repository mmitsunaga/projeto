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

<jsp:useBean id="ProcessoParteDebitodt" scope="session" class= "br.gov.go.tj.projudi.dt.ProcessoParteDebitoDt"/>


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
	
	 <script type="text/javascript"  language="javascript"    >

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
		<div class="area"><h2>&raquo; Cadastro de Débitos</h2></div>
		<form action="ProcessoParteDebito" method="post" name="Formulario" id="Formulario">
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
					<legend class="formEdicaoLegenda">Cadastro de Débitos </legend>
					
					<% if (ProcessoParteDebitodt.getId_Processo().length() == 0){ %>
					<label class="formEdicaoLabel" for="ProcessoNumero">*Número do Processo</label><br> 
					<input class="formEdicaoInput" name="ProcessoNumero" id="ProcessoNumero"  type="text" size="30" maxlength="25" value="<%=ProcessoParteDebitodt.getProcessoNumeroCompleto()%>" onkeyup="mascara(this, '#######.##.####.#.##.####'); autoTab(this,25)" onkeypress="return DigitarNumeroProcesso(this, event)">
					<em><strong> Nova Numeração</strong>:  Digite o Número do Processo completo. Ex. <strong>5000280.28.2010.8.09.0059</strong></em>
					<br />   		
					
					<div id="divBotoesCentralizados" class="divBotoesCentralizados">
						<input id="btnConsultar" type="submit" name="Consultar" value="Consultar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Editar%>');" />
					</div>
					
					<% } else { %>
					
					<label class="formEdicaoLabel" for="ProcessoNumero">*Número do Processo</label><br> 
					<span class="destaque"><a id="acessarProcesso" href="BuscaProcesso?Id_Processo=<%=ProcessoParteDebitodt.getId_Processo()%>"><%=ProcessoParteDebitodt.getProcessoNumeroCompleto() != null && ProcessoParteDebitodt.getProcessoNumeroCompleto().trim().length() > 0 ? ProcessoParteDebitodt.getProcessoNumeroCompleto() : ProcessoParteDebitodt.getProcessoNumero()%></a></span>
					<br/>
					
					<%if (ProcessoParteDebitodt.getId() != null && ProcessoParteDebitodt.getId().length() > 0){%>					
					<label class="formEdicaoLabel" for="Stat">*Status</label><br/> 
					<span class="destaque"><%=ProcessoParteDebitodt.getProcessoDebitoStatus()%></span>
					<br/>
					<%}%>
					
					<label class="formEdicaoLabel" for="Stat"><%=(ProcessoParteDebitodt.getId().length() > 0) ? "*Número" : "Situação"%></label><br/> 
					<span class="destaque"><%=(ProcessoParteDebitodt.getId().length() > 0) ? ProcessoParteDebitodt.getId() : "NOVO CADASTRO"%></span>
					<br/>
					
					<label class="formEdicaoLabel" for="dividaSolidaria">*Dívida Solidária</label><br/>
					<input type="radio" name="dividaSolidaria" value="true" id="dividaSolidariaSim" <%=ProcessoParteDebitodt.isDividaSolidaria() ? "checked" : ""%> onChange="mostrarOpcao()" />Sim 
		       		<input type="radio" name="dividaSolidaria" value="false" id="dividaSolidariaNao" <%=ProcessoParteDebitodt.NotIsDividaSolidaria() ? "checked" : ""%> onChange="mostrarOpcao()" />Não					
					<br />
					
					<label class="formEdicaoLabel" for="tipoDeParte">*Tipo de Parte</label><br/>
					<input type="radio" name="tipoDeParte" value="1" id="poloAtivo" <%=ProcessoParteDebitodt.getTipoParte().equalsIgnoreCase(String.valueOf(ProcessoParteTipoDt.POLO_ATIVO_CODIGO)) ? "checked" : ""%> onclick="mostrarVarios('divPartePoloAtivo');ocultarVarios('divPartePoloPassivo');" />Polo Ativo 
		       		<input type="radio" name="tipoDeParte" value="0" id="poloPassivo" <%=ProcessoParteDebitodt.getTipoParte().equalsIgnoreCase(String.valueOf(ProcessoParteTipoDt.POLO_PASSIVO_CODIGO)) ? "checked" : ""%> onclick="mostrarVarios('divPartePoloPassivo');ocultarVarios('divPartePoloAtivo');" />Polo Passivo					
					<br />
					
					<div id="divPartePolo" style="display: <%=ProcessoParteDebitodt.NotIsDividaSolidaria() ? "none" : "block"%>">
						<div id="divPartePoloAtivo" style="display: <%=ProcessoParteDebitodt.getTipoParte().equalsIgnoreCase(String.valueOf(ProcessoParteTipoDt.POLO_ATIVO_CODIGO)) ? "block" : "none"%>">
							<label class="formEdicaoLabel">*Parte - Polo Ativo </label>
							<br /> 	
							<input type="hidden" name="nomeProcessoPartePoloAtivo" id="nomeProcessoPartePoloAtivo" value="<%=ProcessoParteDebitodt.getListaPartesPromoventes() != null && ProcessoParteDebitodt.getListaPartesPromoventes().size() == 1 ? ((ProcessoParteDt)ProcessoParteDebitodt.getListaPartesPromoventes().get(0)).getNome() : ProcessoParteDebitodt.getNome()%>">
							<select name="idProcessoPartePoloAtivo" id="idProcessoPartePoloAtivo" size="1" onchange="AlterarValue('nomeProcessoPartePoloAtivo',this.options[this.selectedIndex].text)" style="min-width:400px;">
								<%
								List listaPartesPoloAtivo = ProcessoParteDebitodt.getListaPartesPromoventes();
								if (listaPartesPoloAtivo == null || listaPartesPoloAtivo.size() != 1) {
								%>
								<option value="">----------Selecione a Parte----------</option>
								<%
								}
								if(listaPartesPoloAtivo != null && !listaPartesPoloAtivo.isEmpty()){
									for (int i=0;i < listaPartesPoloAtivo.size();i++){
										ProcessoParteDt parteDt = (ProcessoParteDt)listaPartesPoloAtivo.get(i);										
									%>
										<option value="<%=parteDt.getId_ProcessoParte()%>" <%=(parteDt.getId_ProcessoParte().equals(ProcessoParteDebitodt.getId_ProcessoParte()) ? "selected=\"selected\"" : "")%>><%=parteDt.getNome()%></option>											
									<%			   				
									}
								}
								%>			
							</select>
							<br />
						</div>					
						
						<div id="divPartePoloPassivo" style="display: <%=ProcessoParteDebitodt.getTipoParte().equalsIgnoreCase(String.valueOf(ProcessoParteTipoDt.POLO_PASSIVO_CODIGO)) ? "block" : "none"%>">
							<label class="formEdicaoLabel" for="Id_ProcessoDebito">*Parte - Polo Passivo </label>
							<br>					
							<input type="hidden" name="nomeProcessoPartePoloPassivo" id="nomeProcessoPartePoloPassivo" value="<%=(ProcessoParteDebitodt.getListaPartesPromovidas() != null && ProcessoParteDebitodt.getListaPartesPromovidas().size() == 1 ? ((ProcessoParteDt)ProcessoParteDebitodt.getListaPartesPromovidas().get(0)).getNome() : ProcessoParteDebitodt.getNome())%>">
							<select name="idProcessoPartePoloPassivo" id="idProcessoPartePoloPassivo" size="1" onchange="AlterarValue('nomeProcessoPartePoloPassivo',this.options[this.selectedIndex].text)" style="min-width:400px;">
								<%
								List listaPartesPoloPassivo = ProcessoParteDebitodt.getListaPartesPromovidas();
								if (listaPartesPoloPassivo == null || listaPartesPoloPassivo.size() != 1) {
									%>
									<option value="">----------Selecione a Parte----------</option>
									<%
								}
								if(listaPartesPoloPassivo != null && !listaPartesPoloPassivo.isEmpty()){ 	    					
									for (int i=0;i < listaPartesPoloPassivo.size();i++){
										ProcessoParteDt parteDt = (ProcessoParteDt)listaPartesPoloPassivo.get(i);
									%>
										<option value="<%=parteDt.getId_ProcessoParte()%>" <%=(parteDt.getId_ProcessoParte().equals(ProcessoParteDebitodt.getId_ProcessoParte()) ? "selected=\"selected\"" : "")%>><%=parteDt.getNome()%></option>									
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
					<input type="hidden" name="ProcessoDebito" id="ProcessoDebito" value="<%=ProcessoParteDebitodt.getProcessoDebito()%>">
					<select name="Id_ProcessoDebito" id="Id_ProcessoDebito" size="1" onchange="AlterarValue('ProcessoDebito',this.options[this.selectedIndex].text)" style="min-width:300px;">
						<%
						List listaDebitos = ProcessoParteDebitodt.getListaProcessoDebito();
						if (listaDebitos == null || listaDebitos.size() != 1) {
							%>
							<option value="">----------Selecione o Débito----------</option>
							<%
						}
						if(listaDebitos != null && !listaDebitos.isEmpty()){ 	    					
							for (int i=0;i < listaDebitos.size();i++){
								ProcessoDebitoDt debito = (ProcessoDebitoDt)listaDebitos.get(i);
							%>
								<option value="<%=debito.getId()%>" <%=(debito.getId().equals(ProcessoParteDebitodt.getId_ProcessoDebito()) || listaDebitos.size() == 1 ? "selected=\"selected\"" : "")%>><%=debito.getProcessoDebito()%></option>									
							<%			   				
							}
						}
						%> 	    										 	  						   
					</select>
					<br />
					<label class="formEdicaoLabel">*Guia Final e Guia GRS Simplificada </label>
					<br /> 	
					<input type="hidden" name="numeroGuiaEmissao" id="numeroGuiaEmissao" value="<%=ProcessoParteDebitodt.getNumeroGuiaEmissao()%>">
					<select name="id_GuiaEmissao" id="id_GuiaEmissao" size="1" onchange="AlterarValue('numeroGuiaEmissao',this.options[this.selectedIndex].text)" style="min-width:213px;">
						<%
						List listaGuias = ProcessoParteDebitodt.getListaGuiasProcesso();
						if (listaGuias == null || listaGuias.size() != 1) {
							%>
							<option value="">----------Selecione a Guia----------</option>
							<%
						}
 	    				if(listaGuias != null && !listaGuias.isEmpty()){ 	    					 	    					
							for (int i=0;i < listaGuias.size();i++){
		  			  			GuiaEmissaoDt guiaEmissaoDt = (GuiaEmissaoDt)listaGuias.get(i);
		 					%>
		 						<option value="<%=guiaEmissaoDt.getId()%>" <%=(guiaEmissaoDt.getId().equals(ProcessoParteDebitodt.getId_GuiaEmissao()) || listaGuias.size() == 1 ? "selected=\"selected\"" : "")%>><%=guiaEmissaoDt.getNumeroGuiaCompleto()%></option>	
		 					<%			   				
	 	    				}
 	    				}
 	    				%> 	    									 	  						   
					</select>
					
					<br />
					<label class="formEdicaoLabel" for="ProcessoNumeroProad">Número PROAD</label><br> 
					<input class="formEdicaoInput" name="ProcessoNumeroProad" id="ProcessoNumeroProad" type="text" size="30" maxlength="15" value="<%=ProcessoParteDebitodt.getProcessoNumeroPROAD()%>" onkeyup="mascara(this, '###############'); autoTab(this,15)" onkeypress="return DigitarSoNumero(this, event)">				
					
					<% } %>
				</fieldset>
				
				<% if (ProcessoParteDebitodt.getId_Processo().length() != 0 && ProcessoParteDebitodt.getListaPartesComDebito() != null && ProcessoParteDebitodt.getListaPartesComDebito().size() > 0){ %> 
	          		<fieldset id="VisualizaDados" class="VisualizaDados">
		      			<legend>Débitos Cadastrados Para o Processo Selecionado </legend>
		    			<table id="Tabela" class="Tabela">
				        	<thead>
				           		<tr class="TituloColuna">
					           		<td width="3%">&nbsp;</td>
					               	<td width="5%">Número</td>
					               	<td width="15%">Débito</td>
					               	<td width="5%">Status</td>
					               	<td width="30%">Parte</td>
					               	<td width="5%">Polo</td>
					               	<td width="14%">CPF/CNPJ</td>
					               	<td width="7%">Guia</td>
					               	<td width="20%">Valor</td>
					               	<th width="5%">Excluir</th>
				                  	<th width="5%">Editar</th>
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
				          			<td align="left"><%=aux.getProcessoDebitoStatus()%></td>
				          			<td align="left"><%=aux.getNome()%></td>
				          			<td align="center">
				          			<%if( aux.getTipoParte() != null && aux.getTipoParte().equals(String.valueOf(ProcessoParteTipoDt.POLO_ATIVO_CODIGO)) ) { %>
				          				Ativo
			          				<% } else if( aux.getTipoParte() != null && aux.getTipoParte().equals(String.valueOf(ProcessoParteTipoDt.POLO_PASSIVO_CODIGO)) ) { %>
		          						Passivo
			          				<% } else { %>
			          					-
			          				<% } %>				          			
				          			</td>
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
				          			<td class="Centralizado"><input name="formLocalizarimgExcluir" type="image"  title="Excluir Débito" src="./imagens/imgExcluirPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Excluir%>'); AlterarValue('Id_ProcessoParteDebito','<%=aux.getId()%>')"> </td>
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
	</script>
</body>
</html>