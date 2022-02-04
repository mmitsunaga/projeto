
function verInfoRC(e){
	e.preventDefault();
	var url = e.target.href;
	$.get({url: encodeURI(url), dataType: 'html'}, function(data){
	  var config = {modal: true, resizable: true, width:640, height:480, title: 'Informações de Rastreamento'};
      $("#dialog").html(data).css('background-color', 'white').dialog(config).dialog('open');
	});
}
