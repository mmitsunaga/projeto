	/**
	* soma uma quantidade de dias a uma data.
	* dataSoma: data
	* diasSoma: quantidade de dias
	* idInput: campo que recebera o resultado da soma
	**/
function somaDias(data, qtdeDias, idInput) {
	url = 'CalculadoraTemposDatas?AJAX=ajax&PaginaAtual=3&fluxo=1&Data=' + document.getElementById(data).value + '&QtdeDias=' + document.getElementById(qtdeDias).value;
	
    $.ajax({
        url: encodeURI(url),
        context: document.body,
        timeout: 300000, async: true,
        success: function(retorno){
            $("#"+idInput).attr('value',retorno.data); 
        },
        error: function(request, status, error){        	
        	mostrarMensagemErro("Projudi - Erro", request.responseText);
        },
        complete: function(data ){
			$("#formLocalizarBotao").show();
        }
    });	
}

/**
 * Converte quantidade de ano/mes/dia em dias
 * qtdeAno: quantidade de anos
 * qtdeMes: quantidade de meses
 * qtdeDias: quantidade de dias
 * idInput: campo que recebera o resultado da conversao
 */
function converterParaDias(qtdeAno, qtdeMes, qtdeDias, idInput){
	url = 'CalculadoraTemposDatas?AJAX=ajax&PaginaAtual=3&fluxo=2&QtdeAno=' + document.getElementById(qtdeAno).value + '&QtdeMes=' + document.getElementById(qtdeMes).value + '&QtdeDias=' + document.getElementById(qtdeDias).value;
	
    $.ajax({
        url: encodeURI(url),
        context: document.body,
        timeout: 300000, async: true,
        success: function(retorno){
            $("#"+idInput).attr('value',retorno);
        },
        error: function(request, status, error){
        	if (error=='timeout'){
				mostrarMensagemErro('Tempo Excedido', "A ação demorou tempo demais, tente novamente mais tarde.");
			}else{
				mostrarMensagemErro("Projudi - Erro", request.responseText);
			}        	
        },
        complete: function(data ){
			$("#formLocalizarBotao").show();
        }
    });	
}

/**
 * Converte quantidade de dias em ano/mes/dia
 * qtdeDias: quantidade de dias a ser convertido
 * idInput: campo que recebera o resultado da conversao
 */
function converterParaAnoMesDia(qtdeDias, idInput){
	url = 'CalculadoraTemposDatas?AJAX=ajax&PaginaAtual=3&fluxo=3&QtdeDias=' + document.getElementById(qtdeDias).value;
	
    $.ajax({
        url: encodeURI(url),
        context: document.body,
        timeout: 300000, async: true,
        success: function(retorno){
        	$("#"+idInput).attr('value',retorno.data); 
        },
        error: function(request, status, error){
        	if (error=='timeout'){
				mostrarMensagemErro('Tempo Excedido', "A ação demorou tempo demais, tente novamente mais tarde.");
			}else{
				mostrarMensagemErro("Projudi - Erro", request.responseText);
			}        	
        },
        complete: function(data ){
			$("#formLocalizarBotao").show();
        }
    });	
}

/**
 * Subtrai duas datas e informa o resultado em dias
 * data1: data informada (data fim)
 * data2: data informada a ser subtraida (data inicio)
 * idInput: campo que recebera o resultado da conversao
 */
function subtrairDatas(data1, data2, idInput){
	url = 'CalculadoraTemposDatas?AJAX=ajax&PaginaAtual=3&fluxo=4&Data1=' + document.getElementById(data1).value + '&Data2=' + document.getElementById(data2).value;
	
    $.ajax({
        url: encodeURI(url),
        context: document.body,
        timeout: 300000, async: true,
        success: function(retorno){
            $("#"+idInput).attr('value',retorno); 
        },
        error: function(request, status, error){
        	if (error=='timeout'){
				mostrarMensagemErro('Tempo Excedido', "A ação demorou tempo demais, tente novamente mais tarde.");
			}else{
				mostrarMensagemErro("Projudi - Erro", request.responseText);
			}        	
        },
        complete: function(data ){
			$("#formLocalizarBotao").show();
        }
    });	
}

/**
 * Calcula o tempo a ser comutado.
 * tempoTotal: Tempo base, que sera calculado o tempo a ser comutado
 * fracao: fracao que sera incidida sobre o tempoTotal
 * idInputResultado: campo que recebera o resultado do calculo
 */
function calcularTempoComutacao(tempoTotal, fracao, idInputDias, idInputAnos){
	url = 'CalculadoraTemposDatas?AJAX=ajax&PaginaAtual=3&fluxo=5&TempoTotal=' + document.getElementById(tempoTotal).value + '&Fracao=' + fracao;
	var dias = $("#"+idInputDias);
	var anos = $("#"+idInputAnos); 
    $.ajax({
        url: encodeURI(url),
        context: document.body,
        timeout: 300000, async: true,
        success: function(retorno){
        	dias.val(retorno.dias);
            anos.val(retorno.anos);
        },
        error: function(request, status, error){
        	if (error=='timeout'){
				mostrarMensagemErro('Tempo Excedido', "A ação demorou tempo demais, tente novamente mais tarde.");
			}else{
				mostrarMensagemErro("Projudi - Erro", request.responseText);
			}        	
        },
        complete: function(data ){
			$("#formLocalizarBotao").show();
        }
    });	
}


/**
 * Calcula o tempo de condenacao, restante da pena e tempo cumprido ate data
 * data: data referencia para o calculo.
 * idInputPenaRemanescente: campo que recebera o resultado (total da pena remanescente ate a data)
 * idInputRestantePena: campo que recebera o resultado (restante da pena ate a data)
 * idInputTempoCumprido: campo que recebera o resultado (tempoCumprido ate a data)
 * idInputTempoCumpridoNaoHediondo: campo que recebera o resultado (tempoCumpridoNaoHediondo ate a data)
 */
function calcularTempoAteData(idProcesso, data, idInputPenaRemanescenteDias, idInputPenaRemanescenteAnos, idInputRestantePenaDias, idInputRestantePenaAnos, idInputTempoCumpridoDias, idInputTempoCumpridoAnos, idInputPenaRemanescenteDiasNaoHediondo, idInputPenaRemanescenteAnosNaoHediondo, tjSelecionado){
	url = 'CalculadoraTemposDatas?AJAX=ajax&PaginaAtual=3&fluxo=6&IdProcesso=' + idProcesso + '&Data=' + data + '&TjSelecionado=' + tjSelecionado;
	
	  var tempoCondenacaoDias = $("#"+idInputPenaRemanescenteDias);
	  var tempoCondenacaoAnos = $("#"+idInputPenaRemanescenteAnos);
	  var tempoCumpridoDias = $("#"+idInputTempoCumpridoDias);
	  var tempoCumpridoAnos = $("#"+idInputTempoCumpridoAnos);
	  var tempoRestanteTotalDias = $("#"+idInputRestantePenaDias);
	  var restantePenaAnos = $("#"+idInputRestantePenaAnos);
	  var tempoRestanteAteDataDiasCrimesNaoHediondos = $("#"+idInputPenaRemanescenteDiasNaoHediondo);
	  var tempoRestanteAnosCrimesNaoHediondos = $("#"+idInputPenaRemanescenteAnosNaoHediondo);
      
    $.ajax({
        url: encodeURI(url),
        context: document.body,
        timeout: 300000, async: true,
        success: function(retorno){
        	
        	tempoCondenacaoDias.attr('value',retorno.tempoCondenacaoDias);
            tempoCondenacaoAnos.attr('value',retorno.tempoCondenacaoAnos);
            tempoCumpridoDias.attr('value',retorno.tempoCumpridoDias);
            tempoCumpridoAnos.attr('value',retorno.tempoCumpridoAnos);
            tempoRestanteTotalDias.attr('value',retorno.tempoRestanteTotalDias);
            restantePenaAnos.attr('value',retorno.restantePenaAnos);
            tempoRestanteAteDataDiasCrimesNaoHediondos.attr('value',retorno.tempoRestanteAteDataDiasCrimesNaoHediondos);
            tempoRestanteAnosCrimesNaoHediondos.attr('value',retorno.tempoRestanteAnosCrimesNaoHediondos);
        },
        error: function(request, status, error){
        	if (error=='timeout'){
				mostrarMensagemErro('Tempo Excedido', "A ação demorou tempo demais, tente novamente mais tarde.");
			}else{
				mostrarMensagemErro("Projudi - Erro", request.responseText);
			}        	
        },
        complete: function(data ){
			$("#formLocalizarBotao").show();
        }
    });	
}
