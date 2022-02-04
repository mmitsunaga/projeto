<script type="text/javascript">
	function buscaDados(posicao, tamanho) {
		if (posicao==null){
			posicao=$("#CaixaTextoPosicionar").val()-1;
		}
		url = 'PendenciaJson?AJAX=ajax&PaginaAtual=3&fluxo=17&dataInicialFim='+document.getElementById('dataInicialFim').value+'&dataFinalFim='+document.getElementById('dataFinalFim').value +'&nomeBusca='+document.getElementById('nomeBusca').value+ '&PosicaoPaginaAtual=' + posicao;
	
		var corpoTabela = "";
		corpoTabela +='<td class="colunaMinima lista_id">' + 'JsoNid' + '</td>';
		corpoTabela +='<td width="130" align="center">';
		corpoTabela +='<a href="BuscaProcesso?Id_Processo=' + 'JsoNdesc1' + '">' + 'JsoNdesc2' + '</a></td>';
		corpoTabela +='<td>' + 'JsoNdesc3' + '</td>';
		corpoTabela +='<td>' + 'JsoNdesc4' + '</td>';
		corpoTabela +='<td class="lista_data">' + 'JsoNdesc5' + '</td>';
		corpoTabela +='<td class="lista_data">' + 'JsoNdesc6' + '</td>';
		
		corpoTabela +='<td align="center">';
		corpoTabela +='<a href=\"DescartarPendenciaProcesso?PaginaAtual=7&amp;pendencia=' + 'JsoNid' + '&amp;CodigoPendencia='+ 'JsoNhash' +'&amp;finalizada='+ 'JsoNdesc9' +'\">';
		corpoTabela +='<img src=\"imagens\/22x22\/' + 'btn_movimentar.png' + '\" alt=\"Marcar Aguardando Parecer/Peticionamento\" title=\"' + 'Marcar Aguardando Parecer/Peticionamento' + '\" \/>';
		corpoTabela +='<\/a>';
		corpoTabela +='</td>';
	
		corpoTabela +='<td align="center">';
		corpoTabela +='<a href=\"Pendencia?PaginaAtual=-1&amp;pendencia=' + 'JsoNid' + '&amp;CodigoPendencia='+ 'JsoNhash' +'&amp;NovaPesquisa=true&amp;finalizada='+ 'JsoNdesc9' +'\">';
		corpoTabela +='<img src=\"imagens\/' + 'imgLocalizarPequena.png' + '\" alt=\"Selecionar\" title=\"' + 'Ver detalhes da pend&ecirc;ncia' + '\" \/>';
		corpoTabela +='<\/a>';
		corpoTabela +='</td>';
		buscaDados2JSON(posicao, tamanho, url, corpoTabela,null,null,null);
	}
</script>