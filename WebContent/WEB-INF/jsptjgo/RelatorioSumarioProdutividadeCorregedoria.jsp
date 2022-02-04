<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<%@page  import="java.util.List"%>
<%@page import="br.gov.go.tj.projudi.dt.relatorios.RelatorioSumarioDt"%>

<jsp:useBean id="RelatorioSumariodt" scope="session" class= "br.gov.go.tj.projudi.dt.relatorios.RelatorioSumarioDt"/>

<%@page import="br.gov.go.tj.projudi.dt.ServentiaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.UsuarioDt"%>
<%@page import="br.gov.go.tj.projudi.dt.GrupoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ComarcaDt"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>


<%@page import="java.util.HashMap"%>

<%@page import="br.gov.go.tj.projudi.dt.EstatisticaProdutividadeItemDt"%><html>
	<head>
		<title> <%=request.getAttribute("tempPrograma")%> </title>
		<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./js/jscalendar/dhtmlgoodies_calendar.css');
		</style>
		
		
		<script type='text/javascript' src='./js/Funcoes.js'></script>
		<script type='text/javascript' src='./js/jquery.js'></script>
		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
		<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js" ></script>
		<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>
		<script type="text/javascript" src="./js/jscalendar/dhtmlgoodies_calendar.js"></script>
		
		<script language="javascript" type="text/javascript">
		function atualizarCamposTela(opcaoAgrupamento) {
			if(opcaoAgrupamento == <%=RelatorioSumarioDt.COMARCA%>){
				$("#fieldsetServentia").css('display','none');
				$("#fieldsetGrupo").css('display','none');
				document.getElementById('Id_Serventia').value = '';
				document.getElementById('Serventia').value = '';
				document.getElementById('Id_Usuario').value = '';
				document.getElementById('Usuario').value = '';
			} else if(opcaoAgrupamento == <%=RelatorioSumarioDt.COMARCA_SERVENTIA%>){
				$("#fieldsetServentia").css('display','block');
				$("#fieldsetGrupo").css('display','none');
				document.getElementById('Id_Usuario').value = '';
				document.getElementById('Usuario').value = '';
			} else if(opcaoAgrupamento == <%=RelatorioSumarioDt.COMARCA_SERVENTIA_USUARIO%>){
				$("#fieldsetServentia").css('display','block');
				$("#fieldsetGrupo").css('display','block');
			}
		}
		
		function mostrarOcultarCamposPeriodo(opcaoRelatorio) {
			if(opcaoRelatorio == "1") {
				$("#fieldsetMesFechado").css('display','block');
				$("#fieldsetDataInicialFinal").css('display','none');
			} else {
				$("#fieldsetMesFechado").css('display','none');
				$("#fieldsetDataInicialFinal").css('display','block');
			}
		}
		</script>
		
	</head>
	<body onload="javascript:atualizarCamposTela(<%=RelatorioSumariodt.getAgrupamentoRelatorio()%>);javascript:mostrarOcultarCamposPeriodo(<%=RelatorioSumariodt.getOpcaoRelatorio()%>)">
		<div id="divCorpo" class="divCorpo">
	  		<div class="area"><h2>&raquo; <%=request.getAttribute("tempPrograma")%> </h2></div>
			<form action="RelatorioSumarioProdutividade" method="post" name="Formulario" id="Formulario">
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>"/>
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>"/>
			<div id="divPortaBotoes" class="divPortaBotoes">
				<input id="imgNovo"  class="imgNovo" title="Nova Consulta" name="imaNovo" type="image" src="./imagens/imgNovo.png"  
						onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Novo)%>')" >
				<input id="imgImprimir"  class="imgImprimir" title="Gerar Relatório" name="imgImprimir" type="image" 
						src="./imagens/imgImprimir.png"   onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Imprimir)%>')">	

			</div/>
			<div id="divEditar" class="divEditar">
				
				<fieldset class="formEdicao">
					<legend class="formEdicaoLegenda">Agrupamento do Relatório</legend>
					<label for="AgrupamentoRelatorio" style="float:left;">*Agrupamento</label><br>
					<select id="AgrupamentoRelatorio" name="AgrupamentoRelatorio" class="formEdicaoCombo" onchange="javascript:atualizarCamposTela(document.getElementById('AgrupamentoRelatorio').value);">
						<option value="<%=RelatorioSumarioDt.COMARCA%>" <%if(RelatorioSumariodt.getAgrupamentoRelatorio().equals(String.valueOf(RelatorioSumarioDt.COMARCA))){%>selected="true"<%}%>>Comarca</option>
						<option value="<%=RelatorioSumarioDt.COMARCA_SERVENTIA%>" <%if(RelatorioSumariodt.getAgrupamentoRelatorio().equals(String.valueOf(RelatorioSumarioDt.COMARCA_SERVENTIA))){%>selected="true"<%}%>>Comarca-Serventia</option>
						<option value="<%=RelatorioSumarioDt.COMARCA_SERVENTIA_USUARIO%>" <%if(RelatorioSumariodt.getAgrupamentoRelatorio().equals(String.valueOf(RelatorioSumarioDt.COMARCA_SERVENTIA_USUARIO))){%>selected="true"<%}%>>Comarca-Serventia-Usuário</option>
					</select>					
				</fieldset>
				
				<fieldset class="formEdicao"> 
					<legend class="formEdicaoLegenda">Período do relatório</legend>
					<label class="formEdicaoLabel" for="tipo_Relatorio">*Período do relatório</label><br>  
					<input type="radio" name="OpcaoRelatorio" id="OpcaoRelatorio" value="<%=RelatorioSumarioDt.MES_FECHADO%>" onclick="javascript:mostrarOcultarCamposPeriodo(this.value)" 
						<%if(RelatorioSumariodt.getOpcaoRelatorio().equals(String.valueOf(RelatorioSumarioDt.MES_FECHADO))) {%> checked="true" <%} %>/>Mês fechado
				    <input type="radio" name="OpcaoRelatorio" id="OpcaoRelatorio" value="<%=RelatorioSumarioDt.DATA_INICIAL_FINAL%>" onclick="javascript:mostrarOcultarCamposPeriodo(this.value)" 
				    	<%if(RelatorioSumariodt.getOpcaoRelatorio().equals(String.valueOf(RelatorioSumarioDt.DATA_INICIAL_FINAL))) {%> checked="true" <%} %>/>Datas inicial e final
				</fieldset>
				
				<fieldset class="formEdicao" id="fieldsetMesFechado"> 
					
					<legend class="formEdicaoLegenda">Período</legend>
					
					<div class="periodo">
					<label for="DataInicial">*Mês/Ano Inicial</label><br> 
					<select id="MesInicial" name ="MesInicial" class="formEdicaoCombo">
						<option value="1" <%if(RelatorioSumariodt.getMesInicial().equals("1")){%>selected="true"<%}%>>Janeiro</option>
						<option value="2" <%if(RelatorioSumariodt.getMesInicial().equals("2")){%>selected="true"<%}%>>Fevereiro</option>
						<option value="3" <%if(RelatorioSumariodt.getMesInicial().equals("3")){%>selected="true"<%}%>>Março</option>
						<option value="4" <%if(RelatorioSumariodt.getMesInicial().equals("4")){%>selected="true"<%}%>>Abril</option>
						<option value="5" <%if(RelatorioSumariodt.getMesInicial().equals("5")){%>selected="true"<%}%> >Maio</option>
						<option value="6" <%if(RelatorioSumariodt.getMesInicial().equals("6")){%>selected="true"<%}%>>Junho</option>
						<option value="7" <%if(RelatorioSumariodt.getMesInicial().equals("7")){%>selected="true"<%}%>>Julho</option>
						<option value="8" <%if(RelatorioSumariodt.getMesInicial().equals("8")){%>selected="true"<%}%>>Agosto</option>
						<option value="9" <%if(RelatorioSumariodt.getMesInicial().equals("9")){%>selected="true"<%}%>>Setembro</option>
						<option value="10" <%if(RelatorioSumariodt.getMesInicial().equals("10")){%>selected="true"<%}%>>Outubro</option>
						<option value="11" <%if(RelatorioSumariodt.getMesInicial().equals("11")){%>selected="true"<%}%>>Novembro</option>
						<option value="12" <%if(RelatorioSumariodt.getMesInicial().equals("12")){%>selected="true"<%}%>>Dezembro</option>
					</select>
					<select id="AnoInicial" name ="AnoInicial" class="formEdicaoCombo" >
					<%
					for(int i = Funcoes.StringToInt(RelatorioSumariodt.getAnoInicial().toString()) ; i >= 1997; i--) { %>
						<option value="<%=i%>" <%if(RelatorioSumariodt.getAnoInicial().equals(String.valueOf(i))){%>selected="true"<%}%> ><%=i%></option>
					<%} %>
					</select>
						</div>
						<div class="periodo">				
					<label for="DataFinal">*Mês/Ano Final</label><br> 
					<select id="MesFinal" name ="MesFinal" class="formEdicaoCombo">
						<option value="1" <%if(RelatorioSumariodt.getMesFinal().equals("1")){%>selected="true"<%}%>>Janeiro</option>
						<option value="2" <%if(RelatorioSumariodt.getMesFinal().equals("2")){%>selected="true"<%}%>>Fevereiro</option>
						<option value="3" <%if(RelatorioSumariodt.getMesFinal().equals("3")){%>selected="true"<%}%>>Março</option>
						<option value="4" <%if(RelatorioSumariodt.getMesFinal().equals("4")){%>selected="true"<%}%>>Abril</option>
						<option value="5" <%if(RelatorioSumariodt.getMesFinal().equals("5")){%>selected="true"<%}%> >Maio</option>
						<option value="6" <%if(RelatorioSumariodt.getMesFinal().equals("6")){%>selected="true"<%}%>>Junho</option>
						<option value="7" <%if(RelatorioSumariodt.getMesFinal().equals("7")){%>selected="true"<%}%>>Julho</option>
						<option value="8" <%if(RelatorioSumariodt.getMesFinal().equals("8")){%>selected="true"<%}%>>Agosto</option>
						<option value="9" <%if(RelatorioSumariodt.getMesFinal().equals("9")){%>selected="true"<%}%>>Setembro</option>
						<option value="10" <%if(RelatorioSumariodt.getMesFinal().equals("10")){%>selected="true"<%}%>>Outubro</option>
						<option value="11" <%if(RelatorioSumariodt.getMesFinal().equals("11")){%>selected="true"<%}%>>Novembro</option>
						<option value="12" <%if(RelatorioSumariodt.getMesFinal().equals("12")){%>selected="true"<%}%>>Dezembro</option>
					</select>
					<select id="AnoFinal" name ="AnoFinal" class="formEdicaoCombo">
					<%
					for(int i = Funcoes.StringToInt(RelatorioSumariodt.getAnoFinal().toString()) ; i >= 1997; i--) { %>
						<option value="<%=i%>" <%if(RelatorioSumariodt.getAnoFinal().equals(String.valueOf(i))){%>selected="true"<%}%> ><%=i%></option>
					<%} %>
					</select>
					</div>
					<label for="Aviso" style="float:left;color:red;"><small>Período não pode superar 03(três) meses.</small></label><br>  
				
				</fieldset>
				
				<fieldset class="formEdicao" id="fieldsetDataInicialFinal"> 
					<legend class="formEdicaoLegenda">Período</legend>
					
					<label class="formLocalizarLabel" for="DataInicial">*Data Inicial</label>
				    <input class="formLocalizarInput" name="DataInicial" id="DataInicial" type="text" size="10" maxlength="10" value="<%=RelatorioSumariodt.getDataInicial()%>" /> 
				    <img id="calendarioDataInicial" class="calendario" src="./imagens/dlcalendar_2.gif" title="Data Inicial"  alt="Data Inicial" onclick="displayCalendar(document.forms[0].DataInicial,'dd/mm/yyyy',this)" />
					<br> 
					<label class="formLocalizarLabel" for="DataFinal">*Data Final</label>
				    <input class="formLocalizarInput" name="DataFinal" id="DataFinal" type="text" size="10" maxlength="10" value="<%=RelatorioSumariodt.getDataFinal()%>" /> 
				    <img id="calendarioDataFinal" class="calendario" src="./imagens/dlcalendar_2.gif" title="Data Final"  alt="Data Final" onclick="displayCalendar(document.forms[0].DataFinal,'dd/mm/yyyy',this)" />
					<br/>

					<label for="Aviso" style="float:left;color:red;"><small>ATENÇÃO: devido a sobrecarga de processamento, o intervalo entre as datas não pode ser superior a 90 (noventa) dias.</small></label><br> 
				</fieldset>
				
				<fieldset class="formEdicao"> 
					<legend class="formEdicaoLegenda">Comarca</legend>
					<label class="formEdicaoLabel" for="Id_Comarca">Comarca  
					<input class="FormEdicaoimgLocalizar" name="imaLocalizarComarca" type="image"  src="./imagens/imgLocalizarPequena.png" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ComarcaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" > 
					<input class="FormEdicaoimgLocalizar" id="imaLimparComarca" name="imaLimparComarca" type="image"  src="./imagens/16x16/edit-clear.png"  onclick="LimparChaveEstrangeira('Id_Comarca','Comarca'); return false;" />
					</label><br>
					<input id="Id_Comarca" name="Id_Comarca" type="hidden" value="<%=RelatorioSumariodt.getId_Comarca()%>"/>
					<input  class="formEdicaoInputSomenteLeitura" id="Comarca" name="Comarca" readonly="true" type="text" size="60" maxlength="60" value="<%=RelatorioSumariodt.getComarca()%>"/><br />
				</fieldset>
				
				<fieldset class="formEdicao" id="fieldsetServentia" > 
					<legend class="formEdicaoLegenda">Serventia</legend>
					<label class="formEdicaoLabel" for="Id_Serventia">Serventia (Vara) 
					<input class="FormEdicaoimgLocalizar" id="imaLocalizarServentia" name="imaLocalizarServentia" type="image"  src="./imagens/imgLocalizarPequena.png" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" > 
					<input class="FormEdicaoimgLocalizar" id="imaLimparServentia" name="imaLimparServentia" type="image"  src="./imagens/16x16/edit-clear.png"  onclick="LimparChaveEstrangeira('Id_Serventia','Serventia'); return false;" />
					</label><br> 
					<input id="Id_Serventia" name="Id_Serventia" type="hidden" value="<%=RelatorioSumariodt.getId_Serventia()%>"/>
					<input class="formEdicaoInputSomenteLeitura" id="Serventia" name="Serventia" readonly="true" type="text" size="60" maxlength="60" value="<%=RelatorioSumariodt.getServentia()%>"/><br />
				</fieldset> 
				
				<fieldset class="formEdicao" id="fieldsetGrupo"> 
				    <legend class="formEdicaoLegenda">Usuário</legend>
				    <label class="formEdicaoLabel" for="Id_Usuario">Servidor Judiciário 
				    <input class="FormEdicaoimgLocalizar" id="imaLocalizarUsuario" name="imaLocalizarUsuario" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(UsuarioDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" >  
				    <input class="FormEdicaoimgLocalizar" id="imaLimparUsuario" name="imaLimparUsuario" type="image"  src="./imagens/16x16/edit-clear.png"  onclick="LimparChaveEstrangeira('Id_Usuario','Usuario'); return false;" />
				    </label><br> 
				    <input id="Id_Usuario" name="Id_Usuario" type="hidden" value="<%=RelatorioSumariodt.getUsuario().getId()%>"/>
				    <input  class="formEdicaoInputSomenteLeitura" id="Usuario" name="Usuario" readonly="true" type="text" size="60" maxlength="60" value="<%=RelatorioSumariodt.getUsuario().getNome()%>"/>
				    <br />
				    <label for="Aviso" style="float:left;color:red;"><small>ATENÇÃO: se seu nome não aparece na busca de servidor, verifique se sua matrícula do TJGO consta em seu cadastro de usuário.</small></label><br>
				</fieldset> 
				
				<fieldset class="formEdicao"> 
					<legend class="formEdicaoLegenda">Estatística de Produtividade</legend>
					<label class="formEdicaoLabel" for="Id_ItemEstatistica">Estatística de Produtividade 
					<input class="FormEdicaoimgLocalizar" name="imaLocalizarCargoTipo" type="image"  src="./imagens/imgLocalizarPequena.png" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(EstatisticaProdutividadeItemDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" > 
					<input class="FormEdicaoimgLocalizar" id="imaLimparCodigoTipo" name="imaLimparCodigoTipo" type="image"  src="./imagens/16x16/edit-clear.png"  onclick="LimparChaveEstrangeira('Id_ItemEstatistica','ItemEstatistica'); return false;" />
					</label><br> 
					<input id="Id_ItemEstatistica" name="Id_ItemEstatistica" type="hidden" value="<%=RelatorioSumariodt.getIdItemEstatistica()%>"/>
					<input  class="formEdicaoInputSomenteLeitura" id="ItemEstatistica" name="ItemEstatistica" readonly="true" type="text" size="60" maxlength="60" value="<%=RelatorioSumariodt.getItemEstatistica()%>"/>
					<br />
					<label for="Aviso" style="float:left;color:red;"><small>ATENÇÃO: a partir de 01/2020 utilizar os itens Audiências Designadas(50007) e Audiências Realizadas(50008) para as estatísticas de audiências.</small></label><br>
				</fieldset>
				
				<fieldset class="formEdicao"> 
					<legend class="formEdicaoLegenda">*Tipo de Arquivo</legend>
					<label class="formEdicaoLabel" for="tipo_Arquivo">Tipo de Arquivo</label><br>  
					<input type="radio" name="tipo_Arquivo" value="1" checked="true"/>Relatório 
				    <input type="radio" name="tipo_Arquivo" value="2"/>Texto
			    </fieldset>
				 	
			  </div>
			<%if (request.getAttribute("MensagemOk").toString().trim().equals("") == false){ %>
				<div class="divMensagemOk" id="MensagemOk"><%=request.getAttribute("MensagemOk").toString().trim()%></div>
			<%}%>
			</form>
			<%@ include file="Padroes/Mensagens.jspf" %>
		</div>
	</body>
</html>