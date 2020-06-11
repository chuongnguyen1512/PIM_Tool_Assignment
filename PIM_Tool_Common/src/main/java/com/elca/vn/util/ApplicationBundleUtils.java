package com.elca.vn.util;

import org.jacpfx.rcp.context.Context;
import org.jacpfx.rcp.context.JacpContextImpl;

import java.util.Objects;
import java.util.ResourceBundle;

public class ApplicationBundleUtils {

    private ApplicationBundleUtils() {
    }

    public static void reloadBundle(Context context, ResourceBundle oldBundle, ResourceBundle newBundle) {
        if (Objects.isNull(context) || Objects.isNull(oldBundle) || Objects.isNull(newBundle)) {
            return;
        }
        JacpContextImpl newContext = (JacpContextImpl) context;
        newContext.setResourceBundle(newBundle);
        oldBundle = newBundle;
    }

    public static void reloadBundle(Context context, ResourceBundle newBundle) {
        if (Objects.isNull(context) || Objects.isNull(newBundle)) {
            return;
        }
        JacpContextImpl newContext = (JacpContextImpl) context;
        newContext.setResourceBundle(newBundle);
    }
}
