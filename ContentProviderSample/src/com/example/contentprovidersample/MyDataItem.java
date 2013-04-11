package com.example.contentprovidersample;

/**
 * A dummy item representing a piece of content.
 */
public class MyDataItem {
	
		public int id;
		public String content;

		public MyDataItem(int id, String content) {
			this.id = id;
			this.content = content;
		}

		@Override
		public String toString() {
			return id + "--" + content;
		}
	}