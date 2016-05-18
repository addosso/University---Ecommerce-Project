

function rewriteUrl(pageName) {
	  
		    var options = pageName+"?categories=[";
		    var op = document.getElementsByName("categories");
		    
		    for(i=0; i< op.length; i++){
		    	if(op[i].checked){
		    		if(i != op.length)
		    		options+= op[i].value+"-";
		    }
		    }
		    if(options.substr(options.length -1) == "-") 
		    	options = options.substr(0, options.length-1);
		    
		    options +="]";
		    window.location = options;
		
		    
}