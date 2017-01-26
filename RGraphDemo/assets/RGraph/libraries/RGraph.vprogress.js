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
    * The progress bar constructor
    * 
    * @param int id    The ID of the canvas tag
    * @param int value The indicated value of the meter.
    * @param int max   The end value (the upper most) of the meter
    */
    RGraph.VProgress = function (id, value, max)
    {
        this.id                = id;
        this.max               = max;
        this.value             = value;
        this.canvas            = document.getElementById(id);
        this.context           = this.canvas.getContext('2d');
        this.canvas.__object__ = this;
        this.type              = 'vprogress';
        this.coords            = [];
        this.isRGraph          = true;


        /**
        * Compatibility with older browsers
        */
        RGraph.OldBrowserCompat(this.context);

        this.properties = {
            'chart.colors':             ['#0c0'],
            'chart.tickmarks':          true,
            'chart.tickmarks.color':    'black',
            'chart.tickmarks.inner':    false,
            'chart.gutter':             25,
            'chart.numticks':           10,
            'chart.numticks.inner':     50,
            'chart.background.color':   '#eee',
            'chart.shadow':             false,
            'chart.shadow.color':       'rgba(0,0,0,0.5)',
            'chart.shadow.blur':        3,
            'chart.shadow.offsetx':     3,
            'chart.shadow.offsety':     3,
            'chart.title':              '',
            'chart.title.hpos':         null,
            'chart.title.vpos':         null,
            'chart.width':              0,
            'chart.height':             0,
            'chart.text.size':          10,
            'chart.text.color':         'black',
            'chart.text.font':          'Verdana',
            'chart.contextmenu':        null,
            'chart.units.pre':          '',
            'chart.units.post':         '',
            'chart.tooltips':           [],
            'chart.tooltips.effect':     'fade',
            'chart.tooltips.css.class':  'RGraph_tooltip',
            'chart.annotatable':        false,
            'chart.annotate.color':     'black',
            'chart.zoom.mode':          'canvas',
            'chart.zoom.factor':        1.5,
            'chart.zoom.fade.in':       true,
            'chart.zoom.fade.out':      true,
            'chart.zoom.hdir':          'right',
            'chart.zoom.vdir':          'down',
            'chart.zoom.frames':        10,
            'chart.zoom.delay':         50,
            'chart.zoom.shadow':        true,
            'chart.zoom.background':    true,
            'chart.zoom.action':        'zoom',
            'chart.arrows':             false,
            'chart.margin':             0,
            'chart.resizable':          false,
            'chart.label.inner':        false,
            'chart.adjustable':         false
        }

        // Check for support
        if (!this.canvas) {
            alert('[PROGRESS] No canvas support');
            return;
        }

        // Check the common library has been included
        if (typeof(RGraph) == 'undefined') {
            alert('[PROGRESS] Fatal error: The common library does not appear to have been included');
        }
    }


    /**
    * A generic setter
    * 
    * @param string name  The name of the property to set
    * @param string value The value of the poperty
    */
    RGraph.VProgress.prototype.Set = function (name, value)
    {
        this.properties[name.toLowerCase()] = value;
    }


    /**
    * A generic getter
    * 
    * @param string name  The name of the property to get
    */
    RGraph.VProgress.prototype.Get = function (name)
    {
        return this.properties[name.toLowerCase()];
    }


    /**
    * Draws the progress bar
    */
    RGraph.VProgress.prototype.Draw = function ()
    {
        /**
        * Fire the onbeforedraw event
        */
        RGraph.FireCustomEvent(this, 'onbeforedraw');

        // Figure out the width and height
        this.width  = this.canvas.width - (2 * this.Get('chart.gutter'));
        this.height = this.canvas.height - (2 * this.Get('chart.gutter'));
        this.coords = [];

        this.Drawbar();
        this.DrawTickMarks();
        this.DrawLabels();

        this.context.stroke();
        this.context.fill();

        /**
        * Setup the context menu if required
        */
        if (this.Get('chart.contextmenu')) {
            RGraph.ShowContext(this);
        }
        
        /**
        * Alternatively, show the tooltip if requested
        */
        if (typeof(this.Get('chart.tooltips')) == 'function' || this.Get('chart.tooltips').length) {

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
            * Install the onclick event handler for the tooltips
            */
            this.canvas.onclick = function (e)
            {
                e = RGraph.FixEventObject(e);

                var canvas = document.getElementById(this.id);
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
                for (var i=0; i<obj.coords.length; i++) {

                    var mouseX = mouseCoords[0];
                    var mouseY = mouseCoords[1];
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
                        if (text) {

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
                for (var i=0; i<obj.coords.length; i++) {

                    var mouseX = mouseCoords[0];  // In relation to the canvas
                    var mouseY = mouseCoords[1];  // In relation to the canvas
                    var left   = obj.coords[i][0];
                    var top    = obj.coords[i][1];
                    var width  = obj.coords[i][2];
                    var height = obj.coords[i][3];

                    if (mouseX >= left && mouseX <= (left + width) && mouseY >= top && mouseY <= (top + height) ) {
                        canvas.style.cursor = 'pointer';
                        break;
                    }
                    
                    canvas.style.cursor = 'default';
                }
            }
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
        * Instead of using RGraph.common.adjusting.js, handle them here
        */
        if (this.Get('chart.adjustable')) {
            RGraph.AllowAdjusting(this);
        }
        
        /**
        * Fire the RGraph ondraw event
        */
        RGraph.FireCustomEvent(this, 'ondraw');
    }

    /**
    * 
    */
    RGraph.VProgress.prototype.Drawbar = function ()
    {
        // Set a shadow if requested
        if (this.Get('chart.shadow')) {
            RGraph.SetShadow(this, this.Get('chart.shadow.color'), this.Get('chart.shadow.offsetx'), this.Get('chart.shadow.offsety'), this.Get('chart.shadow.blur'));
        }

        // Draw the shadow for MSIE
        if (document.all && this.Get('chart.shadow')) {
            this.context.fillStyle = this.Get('chart.shadow.color');
            this.context.fillRect(this.Get('chart.gutter') + this.Get('chart.shadow.offsetx'), this.Get('chart.gutter') + this.Get('chart.shadow.offsety'), this.width, this.height);
        }

        // Draw the outline
        this.context.fillStyle   = this.Get('chart.background.color');
        this.context.strokeStyle = 'black';
        this.context.strokeRect(this.Get('chart.gutter'), this.Get('chart.gutter'), this.width, this.height);
        this.context.fillRect(this.Get('chart.gutter'), this.Get('chart.gutter'), this.width, this.height);

        // Turn off any shadow
        RGraph.NoShadow(this);

        this.context.strokeStyle = 'black';
        this.context.fillStyle   = this.Get('chart.colors')[0];
        var margin = this.Get('chart.margin');
        var barHeight = this.canvas.height - this.Get('chart.gutter') - this.Get('chart.gutter');

        // Draw the actual bar itself
        if (typeof(this.value) == 'number') {

            this.context.lineWidth   = 1;
            this.context.strokeStyle = '#999';

        } else if (typeof(this.value) == 'object') {

            this.context.beginPath();
            this.context.strokeStyle = '#999';

            var startPoint = this.canvas.height - this.Get('chart.gutter');
            
            for (var i=0; i<this.value.length; ++i) {

                var segmentHeight = (this.value[i] / this.max) * barHeight;

                this.context.fillStyle = this.Get('chart.colors')[i];

                this.context.fillRect(this.Get('chart.gutter') + margin, startPoint - segmentHeight, this.width - margin - margin, segmentHeight);
                this.context.strokeRect(this.Get('chart.gutter') + margin, startPoint - segmentHeight, this.width - margin - margin, segmentHeight);


                // Store the coords
                this.coords.push([this.Get('chart.gutter') + margin, startPoint - segmentHeight, this.width - margin - margin, segmentHeight]);

                startPoint -= segmentHeight;
            }

        }

        /**
        * Inner tickmarks
        */
        if (this.Get('chart.tickmarks.inner')) {
        
            var spacing = (this.canvas.height - this.Get('chart.gutter') - this.Get('chart.gutter')) / this.Get('chart.numticks.inner');

            this.context.lineWidth   = 1;
            this.context.strokeStyle = '#999';

            this.context.beginPath();

            for (var y = this.Get('chart.gutter'); y<this.canvas.height - this.Get('chart.gutter'); y+=spacing) {
                this.context.moveTo(this.Get('chart.gutter'), y);
                this.context.lineTo(this.Get('chart.gutter') + 3, y);

                this.context.moveTo(this.canvas.width - this.Get('chart.gutter'), y);
                this.context.lineTo(this.canvas.width - this.Get('chart.gutter') - 3, y);
            }

            this.context.stroke();
        }

        /**
        * Draw the actual bar
        */
        var barHeight = Math.min(this.height, (this.value / this.max) * this.height);

        this.context.beginPath();
        this.context.strokeStyle = 'black';

        if (typeof(this.value) == 'number') {
            this.context.strokeRect(this.Get('chart.gutter') + margin, this.Get('chart.gutter') + this.height - barHeight, this.width - margin - margin, barHeight);
            this.context.fillRect(this.Get('chart.gutter') + margin, this.Get('chart.gutter') + this.height - barHeight, this.width - margin - margin, barHeight);
        }


        /**
        * Draw the arrows indicating the level if requested
        */
        if (this.Get('chart.arrows')) {
            var x = this.Get('chart.gutter') - 4;
            var y = this.canvas.height - this.Get('chart.gutter') - barHeight;
            
            this.context.lineWidth = 1;
            this.context.fillStyle = 'black';
            this.context.strokeStyle = 'black';

            this.context.beginPath();
                this.context.moveTo(x, y);
                this.context.lineTo(x - 4, y - 2);
                this.context.lineTo(x - 4, y + 2);
            this.context.closePath();

            this.context.stroke();
            this.context.fill();

            x +=  this.width + 8;

            this.context.beginPath();
                this.context.moveTo(x, y);
                this.context.lineTo(x + 4, y - 2);
                this.context.lineTo(x + 4, y + 2);
            this.context.closePath();

            this.context.stroke();
            this.context.fill();
        }




        /**
        * Draw the "in-bar" label
        */
        if (this.Get('chart.label.inner')) {
            this.context.beginPath();
            this.context.fillStyle = 'black';
            RGraph.Text(this.context, this.Get('chart.text.font'), this.Get('chart.text.size') + 2, this.canvas.width / 2, this.canvas.height - this.Get('chart.gutter') - barHeight - 5, String(this.Get('chart.units.pre') + this.value + this.Get('chart.units.post')), 'bottom', 'center');
            this.context.fill();
        }


        // Store the coords
        this.coords.push([this.Get('chart.gutter') + margin, this.Get('chart.gutter') + this.height - barHeight, this.width - margin - margin, barHeight]);
    }

    /**
    * The function that draws the tick marks. Apt name...
    */
    RGraph.VProgress.prototype.DrawTickMarks = function ()
    {
        this.context.strokeStyle = this.Get('chart.tickmarks.color');

        if (this.Get('chart.tickmarks')) {
        
            this.tickInterval = this.height / this.Get('chart.numticks');

            for (var i=this.Get('chart.gutter'); i<=(this.canvas.height - this.Get('chart.gutter') - this.tickInterval); i+=this.tickInterval) {
                this.context.moveTo(this.canvas.width - this.Get('chart.gutter'), i);
                this.context.lineTo(this.canvas.width - this.Get('chart.gutter') + 4, i);
            }
        }

        this.context.stroke();
    }


    /**
    * The function that draws the labels
    */
    RGraph.VProgress.prototype.DrawLabels = function ()
    {
        this.context.fillStyle = this.Get('chart.text.color');

        var xPoints = [];
        var yPoints = [];

        for (i=this.Get('chart.gutter'); i < (this.canvas.height - this.tickInterval); i+= this.tickInterval) {
            xPoints.push(this.canvas.width - this.Get('chart.gutter') + 4);
            yPoints.push(i);
        }

        var xAlignment = 'left';
        var yAlignment = 'center';

        for (i=0; i<xPoints.length; ++i) {
            RGraph.Text(this.context,
                        this.Get('chart.text.font'),
                        this.Get('chart.text.size'),
                        xPoints[i],
                        yPoints[i],
                        this.Get('chart.units.pre') + String( this.max - parseInt( (this.max / yPoints.length) * i)) + this.Get('chart.units.post'),
                        yAlignment,
                        xAlignment);
        }

        // Draw the title text
        if (this.Get('chart.title')) {
            RGraph.DrawTitle(this.canvas, this.Get('chart.title'), this.Get('chart.gutter'), 0, this.Get('chart.text.size') + 2);
        }
    }