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
    
    if (typeof(RGraph) == 'undefined') RGraph = {};

    /**
    * The gantt chart constructor
    * 
    * @param object canvas The cxanvas object
    * @param array  data   The chart data
    */
    RGraph.Gantt = function (id)
    {
        // Get the canvas and context objects
        this.id      = id;
        this.canvas  = document.getElementById(id);
        this.context = this.canvas.getContext("2d");
        this.canvas.__object__ = this;
        this.type              = 'gantt';
        this.isRGraph          = true;


        /**
        * Compatibility with older browsers
        */
        RGraph.OldBrowserCompat(this.context);

        
        // Set some defaults
        this.properties = {
            'chart.background.barcolor1':   'white',
            'chart.background.barcolor2':   'white',
            'chart.background.grid':        true,
            'chart.background.grid.width':  1,
            'chart.background.grid.color':  '#ddd',
            'chart.background.grid.hsize':  20,
            'chart.background.grid.vsize':  20,
            'chart.background.grid.hlines': true,
            'chart.background.grid.vlines': true,
            'chart.background.grid.border': true,
            'chart.background.grid.autofit':false,
            'chart.background.grid.autofit.numhlines': 7,
            'chart.background.grid.autofit.numvlines': 20,
            'chart.background.vbars':       [],
            'chart.text.size':              10,
            'chart.text.font':              'Verdana',
            'chart.text.color':             'black',
            'chart.gutter':                 25,
            'chart.labels':                 [],
            'chart.margin':                 2,
            'chart.title':                  '',
            'chart.title.hpos':             null,
            'chart.title.vpos':             null,
            'chart.events':                 [],
            'chart.borders':                true,
            'chart.defaultcolor':           'white',
            'chart.coords':                 [],
            'chart.tooltips':               [],
            'chart.tooltips.effect':         'fade',
            'chart.tooltips.css.class':      'RGraph_tooltip',
            'chart.xmax':                   0,
            'chart.contextmenu':            null,
            'chart.annotatable':            false,
            'chart.annotate.color':         'black',
            'chart.zoom.factor':            1.5,
            'chart.zoom.fade.in':           true,
            'chart.zoom.fade.out':          true,
            'chart.zoom.hdir':              'right',
            'chart.zoom.vdir':              'down',
            'chart.zoom.frames':            10,
            'chart.zoom.delay':             50,
            'chart.zoom.shadow':            true,
            'chart.zoom.mode':              'canvas',
            'chart.zoom.thumbnail.width':   75,
            'chart.zoom.thumbnail.height':  75,
            'chart.zoom.background':        true,
            'chart.zoom.action':            'zoom',
            'chart.resizable':              false
        }

        // Check the common library has been included
        if (typeof(RGraph) == 'undefined') {
            alert('[GANTT] Fatal error: The common library does not appear to have been included');
        }
    }


    /**
    * A peudo setter
    * 
    * @param name  string The name of the property to set
    * @param value mixed  The value of the property
    */
    RGraph.Gantt.prototype.Set = function (name, value)
    {
        this.properties[name] = value;
    }


    /**
    * A peudo getter
    * 
    * @param name  string The name of the property to get
    */
    RGraph.Gantt.prototype.Get = function (name)
    {
        return this.properties[name];
    }

    
    /**
    * Draws the chart
    */
    RGraph.Gantt.prototype.Draw = function ()
    {
        /**
        * Fire the onbeforedraw event
        */
        RGraph.FireCustomEvent(this, 'onbeforedraw');

        var gutter = this.Get('chart.gutter');

        /**
        * Work out the graphArea
        */
        this.graphArea     = this.canvas.width - (2 * gutter);
        this.graphHeight   = this.canvas.height - (2 * gutter);
        this.numEvents     = this.Get('chart.events').length
        this.barHeight     = this.graphHeight / this.numEvents;
        this.halfBarHeight = this.barHeight / 2;

        /**
        * Draw the background
        */
        RGraph.background.Draw(this);
        
        /**
        * Draw a space for the left hand labels
        */
        this.context.beginPath();
        this.context.lineWidth   = 1;
        this.context.strokeStyle = this.Get('chart.background.grid.color');
        this.context.fillStyle   = 'white';
        this.context.fillRect(0,gutter - 5,gutter * 3, this.canvas.height - (2 * gutter) + 10);
        this.context.moveTo(gutter * 3, gutter);
        this.context.lineTo(gutter * 3, this.canvas.height - gutter);
        
        this.context.stroke();
        this.context.fill();
        
        /**
        * Draw the labels at the top
        */
        this.DrawLabels();
        
        /**
        * Draw the events
        */
        this.DrawEvents();
        
        
        /**
        * Setup the context menu if required
        */
        if (this.Get('chart.contextmenu')) {
            RGraph.ShowContext(this);
        }
        
        /**
        * If the canvas is annotatable, do install the event handlers
        */
        if (this.Get('chart.annotatable')) {
            RGraph.Annotate(this);
        }
        
        /**
        * This bit shows the mini zoom window if requested
        */
        if (this.Get('chart.zoom.mode') == 'thumbnail' || this.Get('chart.zoom.mode') == 'area') {
            RGraph.ShowZoomWindow(this);
        }

        
        /**
        * This function enables resizing
        */
        if (this.Get('chart.resizable')) {
            RGraph.AllowResizing(this);
        }
        
        /**
        * Fire the RGraph ondraw event
        */
        RGraph.FireCustomEvent(this, 'ondraw');
    }

    
    /**
    * Draws the labels at the top and the left of the chart
    */
    RGraph.Gantt.prototype.DrawLabels = function ()
    {
        var gutter = this.Get('chart.gutter');

        this.context.beginPath();
        this.context.fillStyle = this.Get('chart.text.color');

        /**
        * Draw the X labels at the top of the chart.
        */
        var labelSpace = (this.graphArea - (2 * gutter)) / this.Get('chart.labels').length;
        var xPos       = (3 * gutter) + (labelSpace / 2);
        this.context.strokeStyle = 'black'

        for (i=0; i<this.Get('chart.labels').length; ++i) {
            RGraph.Text(this.context, this.Get('chart.text.font'), this.Get('chart.text.size'), xPos + (i * labelSpace), gutter * (3/4), String(this.Get('chart.labels')[i]), 'center', 'center');
        }
        
        // Draw the vertical labels
        for (i=0; i<this.Get('chart.events').length; ++i) {
            var ev = this.Get('chart.events')[i];
            var x  = (3 * gutter);
            var y  = gutter + this.halfBarHeight + (i * this.barHeight);

            RGraph.Text(this.context, this.Get('chart.text.font'), this.Get('chart.text.size'), x - 5, y, String(ev[3]), 'center', 'right');
        }
    }
    
    /**
    * Draws the events to the canvas
    */
    RGraph.Gantt.prototype.DrawEvents = function ()
    {
        var canvas  = this.canvas;
        var context = this.context;
        var gutter  = this.Get('chart.gutter');
        var events  = this.Get('chart.events');

        /**
        * Reset the coords array to prevent it growing
        */
        this.coords = [];

        /**
        * First draw the vertical bars that have been added
        */if (this.Get('chart.vbars')) {
            for (i=0; i<this.Get('chart.vbars').length; ++i) {
                // Boundary checking
                if (this.Get('chart.vbars')[i][0] + this.Get('chart.vbars')[i][1] > this.Get('chart.xmax')) {
                    this.Get('chart.vbars')[i][1] = 364 - this.Get('chart.vbars')[i][0];
                }
    
                var barX   = (3 * gutter) + (this.Get('chart.vbars')[i][0] / this.Get('chart.xmax')) * (this.graphArea - (2 * gutter) );
                var barY   = gutter;
                var width  = ( (this.graphArea - (2 * gutter)) / this.Get('chart.xmax')) * this.Get('chart.vbars')[i][1];
                var height = canvas.height - (2 * gutter);
                
                // Right hand bounds checking
                if ( (barX + width) > (this.canvas.width - gutter) ) {
                    width = this.canvas.width - gutter - barX;
                }
    
                context.fillStyle = this.Get('chart.vbars')[i][2];
                context.fillRect(barX, barY, width, height);
            }
        }

        for (i=0; i<events.length; ++i) {
            
            var ev = events[i];

            context.beginPath();
            context.strokeStyle = 'black';
            context.fillStyle = ev[4] ? ev[4] : this.Get('chart.defaultcolor');

            var barStartX  = (3 * gutter) + (ev[0] / this.Get('chart.xmax')) * (this.graphArea - (2 * gutter) );
            //barStartX += this.margin;
            var barStartY  = gutter + (i * this.barHeight);
            var barWidth   = (ev[1] / this.Get('chart.xmax')) * (this.graphArea - (2 * gutter));

            /**
            * If the width is greater than the graph atrea, curtail it
            */
            if ( (barStartX + barWidth) > (canvas.width - gutter) ) {
                barWidth = canvas.width - gutter - barStartX;
            }

            /**
            *  Draw the actual bar storing store the coordinates
            */
            this.coords.push([barStartX, barStartY + this.Get('chart.margin'), barWidth, this.barHeight - (2 * this.Get('chart.margin'))]);
            context.fillRect(barStartX, barStartY + this.Get('chart.margin'), barWidth, this.barHeight - (2 * this.Get('chart.margin')) );

            // Work out the completeage indicator
            var complete = (ev[2] / 100) * barWidth;

            // Draw the % complete indicator. If it's greater than 0
            if (typeof(ev[2]) == 'number') {
                context.beginPath();
                context.fillStyle = ev[5] ? ev[5] : '#0c0';
                context.fillRect(barStartX,
                                      barStartY + this.Get('chart.margin'),
                                      (ev[2] / 100) * barWidth,
                                      this.barHeight - (2 * this.Get('chart.margin')) );
                
                context.beginPath();
                context.fillStyle = this.Get('chart.text.color');
                RGraph.Text(context, this.Get('chart.text.font'), this.Get('chart.text.size'), barStartX + barWidth + 5, barStartY + this.halfBarHeight, String(ev[2]) + '%', 'center');
            }

            // Redraw the border around the bar
            if (this.Get('chart.borders')) {
                context.strokeStyle = 'black';
                context.beginPath();
                context.strokeRect(barStartX, barStartY + this.Get('chart.margin'), barWidth, this.barHeight - (2 * this.Get('chart.margin')) );
            }
        }


        /**
        * If tooltips are defined, handle them
        */
        if (this.Get('chart.tooltips')) {

            // Register the object for redrawing
            RGraph.Register(this);

            /**
            * If the cursor is over a hotspot, change the cursor to a hand
            */
            this.canvas.onmousemove = function (eventObj)
            {
                eventObj = RGraph.FixEventObject(eventObj);
                var canvas = eventObj.target;
                var obj    = canvas.__object__;
                var len    = obj.coords.length;

                /**
                * Get the mouse X/Y coordinates
                */
                var mouseCoords = RGraph.getMouseXY(eventObj);

                /**
                * Loop through the bars determining if the mouse is over a bar
                */
                for (var i=0; i<len; i++) {

                    var mouseX = mouseCoords[0];  // In relation to the canvas
                    var mouseY = mouseCoords[1];  // In relation to the canvas
                    var left   = obj.coords[i][0];
                    var top    = obj.coords[i][1];
                    var width  = obj.coords[i][2];
                    var height = obj.coords[i][3];

                    if (   mouseX >= left
                        && mouseX <= (left + width)
                        && mouseY >= top
                        && mouseY <= (top + height)
                        && (typeof(obj.Get('chart.tooltips')) == 'function' || obj.Get('chart.tooltips')[i]) ) {

                        canvas.style.cursor = 'pointer';
                        return;
                    }
                }

                canvas.style.cursor = 'default';
            }


            this.canvas.onclick = function (eventObj)
            {
                eventObj = RGraph.FixEventObject(eventObj);

                var canvas  = eventObj.target;
                var context = canvas.getContext('2d');
                var obj     = canvas.__object__;

                var mouseCoords = RGraph.getMouseXY(eventObj);
                var mouseX      = mouseCoords[0];
                var mouseY      = mouseCoords[1];
                
                
                for (i=0; i<obj.coords.length; ++i) {
                    
                    var idx = i;
                    var xCoord = obj.coords[i][0];
                    var yCoord = obj.coords[i][1];
                    var width  = obj.coords[i][2];
                    var height = obj.coords[i][3];

                    if (
                           mouseX >= xCoord
                        && (mouseX <= xCoord + width)
                        && mouseY >= yCoord
                        && (mouseY <= yCoord + height)
                        && obj.Get('chart.tooltips')
                       ) {

                       // Redraw the graph
                        RGraph.Redraw();

                        /**
                        * Get the tooltip text
                        */
                        if (typeof(obj.Get('chart.tooltips')) == 'function') {
                            var text = obj.Get('chart.tooltips')(idx);
                        
                        } else if (typeof(obj.Get('chart.tooltips')) == 'object' && typeof(obj.Get('chart.tooltips')[idx]) == 'function') {
                            var text = obj.Get('chart.tooltips')[idx](idx);
                        
                        } else if (typeof(obj.Get('chart.tooltips')) == 'object') {
                            var text = obj.Get('chart.tooltips')[idx];

                        } else {
                            var text = null;
                        }

                        if (String(text).length && text != null) {

                            // SHOW THE CORRECT TOOLTIP
                            RGraph.Tooltip(canvas, text, eventObj.pageX, eventObj.pageY, idx);
                            
                            /**
                            * Draw a rectangle around the correct bar, in effect highlighting it
                            */
                            context.strokeStyle = 'black';
                            context.fillStyle = 'rgba(255,255,255,0.8)';
                            context.strokeRect(xCoord, yCoord, width, height);
                            context.fillRect(xCoord + 1, yCoord + 1, width - 2, height - 2);
    
                            eventObj.stopPropagation();
                        }
                        return;
                    }
                }
            }
        }
    }