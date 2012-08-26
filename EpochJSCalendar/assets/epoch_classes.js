/*****************************************************************************
Copyright (C) 2006  Nick Baicoianu

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
*****************************************************************************/
//constructor for the main Epoch class (ENGLISH VERSION)
function Epoch(name,mode,targetelement,multiselect)
{
	this.state = 0;
	this.name = name;
	this.curDate = new Date();
	this.mode = mode;
	this.selectMultiple = (multiselect == true); //'false' is not true or not set at all
	
	//the various calendar variables
	//this.selectedDate = this.curDate;
	this.selectedDates = new Array();
	this.calendar;
	this.calHeading;
	this.calCells;
	this.rows;
	this.cols;
	this.cells = new Array();
	
	//The controls
	this.monthSelect;
	this.yearSelect;
	
	//standard initializations
	this.mousein = false;
	this.calConfig();
	this.setDays();
	this.displayYear = this.displayYearInitial;
	this.displayMonth = this.displayMonthInitial;
	
	this.createCalendar(); //create the calendar DOM element and its children, and their related objects
	
	if(this.mode == 'popup' && targetelement && targetelement.type == 'text') //if the target element has been set to be an input text box
	{
		this.tgt = targetelement;
		this.calendar.style.position = 'absolute';
		this.topOffset = this.tgt.offsetHeight; // the vertical distance (in pixels) to display the calendar from the Top of its input element
		this.leftOffset = 0; 					// the horizontal distance (in pixels) to display the calendar from the Left of its input element
		this.calendar.style.top = this.getTop(targetelement) + this.topOffset + 'px';
		this.calendar.style.left = this.getLeft(targetelement) + this.leftOffset + 'px';
		document.body.appendChild(this.calendar);
		this.tgt.calendar = this;
		this.tgt.onfocus = function () {this.calendar.show();}; //the calendar will popup when the input element is focused
		this.tgt.onblur = function () {if(!this.calendar.mousein){this.calendar.hide();}}; //the calendar will popup when the input element is focused
	}
	else
	{
		this.container = targetelement;
		this.container.appendChild(this.calendar);
	}
	
	this.state = 2; //0: initializing, 1: redrawing, 2: finished!
	this.visible ? this.show() : this.hide();
}
//-----------------------------------------------------------------------------
Epoch.prototype.calConfig = function () //PRIVATE: initialize calendar variables
{
	//this.mode = 'flat'; //can be 'flat' or 'popup'
	this.displayYearInitial = this.curDate.getFullYear(); //the initial year to display on load
	this.displayMonthInitial = this.curDate.getMonth(); //the initial month to display on load (0-11)
	this.rangeYearLower = 2005;
	this.rangeYearUpper = 2037;
	this.minDate = new Date(2005,0,1);
	this.maxDate = new Date(2037,0,1);
	this.startDay = 0; // the day the week will 'start' on: 0(Sun) to 6(Sat)
	this.showWeeks = true; //whether the week numbers will be shown
	this.selCurMonthOnly = false; //allow user to only select dates in the currently displayed month
	this.clearSelectedOnChange = true; //whether to clear all selected dates when changing months
	
	//flat mode-only settings:
	//this.selectMultiple = true; //whether the user can select multiple dates (flat mode only)

	switch(this.mode) //set the variables based on the calendar mode
	{
		case 'popup': //popup options
			this.visible = false;
			break;
		case 'flat':
			this.visible = true;
			
			break;
	}
	this.setLang();
};
//-----------------------------------------------------------------------------
Epoch.prototype.setLang = function()  //all language settings for Epoch are made here.  Check Date.dateFormat() for the Date object's language settings
{
	this.daylist = new Array('Su','Mo','Tu','We','Th','Fr','Sa','Su','Mo','Tu','We','Th','Fr','Sa'); /*<lang:en>*/
	this.months_sh = new Array('Jan','Feb','Mar','Apr','May','Jun','Jul','Aug','Sep','Oct','Nov','Dec');
	this.monthup_title = 'Go to the next month';
	this.monthdn_title = 'Go to the previous month';
	this.clearbtn_caption = 'Clear';
	this.clearbtn_title = 'Clears any dates selected on the calendar';
	this.maxrange_caption = 'This is the maximum range';
};
//-----------------------------------------------------------------------------
Epoch.prototype.getTop = function (element) //PRIVATE: returns the absolute Top value of element, in pixels
{
    var oNode = element;
    var iTop = 0;
    
    while(oNode.tagName != 'BODY') {
        iTop += oNode.offsetTop;
        oNode = oNode.offsetParent;
    }
    
    return iTop;
};
//-----------------------------------------------------------------------------
Epoch.prototype.getLeft = function (element) //PRIVATE: returns the absolute Left value of element, in pixels
{
    var oNode = element;
    var iLeft = 0;
    
    while(oNode.tagName != 'BODY') {
        iLeft += oNode.offsetLeft;
        oNode = oNode.offsetParent;        
    }
    
    return iLeft;
};
//-----------------------------------------------------------------------------
Epoch.prototype.show = function () //PUBLIC: displays the calendar
{
	this.calendar.style.display = 'block';
	this.visible = true;
};
//-----------------------------------------------------------------------------
Epoch.prototype.hide = function () //PUBLIC: Hides the calendar
{
	this.calendar.style.display = 'none';
	this.visible = false;
};
//-----------------------------------------------------------------------------
Epoch.prototype.toggle = function () //PUBLIC: Toggles (shows/hides) the calendar depending on its current state
{
	if(this.visible) {
		this.hide();
	}
	else {
		this.show();
	}
};
//-----------------------------------------------------------------------------
Epoch.prototype.setDays = function ()  //PRIVATE: initializes the standard Gregorian Calendar parameters
{
	this.daynames = new Array();
	var j=0;
	for(var i=this.startDay; i< this.startDay + 7;i++) {
		this.daynames[j++] = this.daylist[i];
	}
		
	this.monthDayCount = new Array(31,((this.curDate.getFullYear() - 2000) % 4 ? 28 : 29),31,30,31,30,31,31,30,31,30,31);
};
//-----------------------------------------------------------------------------
Epoch.prototype.setClass = function (element,className) //PRIVATE: sets the CSS class of the element, W3C & IE
{
	element.setAttribute('class',className);
	element.setAttribute('className',className); //<iehack>
};
//-----------------------------------------------------------------------------
Epoch.prototype.createCalendar = function ()  //PRIVATE: creates the full DOM implementation of the calendar
{
	var tbody, tr, td;
	this.calendar = document.createElement('table');
	this.calendar.setAttribute('id',this.name+'_calendar');
	this.setClass(this.calendar,'calendar');
	//to prevent IE from selecting text when clicking on the calendar
	this.calendar.onselectstart = function() {return false;};
	this.calendar.ondrag = function() {return false;};
	tbody = document.createElement('tbody');
	
	//create the Main Calendar Heading
	tr = document.createElement('tr');
	td = document.createElement('td');
	td.appendChild(this.createMainHeading());
	tr.appendChild(td);
	tbody.appendChild(tr);
	
	//create the calendar Day Heading
	tr = document.createElement('tr');
	td = document.createElement('td');
	td.appendChild(this.createDayHeading());
	tr.appendChild(td);
	tbody.appendChild(tr);

	//create the calendar Day Cells
	tr = document.createElement('tr');
	td = document.createElement('td');
	td.setAttribute('id',this.name+'_cell_td');
	this.calCellContainer = td;	//used as a handle for manipulating the calendar cells as a whole
	td.appendChild(this.createCalCells());
	tr.appendChild(td);
	tbody.appendChild(tr);
	
	//create the calendar footer
	tr = document.createElement('tr');
	td = document.createElement('td');
	td.appendChild(this.createFooter());
	tr.appendChild(td);
	tbody.appendChild(tr);
	
	//add the tbody element to the main calendar table
	this.calendar.appendChild(tbody);

	//and add the onmouseover events to the calendar table
	this.calendar.owner = this;
	this.calendar.onmouseover = function() {this.owner.mousein = true;};
	this.calendar.onmouseout = function() {this.owner.mousein = false;};
};
//-----------------------------------------------------------------------------
Epoch.prototype.createMainHeading = function () //PRIVATE: Creates the primary calendar heading, with months & years
{
	//create the containing <div> element
	var container = document.createElement('div');
	container.setAttribute('id',this.name+'_mainheading');
	this.setClass(container,'mainheading');
	//create the child elements and other variables
	this.monthSelect = document.createElement('select');
	this.yearSelect = document.createElement('select');
	var monthDn = document.createElement('input'), monthUp = document.createElement('input');
	var opt, i;
	//fill the month select box
	for(i=0;i<12;i++)
	{
		opt = document.createElement('option');
		opt.setAttribute('value',i);
		if(this.state == 0 && this.displayMonth == i) {
			opt.setAttribute('selected','selected');
		}
		opt.appendChild(document.createTextNode(this.months_sh[i]));
		this.monthSelect.appendChild(opt);
	}
	//and fill the year select box
	for(i=this.rangeYearLower;i<=this.rangeYearUpper;i++)
	{
		opt = document.createElement('option');
		opt.setAttribute('value',i);
		if(this.state == 0 && this.displayYear == i) {
			opt.setAttribute('selected','selected');
		}
		opt.appendChild(document.createTextNode(i));
		this.yearSelect.appendChild(opt);		
	}
	//add the appropriate children for the month buttons
	monthUp.setAttribute('type','button');
	monthUp.setAttribute('value','>');
	monthUp.setAttribute('title',this.monthup_title);
	monthDn.setAttribute('type','button');
	monthDn.setAttribute('value','<');
	monthDn.setAttribute('title',this.monthdn_title);
	this.monthSelect.owner = this.yearSelect.owner = monthUp.owner = monthDn.owner = this;  //hack to allow us to access this calendar in the events (<fix>??)
	
	//assign the event handlers for the controls
	monthUp.onmouseup = function () {this.owner.nextMonth();};
	monthDn.onmouseup = function () {this.owner.prevMonth();};
	this.monthSelect.onchange = function() {
		this.owner.displayMonth = this.value;
		this.owner.displayYear = this.owner.yearSelect.value; 
		this.owner.goToMonth(this.owner.displayYear,this.owner.displayMonth);
	};
	this.yearSelect.onchange = function() {
		this.owner.displayMonth = this.owner.monthSelect.value;
		this.owner.displayYear = this.value; 
		this.owner.goToMonth(this.owner.displayYear,this.owner.displayMonth);
	};
	
	//and finally add the elements to the containing div
	container.appendChild(monthDn);
	container.appendChild(this.monthSelect);
	container.appendChild(this.yearSelect);
	container.appendChild(monthUp);
	return container;
};
//-----------------------------------------------------------------------------
Epoch.prototype.createFooter = function () //PRIVATE: creates the footer of the calendar - goes under the calendar cells
{
	var container = document.createElement('div');
	var clearSelected = document.createElement('input');
	clearSelected.setAttribute('type','button');
	clearSelected.setAttribute('value',this.clearbtn_caption);
	clearSelected.setAttribute('title',this.clearbtn_title);
	clearSelected.owner = this;
	clearSelected.onclick = function() { this.owner.resetSelections(false);};
	container.appendChild(clearSelected);
	return container;
};
//-----------------------------------------------------------------------------
Epoch.prototype.resetSelections = function (returnToDefaultMonth)  //PRIVATE: reset the calendar's selection variables to defaults
{
	this.selectedDates = new Array();
	this.rows = new Array(false,false,false,false,false,false,false);
	this.cols = new Array(false,false,false,false,false,false,false);
	if(this.tgt)  //if there is a target element, clear it too
	{
		this.tgt.value = '';
		if(this.mode == 'popup') {//hide the calendar if in popup mode
			this.hide();
		}
	}
		
	if(returnToDefaultMonth == true) {
		this.goToMonth(this.displayYearInitial,this.displayMonthInitial);
	}
	else {
		this.reDraw();
	}
};
//-----------------------------------------------------------------------------
Epoch.prototype.createDayHeading = function ()  //PRIVATE: creates the heading containing the day names
{
	//create the table element
	this.calHeading = document.createElement('table');
	this.calHeading.setAttribute('id',this.name+'_caldayheading');
	this.setClass(this.calHeading,'caldayheading');
	var tbody,tr,td;
	tbody = document.createElement('tbody');
	tr = document.createElement('tr');
	this.cols = new Array(false,false,false,false,false,false,false);
	
	//if we're showing the week headings, create an empty <td> for filler
	if(this.showWeeks)
	{
		td = document.createElement('td');
		td.setAttribute('class','wkhead');
		td.setAttribute('className','wkhead'); //<iehack>
		tr.appendChild(td);
	}
	//populate the day titles
	for(var dow=0;dow<7;dow++)
	{
		td = document.createElement('td');
		td.appendChild(document.createTextNode(this.daynames[dow]));
		if(this.selectMultiple) { //if selectMultiple is true, assign the cell a CalHeading Object to handle all events
			td.headObj = new CalHeading(this,td,(dow + this.startDay < 7 ? dow + this.startDay : dow + this.startDay - 7));
		}
		tr.appendChild(td);
	}
	tbody.appendChild(tr);
	this.calHeading.appendChild(tbody);
	return this.calHeading;	
};
//-----------------------------------------------------------------------------
Epoch.prototype.createCalCells = function ()  //PRIVATE: creates the table containing the calendar day cells
{
	this.rows = new Array(false,false,false,false,false,false);
	this.cells = new Array();
	var row = -1, totalCells = (this.showWeeks ? 48 : 42);
	var beginDate = new Date(this.displayYear,this.displayMonth,1);
	var endDate = new Date(this.displayYear,this.displayMonth,this.monthDayCount[this.displayMonth]);
	var sdt = new Date(beginDate);
	sdt.setDate(sdt.getDate() + (this.startDay - beginDate.getDay()) - (this.startDay - beginDate.getDay() > 0 ? 7 : 0) );
	//create the table element
	this.calCells = document.createElement('table');
	this.calCells.setAttribute('id',this.name+'_calcells');
	this.setClass(this.calCells,'calcells');
	var tbody,tr,td;
	tbody = document.createElement('tbody');
	for(var i=0;i<totalCells;i++)
	{
		if(this.showWeeks) //if we are showing the week headings
		{
			if(i % 8 == 0)
			{
				row++;
				tr = document.createElement('tr');
				td = document.createElement('td');
				if(this.selectMultiple) { //if selectMultiple is enabled, create the associated weekObj objects
					td.weekObj = new WeekHeading(this,td,sdt.getWeek(),row)
				}
				else //otherwise just set the class of the td for consistent look
				{
					td.setAttribute('class','wkhead');
					td.setAttribute('className','wkhead'); //<iehack>
				}
				td.appendChild(document.createTextNode(sdt.getWeek()));			
				tr.appendChild(td);
				i++;
			}
		}
		else if(i % 7 == 0) //otherwise, new row every 7 cells
		{
			row++;
			tr = document.createElement('tr');
		}
		//create the day cells
		td = document.createElement('td');
		td.appendChild(document.createTextNode(sdt.getDate()));// +' ' +sdt.getUeDay()));
		var cell = new CalCell(this,td,sdt,row);
		this.cells.push(cell);
		td.cellObj = cell;
		sdt.setDate(sdt.getDate() + 1); //increment the date
		tr.appendChild(td);
		tbody.appendChild(tr);
	}
	this.calCells.appendChild(tbody);
	this.reDraw();
	return this.calCells;
};
//-----------------------------------------------------------------------------
Epoch.prototype.reDraw = function () //PRIVATE: reapplies all the CSS classes for the calendar cells, usually called after chaning their state
{
	this.state = 1;
	var i,j;
	for(i=0;i<this.cells.length;i++) {
		this.cells[i].selected = false;
	}
	for(i=0;i<this.cells.length;i++)
	{
		for(j=0;j<this.selectedDates.length;j++) { //if the cell's date is in the selectedDates array, set its selected property to true
			if(this.cells[i].date.getUeDay() == this.selectedDates[j].getUeDay() ) {
				this.cells[i].selected = true;
			}
		}

		this.cells[i].setClass();
	}
	//alert(this.selectedDates);
	this.state = 2;
};
//-----------------------------------------------------------------------------
Epoch.prototype.deleteCells = function () //PRIVATE: removes the calendar cells from the DOM (does not delete the cell objects associated with them
{
	this.calCellContainer.removeChild(this.calCellContainer.firstChild); //get a handle on the cell table (optional - for less indirection)
	this.cells = new Array(); //reset the cells array
};
//-----------------------------------------------------------------------------
Epoch.prototype.goToMonth = function (year,month) //PUBLIC: sets the calendar to display the requested month/year
{
	this.monthSelect.value = this.displayMonth = month;
	this.yearSelect.value = this.displayYear = year;
	this.deleteCells();
	this.calCellContainer.appendChild(this.createCalCells());
};
//-----------------------------------------------------------------------------
Epoch.prototype.nextMonth = function () //PUBLIC: go to the next month.  if the month is december, go to january of the next year
{
	
	//increment the month/year values, provided they're within the min/max ranges
	if(this.monthSelect.value < 11) {
		this.monthSelect.value++;
	}
	else
	{
		if(this.yearSelect.value < this.rangeYearUpper)
		{
			this.monthSelect.value = 0;
			this.yearSelect.value++;
		}
		else {
			alert(this.maxrange_caption);
		}
	}
	//assign the currently displaying month/year values
	this.displayMonth = this.monthSelect.value;
	this.displayYear = this.yearSelect.value;
	
	//and refresh the calendar for the new month/year
	this.deleteCells();
	this.calCellContainer.appendChild(this.createCalCells());
};
//-----------------------------------------------------------------------------
Epoch.prototype.prevMonth = function () //PUBLIC: go to the previous month.  if the month is january, go to december of the previous year
{
	//increment the month/year values, provided they're within the min/max ranges
	if(this.monthSelect.value > 0)
		this.monthSelect.value--;
	else
	{
		if(this.yearSelect.value > this.rangeYearLower)
		{
			this.monthSelect.value = 11;
			this.yearSelect.value--;
		}
		else {
			alert(this.maxrange_caption);
		}
	}
	
	//assign the currently displaying month/year values
	this.displayMonth = this.monthSelect.value;
	this.displayYear = this.yearSelect.value;
	
	//and refresh the calendar for the new month/year
	this.deleteCells();
	this.calCellContainer.appendChild(this.createCalCells());
};
//-----------------------------------------------------------------------------
Epoch.prototype.addZero = function (vNumber) //PRIVATE: pads a 2 digit number with a leading zero
{
	return ((vNumber < 10) ? '0' : '') + vNumber;
};
//-----------------------------------------------------------------------------
Epoch.prototype.addDates = function (dates,redraw)  //PUBLIC: adds the array "dates" to the calendars selectedDates array (no duplicate dates) and redraws the calendar
{
	var j,in_sd;
	for(var i=0;i<dates.length;i++)
	{	
		in_sd = false;
		for(j=0;j<this.selectedDates.length;j++)
		{
			if(dates[i].getUeDay() == this.selectedDates[j].getUeDay())
			{
				in_sd = true;
				break;
			}
		}
		if(!in_sd) { //if the date isn't already in the array, add it!
			this.selectedDates.push(dates[i]);
		}
	}
	if(redraw != false) {//redraw  the calendar if "redraw" is false or undefined
		this.reDraw();
	}
};
//-----------------------------------------------------------------------------
Epoch.prototype.removeDates = function (dates,redraw)  //PUBLIC: adds the dates to the calendars selectedDates array and redraws the calendar
{
	var j;
	for(var i=0;i<dates.length;i++)
	{
		for(j=0;j<this.selectedDates.length;j++)
		{
			if(dates[i].getUeDay() == this.selectedDates[j].getUeDay()) { //search for the dates in the selectedDates array, removing them if the dates match
				this.selectedDates.splice(j,1);
			}
		}
	}
	if(redraw != false) { //redraw  the calendar if "redraw" is false or undefined
		this.reDraw();
	}
};
//-----------------------------------------------------------------------------
Epoch.prototype.outputDate = function (vDate, vFormat) //PUBLIC: outputs a date in the appropriate format (DEPRECATED)
{
	var vDay			= this.addZero(vDate.getDate()); 
	var vMonth			= this.addZero(vDate.getMonth() + 1); 
	var vYearLong		= this.addZero(vDate.getFullYear()); 
	var vYearShort		= this.addZero(vDate.getFullYear().toString().substring(3,4)); 
	var vYear			= (vFormat.indexOf('yyyy') > -1 ? vYearLong : vYearShort);
	var vHour			= this.addZero(vDate.getHours()); 
	var vMinute			= this.addZero(vDate.getMinutes()); 
	var vSecond			= this.addZero(vDate.getSeconds()); 
	return vFormat.replace(/dd/g, vDay).replace(/mm/g, vMonth).replace(/y{1,4}/g, vYear).replace(/hh/g, vHour).replace(/nn/g, vMinute).replace(/ss/g, vSecond);
};
//-----------------------------------------------------------------------------
Epoch.prototype.updatePos = function (target) //PUBLIC: moves the calendar's position to target's location (popup mode only)
{
	this.calendar.style.top = this.getTop(target) + this.topOffset + 'px'
	this.calendar.style.left = this.getLeft(target) + this.leftOffset + 'px'
}
//-----------------------------------------------------------------------------

/*****************************************************************************/
function CalHeading(owner,tableCell,dow)
{
	this.owner = owner;
	this.tableCell = tableCell;
	this.dayOfWeek = dow;
	
	//the event handlers
	this.tableCell.onclick = this.onclick;
}
//-----------------------------------------------------------------------------
CalHeading.prototype.onclick = function ()
{
	//reduce indirection:
	var owner = this.headObj.owner;
	var sdates = owner.selectedDates;
	var cells = owner.cells;
	
	owner.cols[this.headObj.dayOfWeek] = !owner.cols[this.headObj.dayOfWeek];
	for(var i=0;i<cells.length;i++) //cycle through all the cells in the calendar, selecting all cells with the same dayOfWeek as this heading
	{
		if(cells[i].dayOfWeek == this.headObj.dayOfWeek && (!owner.selCurMonthOnly || cells[i].date.getMonth() == owner.displayMonth && cells[i].date.getFullYear() == owner.displayYear)) //if the cell's DoW matches, with other conditions
		{
			if(owner.cols[this.headObj.dayOfWeek]) 		//if selecting, add the cell's date to the selectedDates array
			{
				if(owner.selectedDates.arrayIndex(cells[i].date) == -1) { //if the date isn't already in the array
					sdates.push(cells[i].date);
				}
			}
			else										//otherwise, remove it
			{
				for(var j=0;j<sdates.length;j++) 
				{
					if(cells[i].dayOfWeek == sdates[j].getDay())
					{
						sdates.splice(j,1);	//remove dates that are within the displaying month/year that have the same day of week as the day cell
						break;
					}
				}
			}
			cells[i].selected = owner.cols[this.headObj.dayOfWeek];
		}
	}
	owner.reDraw();
};
/*****************************************************************************/
function WeekHeading(owner,tableCell,week,row)
{
	this.owner = owner;
	this.tableCell = tableCell;
	this.week = week;
	this.tableRow = row;
	this.tableCell.setAttribute('class','wkhead');
	this.tableCell.setAttribute('className','wkhead'); //<iehack>
	//the event handlers
	this.tableCell.onclick = this.onclick;
}
//-----------------------------------------------------------------------------
WeekHeading.prototype.onclick = function ()
{
	//reduce indirection:
	var owner = this.weekObj.owner;
	var cells = owner.cells;
	var sdates = owner.selectedDates;
	var i,j;
	owner.rows[this.weekObj.tableRow] = !owner.rows[this.weekObj.tableRow];
	for(i=0;i<cells.length;i++)
	{
		if(cells[i].tableRow == this.weekObj.tableRow)
		{
			if(owner.rows[this.weekObj.tableRow] && (!owner.selCurMonthOnly || cells[i].date.getMonth() == owner.displayMonth && cells[i].date.getFullYear() == owner.displayYear)) //match all cells in the current row, with option to restrict to current month only
			{
				if(owner.selectedDates.arrayIndex(cells[i].date) == -1) {//if the date isn't already in the array
					sdates.push(cells[i].date);
				}
			}
			else										//otherwise, remove it
			{
				for(j=0;j<sdates.length;j++)
				{
					if(sdates[j].getTime() == cells[i].date.getTime())  //this.weekObj.tableRow && sdates[j].getMonth() == owner.displayMonth && sdates[j].getFullYear() == owner.displayYear)
					{
						sdates.splice(j,1);	//remove dates that are within the displaying month/year that have the same day of week as the day cell
						break;
					}
				}
			}
		}
	}
	owner.reDraw();
};
/*****************************************************************************/
//-----------------------------------------------------------------------------
function CalCell(owner,tableCell,dateObj,row)
{
	this.owner = owner;		//used primarily for event handling
	this.tableCell = tableCell; 			//the link to this cell object's table cell in the DOM
	this.cellClass;			//the CSS class of the cell
	this.selected = false;	//whether the cell is selected (and is therefore stored in the owner's selectedDates array)
	this.date = new Date(dateObj);
	this.dayOfWeek = this.date.getDay();
	this.week = this.date.getWeek();
	this.tableRow = row;
	
	//assign the event handlers for the table cell element
	this.tableCell.onclick = this.onclick;
	this.tableCell.onmouseover = this.onmouseover;
	this.tableCell.onmouseout = this.onmouseout;
	
	//and set the CSS class of the table cell
	this.setClass();
}
//-----------------------------------------------------------------------------
CalCell.prototype.onmouseover = function () //replicate CSS :hover effect for non-supporting browsers <iehack>
{
	this.setAttribute('class',this.cellClass + ' hover');
	this.setAttribute('className',this.cellClass + ' hover');
};
//-----------------------------------------------------------------------------
CalCell.prototype.onmouseout = function () //replicate CSS :hover effect for non-supporting browsers <iehack>
{
	this.cellObj.setClass();
};
//-----------------------------------------------------------------------------
CalCell.prototype.onclick = function () 
{
	//reduce indirection:
	var cell = this.cellObj;
	var owner = cell.owner;
	if(!owner.selCurMonthOnly || cell.date.getMonth() == owner.displayMonth && cell.date.getFullYear() == owner.displayYear)
	{
		if(owner.selectMultiple == true)  //if we can select multiple cells simultaneously, add the currently selected cell's date to the selectedDates array
		{
			if(!cell.selected) //if this cell has been selected
			{
				if(owner.selectedDates.arrayIndex(cell.date) == -1) {
					owner.selectedDates.push(cell.date);
				}
			}
			else		
			{
				var tmp = owner.selectedDates; // to reduce indirection
				//if the cell has been deselected, remove it from the owner calendar's selectedDates array
				for(var i=0;i<tmp.length;i++)
				{
					if(tmp[i].getUeDay() == cell.date.getUeDay()) {
						tmp.splice(i,1);
					}
				}
			}
		}
		else //if we can only select one cell at a time
		{
			owner.selectedDates = new Array(cell.date);
			if(owner.tgt) //if there is a target element to place the value in, do so
			{
				owner.tgt.value = owner.selectedDates[0].dateFormat();
				if(owner.mode == 'popup') {
					owner.hide();
				}
			}
		}
		owner.reDraw(); //redraw the calendar cell styles to reflect the changes
	}
};
//-----------------------------------------------------------------------------
CalCell.prototype.setClass = function ()  //private: sets the CSS class of the cell based on the specified criteria
{
	if(this.selected) {
		this.cellClass = 'cell_selected';
	}
	else if(this.owner.displayMonth != this.date.getMonth() ) {
		this.cellClass = 'notmnth';	
	}
	else if(this.date.getDay() > 0 && this.date.getDay() < 6) {
		this.cellClass = 'wkday';
	}
	else {
		this.cellClass = 'wkend';
	}
	
	if(this.date.getFullYear() == this.owner.curDate.getFullYear() && this.date.getMonth() == this.owner.curDate.getMonth() && this.date.getDate() == this.owner.curDate.getDate()) {
		this.cellClass = this.cellClass + ' curdate';
	}

	this.tableCell.setAttribute('class',this.cellClass);
	this.tableCell.setAttribute('className',this.cellClass); //<iehack>
};
/*****************************************************************************/
Date.prototype.getDayOfYear = function () //returns the day of the year for this date
{
	return parseInt((this.getTime() - new Date(this.getFullYear(),0,1).getTime())/86400000 + 1);
};
//-----------------------------------------------------------------------------
Date.prototype.getWeek = function () //returns the day of the year for this date
{
	return parseInt((this.getTime() - new Date(this.getFullYear(),0,1).getTime())/604800000 + 1);
};
/*function getISOWeek()
{
	var newYear = new Date(this.getFullYear(),0,1);
	var modDay = newYear.getDay();
	if (modDay == 0) modDay=6; else modDay--;
	
	var daynum = ((Date.UTC(this.getFullYear(),this.getMonth(),this.getDate(),0,0,0) - Date.UTC(this.getFullYear()),0,1,0,0,0)) /1000/60/60/24) + 1;
	
	if (modDay < 4 ) {
	    var weeknum = Math.floor((daynum+modDay-1)/7)+1;
	}
	else {
	    var weeknum = Math.floor((daynum+modDay-1)/7);
	    if (weeknum == 0) {
	        year--;
	        var prevNewYear = new Date(this.getFullYear(),0,1);
	        var prevmodDay = prevNewYear.getDay();
	        if (prevmodDay == 0) prevmodDay = 6; else prevmodDay--;
	        if (prevmodDay < 4) weeknum = 53; else weeknum = 52;
	    }
	}
	
	return + weeknum;
}*/
//-----------------------------------------------------------------------------
Date.prototype.getUeDay = function () //returns the number of DAYS since the UNIX Epoch - good for comparing the date portion
{
	return parseInt(Math.floor((this.getTime() - this.getTimezoneOffset() * 60000)/86400000)); //must take into account the local timezone
};
//-----------------------------------------------------------------------------
Date.prototype.dateFormat = function(format)
{
	if(!format) { // the default date format to use - can be customized to the current locale
		format = 'm/d/Y';
	}
	LZ = function(x) {return(x < 0 || x > 9 ? '' : '0') + x};
	var MONTH_NAMES = new Array('January','February','March','April','May','June','July','August','September','October','November','December','Jan','Feb','Mar','Apr','May','Jun','Jul','Aug','Sep','Oct','Nov','Dec');
	var DAY_NAMES = new Array('Sunday','Monday','Tuesday','Wednesday','Thursday','Friday','Saturday','Sun','Mon','Tue','Wed','Thu','Fri','Sat');
	format = format + "";
	var result="";
	var i_format=0;
	var c="";
	var token="";
	var y=this.getFullYear().toString();
	var M=this.getMonth()+1;
	var d=this.getDate();
	var E=this.getDay();
	var H=this.getHours();
	var m=this.getMinutes();
	var s=this.getSeconds();
	var yyyy,yy,MMM,MM,dd,hh,h,mm,ss,ampm,HH,H,KK,K,kk,k;
	// Convert real this parts into formatted versions
	var value = new Object();
	//if (y.length < 4) {y=''+(y-0+1900);}
	value['Y'] = y.toString();
	value['y'] = y.substring(2);
	value['n'] = M;
	value['m'] = LZ(M);
	value['F'] = MONTH_NAMES[M-1];
	value['M'] = MONTH_NAMES[M+11];
	value['j'] = d;
	value['d'] = LZ(d);
	value['D'] = DAY_NAMES[E+7];
	value['l'] = DAY_NAMES[E];
	value['G'] = H;
	value['H'] = LZ(H);
	if (H==0) {value['g']=12;}
	else if (H>12){value['g']=H-12;}
	else {value['g']=H;}
	value['h']=LZ(value['g']);
	if (H > 11) {value['a']='pm'; value['A'] = 'PM';}
	else { value['a']='am'; value['A'] = 'AM';}
	value['i']=LZ(m);
	value['s']=LZ(s);
	//construct the result string
	while (i_format < format.length) {
		c=format.charAt(i_format);
		token="";
		while ((format.charAt(i_format)==c) && (i_format < format.length)) {
			token += format.charAt(i_format++);
			}
		if (value[token] != null) { result=result + value[token]; }
		else { result=result + token; }
		}
	return result;
};
/*****************************************************************************/
Array.prototype.arrayIndex = function(searchVal,startIndex) //similar to array.indexOf() - created to fix IE deficiencies
{
	startIndex = (startIndex != null ? startIndex : 0); //default startIndex to 0, if not set
	for(var i=startIndex;i<this.length;i++)
	{
		if(searchVal == this[i]) {
			return i;
		}
	}
	return -1;
};
/*****************************************************************************/