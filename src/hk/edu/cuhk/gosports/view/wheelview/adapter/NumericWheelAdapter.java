/*
 *  Copyright 2011 Yuri Kanivets
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package hk.edu.cuhk.gosports.view.wheelview.adapter;

import android.content.Context;

/**
 * Numeric Wheel adapter.
 */
public class NumericWheelAdapter extends AbstractWheelTextAdapter {
    
    /** The default min value */
    public static final int DEFAULT_MAX_VALUE = 9;

    /** The default max value */
    private static final int DEFAULT_MIN_VALUE = 0;
    
    // Values
    private int minValue;
    private int maxValue;
    
    // format
    private String format;
    
    private int type;
    
    /**
     * Constructor
     * @param context the current context
     */
    public NumericWheelAdapter(Context context) {
        this(context, DEFAULT_MIN_VALUE, DEFAULT_MAX_VALUE);
    }

    /**
     * Constructor
     * @param context the current context
     * @param minValue the wheel min value
     * @param maxValue the wheel max value
     */
    public NumericWheelAdapter(Context context, int minValue, int maxValue) {
        this(context, minValue, maxValue, null);
    }

    /**
     * Constructor
     * @param context the current context
     * @param minValue the wheel min value
     * @param maxValue the wheel max value
     * @param format the format string
     */
    public NumericWheelAdapter(Context context, int minValue, int maxValue, String format) {
        super(context);
        
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.format = format;
    }
    /**
     * 
     * @param context
     * @param minValue
     * @param maxValue
     * @param unitType  1：时    2：分  3:小时
     */
    public NumericWheelAdapter(Context context, int minValue, int maxValue,String format,int unitType) {
        this(context, minValue, maxValue,format);
        this.type=unitType;
    }

    @Override
    public CharSequence getItemText(int index) {
        if (index >= 0 && index < getItemsCount()) {
            int value = minValue + index;
            /*return format != null ? String.format(format, value) : type == 0 ? Integer
                    .toString(value) : type == 1 ? Integer.toString(value) + "时" : Integer
                    .toString(value) + "分";*/
            //FIXME 目前因为是新增了一个构造方法，因此只对新增的构造方法来特异化输出str
            return format!=null?type==1?String.format(format, value)+"时":String.format(format, value)+"分":type==3?Integer.toString(value)+"小时":Integer.toString(value);
        }
        return null;
    }

    @Override
    public int getItemsCount() {
        return maxValue - minValue + 1;
    }    
}
