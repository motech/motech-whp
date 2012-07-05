(function( $ ) {
    $.widget( "ui.combobox", {
        autoComplete: {},
        _create: function() {
            var input,
                self = this,
                select = this.element.hide(),
                selected = select.children( ":selected" ),
                value = selected.val() ? selected.text() : "",
                wrapper = this.wrapper = $( "<span>" )
                    .addClass( "ui-combobox" )
                    .insertAfter( select );

            input = $( "<input>" )
                .appendTo( wrapper )
                .val( value )
                .keypress(function(e) {
                    if(e.which&& e.which==13 || e.keyCode && e.keyCode==13)
                    {
                        //same code as change function
                        var valid = false;
                        if($(input).val()!="") {
                        var matcher = new RegExp( $.ui.autocomplete.escapeRegex($(input).val()), "i" );
                        valid = false;
                        select.children( "option" ).each(function() {
                            if ( $( this ).text().match( matcher ) ) {
                                $(input).val($(this).text());
                                this.selected = valid = true;
                                return false;
                            }
                        });
                        }
                        if ( !valid ) {
                            // remove invalid value, as it didn't match anything
                            $( this ).val( "" );
                            select.val( "" );
                            input.data( "autocomplete" ).term = "";
                            $(self.element).trigger("invalid-value", event );

                            return false;
                        }
                    }
                    $(self.element).trigger('keypress',[e]);
                })
                .addClass( "ui-state-default ui-combobox-input" )
                .autocomplete({
                    delay: 0,
                    minLength: 0,
                    source: function( request, response ) {
                        var matcher = new RegExp( $.ui.autocomplete.escapeRegex(request.term), "i" );
                        response( select.children( "option" ).map(function() {
                            var text = $( this ).text();
                            if ( this.value && ( !request.term || matcher.test(text) ) )
                                return {
                                    label: text.replace(
                                        new RegExp(
                                            "(?![^&;]+;)(?!<[^<>]*)(" +
                                                $.ui.autocomplete.escapeRegex(request.term) +
                                                ")(?![^<>]*>)(?![^&;]+;)", "gi"
                                        ), "<strong>$1</strong>" ),
                                    value: text,
                                    option: this
                                };
                        }) );
                    },
                    select: function( event, ui ) {
                        ui.item.option.selected = true;
                        self._trigger( "selected", event, {
                            item: ui.item.option
                        });
                        $(self.element).trigger( "autocomplete-selected", event, {
                            item: ui.item.option
                        });
                    },
                    change: function( event, ui ) {
                        if ( !ui.item ) {
                           var valid = false;
                           if($(input).val()!="") {
                            var matcher = new RegExp( $.ui.autocomplete.escapeRegex($(input).val()), "i" );
                            select.children( "option" ).each(function() {
                                if ( $( this).text().match( matcher ) ) {
                                    $(input).val($(this).text());
                                    $(select).val($(this.text));
                                    this.selected = valid = true;
                                    var option = $(this)
                                    $(self.element).trigger( "autocomplete-selected", event ,{item:option});
                                    return false;
                                }
                            });
                           }
                            if ( !valid ) {
                                // remove invalid value, as it didn't match anything
                                $( this ).val( "" );
                                select.val( "" );
                                input.data( "autocomplete" ).term = "";
                                $(self.element).trigger("invalid-value", event );

                                return false;
                            }
                        }

                    }
                })
                .addClass( "ui-widget ui-widget-content ui-corner-left" );

            input.attr("id", select.attr("id") + "-autocomplete");
            input.data( "autocomplete" )._renderItem = function( ul, item ) {
                return $( "<li></li>" )
                    .data( "item.autocomplete", item )
                    .append( "<a>" + item.label + "</a>" )
                    .appendTo( ul );
            };

            $( "<a>" )
                .attr( "tabIndex", -1 )
                .attr( "title", "Show All Items" )
                .appendTo( wrapper )
                .button({
                    icons: {
                        primary: "ui-icon-triangle-1-s"
                    },
                    text: false
                })
                .removeClass( "ui-corner-all" )
                .addClass( "ui-corner-right ui-combobox-toggle" )
                .click(function() {
                    // close if already visible
                    if ( input.autocomplete( "widget" ).is( ":visible" ) ) {
                        input.autocomplete( "close" );
                        return;
                    }

                    $( this ).blur();

                    // pass empty string as value to search for, displaying all results
                    input.autocomplete( "search", "" );
                    input.focus();
                });

            this.autoComplete = input.autocomplete
        },

        destroy: function() {
            this.wrapper.remove();
            this.element.show();
            $.Widget.prototype.destroy.call( this );
        }
    });
})( jQuery );