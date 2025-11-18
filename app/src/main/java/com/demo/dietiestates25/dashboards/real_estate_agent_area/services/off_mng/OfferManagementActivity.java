package com.demo.dietiestates25.dashboards.real_estate_agent_area.services.off_mng;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.dietiestates25.R;
import com.demo.dietiestates25.dashboards.real_estate_agent_area.services.off_mng.real_estate_offers.OfferAdapter;
import com.demo.dietiestates25.dashboards.real_estate_agent_area.services.off_mng.real_estate_offers.OfferData;
import com.demo.dietiestates25.dashboards.real_estate_agent_area.services.off_mng.real_estate_offers.OfferListSetup;
import com.demo.dietiestates25.databinding.ActivityOfferManagementBinding;
import com.demo.dietiestates25.support_classes.dialog_utils.DialogManager;
import com.demo.dietiestates25.utils.FeedbackUtils;
import com.demo.dietiestates25.utils.ResourceUtils;
import com.demo.dietiestates25.utils.ThemeUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Activity che mostra la lista di offerte ricevute per un immobile e permette all'Agente Immobiliare di gestirle (accettare, rifiutare, controproposta).
 */
public class OfferManagementActivity extends AppCompatActivity {

    private DialogManager dialogManager;
    private RecyclerView recyclerView;
    private ImageButton acceptOffer;
    private ImageButton rejectOffer;
    private ImageButton counteroffer;
    private final List<OfferData> offersList = new ArrayList<>();
    private int currentlySelectionIndex = RecyclerView.NO_POSITION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityOfferManagementBinding binding = ActivityOfferManagementBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.Close);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.manage_offers);

        dialogManager = new DialogManager(this);

        acceptOffer = findViewById(R.id.acceptOff);
        rejectOffer = findViewById(R.id.rejectOff);
        counteroffer = findViewById(R.id.counterOff);

        FeedbackUtils.showToastHelpMsg(this, acceptOffer);
        FeedbackUtils.showToastHelpMsg(this, rejectOffer);
        FeedbackUtils.showToastHelpMsg(this, counteroffer);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        OfferListSetup.manageOffers(this, recyclerView, offersList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_info_btn) {
            String infoMsg = ResourceUtils.getXMLString(this, R.string.auto_delete_offers_info_msg);
            dialogManager.showInfoDialog(R.string.auto_delete_offers_title, infoMsg);
        }

        return super.onOptionsItemSelected(item);
    }

    public void defaultStateImgButtons() {
        acceptOffer.setEnabled(false);
        rejectOffer.setEnabled(false);
        counteroffer.setEnabled(false);
    }

    public void enableImgButtons(int position) {
        currentlySelectionIndex = position;

        acceptOffer.setEnabled(true);
        rejectOffer.setEnabled(true);
        counteroffer.setEnabled(true);

        longClick(acceptOffer, R.string.accepted);
        longClick(rejectOffer, R.string.rejected);
        longClick(counteroffer, R.string.counteroffer);
    }

    public int buttonTxtVColorChanger() {
        if(acceptOffer.isPressed()) {
            return Color.GREEN;
        }
        if(rejectOffer.isPressed()) {
            return Color.RED;
        }
        if(counteroffer.isPressed()) {
            return Color.CYAN;
        }
        return ThemeUtils.defaultTxtVColor();
    }

    private void longClick(ImageButton imgBtn, int contentDescId) {
        String actionText = ResourceUtils.getXMLString(this, contentDescId);
        imgBtn.setOnClickListener(view -> updateSelectionCardTxt(actionText));
    }

    private void updateSelectionCardTxt(String actionTxt) {
        if(currentlySelectionIndex != RecyclerView.NO_POSITION) {
            OfferAdapter adapter = (OfferAdapter) recyclerView.getAdapter();

            if(adapter != null) {
                recyclerView.post(() -> adapter.updateOfferStatus(currentlySelectionIndex, actionTxt));
            }
        }
    }
}