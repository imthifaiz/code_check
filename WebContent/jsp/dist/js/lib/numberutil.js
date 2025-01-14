/**
 * Extended toFixed functionality to support the Scan2Track requirement 
 */
Number.prototype.toFixedSpecial = function(n) {
	
	var str = this.toFixed(n);
    if (str.indexOf('e+') < 0){
        return str.replace(/\.?0+$/, '');
    }
    // if number is in scientific notation, pick (b)ase and (p)ower
    str = str.replace('.', '').split('e+').reduce(function(p, b) {
        return p + Array(b - p.length + 2).join(0).replace(/\.?0+$/, '');
    }) + '.' + Array(n + 1).join(0);
    //	Check with capital E
    str = str.toFixed(n);
    if (str.indexOf('E+') < 0){
    	return str;
    }
 	// if number is in scientific notation, pick (b)ase and (p)ower
    return str.replace('.', '').split('E+').reduce(function(p, b) {
        return p + Array(b - p.length + 2).join(0).replace(/\.?0+$/, '');
    }) + '.' + Array(n + 1).join(0);
	
	
};
//old code 27.11.18 by azees (round off issue)
Number.prototype.toFixedSpecial1 = function(n) {
	var str1 = this.toString();
	var str = "";
	if(str1.indexOf(".")!='-1')	
	str = str1.slice(0, (str1.indexOf("."))+(n+1));
	else
		str = str1;
	if (str.toString().indexOf('e+') < 0)
	return str;

	// if number is in scientific notation, pick (b)ase and (p)ower
	return str.replace('.', '').split('e+').reduce(function(p, b) {
	return p + Array(b - p.length + 2).join(0);
	}) + '.' + Array(n + 1).join(0);	


	};
/*Number.prototype.toFixedSpecial1 = function(n) {
	var  str1 = this.toString();
	var str = Math.floor( str1 *Math.pow(10,n))/(Math.pow(10,n));
	 
	    if (str.toString().indexOf('e+') < 0)
	        return str;
	    
	    // if number is in scientific notation, pick (b)ase and (p)ower
	    return str.replace('.', '').split('e+').reduce(function(p, b) {
	        return p + Array(b - p.length + 2).join(0);
	    }) + '.' + Array(n + 1).join(0);	    

	    str = Math.floor( str1 *Math.pow(10,n))/(Math.pow(10,n));
	    if (str.toString().indexOf('E+') < 0)
	        return str;
	    
	    // if number is in scientific notation, pick (b)ase and (p)ower
	    return str.replace('.', '').split('E+').reduce(function(p, b) {
	        return p + Array(b - p.length + 2).join(0);
	    }) + '.' + Array(n + 1).join(0);
};*/