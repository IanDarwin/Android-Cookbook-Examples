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
    * The chart constuctor
    * 
    * @param object canvas
    * @param array data
    */
    RGraph.Rscatter = function (id, data)
    {
        this.id                = id;
        this.canvas            = document.getElementById(id);
        this.context           = this.canvas.getContext('2d');
        this.data              = data;
        this.canvas.__object__ = this;
        this.type              = 'rscatter';
        this.hasTooltips       = false;
        this.isRGraph          = true;


        /**
        * Compatibility with older browsers
        */
        RGraph.OldBrowserCompat(this.context);


        this.centerx = 0;
        this.centery = 0;
        this.radius  = 0;
        this.max     = 0;
        
        this.properties = {
            'chart.colors':                 [], // This is used internally for the key
            'chart.colors.default':         'black',
            'chart.gutter':                 25,
            'chart.title':                  '',
            'chart.title.hpos':             null,
            'chart.title.vpos':             null,
            'chart.labels':                 null,
            'chart.labels.position':       'center',
            'chart.labels.axes':            'nsew',
            'chart.text.color':             'black',
            'chart.text.font':              'Verdana',
            'chart.text.size':              10,
            'chart.key':                    null,
            'chart.key.shadow':             false,
            'chart.key.background':         'white',
            'chart.key.position':           'graph',
            'chart.contextmenu':            null,
            'chart.tooltips.effect':        'fade',
            'chart.tooltips.css.class':     'RGraph_tooltip',
            'chart.tooltips.hotspot':       3,
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
            'chart.resizable':              false,
            'chart.adjustable':             false,
            'chart.ymax':                   null,
            'chart.tickmarks':              'cross',
            'chart.ticksize':               3,
        }
        
        // Check the common library has been included
        if (typeof(RGraph) == 'undefined') {
            alert('[RSCATTER] Fatal error: The common library does not appear to have been included');
        }
    }


    /**
    * A simple setter
    * 
    * @param string name  The name of the property to set
    * @param string value The value of the property
    */
    RGraph.Rscatter.prototype.Set = function (name, value)
    {
        this.properties[name.toLowerCase()] = value;
    }
    
    
    /**
    * A simple getter
    * 
    * @param string name The name of the property to get
    */
    RGraph.Rscatter.prototype.Get = function (name)
    {
        return this.properties[name.toLowerCase()];
    }

    
    /**
    * This method draws the rose chart
    */
    RGraph.Rscatter.prototype.Draw = function ()
    {
        /**
        * Fire the onbeforedraw event
        */
        RGraph.FireCustomEvent(this, 'onbeforedraw');

        // Calculate the radius
        this.radius  = (Math.min(this.canvas.width, this.canvas.height) / 2) - this.Get('chart.gutter');
        this.centerx = this.canvas.width / 2;
        this.centery = this.canvas.height / 2;
        this.coords  = [];
        
        /**
        * Work out the scale
        */
        for (var i=0; i<this.data.length; ++i) {
            this.max = Math.max(this.max, this.data[i][1]);
        }
        this.scale = RGraph.getScale(this.max);
        this.max   = this.scale[4];

        /**
        * Change the centerx marginally if the key is defined
        */
        if (this.Get('chart.key') && this.Get('chart.key').length > 0 && this.Get('chart.key').length >= 3) {
            this.centerx = this.centerx - this.Get('chart.gutter') + 5;
        }
        
        /**
        * Populate the colors array for the purposes of generating the key
        */
        if (typeof(this.Get('chart.key')) == 'object' && RGraph.is_array(this.Get('chart.key')) && this.Get('chart.key')[0]) {
            for (var i=0; i<this.data.length; ++i) {
                if (this.data[i][2] && typeof(this.data[i][2]) == 'string') {
                    this.Get('chart.colors').push(this.data[i][2]);
                }
            }
        }

        this.DrawBackground();
        this.DrawRscatter();
        this.DrawLabels();

        /**
        * Setup the context menu if required
        */
        if (this.Get('chart.contextmenu')) {
            RGraph.ShowContext(this);
        }

        /**
        * Tooltips
        */
        if (this.hasTooltips) {

            /**
            * Register this object for redrawing
            */
            RGraph.Register(this);
            
            /**
            * The onmousemove event
            */
            this.canvas.onmousemove = function (event)
            {
                event = RGraph.FixEventObject(event);

                var mouseCoords = RGraph.getMouseXY(event);
                var mouseX      = mouseCoords[0];
                var mouseY      = mouseCoords[1];
                var obj         = event.target.__object__;
                var canvas      = obj.canvas;
                var context     = obj.context;
                var overHotspot = false;
                var offset      = obj.Get('chart.tooltips.hotspot'); // This is how far the hotspot extends

                for (var i=0; i<obj.coords.length; ++i) {
                
                    var xCoord  = obj.coords[i][0];
                    var yCoord  = obj.coords[i][1];
                    var tooltip = obj.coords[i][3];

                    if (
                        mouseX < (xCoord + offset) &&
                        mouseX > (xCoord - offset) &&
                        mouseY < (yCoord + offset) &&
                        mouseY > (yCoord - offset) &&
                        typeof(tooltip) == 'string' &&
                        tooltip.length > 0
                       ) {

                        overHotspot = true;
                        canvas.style.cursor = 'pointer';

                        if (!RGraph.Registry.Get('chart.tooltip') || RGraph.Registry.Get('chart.tooltip').__text__ != tooltip) {
    
                            RGraph.Redraw();
    
                            /**
                            * Get the tooltip text
                            */
                            if (typeof(tooltip) == 'function') {
                                var text = String(tooltip(i));
    
                            } else {
                                var text = String(tooltip);
                            }
    
                            RGraph.Tooltip(canvas, text, event.pageX + 5, event.pageY - 5, i);
    
                            // Draw a circle around the mark that ONLY highlights it
                            context.beginPath();
                            context.fillStyle = 'rgba(255,255,255,0.5)';
                            context.arc(xCoord, yCoord, 3, 0, 6.2830, 0);
                            context.fill();
                        }
                    }
                }
                
                if (!overHotspot) {
                    canvas.style.cursor = 'default';
                }
            }
        }

        // Draw the title if any has been set
        if (this.Get('chart.title')) {
            RGraph.DrawTitle(this.canvas, this.Get('chart.title'), this.Get('chart.gutter'), this.centerx, this.Get('chart.text.size') + 2);
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
    * This method draws the rose charts background
    */
    RGraph.Rscatter.prototype.DrawBackground = function ()
    {
        this.context.lineWidth = 1;
    
        // Draw the background grey circles
        this.context.strokeStyle = '#ccc';
        for (var i=15; i<this.radius - (document.all ? 5 : 0); i+=15) {// Radius must be greater than 0 for Opera to work
            //this.context.moveTo(this.centerx + i, this.centery);
    
            // Radius must be greater than 0 for Opera to work
            this.context.arc(this.centerx, this.centery, i, 0, (2 * Math.PI), 0);
        }
        this.context.stroke();

        // Draw the background lines that go from the center outwards
        this.context.beginPath();
        for (var i=15; i<360; i+=15) {
        
            // Radius must be greater than 0 for Opera to work
            this.context.arc(this.centerx, this.centery, this.radius, i / 57.3, (i + 0.01) / 57.3, 0);
        
            this.context.lineTo(this.centerx, this.centery);
        }
        this.context.stroke();
        
        this.context.beginPath();
        this.context.strokeStyle = 'black';
    
        // Draw the X axis
        this.context.moveTo(this.centerx - this.radius, this.centery);
        this.context.lineTo(this.centerx + this.radius, this.centery);
    
        // Draw the X ends
        this.context.moveTo(this.centerx - this.radius, this.centery - 5);
        this.context.lineTo(this.centerx - this.radius, this.centery + 5);
        this.context.moveTo(this.centerx + this.radius, this.centery - 5);
        this.context.lineTo(this.centerx + this.radius, this.centery + 5);
        
        // Draw the X check marks
        for (var i=(this.centerx - this.radius); i<(this.centerx + this.radius); i+=20) {
            this.context.moveTo(i,  this.centery - 3);
            this.context.lineTo(i,  this.centery + 3);
        }
        
        // Draw the Y check marks
        for (var i=(this.centery - this.radius); i<(this.centery + this.radius); i+=20) {
            this.context.moveTo(this.centerx - 3, i);
            this.context.lineTo(this.centerx + 3, i);
        }
    
        // Draw the Y axis
        this.context.moveTo(this.centerx, this.centery - this.radius);
        this.context.lineTo(this.centerx, this.centery + this.radius);
    
        // Draw the Y ends
        this.context.moveTo(this.centerx - 5, this.centery - this.radius);
        this.context.lineTo(this.centerx + 5, this.centery - this.radius);
    
        this.context.moveTo(this.centerx - 5, this.centery + this.radius);
        this.context.lineTo(this.centerx + 5, this.centery + this.radius);
        
        // Stroke it
        this.context.closePath();
        this.context.stroke();
    }


    /**
    * This method draws a set of data on the graph
    */
    RGraph.Rscatter.prototype.DrawRscatter = function ()
    {
        var data = this.data;

        for (var i=0; i<data.length; ++i) {

            var d1 = data[i][0];
            var d2 = data[i][1];
            var a   = d1 / (180 / Math.PI); // RADIANS
            var r   = (d2 / this.max) * this.radius;
            var x   = Math.sin(a) * r;
            var y   = Math.cos(a) * r;
            var color = data[i][2] ? data[i][2] : this.Get('chart.colors.default');
            var tooltip = data[i][3] ? data[i][3] : null;
            
            if (tooltip && tooltip.length) {
                this.hasTooltips = true;
            }

            /**
            * Account for the correct quadrant
            */
            x = x + this.centerx;
            y = this.centery - y;


            this.DrawTick(x, y, color);
            
            // Populate the coords array with the coordinates and the tooltip
            this.coords.push([x, y, color, tooltip]);
        }
    }


    /**
    * Unsuprisingly, draws the labels
    */
    RGraph.Rscatter.prototype.DrawLabels = function ()
    {
        
        this.context.lineWidth = 1;
        var key = this.Get('chart.key');
        
        // Set the color to black
        this.context.fillStyle = 'black';
        this.context.strokeStyle = 'black';
        
        var r         = this.radius;
        var color     = this.Get('chart.text.color');
        var font_face = this.Get('chart.text.font');
        var font_size = this.Get('chart.text.size');
        var context   = this.context;
        var axes      = this.Get('chart.labels.axes').toLowerCase();
        
        this.context.fillStyle = this.Get('chart.text.color');

        // Draw any labels
        if (typeof(this.Get('chart.labels')) == 'object' && this.Get('chart.labels')) {
            this.DrawCircularLabels(context, this.Get('chart.labels'), font_face, font_size, r);
        }


        var color = 'rgba(255,255,255,0.8)';

        // The "North" axis labels
        if (axes.indexOf('n') > -1) {
            RGraph.Text(context,font_face,font_size,this.centerx,this.centery - ((r) * 0.2),String(this.scale[0]),'center','center',true,false,color);
            RGraph.Text(context, font_face, font_size, this.centerx, this.centery - ((r) * 0.4), String(this.scale[1]), 'center', 'center', true, false, color);
            RGraph.Text(context, font_face, font_size, this.centerx, this.centery - ((r) * 0.6), String(this.scale[2]), 'center', 'center', true, false, color);
            RGraph.Text(context, font_face, font_size, this.centerx, this.centery - ((r) * 0.8), String(this.scale[3]), 'center', 'center', true, false, color);
            RGraph.Text(context, font_face, font_size, this.centerx, this.centery - r, String(this.scale[4]), 'center', 'center', true, false, color);
        }

        // The "South" axis labels
        if (axes.indexOf('s') > -1) {
            RGraph.Text(context, font_face, font_size, this.centerx, this.centery + ((r) * 0.2), String(this.scale[0]), 'center', 'center', true, false, color);
            RGraph.Text(context, font_face, font_size, this.centerx, this.centery + ((r) * 0.4), String(this.scale[1]), 'center', 'center', true, false, color);
            RGraph.Text(context, font_face, font_size, this.centerx, this.centery + ((r) * 0.6), String(this.scale[2]), 'center', 'center', true, false, color);
            RGraph.Text(context, font_face, font_size, this.centerx, this.centery + ((r) * 0.8), String(this.scale[3]), 'center', 'center', true, false, color);
            RGraph.Text(context, font_face, font_size, this.centerx, this.centery + r, String(this.scale[4]), 'center', 'center', true, false, color);
        }
        
        // The "East" axis labels
        if (axes.indexOf('e') > -1) {
            RGraph.Text(context, font_face, font_size, this.centerx + ((r) * 0.2), this.centery, String(this.scale[0]), 'center', 'center', true, false, color);
            RGraph.Text(context, font_face, font_size, this.centerx + ((r) * 0.4), this.centery, String(this.scale[1]), 'center', 'center', true, false, color);
            RGraph.Text(context, font_face, font_size, this.centerx + ((r) * 0.6), this.centery, String(this.scale[2]), 'center', 'center', true, false, color);
            RGraph.Text(context, font_face, font_size, this.centerx + ((r) * 0.8), this.centery, String(this.scale[3]), 'center', 'center', true, false, color);
            RGraph.Text(context, font_face, font_size, this.centerx + r, this.centery, String(this.scale[4]), 'center', 'center', true, false, color);
        }

        // The "West" axis labels
        if (axes.indexOf('w') > -1) {
            RGraph.Text(context, font_face, font_size, this.centerx - ((r) * 0.2), this.centery, String(this.scale[0]), 'center', 'center', true, false, color);
            RGraph.Text(context, font_face, font_size, this.centerx - ((r) * 0.4), this.centery, String(this.scale[1]), 'center', 'center', true, false, color);
            RGraph.Text(context, font_face, font_size, this.centerx - ((r) * 0.6), this.centery, String(this.scale[2]), 'center', 'center', true, false, color);
            RGraph.Text(context, font_face, font_size, this.centerx - ((r) * 0.8), this.centery, String(this.scale[3]), 'center', 'center', true, false, color);
            RGraph.Text(context, font_face, font_size, this.centerx - r, this.centery, String(this.scale[4]), 'center', 'center', true, false, color);
        }

        /**
        * Draw the key
        */
        RGraph.Text(context, font_face, font_size, this.centerx,  this.centery, '0', 'center', 'center', true, false, color);
        if (key && key.length) {
            RGraph.DrawKey(this, key, this.Get('chart.colors'));
        }
    }


    /**
    * Draws the circular labels that go around the charts
    * 
    * @param labels array The labels that go around the chart
    */
    RGraph.Rscatter.prototype.DrawCircularLabels = function (context, labels, font_face, font_size, r)
    {
        var position = this.Get('chart.labels.position');
        var r        = r + 10;

        for (var i=0; i<labels.length; ++i) {


            var a = (360 / labels.length) * (i + 1) - (360 / (labels.length * 2));
            var a = a - 90 + (this.Get('chart.labels.position') == 'edge' ? ((360 / labels.length) / 2) : 0);

            var x = Math.cos(a / 57.29577866666) * (r + 10);
            var y = Math.sin(a / 57.29577866666) * (r + 10);

            RGraph.Text(context, font_face, font_size, this.centerx + x, this.centery + y, String(labels[i]), 'center', 'center');
        }
    }


    /**
    * Draws a single tickmark
    */
    RGraph.Rscatter.prototype.DrawTick = function (x, y, color)
    {
        var tickmarks    = this.Get('chart.tickmarks');
        var ticksize     = this.Get('chart.ticksize');

        this.context.strokeStyle = color;
        this.context.fillStyle   = color;

        // Cross
        if (tickmarks == 'cross') {

            this.context.beginPath();
            this.context.moveTo(x + ticksize, y + ticksize);
            this.context.lineTo(x - ticksize, y - ticksize);
            this.context.stroke();
    
            this.context.beginPath();
            this.context.moveTo(x - ticksize, y + ticksize);
            this.context.lineTo(x + ticksize, y - ticksize);
            this.context.stroke();
        
        // Circle
        } else if (tickmarks == 'circle') {

            this.context.beginPath();
            this.context.arc(x, y, ticksize, 0, 6.2830, false);
            this.context.fill();

        // Square
        } else if (tickmarks == 'square') {

            this.context.beginPath();
            this.context.fillRect(x - ticksize, y - ticksize, 2 * ticksize, 2 * ticksize);
            this.context.fill();
        
        // Diamond shape tickmarks
         } else if (tickmarks == 'diamond') {

            this.context.beginPath();
                this.context.moveTo(x, y - ticksize);
                this.context.lineTo(x + ticksize, y);
                this.context.lineTo(x, y + ticksize);
                this.context.lineTo(x - ticksize, y);
            this.context.closePath();
            this.context.fill();

        // Plus style tickmarks
        } else if (tickmarks == 'plus') {
        
            this.context.lineWidth = 1;

            this.context.beginPath();
                this.context.moveTo(x, y - ticksize);
                this.context.lineTo(x, y + ticksize);
                this.context.moveTo(x - ticksize, y);
                this.context.lineTo(x + ticksize, y);
            this.context.stroke();
        }
    }
