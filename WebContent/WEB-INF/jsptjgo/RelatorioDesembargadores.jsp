<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<%@page  import="java.util.List"%>
<%@page import="br.gov.go.tj.projudi.dt.relatorios.RelatorioDesembargadoresDt"%>
<%@page import="br.gov.go.tj.projudi.dt.EstatisticaProdutividadeItemDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaDt"%>
<%@page import="java.util.HashMap"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>

<jsp:useBean id="relatorioDesembargadoresDt" scope="session" class= "br.gov.go.tj.projudi.dt.relatorios.RelatorioDesembargadoresDt"/>
<html>
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
		function atualizarCamposTela(tipoRelatorio) {
			if(tipoRelatorio == <%=RelatorioDesembargadoresDt.REALIZADAS%> || tipoRelatorio == <%=RelatorioDesembargadoresDt.RECEBIDAS%>){
				$("#fieldsetData").css('display','block');
			} else if(tipoRelatorio == <%=RelatorioDesembargadoresDt.PENDENTES%>){
				$("#fieldsetData").css('display','none');
				document.getElementById('AnoInicial').value = '';
				document.getElementById('MesInicial').value = '';
				document.getElementById('AnoFinal').value = '';
				document.getElementById('MesInicial').value = '';
			}
		}
		</script>
		
	</head>
	<body onload="javascript:atualizarCamposTela(<%=relatorioDesembargadoresDt.getTipoRelatorio()%>);">
		<div id="divCorpo" class="divCorpo" >
	  		<div class="area"><h2>&raquo; <%=request.getAttribute("tempPrograma")%> </h2></div>
			<form action="RelatorioConclusoesSegundoGrau" method="post" name="Formulario" id="Formulario">
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>"/>
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>"/>
			<!--  
			<div id="divPortaBotoes" class="divPortaBotoes">
				<input id="imgNovo"  class="imgNovo" title="Nova Consulta" name="imaNovo" type="image" src="./imagens/imgNovo.png"  
						onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Novo)%>')" >
				<input id="imgImprimir"  class="imgImprimir" title="Gerar Relatório" name="imgImprimir" type="image" 
						src="./imagens/imgImprimir.png"   onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Imprimir)%>')">	

			</div/><br />
			-->
			<div id="divEditar" class="divEditar">

				<input type="hidden" id="idServentia" name="IdServentia" value="<%=relatorioDesembargadoresDt.getIdServentia()%>"/>
				<input type="hidden" id="Serventia" name="Serventia" value="<%=relatorioDesembargadoresDt.getServentia()%>"/>
				
				<!-- Campo que informa qual relatório será impresso -->
				<input type="hidden" id="RelatorioSelecionado" name="RelatorioSelecionado" value=""/>
				
				<fieldset class="formEdicao"> 
					<legend class="formEdicaoLegenda">Relatório de Conclusões</legend>
					
					<input id="imgImprimir"  class="imgImprimir" title="Gerar Relatório" name="imgImprimir" type="image" align="center"
						src="./imagens/imgImprimir.png"   onclick="AlterarValue('RelatorioSelecionado','1');AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Imprimir)%>')">
					
					
					<fieldset> 
						<legend>*Tipo de Relatório</legend>
						<label class="formEdicaoLabel" for="TipoRelatorio">Tipo de Relatório</label><br>  
					    <!-- Não pode ser consultadas as realizadas, pois deve ser tirado no relatório de pendências (analítico e sumário)-->
					    <input type="radio" name="TipoRelatorio" value="<%=relatorioDesembargadoresDt.RECEBIDAS%>" onclick="javascript:atualizarCamposTela(this.value);"/>Recebidas
						<input type="radio" name="TipoRelatorio" value="<%=relatorioDesembargadoresDt.PENDENTES%>" onclick="javascript:atualizarCamposTela(this.value);" />Pendentes 
				    </fieldset>
					
					<fieldset id="fieldsetData"> 
						
						<legend class="formEdicaoLegenda">Período</legend>
						<div class="col30">
						<label for="DataInicialConclusao"  style="float:left;">*Mês/Ano Inicial</label><br> 
						<select id="MesInicialConclusao" name ="MesInicialConclusao" class="formEdicaoCombo">
							<option value="1" <%if(relatorioDesembargadoresDt.getMesInicial().equals("1")){%>selected="true"<%}%>>Janeiro</option>
							<option value="2" <%if(relatorioDesembargadoresDt.getMesInicial().equals("2")){%>selected="true"<%}%>>Fevereiro</option>
							<option value="3" <%if(relatorioDesembargadoresDt.getMesInicial().equals("3")){%>selected="true"<%}%>>Março</option>
							<option value="4" <%if(relatorioDesembargadoresDt.getMesInicial().equals("4")){%>selected="true"<%}%>>Abril</option>
							<option value="5" <%if(relatorioDesembargadoresDt.getMesInicial().equals("5")){%>selected="true"<%}%> >Maio</option>
							<option value="6" <%if(relatorioDesembargadoresDt.getMesInicial().equals("6")){%>selected="true"<%}%>>Junho</option>
							<option value="7" <%if(relatorioDesembargadoresDt.getMesInicial().equals("7")){%>selected="true"<%}%>>Julho</option>
							<option value="8" <%if(relatorioDesembargadoresDt.getMesInicial().equals("8")){%>selected="true"<%}%>>Agosto</option>
							<option value="9" <%if(relatorioDesembargadoresDt.getMesInicial().equals("9")){%>selected="true"<%}%>>Setembro</option>
							<option value="10" <%if(relatorioDesembargadoresDt.getMesInicial().equals("10")){%>selected="true"<%}%>>Outubro</option>
							<option value="11" <%if(relatorioDesembargadoresDt.getMesInicial().equals("11")){%>selected="true"<%}%>>Novembro</option>
							<option value="12" <%if(relatorioDesembargadoresDt.getMesInicial().equals("12")){%>selected="true"<%}%>>Dezembro</option>
						</select>
						<select id="AnoInicialConclusao" name ="AnoInicialConclusao" class="formEdicaoCombo" >
						<%
						for(int i = Funcoes.StringToInt(relatorioDesembargadoresDt.getAnoInicial().toString()) ; i >= 1997; i--) { %>
							<option value="<%=i%>" <%if(relatorioDesembargadoresDt.getAnoInicial().equals(String.valueOf(i))){%>selected="true"<%}%> ><%=i%></option>
						<%} %>
						</select>
						</div>
						
						<div class="col30">					
						<label for="DataFinalConclusao">*Mês/Ano Final</label><br> 
						<select id="MesFinalConclusao" name ="MesFinalConclusao" class="formEdicaoCombo">
							<option value="1" <%if(relatorioDesembargadoresDt.getMesFinal().equals("1")){%>selected="true"<%}%>>Janeiro</option>
							<option value="2" <%if(relatorioDesembargadoresDt.getMesFinal().equals("2")){%>selected="true"<%}%>>Fevereiro</option>
							<option value="3" <%if(relatorioDesembargadoresDt.getMesFinal().equals("3")){%>selected="true"<%}%>>Março</option>
							<option value="4" <%if(relatorioDesembargadoresDt.getMesFinal().equals("4")){%>selected="true"<%}%>>Abril</option>
							<option value="5" <%if(relatorioDesembargadoresDt.getMesFinal().equals("5")){%>selected="true"<%}%> >Maio</option>
							<option value="6" <%if(relatorioDesembargadoresDt.getMesFinal().equals("6")){%>selected="true"<%}%>>Junho</option>
							<option value="7" <%if(relatorioDesembargadoresDt.getMesFinal().equals("7")){%>selected="true"<%}%>>Julho</option>
							<option value="8" <%if(relatorioDesembargadoresDt.getMesFinal().equals("8")){%>selected="true"<%}%>>Agosto</option>
							<option value="9" <%if(relatorioDesembargadoresDt.getMesFinal().equals("9")){%>selected="true"<%}%>>Setembro</option>
							<option value="10" <%if(relatorioDesembargadoresDt.getMesFinal().equals("10")){%>selected="true"<%}%>>Outubro</option>
							<option value="11" <%if(relatorioDesembargadoresDt.getMesFinal().equals("11")){%>selected="true"<%}%>>Novembro</option>
							<option value="12" <%if(relatorioDesembargadoresDt.getMesFinal().equals("12")){%>selected="true"<%}%>>Dezembro</option>
						</select>
						<select id="AnoFinalConclusao" name ="AnoFinalConclusao" class="formEdicaoCombo">
						<%
						for(int i = Funcoes.StringToInt(relatorioDesembargadoresDt.getAnoFinal().toString()) ; i >= 1997; i--) { %>
							<option value="<%=i%>" <%if(relatorioDesembargadoresDt.getAnoFinal().equals(String.valueOf(i))){%>selected="true"<%}%> ><%=i%></option>
						<%} %>
						</select>
						</div>
						<div class="clear"></div>
						<label for="Aviso" style="float:left;margin-left:25px" ><small>Período não pode superar 03(três) meses.</small></label><br> 
					
					</fieldset>
						
				</fieldset>
				
				
				
				
				
				<fieldset class="formEdicao"> 
					<legend class="formEdicaoLegenda">Relatório de Produtividade</legend>
				
					<fieldset id="fieldsetData"> 
						
						<legend class="formEdicaoLegenda">Período</legend>
						
						<div class="col30">	
						<label for="DataInicialProdutividade"  style="float:left;">*Mês/Ano Inicial</label><br> 
						<select id="MesInicialProdutividade" name ="MesInicialProdutividade" class="formEdicaoCombo">
							<option value="1" <%if(relatorioDesembargadoresDt.getMesInicial().equals("1")){%>selected="true"<%}%>>Janeiro</option>
							<option value="2" <%if(relatorioDesembargadoresDt.getMesInicial().equals("2")){%>selected="true"<%}%>>Fevereiro</option>
							<option value="3" <%if(relatorioDesembargadoresDt.getMesInicial().equals("3")){%>selected="true"<%}%>>Março</option>
							<option value="4" <%if(relatorioDesembargadoresDt.getMesInicial().equals("4")){%>selected="true"<%}%>>Abril</option>
							<option value="5" <%if(relatorioDesembargadoresDt.getMesInicial().equals("5")){%>selected="true"<%}%> >Maio</option>
							<option value="6" <%if(relatorioDesembargadoresDt.getMesInicial().equals("6")){%>selected="true"<%}%>>Junho</option>
							<option value="7" <%if(relatorioDesembargadoresDt.getMesInicial().equals("7")){%>selected="true"<%}%>>Julho</option>
							<option value="8" <%if(relatorioDesembargadoresDt.getMesInicial().equals("8")){%>selected="true"<%}%>>Agosto</option>
							<option value="9" <%if(relatorioDesembargadoresDt.getMesInicial().equals("9")){%>selected="true"<%}%>>Setembro</option>
							<option value="10" <%if(relatorioDesembargadoresDt.getMesInicial().equals("10")){%>selected="true"<%}%>>Outubro</option>
							<option value="11" <%if(relatorioDesembargadoresDt.getMesInicial().equals("11")){%>selected="true"<%}%>>Novembro</option>
							<option value="12" <%if(relatorioDesembargadoresDt.getMesInicial().equals("12")){%>selected="true"<%}%>>Dezembro</option>
						</select>
						<select id="AnoInicialProdutividade" name ="AnoInicialProdutividade" class="formEdicaoCombo" >
						<%
						for(int i = Funcoes.StringToInt(relatorioDesembargadoresDt.getAnoInicial().toString()) ; i >= 1997; i--) { %>
							<option value="<%=i%>" <%if(relatorioDesembargadoresDt.getAnoInicial().equals(String.valueOf(i))){%>selected="true"<%}%> ><%=i%></option>
						<%} %>
						</select>
							</div>
							
							<div class="col30">					
						<label for="DataFinalProdutividade">*Mês/Ano Final</label><br> 
						<select id="MesFinalProdutividade" name ="MesFinalProdutividade" class="formEdicaoCombo">
							<option value="1" <%if(relatorioDesembargadoresDt.getMesFinal().equals("1")){%>selected="true"<%}%>>Janeiro</option>
							<option value="2" <%if(relatorioDesembargadoresDt.getMesFinal().equals("2")){%>selected="true"<%}%>>Fevereiro</option>
							<option value="3" <%if(relatorioDesembargadoresDt.getMesFinal().equals("3")){%>selected="true"<%}%>>Março</option>
							<option value="4" <%if(relatorioDesembargadoresDt.getMesFinal().equals("4")){%>selected="true"<%}%>>Abril</option>
							<option value="5" <%if(relatorioDesembargadoresDt.getMesFinal().equals("5")){%>selected="true"<%}%> >Maio</option>
							<option value="6" <%if(relatorioDesembargadoresDt.getMesFinal().equals("6")){%>selected="true"<%}%>>Junho</option>
							<option value="7" <%if(relatorioDesembargadoresDt.getMesFinal().equals("7")){%>selected="true"<%}%>>Julho</option>
							<option value="8" <%if(relatorioDesembargadoresDt.getMesFinal().equals("8")){%>selected="true"<%}%>>Agosto</option>
							<option value="9" <%if(relatorioDesembargadoresDt.getMesFinal().equals("9")){%>selected="true"<%}%>>Setembro</option>
							<option value="10" <%if(relatorioDesembargadoresDt.getMesFinal().equals("10")){%>selected="true"<%}%>>Outubro</option>
							<option value="11" <%if(relatorioDesembargadoresDt.getMesFinal().equals("11")){%>selected="true"<%}%>>Novembro</option>
							<option value="12" <%if(relatorioDesembargadoresDt.getMesFinal().equals("12")){%>selected="true"<%}%>>Dezembro</option>
						</select>
						<select id="AnoFinalProdutividade" name ="AnoFinalProdutividade" class="formEdicaoCombo">
						<%
						for(int i = Funcoes.StringToInt(relatorioDesembargadoresDt.getAnoFinal().toString()) ; i >= 1997; i--) { %>
							<option value="<%=i%>" <%if(relatorioDesembargadoresDt.getAnoFinal().equals(String.valueOf(i))){%>selected="true"<%}%> ><%=i%></option>
						<%} %>
						</select>
						</div>
						<div class="clear">	</div>
						<label for="Aviso" style="float:left;margin-left:25px" ><small>Período não pode superar 03(três) meses.</small></label><br> 
					
					</fieldset>
					
					<fieldset> 
						<legend class="formEdicaoLegenda">Estatística de Produtividade</legend>
						<label class="formEdicaoLabel" for="Id_ItemEstatistica">Estatística de Produtividade
						<input class="FormEdicaoimgLocalizar" name="imaLocalizarCargoTipo" type="image"  src="./imagens/imgLocalizarPequena.png" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(EstatisticaProdutividadeItemDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" > 
						<input class="FormEdicaoimgLocalizar" id="imaLimparCodigoTipo" name="imaLimparCodigoTipo" type="image"  src="./imagens/16x16/edit-clear.png"  onclick="LimparChaveEstrangeira('Id_ItemEstatistica','ItemEstatistica'); return false;" />
						</label><br>  
						
						<input id="Id_ItemEstatistica" name="Id_ItemEstatistica" type="hidden" value="<%=relatorioDesembargadoresDt.getIdItemEstatistica()%>"/>
						<input  class="formEdicaoInputSomenteLeitura" id="ItemEstatistica" name="ItemEstatistica" readonly="true" type="text" size="60" maxlength="60" value="<%=relatorioDesembargadoresDt.getItemEstatistica()%>"/>
						<br />
					</fieldset>
					
					<input id="imgImprimir"  class="imgImprimir" title="Gerar Relatório" name="imgImprimir" type="image" align="center"
						src="./imagens/imgImprimir.png"   onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Imprimir)%>')">
						
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