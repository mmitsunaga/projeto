<script type="text/javascript">  
	function buscaDados(posicao, tamanho) {
		if (posicao==null) {
			posicao=$("#CaixaTextoPosicionar").val()-1;
		}
		url = 'PendenciaJson?AJAX=ajax&PaginaAtual=3&fluxo=1&dataInicialInicio='+document.getElementById('dataInicialInicio').value+'&dataFinalInicio='+document.getElementById('dataFinalInicio').value +'&nomeBusca='+document.getElementById('nomeBusca').value+ '&PosicaoPaginaAtual=' + posicao;
	
		var corpoTabela = "";
		corpoTabela +='<td class="colunaMinima lista_id">' + 'JsoNid' + '</td>';
		corpoTabela +='<td align="center">' + 'JsoNdesc1' + '</td>';
		corpoTabela +='<td width="130" align="center">';
		corpoTabela +='<a href="BuscaProcesso?Id_Processo=' + 'JsoNdesc6' + '">' + 'JsoNdesc2' + '</a></td>';
		corpoTabela +='<td align="center">' + 'JsoNdesc3' + '</td>';
		corpoTabela +='<td align="center" width="150">' + 'JsoNdesc4' + '</td>';
		corpoTabela +='<td align="center" width="150">' + 'JsoNdesc5' + '</td>';
		corpoTabela +='<td class="colunaMinima"><a href="PendenciaUsuarioServentiaResponsavel?PaginaAtual=4&amp;pendencia=' + 'JsoNid' + '&amp;CodigoPendencia=' + 'JsoNhash' + '&amp;fluxo=0"><img src="imagens/22x22/btn_encaminhar.png" alt="Distribuir" title="Efetuar troca de respons&Aacute;vel" /></a></td>';            	
	
		buscaDados2JSON(posicao, tamanho, url, corpoTabela,null,null,null);
	}
</script>