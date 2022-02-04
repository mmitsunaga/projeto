<script type="text/javascript">
	function buscaDados(posicao, tamanho) {
		if (posicao==null) posicao=$("#CaixaTextoPosicionar").val()-1;
		url = 'PendenciaJson?AJAX=ajax&PaginaAtual=3&fluxo=6&dataInicialInicio='+document.getElementById('PendenciaTipo').value+'&numeroProcesso='+document.getElementById('numeroProcesso').value+'&PosicaoPaginaAtual=' + posicao;
		
		var corpoTabela = "";
		corpoTabela +='<td class="colunaMinima lista_id">' + 'JsoNid' + '</td>';
		corpoTabela +='<td width="160" align="center">';
		corpoTabela +='<a href="BuscaProcesso?Id_Processo=' + 'JsoNdesc5' + '">' + 'JsoNdesc1' + '</a></td>';
		corpoTabela +='<td>' + 'JsoNdesc2' + '</td>';
		corpoTabela +='<td>' + 'JsoNdesc3' + '</td>';
		corpoTabela +='<td class="lista_data">' + 'JsoNdesc4' + '</td>';
		corpoTabela +='<td align="center>';
		corpoTabela +='<a href=\"Pendencia?PaginaAtual=6&amp;pendencia=' + 'JsoNid' + '&amp;CodigoPendencia='+ 'JsoNhash' +'&amp;fluxo=4&amp;op=PrazoDecorrido\">';
		corpoTabela +='<img src=\"imagens/' + 'imgLocalizarPequena.png' + '\" alt=\"Selecionar\" title=\"' + 'Ver detalhes da pend&ecirc;ncia' + '\" \/>';
		corpoTabela +='<\/a>';
		corpoTabela +='</td>';
		
		buscaDados2JSON(posicao, tamanho, url, corpoTabela,null,null,true);
	}
</script>