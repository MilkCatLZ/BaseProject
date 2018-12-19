/*******************************************************************************
 * Copyright (c) 2015 $user, tcloudit.com
 * <p/>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software !!within tcloudit.com!! , including the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, subject to the following conditions:
 * <p/>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p/>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/

package com.base.databinding;


import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;

import static android.view.LayoutInflater.from;


/**
 * {@link RecyclerView.ViewHolder ViewHolder}
 * using {@link ViewDataBinding} tech.
 * Created by yk on 15/11/16.
 */
public class ItemViewHolder<Binding extends ViewDataBinding> extends UltimateRecyclerviewViewHolder {

//    private boolean noBinding = false;

    /**
     * @param parent {@link RecyclerView}
     * @param viewID item view layout res
     */
    public ItemViewHolder(@NonNull ViewGroup parent, @LayoutRes int viewID) {
        this(DataBindingUtil.<Binding>inflate(from(parent.getContext()), viewID, parent, false));
    }

    public ItemViewHolder(@NonNull View rootView) {
        super(rootView);
        binding = (Binding)rootView.getTag(R.id.databinding_binding);
//        noBinding = true;
    }

    private ItemViewHolder(@NonNull Binding binding) {
        super(binding.getRoot());
        this.binding = binding;
        binding.getRoot().setTag(this);
    }

    /**
     * @param variableID used with {@link ViewDataBinding#setVariable(int, Object) setVariable}
     * @param item       from adapter to {@link ViewDataBinding#setVariable(int, Object) setVariable}
     */
    public void onBind(int variableID, Object item) {
        if (binding != null) {
            binding.setVariable(variableID, item);
            binding.executePendingBindings();
        }
    }

    /**
     *
     */
    public final Binding binding;

    @NonNull
    public final Observable.OnPropertyChangedCallback onPropertyChanged
        = new Observable.OnPropertyChangedCallback() {
        @Override
        public void onPropertyChanged(@NonNull Observable sender, int propertyId) {
            ItemViewHolder.this.onPropertyChanged(sender, propertyId);
        }
    };

    public void onPropertyChanged(@NonNull Observable sender, int propertyId) {

    }

    public void onRecycled() {

    }
}
