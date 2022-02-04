<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"  pageEncoding="ISO-8859-1"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.GuiaTipoDt"%>
<html>
<head>	
	<title> | Calculadora - Tempos e Datas |  </title>
	
		<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./css/menusimples.css');
			@import url('./css/geral.css');	
			@import url('./css/dropdown.css');
		</style>
	
  	<script type='text/javascript' src='./js/jquery.js'></script>
	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type='text/javascript' src='./js/Digitacao/DigitarData.js'></script>	
    <script type='text/javascript' src='./js/Digitacao/DigitarSoNumero.js'></script>
	<script type='text/javascript' src='./js/jquery.mask.min.js'></script>
    <script type='text/javascript' src='./js/CalculadoraTemposDatas.js'></script>
	<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>
	
	<script language="javascript" type="text/javascript" >
		$(document).ready(function() {
			$("#dataSoma").mask("99/99/9999");
			$("#data1").mask("99/99/9999");
			$("#data2").mask("99/99/9999");
			$("#horas1").mask("99:99");
			$("#horas2").mask("99:99");
		});
	
		function calculaFracao(idTempoDia){
			var tempo = document.getElementById(idTempoDia).value;
			document.getElementById('fracao16').value = parseInt(tempo/6, 10); 
			document.getElementById('fracao15').value = parseInt(tempo/5, 10);
			document.getElementById('fracao14').value = parseInt(tempo/4, 10);
			document.getElementById('fracao13').value = parseInt(tempo/3, 10);
			document.getElementById('fracao12').value = parseInt(tempo/2, 10);
			document.getElementById('fracao23').value = parseInt(tempo*2/3, 10);
			document.getElementById('fracao25').value = parseInt(tempo*2/5, 10);
			document.getElementById('fracao35').value = parseInt(tempo*3/5, 10);
			document.getElementById('fracao38').value = parseInt(tempo*3/8, 10);
			document.getElementById('fracao512').value = parseInt(tempo*5/12, 10);
			document.getElementById('fracao1124').value = parseInt(tempo*11/24, 10);
		}
		
		function VerificarCampos(idCampo) {
			if (idCampo == "Dias"){
				if (document.Formulario.Dias.value == ""){
					alert("Informe o tempo em Dias!"); 
					return false;
				} else return true;
			} 
			else if (idCampo == "dataSoma"){
				if (document.Formulario.dataSoma.value == "" || document.Formulario.diasSoma.value == "") {
					alert("Informe a data e /ou quantidade de dias!"); 
					return false;
				} else return true;
			}
			else if (idCampo == "data1"){
				if (document.Formulario.data1.value == "" || document.Formulario.data2.value == "") {
					alert("Informe a(s) data(s)!"); 
					return false;
				} else return true;
			}
		}

		function limpar(){
			 document.getElementById('horas1').value = "";
			 document.getElementById('horas2').value = "";
			 document.getElementById('somaHora').value = "";
			return false;
		}
		
		function tempoHoras(idHora1, idHora2){
			var hora1 = document.getElementById(idHora1).value;
			var hora2 = document.getElementById(idHora2).value;
			
		    var horaIni = hora1.split(':');
		    var horaSom = hora2.split(':');
		 
		    var horasTotal = parseInt(horaIni[0], 10) + parseInt(horaSom[0], 10);
		    var minutosTotal = parseInt(horaIni[1], 10) + parseInt(horaSom[1], 10);
		     
		    if(minutosTotal >= 60){
		        minutosTotal -= 60;
		        horasTotal += 1;
		    }
		     
		    var horaFinal = completaZeroEsquerda(horasTotal) + ":" + completaZeroEsquerda(minutosTotal);
		    document.getElementById(idHora1).value = horaFinal;
		    document.getElementById('horaResultado').value = horaFinal;
		    var somaHora = document.getElementById('somaHora').value + " + "  + hora1 + " + " + hora2;
		    document.getElementById('somaHora').value = somaHora;
		    document.getElementById(idHora2).value = "";
		    return false;
		}
		
		
		function completaZeroEsquerda( numero ){
			return ( numero < 10 ? "0" + numero : numero);
		} 
	</script>
</head>

<body class="fundo_execpen">
	
		<% if (request.getAttribute("mostraCabecalho") != null && String.valueOf(request.getAttribute("mostraCabecalho")).equalsIgnoreCase("S")) {%>
		<%@ include file="/Cabecalho_execpenweb._jsp" %>
		<% } %>
		
		<div id="divCorpo" class="divCorpo" >
		
		<div class="area"><h2>&raquo; Calculadora de tempos e datas</h2></div>
		<form action="" method="post" name="Formulario" id="Formulario">
			<div id="divEditar" class="divEditar">
			
				<fieldset class="formEdicao">
				
					<legend class="formEdicaoLegenda">Conversão de Tempos</legend>
					
					<div class="col45">
					<label class="formEdicaoLabel">Converter o tempo em anos para dias: </label><br>
					<input class="formEdicaoInput" name="Ano" id="Ano" type="text" size="4" maxlength="4" value="" onkeypress="return DigitarSoNumero(this, event)" placeholder="Anos"/>
					<input class="formEdicaoInput" name="Mes" id="Mes" type="text" size="4" maxlength="4" value="" onkeypress="return DigitarSoNumero(this, event)" placeholder="Meses"/>
					<input class="formEdicaoInput" name="Dia" id="Dia" type="text" size="4" maxlength="4" value="" onkeypress="return DigitarSoNumero(this, event)" placeholder="Dias"/>
		   	    	<input id="imaConverterDias" name="imaConverterDias" type="button" value="Calcular" 
							onclick="javascript:converterParaDias('Ano','Mes','Dia','TempoDias');return false;" title="Converter o tempo em anos(a-m-d) para dias"><br>
					<input class="formEdicaoInputSomenteLeitura" readonly="readonly" name="TempoDias" id="TempoDias" type="text" size="8" maxlength="8" value=""/>(dias)
					</div>
					<div class="col45">
					<label class="formEdicaoLabel">Converter o tempo em dias para anos: </label><br>		
					<input class="formEdicaoInput" name="Dias" id="Dias" type="text" size="8" maxlength="8" value="" onkeypress="return DigitarSoNumero(this, event)" placeholder="Dias"/>
					<input id="imaConverterAnos" name="imaConverterAnos" type="button" value="Calcular" 
							onclick="if (VerificarCampos('Dias')) {converterParaAnoMesDia('Dias','TempoAnos');} return false;" title="Converter o tempo em dias para anos(a-m-d)"><br>
					<input class="formEdicaoInputSomenteLeitura" readonly="readonly" name="TempoAnos" id="TempoAnos" type="text" size="12" maxlength="10" value=""/>(ano/mes/dia)
					</div>
				</fieldset>
				
				<fieldset class="formEdicao">
					<legend class="formEdicaoLegenda">Somar dias a uma data:</legend>
					
					<label class="formEdicaoLabel" style="min-width: 10px"><small>* Para subtrair, informe a quantidade de dias negativa. Exemplo: -10. </small></label><br><br />
					
						
					<input class="formEdicaoInput" name="dataSoma" id="dataSoma" placeholder="Data" type="text" size="10" maxlength="10" value="" onblur="verifica_data(this);" onkeypress="return DigitarSoNumero(this, event)"/>
					<label class="formEdicaoLabel" style="min-width: 10px"> + </label>
					<input class="formEdicaoInput" name="diasSoma" id="diasSoma" type="text" size="5" maxlength="5" value="" placeholder="Dias"/>
					
					<input id="imaSoma" name="imaSoma" type="button" src="./imagens/imgAtualizarPequena.png" 
							onclick="if (VerificarCampos('dataSoma')) {somaDias('dataSoma','diasSoma', 'resultadoSoma');} return false;" title="Somar..." value="Calcular"><br>
					<input class="formEdicaoInputSomenteLeitura" name="resultadoSoma" id="resultadoSoma" type="text" size="10" maxlength="10" value="" readonly="readonly"/>
				</fieldset>
				
				<fieldset class="formEdicao">
					<legend class="formEdicaoLegenda">Subtrair datas:</legend>
						
					<input class="formEdicaoInput" name="data1" id="data1" placeholder="Data1" type="text" size="10" maxlength="10" value="" onblur="verifica_data(this);" onkeypress="return DigitarSoNumero(this, event)"/>
					<label class="formEdicaoLabel" style="min-width: 10px"> - </label>
					<input class="formEdicaoInput" name="data2" id="data2" placeholder="Data2" type="text" size="10" maxlength="10" value="" onblur="verifica_data(this);" onkeypress="return DigitarSoNumero(this, event)"/>
					
					<input id="imaSoma" name="imaSoma" type="button" src="./imagens/imgAtualizarPequena.png" 
							onclick="if (VerificarCampos('data1')) {subtrairDatas('data1','data2', 'qtdeDias');} return false;" title="Subtrair..." value="Calcular"><br>
					<input class="formEdicaoInputSomenteLeitura" name="qtdeDias" id="qtdeDias" type="text" size="10" maxlength="10" value="" readonly="readonly"/>
					<label class="formEdicaoLabel" style="min-width: 10px">(dias)</label><br>
				</fieldset>
				
				<fieldset class="formEdicao">
					<legend class="formEdicaoLegenda">Fração correspondente</legend>
					
					<input class="formEdicaoInput" placeholder="Dias" name="TempoDiasFracao" id="TempoDiasFracao" type="text" size="8" maxlength="8" value="" onkeypress="return DigitarSoNumero(this, event)"/>
		   	    	<input id="imaConverterDias" value="Calcular" name="imaConverterDias" type="button" src="./imagens/imgAtualizarPequena.png" 
							onclick="calculaFracao('TempoDiasFracao');return false;" title="Calcula o tempo correspondente à fração">

					<br/><br>
					<label class="formEdicaoLabel">Tempo correspondente à fração: </label><br>
					<div class="col15">
					<label class="formEdicaoLabel">Fração 1/6 </label><br><input class="formEdicaoInputSomenteLeitura" id="fracao16" size="6"><label class="formEdicaoLabelPequeno"> dias</label><br><br/>
					</div>
					<div class="col15">
					<label class="formEdicaoLabel">Fração 1/5 </label><br><input class="formEdicaoInputSomenteLeitura" id="fracao15" size="6"><label class="formEdicaoLabelPequeno"> dias</label><br><br/>
					</div>
					<div class="col15">
					<label class="formEdicaoLabel">Fração 1/4 </label><br><input class="formEdicaoInputSomenteLeitura" id="fracao14" size="6"><label class="formEdicaoLabelPequeno"> dias</label><br><br/>
					</div>
					<div class="col15">
					<label class="formEdicaoLabel">Fração 1/3 </label><br><input class="formEdicaoInputSomenteLeitura" id="fracao13" size="6"><label class="formEdicaoLabelPequeno"> dias</label><br><br/>
					</div>
					<div class="col15">
					<label class="formEdicaoLabel">Fração 3/8 </label><br><input class="formEdicaoInputSomenteLeitura" id="fracao38" size="6"><label class="formEdicaoLabelPequeno"> dias</label><br><br/>
					</div>
					<div class="clear"></div>
					<div class="col15">
					<label class="formEdicaoLabel">Fração 2/5 </label><br><input class="formEdicaoInputSomenteLeitura" id="fracao25" size="6"><label class="formEdicaoLabelPequeno"> dias</label><br><br/>
					</div>
					<div class="col15">
					<label class="formEdicaoLabel">Fração 5/12 </label><br><input class="formEdicaoInputSomenteLeitura" id="fracao512" size="6"><label class="formEdicaoLabelPequeno"> dias</label><br><br/>
					</div>
					<div class="col15">
					<label class="formEdicaoLabel">Fração 11/24 </label><br><input class="formEdicaoInputSomenteLeitura" id="fracao1124" size="6"><label class="formEdicaoLabelPequeno"> dias</label><br><br/>
					</div>
					<div class="col15">
					<label class="formEdicaoLabel">Fração 1/2 </label><br><input class="formEdicaoInputSomenteLeitura" id="fracao12" size="6"><label class="formEdicaoLabelPequeno"> dias</label><br><br/>
					</div>
					<div class="col15">
					<label class="formEdicaoLabel">Fração 3/5 </label><br><input class="formEdicaoInputSomenteLeitura" id="fracao35" size="6"><label class="formEdicaoLabelPequeno"> dias</label><br><br/>
					</div>
					<div class="clear"></div>
					<div class="col15">
					<label class="formEdicaoLabel">Fração 2/3 </label><br><input class="formEdicaoInputSomenteLeitura" id="fracao23" size="6"><label class="formEdicaoLabelPequeno"> dias</label><br><br/>
				</div>
				</fieldset>
				
				<fieldset class="formEdicao">
					<legend class="formEdicaoLegenda">Somar horas</legend>
					<label class="formEdicaoLabel" style="min-width: 15px">Tempo em HH:MM </label><br>
					<input class="formEdicaoInput" style="min-width: 10px" name="horas1" id="horas1" type="text" size="7" maxlength="5" value="" onkeypress="return DigitarSoNumero(this, event)"/>
					<label class="formEdicaoLabel" style="min-width: 5px"> + </label>
					<input class="formEdicaoInput"style="min-width: 10px"  name="horas2" id="horas2" type="text" size="7" maxlength="5" value="" onkeypress="return DigitarSoNumero(this, event)"/>
					<input id="imaConverterDias" name="imaConverterDias" type="button" value="Calcular" onclick="tempoHoras('horas1','horas2');return false;" title="Soma horas">
					<input id="imaConverterDias" name="imaConverterDias" type="button" value="Limpar" onclick="limpar();return false;" title="Soma horas"><br>
					
					<input class="formEdicaoInput"style="min-width: 10px"  name="horaResultado" id="horaResultado" type="text" size="7" maxlength="5" value=""/>
					
					
					<br />
					<input class="formEdicaoInputSomenteLeitura" name="somaHora" id="somaHora" type="text" size="100" maxlength="100" value="" readonly="readonly"/>

			</div>
		</form>
	</div>
	<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>
			