var BYTE_LIMIT = 4096;
var data = [BYTE_LIMIT];
var currentBytes = 0;
var current = 0;

update(var array) {
	
	var csvLine;
	
	for (i = 0; i < array.length; I++) {
		
		line += array[i];
		
		if (i = array.length) continue;
		line += ", ";
	}
	
	currentBytes += csvLine.length;
	
	data[current] = csvLine;
	if (currentBytes >= BYTE_LIMIT) {
	
		flush();
		currentBytes = 0;
		current = 0;
	} else {
	
		current++;
	}
}