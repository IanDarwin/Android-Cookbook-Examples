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
    * The horizontal bar chart constructor. The horizontal bar is a minor variant
    * on the bar chart. If you have big labels, this may be useful as there is usually
    * more space available for them.
    * 
    * @param object canvas The canvas object
    * @param array  data   The chart data
    */
    RGraph.HBar = function (id, data)
    {
        // Get the canvas and context objects
        this.id                = id;
        this.canvas            = document.getElementById(id);
        this.context           = this.canvas.getContext ? this.canvas.getContext("2d") : null;
        this.canvas.__object__ = this;
        this.data              = data;
        this.type              = 'hbar';
        this.coords            = [];
        this.isRGraph          = true;


        /**
        * Compatibility with older browsers
        */
        RGraph.OldBrowserCompat(this.context);

        
        this.max = 0;
        this.stackedOrGrouped  = false;

        // Default properties
        this.properties = {
            'chart.gutter':                 25,
            'chart.background.grid':        true,
            'chart.background.grid.color':  '#ddd',
            'chart.background.grid.width':  1,
            'chart.background.grid.hsize':  25,
            'chart.background.grid.vsize':  25,
            'chart.background.barcolor1':   'white',
            'chart.background.barcolor2':   'white',
            'chart.background.grid.hlines': true,
            'chart.background.grid.vlines': true,
            'chart.background.grid.border': true,
            'chart.background.grid.autofit':false,
            'chart.background.grid.autofit.numhlines': 14,
            'chart.background.grid.autofit.numvlines': 20,
            'chart.title':                  '',
            'chart.title.xaxis':            '',
            'chart.title.yaxis':            '',
            'chart.title.xaxis.pos':        0.25,
            'chart.title.yaxis.pos':        0.5,
            'chart.title.hpos':             null,
            'chart.title.vpos':             null,
            'chart.text.size':              10,
            'chart.text.color':             'black',
            'chart.text.font':              'Verdana',
            'chart.colors':                 ['rgb(0,0,255)', '#0f0', '#00f', '#ff0', '#0ff', '#0f0'],
            'chart.labels':                 [],
            'chart.labels.above':           false,
            'chart.contextmenu':            null,
            'chart.key':                    [],
            'chart.key.background':         'white',
            'chart.key.position':           'graph',
            'chart.units.pre':              '',
            'chart.units.post':             '',
            'chart.units.ingraph':          false,
            'chart.strokestyle':            'black',
            'chart.xmax':                   0,
            'chart.axis.color':             'black',
            'chart.shadow':                 false,
            'chart.shadow.color':           '#666',
            'chart.shadow.blur':            3,
            'chart.shadow.offsetx':         3,
            'chart.shadow.offsety':         3,
            'chart.vmargin':                3,
            'chart.grouping':               'grouped',
            'chart.tooltips':               null,
            'chart.tooltips.effect':         'fade',
            'chart.tooltips.css.class':      'RGraph_tooltip',
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

        // Check for support
        if (!this.canvas) {
            alert('[HBAR] No canvas support');
            return;
        }
        
        // Check the canvasText library has been included
        if (typeof(RGraph) == 'undefined') {
            alert('[HBAR] Fatal error: The common library does not appear to have been included');
        }

        for (i=0; i<this.data.length; ++i) {
            if (typeof(this.data[i]) == 'object') {
                this.stackedOrGrouped = true;
            }
        }
    }


    /**
    * A setter
    * 
    * @param name  string The name of the property to set
    * @param value mixed  The value of the property
    */
    RGraph.HBar.prototype.Set = function (name, value)
    {
        if (name == 'chart.labels.abovebar') {
            name = 'chart.labels.above';
        }

        this.properties[name.toLowerCase()] = value;
    }


    /**
    * A getter
    * 
    * @param name  string The name of the property to get
    */
    RGraph.HBar.prototype.Get = function (name)
    {
        if (name == 'chart.labels.abovebar') {
            name = 'chart.labels.above';
        }

        return this.properties[name];
    }


    /**
    * The function you call to draw the bar chart
    */
    RGraph.HBar.prototype.Draw = function ()
    {
        /**
        * Fire the onbeforedraw event
        */
        RGraph.FireCustomEvent(this, 'onbeforedraw');

        var gutter = this.Get('chart.gutter');

        /**
        * Stop the coords array from growing uncontrollably
        */
        this.coords = [];

        /**
        * Work out a few things. They need to be here because they depend on things you can change before you
        * call Draw() but after you instantiate the object
        */
        this.graphwidth     = this.canvas.width - ( (4 * gutter));
        this.graphheight    = this.canvas.height - (2 * gutter);
        this.halfgrapharea  = this.grapharea / 2;
        this.halfTextHeight = this.Get('chart.text.size') / 2;
        this.lgutter = 3 * gutter;

        // Progressively Draw the chart
        this.DrawBackground();

        this.Drawbars();
        this.DrawAxes();
        this.DrawLabels();


        // Draw the key if necessary
        if (this.Get('chart.key').length) {
            RGraph.DrawKey(this, this.Get('chart.key'), this.Get('chart.colors'));
        }

        /**
        * Install the event handlers for tooltips
        */
        if (this.Get('chart.tooltips')) {

            // Need to register this object for redrawing
            RGraph.Register(this);

            /**
            * Install the window onclick handler
            */
            window.onclick = function ()
            {
                RGraph.Redraw();
            }



            /**
            * If the cursor is over a hotspot, change the cursor to a hand
            */
            this.canvas.onmousemove = function (e)
            {
                e = RGraph.FixEventObject(e);

                var canvas = document.getElementById(this.id);
                var obj = canvas.__object__;

                /**
                * Get the mouse X/Y coordinates
                */
                var mouseCoords = RGraph.getMouseXY(e);

                /**
                * Loop through the bars determining if the mouse is over a bar
                */
                for (var i=0,len=obj.coords.length; i<len; i++) {

                    var mouseX = mouseCoords[0];  // In relation to the canvas
                    var mouseY = mouseCoords[1];  // In relation to the canvas
                    var left   = obj.coords[i][0];
                    var top    = obj.coords[i][1];
                    var width  = obj.coords[i][2];
                    var height = obj.coords[i][3];

                    if (   mouseX >= (left)
                        && mouseX <= (left + width)
                        && mouseY >= top
                        && mouseY <= (top + height)
                        && (   typeof(obj.Get('chart.tooltips')) == 'function'
                            || obj.Get('chart.tooltips')[i]
                           ) ) {

                        canvas.style.cursor = 'pointer';
                        return;
                    }

                    canvas.style.cursor = 'default';
                }
            }

            /**
            * Install the onclick event handler for the tooltips
            */
            this.canvas.onclick = function (e)
            {
                e = RGraph.FixEventObject(e);

                //var canvas = document.getElementById(this.id);
                var canvas = e.target;
                var obj = canvas.__object__;

                /**
                * Redraw the graph first, in effect resetting the graph to as it was when it was first drawn
                * This "deselects" any already selected bar
                */
                RGraph.Redraw();

                /**
                * Get the mouse X/Y coordinates
                */
                var mouseCoords = RGraph.getMouseXY(e);

                /**
                * Loop through the bars determining if the mouse is over a bar
                */
                for (var i=0,len=obj.coords.length; i<len; i++) {

                    var mouseX = mouseCoords[0];  // In relation to the canvas
                    var mouseY = mouseCoords[1];  // In relation to the canvas
                    var left   = obj.coords[i][0];
                    var top    = obj.coords[i][1];
                    var width  = obj.coords[i][2];
                    var height = obj.coords[i][3];
                    var idx    = i;

                    if (mouseX >= left && mouseX <= (left + width) && mouseY >= top && mouseY <= (top + height) ) {

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

                        /**
                        * Show a tooltip if it's defined
                        */
                        if (String(text).length && text != null) {

                            obj.context.beginPath();
                            obj.context.strokeStyle = 'black';
                            obj.context.fillStyle   = 'rgba(255,255,255,0.5)';
                            obj.context.strokeRect(left, top, width, height);
                            obj.context.fillRect(left, top, width, height);
        
                            obj.context.stroke();
                            obj.context.fill();

                            RGraph.Tooltip(canvas, text, e.pageX, e.pageY, i);
                        }
                    }
                }

                /**
                * Stop the event bubbling
                */
                e.stopPropagation();
            }

            // This resets the bar graph
            if (RGraph.Registry.Get('chart.tooltip')) {
                RGraph.Registry.Get('chart.tooltip').style.display = 'none';
                RGraph.Registry.Set('chart.tooltip', null)
            }
        }

        /**
        * Setup the context menu if required
        */
        if (this.Get('chart.contextmenu')) {
            RGraph.ShowContext(this);
        }


        /**
        * Draw "in graph" labels
        */
        RGraph.DrawInGraphLabels(this);
        
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
    * This draws the axes
    */
    RGraph.HBar.prototype.DrawAxes = function ()
    {
        var gutter  = this.Get('chart.gutter');
        var halfway = ((this.canvas.width - (4 * gutter)) / 2) + (3 * gutter);

        this.context.beginPath();
        this.context.lineWidth   = 1;
        this.context.strokeStyle = this.Get('chart.axis.color');

        // Draw the Y axis
        if (this.Get('chart.yaxispos') == 'center') {
            this.context.moveTo(halfway, gutter);
            this.context.lineTo(halfway, this.canvas.height - gutter);
        } else {
            this.context.moveTo(gutter * 3, gutter);
            this.context.lineTo(gutter * 3, this.canvas.height - gutter);
        }

        // Draw the X axis
        this.context.moveTo(gutter * 3, this.canvas.height - gutter);
        this.context.lineTo(this.canvas.width - gutter, this.canvas.height - gutter);

        // Draw the Y tickmarks
        var yTickGap = (this.canvas.height - (2 * gutter)) / this.data.length;

        for (y=gutter; y<(this.canvas.height - gutter); y+=yTickGap) {
            if (this.Get('chart.yaxispos') == 'center') {
                this.context.moveTo(halfway + 3, y);
                this.context.lineTo(halfway  - 3, y);
            } else {
                this.context.moveTo(gutter * 3, y);
                this.context.lineTo( (gutter * 3)  - 3, y);
            }
        }


        // Draw the X tickmarks
        xTickGap = (this.canvas.width - (4 * gutter) ) / 10;
        yStart   = this.canvas.height - gutter;
        yEnd     = (this.canvas.height - gutter) + 3;

        for (x=(this.canvas.width - gutter), i=0; this.Get('chart.yaxispos') == 'center' ? x>=(3 * gutter) : x>(3*gutter); x-=xTickGap) {

            if (this.Get('chart.yaxispos') != 'center' || i != 5) {
                this.context.moveTo(x, yStart);
                this.context.lineTo(x, yEnd);
            }
            i++;
        }

        this.context.stroke();
    }


    /**
    * This function draws the background. The common function isn't used because the left gutter is
    * three times as big.
    * 
    * @param  object obj The graph object
    */
    RGraph.HBar.prototype.DrawBackground = function ()
    {
        var gutter  = this.Get('chart.gutter');
        var size    = this.Get('chart.text.size');
        var font    = this.Get('chart.text.font');
        var canvas  = this.canvas;
        var context = this.context;

        this.context.beginPath();

        // Draw the horizontal bars
        this.context.fillStyle = this.Get('chart.background.barcolor1');
        for (var i=gutter; i < (canvas.height - gutter); i+=80) {
            context.fillRect (gutter * 3, i, canvas.width - (gutter * 4), Math.min(40, canvas.height - gutter - i) );
        }

        this.context.fillStyle = this.Get('chart.background.barcolor2');
        for (var i= (40 + gutter); i < (canvas.height - gutter); i+=80) {
            context.fillRect (gutter * 3, i, canvas.width - (gutter * 4), i + 40 > (canvas.height - gutter) ? canvas.height - (gutter + i) : 40);
        }
        
        this.context.stroke();

        // Draw the background grid
        if (this.Get('chart.background.grid')) {
        
            // If autofit is specified, use the .numhlines and .numvlines along with the width to work
            // out the hsize and vsize
            if (this.Get('chart.background.grid.autofit')) {
                var vsize = (canvas.width - (4 * gutter)) / this.Get('chart.background.grid.autofit.numvlines');
                var hsize = (canvas.height - (4 * gutter)) / this.Get('chart.background.grid.autofit.numhlines');
                
                this.Set('chart.background.grid.vsize', vsize);
                this.Set('chart.background.grid.hsize', hsize);
            }

            context.beginPath();
            context.lineWidth   = this.Get('chart.background.grid.width');
            context.strokeStyle = this.Get('chart.background.grid.color');

            // Draw the horizontal lines
            if (this.Get('chart.background.grid.hlines')) {
                for (y=gutter; y < (canvas.height - gutter); y+=this.Get('chart.background.grid.hsize')) {
                    context.moveTo(gutter * 3, y);
                    context.lineTo(canvas.width - gutter, y);
                }
            }

            // Draw the vertical lines
            if (this.Get('chart.background.grid.vlines')) {
                for (x=gutter * 3; x <= (canvas.width - gutter); x+=this.Get('chart.background.grid.vsize')) {
                    context.moveTo(x, gutter);
                    context.lineTo(x, canvas.height - gutter);
                }
            }

            if (this.Get('chart.background.grid.border')) {
                // Make sure a rectangle, the same colour as the grid goes around the graph
                context.strokeStyle = this.Get('chart.background.grid.color');
                context.strokeRect(gutter * 3, gutter, canvas.width - (4 * gutter), canvas.height - (2 * gutter));
            }
        }
        
        context.stroke();


        // Draw the title if one is set
        if ( typeof(this.Get('chart.title')) == 'string') {
            
            RGraph.DrawTitle(canvas,
                             this.Get('chart.title'),
                             gutter,
                             (3 * gutter) + ((canvas.width - (4 * gutter)) / 2),
                             size + 2);
        }

        context.stroke();


        // X axis title
        if (typeof(this.Get('chart.title.xaxis')) == 'string' && this.Get('chart.title.xaxis').length) {        
            context.beginPath();
            RGraph.Text(context, font, size + 2, canvas.width / 2, canvas.height - (gutter * this.Get('chart.title.xaxis.pos')), this.Get('chart.title.xaxis'), 'center', 'center', false, false, false, true);
            context.fill();
        }


        // Y axis title
        if (typeof(this.Get('chart.title.yaxis')) == 'string' && this.Get('chart.title.yaxis').length) {
            context.beginPath();
            RGraph.Text(context, font, size + 2, gutter * this.Get('chart.title.yaxis.pos'), canvas.height / 2, this.Get('chart.title.yaxis'), 'center', 'center', false, 270, false, true);
            context.fill();
        }
    }


    /**
    * This draws the labels for the graph
    */
    RGraph.HBar.prototype.DrawLabels = function ()
    {
        var gutter     = this.Get('chart.gutter');
        var context    = this.context;
        var canvas     = this.canvas;
        var units_pre  = this.Get('chart.units.pre');
        var units_post = this.Get('chart.units.post');
        var text_size  = this.Get('chart.text.size');
        var font       = this.Get('chart.text.font');


        /**
        * Set the units to blank if they're to be used for ingraph labels only
        */
        if (this.Get('chart.units.ingraph')) {
            units_pre  = '';
            units_post = '';
        }


        /**
        * Draw the X axis labels
        */
        this.context.beginPath();
        this.context.fillStyle = this.Get('chart.text.color');

        //var interval = (this.canvas.width - (4 * gutter)) / (t ? 10 : 5);

        if (this.Get('chart.yaxispos') == 'center') {
            RGraph.Text(context, font, text_size, (gutter * 3) + (this.graphwidth * (10/10)), gutter + this.halfTextHeight + this.graphheight + 2, RGraph.number_format(this.scale[4], units_pre, units_post), 'center', 'center');
            RGraph.Text(context, font, text_size, (gutter * 3) + (this.graphwidth * (9/10)), gutter + this.halfTextHeight + this.graphheight + 2, RGraph.number_format(this.scale[3], units_pre, units_post), 'center', 'center');
            RGraph.Text(context, font, text_size, (gutter * 3) + (this.graphwidth * (8/10)), gutter + this.halfTextHeight + this.graphheight + 2, RGraph.number_format(this.scale[2], units_pre, units_post), 'center', 'center');
            RGraph.Text(context, font, text_size, (gutter * 3) + (this.graphwidth * (7/10)), gutter + this.halfTextHeight + this.graphheight + 2, RGraph.number_format(this.scale[1], units_pre, units_post), 'center', 'center');
            RGraph.Text(context, font, text_size, (gutter * 3) + (this.graphwidth * (6/10)), gutter + this.halfTextHeight + this.graphheight + 2, RGraph.number_format(this.scale[0], units_pre, units_post), 'center', 'center');
            
            RGraph.Text(context, font, text_size, (gutter * 3) + (this.graphwidth * (4/10)), gutter + this.halfTextHeight + this.graphheight + 2, RGraph.number_format(-1 * this.scale[0], units_pre, units_post), 'center', 'center');
            RGraph.Text(context, font, text_size, (gutter * 3) + (this.graphwidth * (3/10)), gutter + this.halfTextHeight + this.graphheight + 2, RGraph.number_format(-1 * this.scale[1], units_pre, units_post), 'center', 'center');
            RGraph.Text(context, font, text_size, (gutter * 3) + (this.graphwidth * (2/10)), gutter + this.halfTextHeight + this.graphheight + 2, RGraph.number_format(-1 * this.scale[2], units_pre, units_post), 'center', 'center');
            RGraph.Text(context, font, text_size, (gutter * 3) + (this.graphwidth * (1/10)), gutter + this.halfTextHeight + this.graphheight + 2, RGraph.number_format(-1 * this.scale[3], units_pre, units_post), 'center', 'center');
            RGraph.Text(context, font, text_size, (gutter * 3) + (this.graphwidth * (0)), gutter + this.halfTextHeight + this.graphheight + 2, RGraph.number_format(-1 * this.scale[4], units_pre, units_post), 'center', 'center');

        } else {

            RGraph.Text(context, font, text_size, (gutter * 3) + (this.graphwidth * (5/5)), gutter + this.halfTextHeight + this.graphheight + 2, RGraph.number_format(this.scale[4], units_pre, units_post), 'center', 'center');
            RGraph.Text(context, font, text_size, (gutter * 3) + (this.graphwidth * (4/5)), gutter + this.halfTextHeight + this.graphheight + 2, RGraph.number_format(this.scale[3], units_pre, units_post), 'center', 'center');
            RGraph.Text(context, font, text_size, (gutter * 3) + (this.graphwidth * (3/5)), gutter + this.halfTextHeight + this.graphheight + 2, RGraph.number_format(this.scale[2], units_pre, units_post), 'center', 'center');
            RGraph.Text(context, font, text_size, (gutter * 3) + (this.graphwidth * (2/5)), gutter + this.halfTextHeight + this.graphheight + 2, RGraph.number_format(this.scale[1], units_pre, units_post), 'center', 'center');
            RGraph.Text(context, font, text_size, (gutter * 3) + (this.graphwidth * (1/5)), gutter + this.halfTextHeight + this.graphheight + 2, RGraph.number_format(this.scale[0], units_pre, units_post), 'center', 'center');
        }
        
        this.context.fill();
        this.context.stroke();

        /**
        * The Y axis labels
        */
        if (typeof(this.Get('chart.labels')) == 'object') {
        
            var xOffset = 5;
            var font    = this.Get('chart.text.font');

            // Draw the X axis labels
            this.context.fillStyle = this.Get('chart.text.color');
            
            // How wide is each bar
            var barHeight = (this.canvas.height - (2 * gutter) ) / this.Get('chart.labels').length;
            
            // Reset the xTickGap
            yTickGap = (this.canvas.height - (2 * gutter)) / this.Get('chart.labels').length

            // Draw the X tickmarks
            var i=0;
            for (y=gutter + (yTickGap / 2); y<=this.canvas.height - gutter; y+=yTickGap) {
                RGraph.Text(this.context, font,
                                      this.Get('chart.text.size'),
                                      (gutter * 3) - xOffset,
                                      y,
                                      String(this.Get('chart.labels')[i++]),
                                      'center',
                                      'right');
            }
        }
    }
    
    
    /**
    * This function draws the actual bars
    */
    RGraph.HBar.prototype.Drawbars = function ()
    {
        this.context.lineWidth   = 1;
        this.context.strokeStyle = this.Get('chart.strokestyle');
        this.context.fillStyle   = this.Get('chart.colors')[0];
        var prevX                = 0;
        var prevY                = 0;

        /**
        * Work out the max value
        */
        if (this.Get('chart.xmax')) {
            this.scale = RGraph.getScale(this.Get('chart.xmax'));
            this.max   = this.scale[4];
        } else {
            var grouping = this.Get('chart.grouping');

            for (i=0; i<this.data.length; ++i) {
                if (typeof(this.data[i]) == 'object') {
                    var value = grouping == 'grouped' ? Number(RGraph.array_max(this.data[i], true)) : Number(RGraph.array_sum(this.data[i])) ;
                } else {
                    var value = Number(this.data[i]);
                }

                this.max = Math.max(Math.abs(this.max), Math.abs(value));
            }

            this.scale = RGraph.getScale(this.max);
            this.max   = this.scale[4];
        }


        /**
        * The bars are drawn HERE
        */
        var gutter     = this.Get('chart.gutter');
        var graphwidth = (this.canvas.width - (4 * gutter));
        var halfwidth  = graphwidth / 2;

        for (i=0; i<this.data.length; ++i) {

            // Work out the width and height
            var width  = (this.data[i] / this.max) *  graphwidth;
            var height = this.graphheight / this.data.length;

            var orig_height = height;

            var x       = 3 * gutter;
            var y       = gutter + (i * height);
            var vmargin = this.Get('chart.vmargin');
            var gutter  = gutter;

            // Account for negative lengths - Some browsers (eg Chrome) don't like a negative value
            if (width < 0) {
                x -= width;
                width = Math.abs(width);
            }

            /**
            * Turn on the shadow if need be
            */
            if (this.Get('chart.shadow')) {
                this.context.shadowColor   = this.Get('chart.shadow.color');
                this.context.shadowBlur    = this.Get('chart.shadow.blur');
                this.context.shadowOffsetX = this.Get('chart.shadow.offsetx');
                this.context.shadowOffsetY = this.Get('chart.shadow.offsety');
            }

            /**
            * Draw the bar
            */
            this.context.beginPath();
                if (typeof(this.data[i]) == 'number') {
                    
                    var barHeight = height - (2 * vmargin);
                    var barWidth  = (this.data[i] / this.max) * this.graphwidth;
                    var barX      = 3 * gutter;

                    // Account for Y axis pos
                    if (this.Get('chart.yaxispos') == 'center') {
                        barWidth /= 2;
                        barX += halfwidth;
                    }

                    // Set the fill color
                    this.context.strokeStyle = this.Get('chart.strokestyle');
                    this.context.fillStyle = this.Get('chart.colors')[0];

                    this.context.strokeRect(barX, gutter + (i * height) + this.Get('chart.vmargin'), barWidth, barHeight);
                    this.context.fillRect(barX, gutter + (i * height) + this.Get('chart.vmargin'), barWidth, barHeight);

                    this.coords.push([x, y + vmargin, width, height - (2 * vmargin), this.Get('chart.colors')[0], this.data[i]]);

                /**
                * Stacked bar chart
                */
                } else if (typeof(this.data[i]) == 'object' && this.Get('chart.grouping') == 'stacked') {

                    var barHeight = height - (2 * vmargin);

                    for (j=0; j<this.data[i].length; ++j) {

                        // Set the fill/stroke colors
                        this.context.strokeStyle = this.Get('chart.strokestyle');
                        this.context.fillStyle = this.Get('chart.colors')[j];

                        var width = (this.data[i][j] / this.max) * this.graphwidth;
                        var totalWidth = (RGraph.array_sum(this.data[i]) / this.max) * this.graphwidth;

                        this.context.strokeRect(x, gutter + this.Get('chart.vmargin') + (this.graphheight / this.data.length) * i, width, height - (2 * vmargin) );
                        this.context.fillRect(x, gutter + this.Get('chart.vmargin') + (this.graphheight / this.data.length) * i, width, height - (2 * vmargin) );

                        /**
                        * Store the coords for tooltips
                        */

                        this.coords.push([x, y + vmargin, width, height - (2 * vmargin), this.Get('chart.colors')[j], RGraph.array_sum(this.data[i])]);

                        x += width;
                    }

                /**
                * A grouped bar chart
                */
                } else if (typeof(this.data[i]) == 'object' && this.Get('chart.grouping') == 'grouped') {

                    for (j=0; j<this.data[i].length; ++j) {

                        /**
                        * Turn on the shadow if need be
                        */
                        if (this.Get('chart.shadow')) {
                            RGraph.SetShadow(this, this.Get('chart.shadow.color'), this.Get('chart.shadow.offsetx'), this.Get('chart.shadow.offsety'), this.Get('chart.shadow.blur'));
                        }

                        // Set the fill/stroke colors
                        this.context.strokeStyle = this.Get('chart.strokestyle');
                        this.context.fillStyle = this.Get('chart.colors')[j];

                        var width = (this.data[i][j] / this.max) * (this.canvas.width - (4 * gutter) );
                        var individualBarHeight = (height - (2 * vmargin)) / this.data[i].length;

                        var startX = gutter * 3;
                        var startY = y + vmargin + (j * individualBarHeight);

                        // Account for the Y axis being in the middle
                        if (this.Get('chart.yaxispos') == 'center') {
                            width  /= 2;
                            startX += halfwidth;
                        }
                        
                        if (width < 0) {
                            startX += width;
                            width *= -1;
                        }

                        this.context.strokeRect(startX, startY, width, individualBarHeight);
                        this.context.fillRect(startX, startY, width, individualBarHeight);

                        this.coords.push([startX, startY, width, individualBarHeight, this.Get('chart.colors')[j], this.data[i][j]]);
                    }
                }

            this.context.closePath();
        }

        this.context.fill();
        this.context.stroke();



        /**
        * Now the bars are stroke()ed, turn off the shadow
        */
        RGraph.NoShadow(this);
        
        this.RedrawBars();
    }
    
    
    /**
    * This function goes over the bars after they been drawn, so that upwards shadows are underneath the bars
    */
    RGraph.HBar.prototype.RedrawBars = function ()
    {
        var coords = this.coords;

        RGraph.NoShadow(this);
        this.context.strokeStyle = this.Get('chart.strokestyle');

        for (var i=0; i<coords.length; ++i) {

            this.context.beginPath();
                this.context.strokeStyle = this.Get('chart.strokestyle');
                this.context.fillStyle = coords[i][4];
                this.context.lineWidth = 1;
                this.context.strokeRect(coords[i][0], coords[i][1], coords[i][2], coords[i][3]);
                this.context.fillRect(coords[i][0], coords[i][1], coords[i][2], coords[i][3]);
            this.context.fill();
            this.context.stroke();

            /**
            * Draw labels "above" the bar
            */
            if (this.Get('chart.labels.above')) {

                this.context.fillStyle = this.Get('chart.text.color');
                this.context.strokeStyle = 'black';
                RGraph.NoShadow(this);

                var border = (coords[i][0] + coords[i][2] + 7 + this.context.measureText(this.Get('chart.units.pre') + this.coords[i][5] + this.Get('chart.units.post')).width) > this.canvas.width ? true : false;

                RGraph.Text(this.context,this.Get('chart.text.font'),this.Get('chart.text.size'),coords[i][0] + coords[i][2] + (border ? -5 : 5),coords[i][1] + (coords[i][3] / 2),RGraph.number_format(this.coords[i][5], this.Get('chart.units.pre'), this.Get('chart.units.post')),'center',border ? 'right' : 'left',border,null,border ? 'rgba(255,255,255,0.9)' : null);

            }
        }
    }
