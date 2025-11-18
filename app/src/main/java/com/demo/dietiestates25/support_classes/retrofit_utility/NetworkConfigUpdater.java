package com.demo.dietiestates25.support_classes.retrofit_utility;

import java.util.HashSet;
import java.util.Set;

public class NetworkConfigUpdater {
    private NetworkConfigUpdater(){}

    private static final Set<String> allowedDomains = new HashSet<>();

    public static boolean isDomainAllowed(String domain) { return allowedDomains.contains(domain); }

    public static void addAllowedDomain(String domain) { allowedDomains.add(domain); }
}
