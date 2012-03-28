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
    * The pie chart constructor
    * 
    * @param data array The data to be represented on the pie chart
    */
    RGraph.Pie = function (id, data)
    {
        this.id                = id;
        this.canvas            = document.getElementById(id);
        this.context           = this.canvas.getContext("2d");
        this.canvas.__object__ = this;
        this.total             = 0;
        this.subTotal          = 0;
        this.angles            = [];
        this.data              = data;
        this.properties        = [];
        this.type              = 'pie';
        this.isRGraph          = true;


        /**
        * Compatibility with older browsers
        */
        RGraph.OldBrowserCompat(this.context);

        this.properties = {
            'chart.colors':                 ['rgb(255,0,0)', '#ddd', 'rgb(0,255,0)', 'rgb(0,0,255)', 'pink', 'yellow', 'red', 'rgb(0,255,255)', 'black', 'white'],
            'chart.strokestyle':            '#999',
            'chart.linewidth':              1,
            'chart.labels':                 [],
            'chart.labels.sticks':          false,
            'chart.labels.sticks.color':    '#aaa',
            'chart.segments':               [],
            'chart.gutter':                 25,
            'chart.title':                  '',
            'chart.title.hpos':             null,
            'chart.title.vpos':             null,
            'chart.shadow':                 false,
            'chart.shadow.color':           'rgba(0,0,0,0.5)',
            'chart.shadow.offsetx':         3,
            'chart.shadow.offsety':         3,
            'chart.shadow.blur':            3,
            'chart.text.size':              10,
            'chart.text.color':             'black',
            'chart.text.font':              'Verdana',
            'chart.contextmenu':            null,
            'chart.tooltips':               [],
            'chart.tooltips.effect':         'fade',
            'chart.tooltips.css.class':      'RGraph_tooltip',
            'chart.radius':                 null,
            'chart.highlight.style':        '3d',
            'chart.border':                 false,
            'chart.border.color':           'rgba(255,255,255,0.5)',
            'chart.key.background':         'white',
            'chart.key.position':           'graph',
            'chart.annotatable':            false,
            'chart.annotate.color':         'black',
            'chart.align':                  'center',
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
            'chart.variant':                'pie'
        }
        
        /**
        * Calculate the total
        */
        for (var i=0,len=data.length; i<len; i++) {
            this.total += data[i];
        }
        
        // Check the common library has been included
        if (typeof(RGraph) == 'undefined') {
            alert('[PIE] Fatal error: The common library does not appear to have been included');
        }
    }


    /**
    * A generic setter
    */
    RGraph.Pie.prototype.Set = function (name, value)
    {
        this.properties[name] = value;
    }


    /**
    * A generic getter
    */
    RGraph.Pie.prototype.Get = function (name)
    {
        return this.properties[name];
    }


    /**
    * This draws the pie chart
    */
    RGraph.Pie.prototype.Draw = function ()
    {
        /**
        * Fire the onbeforedraw event
        */
        RGraph.FireCustomEvent(this, 'onbeforedraw');

        this.diameter    = Math.min(this.canvas.height, this.canvas.width) - (2 * this.Get('chart.gutter'));
        this.radius      = this.Get('chart.radius') ? this.Get('chart.radius') : this.diameter / 2;
        // this.centerx now defined below
        this.centery     = this.canvas.height / 2;
        this.subTotal    = 0;
        this.angles      = [];
        
        /**
        * Alignment (Pie is center aligned by default) Only if centerx is not defined - donut defines the centerx
        */
        if (this.Get('chart.align') == 'left') {
            this.centerx = this.radius + this.Get('chart.gutter');
        
        } else if (this.Get('chart.align') == 'right') {
            this.centerx = this.canvas.width - (this.radius + this.Get('chart.gutter'));
        
        } else {
            this.centerx = this.canvas.width / 2;
        }

        /**
        * Draw the shadow if required
        */
        if (this.Get('chart.shadow')) {
        
            var offsetx = document.all ? this.Get('chart.shadow.offsetx') : 0;
            var offsety = document.all ? this.Get('chart.shadow.offsety') : 0;

            this.context.beginPath();
            this.context.fillStyle = this.Get('chart.shadow.color');

            this.context.shadowColor   = this.Get('chart.shadow.color');
            this.context.shadowBlur    = this.Get('chart.shadow.blur');
            this.context.shadowOffsetX = this.Get('chart.shadow.offsetx');
            this.context.shadowOffsetY = this.Get('chart.shadow.offsety');
            
            this.context.arc(this.centerx + offsetx, this.centery + offsety, this.radius, 0, 6.28, 0);
            
            this.context.fill();
            
            // Now turn off the shadow
            RGraph.NoShadow(this);
        }

        /**
        * The total of the array of values
        */
        this.total = RGraph.array_sum(this.data);

        for (var i=0,len=this.data.length; i<len; i++) {
            var angle = (this.data[i] / this.total) * 360;
    
            this.DrawSegment(angle,
                             this.Get('chart.colors')[i],
                             i == (this.data.length - 1));
        }

        /**
        * Redraw the seperating lines
        */
        if (this.Get('chart.linewidth') > 0) {
            this.context.beginPath();
            this.context.lineWidth = this.Get('chart.linewidth');
            this.context.strokeStyle = this.Get('chart.strokestyle');

            for (var i=0,len=this.angles.length; i<len; ++i) {
                this.context.moveTo(this.centerx, this.centery);
                this.context.arc(this.centerx, this.centery, this.radius, this.angles[i][0] / 57.3, (this.angles[i][0] + 0.01) / 57.3, 0);
            }
            
            this.context.stroke();
            
            /**
            * And finally redraw the border
            */
            this.context.beginPath();
            this.context.moveTo(this.centerx, this.centery);
            this.context.arc(this.centerx, this.centery, this.radius, 0, 6.28, 0);
            this.context.stroke();
        }

        /**
        * Draw label sticks
        */
        if (this.Get('chart.labels.sticks')) {
            this.DrawSticks();
            
            // Redraw the border going around the Pie chart if the stroke style is NOT white
            if (
                   this.Get('chart.strokestyle') != 'white'
                && this.Get('chart.strokestyle') != '#fff'
                && this.Get('chart.strokestyle') != '#fffffff'
                && this.Get('chart.strokestyle') != 'rgb(255,255,255)'
                && this.Get('chart.strokestyle') != 'rgba(255,255,255,0)'
               ) {

                this.context.beginPath();
                    this.context.strokeStyle = this.Get('chart.strokestyle');
                    this.context.lineWidth = this.Get('chart.linewidth');
                    this.context.arc(this.centerx, this.centery, this.radius, 0, 6.28, false);
                this.context.stroke();
            }
        }

        /**
        * Draw the labels
        */
        this.DrawLabels();

        /**
        * Draw the title
        */
        if (this.Get('chart.align') == 'left') {
            var centerx = this.radius + this.Get('chart.gutter');

        } else if (this.Get('chart.align') == 'right') {
            var centerx = this.canvas.width - (this.radius + this.Get('chart.gutter'));

        } else {
            var centerx = null;
        }

        RGraph.DrawTitle(this.canvas, this.Get('chart.title'), this.Get('chart.gutter'), centerx, this.Get('chart.text.size') + 2);
        
        
        /**
        * Setup the context menu if required
        */
        if (this.Get('chart.contextmenu')) {
            RGraph.ShowContext(this);
        }

        /**
        * Tooltips
        */
        if (this.Get('chart.tooltips').length) {

            /**
            * Register this object for redrawing
            */
            RGraph.Register(this);
        
            /**
            * The onclick event
            */
            this.canvas.onclick = function (e)
            {
                RGraph.HideZoomedCanvas();

                e = RGraph.FixEventObject(e);

                var mouseCoords = RGraph.getMouseXY(e);

                var canvas  = e.target;
                var context = canvas.getContext('2d');
                var obj     = e.target.__object__;

                var x       = mouseCoords[0] - obj.centerx;
                var y       = mouseCoords[1] - obj.centery;
                var theta   = Math.atan(y / x); // RADIANS
                var hyp     = y / Math.sin(theta);


                RGraph.Redraw();


                /**
                * If it's actually a donut make sure the hyp is bigger
                * than the size of the hole in the middle
                */
                if (obj.Get('chart.variant') == 'donut' && Math.abs(hyp) < (obj.radius / 2)) {
                    return;
                }

                /**
                * The angles for each segment are stored in "angles",
                * so go through that checking if the mouse position corresponds
                */
                var isDonut = obj.Get('chart.variant') == 'donut';
                var hStyle  = obj.Get('chart.highlight.style');
                var segment = RGraph.getSegment(e);

                if (segment) {
                    if (isDonut || hStyle == '2d') {
                        
                        context.beginPath();

                        context.fillStyle = 'rgba(255,255,255,0.5)';

                        context.moveTo(obj.centerx, obj.centery);
                        context.arc(obj.centerx, obj.centery, obj.radius, RGraph.degrees2Radians(obj.angles[segment[5]][0]), RGraph.degrees2Radians(obj.angles[segment[5]][1]), 0);
                        context.lineTo(obj.centerx, obj.centery);
                        context.closePath();
                        
                        context.fill();
                        context.stroke();

                    } else {

                        context.lineWidth = 2;

                        /**
                        * Draw a white segment where the one that has been clicked on was
                        */
                        context.fillStyle = 'white';
                        context.strokeStyle = 'white';
                        context.beginPath();
                        context.moveTo(obj.centerx, obj.centery);
                        context.arc(obj.centerx, obj.centery, obj.radius, obj.angles[segment[5]][0] / 57.3, obj.angles[segment[5]][1] / 57.3, 0);
                        context.stroke();
                        context.fill();
                        
                        context.lineWidth = 1;

                        context.shadowColor   = '#666';
                        context.shadowBlur    = 3;
                        context.shadowOffsetX = 3;
                        context.shadowOffsetY = 3;

                        // Draw the new segment
                        context.beginPath();
                            context.fillStyle   = obj.Get('chart.colors')[segment[5]];
                            context.strokeStyle = 'rgba(0,0,0,0)';
                            context.moveTo(obj.centerx - 3, obj.centery - 3);
                            context.arc(obj.centerx - 3, obj.centery - 3, obj.radius, RGraph.degrees2Radians(obj.angles[segment[5]][0]), RGraph.degrees2Radians(obj.angles[segment[5]][1]), 0);
                            context.lineTo(obj.centerx - 3, obj.centery - 3);
                        context.closePath();
                        
                        context.stroke();
                        context.fill();
                        
                        // Turn off the shadow
                        RGraph.NoShadow(obj);
                        
                        /**
                        * If a border is defined, redraw that
                        */
                        if (obj.Get('chart.border')) {
                            context.beginPath();
                            context.strokeStyle = obj.Get('chart.border.color');
                            context.lineWidth = 5;
                            context.arc(obj.centerx - 3, obj.centery - 3, obj.radius - 2, RGraph.degrees2Radians(obj.angles[i][0]), RGraph.degrees2Radians(obj.angles[i][1]), 0);
                            context.stroke();
                        }
                    }
                        
                    /**
                    * If a tooltip is defined, show it
                    */

                    /**
                    * Get the tooltip text
                    */
                    if (typeof(obj.Get('chart.tooltips')) == 'function') {
                        var text = String(obj.Get('chart.tooltips')(segment[5]));

                    } else if (typeof(obj.Get('chart.tooltips')) == 'object' && typeof(obj.Get('chart.tooltips')[segment[5]]) == 'function') {
                        var text = String(obj.Get('chart.tooltips')[segment[5]](segment[5]));
                    
                    } else if (typeof(obj.Get('chart.tooltips')) == 'object') {
                        var text = String(obj.Get('chart.tooltips')[segment[5]]);

                    } else {
                        var text = '';
                    }

                    if (text) {
                        RGraph.Tooltip(canvas, text, e.pageX, e.pageY, segment[5]);
                    }

                    /**
                    * Need to redraw the key?
                    */
                    if (obj.Get('chart.key') && obj.Get('chart.key').length && obj.Get('chart.key.position') == 'graph') {
                        RGraph.DrawKey(obj, obj.Get('chart.key'), obj.Get('chart.colors'));
                    }

                    e.stopPropagation();

                    return;
                }
            }








            /**
            * The onmousemove event for changing the cursor
            */
            this.canvas.onmousemove = function (e)
            {
                RGraph.HideZoomedCanvas();

                e = RGraph.FixEventObject(e);
                
                var segment = RGraph.getSegment(e);

                if (segment) {
                    e.target.style.cursor = 'pointer';

                    return;
                }

                /**
                * Put the cursor back to null
                */
                e.target.style.cursor = 'default';
            }
        }


        /**
        * If a border is pecified, draw it
        */
        if (this.Get('chart.border')) {
            this.context.beginPath();
            this.context.lineWidth = 5;
            this.context.strokeStyle = this.Get('chart.border.color');

            this.context.arc(this.centerx,
                             this.centery,
                             this.radius - 2,
                             0,
                             6.28,
                             0);

            this.context.stroke();
        }
        
        /**
        * Draw the kay if desired
        */
        if (this.Get('chart.key') != null) {
            //this.Set('chart.key.position', 'graph');
            RGraph.DrawKey(this, this.Get('chart.key'), this.Get('chart.colors'));
        }


        /**
        * If this is actually a donut, draw a big circle in the middle
        */
        if (this.Get('chart.variant') == 'donut') {
            this.context.beginPath();
            this.context.strokeStyle = this.Get('chart.strokestyle');
            this.context.fillStyle   = 'white';//this.Get('chart.fillstyle');
            this.context.arc(this.centerx, this.centery, this.radius / 2, 0, 6.28, 0);
            this.context.stroke();
            this.context.fill();
        }
        
        RGraph.NoShadow(this);
        
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
    * Draws a single segment of the pie chart
    * 
    * @param int degrees The number of degrees for this segment
    */
    RGraph.Pie.prototype.DrawSegment = function (degrees, color, last)
    {
        var context  = this.context;
        var canvas   = this.canvas;
        var subTotal = this.subTotal;

        context.beginPath();

            context.fillStyle   = color;
            context.strokeStyle = this.Get('chart.strokestyle');
            context.lineWidth   = 0;

            context.arc(this.centerx,
                        this.centery,
                        this.radius,
                        subTotal / 57.3,
                        (last ? 360 : subTotal + degrees) / 57.3,
                        0);
    
            context.lineTo(this.centerx, this.centery);
            
            // Keep hold of the angles
            this.angles.push([subTotal, subTotal + degrees])
        this.context.closePath();

        this.context.fill();
    
        /**
        * Calculate the segment angle
        */
        this.Get('chart.segments').push([subTotal, subTotal + degrees]);
        this.subTotal += degrees;
    }

    /**
    * Draws the graphs labels
    */
    RGraph.Pie.prototype.DrawLabels = function ()
    {
        var hAlignment = 'left';
        var vAlignment = 'center';
        var labels     = this.Get('chart.labels');
        var context    = this.context;

        /**
        * Turn the shadow off
        */
        RGraph.NoShadow(this);
        
        context.fillStyle = 'black';
        context.beginPath();

        /**
        * Draw the key (ie. the labels)
        */
        if (labels && labels.length) {

            var text_size = this.Get('chart.text.size');

            for (i=0; i<labels.length; ++i) {
            
                /**
                * T|his ensures that if we're given too many labels, that we don't get an error
                */
                if (typeof(this.Get('chart.segments')[i]) == 'undefined') {
                    continue;
                }

                // Move to the centre
                context.moveTo(this.centerx,this.centery);
                
                var a = this.Get('chart.segments')[i][0] + ((this.Get('chart.segments')[i][1] - this.Get('chart.segments')[i][0]) / 2);

                /**
                * Alignment
                */
                if (a < 90) {
                    hAlignment = 'left';
                    vAlignment = 'center';
                } else if (a < 180) {
                    hAlignment = 'right';
                    vAlignment = 'center';
                } else if (a < 270) {
                    hAlignment = 'right';
                    vAlignment = 'center';
                } else if (a < 360) {
                    hAlignment = 'left';
                    vAlignment = 'center';
                }

                context.fillStyle = this.Get('chart.text.color');

                RGraph.Text(context,
                            this.Get('chart.text.font'),
                            text_size,
                            this.centerx + ((this.radius + 10)* Math.cos(a / 57.3)) + (this.Get('chart.labels.sticks') ? (a < 90 || a > 270 ? 2 : -2) : 0),
                            this.centery + (((this.radius + 10) * Math.sin(a / 57.3))),
                            labels[i],
                            vAlignment,
                            hAlignment);
            }
            
            context.fill();
        }
    }


    /**
    * This function draws the pie chart sticks (for the labels)
    */
    RGraph.Pie.prototype.DrawSticks = function ()
    {
        var context  = this.context;
        var segments = this.Get('chart.segments');
        var offset   = this.Get('chart.linewidth') / 2;

        for (var i=0; i<segments.length; ++i) {

            var degrees = segments[i][1] - segments[i][0];

            context.beginPath();
            context.strokeStyle = this.Get('chart.labels.sticks.color');
            context.lineWidth   = 1;

            var midpoint = (segments[i][0] + (degrees / 2)) / 57.3;

            context.arc(this.centerx,
                        this.centery,
                        this.radius + 7,
                        midpoint,
                        midpoint,
                        0);
            
            
            context.arc(this.centerx,
                        this.centery,
                        this.radius - offset,
                        midpoint,
                        midpoint,
                        0);

            context.stroke();
        }
    }