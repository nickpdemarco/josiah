// BEGIN REDACT
/**
 * Front end logic for providing real time autocorrect suggestions.
 */

$(function() {
  // Cache common queries to improve performance and style.
  var $input = $('#autocorrect-input');
  var $suggestions = $('#suggestions');

  // When the input box gets focus, show autocorrect suggestions and configure
  // position and size of suggestions dropdown.
  $input.focus(function() {
    $suggestions.show();

    $suggestions.offset({
      top: $input.offset().top + $input.outerHeight(),
      left: $input.offset().left
    });
    $suggestions.width($input.width());
  });

  // When the input box loses focus to anything but the suggestions box, hide
  // the suggestions box.
  $input.blur(function(e) {
    if (!e.relatedTarget || e.relatedTarget.className != 'suggestion-text') {
      $suggestions.hide();
    }
  });

  // On each key up in the input box, query the Autocorrect API for suggestions.
  $input.on('keyup change', function() {
    // Grab our current autocorrect configuration and text to send off.
    var queryParams = {
      whitespace: $('#whitespace').is(':checked'),
      prefix: $('#prefix').is(':checked'),
      smart: $('#smart').is(':checked'),
      led: $('#led').is(':checked'),
      ledNum: $('#led-num').val(),
      text: $input.val()
    };

    // Query the API server.
    $.get('/autocorrect/suggest', queryParams).
        // On success, insert the suggestions into the DOM.
        done(function(data) {
          data = JSON.parse(data);
          for (var i = 0; i < data.length; i++) {
            $('#suggestion-' + i).text(data[i] ? data[i] : "");
          }
        }).
        // On failure, alert the user.
        fail(function() {
          alert('There was an error getting suggestions from the server.');
        });
  });

  // Bind each of the suggestions to a click handler that fills the input
  // box with that suggestion's text when clicked.
  for (var i = 0; i < 5; i++) {
    // We have to create a function and immediately apply it to create a
    // closure that keeps an instance of i separate from the for-loop's.
    // Otherwise as the for-loop mutates i, all click handlers will have
    // i = 5.
    (function(i) {
      var $currentSuggestion = $('#suggestion-' + i);
      $currentSuggestion.click(function() {
        $input.val($currentSuggestion.text());
        $input.focus();
      });
    })(i);
  }
});
// END REDACT
