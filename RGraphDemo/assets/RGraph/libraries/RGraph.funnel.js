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
    * The bar chart constructor
    * 
    * @param object canvas The canvas object
    * @param array  data   The chart data
    */
    RGraph.Funnel = function (id, data)
    {
        // Get the canvas and context objects
        this.id                = id;
        this.canvas            = document.getElementById(id);
        this.context           = this.canvas.getContext ? this.canvas.getContext("2d") : null;
        this.canvas.__object__ = this;
        this.type              = 'funnel';
        this.coords            = [];
        this.isRGraph          = true;


        /**
        * Compatibility with older browsers
        */
        RGraph.OldBrowserCompat(this.context);


        // Check for support
        if (!this.canvas) {
            alert('[FUNNEL] No canvas support');
            return;
        }
        
        // Check the common library has been included
        if (typeof(RGraph) == 'undefined') {
            alert('[FUNNEL] Fatal error: The common library does not appear to have been included');
        }
        
        /**
        * The funnel charts properties
        */
        this.properties = {
            'chart.gutter':                25,
            'chart.labels':                null,
            'chart.title':                 '',
            'chart.title.hpos':             null,
            'chart.title.vpos':            null,
            'chart.colors':                ['red', 'green', 'gray', 'blue', 'black', 'gray'],
            'chart.text.size':             10,
            'chart.text.boxed':            true,
            'chart.text.halign':           'left',
            'chart.text.color':            'black',
            'chart.text.font':             'Verdana',
            'chart.contextmenu':           null,
            'chart.shadow':                false,
            'chart.shadow.color':          '#666',
            'chart.shadow.blur':           3,
            'chart.shadow.offsetx':        3,
            'chart.shadow.offsety':        3,
            'chart.key':                   [],
            'chart.key.position':          'graph',
            'chart.key.background':        'white',
            'chart.key.shadow':            false,
            'chart.tooltips':              null,
            'chart.tooltips.effect':        'fade',
            'chart.tooltips.css.class':     'RGraph_tooltip',
            'chart.annotatable':           false,
            'chart.annotate.color':        'black',
            'chart.zoom.factor':           1.5,
            'chart.zoom.fade.in':          true,
            'chart.zoom.fade.out':         true,
            'chart.zoom.factor':           1.5,
            'chart.zoom.fade.in':          true,
            'chart.zoom.fade.out':         true,
            'chart.zoom.hdir':             'right',
            'chart.zoom.vdir':             'down',
            'chart.zoom.frames':           10,
            'chart.zoom.delay':            50,
            'chart.zoom.shadow':           true,
            'chart.zoom.mode':             'canvas',
            'chart.zoom.thumbnail.width':  75,
            'chart.zoom.thumbnail.height': 75,
            'chart.zoom.background':        true,
            'chart.zoom.action':            'zoom',
            'chart.resizable':              false
        }

        // Store the data
        this.data = data;
    }


    /**
    * A setter
    * 
    * @param name  string The name of the property to set
    * @param value mixed  The value of the property
    */
    RGraph.Funnel.prototype.Set = function (name, value)
    {
        this.properties[name.toLowerCase()] = value;
    }


    /**
    * A getter
    * 
    * @param name  string The name of the property to get
    */
    RGraph.Funnel.prototype.Get = function (name)
    {
        return this.properties[name.toLowerCase()];
    }


    /**
    * The function you call to draw the bar chart
    */
    RGraph.Funnel.prototype.Draw = function ()
    {
        /**
        * Fire the onbeforedraw event
        */
        RGraph.FireCustomEvent(this, 'onbeforedraw');

        // This stops the coords array from growing
        this.coords = [];

        RGraph.DrawTitle(this.canvas, this.Get('chart.title'), this.Get('chart.gutter'), null, this.Get('chart.text.size') + 2);
        this.DrawFunnel();
        
        
        /**
        * Setup the context menu if required
        */
        if (this.Get('chart.contextmenu')) {
            RGraph.ShowContext(this);
        }
        
        /**
        * The tooltip handler
        */
        if (this.Get('chart.tooltips')) {
        
            RGraph.Register(this);

            this.canvas.onclick = function (e)
            {
                RGraph.Redraw();

                var e       = RGraph.FixEventObject(e);
                var canvas  = e.target;
                var context = canvas.getContext('2d');
                var obj     = canvas.__object__;

                var mouseCoords = RGraph.getMouseXY(e);
                var coords = obj.coords;
                var x = mouseCoords[0];
                var y = mouseCoords[1];

                for (i=0; i<coords.length; ++i) {
                    if (
                           x > coords[i][0]
                        && x < coords[i][2]
                        && y > coords[i][1]
                        && y < coords[i][5]
                       ) {
                       
                        /**
                        * Handle the right corner
                        */
                        if (x > coords[i][4]) {
                            var w1 = coords[i][2] - coords[i][4];
                            var h1 = coords[i][5] - coords[i][3];;
                            var a1 = Math.atan(h1 / w1) * 57.3; // DEGREES

                            var w2 = coords[i][2] - mouseCoords[0];
                            var h2 = mouseCoords[1] - coords[i][1];
                            var a2 = Math.atan(h2 / w2) * 57.3; // DEGREES

                            if (a2 > a1) {
                                continue;
                            }
                        
                        /**
                        * Handle the left corner
                        */
                        } else if (x < coords[i][6]) {
                            var w1 = coords[i][6] - coords[i][0];
                            var h1 = coords[i][7] - coords[i][1];;
                            var a1 = Math.atan(h1 / w1) * 57.3; // DEGREES

                            var w2 = mouseCoords[0] - coords[i][0];
                            var h2 = mouseCoords[1] - coords[i][1];
                            var a2 = Math.atan(h2 / w2) * 57.3; // DEGREES
                        
                            if (a2 > a1) {
                                continue;
                            }
                        }

                        // Is there a tooltip defined?
                        if (!obj.Get('chart.tooltips')[i] && typeof(obj.Get('chart.tooltips')) != 'function') {
                            break;
                        }

                        context.beginPath();

                        RGraph.NoShadow(obj);

                        context.fillStyle = 'rgba(255,255,255,0.5)';
                        context.moveTo(coords[i][0], coords[i][1]);
                        context.lineTo(coords[i][2], coords[i][3]);
                        context.lineTo(coords[i][4], coords[i][5]);
                        context.lineTo(coords[i][6], coords[i][7]);
                        context.closePath();

                        context.stroke();
                        context.fill();
                    
                        /**
                        * Draw the key again for in-graph keys so they don't appear "under" the highlight
                        */
                        if (obj.Get('chart.key').length && obj.Get('chart.key.position') == 'graph') {
                            RGraph.DrawKey(obj, obj.Get('chart.key'), obj.Get('chart.colors'));
                        }
                        
                        /**
                        * Redraw the labels if necessary
                        */
                        if (obj.Get('chart.labels')) {
                            obj.DrawLabels();
                        }

                        /**
                        * Get the tooltip text
                        */
                        if (typeof(obj.Get('chart.tooltips')) == 'function') {
                            var text = obj.Get('chart.tooltips')(i);
                        
                        } else if (typeof(obj.Get('chart.tooltips')) == 'object' && typeof(obj.Get('chart.tooltips')[i]) == 'function') {
                            var text = obj.Get('chart.tooltips')[i](i);
                        
                        } else if (typeof(obj.Get('chart.tooltips')) == 'object') {
                            var text = obj.Get('chart.tooltips')[i];

                        } else {
                            var text = '';
                        }

                        // Show the tooltip
                        RGraph.Tooltip(canvas, text, e.pageX, e.pageY, i);
        
                        // Stop the event propagating
                        e.stopPropagation();
                        
                        break;
                    }
                }
            }
            
            /**
            * Onmousemove event handler
            */
            this.canvas.onmousemove = function (e)
            {
                var e = RGraph.FixEventObject(e);

                var canvas = e.target;
                var context = canvas.getContext('2d');
                var obj     = canvas.__object__;
                var overFunnel = false;
                var coords = obj.coords;

                var mouseCoords = RGraph.getMouseXY(e);
                var x = mouseCoords[0];
                var y = mouseCoords[1];

                for (i=0; i<coords.length; ++i) {
                    if (
                           x > coords[i][0]
                        && x < coords[i][2]
                        && y > coords[i][1]
                        && y < coords[i][5]
                       ) {
                       
                        /**
                        * Handle the right corner
                        */
                        if (x > coords[i][4]) {
                            var w1 = coords[i][2] - coords[i][4];
                            var h1 = coords[i][5] - coords[i][3];;
                            var a1 = Math.atan(h1 / w1) * 57.3; // DEGREES

                            var w2 = coords[i][2] - mouseCoords[0];
                            var h2 = mouseCoords[1] - coords[i][1];
                            var a2 = Math.atan(h2 / w2) * 57.3; // DEGREES

                            if (a2 > a1) {
                                continue;
                            }
                        
                        /**
                        * Handle the left corner
                        */
                        } else if (x < coords[i][6]) {
                            var w1 = coords[i][6] - coords[i][0];
                            var h1 = coords[i][7] - coords[i][1];;
                            var a1 = Math.atan(h1 / w1) * 57.3; // DEGREES

                            var w2 = mouseCoords[0] - coords[i][0];
                            var h2 = mouseCoords[1] - coords[i][1];
                            var a2 = Math.atan(h2 / w2) * 57.3; // DEGREES
                        
                            if (a2 > a1) {
                                continue;
                            }
                        }
                        
                        // Is there a tooltip defined?
                        if (!obj.Get('chart.tooltips')[i] && typeof(obj.Get('chart.tooltips')) != 'function') {
                            break;
                        }

                        overFunnel = true;
                        canvas.style.cursor = 'pointer';
        
                        // Stop the event propagating
                        e.stopPropagation();
                        
                        break;
                    }
                }
                
                if (!overFunnel) {
                    canvas.style.cursor = 'default';
                    canvas.style.cursor = 'default';
                }
            }

        // This resets the canvas events - getting rid of any installed event handlers
        } else {
            this.canvas.onclick     = null;
            this.canvas.onmousemove = null;
        }


        /**
        * Draw the labels on the chart
        */
        this.DrawLabels();
        
        /**
        * If the canvas is annotatable, do install the event handlers
        */
        if (this.Get('chart.annotatable')) {
            RGraph.Annotate(this);
        }
        
        /**
        * This bit shows the mini zoom window if requested
        */
        if (this.Get('chart.zoom.mode') == 'thumbnail'|| this.Get('chart.zoom.mode') == 'area') {
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
    * This function actually draws the chart
    */
    RGraph.Funnel.prototype.DrawFunnel = function ()
    {
        var context   = this.context;
        var canvas    = this.canvas;
        var width     = this.canvas.width - (2 * this.Get('chart.gutter'));
        var height    = this.canvas.height - (2 * this.Get('chart.gutter'));
        var total     = RGraph.array_max(this.data);
        var accheight = this.Get('chart.gutter');


        /**
        * Loop through each segment to draw
        */
        
        // Set a shadow if it's been requested
        if (this.Get('chart.shadow')) {
            context.shadowColor   = this.Get('chart.shadow.color');
            context.shadowBlur    = this.Get('chart.shadow.blur');
            context.shadowOffsetX = this.Get('chart.shadow.offsetx');
            context.shadowOffsetY = this.Get('chart.shadow.offsety');
        }

        for (i=0; i<this.data.length; ++i) {

            i = Number(i);
            
            var curvalue  = this.data[i];
            var curwidth  = (curvalue / total) * width;
            var curheight = height / this.data.length;
            var halfCurWidth = (curwidth / 2);
            var nextvalue = this.data[i + 1] ?  this.data[i + 1] : 0;
            var nextwidth = this.data[i + 1] ? (nextvalue / total) * width : 0;
            var halfNextWidth = (nextwidth / 2);
            var center    = (canvas.width / 2);
            var gutter    = this.Get('chart.gutter');

            /**
            * First segment
            */
            if (i == 0) {
                var x1 = center - halfCurWidth;
                var y1 = gutter;
                var x2 = center + halfCurWidth;
                var y2 = gutter;
                var x3 = center + halfNextWidth;
                var y3 = accheight + curheight;
                var x4 = center - halfNextWidth;
                var y4 = accheight + curheight;

            /**
            * Subsequent segments
            */
            } else {
                var x1 = center - halfCurWidth;
                var y1 = accheight;
                var x2 = center + halfCurWidth;
                var y2 = accheight;
                var x3 = center + halfNextWidth;
                var y3 = accheight + curheight;
                var x4 = center - halfNextWidth;
                var y4 = accheight + curheight;
            }

            /**
            * Set the fill colour. If i is over 0 then don't use an offset
            */
            if (document.all && this.Get('chart.shadow')) {
                this.DrawIEShadow([x1, y1, x2, y2, x3, y3, x4, y4], i > 0 && this.Get('chart.shadow.offsety') < 0);
            }

            context.fillStyle = this.Get('chart.colors')[i];

            context.beginPath();
                context.moveTo(x1, y1);
                context.lineTo(x2, y2);
                context.lineTo(x3, y3);
                context.lineTo(x4, y4);
            context.closePath();

            /**
            * Store the coordinates
            */
            this.coords.push([x1, y1, x2, y2, x3, y3, x4, y4]);

            context.stroke();
            context.fill();

            accheight += curheight;
        }
        
        /**
        * Lastly, draw the key if necessary
        */
        if (this.Get('chart.key') && this.Get('chart.key').length) {
            RGraph.DrawKey(this, this.Get('chart.key'), this.Get('chart.colors'));
        }
    }
    
    /**
    * Draws the labels
    */
    RGraph.Funnel.prototype.DrawLabels = function ()
    {
        /**
        * Draws the labels (draws them "as we go")
        */
        if (this.Get('chart.labels') && this.Get('chart.labels').length > 0) {

            var context = this.context;

            for (var j=0; j<this.coords.length; ++j) {  // MUST be "j"
                context.beginPath();
                
                // Set the color back to black
                context.fillStyle = this.Get('chart.text.color');
                
                // Turn off any shadow
                RGraph.NoShadow(this);
                
                var label = this.Get('chart.labels')[j];

                RGraph.Text(context, this.Get('chart.text.font'), this.Get('chart.text.size'), this.Get('chart.text.halign') == 'left' ? 15 : this.canvas.width / 2, this.coords[j][1], label, 'center', this.Get('chart.text.halign') == 'left' ? 'left' : 'center', true, null, this.Get('chart.text.boxed') ? 'white' : null);
            }
        }
    }


    /**
    * This function is used by MSIE only to manually draw the shadow
    * 
    * @param array coords The coords for the bar
    */
    RGraph.Funnel.prototype.DrawIEShadow = function (coords, noOffset)
    {
        var prevFillStyle = this.context.fillStyle;
        var offsetx = this.Get('chart.shadow.offsetx');
        var offsety = this.Get('chart.shadow.offsety');
        var context = this.context;

        context.lineWidth = 1;
        context.fillStyle = this.Get('chart.shadow.color');
        
        // Draw the shadow
        context.beginPath();
            context.moveTo(coords[0] + (noOffset ? 0 : offsetx), coords[1] + (noOffset ? 0 : offsety));
            context.lineTo(coords[2] + (noOffset ? 0 : offsetx), coords[3] + (noOffset ? 0 : offsety));
            context.lineTo(coords[4] + (noOffset ? 0 : offsetx), coords[5] + (noOffset ? 0 : offsety));
            context.lineTo(coords[6] + (noOffset ? 0 : offsetx), coords[7] + (noOffset ? 0 : offsety));
        context.closePath();
        
        context.fill();
        

        
        // Change the fillstyle back to what it was
        this.context.fillStyle = prevFillStyle;
    }