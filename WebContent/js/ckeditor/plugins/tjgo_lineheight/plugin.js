(function() {
  
  function getLineHeight( element, useComputedState ) {
    useComputedState = useComputedState === undefined || useComputedState;

    var lineHeight;
    if ( useComputedState )
      lineHeight = element.getComputedStyle( 'line-height' );
    else {
      while ( element.getStyle( 'line-height' ) ) {
        var parent = element.getParent();
        if ( !parent )
          break;
        element = parent;
      }
      lineHeight = element.getStyle( 'line-height' ) || '';

    }
    
    // Sometimes computed values doesn't tell.
    lineHeight && ( lineHeight = lineHeight.replace( /(?:-(?:moz|webkit)-)?(?:start|auto)/i, '' ) );

    //!align && useComputedState && ( align = element.getComputedStyle( 'direction' ) == 'rtl' ? 'right' : 'left' );
    return lineHeight;
  }

  function lineheightCommand( editor, name, value ) {
    this.editor = editor;
    this.name = name;
    this.value = value;
    this.context = 'p';

    // //var classes = editor.config.justifyClasses,
    var blockTag = editor.config.enterMode == CKEDITOR.ENTER_P ? 'p' : 'div';

    this.allowedContent = {
      'caption div h1 h2 h3 h4 h5 h6 p pre td th li': {
        // Do not add elements, but only text-align style if element is validated by other rule.
        propertiesOnly: true,
        styles: 'line-height',
        classes: null
      }
    };

    // // In enter mode BR we need to allow here for div, because when non other
    // // feature allows div justify is the only plugin that uses it.
    if ( editor.config.enterMode == CKEDITOR.ENTER_BR )
      this.allowedContent.div = true;
  }

  lineheightCommand.prototype = {
    exec: function( editor ) {
      
      var selection = editor.getSelection(),
        enterMode = editor.config.enterMode;

      if ( !selection )
        return;

      var bookmarks = selection.createBookmarks(),
        ranges = selection.getRanges( true );

      // var cssClassName = this.cssClassName,
      var iterator, block;

      var useComputedState = editor.config.useComputedState;
      useComputedState = useComputedState === undefined || useComputedState;

      for ( var i = ranges.length - 1; i >= 0; i-- ) {
        iterator = ranges[ i ].createIterator();
        iterator.enlargeBr = enterMode != CKEDITOR.ENTER_BR;

        while ( ( block = iterator.getNextParagraph( enterMode == CKEDITOR.ENTER_P ? 'p' : 'div' ) ) ) {
          //block.removeAttribute( 'align' );
          block.removeStyle( 'line-height' );

          // Remove any of the alignment classes from the className.
          // var className = cssClassName && ( block.$.className = CKEDITOR.tools.ltrim( block.$.className.replace( this.cssClassRegex, '' ) ) );

          var apply = ( this.state == CKEDITOR.TRISTATE_OFF ) && ( ! useComputedState || ( getLineHeight( block, true ) != this.value ) );

          if( apply )
            block.setStyle( 'line-height', this.value );

        }

      }
      editor.focus();
      editor.forceNextSelectionCheck();
      selection.selectBookmarks( bookmarks );
    },

    refresh: function( editor, path ) {
      var firstBlock = path.block || path.blockLimit;
      this.setState( firstBlock.getName() != 'body' && getLineHeight( firstBlock, this.editor.config.useComputedState ) == this.value ? CKEDITOR.TRISTATE_ON : CKEDITOR.TRISTATE_OFF );
    }
  };

  CKEDITOR.plugins.add( 'tjgo_lineheight', {
    lang: 'pt-br', // %REMOVE_LINE_CORE%
    icons: 'lineheight', // %REMOVE_LINE_CORE%
    hidpi: false, // %REMOVE_LINE_CORE%
    init: function( editor ) {
      if ( editor.blockless )
        return;

      var lineHeightCmd = new lineheightCommand( editor, 'tjgo_lineheight', '30px' );

      editor.addCommand( 'lineHeightCmd', lineHeightCmd );


      if ( editor.ui.addButton ) {
        editor.ui.addButton( 'LineHeight', {
          label: editor.lang.tjgo_lineheight.label,
          command: 'lineHeightCmd',
          toolbar: 'blocks'
        });
      }

      //editor.on( 'dirChanged', onDirChanged );
    }
  });
})();
 