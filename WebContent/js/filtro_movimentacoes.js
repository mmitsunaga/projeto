$( document ).ready(
	function(){
		var filtroTabelaMovimentacao = filtro.construirObjDeTiposDeFiltros( 'tbody#tabListaProcesso' , function(){ return this.$obj.children( 'tr.filtro-entrada' ); }, function( $eEntrada ){ return $eEntrada.find( 'td.filtro_coluna_movimentacao span.filtro_tipo_movimentacao' ).text(); } );
		filtro.construirMenuFiltro( 'div#lista-agregadores', filtroTabelaMovimentacao, 6 , 4 , 29 );
	}
);

var filtro = {
	construirMenuFiltro : function ( seletorMenu, objDeTiposDeFiltros, qtdFiltrosNaoAgrupados, qtdDeBotoesPorLinha, nrDeCaracteresMaximo ){
		var $eMenu = $( seletorMenu );
		var qtdDeFiltrosAgrupados = 0;
		qtdDeEntradasAgrupadas = 0;
		var filtrosAgrupados = new Array();
		var filtrosNaoAgrupados = new Array();
		if( objDeTiposDeFiltros.qtdDeTiposDeFiltros < qtdFiltrosNaoAgrupados ){
			qtdFiltrosNaoAgrupados = objDeTiposDeFiltros.qtdDeTiposDeFiltros;
		}
		
		$eMenu.append( '<div class="filtro-menu-linha" style="clear:both; height: 22px;"></div>' );
		var $eMenuDiv = $eMenu.children( 'div.filtro-menu-linha:last-child' );
		
		$eMenuDiv.append( '<span class="filtro-menu-botao" style="width: '+ (100 /qtdDeBotoesPorLinha ) +'%; display: block; float: left;"><input id="filtro-chkb-todos" type="radio" name="filtros" value="c" /><label for="filtro-chkb-todos">TODOS</label></span>' );
		this.resumirTexto( $eMenuDiv.find( 'label[for="filtro-chkb-todos"]' ), nrDeCaracteresMaximo, objDeTiposDeFiltros.qtdDeEntradas );
		$eMenuDiv.find( 'input#filtro-chkb-todos' ).click( 
			{	'filtros' 				: 	objDeTiposDeFiltros.mapDeFiltros, 
				'$obj' 					:  	objDeTiposDeFiltros.$obj, 
				'$objMenu'				:	$eMenu 
			},
			function ( eventObject ){
				
				eventObject.data.$obj.children( 'tr.filtro-entrada' ).show();
				eventObject.data.$objMenu.find( 'input[type="radio"]:not( input[type="radio"]#filtro-chkb-todos )' ).attr( 'checked' , false );
				eventObject.data.$obj.children( 'tr[id^="linha"]:visible' ).each(
						function( index, element ){
							trocarDisplay( element );
							trocarDisplay( $(element).children( 'td').get( 0 ) );
						}
					);
				calcularTamanhoIframe();
			}
		); 
		$eMenu.find( 'input[type="radio"]#filtro-chkb-todos' ).attr( 'checked', true );
		
		
		var filtrosOrdenados = [];
		for(var classeDoFiltro in objDeTiposDeFiltros.mapDeFiltros ){
			filtrosOrdenados.push( [ classeDoFiltro ,  objDeTiposDeFiltros.mapDeFiltros[ classeDoFiltro ].qtdDeEntradas, objDeTiposDeFiltros.mapDeFiltros[ classeDoFiltro ].legenda ] );
		}
		
		filtrosOrdenados.sort( function ( a , b ){ return (  b[1] - a[1] ); } );
		
		if( filtrosOrdenados.length === qtdFiltrosNaoAgrupados + 1 ){
			qtdFiltrosNaoAgrupados = qtdFiltrosNaoAgrupados + 1;
		}
		
		for( var x = 0; x < filtrosOrdenados.length; x++ ){
			var filtro = objDeTiposDeFiltros.mapDeFiltros[ filtrosOrdenados[ x ][ 0 ] ];
			

			
			if( x < qtdFiltrosNaoAgrupados ){
				
				if( (x + 1 ) % qtdDeBotoesPorLinha === 0 ){
					$eMenu.append( '<div class="filtro-menu-linha" style="clear:both; height: 22px;"></div>' );
					$eMenuDiv = $eMenu.children( 'div.filtro-menu-linha:last-child' );
				}
				
				filtrosNaoAgrupados.push( filtro );
				$eMenuDiv.append( '<span class="filtro-menu-botao" style="width: '+ ( 100 / qtdDeBotoesPorLinha ) +'%; display: block; float: left;"><input id="filtro-chkb-'+ filtro.classe +'" type="radio" name="filtros" value="c" /><label for="filtro-chkb-'+ filtro.classe +'">' + filtro.legenda + '</label></span>' );
				this.resumirTexto( $eMenuDiv.find( 'label[for="filtro-chkb-'+ filtro.classe +'"]' ), nrDeCaracteresMaximo, filtro.qtdDeEntradas );
				$eMenuDiv.find( 'input[type="radio"]#filtro-chkb-'+ filtro.classe ).click( 
					{	'classe' 	: 	filtro.classe, 
						'$obj' 		:  	objDeTiposDeFiltros.$obj, 
						'$objMenu'	:	$eMenu
					},
					function ( eventObject ){
						var $elemVisiveis = eventObject.data.$obj.children( 'tr.filtro-entrada.' + eventObject.data.classe );
						var $elemInvisiveis = eventObject.data.$obj.children( 'tr.filtro-entrada:not( tr.filtro-entrada.' + eventObject.data.classe + ' )' );
						$elemVisiveis.show();
						$elemInvisiveis.hide();
						eventObject.data.$obj.children( 'tr[id^="linha"]:visible' ).each(
								function( index, element ){
									trocarDisplay( element );
									trocarDisplay( $(element).children( 'td').get( 0 ) );
								}
							);
						eventObject.data.$objMenu.find( 'input[type="radio"]:not( input[type="radio"]#filtro-chkb-'+ eventObject.data.classe + ' )' ).attr( 'checked' , false );
						calcularTamanhoIframe();
					}
				);				
			}else{
				filtrosAgrupados.push( filtro );
				qtdDeFiltrosAgrupados++;
				qtdDeEntradasAgrupadas = qtdDeEntradasAgrupadas + filtro.qtdDeEntradas;
			}
		}
		
		
		
		if( filtrosAgrupados.length > 1 ){
			
			if( $eMenuDiv.children( 'span' ).length === qtdDeBotoesPorLinha ){
				$eMenu.append( '<div class="filtro-menu-linha" style="clear:both; height: 22px;"></div>' );
				$eMenuDiv = $eMenu.children( 'div.filtro-menu-linha:last-child' );
				alert( 'XX' );
			}
			
			$eMenuDiv.append( '<span class="filtro-menu-botao" style="width: '+ (100 /qtdDeBotoesPorLinha ) +'%;"><input id="filtro-chkb-outros" type="radio" name="filtros" value="c" /><label for="filtro-chkb-outros">OUTROS(S)</label></span>' );
			this.resumirTexto( $eMenuDiv.find( 'label[for="filtro-chkb-outros"]' ), nrDeCaracteresMaximo, qtdDeEntradasAgrupadas );
			
			$eMenuDiv.find( 'input#filtro-chkb-outros' ).click( 
				{	'filtrosAgrupados' 		: 	filtrosAgrupados, 
					'filtrosNaoAgrupados'	: 	filtrosNaoAgrupados,
					'$obj' 					:  	objDeTiposDeFiltros.$obj, 
					'$objMenu'				:	$eMenu 
				},
				function ( eventObject ){
					var $elemVisiveis = $();
					for( var x = 0; x < eventObject.data.filtrosAgrupados.length; x++ ){
						$elemVisiveis = $elemVisiveis.add( eventObject.data.$obj.children( 'tr.filtro-entrada.' +  filtrosAgrupados[ x ].classe ) );
					}
					var $elemInvisiveis = $();
					for( var x = 0; x < eventObject.data.filtrosNaoAgrupados.length; x++ ){			
						$elemInvisiveis = $elemInvisiveis.add( eventObject.data.$obj.children( 'tr.filtro-entrada.' +  filtrosNaoAgrupados[ x ].classe ) );
					}
					$elemVisiveis.show();
					$elemInvisiveis.hide();
					eventObject.data.$objMenu.find( 'input[type="radio"]:not( input[type="radio"]#filtro-chkb-outros )' ).attr( 'checked' , false );
					eventObject.data.$obj.children( 'tr[id^="linha"]:visible' ).each(
						function( index, element ){
							trocarDisplay( element );
							trocarDisplay( $(element).children( 'td').get( 0 ) );
						}
					);
					calcularTamanhoIframe();
				}
			);
			
		}
 		
	},
	construirObjDeTiposDeFiltros : function ( seletorObjAlvo, funcTodosObjEntradas, funcObjCriterioDoFiltro ){
		var $eAlvo = $( seletorObjAlvo );
		var mapTiposDeCriterio = new Object();
		var tiposDeFiltros = new Object();
		
		tiposDeFiltros.qtdDeEntradas = 0;
		tiposDeFiltros.qtdDeTiposDeFiltros = 0;
		tiposDeFiltros.$obj = $eAlvo;
		tiposDeFiltros.funcObjCriterioDoFiltro = funcObjCriterioDoFiltro;
		tiposDeFiltros.funcTodosObjEntradas = funcTodosObjEntradas;
		tiposDeFiltros.mapDeFiltros = new Array();
		
		var $aAlvoEntradas = tiposDeFiltros.funcTodosObjEntradas();
		
		$aAlvoEntradas.each(
			function ( index, element ){
				var $eEntrada  = $( element );
				var criterio = tiposDeFiltros.funcObjCriterioDoFiltro( $eEntrada );
				var nomeDaClasseFiltro;
				if( mapTiposDeCriterio[ criterio ] === undefined || mapTiposDeCriterio[ criterio ] === null ){		
					nomeDaClasseFiltro = 'pj-filtro-' + tiposDeFiltros.qtdDeTiposDeFiltros;
					mapTiposDeCriterio[ criterio ] = nomeDaClasseFiltro;
					tiposDeFiltros.qtdDeTiposDeFiltros++;
					tiposDeFiltros.mapDeFiltros[ nomeDaClasseFiltro ] = { 	classe			:	nomeDaClasseFiltro,
																			qtdDeEntradas	:	1 ,
																			legenda			:	criterio	};
				}else{
					nomeDaClasseFiltro = mapTiposDeCriterio[ criterio ];
					tiposDeFiltros.mapDeFiltros[ nomeDaClasseFiltro ].qtdDeEntradas++;
				}
				tiposDeFiltros.qtdDeEntradas++;
				$eEntrada.addClass( nomeDaClasseFiltro );
			}
		);
		return tiposDeFiltros;
	},
	resumirTexto : function( $objJquery, nrMaxDeCaracteres, nrDeEntradas ){
		
		var strTextoOriginal = $objJquery.text();
		var strNrDeEntradas = '(' + nrDeEntradas + ')';
		$objJquery.attr( 'title' , ( strTextoOriginal + strNrDeEntradas ) );
		
		if( strTextoOriginal.length <= nrMaxDeCaracteres ) return;
		var strReticiencias = '...';
		
		var espacoReservado = strReticiencias.length ;
		
		var strTextoFinal = strTextoOriginal.substring( 0, nrMaxDeCaracteres - espacoReservado - 1 );
		strTextoFinal = strTextoFinal + strReticiencias;
		$objJquery.text( strTextoFinal );
	}
};