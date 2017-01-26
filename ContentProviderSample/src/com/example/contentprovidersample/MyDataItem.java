package com.example.contentprovidersample;

import java.io.Serializable;

/**
 * A dummy item representing a piece of content.
 */
public class MyDataItem implements Serializable {
	
		private static final long serialVersionUID = 6526763846629909879L;
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