<%@ page import="java.util.*,java.io.*" %>
<script language="javascript1.2">

// menubar
if (!exclude) {

// *** POSITIONING AND STYLES *********************************************


var menuALIGN  = "center",	absLEFT     = 	0,		absTOP      = 	82;
var staticMENU = false,		stretchMENU =   true,		showBORDERS = true;

var baseHREF   = "js/",	        zORDER      = 	100,		mCOLOR      = 	"#dddddd"; //main menu bgcolor

var rCOLOR     = "#cccccc",	keepLIT     =	true,		bSIZE       = 	1;
var bCOLOR     = "white",	aLINK       = 	"black",	aHOVER      = 	"";
var aDEC       = "none",	fFONT       = 	"arial",	fSIZE       = 	12;
var fWEIGHT    = "bold",	tINDENT     = 	7,		vPADDING    = 	5;
var vtOFFSET   = 0,		vOFFSET     = 	0,		hOFFSET     = 	0;


var smCOLOR    = "white",	srCOLOR     = 	"#cccccc",	sbSIZE      = 	1;
var sbCOLOR    = "black",	saLINK      = 	"black",	saHOVER     = 	"";
var saDEC      = "none",	sfFONT      = 	"arial",	sfSIZE      = 	12;
var sfWEIGHT   = "normal",	stINDENT    = 	5,		svPADDING   =   2;
var svtOFFSET  = 0,		shSIZE      =	0,		shCOLOR     =	"#ffffff";
var shOPACITY  = 45,             vLINK = "black";


//** LINKS ***********************************************************

<%! ArrayList menulist; Hashtable[] col= new Hashtable[11];
Hashtable[] boldcol= new Hashtable[11];
    public void getHashContents(Hashtable hash,JspWriter out) throws Exception
    {
        for(int i=0; i < hash.size(); i++)
        {
	Hashtable subHash = (Hashtable)hash.get(new Integer(i));
	if(subHash != null)
            {
                Enumeration e = subHash.keys();
                while(e.hasMoreElements())
                {
                    String Key = e.nextElement().toString();
                    String url = subHash.get(Key).toString();

  	            out.write("addSubmenuItem(\""+url+"\",\""+Key+"\",\"\");");

                }
            }
        }
    }
    public void getBoldHashContents(Hashtable hash,JspWriter out) throws Exception
    {
       int j=0;
       for(int i=0; i < hash.size(); i++)
       {
	Hashtable subHash = (Hashtable)hash.get(new Integer(i));
	if(subHash != null)
            {
                Enumeration e = subHash.keys();
                while(e.hasMoreElements())
                {
                	
                    String Key = e.nextElement().toString();
                    String url = subHash.get(Key).toString();
                  //  System.out.println("kye...................."+url );
                   String urlPart = Key.substring(0,7);
                   if(urlPart.equalsIgnoreCase("Summary")||  url.equals("closeByOrder.jsp")  )
                   	{
                    	 out.write("addSubmenuItem(\""+url+"\",\""+"<b>"+Key+"</b>"+"\",\"\");");
                   	}else{
                        out.write("addSubmenuItem(\""+url+"\",\""+Key+"\",\"\");");
                        }
                 
                    
                       
				}				
				 }
            }
    }  
    

%>
<%
    menulist = (ArrayList)session.getAttribute("DROPDOWN_MENU");  //      Getting the menu lists from session
    for(int i=0;i<11;i++)
    {
    col[i] = (Hashtable) menulist.get(i);

     }
    for(int j=0;j<11;j++)
    {
    	boldcol[j]=(Hashtable) menulist.get(j);
    }
%>
addMainItem("", "USER ADMIN",113,"center","");
defineSubmenuProperties(225,"left","left");
<%
//  USER ADMIN MENU
getBoldHashContents(boldcol[0],out);

 %>
addMainItem("", "SYS MASTER",123,"center","");
defineSubmenuProperties(260,"left","left");
<%
//    MESSAGE MENU
//getHashContents(col[2],out);
getBoldHashContents(boldcol[2],out);
%>
addMainItem("", "SYS ADMIN",123,"center","");
defineSubmenuProperties(233,"left","left");

<%
//     AUTHORISATION MENU
//getHashContents(col[1],out);
getBoldHashContents(boldcol[1],out);
 %>
 
 addMainItem("", "ORDER ADMIN",123,"center","");
 defineSubmenuProperties(273,"left","left");

 <%
     //Order Admin
 getBoldHashContents(boldcol[4],out);
  %>
  
  addMainItem("", "ORDER CONFIG",123,"center","");
  defineSubmenuProperties(273,"left","left");

  <%
      //Order CONFIG
  getBoldHashContents(boldcol[6],out);
   %>
  
 
 addMainItem("", "ORDER MGT",123,"center","");
 defineSubmenuProperties(273,"left","left");

 <%
     //      
 //getHashContents(col[3],out);
 getBoldHashContents(boldcol[3],out);
  %>

addMainItem("", "INBOUND TRN",123,"center","");
defineSubmenuProperties(215,"left","left");
<%
//    Inbound MENU

getHashContents(col[9],out);
%>
addMainItem("", "In House",123,"center","");
defineSubmenuProperties(200,"left","left");
<%
    //instore MENU
getHashContents(col[5],out);
%>
addMainItem("", "OUTBOUND TRN",123,"center","");
defineSubmenuProperties(280,"left","left");
<%
//    REPORTS MENU

getHashContents(col[10],out);
%>


addMainItem("", "REPORTS",113,"center","");
defineSubmenuProperties(320,"left","left"); //225
<%
//    REPORTS MENU
getHashContents(col[7],out);
%>




}
// End of custom.js ************************

</script>



