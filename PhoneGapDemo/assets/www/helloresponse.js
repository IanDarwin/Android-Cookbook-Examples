function showAlert(){
	alert('Hello World from PHONE GAP using Javascript!!! ');
}

var timer;
var is_timer_on = 0;

/*Setting the Timer to be called after 500ms */
function startRecallAfterXMilliSec() {
		console.log('start');
		is_timer_on = 1;
		timer =	setTimeout("loadNewHB()",500);
	}
	
/*Stop the timer*/	
function stopRecall(){
console.log('stop');
is_timer_on = 0;
		clearTimeout();
}

/* Load the HTML control with new value */
function loadNewHB() {
	if(is_timer_on){
		console.log('load');
		var randomnumber=Math.floor(Math.random()*60)
		$('#name').val(52+randomnumber);
		startRecallAfterXMilliSec();
	}
}
	