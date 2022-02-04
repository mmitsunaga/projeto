<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<%@page  import="br.gov.go.tj.projudi.dt.MandadoJudicialDt"%>
<%@page  import="br.gov.go.tj.projudi.dt.UsuarioServentiaDt"%>
<%@page  import="br.gov.go.tj.projudi.dt.EscalaDt"%>

<jsp:useBean id="Escaladt" scope="session" class= "br.gov.go.tj.projudi.dt.EscalaDt"/>
<jsp:useBean id="MandadoJudicialdt" scope="session" class= "br.gov.go.tj.projudi.dt.MandadoJudicialDt"/>

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title>Meus Mandados</title>
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />	<script type="text/javascript" src="js/jquery.js"> </script>
    <link href="./css/Paginacao.css"  type="text/css"  rel="stylesheet" />
    <link href="js/jscalendar/dhtmlgoodies_calendar.css"  type="text/css"  rel="stylesheet" />
<!-- 	<link href="./js/noUiSlider-12.1.0/distribute/nouislider.min.css" rel="stylesheet" type="text/css" /> -->
	<script type='text/javascript' src='js/ui/jquery-ui.min.js'></script>  
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js" ></script>
	<script type="text/javascript" src="./js/jscalendar/dhtmlgoodies_calendar.js"></script>
	<script type="text/javascript" src="./js/Digitacao/DigitarData.js"></script>		
	<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>
<!-- 	<script language="javascript" type="text/javascript" src="./js/noUiSlider-12.1.0/distribute/nouislider.min.js" ></script>       -->
    
</head>
<body>
  <div  id="divCorpo" class="divCorpo">
<!-- 	<div id="divEditar" class="divEditar"> -->
		<div style="border:1px solid #ddd; background: #fafafa; margin:10px 0; padding: 5px 10px;">
			<h3><%=request.getAttribute("serventia")%></h3>
			<br/>
			
			<div id="abas" class="ui-tabs ui-corner-all ui-widget ui-widget-content">
				<ul role="tablist" class="ui-tabs-nav ui-corner-all ui-helper-reset ui-helper-clearfix ui-widget-header">
					<li role="tab" tabindex="0" class="ui-tabs-tab ui-corner-top ui-state-default ui-tab ui-tabs-active ui-state-active ui-state-focus" aria-controls="meusMandados" aria-labelledby="ui-id-1" aria-selected="true" aria-expanded="true"><a href="meusMandados" onclick="javascript:calcularTamanhoIframeMeusMandados(); false;" role="presentation" tabindex="-1" class="ui-tabs-anchor" id="ui-id-1"><span>Meus Mandados</span></a></li>
	 				<li role="tab" tabindex="-1" class="ui-tabs-tab ui-corner-top ui-state-default ui-tab" aria-controls="distribuicaoDaCentral" aria-labelledby="ui-id-2" aria-selected="false" aria-expanded="false"><a href="#distribuicaoDaCentral" onclick="javascript:buscaDadosAba2(); false;" role="presentation" tabindex="-1" class="ui-tabs-anchor" id="ui-id-2"><span>Distribuição da Central</span></a></li> 
<!-- 					<li role="tab" tabindex="-1" class="ui-tabs-tab ui-corner-top ui-state-default ui-tab" aria-controls="eventos" aria-labelledby="ui-id-3" aria-selected="false" aria-expanded="false"><a href="#eventos" onclick="javascript:navegarArquivos(); false;" role="presentation" tabindex="-1" class="ui-tabs-anchor" id="ui-id-3"><span>Eventos</span></a></li> -->
				</ul>
			
			
			
			<div id="meusMandados">
			
				<form name="Formulario" id="Formulario">
				
				<label for="nomeBusca1">Início:</label>
				<input class="formEdicaoInput" name="nomeBusca1" id="nomeBusca1"  type="text" size="10" maxlength="10" value="" onkeyup="mascara_data(this)" onblur="verifica_data(this)"> 
				<img id="calendarioNomeBusca1" src="./imagens/dlcalendar_2.gif" height="13" width="13" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].nomeBusca1,'dd/mm/yyyy',this)"/>
			
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;

				<label for="nomeBusca2">Fim:</label>
				<input class="formEdicaoInput" name="nomeBusca2" id="nomeBusca2"  type="text" size="10" maxlength="10" value="" onkeyup="mascara_data(this)" onblur="verifica_data(this)"> 
				<img id="calendarioNomeBusca2" src="./imagens/dlcalendar_2.gif" height="13" width="13" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].nomeBusca2,'dd/mm/yyyy',this)"/>
			
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				
				<%-- <%@ include file="Padroes/ConfirmarOperacao.jspf"%> --%>

	          	<input id="formLocalizarBotao" class="formLocalizarBotao" type="button" name="Localizar" value="Consultar" onclick="javascript:buscaDadosJSON('MandadoJudicial', false,  '2&fluxo=1', 4 , '0', <%=Configuracao.TamanhoRetornoConsulta%>); return false;"/>
			
				</form>
				
				
				
				<div id="divTabela" class="divTabela"> 
		          <table id="tabelaLocalizar" class="Tabela">
		            <thead>
		              <tr>
		                <th width='20px' align="center"></th>
		                <th width='40px' align="center">Número</th> 
		               	<th>Regiao</th>
		           	   	<th>Área</th>     
		               	<th>Tipo</th>     
		               	<th>Zona</th>     
		       			<th>Data de Distribuição</th>
						<th>Data Limite</th>
						<th>Situação</th>
						<th class="colunaMinima" title="Seleciona o registro para edição">Selecionar</th>
		              </tr>
		            </thead>
		          	<tbody id="CorpoTabela">&nbsp;</tbody>
		          </table>
		        </div>
		        <div id="Paginacao" class="Paginacao"></div>
		
			
			</div>
			
	 		<div id="distribuicaoDaCentral">

	 
				<div class="col30">
				
				<fieldset class="formEdicao">
					<legend class="formEdicaoLegenda">Escalas</legend>
			        
			        <br>
				 
					<select id="nomeBusca3" nome="nomeBusca3" class="formEdicaoCombo">
							<% 
							List<EscalaDt> listaEscala = (List<EscalaDt>) request.getAttribute("listaEscala");
							if(listaEscala != null) {
								for(EscalaDt escala: listaEscala){
									%> <option value='<%=escala.getId()%>'> <%=escala.getEscala()%> </option> <%
								} 
							}
							%>
					</select>					
			    </fieldset>
			
				</div>
					 
			 

				<div id="divTabelaAba2" class="divTabela"> 
		          <table id="tabelaLocalizarAba2" class="Tabela">
		            <thead>
		              <tr>
		                <th width='20px' align="center"></th>
		                <th width='40px' align="center">Id</th>                
						<th>Oficial</th>
						<th>Quantidade</th>
		                <th class="colunaMinima" title="Seleciona o registro para edição">Selecionar</th>
		              </tr>
		            </thead>
		          	<tbody id="CorpoTabelaAba2">&nbsp;</tbody>
		          </table>
		        </div>
		        <div id="PaginacaoAba2" class="Paginacao"></div>

			</div>   
			
<!-- 			<div id="eventos">bli</div> -->
			
<!-- 			</div> -->
			

		</div>
	</div>
	<%@ include file="Padroes/Mensagens.jspf" %>
 </div>


<script type="text/javascript">

	$(document).ready(function(){
		$( "#abas" ).tabs({ active: 0 });
		
		$( "#abas" ).tabs({
			beforeActivate: function( event, ui ) { 
				tab = ui.index;
					if(tab==0) {
						calcularTamanhoIframeMeusMandados();
					}
				}
			});
	});
/*
	var slider = document.getElementById('timeSlider');
	
	agora = new Date();
	diaHoje = agora.getDate();
	umDia = 1;
	
	noUiSlider.create(slider, {
	    start: [diaHoje - umDia, diaHoje - umDia],
	    step: umDia,
	    connect: true,
	    pips: {
            mode: 'values',
            values: 'values'
        },
	    range: {
	        'min': 1,
	        'max': 28
	    }
	});
	*/
	/*
	function getDayOfYear() {
	    var now = new Date();
	    var start = new Date(now.getFullYear(), 0, 0);
	    var diff = (now - start) + ((start.getTimezoneOffset() - now.getTimezoneOffset()) * 60 * 1000);
	    var oneDay = 1000 * 60 * 60 * 24;
	    return Math.floor(diff / oneDay);
	}

	function createTimeSlider(startYear) {
		
	    var pipsSlider = document.getElementById('timeSlider');
	    date = new Date();
	    rangeNow = date.getFullYear() + getDayOfYear() / 365;
	    rangeMax = rangeNow;
	    rangeMin = startYear - 0.2;

	    values = []
	    for(i = startYear; i< rangeNow; i++) {
	        values.push(i);
	    }

	    noUiSlider.create(slider, {
	        range: {
	            max: 28,
	            min: 1
	        },
	        start: [rangeMin, rangeNow],
	        connect: true,
	        orientation: "horizontal",
	        direction: 'rtl', // rtl is now at top
	        pips: {
	            mode: 'values',
	            values: values
	        },
	        // tooltips: true
	    });
	    return pipsSlider;
	}

	var timeSlider = createTimeSlider(2018);
	rangeMin = 2010 - 0.2;

	timeSlider.noUiSlider.on('update', function(values, handle) {
	    console.log(values);
	    if(values[0] != rangeMin) {
	        lowerKnob = document.getElementsByClassName('noUi-handle-lower')[0];
	        lowerKnob.classList.add("dark-button");
	    } else {
	        lowerKnob = document.getElementsByClassName('noUi-handle-lower')[0];
	        lowerKnob.classList.remove("dark-button");
	    }

	    if(values[1] != rangeMax) {
	        lowerKnob = document.getElementsByClassName('noUi-handle-upper')[0];
	        lowerKnob.classList.add("dark-button");
	    } else {
	        lowerKnob = document.getElementsByClassName('noUi-handle-upper')[0];
	        lowerKnob.classList.remove("dark-button");
	    }


	})
*/

	function buscaDadosAba2(){
		buscaDadosJSON('MandadoJudicial', false,  '2&fluxo=2', 3 , '0', <%=Configuracao.TamanhoRetornoConsulta%>, "CorpoTabelaAba2");
	}
	
	$("#nomeBusca3").change(function(){
		buscaDadosAba2();
	});

	function calcularTamanhoIframeMeusMandados() {

		var objIframe = window.parent.document.getElementById('Principal');
		if (objIframe != null) {
			var objTela = window.document.body;
			objIframe.height = objTela.clientHeight + 400;
		}
	}
	
</script>

</body>

</html>
