/*
 * Atualizado 14/08/2018
 * 
 * Atualizado 09/01/2019 - Adicionado fun&ccedil;&atilde;o "limparCamposGuiaApelacao" para a guia de recurso de apela&ccedil;&atilde;o.
 */
function alteradoApelante() {  
	var apelante = document.getElementById("id_apelante").value;
	var apelado = document.getElementById("id_apelado").value;
	
	if( apelante == apelado ) {
		document.getElementById("id_apelado").value = "";
	}
}

function alteradoApelado() {
	var apelante = document.getElementById("id_apelante").value;
	var apelado = document.getElementById("id_apelado").value;
	
	if( apelante == apelado ) {
		document.getElementById("id_apelante").value = "";
	}
}

function finalidadeAlterada() {
	
	var finalidade = document.getElementById("finalidade").value;
	
	if( document.getElementById("labelInserirCitacao") != null )
		document.getElementById("labelInserirCitacao").style.color = "black";
	
	if( document.getElementById("labelInserirPenhora") != null )
		document.getElementById("labelInserirPenhora").style.color = "black";
	
	if( document.getElementById("labelInserirAvaliacao") != null )
		document.getElementById("labelInserirAvaliacao").style.color = "black";
	
	
	if( finalidade == "41" ) {
		
	}
	if( finalidade == "44" ) {
		if( document.getElementById("labelInserirCitacao") != null )
			document.getElementById("labelInserirCitacao").style.color = "red";
		
		if( document.getElementById("labelInserirPenhora") != null )
			document.getElementById("labelInserirPenhora").style.color = "red";
		
		if( document.getElementById("labelInserirAvaliacao") != null )
			document.getElementById("labelInserirAvaliacao").style.color = "red";
	}
	if( finalidade == "45" ) {
		if( document.getElementById("labelInserirCitacao") != null )
			document.getElementById("labelInserirCitacao").style.color = "red";
		
		if( document.getElementById("labelInserirPenhora") != null )
			document.getElementById("labelInserirPenhora").style.color = "red";
	}
	if( finalidade == "46" ) {
		if( document.getElementById("labelInserirCitacao") != null )
			document.getElementById("labelInserirCitacao").style.color = "red";
		
		if( document.getElementById("labelInserirPenhora") != null )
			document.getElementById("labelInserirPenhora").style.color = "red";
		
		if( document.getElementById("labelInserirAvaliacao") != null )
			document.getElementById("labelInserirAvaliacao").style.color = "red";
	}
}

function finalidadeGuiaLocomocaoAlterada() {
	if($('#finalidade').val() == "8" || $('#finalidade').val() == "9" || $('#finalidade').val() == "44" || $('#finalidade').val() == "45" || $('#finalidade').val() == "46") {
		$('#penhora option:eq(0)').prop('selected', true);
		$('#intimacao option:eq(0)').prop('selected', true);
		$('#penhora').prop('disabled', 'disabled');
		$('#intimacao').prop('disabled', 'disabled');
	} else {
		$('#penhora').prop('disabled', false);
		if($('#finalidade').val() == "41") {
			$('#penhora option:eq(1)').prop('selected', true);
		} else {
			$('#penhora option:eq(0)').prop('selected', true);			
		}
		penhoraAlterada();
	}
}

function penhoraAlterada() {
	if($('#penhora').val() == "0") {		
		$('#intimacao option:eq(0)').prop('selected', true);
		$('#intimacao').prop('disabled', 'disabled');
	} else {
		$('#intimacao').prop('disabled', false);
	}
}

function citacaoHoraCertaAlterada() {
	if($('#citacaoHoraCerta').val() == "1") {
		$('#foraHorarioNormal option:eq(0)').prop('selected', true);
		$('#oficialCompanheiro option:eq(0)').prop('selected', true);
	}
}

function foraHorarioNormalAlterada() {
	if($('#foraHorarioNormal').val() == "1") {
		$('#citacaoHoraCerta option:eq(0)').prop('selected', true);
		$('#oficialCompanheiro option:eq(0)').prop('selected', true);
	}
}

function oficialCompanheiroAlterado() {
	if($('#oficialCompanheiro').val() == "1") {
		$('#citacaoHoraCerta option:eq(0)').prop('selected', true);
		$('#foraHorarioNormal option:eq(0)').prop('selected', true);
	}
}

function tratamentoRateio50Porcento(componenteTexto, componentCheck, opcaoMenu, lista) {
	//if opcao 303
	if( opcaoMenu.value === "303" ) {
		for(var i = 0; i < lista.length; i++ ) {
			document.getElementById(lista[i]).readOnly = true;
		}
		
		if( componentCheck.checked ) {
			//coloca 50 no campo
			componenteTexto.value = "50.00";
		}
		else {
			componentCheck.removeAttribute("readonly");
			componenteTexto.value = "0.00";
		}
		//chama a soma
		somarRateioPartesVariavel(null, lista);
	}
}

function mostrarDivRateioPartes(componente, opcao, listaParteRateio, listaEmitirGuia) {
	switch(opcao) {
		case "404": {
			Mostrar(componente);
			for(var i = 0; i < listaParteRateio.length; i++ ) {
				document.getElementById(listaParteRateio[i]).value = "0.00";
				document.getElementById(listaParteRateio[i]).removeAttribute("readonly");
			}
			break;
		}
		case "303": {
			Mostrar(componente);
			for(var i = 0; i < listaParteRateio.length; i++ ) {
				document.getElementById(listaParteRateio[i]).value = "0.00";
				document.getElementById(listaParteRateio[i]).readOnly = true;
			}
			break;
		}
		default: {
			Ocultar(componente);
			break;
		}
	}
	
	for(var m = 0; m < listaEmitirGuia.length; m++ ) {
		document.getElementById(listaEmitirGuia[m]).checked = false;
	}
	document.getElementById("rateioParteVariavelTotal").value = "0.00";
}

function somarRateioPartesVariavel(componente, lista) {
	var total = document.getElementById("rateioParteVariavelTotal");
	total.value = 0.0;
	
	for(var i = 0; i < lista.length; i++ ) {
		var item = lista[i];
		document.getElementById(item).value = document.getElementById(item).value.replace(",",".");
		total.value = (parseFloat(total.value) + parseFloat(document.getElementById(item).value)).toFixed(2);
	}
	
	if( total.value == 100.0 ) {
		total.className = "formEdicaoInputSomenteLeitura";
	}
	else {
		total.className = "formEdicaoInputSomenteLeituraVermelho";
	}
}

function mostrarOcultarDivAreaDistribuicaoGuiaInicial(componente) {
	if( componente.value.length > 0 ) {
		Ocultar('divComarca');
		Ocultar('imaLocalizarComarca');
		Ocultar('Comarca');
		Ocultar('divAreaDistribuicao');
		Ocultar('imaLocalizarAreaDistribuicao');
		Ocultar('AreaDistribuicao');
	}
	else {
		Mostrar('divComarca');
		Mostrar('imaLocalizarComarca');
		Mostrar('Comarca');
		Mostrar('divAreaDistribuicao');
		Mostrar('imaLocalizarAreaDistribuicao');
		Mostrar('AreaDistribuicao');
	}
}

function isProcessoVinculadoGuiaInicialPreenchido( componente, valorPermissao, idForm ) {
	if( componente !== null && componente.value.length >= 25 ) {
		AlterarValue('PaginaAtual',valorPermissao);
		FormSubmit(idForm);
	}
}

function zerarCampoLimpo(componente, valor) {
	if( componente.value.length == 0 ) {
		if( valor.length > 0 ) {
			componente.value = valor;
		}
		else {
			componente.value = '0';
		}
	}
}

function somarQuantidade(componente) {
	componente.value++;
}

function subtrairQuantidade(componente, minimo) {
	if( componente.value > 0 ) {
		if( componente.value > minimo ) {
			componente.value--;
		}
	}
}

function minimoUm(componente, textoNome) {
	if( componente.value < 1 ) {
		$("#dialog").text("Aten&ccedil;&atilde;o Sr(a) Contador(a): O item \"" + textoNome + "\" deve ter a cobran&ccedil;a de quantidade m&iacute;nima de 1.");
		$("#dialog").dialog("open");
		componente.value = "1";
	}
}

function PercentualMaximo(componente, valorMaximo) {
	
	var valor = valorMaximo.replace(",",".");
	
	if( (componente.value.replace(",",".")*1) > (valor*1) ) {
		componente.value = valorMaximo;
	}
}

function retirarAcentos(texto) {
	texto = texto.toLowerCase();
	texto = texto.replace(new RegExp(/[àáâãäå]/g),"a");
	texto = texto.replace(new RegExp(/[èéêë]/g),"e");
	texto = texto.replace(new RegExp(/[ìíîï]/g),"i");
	texto = texto.replace(new RegExp(/ñ/g),"n");
	texto = texto.replace(new RegExp(/[òóôõö]/g),"o");
	texto = texto.replace(new RegExp(/[ùúûü]/g),"u");
	
	texto = texto.normalize('NFD').replace(/[\u0300-\u036f]/g, "");
	
	return texto;
}

function limparAreaDistribuicaoProcessoTipoGuiaInicial(url, paginaAtual, passoEditar) {
	AlterarValue('ProcessoTipo','');
	AlterarValue('Id_ProcessoTipo','');
	
	AlterarValue('AreaDistribuicao','');
	AlterarValue('Id_AreaDistribuicao','');
	
	url += "?PaginaAtual="+paginaAtual+"&PassoEditar="+passoEditar;
	
	$.ajax({
		url: encodeURI(url), 
		timeout: 300000, 
		async: true, 
		success: function(retorno){}
	});
}

function limparCamposGuiaApelacao(componenteCheck, componenteValor) {
	$("#"+componenteCheck).prop("checked", false);
	$("#"+componenteValor).val('0');
}

function validaQuantidadeParcelas(componente) {
	if(componente.value > 5) {
		Mostrar('divMotivoParcelamento');
	}
	else {
		Ocultar('divMotivoParcelamento');
		document.getElementById("motivoParcelamento").value = "";
	}
}