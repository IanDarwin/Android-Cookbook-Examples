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
    
    ModalDialog = {}
    ModalDialog.dialog     = null;
    ModalDialog.background = null;
    ModalDialog.offset     = 50;
    ModalDialog.events     = [];

    /**
    * Shows the dialog with the supplied DIV acting as the contents
    * 
    * @param string id    The ID of the DIV to use as the dialogs contents
    * @param int    width The width of the dialog
    */
    ModalDialog.Show = function (id, width)
    {
        ModalDialog.id    = id;
        ModalDialog.width = width;

        ModalDialog.ShowBackground();
        ModalDialog.ShowDialog();

        // Install the event handlers
        window.onresize = ModalDialog.Resize;

        
        // Call them initially
        ModalDialog.Resize();
        
        ModalDialog.FireCustomEvent('onmodaldialog');
    }
    
    
    /**
    * Shows the background semi-transparent darkened DIV
    */
    ModalDialog.ShowBackground = function ()
    {
        // Create the background if neccessary
        if (!ModalDialog.background) {
            ModalDialog.background = document.createElement('DIV');
            ModalDialog.background.className      = 'ModalDialog_background';
            ModalDialog.background.style.position = 'fixed';
            ModalDialog.background.style.top      = 0;
            ModalDialog.background.style.left     = 0;
            ModalDialog.background.style.width    = (screen.width + 100) + 'px';
            ModalDialog.background.style.height   = (screen.height + 100) + 'px';

            ModalDialog.background.style.backgroundColor = 'rgb(204,204,204)';
            ModalDialog.background.style.filter = 'Alpha(opacity=0)';
            ModalDialog.background.style.opacity = 0;
            ModalDialog.background.style.zIndex = 3276;
            
            document.body.appendChild(ModalDialog.background);
        }

        ModalDialog.background.style.visibility = 'visible';
    }


    /**
    * Shows the dialog itself
    */
    ModalDialog.ShowDialog = function ()
    {
        // Create the DIV if necessary
        if (!ModalDialog.dialog) {
            ModalDialog.dialog = document.createElement('DIV');
    
            ModalDialog.dialog.id                    = 'ModalDialog_dialog';
            ModalDialog.dialog.className             = 'ModalDialog_dialog';

            var borderRadius = '15px';

            ModalDialog.dialog.style.borderRadius       = borderRadius;
            ModalDialog.dialog.style.MozBorderRadius    = borderRadius;
            ModalDialog.dialog.style.WebkitBorderRadius = borderRadius;

            ModalDialog.dialog.style.boxShadow    = '3px 3px 3px rgba(96,96,96,0.5)';
            ModalDialog.dialog.style.MozBoxShadow = '3px 3px 3px rgba(96,96,96,0.5)';
            ModalDialog.dialog.style.WebkitBoxShadow    = 'rgba(96,96,96,0.5) 3px 3px 3px';

            ModalDialog.dialog.style.position        = 'fixed';
            ModalDialog.dialog.style.backgroundColor = 'white';
            ModalDialog.dialog.style.width           = parseInt(ModalDialog.width) + 'px';
            ModalDialog.dialog.style.border          = '2px solid #999';
            ModalDialog.dialog.style.zIndex          = 32767;
            ModalDialog.dialog.style.backgroundColor = 'white';
            ModalDialog.dialog.style.padding         = '5px';
            ModalDialog.dialog.style.paddingTop      = '25px';
            ModalDialog.dialog.style.overflow      = 'hidden';
            ModalDialog.dialog.style.opacity       = 0;
            
            if (document.all) {
                ModalDialog.dialog.style.zIndex = 32767;
                ModalDialog.dialog.style.filter = 'progid:DXImageTransform.Microsoft.Shadow(color=#aaaaaa,direction=135); Alpha(opacity=0)';
            }

            ModalDialog.dialog.innerHTML = document.getElementById(ModalDialog.id).innerHTML;


            // Accomodate various browsers
            if (navigator.userAgent.indexOf('Opera') != -1) {
                ModalDialog.dialog.style.paddingTop = '25px';

            } else if (navigator.userAgent.indexOf('MSIE') != -1) {
                ModalDialog.dialog.style.paddingTop = '25px';

            } else if (navigator.userAgent.indexOf('Safari') != -1) {
                ModalDialog.dialog.style.paddingTop = '25px';
            }

            document.body.appendChild(ModalDialog.dialog);
            
            // Now create the grey bar at the top of the dialog
            var bar = document.createElement('DIV');
            bar.className = 'ModalDialog_topbar';
            bar.style.top = 0;
            bar.style.left = 0;
            bar.style.width = (ModalDialog.dialog.offsetWidth - 4) + 'px';
            bar.style.height = '20px';
            bar.style.backgroundColor = '#bbb';
            bar.style.borderBottom = '2px solid #999';
            bar.style.zIndex    = -32765;
            bar.style.position = 'absolute';
            
            var borderRadius = '11px';

            bar.style.WebkitBorderTopLeftRadius = borderRadius;
            bar.style.WebkitBorderTopRightRadius = borderRadius;
            bar.style.MozBorderRadiusTopleft = borderRadius;
            bar.style.MozBorderRadiusTopright = borderRadius;
            bar.style.borderTopRightRadius = borderRadius;
            bar.style.borderTopLeftRadius = borderRadius;

            ModalDialog.dialog.appendChild(bar);
            
            // Now reposition it in the center
            ModalDialog.dialog.style.left = (document.body.offsetWidth / 2) - (ModalDialog.dialog.offsetWidth / 2) + 'px';
            ModalDialog.dialog.style.top  = '30%';
        }
        
        // Show the dialog
        ModalDialog.dialog.style.visibility = 'visible';
        
        // A simple fade-in effect
        setTimeout('ModalDialog.dialog.style.opacity = 0.2', 50);
        setTimeout('ModalDialog.dialog.style.opacity = 0.4', 100);
        setTimeout('ModalDialog.dialog.style.opacity = 0.6', 150);
        setTimeout('ModalDialog.dialog.style.opacity = 0.8', 200);
        setTimeout('ModalDialog.dialog.style.opacity = 1', 250);

        setTimeout('ModalDialog.background.style.opacity = 0.1', 50);
        setTimeout('ModalDialog.background.style.opacity = 0.2', 100);
        setTimeout('ModalDialog.background.style.opacity = 0.3', 150);
        setTimeout('ModalDialog.background.style.opacity = 0.4', 200);
        setTimeout('ModalDialog.background.style.opacity = 0.5', 250);

        setTimeout('ModalDialog.dialog.style.filter = "progid:DXImageTransform.Microsoft.Shadow(color=#aaaaaa,direction=135); Alpha(opacity=20)"', 50);
        setTimeout('ModalDialog.dialog.style.filter = "progid:DXImageTransform.Microsoft.Shadow(color=#aaaaaa,direction=135); Alpha(opacity=40)"', 100);
        setTimeout('ModalDialog.dialog.style.filter = "progid:DXImageTransform.Microsoft.Shadow(color=#aaaaaa,direction=135); Alpha(opacity=60)"', 150);
        setTimeout('ModalDialog.dialog.style.filter = "progid:DXImageTransform.Microsoft.Shadow(color=#aaaaaa,direction=135); Alpha(opacity=80)"', 200);
        setTimeout('ModalDialog.dialog.style.filter = "progid:DXImageTransform.Microsoft.Shadow(color=#aaaaaa,direction=135); Alpha(opacity=100)"', 250);

        setTimeout('ModalDialog.background.style.filter = "progid:DXImageTransform.Microsoft.Shadow(color=#aaaaaa,direction=135); Alpha(opacity=10)"', 50);
        setTimeout('ModalDialog.background.style.filter = "progid:DXImageTransform.Microsoft.Shadow(color=#aaaaaa,direction=135); Alpha(opacity=20)"', 100);
        setTimeout('ModalDialog.background.style.filter = "progid:DXImageTransform.Microsoft.Shadow(color=#aaaaaa,direction=135); Alpha(opacity=30)"', 150);
        setTimeout('ModalDialog.background.style.filter = "progid:DXImageTransform.Microsoft.Shadow(color=#aaaaaa,direction=135); Alpha(opacity=40)"', 200);
        setTimeout('ModalDialog.background.style.filter = "progid:DXImageTransform.Microsoft.Shadow(color=#aaaaaa,direction=135); Alpha(opacity=50)"', 250);
    }

    
    /**
    * Hides everything
    */
    ModalDialog.Close = function ()
    {
        if (ModalDialog.dialog) {
            ModalDialog.dialog.style.visibility = 'hidden';
            ModalDialog.dialog.style.opacity = 0;
            ModalDialog.dialog.style.filter = "progid:DXImageTransform.Microsoft.Shadow(color=#aaaaaa,direction=135); Alpha(opacity=0)";
        }

        if (ModalDialog.background) {
            ModalDialog.background.style.visibility = 'hidden';
            ModalDialog.background.style.opacity = 0;
            ModalDialog.background.style.filter = "progid:DXImageTransform.Microsoft.Shadow(color=#aaaaaa,direction=135); Alpha(opacity=0)";
        }        
    }
    
    // An alias
    ModalDialog.Hide = ModalDialog.Close
    
    
    /**
    * Accommodate the window being resized
    */
    ModalDialog.Resize = function ()
    {
        if (ModalDialog.dialog) {
            ModalDialog.dialog.style.left = (document.body.offsetWidth / 2) - (ModalDialog.dialog.offsetWidth / 2) + 'px';
        }

        ModalDialog.background.style.width  = '2500px';
        ModalDialog.background.style.height = '2500px';
    }


    /**
    * Returns the page height
    * 
    * @return int The page height
    */
    ModalDialog.getPageHeight = function ()
    {
        // Chrome
        if (navigator.userAgent.indexOf('Chrome') != -1) {
            return Math.max(document.body.clientHeight, document.body.scrollHeight);

        // Safari
        } else if (navigator.userAgent.indexOf('Safari') != -1) {
            return Math.max(document.body.clientHeight, document.body.scrollHeight) + 40;

        // Opera
        } else if (navigator.userAgent.indexOf('Opera') != -1) {

            var height = Math.max(document.body.clientHeight, document.body.offsetHeight);
            height = Math.max(height, document.body.scrollHeight);

            return height + 40;

        // MSIE
        } else if (navigator.userAgent.indexOf('MSIE') != -1) {
            var height = Math.max(document.body.scrollHeight, document.body.clientHeight);
            height = Math.max(height, screen.height);

            return height;
        
        // Firefox and other
        } else {
            var height = Math.max(document.body.clientHeight, document.body.scrollHeight) + 40;
            
            if (window.innerHeight > height) {
                height = window.innerHeight;
            }

            return height;
        }
    }


    /**
    * Returns the page height
    * 
    * @return int The page height
    */
    ModalDialog.AddCustomEventListener = function (name, func)
    {
        if (typeof(ModalDialog.events) == 'undefined') {
            ModalDialog.events = [];
        }

        ModalDialog.events.push([name, func]);
    }


    /**
    * Used to fire the ModalDialog custom event
    * 
    * @param object obj   The graph object that fires the event
    * @param string event The name of the event to fire
    */
    ModalDialog.FireCustomEvent = function (name)
    {
        for (var i=0; i<ModalDialog.events.length; ++i) {
            if (typeof(ModalDialog.events[i][0]) == 'string' && ModalDialog.events[i][0] == name && typeof(ModalDialog.events[i][1]) == 'function') {
                ModalDialog.events[i][1]();
            }
        }
    }