function buscaDados(posicao, tamanho) {
//	alert('PendenciaPublica.js');
	if (posicao==null) posicao=$("#CaixaTextoPosicionar").val()-1;
	url = 'PendenciaDwr?AJAX=ajax&PaginaAtual=3&fluxo=21&dataInicial='+document.getElementById('dataInicial').value+'&dataFinal='+document.getElementById('dataFinal').value +'&Id_Serventia='+document.getElementById('Id_Serventia').value + '&PosicaoPaginaAtual=' + posicao;

	var corpoTabela = "";

	corpoTabela +='<td align="center">' + 'JsoNdesc1' + '</td>';
	corpoTabela +='<td align="center">' + 'JsoNdesc2' + '</td>';
	corpoTabela +='<td align="center">' + 'JsoNdesc3' + '</td>';          	
	corpoTabela +='<td class="colunaMinima"><a href="PendenciaPublicacao?PaginaAtual=6&amp;Id_Pendencia=' + 'JsoNid' + '"><img src="imagens/22x22/ico_editar.png" alt="Abrir publica&ccedil;&atilde;o" title="Abrir publica&ccedil;&atilde;o" /></a></td>';
	
	buscaDadosJSON(posicao, tamanho, url, corpoTabela, null, '#TabelaArquivos');
}