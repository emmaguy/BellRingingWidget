package dev.emmaguy.bell;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

public class BellWidgetProvider extends AppWidgetProvider
{
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
    {   	
        for(int appWidgetId : appWidgetIds)
        {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.view);

            Intent clickIntent = new Intent(context, BellWidgetProvider.class);
            clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

            // use appWidgetId as unique id to create broadcast as android reuses Intents
            // and otherwise all instances would be triggered
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, appWidgetId, clickIntent, 0);
            views.setOnClickPendingIntent(R.id.linearLayout, pendingIntent);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        if (intent.getAction() == null)
        {
            Bundle extras = intent.getExtras();
            if(extras != null)
            {
                int widgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
                RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.view);

                try
                {
                    MediaPlayer mPlay = MediaPlayer.create(context, R.raw.bellringing);
                    mPlay.setLooping(true);
                    mPlay.start();

                    updateBellImage(context, widgetId, remoteViews, R.drawable.bell2);
                    updateBellImage(context, widgetId, remoteViews, R.drawable.bell3);
                    updateBellImage(context, widgetId, remoteViews, R.drawable.bell4);
                    updateBellImage(context, widgetId, remoteViews, R.drawable.bell5);
                    updateBellImage(context, widgetId, remoteViews, R.drawable.bell6);
                    updateBellImage(context, widgetId, remoteViews, R.drawable.bell7);
                    updateBellImage(context, widgetId, remoteViews, R.drawable.bell8);
                    updateBellImage(context, widgetId, remoteViews, R.drawable.bell1);

                    mPlay.stop();
                    mPlay.release();
                }
                catch (Exception e)
                {
                    Log.e("BellWidgetProvider::onReceive error message", e.toString());
                    String str = "";
                    for(StackTraceElement msg : e.getStackTrace())
                    {
                        str += msg.toString() + "\n";
                    }
                    Log.e("BellWidgetProvider::onReceive stack trace", str);
                }
            }
        }
        else
        {
            super.onReceive(context, intent);
        }
    }

    private void updateBellImage(Context context, int widgetId, RemoteViews remoteViews, int bellId) throws InterruptedException
    {
        remoteViews.setImageViewResource(R.id.imageView, bellId);
        AppWidgetManager.getInstance(context).updateAppWidget(widgetId,  remoteViews);
        Thread.sleep(200);
    }
}