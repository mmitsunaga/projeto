function buttonOn(nomeBotao) {
	
	nomeBotao.className='Botao';
}

function buttonOver(nomeCampo) {
	
	nomeCampo.className='BotaoOn';
}
function abre(url) {
	window.open(url,'janela', 'width=750,height=500,left=10,top=30,' + 
				'screenX=10,screenY=30,toolbar=no,location=no,' +
				'directories=no,status=no,menubar=yes,scrollbars=yes,' +
				'copyhistory=no,resizable=yes');
}
function abreManual(url) {
	window.open(url,'janela','width=750,height=450,left=10,top=30,' +
				'screenX=10,screenY=30,toolbar=yes,location=no,' + 
				'directories=no,status=no,menubar=yes,scrollbars=yes,' +
				'copyhistory=no,resizable=yes');
}