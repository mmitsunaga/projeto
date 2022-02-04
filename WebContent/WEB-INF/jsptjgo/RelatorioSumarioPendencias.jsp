<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<%@page  import="java.util.List"%>

<jsp:useBean id="RelatorioSumariodt" scope="session" class= "br.gov.go.tj.projudi.dt.relatorios.RelatorioSumarioDt"/>

<%@page import="br.gov.go.tj.projudi.dt.ComarcaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.UsuarioDt"%>
<%@page import="br.gov.go.tj.projudi.dt.PendenciaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.relatorios.RelatorioSumarioDt"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>


<%@page import="br.gov.go.tj.projudi.dt.PendenciaTipoDt"%><html>
	<head>
		<title> <%=request.getAttribute("tempPrograma")%> </title>
		<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./js/jscalendar/dhtmlgoodies_calendar.css');
		</style>
		
		
		<script type='text/javascript' src='./js/Funcoes.js'></script>
		<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js" ></script>
		<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>
		<script type="text/javascript" src="./js/jscalendar/dhtmlgoodies_calendar.js"></script>
		<script type='text/javascript' src='./js/jquery.js'></script>
   		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script> 
   		
   		<script language="javascript" type="text/javascript">
		function atualizarCamposTela(opcaoAgrupamento) {
			if(opcaoAgrupamento == <%=RelatorioSumarioDt.COMARCA%> || opcaoAgrupamento == <%=RelatorioSumarioDt.COMARCA_SERVENTIA%>){
				$("#fieldsetGrupo").css('display','none');
				document.getElementById('Id_Usuario').value = '';
				document.getElementById('Usuario').value = '';
			} else if(opcaoAgrupamento == <%=RelatorioSumarioDt.COMARCA_SERVENTIA_USUARIO%>){
				$("#fieldsetGrupo").css('display','block');
			}
		}
		</script>
		
	</head>
	<body onload="javascript:atualizarCamposTela(<%=RelatorioSumariodt.getAgrupamentoRelatorio()%>);">
		<div id="divCorpo" class="divCorpo" >
	  		<div class="area"><h2>&raquo; <%=request.getAttribute("tempPrograma")%> </h2></div>
			<form action="RelatorioSumarioPendencia" method="post" name="Formulario" id="Formulario">
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>"/>
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>"/>
			<div id="divPortaBotoes" class="divPortaBotoes">
				<input id="imgNovo"  class="imgNovo" title="Nova Consulta" name="imaNovo" type="image" src="./imagens/imgNovo.png"  
						onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Novo)%>')" >
				<input id="imgImprimir"  class="imgImprimir" title="Gerar Relatório" name="imgImprimir" type="image" 
						src="./imagens/imgImprimir.png"   onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Imprimir)%>')">	

			</div><br />
			<div id="divEditar" class="divEditar">
				
				<input type="hidden" id="AgrupamentoRelatorio" name="AgrupamentoRelatorio" value="<%=RelatorioSumariodt.getAgrupamentoRelatorio()%>"/>
									
				<fieldset class="formEdicao"> 
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
					<label for="Aviso" style="float:left;margin-left:25px;"><small>Período não pode superar 03(três) meses.</small></label><br>  
				
				</fieldset>
				
				<%--<fieldset class="formEdicao">
				<legend class="formEdicaoLegenda">Sistema</legend>
				<label for="Sistema" style="float:left;">Sistema</label><br>
				<select id="Sistema" name="Sistema" class="formEdicaoCombo">
					<option value="<%=RelatorioSumariodt.getId_Sistema()%>" <%if(RelatorioSumariodt.getId_Sistema().equals("1")){%>selected="true"<%}%>>PROJUDI</option>
					<option value="<%=RelatorioSumariodt.getId_Sistema()%>" <%if(RelatorioSumariodt.getId_Sistema().equals("2")){%>selected="true"<%}%>>SPG</option>
					<option value="<%=RelatorioSumariodt.getId_Sistema()%>" <%if(RelatorioSumariodt.getId_Sistema().equals("?")){%>selected="true"<%}%>>PROJUDI-SPG</option>
				</select>					
				</fieldset>--%>
				
				<fieldset class="formEdicao" id="fieldsetGrupo"> 
				    <legend class="formEdicaoLegenda">Usuário</legend>
				    <label class="formEdicaoLabel" for="Id_Usuario">Servidor Judiciário
				    <input class="FormEdicaoimgLocalizar" name="imaLocalizarServentia" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(UsuarioDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" >  
				    <input class="FormEdicaoimgLocalizar" id="imaLimparUsuario" name="imaLimparUsuario" type="image"  src="./imagens/16x16/edit-clear.png"  onclick="LimparChaveEstrangeira('Id_Usuario','Usuario'); return false;" />
				    </label><br>  
				    
				    <input id="Id_Usuario" name="Id_Usuario" type="hidden" value="<%=RelatorioSumariodt.getUsuario().getId()%>"/>
				    <input  class="formEdicaoInputSomenteLeitura" id="Usuario" name="Usuario" readonly="true" type="text" size="60" maxlength="60" value="<%=RelatorioSumariodt.getUsuario().getNome()%>"/>
				</fieldset>
				
				<fieldset class="formEdicao" id="fieldsetPendencia"> 
					<legend class="formEdicaoLegenda">Tipo de Pendência</legend>
					<label class="formEdicaoLabel" for="Id_PendenciaTipo">Tipo de Pendência
					 <input class="FormEdicaoimgLocalizar" name="imaLocalizarPendenciaTipo" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(PendenciaTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" >  
				    <input class="FormEdicaoimgLocalizar" id="imaLimparPendenciaTipo" name="imaLimparPendenciaTipo" type="image"  src="./imagens/16x16/edit-clear.png"  onclick="LimparChaveEstrangeira('Id_PendenciaTipo','PendenciaTipo'); return false;" />
					</label><br>  
				   
				    <input id="Id_PendenciaTipo" name="Id_PendenciaTipo" type="hidden" value="<%=RelatorioSumariodt.getId_PendenciaTipo()%>"/>
				    <input  class="formEdicaoInputSomenteLeitura" id="PendenciaTipo" name="PendenciaTipo" readonly="true" type="text" size="60" maxlength="60" value="<%=RelatorioSumariodt.getPendenciaTipo()%>"/>
				</fieldset>
				
				<fieldset class="formEdicao"> 
					<legend class="formEdicaoLegenda">Tipo de Arquivo</legend>
					<label class="formEdicaoLabel" for="tipo_Arquivo">Tipo de Arquivo</label><br>  
					<input type="radio" name="tipo_Arquivo" value="1" checked="true"/>Relatório 
				    <input type="radio" name="tipo_Arquivo" value="2"/>Texto
			    </fieldset>
			    		 	
			  </div>
			
			</form>
		</div>
		<%@ include file="Padroes/Mensagens.jspf" %>
	</body>
</html>