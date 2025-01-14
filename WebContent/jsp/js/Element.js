
//function Element(eleID, eleName, eleType)
function Element(eleID,eleName,eleCountry)
{
	this.eleID  	 = eleID;		// The element id
	this.eleName     = eleName;		// The element name
	this.eleCountry  = eleCountry;             // The element type
}
//function eleAdd(eleID, eleName, eleType)
function eleAdd(eleID, eleName, eleCountry)
{
	var ele = new Element(eleID, eleName, eleCountry);
	//var ele = new Element(eleID,eleName);
	this.eleList[this.length++] = ele;
}
function eleGet(eleCountry)
{
	var newList = new Array();
	for (var index=0; index < this.length; index++)
	{
		var element = this.eleList[index];
		if (element.eleCountry == eleCountry)
			newList[newList.length] = element;
	}
	return (newList);
}
function ElementList()
{
	this.eleList = new Array();
	this.length  = 0;
	this.AddElement  = eleAdd;
	this.GetElements = eleGet;
}