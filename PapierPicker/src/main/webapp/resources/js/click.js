$('#exampleModal').on('show.bs.modal', function (event) {
  var button = $(event.relatedTarget) // Button that triggered the modal
  var recipient = button.data('whatever') // Extract info from data-* attributes
  var recipient2 = button.data('whatever2') // Extract info from data-* attributes
  var recipient3 = button.data('whatever3') // Extract info from data-* attributes
  // If necessary, you could initiate an AJAX request here (and then do the updating in a callback).
  // Update the modal's content. We'll use jQuery here, but you could use a data binding library or other methods instead.
  var modal = $(this)
  document.getElementById('dataDate').setAttribute('value', recipient);
  document.getElementById("p1").innerHTML = recipient2 + "&nbsp;" + recipient3;
})