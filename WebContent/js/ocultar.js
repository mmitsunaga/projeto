/**
 * Ocultar os checkboxes desmarcados
 * @author Ronneesley Moura Teles
 * @since 25/08/2008 13:44
 * dependencias jquery.js e conclitos.js
 */
function apenasMarcados(opc){	
	$("div#ListaCheckBox ul li input[type=checkbox]:not(:checked)").each(function(i){
		$(this.parentNode).css("display", opc == true?"none":"");				
	});
}

function apenasDesmarcados(opc){	
	$("div#ListaCheckBox ul li input[type=checkbox][checked]").each(function(i){
		$(this.parentNode).css("display", opc == true?"none":"");				
	});
}

function mostrarTodos(opc){
	$("div#ListaCheckBox ul li input[type=checkbox]").each(function(i){
		$(this.parentNode).css("display", opc == true?"":"none");				
	});
}

$(document).ready(function(){
	$("#MostrarTodos").click(function(){
		mostrarTodos(true);
	});
	
	$("#MostrarDesmarcados").click(function(){
		apenasMarcados(false);
		apenasDesmarcados(true);
	});
	
	$("#MostrarMarcados").click(function(){
		apenasDesmarcados(false);
		apenasMarcados(true);
	});
});
