<script type="text/javascript">

	function Posicionar(tamanho){
		posicao=$("#CaixaTextoPosicionar").val()-1;
		buscaDados(posicao, tamanho);
	}
	
	function CriarPaginacao(PaginaAtual, TotalPaginas, tamanho){
        var tempString;									
		var loPaginaAtual = PaginaAtual; 
		var total = Math.ceil((TotalPaginas / tamanho));  						
		var loConte =1;
		
		//Guarda a �ltima p�gina selecionada
		var loPaginaSelecionada= loPaginaAtual;
		
		//determino onde vai come�ar a contagem de p�ginas
		var loPaginaInicial= loPaginaAtual - Math.floor((tamanho / 2 ));
		if (loPaginaInicial<1) loPaginaInicial = 1;
		
		var loPaginaFinal = loPaginaInicial + tamanho -1;
		
		if (loPaginaFinal > total)
			loPaginaFinal = total;
		
		if (loPaginaInicial > (loPaginaFinal - (tamanho -1)))
			loPaginaInicial = loPaginaFinal - (tamanho -1);
			
		if (loPaginaInicial<1) loPaginaInicial = 1;
			
		tempString = "<b>P&aacute;gina <\/b>\n";
		tempString +="<a href=\"javascript:buscaDados(0," + tamanho + ")\">Primeira<\/a>";

		loPaginaAtual = loPaginaInicial;
		while(loPaginaAtual<=loPaginaFinal){	
			if ((loPaginaAtual-1)==loPaginaSelecionada){
				tempString+= "<b>| " + (loPaginaAtual) + " |<\/b>";
			} else {
				tempString+= "<a href=\"javascript:buscaDados("+(loPaginaAtual-1)+"," + tamanho + ")\">" + (loPaginaAtual) + "<\/a>";
			}		
			loPaginaAtual++;			
		}
		tempString+="<a href=\"javascript:buscaDados(" + (total-1) +"," + tamanho + ")\">Última<\/a>";
		tempString+="<input id=\"CaixaTextoPosicionar\" value=\"" + (total) + "\" class=\"CaixaTextoPosicionar\" type=\"text\" size=\"5\" maxlength=\"10\" /><input class=\"BotaoIr\" type=\"button\" value=\"Ir\" onclick=\"Posicionar(" + tamanho + "); return false;\" />";
		return tempString;
	}

</script>