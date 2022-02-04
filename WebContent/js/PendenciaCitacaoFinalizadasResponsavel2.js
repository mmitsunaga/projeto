function buscaDados(posicao, tamanho) {
	alert('PendenciaCitacaoFinalizadasResponsavel2.js');
	if (posicao==null) posicao=$("#CaixaTextoPosicionar").val()-1;
	url = 'PendenciaDwr?AJAX=ajax&PaginaAtual=3&fluxo=14&dataInicialFim='+document.getElementById('dataInicialFim').value+'&dataFinalFim='+document.getElementById('dataFinalFim').value +'&nomeBusca='+document.getElementById('nomeBusca').value+ '&PosicaoPaginaAtual=' + posicao;

	var corpoTabela = "";
	corpoTabela +='<td class="colunaMinima lista_id">' + 'JsoNid' + '</td>';
	corpoTabela +='<td width="130" align="center">';
	corpoTabela +='<a href="BuscaProcesso?Id_Processo=' + 'JsoNdesc1' + '">' + 'JsoNdesc2' + '</a></td>';
	corpoTabela +='<td>' + 'JsoNdesc3' + '</td>';
	corpoTabela +='<td>' + 'JsoNdesc4' + '</td>';
	corpoTabela +='<td class="lista_data">' + 'JsoNdesc5' + '</td>';
	corpoTabela +='<td class="lista_data">' + 'JsoNdesc6' + '</td>';
	corpoTabela +='<td align="center">';
	corpoTabela +='<a href=\"Pendencia?PaginaAtual=-1&amp;pendencia=' + 'JsoNid' + '&amp;CodigoPendencia='+ 'JsoNhash' +'&amp;NovaPesquisa=true\">';
	corpoTabela +='<img src=\"imagens/' + 'imgLocalizarPequena.png' + '\" alt=\"Selecionar\" title=\"' + 'Ver detalhes da pend&ecirc;ncia' + '\" \/>';
	corpoTabela +='<\/a>';
	corpoTabela +='</td>';
	
	buscaDadosJSON(posicao, tamanho, url, corpoTabela);

}