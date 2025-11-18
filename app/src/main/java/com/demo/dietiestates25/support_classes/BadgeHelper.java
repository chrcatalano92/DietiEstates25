package com.demo.dietiestates25.support_classes;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.demo.dietiestates25.R;

/**
 * Classe Helper per aggiungere un badge al CardView.
 * ALTA COESIONE (SRP): La sua Sola Responsabilità è aggiungere, aggiornare e rimuovere un badge numerico (come le notifiche) su una CardView.
 * BASSO ACCOPPIAMENTO: Dipende solo dal Context (per creare le View) e dalla CardView su cui deve operare. Non sa nulla di ViewModel o logica di business.
 * CLEAN CODE e DRY: I metodi 'dpToPx', 'findExistingBadge', 'createBadgeView' sono helper privati puliti che evitano duplicazioni.
 */
public class BadgeHelper {
    private final Context context;
    private static final String BADGE_TAG = "custom_badge_tag";
    private static final int BADGE_TXT_SIZE_SP = 12;
    private static final int BADGE_SIZE_DP = 34;
    private static final int BADGE_MARGIN_DP = 8;
    private static final int BADGE_TXT_COLOR = Color.WHITE;

    public BadgeHelper(@NonNull Context context) {
        this.context = context;
    }

    public void addCustomBadgeToCardView(@NonNull CardView cardView, int count) {
        removeExistingBadge(cardView);

        if(count > 0) {
            TextView badgeView = createBadgeView(count);

            ViewGroup.LayoutParams params = createBadgeLayoutParams();

            setupCardViewForBadge(cardView);

            cardView.addView(badgeView, params);
        }
    }

    @NonNull
    private TextView createBadgeView(int count) {
        TextView badgeView = new TextView(context);
        badgeView.setTag(BADGE_TAG);

        updateBadgeTxt(badgeView, count);
        badgeView.setBackgroundResource(R.drawable.badge_circle);
        badgeView.setTextColor(BADGE_TXT_COLOR);
        badgeView.setTextSize(TypedValue.COMPLEX_UNIT_SP, BADGE_TXT_SIZE_SP);
        badgeView.setGravity(Gravity.CENTER);

        return badgeView;
    }

    @NonNull
    private FrameLayout.LayoutParams createBadgeLayoutParams() {
        int size = dpToPx(BADGE_SIZE_DP);
        int margin = dpToPx(BADGE_MARGIN_DP);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                size,
                size,
                Gravity.TOP | Gravity.END
        );

        params.setMargins(0, margin, margin, 0);

        return params;
    }

    private void setupCardViewForBadge(CardView cardView) {
        cardView.setClipChildren(false);
        cardView.setClipToPadding(false);
    }

    public void updateCustomBadge(@NonNull CardView cardView, int count) {
        if(shouldRemoveBadge(count)) {
            removeExistingBadge(cardView);
        }
        else {
            updateOrCreateBadge(cardView, count);
        }
    }

    private boolean shouldRemoveBadge(int count) { return count <= 0; }

    private void updateOrCreateBadge(CardView cardView, int count) {
        TextView existingBadge = findExistingBadge(cardView);

        if(existingBadge != null) {
            updateBadgeTxt(existingBadge, count);
        }
        else {
            addCustomBadgeToCardView(cardView, count);
        }
    }

    private void removeExistingBadge(CardView cardView) {
        TextView badgeView = findExistingBadge(cardView);

        if(badgeView != null) { cardView.removeView(badgeView); }
    }

    private TextView findExistingBadge(CardView cardView) {
        View view = cardView.findViewWithTag(BADGE_TAG);

        return view instanceof TextView ? (TextView) view : null;
    }

    private void updateBadgeTxt(TextView badge, int count) {
        String badgeTxt;

        if(count > 100) { badgeTxt = "100+"; }
        else { badgeTxt = String.valueOf(count); }

        badge.setText(badgeTxt);
    }

    private int dpToPx(int dp) { return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics()); }
}
