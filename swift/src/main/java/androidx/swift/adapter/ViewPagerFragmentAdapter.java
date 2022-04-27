/*
 * Copyright 2022 RAE
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
 */

package androidx.swift.adapter;

import android.view.ViewGroup;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
public class ViewPagerFragmentAdapter extends FragmentPagerAdapter {
    private final List<String> mTitle = new ArrayList<>();
    private final List<Integer> mIcons = new ArrayList<>();
    private final List<Fragment> mFragments = new ArrayList<>();
    private final FragmentManager mFragmentManager;
    private int mContainerId = -1;

    public ViewPagerFragmentAdapter(@NonNull FragmentManager fm) {
        super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mFragmentManager = fm;
    }

    public void addFragment(String name, Fragment fragment) {
        mTitle.add(name);
        mFragments.add(fragment);
    }

    public void addFragment(@DrawableRes int resId, String name, Fragment fragment) {
        mIcons.add(resId);
        mTitle.add(name);
        mFragments.add(fragment);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return mFragments.indexOf((Fragment) object);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitle.get(position);
    }

    public int getIconId(int position) {
        return mIcons.get(position);
    }

    public List<Fragment> getFragments() {
        return mFragments;
    }

    public int indexOf(Fragment fragment) {
        return mFragments.indexOf(fragment);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        mContainerId = container.getId();
        return super.instantiateItem(container, position);
    }

    public Fragment getFragment(int position) {
        String tag = "android:switcher:" + mContainerId + ":" + getItemId(position);
        return mFragmentManager.findFragmentByTag(tag);
    }

    public void clear() {
        mFragments.clear();
        mTitle.clear();
    }

    /**
     * 释放所有Fragment
     */
    public void destroy() {
        // 重新创建Fragment
        FragmentManager fragmentManager = mFragmentManager;
        // 重新加载 Fragment
        List<Fragment> fragments = fragmentManager.getFragments();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        for (Fragment fragment : fragments) {
            if (fragment.getTag() != null && fragment.getTag().startsWith("android:switcher")) {
                transaction.remove(fragment);
            }
        }
        transaction.commitNowAllowingStateLoss();
        this.clear();
        this.notifyDataSetChanged();
    }
}
