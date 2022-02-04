<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.AudienciaTipoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaCargoDt"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>

<jsp:useBean id="AudienciaAgendaDt" scope="session" class= "br.gov.go.tj.projudi.dt.AudienciaAgendaDt" />

<html>
	<head>
    	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE" />
    	
		<title>Agendas para Audiências</title>
    	
    	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
    	
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./css/Paginacao.css');
			@import url('./js/jscalendar/dhtmlgoodies_calendar.css?random=20051112');
		</style>
		
      	
      	
      	<script type='text/javascript' src='./js/Funcoes.js'></script>
		<script type='text/javascript' src='./js/jquery.js'></script>
   		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>
      	<script type="text/javascript" src="./js/Digitacao/MascararHoraResumida.js"></script>
		<script type="text/javascript" src="./js/Digitacao/DigitarHoraResumida.js"></script>
		<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js"></script>
		<script type="text/javascript" src="./js/Digitacao/AutoTab.js"></script>
		<script type="text/javascript" src="./js/Digitacao/DigitarData.js"></script>
		<script type="text/javascript" src="./js/jscalendar/dhtmlgoodies_calendar.js?random=20060118"></script>
		
	</head>
	
	<body>
		<div id="divCorpo" class="divCorpo" />
			<!-- Título da Página -->
	  		<div class="area"><h2>&raquo;Geração de Agendas para Audiências</h2></div>
			
			<form name="Formulario" id="Formulario" method="post" action="<%=request.getAttribute("tempRetorno")%>" >
				
				<input type="hidden"  id="PaginaAtual" name="PaginaAtual" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input type="hidden"  id="PaginaAnterior" name="PaginaAnterior" value="<%=String.valueOf(Configuracao.Editar)%>" />
				<input type="hidden" name="TituloPagina" value="<%=request.getAttribute("tempTituloPagina")%>" />	
				<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />						

				<div id="divPortaBotoes" class="divPortaBotoes">
					<input id="imgNovo"  class="imgNovo" title="Novo - Limpa os campos da tela" name="imaNovo" type="image" src="./imagens/imgNovo.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Novo)%>');" />
					<input id="imgSalvar" class="imgSalvar" title="Salvar - Criar as agendas para audiências" name="imasalvar" type="image"  src="./imagens/imgSalvar.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Salvar)%>')" />
					<input id="imgLocalizar" class="imgLocalizar" title="Localizar - Localiza as agendas livres para audiências" name="imaLocalizar" type="image"  src="./imagens/imgLocalizar.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Localizar)%>')" />
					<input id="imgAtualizar" class="imgAtualizar" title="Atualizar - Atualiza os dados da tela" name="imaAtualizar" type="image"  src="./imagens/imgAtualizar.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Editar)%>')" />
				</div>
				<br />
				
				<div class="divEditar" id="divEditar">
					<!-- Fieldset geral -->
					<fieldset class="formEdicao">
						<legend>SERVENTIA: <%=AudienciaAgendaDt.getServentia()%></legend>
						
						<!-- Fieldset serventia cargo e audiência tipo -->
						
							<!-- Audiência tipo -->
							<label class="formLocalizarLabel">*Tipo da Audiência
							
							<input class="FormEdicaoimgLocalizar" id="imaLocalizarAudienciaTipo" name="imaLocalizarAudienciaTipo" readonly type="image"  src="./imagens/imgLocalizarPequena.png" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(AudienciaTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>');" />
							<input class="FormEdicaoimgLocalizar" id="imaLimparAudienciaTipo" name="imaLimparAudienciaTipo" type="image"  src="./imagens/16x16/edit-clear.png"  onclick="LimparChaveEstrangeira('Id_AudienciaTipo','AudienciaTipo'); return false;" />  </label><br>
							
							<input class="formEdicaoInputSomenteLeitura" name="AudienciaTipo" id="AudienciaTipo" type="text" size="87" maxlength="100" value="<%=AudienciaAgendaDt.getAudienciaTipo()%>" />
							<input type="hidden" id="Id_AudienciaTipo" name="Id_AudienciaTipo" value="" />
							<br />
							
							<!-- Serventia cargo -->
							<% if (request.getAttribute("podeSelecionarCargo") != null && (Funcoes.StringToBoolean(request.getAttribute("podeSelecionarCargo").toString()) == true)){ %>
							<label class="formLocalizarLabel">*Cargo da Serventia
							<input class="FormEdicaoimgLocalizar" id="imaLocalizarServentiaCargo" name="imaLocalizarServentiaCargo" readonly type="image"  src="./imagens/imgLocalizarPequena.png" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ServentiaCargoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>');" />
							<input class="FormEdicaoimgLocalizar" id="imaLimparServentiaCargo" name="imaLimparServentiaCargo" type="image"  src="./imagens/16x16/edit-clear.png"  onclick="LimparChaveEstrangeira('Id_ServentiaCargo','ServentiaCargo'); return false;" />  
							</label><br>
							
							<input class="formEdicaoInputSomenteLeitura" name="ServentiaCargo" id="ServentiaCargo" type="text" size="87" maxlength="100" value="<%=AudienciaAgendaDt.getAudienciaProcessoDt().getServentiaCargo()%>" />
							<input type="hidden" id="Id_ServentiaCargo" name="Id_ServentiaCargo" value="" />
							<br />
							<% } %>
						
						<!-- Fim fieldset serventia cargo e audiência tipo -->
												
						<!-- Fieldset período -->
						<fieldset>
							<legend>*Período</legend>
							<label>Quantidade</label><br> 
							<input name="QuantidadeAudiencias" title="Quantidade de audiências por horário" maxlength="3" size="10" onkeypress="return DigitarSoNumero(this, event)" value="<%=AudienciaAgendaDt.getQuantidadeAudienciasSimultaneas()%>" />
							<label for="Aviso"  ><br><small>* Quantidade de audiêcias que serão realizadas simultaneamente. Valor máximo: <%=AudienciaAgendaDt.getQuantidadeMaximaAudienciasSimultaneas()%>.</small></label><br> 
							<br />
							<div class="col15">
							<label>Data Inicial</label><br>
							<input id="DataInicial" name="DataInicial" size="10" maxlength="10" title="Clique para escolher uma data."  value="<%=AudienciaAgendaDt.getDataInicial()%>" onkeyup="mascara_data(this)" onkeypress="return DigitarSoNumero(this, event)" />
							<img id="calDataInicial" src="./imagens/dlcalendar_2.gif" height="15" width="15" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].DataInicial,'dd/mm/yyyy',this)" />
							</div>
							 <div class="col15">
							<label>Data Final</label><br>
							<input id="DataFinal" name="DataFinal" size="10" maxlength="10" title="Clique para escolher uma data."  value="<%=AudienciaAgendaDt.getDataFinal()%>" onkeyup="mascara_data(this)" onkeypress="return DigitarSoNumero(this, event)" /> 
							<img id="calDataFinal" src="./imagens/dlcalendar_2.gif" height="15" width="15" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].DataFinal,'dd/mm/yyyy',this)" />
							</div>
							<label for="Aviso"  ><br><small>* Período entre Data Inicial e Data Final não pode superar 90(noventa) dias.</small></label><br> 
						</fieldset>
						<!-- Fim fieldset período -->
						<br />
								
						<!-- Fieldset horários -->
						<fieldset>
							<legend>*Horários</legend> 							
								
							<!-- Fieldset segunda -->
							<fieldset class="fieldAgenda">
								<legend>SEG</legend> 
								<label class="">Inicial</label><br> 
								<input name="HorariosDuracao" 
									index="3" maxlength="5" size="5" 
									onkeypress="return DigitarHoraResumida(this, event)"
									onkeyup="MascararHoraResumida(this); autoTab(this,5)" 
									value="<%=AudienciaAgendaDt.getHorariosDuracao() != null?AudienciaAgendaDt.getHorariosDuracao()[0].equals(null)?"":AudienciaAgendaDt.getHorariosDuracao()[0]:""%>" />
									<br />
								<label class="">Final</label><br> 
								<input name="HorariosDuracao" 
									index="4" maxlength="5" size="5" 
									onkeypress="return DigitarHoraResumida(this, event)"
									onkeyup="MascararHoraResumida(this); autoTab(this,5)" 
									value="<%=AudienciaAgendaDt.getHorariosDuracao() != null?AudienciaAgendaDt.getHorariosDuracao()[1].equals(null)?"":AudienciaAgendaDt.getHorariosDuracao()[1]:""%>" />
								<br />
								<label class="">Duração</label><br> 
								<input name="HorariosDuracao" title="Informe o tempo em minutos para cada audiência. Ex. 15"
									index="5" maxlength="5" size="5" 
									onkeypress="return DigitarSoNumero(this, event)"
									onkeyup="autoTab(this,5)" 
									value="<%=AudienciaAgendaDt.getHorariosDuracao() != null?AudienciaAgendaDt.getHorariosDuracao()[2].equals(null)?"":AudienciaAgendaDt.getHorariosDuracao()[2]:""%>" />
							</fieldset>
							<!-- Fim fieldset segunda -->
								
							<!-- Fieldset terça -->
							<fieldset class="fieldAgenda">
								<legend>TER</legend> 
								<label>Inicial</label><br>
								<input name="HorariosDuracao" 
									index="6" maxlength="5" size="5" 
									onkeypress="return DigitarHoraResumida(this, event)"
									onkeyup="MascararHoraResumida(this); autoTab(this,5)" 
									value="<%=AudienciaAgendaDt.getHorariosDuracao() != null?AudienciaAgendaDt.getHorariosDuracao()[3].equals(null)?"":AudienciaAgendaDt.getHorariosDuracao()[3]:""%>" />
								<br />
								<label class="">Final</label><br>
								<input name="HorariosDuracao" 
									index="7" maxlength="5" size="5" 
									onkeypress="return DigitarHoraResumida(this, event)"
									onkeyup="MascararHoraResumida(this); autoTab(this,5)" 
									value="<%=AudienciaAgendaDt.getHorariosDuracao() != null?AudienciaAgendaDt.getHorariosDuracao()[4].equals(null)?"":AudienciaAgendaDt.getHorariosDuracao()[4]:""%>" />
								<br />
								<label class="">Duração</label><br>
								<input name="HorariosDuracao" title="Informe o tempo em minutos para cada audiência. Ex. 15"
									index="8" maxlength="5" size="5" 
									onkeypress="return DigitarSoNumero(this, event)"
									onkeyup="autoTab(this,5)" 
									value="<%=AudienciaAgendaDt.getHorariosDuracao() != null?AudienciaAgendaDt.getHorariosDuracao()[5].equals(null)?"":AudienciaAgendaDt.getHorariosDuracao()[5]:""%>" />
							</fieldset>
							<!-- Fim fieldset terça -->
							
							<!-- Fieldset quarta -->
							<fieldset class="fieldAgenda">
								<legend>QUA</legend> 
								<label class="">Inicial</label><br>
								<input name="HorariosDuracao" 
									index="9" maxlength="5" size="5" 
									onkeypress="return DigitarHoraResumida(this, event)"
									onkeyup="MascararHoraResumida(this); autoTab(this,5)" 
									value="<%=AudienciaAgendaDt.getHorariosDuracao() != null?AudienciaAgendaDt.getHorariosDuracao()[6].equals(null)?"":AudienciaAgendaDt.getHorariosDuracao()[6]:""%>" />
								<br />
								<label class="">Final</label><br>
								<input name="HorariosDuracao" 
									index="10" maxlength="5" size="5" 
									onkeypress="return DigitarHoraResumida(this, event)"
									onkeyup="MascararHoraResumida(this); autoTab(this,5)" 
									value="<%=AudienciaAgendaDt.getHorariosDuracao() != null?AudienciaAgendaDt.getHorariosDuracao()[7].equals(null)?"":AudienciaAgendaDt.getHorariosDuracao()[7]:""%>" />
								<br />
								<label class="">Duração</label><br>
								<input name="HorariosDuracao" title="Informe o tempo em minutos para cada audiência. Ex. 15"
									index="11" maxlength="5" size="5" 
									onkeypress="return DigitarSoNumero(this, event)"
									onkeyup="autoTab(this,5)" 
									value="<%=AudienciaAgendaDt.getHorariosDuracao() != null?AudienciaAgendaDt.getHorariosDuracao()[8].equals(null)?"":AudienciaAgendaDt.getHorariosDuracao()[8]:""%>" />
							</fieldset>
							<!-- Fim fieldset quarta -->
								
							<!-- Fieldset quinta -->
							<fieldset class="fieldAgenda">
								<legend>QUI</legend>
								<label class="">Inicial</label><br>
								<input name="HorariosDuracao" 
									index="12" maxlength="5" size="5" 
									onkeypress="return DigitarHoraResumida(this, event)"
									onkeyup="MascararHoraResumida(this); autoTab(this,5)" 
									value="<%=AudienciaAgendaDt.getHorariosDuracao() != null?AudienciaAgendaDt.getHorariosDuracao()[9].equals(null)?"":AudienciaAgendaDt.getHorariosDuracao()[9]:""%>" />
								<br />
								<label class="">Final</label><br>
								<input name="HorariosDuracao" 
									index="13" maxlength="5" size="5" 
									onkeypress="return DigitarHoraResumida(this, event)"
									onkeyup="MascararHoraResumida(this); autoTab(this,5)" 
									value="<%=AudienciaAgendaDt.getHorariosDuracao() != null?AudienciaAgendaDt.getHorariosDuracao()[10].equals(null)?"":AudienciaAgendaDt.getHorariosDuracao()[10]:""%>" />
								<br />
								<label class="">Duração</label><br>
								<input name="HorariosDuracao" title="Informe o tempo em minutos para cada audiência. Ex. 15"
									index="14" maxlength="5" size="5" 
									onkeypress="return DigitarSoNumero(this, event)"
									onkeyup="autoTab(this,5)" 
									value="<%=AudienciaAgendaDt.getHorariosDuracao() != null?AudienciaAgendaDt.getHorariosDuracao()[11].equals(null)?"":AudienciaAgendaDt.getHorariosDuracao()[11]:""%>" />
							</fieldset>
							<!-- Fim fieldset quinta -->
								
							<!-- Fieldset sexta -->
								<fieldset class="fieldAgenda">
								<legend>SEX</legend>
								<label class="">Inicial</label><br>
								<input name="HorariosDuracao" 
									index="15" maxlength="5" size="5" 
									onkeypress="return DigitarHoraResumida(this, event)"
									onkeyup="MascararHoraResumida(this); autoTab(this,5)" 
									value="<%=AudienciaAgendaDt.getHorariosDuracao() != null?AudienciaAgendaDt.getHorariosDuracao()[12].equals(null)?"":AudienciaAgendaDt.getHorariosDuracao()[12]:""%>" />
								<br />
								<label class="">Final</label><br>
								<input name="HorariosDuracao" 
									index="16" maxlength="5" size="5" 
									onkeypress="return DigitarHoraResumida(this, event)"
									onkeyup="MascararHoraResumida(this); autoTab(this,5)" 
									value="<%=AudienciaAgendaDt.getHorariosDuracao() != null?AudienciaAgendaDt.getHorariosDuracao()[13].equals(null)?"":AudienciaAgendaDt.getHorariosDuracao()[13]:""%>" />
								<br />
								<label class="">Duração</label><br>
								<input name="HorariosDuracao" title="Informe o tempo em minutos para cada audiência. Ex. 15"
									index="17" maxlength="5" size="5" 
									onkeypress="return DigitarSoNumero(this, event)"
									onkeyup="autoTab(this,5)" 
									value="<%=AudienciaAgendaDt.getHorariosDuracao() != null?AudienciaAgendaDt.getHorariosDuracao()[14].equals(null)?"":AudienciaAgendaDt.getHorariosDuracao()[14]:""%>" />
							</fieldset>
							<!-- Fim fieldset sexta -->
									
							<!-- Fieldset sábado -->
							<fieldset class="fieldAgenda">
								<legend>SÁB</legend> 
								<label class="">Inicial</label><br>
								<input name="HorariosDuracao" 
									index="18" maxlength="5" size="5" 
									onkeypress="return DigitarHoraResumida(this, event)"
									onkeyup="MascararHoraResumida(this); autoTab(this,5)" 
									value="<%=AudienciaAgendaDt.getHorariosDuracao() != null?AudienciaAgendaDt.getHorariosDuracao()[15].equals(null)?"":AudienciaAgendaDt.getHorariosDuracao()[15]:""%>" />
								<br />
								<label class="">Final</label><br>
								<input name="HorariosDuracao" 
									index="19" maxlength="5" size="5" 
									onkeypress="return DigitarHoraResumida(this, event)"
									onkeyup="MascararHoraResumida(this); autoTab(this,5)" 
									value="<%=AudienciaAgendaDt.getHorariosDuracao() != null?AudienciaAgendaDt.getHorariosDuracao()[16].equals(null)?"":AudienciaAgendaDt.getHorariosDuracao()[16]:""%>" />
								<br />
								<label class="">Duração</label><br>
								<input name="HorariosDuracao" title="Informe o tempo em minutos para cada audiência. Ex. 15"
									index="20" maxlength="5" size="5" 
									onkeypress="return DigitarSoNumero(this, event)"
									onkeyup="autoTab(this,5)" 
									value="<%=AudienciaAgendaDt.getHorariosDuracao() != null?AudienciaAgendaDt.getHorariosDuracao()[17].equals(null)?"":AudienciaAgendaDt.getHorariosDuracao()[17]:""%>" />
							</fieldset>
							<!-- Fim fieldset sábado -->
                            							<!-- Fieldset domingo -->
							<fieldset class="fieldAgenda">
								<legend>DOM</legend> 
								<label class="">Inicial</label><br>
								<input name="HorariosDuracao" 
									index="0" maxlength="5" size="5" 
									onkeypress="return DigitarHoraResumida(this, event)"
									onkeyup="MascararHoraResumida(this); autoTab(this,5)" 
									value="<%=AudienciaAgendaDt.getHorariosDuracao() != null?AudienciaAgendaDt.getHorariosDuracao()[18].equals(null)?"":AudienciaAgendaDt.getHorariosDuracao()[18]:""%>" />
								<br />
								<label class="">Final</label><br>
								<input name="HorariosDuracao" 
									index="1" maxlength="5" size="5" 
									onkeypress="return DigitarHoraResumida(this, event)"
									onkeyup="MascararHoraResumida(this); autoTab(this,5)" 
									value="<%=AudienciaAgendaDt.getHorariosDuracao() != null?AudienciaAgendaDt.getHorariosDuracao()[19].equals(null)?"":AudienciaAgendaDt.getHorariosDuracao()[19]:""%>" />
								<br />
								<label class="">Duração</label><br>
								<input name="HorariosDuracao" title="Informe o tempo em minutos para cada audiência. Ex. 15"
									index="2" maxlength="5" size="5" 
									onkeypress="return DigitarSoNumero(this, event)"
									onkeyup="autoTab(this,5)" 
									value="<%=AudienciaAgendaDt.getHorariosDuracao() != null?AudienciaAgendaDt.getHorariosDuracao()[20].equals(null)?"":AudienciaAgendaDt.getHorariosDuracao()[20]:""%>" />
							</fieldset>
								
						</fieldset>
						<br />
						
					</fieldset>
					
					<%@ include file="Padroes/ConfirmarOperacao.jspf"%>
					
				</div>
				<br />

				
			</form>
		</div>
		
		<%@include file="Padroes/Mensagens.jspf"%> 
		
	</body>
</html>
