<!-- Begin


var message="Sorry, that function is disabled."; 

function click(e) {
if (document.all) {
if (event.button == 2) {
alert(message);
return false;
}
}
if (document.layers) {
if (e.which == 3) {
alert(message);
return false;
}
}
}
if (document.layers) {
document.captureEvents(Event.MOUSEDOWN);
}
document.onmousedown=click;

function stopError() {
 return true;
 }
window.onerror = stopError;
 

//  End -->
