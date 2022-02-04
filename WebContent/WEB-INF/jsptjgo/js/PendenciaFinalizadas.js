<script type="text/javascript">  
	function buscaDados(posicao, tamanho) {
		if (posicao==null) {
			posicao=$("#CaixaTextoPosicionar").val()-1;
		}
		url = 'PendenciaJson?AJAX=ajax&PaginaAtual=3&fluxo=7&dataInicialInicio='+document.getElementById('dataInicialInicio').value+'&dataFinalInicio='+document.getElementById('dataFinalInicio').value+'&Id_PendenciaTipo='+document.getElementById('Id_PendenciaTipo').value+'&Id_PendenciaStatus='+document.getElementById('Id_PendenciaStatus').value+'&prioridade='+true+'&filtroTipo='+Formulario.filtroTipo.value+'&dataInicialFim='+document.getElementById('dataInicialFim').value +'&dataFinalFim='+document.getElementById('dataFinalFim').value + '&nomeBusca='+document.getElementById('nomeBusca').value + '&PosicaoPaginaAtual=' + posicao;
		
	    var corpoTabela = "";
		corpoTabela +='<td class="colunaMinima lista_id">' + 'JsoNid' + '</td>';
		corpoTabela +='<td width="130" align="center">';
		corpoTabela +='<a href="BuscaProcesso?Id_Processo=' + 'JsoNdesc1' + '">' + 'JsoNdesc2' + '</a></td>';
		
		corpoTabela +='<td>' + 'JsoNdesc3' + '</td>';
		corpoTabela +='<td>' + 'JsoNdesc4' + '</td>';
		corpoTabela +='<td class="lista_data">' + 'JsoNdesc5' + '</td>';
		corpoTabela +='<td class="lista_data">' + 'JsoNdesc6' + '</td>';
		corpoTabela +='<td class="lista_data">' + 'JsoNdesc7' + '</td>';
		corpoTabela +='<td>' + 'JsoNdesc8' + '</td>';
		
		corpoTabela +='<td align="center">';
		corpoTabela +='<a href=\"Pendencia?PaginaAtual=7&amp;pendencia=' + 'JsoNid' + '&amp;CodigoPendencia='+ 'JsoNhash' +'&amp;fluxo=17\">';
		corpoTabela +='<img src=\"imagens/' + 'imgLocalizarPequena.png' + '\" alt=\"Selecionar\" title=\"' + 'Ver detalhes da pend&ecirc;ncia' + '\" \/>';
		corpoTabela +='<\/a>';
		corpoTabela +='</td>';
		buscaDados2JSON(posicao, tamanho, url, corpoTabela,null,null,null);
	}
</script>