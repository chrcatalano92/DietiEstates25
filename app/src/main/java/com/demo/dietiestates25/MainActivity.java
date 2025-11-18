package com.demo.dietiestates25;

import static com.demo.dietiestates25.utils.LocaleUtils.*;
import static com.demo.dietiestates25.utils.FragmentUtils.replaceFragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.preference.PreferenceManager;

import com.demo.dietiestates25.fragments_main_activity.HomeFragment;
import com.demo.dietiestates25.fragments_main_activity.MenuFragment;
import com.demo.dietiestates25.fragments_main_activity.ReservedAreaFragment;
import com.demo.dietiestates25.fragments_main_activity.SettingsFragment;

import com.demo.dietiestates25.support_classes.ThemeHelper;

import com.demo.dietiestates25.databinding.ActivityMainBinding;
import com.demo.dietiestates25.support_classes.dialog_utils.DialogManager;
import com.demo.dietiestates25.support_classes.retrofit_utility.IpAddressReader;
import com.demo.dietiestates25.support_classes.retrofit_utility.NetworkConfigUpdater;
import com.google.android.material.navigation.NavigationView;

/**
 * L'Activity principale dell'Applicazione. Funge da Contenitore per i Fragment
 * e gestisce la Navigazione principale (Drawer e Bottom Navigation.)
 */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private ActivityMainBinding binding;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        addAllowedDomains();

        applySavedTheme(this);

        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // La logica di setup della UI è ora in un metodo dedicato
        setupNavigation();

        // Carica il Fragment iniziale solo al primo avvio
        if(savedInstanceState == null) {
            replaceFragment(getSupportFragmentManager(), new HomeFragment());
            binding.bottomNavView.setSelectedItemId(R.id.home); // Imposta l'icona Home come attiva
        }

        binding.bottomNavView.setOnItemSelectedListener(item -> {
            if(getItemId(item) == R.id.profile) {
                replaceFragment(getSupportFragmentManager(), new MenuFragment());
            }
            else if(getItemId(item) == R.id.settings) {
                replaceFragment(getSupportFragmentManager(), new SettingsFragment());
            }
            else {
                replaceFragment(getSupportFragmentManager(), new HomeFragment());
            }
            return true;
        });
    }

    /**
     * Configura la Toolbar, il drawerLayout e la BottomNavigationView.
     * Questo metodo centralizza La Logica di Setup della navigazione.
     */
    private void setupNavigation() {
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        binding.navView.setNavigationItemSelectedListener(this);

        toggle = new ActionBarDrawerToggle(this, binding.drawerLayout, R.string.nav_open, R.string.nav_close);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        binding.bottomNavView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.profile) {
                replaceFragment(getSupportFragmentManager(), new MenuFragment());
            } else if (itemId == R.id.settings) {
                replaceFragment(getSupportFragmentManager(), new SettingsFragment());
            } else {
                replaceFragment(getSupportFragmentManager(), new HomeFragment());
            }
            return true;
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.area_riservata) {
            replaceFragment(getSupportFragmentManager(), new ReservedAreaFragment());
        } else if (itemId == R.id.contattaci) {
            new DialogManager(this).showContactUsDialog();
        }

        // Chiude il Drawer dopo la selezione
        binding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Gestisce il Click sull'icona del Menu (Hamburger) nella Toolbar.
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        String currLanguage = getCurrentLanguage(newBase);
        Configuration conf = getConfiguration(newBase);
        updateResources(currLanguage, conf);
        Context newContext = newBase.createConfigurationContext(conf);

        super.attachBaseContext(newContext);
    }

    private void addAllowedDomains() {
        String newIp = IpAddressReader.readIpAddressFromFile(this);
        int port = 8080;
        String domain = newIp + ":" + port;

        NetworkConfigUpdater.addAllowedDomain(domain);
    }

    private void applySavedTheme(Context context) {
        SharedPreferences sharePref = PreferenceManager.getDefaultSharedPreferences(context);
        String theme = sharePref.getString(context.getString(R.string.settings_theme_key), "system");
        ThemeHelper.applyTheme(theme, context);
    }

    private static int getItemId(MenuItem item) {
        return item.getItemId();
    }


}