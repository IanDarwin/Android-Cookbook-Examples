package com.oreilly.recipe12;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.widget.Toast;

public class CustomMenu extends Activity {
	
    
	private static final int OPTION_1 = 0;
	private static final int OPTION_2 = 1;
	private int GROUP_ID = 4;
	private int ITEM_ID =3;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	
        SubMenu sub1 = menu.addSubMenu(GROUP_ID, ITEM_ID , Menu.NONE, R.string.submenu_1);
        sub1.setHeaderIcon(R.drawable.icon);
        sub1.setIcon(R.drawable.icon);
       
        sub1.add(GROUP_ID , OPTION_1, 0, "Submenu Option 1");
        sub1.add(GROUP_ID, OPTION_2, 1, "Submenu Option 2");
       
		return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case OPTION_1: 
            	Toast.makeText(this, "Submenu 1, Option 1", Toast.LENGTH_LONG).show();
                break;
            case OPTION_2:
            	Toast.makeText(this, "Submenu 1, Option 2", Toast.LENGTH_LONG).show();
                break;
        }
        return true;
    }
}