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
    * This gunction shows a context menu containing the parameters
    * provided to it
    * 
    * @param object canvas    The canvas object
    * @param array  menuitems The context menu menuitems
    * @param object e         The event object
    */
    RGraph.Contextmenu = function (canvas, menuitems, e)
    {
        e = RGraph.FixEventObject(e);

        /**
        * Hide any existing menu
        */
        if (RGraph.Registry.Get('chart.contextmenu')) {
            RGraph.HideContext();
        }
        
        // Hide any zoomed canvas
        RGraph.HideZoomedCanvas();

        /**
        * Hide the palette if necessary
        */
        RGraph.HidePalette();
        
        /**
        * This is here to ensure annotating is OFF
        */
        canvas.__object__.Set('chart.mousedown', false)

        var x      = e.pageX;
        var y      = e.pageY;
        var div    = document.createElement('div');
        var bg     = document.createElement('div');

        div.className             = 'RGraph_contextmenu';
        div.__canvas__            = canvas; /* Store a reference to the canvas on the contextmenu object */
        div.style.position        = 'absolute';
        div.style.left            = 0;
        div.style.top             = 0;
        div.style.border          = '1px solid black';
        div.style.backgroundColor = 'white';
        div.style.boxShadow       = '3px 3px 3px rgba(96,96,96,0.5)';
        div.style.MozBoxShadow    = '3px 3px 3px rgba(96,96,96,0.5)';
        div.style.WebkitBoxShadow = '3px 3px 3px rgba(96,96,96,0.5)';
        div.style.filter          = 'progid:DXImageTransform.Microsoft.Shadow(color=#aaaaaa,direction=135)';
        div.style.opacity         = 0; // FIXME TODO Not currently supported in MSIE

        bg.className             = 'RGraph_contextmenu_background';
        bg.style.position        = 'absolute';
        bg.style.backgroundColor = '#ccc';
        bg.style.borderRight     = '1px solid #aaa';
        bg.style.top             = 0;
        bg.style.left            = 0;
        bg.style.width           = '18px';
        bg.style.height          = '100%';
        bg.style.opacity         = 0;


        div = document.body.appendChild(div);
        bg  = div.appendChild(bg);


        /**
        * Now add the context menu items
        */
        for (i=0; i<menuitems.length; ++i) {
            
            var menuitem = document.createElement('div');
            
            menuitem.__canvas__      = canvas;
            menuitem.__contextmenu__ = div;
            menuitem.className       = 'RGraph_contextmenu_item';
            
            if (menuitems[i]) {
                menuitem.style.padding = '2px 5px 2px 23px';
                menuitem.style.fontFamily = 'Arial';
                menuitem.style.fontSize = '10pt';
                menuitem.style.fontWeight = 'normal';
                menuitem.innerHTML = menuitems[i][0];

                if (RGraph.is_array(menuitems[i][1])) {
                    menuitem.style.backgroundImage = 'url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAcAAAAHCAYAAADEUlfTAAAAQUlEQVQImY3NoQ2AMABE0ZewABMyGQ6mqWODzlAclBSFO8HZl8uf0FFxCHtwYkt4Y6ChYE44cGH9/fyae2p2LAleW9oVTQuVf6gAAAAASUVORK5CYII=)';
                    menuitem.style.backgroundRepeat = 'no-repeat';
                    menuitem.style.backgroundPosition = '97% center';
                }

                // Add the mouseover event
                if (menuitems[i][1]) {
                    if (menuitem.addEventListener) {
                        menuitem.addEventListener("mouseover", function (e) {RGraph.HideContextSubmenu(); e.target.style.backgroundColor = 'rgba(0,0,0,0.2)'; e.target.style.cursor = 'pointer';}, false);
                        menuitem.addEventListener("mouseout", function (e) {e.target.style.backgroundColor = 'inherit'; e.target.style.cursor = 'default';}, false);
                    } else  {
                        menuitem.attachEvent("onmouseover", function () {RGraph.HideContextSubmenu();event.srcElement.style.backgroundColor = '#eee';event.srcElement.style.cursor = 'pointer';}
                    , false);
                        menuitem.attachEvent("onmouseout", function () {event.srcElement.style.backgroundColor = 'inherit'; event.srcElement.style.cursor = 'default';}, false);
                    }
                } else {
                    if (menuitem.addEventListener) {
                        menuitem.addEventListener("mouseover", function (e) {e.target.style.cursor = 'default';}, false);
                        menuitem.addEventListener("mouseout", function (e) {e.target.style.cursor = 'default';}, false);
                    } else  {
                        menuitem.attachEvent("onmouseover", function () {event.srcElement.style.cursor = 'default'}, false);
                        menuitem.attachEvent("onmouseout", function () {event.srcElement.style.cursor = 'default';}, false);
                    }
                }

            } else {
                menuitem.style.borderBottom = '1px solid #ddd';
                menuitem.style.marginLeft = '25px';
            }

            div.appendChild(menuitem);

            /**
            * Install the event handler that calls the menuitem
            */
            if (menuitems[i] && menuitems[i][1] && typeof(menuitems[i][1]) == 'function') {
                if (document.all) {
                    menuitem.attachEvent('onclick', menuitems[i][1]);
                    menuitem.attachEvent('onclick', function () {RGraph.HideContext();});
                } else {
                    menuitem.addEventListener('click', menuitems[i][1], false);
                }
            
            // Submenu
            } else if (menuitems[i] && menuitems[i][1] && RGraph.is_array(menuitems[i][1])) {
                var tmp = menuitems[i][1]; // This is here because of "references vs primitives" and how they're passed around in Javascript
                menuitem.addEventListener('mouseover', function (e) {RGraph.Contextmenu_submenu(canvas.__object__, tmp, e.target);}, false);
                // TODO Need to accommodate MSIE here with attachEvent()
            }
        }
        
        /**
        * Now all the menu items have been added, set the shadow width
        * Shadow now handled by CSS3?
        */
        div.style.width = (div.offsetWidth + 10) + 'px';
        div.style.height = (div.offsetHeight - (RGraph.isIE9() ? 10 : 2)) + 'px';

        /**
        * Set the background (the left bar) width if it's MSIE
        */
        if (document.all) {
            bg.style.height = (div.offsetHeight - 10) + 'px';
        }

        // Show the menu to the left or the right (normal) of the cursor?
        if (x + div.offsetWidth > document.body.offsetWidth) {
            x -= div.offsetWidth;
        }
        
        // Reposition the menu (now we have the real offsetWidth)
        div.style.left = x + 'px';
        div.style.top = y + 'px';

        /**
        * Do a little fade in effect
        */
        setTimeout("if (obj = RGraph.Registry.Get('chart.contextmenu')) obj.style.opacity = 0.2", 50);
        setTimeout("if (obj = RGraph.Registry.Get('chart.contextmenu')) obj.style.opacity = 0.4", 100);
        setTimeout("if (obj = RGraph.Registry.Get('chart.contextmenu')) obj.style.opacity = 0.6", 150);
        setTimeout("if (obj = RGraph.Registry.Get('chart.contextmenu')) obj.style.opacity = 0.8", 200);
        setTimeout("if (obj = RGraph.Registry.Get('chart.contextmenu')) obj.style.opacity = 1", 250);

        // The fade in effect on the left gray bar
        setTimeout("if (obj = RGraph.Registry.Get('chart.contextmenu.bg')) obj.style.opacity = 0.2", 50);
        setTimeout("if (obj = RGraph.Registry.Get('chart.contextmenu.bg')) obj.style.opacity = 0.4", 100);
        setTimeout("if (obj = RGraph.Registry.Get('chart.contextmenu.bg')) obj.style.opacity = 0.6", 150);
        setTimeout("if (obj = RGraph.Registry.Get('chart.contextmenu.bg')) obj.style.opacity = 0.8", 200);
        setTimeout("if (obj = RGraph.Registry.Get('chart.contextmenu.bg')) obj.style.opacity = 1", 250);

        // Store the context menu in the registry
        RGraph.Registry.Set('chart.contextmenu', div);
        RGraph.Registry.Set('chart.contextmenu.bg', bg);
        RGraph.Registry.Get('chart.contextmenu').oncontextmenu = function () {return false;};
        RGraph.Registry.Get('chart.contextmenu.bg').oncontextmenu = function () {return false;};

        /**
        * Install the event handlers that hide the context menu
        */
        canvas.addEventListener('click', function () {RGraph.HideContext();}, false);

        window.onclick = function (e)
        {
            RGraph.HideContext();
            
            // Removed on 3/7/10 - stops a bug in conjunction with annotating which presents itself on the Rscatter
            //RGraph.Redraw();
            
            // Fire the onclick event again
            if (e.target.onclick && e.button == 0) {
                e.target.onclick(e);
            }
        }

        window.onresize = function () {RGraph.HideContext();}

        e.stopPropagation();

        /**
        * Fire the (RGraph) oncontextmenu event
        */
        RGraph.FireCustomEvent(canvas.__object__, 'oncontextmenu');

        return false;
    }


    /**
    * Hides the context menu if it's currently visible
    */
    RGraph.HideContext = function ()
    {
        var cm   = RGraph.Registry.Get('chart.contextmenu');
        var cmbg = RGraph.Registry.Get('chart.contextmenu.bg');
        
        //Hide any submenu currently being displayed
        RGraph.HideContextSubmenu();

        if (cm) {
            cm.parentNode.removeChild(cm);
            cmbg.parentNode.removeChild(cmbg);

            cm.style.visibility = 'hidden';
            cm.style.display = 'none';
            RGraph.Registry.Set('chart.contextmenu', null);
            
            cmbg.style.visibility = 'hidden';
            cmbg.style.display = 'none';
            RGraph.Registry.Set('chart.contextmenu.bg', null);
        }
    }


    /**
    * Hides the context menus SUBMENU if it's currently visible
    */
    RGraph.HideContextSubmenu = function ()
    {
        var sub  = RGraph.Registry.Get('chart.contextmenu.submenu');

        if (sub) {
            sub.style.visibility = 'none';
            sub.style.display    = 'none';
            RGraph.Registry.Set('chart.contextmenu.submenu', null);
        }
    }


    /**
    * Shows the context menu after making a few checks - not opera (doesn't support oncontextmenu,
    * not safari (tempermentality), not chrome (hmmm)
    */
    RGraph.ShowContext = function (obj)
    {
        RGraph.HidePalette();

        if (obj.Get('chart.contextmenu') && obj.Get('chart.contextmenu').length) {

            var isOpera      = navigator.userAgent.indexOf('Opera') >= 0;
            var isSafari     = navigator.userAgent.indexOf('Safari') >= 0;
            var isChrome     = navigator.userAgent.indexOf('Chrome') >= 0;
            var isMacFirefox = navigator.userAgent.indexOf('Firefox') > 0 && navigator.userAgent.indexOf('Mac') > 0;

            if (((!isOpera && !isSafari) || isChrome) && !isMacFirefox) {

                obj.canvas.oncontextmenu = function (e)
                {
                    e = RGraph.FixEventObject(e);

                    if (e.ctrlKey) return true;

                    RGraph.Contextmenu(obj.canvas, obj.Get('chart.contextmenu'), e);

                    return false;
                }

            // Accomodate Opera and Safari - use double click event
            } else {

                obj.canvas.addEventListener('dblclick', function (e)
                {
                    if (e.ctrlKey) return true;

                    if (!RGraph.Registry.Get('chart.contextmenu')) {
                        RGraph.Contextmenu(obj.canvas, obj.Get('chart.contextmenu'), e);
                    }
                }, false);
            }
        }
    }


    /**
    * This draws a submenu should it be necessary
    * 
    * @param object obj  The graph object
    * @param object menu The context menu
    */
    RGraph.Contextmenu_submenu = function (obj, menuitems, parentMenuItem)
    {
        RGraph.HideContextSubmenu();

        var canvas  = obj.canvas;
        var context = obj.context;
        var menu    = parentMenuItem.parentNode;

        var subMenu = document.createElement('DIV');
        subMenu.style.position = 'absolute';
        subMenu.style.width = '100px';
        subMenu.style.top = menu.offsetTop + parentMenuItem.offsetTop + 'px';
        subMenu.style.left            = (menu.offsetLeft + menu.offsetWidth) + 'px';
        subMenu.style.backgroundColor = 'white';
        subMenu.style.border          = '1px solid black';
        subMenu.className             = 'RGraph_contextmenu';
        subMenu.__contextmenu__       = menu;
        subMenu.style.boxShadow       = '3px 3px 3px rgba(96,96,96,0.5)';
        subMenu.style.MozBoxShadow    = '3px 3px 3px rgba(96,96,96,0.5)';
        subMenu.style.WebkitBoxShadow = '3px 3px 3px rgba(96,96,96,0.5)';
        subMenu.style.filter          = 'progid:DXImageTransform.Microsoft.Shadow(color=#aaaaaa,direction=135)';
        document.body.appendChild(subMenu);
        
        for (var i=0; i<menuitems.length; ++i) {
                    
            var menuitem = document.createElement('DIV');
            
            menuitem.__canvas__      = canvas;
            menuitem.__contextmenu__ = menu;
            menuitem.className       = 'RGraph_contextmenu_item';
            
            if (menuitems[i]) {
                menuitem.style.padding = '2px 5px 2px 23px';
                menuitem.style.fontFamily = 'Arial';
                menuitem.style.fontSize = '10pt';
                menuitem.style.fontWeight = 'normal';
                menuitem.innerHTML = menuitems[i][0];
        
                if (menuitems[i][1]) {
                    if (menuitem.addEventListener) {
                        menuitem.addEventListener("mouseover", function (e) {e.target.style.backgroundColor = 'rgba(0,0,0,0.2)'; e.target.style.cursor = 'pointer';}, false);
                        menuitem.addEventListener("mouseout", function (e) {e.target.style.backgroundColor = 'inherit'; e.target.style.cursor = 'default';}, false);
                    } else  {
                        menuitem.attachEvent("onmouseover", function () {event.srcElement.style.backgroundColor = 'rgba(0,0,0,0.2)'; event.srcElement.style.cursor = 'pointer'}, false);
                        menuitem.attachEvent("onmouseout", function () {event.srcElement.style.backgroundColor = 'inherit'; event.srcElement.style.cursor = 'default';}, false);
                    }
                } else {
                    if (menuitem.addEventListener) {
                        menuitem.addEventListener("mouseover", function (e) {e.target.style.cursor = 'default';}, false);
                        menuitem.addEventListener("mouseout", function (e) {e.target.style.cursor = 'default';}, false);
                    } else  {
                        menuitem.attachEvent("onmouseover", function () {event.srcElement.style.cursor = 'default'}, false);
                        menuitem.attachEvent("onmouseout", function () {event.srcElement.style.cursor = 'default';}, false);
                    }
                }
            } else {
                menuitem.style.borderBottom = '1px solid #ddd';
                menuitem.style.marginLeft = '25px';
            }
            
            subMenu.appendChild(menuitem);
        
            if (menuitems[i] && menuitems[i][1]) {
                if (document.all) {
                    menuitem.attachEvent('onclick', menuitems[i][1]);
                } else {
                    menuitem.addEventListener('click', menuitems[i][1], false);
                }
            }
        }


        var bg                   = document.createElement('DIV');
        bg.className             = 'RGraph_contextmenu_background';
        bg.style.position        = 'absolute';
        bg.style.backgroundColor = '#ccc';
        bg.style.borderRight     = '1px solid #aaa';
        bg.style.top             = 0;
        bg.style.left            = 0;
        bg.style.width           = '18px';
        bg.style.height          = '100%';

        bg  = subMenu.appendChild(bg);

        RGraph.Registry.Set('chart.contextmenu.submenu', subMenu);
    }
