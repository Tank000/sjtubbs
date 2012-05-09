/* 
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tyt.bbs.provider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Convenience definitions for Provider
 */
public final class DataColums {
	public static final String CONTENT = "content://";
	public static final String AUTHORITY = "com.tyt.bbs";
	private static final String TYPE_PRIMARY_KEY = "INTEGER PRIMARY KEY AUTOINCREMENT";
	private static final String TYPE_TEXT = "TEXT";
	
	public static final class PostData implements BaseColumns {

		public static final Uri CONTENT_URI
		= Uri.parse(new StringBuilder(CONTENT).append(AUTHORITY).append("/posts").toString());
		
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.tyt.bbs.post";

        /**
         * The MIME type of a {@link #CONTENT_URI} sub-directory of a single note.
         */
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.tyt.bbs.post";
		/**
		 * The default sort order for this table
		 */
		public static final String DEFAULT_SORT_ORDER = "_id DESC";

		public static final String AUTHOR = "author";

		public static final String TIME = "time";

		public static final String BOARD = "board";
		
		public static final String TITLE = "title";
		
		public static final String TEXT = "text";
		
		public static final String LINK = "link";
		
		public static final String REID = "reid";
		
        public static final String[] COLUMNS = new String[] {_ID,AUTHOR, TIME, BOARD,TITLE,TEXT,LINK,REID};
		
		public static final String[] TYPES = new String[] {TYPE_PRIMARY_KEY,TYPE_TEXT, TYPE_TEXT, TYPE_TEXT, TYPE_TEXT,TYPE_TEXT, TYPE_TEXT, TYPE_TEXT};

        
		public static final Uri CONTENT_URI(long mRssId) {
			return Uri.parse(new StringBuilder(CONTENT).append(AUTHORITY).append("/rss/").append(mRssId).toString());
		}
	}
}
