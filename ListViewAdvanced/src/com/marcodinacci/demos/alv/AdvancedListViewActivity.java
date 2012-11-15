package com.marcodinacci.demos.alv;

/**
 * This file is part of AdvancedListViewDemo.
 * You should have downloaded this file from www.intransitione.com, if not, 
 * please inform me by writing an e-mail at the address below:
 *
 * Copyright [2011] [Marco Dinacci <marco.dinacci@gmail.com>]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.

 * The license text is available online and in the LICENSE file accompanying 
 * the distribution of this program.
 */

import android.app.ListActivity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;

public class AdvancedListViewActivity extends ListActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Context ctx = getApplicationContext();
		Resources res = ctx.getResources();

		String[] options = res.getStringArray(R.array.country_names);
		TypedArray icons = res.obtainTypedArray(R.array.country_icons);
		
		setListAdapter(new ImageAndTextAdapter(ctx, R.layout.main_list_item,
				options, icons));
    }
}