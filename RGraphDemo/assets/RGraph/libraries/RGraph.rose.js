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
    * The rose chart constuctor
    * 
    * @param object canvas
    * @param array data
    */
    RGraph.Rose = function (id, data)
    {
        this.id                = id;
        this.canvas            = document.getElementById(id);
        this.context           = this.canvas.getContext('2d');
        this.data              = data;
        this.canvas.__object__ = this;
        this.type              = 'rose';
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
            'chart.colors':                 ['rgb(255,0,0)', 'rgb(0,255,255)', 'rgb(0,255,0)', 'rgb(127,127,127)', 'rgb(0,0,255)', 'rgb(255,128,255)'],
            'chart.colors.alpha':           null,
            'chart.strokestyle':            'black',
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
            'chart.resizable':              false,
            'chart.adjustable':             false,
            'chart.ymax':                   null
        }
        
        // Check the common library has been included
        if (typeof(RGraph) == 'undefined') {
            alert('[ROSE] Fatal error: The common library does not appear to have been included');
        }
    }


    /**
    * A simple setter
    * 
    * @param string name  The name of the property to set
    * @param string value The value of the property
    */
    RGraph.Rose.prototype.Set = function (name, value)
    {
        this.properties[name.toLowerCase()] = value;
    }
    
    
    /**
    * A simple getter
    * 
    * @param string name The name of the property to get
    */
    RGraph.Rose.prototype.Get = function (name)
    {
        return this.properties[name.toLowerCase()];
    }

    
    /**
    * This method draws the rose chart
    */
    RGraph.Rose.prototype.Draw = function ()
    {
        /**
        * Fire the onbeforedraw event
        */
        RGraph.FireCustomEvent(this, 'onbeforedraw');

        // Calculate the radius
        this.radius       = (Math.min(this.canvas.width, this.canvas.height) / 2);
        this.centerx      = this.canvas.width / 2;
        this.centery      = this.canvas.height / 2;
        this.angles       = [];
        this.total        = 0;
        this.startRadians = 0;
        
        /**
        * Change the centerx marginally if the key is defined
        */
        if (this.Get('chart.key') && this.Get('chart.key').length > 0 && this.Get('chart.key').length >= 3) {
            this.centerx = this.centerx - this.Get('chart.gutter') + 5;
        }

        this.DrawBackground();
        this.DrawRose();
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
        if (this.Get('chart.tooltips')) {

            /**
            * Register this object for redrawing
            */
            RGraph.Register(this);
        
            /**
            * The onclick event
            */
            this.canvas.onclick = function (e)
            {
                var obj     = e.target.__object__;
                var canvas  = e.target;
                var context = canvas.getContext('2d');

                e = RGraph.FixEventObject(e);

                RGraph.Redraw();
                
                var segment = RGraph.getSegment(e);
                if (segment && obj.Get('chart.tooltips')[segment[5]]) {
                    context.beginPath();
                        context.strokeStyle = 'black';
                        context.fillStyle = 'rgba(255,255,255,0.5)';
                        context.arc(segment[0], segment[1], segment[2], segment[3] / 57.3, segment[4] / 57.3, false);
                        context.lineTo(obj.centerx, obj.centery);
                    context.closePath();
                    context.fill();
                    context.stroke();
                    
                    context.strokeStyle = 'rgba(0,0,0,0)';
                    obj.DrawLabels();
                    
                    /**
                    * Show the tooltip
                    */
                    RGraph.Tooltip(canvas, obj.Get('chart.tooltips')[segment[5]], e.pageX, e.pageY, segment[5]);

                    e.stopPropagation();

                    return;
                }
            }
            
            
            /**
            * The onmousemove event
            */
            this.canvas.onmousemove = function (e)
            {
                var obj     = e.target.__object__;
                var canvas  = e.target;
                var context = canvas.getContext('2d');

                e = RGraph.FixEventObject(e);
                
                var segment = RGraph.getSegment(e);

                if (segment && obj.Get('chart.tooltips')[segment[5]]) {
                    canvas.style.cursor = 'pointer';
                    return;
                }

                canvas.style.cursor = 'default';
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
        * This function enables adjusting
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
    * This method draws the rose charts background
    */
    RGraph.Rose.prototype.DrawBackground = function ()
    {
        this.context.lineWidth = 1;
    
        // Draw the background grey circles
        this.context.strokeStyle = '#ccc';
        for (var i=15; i<this.radius - this.Get('chart.gutter') - (document.all ? 5 : 0); i+=15) {// Radius must be greater than 0 for Opera to work
            //this.context.moveTo(this.centerx + i, this.centery);
    
            // Radius must be greater than 0 for Opera to work
            this.context.arc(this.centerx, this.centery, i, 0, (2 * Math.PI), 0);
        }
        this.context.stroke();

        // Draw the background lines that go from the center outwards
        this.context.beginPath();
        for (var i=15; i<360; i+=15) {
        
            // Radius must be greater than 0 for Opera to work
            this.context.arc(this.centerx, this.centery, this.radius - this.Get('chart.gutter'), i / 57.3, (i + 0.1) / 57.3, 0); // The 0.01 avoids a bug in Chrome 6
        
            this.context.lineTo(this.centerx, this.centery);
        }
        this.context.stroke();
        
        this.context.beginPath();
        this.context.strokeStyle = 'black';
    
        // Draw the X axis
        this.context.moveTo(this.centerx - this.radius + this.Get('chart.gutter'), this.centery);
        this.context.lineTo(this.centerx + this.radius - this.Get('chart.gutter'), this.centery);
    
        // Draw the X ends
        this.context.moveTo(this.centerx - this.radius + this.Get('chart.gutter'), this.centery - 5);
        this.context.lineTo(this.centerx - this.radius + this.Get('chart.gutter'), this.centery + 5);
        this.context.moveTo(this.centerx + this.radius - this.Get('chart.gutter'), this.centery - 5);
        this.context.lineTo(this.centerx + this.radius - this.Get('chart.gutter'), this.centery + 5);
        
        // Draw the X check marks
        for (var i=(this.centerx - this.radius + this.Get('chart.gutter')); i<(this.centerx + this.radius - this.Get('chart.gutter')); i+=20) {
            this.context.moveTo(i,  this.centery - 3);
            this.context.lineTo(i,  this.centery + 3);
        }
        
        // Draw the Y check marks
        for (var i=(this.centery - this.radius + this.Get('chart.gutter')); i<(this.centery + this.radius - this.Get('chart.gutter')); i+=20) {
            this.context.moveTo(this.centerx - 3, i);
            this.context.lineTo(this.centerx + 3, i);
        }
    
        // Draw the Y axis
        this.context.moveTo(this.centerx, this.centery - this.radius + this.Get('chart.gutter'));
        this.context.lineTo(this.centerx, this.centery + this.radius - this.Get('chart.gutter'));
    
        // Draw the Y ends
        this.context.moveTo(this.centerx - 5, this.centery - this.radius + this.Get('chart.gutter'));
        this.context.lineTo(this.centerx + 5, this.centery - this.radius + this.Get('chart.gutter'));
    
        this.context.moveTo(this.centerx - 5, this.centery + this.radius - this.Get('chart.gutter'));
        this.context.lineTo(this.centerx + 5, this.centery + this.radius - this.Get('chart.gutter'));
        
        // Stroke it
        this.context.closePath();
        this.context.stroke();
    }


    /**
    * This method draws a set of data on the graph
    */
    RGraph.Rose.prototype.DrawRose = function ()
    {
        var data = this.data;

        // Must be at least two data points
        if (data.length < 2) {
            alert('[ROSE] Must be at least two data points! [' + data + ']');
            return;
        }
    
        // Work out the maximum value and the sum
        if (!this.Get('chart.ymax')) {
            this.scale = RGraph.getScale(RGraph.array_max(data));
            this.max = this.scale[4];
        } else {
            var ymax = this.Get('chart.ymax');

            this.scale = [
                          ymax * 0.2,
                          ymax * 0.4,
                          ymax * 0.6,
                          ymax * 0.8,
                          ymax * 1
                         ];
            this.max = this.scale[4];
        }
        
        this.sum = RGraph.array_sum(data);
        
        // Move to the centre
        this.context.moveTo(this.centerx, this.centery);
    
        this.context.stroke(); // Stroke the background so it stays grey
    
        // Transparency
        if (this.Get('chart.colors.alpha')) {
            this.context.globalAlpha = this.Get('chart.colors.alpha');
        }

        for (var i=0; i<this.data.length; ++i) {

            this.context.strokeStyle = this.Get('chart.strokestyle');
            
            if (this.Get('chart.colors')[i]) {
                this.context.fillStyle = this.Get('chart.colors')[i];
            }
    
            var segmentRadians = (1 / this.data.length) * (2 * Math.PI);
    
            this.context.beginPath(); // Begin the segment   
                var radius = (this.data[i] / this.max) * (this.radius - this.Get('chart.gutter') - 10);
                this.context.arc(this.centerx, this.centery, radius, this.startRadians - (Math.PI / 2), this.startRadians + segmentRadians - (Math.PI / 2), 0);
                this.context.lineTo(this.centerx, this.centery);
                this.context.fill();
            this.context.closePath(); // End the segment
            
            // Store the start and end angles
            this.angles.push([
                              ((this.startRadians - (Math.PI / 2)) * 57.3) + 90,
                              (((this.startRadians + segmentRadians) - (Math.PI / 2)) * 57.3) + 90,
                              radius
                             ]);

            this.startRadians += segmentRadians;
            this.context.stroke();
        }

        // Turn off the transparency
        if (this.Get('chart.colors.alpha')) {
            this.context.globalAlpha = 1;
        }

        // Draw the title if any has been set
        if (this.Get('chart.title')) {
            RGraph.DrawTitle(this.canvas, this.Get('chart.title'), this.Get('chart.gutter'), this.centerx, this.Get('chart.text.size') + 2);
        }
    }


    /**
    * Unsuprisingly, draws the labels
    */
    RGraph.Rose.prototype.DrawLabels = function ()
    {
        this.context.lineWidth = 1;
        var key = this.Get('chart.key');

        if (key && key.length) {
            RGraph.DrawKey(this, key, this.Get('chart.colors'));
        }
        
        // Set the color to black
        this.context.fillStyle = 'black';
        this.context.strokeStyle = 'black';
        
        var r         = this.radius - 10;
        var font_face = this.Get('chart.text.font');
        var font_size = this.Get('chart.text.size');
        var context   = this.context;
        var axes      = this.Get('chart.labels.axes').toLowerCase();

        // Draw any labels

        if (typeof(this.Get('chart.labels')) == 'object' && this.Get('chart.labels')) {
            this.DrawCircularLabels(context, this.Get('chart.labels'), font_face, font_size, r + 10);
        }


        var color = 'rgba(255,255,255,0.8)';

        // The "North" axis labels
        if (axes.indexOf('n') > -1) {
            RGraph.Text(context, font_face, font_size, this.centerx, this.centery - ((r - this.Get('chart.gutter')) * 0.2), String(this.scale[0]), 'center', 'center', true, false, color);
            RGraph.Text(context, font_face, font_size, this.centerx, this.centery - ((r - this.Get('chart.gutter')) * 0.4), String(this.scale[1]), 'center', 'center', true, false, color);
            RGraph.Text(context, font_face, font_size, this.centerx, this.centery - ((r - this.Get('chart.gutter')) * 0.6), String(this.scale[2]), 'center', 'center', true, false, color);
            RGraph.Text(context, font_face, font_size, this.centerx, this.centery - ((r - this.Get('chart.gutter')) * 0.8), String(this.scale[3]), 'center', 'center', true, false, color);
            RGraph.Text(context, font_face, font_size, this.centerx, this.centery - r + this.Get('chart.gutter'), String(this.scale[4]), 'center', 'center', true, false, color);
        }

        // The "South" axis labels
        if (axes.indexOf('s') > -1) {
            RGraph.Text(context, font_face, font_size, this.centerx, this.centery + ((r - this.Get('chart.gutter')) * 0.2), String(this.scale[0]), 'center', 'center', true, false, color);
            RGraph.Text(context, font_face, font_size, this.centerx, this.centery + ((r - this.Get('chart.gutter')) * 0.4), String(this.scale[1]), 'center', 'center', true, false, color);
            RGraph.Text(context, font_face, font_size, this.centerx, this.centery + ((r - this.Get('chart.gutter')) * 0.6), String(this.scale[2]), 'center', 'center', true, false, color);
            RGraph.Text(context, font_face, font_size, this.centerx, this.centery + ((r - this.Get('chart.gutter')) * 0.8), String(this.scale[3]), 'center', 'center', true, false, color);
            RGraph.Text(context, font_face, font_size, this.centerx, this.centery + r - this.Get('chart.gutter'), String(this.scale[4]), 'center', 'center', true, false, color);
        }
        
        // The "East" axis labels
        if (axes.indexOf('e') > -1) {
            RGraph.Text(context, font_face, font_size, this.centerx + ((r - this.Get('chart.gutter')) * 0.2), this.centery, String(this.scale[0]), 'center', 'center', true, false, color);
            RGraph.Text(context, font_face, font_size, this.centerx + ((r - this.Get('chart.gutter')) * 0.4), this.centery, String(this.scale[1]), 'center', 'center', true, false, color);
            RGraph.Text(context, font_face, font_size, this.centerx + ((r - this.Get('chart.gutter')) * 0.6), this.centery, String(this.scale[2]), 'center', 'center', true, false, color);
            RGraph.Text(context, font_face, font_size, this.centerx + ((r - this.Get('chart.gutter')) * 0.8), this.centery, String(this.scale[3]), 'center', 'center', true, false, color);
            RGraph.Text(context, font_face, font_size, this.centerx + r - this.Get('chart.gutter'), this.centery, String(this.scale[4]), 'center', 'center', true, false, color);
        }

        // The "West" axis labels
        if (axes.indexOf('w') > -1) {
            RGraph.Text(context, font_face, font_size, this.centerx - ((r - this.Get('chart.gutter')) * 0.2), this.centery, String(this.scale[0]), 'center', 'center', true, false, color);
            RGraph.Text(context, font_face, font_size, this.centerx - ((r - this.Get('chart.gutter')) * 0.4), this.centery, String(this.scale[1]), 'center', 'center', true, false, color);
            RGraph.Text(context, font_face, font_size, this.centerx - ((r - this.Get('chart.gutter')) * 0.6), this.centery, String(this.scale[2]), 'center', 'center', true, false, color);
            RGraph.Text(context, font_face, font_size, this.centerx - ((r - this.Get('chart.gutter')) * 0.8), this.centery, String(this.scale[3]), 'center', 'center', true, false, color);
            RGraph.Text(context, font_face, font_size, this.centerx - r + this.Get('chart.gutter'), this.centery, String(this.scale[4]), 'center', 'center', true, false, color);
        }

        RGraph.Text(context, font_face, font_size, this.centerx,  this.centery, '0', 'center', 'center', true, false, color);
    }


    /**
    * Draws the circular labels that go around the charts
    * 
    * @param labels array The labels that go around the chart
    */
    RGraph.Rose.prototype.DrawCircularLabels = function (context, labels, font_face, font_size, r)
    {
        var position = this.Get('chart.labels.position');
        var r        = r - this.Get('chart.gutter') + 10;

        for (var i=0; i<labels.length; ++i) {


            var a = (360 / labels.length) * (i + 1) - (360 / (labels.length * 2));
            var a = a - 90 + (this.Get('chart.labels.position') == 'edge' ? ((360 / labels.length) / 2) : 0);

            var x = Math.cos(a / 57.29577866666) * (r + 10);
            var y = Math.sin(a / 57.29577866666) * (r + 10);

            RGraph.Text(context, font_face, font_size, this.centerx + x, this.centery + y, String(labels[i]), 'center', 'center');
        }
    }