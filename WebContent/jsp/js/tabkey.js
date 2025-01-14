/*
Script Name:  Tab Key Emulation
Description:  The tab key is no longer required to tab between fields.
              The user can go to the next form field just by pressing the enter key instead of the tab key.
              Useful with 10-key form input.  What a time saver!

//

<!-- Begin*/
  nextfield = "box1"; // name of first box on page
  netscape  = "";
  ver = navigator.appVersion; len = ver.length;
  for(iln = 0; iln < len; iln++)
    if (ver.charAt(iln) == "(") break;
  netscape = (ver.charAt(iln+1).toUpperCase() != "C");


   function keyDown(DnEvents) { // handles keypress
     // determines whether Netscape or Internet Explorer
     k = (netscape) ? DnEvents.which : window.event.keyCode;
     if (k == 13)
     { // enter key pressed
       if (nextfield == 'done')
         return true; // submit, we finished all fields
       else {
         // we're not done yet, send focus to next box
         eval('document.yourform.' + nextfield + '.focus()');
         return false;
       }
     }
   }

   document.onkeydown = keyDown; // work together to analyze keystrokes
   if (netscape) document.captureEvents(Event.KEYDOWN|Event.KEYUP);
