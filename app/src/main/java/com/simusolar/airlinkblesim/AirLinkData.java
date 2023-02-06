package com.simusolar.airlinkblesim;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.BatteryManager;

import com.upokecenter.cbor.CBORObject;

import java.util.Random;

public class AirLinkData {

    private static CBORObject advertData;
    private static CBORObject pcMap;
    private static CBORObject battMap;
    private static CBORObject tempMap;
    private static CBORObject ccfgMap;
    private static CBORObject dcfgMap;
    private static CBORObject dfuMap;

    public CBORObject getAdvertData() {
        return advertData;
    }

    public CBORObject getPcMap() {
        return pcMap;
    }

    public CBORObject getBattMap() {
        return battMap;
    }

    public CBORObject getTempMap() {
        return tempMap;
    }

    public CBORObject getCcfgMap() {
        return ccfgMap;
    }

    public CBORObject getDcfgMap() {
        return dcfgMap;
    }

    public CBORObject getDfuMap() {
        return dfuMap;
    }

    //public AirLinkData() {
    //    setData();
    //}

    public static void setData(Context context, SharedPreferences prefs) {
        System.out.println("setData() called");

        //get battery percentage
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, ifilter);

        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        float batteryPct = level * 100 / (float)scale;

        //get battery health
        int batteryHealth = batteryStatus.getIntExtra(BatteryManager.EXTRA_HEALTH, -1);
        int chargingStatus = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            chargingStatus = batteryStatus.getIntExtra(String.valueOf(BatteryManager.BATTERY_PROPERTY_STATUS), -1);
        }

        //generate random number
        Random rand = new Random();
        int did = rand.nextInt(9999) + 1000;

        pcMap = CBORObject.NewMap()
                .Add("rtr", 65001)
                .Add("rv", 1)
                .Add("re", prefs.getInt("glb_re", 0))
                .Add("mo", 0)
                .Add("lcr", 7200)
                .Add("pts", 1630585953)
                .Add("lts", 1630585953)
                .Add("thi", 0)
                .Add("tkn", prefs.getString("pc_tkn", "*12345666#"))
                .Add("ts", 1670585953);

        battMap = CBORObject.NewMap()
                .Add("rtr", 65009)
                .Add("vb", 0)
                .Add("cp", batteryPct)
                .Add("cs", chargingStatus)
                .Add("th", 10)
                .Add("lb", 0)
                .Add("cmin", 0)
                .Add("cmax", 0)
                .Add("tc", 0)
                .Add("qc", 0)
                .Add("bh", batteryHealth)
                .Add("thi", 0)
                .Add("ts", 1670585241);

        tempMap = CBORObject.NewMap()
                .Add("rtr", 65011)
                .Add("temp", 1)
                .Add("tmax", 0)
                .Add("tlim", 0)
                .Add("thi", 0)
                .Add("ts", 1670585953);

        dcfgMap = CBORObject.NewMap()
                .Add("rtr", 65003)
                .Add("rv", 1)
                .Add("did", prefs.getInt("glb_did", did))
                .Add("pul", "s")
                .Add("tdf", 0)
                .Add("pst", prefs.getInt("glb_pst", 1))
                .Add("df", 0)
                .Add("cut", prefs.getString("dcfg_cut", ""))
                .Add("opmax", 1000);

        ccfgMap = CBORObject.NewMap()
                .Add("rtr", 65002)
                .Add("rv", 1)
                .Add("cn", "John Doe")
                .Add("cp", "255-555-5555")
                .Add("rid", 0)
                .Add("pst", prefs.getInt("glb_pst", 1));

        dfuMap = CBORObject.NewMap()
                .Add("rtr", 65004)
                .Add("rv", 1)
                .Add("fv", 257)
                .Add("fs", 100000)
                .Add("hv", 1);

        advertData = CBORObject.NewArray()
                .Add(prefs.getInt("adv_rv", 0))
                .Add(prefs.getInt("adv_ft", 0))
                .Add(prefs.getInt("glb_did", did))
                .Add(prefs.getInt("adv_gts", 1630585957))
                .Add(prefs.getInt("glb_pst", 1))
                .Add(prefs.getInt("adv_fv", 0))
                .Add(prefs.getInt("adv_cr", 7050))
                .Add(prefs.getString("adv_pu", "m"));

    }


}
