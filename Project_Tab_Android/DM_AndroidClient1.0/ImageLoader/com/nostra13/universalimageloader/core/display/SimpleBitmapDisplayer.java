/*******************************************************************************
 * Copyright 2011-2013 Sergey Tarasevich
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.nostra13.universalimageloader.core.display;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.nostra13.universalimageloader.utils.L;

/**
 * Just displays {@link Bitmap} in {@link ImageView}
 * 
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 * @since 1.5.6
 */
public final class SimpleBitmapDisplayer implements BitmapDisplayer {
	@Override
	public Bitmap display(Bitmap bitmap, ImageView imageView) {
		if(bitmap != null){
			L.i("SimpleBitmapDisplayer width: [%d] height: [%d]", bitmap.getWidth(),bitmap.getHeight());
		}
		
		imageView.setImageBitmap(bitmap);
		return bitmap;
	}
}