
function abrirCaptcha(action, id, hash){
	var url = encodeURI(action + '?PaginaAtual=-1&PassoBusca=2&Id_Arquivo=' + id + '&Hash=' + hash);
	$.get(url, function(data) {
		var dialog = $("#dialog")
			.html(data)
			.dialog({
				title: 'Confirma&ccedil;&atilde;o da Consulta',
				dialogClass: 'fixed-dialog',
				modal: true, 
				closeOnEscape:true, 
				width: 450,
				buttons: [{ text: "Enviar", click: function(){ enviarCaptcha(action, dialog); }}],
				open: function() {				     
				     $(this).siblings('.ui-dialog-buttonpane').find('button:eq(0)').focus(); 
				}
			});		
		$("#dialog").dialog('open');
		$('#textoDigitado').focus();
	});
}

function enviarCaptcha(action, dialogo){
	$.ajax({
		method: 'post',
		url: encodeURI(action + '?PaginaAtual=-1&PassoBusca=3'),
		data: $("#form").serialize(),
	}).done(function(data){
		let info = $('#Viewstate').val();
		$('<form action="'+ encodeURI(action + '?PaginaAtual=-1&PassoBusca=4&Viewstate='+ info) +'" method="post"></form>').appendTo('body').submit().remove();
		dialogo.dialog('close');
	}).fail(function(data){
		dialogo.html(data.responseText);
	});
}