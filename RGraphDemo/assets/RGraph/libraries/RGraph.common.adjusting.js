    /**
    * o------------------------------------------------------------------------------o
    * | This file is part of the RGraph package - you can learn more at:             |
    * |                                                                              |
    * |                          http://www.rgraph.net                               |
    * |                                                                              |
    * | This package is licensed under the RGraph license. For all kinds of business |
    * | purposes there is a small one-time licensing fee to pay and for non          |
    * | commercial  purposes it is free to use. You can read the full license here:  |
    * |                                                                              |
    * |                      http://www.rgraph.net/LICENSE.txt                       |
    * o------------------------------------------------------------------------------o
    */

    if (typeof(RGraph) == 'undefined') RGraph = {isRGraph:true,type:'common'};

    RGraph.AllowAdjusting = function (obj)
    {
        var canvas  = obj.canvas;
        var context = obj.context;
        
        RGraph.Register(obj);
            
        if (obj.type == 'line') {
            canvas.onmousedown = function (e)
            {
                e = RGraph.FixEventObject(e);
    
                var obj         = e.target.__object__;
                var id          = obj.id;
                var canvas      = obj.canvas;
                var context     = obj.context;
                var coords      = obj.coords;
                var mouseCoords = RGraph.getMouseXY(e);
    
                RGraph.Redraw();
    
                for (var i=0; i<coords.length; ++i) {
    
                    if (   mouseCoords[0] > coords[i][0] - 5
                        && mouseCoords[1] > coords[i][1] - 5
                        && mouseCoords[0] < coords[i][0] + 5
                        && mouseCoords[1] < coords[i][1] + 5
                       ) {

                        var numDataSeries = obj.original_data.length;
                        var numDataPoints = obj.original_data[0].length;
                        var data_series   = i / numDataPoints;
                            data_series = Math.floor(data_series);
    
    
    
                      canvas.style.cursor = 'ns-resize';
                      RGraph.Registry.Set('chart.adjusting.line.' + id, [obj, i, [coords[i][0], coords[i][1]], data_series]);
    
                      return;
                    }
                }
            }
    
    
            canvas.onmousemove = function (e)
            {
                e = RGraph.FixEventObject(e);
                var id = e.target.__object__.id;
    
                var state = RGraph.Registry.Get('chart.adjusting.line.' + id);
    
                if (state) {
                    var obj         = state[0];
                    var idx         = state[1];
                    var canvas      = obj.canvas;
                    var context     = obj.context;
                    var data_series = state[3];
                    var points      = obj.original_data[data_series];
                    var mouseCoords = RGraph.getMouseXY(e);
                    var x           = mouseCoords[0];
                    var y           = mouseCoords[1];
    
                    if (y >= (obj.canvas.height - obj.Get('chart.gutter'))) {
                        y = obj.canvas.height - obj.Get('chart.gutter');
                    } else if (y <= obj.Get('chart.gutter')) {
                        y = obj.Get('chart.gutter');
                    }
    
                    var pos   = obj.canvas.height - (2 * obj.Get('chart.gutter'));
                        pos   = pos - (y - obj.Get('chart.gutter'));
                    var value = (obj.max / (obj.canvas.height - (2 * obj.Get('chart.gutter')))) * pos;
    
                    // Adjust the index so that it's applicable to the correct data series
                    for (var i=0; i<data_series; ++i) {
                        idx -= obj.original_data[0].length;
                    }
    
                    obj.original_data[data_series][idx] = value;
    
                    obj.Set('chart.ymax', obj.max);
                    canvas.style.cursor = 'ns-resize';
                    RGraph.Redraw();

                    /**
                    * Fire the onadjust event
                    */
                    RGraph.FireCustomEvent(obj, 'onadjust');
    
                    return;
    
                } else {
                    
                    var canvas  = e.target;
                    var context = canvas.__object__.context;
                    var obj     = canvas.__object__;
                    var mouseCoords = RGraph.getMouseXY(e);
                    var x       = mouseCoords[0];
                    var y       = mouseCoords[1];
    
                    for (var i=0; i<obj.coords.length; ++i) {
    
                        if (   x > obj.coords[i][0] - 5
                            && y > obj.coords[i][1] - 5
                            && x < obj.coords[i][0] + 5
                            && y < obj.coords[i][1] + 5
                           ) {
    
                           canvas.style.cursor = 'ns-resize';
                           return;
                        }
                    }
                }
                
                e.target.style.cursor = null;
            }
    
    
            canvas.onmouseup = function (e)
            {
                var id = e.target.__object__.id;

                RGraph.Registry.Set('chart.adjusting.line.' + id, null);
                e.target.style.cursor = null;
            }
    
    
            canvas.onmouseout = function (e)
            {
                canvas.onmouseup(e);
            }
        
        /**
        * Progress bar
        */
        } else if (obj.type == 'hprogress') {

            
            canvas.onmousedown = function (e)
            {
                var id = e.target.__object__.id;

                RGraph.Registry.Set('chart.adjusting.progress.' + id, [true]);
                
                canvas.onmousemove(e);
            }


            canvas.onmousemove = function (e)
            {
                var id    = e.target.__object__.id;
                var state = RGraph.Registry.Get('chart.adjusting.progress.' + id);

                if (state && state.length) {
                    var obj     = e.target.__object__;
                    var canvas  = obj.canvas;
                    var context = obj.context;
                    
                    if (obj.type == 'hprogress') {
                    
                        var coords = RGraph.getMouseXY(e);
                            coords[0] = Math.max(0, coords[0] - obj.Get('chart.gutter'));
                        var barWidth  = canvas.width - (2 * obj.Get('chart.gutter'));
                        
                        // Work out the new value
                        var value = (coords[0] / barWidth) * obj.max;
                        
                        obj.value = Math.max(0, value.toFixed());
                        RGraph.Clear(obj.canvas);
                        obj.Draw();

                    } else if (obj.type == 'vprogress') {

                        var coords = RGraph.getMouseXY(e);
                            coords[1] = Math.max(0, coords[1] - obj.Get('chart.gutter'));
                        var barHeight = canvas.height - (2 * obj.Get('chart.gutter'));
                        
                        // Work out the new value
                        var value = ( (barHeight - coords[1]) / barHeight) * obj.max;
                        
                        obj.value = Math.max(0, value.toFixed());
                        RGraph.Clear(obj.canvas);
                        obj.Draw();
                    }

                    /**
                    * Fire the onadjust event
                    */
                    RGraph.FireCustomEvent(obj, 'onadjust');
                }
            }
            
            
            canvas.onmouseup = function (e)
            {
                var id = e.target.__object__.id;
                RGraph.Registry.Set('chart.adjusting.progress.' + id, null);
            }
    
    
            canvas.onmouseout = function (e)
            {
                canvas.onmouseup(e);
            }
        
        /**
        * Rose chart
        */
        } else if (obj.type == 'rose') {


            obj.Set('chart.ymax', obj.max);


            canvas.onmousemove = function (e)
            {
                var obj     = e.target.__object__;
                var id      = obj.id;
                var canvas  = obj.canvas;
                var context = obj.context;
                var coords  = RGraph.getMouseXY(e);
                var segment = RGraph.Registry.Get('chart.adjusting.rose.' + id);
                var x       = Math.abs(coords[0] - obj.centerx);
                var y       = Math.abs(coords[1] - obj.centery);
                var theta   = Math.atan(y / x) * (180 / Math.PI); // theta is now in DEGREES


                // Account for the correct quadrant
                if (coords[0] >= obj.centerx && coords[1] < obj.centery) {
                    theta = 90 - theta;
                } else if (coords[0] >= obj.centerx && coords[1] >= obj.centery) {
                    theta += 90;
                } else if (coords[0] < obj.centerx && coords[1] >= obj.centery) {
                    theta = 90 - theta;
                    theta = 180 + theta;
                    
                } else if (coords[0] < obj.centerx && coords[1] < obj.centery) {
                    theta = theta + 270;
                }

                var Opp = y;
                var Adj = x;
                var Hyp = Math.abs(Adj / Math.sin(theta / (180 / Math.PI)));

                for (var i=0; i<obj.angles.length; ++i) {
                    if (
                           theta > obj.angles[i][0]
                        && theta < obj.angles[i][1] ) {

                        if (RGraph.Registry.Get('chart.adjusting.rose.' + id) && i == segment[5]) {
                            var newvalue  = (Hyp / (obj.radius - 25) ) * obj.max;
                            obj.data[i]   = Math.min(newvalue, obj.max);

                            RGraph.Clear(obj.canvas);
                            obj.Draw();

                            /**
                            * Fire the onadjust event
                            */
                            RGraph.FireCustomEvent(obj, 'onadjust');
                        }
                        
                        if (Hyp <= (obj.angles[i][2] + 5) && Hyp >= (obj.angles[i][2] - 5) ) {
                            canvas.style.cursor = 'move';
                            return;
                        
                        } else if (obj.Get('chart.tooltips') && Hyp <= (obj.angles[i][2] - 5) ) {
                            canvas.style.cursor = 'pointer';
                            return;
                        }

                    }
                }

                canvas.style.cursor = 'default';
            }


            canvas.onmousedown = function (e)
            {
                var obj     = e.target.__object__;
                var id      = obj.id;
                var canvas  = obj.canvas;
                var context = obj.context;
                var coords  = RGraph.getMouseXY(e);
                var segment = RGraph.getSegment(e, 5);

                if (segment && segment.length && !RGraph.Registry.Get('chart.adjusting.rose.' + id)) {
                    var x = Math.abs(coords[0] - obj.centerx);
                    var y = Math.abs(coords[1] - obj.centery);

                    var a = Math.atan(y / x) * (180 / Math.PI); // a is now in DEGREES

                    // Account for the correct quadrant
                    if (coords[0] >= obj.centerx && coords[1] < obj.centery) {
                        a  = 90 - a;
                        a += 270;
                    } else if (coords[0] >= obj.centerx && coords[1] >= obj.centery) {
                        // Nada
                    } else if (coords[0] < obj.centerx && coords[1] >= obj.centery) {
                         a  = 90 - a;
                         a += 90;
                    } else if (coords[0] < obj.centerx && coords[1] < obj.centery) {
                        a += 180;
                    }

                    var hyp = Math.abs(y / Math.sin(a / 57.3));

                    if (hyp >= (segment[2] - 10) ) {

                        /**
                        * Hide any currently shown tooltip
                        */
                        if (RGraph.Registry.Get('chart.tooltip')) {
                            RGraph.Registry.Get('chart.tooltip').style.display = 'none';
                            RGraph.Registry.Set('chart.tooltip', null);
                        }
                        
                        RGraph.Registry.Set('chart.adjusting.rose.' + id, segment);
                        
                        e.stopPropagation();
                    }
                }
            }


            canvas.onmouseup = function (e)
            {
                var obj = e.target.__object__;
                var id  = obj.id;

                if (RGraph.Registry.Get('chart.adjusting.rose.' + id)) {

                    RGraph.Registry.Set('chart.adjusting.rose.' + id, null);
                    e.stopPropagation();
                    
                    return false;
                }
            }
    
    
            canvas.onmouseout = function (e)
            {
                canvas.onmouseup(e);
            }

        /**
        * Bar chart
        */
        } else if (obj.type == 'bar') {
        
            // Stacked bar charts not supported
            if (obj.Get('chart.grouping') == 'stacked') {
                alert('[BAR] Adjusting stacked bar charts is not supported');
                return;
            }


            var canvas  = obj.canvas;
            var context = obj.context;


            canvas.onmousemove = function (e)
            {
                var obj     = e.target.__object__;
                var id      = obj.id;
                var canvas  = obj.canvas;
                var context = obj.context;
                var mouse   = RGraph.getMouseXY(e);
                var mousex  = mouse[0];
                var mousey  = mouse[1]; // mousey, mousey...

                // Loop through the coords to see if the mouse position is at the top of a bar
                for (var i=0; i<obj.coords.length; ++i) {
                    if (mousex > obj.coords[i][0] && mousex < (obj.coords[i][0] + obj.coords[i][2])) {
                        
                        // Change the mouse pointer
                        if (mousey > (obj.coords[i][1] - 5) && mousey < (obj.coords[i][1] + 5)) {
                            canvas.style.cursor = 'ns-resize';
                        } else {
                            canvas.style.cursor = 'default';
                        }

                        var idx = RGraph.Registry.Get('chart.adjusting.bar.' + id)
                        
                        if (typeof(idx) == 'number') {
                            var newheight = obj.grapharea - (mousey - obj.Get('chart.gutter'));
                            var newvalue  = (newheight / obj.grapharea) * obj.max;
                            
                            // Top and bottom boundaries
                            if (newvalue > obj.max) newvalue = obj.max;
                            if (newvalue < 0)       newvalue = 0;

                            ///////////////// This was fun to work out... /////////////////
                            for (var j=0, index=0; j<obj.data.length; ++j,++index) {
                                if (typeof(obj.data[j]) == 'object') {
                                    for (var k=0; k<obj.data[j].length && index <= idx; ++k, ++index) {
                                        if (index == idx) {
                                            obj.data[j][k] = newvalue;
                                            var b = true;
                                            break;
                                        }
                                    }
                                    
                                    --index;
                                } else if (typeof(obj.data[j]) == 'number') {
                            
                                    if (index == idx) {
                                        obj.data[j] = newvalue;
                                        // No need to set b
                                        break;
                                    }
                                }
                                
                                if (b) {
                                    break;
                                }
                            }
                            ///////////////////////////////////////////////////////////////

                            RGraph.Clear(canvas);
                            obj.Draw();

                            /**
                            * Fire the onadjust event
                            */
                            RGraph.FireCustomEvent(obj, 'onadjust');
                        }

                        return;
                    }
                }
                
                canvas.style.cursor = 'default';
            }



            canvas.onmousedown = function (e)
            {
                var obj     = e.target.__object__;
                var id      = obj.id;
                var canvas  = obj.canvas;
                var context = obj.context;
                var mouse   = RGraph.getMouseXY(e);
                var mousex  = mouse[0];
                var mousey  = mouse[1]; // mousey, mousey...

                // Loop through the coords to see if the mouse position is at the top of a bar
                for (var i=0; i<obj.coords.length; ++i) {
                    if (
                           mousex > obj.coords[i][0] && mousex < (obj.coords[i][0] + obj.coords[i][2])
                        
                       ) {

                        obj.Set('chart.ymax', obj.max);
                        RGraph.Registry.Set('chart.adjusting.bar.' + id, i);
                        canvas.onmousemove(e);
                    }
                }
            }



            canvas.onmouseup = function (e)
            {
                var id = e.target.__object__.id;
                
                RGraph.Registry.Set('chart.adjusting.bar.' + id, null);
            }


            canvas.onmouseout = function (e)
            {
                canvas.onmouseup(e);
            }


        /**
        * The Tradar chart
        */
        } else if (obj.type == 'tradar') {


            var canvas = obj.canvas;
            var context = obj.context;
            
            
            canvas.onmousemove = function (e)
            {
                var obj         = e.target.__object__;
                var id          = obj.id;
                var canvas      = obj.canvas;
                var context     = obj.context;
                var mouseDown   = RGraph.Registry.Get('chart.adjusting.tradar.' + id);
                var mouseCoords = RGraph.getMouseXY(e);


                if (mouseDown) {

                    canvas.style.cursor = 'pointer';

                    var dx  = mouseCoords[0] - obj.centerx;
                    var dy  = mouseCoords[1] - obj.centery;
                    var hyp = Math.sqrt((dx * dx) + (dy * dy));

                    var newvalue = (hyp / (obj.size / 2)) * obj.max;
                    
                    newvalue = Math.min(obj.max, newvalue);
                    newvalue = Math.max(0, newvalue);

                    /**
                    * Only redraw the graph if the mouse is in the same quadrant as the point
                    */
                    if ( (dx >= 0 ? true : false) == mouseDown[1] && (dy >= 0 ? true : false) == mouseDown[2]) {
                        obj.data[mouseDown[0]] = newvalue;
                        RGraph.Clear(canvas);
                        obj.Draw();

                        /**
                        * Fire the onadjust event
                        */
                        RGraph.FireCustomEvent(obj, 'onadjust');
                    }


                } else {

                    // Determine if the mouse is near a point, and if so, change the pointer
                    for (var i=0; i<obj.coords.length; ++i) {
                        
                        var dx = Math.abs(mouseCoords[0] - obj.coords[i][0]);
                        var dy = Math.abs(mouseCoords[1] - obj.coords[i][1]);
                        var a  = Math.atan(dy / dx);
    
                        
                        var hyp = Math.sqrt((dx * dx) + (dy * dy));
    
                        if (hyp <= 5) {
                            canvas.style.cursor = 'pointer';
                            return;
                        }
                    }

                    canvas.style.cursor = 'default';
                }
            }
            
            
            canvas.onmousedown = function (e)
            {
                e = RGraph.FixEventObject(e);
                
                var obj         = e.target.__object__;
                var id          = obj.id;
                var canvas      = obj.canvas;
                var context     = obj.context;
                var mouseCoords = RGraph.getMouseXY(e);


                // Determine if the mouse is near a point
                for (var i=0; i<obj.coords.length; ++i) {
                    
                    var dx = Math.abs(mouseCoords[0] - obj.coords[i][0]);
                    var dy = Math.abs(mouseCoords[1] - obj.coords[i][1]);
                    var a  = Math.atan(dy / dx);

                    
                    var hyp = Math.sqrt((dx * dx) + (dy * dy));

                    if (hyp <= 5) {
                        canvas.style.cursor = 'pointer';
                        RGraph.Registry.Set('chart.adjusting.tradar.' + id, [i, obj.coords[i][0] > obj.centerx, obj.coords[i][1] > obj.centery]);
                        return;
                    }
                }
                    
                canvas.style.cursor = 'default';
            }


            canvas.onmouseup = function (e)
            {
                RGraph.Registry.Set('chart.adjusting.tradar.' + e.target.id, null);
                canvas.style.cursor = 'default';
            }
    
    
            canvas.onmouseout = function (e)
            {
                canvas.onmouseup(e);
            }
        }
    }


    /**
    * Returns 1 or -1 depening on whether the given number is positive or negative.
    * Zero is considered positive.
    * 
    * @param  int num The number
    * @return int     1 if the number is positive or zero, -1 if it's negative
    */
    //RGraph.getSign = function (num)
    //{
    //    return num >= 0 ? 1 : -1;
    //}