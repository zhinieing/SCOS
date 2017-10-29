package es.source.code.br;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import es.source.code.service.UpdateService;

public class DeviceStartedListener extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent updateService = new Intent(context, UpdateService.class);

        context.startService(updateService);
    }
}
