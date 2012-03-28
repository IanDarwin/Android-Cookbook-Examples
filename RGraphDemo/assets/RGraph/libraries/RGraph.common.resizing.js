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


    /**
    * This function can be used to allow resizing
    * 
    * @param object obj Your graph object
    */
    RGraph.AllowResizing = function (obj)
    {
        if (obj.Get('chart.resizable')) {
            var canvas  = obj.canvas;
            var context = obj.context;
            var resizeHandle = 15;
            RGraph.Resizing.canvas = canvas;
            
            /**
            * Add the original width and height to the canvas
            */
            if (!canvas.__original_width__ && !canvas.__original_height__) {
                canvas.__original_width__  = canvas.width;
                canvas.__original_height__ = canvas.height;
            }
    
            /**
            * Draw the resize handle
            */
            var textWidth = context.measureText('Reset').width + 2;

            obj.context.beginPath();
                obj.context.strokeStyle = 'gray';
                obj.context.fillStyle = 'rgba(0,0,0,0)';
                obj.context.lineWidth = 1;
                obj.context.fillRect(obj.canvas.width - resizeHandle, obj.canvas.height - resizeHandle - 2, resizeHandle, resizeHandle + 2);
                obj.context.fillRect(obj.canvas.width - resizeHandle - textWidth, obj.canvas.height - resizeHandle, resizeHandle + textWidth, resizeHandle + 2);


                // Draw the arrows
                
                    // Vertical line
                    obj.context.moveTo(obj.canvas.width - (resizeHandle / 2), obj.canvas.height - resizeHandle);
                    obj.context.lineTo(obj.canvas.width - (resizeHandle / 2), obj.canvas.height);


                    obj.context.moveTo(obj.canvas.width, obj.canvas.height - (resizeHandle / 2));
                    obj.context.lineTo(obj.canvas.width - resizeHandle, obj.canvas.height - (resizeHandle / 2));
                
            context.fill();
            context.stroke();

            // Top arrow head
            context.fillStyle = 'gray';
            context.beginPath();
                context.moveTo(canvas.width - (resizeHandle / 2), canvas.height - resizeHandle);
                context.lineTo(canvas.width - (resizeHandle / 2) + 3, canvas.height - resizeHandle + 3);
                context.lineTo(canvas.width - (resizeHandle / 2) - 3, canvas.height - resizeHandle + 3);
            context.closePath();
            context.fill();

            // Bottom arrow head
            context.beginPath();
                context.moveTo(canvas.width - (resizeHandle / 2), canvas.height);
                context.lineTo(canvas.width - (resizeHandle / 2) + 3, canvas.height - 3);
                context.lineTo(canvas.width - (resizeHandle / 2) - 3, canvas.height - 3);
            context.closePath();
            context.fill();

            // Left arrow head
            context.beginPath();
                context.moveTo(canvas.width - resizeHandle, canvas.height - (resizeHandle / 2));
                context.lineTo(canvas.width - resizeHandle + 3, canvas.height - (resizeHandle / 2) + 3);
                context.lineTo(canvas.width - resizeHandle + 3, canvas.height - (resizeHandle / 2) - 3);
            context.closePath();
            context.fill();

            // Right arrow head
            context.beginPath();
                context.moveTo(canvas.width, canvas.height - (resizeHandle / 2));
                context.lineTo(canvas.width - 3, canvas.height - (resizeHandle / 2) + 3);
                context.lineTo(canvas.width  - 3, canvas.height - (resizeHandle / 2) - 3);
            context.closePath();
            context.fill();
            
            // Circle at the centre of the arrows
            context.beginPath();
                context.fillStyle = 'white';
                context.moveTo(canvas.width, canvas.height - (resizeHandle / 2));
                context.strokeRect(canvas.width - (resizeHandle / 2) - 2, canvas.height - (resizeHandle / 2) - 2, 4, 4);
                context.fillRect(canvas.width - (resizeHandle / 2) - 2, canvas.height - (resizeHandle / 2) - 2, 4, 4);
            context.fill();
            context.stroke();


            // Draw the "Reset" button
            context.beginPath();
                context.fillStyle = 'gray';
                context.moveTo(canvas.width - resizeHandle - 3, canvas.height - resizeHandle / 2);
                context.lineTo(canvas.width - resizeHandle - resizeHandle, canvas.height - (resizeHandle / 2));
                context.lineTo(canvas.width - resizeHandle - resizeHandle + 2, canvas.height - (resizeHandle / 2) - 2);
                context.lineTo(canvas.width - resizeHandle - resizeHandle + 2, canvas.height - (resizeHandle / 2) + 2);
                context.lineTo(canvas.width - resizeHandle - resizeHandle, canvas.height - (resizeHandle / 2));
            context.stroke();
            context.fill();

            context.beginPath();
                context.moveTo(canvas.width - resizeHandle - resizeHandle - 1, canvas.height - (resizeHandle / 2) - 3);
                context.lineTo(canvas.width - resizeHandle - resizeHandle - 1, canvas.height - (resizeHandle / 2) + 3);
            context.stroke();
            context.fill();


            window.onmousemove = function (e)
            {
                e = RGraph.FixEventObject(e);
                
                var canvas    = RGraph.Resizing.canvas;
                var newWidth  = RGraph.Resizing.originalw - (RGraph.Resizing.originalx - e.pageX);// - 5
                var newHeight = RGraph.Resizing.originalh - (RGraph.Resizing.originaly - e.pageY);// - 5

                if (RGraph.Resizing.mousedown) {
                    if (newWidth > (canvas.__original_width__ / 2)) RGraph.Resizing.div.style.width = newWidth + 'px';
                    if (newHeight > (canvas.__original_height__ / 2)) RGraph.Resizing.div.style.height = newHeight + 'px';
                }
            }

            /**
            * The window onmouseup function
            */
            var MouseupFunc = function (e)
            {
                if (!RGraph.Resizing || !RGraph.Resizing.div || !RGraph.Resizing.mousedown) {
                    return;
                }
    
                if (RGraph.Resizing.div) {

                    var div   = RGraph.Resizing.div;
                    var canvas = div.__canvas__;
                    var coords = RGraph.getCanvasXY(div.__canvas__);

                    var parentNode = canvas.parentNode;

                    if (canvas.style.position != 'absolute') {
                        // Create a DIV to go in the canvases place
                        var placeHolderDIV = document.createElement('DIV');
                            placeHolderDIV.style.width = (RGraph.Resizing.originalw)+ 'px';
                            placeHolderDIV.style.height = (RGraph.Resizing.originalh) + 'px';
                            //placeHolderDIV.style.backgroundColor = 'red';
                            placeHolderDIV.style.position = canvas.style.position;
                            placeHolderDIV.style.left     = canvas.style.left;
                            placeHolderDIV.style.top      = canvas.style.top;
                            placeHolderDIV.style.cssFloat = canvas.style.cssFloat;
                        parentNode.insertBefore(placeHolderDIV, canvas);
                    }


                    // Now set the canvas to be positioned absolutely
                    canvas.style.backgroundColor = 'white';
                    canvas.style.position        = 'absolute';
                    canvas.style.border          = '1px dashed gray';
                    canvas.style.left            = RGraph.Resizing.originalCanvasX + 'px';
                    canvas.style.top             = RGraph.Resizing.originalCanvasY + 'px';

                    canvas.width = parseInt(div.style.width);
                    canvas.height = parseInt(div.style.height);
                    canvas.__object__.Draw();

                    // Get rid of transparent semi-opaque DIV
                    RGraph.Resizing.mousedown = false;
                    div.style.left = '-1000px';
                    div.style.top = '-1000px';
                }

                /**
                * Fire the onresize event
                */
                RGraph.FireCustomEvent(canvas.__object__, 'onresize');
            }


            window.onmouseup = MouseupFunc;


            canvas.onmousemove = function (e)
            {
                e = RGraph.FixEventObject(e);
                
                var coords  = RGraph.getMouseXY(e);
                var canvas  = e.target;
                var context = canvas.getContext('2d');

                RGraph.Resizing.title = canvas.title;
                
                if (   (coords[0] > (canvas.width - resizeHandle)
                    && coords[0] < canvas.width
                    && coords[1] > (canvas.height - resizeHandle)
                    && coords[1] < canvas.height)) {
    
                        canvas.title = 'Resize the graph';
                        canvas.style.cursor = 'move';

                } else if (   coords[0] > (canvas.width - resizeHandle - resizeHandle)
                           && coords[0] < canvas.width - resizeHandle
                           && coords[1] > (canvas.height - resizeHandle)
                           && coords[1] < canvas.height) {
    
                    canvas.style.cursor = 'pointer';
                    canvas.title = 'Reset graph to original size';

                } else {
    
                    canvas.style.cursor = 'default';
                    canvas.title = '';
                }
            }


            canvas.onmousedown = function (e)
            {
                e = RGraph.FixEventObject(e);
    
                var coords = RGraph.getMouseXY(e);
                var canvasCoords = RGraph.getCanvasXY(e.target);
                
                if (   coords[0] > (obj.canvas.width - resizeHandle)
                    && coords[0] < obj.canvas.width
                    && coords[1] > (obj.canvas.height - resizeHandle)
                    && coords[1] < obj.canvas.height) {

                    RGraph.Resizing.mousedown = true;


                    /**
                    * Create the semi-opaque DIV
                    */
                    var div = document.createElement('DIV');
                    div.style.position = 'absolute';
                    div.style.left     = canvasCoords[0] + 'px';
                    div.style.top      = canvasCoords[1] + 'px';
                    div.style.width    = canvas.width +'px';
                    div.style.height   = canvas.height + 'px';
                    div.style.border   = '1px dotted black';
                    div.style.backgroundColor = 'gray';
                    div.style.opacity  = 0.5;
                    div.__canvas__ = e.target;

                    document.body.appendChild(div);
                    RGraph.Resizing.div = div;

                    // This is a repetition of the window.onmouseup function
                    div.onmouseup = function (e)
                    {
                        MouseupFunc(e);
                    }

                    
                    RGraph.Resizing.div.onmouseover = function (e)
                    {
                        e = RGraph.FixEventObject(e);
                        e.stopPropagation();
                    }
    
                    // The mouse
                    RGraph.Resizing.originalx = e.pageX;
                    RGraph.Resizing.originaly = e.pageY;
                    
                    //The canvas
                    RGraph.Resizing.originalw = obj.canvas.width;
                    RGraph.Resizing.originalh = obj.canvas.height;
                    
                    RGraph.Resizing.originalCanvasX = RGraph.getCanvasXY(obj.canvas)[0];
                    RGraph.Resizing.originalCanvasY = RGraph.getCanvasXY(obj.canvas)[1];
                }
            }


            /**
            * This function facilitates the reset button
            */
            canvas.onclick = function (e)
            {
                var coords = RGraph.getMouseXY(e);
                var canvas = e.target;

                if (   coords[0] > (canvas.width - resizeHandle - resizeHandle)
                    && coords[0] < canvas.width - resizeHandle
                    && coords[1] > (canvas.height - resizeHandle)
                    && coords[1] < canvas.height) {

                    // Restore the original width and height
                    canvas.width = canvas.__original_width__;
                    canvas.height = canvas.__original_height__;
                    
                    // Lose the border
                    canvas.style.border = '';

                    // Redraw the canvas
                    canvas.__object__.Draw();
                    
                    // Set the width and height on the DIV
                    RGraph.Resizing.div.style.width  = canvas.__original_width__ + 'px';
                    RGraph.Resizing.div.style.height = canvas.__original_height__ + 'px';
                    
                    /**
                    * Fire the resize event
                    */
                    RGraph.FireCustomEvent(canvas.__object__, 'onresize');
                }
            }
        }
    }