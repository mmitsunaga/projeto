<script type="text/javascript">

	_tempBuscaId = '<%=request.getAttribute("tempBuscaId")%>';
	_tempBuscaDescricao = '<%=request.getAttribute("tempBuscaDescricao")%>';
	_PaginaEditar = '<%=Configuracao.Editar%>';
	_PaginaExcluir = '<%=Configuracao.Excluir%>';
	
	function buscaDadosPadrao(url, posicao, tamanho, mostrarExcluir){
		
		//var local = url;
		
		//alert(local);
		//alert(posicao);
		//alert(tamanho);
		//alert(mostrarExcluir);
		//alert('/'+local+'?PaginaAtual=' + <%=Configuracao.LocalizarDWR%>);
		
		//var obj = jQuery.parseJSON(dados);
		var tabela =  $('#CorpoTabela');
		var paginaAtual=0;
		var totalPaginas =0;
		var nomeBusca = encodeURI($("#nomeBusca").val());
		
		tabela.html('');
				
		if(mostrarExcluir)		
			tabela.append('<td class="Centralizado"></td>'); 
		tabela.append('</tr>');
		
		$.ajax({
			url: '/'+url+'?PaginaAtual=' + <%=Configuracao.LocalizarDWR%> + '&NomeBusca=' + nomeBusca + '&PosicaoPaginaAtual=' + posicao,
			context: document.body,
			timeout: 300000, async: true,
			success: function(retorno){
				//alert(retorno);
				//var obj = $.parseJSON(retorno);  
				var inLinha=1;
				//alert(data[0].id);
				$.each(retorno, function(i,item){
					//alert(item.desc);
					if(item.id=="-50000"){						
						//Quantidade pï¿½ginas
						totalPaginas = item.desc;						
					}else if (item.id=="-60000"){
						//posiï¿½ï¿½o atual
						paginaAtual = item.desc;
					}else {
						//tabela.append('<tr> <td>' + item.id  + '</td><td>' + item.descricao +  '</td> <td><input name="Criar" type="submit" onclick="javascrip:criar(); return false;" /></td> </tr>');						
						tabela.append('<tr class="TabelaLinha' + inLinha + '">');
						tabela.append('<td >' + i + '</td>');
						tabela.append('<td >' + item.id + '</td>');
						tabela.append('<td onClick="selecionaSubmete(\'' + item.id + '\',\'' + item.desc + '\')">' + item.desc + '</td>');
						tabela.append('<td class="Centralizado" ><input name="formLocalizarimgEditar" type="image"  src="./imagens/imgEditar.png" onClick="AlterarEditar(\'' + item.id + '\',\'' + item.desc + '\')" />   </td>');
						if(mostrarExcluir)		
							tabela.append('<td class="Centralizado"><input name="formLocalizarimgexcluir" type="image" src="./imagens/imgExcluir.png" onClick="AlterarExcluir(\''+ item.id + '\')" /></td>'); 
						tabela.append('</tr>');
		
						if (inLinha=1) inLinha=2; else inLinha=1;
					}	
				});
			},
			error: function(request, status, error){				
				if (error=='timeout'){
					mostrarMensagemErro('Tempo Excedido', "A ação demorou tempo demais, tente novamente mais tarde.");
				}else{
					mostrarMensagemErro("Projudi - Erro", request.responseText);
				}
	        },	
		  complete: function(data ){
			 ; //alert(data + ' ok');
		  }
		});
											  					        		
		
		//crio a pï¿½ginaï¿½ï¿½o
		CriarPaginacao(paginaAtual,totalPaginas, tamanho); 	
		
	}	
</script>