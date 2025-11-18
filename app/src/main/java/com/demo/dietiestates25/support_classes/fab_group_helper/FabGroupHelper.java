package com.demo.dietiestates25.support_classes.fab_group_helper;

import android.view.View;

import androidx.annotation.NonNull;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class FabGroupHelper {

    public interface OnFabGroupStateChangeListener {
        void onGroupExpanded(FabGroup group);

        void onGroupClosed(FabGroup group);
    }

    public static class FabGroup {
        private final ExtendedFloatingActionButton mainFab;
        private final List<ExtendedFloatingActionButton> secondaryFabs;
        private boolean isExpanded = false;
        private OnFabGroupStateChangeListener listener;

        public FabGroup(@NonNull ExtendedFloatingActionButton mainFab, @NonNull List<ExtendedFloatingActionButton> secondaryFabs) {
            this.mainFab = mainFab;
            this.secondaryFabs = new ArrayList<>(secondaryFabs);

            setupMainFabClickListener();
            hideSecondaryFabs();
        }

        private void hideSecondaryFabs() {
            for (ExtendedFloatingActionButton fab : secondaryFabs) {
                fab.setVisibility(View.INVISIBLE);
                fab.setScaleX(0);
                fab.setScaleY(0);
            }
        }

        public ExtendedFloatingActionButton getMainFab() {
            return mainFab;
        }

        private void setupMainFabClickListener() {
            mainFab.setOnClickListener(view -> toggle());
        }

        private void toggle() {
            if (isExpanded) {
                close();
            } else {
                expand();
            }
        }

        private void close() {
            isExpanded = false;
            animateSecondaryFabs(false);

            if (listener != null) {
                listener.onGroupExpanded(this);
            }
        }

        private void expand() {
            isExpanded = true;
            animateSecondaryFabs(true);

            if (listener != null) {
                listener.onGroupExpanded(this);
            }
        }

        public boolean isExpanded() {
            return isExpanded;
        }

        private void animateSecondaryFabs(boolean show) {
            for (int i = 0; i < secondaryFabs.size(); i++) {
                ExtendedFloatingActionButton fab = secondaryFabs.get(i);

                if (show) {
                    fab.setVisibility(View.VISIBLE);
                    fab.setScaleX(0);
                    fab.setScaleY(0);
                    fab.animate()
                            .scaleX(1)
                            .scaleY(1)
                            .setDuration(300)
                            .setStartDelay(i * 100L)
                            .start();
                } else {
                    fab.animate()
                            .scaleX(0)
                            .scaleY(0)
                            .setDuration(300)
                            .setStartDelay((secondaryFabs.size() - 1 - i) * 100L)
                            .withEndAction(() -> fab.setVisibility(View.INVISIBLE))
                            .start();
                }
            }
        }

        public void setOnFabGroupStateChangeListener(OnFabGroupStateChangeListener listener) {
            this.listener = listener;
        }
    }

    private final List<FabGroup> fabGroups = new ArrayList<>();

    public FabGroup createGroup(@NonNull ExtendedFloatingActionButton mainFab, @NonNull List<ExtendedFloatingActionButton> secondaryFabs) {
        for (ExtendedFloatingActionButton fab : secondaryFabs) {
            fab.setVisibility(View.INVISIBLE);
            fab.setScaleX(0);
            fab.setScaleY(0);
        }

        FabGroup newGroup = new FabGroup(mainFab, secondaryFabs);
        newGroup.setOnFabGroupStateChangeListener(new OnFabGroupStateChangeListener() {
            @Override
            public void onGroupExpanded(FabGroup group) {
                for (FabGroup otherGroup : fabGroups) {
                    if (otherGroup != group && otherGroup.isExpanded()) {
                        otherGroup.close();
                    }
                }
            }

            @Override
            public void onGroupClosed(FabGroup group) {
                // Not used
            }
        });

        fabGroups.add(newGroup);

        return newGroup;
    }

    public void closeAllGroups() {
        for(FabGroup group : fabGroups) {
            group.close();
        }
    }
}
